/**
 * <p>Title: UserHDAO Class>
 * <p>Description:	UserHDAO is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ajay Sharma
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolEventComparator;
import edu.wustl.catissuecore.util.CollectionProtocolSeqComprator;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.SpecimenComparator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.DomainBeanIdentifierComparator;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * UserHDAO is used to add user information into the database using Hibernate.
 * 
 * @author kapil_kaveeshwar
 */
public class SpecimenCollectionGroupBizLogic extends DefaultBizLogic
{
	/**
	 * Saves the user object in the database.
	 * 
	 * @param obj
	 *            The user object to be saved.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws DAOException
	 * @throws  
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		if (specimenCollectionGroup.getSpecimenCollectionSite() != null)
		{
			Object siteObj = dao.retrieve(Site.class.getName(), specimenCollectionGroup.getSpecimenCollectionSite().getId());
			if (siteObj != null)
			{
				// check for closed Site
				checkStatus(dao, specimenCollectionGroup.getSpecimenCollectionSite(), "Site");
				specimenCollectionGroup.setSpecimenCollectionSite((Site) siteObj);
			}
		}
		Object collectionProtocolEventObj = dao.retrieve(CollectionProtocolEvent.class.getName(), specimenCollectionGroup
				.getCollectionProtocolEvent().getId());
		Collection specimenCollection = null;
		Long userId = Utility.getUserID(dao, sessionDataBean);
		if (collectionProtocolEventObj != null)
		{
			CollectionProtocolEvent cpe = (CollectionProtocolEvent) collectionProtocolEventObj;
			// check for closed CollectionProtocol
			checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");
			specimenCollectionGroup.setCollectionProtocolEvent(cpe);
			specimenCollection = getCollectionSpecimen(specimenCollectionGroup, cpe, userId );
		}
		setCollectionProtocolRegistration(dao, specimenCollectionGroup, null);
		generateSCGLabel(specimenCollectionGroup);
		String barcode=specimenCollectionGroup.getName();
		generateSCGBarcode(specimenCollectionGroup);
		if((barcode!=specimenCollectionGroup.getName())&&barcode!=null)
		{
			specimenCollectionGroup.setBarcode(barcode);
		}
		//This check is added if empty values added by UI tnen shud add default values in parameters
		checkSCGEvents(specimenCollectionGroup.getSpecimenEventParametersCollection(),sessionDataBean);
		dao.insert(specimenCollectionGroup, sessionDataBean, true, true);
		if (specimenCollection != null)
		{
			new NewSpecimenBizLogic().insertMultiple(specimenCollection, (AbstractDAO)dao, sessionDataBean);
		}

	}

	/**
	 * @param specimenCollectionGroup
	 * @param specimenCollectionRequirementGroup
	 * @param userId
	 * @return
	 */
	private Collection<AbstractDomainObject> getCollectionSpecimen(SpecimenCollectionGroup specimenCollectionGroup,
			CollectionProtocolEvent cpe, Long userId)
	{
		Collection<AbstractDomainObject> cloneSpecimenCollection = null;
		try
		{
			Collection<SpecimenRequirement> reqSpecimenCollection = cpe.getSpecimenRequirementCollection();
			List<SpecimenRequirement> reqSpecimenList = new LinkedList<SpecimenRequirement>(reqSpecimenCollection);
			CollectionProtocolUtil.getSortedCPEventList(reqSpecimenList);
			if (reqSpecimenList != null && !reqSpecimenList.isEmpty())
			{
				cloneSpecimenCollection = new LinkedHashSet<AbstractDomainObject>();
				Iterator<SpecimenRequirement> itReqSpecimenCollection = reqSpecimenList.iterator();
				while (itReqSpecimenCollection.hasNext())
				{
					SpecimenRequirement SpecimenRequirement = itReqSpecimenCollection.next();
					if (Constants.NEW_SPECIMEN.equals(SpecimenRequirement.getLineage()))
					{
						Specimen cloneSpecimen = getCloneSpecimen(SpecimenRequirement, null, specimenCollectionGroup, userId);
						//kalpana : bug #6224
						if (edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
						{
							LabelGenerator specimenLableGenerator = LabelGeneratorFactory
									.getInstance(Constants.SPECIMEN_LABEL_GENERATOR_PROPERTY_NAME);
							specimenLableGenerator.setLabel(cloneSpecimen);
						}
						cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
						cloneSpecimenCollection.add(cloneSpecimen);
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return cloneSpecimenCollection;
	}

	/**
	 * @param reqSpecimen
	 * @param pSpecimen
	 * @param specimenCollectionGroup
	 * @param userId
	 * @return
	 */
	private Specimen getCloneSpecimen(SpecimenRequirement reqSpecimen, Specimen pSpecimen, SpecimenCollectionGroup specimenCollectionGroup,
			Long userId)
	{
		Specimen newSpecimen;
		try
		{
			newSpecimen = (Specimen) new SpecimenObjectFactory().getDomainObject(reqSpecimen.getClassName(), reqSpecimen);
		}
		catch (AssignDataException e1)
		{
			e1.printStackTrace();
			return null;
		}
		newSpecimen.setParentSpecimen(pSpecimen);
		//bug no. 7690
		if (!reqSpecimen.getSpecimenEventCollection().isEmpty() && reqSpecimen.getSpecimenEventCollection() != null)
		{
			newSpecimen.setPropogatingSpecimenEventCollection(reqSpecimen.getSpecimenEventCollection(), userId);
		}

		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);

		/*Collection childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
		 if (childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
		 {
		 Collection childrenSpecimen = new LinkedHashSet();
		 Iterator<SpecimenRequirement> it = childrenSpecimenCollection.iterator();
		 while (it.hasNext())
		 {
		 SpecimenRequirement childSpecimen = it.next();
		 Specimen newchildSpecimen = getCloneSpecimen(childSpecimen, newSpecimen, specimenCollectionGroup, userId);
		 childrenSpecimen.add(newchildSpecimen);
		 }
		 newSpecimen.setChildSpecimenCollection(childrenSpecimen);
		 }*/
		Collection childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
		List childrenspecimenList = new LinkedList(childrenSpecimenCollection);
		CollectionProtocolUtil.getSortedCPEventList(childrenspecimenList);

		if (childrenspecimenList != null && !childrenspecimenList.isEmpty())
		{
			Collection childrenSpecimen = new LinkedHashSet();
			Iterator<SpecimenRequirement> it = childrenspecimenList.iterator();
			while (it.hasNext())
			{
				SpecimenRequirement childSpecimen = it.next();
				Specimen newchildSpecimen = getCloneSpecimen(childSpecimen, newSpecimen, specimenCollectionGroup, userId);
				childrenSpecimen.add(newchildSpecimen);
			}
			newSpecimen.setChildSpecimenCollection(childrenSpecimen);
		}

		return newSpecimen;
	}

	/**
	 * @param specimenCollectionGroup
	 * @throws DAOException
	 */
	private void generateSCGLabel(SpecimenCollectionGroup specimenCollectionGroup) throws DAOException
	{
		try
		{
			if (Variables.isSpecimenCollGroupLabelGeneratorAvl)
			{
				LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
						.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
				specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
			}
		}
		catch (NameGeneratorException nameGeneratorException)
		{
			throw new DAOException(nameGeneratorException.getMessage(), nameGeneratorException);
		}
	}

	/**
	 * Method to generate barcode for the SpecimenCollectionGroup
	 * @param specimenCollectionGroup Object of SpecimenCollectionGroup
	 * @throws DAOException DAO exception
	 */
	private void generateSCGBarcode(SpecimenCollectionGroup specimenCollectionGroup) throws DAOException
	{
		try
		{
			if (Variables.isSpecimenCollGroupBarcodeGeneratorAvl)
			{
				BarcodeGenerator specimenCollectionGroupBarcodeGenerator = BarcodeGeneratorFactory
						.getInstance(Constants.SPECIMEN_COLL_GROUP_BARCODE_GENERATOR_PROPERTY_NAME);
				specimenCollectionGroupBarcodeGenerator.setBarcode(specimenCollectionGroup);
			}
		}
		catch (NameGeneratorException nameGeneratorException)
		{
			throw new DAOException(nameGeneratorException.getMessage(), nameGeneratorException);
		}
	}

	/**
	 * This function used to get specimenCollectionGroup object from id and 
	 * populate all its associated entities.
	 * @param scgId 				SpecimenCollectionGroup id
	 * @param bean 					SessionDataBean
	 * @param retrieveAssociates	flag for retrieve associated entities or not.
	 * @return object of CollectionProtocol
	 * @throws BizLogicException If fails to retrieve any of the required entity.
	 */
	public SpecimenCollectionGroup getSCGFromId(Long scgId, SessionDataBean bean, boolean retrieveAssociates) throws BizLogicException
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(bean);

			Object object = dao.retrieve(SpecimenCollectionGroup.class.getName(), scgId);

			if (object == null)
			{
				throw new BizLogicException("Cannot find CP. Failed to find " + "SCG for id " + scgId);
			}
			SpecimenCollectionGroup specCollGroup = (SpecimenCollectionGroup) object;
			if (retrieveAssociates)
			{
				retreiveAssociates(scgId, specCollGroup);
			}
			return specCollGroup;
		}
		catch (DAOException exception)
		{
			throw new BizLogicException(exception.getMessage(), exception);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException daoException)
			{
				Logger.out.fatal("Failed to close session due to " + daoException.getMessage(), daoException);
			}
		}
	}

	/**
	 * 
	 * @param scgId
	 * @param specCollGroup
	 * @throws BizLogicException
	 */
	private void retreiveAssociates(Long scgId, SpecimenCollectionGroup specCollGroup) throws BizLogicException
	{
		CollectionProtocolRegistration collProtReg = specCollGroup.getCollectionProtocolRegistration();

		if (collProtReg == null)
		{
			throw new BizLogicException("Cannot find CP. CPR for " + "SCG id " + scgId + " is unexpectedly null.");
		}
		CollectionProtocol collProt = collProtReg.getCollectionProtocol();
		if (collProt == null)
		{
			throw new BizLogicException("Cannot find CP. for SCG id " + scgId);
		}
		Long id = collProt.getId();

		Collection<Specimen> specimenCollection = specCollGroup.getSpecimenCollection();
		retrieveSpecimens(specimenCollection);

	}

	/**
	 * @param specimenCollection
	 */
	private void retrieveSpecimens(Collection specimenCollection)
	{
		if (specimenCollection == null)
		{
			return;
		}

		Iterator<Specimen> specIterator = specimenCollection.iterator();
		while (specIterator.hasNext())
		{
			Specimen specimen = specIterator.next();
			Collection childSpecimenCollection = specimen.getChildSpecimenCollection();
			retrieveSpecimens(childSpecimenCollection);
		}
	}

	private Set getProtectionObjects(AbstractDomainObject obj)
	{
		Set protectionObjects = new HashSet();

		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		protectionObjects.add(specimenCollectionGroup);

		Logger.out.debug(protectionObjects.toString());
		return protectionObjects;
	}

	protected String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		String[] dynamicGroups = new String[1];

		dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(
				specimenCollectionGroup.getCollectionProtocolRegistration(), Constants.getCollectionProtocolPGName(null));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		return dynamicGroups;

	}

