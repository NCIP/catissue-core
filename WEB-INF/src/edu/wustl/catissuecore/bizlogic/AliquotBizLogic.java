/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	AliquotBizLogic is used to add new aliquots into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jun 27, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class AliquotBizLogic extends NewSpecimenBizLogic
{

	/**
	 * Saves the AliquotSpecimen object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Specimen aliquot = (Specimen) obj;
		String specimenKey = "Specimen:";
		Map aliquotMap = aliquot.getAliqoutMap();

		//Retrieving the parent specimen of the aliquot
		Specimen parentSpecimen = (Specimen) dao.retrieve(Specimen.class.getName(), aliquot.getParentSpecimen().getId());
		double dQuantity = 0;
		List aliquotList = new ArrayList();
		for (int i = 1; i <= aliquot.getNoOfAliquots(); i++)
		{
			//Preparing the map keys
			String quantityKey = specimenKey + i + "_quantity";
			String barcodeKey = specimenKey + i + "_barcode";
			String containerIdKey = specimenKey + i + "_StorageContainer_id";
			String posDim1Key = specimenKey + i + "_positionDimensionOne";
			String posDim2Key = specimenKey + i + "_positionDimensionTwo";
			String idKey = specimenKey + i + "_id";
			String labelKey = specimenKey + i + "_label";
			String virtuallyLocatedKey = specimenKey + i + "_virtuallyLocated";
			String storageContainerNameKey = specimenKey + i + "_stContainerName";
			//Retrieving the quantity, barcode & location values for each aliquot
			String quantity = (String) aliquotMap.get(quantityKey);
			String barcode = (String) aliquotMap.get(barcodeKey);
			String containerId = (String) aliquotMap.get(containerIdKey);
			String posDim1 = (String) aliquotMap.get(posDim1Key);
			String posDim2 = (String) aliquotMap.get(posDim2Key);
			String label = (String) aliquotMap.get(labelKey);
			String virtuallyLocated = (String) aliquotMap.get(virtuallyLocatedKey);
			Logger.out.info("---------------virtually located value:" + aliquotMap.get(virtuallyLocatedKey));
			dQuantity = dQuantity + Double.parseDouble(quantity);

			//Create an object of Specimen Subclass
			Specimen aliquotSpecimen = Utility.getSpecimen(parentSpecimen);

			/**
			 * Start: Change for API Search   --- Jitendra 06/10/2006
			 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
			 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
			 * So we removed default class level initialization on domain object and are initializing in method
			 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
			 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
			 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setSpecimenDefault(aliquotSpecimen);
			//End:- Change for API Search
			aliquotSpecimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
//			 set event parameters from parent specimen - added by Ashwin for bug id# 2476
			aliquotSpecimen.setSpecimenEventCollection(populateAliquoteSpecimenEventCollection(parentSpecimen,aliquotSpecimen));

			if (parentSpecimen != null)
			{
				//check for closed ParentSpecimen
				checkStatus(dao, parentSpecimen, "Parent Specimen");
				aliquotSpecimen.setParentSpecimen(parentSpecimen);
				aliquotSpecimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
				aliquotSpecimen.setType(parentSpecimen.getType());
				aliquotSpecimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());

				if (aliquotSpecimen instanceof MolecularSpecimen)
				{
					Double concentration = ((MolecularSpecimen) parentSpecimen).getConcentrationInMicrogramPerMicroliter();

					if (concentration != null)
					{
						((MolecularSpecimen) aliquotSpecimen).setConcentrationInMicrogramPerMicroliter(concentration);
					}
				}
			}

			//Setting quantities & barcode values
			aliquotSpecimen.setQuantity(new Quantity(quantity));
			aliquotSpecimen.setAvailableQuantity(new Quantity(quantity));

			//Explicity set barcode to null if it is empty as its a unique key in the database
			if (barcode != null && barcode.trim().length() == 0)
			{
				barcode = null;
			}

			aliquotSpecimen.setBarcode(barcode);

			if (label != null && label.trim().length() == 0)
			{
				label = null;
			}

			aliquotSpecimen.setLabel(label);

			try
			{
				if (virtuallyLocated == null && containerId != null)
				{
					//Setting the storage container of the aliquot
//					StorageContainer container = (StorageContainer) dao.retrieve(StorageContainer.class.getName(), new Long(containerId));
//
//					if (container != null)
//					{
					StorageContainer storageContainerObj = new StorageContainer();
					storageContainerObj.setId(new Long(containerId));
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = {"name"};
					String[] whereColumnName = {"id"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
					String[] whereColumnCondition = {"="};
					Object[] whereColumnValue = {new Long(containerId)};
					String joinCondition = null;

					List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

					if (!list.isEmpty())
					{
						storageContainerObj.setName((String)list.get(0));
						aliquotMap.put(storageContainerNameKey, (String)list.get(0));
					}

						//check for closed Storage Container
						checkStatus(dao, storageContainerObj, "Storage Container");

						StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
								Constants.STORAGE_CONTAINER_FORM_ID);

						//check for all validations on the storage container.
						storageContainerBizLogic.checkContainer(dao, containerId, posDim1, posDim2, sessionDataBean,false);
//						chkContainerValidForSpecimen(container, aliquotSpecimen);

						aliquotSpecimen.setStorageContainer(storageContainerObj);
						
//					}
				}
				else
				{
					aliquotSpecimen.setStorageContainer(null);

				}
			}
			catch (SMException sme)
			{
				sme.printStackTrace();
				throw handleSMException(sme);
			}

			//Setting the attributes - storage positions, available, acivity status & lineage
			if (virtuallyLocated == null && containerId != null)
			{
				aliquotSpecimen.setPositionDimensionOne(new Integer(posDim1));
				aliquotSpecimen.setPositionDimensionTwo(new Integer(posDim2));
			}
			else
			{
				aliquotSpecimen.setPositionDimensionOne(null);
				aliquotSpecimen.setPositionDimensionTwo(null);
			}
			aliquotSpecimen.setAvailable(Boolean.TRUE);
			aliquotSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
			aliquotSpecimen.setLineage(Constants.ALIQUOT);

			//Inserting an aliquot in the database
			dao.insert(aliquotSpecimen, sessionDataBean, true, false);//NEEDS TO BE FIXED FOR SECURE INSERT

			//Setting the identifier values in the map
			aliquotMap.put(idKey, String.valueOf(aliquotSpecimen.getId()));

			//TO BE DELETED LATER
			aliquot.setId(aliquotSpecimen.getId());

			//Adding dummy entry of external identifier for Query Module to fulfil the join condition
			Collection externalIdentifierCollection = new HashSet();
			ExternalIdentifier exId = new ExternalIdentifier();
			exId.setName(null);
			exId.setValue(null);
			exId.setSpecimen(aliquotSpecimen);
			externalIdentifierCollection.add(exId);
			dao.insert(exId, sessionDataBean, true, true);
			aliquotList.add(aliquotSpecimen);
		}
		/* Vaishali - Inserting authorization data */
		Iterator itr = aliquotList.iterator();
		while (itr.hasNext())
		{
			Specimen aliquotSpecimen = (Specimen) itr.next();

			Set protectionObjects = new HashSet();
			protectionObjects.add(aliquotSpecimen);
			if (aliquotSpecimen.getSpecimenCharacteristics() != null)
			{
				protectionObjects.add(aliquotSpecimen.getSpecimenCharacteristics());
			}
			try
			{
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null, protectionObjects, getDynamicGroups(aliquotSpecimen));
			}
			catch (SMException e)
			{
				throw handleSMException(e);
			}
		}

		//Setting the no. of aliquots in the map
		aliquotMap.put(Constants.SPECIMEN_COUNT, String.valueOf(aliquot.getNoOfAliquots()));

		//Adjusting the available quantity of parent specimen
		if (parentSpecimen != null)
		{
			double availableQuantity = parentSpecimen.getAvailableQuantity().getValue().doubleValue();
			availableQuantity = availableQuantity - dQuantity;
			parentSpecimen.setAvailableQuantity(new Quantity(String.valueOf(availableQuantity)));
			if (availableQuantity <= 0)
			{
				parentSpecimen.setAvailable(new Boolean(false));
			}

			//dao.update(parentSpecimen,sessionDataBean,Constants.IS_AUDITABLE_TRUE,Constants.IS_SECURE_UPDATE_TRUE, Constants.HAS_OBJECT_LEVEL_PRIVILEGE_FALSE);
		}

		//Populate aliquot map with parent specimen's data
		populateParentSpecimenData(aliquotMap, parentSpecimen);

		//Setting the aliquot map populated with identifiers
		aliquot.setAliqoutMap(aliquotMap);

		//Setting value of isAliquot as true for ForwardTo processor
		aliquot.setLineage(Constants.ALIQUOT);

	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Specimen aliquot = (Specimen) obj;
		String specimenKey = "Specimen:";
		Map aliquotMap = aliquot.getAliqoutMap();
		try
		{
			for (int i = 1; i <= aliquot.getNoOfAliquots(); i++)
			{

				String containerIdKey = specimenKey + i + "_StorageContainer_id";
				String storageContainerNameKey = specimenKey + i + "_stContainerName";
				String posDim1Key = specimenKey + i + "_positionDimensionOne";
				String posDim2Key = specimenKey + i + "_positionDimensionTwo";
				
				String virtuallyLocatedKey = specimenKey + i + "_virtuallyLocated";
				String virtuallyLocated = (String) aliquotMap.get(virtuallyLocatedKey);
				if (virtuallyLocated == null)
				{
					String contId = (String) aliquotMap.get(containerIdKey);
					String contName = (String) aliquotMap.get(storageContainerNameKey);
					String posOne = (String) aliquotMap.get(posDim1Key);
					String posTwo = (String) aliquotMap.get(posDim2Key);
					
					StorageContainer storageContainer = new StorageContainer();
					storageContainer.setId(new Long(contId));
					storageContainer.setName(contName);
					
					Map containerMap = StorageContainerUtil.getContainerMapFromCache();
					StorageContainerUtil.deleteSinglePositionInContainerMap(storageContainer,containerMap,new Integer(posOne).intValue(),new Integer(posTwo).intValue());
				}

			}
		}
		catch (Exception e)
		{

		}

	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		return true;
	}

	/* This function populates the parent specimen information in aliquot map. This map will be
	 * be retrieved in ForwardToProcessor & will be set in the request scope. Then the map will
	 * be retrieved from request scope in AliquotAction if the page is of "Aliquot Summary" &
	 * the formbean will be populated with the appropriate data.
	 */
	private void populateParentSpecimenData(Map aliquotMap, Specimen parentSpecimen)
	{
		aliquotMap.put(Constants.CDE_NAME_SPECIMEN_CLASS, parentSpecimen.getClassName());
		aliquotMap.put(Constants.CDE_NAME_SPECIMEN_TYPE, parentSpecimen.getType());
		aliquotMap.put(Constants.CDE_NAME_TISSUE_SITE, parentSpecimen.getSpecimenCharacteristics().getTissueSite());
		aliquotMap.put(Constants.CDE_NAME_TISSUE_SIDE, parentSpecimen.getSpecimenCharacteristics().getTissueSide());
		aliquotMap.put(Constants.CDE_NAME_PATHOLOGICAL_STATUS, parentSpecimen.getPathologicalStatus());
		aliquotMap.put(Constants.SPECIMEN_TYPE_QUANTITY, parentSpecimen.getAvailableQuantity().toString());

		if (parentSpecimen instanceof MolecularSpecimen)
		{
			aliquotMap.put("concentration", Utility.toString(((MolecularSpecimen) parentSpecimen).getConcentrationInMicrogramPerMicroliter()));
		}
		else
		{
			aliquotMap.put("concentration", "");
		}
	}

	public long getNextAvailableNumber(String sourceObjectName) throws DAOException
	{
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);

		dao.openSession(null);

		List list = dao.retrieve(sourceObjectName, selectColumnName);

		dao.closeSession();

		if (list != null && !list.isEmpty())
		{
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				if (!str.equals(""))
				{
					long no = Long.parseLong(str);
					return no + 1;
				}
			}
		}

		return 1;
	}
