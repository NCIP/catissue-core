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
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.exception.UserNotAuthorizedException;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * <p>This class initializes the fields of SpecimenArrayBizLogic.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#insert(java.lang.Object, edu.wustl.common.dao.DAO, edu.wustl.common.beans.SessionDataBean)
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			SpecimenArray specimenArray = (SpecimenArray) obj;

			checkStorageContainerAvailablePos(specimenArray, dao, sessionDataBean);


			doUpdateSpecimenArrayContents(specimenArray, null, dao, sessionDataBean, true);

			dao.insert(specimenArray.getCapacity(),  true);
			dao.insert(specimenArray, true);
			SpecimenArrayContent specimenArrayContent = null;
			// TODO move this method to HibernateDAOImpl for common use (for collection insertion)
			for (Iterator iter = specimenArray.getSpecimenArrayContentCollection().iterator(); iter.hasNext();)
			{
				specimenArrayContent = (SpecimenArrayContent) iter.next();
				specimenArrayContent.setSpecimenArray(specimenArray);
				dao.insert(specimenArrayContent,true);
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
	{
		SpecimenArray specimenArray = (SpecimenArray) obj;

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

	public void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
		
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
		
			if (Status.ACTIVITY_STATUS_DISABLED.equals(specimenArrayCurrentObject.getActivityStatus()))
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

	/**
	 * @see edu.wustl.common.bizlogic.AbstractBizLogic#update(edu.wustl.common.dao.DAO, java.lang.Object, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */

	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
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
			doUpdateSpecimenArrayContents(specimenArray, oldSpecimenArray, dao, sessionDataBean, false);

			dao.update(specimenArray.getCapacity());
			dao.update(specimenArray);
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
					dao.insert(specimenArrayContent, true);
				}
				else
				{
					dao.update(specimenArrayContent, true);
				}
			}

			if (Status.ACTIVITY_STATUS_DISABLED.equals(specimenArray.getActivityStatus()))
			{
				Map disabledCont = null;
			
				disabledCont = getContForDisabledSpecimenArrayFromCache();
				
				if (disabledCont == null)
				{
					disabledCont = new TreeMap();
				}
				Object objectContainer = null;
				if (specimenArray.getLocatedAtPosition() != null && specimenArray.getLocatedAtPosition().getOccupiedContainer() != null
						&& specimenArray.getLocatedAtPosition().getOccupiedContainer().getId() != null)
				{
					objectContainer = dao.retrieveById(StorageContainer.class.getName(), specimenArray.getLocatedAtPosition().getOccupiedContainer().getId());
				}
				if (objectContainer != null)
				{

					SpecimenArray storageContainer = (SpecimenArray) objectContainer;
					addEntriesInDisabledMap(specimenArray, storageContainer, disabledCont);
				}

				ContainerPosition prevPosition = specimenArray.getLocatedAtPosition();

				specimenArray.setLocatedAtPosition(null);
				dao.update(specimenArray);

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
					throw getBizLogicException(e, "dao.error", "Problem while chaching");
				}
			}
		}
	catch(DAOException daoExp)
	{
		throw getBizLogicException(daoExp, "dao.error", "");
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
	 * @throws BizLogicException 
	 * @throws UserNotAuthorizedException
	 */
	private void doUpdateSpecimenArrayContents(SpecimenArray specimenArray, SpecimenArray oldSpecimenArray, DAO dao, SessionDataBean sessionDataBean,
			boolean isInsertOperation) throws BizLogicException
	{
		try
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
				Object object = dao.retrieveById(SpecimenArrayType.class.getName(), specimenArray.getSpecimenArrayType().getId());
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
							throw getBizLogicException(null, "dao.error", Constants.ARRAY_SPEC_NOT_COMPATIBLE_EXCEPTION_MESSAGE);
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
										throw getBizLogicException(null, "dao.error", " Quantity '" + tempQuantity + "' should be less than current Distributed Quantity '"
												+ specimen.getAvailableQuantity().doubleValue() + "' of specimen :: " + specimen.getLabel());
									}
								}
							}
							else
							{
								throw getBizLogicException(null, "dao.error", Constants.ARRAY_MOLECULAR_QUAN_EXCEPTION_MESSAGE + specimen.getLabel());
							}
						}
						specimenArrayContent.setSpecimen(specimen);
						//Added by jitendra 
						if (specimenArrayContent.getPositionDimensionOne() == null || specimenArrayContent.getPositionDimensionTwo() == null)
						{
							throw getBizLogicException(null, "array.contentPosition.err.msg", "");
						}
						updatedSpecArrayContentCollection.add(specimenArrayContent);
					}
				}
			}

			// There should be at least one valid specimen in array
			if (updatedSpecArrayContentCollection.isEmpty())
			{
				throw getBizLogicException(null, "dao.error", Constants.ARRAY_NO_SPECIMEN__EXCEPTION_MESSAGE);
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
							dao.update(oldSpecimen);
						}
					}
				}
			}
			specimenArray.setSpecimenArrayContentCollection(updatedSpecArrayContentCollection);

		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private void checkStorageContainerAvailablePos(SpecimenArray specimenArray, DAO dao, SessionDataBean sessionDataBean) throws BizLogicException
			
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
	 * @throws BizLogicException
	 */
	private void retriveScName(SpecimenArray specimenArray, DAO dao) throws BizLogicException
	{
		try
		{
			if (specimenArray.getLocatedAtPosition().getParentContainer().getId() != null)
			{
				StorageContainer storageContainerObj = new StorageContainer();
				storageContainerObj.setId(specimenArray.getLocatedAtPosition().getParentContainer().getId());
				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = {"name"};
				//String[] whereColumnName = {"id"}; //"storageContainer."+edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER
				//	String[] whereColumnCondition = {"="};
				//Object[] whereColumnValue = {specimenArray.getLocatedAtPosition().getParentContainer().getId()};
				//String joinCondition = null;

				QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				queryWhereClause.addCondition(new EqualClause("id", specimenArray
						.getLocatedAtPosition().getParentContainer().getId()));

				List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

				if (!list.isEmpty())
				{
					storageContainerObj.setName((String) list.get(0));
					specimenArray.getLocatedAtPosition().setParentContainer(storageContainerObj);
				}
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
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
	 * @throws BizLogicException
	 */
	private Specimen getSpecimen(DAO dao, SpecimenArrayContent arrayContent) throws BizLogicException
	{
		try
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
					if (!activityStatus.equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
					{
						throw getBizLogicException(null, "dao.error", Constants.ARRAY_SPECIMEN_NOT_ACTIVE_EXCEPTION_MESSAGE + columnValue);
					}
					//return specimenCollectionGroup;
				}
				else
				{
					throw getBizLogicException(null, "dao.error", Constants.ARRAY_SPECIMEN_DOES_NOT_EXIST_EXCEPTION_MESSAGE + columnValue);
				}
			}
			return specimen;

		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
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
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		try
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
				throw getBizLogicException(null, "domain.object.null.err.msg", "Specimen Array");
			}

			Validator validator = new Validator();

			if (specimenArray.getActivityStatus() == null)
			{
				specimenArray.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			}
			String message = "";
			if (specimenArray.getSpecimenArrayType() == null || specimenArray.getSpecimenArrayType().getId() == null
					|| specimenArray.getSpecimenArrayType().getId().longValue() == -1)
			{
				message = ApplicationProperties.getValue("array.arrayType");
				throw getBizLogicException(null, "errors.item.required",message);
			}

			//fetch array type to check specimen class
			Object object = dao.retrieveById(SpecimenArrayType.class.getName(), specimenArray.getSpecimenArrayType().getId());
			SpecimenArrayType specimenArrayType = null;

			if (object != null)
			{
				specimenArrayType = (SpecimenArrayType) object;
			}
			else
			{
				message = ApplicationProperties.getValue("array.arrayType");
				throw getBizLogicException(null, "errors.invalid",message);
			}

			//	validate name of array
			if (validator.isEmpty(specimenArray.getName()))
			{
				message = ApplicationProperties.getValue("array.arrayLabel");
				throw getBizLogicException(null, "errors.item.required",message);
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
				throw getBizLogicException(null, "errors.item.format",message);
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
				throw getBizLogicException(null, "dao.error","The Storage Container you specified is full");
			}
			else if (xPos == null || yPos == null || xPos.intValue() < 0 || yPos.intValue() < 0)
			{
				throw getBizLogicException(null, "errors.item.format", ApplicationProperties
						.getValue("array.positionInStorageContainer"));
			}

			// validate user 
			if (specimenArray.getCreatedBy() == null || specimenArray.getCreatedBy().getId() == null
					|| !validator.isValidOption(String.valueOf(specimenArray.getCreatedBy().getId())))
			{
				message = ApplicationProperties.getValue("array.user");
				throw getBizLogicException(null, "errors.item.required", message);
			}

			//validate capacity 
			if (specimenArray.getCapacity() == null || specimenArray.getCapacity().getOneDimensionCapacity() == null
					|| specimenArray.getCapacity().getTwoDimensionCapacity() == null)
			{
				throw getBizLogicException(null, "array.capacity.err.msg", "");
			}

			List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
			String specimenClass = specimenArrayType.getSpecimenClass();

			if (!isValidClassName(specimenClass))
			{
				throw getBizLogicException(null, "protocol.class.errMsg", "");
			}

			if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
			{
				throw getBizLogicException(null, "protocol.class.errMsg", "");
			}

			Collection specimenTypes = specimenArrayType.getSpecimenTypeCollection();
			if (specimenTypes == null || specimenTypes.isEmpty())
			{
				throw getBizLogicException(null, "protocol.type.errMsg", "");
			}
			else
			{
				Iterator itr = specimenTypes.iterator();
				while (itr.hasNext())
				{
					String specimenType = (String) itr.next();
					if (!Validator.isEnumeratedValue(AppUtility.getSpecimenTypes(specimenClass), specimenType))
					{
						throw getBizLogicException(null, "protocol.type.errMsg", "");
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
						throw getBizLogicException(null, "class.name.different", message);
					}
					if (specimenTypes != null && !specimenTypes.isEmpty() && tempSpecimen != null)
					{
						if (!specimenTypes.contains(tempSpecimen.getSpecimenType()))
						{
							message = getMessage(tempSpecimenArrayContent);
							throw getBizLogicException(null, "type.different", message);
						}
					}
				}
			}
			else
			{
				throw getBizLogicException(null, "dao.error", "Specimen Array for uploading is null");
			}
			return true;
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
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
	 * @throws BizLogicException
	 */
	private void retriveScId(DAO dao, SpecimenArray specimenArray) throws BizLogicException
	{
		try
		{
			String message = null;
			if (specimenArray.getLocatedAtPosition() != null && specimenArray.getLocatedAtPosition().getParentContainer() != null
					&& specimenArray.getLocatedAtPosition().getParentContainer().getName() != null)
			{

				StorageContainer storageContainerObj = (StorageContainer) HibernateMetaData.getProxyObjectImpl(specimenArray.getLocatedAtPosition()
						.getParentContainer());
				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = {"id"};

				//String[] whereColumnName = {"name"};
				//	String[] whereColumnCondition = {"="};
				//Object[] whereColumnValue = {specimenArray.getLocatedAtPosition().getParentContainer().getName()};
				//String joinCondition = null;

				QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				queryWhereClause.addCondition(new EqualClause("name", specimenArray
						.getLocatedAtPosition().getParentContainer().getName()));

				List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

				if (!list.isEmpty())
				{
					storageContainerObj.setId((Long) list.get(0));
					specimenArray.getLocatedAtPosition().setParentContainer(storageContainerObj);
				}
				else
				{
					message = ApplicationProperties.getValue("array.positionInStorageContainer");
					throw getBizLogicException(null, "errors.invalid", message);
				}
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
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
	 * @throws BizLogicException
	 */
	public int getUniqueIndexForName() throws BizLogicException
	{
		try
		{
		String sourceObjectName = "CATISSUE_CONTAINER";
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_IDENTIFIER"};
		return AppUtility.getNextUniqueNo(sourceObjectName, selectColumnName);
		}
		catch(ApplicationException exp)
		{
			throw getBizLogicException(exp, "dao.error", "");
		}
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
	public NewSpecimenArrayOrderItem getNewSpecimenArrayOrderItem(Long orderItemId)throws BizLogicException
	{
		DAO dao = null;
		NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = null;
		try
		{
			dao = openDAOSession(null);
			newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) dao.retrieveById(NewSpecimenArrayOrderItem.class.getName(), orderItemId);

		}
		catch (DAOException daoExp)
		{
			daoExp.printStackTrace();
		}
		finally
		{
			closeDAOSession(dao);
		}

		return newSpecimenArrayOrderItem;

	}

	//END

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		SpecimenArray specimenArray = null;
		Specimen specimen = null;
		StringBuffer sb = new StringBuffer();
		try
		{
			sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME);
			Collection<SpecimenArrayContent> specimenArrayContentCollection = null;

			if (domainObject instanceof SpecimenArray)
			{
				specimenArray = (SpecimenArray) domainObject;
			}

			if(specimenArray.getSpecimenArrayContentCollection().isEmpty())
			{
				specimenArray = (SpecimenArray) dao.retrieveById(SpecimenArray.class.getName(), specimenArray.getId());
				specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();
			}
			else
			{
				specimenArrayContentCollection = specimenArray.getSpecimenArrayContentCollection();
			}

			for (SpecimenArrayContent specimenArrayContent : specimenArrayContentCollection)
			{
				specimen = getSpecimen(dao, specimenArrayContent);
				if (specimen != null)
				{
					SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
					CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
					sb.append(Constants.UNDERSCORE).append(cpr.getCollectionProtocol().getId());
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 * 
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean) throws BizLogicException
	{

		boolean isAuthorized = false;

		String protectionElementName = null;
		SpecimenArray specimenArray = null;
		Specimen specimen = null;
		SpecimenPosition specimenPosition = null;
		try
		{
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
						sc = (StorageContainer) dao.retrieveById(StorageContainer.class.getName(), specimen.getSpecimenPosition().getStorageContainer()
								.getId());
					}

					specimenPosition = specimen.getSpecimenPosition();

					if (specimenPosition != null) // Specimen is NOT Virtually Located
					{
						site = sc.getSite();
						Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());

						if (!siteIdSet.contains(site.getId()))
						{
							//bug 11611 and 11659
							throw AppUtility.getUserNotAuthorizedException(Constants.Association, site.getObjectId());
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
				isAuthorized = AppUtility.checkOnCurrentAndFuture(sessionDataBean, protectionElementName, privilegeName);
			}
			if (!isAuthorized)
			{
				//throw Utility.getUserNotAuthorizedException(privilegeName, protectionElementName);
				throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName);
			}

		}
		catch (SMException e)
		{
			throw getBizLogicException(e, "sm.operation.error", "Error in checking has privilege");
		} catch (ApplicationException e) {
			ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(errorKey,e ,"SpecimenArrayBizLogic.java :");	
		}
		return isAuthorized;
	}

	public Map getContForDisabledSpecimenArrayFromCache() throws BizLogicException
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