	/**
	 * Updates the persistent object in the database.
	 * 
	 * @param obj
	 *            The object to be updated.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		SpecimenCollectionGroup oldspecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;
		//lazy false change

		Object object = dao.retrieve(SpecimenCollectionGroup.class.getName(), oldspecimenCollectionGroup.getId());

		SpecimenCollectionGroup persistentSCG = null;
		if (object != null)
		{
			persistentSCG = (SpecimenCollectionGroup) object;
		}

		// Adding default events if they are null from API
		Collection spEventColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
		
		//Copy the SCG events for propagating changes in specimens.
		Collection copiedSCGEventsColl = copySCGEvents(spEventColl);
		if (spEventColl == null || spEventColl.isEmpty())
		{
			setDefaultEvents(specimenCollectionGroup, sessionDataBean);
		}
		else
		{
			//Collection pEvtPrmColl = persistentSCG.getSpecimenEventParametersCollection();
			//Iterator evntIterator = pEvtPrmColl.iterator();
			if (persistentSCG.getSpecimenEventParametersCollection() == null || persistentSCG.getSpecimenEventParametersCollection().isEmpty())
			{
				//Collection newScgEventColl = checkSCGEvents(spEventColl,sessionDataBean);
				persistentSCG.setSpecimenEventParametersCollection(spEventColl);
			}
			Collection pEvtPrmColl = persistentSCG.getSpecimenEventParametersCollection();
			Iterator evntIterator = pEvtPrmColl.iterator();
			while (evntIterator.hasNext())
			{
				SpecimenEventParameters event = (SpecimenEventParameters) evntIterator.next();
				SpecimenEventParameters newEvent = (SpecimenEventParameters) getCorrespondingObject(spEventColl, event.getClass());
				updateEvent(event, newEvent, sessionDataBean);
				//spEventColl.remove(newEvent);

			}
		}
		// Check for different closed site
		Site oldSite = oldspecimenCollectionGroup.getSpecimenCollectionSite();
		Site site = specimenCollectionGroup.getSpecimenCollectionSite();
		if (oldSite == null && site != null)
		{
			checkStatus(dao, site, "Site");
		}
		else if (!site.getId().equals(oldSite.getId()))
		{
			checkStatus(dao, site, "Site");
		}

		// site check complete
		Long oldEventId = oldspecimenCollectionGroup.getCollectionProtocolEvent().getId();
		Long eventId = specimenCollectionGroup.getCollectionProtocolEvent().getId();
		if (oldEventId.longValue() != eventId.longValue())
		{
			// -- check for closed CollectionProtocol
			Object proxyObject = dao.retrieve(CollectionProtocolEvent.class.getName(), specimenCollectionGroup.getCollectionProtocolEvent().getId());
			if (proxyObject != null)
			{
				// check for closed CollectionProtocol
				CollectionProtocolEvent cpe = (CollectionProtocolEvent) proxyObject;
				if (!cpe.getCollectionProtocol().getId().equals(
						oldspecimenCollectionGroup.getCollectionProtocolEvent().getCollectionProtocol().getId()))
					checkStatus(dao, cpe.getCollectionProtocol(), "Collection Protocol");

				specimenCollectionGroup.setCollectionProtocolEvent((CollectionProtocolEvent) proxyObject);
			}
		}
		// CollectionProtocol check complete.

		//setCollectionProtocolRegistration(dao, specimenCollectionGroup, oldspecimenCollectionGroup);

		// Mandar 22-Jan-07 To disable consents accordingly in SCG and
		// Specimen(s) start

		if (!Constants.WITHDRAW_RESPONSE_NOACTION.equalsIgnoreCase(specimenCollectionGroup.getConsentWithdrawalOption()))
		{
			verifyAndUpdateConsentWithdrawn(specimenCollectionGroup, oldspecimenCollectionGroup, dao, sessionDataBean);
			persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup.getConsentTierStatusCollection());

		}
		// Mandar 22-Jan-07 To disable consents accordingly in SCG and
		// Specimen(s) end
		// Mandar 24-Jan-07 To update consents accordingly in SCG and
		// Specimen(s) start
		else if (!specimenCollectionGroup.getApplyChangesTo().equalsIgnoreCase(Constants.APPLY_NONE))
		{
			ConsentUtil.updateSpecimenStatusInSCG(specimenCollectionGroup, oldspecimenCollectionGroup, dao);
		}
		// Mandar 24-Jan-07 To update consents accordingly in SCG and
		// Specimen(s) end
		specimenCollectionGroup.setCollectionProtocolRegistration(persistentSCG.getCollectionProtocolRegistration());

		Integer offset = specimenCollectionGroup.getOffset();
		if (offset != null)
		{

			if (oldspecimenCollectionGroup.getOffset() != null)
				offset = offset - oldspecimenCollectionGroup.getOffset();
			if (offset != 0)
			{
				//updateOffset(offset, specimenCollectionGroup, sessionDataBean, dao);
				getDetailsOfCPRForSCG(specimenCollectionGroup.getCollectionProtocolRegistration(), dao);
				CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
				cprBizLogic.checkAndUpdateChildOffset(dao, sessionDataBean, oldspecimenCollectionGroup.getCollectionProtocolRegistration(), offset
						.intValue());
				cprBizLogic.updateForOffset(dao, sessionDataBean, specimenCollectionGroup.getCollectionProtocolRegistration(), offset.intValue());
			}

		}

		persistentSCG.setSpecimenCollectionSite(site);
		persistentSCG.setOffset(offset);
		persistentSCG.setCollectionStatus(specimenCollectionGroup.getCollectionStatus());
		persistentSCG.setComment(specimenCollectionGroup.getComment());
		persistentSCG.setActivityStatus(specimenCollectionGroup.getActivityStatus());
		persistentSCG.setSurgicalPathologyNumber(specimenCollectionGroup.getSurgicalPathologyNumber());
		persistentSCG.setClinicalDiagnosis(specimenCollectionGroup.getClinicalDiagnosis());
		persistentSCG.setClinicalStatus(specimenCollectionGroup.getClinicalStatus());
		persistentSCG.setName(specimenCollectionGroup.getName());
		persistentSCG.setBarcode(specimenCollectionGroup.getBarcode());
		persistentSCG.setConsentTierStatusCollection(specimenCollectionGroup.getConsentTierStatusCollection());
		dao.update(persistentSCG, sessionDataBean, true, true, false);
		/**
		 * Name : Ashish Gupta Reviewer Name : Sachin Lale Bug ID: 2741 Patch
		 * ID: 2741_6 Description: Method to update events in all specimens
		 * related to this scg
		 */
		// Populating Events in all specimens
		if (specimenCollectionGroup.isApplyEventsToSpecimens())
		{
			updateEvents(copiedSCGEventsColl, oldspecimenCollectionGroup, dao, sessionDataBean);
		}
		// Audit.
		dao.audit(obj, oldObj, sessionDataBean, true);
		SpecimenCollectionGroup oldSpecimenCollectionGroup = (SpecimenCollectionGroup) oldObj;

