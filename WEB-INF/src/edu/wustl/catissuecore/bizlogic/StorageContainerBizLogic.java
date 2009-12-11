/**
 * <p>Title: StorageContainerHDAO Class>
 * <p>Description:	StorageContainerHDAO is used to add Storage Container information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.Position;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AuditException;
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
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * @author vaishali_khandelwal
 */
/**
 * @author geeta_jaggal
 *
 */
public class StorageContainerBizLogic extends CatissueDefaultBizLogic
{
	/**
	 * Logger object.
	 */
	private static final transient Logger logger = Logger.getCommonLogger(StorageContainerBizLogic.class);
	/**
	 * Saves the storageContainer object in the database.
	 * @param dao - DAo object
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void insert(Object obj, final DAO dao, final SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			final SiteBizLogic sBiz = new SiteBizLogic();
			final StorageTypeBizLogic stBiz = new StorageTypeBizLogic();
			int posOneCapacity = 1, posTwoCapacity = 1;
			int posDimOne = Constants.STORAGE_CONTAINER_FIRST_ROW, posDimTwo 
			= Constants.STORAGE_CONTAINER_FIRST_COLUMN;
			boolean[][] fullStatus = null;
			final int noOfContainers = container.getNoOfContainers().intValue();
			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null)
			{
				final Object object = dao.retrieveById(StorageContainer.class.getName(), container
						.getLocatedAtPosition().getParentContainer().getId());
				if (object != null)
				{
					final StorageContainer parentContainer = (StorageContainer) object;
					this.checkStatus(dao, parentContainer, "Parent Container");
					final int totalCapacity = parentContainer.getCapacity()
							.getOneDimensionCapacity().intValue()
							* parentContainer.getCapacity().getTwoDimensionCapacity().intValue();
					final Collection children = this.getChildren(dao,
							parentContainer.getId());
					if ((noOfContainers + children.size()) > totalCapacity)
					{
						throw this.getBizLogicException(null, "errors.storageContainer.overflow",
								"");
					}
					else
					{
						if (false == StorageContainerUtil.validatePosition(parentContainer, container))
						{
							throw this.getBizLogicException(null,
									"errors.storageContainer.dimensionOverflow", "");
						}
						this.checkContainer(dao, container.getLocatedAtPosition()
								.getParentContainer().getId().toString(), container
								.getLocatedAtPosition().getPositionDimensionOne().toString(),
								container.getLocatedAtPosition().getPositionDimensionTwo()
										.toString(), sessionDataBean, false, null);
						final boolean parentContValidToUSe = StorageContainerUtil.isParentContainerValidToUSe(
								container, parentContainer);
						if (!parentContValidToUSe)
						{
							throw this.getBizLogicException(null, "parent.container.not.valid", "");
						}
						final ContainerPosition cntPos = container.getLocatedAtPosition();
						cntPos.setParentContainer(parentContainer);
						container.setSite(parentContainer.getSite());
						posOneCapacity = parentContainer.getCapacity().getOneDimensionCapacity()
								.intValue();
						posTwoCapacity = parentContainer.getCapacity().getTwoDimensionCapacity()
								.intValue();
						fullStatus = StorageContainerUtil.getStorageContainerFullStatus(dao, parentContainer,
								children);
						posDimOne = cntPos.getPositionDimensionOne().intValue();
						posDimTwo = cntPos.getPositionDimensionTwo().intValue();
						container.setLocatedAtPosition(cntPos);
					}
				}
				else
				{
					throw this.getBizLogicException(null, "errors.storageContainerExist", "");
				}
			}
			else
			{
				sBiz.loadSite(dao, container);
			}
			stBiz.loadStorageType(dao, container);
			for (int i = 0; i < noOfContainers; i++)
			{
				final StorageContainer cont = new StorageContainer(container);
				if (cont.getLocatedAtPosition() != null
						&& cont.getLocatedAtPosition().getParentContainer() != null)
				{
					final ContainerPosition cntPos = cont.getLocatedAtPosition();
					cntPos.setPositionDimensionOne(Integer.valueOf(posDimOne));
					cntPos.setPositionDimensionTwo(Integer.valueOf(posDimTwo));
					cntPos.setOccupiedContainer(cont);
					cont.setLocatedAtPosition(cntPos);
				}
				logger.debug("Collection protocol size:"
						+ container.getCollectionProtocolCollection().size());
				if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl)
				{
					LabelGenerator storagecontLblGenerator;
					try
					{
						storagecontLblGenerator = LabelGeneratorFactory
								.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
						storagecontLblGenerator.setLabel(cont);
						container.setName(cont.getName());
					}
					catch (final NameGeneratorException e)
					{
						logger.error(e.getMessage(), e);
						e.printStackTrace() ;
						throw this.getBizLogicException(e, "name.generator.exp", "");
					}
				}
				if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerBarcodeGeneratorAvl)
				{
					try
					{
						BarcodeGeneratorFactory
						.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
					}
					catch (final NameGeneratorException e)
					{
						logger.error(e.getMessage(), e);
						e.printStackTrace();
						throw this.getBizLogicException(e, "name.generator.exp", "");
					}
				}
				final AuditManager auditManager = this.getAuditManager(sessionDataBean);
				dao.insert(cont.getCapacity());
				auditManager.insertAudit(dao, cont.getCapacity());
				if (cont.isFull() == null)
				{
					cont.setFull(false);
				}
				dao.insert(cont);
				auditManager.insertAudit(dao, cont);
				container.setId(cont.getId());
				container.setCapacity(cont.getCapacity());
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getParentContainer() != null)
				{
					do
					{
						if (posDimTwo == posTwoCapacity)
						{
							if (posDimOne == posOneCapacity)
							{
								posDimOne = Constants.STORAGE_CONTAINER_FIRST_ROW;
							}
							else
							{
								posDimOne = (posDimOne + 1)
										% (posOneCapacity + 1);
							}
							posDimTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
						}
						else
						{
							posDimTwo = posDimTwo + 1;
						}

						logger.debug("positionDimensionTwo: " + posDimTwo);
						logger.debug("positionDimensionOne: " + posDimOne);
					}
					while (fullStatus[posDimOne][posDimTwo] != false);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}
	/**
	 * @param obj - AbstractDomainObject.
	 * @return string array of DynamicGroups
	 * @throws SMException - throws SMException
	 */
	protected String[] getDynamicGroups(final AbstractDomainObject obj) throws SMException
	{
		String[] dynamicGroups = null;
		final StorageContainer storageContainer = (StorageContainer) obj;

		if (storageContainer.getLocatedAtPosition() != null
				&& storageContainer.getLocatedAtPosition().getParentContainer() != null)
		{
			dynamicGroups = SecurityManagerFactory.getSecurityManager().getProtectionGroupByName(
					storageContainer.getLocatedAtPosition().getParentContainer());
		}
		else
		{
			dynamicGroups = SecurityManagerFactory.getSecurityManager().getProtectionGroupByName(
					storageContainer.getSite());
		}
		return dynamicGroups;
	}
	/**
	 * @param obj AbstractDomainObject
	 * @param dao DAO object
	 * @param sessionDataBean SessionDataBean
	 */
	public void postInsert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		super.postInsert(obj, dao, sessionDataBean);
	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao DAO object 
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void update(final DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			final StorageContainer oldContainer = (StorageContainer) oldObj;
			final SiteBizLogic sBiz = new SiteBizLogic();
			StorageContainer persistentOldContainerForChange = null;
			final Object object = dao.retrieveById(StorageContainer.class.getName(), oldContainer
					.getId());
			persistentOldContainerForChange = (StorageContainer) object;
			if (container.getLocatedAtPosition() != null)
			{
				final StorageContainer parentStorageContainer = (StorageContainer) dao
						.retrieveById(StorageContainer.class.getName(), container
								.getLocatedAtPosition().getParentContainer().getId());
				container.getLocatedAtPosition().setParentContainer(parentStorageContainer);
			}
			logger.debug("container.isParentChanged() : " + container.isParentChanged());
			if (container.isParentChanged())
			{
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getParentContainer() != null)
				{
					if (StorageContainerUtil.isUnderSubContainer(container, container.getLocatedAtPosition()
							.getParentContainer().getId(), dao))
					{
						throw this.getBizLogicException(null,
								"errors.container.under.subcontainer", "");
					}
					logger.debug("Loading ParentContainer: "
							+ container.getLocatedAtPosition().getParentContainer().getId());
					if (false == StorageContainerUtil.validatePosition(dao, container))
					{
						throw this.getBizLogicException(null,
								"errors.storageContainer.dimensionOverflow", "");
					}
					final boolean canUse = StorageContainerUtil.isContainerAvailableForPositions(dao, container);
					logger.debug("canUse : " + canUse);
					if (!canUse)
					{
						throw this.getBizLogicException(null, "errors.storageContainer.inUse", "");
					}
					this.checkStatus(dao, container.getLocatedAtPosition().getParentContainer(),
							"Parent Container");
					final Site site = new SiteBizLogic().getSite(dao, container.getLocatedAtPosition()
							.getParentContainer().getId());
					this.checkStatus(dao, site, "Parent Container Site");
					container.setSite(site);
				}
			}
			else
			{
				if (container.isPositionChanged())
				{
					final String sourceObjectName = StorageContainer.class.getName();
					final String[] selectColumnName = {"id", "capacity.oneDimensionCapacity",
							"capacity.twoDimensionCapacity"};
					final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("id", container
							.getLocatedAtPosition().getParentContainer().getId()));
					final List list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClause);
					if (!list.isEmpty())
					{
						final Object[] obj1 = (Object[]) list.get(0);
						logger.debug("**************PC obj::::::: --------------- " + obj1);
						logger.debug((Long) obj1[0]);
						logger.debug((Integer) obj1[1]);
						logger.debug((Integer) obj1[2]);

						final Integer pcCapacityOne = (Integer) obj1[1];
						final Integer pcCapacityTwo = (Integer) obj1[2];

						if (!StorageContainerUtil.validatePosition(pcCapacityOne.intValue(), pcCapacityTwo
								.intValue(), container))
						{
							throw this.getBizLogicException(null,
									"errors.storageContainer.dimensionOverflow", "");
						}
					}
					/**
					 * Only if parentContainerID, positionOne or positionTwo is
					 * changed check for availability of position
					 */
					if (oldContainer.getLocatedAtPosition() != null
							&& oldContainer.getLocatedAtPosition().getPositionDimensionOne() != null
							&& oldContainer.getLocatedAtPosition().getPositionDimensionOne()
									.intValue() != container.getLocatedAtPosition()
									.getPositionDimensionOne().intValue()
							|| oldContainer.getLocatedAtPosition().getPositionDimensionTwo()
									.intValue() != container.getLocatedAtPosition()
									.getPositionDimensionTwo().intValue())
					{
						final boolean canUse = StorageContainerUtil
								.isContainerAvailableForPositions(dao, container);
						logger.debug("canUse : " + canUse);
						if (!canUse)
						{

							throw this.getBizLogicException(null, "errors.storageContainer.inUse",
									"");
						}
					}

				}
			}
			boolean flag = true;
			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null
					&& oldContainer.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer().getId().longValue() == oldContainer
							.getLocatedAtPosition().getParentContainer().getId().longValue()
					&& container.getLocatedAtPosition().getPositionDimensionOne().longValue() == oldContainer
							.getLocatedAtPosition().getPositionDimensionOne().longValue()
					&& container.getLocatedAtPosition().getPositionDimensionTwo().longValue() == oldContainer
							.getLocatedAtPosition().getPositionDimensionTwo().longValue())
			{
				flag = false;
			}

			if (flag)
			{
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getParentContainer() != null)
				{
					this.checkContainer(dao, container.getLocatedAtPosition().getParentContainer()
							.getId().toString(), container.getLocatedAtPosition()
							.getPositionDimensionOne().toString(), container.getLocatedAtPosition()
							.getPositionDimensionTwo().toString(), sessionDataBean, false, null);
				}

			}
			final Integer oldContainerDimOne = oldContainer.getCapacity().getOneDimensionCapacity();
			final Integer oldContainerDimTwo = oldContainer.getCapacity().getTwoDimensionCapacity();
			final Integer newContainerDimOne = container.getCapacity().getOneDimensionCapacity();
			final Integer newContainerDimTwo = container.getCapacity().getTwoDimensionCapacity();
			if (oldContainerDimOne.intValue() > newContainerDimOne.intValue()
					|| oldContainerDimTwo.intValue() > newContainerDimTwo.intValue())
			{
				final boolean canReduceDimension = StorageContainerUtil.canReduceDimension(dao, oldContainer
						.getId(), newContainerDimOne, newContainerDimTwo);
				if (!canReduceDimension)
				{
					throw this.getBizLogicException(null, "errors.storageContainer.cannotReduce",
							"");
				}
			}
			if (container.getId() != null)
			{
				new SiteBizLogic().checkClosedSite(dao, container.getId(), "Container site");
			}
			sBiz.setSiteForSubContainers(container, container.getSite(), dao);
			final boolean restrictionsCanChange = StorageContainerUtil.isContainerEmpty(dao, container);
			logger.info("--------------container Available :" + restrictionsCanChange);
			if (!restrictionsCanChange)
			{
				final boolean restrictionsChanged = StorageContainerUtil.checkForRestrictionsChanged(container,
						oldContainer);
				logger.info("---------------restriction changed -:" + restrictionsChanged);
				if (restrictionsChanged)
				{

					throw this.getBizLogicException(null,
							"errros.storageContainer.restrictionCannotChanged", "");
				}
			}
			final Collection<SpecimenPosition> specimenPosColl = this
					.getSpecimenPositionCollForContainer(dao, container.getId());
			container.setSpecimenPositionCollection(specimenPosColl);
			this.setValuesinPersistentObject(persistentOldContainerForChange, container, dao);
			dao.update(persistentOldContainerForChange);
			dao.update(persistentOldContainerForChange.getCapacity());
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.updateAudit(dao, obj, oldObj);
			auditManager.updateAudit(dao, container.getCapacity(), oldContainer.getCapacity());
			logger.debug("container.getActivityStatus() " + container.getActivityStatus());
			if (container.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				final Long[] containerIDArr = {container.getId()};
				if (StorageContainerUtil.isContainerAvailableForDisabled(dao, containerIDArr))
				{
					final List disabledConts = new ArrayList();
					final List<StorageContainer> disabledContainerList = new ArrayList<StorageContainer>();
					disabledContainerList.add(persistentOldContainerForChange);
					this.addEntriesInDisabledMap(persistentOldContainerForChange, disabledConts);
					this.setDisableToSubContainer(persistentOldContainerForChange, disabledConts,
							dao, disabledContainerList);
					persistentOldContainerForChange.getOccupiedPositions().clear();
					logger.debug("container.getActivityStatus() "
							+ container.getActivityStatus());
					this.disableSubStorageContainer(dao, sessionDataBean, disabledContainerList);
					final ContainerPosition prevPosition = persistentOldContainerForChange
							.getLocatedAtPosition();
					persistentOldContainerForChange.setLocatedAtPosition(null);
					dao.update(persistentOldContainerForChange);
					if (prevPosition != null)
					{
						dao.delete(prevPosition);
					}
				}
				else
				{
					throw this.getBizLogicException(null, "errors.container.contains.specimen", "");
				}
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}
	/**
	 * @param persistentobject - StorageContainer persistent object.
	 * @param newObject - StorageContainer newObject
	 * @param dao - DAO object
	 * @throws BizLogicException throws BizLogicException
	 */
	private void setValuesinPersistentObject(StorageContainer persistentobject,
			StorageContainer newObject, DAO dao) throws BizLogicException
	{
		try
		{
			persistentobject.setActivityStatus(newObject.getActivityStatus());
			persistentobject.setBarcode(newObject.getBarcode());
			final Capacity persistCapacity = persistentobject.getCapacity();
			final Capacity newCapacity = newObject.getCapacity();
			persistCapacity.setOneDimensionCapacity(newCapacity.getOneDimensionCapacity());
			persistCapacity.setTwoDimensionCapacity(newCapacity.getTwoDimensionCapacity());
			final Collection children = this.getChildren(dao, newObject.getId());
			this.setChildren(children, dao, persistentobject.getId());
			persistentobject.setCollectionProtocolCollection(newObject
					.getCollectionProtocolCollection());
			persistentobject.setComment(newObject.getComment());
			persistentobject.setFull(newObject.isFull());
			persistentobject.setHoldsSpecimenArrayTypeCollection(newObject
					.getHoldsSpecimenArrayTypeCollection());
			persistentobject.setHoldsSpecimenClassCollection(newObject
					.getHoldsSpecimenClassCollection());
			persistentobject.setHoldsStorageTypeCollection(newObject
					.getHoldsStorageTypeCollection());
			persistentobject.setName(newObject.getName());
			persistentobject.setNoOfContainers(newObject.getNoOfContainers());
			persistentobject.setParentChanged(newObject.isParentChanged());
			persistentobject.setPositionChanged(newObject.isPositionChanged());
			setContPos(persistentobject, newObject);
			persistentobject.setSimilarContainerMap(newObject.getSimilarContainerMap());
			persistentobject.setSite(newObject.getSite());
			persistentobject.setStartNo(newObject.getStartNo());
			persistentobject.setStorageType(newObject.getStorageType());
			persistentobject.setTempratureInCentigrade(newObject.getTempratureInCentigrade());
		}
		catch (final ApplicationException exp)
		{
			logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}
	/**
	 * Set container position.
	 * @param persistentobject StorageContainer object
	 * @param newObject StorageContainer object
	 */
	private void setContPos(StorageContainer persistentobject, StorageContainer newObject)
	{
		if (newObject.getLocatedAtPosition() != null)
		{
			ContainerPosition cntPos = persistentobject.getLocatedAtPosition();
			if (cntPos == null)
			{
				cntPos = new ContainerPosition();
				persistentobject.setLocatedAtPosition(cntPos);
			}
			cntPos.setPositionDimensionOne(newObject.getLocatedAtPosition()
					.getPositionDimensionOne());
			cntPos.setPositionDimensionTwo(newObject.getLocatedAtPosition()
					.getPositionDimensionTwo());
			cntPos.setParentContainer(newObject.getLocatedAtPosition().getParentContainer());
			cntPos.setOccupiedContainer(persistentobject);
		}
		if (newObject.getSpecimenPositionCollection() != null)
		{
			final Collection<SpecimenPosition> specPosColl = persistentobject
					.getSpecimenPositionCollection();
			specPosColl.addAll(newObject.getSpecimenPositionCollection());
		}
	}
	/**
	* @param children
	* @param dao
	* @param containerId
	* @throws DAOException
	*/
	private void setChildren(Collection children, DAO dao, Long containerId)
			throws ApplicationException
	{
		if (children != null)
		{
			getChildren(dao, containerId).addAll(children);
		}
	}
	/**
	 * @param dao
	 * @param containerId
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException 
	 */
	public Collection getChildren(DAO dao, Long containerId) throws ApplicationException
	{
		final String hql = "select cntPos.occupiedContainer from ContainerPosition cntPos, StorageContainer container where cntPos.occupiedContainer.id=container.id and cntPos.parentContainer.id ="
				+ containerId;
		List childrenColl = new ArrayList();
		childrenColl = dao.executeQuery(hql);
		return childrenColl;
	}

	/**
	 * @param dao DAO object
	 * @param currentObj Transient object
	 * @param oldObj Persistent object
	 * @param sessionDataBean SessionDataBean object
	 *  
	 */
	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			super.postUpdate(dao, currentObj, oldObj, sessionDataBean);
		}
		catch (final ApplicationException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}
	/**
	 * @param dao - DAO object.
	 * @param storageContainerID - String
	 * @param positionOne - String
	 * @param positionTwo -String 
	 * @param sessionDataBean - SessionDataBean object
	 * @param multipleSpecimen - boolean 
	 * @param specimen -Specimen object
	 * @throws BizLogicException throws BizLogicException
	 */
	public void checkContainer(DAO dao, String storageContainerID, String positionOne,
			String positionTwo, SessionDataBean sessionDataBean, boolean multipleSpecimen,
			Specimen specimen) throws BizLogicException
	{
		try
		{
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER,
					"capacity.oneDimensionCapacity", "capacity.twoDimensionCapacity", "name"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(Constants.SYSTEM_IDENTIFIER, Long
					.valueOf(storageContainerID)));
			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			if (!list.isEmpty())
			{
				final Object[] obj = (Object[]) list.get(0);
				logger.debug("**********SC found for given ID ****obj::::::: --------------- "
						+ obj);
				logger.debug((Long) obj[0]);
				logger.debug((Integer) obj[1]);
				logger.debug((Integer) obj[2]);
				logger.debug((String) obj[3]);

				final StorageContainer pc = new StorageContainer();
				pc.setId((Long) obj[0]);
				pc.setName((String) obj[3]);
				final Capacity cntPos = new Capacity();
				if (obj[1] != null && obj[2] != null)
				{
					cntPos.setOneDimensionCapacity((Integer) obj[1]);
					cntPos.setTwoDimensionCapacity((Integer) obj[2]);
					pc.setCapacity(cntPos);
				}
				// check if user has privilege to use the container
				final boolean hasAccess = StorageContainerUtil.validateContainerAccess(dao, pc, sessionDataBean);
				logger.debug("hasAccess..............." + hasAccess);
				if (!hasAccess)
				{
					throw this.getBizLogicException(null, "access.use.object.denied", "");
				}
				this.checkStatus(dao, pc, "Storage Container");
				new SiteBizLogic().checkClosedSite(dao, pc.getId(), "Container Site");
				final boolean isValidPosition = StorageContainerUtil.validatePosition(pc, positionOne, positionTwo);
				logger.debug("isValidPosition : " + isValidPosition);
				boolean canUsePosition = false;
				if (isValidPosition) // if position is valid
				{
					canUsePosition = StorageContainerUtil.isPositionAvailable(dao, pc, positionOne, positionTwo,
							specimen);
					logger.debug("canUsePosition : " + canUsePosition);
					if (!canUsePosition)
					{
						if (multipleSpecimen)
						{

							throw this.getBizLogicException(null,
									"errors.storageContainer.Multiple.inUse", "");
						}
						else
						{
							throw this.getBizLogicException(null, "errors.storageContainer.inUse",
									"");
						}
					}
				}
				else
				{
					throw this.getBizLogicException(null,
							"errors.storageContainer.dimensionOverflow", "");
				}
			}
			else
			{
				throw this.getBizLogicException(null, "errors.storageContainerExist", "");
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}
	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values.
	 * @param obj Domain Object
	 * @param dao DAO object
	 * @param operation Add/Edit
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		try
		{
			if (obj == null)
			{
				throw this.getBizLogicException(null, "domain.object.null.err.msg", "");
			}
			final StorageContainer container = (StorageContainer) obj;
			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
			 * Search, default values will not get set for the object since
			 * setAllValues() method of domainObject will not get called. To avoid
			 * null pointer exception, we are setting the default values same we
			 * were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setContainerDefault(container);
			String message = "";
			final Validator validator = new Validator();
			if (container.getStorageType() == null)
			{
				message = ApplicationProperties.getValue("storageContainer.type");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}
			if (container.getNoOfContainers() == null)
			{
				container.setNoOfContainers(Integer.valueOf(1));
			}
			if (Validator.isEmpty(container.getNoOfContainers().toString()))
			{
				message = ApplicationProperties.getValue("storageContainer.noOfContainers");
				throw this.getBizLogicException(null, "errors.item.required", message);
			}
			if (!validator.isNumeric(container.getNoOfContainers().toString(), 1))
			{
				message = ApplicationProperties.getValue("storageContainer.noOfContainers");
				throw this.getBizLogicException(null, "errors.item.format", message);
			}
			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() == null)
			{
				if (container.getSite() == null || container.getSite().getId() == null
						|| container.getSite().getId() <= 0)
				{
					message = ApplicationProperties.getValue("storageContainer.site");
					throw this.getBizLogicException(null, "errors.item.invalid", message);
				}
			}
			if (container.getTempratureInCentigrade() != null
					&& !Validator.isEmpty(container.getTempratureInCentigrade().toString())
					&& (!validator
							.isDouble(container.getTempratureInCentigrade().toString(), false)))
			{
				message = ApplicationProperties.getValue("storageContainer.temperature");
				throw this.getBizLogicException(null, "errors.item.format", message);
			}
			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null)
			{
				if (container.getLocatedAtPosition().getParentContainer().getId() == null)
				{
					final String sourceObjectName = StorageContainer.class.getName();
					final String[] selectColumnName = {"id"};
					final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("name", container
							.getLocatedAtPosition().getParentContainer().getName()));
					final List list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClause);
					if (!list.isEmpty())
					{
						container.getLocatedAtPosition().getParentContainer().setId(
								(Long) list.get(0));
					}
					else
					{
						final String message1 = ApplicationProperties
								.getValue("specimen.storageContainer");
						throw this.getBizLogicException(null, "errors.invalid", message1);
					}
				}
				final Integer xPos = container.getLocatedAtPosition().getPositionDimensionOne();
				final Integer yPos = container.getLocatedAtPosition().getPositionDimensionTwo();
				/**
				 * Following code is added to set the x and y dimension in case only
				 * storage container is given and x and y positions are not given
				 */
				if (xPos == null || yPos == null)
				{
					final Container cont = container.getLocatedAtPosition().getParentContainer();
					final Position position = StorageContainerUtil.getFirstAvailablePositionInContainer(cont, dao);
					if (position != null)
					{
						final ContainerPosition cntPos = container.getLocatedAtPosition();
						cntPos.setPositionDimensionOne(position.getXPos());
						cntPos.setPositionDimensionTwo(position.getYPos());
						cntPos.setOccupiedContainer(container);
					}
					else
					{
						throw this.getBizLogicException(null, "storage.specified.full", "");
					}
				}
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getPositionDimensionOne() != null
						&& Validator.isEmpty(String.valueOf(container.getLocatedAtPosition()
								.getPositionDimensionOne())))
				{
					message = ApplicationProperties.getValue("storageContainer.oneDimension");
					throw this.getBizLogicException(null, "errors.item.required", message);
				}
				else
				{
					if (container.getLocatedAtPosition() != null
							&& container.getLocatedAtPosition().getPositionDimensionOne() != null
							&& !validator.isNumeric(String.valueOf(container.getLocatedAtPosition()
									.getPositionDimensionOne())))
					{
						message = ApplicationProperties.getValue("storageContainer.oneDimension");
						throw this.getBizLogicException(null, "errors.item.format", message);
					}
				}
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getPositionDimensionTwo() != null
						&& !Validator.isEmpty(String.valueOf(container.getLocatedAtPosition()
								.getPositionDimensionTwo()))
						&& (!validator.isNumeric(String.valueOf(container.getLocatedAtPosition()
								.getPositionDimensionTwo()))))
				{
					message = ApplicationProperties.getValue("storageContainer.twoDimension");
					throw this.getBizLogicException(null, "errors.item.format", message);

				}
			}
			if (operation.equals(Constants.ADD))
			{
				if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(container.getActivityStatus()))
				{
					throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
				}
				if (container.isFull().booleanValue())
				{
					throw this.getBizLogicException(null,
							"storageContainer.isContainerFull.errMsg", "");
				}
			}
			else
			{
				if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, container
						.getActivityStatus()))
				{
					throw this.getBizLogicException(null, "activityStatus.errMsg", "");
				}
			}
			return true;
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}
	
	/**
	 * @return list of storage containers
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getStorageContainerList() throws BizLogicException
	{
		final String sourceObjectName = StorageContainer.class.getName();
		final String[] displayNameFields = {Constants.SYSTEM_IDENTIFIER};
		final String valueField = Constants.SYSTEM_IDENTIFIER;
		return this.getList(sourceObjectName, displayNameFields, valueField, true);
	}
	
	/**
	 * To get the ids of the CollectionProtocol that the given StorageContainer
	 * can hold.
	 * @param type
	 *            The reference to StorageType object.
	 * @return The array of ids of CollectionProtocol that the given
	 *         StorageContainer can hold.
	 * @throws BizLogicException throws BizLogicException
	 */
	public long[] getDefaultHoldCollectionProtocolList(StorageContainer container)
			throws BizLogicException
	{
		final Collection spcimenArrayTypeCollection = (Collection) this.retrieveAttribute(
				StorageContainer.class.getName(), container.getId(),
				"elements(collectionProtocolCollection)");
		long[] cpList = null;
		if (spcimenArrayTypeCollection.isEmpty())
		{
			cpList = new long[]{-1};
		}
		else
		{
			cpList = AppUtility.getobjectIds(spcimenArrayTypeCollection);
		}
		return cpList;
	}
	
	/**
	 * @param dao - DAO object.
	 * @param containerId - Long 
	 * @return  collection of SpecimenPosition
	 * @throws BizLogicException throws BizLogicException
	 */
	private Collection<SpecimenPosition> getSpecimenPositionCollForContainer(DAO dao,
			Long containerId) throws BizLogicException
	{
		List<SpecimenPosition> specimenPosColl = null;
		try
		{
			if (containerId != null)
			{
				specimenPosColl	= dao.retrieve(SpecimenPosition.class.getName(),
						"storageContainer.id", containerId);
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return specimenPosColl;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check.
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 * @param dao DAO object
	 * @param domainObject Abstract Domain object
	 */
	public String getObjectId(DAO dao, Object domainObject) throws BizLogicException
	{
		String objId = null;
		if (domainObject instanceof StorageContainer)
		{
			final StorageContainer storageContainer = (StorageContainer) domainObject;
			Site site = null;
			if (storageContainer.getLocatedAtPosition() != null
					&& storageContainer.getLocatedAtPosition().getParentContainer() != null)
			{
				try
				{
					final Object object = dao.retrieveById(StorageContainer.class.getName(),
							storageContainer.getLocatedAtPosition().getParentContainer().getId());
					if (object != null)
					{
						final StorageContainer parentContainer = (StorageContainer) object;
						site = parentContainer.getSite();
					}
				}
				catch (final DAOException e)
				{
					logger.error(e.getMessage(), e);
					e.printStackTrace();
					throw this
					.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
				}
			}
			else
			{
				site = storageContainer.getSite();
			}
			if (site != null)
			{
				final StringBuffer sb = new StringBuffer();
				sb.append(Site.class.getName()).append("_").append(site.getId().toString());
				objId = sb.toString();
			}
		}
		return objId;
	}

	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'.
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 * @param domainObject AbstractDomain object
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_STORAGE_CONTAINER;
	}
	/**
	 * @param dao - DAO object.
	 * @param sessionDataBean - SessionDataBean object
	 * @param disabledContainerList - list of StorageContainers 
	 * @throws BizLogicException throws BizLogicException
	 */
	private void disableSubStorageContainer(DAO dao, SessionDataBean sessionDataBean,
			List<StorageContainer> disabledContainerList) throws BizLogicException
	{
		try
		{
			final int count = disabledContainerList.size();
			final List containerIdList = new ArrayList();
			for (int i = 0; i < count; i++)
			{
				final StorageContainer container = disabledContainerList.get(i);
				containerIdList.add(container.getId());
			}
			final List listOfSpecimenIDs = this.getRelatedObjects(dao, Specimen.class,
					"specimenPosition.storageContainer", edu.wustl.common.util.Utility
							.toLongArray(containerIdList));
			if (!listOfSpecimenIDs.isEmpty())
			{

				throw this.getBizLogicException(null, "errors.container.contains.specimen", "");
			}
			for (int i = 0; i < count; i++)
			{
				final StorageContainer container = disabledContainerList.get(i);
				dao.update(container);
			}
			this.auditDisabledObjects(dao, "CATISSUE_CONTAINER", containerIdList);
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}
	/**
	 * @param storageContainer - StorageContainer object.
	 * @param disabledConts -List of disabledConts
	 * @param dao - DAO object
	 * @param disabledContainerList - list of disabledContainers
	 * @throws BizLogicException throws BizLogicException
	 */
	private void setDisableToSubContainer(StorageContainer storageContainer, List disabledConts,
			DAO dao, List disabledContainerList) throws BizLogicException
	{

		try
		{
			if (storageContainer != null)
			{
				final Collection childrenColl = new StorageContainerBizLogic().getChildren(dao,
						storageContainer.getId());
				final Iterator iterator = childrenColl.iterator();
				while (iterator.hasNext())
				{
					final StorageContainer container = (StorageContainer) iterator.next();
					container.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
					this.addEntriesInDisabledMap(container, disabledConts);
					container.setLocatedAtPosition(null);
					disabledContainerList.add(container);
					setDisableToSubContainer(container, disabledConts, dao,
							disabledContainerList);
				}
			}
			storageContainer.getOccupiedPositions().clear();

		}
		catch (final ApplicationException exp)
		{
			logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey(exp.getErrorKeyName());
			throw new BizLogicException(errorKey, exp, exp.getMsgValues());
		}

	}
	/**
	 * @param container - StorageContainer object.
	 * @param disabledConts - List of disabledConts
	 */
	private void addEntriesInDisabledMap(StorageContainer container, List disabledConts)
	{
		final String contNameKey = "StorageContName";
		final String contIdKey = "StorageContIdKey";
		final String parentContNameKey = "ParentContName";
		final String parentContIdKey = "ParentContId";
		final String pos1Key = "pos1";
		final String pos2Key = "pos2";
		final Map<String, Object> containerDetails = new TreeMap<String, Object>();
		containerDetails.put(contNameKey, container.getName());
		containerDetails.put(contIdKey, container.getId());
		if (container != null && container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null)
		{
			containerDetails.put(parentContNameKey, container.getLocatedAtPosition()
					.getParentContainer().getName());
			containerDetails.put(parentContIdKey, container.getLocatedAtPosition()
					.getParentContainer().getId());
			containerDetails.put(pos1Key, container.getLocatedAtPosition()
					.getPositionDimensionOne());
			containerDetails.put(pos2Key, container.getLocatedAtPosition()
					.getPositionDimensionTwo());
		}
		disabledConts.add(containerDetails);
	}
}