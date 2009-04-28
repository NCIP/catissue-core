/*
 * Created on Sep 22, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * SpecimenArrayAliquotsBizLogic class is used to create SpecimenArray aliquots from the parent SpecimenArray
 * and to inserts all the aliquotes into the database 
 * @author jitendra_agrawal
 */
public class SpecimenArrayAliquotsBizLogic extends CatissueDefaultBizLogic
{

	private transient Logger logger = Logger.getCommonLogger(SpecimenArrayAliquotsBizLogic.class);

	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			SpecimenArray specimenArray = (SpecimenArray) obj;
			String specimenKey = "SpecimenArray:";
			String storageContainerId = "_StorageContainer_id";
			Map aliquotMap = specimenArray.getAliqoutMap();
			SpecimenArray parentSpecimenArray = (SpecimenArray) dao.retrieveById(
					SpecimenArray.class.getName(), specimenArray.getId());
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
					.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
			List positionsToBeAllocatedList = new ArrayList();
			List usedPositionsList = new ArrayList();
			for (int i = 1; i <= specimenArray.getAliquotCount(); i++)
			{
				StorageContainerUtil.prepareContainerMap(dao, aliquotMap, specimenKey,
						positionsToBeAllocatedList, usedPositionsList, i, storageContainerId);
			}
			for (int i = 0; i < positionsToBeAllocatedList.size(); i++)
			{
				StorageContainerUtil.allocatePositionToSingleContainerOrSpecimen(
						positionsToBeAllocatedList.get(i), aliquotMap, usedPositionsList,
						specimenKey, storageContainerId);
			}
			for (int i = 1; i <= specimenArray.getAliquotCount(); i++)
			{
				//Preparing the map keys			
				String radioButonKey = "radio_" + i;
				String labelKey = specimenKey + i + "_label";
				String idKey = specimenKey + i + "_id";
				String barcodeKey = specimenKey + i + "_barcode";
				String containerIdKey = specimenKey + i + "_StorageContainer_id";
				String containerNameKey = specimenKey + i + "_StorageContainer_name";
				String posDim1Key = specimenKey + i + "_positionDimensionOne";
				String posDim2Key = specimenKey + i + "_positionDimensionTwo";
				String storageContainerNameKey = specimenKey + i + "_StorageContainer_name";

				//Retrieving the quantity, barcode & location values for each aliquot
				String label = (String) aliquotMap.get(labelKey);
				String barcode = (String) aliquotMap.get(barcodeKey);

				String containerId = (String) aliquotMap.get(containerIdKey);
				String posDim1 = (String) aliquotMap.get(posDim1Key);
				String posDim2 = (String) aliquotMap.get(posDim2Key);
				//Create an object of Specimen Subclass
				SpecimenArray aliquotSpecimenArray = new SpecimenArray();

				/**
				 * Start: Change for API Search   --- Jitendra 06/10/2006
				 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
				 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
				 * So we removed default class level initialization on domain object and are initializing in method
				 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
				 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
				 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setSpecimenArrayDefault(aliquotSpecimenArray);

				//End: Change for API Search

				ContainerPosition cntPos = aliquotSpecimenArray.getLocatedAtPosition();
				if (cntPos == null)
				{
					cntPos = new ContainerPosition();
				}
				if (parentSpecimenArray != null)
				{
					//check for closed ParentSpecimenArray
					checkStatus(dao, parentSpecimenArray,
							"Fail to create Aliquots, Parent SpecimenArray");
					//		cntPos.setOccupiedContainer(aliquotSpecimenArray);

					//		aliquotSpecimenArray.setParent(parentSpecimenArray);
					aliquotSpecimenArray.setSpecimenArrayType(parentSpecimenArray
							.getSpecimenArrayType());
					aliquotSpecimenArray.setCreatedBy(parentSpecimenArray.getCreatedBy());
					aliquotSpecimenArray.setCapacity(parentSpecimenArray.getCapacity());
					Collection specimenArrayContentCollection = populateSpecimenArrayContentCollectionForAliquot(
							parentSpecimenArray, aliquotSpecimenArray, specimenArray
									.getAliquotCount(), dao);
					aliquotSpecimenArray
							.setSpecimenArrayContentCollection(specimenArrayContentCollection);
				}

				aliquotSpecimenArray.setAliquot(true);
				aliquotSpecimenArray.setFull(Boolean.valueOf(false));

				//Explicity set barcode to null if it is empty as its a unique key in the database
				if (barcode != null && barcode.trim().length() == 0)
				{
					barcode = null;
				}
				aliquotSpecimenArray.setBarcode(barcode);

				//Explicity set barcode to null if it is empty as its a unique key in the database
				if (label != null && label.trim().length() == 0)
				{
					label = null;
				}
				aliquotSpecimenArray.setName(label);
				StorageContainer storageContainerObj = new StorageContainer();
				if (containerId != null)
				{
					storageContainerObj.setId(new Long(containerId));

					//check for closed Storage Container
					checkStatus(dao, storageContainerObj, "Storage Container");

					StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory
							.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

					//check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao, containerId, posDim1, posDim2,
							sessionDataBean, false, null);

					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = {"name"};
					//String[] whereColumnName = {"id"}; //"storageContainer."+edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER
					//String[] whereColumnCondition = {"="};
					//Object[] whereColumnValue = {new Long(containerId)};
					//String joinCondition = null;

					QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("id", Long.valueOf(containerId)));

					List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

					if (!list.isEmpty())
					{
						storageContainerObj.setName((String) list.get(0));
						aliquotMap.put(storageContainerNameKey, (String) list.get(0));
					}
					//					}
				}
				else
				{
					aliquotSpecimenArray.setLocatedAtPosition(null);
					//		aliquotSpecimenArray.setStorageContainer(null);
				}

				//Setting the attributes - storage positions, available, acivity status & lineage
				if (containerId != null)
				{
					cntPos.setPositionDimensionOne(new Integer(posDim1));
					cntPos.setPositionDimensionTwo(new Integer(posDim2));
					cntPos.setOccupiedContainer(aliquotSpecimenArray);
					cntPos.setParentContainer(storageContainerObj);
				}
				else
				{
					//				cntPos.setPositionDimensionOne(null);
					//				cntPos.setPositionDimensionTwo(null);
					cntPos = null;
				}

				aliquotSpecimenArray.setLocatedAtPosition(cntPos);

				aliquotSpecimenArray.setAvailable(Boolean.TRUE);
				aliquotSpecimenArray.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
				//aliquotSpecimenArray.setLineage(Constants.ALIQUOT);

				//Inserting an aliquot in the database
				if (isAuthorized((DAO) dao, obj, sessionDataBean))
				{
					specimenArrayBizLogic.insert(aliquotSpecimenArray, sessionDataBean);
				}
				else
				{
					ErrorKey errorKey = ErrorKey.getErrorKey("user.not.auth");
					UserNotAuthorizedException exc = new UserNotAuthorizedException(errorKey, null,
							"");
					throw getBizLogicException(exc, "user.not.auth", "User not authorized");
				}
				//postInsert(aliquotSpecimenArray, dao, sessionDataBean);

				// set ID of Specimen array inserted to be used in Aliqut summary page
				aliquotMap.put(idKey, aliquotSpecimenArray.getId());

			}

			if (parentSpecimenArray != null)
			{
				SpecimenArray oldSpecimenArray = (SpecimenArray) dao.retrieveById(
						SpecimenArray.class.getName(), specimenArray.getId());;
				updateParentSpecimenArray(parentSpecimenArray);
				if (isAuthorized((DAO) dao, obj, sessionDataBean))
				{
					specimenArrayBizLogic.update(parentSpecimenArray, oldSpecimenArray,
							sessionDataBean);
				}
				else
				{
					ErrorKey errorKey = ErrorKey.getErrorKey("user.not.auth");
					UserNotAuthorizedException exc = new UserNotAuthorizedException(errorKey, null,
							"");
					throw getBizLogicException(exc, "user.not.auth", "User not authorized");
				}
			}

			//Populate aliquot map with parent specimenArray's data
			populateParentSpecimenArrayData(aliquotMap, specimenArray, parentSpecimenArray, dao);

		}
		catch (Exception daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private void updateParentSpecimenArray(SpecimenArray parentSpecimenArray)
	{
		parentSpecimenArray.setAvailable(Boolean.valueOf(false));
		parentSpecimenArray.setAliquot(true);
		Collection specimenArrayContentCollection = parentSpecimenArray
				.getSpecimenArrayContentCollection();
		if (specimenArrayContentCollection != null && !specimenArrayContentCollection.isEmpty())
		{
			Iterator itr = specimenArrayContentCollection.iterator();
			while (itr.hasNext())
			{
				SpecimenArrayContent arrayContent = (SpecimenArrayContent) itr.next();
				if (arrayContent.getSpecimen() instanceof MolecularSpecimen)
				{
					arrayContent.setInitialQuantity(Double.valueOf("0"));
				}
			}
		}
	}

	/**
	 * This function populates the parent specimenArray information in aliquot map. This map will be
	 * be retrieved in ForwardToProcessor & will be set in the request scope. Then the map will
	 * be retrieved from request scope in SpecimenArrayAliquotAction if the page is of "SpecimenArrayAliquotSummary" &
	 * the formbean will be populated with the appropriate data.
	 * 
	 * @param aliquotMap Map
	 * @param specimenArray SpecimenArray
	 * @throws BizLogicException 
	 */
	private void populateParentSpecimenArrayData(Map aliquotMap, SpecimenArray specimenArray,
			SpecimenArray parentSpecimenArray, DAO dao) throws BizLogicException,DAOException
	{
		try
		{
			aliquotMap.put(Constants.ALIQUOT_SPECIMEN_ARRAY_TYPE, parentSpecimenArray
					.getSpecimenArrayType().getName());
			aliquotMap.put(Constants.ALIQUOT_SPECIMEN_CLASS, parentSpecimenArray
					.getSpecimenArrayType().getSpecimenClass());
			/**
			 * Name : Virender
			 * Reviewer: Prafull
			 * Retriving specimenObject
			 * replaced aliquotMap.put(Constants.ALIQUOT_SPECIMEN_TYPES, parentSpecimenArray.getSpecimenArrayType().getSpecimenTypeCollection());
			 */
			Collection specimenTypeCollection = (Collection) retrieveAttribute(dao,
					SpecimenArray.class,parentSpecimenArray.getId(),
					"elements(specimenArrayType.specimenTypeCollection)");
		
			aliquotMap.put(Constants.ALIQUOT_SPECIMEN_TYPES, specimenTypeCollection);
			aliquotMap.put(Constants.ALIQUOT_ALIQUOT_COUNTS, String.valueOf(specimenArray
					.getAliquotCount()));

			specimenArray.setAliqoutMap(aliquotMap);
		}
		catch (Exception daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	/**
	 * 
	 * @param parentSpecimenArray SpecimenArray
	 * @param aliquotSpecimenArray SpecimenArray
	 * @param aliquotCount int
	 * @param dao DAO
	 * @return Collection
	 */
	private Collection populateSpecimenArrayContentCollectionForAliquot(
			SpecimenArray parentSpecimenArray, SpecimenArray aliquotSpecimenArray,
			int aliquotCount, DAO dao) throws BizLogicException
	{
		try
		{
			Collection parentSpecimenArrayContentCollection = parentSpecimenArray
					.getSpecimenArrayContentCollection();
			Collection specimenArrayContentCollection = new HashSet();
			Iterator iter = parentSpecimenArrayContentCollection.iterator();
			SpecimenArrayContent specimenArrayContent = null;

			for (int i = 0; iter.hasNext(); i++)
			{
				SpecimenArrayContent parentSpecimenArrayContent = (SpecimenArrayContent) iter
						.next();
				specimenArrayContent = new SpecimenArrayContent();

				/**
				 * Start: Change for API Search   --- Jitendra 06/10/2006
				 * In Case of Api Search, previoulsy it was failing since there was default class level initialization 
				 * on domain object. For example in User object, it was initialized as protected String lastName=""; 
				 * So we removed default class level initialization on domain object and are initializing in method
				 * setAllValues() of domain object. But in case of Api Search, default values will not get set 
				 * since setAllValues() method of domainObject will not get called. To avoid null pointer exception,
				 * we are setting the default values same as we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setSpecimenArrayContentDefault(specimenArrayContent);
				//End:-  Change for API Search 

				specimenArrayContent.setSpecimen(parentSpecimenArrayContent.getSpecimen());
				specimenArrayContent.setPositionDimensionOne(parentSpecimenArrayContent
						.getPositionDimensionOne());
				specimenArrayContent.setPositionDimensionTwo(parentSpecimenArrayContent
						.getPositionDimensionTwo());
				specimenArrayContent.setSpecimenArray(aliquotSpecimenArray);
				// Due to Lazy loading instanceOf method was returning false everytime. Fix for bug id:4864
				// Object is explicitly retrieved from DB
				Specimen specimen = (Specimen) dao.retrieveById(Specimen.class.getName(),
						parentSpecimenArrayContent.getSpecimen().getId());

				Double quantity = new Double(0);
				if (specimen instanceof MolecularSpecimen)
				{
					if (aliquotCount > 0)
					{
						Double parentInitialQuantity = parentSpecimenArrayContent
								.getInitialQuantity();
						Double initialQuantity = parentInitialQuantity.doubleValue() / aliquotCount;
						specimenArrayContent.setInitialQuantity(initialQuantity);
						// reset quantity value of parent array content to 0.0
						//parentSpecimenArrayContent.getInitialQuantity().setValue(Double.valueOf("0"));
					}

					specimenArrayContent
							.setConcentrationInMicrogramPerMicroliter(parentSpecimenArrayContent
									.getConcentrationInMicrogramPerMicroliter());
				}

				specimenArrayContentCollection.add(specimenArrayContent);
			}
			return specimenArrayContentCollection;
		}
		catch (Exception daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	public long getNextAvailableNumber(String sourceObjectName) throws BizLogicException
	{
		JDBCDAO jdbcDao = null;
		try
		{
			String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};

			jdbcDao = openJDBCSession();
			List list = jdbcDao.retrieve(sourceObjectName, selectColumnName);
			jdbcDao.closeSession();
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
		catch (Exception daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		finally
		{
			closeJDBCSession(jdbcDao);
		}
	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		SpecimenArray specimenArrayAliquot = (SpecimenArray) obj;
		String specimenKey = "SpecimenArray:";
		Map aliquotMap = specimenArrayAliquot.getAliqoutMap();
		try
		{
			for (int i = 1; i <= specimenArrayAliquot.getAliquotCount(); i++)
			{
				String containerIdKey = specimenKey + i + "_StorageContainer_id";
				String posDim1Key = specimenKey + i + "_positionDimensionOne";
				String posDim2Key = specimenKey + i + "_positionDimensionTwo";
				String storageContainerNameKey = specimenKey + i + "_StorageContainer_name";

				String contId = (String) aliquotMap.get(containerIdKey);
				String contName = (String) aliquotMap.get(storageContainerNameKey);
				String posOne = (String) aliquotMap.get(posDim1Key);
				String posTwo = (String) aliquotMap.get(posDim2Key);

				StorageContainer storageContainer = new StorageContainer();
				storageContainer.setId(new Long(contId));
				storageContainer.setName(contName);

				Map containerMap = StorageContainerUtil.getContainerMapFromCache();
				StorageContainerUtil.deleteSinglePositionInContainerMap(storageContainer,
						containerMap, new Integer(posOne).intValue(), new Integer(posTwo)
								.intValue());
			}
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
		}

	}

	protected void checkStatus(DAO dao, IActivityStatus ado, String errorName)
			throws BizLogicException
	{
		if (ado != null)
		{
			Long identifier = ((AbstractDomainObject) ado).getId();
			if (identifier != null)
			{
				String className = ado.getClass().getName();
				String activityStatus = ado.getActivityStatus();
				if (activityStatus == null)
				{
					activityStatus = getActivityStatus(dao, className, identifier);
				}
				if (activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED))
				{
					throw getBizLogicException(null, "error.object.closed", errorName);
				}
				if (activityStatus.equals(Status.ACTIVITY_STATUS_DISABLED))
				{
					throw getBizLogicException(null, "error.object.disabled", errorName);
				}
			}
		}
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		SpecimenArrayBizLogic specimenArrayBizLogic = null;
		try
		{
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
					.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return specimenArrayBizLogic.getObjectId(dao, domainObject);
	}

	/**
	 * (non-Javadoc)
	 * @throws  
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
		return specimenArrayBizLogic.isAuthorized(dao, domainObject, sessionDataBean);
	}
}