//	 set event parameters from parent specimen - added by Ashwin for bug id# 2476
	/**
	 * Set event parameters from parent specimen to aliquot
	 * @param parentSpecimen specimen
	 * @return set
	 */
	private Set populateAliquoteSpecimenEventCollection(Specimen parentSpecimen,Specimen aliquotSpecimen)
	{
		Set aliquoteEventCollection = new HashSet();
		Set parentSpecimeneventCollection = (Set) parentSpecimen.getSpecimenEventCollection();
		SpecimenEventParameters specimenEventParameters = null;
		SpecimenEventParameters aliquotSpecimenEventParameters = null;
		
		try
		{
			if (parentSpecimeneventCollection != null)
			{	
				for (Iterator iter = parentSpecimeneventCollection.iterator(); iter.hasNext();)
				{
					specimenEventParameters = (SpecimenEventParameters) iter.next();
					aliquotSpecimenEventParameters = (SpecimenEventParameters) specimenEventParameters.clone();
					aliquotSpecimenEventParameters.setId(null);
					aliquotSpecimenEventParameters.setSpecimen(aliquotSpecimen);
					aliquoteEventCollection.add(aliquotSpecimenEventParameters);
				}
			}
		}
		catch (CloneNotSupportedException exception)
		{
			exception.printStackTrace();
		}
		
		return aliquoteEventCollection;
	}
}