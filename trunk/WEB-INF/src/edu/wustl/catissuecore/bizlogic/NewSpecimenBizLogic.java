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
import java.util.TreeMap;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.integration.IntegrationManager;
import edu.wustl.catissuecore.integration.IntegrationManagerFactory;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.WithdrawConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
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

			//Inserting authorization data 
			Specimen specimen = (Specimen) obj;
			Set protectionObjects = new HashSet();
			protectionObjects.add(specimen);
			if (specimen.getSpecimenCharacteristics() != null)
			{
				protectionObjects.add(specimen.getSpecimenCharacteristics());
			}
			try
			{
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, getDynamicGroups(specimen));
			}
			catch (SMException e)
			{
				throw handleSMException(e);
			}

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
		List specimenList = new ArrayList();
		Iterator specimenIterator = specimenMap.keySet().iterator();
		int count = 0;
		while (specimenIterator.hasNext())
		{
			count++;
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

			resetId(specimen);

			try
			{
				insertSingleSpecimen(specimen, dao, sessionDataBean, true);
				specimenList.add(specimen);
			}
			catch (DAOException daoException)
			{
				String message = " (This message is for Specimen number " + count + ")";
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
				resetId(derivedSpecimen);
				derivedSpecimen.setParentSpecimen(specimen);
				derivedSpecimen.setSpecimenCollectionGroup(specimen.getSpecimenCollectionGroup());

				try
				{
					insertSingleSpecimen(derivedSpecimen, dao, sessionDataBean, true);
					specimenList.add(derivedSpecimen);
				}
				catch (DAOException daoException)
				{
					int j = i + 1;
					String message = " (This message is for Derived Specimen " + j + " of Parent Specimen number " + count + ")";
					daoException.setSupportingMessage(message);
					throw daoException;
				}
			}
		}

		//inserting authorization data 
		Iterator itr = specimenList.iterator();
		while (itr.hasNext())
		{
			Specimen specimen = (Specimen) itr.next();
			Set protectionObjects = new HashSet();
			protectionObjects.add(specimen);
			if (specimen.getSpecimenCharacteristics() != null)
			{
				protectionObjects.add(specimen.getSpecimenCharacteristics());
			}
			try
			{
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, getDynamicGroups(specimen));
			}
			catch (SMException e)
			{
				throw handleSMException(e);
			}

		}
	}

	/**
	 * By Rahul Ner
	 * @param specimen
	 */
	private void resetId(Specimen specimen)
	{
		specimen.setId(null);
		Iterator childItr = null;

		if (specimen.getSpecimenEventCollection() != null)
		{

			childItr = specimen.getSpecimenEventCollection().iterator();
			while (childItr.hasNext())
			{
				SpecimenEventParameters eventParams = (SpecimenEventParameters) childItr.next();
				eventParams.setSpecimen(specimen);
				eventParams.setId(null);
			}

		}

		if (specimen.getExternalIdentifierCollection() != null)
		{
			childItr = specimen.getExternalIdentifierCollection().iterator();
			while (childItr.hasNext())
			{
				ExternalIdentifier externalIdentifier = (ExternalIdentifier) childItr.next();
				externalIdentifier.setSpecimen(specimen);
				externalIdentifier.setId(null);
			}
		}

		if (specimen.getBiohazardCollection() != null)
		{
			childItr = specimen.getBiohazardCollection().iterator();
			while (childItr.hasNext())
			{
				Biohazard biohazard = (Biohazard) childItr.next();
				//biohazard.setId(null);
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
		if (supportingMessage != null && formatedException != null)
		{
			formatedException += supportingMessage;
		}
		if (formatedException == null)
		{
			formatedException = daoException.getMessage();
			if (supportingMessage != null)
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

			Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();

			if (externalIdentifierCollection != null)
			{
				if (externalIdentifierCollection.isEmpty()) //Dummy entry added for query
				{
					ExternalIdentifier exId = new ExternalIdentifier();

					exId.setName(null);
					exId.setValue(null);
					exId.setSpecimen(specimen);
					externalIdentifierCollection.add(exId);
				}
				else
				{
					/**
					 *  Bug 3007 - Santosh
					 */
					Iterator it = externalIdentifierCollection.iterator();
					while (it.hasNext())
					{
						ExternalIdentifier exId = (ExternalIdentifier) it.next();
						exId.setSpecimen(specimen);
						//					dao.insert(exId, sessionDataBean, true, true);
					}
				}
			}
			else
			{
				//Dummy entry added for query.
				externalIdentifierCollection = new HashSet();
				ExternalIdentifier exId = new ExternalIdentifier();
				exId.setName(null);
				exId.setValue(null);
				exId.setSpecimen(specimen);
				externalIdentifierCollection.add(exId);
				specimen.setExternalIdentifierCollection(externalIdentifierCollection);
			}

			//Set protectionObjects = new HashSet();
			if(specimen.getLineage() == null)
			{
			specimen.setLineage(Constants.NEW_SPECIMEN);
			}
			setSpecimenAttributes(dao, specimen, sessionDataBean, partOfMulipleSpecimen);
			if(specimen.getAvailableQuantity().getValue().doubleValue() == 0)
			{
				specimen.setAvailable(new Boolean(false));
			}
			dao.insert(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true);
			dao.insert(specimen, sessionDataBean, true, true);
			//protectionObjects.add(specimen);

			/*if (specimen.getSpecimenCharacteristics() != null)
			 {
			 protectionObjects.add(specimen.getSpecimenCharacteristics());
			 }*/

			//Mandar : 17-july-06 : autoevents start
			//			Collection specimenEventsCollection = specimen.getSpecimenEventCollection();
			//			Iterator specimenEventsCollectionIterator = specimenEventsCollection.iterator();
			//			while (specimenEventsCollectionIterator.hasNext())
			//			{
			//
			//				Object eventObject = specimenEventsCollectionIterator.next();
			//				
			//
			//				if (eventObject instanceof CollectionEventParameters)
			//				{
			//					CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObject;
			//					collectionEventParameters.setSpecimen(specimen);
			//					//collectionEventParameters.setId(null);
			//					dao.insert(collectionEventParameters, sessionDataBean, true, true);
			//
			//				}
			//				else if (eventObject instanceof ReceivedEventParameters)
			//				{
			//					ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObject;
			//					receivedEventParameters.setSpecimen(specimen);
			//					//receivedEventParameters.setId(null);
			//					dao.insert(receivedEventParameters, sessionDataBean, true, true);
			//
			//				}
			//
			//			}
			//Mandar : 17-july-06 : autoevents end
			//Inserting data for Authorization
			//SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, getDynamicGroups(specimen));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}

	}

	synchronized public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
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
		if (obj instanceof HashMap)
		{
			HashMap specimenMap = (HashMap) obj;
			Iterator specimenIterator = specimenMap.keySet().iterator();
			while (specimenIterator.hasNext())
			{
				Specimen specimen = (Specimen) specimenIterator.next();
				updateStorageLocations((TreeMap) containerMap, specimen);
				List derivedSpecimens = (List) specimenMap.get(specimen);

				if (derivedSpecimens != null)
				{
					for (int i = 0; i < derivedSpecimens.size(); i++)
					{

						Specimen derivedSpecimen = (Specimen) derivedSpecimens.get(i);
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
	 * This method gets called after update method. Any logic after updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * */
	protected void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException,
			UserNotAuthorizedException
	{
		/**
		 * Bug 3094 --> This jdbc query updates all the aliquots of a specimen, saperate query is written to improve the performance
		 */
		
		Specimen currentSpecimen = (Specimen) currentObj;
		Specimen oldSpecimen = (Specimen) oldObj;
		String type = currentSpecimen.getType();
		String pathologicalStatus = currentSpecimen.getPathologicalStatus();
		String id = currentSpecimen.getId().toString();
		if(!currentSpecimen.getPathologicalStatus().equals(oldSpecimen.getPathologicalStatus())||!currentSpecimen.getType().equals(oldSpecimen.getType()))
		{
		try
		{
			JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);

			String queryStr = "UPDATE CATISSUE_SPECIMEN SET TYPE = '" + type + "',PATHOLOGICAL_STATUS = '" + pathologicalStatus
					+ "' WHERE LINEAGE = 'ALIQUOT' AND PARENT_SPECIMEN_ID ='" + id + "';";

			jdbcDao.executeUpdate(queryStr);
			jdbcDao.closeSession();
		}
		catch (Exception e)
		{
               Logger.out.debug("Exception occured while updating aliquots");
		}
		}
	}

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

		}
	}

	protected String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
	{
		Specimen specimen = (Specimen) obj;
		String[] dynamicGroups = new String[1];

		dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(specimen.getSpecimenCollectionGroup(),
				Constants.getCollectionProtocolPGName(null));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		return dynamicGroups;
	}

	protected void chkContainerValidForSpecimen(StorageContainer container, Specimen specimen, DAO dao) throws DAOException
	{

		boolean aa = container.getHoldsSpecimenClassCollection().contains(specimen.getClassName());
		boolean bb = container.getCollectionProtocolCollection().contains(
				specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol());
		if (!container.getHoldsSpecimenClassCollection().contains(specimen.getClassName())
				|| (!container.getCollectionProtocolCollection().contains(
						specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol()) && container
						.getCollectionProtocolCollection().size() != 0))
		{
			throw new DAOException("This Storage Container not valid for Specimen");
		}
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
			double newQty = Double.parseDouble(tissueSpecimenObj.getInitialquantity().toString());//tissueSpecimenObj.getQuantityInGram().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(tissueSpecimenOldObj.getInitialquantity().toString());//tissueSpecimenOldObj.getQuantityInGram().doubleValue();
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
				tissueSpecimenObj.setAvailableQuantity(new Quantity(String.valueOf(newAvailableQty)));//tissueSpecimenObj.setAvailableQuantityInGram(new Double(newAvailableQty));
			}

		}
		else if (obj instanceof MolecularSpecimen)
		{
			Logger.out.debug("In MolecularSpecimen");
			MolecularSpecimen molecularSpecimenObj = (MolecularSpecimen) obj;
			MolecularSpecimen molecularSpecimenOldObj = (MolecularSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(molecularSpecimenObj.getInitialquantity().toString());//molecularSpecimenObj.getQuantityInMicrogram().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(molecularSpecimenOldObj.getInitialquantity().toString());//molecularSpecimenOldObj.getQuantityInMicrogram().doubleValue();
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
				molecularSpecimenObj.setAvailableQuantity(new Quantity(String.valueOf(newAvailableQty)));//molecularSpecimenObj.setAvailableQuantityInMicrogram(new Double(newAvailableQty));
			}
		}
		else if (obj instanceof CellSpecimen)
		{
			Logger.out.debug("In CellSpecimen");
			CellSpecimen cellSpecimenObj = (CellSpecimen) obj;
			CellSpecimen cellSpecimenOldObj = (CellSpecimen) oldObj;
			// get new qunatity modifed by user
			long newQty = (long) Double.parseDouble(cellSpecimenObj.getInitialquantity().toString());//cellSpecimenObj.getQuantityInCellCount().intValue();
			// get old qunatity from database
			long oldQty = (long) Double.parseDouble(cellSpecimenOldObj.getInitialquantity().toString());//cellSpecimenOldObj.getQuantityInCellCount().intValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			long oldAvailableQty = (long) Double.parseDouble(cellSpecimenOldObj.getAvailableQuantity().toString());//cellSpecimenOldObj.getAvailableQuantityInCellCount().intValue();

			long distQty = 0;
			long newAvailableQty = 0;
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
				cellSpecimenObj.setAvailableQuantity(new Quantity(String.valueOf(newAvailableQty)));//cellSpecimenObj.setAvailableQuantityInCellCount(new Integer(newAvailableQty));
			}
		}
		else if (obj instanceof FluidSpecimen)
		{
			Logger.out.debug("In FluidSpecimen");
			FluidSpecimen fluidSpecimenObj = (FluidSpecimen) obj;
			FluidSpecimen fluidSpecimenOldObj = (FluidSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(fluidSpecimenObj.getInitialquantity().toString());//fluidSpecimenObj.getQuantityInMilliliter().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(fluidSpecimenOldObj.getInitialquantity().toString());//fluidSpecimenOldObj.getQuantityInMilliliter().doubleValue();
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
				fluidSpecimenObj.setAvailableQuantity(new Quantity(String.valueOf(newAvailableQty)));//fluidSpecimenObj.setAvailableQuantityInMilliliter(new Double(newAvailableQty));
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

		//Added for api Search 
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
		//		 if(specimenOld.getAvailableQuantity().getValue().longValue() != specimen.getAvailableQuantity().getValue().longValue())
		//		 {
		//		 	throw new DAOException("Available Quantity should not be changed while updating the specimen");
		//		 }

		//End:- Change for API Search

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

		if(!specimen.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION   ) )
			updateConsentWithdrawStatus(specimen, dao, sessionDataBean);
		else if(!specimen.getApplyChangesTo().equalsIgnoreCase(Constants.APPLY_NONE ) )
			updateConsentStatus(specimen, specimenOld);

		//Mandar: 16-Jan-07
//		updateConsentWithdrawStatus(specimen, dao, sessionDataBean);
		/**
		 * Refer bug 3269 
		 * 1. If quantity of old object > 0 and it is unavailable, it was marked 
         *    unavailale by user. 
         * 2. If quantity of old object = 0, we can assume that it is unavailable because its quantity 
         *    has become 0.
		 */
		
		if(specimen.getAvailableQuantity().getValue().doubleValue() == 0)
		{
			specimen.setAvailable(new Boolean(false));
		}
		else if(specimenOld.getAvailableQuantity().getValue().doubleValue()==0)
		{
			// quantity of old object is zero and that of current is nonzero
			specimen.setAvailable(new Boolean(true));
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
		if(specimen.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION ) )
		{
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
//		else
//		{
//			if(specimen.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_DISCARD ) )
//			{
//				setDisableToSubSpecimen(specimen);
//				Long specimenIDArr[] = new Long[1];
//				specimenIDArr[0] = specimen.getId();
//
//				disableSubSpecimens(dao, specimenIDArr);
//			}
//		}
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

	private void setSpecimenAttributes(DAO dao, Specimen specimen, SessionDataBean sessionDataBean, boolean partOfMultipleSpecimen)
			throws DAOException, SMException
	{

		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		// set barcode to null in case it is blank
		if (specimen.getBarcode() != null && specimen.getBarcode().trim().equals(""))
		{
			specimen.setBarcode(null);
		}
		// TODO
		//Load & set Specimen Collection Group if present
		if (specimen.getSpecimenCollectionGroup() != null)
		{
			SpecimenCollectionGroup specimenCollectionGroupObj = null;
			if (partOfMultipleSpecimen)
			{
				/*String sourceObjectName = SpecimenCollectionGroup.class.getName();
				 String[] selectColumnName = {"id"};
				 String[] whereColumnName = {"name"}; 
				 String[] whereColumnCondition = {"="};
				 Object[] whereColumnValue = {specimen.getSpecimenCollectionGroup().getName()};
				 String joinCondition = null;

				 List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

				 specimenCollectionGroupObj = new SpecimenCollectionGroup();
				 specimenCollectionGroupObj.setId((Long)list.get(0));*/
				List spgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.NAME, specimen.getSpecimenCollectionGroup().getName());

				specimenCollectionGroupObj = (SpecimenCollectionGroup) spgList.get(0);
				
				Collection consentTierStatusCollection= specimenCollectionGroupObj.getConsentTierStatusCollection();
				Collection consentTierStatusCollectionForSpecimen = new HashSet(); 
				Iterator itr = consentTierStatusCollection.iterator();
				while(itr.hasNext())
				{
					 ConsentTierStatus conentTierStatus = (ConsentTierStatus) itr.next();
					 ConsentTierStatus consentTierStatusForSpecimen = new ConsentTierStatus();
					 consentTierStatusForSpecimen.setStatus(conentTierStatus.getStatus());
					 consentTierStatusForSpecimen.setConsentTier(conentTierStatus.getConsentTier());
					 consentTierStatusCollectionForSpecimen.add(consentTierStatusForSpecimen);
				}
				specimen.setConsentTierStatusCollection(consentTierStatusCollectionForSpecimen);
			}
			else
			{
				specimenCollectionGroupObj = new SpecimenCollectionGroup();
				specimenCollectionGroupObj.setId(specimen.getSpecimenCollectionGroup().getId());

			}
			if (specimenCollectionGroupObj != null)
			{
				/*if (specimenCollectionGroupObj.getActivityStatus().equals(Constants.ACTIVITY_STATUS_CLOSED))
				 {
				 throw new DAOException("Specimen Collection Group " + ApplicationProperties.getValue("error.object.closed"));
				 }*/
				checkStatus(dao, specimenCollectionGroupObj, "Specimen Collection Group");
				specimen.setSpecimenCollectionGroup(specimenCollectionGroupObj);
			}
		}

		//Load & set Parent Specimen if present
		if (specimen.getParentSpecimen() != null)
		{
			//			Object parentSpecimenObj = dao.retrieve(Specimen.class.getName(), specimen.getParentSpecimen().getId());
			//			if (parentSpecimenObj != null)
			//			{
			Specimen parentSpecimen = new Specimen();
			parentSpecimen.setId(specimen.getParentSpecimen().getId());
			// check for closed Parent Specimen
			checkStatus(dao, parentSpecimen, "Parent Specimen");
			specimen.setLineage(Constants.DERIVED_SPECIMEN);
			// set parent specimen event parameters -- added by Ashwin for bug id# 2476
			specimen.setSpecimenEventCollection(populateDeriveSpecimenEventCollection(specimen.getParentSpecimen(), specimen));
			//			}
		}

		//Load & set Storage Container
		if (specimen.getStorageContainer() != null)
		{
			if (specimen.getStorageContainer().getId() != null)
			{
				//				Object containerObj = dao.retrieve(StorageContainer.class.getName(), specimen.getStorageContainer().getId());
				//				if (containerObj != null)
				//				{
				//					StorageContainer container = (StorageContainer) containerObj;
				StorageContainer storageContainerObj = new StorageContainer();
				storageContainerObj.setId(specimen.getStorageContainer().getId());
				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = {"name"};
				String[] whereColumnName = {"id"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition = {"="};
				Object[] whereColumnValue = {specimen.getStorageContainer().getId()};
				String joinCondition = null;

				List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

				if (!list.isEmpty())
				{
					storageContainerObj.setName((String) list.get(0));
				}

				// check for closed Storage Container
				checkStatus(dao, storageContainerObj, "Storage Container");

				StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
						Constants.STORAGE_CONTAINER_FORM_ID);

				// --- check for all validations on the storage container.
				storageContainerBizLogic.checkContainer(dao, storageContainerObj.getId().toString(), specimen.getPositionDimensionOne().toString(),
						specimen.getPositionDimensionTwo().toString(), sessionDataBean, partOfMultipleSpecimen);
				//    chkContainerValidForSpecimen(specimen.getStorageContainer(), specimen,dao);
				specimen.setStorageContainer(storageContainerObj);
				//				}
				//				else
				//				{
				//					throw new DAOException(ApplicationProperties.getValue("errors.storageContainerExist"));
				//				}
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
		boolean result;

		if (obj instanceof Map)
		{
			//validation on multiple specimens are performed in MultipleSpecimenValidationUtil, so dont require to perform the bizlogic validations.
			return true;
			//result = validateMultipleSpecimen((Map) obj, dao, operation);
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
		//Added by Ashish		
		//Logger.out.debug("Start-Inside validate method of specimen bizlogic");
		if (specimen == null)
		{
			throw new DAOException(ApplicationProperties.getValue("specimen.object.null.err.msg", "Specimen"));
		}

		Validator validator = new Validator();

		if (specimen.getSpecimenCollectionGroup() == null || specimen.getSpecimenCollectionGroup().getId() == null
				|| specimen.getSpecimenCollectionGroup().getId().equals("-1"))
		{
			String message = ApplicationProperties.getValue("specimen.specimenCollectionGroup");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (specimen.getParentSpecimen() != null
				&& (specimen.getParentSpecimen().getId() == null || validator.isEmpty(specimen.getParentSpecimen().getId().toString())))
		{
			String message = ApplicationProperties.getValue("createSpecimen.parent");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		if (validator.isEmpty(specimen.getLabel()))
		{
			String message = ApplicationProperties.getValue("specimen.label");
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

		if (specimen.getSpecimenEventCollection() != null)
		{
			Iterator specimenEventCollectionIterator = specimen.getSpecimenEventCollection().iterator();
			while (specimenEventCollectionIterator.hasNext())
			{
				//CollectionEvent validation.
				Object eventObject = specimenEventCollectionIterator.next();
				if (eventObject instanceof CollectionEventParameters)
				{
					CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObject;
					collectionEventParameters.getUser();
					if (collectionEventParameters.getUser() == null || collectionEventParameters.getUser().getId() == null)
					{
						String message = ApplicationProperties.getValue("specimen.collection.event.user");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					if (!validator.checkDate(Utility.parseDateToString(collectionEventParameters.getTimestamp(), Constants.DATE_PATTERN_MM_DD_YYYY)))
					{

						String message = ApplicationProperties.getValue("specimen.collection.event.date");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					// checks the collectionProcedure
					if (!validator.isValidOption(collectionEventParameters.getCollectionProcedure()))
					{
						String message = ApplicationProperties.getValue("collectioneventparameters.collectionprocedure");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
					if (!Validator.isEnumeratedValue(procedureList, collectionEventParameters.getCollectionProcedure()))
					{
						throw new DAOException(ApplicationProperties.getValue("events.collectionProcedure.errMsg"));
					}

					if (!validator.isValidOption(collectionEventParameters.getContainer()))
					{
						String message = ApplicationProperties.getValue("collectioneventparameters.container");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}

					List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
					if (!Validator.isEnumeratedValue(containerList, collectionEventParameters.getContainer()))
					{
						throw new DAOException(ApplicationProperties.getValue("events.container.errMsg"));
					}

				}
				//ReceivedEvent validation
				else if (eventObject instanceof ReceivedEventParameters)
				{
					ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObject;
					if (receivedEventParameters.getUser() == null || receivedEventParameters.getUser().getId() == null)
					{
						String message = ApplicationProperties.getValue("specimen.recieved.event.user");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					if (!validator.checkDate(Utility.parseDateToString(receivedEventParameters.getTimestamp(), Constants.DATE_PATTERN_MM_DD_YYYY)))
					{
						String message = ApplicationProperties.getValue("specimen.recieved.event.date");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}
					// checks the collectionProcedure
					if (!validator.isValidOption(receivedEventParameters.getReceivedQuality()))
					{
						String message = ApplicationProperties.getValue("collectioneventparameters.receivedquality");
						throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
					}

					List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
					if (!Validator.isEnumeratedValue(qualityList, receivedEventParameters.getReceivedQuality()))
					{
						throw new DAOException(ApplicationProperties.getValue("events.receivedQuality.errMsg"));
					}
				}
			}
		}

		//Validations for Biohazard Add-More Block
		Collection bioHazardCollection = specimen.getBiohazardCollection();
		Biohazard biohazard = null;
		if (bioHazardCollection != null && !bioHazardCollection.isEmpty())
		{
			Iterator itr = bioHazardCollection.iterator();
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

		//validations for external identifiers
		Collection extIdentifierCollection = specimen.getExternalIdentifierCollection();
		ExternalIdentifier extIdentifier = null;
		if (extIdentifierCollection != null && !extIdentifierCollection.isEmpty())
		{
			Iterator itr = extIdentifierCollection.iterator();
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
		//End Ashish

		if (Constants.ALIQUOT.equals(specimen.getLineage()))
		{
			//return true;
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
		//Logger.out.debug("End-Inside validate method of specimen bizlogic");
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

		if (specimen.getInitialquantity() == null || specimen.getInitialquantity().getValue() == null)
		{
			String quantityString = ApplicationProperties.getValue("specimen.quantity");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", quantityString));
		}

		/**
		 * This method gives first valid storage position to a specimen if it is not given. 
		 * If storage position is given it validates the storage position 
		 **/
		StorageContainerUtil.validateStorageLocationForSpecimen(specimen);

	}

	/**
	 * validates multiple specimen. Internally it for each specimen it innvokes validateSingleSpecimen. 
	 * @throws DAOException
	 * @throws DAOException
	 private boolean validateMultipleSpecimen(Map specimenMap, DAO dao, String operation) throws DAOException
	 {

	 populateStorageLocations(dao, specimenMap);
	 Iterator specimenIterator = specimenMap.keySet().iterator();
	 boolean result = true;
	 while (specimenIterator.hasNext() && result == true)
	 {
	 Specimen specimen = (Specimen) specimenIterator.next();
	 //validate single specimen
	 */

	/*	if (specimenCollectionGroup != null)
	 {

	 if (specimenCollectionGroup.getActivityStatus().equals(Constants.ACTIVITY_STATUS_CLOSED))
	 {
	 throw new DAOException("Specimen Collection Group " + ApplicationProperties.getValue("error.object.closed"));
	 }
	 
	 specimen.setSpecimenCollectionGroup(specimenCollectionGroup);
	 }  
	 List spgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.NAME, specimen.getSpecimenCollectionGroup().getName());
	 if(spgList!=null && !spgList.isEmpty())
	 {
	 specimenCollectionGroup = (SpecimenCollectionGroup) spgList.get(0);
	 }
	 
	 else if(specimen.getParentSpecimen()!=null)
	 {
	 List spList = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL, specimen.getParentSpecimen().getLabel());
	 if (spList != null && !spList.isEmpty())
	 {
	 Specimen sp = (Specimen) spList.get(0);
	 specimenCollectionGroup = sp.getSpecimenCollectionGroup();
	 }
	 
	 } */
	/*
	 // TODO uncomment code for label, performance
	 
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
	 derivedSpecimen.setSpecimenCharacteristics(specimen.getSpecimenCharacteristics());
	 derivedSpecimen.setSpecimenCollectionGroup(specimen.getSpecimenCollectionGroup());
	 derivedSpecimen.setPathologicalStatus(specimen.getPathologicalStatus());

	 try
	 {
	 result = validateSingleSpecimen(derivedSpecimen, dao, operation, false);
	 }
	 catch (DAOException daoException)
	 {
	 int j = i + 1;
	 String message = daoException.getMessage();
	 message += " (This message is for Derived Specimen " + j + " of Parent Specimen number " + specimen.getId() + ")";
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
	 */
	/**
	 * 
	 *  Start --> Code added for auto populating storage locations in case of multiple specimen
	 */

	/**
	 * This method populates SCG Id and storage locations for Multiple Specimen
	 * @param dao
	 * @param specimenMap
	 * @throws DAOException
	 private void populateStorageLocations(DAO dao, Map specimenMap) throws DAOException
	 {
	 final String saperator = "$";
	 Map tempSpecimenMap = new HashMap();
	 Iterator specimenIterator = specimenMap.keySet().iterator();
	 while (specimenIterator.hasNext())
	 {
	 Specimen specimen = (Specimen) specimenIterator.next();
	 //validate single specimen
	 if (specimen.getSpecimenCollectionGroup() != null)
	 {
	 String[] selectColumnName = {"collectionProtocolRegistration.id"};
	 String[] whereColumnName = {Constants.NAME};
	 String[] whereColumnCondition = {"="};
	 String[] whereColumnValue = {specimen.getSpecimenCollectionGroup().getName()};
	 List spCollGroupList = dao.retrieve(SpecimenCollectionGroup.class.getName(), selectColumnName, whereColumnName, whereColumnCondition,
	 whereColumnValue, null);
	 // TODO saperate calls for SCG - ID and cpid
	 // SCG - ID will be needed before populateStorageLocations
	 
	 // TODO test
	 if (!spCollGroupList.isEmpty())
	 {
	 //Object idList[] = (Object[]) spCollGroupList.get(0); // Move up + here
	 long cpId = ((Long) spCollGroupList.get(0)).longValue();
	 //Long scgId = (Long) idList[0]; // Move up 
	 //long cpId = ((Long) idList[0]).longValue();//here
	 //specimen.getSpecimenCollectionGroup().setId(scgId); // Move up
	 List tempListOfSpecimen = (ArrayList) tempSpecimenMap.get(cpId + saperator + specimen.getClassName());
	 if (tempListOfSpecimen == null)
	 {
	 tempListOfSpecimen = new ArrayList();
	 }
	 int i = 0;
	 for (; i < tempListOfSpecimen.size(); i++)
	 {
	 Specimen sp = (Specimen) tempListOfSpecimen.get(i);
	 
	 if ((sp.getId() != null) && (specimen.getId().longValue() < sp.getId().longValue()))
	 break;
	 }
	 tempListOfSpecimen.add(i, specimen);
	 tempSpecimenMap.put(cpId + saperator + specimen.getClassName(), tempListOfSpecimen);
	 
	 List listOfDerivedSpecimen = (ArrayList) specimenMap.get(specimen);
	 // TODO
	 if (listOfDerivedSpecimen != null)
	 {
	 for (int j = 0; j < listOfDerivedSpecimen.size(); j++)
	 {
	 Specimen tempDerivedSpecimen = (Specimen) listOfDerivedSpecimen.get(j);
	 String derivedKey = cpId + saperator + tempDerivedSpecimen.getClassName();
	 List listOfSpecimen = (ArrayList) tempSpecimenMap.get(derivedKey);
	 if (listOfSpecimen == null)
	 {
	 listOfSpecimen = new ArrayList();
	 }
	 listOfSpecimen.add(tempDerivedSpecimen);
	 tempSpecimenMap.put(derivedKey, listOfSpecimen);
	 }
	 }
	 }
	 }
	 }

	 
	 Iterator keyIterator = tempSpecimenMap.keySet().iterator();
	 while (keyIterator.hasNext())
	 {
	 String key = (String) keyIterator.next();
	 StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
	 Constants.STORAGE_CONTAINER_FORM_ID);
	 String split[] = key.split("[$]");
	 // TODO when moved to acion pass true
	 TreeMap containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen((Long.parseLong(split[0])), split[1], 0, "", false);
	 List listOfSpecimens = (ArrayList) tempSpecimenMap.get(key);
	 allocatePositionToSpecimensList(specimenMap, listOfSpecimens, containerMap);
	 }
	 }
	 */

	/**
	 * This function gets the default positions for list of specimens
	 * @param specimenMap
	 * @param listOfSpecimens
	 * @param containerMap
	 private void allocatePositionToSpecimensList(Map specimenMap, List listOfSpecimens, Map containerMap)
	 {
	 List newListOfSpecimen = new ArrayList();
	 */
	/*	for (int i = 0; i < listOfSpecimens.size(); i++)
	 {
	 Specimen tempSpecimen = (Specimen) listOfSpecimens.get(i);
	 newListOfSpecimen.add(tempSpecimen);
	 List listOfDerivedSpecimen = (ArrayList) specimenMap.get(tempSpecimen);
	 // TODO
	 if (listOfDerivedSpecimen != null)
	 {
	 for (int j = 0; j < listOfDerivedSpecimen.size(); j++)
	 {
	 Specimen tempDerivedSpecimen = (Specimen) listOfDerivedSpecimen.get(j);
	 newListOfSpecimen.add(tempDerivedSpecimen);
	 }
	 }
	 }
	 */
	/*
	 Iterator iterator = containerMap.keySet().iterator();
	 int i = 0;
	 while (iterator.hasNext())
	 {
	 NameValueBean nvb = (NameValueBean) iterator.next();
	 Map tempMap = (Map) containerMap.get(nvb);
	 if (tempMap.size() > 0)
	 {
	 boolean result = false;
	 for (; i < newListOfSpecimen.size(); i++)
	 {
	 Specimen tempSpecimen = (Specimen) newListOfSpecimen.get(i);
	 result = allocatePositionToSingleSpecimen(specimenMap, tempSpecimen, tempMap, nvb);
	 if (result == false) // container is exhausted
	 break;
	 }
	 if (result == true)
	 break;
	 }
	 }
	 }
	 */
	/**
	 *  This function gets the default position specimen,the position should not be used by any other specimen in specimenMap
	 *  This is required because we might have given the same position to another specimen.
	 * @param specimenMap
	 * @param tempSpecimen
	 * @param tempMap
	 * @param nvb
	 * @return
	 
	 private boolean allocatePositionToSingleSpecimen(Map specimenMap, Specimen tempSpecimen, Map tempMap, NameValueBean nvbForContainer)
	 {
	 Iterator itr = tempMap.keySet().iterator();
	 String containerId = nvbForContainer.getValue(), xPos, yPos;
	 while (itr.hasNext())
	 {
	 NameValueBean nvb = (NameValueBean) itr.next();
	 xPos = nvb.getValue();

	 List list = (List) tempMap.get(nvb);
	 for (int i = 0; i < list.size(); i++)
	 {
	 nvb = (NameValueBean) list.get(i);
	 yPos = nvb.getValue();
	 boolean result = checkPositionValidForSpecimen(containerId, xPos, yPos, specimenMap);
	 if (result == true)
	 {
	 StorageContainer tempStorageContainer = new StorageContainer();
	 tempStorageContainer.setId(new Long(Long.parseLong(containerId)));
	 tempSpecimen.setPositionDimensionOne(new Integer(Integer.parseInt(xPos)));
	 tempSpecimen.setPositionDimensionTwo(new Integer(Integer.parseInt(yPos)));
	 tempSpecimen.setStorageContainer(tempStorageContainer);
	 return true;
	 }
	 }

	 }
	 return false;
	 }
	 */
	/**
	 * This method checks whether the given parameters match with parameters in specimen Map
	 * @param containerId
	 * @param pos
	 * @param pos2
	 * @param specimenMap
	 * @return

	 private boolean checkPositionValidForSpecimen(String containerId, String xpos, String ypos, Map specimenMap)
	 {

	 // TODO can be optimised by passing list		
	 Iterator specimenIterator = specimenMap.keySet().iterator();
	 while (specimenIterator.hasNext())
	 {
	 Specimen specimen = (Specimen) specimenIterator.next();
	 boolean matchFound = checkMatchingPosition(containerId, xpos, ypos, specimen);
	 if (matchFound == true)
	 return false;

	 List derivedSpecimens = (List) specimenMap.get(specimen);

	 if (derivedSpecimens != null)
	 {
	 for (int i = 0; i < derivedSpecimens.size(); i++)
	 {

	 Specimen derivedSpecimen = (Specimen) derivedSpecimens.get(i);
	 matchFound = checkMatchingPosition(containerId, xpos, ypos, derivedSpecimen);
	 if (matchFound == true)
	 return false;
	 }
	 }
	 }
	 return true;
	 }
	 */
	/**
	 * This method checks whether the given parameters match with parameters of the specimen 
	 * @param containerId
	 * @param pos
	 * @param pos2
	 * @param specimen
	 * @return

	 private boolean checkMatchingPosition(String containerId, String xpos, String ypos, Specimen specimen)
	 {
	 String storageContainerId = "";
	 if (specimen.getStorageContainer() != null && specimen.getStorageContainer().getId() != null)
	 storageContainerId += specimen.getStorageContainer().getId();
	 else
	 return false;

	 String pos1 = specimen.getPositionDimensionOne() + "";
	 String pos2 = specimen.getPositionDimensionTwo() + "";
	 if (storageContainerId.equals(containerId) && xpos.equals(pos1) && ypos.equals(pos2))
	 return true;
	 return false;
	 }
	 */

	/**
	 * 
	 *  End --> Code added for auto populating storage locations in case of multiple specimen
	 */

	/* This function checks whether the storage position of a specimen is changed or not 
	 * & returns the status accordingly.
	 */
	private boolean isStoragePositionChanged(Specimen oldSpecimen, Specimen newSpecimen)
	{
		StorageContainer oldContainer = oldSpecimen.getStorageContainer();
		StorageContainer newContainer = newSpecimen.getStorageContainer();

		//Added for api: Jitendra
		if ((oldContainer == null && newContainer != null) || (oldContainer != null && newContainer == null))
		{
			return true;
		}

		if (oldContainer != null && newContainer != null)
		{
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
		else
		{
			return false;
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

	// added by Ashwin for bug id# 2476 
	/**
	 * Set event parameters from parent specimen to derived specimen
	 * @param parentSpecimen specimen
	 * @return set
	 */
	private Set populateDeriveSpecimenEventCollection(Specimen parentSpecimen, Specimen deriveSpecimen)
	{
		Set deriveEventCollection = new HashSet();
		Set parentSpecimeneventCollection = (Set) parentSpecimen.getSpecimenEventCollection();
		SpecimenEventParameters specimenEventParameters = null;
		SpecimenEventParameters deriveSpecimenEventParameters = null;

		try
		{
			if (parentSpecimeneventCollection != null)
			{
				for (Iterator iter = parentSpecimeneventCollection.iterator(); iter.hasNext();)
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

	//Mandar: 16-Jan-07 ConsentWithdrawl
	/*
	 * This method updates the consents and specimen based on the the consent withdrawal option.
	 */
	private void updateConsentWithdrawStatus(Specimen specimen, DAO dao, SessionDataBean sessionDataBean)
	{
		if(!specimen.getConsentWithdrawalOption().equalsIgnoreCase(Constants.WITHDRAW_RESPONSE_NOACTION ))
		{
			String consentWithdrawOption = specimen.getConsentWithdrawalOption();
			Collection consentTierStatusCollection = specimen.getConsentTierStatusCollection();
			Iterator itr = consentTierStatusCollection.iterator();
			while(itr.hasNext())
			{
				ConsentTierStatus status = (ConsentTierStatus)itr.next();
				long consentTierID = status.getConsentTier().getId().longValue();
				if(status.getStatus().equalsIgnoreCase(Constants.WITHDRAWN) )
				{
					Collection childSpecimens = specimen.getChildrenSpecimen();
					Iterator childItr = childSpecimens.iterator();  
					while(childItr.hasNext() )
					{
						Specimen childSpecimen = (Specimen)childItr.next();
						WithdrawConsentUtil.updateSpecimenStatus(childSpecimen, consentWithdrawOption, consentTierID, dao, sessionDataBean  );
					}
				}
			}
		}
	}
	
	/*
	 * This method is used to update the consent staus of child specimens as per the option selected by the user.
	 */
	private void updateConsentStatus(Specimen specimen, Specimen oldSpecimen)
	{
		if(!specimen.getApplyChangesTo().equalsIgnoreCase(Constants.APPLY_NONE ))
		{
			String applyChangesTo = specimen.getApplyChangesTo();
			Collection consentTierStatusCollection = specimen.getConsentTierStatusCollection();
			Collection oldConsentTierStatusCollection = oldSpecimen.getConsentTierStatusCollection();
			Iterator itr = consentTierStatusCollection.iterator();
			while(itr.hasNext())
			{
				ConsentTierStatus status = (ConsentTierStatus)itr.next();
				long consentTierID = status.getConsentTier().getId().longValue();
				String statusValue = status.getStatus();
				Collection childSpecimens = specimen.getChildrenSpecimen();
				Iterator childItr = childSpecimens.iterator();  
				while(childItr.hasNext() )
				{
					Specimen childSpecimen = (Specimen)childItr.next();
					WithdrawConsentUtil.updateSpecimenConsentStatus(childSpecimen, applyChangesTo, consentTierID, statusValue, consentTierStatusCollection, oldConsentTierStatusCollection);
				}
			}
		}
		
	}
}