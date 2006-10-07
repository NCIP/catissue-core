/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	NewSpecimenBizLogicHDAO is used to add new specimen information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.QuantityInCount;
import edu.wustl.catissuecore.domain.QuantityInGram;
import edu.wustl.catissuecore.domain.QuantityInMicrogram;
import edu.wustl.catissuecore.domain.QuantityInMilliliter;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.integration.IntegrationManager;
import edu.wustl.catissuecore.integration.IntegrationManagerFactory;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class NewSpecimenBizLogic extends IntegrationBizLogic
{

	/**
	 * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		if (obj instanceof Map)
		{
			insertMultipleSpecimen((Map) obj, dao, sessionDataBean);
		}
		else
		{
			insertSingleSpecimen((Specimen) obj, dao, sessionDataBean, false);
		}
	}

	/**
	 * Insert multiple specimen into the data base.
	 * @param specimenList
	 * @param dao
	 * @param sessionDataBean
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void insertMultipleSpecimen(Map specimenMap, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Iterator specimenIterator = specimenMap.keySet().iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			
			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setSpecimenDefault(specimen);
			//End:- Change for API Search
			
			Long parentSpecimenId = specimen.getId();
			specimen.setId(null);
			try
			{
				insertSingleSpecimen(specimen, dao, sessionDataBean, true);
			}
			catch (DAOException daoException)
			{
				String message = " (This message is for Specimen number " + parentSpecimenId + ")";
				daoException.setSupportingMessage(message);
				throw daoException;
			}

			List derivedSpecimens = (List) specimenMap.get(specimen);
			if (derivedSpecimens == null)
			{
				continue;
			}
			//insert derived specimens
			for (int i = 0; i < derivedSpecimens.size(); i++)
			{
				Specimen derivedSpecimen = (Specimen) derivedSpecimens.get(i);
				derivedSpecimen.setParentSpecimen(specimen);
				derivedSpecimen.setSpecimenCollectionGroup(specimen.getSpecimenCollectionGroup());

				try
				{
					insertSingleSpecimen(derivedSpecimen, dao, sessionDataBean, true);
				}
				catch (DAOException daoException)
				{
					int j = i + 1;
					String message = " (This message is for Derived Specimen " + j + " of Parent Specimen number " + parentSpecimenId + ")";
					daoException.setSupportingMessage(message);
					throw daoException;
				}
			}
		}
	}

	/**
	 * This method gives the error message.
	 * This method is overrided for customizing error message
	 * @param ex - DAOException
	 * @param obj - Object
	 * @return - error message string
	 */
	public String getErrorMessage(DAOException daoException, Object obj, String operation)
	{
		if (obj instanceof HashMap)
		{
			obj = new Specimen();
		}
		String supportingMessage = daoException.getSupportingMessage();
		String formatedException = formatException(daoException.getWrapException(), obj, operation);	
		if(supportingMessage!=null && formatedException!=null)
		{
			formatedException += supportingMessage;
		}
		if(formatedException==null)
		{
			formatedException = daoException.getMessage();
			if(supportingMessage!=null)
			formatedException += supportingMessage;
		}
		return formatedException;
	}

	/**
	 * Insert single specimen into the data base.
	 * @param specimen
	 * @param dao
	 * @param sessionDataBean
	 * @param partOfMulipleSpecimen
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void insertSingleSpecimen(Specimen specimen, DAO dao, SessionDataBean sessionDataBean, boolean partOfMulipleSpecimen)
			throws DAOException, UserNotAuthorizedException
	{
		try
		{
			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setSpecimenDefault(specimen);
			//End:- Change for API Search

			Set protectionObjects = new HashSet();
			specimen.setLineage(Constants.NEW_SPECIMEN);
			setSpecimenAttributes(dao, specimen, sessionDataBean, partOfMulipleSpecimen);
			dao.insert(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true);
			dao.insert(specimen, sessionDataBean, true, true);
			protectionObjects.add(specimen);

			if (specimen.getSpecimenCharacteristics() != null)
			{
				protectionObjects.add(specimen.getSpecimenCharacteristics());
			}
			Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();

			if (externalIdentifierCollection != null)
			{
				if (externalIdentifierCollection.isEmpty()) //Dummy entry added for query
				{
					ExternalIdentifier exId = new ExternalIdentifier();

					exId.setName(null);
					exId.setValue(null);
					externalIdentifierCollection.add(exId);
				}

				Iterator it = externalIdentifierCollection.iterator();
				while (it.hasNext())
				{
					ExternalIdentifier exId = (ExternalIdentifier) it.next();
					exId.setSpecimen(specimen);
					dao.insert(exId, sessionDataBean, true, true);
				}
			}

			//Mandar : 17-july-06 : autoevents start
			Collection specimenEventsCollection = specimen.getSpecimenEventCollection();
			Iterator specimenEventsCollectionIterator = specimenEventsCollection.iterator();
			while (specimenEventsCollectionIterator.hasNext())
			{
				
				Object eventObject = specimenEventsCollectionIterator.next();

				if (eventObject instanceof CollectionEventParameters)
				{
					CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObject;
					collectionEventParameters.setSpecimen(specimen);
					dao.insert(collectionEventParameters, sessionDataBean, true, true);
				
				}
				else if (eventObject instanceof ReceivedEventParameters)
				{
					ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObject;
					receivedEventParameters.setSpecimen(specimen);
									dao.insert(receivedEventParameters, sessionDataBean, true, true);
					
				}

			}
		
			//Mandar : 17-july-06 : autoevents end

			//Inserting data for Authorization

			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, getDynamicGroups(specimen));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}

	}

	private String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
	{
		Specimen specimen = (Specimen) obj;
		String[] dynamicGroups = new String[1];

		dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(specimen.getSpecimenCollectionGroup(),
				Constants.getCollectionProtocolPGName(null));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		return dynamicGroups;
	}

	private SpecimenCollectionGroup loadSpecimenCollectionGroup(Long specimenID, DAO dao) throws DAOException
	{
		//get list of Participant's names
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

	private SpecimenCharacteristics loadSpecimenCharacteristics(Long specimenID, DAO dao) throws DAOException
	{
		//get list of Participant's names
		String sourceObjectName = Specimen.class.getName();
		String[] selectedColumn = {"specimenCharacteristics." + Constants.SYSTEM_IDENTIFIER};
		String whereColumnName[] = {Constants.SYSTEM_IDENTIFIER};
		String whereColumnCondition[] = {"="};
		Object whereColumnValue[] = {specimenID};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		List list = dao.retrieve(sourceObjectName, selectedColumn, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		if (!list.isEmpty())
		{
			Long specimenCharacteristicsId = (Long) list.get(0);
			SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setId(specimenCharacteristicsId);
			return specimenCharacteristics;

			//return (SpecimenCharacteristics)list.get(0);
		}
		return null;
	}

	private void setAvailableQuantity(Specimen obj, Specimen oldObj) throws DAOException
	{
		if (obj instanceof TissueSpecimen)
		{
			Logger.out.debug("In TissueSpecimen");
			TissueSpecimen tissueSpecimenObj = (TissueSpecimen) obj;
			TissueSpecimen tissueSpecimenOldObj = (TissueSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(tissueSpecimenObj.getQuantity().toString());//tissueSpecimenObj.getQuantityInGram().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(tissueSpecimenOldObj.getQuantity().toString());//tissueSpecimenOldObj.getQuantityInGram().doubleValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			double oldAvailableQty = Double.parseDouble(tissueSpecimenOldObj.getAvailableQuantity().toString());//tissueSpecimenOldObj.getAvailableQuantityInGram().doubleValue();

			double distQty = 0;
			double newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				// set new available quantity
				tissueSpecimenObj.setAvailableQuantity(new QuantityInGram(newAvailableQty));//tissueSpecimenObj.setAvailableQuantityInGram(new Double(newAvailableQty));
			}

		}
		else if (obj instanceof MolecularSpecimen)
		{
			Logger.out.debug("In MolecularSpecimen");
			MolecularSpecimen molecularSpecimenObj = (MolecularSpecimen) obj;
			MolecularSpecimen molecularSpecimenOldObj = (MolecularSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(molecularSpecimenObj.getQuantity().toString());//molecularSpecimenObj.getQuantityInMicrogram().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(molecularSpecimenOldObj.getQuantity().toString());//molecularSpecimenOldObj.getQuantityInMicrogram().doubleValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			double oldAvailableQty = Double.parseDouble(molecularSpecimenOldObj.getAvailableQuantity().toString());//molecularSpecimenOldObj.getAvailableQuantityInMicrogram().doubleValue();

			double distQty = 0;
			double newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				// set new available quantity
				molecularSpecimenObj.setAvailableQuantity(new QuantityInMicrogram(newAvailableQty));//molecularSpecimenObj.setAvailableQuantityInMicrogram(new Double(newAvailableQty));
			}
		}
		else if (obj instanceof CellSpecimen)
		{
			Logger.out.debug("In CellSpecimen");
			CellSpecimen cellSpecimenObj = (CellSpecimen) obj;
			CellSpecimen cellSpecimenOldObj = (CellSpecimen) oldObj;
			// get new qunatity modifed by user
			int newQty = (int) Double.parseDouble(cellSpecimenObj.getQuantity().toString());//cellSpecimenObj.getQuantityInCellCount().intValue();
			// get old qunatity from database
			int oldQty = (int) Double.parseDouble(cellSpecimenOldObj.getQuantity().toString());//cellSpecimenOldObj.getQuantityInCellCount().intValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			int oldAvailableQty = (int) Double.parseDouble(cellSpecimenOldObj.getAvailableQuantity().toString());//cellSpecimenOldObj.getAvailableQuantityInCellCount().intValue();

			int distQty = 0;
			int newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				// set new available quantity
				cellSpecimenObj.setAvailableQuantity(new QuantityInCount(newAvailableQty));//cellSpecimenObj.setAvailableQuantityInCellCount(new Integer(newAvailableQty));
			}
		}
		else if (obj instanceof FluidSpecimen)
		{
			Logger.out.debug("In FluidSpecimen");
			FluidSpecimen fluidSpecimenObj = (FluidSpecimen) obj;
			FluidSpecimen fluidSpecimenOldObj = (FluidSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(fluidSpecimenObj.getQuantity().toString());//fluidSpecimenObj.getQuantityInMilliliter().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(fluidSpecimenOldObj.getQuantity().toString());//fluidSpecimenOldObj.getQuantityInMilliliter().doubleValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			double oldAvailableQty = Double.parseDouble(fluidSpecimenOldObj.getAvailableQuantity().toString());//fluidSpecimenOldObj.getAvailableQuantityInMilliliter().doubleValue();

			double distQty = 0;
			double newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty + "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				fluidSpecimenObj.setAvailableQuantity(new QuantityInMilliliter(newAvailableQty));//fluidSpecimenObj.setAvailableQuantityInMilliliter(new Double(newAvailableQty));
			}
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
	 */
	public void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Specimen specimen = (Specimen) obj;
		Specimen specimenOld = (Specimen) oldObj;
		
		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setSpecimenDefault(specimen);
		//End:- Change for API Search

		//commented by vaishali - no ore required 
		/*if (isStoragePositionChanged(specimenOld, specimen))
		 {
		 throw new DAOException(
		 "Storage Position should not be changed while updating the specimen");
		 }*/

		setAvailableQuantity(specimen, specimenOld);

		if (specimen.isParentChanged())
		{
			//Check whether continer is moved to one of its sub container.
			if (isUnderSubSpecimen(specimen, specimen.getParentSpecimen().getId()))
			{
				throw new DAOException(ApplicationProperties.getValue("errors.specimen.under.subspecimen"));
			}
			Logger.out.debug("Loading ParentSpecimen: " + specimen.getParentSpecimen().getId());

			// check for closed ParentSpecimen
			checkStatus(dao, specimen.getParentSpecimen(), "Parent Specimen");

			SpecimenCollectionGroup scg = loadSpecimenCollectionGroup(specimen.getParentSpecimen().getId(), dao);

			specimen.setSpecimenCollectionGroup(scg);
			SpecimenCharacteristics sc = loadSpecimenCharacteristics(specimen.getParentSpecimen().getId(), dao);

			if (!Constants.ALIQUOT.equals(specimen.getLineage()))//specimen instanceof OriginalSpecimen)
			{
				specimen.setSpecimenCharacteristics(sc);
			}
		}

		//check for closed Specimen Collection Group
		if (!specimen.getSpecimenCollectionGroup().getId().equals(specimenOld.getSpecimenCollectionGroup().getId()))
			checkStatus(dao, specimen.getSpecimenCollectionGroup(), "Specimen Collection Group");

		setSpecimenGroupForSubSpecimen(specimen, specimen.getSpecimenCollectionGroup(), specimen.getSpecimenCharacteristics());

		if (!Constants.ALIQUOT.equals(specimen.getLineage()))//specimen instanceof OriginalSpecimen)
		{
			dao.update(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true, false);
		}

		dao.update(specimen, sessionDataBean, true, false, false);//dao.update(specimen, sessionDataBean, true, true, false);

		//Audit of Specimen.
		dao.audit(obj, oldObj, sessionDataBean, true);

		//Audit of Specimen Characteristics.
		dao.audit(specimen.getSpecimenCharacteristics(), specimenOld.getSpecimenCharacteristics(), sessionDataBean, true);

		Collection oldExternalIdentifierCollection = specimenOld.getExternalIdentifierCollection();

		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if (externalIdentifierCollection != null)
		{
			Iterator it = externalIdentifierCollection.iterator();
			while (it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier) it.next();
				exId.setSpecimen(specimen);
				dao.update(exId, sessionDataBean, true, true, false);

				ExternalIdentifier oldExId = (ExternalIdentifier) getCorrespondingOldObject(oldExternalIdentifierCollection, exId.getId());
				dao.audit(exId, oldExId, sessionDataBean, true);
			}
		}

		//Disable functionality
		Logger.out.debug("specimen.getActivityStatus() " + specimen.getActivityStatus());
		if (specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			//			 check for disabling a specimen 
			boolean disposalEventPresent = false;
			Collection eventCollection = specimen.getSpecimenEventCollection();
			Iterator itr = eventCollection.iterator();
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
			Logger.out.debug("specimen.getActivityStatus() " + specimen.getActivityStatus());
			Long specimenIDArr[] = new Long[1];
			specimenIDArr[0] = specimen.getId();

			disableSubSpecimens(dao, specimenIDArr);
		}
	}

	private boolean isUnderSubSpecimen(Specimen specimen, Long parentSpecimenID)
	{
		if (specimen != null)
		{
			Iterator iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				//Logger.out.debug("SUB CONTINER container "+parentContainerID.longValue()+" "+container.getId().longValue()+"  "+(parentContainerID.longValue()==container.getId().longValue()));
				if (parentSpecimenID.longValue() == childSpecimen.getId().longValue())
					return true;
				if (isUnderSubSpecimen(childSpecimen, parentSpecimenID))
					return true;
			}
		}
		return false;
	}

	private void setSpecimenGroupForSubSpecimen(Specimen specimen, SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCharacteristics specimenCharacteristics)
	{
		if (specimen != null)
		{
			Logger.out.debug("specimen() " + specimen.getId());
			Logger.out.debug("specimen.getChildrenContainerCollection() " + specimen.getChildrenSpecimen().size());

			Iterator iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				childSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
				//((OriginalSpecimen)childSpecimen).setSpecimenCharacteristics(specimenCharacteristics);

				setSpecimenGroupForSubSpecimen(childSpecimen, specimenCollectionGroup, specimenCharacteristics);
			}
		}
	}

	//  TODO TO BE REMOVED 
	private void setDisableToSubSpecimen(Specimen specimen)
	{
		if (specimen != null)
		{
			Iterator iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				childSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				setDisableToSubSpecimen(childSpecimen);
			}
		}
	}

	private void setSpecimenAttributes(DAO dao, Specimen specimen, SessionDataBean sessionDataBean, boolean isCollectionGroupName)
			throws DAOException, SMException
	{
		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//Load & set Specimen Collection Group if present
		if (specimen.getSpecimenCollectionGroup() != null)
		{
			Object specimenCollectionGroupObj = null;
			if (isCollectionGroupName)
			{
				List spgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.NAME, specimen.getSpecimenCollectionGroup().getName());

				specimenCollectionGroupObj = spgList.get(0);
			}
			else
			{
				specimenCollectionGroupObj = dao.retrieve(SpecimenCollectionGroup.class.getName(), specimen.getSpecimenCollectionGroup().getId());

			}
			if (specimenCollectionGroupObj != null)
			{
				SpecimenCollectionGroup spg = (SpecimenCollectionGroup) specimenCollectionGroupObj;

				if (spg.getActivityStatus().equals(Constants.ACTIVITY_STATUS_CLOSED))
				{
					throw new DAOException("Specimen Collection Group " + ApplicationProperties.getValue("error.object.closed"));
				}
				//checkStatus(dao, spg, );
				specimen.setSpecimenCollectionGroup(spg);
			}
		}

		//Load & set Parent Specimen if present
		if (specimen.getParentSpecimen() != null)
		{
			Object parentSpecimenObj = dao.retrieve(Specimen.class.getName(), specimen.getParentSpecimen().getId());
			if (parentSpecimenObj != null)
			{
				Specimen parentSpecimen = (Specimen) parentSpecimenObj;

				// check for closed Parent Specimen
				checkStatus(dao, parentSpecimen, "Parent Specimen");
				
				specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
				specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
				specimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
				specimen.setLineage(Constants.DERIVED_SPECIMEN);
			}
		}

		//Load & set Storage Container
		if (specimen.getStorageContainer() != null)
		{
			Object containerObj = dao.retrieve(StorageContainer.class.getName(), specimen.getStorageContainer().getId());
			if (containerObj != null)
			{
				StorageContainer container = (StorageContainer) containerObj;
				// check for closed Storage Container
				checkStatus(dao, container, "Storage Container");

				StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
						Constants.STORAGE_CONTAINER_FORM_ID);

				// --- check for all validations on the storage container.
				storageContainerBizLogic.checkContainer(dao, container.getId().toString(), specimen.getPositionDimensionOne().toString(), specimen
						.getPositionDimensionTwo().toString(), sessionDataBean);

				specimen.setStorageContainer(container);
			}
		}

		//Setting the Biohazard Collection
		Set set = new HashSet();
		Collection biohazardCollection = specimen.getBiohazardCollection();
		if (biohazardCollection != null)
		{
			Iterator it = biohazardCollection.iterator();
			while (it.hasNext())
			{
				Biohazard hazard = (Biohazard) it.next();
				Logger.out.debug("hazard.getId() " + hazard.getId());
				Object bioObj = dao.retrieve(Biohazard.class.getName(), hazard.getId());
				if (bioObj != null)
				{
					Biohazard hazardObj = (Biohazard) bioObj;
					set.add(hazardObj);
				}
			}
		}
		specimen.setBiohazardCollection(set);
	}

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

	//    public void disableRelatedObjectsForStorageContainer(DAO dao, Long storageContainerIdArr[])throws DAOException 
	//    {
	//    	Logger.out.debug("disableRelatedObjectsForStorageContainer NewSpecimenBizLogic");
	//    	List listOfSpecimenId = super.disableObjects(dao, Specimen.class, "storageContainer", 
	//    			"CATISSUE_SPECIMEN", "STORAGE_CONTAINER_IDENTIFIER", storageContainerIdArr);
	//    	if(!listOfSpecimenId.isEmpty())
	//    	{
	//    		disableSubSpecimens(dao,Utility.toLongArray(listOfSpecimenId));
	//    	}
	//    }

	private void disableSubSpecimens(DAO dao, Long speIDArr[]) throws DAOException
	{
		List listOfSubElement = super.disableObjects(dao, Specimen.class, "parentSpecimen", "CATISSUE_SPECIMEN", "PARENT_SPECIMEN_ID", speIDArr);

		if (listOfSubElement.isEmpty())
			return;
		disableSubSpecimens(dao, Utility.toLongArray(listOfSubElement));
	}

	/**
	 * @param dao
	 * @param privilegeName
	 * @param longs
	 * @param userId
	 * @throws DAOException
	 * @throws SMException
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
	 * @param dao
	 * @param privilegeName
	 * @param class1
	 * @param longs
	 * @param userId
	 * @throws DAOException
	 * @throws SMException
	 */
	private void assignPrivilegeToSubSpecimens(DAO dao, String privilegeName, Class class1, Long[] speIDArr, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, Specimen.class, "parentSpecimen", speIDArr);

		if (listOfSubElement.isEmpty())
			return;
		super.setPrivilege(dao, privilegeName, Specimen.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser, assignOperation);
		List specimenCharacteristicsIds = super.getRelatedObjects(dao, Specimen.class, new String[]{"specimenCharacteristics."
				+ Constants.SYSTEM_IDENTIFIER}, new String[]{Constants.SYSTEM_IDENTIFIER}, Utility.toLongArray(listOfSubElement));
		super.setPrivilege(dao, privilegeName, Address.class, Utility.toLongArray(specimenCharacteristicsIds), userId, roleId, assignToUser,
				assignOperation);

		assignPrivilegeToSubSpecimens(dao, privilegeName, Specimen.class, Utility.toLongArray(listOfSubElement), userId, roleId, assignToUser,
				assignOperation);
	}

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

	// validation code here
	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws SMException
	 * @throws DAOException
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
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		//		Added by Ashish
		/*		
		 Specimen specimen = (Specimen) obj;
		 boolean parentPresent = false;
		 Long collectionEventUserId = null;
		 Map biohazard = null;
		 int bhCounter=1;
		 if (specimen == null)
		 throw new DAOException("domain.object.null.err.msg", new String[]{"Specimen"});
		 Validator validator = new Validator();
		 
		 if (specimen.getSpecimenCollectionGroup().getId().equals("-1"))
		 {
		 String message = ApplicationProperties.getValue("specimen.specimenCollectionGroupId");
		 throw new DAOException("errors.item.required", new String[]{message});            
		 }
		 
		 if(specimen.getParentSpecimen() != null)
		 {
		 parentPresent = true;
		 }
		 
		 if(parentPresent && !validator.isValidOption(specimen.getParentSpecimen().getId().toString()))
		 {
		 String message = ApplicationProperties.getValue("createSpecimen.parent");
		 throw new DAOException("errors.item.required", new String[]{message});	     		
		 }
		 
		 if (specimen.getSpecimenCharacteristics().getTissueSite().equals("-1"))
		 {
		 String message = ApplicationProperties.getValue("specimen.tissueSite");
		 throw new DAOException("errors.item.required", new String[]{message});	
		 
		 }
		 
		 if (specimen.getSpecimenCharacteristics().getTissueSide().equals("-1"))
		 {
		 String message = ApplicationProperties.getValue("specimen.tissueSide");
		 throw new DAOException("errors.item.required", new String[]{message});	
		 
		 }
		 
		 if (specimen.getPathologicalStatus().equals("-1"))
		 {
		 String message = ApplicationProperties.getValue("specimen.pathologicalStatus");
		 throw new DAOException("errors.item.required", new String[]{message});	
		 
		 }  	
		 
		 if(operation.equalsIgnoreCase(Constants.ADD  ) )
		 {
		 Iterator specimenEventCollectionIterator = specimen.getSpecimenEventCollection().iterator();
		 while(specimenEventCollectionIterator.hasNext())
		 {
		 //CollectionEvent validation.
		 Object eventObject = specimenEventCollectionIterator.next();
		 if(eventObject instanceof CollectionEventParameters)
		 {
		 CollectionEventParameters collectionEventParameters =  (CollectionEventParameters)eventObject;
		 collectionEventUserId = collectionEventParameters.getId();
		 //     				if ((collectionEventUserId) == -1L)
		 //    	            {
		 //    	     			
		 //    	    			throw new DAOException("errors.item.required", new String[]{"Collection Name"});
		 //    	           		
		 //    	            }
		 if (!validator.checkDate(Utility.parseDateToString(collectionEventParameters.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY)) )
		 {
		 
		 throw new DAOException("errors.item.required", new String[]{"Collection Date"});
		 
		 }    	
		 // checks the collectionProcedure
		 if (!validator.isValidOption( collectionEventParameters.getCollectionProcedure() ) )
		 {
		 String message = ApplicationProperties.getValue("collectioneventparameters.collectionprocedure");
		 throw new DAOException("errors.item.required", new String[]{message});
		 
		 }
		 }
		 //     			ReceivedEvent validation
		 else if(eventObject instanceof ReceivedEventParameters)
		 {
		 ReceivedEventParameters receivedEventParameters =  (ReceivedEventParameters)eventObject;
		 collectionEventUserId = receivedEventParameters.getId();
		 //     				if ((receivedEventParameters.getId()) == -1L)
		 //     		        {
		 //     					throw new DAOException("errors.item.required", new String[]{"Received user"});
		 //     		       		
		 //     		        }
		 if (!validator.checkDate(Utility.parseDateToString(receivedEventParameters.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY))) 
		 {
		 throw new DAOException("errors.item.required", new String[]{"Received date"});     		       		
		 }

		 // checks the collectionProcedure
		 if (!validator.isValidOption(receivedEventParameters.getReceivedQuality() ) )
		 {
		 String message = ApplicationProperties.getValue("receivedeventparameters.receivedquality");
		 throw new DAOException("errors.item.required", new String[]{message});     		       		
		 }     				
		 }  		
		 }
		 }
		 //Validations for Biohazard Add-More Block
		 
		 //     	if(specimen.getBiohazardCollection() != null && specimen.getBiohazardCollection().size() != 0)
		 //        {
		 //     		biohazard = new HashMap();
		 //        	
		 //        	int i=1;
		 //        	
		 //        	Iterator it = specimen.getBiohazardCollection().iterator();
		 //        	while(it.hasNext())
		 //        	{
		 //        		String key1 = "Biohazard:" + i + "_type";
		 //				String key2 = "Biohazard:" + i + "_id";
		 //				String key3 = "Biohazard:" + i + "_persisted";
		 //				
		 //				Biohazard hazard = (Biohazard)it.next();
		 //				
		 //				biohazard.put(key1,hazard.getType());
		 //				biohazard.put(key2,hazard.getId());
		 //				
		 //				//boolean for showing persisted value
		 //				biohazard.put(key3,"true");
		 //				
		 //				i++;
		 //        	}
		 //        	
		 //        	bhCounter = specimen.getBiohazardCollection().size();
		 //        }
		 //        String className = "Biohazard:";
		 //        String key1 = "_type";
		 //        String key2 = "_" + Constants.SYSTEM_IDENTIFIER;
		 //        String key3 = "_persisted";
		 //        int index = 1;
		 //        
		 //        while(true)
		 //        {
		 //        	String keyOne = className + index + key1;
		 //			String keyTwo = className + index + key2;
		 //			String keyThree = className + index + key3;
		 //			
		 //        	String value1 = (String)biohazard.get(keyOne);
		 //        	String value2 = (String)biohazard.get(keyTwo);
		 //        	String value3 = (String)biohazard.get(keyThree);
		 //        	
		 //        	if(value1 == null || value2 == null || value3 == null)
		 //        	{
		 //        		break;
		 //        	}
		 //        	else if(!validator.isValidOption(value1) && !validator.isValidOption(value2))
		 //        	{
		 //        		biohazard.remove(keyOne);
		 //        		biohazard.remove(keyTwo);
		 //        		biohazard.remove(keyThree);
		 //        	}
		 //        	else if((validator.isValidOption(value1) && !validator.isValidOption(value2)) 
		 //        			|| (!validator.isValidOption(value1) && validator.isValidOption( value2)))   		
		 //        	{
		 //        		String message = ApplicationProperties.getValue("newSpecimen.msg");
		 //    			throw new DAOException("errors.newSpecimen.biohazard.missing", new String[]{message});	
		 //        		
		 //        		
		 //        	}
		 //        	index++;
		 //        }
		 
		 
		 */
		//End
		boolean result;

		if (obj instanceof Map)
		{
			result = validateMultipleSpecimen((Map) obj, dao, operation);
		}
		else
		{
			result = validateSingleSpecimen((Specimen) obj, dao, operation, false);
		}
		return result;

	}

	/**
	 * validates single specimen. 
	 */
	private boolean validateSingleSpecimen(Specimen specimen, DAO dao, String operation, boolean partOfMulipleSpecimen) throws DAOException
	{
		if (Constants.ALIQUOT.equals(specimen.getLineage()))
		{
			return true;
		}

		validateFields(specimen, dao, operation, partOfMulipleSpecimen);

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

		SpecimenCharacteristics characters = specimen.getSpecimenCharacteristics();

		if (characters == null)
		{
			throw new DAOException(ApplicationProperties.getValue("specimen.characteristics.errMsg"));
		}
		else
		{
			if (specimen.getSpecimenCollectionGroup() != null)
			{
				//				NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
				List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE, null);

				if (!Validator.isEnumeratedValue(tissueSiteList, characters.getTissueSite()))
				{
					throw new DAOException(ApplicationProperties.getValue("protocol.tissueSite.errMsg"));
				}

				//		    	NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
				List tissueSideList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SIDE, null);

				if (!Validator.isEnumeratedValue(tissueSideList, characters.getTissueSide()))
				{
					throw new DAOException(ApplicationProperties.getValue("specimen.tissueSide.errMsg"));
				}

				List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

				if (!Validator.isEnumeratedValue(pathologicalStatusList, specimen.getPathologicalStatus()))
				{
					throw new DAOException(ApplicationProperties.getValue("protocol.pathologyStatus.errMsg"));
				}
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

		return true;
	}

	private void validateFields(Specimen specimen, DAO dao, String operation, boolean partOfMulipleSpecimen) throws DAOException
	{
		Validator validator = new Validator();

		if (partOfMulipleSpecimen)
		{

			if (specimen.getSpecimenCollectionGroup() == null || validator.isEmpty(specimen.getSpecimenCollectionGroup().getName()))
			{
				String quantityString = ApplicationProperties.getValue("specimen.specimenCollectionGroup");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
			}

			List spgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.NAME, specimen.getSpecimenCollectionGroup().getName());

			if (spgList.size() == 0)
			{
				throw new DAOException(ApplicationProperties.getValue("errors.item.unknown", "Specimen Collection Group "
						+ specimen.getSpecimenCollectionGroup().getName()));
			}
		}

		if (validator.isEmpty(specimen.getLabel()))
		{
			String labelString = ApplicationProperties.getValue("specimen.label");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", labelString));
		}

		if (specimen.getQuantity() == null || specimen.getQuantity().getValue() == null)
		{
			String quantityString = ApplicationProperties.getValue("specimen.quantity");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
		}

		Long storageContainerId = specimen.getStorageContainer().getId();
		Integer xPos = specimen.getPositionDimensionOne();
		Integer yPos = specimen.getPositionDimensionTwo();

		if (storageContainerId == null || xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
		{
			throw new DAOException(ApplicationProperties.getValue("errors.item.format", ApplicationProperties
					.getValue("specimen.positionInStorageContainer")));
		}
	}

	/**
	 * validates multiple specimen. Internally it for each specimen it innvokes validateSingleSpecimen. 
	 * @throws DAOException
	 * @throws DAOException
	 */
	private boolean validateMultipleSpecimen(Map specimenMap, DAO dao, String operation) throws DAOException

	{
		Iterator specimenIterator = specimenMap.keySet().iterator();
		boolean result = true;
		while (specimenIterator.hasNext() && result == true)
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			//validate single specimen

			try
			{
				result = validateSingleSpecimen(specimen, dao, operation, true);
			}
			catch (DAOException daoException)
			{
				String message = daoException.getMessage();
				message += " (This message is for Specimen number " + specimen.getId() + ")";
				daoException.setMessage(message);
				throw daoException;
			}

			List derivedSpecimens = (List) specimenMap.get(specimen);

			if (derivedSpecimens == null)
			{
				continue;
			}

			//validate derived specimens
			for (int i = 0; i < derivedSpecimens.size(); i++)
			{

				Specimen derivedSpecimen = (Specimen) derivedSpecimens.get(i);
				try
				{
					result = validateSingleSpecimen(derivedSpecimen, dao, operation, false);
				}
				catch (DAOException daoException)
				{
					int j = i + 1;
					String message = daoException.getMessage();
					message += " (This message is for Derived Specimen " + j + " of Parent Specimen number " + specimen.getId()+")";
					daoException.setMessage(message);
					throw daoException;
				}

				if (!result)
				{
					break;
				}
			}

		}
		return result;
	}

	/* This function checks whether the storage position of a specimen is changed or not 
	 * & returns the status accordingly.
	 */
	private boolean isStoragePositionChanged(Specimen oldSpecimen, Specimen newSpecimen)
	{
		StorageContainer oldContainer = oldSpecimen.getStorageContainer();
		StorageContainer newContainer = newSpecimen.getStorageContainer();

		if (oldContainer.getId().longValue() == newContainer.getId().longValue())
		{
			if (oldSpecimen.getPositionDimensionOne().intValue() == newSpecimen.getPositionDimensionOne().intValue())
			{
				if (oldSpecimen.getPositionDimensionTwo().intValue() == newSpecimen.getPositionDimensionTwo().intValue())
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	/**
	 * This method fetches linked data from integrated application i.e. CAE/caTies.
	 */
	public List getLinkedAppData(Long id, String applicationID)
	{
		Logger.out.debug("In getIntegrationData() of SpecimenBizLogic ");

		Logger.out.debug("ApplicationName in getIntegrationData() of SCGBizLogic==>" + applicationID);

		long identifiedPathologyReportId = 0;

		try
		{
			//JDBC call to get matching identifier from database
			Class.forName("org.gjt.mm.mysql.Driver");

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/catissuecore", "catissue_core", "catissue_core");

			Statement stmt = connection.createStatement();

			String specimenCollectionGroupQuery = "select SPECIMEN_COLLECTION_GROUP_ID from CATISSUE_SPECIMEN where IDENTIFIER=" + id;

			ResultSet specimenCollectionGroupResultSet = stmt.executeQuery(specimenCollectionGroupQuery);

			long specimenCollectionGroupId = 0;
			while (specimenCollectionGroupResultSet.next())
			{
				specimenCollectionGroupId = specimenCollectionGroupResultSet.getLong(1);
				break;
			}
			Logger.out.debug("SpecimenCollectionGroupId==>" + specimenCollectionGroupId);
			if (specimenCollectionGroupId == 0)
			{
				List exception = new ArrayList();
				exception.add("SpecimenCollectionGroupId is not available for Specimen");
				return exception;
			}

			String clinicalReportQuery = "select CLINICAL_REPORT_ID from CATISSUE_SPECIMEN_COLL_GROUP where IDENTIFIER=" + specimenCollectionGroupId;

			ResultSet clinicalReportResultSet = stmt.executeQuery(clinicalReportQuery);

			long clinicalReportId = 0;
			while (clinicalReportResultSet.next())
			{
				clinicalReportId = clinicalReportResultSet.getLong(1);
				break;
			}
			Logger.out.debug("ClinicalReportId==>" + clinicalReportId);
			clinicalReportResultSet.close();
			if (clinicalReportId == 0)
			{
				List exception = new ArrayList();
				exception.add("ClinicalReportId is not available for SpecimenCollectionGroup");
				return exception;
			}

			String identifiedPathologyReportIdQuery = "select IDENTIFIER from CATISSUE_IDENTIFIED_PATHOLOGY_REPORT where CLINICAL_REPORT_ID="
					+ clinicalReportId;

			ResultSet identifiedPathologyReportResultSet = stmt.executeQuery(identifiedPathologyReportIdQuery);

			while (identifiedPathologyReportResultSet.next())
			{
				identifiedPathologyReportId = identifiedPathologyReportResultSet.getLong(1);
				break;
			}
			Logger.out.debug("IdentifiedPathologyReportId==>" + identifiedPathologyReportId);
			identifiedPathologyReportResultSet.close();
			if (identifiedPathologyReportId == 0)
			{
				List exception = new ArrayList();
				exception.add("IdentifiedPathologyReportId is not available for linked ClinicalReportId");
				return exception;
			}

			stmt.close();

			connection.close();
		}
		catch (Exception e)
		{
			Logger.out.debug("JDBC Exception==>" + e.getMessage());
		}

		IntegrationManager integrationManager = IntegrationManagerFactory.getIntegrationManager(applicationID);

		return integrationManager.getLinkedAppData(new Specimen(), new Long(identifiedPathologyReportId));
	}

	public String getPageToShow()
	{
		return new String();
	}

	public List getMatchingObjects()
	{
		return new ArrayList();
	}
}