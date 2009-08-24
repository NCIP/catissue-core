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
import edu.wustl.common.exception.ApplicationException;
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
 * SpecimenArrayAliquotsBizLogic class is used to create SpecimenArray aliquots
 * from the parent SpecimenArray and to inserts all the aliquotes into the
 * database
 * @author jitendra_agrawal
 */
public class SpecimenArrayAliquotsBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger
			.getCommonLogger(SpecimenArrayAliquotsBizLogic.class);

	/**
	 * @param obj : obj
	 * @param dao : dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final SpecimenArray specimenArray = (SpecimenArray) obj;
			final String specimenKey = "SpecimenArray:";
			final String storageContainerId = "_StorageContainer_id";
			final Map aliquotMap = specimenArray.getAliqoutMap();
			final SpecimenArray parentSpecimenArray = (SpecimenArray) dao.retrieveById(
					SpecimenArray.class.getName(), specimenArray.getId());
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
					.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
			final List positionsToBeAllocatedList = new ArrayList();
			final List usedPositionsList = new ArrayList();
			for (int i = 1; i <= specimenArray.getAliquotCount(); i++)
			{
				StorageContainerUtil.prepareContainerMap(dao, aliquotMap, specimenKey,
						positionsToBeAllocatedList, usedPositionsList, i, storageContainerId);
			}
			for (int i = 0; i < positionsToBeAllocatedList.size(); i++)
			{
				StorageContainerUtil.allocatePositionToSingleContainerOrSpecimen(
						positionsToBeAllocatedList.get(i), aliquotMap, usedPositionsList,
						specimenKey, storageContainerId, dao);
			}
			for (int i = 1; i <= specimenArray.getAliquotCount(); i++)
			{
				final String labelKey = specimenKey + i + "_label";
				final String idKey = specimenKey + i + "_id";
				final String barcodeKey = specimenKey + i + "_barcode";
				final String containerIdKey = specimenKey + i + "_StorageContainer_id";
				final String posDim1Key = specimenKey + i + "_positionDimensionOne";
				final String posDim2Key = specimenKey + i + "_positionDimensionTwo";
				final String storageContainerNameKey = specimenKey + i + "_StorageContainer_name";

				// Retrieving the quantity, barcode & location values for each
				// aliquot
				String label = (String) aliquotMap.get(labelKey);
				String barcode = (String) aliquotMap.get(barcodeKey);

				final String containerId = (String) aliquotMap.get(containerIdKey);
				final String posDim1 = (String) aliquotMap.get(posDim1Key);
				final String posDim2 = (String) aliquotMap.get(posDim2Key);
				// Create an object of Specimen Subclass
				final SpecimenArray aliquotSpecimenArray = new SpecimenArray();

				/**
				 * Start: Change for API Search --- Jitendra 06/10/2006 In Case
				 * of Api Search, previoulsy it was failing since there was
				 * default class level initialization on domain object. For
				 * example in User object, it was initialized as protected
				 * String lastName=""; So we removed default class level
				 * initialization on domain object and are initializing in
				 * method setAllValues() of domain object. But in case of Api
				 * Search, default values will not get set since setAllValues()
				 * method of domainObject will not get called. To avoid null
				 * pointer exception, we are setting the default values same as
				 * we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setSpecimenArrayDefault(aliquotSpecimenArray);

				// End: Change for API Search

				ContainerPosition cntPos = aliquotSpecimenArray.getLocatedAtPosition();
				if (cntPos == null)
				{
					cntPos = new ContainerPosition();
				}
				if (parentSpecimenArray != null)
				{
					// check for closed ParentSpecimenArray
					this.checkStatus(dao, parentSpecimenArray,
							"Fail to create Aliquots, Parent SpecimenArray");
					// cntPos.setOccupiedContainer(aliquotSpecimenArray);

					// aliquotSpecimenArray.setParent(parentSpecimenArray);
					aliquotSpecimenArray.setSpecimenArrayType(parentSpecimenArray
							.getSpecimenArrayType());
					aliquotSpecimenArray.setCreatedBy(parentSpecimenArray.getCreatedBy());
					aliquotSpecimenArray.setCapacity(parentSpecimenArray.getCapacity());
					final Collection specimenArrayContentCollection = this
							.populateSpecimenArrayContentCollectionForAliquot(parentSpecimenArray,
									aliquotSpecimenArray, specimenArray.getAliquotCount(), dao);
					aliquotSpecimenArray
							.setSpecimenArrayContentCollection(specimenArrayContentCollection);
				}

				aliquotSpecimenArray.setAliquot(true);
				aliquotSpecimenArray.setFull(Boolean.valueOf(false));

				// Explicity set barcode to null if it is empty as its a unique
				// key in the database
				if (barcode != null && barcode.trim().length() == 0)
				{
					barcode = null;
				}
				aliquotSpecimenArray.setBarcode(barcode);

				// Explicity set barcode to null if it is empty as its a unique
				// key in the database
				if (label != null && label.trim().length() == 0)
				{
					label = null;
				}
				aliquotSpecimenArray.setName(label);
				final StorageContainer storageContainerObj = new StorageContainer();
				if (containerId != null)
				{
					storageContainerObj.setId(new Long(containerId));

					// check for closed Storage Container
					this.checkStatus(dao, storageContainerObj, "Storage Container");

					final StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) factory
							.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

					// check for all validations on the storage container.
					storageContainerBizLogic.checkContainer(dao, containerId, posDim1, posDim2,
							sessionDataBean, false, null);

					final String sourceObjectName = StorageContainer.class.getName();
					final String[] selectColumnName = {"name"};
					// String[] whereColumnName = {"id"};
					// //"storageContainer."+edu
					// .wustl.common.util.global.Constants.SYSTEM_IDENTIFIER
					// String[] whereColumnCondition = {"="};
					// Object[] whereColumnValue = {new Long(containerId)};
					// String joinCondition = null;

					final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("id", Long.valueOf(containerId)));

					final List list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClause);

					if (!list.isEmpty())
					{
						storageContainerObj.setName((String) list.get(0));
						aliquotMap.put(storageContainerNameKey, list.get(0));
					}
					// }
				}
				else
				{
					aliquotSpecimenArray.setLocatedAtPosition(null);
					// aliquotSpecimenArray.setStorageContainer(null);
				}

				// Setting the attributes - storage positions, available,
				// acivity status & lineage
				if (containerId != null)
				{
					cntPos.setPositionDimensionOne(new Integer(posDim1));
					cntPos.setPositionDimensionTwo(new Integer(posDim2));
					cntPos.setOccupiedContainer(aliquotSpecimenArray);
					cntPos.setParentContainer(storageContainerObj);
				}
				else
				{
					// cntPos.setPositionDimensionOne(null);
					// cntPos.setPositionDimensionTwo(null);
					cntPos = null;
				}

				aliquotSpecimenArray.setLocatedAtPosition(cntPos);

				aliquotSpecimenArray.setAvailable(Boolean.TRUE);
				aliquotSpecimenArray.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
				// aliquotSpecimenArray.setLineage(Constants.ALIQUOT);

				// Inserting an aliquot in the database
				if (this.isAuthorized(dao, obj, sessionDataBean))
				{
					specimenArrayBizLogic.insert(aliquotSpecimenArray, dao, sessionDataBean);
				}
				else
				{
					final ErrorKey errorKey = ErrorKey.getErrorKey("user.not.auth");
					final UserNotAuthorizedException exc = new UserNotAuthorizedException(errorKey,
							null, "");
					throw this.getBizLogicException(exc, exc.getErrorKeyName(), exc.getMsgValues());
				}
				// postInsert(aliquotSpecimenArray, dao, sessionDataBean);

				// set ID of Specimen array inserted to be used in Aliqut
				// summary page
				aliquotMap.put(idKey, aliquotSpecimenArray.getId());

			}

			if (parentSpecimenArray != null)
			{
				final SpecimenArray oldSpecimenArray = (SpecimenArray) dao.retrieveById(
						SpecimenArray.class.getName(), specimenArray.getId());;
				this.updateParentSpecimenArray(parentSpecimenArray);
				if (this.isAuthorized(dao, obj, sessionDataBean))
				{
					specimenArrayBizLogic.update(dao, parentSpecimenArray, oldSpecimenArray,
							sessionDataBean);
				}
				else
				{
					final ErrorKey errorKey = ErrorKey.getErrorKey("user.not.auth");
					final UserNotAuthorizedException exc = new UserNotAuthorizedException(errorKey,
							null, "");
					throw this.getBizLogicException(exc, exc.getErrorKeyName(), exc.getMsgValues());
				}
			}

			// Populate aliquot map with parent specimenArray's data
			this.populateParentSpecimenArrayData(aliquotMap, specimenArray, parentSpecimenArray,
					dao);

		}
		catch (final ApplicationException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * 
	 * @param parentSpecimenArray : parentSpecimenArray
	 */
	private void updateParentSpecimenArray(SpecimenArray parentSpecimenArray)
	{
		parentSpecimenArray.setAvailable(Boolean.valueOf(false));
		parentSpecimenArray.setAliquot(true);
		final Collection specimenArrayContentCollection = parentSpecimenArray
				.getSpecimenArrayContentCollection();
		if (specimenArrayContentCollection != null && !specimenArrayContentCollection.isEmpty())
		{
			final Iterator itr = specimenArrayContentCollection.iterator();
			while (itr.hasNext())
			{
				final SpecimenArrayContent arrayContent = (SpecimenArrayContent) itr.next();
				if (arrayContent.getSpecimen() instanceof MolecularSpecimen)
				{
					arrayContent.setInitialQuantity(Double.valueOf("0"));
				}
			}
		}
	}

	/**
	 * This function populates the parent specimenArray information in aliquot
	 * map. This map will be be retrieved in ForwardToProcessor & will be set in
	 * the request scope. Then the map will be retrieved from request scope in
	 * SpecimenArrayAliquotAction if the page is of
	 * "SpecimenArrayAliquotSummary" & the formbean will be populated with the
	 * appropriate data.
	 * @param dao : dao
	 * @param aliquotMap
	 *            Map
	 * @param specimenArray
	 *            SpecimenArray
	 * @param parentSpecimenArray : parentSpecimenArray
	 * @throws BizLogicException : BizLogicException
	 * @throws DAOException : DAOException
	 */
	private void populateParentSpecimenArrayData(Map aliquotMap, SpecimenArray specimenArray,
			SpecimenArray parentSpecimenArray, DAO dao) throws BizLogicException, DAOException
	{
		try
		{
			aliquotMap.put(Constants.ALIQUOT_SPECIMEN_ARRAY_TYPE, parentSpecimenArray
					.getSpecimenArrayType().getName());
			aliquotMap.put(Constants.ALIQUOT_SPECIMEN_CLASS, parentSpecimenArray
					.getSpecimenArrayType().getSpecimenClass());
			/**
			 * Name : Virender Reviewer: Prafull Retriving specimenObject
			 * replaced aliquotMap.put(Constants.ALIQUOT_SPECIMEN_TYPES,
			 * parentSpecimenArray
			 * .getSpecimenArrayType().getSpecimenTypeCollection());
			 */
			final Collection specimenTypeCollection = (Collection) this.retrieveAttribute(dao,
					SpecimenArray.class, parentSpecimenArray.getId(),
					"elements(specimenArrayType.specimenTypeCollection)");

			aliquotMap.put(Constants.ALIQUOT_SPECIMEN_TYPES, specimenTypeCollection);
			aliquotMap.put(Constants.ALIQUOT_ALIQUOT_COUNTS, String.valueOf(specimenArray
					.getAliquotCount()));

			specimenArray.setAliqoutMap(aliquotMap);
		}
		catch (final ApplicationException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param parentSpecimenArray
	 *            SpecimenArray
	 * @param aliquotSpecimenArray
	 *            SpecimenArray
	 * @param aliquotCount
	 *            int
	 * @param dao
	 *            DAO
	 * @return Collection
	 * @throws BizLogicException : BizLogicException
	 */
	private Collection populateSpecimenArrayContentCollectionForAliquot(
			SpecimenArray parentSpecimenArray, SpecimenArray aliquotSpecimenArray,
			int aliquotCount, DAO dao) throws BizLogicException
	{
		try
		{
			final Collection parentSpecimenArrayContentCollection = parentSpecimenArray
					.getSpecimenArrayContentCollection();
			final Collection specimenArrayContentCollection = new HashSet();
			final Iterator iter = parentSpecimenArrayContentCollection.iterator();
			SpecimenArrayContent specimenArrayContent = null;

			for (int i = 0; iter.hasNext(); i++)
			{
				final SpecimenArrayContent parentSpecimenArrayContent = (SpecimenArrayContent) iter
						.next();
				specimenArrayContent = new SpecimenArrayContent();

				/**
				 * Start: Change for API Search --- Jitendra 06/10/2006 In Case
				 * of Api Search, previoulsy it was failing since there was
				 * default class level initialization on domain object. For
				 * example in User object, it was initialized as protected
				 * String lastName=""; So we removed default class level
				 * initialization on domain object and are initializing in
				 * method setAllValues() of domain object. But in case of Api
				 * Search, default values will not get set since setAllValues()
				 * method of domainObject will not get called. To avoid null
				 * pointer exception, we are setting the default values same as
				 * we were setting in setAllValues() method of domainObject.
				 */
				ApiSearchUtil.setSpecimenArrayContentDefault(specimenArrayContent);
				// End:- Change for API Search

				specimenArrayContent.setSpecimen(parentSpecimenArrayContent.getSpecimen());
				specimenArrayContent.setPositionDimensionOne(parentSpecimenArrayContent
						.getPositionDimensionOne());
				specimenArrayContent.setPositionDimensionTwo(parentSpecimenArrayContent
						.getPositionDimensionTwo());
				specimenArrayContent.setSpecimenArray(aliquotSpecimenArray);
				// Due to Lazy loading instanceOf method was returning false
				// everytime. Fix for bug id:4864
				// Object is explicitly retrieved from DB
				final Specimen specimen = (Specimen) dao.retrieveById(Specimen.class.getName(),
						parentSpecimenArrayContent.getSpecimen().getId());

				new Double(0);
				if (specimen instanceof MolecularSpecimen)
				{
					if (aliquotCount > 0)
					{
						final Double parentInitialQuantity = parentSpecimenArrayContent
								.getInitialQuantity();
						final Double initialQuantity = parentInitialQuantity.doubleValue()
								/ aliquotCount;
						specimenArrayContent.setInitialQuantity(initialQuantity);
						// reset quantity value of parent array content to 0.0
						// parentSpecimenArrayContent.getInitialQuantity().
						// setValue(Double.valueOf("0"));
					}

					specimenArrayContent
							.setConcentrationInMicrogramPerMicroliter(parentSpecimenArrayContent
									.getConcentrationInMicrogramPerMicroliter());
				}

				specimenArrayContentCollection.add(specimenArrayContent);
			}
			return specimenArrayContentCollection;
		}
		catch (final ApplicationException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * 
	 * @param sourceObjectName : sourceObjectName
	 * @return long
	 * @throws BizLogicException : BizLogicException
	 */
	public long getNextAvailableNumber(String sourceObjectName) throws BizLogicException
	{
		JDBCDAO jdbcDao = null;
		try
		{
			final String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};

			jdbcDao = this.openJDBCSession();
			final List list = jdbcDao.retrieve(sourceObjectName, selectColumnName);
			jdbcDao.closeSession();
			if (list != null && !list.isEmpty())
			{
				final List columnList = (List) list.get(0);
				if (!columnList.isEmpty())
				{
					final String str = (String) columnList.get(0);
					if (!str.equals(""))
					{
						final long no = Long.parseLong(str);
						return no + 1;
					}
				}
			}
			return 1;
		}
		catch (final ApplicationException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeJDBCSession(jdbcDao);
		}
	}

	/**
	 * @param obj : obj
	 * @param dao : dao
	 * @param sessionDataBean : sessionDataBean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		final SpecimenArray specimenArrayAliquot = (SpecimenArray) obj;
		final String specimenKey = "SpecimenArray:";
		final Map aliquotMap = specimenArrayAliquot.getAliqoutMap();
		try
		{
			for (int i = 1; i <= specimenArrayAliquot.getAliquotCount(); i++)
			{
				final String containerIdKey = specimenKey + i + "_StorageContainer_id";
				final String storageContainerNameKey = specimenKey + i + "_StorageContainer_name";

				final String contId = (String) aliquotMap.get(containerIdKey);
				final String contName = (String) aliquotMap.get(storageContainerNameKey);

				final StorageContainer storageContainer = new StorageContainer();
				storageContainer.setId(new Long(contId));
				storageContainer.setName(contName);
			}
		}
		catch (final Exception e)
		{
			this.logger.debug(e.getMessage(), e);
		}
		super.postInsert(obj, dao, sessionDataBean);
	}

	/**
	 * @param dao :dao
	 * @param ado : ado
	 * @param errorName : errorName
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void checkStatus(DAO dao, IActivityStatus ado, String errorName)
			throws BizLogicException
	{
		if (ado != null)
		{
			final Long identifier = ((AbstractDomainObject) ado).getId();
			if (identifier != null)
			{
				final String className = ado.getClass().getName();
				String activityStatus = ado.getActivityStatus();
				if (activityStatus == null)
				{
					activityStatus = this.getActivityStatus(dao, className, identifier);
				}
				if (activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED.toString()))
				{
					throw this.getBizLogicException(null, "error.object.closed", errorName);
				}
				if (activityStatus.equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
				{
					throw this.getBizLogicException(null, "error.object.disabled", errorName);
				}
			}
		}
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @param dao : dao
	 * @param domainObject : domainObject
	 * @return String
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
	 *      java.lang.Object)
	 */
	@Override
	public String getObjectId(DAO dao, Object domainObject)
	{
		SpecimenArrayBizLogic specimenArrayBizLogic = null;
		try
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
					.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
		}
		catch (final BizLogicException e)
		{
			this.logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return specimenArrayBizLogic.getObjectId(dao, domainObject);
	}

	/**
	 * (non-Javadoc)
	 * @param dao : dao
	 * @param domainObject : domainObject
	 * @
	 * @throws BizLogicException : BizLogicException
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO,
	 *      java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	@Override
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic) factory
				.getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
		return specimenArrayBizLogic.isAuthorized(dao, domainObject, sessionDataBean);
	}

	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		return super.validate(obj, dao, operation);
	}
}