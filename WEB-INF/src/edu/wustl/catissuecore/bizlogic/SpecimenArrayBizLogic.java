/*
 * <p>Title: SpecimenArrayBizLogic Class </p>
 * <p>Description:This class performs business level logic for Specimen Array</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Aug 28,2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>This class initializes the fields of SpecimenArrayBizLogic.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayBizLogic extends DefaultBizLogic
{

	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object, edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;

		try
		{
			//Added for Api Search
			checkStorageContainerAvailablePos(specimenArray, dao, sessionDataBean);
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}

		doUpdateSpecimenArrayContents(specimenArray, null, dao, sessionDataBean, true);

		dao.insert(specimenArray.getCapacity(), sessionDataBean, true, false);
		dao.insert(specimenArray, sessionDataBean, true, false);
		SpecimenArrayContent specimenArrayContent = null;
		// TODO move this method to HibernateDAOImpl for common use (for collection insertion)
		for (Iterator iter = specimenArray.getSpecimenArrayContentCollection().iterator(); iter.hasNext();)
		{
			specimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent.setSpecimenArray(specimenArray);
			dao.insert(specimenArrayContent, sessionDataBean, true, false);
		}
	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		try
		{
			if (specimenArray.getLocatedAtPosition() != null && specimenArray.getLocatedAtPosition().getParentContainer() != null)
			{

				Map containerMap = StorageContainerUtil.getContainerMapFromCache();
				//				if(specimenArray.getLocatedAtPosition() != null)
				//				{
				StorageContainerUtil.deleteSinglePositionInContainerMap((StorageContainer) specimenArray.getLocatedAtPosition().getParentContainer(),
						containerMap, specimenArray.getLocatedAtPosition().getPositionDimensionOne().intValue(), specimenArray.getLocatedAtPosition()
								.getPositionDimensionTwo().intValue());
				//				}

			}
		}
		catch (Exception e)
		{
			throw new DAOException("Failed to delete Position from Storage Container cache");
		}

	}

	public void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException,
			UserNotAuthorizedException
	{
		try
		{
			Map containerMap = StorageContainerUtil.getContainerMapFromCache();
			SpecimenArray specimenArrayCurrentObject = (SpecimenArray) currentObj;
			SpecimenArray specimenArrayOldObject = (SpecimenArray) oldObj;

			if (specimenArrayOldObject != null
					&& specimenArrayOldObject.getLocatedAtPosition() != null
					&& specimenArrayOldObject.getLocatedAtPosition().getParentContainer() != null) {
				Container oldParentCont = (Container) HibernateMetaData
						.getProxyObjectImpl(specimenArrayOldObject.getLocatedAtPosition()
								.getParentContainer());
				StorageContainerUtil.insertSinglePositionInContainerMap(
						oldParentCont, containerMap, specimenArrayOldObject
								.getLocatedAtPosition()
								.getPositionDimensionOne().intValue(),
								specimenArrayOldObject.getLocatedAtPosition()
								.getPositionDimensionTwo().intValue());
			}
			if (specimenArrayCurrentObject != null
					&& specimenArrayCurrentObject.getLocatedAtPosition() != null
					&& specimenArrayCurrentObject.getLocatedAtPosition()
							.getParentContainer() != null) {
				Container currentParentCont = (Container) specimenArrayCurrentObject
						.getLocatedAtPosition().getParentContainer();
				StorageContainerUtil.deleteSinglePositionInContainerMap(
						currentParentCont, containerMap, specimenArrayCurrentObject
								.getLocatedAtPosition()
								.getPositionDimensionOne().intValue(),
								specimenArrayCurrentObject.getLocatedAtPosition()
								.getPositionDimensionTwo().intValue());
			}
		
			if (Constants.ACTIVITY_STATUS_DISABLED.equals(specimenArrayCurrentObject.getActivityStatus()))
			{
				Map disabledConts = getContForDisabledSpecimenArrayFromCache();

				Set keySet = disabledConts.keySet();
				Iterator itr = keySet.iterator();
				while (itr.hasNext())
				{
					String Id = (String) itr.next();
					Map disabledContDetails = (TreeMap) disabledConts.get(Id);
					String contNameKey = "StorageContName";
					//String contIdKey = "StorageContIdKey";
					String pos1Key = "pos1";
					String pos2Key = "pos2";

					StorageContainer cont = new StorageContainer();
					cont.setId(new Long(Id));
					cont.setName((String) disabledContDetails.get(contNameKey));
					int x = ((Integer) disabledContDetails.get(pos1Key)).intValue();
					int y = ((Integer) disabledContDetails.get(pos2Key)).intValue();

					StorageContainerUtil.insertSinglePositionInContainerMap(cont, containerMap, x, y);
				}

			}
		}
		catch (Exception e)
		{
			throw new BizLogicException("Failed to Update Storage Container Positions");
		}
	}

	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#update(edu.wustl.common.dao.DAO, java.lang.Object, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */

	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;
		SpecimenArray oldSpecimenArray = (SpecimenArray) oldObj;

		//		try
		//		{
		//			//Added for Api Search
		//			checkStorageContainerAvailablePos(specimenArray,dao,sessionDataBean);
		//		}
		//		catch (SMException e)
		//		{
		//			throw handleSMException(e);
		//		}

		boolean flag = true;
		if (specimenArray.getLocatedAtPosition().getParentContainer().getId().longValue() == oldSpecimenArray.getLocatedAtPosition()
				.getParentContainer().getId().longValue()
				//	&& specimenArray.getLocatedAtPosition() != null 
				&& oldSpecimenArray.getLocatedAtPosition() != null
				&& specimenArray.getLocatedAtPosition().getPositionDimensionOne().longValue() == oldSpecimenArray.getLocatedAtPosition()
						.getPositionDimensionOne().longValue()
				&& specimenArray.getLocatedAtPosition().getPositionDimensionTwo().longValue() == oldSpecimenArray.getLocatedAtPosition()
						.getPositionDimensionTwo().longValue())
		{
			flag = false;
		}

		if (flag)
		{
			try
			{
				StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
						Constants.STORAGE_CONTAINER_FORM_ID);
				//check for all validations on the storage container.
				//				if(specimenArray.getLocatedAtPosition() != null)
				//				{
				storageContainerBizLogic.checkContainer(dao, specimenArray.getLocatedAtPosition().getParentContainer().getId().toString(),
						specimenArray.getLocatedAtPosition().getPositionDimensionOne().toString(), specimenArray.getLocatedAtPosition()
								.getPositionDimensionTwo().toString(), sessionDataBean, false,null);
				//				}
			}
			catch (SMException sme)
			{
				sme.printStackTrace();
				throw handleSMException(sme);
			}
		}
		doUpdateSpecimenArrayContents(specimenArray, oldSpecimenArray, dao, sessionDataBean, false);

		dao.update(specimenArray.getCapacity(), sessionDataBean, true, false, false);
		dao.update(specimenArray, sessionDataBean, true, false, false);
		SpecimenArrayContent specimenArrayContent = null;
		//SpecimenArray oldSpecimenArray = (SpecimenArray) oldObj;
		Collection oldSpecArrayContents = ((SpecimenArray) oldObj).getSpecimenArrayContentCollection();

		for (Iterator iter = specimenArray.getSpecimenArrayContentCollection().iterator(); iter.hasNext();)
		{
			specimenArrayContent = (SpecimenArrayContent) iter.next();
			specimenArrayContent.setSpecimenArray(specimenArray);
			// increment by 1 because of array index starts from 0.
			if (specimenArrayContent.getPositionDimensionOne() != null)
			{
				//Bug: 2365: grid location of parent array was getting changed 
				if (specimenArray.isAliquot())
				{
					specimenArrayContent.setPositionDimensionOne(new Integer(specimenArrayContent.getPositionDimensionOne().intValue()));
					specimenArrayContent.setPositionDimensionTwo(new Integer(specimenArrayContent.getPositionDimensionTwo().intValue()));
				}
				else
				{
					specimenArrayContent.setPositionDimensionOne(new Integer(specimenArrayContent.getPositionDimensionOne().intValue() + 1));
					specimenArrayContent.setPositionDimensionTwo(new Integer(specimenArrayContent.getPositionDimensionTwo().intValue() + 1));
				}
			}

			if (checkExistSpecimenArrayContent(specimenArrayContent, oldSpecArrayContents) == null)
			{
				dao.insert(specimenArrayContent, sessionDataBean, true, false);
			}
			else
			{
				dao.update(specimenArrayContent, sessionDataBean, true, false, false);
			}
		}

		if (Constants.ACTIVITY_STATUS_DISABLED.equals(specimenArray.getActivityStatus()))
		{
			Map disabledCont = null;
			try
			{
				disabledCont = getContForDisabledSpecimenArrayFromCache();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			if (disabledCont == null)
			{
				disabledCont = new TreeMap();
			}
			Object objectContainer = null;
			if (specimenArray.getLocatedAtPosition() != null && specimenArray.getLocatedAtPosition().getOccupiedContainer() != null
					&& specimenArray.getLocatedAtPosition().getOccupiedContainer().getId() != null)
			{
				objectContainer = dao.retrieve(StorageContainer.class.getName(), specimenArray.getLocatedAtPosition().getOccupiedContainer().getId());
			}
			if (objectContainer != null)
			{

				SpecimenArray storageContainer = (SpecimenArray) objectContainer;
				addEntriesInDisabledMap(specimenArray, storageContainer, disabledCont);
			}

			ContainerPosition prevPosition = specimenArray.getLocatedAtPosition();
			
			specimenArray.setLocatedAtPosition(null);
			dao.update(specimenArray, sessionDataBean, true, false, false);
			
			if(prevPosition!=null)
			{
				dao.delete(prevPosition);
			}	

			try
			{
				CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
				catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_CONTAINER_FOR_DISABLED_SPECIENARRAY, (Serializable) disabledCont);
			}
			catch (CacheException e)
			{

			}
		}
	}

	/**
	 * @param specimenArrayContent array contents
	 * @param checkExitSpecimenArrayContent spec array contents
	 * @return whether it is new or old
	 */
	private SpecimenArrayContent checkExistSpecimenArrayContent(SpecimenArrayContent specimenArrayContent, Collection specArrayContentCollection)
	{
		boolean isNew = true;
		SpecimenArrayContent arrayContent = null;

		for (Iterator iter = specArrayContentCollection.iterator(); iter.hasNext();)
		{
			arrayContent = (SpecimenArrayContent) iter.next();
			if (specimenArrayContent.getId() == null)
			{
				isNew = true;
				break;
			}
			else if (arrayContent.getId() != null)
			{
				if (arrayContent.getId().longValue() == specimenArrayContent.getId().longValue())
				{
					isNew = false;
					break;
				}
			}
		}
		if (isNew)
		{
			arrayContent = null;
		}
		return arrayContent;
	}

	/**
	 * @param specimenArray specimen array
	 * @param dao dao
	 * @param sessionDataBean session data bean
	 * @param isInsertOperation is insert operation
	 * @throws DAOException 
	 * @throws UserNotAuthorizedException
	 */
	private void doUpdateSpecimenArrayContents(SpecimenArray specimenArray, SpecimenArray oldSpecimenArray, DAO dao, SessionDataBean sessionDataBean,
			boolean isInsertOperation) throws DAOException, UserNotAuthorizedException
	{
		Collection oldSpecimenArrayContentCollection = null;
		if (oldSpecimenArray != null)
		{
			oldSpecimenArrayContentCollection = oldSpecimenArray.getSpecimenArrayContentCollection();
		}

		Collection specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();
		Collection updatedSpecArrayContentCollection = new HashSet();
		SpecimenArrayContent specimenArrayContent = null;
		Specimen specimen = null;
		//		try
		//		{
		//			checkStorageContainerAvailablePos(specimenArray,dao,sessionDataBean);
		//		}
		//		catch (SMException e)
		//		{
		//			throw handleSMException(e);
		//		}

		if (specimenArrayContentCollection != null && !specimenArrayContentCollection.isEmpty())
		{
			double quantity = 0.0;
			// fetch array type to check specimen class
			Object object = dao.retrieve(SpecimenArrayType.class.getName(), specimenArray.getSpecimenArrayType().getId());
			SpecimenArrayType arrayType = null;

			if (object != null)
			{
				arrayType = (SpecimenArrayType) object;
			}

			for (Iterator iter = specimenArrayContentCollection.iterator(); iter.hasNext();)
			{
				specimenArrayContent = (SpecimenArrayContent) iter.next();

				/**
				 * Start: Change for API Search   --- Jitendra 06/10/2006
				 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
				 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
				 * So we removed default class level initialization on domain object and are initializing in method
				 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
				 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
				 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
				 */
				//ApiSearchUtil.setSpecimenArrayContentDefault(specimenArrayContent);
				//End:- Change for API Search 
				specimen = getSpecimen(dao, specimenArrayContent);
				if (specimen != null)
				{
					// check whether array & specimen are compatible on the basis of class
					if (!isArrayAndSpecimenCompatibile(arrayType, specimen))
					{
						throw new DAOException(Constants.ARRAY_SPEC_NOT_COMPATIBLE_EXCEPTION_MESSAGE);
					}

					// set quantity object to null when there is no value.. [due to Hibernate exception]
					if (specimenArrayContent.getInitialQuantity() != null)
					{
						if (specimenArrayContent.getInitialQuantity() == null)
						{
							specimenArrayContent.setInitialQuantity(null);
						}
					}

					// if molecular then check available quantity
					if (specimen instanceof MolecularSpecimen)
					{
						if (specimenArrayContent.getInitialQuantity() != null)
						{
							quantity = specimenArrayContent.getInitialQuantity().doubleValue();
							double tempQuantity = quantity;
							SpecimenArrayContent oldArrayContent = null;
							// incase if specimenArray is created from aliquot page, then skip the Available quantity of specimen. 
							if (!specimenArray.isAliquot())
							{
								//in case of update, reduce specimen's quantity by difference of new specimenArrayContent's quantiy
								//and old specimenArrayContent's quantiy.
								if (oldSpecimenArrayContentCollection != null)
								{
									oldArrayContent = checkExistSpecimenArrayContent(specimenArrayContent, oldSpecimenArrayContentCollection);
									if (oldArrayContent != null)
									{
										quantity = quantity - oldArrayContent.getInitialQuantity().doubleValue();
									}
								}

								if (!isAvailableQty(specimen, quantity))
								{
									throw new DAOException(" Quantity '" + tempQuantity + "' should be less than current Distributed Quantity '"
											+ specimen.getAvailableQuantity().doubleValue() + "' of specimen :: " + specimen.getLabel());
								}
							}
						}
						else
						{
							throw new DAOException(Constants.ARRAY_MOLECULAR_QUAN_EXCEPTION_MESSAGE + specimen.getLabel());
						}
					}
					specimenArrayContent.setSpecimen(specimen);
					//Added by jitendra 
					if (specimenArrayContent.getPositionDimensionOne() == null || specimenArrayContent.getPositionDimensionTwo() == null)
					{
						throw new DAOException(ApplicationProperties.getValue("array.contentPosition.err.msg"));
					}
					updatedSpecArrayContentCollection.add(specimenArrayContent);
				}
			}
		}

		// There should be at least one valid specimen in array
		if (updatedSpecArrayContentCollection.isEmpty())
		{
			throw new DAOException(Constants.ARRAY_NO_SPECIMEN__EXCEPTION_MESSAGE);
		}

		//In case of update, if specimen is removed from specimen array, then specimen array content's quantity 
		//should get added into specimen's available quantity.
		if (!isInsertOperation)
		{
			Iterator itr = oldSpecimenArrayContentCollection.iterator();
			while (itr.hasNext())
			{
				SpecimenArrayContent oldSpecimenArrayContent = (SpecimenArrayContent) itr.next();
				SpecimenArrayContent newSpecimenArrayContent = checkExistSpecimenArrayContent(oldSpecimenArrayContent, specimenArrayContentCollection);
				if (newSpecimenArrayContent == null || newSpecimenArrayContent.getSpecimen().getLabel() == null
						|| newSpecimenArrayContent.getSpecimen().getLabel().equals(""))
				{
					Specimen oldSpecimen = getSpecimen(dao, oldSpecimenArrayContent);
					if (oldSpecimen != null && oldSpecimen instanceof MolecularSpecimen)
					{
						Double oldQuantity = oldSpecimenArrayContent.getInitialQuantity();
						Double quantity = oldSpecimen.getAvailableQuantity();
						double newQuantity = quantity.doubleValue() + oldQuantity.doubleValue();
						quantity = newQuantity;
						oldSpecimen.setAvailableQuantity(quantity);
						dao.update(oldSpecimen, sessionDataBean, true, false, false);
					}
				}
			}
		}
		specimenArray.setSpecimenArrayContentCollection(updatedSpecArrayContentCollection);
	}

	private void checkStorageContainerAvailablePos(SpecimenArray specimenArray, DAO dao, SessionDataBean sessionDataBean) throws DAOException,
			SMException
	{
		if (specimenArray.getLocatedAtPosition() != null && specimenArray.getLocatedAtPosition().getParentContainer() != null)
		{
			retriveScId(dao, specimenArray);
			retriveScName(specimenArray, dao);
			StorageContainer storageContainerObj = (StorageContainer) specimenArray.getLocatedAtPosition().getParentContainer();
			//check for closed Storage Container
			checkStatus(dao, storageContainerObj, "Storage Container");
			StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.STORAGE_CONTAINER_FORM_ID);
			// --- check for all validations on the storage container.
			storageContainerBizLogic.checkContainer(dao, storageContainerObj.getId().toString(), specimenArray.getLocatedAtPosition()
					.getPositionDimensionOne().toString(), specimenArray.getLocatedAtPosition().getPositionDimensionTwo().toString(),
					sessionDataBean, false,null);
			specimenArray.getLocatedAtPosition().setParentContainer((Container) storageContainerObj);
		}
	}

	/**
	 * @param specimenArray
	 * @param dao
	 * @throws DAOException
	 */
	private void retriveScName(SpecimenArray specimenArray, DAO dao) throws DAOException
	{
		if (specimenArray.getLocatedAtPosition().getParentContainer().getId() != null)
		{
			StorageContainer storageContainerObj = new StorageContainer();
			storageContainerObj.setId(specimenArray.getLocatedAtPosition().getParentContainer().getId());
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"name"};
			String[] whereColumnName = {"id"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {specimenArray.getLocatedAtPosition().getParentContainer().getId()};
			String joinCondition = null;

			List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

			if (!list.isEmpty())
			{
				storageContainerObj.setName((String) list.get(0));
				specimenArray.getLocatedAtPosition().setParentContainer(storageContainerObj);
			}
		}
	}

	/**
	 * @param specimen specimen
	 * @param quantity quantity
	 * @return whether the quantity is available.
	 */
	private boolean isAvailableQty(Specimen specimen, double quantity)
	{

		if (specimen instanceof MolecularSpecimen)
		{
			MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			double availabeQty = Double.parseDouble(molecularSpecimen.getAvailableQuantity().toString());//molecularSpecimen.getAvailableQuantityInMicrogram().doubleValue();
			if (quantity > availabeQty)
				return false;
			else
			{
				availabeQty = availabeQty - quantity;
				molecularSpecimen.setAvailableQuantity(new Double(availabeQty));//molecularSpecimen.setAvailableQuantityInMicrogram(new Double(availabeQty));
			}
		}
		return true;
	}

	/**
	 * @param dao dao
	 * @param arrayContent
	 * @return
	 * @throws DAOException
	 */
	private Specimen getSpecimen(DAO dao, SpecimenArrayContent arrayContent) throws DAOException
	{
		//get list of Participant's names
		Specimen specimen = arrayContent.getSpecimen();

		if (specimen != null)
		{
			String columnName = null;
			String columnValue = null;

			if ((specimen.getLabel() != null) && (!specimen.getLabel().trim().equals("")))
			{
				columnName = Constants.SPECIMEN_LABEL_COLUMN_NAME;
				columnValue = specimen.getLabel();
			}
			else if ((specimen.getBarcode() != null) && (!specimen.getBarcode().trim().equals("")))
			{
				columnName = Constants.SPECIMEN_BARCODE_COLUMN_NAME;
				columnValue = specimen.getBarcode();
			}
			else
			{
				return null;
			}
			String sourceObjectName = Specimen.class.getName();
			String whereColumnName = columnName;
			String whereColumnValue = columnValue;

			List list = dao.retrieve(sourceObjectName, whereColumnName, whereColumnValue);
			if (!list.isEmpty())
			{
				specimen = (Specimen) list.get(0);
				/**
				 * Name : Virender
				 * Reviewer: Prafull
				 * Calling Domain object from Proxy Object
				 */
				specimen = (Specimen) HibernateMetaData.getProxyObjectImpl(specimen);
				String activityStatus = specimen.getActivityStatus();
				//Bug: 2872:-  User should not able to add close/disable specimen in Specimen Array.
				if (!activityStatus.equals(Constants.ACTIVITY_STATUS_ACTIVE))
				{
					throw new DAOException(Constants.ARRAY_SPECIMEN_NOT_ACTIVE_EXCEPTION_MESSAGE + columnValue);
				}
				//return specimenCollectionGroup;
			}
			else
			{
				throw new DAOException(Constants.ARRAY_SPECIMEN_DOES_NOT_EXIST_EXCEPTION_MESSAGE + columnValue);
			}
		}
		return specimen;
	}

	/**
	 * @param array array
	 * @param specimen specimen 
	 * @return true if compatible else false 
	 *  |
	 *  | 
	 *   ----- on the basis of specimen class
	 */
	private boolean isArrayAndSpecimenCompatibile(SpecimenArrayType arrayType, Specimen specimen)
	{
		boolean compatible = false;
		String arraySpecimenClassName = arrayType.getSpecimenClass();
		String specSpecimenClassName = getClassName(specimen);

		if (arraySpecimenClassName.equals(specSpecimenClassName))
		{
			compatible = true;
		}
		return compatible;
	}

	/**
	 * This function returns the actual type of the specimen i.e Cell / Fluid / Molecular / Tissue.
	 */

	public final String getClassName(Specimen specimen)
	{
		String className = "";

		if (specimen instanceof CellSpecimen)
		{
			className = Constants.CELL;
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			className = Constants.MOLECULAR;
		}
		else if (specimen instanceof FluidSpecimen)
		{
			className = Constants.FLUID;
		}
		else if (specimen instanceof TissueSpecimen)
		{
			className = Constants.TISSUE;
		}
		return className;
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;

		/**
		 * Start: Change for API Search   --- Jitendra 06/10/2006
		 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
		 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
		 * So we removed default class level initialization on domain object and are initializing in method
		 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
		 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
		 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
		 */
		ApiSearchUtil.setSpecimenArrayDefault(specimenArray);
		//End:- Change for API Search

		//Added by Ashish	
		if (specimenArray == null)
		{
			throw new DAOException(ApplicationProperties.getValue("domain.object.null.err.msg", "Specimen Array"));
		}

		Validator validator = new Validator();

		if (specimenArray.getActivityStatus() == null)
		{
			specimenArray.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		}
		String message = "";
		if (specimenArray.getSpecimenArrayType() == null || specimenArray.getSpecimenArrayType().getId() == null
				|| specimenArray.getSpecimenArrayType().getId().longValue() == -1)
		{
			message = ApplicationProperties.getValue("array.arrayType");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		//fetch array type to check specimen class
		Object object = dao.retrieve(SpecimenArrayType.class.getName(), specimenArray.getSpecimenArrayType().getId());
		SpecimenArrayType specimenArrayType = null;

		if (object != null)
		{
			specimenArrayType = (SpecimenArrayType) object;
		}
		else
		{
			message = ApplicationProperties.getValue("array.arrayType");
			throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
		}

		//	validate name of array
		if (validator.isEmpty(specimenArray.getName()))
		{
			message = ApplicationProperties.getValue("array.arrayLabel");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		// validate storage position
		/*if (specimenArray.getPositionDimensionOne() == null || specimenArray.getPositionDimensionTwo() == null
		 || !validator.isNumeric(String.valueOf(specimenArray.getPositionDimensionOne()), 1)
		 || !validator.isNumeric(String.valueOf(specimenArray.getPositionDimensionTwo()), 1)
		 || (!validator.isNumeric(String.valueOf(specimenArray.getStorageContainer().getId()), 1) && validator.isEmpty(specimenArray.getStorageContainer().getName())))*/
		if ((!validator.isNumeric(String.valueOf(specimenArray.getLocatedAtPosition().getParentContainer().getId()), 1) && validator
				.isEmpty(specimenArray.getLocatedAtPosition().getParentContainer().getName())))
		{
			message = ApplicationProperties.getValue("array.positionInStorageContainer");
			throw new DAOException(ApplicationProperties.getValue("errors.item.format", message));
		}

		if (specimenArray.getLocatedAtPosition() != null && specimenArray.getLocatedAtPosition().getParentContainer() != null)
		{
			retriveScId(dao, specimenArray);
		}

		Integer xPos = null;
		Integer yPos = null;
		if (specimenArray.getLocatedAtPosition() != null)
		{
			xPos = specimenArray.getLocatedAtPosition().getPositionDimensionOne();
			yPos = specimenArray.getLocatedAtPosition().getPositionDimensionTwo();
		}
		boolean isContainerFull = false;
		/**
		 *  Following code is added to set the x and y dimension in case only storage container is given 
		 *  and x and y positions are not given 
		 */
		if (xPos == null || yPos == null)
		{
			isContainerFull = true;
			Map containerMapFromCache = null;
			containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
			isContainerFull = setPositions(specimenArray, isContainerFull, containerMapFromCache);
			xPos = specimenArray.getLocatedAtPosition().getPositionDimensionOne();
			yPos = specimenArray.getLocatedAtPosition().getPositionDimensionTwo();
		}

		if (isContainerFull)
		{
			throw new DAOException("The Storage Container you specified is full");
		}
		else if (xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
		{
			throw new DAOException(ApplicationProperties.getValue("errors.item.format", ApplicationProperties
					.getValue("array.positionInStorageContainer")));
		}

		// validate user 
		if (specimenArray.getCreatedBy() == null || specimenArray.getCreatedBy().getId() == null
				|| !validator.isValidOption(String.valueOf(specimenArray.getCreatedBy().getId())))
		{
			message = ApplicationProperties.getValue("array.user");
			throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
		}

		//validate capacity 
		if (specimenArray.getCapacity() == null || specimenArray.getCapacity().getOneDimensionCapacity() == null
				|| specimenArray.getCapacity().getTwoDimensionCapacity() == null)
		{
			throw new DAOException(ApplicationProperties.getValue("array.capacity.err.msg"));
		}

		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
		String specimenClass = specimenArrayType.getSpecimenClass();

		if (!isValidClassName(specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}

		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}

		Collection specimenTypes = specimenArrayType.getSpecimenTypeCollection();
		if (specimenTypes == null || specimenTypes.isEmpty())
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
		}
		else
		{
			Iterator itr = specimenTypes.iterator();
			while (itr.hasNext())
			{
				String specimenType = (String) itr.next();
				if (!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass), specimenType))
				{
					throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
				}
			}
		}
		/*Bug no. 7810
		 Bug Description : Incompatible specimen gets added to the specimen array*/
		Collection specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();
		if (!specimenArrayContentCollection.isEmpty())
		{
			Iterator iterator = specimenArrayContentCollection.iterator();
			while (iterator.hasNext())
			{
				SpecimenArrayContent tempSpecimenArrayContent = (SpecimenArrayContent) iterator.next();
				Specimen tempSpecimen = getSpecimen(dao, tempSpecimenArrayContent);
				if (specimenClass != null && tempSpecimen != null && !specimenClass.equalsIgnoreCase(tempSpecimen.getClassName()))
				{
					message = getMessage(tempSpecimenArrayContent);
					throw new DAOException(ApplicationProperties.getValue("class.name.different", message));
				}
				if (specimenTypes != null && !specimenTypes.isEmpty() && tempSpecimen != null)
				{
					if (!specimenTypes.contains(tempSpecimen.getSpecimenType()))
					{
						message = getMessage(tempSpecimenArrayContent);
						throw new DAOException(ApplicationProperties.getValue("type.different", message));
					}
				}
			}
		}
		else
		{
			throw new DAOException("Specimen Array for uploading is null");
		}
		return true;
	}

	/**
	 * @param specimenArray
	 * @param isContainerFull
	 * @param containerMapFromCache
	 * @return
	 */
	private boolean setPositions(SpecimenArray specimenArray, boolean isContainerFull, Map containerMapFromCache)
	{
		if (containerMapFromCache != null)
		{
			Iterator itr = containerMapFromCache.keySet().iterator();
			while (itr.hasNext())
			{
				NameValueBean nvb = (NameValueBean) itr.next();
				if (nvb.getValue().toString().equals(specimenArray.getLocatedAtPosition().getParentContainer().getId().toString()))
				{
					Map tempMap = (Map) containerMapFromCache.get(nvb);
					Iterator tempIterator = tempMap.keySet().iterator();
					NameValueBean nvb1 = (NameValueBean) tempIterator.next();
					List list = (List) tempMap.get(nvb1);
					NameValueBean nvb2 = (NameValueBean) list.get(0);
					ContainerPosition locatedAtPos = specimenArray.getLocatedAtPosition();
					locatedAtPos.setPositionDimensionOne(new Integer(nvb1.getValue()));
					locatedAtPos.setPositionDimensionTwo(new Integer(nvb2.getValue()));
					isContainerFull = false;
					break;
				}

			}
		}
		return isContainerFull;
	}

	/**
	 * @param dao
	 * @param specimenArray
	 * @throws DAOException
	 */
	private void retriveScId(DAO dao, SpecimenArray specimenArray) throws DAOException
	{
		String message = null;
		if (specimenArray.getLocatedAtPosition() != null && specimenArray.getLocatedAtPosition().getParentContainer() != null
				&& specimenArray.getLocatedAtPosition().getParentContainer().getName() != null)
		{

			StorageContainer storageContainerObj = (StorageContainer) HibernateMetaData.getProxyObjectImpl(specimenArray.getLocatedAtPosition()
					.getParentContainer());
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"id"};
			String[] whereColumnName = {"name"};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {specimenArray.getLocatedAtPosition().getParentContainer().getName()};
			String joinCondition = null;

			List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

			if (!list.isEmpty())
			{
				storageContainerObj.setId((Long) list.get(0));
				specimenArray.getLocatedAtPosition().setParentContainer(storageContainerObj);
			}
			else
			{
				message = ApplicationProperties.getValue("array.positionInStorageContainer");
				throw new DAOException(ApplicationProperties.getValue("errors.invalid", message));
			}
		}
	}

	private boolean isValidClassName(String className)
	{
		if ((className != null) && (className.equalsIgnoreCase(Constants.CELL)) || (className.equalsIgnoreCase(Constants.MOLECULAR))
				|| (className.equalsIgnoreCase(Constants.FLUID)) || (className.equalsIgnoreCase(Constants.TISSUE)))
		{
			return true;
		}
		return false;
	}

	/**
	 * get Unique index to be appended to Name
	 * @return unique no. to be appended to array name
	 * @throws DAOException
	 */
	public int getUniqueIndexForName() throws DAOException
	{
		String sourceObjectName = "CATISSUE_CONTAINER";
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_IDENTIFIER"};
		return Utility.getNextUniqueNo(sourceObjectName, selectColumnName);
	}

	/**
	 * @param tempSpecimenArrayContent
	 * @return the message to be displayed when exception occurs
	 */
	public String getMessage(SpecimenArrayContent tempSpecimenArrayContent)
	{
		Specimen specimen = tempSpecimenArrayContent.getSpecimen();
		String msg = " ";
		if (specimen != null)
		{
			if ((specimen.getLabel() != null) && (!specimen.getLabel().trim().equals("")))
			{
				msg = "label " + specimen.getLabel();
			}
			else if ((specimen.getBarcode() != null) && (!specimen.getBarcode().trim().equals("")))
			{
				msg = "barcode " + specimen.getBarcode();
			}
		}
		return msg;
	}

	/*
	 * T
	 */
	public NewSpecimenArrayOrderItem getNewSpecimenArrayOrderItem(Long orderItemId)
	{

		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

		NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = null;

		try
		{
			dao.openSession(null);
			newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) dao.retrieve(NewSpecimenArrayOrderItem.class.getName(), orderItemId);

		}
		catch (DAOException daoExp)
		{
			daoExp.printStackTrace();
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{

				e.printStackTrace();
			}
		}

		return newSpecimenArrayOrderItem;

	}

	//END

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject)
	{
		SpecimenArray specimenArray = null;
		Specimen specimen = null;
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME);
		Collection<SpecimenArrayContent> specimenArrayContentCollection = null;

		if (domainObject instanceof SpecimenArray)
		{
			specimenArray = (SpecimenArray) domainObject;
		}

		if(specimenArray.getSpecimenArrayContentCollection().isEmpty())
		{
			try 
			{
				specimenArray = (SpecimenArray) dao.retrieve(SpecimenArray.class.getName(), specimenArray.getId());
			} 
			catch (DAOException e) 
			{
				e.printStackTrace();
			}
			specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();
		}
		else
		{
			specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();
		}

		for (SpecimenArrayContent specimenArrayContent : specimenArrayContentCollection)
		{
			try
			{
				specimen = getSpecimen(dao, specimenArrayContent);
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}

			if (specimen != null)
			{
				SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
				CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
				sb.append(Constants.UNDERSCORE).append(cpr.getCollectionProtocol().getId());
			}
		}
		return sb.toString();
	}

	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return edu.wustl.catissuecore.util.global.Constants.ADD_EDIT_SPECIMEN_ARRAY;
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
		SpecimenArray specimenArray = null;
		Specimen specimen = null;
		SpecimenPosition specimenPosition = null;

		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}

		//	Get the base object id against which authorization will take place 
		protectionElementName = getObjectId(dao, domainObject);
		Site site = null;
		StorageContainer sc = null;
		//	Handle for SERIAL CHECKS, whether user has access to source site or not
		if (domainObject instanceof SpecimenArray)
		{
			specimenArray = (SpecimenArray) domainObject;
		}

		Collection<SpecimenArrayContent> specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();

		for (SpecimenArrayContent specimenArrayContent : specimenArrayContentCollection)
		{
			try
			{
				specimen = getSpecimen(dao, specimenArrayContent);

				if (specimen == null)
				{
					continue;
				}
				if (specimen.getSpecimenPosition() != null)
				{
					sc = specimen.getSpecimenPosition().getStorageContainer();
				}

				if (specimen.getSpecimenPosition() != null && specimen.getSpecimenPosition().getStorageContainer().getSite() == null)
				{
					sc = (StorageContainer) dao.retrieve(StorageContainer.class.getName(), specimen.getSpecimenPosition().getStorageContainer()
							.getId());
				}

				specimenPosition = specimen.getSpecimenPosition();

				if (specimenPosition != null) // Specimen is NOT Virtually Located
				{
					site = sc.getSite();
					Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());

					if (!siteIdSet.contains(site.getId()))
					{
						throw Utility.getUserNotAuthorizedException(Constants.Association, site.getObjectId());
					}
				}
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}

		//Get the required privilege name which we would like to check for the logged in user.
		String privilegeName = getPrivilegeName(domainObject);
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());

		// Checking whether the logged in user has the required privilege on the given protection element
		String[] prArray = protectionElementName.split("_");
		String baseObjectId = prArray[0];
		String objId = "";
		for (int i = 1; i < prArray.length; i++)
		{
			objId = baseObjectId + "_" + prArray[i];
			isAuthorized = privilegeCache.hasPrivilege(objId, privilegeName);
			if (!isAuthorized)
			{
				break;
			}
		}

		if (isAuthorized)
		{
			return isAuthorized;
		}
		else
		// Check for ALL CURRENT & FUTURE CASE
		{
			isAuthorized = Utility.checkOnCurrentAndFuture(dao, sessionDataBean, protectionElementName, privilegeName);
		}
		if (!isAuthorized)
		{
			throw Utility.getUserNotAuthorizedException(privilegeName, protectionElementName);
		}
		return isAuthorized;
	}

	public Map getContForDisabledSpecimenArrayFromCache() throws Exception
	{

		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		Map disabledconts = (TreeMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_CONTAINER_FOR_DISABLED_SPECIENARRAY);
		return disabledconts;
	}

	private void addEntriesInDisabledMap(SpecimenArray specimenarray, SpecimenArray container, Map disabledConts)
	{
		String contNameKey = "StorageContName";
		String contIdKey = "StorageContIdKey";
		String pos1Key = "pos1";
		String pos2Key = "pos2";
		Map containerDetails = new TreeMap();
		containerDetails.put(contNameKey, container.getName());
		containerDetails.put(contIdKey, container.getId());
		containerDetails.put(pos1Key, specimenarray.getLocatedAtPosition().getPositionDimensionOne());
		containerDetails.put(pos2Key, specimenarray.getLocatedAtPosition().getPositionDimensionTwo());

		disabledConts.put(specimenarray.getId().toString(), containerDetails);

	}

}