		// Disable the related specimens to this specimen group
		Logger.out.debug("specimenCollectionGroup.getActivityStatus() " + specimenCollectionGroup.getActivityStatus());
		if (specimenCollectionGroup.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			Logger.out.debug("specimenCollectionGroup.getActivityStatus() " + specimenCollectionGroup.getActivityStatus());
			Long specimenCollectionGroupIDArr[] = {specimenCollectionGroup.getId()};

			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao, specimenCollectionGroupIDArr);
		}

	}

	private Object getCorrespondingObject(Collection objectCollection, Class eventClass)
	{
		Iterator iterator = objectCollection.iterator();
		while (iterator.hasNext())
		{
			AbstractDomainObject abstractDomainObject = (AbstractDomainObject) iterator.next();
			if (abstractDomainObject.getClass().hashCode() == eventClass.hashCode())
			{
				return abstractDomainObject;
			}
		}

		return null;
	}
	private Collection copySCGEvents(Collection scgEventColl)
	{
		Collection<SpecimenEventParameters> newSCGEventColl = new HashSet<SpecimenEventParameters>();

		if (scgEventColl != null && !scgEventColl.isEmpty())
		{
			Iterator scgEventCollIter = scgEventColl.iterator();
			while (scgEventCollIter.hasNext())
			{
				Object scgEventCollObj = scgEventCollIter.next();
				if (scgEventCollObj instanceof CollectionEventParameters)
				{
					CollectionEventParameters collEventParam = (CollectionEventParameters) scgEventCollObj;
					CollectionEventParameters newCollEventParam = new CollectionEventParameters(collEventParam);
					newCollEventParam.setUser(collEventParam.getUser());
					newCollEventParam.setTimestamp(collEventParam.getTimestamp());
					newSCGEventColl.add(newCollEventParam);
				}
				if (scgEventCollObj instanceof ReceivedEventParameters)
				{

					ReceivedEventParameters recEventParam = (ReceivedEventParameters) scgEventCollObj;
					ReceivedEventParameters newRecEventParam = new ReceivedEventParameters(recEventParam);
					newRecEventParam.setUser(recEventParam.getUser());
					newRecEventParam.setTimestamp(recEventParam.getTimestamp());
					newSCGEventColl.add(newRecEventParam);
				}
				
			}
		}
		return newSCGEventColl;
	}

	private void checkSCGEvents(Collection scgEventColl, SessionDataBean sessionDataBean)
	{
		if (scgEventColl != null && !scgEventColl.isEmpty())
		{
			User user = new User();
			user.setId(sessionDataBean.getUserId());
			Iterator scgEventCollIter = scgEventColl.iterator();
			while (scgEventCollIter.hasNext())
			{
				Object scgEventCollObj = scgEventCollIter.next();
				if (scgEventCollObj instanceof CollectionEventParameters)
				{
					CollectionEventParameters collEventParam = (CollectionEventParameters) scgEventCollObj;
					if (collEventParam.getUser() == null)
					{
						collEventParam.setUser(user);
					}
					if (collEventParam.getCollectionProcedure() != null && collEventParam.getCollectionProcedure().equals(""))
					{
						collEventParam.setCollectionProcedure(Constants.NOT_SPECIFIED);
					}
					if (collEventParam.getContainer() != null && collEventParam.getContainer().equals(""))
					{
						collEventParam.setContainer(Constants.NOT_SPECIFIED);
					}
				}
				if (scgEventCollObj instanceof ReceivedEventParameters)
				{
					ReceivedEventParameters recEventParam = (ReceivedEventParameters) scgEventCollObj;
					if (recEventParam.getUser() == null)
					{
						recEventParam.setUser(user);
					}

					if (recEventParam.getReceivedQuality() != null && recEventParam.getReceivedQuality().equals(""))
					{
						recEventParam.setReceivedQuality(Constants.NOT_SPECIFIED);

					}

				}
			}
		}

	}

	private void updateEvent(SpecimenEventParameters event, SpecimenEventParameters newEvent, SessionDataBean sessionDataBean)
	{
		User user = new User();
		user.setId(sessionDataBean.getUserId());
		if (event instanceof CollectionEventParameters)
		{
			CollectionEventParameters toChangeCollectionEventParameters = (CollectionEventParameters) event;
			CollectionEventParameters newCollectionEventParameters = (CollectionEventParameters) newEvent;
			if (newCollectionEventParameters.getUser() != null)
				toChangeCollectionEventParameters.setUser(newCollectionEventParameters.getUser());
			toChangeCollectionEventParameters.setTimestamp(newCollectionEventParameters.getTimestamp());
			if (!newCollectionEventParameters.getCollectionProcedure().equals(""))
				toChangeCollectionEventParameters.setCollectionProcedure(newCollectionEventParameters.getCollectionProcedure());
			if (!newCollectionEventParameters.getComment().equals(""))
				toChangeCollectionEventParameters.setComment(newCollectionEventParameters.getComment());
			if (!newCollectionEventParameters.getContainer().equals(""))
				toChangeCollectionEventParameters.setContainer(newCollectionEventParameters.getContainer());

			if (toChangeCollectionEventParameters.getUser() == null)
			{
				toChangeCollectionEventParameters.setUser(user);
			}
			if(toChangeCollectionEventParameters.getCollectionProcedure().equals(""))
			{
				toChangeCollectionEventParameters.setCollectionProcedure((String) DefaultValueManager
						.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
			}
			if(toChangeCollectionEventParameters.getContainer().equals(""))
			{
				toChangeCollectionEventParameters.setContainer((String) DefaultValueManager
						.getDefaultValue(Constants.DEFAULT_CONTAINER));
			}
		}
		else
		{
			ReceivedEventParameters toChanagereceivedEventParameters = (ReceivedEventParameters) event;
			ReceivedEventParameters newreceivedEventParameters = (ReceivedEventParameters) newEvent;
			if (!newreceivedEventParameters.getComment().equals(""))
				toChanagereceivedEventParameters.setComment(newreceivedEventParameters.getComment());
			if (!newreceivedEventParameters.getReceivedQuality().equals(""))
				toChanagereceivedEventParameters.setReceivedQuality(newreceivedEventParameters.getReceivedQuality());

			toChanagereceivedEventParameters.setTimestamp(newreceivedEventParameters.getTimestamp());
			if (newreceivedEventParameters.getUser() != null)
				toChanagereceivedEventParameters.setUser(newreceivedEventParameters.getUser());

			if (toChanagereceivedEventParameters.getUser() == null)
			{
				toChanagereceivedEventParameters.setUser(user);
			}
			if(toChanagereceivedEventParameters.getReceivedQuality().equals(""))
			{
				toChanagereceivedEventParameters.setReceivedQuality((String)DefaultValueManager
						.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
				
			}
		}
	}

	/**
	 * @param specimenCollectionGroup
	 * @param sessionDataBean
	 *            Sets the default events if they are not specified
	 */
	private void setDefaultEvents(SpecimenCollectionGroup specimenCollectionGroup, SessionDataBean sessionDataBean)
	{
		Collection specimenEventColl = new HashSet();
		User user = new User();
		user.setId(sessionDataBean.getUserId());
		CollectionEventParameters collectionEventParameters = EventsUtil.populateCollectionEventParameters(user);
		collectionEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);
		specimenEventColl.add(collectionEventParameters);

		ReceivedEventParameters receivedEventParameters = EventsUtil.populateReceivedEventParameters(user);
		receivedEventParameters.setSpecimenCollectionGroup(specimenCollectionGroup);
		specimenEventColl.add(receivedEventParameters);

		specimenCollectionGroup.setSpecimenEventParametersCollection(specimenEventColl);
	}

	/**
	 * @param specimenCollectionGroup
	 * @param oldspecimenCollectionGroup
	 * @param dao
	 * @param sessionDataBean
	 * @throws UserNotAuthorizedException
	 * @throws DAOException
	 */
	private void updateEvents(Collection newEventColl, SpecimenCollectionGroup oldspecimenCollectionGroup, DAO dao,
			SessionDataBean sessionDataBean) throws UserNotAuthorizedException, DAOException
	{
		CollectionEventParameters scgCollectionEventParameters = null;
		ReceivedEventParameters scgReceivedEventParameters = null;
		//Collection newEventColl = specimenCollectionGroup.getSpecimenEventParametersCollection();
		if (newEventColl != null && !newEventColl.isEmpty())
		{
			Iterator newEventCollIter = newEventColl.iterator();
			while (newEventCollIter.hasNext())
			{
				Object newEventCollObj = newEventCollIter.next();
				if (newEventCollObj instanceof CollectionEventParameters)
				{
					scgCollectionEventParameters = (CollectionEventParameters) newEventCollObj;
					continue;
				}
				else if (newEventCollObj instanceof ReceivedEventParameters)
				{
					scgReceivedEventParameters = (ReceivedEventParameters) newEventCollObj;
				}
			}
		}
		// populateEventsInSpecimens(oldspecimenCollectionGroup,)
		Collection specimenColl = oldspecimenCollectionGroup.getSpecimenCollection();
		if (specimenColl != null && !specimenColl.isEmpty())
		{
			SpecimenEventParametersBizLogic specimenEventParametersBizLogic = (SpecimenEventParametersBizLogic) BizLogicFactory.getInstance()
					.getBizLogic(Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID);
			Iterator iter = specimenColl.iterator();
			while (iter.hasNext())
			{
				Specimen specimen = (Specimen) iter.next();
				Collection eventColl = specimen.getSpecimenEventCollection();
				if (eventColl != null && !eventColl.isEmpty())
				{
					Iterator eventIter = eventColl.iterator();
					while (eventIter.hasNext())
					{
						Object eventObj = eventIter.next();
						if (eventObj instanceof CollectionEventParameters)
						{
							CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObj;
							CollectionEventParameters newcollectionEventParameters = populateCollectionEventParameters(eventObj,
									scgCollectionEventParameters, collectionEventParameters);
							specimenEventParametersBizLogic.update(dao, newcollectionEventParameters, collectionEventParameters, sessionDataBean);
							continue;
						}
						else if (eventObj instanceof ReceivedEventParameters)
						{
							ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObj;
							ReceivedEventParameters newReceivedEventParameters = populateReceivedEventParameters(receivedEventParameters,
									scgReceivedEventParameters);
							specimenEventParametersBizLogic.update(dao, newReceivedEventParameters, receivedEventParameters, sessionDataBean);
						}
					}
				}
			}
		}
	}

	/**
	 * @param eventObj
	 * @param scgCollectionEventParameters
	 */
	private CollectionEventParameters populateCollectionEventParameters(Object eventObj, CollectionEventParameters scgCollectionEventParameters,
			CollectionEventParameters collectionEventParameters)
	{
		// CollectionEventParameters newcollectionEventParameters =
		// collectionEventParameters;
		CollectionEventParameters newcollectionEventParameters = new CollectionEventParameters();
		if (scgCollectionEventParameters.getCollectionProcedure() != null && !scgCollectionEventParameters.getCollectionProcedure().equals(""))
			newcollectionEventParameters.setCollectionProcedure(scgCollectionEventParameters.getCollectionProcedure());
		else
			newcollectionEventParameters.setCollectionProcedure(collectionEventParameters.getCollectionProcedure());

		if (scgCollectionEventParameters.getContainer() != null && !scgCollectionEventParameters.getContainer().equals(""))
			newcollectionEventParameters.setContainer(scgCollectionEventParameters.getContainer());
		else
			newcollectionEventParameters.setContainer(collectionEventParameters.getContainer());

		newcollectionEventParameters.setTimestamp(scgCollectionEventParameters.getTimestamp());

		if (scgCollectionEventParameters.getUser() != null && !scgCollectionEventParameters.getUser().getId().equals(""))
			newcollectionEventParameters.setUser(scgCollectionEventParameters.getUser());
		else
			newcollectionEventParameters.setUser(collectionEventParameters.getUser());

		if (scgCollectionEventParameters.getComment() != null && !scgCollectionEventParameters.getComment().equals(""))
			newcollectionEventParameters.setComment(scgCollectionEventParameters.getComment());
		else
			newcollectionEventParameters.setComment(collectionEventParameters.getComment());

		newcollectionEventParameters.setSpecimen(collectionEventParameters.getSpecimen());
		newcollectionEventParameters.setSpecimenCollectionGroup(collectionEventParameters.getSpecimenCollectionGroup());
		newcollectionEventParameters.setId(collectionEventParameters.getId());

		return newcollectionEventParameters;
	}

	/**
	 * @param receivedEventParameters
	 * @param scgReceivedEventParameters
	 * @return
	 */
	private ReceivedEventParameters populateReceivedEventParameters(ReceivedEventParameters receivedEventParameters,
			ReceivedEventParameters scgReceivedEventParameters)
	{
		// ReceivedEventParameters newReceivedEventParameters =
		// receivedEventParameters;
		ReceivedEventParameters newReceivedEventParameters = new ReceivedEventParameters();
		if (scgReceivedEventParameters.getReceivedQuality() != null && !scgReceivedEventParameters.getReceivedQuality().equals(""))
			newReceivedEventParameters.setReceivedQuality(scgReceivedEventParameters.getReceivedQuality());
		else
			newReceivedEventParameters.setReceivedQuality(receivedEventParameters.getReceivedQuality());

		newReceivedEventParameters.setTimestamp(scgReceivedEventParameters.getTimestamp());

		if (scgReceivedEventParameters.getUser() != null && scgReceivedEventParameters.getUser().getId() != -1)
			newReceivedEventParameters.setUser(scgReceivedEventParameters.getUser());
		else
			newReceivedEventParameters.setUser(receivedEventParameters.getUser());

		newReceivedEventParameters.setId(receivedEventParameters.getId());
		if (scgReceivedEventParameters.getComment() != null && !scgReceivedEventParameters.getComment().equals(""))
			newReceivedEventParameters.setComment(scgReceivedEventParameters.getComment());
		else
			newReceivedEventParameters.setComment(receivedEventParameters.getComment());

		newReceivedEventParameters.setSpecimen(receivedEventParameters.getSpecimen());
		newReceivedEventParameters.setSpecimenCollectionGroup(receivedEventParameters.getSpecimenCollectionGroup());
		return newReceivedEventParameters;
	}

	private void setCollectionProtocolRegistration(DAO dao, SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws DAOException
	{
		CollectionProtocolRegistration cpr = specimenCollectionGroup.getCollectionProtocolRegistration();
		if (cpr == null)
		{
			cpr = oldSpecimenCollectionGroup.getCollectionProtocolRegistration();
		}
		if (cpr == null)
		{
			throw new DAOException("CPR cannot be null for SCG");
		}
		Long id = null;

		if (cpr.getId() != null && cpr.getId().longValue() > 0)
		{
			id = cpr.getId();
		}
		else
		{
			id = getCPRIDFromParticipant(dao, specimenCollectionGroup, oldSpecimenCollectionGroup);
		}
		cpr = (CollectionProtocolRegistration) dao.retrieve(cpr.getClass().getName(), id);
		specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
		specimenCollectionGroup.getCollectionProtocolRegistration().getSpecimenCollectionGroupCollection().add(specimenCollectionGroup);
	}

	/**
	 * @param dao
	 * @param specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup
	 * @throws DAOException
	 */
	private Long getCPRIDFromParticipant(DAO dao, SpecimenCollectionGroup specimenCollectionGroup, SpecimenCollectionGroup oldSpecimenCollectionGroup)
			throws DAOException
	{
		List list = getCPRIdList(dao, specimenCollectionGroup, oldSpecimenCollectionGroup);
		if (!list.isEmpty())
		{
			return ((Long) list.get(0));
		}
		return null;
	}

	/**
	 * @param dao
	 * @param specimenCollectionGroup
	 * @param oldSpecimenCollectionGroup
	 * @return
	 * @throws DAOException
	 */
	private List getCPRIdList(DAO dao, SpecimenCollectionGroup specimenCollectionGroup, SpecimenCollectionGroup oldSpecimenCollectionGroup)
			throws DAOException
	{
		String sourceObjectName = CollectionProtocolRegistration.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName = new String[2];
		String[] whereColumnCondition = {"=", "="};
		Object[] whereColumnValue = new Object[2];
		String joinCondition = Constants.AND_JOIN_CONDITION;
		CollectionProtocolRegistration cpr = specimenCollectionGroup.getCollectionProtocolRegistration();

		whereColumnName[0] = "collectionProtocol." + Constants.SYSTEM_IDENTIFIER;
		whereColumnValue[0] = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId();
		if (cpr.getParticipant() != null)
		{
			// check for closed Participant
			Participant participantObject = cpr.getParticipant();

			if (oldSpecimenCollectionGroup != null)
			{
				Participant participantObjectOld = oldSpecimenCollectionGroup.getCollectionProtocolRegistration().getParticipant();
				if (!participantObject.getId().equals(participantObjectOld.getId()))
					checkStatus(dao, participantObject, "Participant");
			}
			else
				checkStatus(dao, participantObject, "Participant");

			whereColumnName[1] = "participant." + Constants.SYSTEM_IDENTIFIER;
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getParticipant().getId();
		}
		else
		{
			whereColumnName[1] = "protocolParticipantIdentifier";
			whereColumnValue[1] = specimenCollectionGroup.getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
			Logger.out.debug("Value returned:" + whereColumnValue[1]);
		}

		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		return list;
	}

	private void setCollectionProtocolRegistrationOld(DAO dao, SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCollectionGroup oldSpecimenCollectionGroup) throws DAOException
	{
		List list = getCPRIdList(dao, specimenCollectionGroup, oldSpecimenCollectionGroup);
		if (!list.isEmpty())
		{
			// check for closed CollectionProtocolRegistration
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of
			 * Api Search, previoulsy it was failing since there was default
			 * class level initialization on domain object. For example in User
			 * object, it was initialized as protected String lastName=""; So we
			 * removed default class level initialization on domain object and
			 * are initializing in method setAllValues() of domain object. But
			 * in case of Api Search, default values will not get set since
			 * setAllValues() method of domainObject will not get called. To
			 * avoid null pointer exception, we are setting the default values
			 * same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setCollectionProtocolRegistrationDefault(collectionProtocolRegistration);
			// End:- Change for API Search

			collectionProtocolRegistration.setId((Long) list.get(0));
			if (oldSpecimenCollectionGroup != null)
			{
				CollectionProtocolRegistration collectionProtocolRegistrationOld = oldSpecimenCollectionGroup.getCollectionProtocolRegistration();

				if (!collectionProtocolRegistration.getId().equals(collectionProtocolRegistrationOld.getId()))
					checkStatus(dao, collectionProtocolRegistration, "Collection Protocol Registration");
			}
			else
				checkStatus(dao, collectionProtocolRegistration, "Collection Protocol Registration");

			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
		}
	}

	// public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[])
	// throws
	// DAOException
	// {
	// List listOfSubElement = super.disableObjects(dao,
	// SpecimenCollectionGroup.class, "collectionProtocolRegistration",
	// "CATISSUE_SPECIMEN_COLL_GROUP", "COLLECTION_PROTOCOL_REG_ID",
	// collProtRegIDArr);
	// if (!listOfSubElement.isEmpty())
	// {
	// NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)
	// BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
	// bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao,
	// Utility.toLongArray(listOfSubElement));
	// }
	// }

	public void disableRelatedObjects(DAO dao, Long collProtRegIDArr[]) throws DAOException
	{
		List listOfSubElement = getRelatedObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration", collProtRegIDArr);
		dao.disableRelatedObjects("CATISSUE_ABS_SPECI_COLL_GROUP", Constants.SYSTEM_IDENTIFIER_COLUMN_NAME, Utility.toLongArray(listOfSubElement));
		auditDisabledObjects(dao, "CATISSUE_SPECIMEN_COLL_GROUP", listOfSubElement);
		if (!listOfSubElement.isEmpty())
		{
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.disableRelatedObjectsForSpecimenCollectionGroup(dao, Utility.toLongArray(listOfSubElement));
		}
	}

	/**
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @param longs
	 * @throws DAOException
	 * @throws SMException
	 */
	public void assignPrivilegeToRelatedObjects(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, SpecimenCollectionGroup.class, "collectionProtocolRegistration", objectIds);
		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, SpecimenCollectionGroup.class, Utility.toLongArray(listOfSubElement), userId, roleId,
					assignToUser, assignOperation);
			NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao, privilegeName, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
					assignOperation);
		}

	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class,
	 *      Long[], Long, String, boolean)
	 */
	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser, assignOperation);

		NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		bizLogic.assignPrivilegeToRelatedObjectsForSCG(dao, privilegeName, objectIds, userId, roleId, assignToUser, assignOperation);
	}

	/**
	 * check for the specimen associated with the SCG
	 * 
	 * @param obj
	 * @param dao
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	protected boolean isSpecimenExists(Object obj, DAO dao, Long scgId) throws DAOException, ClassNotFoundException
	{

		String hql = " select" + " elements(scg.specimenCollection) " + " from " + " edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg , "
				+ " edu.wustl.catissuecore.domain.Specimen as s" + " where scg.id = " + scgId + " and"
				+ " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '" + Constants.ACTIVITY_STATUS_ACTIVE + "'";

		List specimenList = (List) executeHqlQuery(dao, hql);
		if ((specimenList != null) && (specimenList).size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		SpecimenCollectionGroup group = (SpecimenCollectionGroup) obj;

		// Added by Ashish

		if (group == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg", "SpecimenCollectionGroup"));
		}

		Validator validator = new Validator();
		String message = "";

		if (group.getCollectionProtocolRegistration() == null)
		{
			message = ApplicationProperties.getValue("errors.specimenCollectionGroup.collectionprotocolregistration");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (group.getCollectionProtocolRegistration().getCollectionProtocol() == null
				|| group.getCollectionProtocolRegistration().getCollectionProtocol().getId() == null)
		{
			message = ApplicationProperties.getValue("errors.specimenCollectionGroup.collectionprotocol");
			throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
		}

		if ((group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier() == null && (group.getCollectionProtocolRegistration()
				.getParticipant() == null || group.getCollectionProtocolRegistration().getParticipant().getId() == null)))
		{
			throw new DAOException(ApplicationProperties.getValue("errors.collectionprotocolregistration.atleast"));
		}

		if (group.getSpecimenCollectionSite() == null || group.getSpecimenCollectionSite().getId() == null
				|| group.getSpecimenCollectionSite().getId() == 0)
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.site");
			throw new DAOException(ApplicationProperties.getValue("errors.item.invalid", message));
		}

		// Check what user has selected Participant Name / Participant Number

		// if participant name field is checked.
		// if(group.getCollectionProtocolRegistration().getParticipant().getId()
		// == -1)
		// {
		// message =
		// ApplicationProperties.getValue("specimenCollectionGroup.protocoltitle");
		// throw new
		// DAOException(ApplicationProperties.getValue("errors.item.required",message));
		//				
		// String message =
		// ApplicationProperties.getValue("specimenCollectionGroup.collectedByParticipant");
		// throw new DAOException("errors.item.selected", new
		// String[]{message});
		//				
		// }

		// if(!validator.isValidOption(group.getCollectionProtocolRegistration().getProtocolParticipantIdentifier()))
		// {
		// String message =
		// ApplicationProperties.getValue("specimenCollectionGroup.collectedByProtocolParticipantNumber");
		// throw new DAOException("errors.item.selected", new
		// String[]{message});
		//				
		// }

		if (validator.isEmpty(group.getName()))
		{
			if ((Constants.ADD.equals(operation) && !edu.wustl.catissuecore.util.global.Variables.isSpecimenCollGroupLabelGeneratorAvl)
					|| Constants.EDIT.equals(operation))
			{
				message = ApplicationProperties.getValue("specimenCollectionGroup.groupName");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
			}
		}

		// Mandatory Field : Study Calendar event point
		if (group.getCollectionProtocolEvent() == null || group.getCollectionProtocolEvent().getId() == null)
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.studyCalendarEventPoint");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// Mandatory Field : clinical Diagnosis
		if (validator.isEmpty(group.getClinicalDiagnosis()))
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.clinicalDiagnosis");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// Mandatory Field : clinical Status
		if (validator.isEmpty(group.getClinicalStatus()))
		{
			message = ApplicationProperties.getValue("specimenCollectionGroup.clinicalStatus");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// Condition for medical Record Number.

		// END

		List clinicalDiagnosisList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_DIAGNOSIS, null);
		if (!Validator.isEnumeratedValue(clinicalDiagnosisList, group.getClinicalDiagnosis()))
		{
			throw new DAOException(ApplicationProperties.getValue("spg.clinicalDiagnosis.errMsg"));
		}

		// NameValueBean undefinedVal = new
		// NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
		List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CLINICAL_STATUS, null);
		if (!Validator.isEnumeratedValue(clinicalStatusList, group.getClinicalStatus()))
		{
			throw new DAOException(ApplicationProperties.getValue("collectionProtocol.clinicalStatus.errMsg"));
		}

		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(group.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, group.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		//Bug #7808
		if (!Validator.isEnumeratedValue(Constants.SCG_COLLECTION_STATUS_VALUES, group.getCollectionStatus()))
		{
			throw new DAOException(ApplicationProperties.getValue("specimencollectiongroup.collectionStatus.errMsg"));
		}

		// check the activity status of all the specimens associated to the
		// Specimen Collection Group
		if (group.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
		{
			try
			{

				boolean isSpecimenExist = (boolean) isSpecimenExists(obj, dao, (Long) group.getId());
				if (isSpecimenExist)
				{
					throw new DAOException(ApplicationProperties.getValue("specimencollectiongroup.specimen.exists"));
				}

			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		/*
		 * Bug ID: 4165 Patch ID: 4165_11 Description: Validation added to check
		 * incorrect events added through API
		 */
		// Events Validation
		if (group.getSpecimenEventParametersCollection() != null && !group.getSpecimenEventParametersCollection().isEmpty())
		{
			Iterator specimenEventCollectionIterator = group.getSpecimenEventParametersCollection().iterator();
			while (specimenEventCollectionIterator.hasNext())
			{
				Object eventObject = specimenEventCollectionIterator.next();
				EventsUtil.validateEventsObject(eventObject, validator);
			}
		}
		else
		{
			throw new DAOException(ApplicationProperties.getValue("error.specimenCollectionGroup.noevents"));
		}

		return true;
	}

	public String getPageToShow()
	{
		return new String();
	}

	public List getMatchingObjects()
	{
		return new ArrayList();
	}

	public int getNextGroupNumber() throws DAOException
	{
		String sourceObjectName = "CATISSUE_SPECIMEN_COLL_GROUP";
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_IDENTIFIER"};
		return Utility.getNextUniqueNo(sourceObjectName, selectColumnName);
	}

	/**
	 * Patch Id : FutureSCG_8 Description : method to get SCGTree ForCPBasedView
	 */
	/**
	 * Creates tree which consists of SCG nodes and specimen nodes under each
	 * SCG if available. For a CPR if there is any SCG created those are shown
	 * with its details like '<# event day>_<Event point label>_<SCG_recv_date>'.
	 * When user clicks on this node ,Edit SCG page will be shown on right side
	 * panel of the screen, where now user can edit this SCG. But if there are
	 * no SCGs present for a CPR , a future(dummy) SCG is shown in tree as '<#
	 * event day>_<Event point label>'. When user clicks on this node , Add SCG
	 * page will be shown on right side panel of the screen, where now user can
	 * actually add new SCG and specimens for this CPR.
	 * 
	 * @param cpId
	 *            id of collection protocol
	 * @param participantId
	 *            id of participant
	 * @return Vector tree data structure
	 * @throws DAOException
	 *             daoException
	 * @throws ClassNotFoundException
	 *             classNotFoundException
	 */
	Integer offsetForCPOrEvent = null;

	Integer offsetForArmCP = null;

	public String getSCGTreeForCPBasedView(Long cpId, Long participantId) throws DAOException, ClassNotFoundException
	{

		long startTime = System.currentTimeMillis();
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			Date regDate = null;
			Integer offset = null;
			CollectionProtocolRegistration cpr = null;
			Logger.out.debug("Start of getSCGTreeForCPBasedView");
			String hql1 = "select cpr.id,cpr.registrationDate,cpr.offset from " + CollectionProtocolRegistration.class.getName()
					+ " as cpr where cpr.collectionProtocol.id = " + cpId.toString() + " and cpr.participant.id = " + participantId.toString();
			List list = executeQuery(hql1);
			if (list != null && list.size() > 0)
			{
				Object[] obj = (Object[]) list.get(0);
				cpr = new CollectionProtocolRegistration();
				cpr.setId((Long) obj[0]);
				cpr.setRegistrationDate((Date) obj[1]);
				cpr.setOffset((Integer) obj[2]);

			}

			StringBuffer xmlString = new StringBuffer();
			xmlString.append("<node>");
			childCPtree(xmlString, participantId, cpr, cpId);
			xmlString.append("</node>");

			long endTime = System.currentTimeMillis();
			Logger.out.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB (SpecimenCollectionGroupBizlogic)-  : " + (endTime - startTime));
			return xmlString.toString();
		}
		catch (DAOException e)
		{
			Logger.out.error(e.getMessage(), e);
			return null;
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
				return null;
			}
		}
	}

	/**
	 * get all the associated scg and specimens to cp and participant
	 * @param xmlString
	 * @param participantId
	 * @param collectionProtocolRegistration
	 * @param collectionProtocol
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public void childCPtree(StringBuffer xmlString, Long participantId, CollectionProtocolRegistration collectionProtocolRegistration, Long cpId)
			throws DAOException, ClassNotFoundException
	{
		//done
		Date evtLastDate = SCGTreeFrCPBasedView(xmlString, collectionProtocolRegistration);

		String hql = "select  cp." + Constants.CHILD_COLLECTION_PROTOCOL_COLLECTION + " from " + CollectionProtocol.class.getName()
				+ " as cp where cp.id= " + cpId.toString();

		List cpchildList = executeQuery(hql);
		CollectionProtocolSeqComprator comparator = new CollectionProtocolSeqComprator();
		Collections.sort(cpchildList, comparator);

		if (cpchildList.size() != 0)
		{
			for (int count = 0; count < cpchildList.size(); count++)
			{
				CollectionProtocol colProt = (CollectionProtocol) cpchildList.get(count);

				String dispName = colProt.getShortTitle();
				Date participantRegDate = getPartRegDateForCP(colProt, participantId, collectionProtocolRegistration.getRegistrationDate(),
						evtLastDate, collectionProtocolRegistration.getOffset());
				String anticipatoryDate = "";
				if (participantRegDate != null)
				{
					//bug no:6526 date format changed to mm-dd-yyyy
					anticipatoryDate = Utility.parseDateToString(participantRegDate, "MM-dd-yyyy");
					dispName = colProt.getShortTitle() + ":" + anticipatoryDate;

				}
				CollectionProtocolRegistration cpr = getCollectionProtocolReg(colProt, participantId);
				String participantRegStatus = "Pending";
				if (cpr != null)
					participantRegStatus = "Registered";

				xmlString.append("<node id= \"" + Constants.SUB_COLLECTION_PROTOCOL + "_" + colProt.getId() + "\" " + "name=\"" + dispName + "\" "
						+ "toolTip=\"" + colProt.getTitle() + "\" " + "type=\"" + colProt.getType() + "\" " + "cpType=\"" + colProt.getType() + "\" "
						+ "regDate=\"" + anticipatoryDate + "\" " + "participantRegStatus=\"" + participantRegStatus + "\">");
				if (cpr != null)
				{
					if (cpr.getOffset() != null)
						addOffset(cpr.getOffset());
				}
				childCPtree(xmlString, participantId, collectionProtocolRegistration, colProt.getId());
				xmlString.append("</node>");
			}

		}

	}

	/**
	 * get CollectionProtocolEvents from the collection of SCG 
	 * get all the SCG associated to CPR,get the list of CollectionProtocolEvents from SCG
	 * Created a map having key as id of CollectionProtocolEvent and value as the list of scg 
	 * assosiated to it.
	 * @param xmlString
	 * @param collectionProtocol
	 * @param collectionProtocolRegistration
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public Date SCGTreeFrCPBasedView(StringBuffer xmlString, CollectionProtocolRegistration collectionProtocolRegistration) throws DAOException,
			ClassNotFoundException
	{
		/** 
		 * replaced get SCGColelction call with HQL.  
		 */
		String hql = "select scg.id,scg.name,scg.activityStatus,scg.collectionStatus,scg.offset,"
				+ "scg.collectionProtocolEvent.id,scg.collectionProtocolEvent.studyCalendarEventPoint,"
				+ "scg.collectionProtocolEvent.collectionPointLabel from " + SpecimenCollectionGroup.class.getName()
				+ " as scg where scg.collectionProtocolRegistration.id = " + collectionProtocolRegistration.getId() + " and scg.activityStatus <> '"
				+ Constants.ACTIVITY_STATUS_DISABLED + "' order by scg.collectionProtocolEvent.studyCalendarEventPoint";

		List list = executeQuery(hql);

		/**
		 * Map to store key as event id and value as collectionprotocolevt object
		 */
		Map<Long, CollectionProtocolEvent> collectionProtocolEventsIdMap = new HashMap<Long, CollectionProtocolEvent>();
		/**
		 * Iterate and create SCG and CollectionProtocolEvent objects
		 * After this loop a collectionProtocolEventsIdMap will populate with valus like
		 * key = event id and value = CollectionProtocolEvent object with respective SCGCollection
		 */
		for (int i = 0; i < list.size(); i++)
		{
			Object[] obj1 = (Object[]) list.get(i);
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			scg.setId((Long) obj1[0]);
			scg.setName((String) obj1[1]);
			scg.setActivityStatus((String) obj1[2]);
			scg.setCollectionStatus((String) obj1[3]);
			scg.setOffset((Integer) obj1[4]);

			CollectionProtocolEvent cpe = new CollectionProtocolEvent();
			cpe.setId((Long) obj1[5]);
			cpe.setStudyCalendarEventPoint((Double) obj1[6]);
			cpe.setCollectionPointLabel((String) obj1[7]);

			CollectionProtocolEvent tempCpe = collectionProtocolEventsIdMap.get(cpe.getId());

			if (tempCpe == null)
			{
				Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>();
				specimenCollectionGroupCollection.add(scg);
				cpe.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
				collectionProtocolEventsIdMap.put(cpe.getId(), cpe);

			}
			else
			{
				tempCpe.getSpecimenCollectionGroupCollection().add(scg);
			}
			scg.setCollectionProtocolEvent(cpe);
		}

		/**
		 * Get the distinct collectionprotocolevnt object from the Map
		 * Sort the Event objects
		 */

		List collectionProtocolEvents = new ArrayList(collectionProtocolEventsIdMap.values());
		CollectionProtocolEventComparator comparator = new CollectionProtocolEventComparator();
		Collections.sort(collectionProtocolEvents, comparator);

		/**
		 * Iterate over CPE obects
		 * Get SCGCollection
		 * Sort SCG list of the CPE.
		 * Create XML node
		 */
		Iterator collectionProtocolEventsItr = collectionProtocolEvents.iterator();
		Date eventLastDate = new Date();
		while (collectionProtocolEventsItr.hasNext())
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) collectionProtocolEventsItr.next();
			List scgList = new ArrayList<SpecimenCollectionGroup>(collectionProtocolEvent.getSpecimenCollectionGroupCollection());
			DomainBeanIdentifierComparator idComparator = new DomainBeanIdentifierComparator();
			Collections.sort(scgList, idComparator);
			if (scgList != null && !scgList.isEmpty())
			{
				eventLastDate = createTreeNodeForExistngSCG(xmlString, collectionProtocolEvent.getStudyCalendarEventPoint(), collectionProtocolEvent
						.getCollectionPointLabel(), scgList, collectionProtocolRegistration.getRegistrationDate(), collectionProtocolRegistration
						.getOffset());
			}
		}
		return eventLastDate;
	}

	/**
	 * Get the CollectionProtocolRegistration associated to CP and participant. 
	 * @param colProt
	 * @param participantId
	 * @return
	 */
	private CollectionProtocolRegistration getCollectionProtocolReg(CollectionProtocol colProt, Long participantId)
	{
		CollectionProtocolRegistration collectionProtocolRegistration = null;

		Iterator collectionProtocolRegistrationCollItr = colProt.getCollectionProtocolRegistrationCollection().iterator();
		while (collectionProtocolRegistrationCollItr.hasNext())
		{
			collectionProtocolRegistration = (CollectionProtocolRegistration) collectionProtocolRegistrationCollItr.next();
			if (collectionProtocolRegistration.getParticipant().getId().equals(participantId))
			{
				return collectionProtocolRegistration;
			}
		}
		return collectionProtocolRegistration;

	}

	/**
	 * @param colProt
	 * @param participantId
	 * @param parentRegDate
	 * @param eventLastDate
	 * @param parentOffset
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private Date getPartRegDateForCP(CollectionProtocol colProt, Long participantId, Date parentRegDate, Date eventLastDate, Integer parentOffset)
			throws DAOException, ClassNotFoundException
	{
		Date participantRegDate = null;
		//		bug 6560 fix

		Double eventPoint = colProt.getStudyCalendarEventPoint();
		CollectionProtocolRegistration cpr = getCollectionProtocolReg(colProt, participantId);

		if (cpr == null)
		{
			if (colProt.getSequenceNumber() != null)
			{
				int noOfDaysToAdd = 0;
				if (eventPoint != null)
				{
					if (parentRegDate != null)
					{
						noOfDaysToAdd = eventPoint.intValue();
						if (offsetForCPOrEvent != null)
							noOfDaysToAdd = noOfDaysToAdd + offsetForCPOrEvent.intValue();
						//This check is beaause child cp take there anticipated dates according to there parent cp and if in parent date already offset is captured then no need ot take that offeet in child
						if (parentOffset != null)
							noOfDaysToAdd = noOfDaysToAdd - parentOffset.intValue();
						participantRegDate = Utility.getNewDateByAdditionOfDays(parentRegDate, noOfDaysToAdd);

					}
					else if (eventLastDate != null)
						participantRegDate = Utility.getNewDateByAdditionOfDays(eventLastDate, Constants.DAYS_TO_ADD_CP);
					else if (parentRegDate != null)
						participantRegDate = Utility.getNewDateByAdditionOfDays(parentRegDate, Constants.DAYS_TO_ADD_CP);
				}
			}
		}
		else
		{
			participantRegDate = cpr.getRegistrationDate();
		}

		return participantRegDate;
	}

	/**
	 * @param specimenEventParaCol
	 * @return
	 */
	private Collection getCollectionEventParameters(Collection specimenEventParaCol)
	{
		Collection collectionEventParameters = new ArrayList();

		Iterator specimenEventParaColItr = specimenEventParaCol.iterator();
		while (specimenEventParaColItr.hasNext())
		{
			SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) specimenEventParaColItr.next();
			if (specimenEventParameters instanceof CollectionEventParameters)
			{
				collectionEventParameters.add(specimenEventParameters);
			}

		}

		return collectionEventParameters;
	}

	/**
	 * Create and add node for SCg
	 * @param xmlString
	 * @param eventPoint
	 * @param collectionPointLabel
	 * @param scgList
	 * @param regDate
	 * @param parentOffset
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private Date createTreeNodeForExistngSCG(StringBuffer xmlString, Double eventPoint, String collectionPointLabel, List scgList, Date regDate,
			Integer parentOffset) throws DAOException, ClassNotFoundException
	{
		Date eventLastDate = null;

		for (int i = 0; i < scgList.size(); i++)
		{
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) scgList.get(i);

			String scgNodeLabel = "";
			// String scgActivityStatus = (String) obj1[2];

			/**
			 * Name: Vijay Pande Reviewer Name: Aarti Sharma recievedEvent
			 * related to scg is trieved from db and, proper receivedDate and
			 * scgNodeLabel are set to set toolTip of the node
			 */
			if (specimenCollectionGroup.getOffset() != null)
			{
				addOffset(specimenCollectionGroup.getOffset());
			}
			String receivedDate = "";
			if (specimenCollectionGroup.getId() != null && specimenCollectionGroup.getId() > 0)
			{

				Collection eventsColl = getCollectionEventParameters(specimenCollectionGroup.getSpecimenEventParametersCollection());

				if (eventsColl != null && !eventsColl.isEmpty())
				{
					receivedDate = "";
					Iterator iter = eventsColl.iterator();
					while (iter.hasNext())
					{
						CollectionEventParameters collectionEventParameters = (CollectionEventParameters) iter.next();
						eventLastDate = collectionEventParameters.getTimestamp();
						//bug no:6526 date format changed to mm-dd-yyyy
						receivedDate = Utility.parseDateToString(collectionEventParameters.getTimestamp(), "MM-dd-yyyy");
						scgNodeLabel = "T" + eventPoint + ": " + collectionPointLabel + ": " + receivedDate;
						break;
					}
				}
				if (scgNodeLabel.equalsIgnoreCase("") && receivedDate.equalsIgnoreCase(""))
				{
					int noOfDaysToAdd = 0;
					if (eventPoint != null)
						noOfDaysToAdd += eventPoint.intValue();
					if (offsetForCPOrEvent != null)
					{
						noOfDaysToAdd += offsetForCPOrEvent.intValue();
					}
					// This check is beaause event take there anticipated dates according to there parent and if in parent date already offset is captured then no need ot take that offeet in child
					if (parentOffset != null)
					{
						noOfDaysToAdd -= parentOffset.intValue();
					}

					Date evtDate = Utility.getNewDateByAdditionOfDays(regDate, noOfDaysToAdd);
					eventLastDate = evtDate;
					//bug no:6526 date format changed to mm-dd-yyyy
					receivedDate = Utility.parseDateToString(evtDate, "MM-dd-yyyy");
					// String toolTipText = scgNodeLabel+ ": "+scgName;//
					scgNodeLabel = "T" + eventPoint + ": " + collectionPointLabel + ": " + receivedDate;
				}
			}
			String toolTipText = getToolTipText(eventPoint.toString(), collectionPointLabel, receivedDate);

			// creating SCG node
			xmlString.append("<node id= \"" + Constants.SPECIMEN_COLLECTION_GROUP + "_" + specimenCollectionGroup.getId().toString() + "\" "
					+ "name=\"" + scgNodeLabel + "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN_COLLECTION_GROUP + "\" "
					+ "scgCollectionStatus=\"" + specimenCollectionGroup.getCollectionStatus() + "\" " + "evtDate=\"" + receivedDate + "\">");

			// Adding specimen Nodes to SCG tree
			addSpecimenNodesToSCGTree(xmlString, specimenCollectionGroup);
			xmlString.append("</node>");

		}
		return eventLastDate;
	}

	/**
	 * 1. Get all specimens attributes of given scg id
	 * 2. Iterate over a specimen attribute
	 * 		2.1 create specimen object
	 *    	2.3 Add specimen in the TreeMap where key is parentId and value is List of SpeicmenObjects
	 * 3. Get List of Speicmen Object from TreeMap where parentID =0 i.e. speicmen having no parent.
	 * 4. Iterate over above list
	 * 		4.1 Call createXML of specimen
	 * 			4.1.1 Generate xml of specimen
	 * 			4.1.2 Get childSpeicmen from TreeMap
	 * 			4.1.3 recursively call createXML on childSpecimens 
	 * @param xmlString
	 * @param specimenCollectionGroup
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void addSpecimenNodesToSCGTree(StringBuffer xmlString, SpecimenCollectionGroup specimenCollectionGroup) throws DAOException,
			ClassNotFoundException
	{
		/**
		 * TreeMap to store key as parentSpecimeniD and Child as List of child specimens
		 */
		TreeMap<Long, List<Specimen>> specimenChildrenMap = new TreeMap<Long, List<Specimen>>();

		String hql = "select sp.id,sp.label,sp.parentSpecimen.id,sp.activityStatus,sp.specimenType,sp.collectionStatus	from "
				+ Specimen.class.getName() + " as sp where sp.specimenCollectionGroup.id = " + specimenCollectionGroup.getId()
				+ " and sp.activityStatus <> '" + Constants.ACTIVITY_STATUS_DISABLED + "' order by sp.id";

		List list = executeQuery(hql);

		/**
		 * Iterate over a resultlist the  output after this iteration is a TreeMap populated with key = parentSpecimenID
		 * and value = List of SpecimenObject which is child of Parentkey
		 * IF specimen has no parent then parent key is store as Long(0)
		 */
		for (int i = 0; i < list.size(); i++)
		{
			Object[] obj = (Object[]) list.get(i);
			Specimen specimen = new Specimen();
			specimen.setId((Long) obj[0]);
			specimen.setLabel((String) obj[1]);
			specimen.setActivityStatus((String) obj[3]);
			specimen.setSpecimenType((String) obj[4]);
			specimen.setCollectionStatus((String) obj[5]);
			Long parentSpecimenId = (Long) obj[2];

			if (parentSpecimenId == null)
			{
				parentSpecimenId = new Long(0);
			}

			List<Specimen> l = specimenChildrenMap.get(parentSpecimenId);
			if (l == null)
			{
				l = new ArrayList<Specimen>();
				l.add(specimen);
				specimenChildrenMap.put(parentSpecimenId, l);
			}
			else
			{
				l.add(specimen);
			}
		}
		/**
		 * Start creating a node of those specimns having no parent 
		 */
		if (!specimenChildrenMap.keySet().isEmpty())
		{
			List<Specimen> specList = specimenChildrenMap.get(new Long(0));
			SpecimenComparator comparator = new SpecimenComparator();
			Collections.sort(specList,comparator);
			for (Specimen spec : specList)
			{
				createSpecimenXML(xmlString, spec, specimenChildrenMap);
			}
		}

	}

	private void createSpecimenXML(StringBuffer xmlString, Specimen specimen, TreeMap<Long, List<Specimen>> specimenChildrenMap) throws DAOException,
			ClassNotFoundException
	{
		Long spId = specimen.getId();
		String spLabel1 = specimen.getLabel();
		// String spActivityStatus = (String) specimens[3];
		String type = specimen.getSpecimenType();
		String spCollectionStatus = specimen.getCollectionStatus();

		// Added later for toolTip text for specimens
		String toolTipText = "Label : " + spLabel1 + " ; Type : " + type;

		//List collectionEventPara = (List)getCollectionEventParameters(specimen.getSpecimenEventCollection());
		String hqlCon = "select colEveParam.container from " + CollectionEventParameters.class.getName()
				+ " as colEveParam where colEveParam.specimen.id = " + spId;

		List container = executeQuery(hqlCon);
		for (int k = 0; k < container.size(); k++)
		{
			//CollectionEventParameters collectionEventParameters = (CollectionEventParameters)collectionEventPara.get(k);
			String con = (String) container.get(k);
			toolTipText += " ; Container : " + con;
		}
		xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" " + "toolTip=\""
				+ toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\"" + spCollectionStatus + "\">");

		//Collection childrenSpecimen = specimen.getChildSpecimenCollection();

		// Get childrens of curretn specimen from TreeMap
		List<Specimen> childrenSpecimen = (List) specimenChildrenMap.get(spId);
		if (childrenSpecimen != null)
		{
			SpecimenComparator comparator = new SpecimenComparator();
			Collections.sort(childrenSpecimen, comparator);
			for (Specimen childSpecimen : childrenSpecimen)
			{
				if (!Constants.ACTIVITY_STATUS_DISABLED.equals(childSpecimen.getActivityStatus()))
					createSpecimenXML(xmlString, childSpecimen, specimenChildrenMap);
			}

		}
		xmlString.append("</node>");
	}

	/**
	 * Add specimens to xmlString
	 * @param xmlString
	 * @param specimenCollectionGroup
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void addSpecNodesToSCGTree(StringBuffer xmlString, SpecimenCollectionGroup specimenCollectionGroup) throws DAOException,
			ClassNotFoundException
	{
		/**
		 * kalpana Bug #5906 Reviewer : vaishali To display the children
		 * specimens of the specimen in acending order. changed to order by
		 * specimen label.
		 */

		Map childSpecimenMap = new HashMap();

		List specimenList = new ArrayList();
		specimenList.addAll(specimenCollectionGroup.getSpecimenCollection());
