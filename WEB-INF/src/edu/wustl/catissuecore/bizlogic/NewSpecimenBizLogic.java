/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	NewSpecimenBizLogicHDAO is used to add new specimen information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 *@version Suite V1.1
 * Code re factoring on 15th May 2008
 */
package edu.wustl.catissuecore.bizlogic;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.wustl.catissuecore.TaskTimeCalculater;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.MultipleSpecimenValidationUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.WithdrawConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenHDAO is used to add new specimen information into the database using hibernate
 */
public class NewSpecimenBizLogic extends DefaultBizLogic
{
	private Map<Long, Collection<String>> containerHoldsSpecimenClasses = new HashMap<Long, Collection<String>>();
	private Map<Long, Collection<CollectionProtocol>> containerHoldsCPs = new HashMap<Long, Collection<CollectionProtocol>>();
	private HashSet<String> storageContainerIds = new HashSet<String>();
	private SecurityManager securityManager = new SecurityManager(this.getClass());
	private boolean cpbased = false;
	/**
	 * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param sessionDataBean The session in which the object is saved.
	 * @param dao DAO object
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		if (!isCpbased())
		{
			isAuthorise(sessionDataBean.getUserName());
		}
		storageContainerIds= new HashSet<String>();
		Collection<Specimen> insertableSpecimens;
		try
		{
			insertableSpecimens = getInsertableSpecimens(obj, dao, sessionDataBean);
			insertSpecimens(dao,sessionDataBean,insertableSpecimens);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
		finally
		{
			storageContainerIds.clear();
		}
	}
	/**
	 * @param dao DAO Object
	 * @param sessionDataBean session details
	 * @param insertableSpecimens Specimens to be insert
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void insertSpecimens(DAO dao,SessionDataBean sessionDataBean,
			Collection<Specimen> insertableSpecimens) throws DAOException,
			UserNotAuthorizedException
	{
		Iterator<Specimen> insertSpecIterator = insertableSpecimens.iterator();
		while (insertSpecIterator.hasNext())
		{
			Specimen specimen = (Specimen)insertSpecIterator.next();
			if (specimen.getSpecimenCharacteristics()!= null)
			{
				dao.insert(specimen.getSpecimenCharacteristics(), sessionDataBean, false, false);
			}
			dao.insert(specimen, sessionDataBean, false, false);
			if(specimen.getChildrenSpecimen()!= null)
			{
				insertSpecimens(dao, sessionDataBean, specimen.getChildrenSpecimen());
			}
		}
	}
	/**
	 * @param obj Type of object i.e. LinkedHashSet or Abstract Domain object
	 * @param dao DAO object
	 * @param sessionDataBean session details
	 * @return insertableSpecimens returns specimens after insert
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private Collection<Specimen> getInsertableSpecimens(Object obj, DAO dao,SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		Collection<Specimen> insertableSpecimens;
		if (obj.getClass().hashCode() == LinkedHashSet.class.hashCode())
		{
			insertableSpecimens = insertMultipleSpecimen((LinkedHashSet) obj, dao, sessionDataBean);
		}
		else if (obj instanceof Specimen)
		{

			Specimen insertableSpecimen = insertSingleSpecimen((Specimen) obj, dao, sessionDataBean, false,Constants.FIRST_COUNT_1);
			insertableSpecimens = new LinkedHashSet<Specimen>();
			insertableSpecimens.add(insertableSpecimen);
		}
		else
		{
			throw new DAOException("Object should be either specimen or LinkedHashMap " + "of specimen objects.");
		}
		return insertableSpecimens;
	}
	/**
	 * @param userName Logged in User
	 * @throws UserNotAuthorizedException User is not authorized
	 */
	private void isAuthorise(String userName) throws UserNotAuthorizedException
	{
		// To get privilegeCache through
		// Singleton instance of PrivilegeManager, requires User LoginName
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);
		try
		{
			// Call to SecurityManager.isAuthorized bypassed &
			// instead, call redirected to privilegeCache.hasPrivilege
			if (!privilegeCache.hasPrivilege(Specimen.class, Permissions.CREATE))
			{
				throw new UserNotAuthorizedException("User is not authorised to create specimens");
			}
		}
		catch (SMException exception)
		{
			throw new UserNotAuthorizedException(exception.getMessage(), exception);
		}
	}
	/**
	 * Insert multiple specimen into the data base.
	 * @param dao DAO object
	 * @param sessionDataBean Session Details
	 * @param specimenCollection Collection of specimen
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 * @return insertSpecimenCollection Specimen collection after insert
	 */
	private Collection<Specimen> insertMultipleSpecimen(LinkedHashSet<Specimen> specimenCollection, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Iterator<Specimen> specimenIterator = specimenCollection.iterator();
		int count = 0;
		double dQuantity = 0;
		boolean disposeSpecimen = false;
		Long parentSpecimenId = null;
		Collection<Specimen> insertSpecimenCollection = new LinkedHashSet<Specimen>();
		try
		{
			while (specimenIterator.hasNext())
			{
				TaskTimeCalculater.startTask("Multiple specimen ", NewSpecimenBizLogic.class);
				count++;
				Specimen specimen = (Specimen) specimenIterator.next();
				Specimen insertSpecimen = insertSingleSpecimen(specimen, dao, sessionDataBean, true, count);
				dQuantity = Double.parseDouble(specimen.getAvailableQuantity().toString());
				disposeSpecimen = specimen.getDisposeParentSpecimen();
				if(specimen.getParentSpecimen()!=null)
				{
					parentSpecimenId = specimen.getParentSpecimen().getId();
				}
				if(specimen.getLineage().equals(Constants.ALIQUOT))
				{
					calculateQuantity(dQuantity, insertSpecimen);
				}
				insertSpecimenCollection.add(insertSpecimen);
			}
			if(disposeSpecimen)
			{
				Specimen parentSpecimen = new Specimen();
				parentSpecimen.setId(parentSpecimenId);
				disposeSpecimen(dao, sessionDataBean, parentSpecimen);
			}
			return insertSpecimenCollection;
		}
		catch (DAOException daoException)
		{
			String message = " (This message is for Specimen number " + count + ")";
			daoException.setSupportingMessage(message);
			throw daoException;
		}
	}
	/**
	 * @param dQuantity quantity of Specimen
	 * @param insertSpecimen Specimen object to insert
	 */
	private void calculateQuantity(double dQuantity, Specimen insertSpecimen)
	{
		double availableQuantity = insertSpecimen.getParentSpecimen().getAvailableQuantity().getValue().doubleValue();
		DecimalFormat dFormat = new DecimalFormat("#.000");
		availableQuantity = availableQuantity - dQuantity;
		availableQuantity = Double.parseDouble(dFormat.format(availableQuantity));
		insertSpecimen.getParentSpecimen().setAvailableQuantity(new Quantity(String.valueOf(availableQuantity)));
		if (availableQuantity <= 0)
		{
			insertSpecimen.getParentSpecimen().setAvailable(new Boolean(false));
			insertSpecimen.getParentSpecimen().setAvailableQuantity(new Quantity("0"));
		}
	}
	/**
	 * @param specimen Parent Specimen
	 * @throws DAOException Database related Exception
	 * This method retrieves the parent specimen events and sets them in the parent specimen
	 */
	private void setParentSpecimenData(Specimen specimen) throws DAOException
	{
		Specimen parent = specimen.getParentSpecimen();
		specimen.setPathologicalStatus(specimen.getParentSpecimen().getPathologicalStatus());
		setParentCharacteristics(parent, specimen);
		setParentConsents(parent, specimen);
		if (parent != null)
		{
			Set<Biohazard> set = new HashSet<Biohazard>();
			Collection<Biohazard> biohazardCollection = parent.getBiohazardCollection();
			if (biohazardCollection != null)
			{
				Iterator<Biohazard> it = biohazardCollection.iterator();
				while (it.hasNext())
				{
					Biohazard hazard = (Biohazard) it.next();
					set.add(hazard);
				}
			}
			specimen.setBiohazardCollection(set);
		}
	}
	/**
	 * @param parentSpecimen Parent Specimen object
	 * @param childSpecimen  Child Specimen Object
	 */
	private void setParentConsents(Specimen parentSpecimen,Specimen childSpecimen)
	{
		if(parentSpecimen.getConsentTierStatusCollection()!=null && childSpecimen.getConsentTierStatusCollection()==null)
		{
				Set<ConsentTierStatus> set = new HashSet<ConsentTierStatus>();
				Collection<ConsentTierStatus> consentTierStatusCollection = parentSpecimen.getConsentTierStatusCollection();
				if (consentTierStatusCollection != null)
				{
					Iterator<ConsentTierStatus> it = consentTierStatusCollection.iterator();
					while (it.hasNext())
					{
						ConsentTierStatus conentTierStatus = (ConsentTierStatus) it.next();
						ConsentTierStatus consentTierStatusForSpecimen = new ConsentTierStatus();
						consentTierStatusForSpecimen.setStatus(conentTierStatus.getStatus());
						consentTierStatusForSpecimen.setConsentTier(conentTierStatus.getConsentTier());
						set.add(consentTierStatusForSpecimen);
					}
				}
				childSpecimen.setConsentTierStatusCollection(set);
		}
	}
	/**
	 * @param specimen This method sets the created on date = collection date
	 **/
	private void setCreatedOnDate(Specimen specimen)
	{
		Collection<SpecimenEventParameters> specimenEventsCollection = specimen.getSpecimenEventCollection();
		if (specimenEventsCollection != null)
		{
			Iterator<SpecimenEventParameters> specimenEventsCollectionIterator = specimenEventsCollection.iterator();
			while (specimenEventsCollectionIterator.hasNext())
			{
				Object eventObject = specimenEventsCollectionIterator.next();
				if (eventObject instanceof CollectionEventParameters)
				{
					CollectionEventParameters collEventParam = (CollectionEventParameters) eventObject;
					specimen.setCreatedOn(collEventParam.getTimestamp());
				}
			}
		}
	}
	/**
	 * @param specimen Set default events to specimens
	 * @param sessionDataBean Session data bean
	 * This method sets the default events to specimens if they are null
	 */
	private void setDefaultEventsToSpecimen(Specimen specimen, SessionDataBean sessionDataBean)
	{
		Collection<SpecimenEventParameters> specimenEventColl = new HashSet<SpecimenEventParameters>();
		User user = new User();
		user.setId(sessionDataBean.getUserId());
		CollectionEventParameters collectionEventParameters = EventsUtil.populateCollectionEventParameters(user);
		collectionEventParameters.setSpecimen(specimen);
		specimenEventColl.add(collectionEventParameters);

		ReceivedEventParameters receivedEventParameters = EventsUtil.populateReceivedEventParameters(user);
		receivedEventParameters.setSpecimen(specimen);
		specimenEventColl.add(receivedEventParameters);

		specimen.setSpecimenEventCollection(specimenEventColl);
	}
	/**
	 * This method gives the error message.
	 * This method is override for customizing error message
	 * @param obj - Object
	 * @param operation Type of operation
	 * @param daoException Database related Exception
	 * @return formatedException returns formated exception
	 */
	public String getErrorMessage(DAOException daoException, Object obj, String operation)
	{
		if (obj instanceof HashMap)
		{
			obj = new Specimen();
		}
		String supportingMessage = daoException.getSupportingMessage();
		String formatedException = formatException(daoException.getWrapException(), obj, operation);
		if (supportingMessage != null && formatedException != null)
		{
			formatedException += supportingMessage;
		}
		if (formatedException == null)
		{
			formatedException = daoException.getMessage();
			if (supportingMessage != null)
			{
				formatedException += supportingMessage;
			}
		}
		return formatedException;
	}

	/**
	 * Insert single specimen into the data base.
	 * @param specimen Specimen Object
	 * @param dao DAO object
	 * @param sessionDataBean session details
	 * @param partOfMulipleSpecimen boolean True or False
	 * @param count For generating label
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 * @return specimen Specimen object after insert
	 */
	private Specimen insertSingleSpecimen(Specimen specimen, DAO dao, SessionDataBean sessionDataBean,
			boolean partOfMulipleSpecimen, int count) throws DAOException, UserNotAuthorizedException
	{
		try
		{
			ApiSearchUtil.setSpecimenDefault(specimen);
			if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
			{
				specimen.setLabel(specimen.getSpecimenCollectionGroup().getId()+"_"+count);
			}
			Specimen parentSpecimen = specimen.getParentSpecimen();
			if ((specimen.getParentSpecimen() == null))
			{
				setCreatedOnDate(specimen);
			}
			if (specimen.getParentSpecimen() != null && specimen.getParentSpecimen().getId() != null && specimen.getParentSpecimen().getId() > 0)
			{
				TaskTimeCalculater setParentData = TaskTimeCalculater.startTask("Set parent Data", NewSpecimenBizLogic.class);
				parentSpecimen = (Specimen) dao.retrieve( Specimen.class.getName(), specimen.getParentSpecimen().getId()  );
				specimen.setParentSpecimen(parentSpecimen);

				TaskTimeCalculater.endTask(setParentData);
			}
			setParentSCG(specimen, dao, parentSpecimen);
			if(specimen.getParentSpecimen()!=null)
			{
				setParentSpecimenData(specimen);
			}
			allocatePositionForSpecimen(specimen);
			setStorageLocationToNewSpecimen(dao, specimen, sessionDataBean, true);
			setSpecimenData(specimen, dao, sessionDataBean,partOfMulipleSpecimen);
			generateLabel(specimen);
			generateBarCode(specimen);
			return specimen;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}
	}
	/**
	 * @param dao DAO object
	 * @param sessionDataBean Session details
	 * @param parentSpecimen parent specimen object
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User is not Authorized
	 */
	private void disposeSpecimen(DAO dao, SessionDataBean sessionDataBean, Specimen parentSpecimen)
			throws DAOException, UserNotAuthorizedException
	{
		DisposalEventParameters disposalEvent = new DisposalEventParameters();
		disposalEvent.setSpecimen(parentSpecimen);
		disposalEvent.setReason("Specimen is Aliquoted");
		disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
		User user = new User();
		user.setId(sessionDataBean.getUserId());
		disposalEvent.setUser(user);
		disposalEvent.setActivityStatus(Constants.ACTIVITY_STATUS_CLOSED);
		SpecimenEventParametersBizLogic specimenEventParametersBizLogic = new SpecimenEventParametersBizLogic();
		specimenEventParametersBizLogic.insert(disposalEvent,dao,sessionDataBean);
		specimenEventParametersBizLogic.postInsert(disposalEvent,dao,sessionDataBean);
		parentSpecimen.setAvailable(new Boolean(false));
		parentSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_CLOSED);
	}
	/**
	 * @param specimen Specimen object
	 * @param dao DAO object
	 * @param sessionDataBean Session details
	 * @param partOfMulipleSpecimen Boolean true or false
	 * @throws SMException Security related exception
	 * @throws DAOException Database related Exception
	 */
	private void setSpecimenData(Specimen specimen, DAO dao,SessionDataBean sessionDataBean,
			boolean partOfMulipleSpecimen)throws SMException,DAOException
	{
		Collection<SpecimenEventParameters> specimenEventColl = specimen.getSpecimenEventCollection();
		Quantity avQty = specimen.getAvailableQuantity();
		if (sessionDataBean != null && (specimenEventColl == null || specimenEventColl.isEmpty()))
		{
			setDefaultEventsToSpecimen(specimen, sessionDataBean);
		}
		setExternalIdentifiers(specimen, specimen.getExternalIdentifierCollection());
		if (avQty != null && avQty.getValue().doubleValue() == 0)
		{
			if (Constants.COLLECTION_STATUS_COLLECTED.equals(specimen.getCollectionStatus()))
			{
				specimen.setAvailableQuantity(specimen.getInitialQuantity());
				specimen.setAvailable(new Boolean(true));
			}
		}
		if (specimen.getLineage() == null && specimen.getParentSpecimen()==null)
		{
			specimen.setLineage(Constants.NEW_SPECIMEN);
		}
		setSpecimenAttributes(dao, specimen, sessionDataBean, partOfMulipleSpecimen);
		if (specimen.getIsCollectionProtocolRequirement() ==null)
		{
			specimen.setIsCollectionProtocolRequirement(Boolean.FALSE);
		}
		insertChildSpecimens(specimen, dao, sessionDataBean,partOfMulipleSpecimen);
	}

	/**
	 * @param specimen Specimen Object
	 * @param dao DAO object
	 * @param sessionDataBean Session data
	 * @param partOfMulipleSpecimen boolean true or false
	 * @throws DAOException Database related exception
	 * @throws SMException Security related exception
	 */
	private void insertChildSpecimens(Specimen specimen, DAO dao,SessionDataBean sessionDataBean,
			boolean partOfMulipleSpecimen) throws SMException,DAOException
	{
		Collection<Specimen> childSpecimenCollection = specimen.getChildrenSpecimen();
		Iterator<Specimen> it = childSpecimenCollection.iterator();
		int ctr=Constants.FIRST_COUNT_1;
		while (it.hasNext())
		{
			Specimen childSpecimen = (Specimen) it.next();
			if(childSpecimen.getParentSpecimen() == null)
			{
				childSpecimen.setParentSpecimen(specimen);
			}
			if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
			{
				childSpecimen.setLabel(specimen.getLabel()+ "_"+ ctr++);
			}
			childSpecimen.setSpecimenCollectionGroup(specimen.getSpecimenCollectionGroup());
			setParentCharacteristics(specimen, childSpecimen);
			childSpecimen.setCreatedOn(specimen.getCreatedOn());
			setSpecimenData(childSpecimen, dao, sessionDataBean,partOfMulipleSpecimen);
		}
	}
	/**
	 * @param parentSpecimen Parent Specimen Object
	 * @param childSpecimen Child Specimen Object
	 */
	private void setParentCharacteristics(Specimen parentSpecimen,
			Specimen childSpecimen)
	{
		SpecimenCharacteristics characteristics  = null;
		if(Constants.ALIQUOT.equals(childSpecimen.getLineage()))
		{
			childSpecimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			return;
		}
		SpecimenCharacteristics parentSpecChar = parentSpecimen.getSpecimenCharacteristics();
		if (parentSpecChar != null)
		{
			characteristics = new SpecimenCharacteristics();
			characteristics.setTissueSide(parentSpecChar.getTissueSide());
			characteristics.setTissueSite(parentSpecChar.getTissueSite());
		}
		childSpecimen.setSpecimenCharacteristics(characteristics);
	}
	/**
	 * @param specimen Specimen Object
	 * @throws DAOException Database related exception
	 */
	private void generateBarCode(Specimen specimen) throws DAOException
	{
		if (edu.wustl.catissuecore.util.global.Variables.isSpecimenBarcodeGeneratorAvl)
		{
			if ((specimen.getBarcode() == null || specimen.getBarcode().equals("")) && !specimen.getIsCollectionProtocolRequirement())
			{
				try
				{
					BarcodeGenerator spBarcodeGenerator = BarcodeGeneratorFactory.getInstance(Constants.SPECIMEN_BARCODE_GENERATOR_PROPERTY_NAME);
					spBarcodeGenerator.setBarcode(specimen);
				}
				catch (NameGeneratorException e)
				{
					throw new DAOException(e.getMessage());
				}
			}
		}
	}
	/**
	 * @param specimen Specimen Object
	 * @param externalIdentifierCollection Collection of external identifier
	 */
	private void setExternalIdentifiers(Specimen specimen, Collection<ExternalIdentifier> externalIdentifierCollection)
	{
		if (externalIdentifierCollection != null)
		{
			if (externalIdentifierCollection.isEmpty()) //Dummy entry added for query
			{
				setEmptyExternalIdentifier(specimen, externalIdentifierCollection);
			}
			else
			{
				setSpecimenToExternalIdentifier(specimen, externalIdentifierCollection);
			}
		}
		else
		{
			//Dummy entry added for query.
			externalIdentifierCollection = new HashSet<ExternalIdentifier>();
			setEmptyExternalIdentifier(specimen, externalIdentifierCollection);
			specimen.setExternalIdentifierCollection(externalIdentifierCollection);
		}
	}
	/**
	 * @param specimen Specimen Object
	 * @param dao DAO object
	 * @param parentSpecimen Parent Specimen Object
	 * @throws DAOException Database related Exception
	 */
	private void setParentSCG(Specimen specimen, DAO dao, Specimen parentSpecimen) throws DAOException
	{
		if (parentSpecimen == null)
		{
			if (specimen.getSpecimenCollectionGroup().getGroupName() != null)
			{
				List spgList = dao.retrieve(SpecimenCollectionGroup.class.getName(),
						Constants.NAME, specimen.getSpecimenCollectionGroup().getGroupName());
				specimen.setSpecimenCollectionGroup((AbstractSpecimenCollectionGroup) spgList.get(0));
			}
		}
		else
		{
			if (parentSpecimen.getId() == null)
			{
				List parentSpecimenList = dao.retrieve(Specimen.class.getName(), "label", parentSpecimen.getLabel());
				if (parentSpecimenList != null && !parentSpecimenList.isEmpty())
				{
					parentSpecimen = (Specimen) parentSpecimenList.get(0);
				}
			}
			specimen.setParentSpecimen(parentSpecimen);
			specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
		}
	}
	/**
	 * @param specimen Specimen Object
	 * @throws DAOException Database related Exception
	 */
	private void generateLabel(Specimen specimen) throws DAOException
	{
		/**
		 * Call Specimen label generator if automatic generation is specified
		 */
		if (edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
		{
			try
			{
				TaskTimeCalculater labelGen = TaskTimeCalculater.startTask("Time required for label Generator", NewSpecimenBizLogic.class);
				LabelGenerator spLblGenerator = LabelGeneratorFactory.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
				spLblGenerator.setLabel(specimen);
				TaskTimeCalculater.endTask(labelGen);
			}
			catch (NameGeneratorException e)
			{
				throw new DAOException(e.getMessage());
			}
		}
	}
	/**
	 * @param specimen Specimen object
	 * @param externalIdentifierCollection Collection of external identifier
	 */
	private void setSpecimenToExternalIdentifier(Specimen specimen, Collection<ExternalIdentifier> externalIdentifierCollection)
	{
		Iterator<ExternalIdentifier> it = externalIdentifierCollection.iterator();
		while (it.hasNext())
		{
			ExternalIdentifier exId = (ExternalIdentifier) it.next();
			exId.setSpecimen(specimen);
		}
	}
	/**
	 * @param specimen Specimen object
	 * @param externalIdentifierCollection Collection of external identifier
	 */
	private void setEmptyExternalIdentifier(Specimen specimen, Collection<ExternalIdentifier> externalIdentifierCollection)
	{
		ExternalIdentifier exId = new ExternalIdentifier();
		exId.setName(null);
		exId.setValue(null);
		exId.setSpecimen(specimen);
		externalIdentifierCollection.add(exId);
	}
	/**
	 * To be remove
	 *@param obj Domain object
	 *@param dao DAO object
	 *@param sessionDataBean The session in which the object is saved.
     *@throws DAOException Database related Exception
	 *@throws UserNotAuthorizedException User Not Authorized Exception
	 */
	synchronized public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
								throws DAOException, UserNotAuthorizedException
	{
		Map containerMap = getStorageContainerMap();
		if (obj instanceof LinkedHashSet)
		{
			LinkedHashSet specimenCollection = (LinkedHashSet) obj;
			Iterator<Specimen> specimenIterator = specimenCollection.iterator();
			while (specimenIterator.hasNext())
			{
				Specimen specimen = (Specimen) specimenIterator.next();
				updateStorageLocations((TreeMap) containerMap, specimen);
				Collection<Specimen> childSpecimens = specimen.getChildrenSpecimen();

				if (childSpecimens != null)
				{
					Iterator<Specimen> childSpecimenIterator = childSpecimens.iterator();
					while (childSpecimenIterator.hasNext())
					{
						Specimen derivedSpecimen = (Specimen) childSpecimenIterator.next();
						updateStorageLocations((TreeMap) containerMap, derivedSpecimen);
					}
				}
			}
		}
		else
		{
			updateStorageLocations((TreeMap) containerMap, (Specimen) obj);
		}

	}
	/**
	 * To be remove
	 *@param speCollection Specimen Collection
	 *@param dao DAO object
	 *@param sessionDataBean The session in which the object is saved.
     *@throws DAOException Database related Exception
	 *@throws UserNotAuthorizedException User Not Authorized Exception
	 */
	synchronized public void postInsert(Collection<Specimen> speCollection, DAO dao, SessionDataBean sessionDataBean) throws DAOException,
			UserNotAuthorizedException
	{
		Map containerMap = getStorageContainerMap();
		Iterator<Specimen> specimenIterator = speCollection.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			updateStorageLocations((TreeMap) containerMap, specimen);
			Collection<Specimen> childSpecimens = specimen.getChildrenSpecimen();

			if (childSpecimens != null)
			{
				postInsert(childSpecimens, dao, sessionDataBean);
			}
		}
	}
	/**
	 * @return containerMap Map of containers
	 */
	private Map getStorageContainerMap()
	{
		Map containerMap = null;
		try
		{
			containerMap = StorageContainerUtil.getContainerMapFromCache();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return containerMap;
	}
	/**
	 * @param currentObj Current Object
	 * @param oldObj Persistent Object
	 */
	private void updateChildAttributes(Object currentObj, Object oldObj)
	{
		Specimen currentSpecimen = (Specimen) currentObj;
		Specimen oldSpecimen = (Specimen) oldObj;
		String type = currentSpecimen.getType();
		String pathologicalStatus = currentSpecimen.getPathologicalStatus();
		String id = currentSpecimen.getId().toString();
		if (!currentSpecimen.getPathologicalStatus().equals(oldSpecimen.getPathologicalStatus())
				|| !currentSpecimen.getType().equals(oldSpecimen.getType()))
		{
			try
			{
				JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
				jdbcDao.openSession(null);

				String queryStr = "UPDATE CATISSUE_SPECIMEN SET TYPE = '" + type + "',PATHOLOGICAL_STATUS = '" + pathologicalStatus
						+ "' WHERE LINEAGE = 'ALIQUOT' AND PARENT_SPECIMEN_ID ='" + id + "';";
				jdbcDao.executeUpdate(queryStr);
			}
			catch (Exception e)
			{
				Logger.out.debug("Exception occured while updating aliquots");
			}
		}
	}
	/**
	 * @param containerMap Map of containers
	 * @param specimen Current Specimen
	 */
	void updateStorageLocations(TreeMap containerMap, Specimen specimen)
	{
		try
		{
			if (specimen.getStorageContainer() != null)
			{
				StorageContainerUtil.deleteSinglePositionInContainerMap(specimen.getStorageContainer(), containerMap, specimen
						.getPositionDimensionOne().intValue(), specimen.getPositionDimensionTwo().intValue());
			}
		}
		catch (Exception e)
		{
			Logger.out.error("Exception occured while updating aliquots");
		}
	}
	/**
	 * @param obj AbstractDomainObject
	 * @return dynamicGroups Dynamic group entry for CSM
	 * @throws SMException SMException
	 */
	protected String[] getDynamicGroups(AbstractSpecimenCollectionGroup obj) throws SMException
	{
		TaskTimeCalculater getDynaGrps = TaskTimeCalculater.startTask("DynamicGroup", NewSpecimenBizLogic.class);
		String[] dynamicGroups = new String[1];
		dynamicGroups[0] = securityManager.getProtectionGroupByName(obj, Constants.getCollectionProtocolPGName(null));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		TaskTimeCalculater.endTask(getDynaGrps);
		return dynamicGroups;
	}
	/**
	 * To be remove
	 * @param container Storage Container Object
	 * @param specimen Current Specimen Object
	 * @param dao DAO Object
	 * @throws DAOException Database related exception
	 */
	protected void chkContainerValidForSpecimen(StorageContainer container, Specimen specimen, DAO dao)
											throws DAOException
	{
		Collection holdsSpecimenClassColl = containerHoldsSpecimenClasses.get(container.getId());
		if (holdsSpecimenClassColl == null || holdsSpecimenClassColl.isEmpty())
		{
			if (container.getHoldsSpecimenClassCollection() == null || container.getHoldsSpecimenClassCollection().isEmpty())
			{
				holdsSpecimenClassColl = (Collection) dao.retrieveAttribute(StorageContainer.class.getName(), container.getId(),
					"elements(holdsSpecimenClassCollection)");
			}
			else
			{
				holdsSpecimenClassColl = container.getHoldsSpecimenClassCollection();
			}
			containerHoldsSpecimenClasses.put(container.getId(), holdsSpecimenClassColl);
		}
		if (!holdsSpecimenClassColl.contains(specimen.getClassName()))
		{
			throw new DAOException("This Storage Container cannot hold " + specimen.getClassName() + " Specimen ");
		}
		Collection collectionProtColl = containerHoldsCPs.get(container.getId());
		if (collectionProtColl == null)
		{
			collectionProtColl = container.getCollectionProtocolCollection();
			if(collectionProtColl == null ||collectionProtColl.isEmpty())
			{
				collectionProtColl = (Collection) dao.retrieveAttribute(StorageContainer.class.getName(), container.getId(),
					"elements(collectionProtocolCollection)");
			}
			containerHoldsCPs.put(container.getId(), collectionProtColl);
		}
		CollectionProtocol protocol = retriveSCGAndCP(specimen, dao);
		if (collectionProtColl != null && !collectionProtColl.isEmpty())
		{
			if (getCorrespondingOldObject(collectionProtColl, protocol.getId())==null)
			{
				throw new DAOException("This Storage Container cannot hold specimen of collection protocol " + protocol.getTitle());
			}
		}
	}
	/**
	 * @param specimen Current Specimen
	 * @param dao DAO object
	 * @return CP
	 * @throws DAOException database exception
	 */
	private CollectionProtocol retriveSCGAndCP(Specimen specimen, DAO dao) throws DAOException
	{
		AbstractSpecimenCollectionGroup scg = null;
		CollectionProtocol protocol = null;
		if (specimen.getSpecimenCollectionGroup() != null)
		{
			scg = specimen.getSpecimenCollectionGroup();
		}
		else if (specimen.getId() != null)
		{
			scg = (AbstractSpecimenCollectionGroup)dao.retrieveAttribute(Specimen.class.getName(), specimen.getId(), "specimenCollectionGroup");
		}
		if (scg != null)
		{
			protocol = (CollectionProtocol) dao.retrieveAttribute(SpecimenCollectionGroup.class.getName(), scg.getId(),
					"collectionProtocolRegistration.collectionProtocol");
		}
		if (protocol == null)
		{
			throw new DAOException("This Collection Protocol not found");
		}
		return protocol;
	}
	/**
	 * @param specimenID Specimen Identifier
	 * @param dao DAO object
	 * @return specimenCollectionGroup SCG object
	 * @throws DAOException Database related Exception
	 */
	private SpecimenCollectionGroup loadSpecimenCollectionGroup(Long specimenID, DAO dao) throws DAOException
	{
		String sourceObjectName = Specimen.class.getName();
		String[] selectedColumn = {"specimenCollectionGroup." + Constants.SYSTEM_IDENTIFIER};
		String whereColumnName[] = {Constants.SYSTEM_IDENTIFIER};
		String whereColumnCondition[] = {"="};
		Object whereColumnValue[] = {specimenID};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		List list = dao.retrieve(sourceObjectName, selectedColumn, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		if (!list.isEmpty())
		{
			Long specimenCollectionGroupId = (Long) list.get(0);
			SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
			specimenCollectionGroup.setId(specimenCollectionGroupId);
			return specimenCollectionGroup;
		}
		return null;
	}
	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param oldObj Persistent object
	 * @param dao DAO object
	 * @param sessionDataBean The session in which the object is saved
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	public void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
						throws DAOException, UserNotAuthorizedException
	{
		if (obj.getClass().hashCode() == LinkedHashSet.class.hashCode())
		{
			updateAnticipatorySpecimens(dao,(LinkedHashSet)obj, sessionDataBean);
		}
		else if (obj instanceof Specimen)
		{
			updateSpecimen(dao, obj, oldObj, sessionDataBean);
		}
		else
		{
			throw new DAOException("Object should be either specimen or LinkedHashMap " + "of specimen objects.");
		}
	}
	/**
	 * @param obj The object to be updated.
	 * @param oldObj Persistent object
	 * @param dao DAO object
	 * @param sessionDataBean The session in which the object is saved
	 * @throws DAOException Database related Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void updateSpecimen(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		Specimen specimen = (Specimen) obj;
		Specimen specimenOld = (Specimen) HibernateMetaData.getProxyObjectImpl(oldObj);
		ApiSearchUtil.setSpecimenDefault(specimen);
		validateSpecimen(dao, specimen, specimenOld);
		updateSpecimenData(dao, sessionDataBean, specimen, specimenOld);
		List persistentSpecimenList =dao.retrieve(Specimen.class.getName(),Constants.ID,specimenOld.getId());
		Specimen persistentSpecimen=(Specimen) persistentSpecimenList.get(0);
		//Calculate Quantity
		calculateAvailableQunatity(specimen,persistentSpecimen);
		//Set Specimen Domain Object
		createPersistentSpecimenObj(dao, sessionDataBean, specimen, specimenOld, persistentSpecimen);
		dao.update(persistentSpecimen, sessionDataBean, true, false, false);
		updateChildAttributes(specimen, specimenOld);
		//Audit of Specimen
		dao.audit(persistentSpecimen, oldObj, sessionDataBean, true);
		//Audit of Specimen Characteristics
		dao.audit(persistentSpecimen.getSpecimenCharacteristics(), specimenOld.getSpecimenCharacteristics(), sessionDataBean, true);
		//Disable functionality
		disableSpecimen(dao, specimen, persistentSpecimen);
	}
	/**
	 * @param dao DAO object
	 * @param specimen Specimen
	 * @param persistentSpecimen parentSpecimen
	 * @throws DAOException Database related exception
	 */
	private void disableSpecimen(DAO dao, Specimen specimen, Specimen persistentSpecimen)
			throws DAOException
	{
		if (specimen.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION))
		{
			if (specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
			{
				boolean disposalEventPresent = false;
				Collection<SpecimenEventParameters> eventCollection = persistentSpecimen.getSpecimenEventCollection();
				Iterator<SpecimenEventParameters> itr = eventCollection.iterator();
				while (itr.hasNext())
				{
					Object eventObject = itr.next();
					if (eventObject instanceof DisposalEventParameters)
					{
						disposalEventPresent = true;
						break;
					}
				}
				if (!disposalEventPresent)
				{
					throw new DAOException(ApplicationProperties.getValue("errors.specimen.not.disabled.no.disposalevent"));
				}

				setDisableToSubSpecimen(specimen);
				Long specimenIDArr[] = new Long[Constants.FIRST_COUNT_1];
				specimenIDArr[0] = specimen.getId();
				disableSubSpecimens(dao, specimenIDArr);
			}
		}
	}
	/**
	 * @param dao DAO object
	 * @param sessionDataBean Session details
	 * @param specimen Current Specimen
	 * @param specimenOld Old Specimen
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void updateSpecimenData(DAO dao, SessionDataBean sessionDataBean, Specimen specimen,
			Specimen specimenOld) throws DAOException, UserNotAuthorizedException
	{
		if (!Constants.ALIQUOT.equals(specimen.getLineage()))
		{
			dao.update(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true, false);
		}
		if (!specimen.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION))
		{
			updateConsentWithdrawStatus(specimen, specimenOld, dao, sessionDataBean);
		}
		else if (!specimen.getApplyChangesTo().equalsIgnoreCase(Constants.APPLY_NONE))
		{
			updateConsentStatus(specimen, dao, specimenOld);
		}
		if ((specimen.getAvailableQuantity() != null && specimen.getAvailableQuantity().getValue().doubleValue() == 0)
				|| specimen.getCollectionStatus() == null || specimen.getCollectionStatus().equalsIgnoreCase(Constants.COLLECTION_STATUS_PENDING))
		{
			specimen.setAvailable(new Boolean(false));
		}
		else if (specimenOld.getAvailableQuantity() != null && specimenOld.getAvailableQuantity().getValue().doubleValue() == 0)
		{
			specimen.setAvailable(new Boolean(true));
		}
		else
		{
			specimen.setAvailable(new Boolean(true));
		}
	}
	/**
	 * @param dao DAO object
	 * @param specimen Current Specimen
	 * @param specimenOld Old Specimen
	 * @throws DAOException Database related exception
	 */
	private void validateSpecimen(DAO dao, Specimen specimen, Specimen specimenOld)
			throws DAOException
	{
		if (isStoragePositionChanged(specimenOld, specimen))
		{
			throw new DAOException("Storage Position should not be changed while updating the specimen");
		}
		if (!specimenOld.getLineage().equals(specimen.getLineage()))
		{
			throw new DAOException("Lineage should not be changed while updating the specimen");
		}
		if (!specimenOld.getClassName().equals(specimen.getClassName()))
		{
			throw new DAOException("Class should not be changed while updating the specimen");
		}
		if (specimen.isParentChanged())
		{
			//Check whether container is moved to one of its sub container.
			if (isUnderSubSpecimen(specimen, specimen.getParentSpecimen().getId()))
			{
				throw new DAOException(ApplicationProperties.getValue("errors.specimen.under.subspecimen"));
			}
			Logger.out.debug("Loading ParentSpecimen: " + specimen.getParentSpecimen().getId());
			SpecimenCollectionGroup scg = loadSpecimenCollectionGroup(specimen.getParentSpecimen().getId(), dao);
			specimen.setSpecimenCollectionGroup(scg);
		}
	}
	/**
	 * @param dao DAO object
	 * @param sessionDataBean Session data
	 * @param specimen Current Specimen
	 * @param specimenOld Persistent Specimen
	 * @param persistentSpecimen Persistent Specimen
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void createPersistentSpecimenObj(DAO dao, SessionDataBean sessionDataBean,
			Specimen specimen, Specimen specimenOld, Specimen persistentSpecimen)
			throws DAOException, UserNotAuthorizedException
	{
		persistentSpecimen.setLabel(specimen.getLabel());
		persistentSpecimen.setBarcode(specimen.getBarcode());
		persistentSpecimen.setCreatedOn(specimen.getCreatedOn());
		persistentSpecimen.setAvailable(specimen.getAvailable());
		persistentSpecimen.setBiohazardCollection(specimen.getBiohazardCollection());
		persistentSpecimen.setNoOfAliquots(specimen.getNoOfAliquots());
		persistentSpecimen.setActivityStatus(specimen.getActivityStatus());
		persistentSpecimen.setAliqoutMap(specimen.getAliqoutMap());
		persistentSpecimen.setComment(specimen.getComment());
		persistentSpecimen.setDisposeParentSpecimen(specimen.getDisposeParentSpecimen());
		persistentSpecimen.setLineage(specimen.getLineage());
		persistentSpecimen.setPathologicalStatus(specimen.getPathologicalStatus());
		persistentSpecimen.setType(specimen.getType());
		persistentSpecimen.setCollectionStatus(specimen.getCollectionStatus());
		persistentSpecimen.setConsentTierStatusCollection(specimen.getConsentTierStatusCollection());
		Double conc = 0D;
		if(Constants.MOLECULAR.equals(specimen.getLineage()))
		{
			conc = ((MolecularSpecimen)specimen).getConcentrationInMicrogramPerMicroliter();
			((MolecularSpecimen)persistentSpecimen).setConcentrationInMicrogramPerMicroliter(conc);
		}
		String oldStatus = specimenOld.getCollectionStatus();
		if (!Constants.COLLECTION_STATUS_COLLECTED.equals(oldStatus))
		{
			generateLabel(persistentSpecimen);
			generateBarCode(persistentSpecimen);
		}
		setExternalIdentifier(dao, sessionDataBean, specimen, specimenOld, persistentSpecimen);
	}
	/**
	 * @param dao DAO object
	 * @param sessionDataBean Session data
	 * @param specimen Current Specimen
	 * @param specimenOld Persistent Specimen
	 * @param persistentSpecimen Persistent Specimen
	 * @throws DAOException Database related exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private void setExternalIdentifier(DAO dao, SessionDataBean sessionDataBean, Specimen specimen,
			Specimen specimenOld, Specimen persistentSpecimen) throws DAOException,
			UserNotAuthorizedException
	{
		Collection<ExternalIdentifier> oldExternalIdentifierCollection = specimenOld.getExternalIdentifierCollection();
		Collection<ExternalIdentifier> externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if (externalIdentifierCollection != null)
		{
			Iterator<ExternalIdentifier> it = externalIdentifierCollection.iterator();
			Collection<ExternalIdentifier> perstExIdColl = persistentSpecimen.getExternalIdentifierCollection();
			while (it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier) it.next();
				ExternalIdentifier persistExId = null;
				if (exId.getId()== null)
				{
					exId.setSpecimen(persistentSpecimen);
					persistExId = exId;
					dao.insert( exId, sessionDataBean, false, false);
				}
				else
				{
					persistExId = (ExternalIdentifier) getCorrespondingOldObject(perstExIdColl, exId.getId());
					persistExId.setName(exId.getName());
					persistExId.setValue(exId.getValue());
					ExternalIdentifier oldExId = (ExternalIdentifier) getCorrespondingOldObject(oldExternalIdentifierCollection, exId.getId());
					dao.audit(exId, oldExId, sessionDataBean, true);
				}
			}
			persistentSpecimen.setExternalIdentifierCollection(perstExIdColl);
		}
	}
	/**
	 * @param specimen Current Specimen
	 * @param parentSpecimenID Parent Specimen Identifier
	 * @return boolean true or false
	 */
	private boolean isUnderSubSpecimen(Specimen specimen, Long parentSpecimenID)
	{
		if (specimen != null)
		{
			Iterator<Specimen> iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				if (parentSpecimenID.longValue() == childSpecimen.getId().longValue())
				{
					return true;
				}
				if (isUnderSubSpecimen(childSpecimen, parentSpecimenID))
				{
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * @param specimen Current Specimen
	 */
	private void setDisableToSubSpecimen(Specimen specimen)
	{
		if (specimen != null)
		{
			Iterator<Specimen> iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				childSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				setDisableToSubSpecimen(childSpecimen);
			}
		}
	}
	/**
	 * @param dao DAO object
	 * @param specimen Current Specimen
	 * @param sessionDataBean Session details
	 * @param partOfMultipleSpecimen boolean true or false
	 * @throws DAOException Database related exception
	 * @throws SMException Security related exception
	 */
	private void setSpecimenAttributes(DAO dao, Specimen specimen, SessionDataBean sessionDataBean, boolean partOfMultipleSpecimen)
			throws DAOException, SMException
	{
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		if (specimen.getBarcode() != null && specimen.getBarcode().trim().equals(""))
		{
			specimen.setBarcode(null);
		}
		if (specimen.getSpecimenCollectionGroup() != null)
		{
			AbstractSpecimenCollectionGroup specimenCollectionGroupObj = null;
			if (partOfMultipleSpecimen)
			{
				Collection<ConsentTierStatus> consentTierStatusCollection = null;
				if (specimen.getSpecimenCollectionGroup().getGroupName() != null)
				{
					specimenCollectionGroupObj =  specimen.getSpecimenCollectionGroup();
					consentTierStatusCollection = ((SpecimenCollectionGroup)specimenCollectionGroupObj)
														.getConsentTierStatusCollection();
				}
				if (consentTierStatusCollection != null)
				{
					Collection<ConsentTierStatus> consentTierStatusCollectionForSpecimen = new HashSet<ConsentTierStatus>();
					Iterator<ConsentTierStatus> itr = consentTierStatusCollection.iterator();
					while (itr.hasNext())
					{
						ConsentTierStatus conentTierStatus = (ConsentTierStatus) itr.next();
						ConsentTierStatus consentTierStatusForSpecimen = new ConsentTierStatus();
						consentTierStatusForSpecimen.setStatus(conentTierStatus.getStatus());
						consentTierStatusForSpecimen.setConsentTier(conentTierStatus.getConsentTier());
						consentTierStatusCollectionForSpecimen.add(consentTierStatusForSpecimen);
					}
					specimen.setConsentTierStatusCollection(consentTierStatusCollectionForSpecimen);
				}
			}
			else
			{
				specimenCollectionGroupObj = specimen.getSpecimenCollectionGroup();
			}
			if (specimenCollectionGroupObj != null)
			{
				checkStatus(dao, specimenCollectionGroupObj, "Specimen Collection Group");
			}
		}
		//Load & set Parent Specimen if present
		setParentSpecimen(dao, specimen);
	}
	/**
	 * @param dao DAO Object
	 * @param specimen Current Specimen
	 * @throws DAOException Database related exception
	 */
	private void setParentSpecimen(DAO dao, Specimen specimen) throws DAOException
	{
		if (specimen.getParentSpecimen() != null)
		{
			Specimen parentSpecimen =specimen.getParentSpecimen();
			if (specimen.getParentSpecimen().getActivityStatus() == null)
			{
				checkStatus(dao, parentSpecimen, "Parent Specimen");
			}
			else if (!specimen.getParentSpecimen().getActivityStatus().equals(Constants.ACTIVITY_STATUS_ACTIVE))
			{
				throw new DAOException("Parent Specimen " + ApplicationProperties.getValue("error.object.closed"));
			}
			if (specimen.getLineage() == null)
			{
				specimen.setLineage(Constants.DERIVED_SPECIMEN);
			}
			specimen.setSpecimenEventCollection(populateDeriveSpecimenEventCollection(specimen.getParentSpecimen(), specimen));
		}
	}
	/**
	 * @param dao DAO object
	 * @param specimen Current Specimen
	 * @param sessionDataBean Session details
	 * @param partOfMultipleSpecimen boolean true or false
	 * @throws DAOException Database related exception
	 * @throws SMException Security related exception
	 */
	private void setStorageLocationToNewSpecimen(DAO dao, Specimen specimen, SessionDataBean sessionDataBean, boolean partOfMultipleSpecimen)
			throws DAOException, SMException
	{
		if (specimen.getStorageContainer() != null )
		{
			StorageContainer storageContainerObj = null;
			if (specimen.getParentSpecimen() != null)
			{
				storageContainerObj = chkParentStorageContainer(specimen,storageContainerObj);
			}
			if (storageContainerObj == null)
			{
				if (specimen.getStorageContainer().getId() != null)
				{
					String sourceObjectName = StorageContainer.class.getName();
					storageContainerObj = (StorageContainer)dao.retrieve(sourceObjectName, specimen.getStorageContainer().getId());
				}
				else
				{
					 storageContainerObj = setStorageContainerId(dao, specimen);
				}
				if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(storageContainerObj.getActivityStatus()))
				{
					throw new DAOException("Storage container is closed!");
				}
				chkContainerValidForSpecimen(storageContainerObj, specimen, dao);
				validateUserForContainer(sessionDataBean, storageContainerObj);
			}
			if (specimen.getPositionDimensionOne() == null || specimen.getPositionDimensionTwo() == null)
			{
				LinkedList<Integer> positionValues =StorageContainerUtil.getFirstAvailablePositionsInContainer(storageContainerObj,
				getStorageContainerMap(), storageContainerIds);
				specimen.setPositionDimensionOne(positionValues.get(0));
				specimen.setPositionDimensionTwo(positionValues.get(1));
			}
			String storageValue = storageContainerObj.getName()+":"+specimen.getPositionDimensionOne()+" ,"+
			specimen.getPositionDimensionTwo();
			if (!storageContainerIds.contains(storageValue))
			{
				storageContainerIds.add(storageValue);
			}
			StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.STORAGE_CONTAINER_FORM_ID);
			storageContainerBizLogic.checkContainer(dao, storageContainerObj.getId().toString(), specimen.getPositionDimensionOne().toString(),
					specimen.getPositionDimensionTwo().toString(), sessionDataBean, partOfMultipleSpecimen);
			specimen.setStorageContainer(storageContainerObj);
		}
		if (specimen.getChildrenSpecimen() != null)
		{
			setChildrenSpecimenStorage(specimen.getChildrenSpecimen(), dao, sessionDataBean, partOfMultipleSpecimen);
		}
	}
	/**
	 * @param sessionDataBean Session Details
	 * @param storageContainerObj Storage Container Object
	 * @throws SMException Security related exception
	 * @throws DAOException Database related exception
	 */
	private void validateUserForContainer(SessionDataBean sessionDataBean,Container storageContainerObj) throws SMException,
			DAOException
      {
		Container parentStorageContainer = storageContainerObj.getParent();
		// To get privilegeCache through
		// Singleton instance of PrivilegeManager, requires User LoginName
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(sessionDataBean.getUserName());
		if (parentStorageContainer!=null)
		{
			validateUserForContainer(sessionDataBean, parentStorageContainer);
		}
		Object o = HibernateMetaData.getProxyObjectImpl(storageContainerObj);
		String storageContainerSecObj = o.getClass().getName() + "_"+ storageContainerObj.getId();
		boolean userAuthorize = privilegeCache.hasPrivilege(storageContainerSecObj, Permissions.USE);

		if (!userAuthorize)
		{
			throw new DAOException("User is not authorized to use " +
					"storage container " + storageContainerObj.getName() );
		}
	}
	/**
	 * @param specimen Current Specimen
	 * @param storageContainerObj Storage Container Object
	 * @return storageContainerObj Storage Container Object
	 */
	private StorageContainer chkParentStorageContainer(Specimen specimen,StorageContainer storageContainerObj)
	{
		Specimen parent = specimen.getParentSpecimen();
		if (parent.getStorageContainer()!=null)
		{
			StorageContainer parentContainer = parent.getStorageContainer();
			StorageContainer specimenContainer = specimen.getStorageContainer();
			if (parentContainer.getId().equals(specimenContainer.getId())||
					parentContainer.getName().equals(specimenContainer.getName()))
			{
				storageContainerObj = parentContainer;
			}
		}
		return storageContainerObj;
	}
	/**
	 * @param specimen Current Specimen
	 * @param dao object
	 * @return storageContainerObj Storage Container Object
	 * @throws DAOException Database related exception
	 */
	private StorageContainer setStorageContainerId(DAO dao, Specimen specimen) throws DAOException
	{
		String sourceObjectName = StorageContainer.class.getName();
		List list = dao.retrieve(sourceObjectName, "name" , specimen.getStorageContainer().getName());
		if (!list.isEmpty())
		{
			return (StorageContainer) list.get(0);
		}
		return null;
	}
	/**
	 * @param specimenCollection Collection of specimen object
	 * @param dao DAO object
	 * @param sessionDataBean Session details
	 * @param partOfMultipleSpecimen boolean true or false
	 * @throws DAOException Database related exception
	 * @throws SMException Security related exception
	 */
	private void setChildrenSpecimenStorage(Collection<Specimen> specimenCollection, DAO dao, SessionDataBean sessionDataBean, boolean partOfMultipleSpecimen)
			throws DAOException, SMException
	{
		Iterator<Specimen> iterator = specimenCollection.iterator();
		while (iterator.hasNext())
		{
			Specimen specimen = (Specimen) iterator.next();
			specimen.setSpecimenCollectionGroup(specimen.getParentSpecimen().getSpecimenCollectionGroup());
			setStorageLocationToNewSpecimen(dao, specimen, sessionDataBean, partOfMultipleSpecimen);

		}
	}
	/**
	 * @param specimen Current Specimen
	 * @throws DAOException Database related exception
	 */
	private void allocatePositionForSpecimen(Specimen specimen) throws DAOException
	{
		if (specimen.getPositionDimensionOne() != null ||
				specimen.getPositionDimensionTwo() != null)
		{
			if(specimen.getStorageContainer() != null)
			{
				String storageValue = specimen.getStorageContainer().getName()+":"+specimen.getPositionDimensionOne()+" ,"+
				specimen.getPositionDimensionTwo();
				if (!storageContainerIds.contains(storageValue))
				{
						storageContainerIds.add(storageValue);
				}
				else
				{
					throw new DAOException("Stroage location already in use");
				}
			}
		}
		if(specimen.getChildrenSpecimen()!=null)
		{
			Iterator<Specimen> childrenIterator = specimen.getChildrenSpecimen().iterator();
			while(childrenIterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) childrenIterator.next();
				childSpecimen.setParentSpecimen(specimen);
				allocatePositionForSpecimen(childSpecimen);
			}
		}
	}
	/**
	 * @param dao DAO Object
	 * @param specimenCollectionGroupArr Array of Specimen Collection Group
	 * @throws DAOException Database related exception
	 */
	public void disableRelatedObjectsForSpecimenCollectionGroup(DAO dao, Long specimenCollectionGroupArr[]) throws DAOException
	{
		Logger.out.debug("disableRelatedObjects NewSpecimenBizLogic");
		List listOfSpecimenId = super.disableObjects(dao, Specimen.class, "specimenCollectionGroup", "CATISSUE_SPECIMEN",
				"SPECIMEN_COLLECTION_GROUP_ID", specimenCollectionGroupArr);
		if (!listOfSpecimenId.isEmpty())
		{
			disableSubSpecimens(dao, Utility.toLongArray(listOfSpecimenId));
		}
	}
	/**
	 * @param dao DAO object
	 * @param speIDArr Array of Specimen Id
	 * @throws DAOException Database related exception
	 */
	private void disableSubSpecimens(DAO dao, Long speIDArr[]) throws DAOException
	{
		List listOfSubElement = super.disableObjects(dao, Specimen.class, "parentSpecimen", "CATISSUE_SPECIMEN", "PARENT_SPECIMEN_ID", speIDArr);
		if (listOfSubElement.isEmpty())
		{
			return;
		}
		disableSubSpecimens(dao, Utility.toLongArray(listOfSubElement));
	}
	/**
	 * Todo Remove this method 
	 * @param dao DAO object
	 * @param privilegeName Type of privilege
	 * @param specimenCollectionGroupArr Array of specimenCollectionGroup
	 * @param userId User Identifier
	 * @param assignToUser boolean
	 * @param roleId Role id
	 * @param assignOperation boolean
	 * @throws DAOException Database related exception
	 * @throws SMException Security related exception
	 */
	public void assignPrivilegeToRelatedObjectsForSCG(DAO dao, String privilegeName, Long[] specimenCollectionGroupArr, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		Logger.out.debug("assignPrivilegeToRelatedObjectsForSCG NewSpecimenBizLogic");
		List listOfSpecimenId = super.getRelatedObjects(dao, Specimen.class, "specimenCollectionGroup", specimenCollectionGroupArr);
		if (!listOfSpecimenId.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, Specimen.class, Utility.toLongArray(listOfSpecimenId), userId, roleId, assignToUser,
					assignOperation);
			List specimenCharacteristicsIds = super.getRelatedObjects(dao, Specimen.class, new String[]{"specimenCharacteristics."
					+ Constants.SYSTEM_IDENTIFIER}, new String[]{Constants.SYSTEM_IDENTIFIER}, Utility.toLongArray(listOfSpecimenId));
			super.setPrivilege(dao, privilegeName, Address.class, Utility.toLongArray(specimenCharacteristicsIds), userId, roleId, assignToUser,
					assignOperation);
			assignPrivilegeToSubSpecimens(dao, privilegeName, Specimen.class, Utility.toLongArray(listOfSpecimenId), userId, roleId, assignToUser,
					assignOperation);
		}
	}
	/**
	 * Todo Remove this method
	 * @param dao DAO object
	 * @param privilegeName privilegeName
	 * @param class1 Class
	 * @param speIDArr Array of Specimen Id
	 * @param userId User Identifier
	 * @param roleId Role Identifier
	 * @param assignToUser boolean true or false
	 * @param assignOperation boolean
	 * @throws SMException Security related Exception
	 * @throws DAOException Database related exception
	 */
	private void assignPrivilegeToSubSpecimens(DAO dao, String privilegeName, Class class1, Long[] speIDArr, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, Specimen.class, "parentSpecimen", speIDArr);

		if (listOfSubElement.isEmpty())
		{
			return;
		}
		super.setPrivilege(dao, privilegeName, Specimen.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser, assignOperation);
		List specimenCharacteristicsIds = super.getRelatedObjects(dao, Specimen.class, new String[]{"specimenCharacteristics."
				+ Constants.SYSTEM_IDENTIFIER}, new String[]{Constants.SYSTEM_IDENTIFIER}, Utility.toLongArray(listOfSubElement));
		super.setPrivilege(dao, privilegeName, Address.class, Utility.toLongArray(specimenCharacteristicsIds), userId, roleId, assignToUser,
				assignOperation);

		assignPrivilegeToSubSpecimens(dao, privilegeName, Specimen.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
				assignOperation);
	}

	/* Todo Remove this method
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#setPrivilege(edu.wustl.common.dao.DAO, java.lang.String, java.lang.Class, java.lang.Long[], java.lang.Long, java.lang.String, boolean, boolean)
	 */
	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser, assignOperation);
		List specimenCharacteristicsIds = super.getRelatedObjects(dao, Specimen.class, new String[]{"specimenCharacteristics."
				+ Constants.SYSTEM_IDENTIFIER}, new String[]{Constants.SYSTEM_IDENTIFIER}, objectIds);
		super.setPrivilege(dao, privilegeName, Address.class, Utility.toLongArray(specimenCharacteristicsIds), userId, roleId, assignToUser,
				assignOperation);

		assignPrivilegeToSubSpecimens(dao, privilegeName, Specimen.class, objectIds, userId, roleId, assignToUser, assignOperation);
	}
	/**
	 * Todo Remove this method
	 * @param dao DAO object
	 * @param privilegeName privilegeName
	 * @param objectIds Array of Passed object Id
	 * @param userId User Identifier
	 * @param roleId Role Identifier
	 * @param assignToUser boolean true or false
	 * @param assignOperation boolean
	 * @throws SMException Security related Exception
	 * @throws DAOException Database related exception
	 */
	public void assignPrivilegeToRelatedObjectsForDistributedItem(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		String[] selectColumnNames = {"specimen.id"};
		String[] whereColumnNames = {"id"};
		List listOfSubElement = super.getRelatedObjects(dao, DistributedItem.class, selectColumnNames, whereColumnNames, objectIds);
		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, Specimen.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);
		}
	}
	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 * @param obj Type of object linkedHashSet or domain object
	 * @param dao DAO object
	 * @param operation Type of Operation
	 * @return result
	 * @throws DAOException Database related exception
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		boolean result;

		if (obj instanceof LinkedHashSet)
		{
			if(operation.equals(Constants.ADD))
			{
				return MultipleSpecimenValidationUtil.validateMultipleSpecimen((LinkedHashSet) obj, dao, operation);
			}
			else
			{
				return true;
			}
		}
		else
		{
			result = validateSingleSpecimen((Specimen) obj, dao, operation, false);
		}
		return result;
	}
	/**
	 * Validate Single Specimen
	 * @param specimen Specimen Object to validate
	 * @param dao DAO object
	 * @param operation Add/Edit
	 * @param partOfMulipleSpecimen boolean
	 * @return boolean
	 * @throws DAOException Database related exception
	 */
	private boolean validateSingleSpecimen(Specimen specimen, DAO dao, String operation, boolean partOfMulipleSpecimen) throws DAOException
	{
		if (specimen == null)
		{
			throw new DAOException(ApplicationProperties.getValue("specimen.object.null.err.msg", "Specimen"));
		}
		Validator validator = new Validator();
		validateSpecimenData(specimen, validator);
		validateStorageContainer(specimen, dao);
		validateSpecimenEvent(specimen, validator);
		validateBioHazard(specimen, validator);
		validateExternalIdentifier(specimen, validator);
		validateFields(specimen, dao, operation, partOfMulipleSpecimen);
		validateEnumeratedData(specimen,operation,validator);
		validateSpecimenCharacterstics(specimen);
		return true;
	}
	/**
	 * @param specimen Specimen to validate
	 * @param operation Add/Edit
	 * @param validator Validator ObjectClass contains the methods used for validation of the fields in the userform
	 * @throws DAOException Database related exception
	 */
	private void validateEnumeratedData(Specimen specimen,String operation,Validator validator) throws DAOException
	{
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
		String specimenClass = Utility.getSpecimenClassName(specimen);
		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}

		if (!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass), specimen.getType()))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
		}
		if (operation.equals(Constants.EDIT))
		{
			if (specimen.getCollectionStatus() != null && specimen.getCollectionStatus().equals("Collected")
					&& !specimen.getAvailable().booleanValue())
			{
				throw new DAOException(ApplicationProperties.getValue("specimen.available.operation"));
			}
		}
		if (operation.equals(Constants.ADD))
		{
			if (!specimen.getAvailable().booleanValue())
			{
				throw new DAOException(ApplicationProperties.getValue("specimen.available.errMsg"));
			}

			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(specimen.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, specimen.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}
		if (specimen.getCreatedOn() != null && specimen.getLineage() != null && !specimen.getLineage().equalsIgnoreCase(Constants.NEW_SPECIMEN))
		{
			String tempDate = Utility.parseDateToString(specimen.getCreatedOn(), Constants.DATE_PATTERN_MM_DD_YYYY);
			if (!validator.checkDate(tempDate))
			{
				throw new DAOException(ApplicationProperties.getValue("error.invalid.createdOnDate"));
			}
		}
	}
	/**
	 * @param specimen Specimen to validate
	 * @param validator Validator ObjectClass contains the methods used for validation of the fields in the userform
	 * @throws DAOException Database related exception
	 */
	private void validateSpecimenData(Specimen specimen, Validator validator) throws DAOException
	{
		if (specimen.getSpecimenCollectionGroup() == null
				&& ((specimen.getSpecimenCollectionGroup().getId() == null || specimen.getSpecimenCollectionGroup().getId().equals("-1")) || (specimen
						.getSpecimenCollectionGroup().getGroupName() == null || specimen.getSpecimenCollectionGroup().getGroupName().equals(""))))
		{
			String message = ApplicationProperties.getValue("specimen.specimenCollectionGroup");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}
		if (specimen.getParentSpecimen() != null
				&& (specimen.getParentSpecimen().getLabel() == null || validator.isEmpty(specimen.getParentSpecimen().getLabel())))
		{
			String message = ApplicationProperties.getValue("createSpecimen.parent");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}
		if (validator.isEmpty(specimen.getClassName()))
		{
			String message = ApplicationProperties.getValue("specimen.type");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}
		if (validator.isEmpty(specimen.getType()))
		{
			String message = ApplicationProperties.getValue("specimen.subType");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}
	}
	/**
	 * @param specimen Specimen to validate
	 * @throws DAOException Database related exception
	 */
	private void validateSpecimenCharacterstics(Specimen specimen) throws DAOException
	{
		SpecimenCharacteristics characters = specimen.getSpecimenCharacteristics();

		if (characters == null)
		{
			throw new DAOException(ApplicationProperties.getValue("specimen.characteristics.errMsg"));
		}
		else
		{
			if (specimen.getSpecimenCollectionGroup() != null)
			{
				List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE, null);
				if (!Validator.isEnumeratedValue(tissueSiteList, characters.getTissueSite()))
				{
					if(specimen.getParentSpecimen() == null)
					{
						throw new DAOException(ApplicationProperties.getValue("protocol.tissueSite.errMsg"));
					}
				}
				List tissueSideList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SIDE, null);

				if (!Validator.isEnumeratedValue(tissueSideList, characters.getTissueSide()))
				{
					if(specimen.getParentSpecimen() == null)
					{
						throw new DAOException(ApplicationProperties.getValue("specimen.tissueSide.errMsg"));
					}
				}
				List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);
				if (!Validator.isEnumeratedValue(pathologicalStatusList, specimen.getPathologicalStatus()))
				{
					if(specimen.getParentSpecimen() == null)
					{
						throw new DAOException(ApplicationProperties.getValue("protocol.pathologyStatus.errMsg"));
					}
				}
			}
		}
	}
	/**
	 * @param specimen Specimen to validate
	 * @param validator Validator ObjectClass contains the methods used for validation of the fields in the userform
	 * @throws DAOException Database related exception
	 */
	private void validateSpecimenEvent(Specimen specimen, Validator validator) throws DAOException
	{
		Collection<SpecimenEventParameters> specimenEventCollection = null;
		specimenEventCollection = specimen.getSpecimenEventCollection();
		if (specimenEventCollection != null && !specimenEventCollection.isEmpty())
		{
			Iterator<SpecimenEventParameters> specimenEventCollectionIterator = specimenEventCollection.iterator();
			while (specimenEventCollectionIterator.hasNext())
			{
				Object eventObject = specimenEventCollectionIterator.next();
				EventsUtil.validateEventsObject(eventObject, validator);
			}
		}
		else
		{
			if(specimen.getParentSpecimen()==null)
			{
				throw new DAOException(ApplicationProperties.getValue("error.specimen.noevents"));
			}
		}
	}
	/**
	 * @param specimen Specimen to validate
	 * @param dao DAO object
	 * @throws DAOException Database related exception
	 */
	private void validateStorageContainer(Specimen specimen, DAO dao) throws DAOException
	{
		if (specimen.getStorageContainer() != null
				&& (specimen.getStorageContainer().getId() == null && specimen.getStorageContainer().getName() == null))
		{
			String message = ApplicationProperties.getValue("specimen.storageContainer");
			throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
		}
		if (specimen.getStorageContainer() != null && specimen.getStorageContainer().getName() != null)
		{
			StorageContainer storageContainerObj = specimen.getStorageContainer();
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"id"};
			String[] whereColumnName = {"name"};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {specimen.getStorageContainer().getName()};
			String joinCondition = null;
			List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
			if (!list.isEmpty())
			{
				storageContainerObj.setId((Long) list.get(0));
				specimen.setStorageContainer(storageContainerObj);
			}
			else
			{
				String message = ApplicationProperties.getValue("specimen.storageContainer");
				throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
			}
		}
	}
	/**
	 * @param specimen Specimen to validate
	 * @param validator Validator ObjectClass contains the methods used for validation of the fields in the userform
	 * @throws DAOException Database related exception
	 */
	private void validateExternalIdentifier(Specimen specimen, Validator validator)
			throws DAOException
	{
		Collection<ExternalIdentifier> extIdentifierCollection = specimen.getExternalIdentifierCollection();
		ExternalIdentifier extIdentifier = null;
		if (extIdentifierCollection != null && !extIdentifierCollection.isEmpty())
		{
			Iterator<ExternalIdentifier> itr = extIdentifierCollection.iterator();
			while (itr.hasNext())
			{
				extIdentifier = (ExternalIdentifier) itr.next();
				if (validator.isEmpty(extIdentifier.getName()))
				{
					String message = ApplicationProperties.getValue("specimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.specimen.externalIdentifier.missing", message));
				}
				if (validator.isEmpty(extIdentifier.getValue()))
				{
					String message = ApplicationProperties.getValue("specimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.specimen.externalIdentifier.missing", message));
				}
			}
		}
	}
	/**
	 * @param specimen Specimen to validate
	 * @param validator Validator ObjectClass contains the methods used for validation of the fields in the userform
	 * @throws DAOException Database related exception
	 */
	private void validateBioHazard(Specimen specimen, Validator validator) throws DAOException
	{
		Collection<Biohazard> bioHazardCollection = specimen.getBiohazardCollection();
		Biohazard biohazard = null;
		if (bioHazardCollection != null && !bioHazardCollection.isEmpty())
		{
			Iterator<Biohazard> itr = bioHazardCollection.iterator();
			while (itr.hasNext())
			{
				biohazard = (Biohazard) itr.next();
				if (!validator.isValidOption(biohazard.getType()))
				{
					String message = ApplicationProperties.getValue("newSpecimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.newSpecimen.biohazard.missing", message));
				}
				if (biohazard.getId() == null)
				{
					String message = ApplicationProperties.getValue("newSpecimen.msg");
					throw new DAOException(ApplicationProperties.getValue("errors.newSpecimen.biohazard.missing", message));
				}
			}
		}
	}
	/**
	 * @param specimen Specimen to validate
	 * @param operation Add/Edit
	 * @param dao DAO object
	 * @param partOfMulipleSpecimen boolean
	 * @throws DAOException Database related exception
	 */
	private void validateFields(Specimen specimen, DAO dao, String operation, boolean partOfMulipleSpecimen) throws DAOException
	{
		Validator validator = new Validator();

		if (partOfMulipleSpecimen)
		{
			if (specimen.getSpecimenCollectionGroup() == null || validator.isEmpty(specimen.getSpecimenCollectionGroup().getGroupName()))
			{
				String quantityString = ApplicationProperties.getValue("specimen.specimenCollectionGroup");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
			}
			List spgList = dao
					.retrieve(SpecimenCollectionGroup.class.getName(), Constants.NAME, specimen.getSpecimenCollectionGroup().getGroupName());
			if (spgList.size() == 0)
			{
				throw new DAOException(ApplicationProperties.getValue("errors.item.unknown", "Specimen Collection Group "
						+ specimen.getSpecimenCollectionGroup().getGroupName()));
			}
		}
		if (specimen.getInitialQuantity() == null || specimen.getInitialQuantity().getValue() == null)
		{
			String quantityString = ApplicationProperties.getValue("specimen.quantity");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
		}

		if (specimen.getAvailableQuantity() == null || specimen.getAvailableQuantity().getValue() == null)
		{
			String quantityString = ApplicationProperties.getValue("specimen.availableQuantity");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
		}
		/**
		 * This method gives first valid storage position to a specimen if it is not given
		 * If storage position is given it validates the storage position
		 **/
		StorageContainerUtil.validateStorageLocationForSpecimen(specimen);
	}
	/**
	 * This function checks whether the storage position of a specimen is changed or not
	 * & returns the status accordingly
	 * @param oldSpecimen Persistent Object
	 * @param newSpecimen New Object
	 * @return boolean
	 */
	private boolean isStoragePositionChanged(Specimen oldSpecimen, Specimen newSpecimen)
	{
		boolean isEqual= true;
		StorageContainer oldContainer = oldSpecimen.getStorageContainer();
		StorageContainer newContainer = newSpecimen.getStorageContainer();
		if (oldContainer == null && newContainer == null)
		{
			return false;
		}
		if ((oldContainer == null && newContainer != null) || (oldContainer != null && newContainer == null))
		{
			return isEqual;
		}
		if (oldContainer.getId().longValue() == newContainer.getId().longValue())
		{
			int oldConatinerPos1 = oldSpecimen.getPositionDimensionOne();
			int newConatinerPos1 = newSpecimen.getPositionDimensionOne();
			int oldConatinerPos2 = oldSpecimen.getPositionDimensionTwo();
			int newConatinerPos2 = newSpecimen.getPositionDimensionTwo();
			if (oldConatinerPos1 == newConatinerPos1 && oldConatinerPos2 == newConatinerPos2)
			{
				isEqual= false;
			}
		}
		return isEqual;
	}
	/**
	 * Set event parameters from parent specimen to derived specimen
	 * @param parentSpecimen specimen
	 * @param deriveSpecimen Derived Specimen
	 * @return set
	 */
	private Set<AbstractDomainObject> populateDeriveSpecimenEventCollection(Specimen parentSpecimen, Specimen deriveSpecimen)
	{
		Set<AbstractDomainObject> deriveEventCollection = new HashSet<AbstractDomainObject>();
		Set<SpecimenEventParameters> parentSpecimeneventCollection = (Set<SpecimenEventParameters>) parentSpecimen.getSpecimenEventCollection();
		SpecimenEventParameters specimenEventParameters = null;
		SpecimenEventParameters deriveSpecimenEventParameters = null;
		try
		{
			if (parentSpecimeneventCollection != null)
			{
				for (Iterator<SpecimenEventParameters> iter = parentSpecimeneventCollection.iterator(); iter.hasNext();)
				{
					specimenEventParameters = (SpecimenEventParameters) iter.next();
					deriveSpecimenEventParameters = (SpecimenEventParameters) specimenEventParameters.clone();
					deriveSpecimenEventParameters.setId(null);
					deriveSpecimenEventParameters.setSpecimen(deriveSpecimen);
					deriveEventCollection.add(deriveSpecimenEventParameters);
				}
			}
		}
		catch (CloneNotSupportedException exception)
		{
			exception.printStackTrace();
		}
		return deriveEventCollection;
	}
	/**
	 * This method will retrive no of specimen in the catissue_specimen table
	 * @param sessionData Session data
	 * @return Count of Specimen
	 */
	public int totalNoOfSpecimen(SessionDataBean sessionData)
	{
		String sql = "select MAX(IDENTIFIER) from CATISSUE_SPECIMEN";
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		int noOfRecords = 0;
		try
		{
			jdbcDao.openSession(sessionData);
			List resultList = jdbcDao.executeQuery(sql, sessionData, false, null);
			String number = (String) ((List) resultList.get(0)).get(0);
			if (number == null || number.equals(""))
			{
				number = "0";
			}
			noOfRecords = Integer.parseInt(number);
			jdbcDao.closeSession();
		}
		catch (DAOException daoexception)
		{
			daoexception.printStackTrace();
		}
		catch (ClassNotFoundException classnotfound)
		{
			classnotfound.printStackTrace();
		}
		return noOfRecords;
	}

	/**
	 * This function will retrive SCG Id from SCG Name
	 * @param specimen Current Specimen
	 * @param dao DAO Object
	 * @throws DAOException Database related exception
	 */
	public void retriveSCGIdFromSCGName(Specimen specimen, DAO dao) throws DAOException
	{
		String specimenCollGpName = specimen.getSpecimenCollectionGroup().getGroupName();
		if (specimenCollGpName != null && !specimenCollGpName.equals(""))
		{
			String[] selectColumnName = {"id"};
			String[] whereColumnName = {"name"};
			String[] whereColumnCondition = {"="};
			String[] whereColumnValue = {specimenCollGpName};
			List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), selectColumnName, whereColumnName, whereColumnCondition,
					whereColumnValue, null);
			if (scgList != null && !scgList.isEmpty())
			{
				specimen.getSpecimenCollectionGroup().setId(((Long) scgList.get(0)));
			}
		}
	}
	/**
	 * This method updates the consents and specimen based on the the consent withdrawal option.
	 * @param specimen New object
	 * @param oldSpecimen Old Object
	 * @param dao DAO object
	 * @param sessionDataBean Session Details
	 * @throws DAOException Database exception
	 */
	private void updateConsentWithdrawStatus(Specimen specimen, Specimen oldSpecimen, DAO dao, SessionDataBean sessionDataBean) throws DAOException
	{
		if (!specimen.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION))
		{

			String consentWithdrawOption = specimen.getConsentWithdrawalOption();
			Collection<ConsentTierStatus> consentTierStatusCollection = specimen.getConsentTierStatusCollection();
			Iterator<ConsentTierStatus> itr = consentTierStatusCollection.iterator();
			while (itr.hasNext())
			{
				ConsentTierStatus status = (ConsentTierStatus) itr.next();
				long consentTierID = status.getConsentTier().getId().longValue();
				if (status.getStatus().equalsIgnoreCase(Constants.WITHDRAWN))
				{
					WithdrawConsentUtil.updateSpecimenStatus(specimen, consentWithdrawOption, consentTierID, dao, sessionDataBean);
				}
			}
		}
	}
	/**
	 * This method is used to update the consent status of child specimens as per the option selected by the user
	 * @param specimen New object
	 * @param oldSpecimen Old Object
	 * @param dao DAO object
	 * @throws DAOException Database exception
	 */
	private void updateConsentStatus(Specimen specimen, DAO dao, Specimen oldSpecimen) throws DAOException
	{
		if (!specimen.getApplyChangesTo().equalsIgnoreCase(Constants.APPLY_NONE))
		{
			String applyChangesTo = specimen.getApplyChangesTo();
			Collection<ConsentTierStatus> consentTierStatusCollection = specimen.getConsentTierStatusCollection();
			Collection<ConsentTierStatus> oldConsentTierStatusCollection = oldSpecimen.getConsentTierStatusCollection();
			Iterator<ConsentTierStatus> itr = consentTierStatusCollection.iterator();
			while (itr.hasNext())
			{
				ConsentTierStatus status = (ConsentTierStatus) itr.next();
				long consentTierID = status.getConsentTier().getId().longValue();
				String statusValue = status.getStatus();
				Collection childSpecimens = (Collection)dao.retrieveAttribute(Specimen.class.getName(), specimen.getId(),
						"elements(childrenSpecimen)");
				Iterator childItr = childSpecimens.iterator();
				while (childItr.hasNext())
				{
					Specimen childSpecimen = (Specimen) childItr.next();
					WithdrawConsentUtil.updateSpecimenConsentStatus(childSpecimen, applyChangesTo, consentTierID, statusValue,
							consentTierStatusCollection, oldConsentTierStatusCollection, dao);
				}
			}
		}
	}
	/**
	 * This function is used to update specimens and their dervied & aliquot specimens
	 * @param newSpecimenCollection List of specimens to update along with children
	 * specimens.
	 * @param sessionDataBean current user session information
	 * @param dao DAO object
	 * @throws DAOException If DAO fails to update one or more specimens
	 * this function will throw DAOException.
	 */
	public void updateAnticipatorySpecimens(DAO dao, Collection<Specimen> newSpecimenCollection, SessionDataBean sessionDataBean) throws DAOException
	{
		updateMultipleSpecimens(dao,newSpecimenCollection, sessionDataBean, true);
	}

	/**
	 * This function is used to bulk update multiple specimens. If
	 * any specimen contains children specimens those will be inserted
	 * @param newSpecimenCollection List of specimens to update along with
	 * new children specimens if any. 7
	 * @param sessionDataBean current user session information
	 * @throws DAOException If DAO fails to update one or more specimens
	 * this function will throw DAOException.
	 */
	public void bulkUpdateSpecimens(Collection<Specimen> newSpecimenCollection, SessionDataBean sessionDataBean) throws DAOException
	{
		Iterator<Specimen> iterator = newSpecimenCollection.iterator();
		DAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		int specimenCtr = Constants.FIRST_COUNT_1;
		int childSpecimenCtr = 0;
		try
		{
			((HibernateDAO) dao).openSession(sessionDataBean);
			while (iterator.hasNext())
			{
				Specimen newSpecimen = (Specimen) iterator.next();
				if(newSpecimen.getStorageContainer() != null && newSpecimen.getStorageContainer().getId() == null)
				{
					newSpecimen.setStorageContainer(setStorageContainerId(dao, newSpecimen));
				}
			}
			iterator = newSpecimenCollection.iterator();
			while (iterator.hasNext())
			{
				Specimen newSpecimen = (Specimen) iterator.next();
				Specimen specimenDO = updateSingleSpecimen(dao, newSpecimen, sessionDataBean, false);
				Collection<Specimen> childrenSpecimenCollection = newSpecimen.getChildrenSpecimen();
				if (childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
				{
					childSpecimenCtr = updateChildSpecimen(sessionDataBean, dao, childSpecimenCtr,
							specimenDO, childrenSpecimenCollection);
				}
				specimenCtr++;
			}
			specimenCtr = 0;
			((HibernateDAO) dao).commit();
			postInsert(newSpecimenCollection, dao, sessionDataBean);
		}
		catch (Exception exception)
		{
			((AbstractDAO) dao).rollback();
			String errorMsg = "Failed to save. ";
			if (specimenCtr != 0)
			{
				errorMsg = "specimen number " + specimenCtr + " cannot be saved. ";
				if (childSpecimenCtr != 0)
				{
					errorMsg = "Cannot insert child specimen " + childSpecimenCtr + ", of specimen " + specimenCtr + ". ";
				}
			}
			throw new DAOException(errorMsg + exception.getMessage());
		}
		finally
		{
			((HibernateDAO) dao).closeSession();
		}
	}
	/**
	 * @param sessionDataBean Session details
	 * @param dao DAO Object
	 * @param childSpecimenCtr Count
	 * @param specimenDO Persistent object
	 * @param childrenSpecimenCollection childSpecimen Collection
	 * @return count
	 * @throws DAOException Database Exception
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 */
	private int updateChildSpecimen(SessionDataBean sessionDataBean, DAO dao, int childSpecimenCtr,
			Specimen specimenDO, Collection<Specimen> childrenSpecimenCollection)
			throws DAOException, UserNotAuthorizedException
	{
		Iterator<Specimen> childIterator = childrenSpecimenCollection.iterator();
		while (childIterator.hasNext())
		{
			childSpecimenCtr++;
			Specimen childSpecimen = (Specimen) childIterator.next();
			childSpecimen.setParentSpecimen(specimenDO);
			insert( childSpecimen, dao, sessionDataBean);
		}
		childSpecimenCtr = 0;
		return childSpecimenCtr;
	}
	/**
	 * @param dao DAO object
	 * @param newSpecimenCollection Specimen Collection
	 * @param sessionDataBean Session Details
	 * @param updateChildrens boolean
	 * @throws DAOException Database exception
	 */
	protected void updateMultipleSpecimens(DAO dao,Collection<Specimen> newSpecimenCollection, SessionDataBean sessionDataBean, boolean updateChildrens)
			throws DAOException
	{
		Iterator<Specimen> iterator = newSpecimenCollection.iterator();
		try
		{
			AbstractSpecimenCollectionGroup scg = null;
			while (iterator.hasNext())
			{
				Specimen newSpecimen = (Specimen) iterator.next();
				if(scg == null)
				{
					scg =(AbstractSpecimenCollectionGroup)dao.retrieveAttribute(Specimen.class.getName(),
								newSpecimen.getId(), "specimenCollectionGroup");
				}
				newSpecimen.setSpecimenCollectionGroup(scg);
				allocatePositionForSpecimen(newSpecimen);
			}
			iterator = newSpecimenCollection.iterator();
			while (iterator.hasNext())
			{
				Specimen newSpecimen = (Specimen) iterator.next();
				setStorageLocationToNewSpecimen(dao, newSpecimen, sessionDataBean, true);
			}
			iterator = newSpecimenCollection.iterator();
			while (iterator.hasNext())
			{
				Specimen newSpecimen = (Specimen) iterator.next();
				updateSingleSpecimen(dao, newSpecimen, sessionDataBean, updateChildrens);
			}
			postInsert(newSpecimenCollection, dao, sessionDataBean);
		}
		catch (Exception exception)
		{
			throw new DAOException("Failed to update multiple specimen " + exception.getMessage());
		}
		finally
		{
			storageContainerIds.clear();
		}
	}
	/**
	 * @param dao DAO object
	 * @param sessionDataBean Session Details
	 * @param updateChildrens boolean
	 * @param newSpecimen Specimen Object
	 * @throws DAOException Database exception
	 * @return Specimen object
	 */
	public Specimen updateSingleSpecimen(DAO dao, Specimen newSpecimen, SessionDataBean sessionDataBean, boolean updateChildrens) throws DAOException
	{
		try
		{
			List specList = dao.retrieve(Specimen.class.getName(), "id", newSpecimen.getId());
			if (specList != null && !specList.isEmpty())
			{
				Specimen specimenDO = (Specimen) specList.get(0);
				updateSpecimenDomainObject(dao, newSpecimen, specimenDO);
				if (updateChildrens)
				{
					updateChildrenSpecimens(dao, newSpecimen, specimenDO);
				}
				dao.update(specimenDO, sessionDataBean, false, false, false);
				return specimenDO;
			}
			else
			{
				throw new DAOException("Invalid Specimen with label" + newSpecimen.getLabel());
			}
		}
		catch (UserNotAuthorizedException authorizedException)
		{
			throw new DAOException("User not authorized to update specimens" + authorizedException.getMessage());

		}
		catch (SMException exception)
		{
			throw new DAOException(exception.getMessage(), exception);
		}
	}
	/**
	 * @param dao DAO object
	 * @param specimenVO New Object
	 * @param specimenDO Persistent object
	 * @throws DAOException Database exception
	 * @throws SMException Security exception
	 */
	private void updateChildrenSpecimens(DAO dao, Specimen specimenVO, Specimen specimenDO) throws DAOException, SMException
	{
		Collection<Specimen> childrenSpecimens = specimenDO.getChildrenSpecimen();
		if (childrenSpecimens == null || childrenSpecimens.isEmpty())
		{
			return;
		}
		Iterator<Specimen> iterator = childrenSpecimens.iterator();
		while (iterator.hasNext())
		{
			Specimen specimen = (Specimen) iterator.next();
			Specimen relatedSpecimen = getCorelatedSpecimen(specimen.getId(), specimenVO.getChildrenSpecimen());
			if (relatedSpecimen != null)
			{
				updateSpecimenDomainObject(dao, relatedSpecimen, specimen);
				updateChildrenSpecimens(dao, relatedSpecimen, specimen);
			}
		}
	}
	/**
	 * @param id Identifier
	 * @param specimenCollection Specimen Collection
	 * @return Specimen
	 * @throws DAOException Database exception
	 */
	private Specimen getCorelatedSpecimen(Long id, Collection<Specimen> specimenCollection) throws DAOException
	{
		Iterator<Specimen> iterator = specimenCollection.iterator();
		while (iterator.hasNext())
		{
			Specimen specimen = (Specimen) iterator.next();
			if (specimen.getId().longValue() == id.longValue())
			{
				return specimen;
			}
		}
		return null;
	}
	/**
	 * @param specimen Specimen
	 * @param dao DAO object
	 * @throws DAOException Database exception
	 */
	private void checkDuplicateSpecimenFields(Specimen specimen, DAO dao) throws DAOException
	{
		List list = dao.retrieve(Specimen.class.getCanonicalName(), "label", specimen.getLabel());
		if (!list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Specimen specimenObject = (Specimen) (list.get(i));
				if (!specimenObject.getId().equals(specimen.getId()))
				{
					throw new DAOException("Label " + specimen.getLabel() + " is already exists!");

				}
			}
		}
		if (specimen.getBarcode() != null)
		{
			list = dao.retrieve(Specimen.class.getCanonicalName(), "barcode", specimen.getBarcode());
			if (!list.isEmpty())
			{
				for (int i = 0; i < list.size(); i++)
				{
					Specimen specimenObject = (Specimen) (list.get(i));
					if (!specimenObject.getId().equals(specimen.getId()))
					{
						throw new DAOException("Barcode " + specimen.getBarcode() + " is already exists.");

					}
				}
			}
		}
	}
	/**
	 * @param dao DAO object
	 * @param specimenVO New Object
	 * @param specimenDO Persistent object
	 * @throws DAOException Database exception
	 * @throws SMException Security exception
	 */
	private void updateSpecimenDomainObject(DAO dao, Specimen specimenVO, Specimen specimenDO) throws DAOException, SMException
	{
		if (specimenVO.getBarcode() != null && specimenVO.getBarcode().trim().length() == 0)
		{
			specimenVO.setBarcode(null);
		}
		checkDuplicateSpecimenFields(specimenVO, dao);
		specimenDO.setLabel(specimenVO.getLabel());
		specimenDO.setBarcode(specimenVO.getBarcode());
		specimenDO.setAvailable(specimenVO.getAvailable());
		if (specimenVO.getStorageContainer() != null)
		{
			setStorageContainer(dao, specimenVO, specimenDO);
		}
		else
		{
			specimenDO.setStorageContainer(null);
		}
		calculateAvailableQunatity(specimenVO, specimenDO);
		String oldStatus = specimenDO.getCollectionStatus();
		if (specimenVO.getCollectionStatus() != null)
		{
			specimenDO.setCollectionStatus(specimenVO.getCollectionStatus());
		}
		if (!Constants.COLLECTION_STATUS_COLLECTED.equals(oldStatus))
		{
			generateLabel(specimenDO);
			generateBarCode(specimenDO);
		}
		// code for multiple specimen edit
		if (specimenVO.getCreatedOn() != null)
		{
			specimenDO.setCreatedOn(specimenVO.getCreatedOn());
		}
		setSpecimenData(specimenVO, specimenDO);
		if (Constants.MOLECULAR.equals(specimenVO.getClassName()))
		{
			Double concentration = ((MolecularSpecimen) specimenVO).getConcentrationInMicrogramPerMicroliter();
			((MolecularSpecimen) specimenDO).setConcentrationInMicrogramPerMicroliter(concentration);
		}

	}
	/**
	 * @param specimenVO New Object
	 * @param specimenDO Persistent object
	 */
	private void setSpecimenData(Specimen specimenVO, Specimen specimenDO)
	{
		if (specimenVO.getPathologicalStatus() != null)
		{
			specimenDO.setPathologicalStatus(specimenVO.getPathologicalStatus());
		}

		if (specimenVO.getSpecimenCharacteristics() != null)
		{
			SpecimenCharacteristics characteristics = specimenVO.getSpecimenCharacteristics();
			if (characteristics.getTissueSide() != null || characteristics.getTissueSite() != null)
			{
				SpecimenCharacteristics specimenCharacteristics = specimenDO.getSpecimenCharacteristics();
				if (specimenCharacteristics != null)
				{
					specimenCharacteristics.setTissueSide(specimenVO.getSpecimenCharacteristics().getTissueSide());
					specimenCharacteristics.setTissueSite(specimenVO.getSpecimenCharacteristics().getTissueSite());
				}
			}
		}
		if (specimenVO.getComment() != null)
		{
			specimenDO.setComment(specimenVO.getComment());
		}
		if (specimenVO.getBiohazardCollection() != null && !specimenVO.getBiohazardCollection().isEmpty())
		{
			specimenDO.setBiohazardCollection(specimenVO.getBiohazardCollection());
		}
		// external identifier not set while editing multiple specimen
		if (specimenVO.getExternalIdentifierCollection() != null && !specimenVO.getExternalIdentifierCollection().isEmpty())
		{
			Iterator<ExternalIdentifier> itr = specimenVO.getExternalIdentifierCollection().iterator();
			while(itr.hasNext())
			{
				ExternalIdentifier ex = (ExternalIdentifier) itr.next();
				ex.setSpecimen(specimenVO);
			}
			specimenDO.setExternalIdentifierCollection(specimenVO.getExternalIdentifierCollection());
		}
	}
	/**
	 * Logic for Calculate Quantity
	 * @param specimenVO New Specimen object
	 * @param specimenDO Persistent Object
	 * @throws DAOException Database Exception
	 */
	private void calculateAvailableQunatity(Specimen specimenVO, Specimen specimenDO) throws DAOException
	{
		if (specimenVO.getInitialQuantity() != null)
		{
			Quantity quantity = specimenVO.getInitialQuantity();
			Quantity availableQuantity = specimenVO.getAvailableQuantity();
			if (availableQuantity == null)
			{
				availableQuantity = new Quantity("0");
				specimenDO.setAvailableQuantity(availableQuantity);
			}
			double modifiedInitQty = quantity.getValue();
			double oldInitQty = specimenDO.getInitialQuantity().getValue();
			double differenceQty = modifiedInitQty - oldInitQty;
			double newAvailQty  =  differenceQty + availableQuantity.getValue();
			if (newAvailQty <0)
			{
				newAvailQty =0;
			}
			availableQuantity = specimenDO.getAvailableQuantity();
			if (availableQuantity == null)
			{
				availableQuantity = new Quantity("0");
				specimenDO.setAvailableQuantity(availableQuantity);
			}
			availableQuantity.setValue(newAvailQty);
			calculateParentQuantity(specimenDO, differenceQty, newAvailQty);
			if(specimenDO.getChildrenSpecimen()==null ||specimenDO.getChildrenSpecimen().isEmpty())
			{
				availableQuantity.setValue(newAvailQty);
			}
			if((specimenDO.getAvailableQuantity()!=null && specimenDO.getAvailableQuantity().getValue().doubleValue() > 0))
			{
				specimenDO.setAvailable(Boolean.TRUE);
			}
			Quantity oldInitialQty = null;
			if (specimenDO.getInitialQuantity() == null)
			{
				oldInitialQty = new Quantity();
				specimenDO.setInitialQuantity(oldInitialQty);
			}
			else
			{
				oldInitialQty = specimenDO.getInitialQuantity();
			}
			oldInitialQty.setValue(modifiedInitQty);
		}
	}
	/**
	 * @param specimenDO Persistent object
	 * @param differenceQty Change in quantity
	 * @param newAvailQty New Available quantity
	 * @throws DAOException Database Exception
	 */
	private void calculateParentQuantity(Specimen specimenDO, double differenceQty,
			double newAvailQty) throws DAOException
	{
		if(specimenDO.getParentSpecimen()!=null)
		{
			if(specimenDO.getLineage().equals("Aliquot"))
			{
				double parentAvl=0.0;
				if(!specimenDO.getCollectionStatus().equals("Pending"))
				{
					parentAvl = specimenDO.getParentSpecimen().getAvailableQuantity().getValue() - differenceQty;
				}
				else
				{
					parentAvl = specimenDO.getParentSpecimen().getAvailableQuantity().getValue() - newAvailQty;
				}
				if(parentAvl < 0)
				{
					throw new DAOException("Insufficient Parent's Available Quantity");
				}
				specimenDO.getParentSpecimen().getAvailableQuantity().setValue(parentAvl);
			}
		}
	}
	/**
	 * @return containerHoldsCPs
	 */
	public Map<Long, Collection<CollectionProtocol>> getContainerHoldsCPs()
	{
		return containerHoldsCPs;
	}
	/**
	 * @param containerHoldsCPs Map of container that can holds CP
	 */
	public void setContainerHoldsCPs(Map<Long, Collection<CollectionProtocol>> containerHoldsCPs)
	{
		this.containerHoldsCPs = containerHoldsCPs;
	}
	/**
	 * @return containerHoldsSpecimenClasses containerHoldsSpecimenClasses
	 */
	public Map<Long, Collection<String>> getContainerHoldsSpecimenClasses()
	{
		return containerHoldsSpecimenClasses;
	}
	/**
	 * @param containerHoldsSpecimenClasses container of SpecimenClasses
	 */
	public void setContainerHoldsSpecimenClasses(Map<Long, Collection<String>> containerHoldsSpecimenClasses)
	{
		this.containerHoldsSpecimenClasses = containerHoldsSpecimenClasses;
	}
	/**
	 * @param dao DAO object
	 * @param specimenVO New Specimen object
	 * @param specimenDO Persistent Object
	 * @throws DAOException Database Exception
	 * @throws SMException Security exception
	 */
	private void setStorageContainer(DAO dao, Specimen specimenVO, Specimen specimenDO) throws DAOException, SMException
	{
		StorageContainer storageContainer = specimenVO.getStorageContainer();

		specimenDO.setPositionDimensionOne(specimenVO.getPositionDimensionOne());
		specimenDO.setPositionDimensionTwo(specimenVO.getPositionDimensionTwo());
		specimenDO.setStorageContainer(storageContainer);
	}
	/**
	 * @return boolean
	 */
	public boolean isCpbased()
	{
		return cpbased;
	}
	/**
	 * @param cpbased boolean
	 */
	public void setCpbased(boolean cpbased)
	{
		this.cpbased = cpbased;
	}

	/**
	 * This function throws BizLogicException if the domainObj is of type SpecimenCollectionRequirementGroup
	 * @param domainObj current domain object
	 * @param uiForm current form
	 * @throws BizLogicException BizLogic exception
	 */
	protected void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm) throws BizLogicException
	{

		Specimen specimen = (Specimen) domainObj;
		AbstractSpecimenCollectionGroup absspecimenCollectionGroup = specimen.getSpecimenCollectionGroup();
		Object proxySpecimenCollectionGroup = HibernateMetaData.getProxyObjectImpl(absspecimenCollectionGroup);
		if ((proxySpecimenCollectionGroup instanceof SpecimenCollectionRequirementGroup))
		{
			NewSpecimenForm newSpecimenForm = (NewSpecimenForm) uiForm;
			newSpecimenForm.setForwardTo(Constants.PAGEOF_SPECIMEN_COLLECTION_REQUIREMENT_GROUP);
			throw new BizLogicException("The Specimen is Added as Requirement, this can not be edited!!");

		}
	}
}