//		Mandar 27aug08--start
		SpecimenComparator comparator = new SpecimenComparator();
		Collections.sort(specimenList,comparator);
//		Mandar 27Aug08--end		

		List finalList = new ArrayList();
		List childrenList = new ArrayList();

		// Stack
		Stack spStack = new Stack();

		// Here iterate over specimenList to separate out Specimens and child
		// Specimens
		for (int i = 0; i < specimenList.size(); i++)
		{
			Specimen specimen = (Specimen) specimenList.get(i);

			if (!Constants.ACTIVITY_STATUS_DISABLED.equals(specimen.getActivityStatus()))
			{

				// Long peekSpecimenId = null;
				if (specimen.getId() != null)
				{
					if (specimen.getParentSpecimen() == null)
					{
						// if parentSpecimenId is null then it's going to be parent
						// specimen
						finalList.add(specimenList.get(i));
					}
					else
					{
						childrenList.add(specimenList.get(i));
					}
				}
			}
		}

		/**
		 * kalpana Bug #5906 Reviewer : vaishali To display the children
		 * specimens of the specimen in acending order. Map hold parent specimen
		 * id and the count of all the childern under that parent specimen.
		 */

		Map countOfChildSpecimenMap = new HashMap<Long, Integer>();
		Integer countOfChildSpecimen = 1;

		for (int i = 0; i < childrenList.size(); i++)
		{
			Specimen specimen = (Specimen) childrenList.get(i);

			/*
			 * fetching the count of children specimen if it's parent specimen
			 * Id is there in the Map else setting it to 1.
			 */

			if (countOfChildSpecimenMap.containsKey(specimen.getParentSpecimen().getId()))
			{
				countOfChildSpecimen = (Integer) countOfChildSpecimenMap.get(specimen.getParentSpecimen().getId());
			}
			else
			{
				countOfChildSpecimen = 1;
			}

			for (int j = 0; j < finalList.size(); j++)
			{
				Specimen specimenParent = (Specimen) finalList.get(j);

				// This if statement is not working.......convert Long objects
				// to long values
				if (specimen.getParentSpecimen().getId().longValue() == specimenParent.getId().longValue())
				{

					finalList.add(j + countOfChildSpecimen, childrenList.get(i));
					countOfChildSpecimen++;
					countOfChildSpecimenMap.put(specimen.getParentSpecimen().getId(), countOfChildSpecimen);

					/*
					 * kalpana Update the children specimen count of all the
					 * parent specimens accept the immediate parent
					 */
					updateChildSpecimenCount(finalList, (Specimen) specimenParent.getParentSpecimen(), countOfChildSpecimenMap, countOfChildSpecimen);
					break;
					// Here break is important ....once parent is found
				}
			}
		}

		// Here create XML String rep. of parent/child specimen tree from
		// finalList
		for (int i = 0; i < finalList.size(); i++)
		{

			Specimen specimen = (Specimen) finalList.get(i);
			Long spId = specimen.getId();
			Long parentId = null;
			if (specimen.getParentSpecimen() != null)
			{
				parentId = specimen.getParentSpecimen().getId();
			}
			String spLabel1 = specimen.getLabel();
			// String spActivityStatus = (String) specimens[3];
			String type = specimen.getSpecimenType();
			String spCollectionStatus = specimen.getCollectionStatus();

			// Added later for toolTip text for specimens
			String toolTipText = "Label : " + spLabel1 + " ; Type : " + type;

			List collectionEventPara = (List) getCollectionEventParameters(specimen.getSpecimenEventCollection());
			/*String hqlCon = "select colEveParam.container from " + CollectionEventParameters.class.getName()
			 + " as colEveParam where colEveParam.specimen.id = " + spId;

			 List container = executeQuery(hqlCon);
			 */for (int k = 0; k < collectionEventPara.size(); k++)
			{
				CollectionEventParameters collectionEventParameters = (CollectionEventParameters) collectionEventPara.get(k);
				String con = (String) collectionEventParameters.getContainer();
				toolTipText += " ; Container : " + con;
			}

			// If parent id is null, then it's going to be a new specimens
			if (parentId == null)
			{
				while (!spStack.isEmpty()) // closes all specimens node
				// when a new specimens starts
				{
					spStack.pop();
					xmlString.append("</node>");
				}

				if (i == finalList.size() - 1) // If last element & parent
				// specimen
				{
					xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" " + "toolTip=\""
							+ toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\"" + spCollectionStatus + "\">");
					xmlString.append("</node>");
				}
				else
				// If not the last element and parent specimen
				{
					xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" " + "toolTip=\""
							+ toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\"" + spCollectionStatus + "\">");
					spStack.push((Specimen) finalList.get(i));
				}
			}

			else
			{
				Specimen previousSp = (Specimen) finalList.get(i - 1);
				Long previousSpId = previousSp.getId();

				// If immediate prevoius node is parent of current node
				if (parentId.longValue() == previousSpId.longValue())
				{
					if (i != finalList.size() - 1) // if not the last element
					// in specimen list
					{
						Specimen nextSp = (Specimen) finalList.get(i + 1); // Getting
						// next
						// sp
						Long nextSpPid = null;
						if (nextSp.getParentSpecimen() != null)
							nextSpPid = nextSp.getParentSpecimen().getId();

						if (nextSpPid != null)
						{
							// Check if current specimen have children
							// specimens
							if (spId.longValue() == nextSpPid.longValue())
							{
								xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
										+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
										+ spCollectionStatus + "\">");
								spStack.push((Specimen) finalList.get(i));
							}
							else
							// if current specimen doesn't have
							// children specimens
							{
								xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
										+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
										+ spCollectionStatus + "\"></node> ");
							}
						}
						else
						{
							xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
									+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
									+ spCollectionStatus + "\"></node> ");
						}
					}

					else
					// last element in specimen List
					{
						xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
								+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
								+ spCollectionStatus + "\"></node> ");
						while (!spStack.isEmpty())
						{
							spStack.pop();
							xmlString.append("</node>");
						}
					}
				}
				else
				// current node has parent but not the immediate
				// prevoius node. Parent may be on the stack
				{
					Long peekSpecimenId2 = null;
					do
					{
						Specimen peekSp = (Specimen) spStack.peek(); // Retrieving
						// the
						// peek
						// element
						// at
						// stack
						Long peekSpId = (Long) peekSp.getId();
						if (parentId.longValue() == peekSpId.longValue())// If
						// current
						// node
						// has
						// parent
						// as
						// peek
						// (last)
						// element
						// at
						// stack
						{
							if (i != finalList.size() - 1) // if not the last
							// element
							{
								Specimen nextSp = (Specimen) finalList.get(i + 1);

								Long nextSpPid = null;
								if (nextSp.getParentSpecimen() != null)
									nextSpPid = nextSp.getParentSpecimen().getId();

								// Here nextSpPid may be null
								if (nextSpPid != null)
								{
									// If it has children
									if (spId.longValue() == nextSpPid.longValue())
									{
										xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1
												+ "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" "
												+ "collectionStatus=\"" + spCollectionStatus + "\">");
										spStack.push((Specimen) finalList.get(i));
									}
									else
									// if it doesn't has children
									{
										xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1
												+ "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" "
												+ "collectionStatus=\"" + spCollectionStatus + "\"></node> ");
										break; // Note this break is imp
									}

								}
								else
								// next node parent id is null then
								// , then it's parent specimen
								{
									xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1
											+ "\" " + "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" "
											+ "collectionStatus=\"" + spCollectionStatus + "\"></node> ");
									break;
								}

							}
							else
							// If last element
							{
								xmlString.append("<node id=\"" + Constants.SPECIMEN + "_" + spId.toString() + "\" " + "name=\"" + spLabel1 + "\" "
										+ "toolTip=\"" + toolTipText + "\" " + "type=\"" + Constants.SPECIMEN + "\" " + "collectionStatus=\""
										+ spCollectionStatus + "\"></node> ");
								while (!spStack.isEmpty())
								{
									spStack.pop();
									xmlString.append("</node>");
								}

								break; // If last element,& stack is empty,
								// simply break
							}

						}
						else
						// If node has parent but at peek element of
						// stack
						{
							// So iterate till, node has parent element at
							// stack
							Long peekSpecimenId1 = null;
							do
							{

								if (!spStack.isEmpty())
								{
									spStack.pop();
									xmlString.append("</node>");

									Specimen peekSp1 = null;
									if (!spStack.isEmpty())
									{
										peekSp1 = (Specimen) spStack.peek(); // Retrieving
									}
									// the
									// peek
									// element
									// at
									// stack
									if (peekSp1 != null)
										peekSpecimenId1 = (Long) peekSp1.getId();
								}

							}
							while (peekSpecimenId1 != null && parentId.longValue() != peekSpecimenId1.longValue() && !spStack.isEmpty());
						}

						if (!spStack.empty())
						{
							Specimen peekSp2 = (Specimen) spStack.peek(); // Retrieving
							// the
							// peek
							// element
							// at
							// stack
							peekSpecimenId2 = (Long) peekSp2.getId();
						}
					}
					while (peekSpecimenId2 != null && parentId.longValue() == peekSpecimenId2.longValue() && !spStack.empty());

				}

			}

		}
	}

	/**
	 * kalpana Bug #5906 Reviewer : vaishali Update the children specimen count
	 * of all the parent specimens accept the immediate parent
	 */

	private void updateChildSpecimenCount(List finalList, Specimen parentSpecimen, Map countOfChildSpecimenMap, Integer countOfChildSpecimen)
	{

		for (int j = 0; j < finalList.size(); j++)
		{
			Specimen specimen = (Specimen) finalList.get(j);
			Long specimenId = (Long) specimen.getId();
			Long parentSpecimenId = null;

			if (specimen.getParentSpecimen() != null)
			{
				parentSpecimenId = (Long) specimen.getParentSpecimen().getId();

				if (specimenId != null && parentSpecimen != null && specimenId.equals(parentSpecimen.getId()))
				{
					if (countOfChildSpecimenMap.containsKey(specimenId))
					{
						Integer newChildCount = (Integer) countOfChildSpecimenMap.get(specimenId);
						newChildCount = newChildCount + 1;
						countOfChildSpecimenMap.put(specimenId, newChildCount);

						updateChildSpecimenCount(finalList, (Specimen) specimen.getParentSpecimen(), countOfChildSpecimenMap, countOfChildSpecimen);
						return;
					}
				}
			}

		}

	}

	private void addOffset(Integer offsetToAdd)
	{
		Integer offset = null;
		if (offsetForCPOrEvent != null)
		{
			offset = new Integer(offsetForCPOrEvent.intValue() + offsetToAdd.intValue());
			offsetForCPOrEvent = offset;
		}
		else
		{
			offset = new Integer(offsetToAdd.intValue());
			offsetForCPOrEvent = offset;
		}
	}

	private int getNoOfDaysAccordingToOffset(Integer offset, Integer offsetForCPOrEvent)
	{
		int noOfDays = 0;
		if (offsetForCPOrEvent != null && offset != null && offsetForCPOrEvent.intValue() > offset.intValue())
			noOfDays = offsetForCPOrEvent.intValue() - offset.intValue();

		if (offsetForCPOrEvent != null && offset == null)
			noOfDays = offsetForCPOrEvent.intValue();

		return noOfDays;
	}

	private List executeHqlQuery(DAO dao, String hql) throws DAOException, ClassNotFoundException
	{
		List list = dao.executeQuery(hql, null, false, null);
		return list;
	}

	/**
	 * This function sets the data in QuertTreeNodeData object adds in a list of
	 * these nodes.
	 * 
	 * @param identifier
	 * @param objectName
	 * @param displayName
	 * @param parentIdentifier
	 * @param parentObjectName
	 * @param combinedParentIdentifier
	 * @param combinedParentObjectName
	 * @param activityStatus
	 * @param vector
	 */

	// Commented out as it is not required after Flex related chages
	/*
	 * private void setQueryTreeNode(String identifier, String objectName,
	 * String displayName, String parentIdentifier, String parentObjectName,
	 * String combinedParentIdentifier, String combinedParentObjectName, String
	 * activityStatus, String toolTipText, Vector vector) { if
	 * (!activityStatus.equals(Constants.ACTIVITY_STATUS_DISABLED)) {
	 * QueryTreeNodeData treeNode = new QueryTreeNodeData();
	 * treeNode.setIdentifier(identifier); treeNode.setObjectName(objectName);
	 * treeNode.setDisplayName(displayName);
	 * treeNode.setParentIdentifier(parentIdentifier);
	 * treeNode.setParentObjectName(parentObjectName);
	 * treeNode.setCombinedParentIdentifier(combinedParentIdentifier);
	 * treeNode.setCombinedParentObjectName(combinedParentObjectName);
	 * treeNode.setToolTipText(toolTipText); vector.add(treeNode); } }
	 */
	/**
	 * Patch Id : FutureSCG_14 Description : method to getToolTipText
	 */
	/**
	 * Generates tooltip for SCGs in c based views
	 * 
	 * @param eventDays
	 *            no of days
	 * @param collectionPointLabel
	 *            label of CPE
	 * @param receivedDate
	 *            received date for specimens
	 * @return String Tooltip text
	 */
	private static String getToolTipText(String eventDays, String collectionPointLabel, String receivedDate)
	{
		StringBuffer toolTipText = new StringBuffer();
		toolTipText.append("Event point : ");
		toolTipText.append(eventDays);
		toolTipText.append(";  Collection point label : ");
		toolTipText.append(collectionPointLabel);
		if (receivedDate != null)
		{
			toolTipText.append(";  Received date : ");
			toolTipText.append(receivedDate);
		}
		return toolTipText.toString();
	}

	// Mandar : 15-Jan-07 For Consent Tracking Withdrawal -------- start
	/*
	 * This method verifies and updates SCG and child elements for withdrawn
	 * consents
	 */
	private void verifyAndUpdateConsentWithdrawn(SpecimenCollectionGroup specimenCollectionGroup, SpecimenCollectionGroup oldspecimenCollectionGroup,
			DAO dao, SessionDataBean sessionDataBean) throws DAOException
	{
		Collection newConsentTierStatusCollection = specimenCollectionGroup.getConsentTierStatusCollection();
		Iterator itr = newConsentTierStatusCollection.iterator();
		while (itr.hasNext())
		{
			ConsentTierStatus consentTierStatus = (ConsentTierStatus) itr.next();
			if (consentTierStatus.getStatus().equalsIgnoreCase(Constants.WITHDRAWN))
			{
				long consentTierID = consentTierStatus.getConsentTier().getId().longValue();
				String cprWithdrawOption = specimenCollectionGroup.getConsentWithdrawalOption();
				ConsentUtil.updateSCG(specimenCollectionGroup, oldspecimenCollectionGroup, consentTierID, cprWithdrawOption, dao, sessionDataBean);
				// break;
			}
		}
	}

	public String retriveSCGNameFromSCGId(String id) throws DAOException
	{
		String scgName = "";
		String[] selectColumnName = {"name"};
		String[] whereColumnName = {"id"};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {Long.parseLong(id)};
		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		dao.openSession(null);
		List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), selectColumnName, whereColumnName, whereColumnCondition,
				whereColumnValue, null);
		if (scgList != null && !scgList.isEmpty())
		{

			scgName = (String) scgList.get(0);
		}

		dao.closeSession();
		return scgName;
	}

	public Map getSpecimenList(Long id) throws DAOException
	{

		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			Object object = dao.retrieve(SpecimenCollectionGroup.class.getName(), id);

			if (object != null)
			{
				SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) object;
				Collection specimenCollection = specimenCollectionGroup.getSpecimenCollection();
				return CollectionProtocolUtil.getSpecimensMap(specimenCollection, "E1");
			}
			else
			{
				return null;
			}
		}
		catch (DAOException exception)
		{
			throw exception;
		}
		finally
		{
			dao.commit();
			dao.closeSession();
		}
	}

	private void updateOffset(Integer offset, SpecimenCollectionGroup specimenCollectionGroup, SessionDataBean sessionDataBean, DAO dao)
			throws DAOException, UserNotAuthorizedException
	{
		//CollectionProtocolRegistration cpr = getCollectionProtocolRegForSCG(specimenCollectionGroup, dao);
		//specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
		CollectionProtocolEvent event = (CollectionProtocolEvent) specimenCollectionGroup.getCollectionProtocolEvent();
		CollectionProtocolRegistration cpr = specimenCollectionGroup.getCollectionProtocolRegistration();
		if (cpr != null)
		{
			String sourceObjName = SpecimenCollectionGroup.class.getName();
			String whereColName[] = {"collectionProtocolEvent.id", "collectionProtocolRegistration.id"};
			String whereColCond[] = {">", "="};
			Object whereColVal[] = {event.getId(), cpr.getId()};
			List specimenCollectionGroupCollection = (List) dao.retrieve(sourceObjName, null, whereColName, whereColCond, whereColVal,
					Constants.AND_JOIN_CONDITION);
			/*Collection specimenCollectionGroupCollection = (Collection) dao.retrieveAttribute(CollectionProtocolRegistration.class.getName(), cpr
			 .getId(), Constants.COLUMN_NAME_SCG_COLL);*/
			if (specimenCollectionGroupCollection != null && !specimenCollectionGroupCollection.isEmpty())
			{
				Iterator specimenCollectionGroupIterator = specimenCollectionGroupCollection.iterator();
				while (specimenCollectionGroupIterator.hasNext())
				{
					SpecimenCollectionGroup scg = (SpecimenCollectionGroup) specimenCollectionGroupIterator.next();
					//if(scg.getId().longValue() > specimenCollectionGroup.getId().longValue())
					//{
					if (scg.getOffset() != null)
						scg.setOffset(scg.getOffset() + offset);
					else
						scg.setOffset(offset);
					dao.update(scg, sessionDataBean, true, true, false);
					//}
				}
			}
		}

	}

	private void getDetailsOfCPRForSCG(CollectionProtocolRegistration cpr, DAO dao) throws DAOException
	{
		if (cpr != null && cpr.getId() != null)
		{
			String sourceObjName = CollectionProtocolRegistration.class.getName();
			String[] selectColName = {"participant", "collectionProtocol"};
			String[] whereColName = {"id"};
			String[] whereColCond = {"="};
			Object[] whereColVal = {cpr.getId()};

			List list = dao.retrieve(sourceObjName, selectColName, whereColName, whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
			if (list != null && !list.isEmpty())
			{
				Object[] obj = (Object[]) list.get(0);
				Participant participant = (Participant) obj[0];
				CollectionProtocol collectionProtocol = (CollectionProtocol) obj[1];
				cpr.setParticipant(participant);
				cpr.setCollectionProtocol(collectionProtocol);
			}
		}
	}

	public SpecimenCollectionGroup retrieveSCG(DAO dao, AbstractSpecimenCollectionGroup scg) throws DAOException
	{
		List scgList = null;
		SpecimenCollectionGroup absScg = null;
		if (scg.getId() != null)
		{
			absScg = (SpecimenCollectionGroup) dao.retrieve(SpecimenCollectionGroup.class.getName(), scg.getId());
		}
		else if (scg.getGroupName() != null)
		{
			scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.NAME, scg.getGroupName());
			if (scgList == null || scgList.isEmpty())
			{
				throw new DAOException("Failed to retrieve SCG, either Name or Identifier is required");
			}
			absScg = ((SpecimenCollectionGroup) (scgList.get(0)));
		}
		return absScg;
	}

	public CollectionProtocolRegistration chkParticipantRegisteredToCP(Long participantId, Long collectionProtocolId, String type)
			throws ClassNotFoundException, DAOException
	{
		//Date regDate = null;
		CollectionProtocolRegistration cpr = null;
		String cprHqlQuery = "select cpr.id,cpr.registrationDate,cpr.offset from " + CollectionProtocolRegistration.class.getName()
				+ " as cpr where cpr.participant.id = " + participantId.toString() + " and cpr.collectionProtocol.id = "
				+ collectionProtocolId.toString();
		List cprList = executeQuery(cprHqlQuery);
		if (cprList != null && !cprList.isEmpty())
		{
			cpr = new CollectionProtocolRegistration();
			Object[] obj = (Object[]) cprList.get(0);
			Date regDate = (Date) obj[1];
			cpr.setRegistrationDate(regDate);
			if (obj[2] != null)
			{
				Integer offset = (Integer) obj[2];
				//offsetForCPOrEvent = offset;
				/*if (!Constants.ARM_CP_TYPE.equals(type))
				 offsetForArmCP = offsetForCPOrEvent;*/
				cpr.setOffset(offset);
			}

			return cpr;
		}
		return null;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject)
	{
		String objectId = "";
		if (domainObject instanceof SpecimenCollectionGroup)
		{
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) domainObject;

			CollectionProtocolRegistration cpr = specimenCollectionGroup.getCollectionProtocolRegistration();
			CollectionProtocol cp = cpr.getCollectionProtocol();
			objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cp.getId();
		}
		return objectId;
	}

	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_SCG;
	}

	/**
	 * (non-Javadoc)
	 * @throws UserNotAuthorizedException 
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 * 
	 */
	public boolean isAuthorized(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean) throws UserNotAuthorizedException
	{
		boolean isAuthorized = false;
		String protectionElementName = null;

		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}

		//	Get the base object id against which authorization will take place 
		if (domainObject instanceof List)
		{
			List list = (List) domainObject;
			for (Object domainObject2 : list)
			{
				protectionElementName = getObjectId(dao, domainObject2);
			}
		}
		else
		{
			protectionElementName = getObjectId(dao, domainObject);
		}

		if (protectionElementName.equals(Constants.allowOperation))
		{
			return true;
		}
		//Get the required privilege name which we would like to check for the logged in user.
		String privilegeName = getPrivilegeName(domainObject);
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		//Checking whether the logged in user has the required privilege on the given protection element
		isAuthorized = privilegeCache.hasPrivilege(protectionElementName, privilegeName);

		if (isAuthorized)
		{
			return isAuthorized;
		}
		else
		// Check for ALL CURRENT & FUTURE CASE
		{
			String protectionElementNames[] = protectionElementName.split("_");

			Long cpId = Long.valueOf(protectionElementNames[1]);
			Set<Long> cpIdSet = new UserBizLogic().getRelatedCPIds(sessionDataBean.getUserId(), false);

			if (cpIdSet.contains(cpId))
			{
				throw Utility.getUserNotAuthorizedException(privilegeName, protectionElementName);
			}
			isAuthorized = edu.wustl.catissuecore.util.global.Utility.checkForAllCurrentAndFutureCPs(dao, privilegeName, sessionDataBean,
					protectionElementNames[1]);
		}
		if (!isAuthorized)
		{
			throw Utility.getUserNotAuthorizedException(privilegeName, protectionElementName);
		}
		return isAuthorized;
	}

	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.REGISTRATION+","+Permissions.READ_DENIED;
	}
	
	public boolean hasPrivilegeToView(String objName, Long identifier, SessionDataBean sessionDataBean)
	{
		return edu.wustl.catissuecore.util.global.Utility.hasPrivilegeToView(objName, identifier, sessionDataBean, getReadDeniedPrivilegeName());
	}

}