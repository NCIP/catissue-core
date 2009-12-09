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
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.NameValueBeanRelevanceComparator;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
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
	private final transient Logger logger = Logger.getCommonLogger(StorageContainerBizLogic.class);

	/**
	 * Getting containersMaxLimit from the xml file in static variable.
	 */
	private static final int containersMaxLimit = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.CONTAINERS_MAX_LIMIT));
	private static final String TYPE_CONTAINER = "container";
	private static final String TYPE_SPECIMEN = "specimen";
	private static final String TYPE_SPECIMEN_ARRAY = "specimen_array";
	private static final boolean IS_CP_UNIQUE = true;
	private static final boolean IS_SPCLASS_UNIQUE = true;
	private static final boolean IS_CP_NONUNIQUE = false;
	private static final boolean IS_SPCLASS_NONUNIQUE = false;

	/**
	 * Saves the storageContainer object in the database.
	 * @param dao - DAo object
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			SiteBizLogic sBiz = new SiteBizLogic();
			StorageTypeBizLogic stBiz = new StorageTypeBizLogic();
			int posOneCapacity = 1, posTwoCapacity = 1;
			int positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW, positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
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
						final boolean parentContainerValidToUSe = StorageContainerUtil.isParentContainerValidToUSe(
								container, parentContainer);
						if (!parentContainerValidToUSe)
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
						positionDimensionOne = cntPos.getPositionDimensionOne().intValue();
						positionDimensionTwo = cntPos.getPositionDimensionTwo().intValue();
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

					cntPos.setPositionDimensionOne(new Integer(positionDimensionOne));
					cntPos.setPositionDimensionTwo(new Integer(positionDimensionTwo));
					cntPos.setOccupiedContainer(cont);
					cont.setLocatedAtPosition(cntPos);
				}
				this.logger.debug("Collection protocol size:"
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
						this.logger.error(e.getMessage(), e);
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
						this.logger.error(e.getMessage(), e);
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
						if (positionDimensionTwo == posTwoCapacity)
						{
							if (positionDimensionOne == posOneCapacity)
							{
								positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW;
							}
							else
							{
								positionDimensionOne = (positionDimensionOne + 1)
										% (posOneCapacity + 1);
							}
							positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
						}
						else
						{
							positionDimensionTwo = positionDimensionTwo + 1;
						}

						this.logger.debug("positionDimensionTwo: " + positionDimensionTwo);
						this.logger.debug("positionDimensionOne: " + positionDimensionOne);
					}
					while (fullStatus[positionDimensionOne][positionDimensionTwo] != false);
				}
			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}
	/**
	 * @param obj - AbstractDomainObject.
	 * @return string array of DynamicGroups
	 * @throws SMException - throws SMException
	 */
	protected String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
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
	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
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
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			final StorageContainer oldContainer = (StorageContainer) oldObj;
			SiteBizLogic sBiz = new SiteBizLogic();
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
			this.logger.debug("container.isParentChanged() : " + container.isParentChanged());
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
					this.logger.debug("Loading ParentContainer: "
							+ container.getLocatedAtPosition().getParentContainer().getId());
					if (false == StorageContainerUtil.validatePosition(dao, container))
					{
						throw this.getBizLogicException(null,
								"errors.storageContainer.dimensionOverflow", "");
					}
					final boolean canUse = StorageContainerUtil.isContainerAvailableForPositions(dao, container);
					this.logger.debug("canUse : " + canUse);
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
						this.logger.debug("**************PC obj::::::: --------------- " + obj1);
						this.logger.debug((Long) obj1[0]);
						this.logger.debug((Integer) obj1[1]);
						this.logger.debug((Integer) obj1[2]);

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
						this.logger.debug("canUse : " + canUse);
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
			this.logger.info("--------------container Available :" + restrictionsCanChange);
			if (!restrictionsCanChange)
			{
				final boolean restrictionsChanged = StorageContainerUtil.checkForRestrictionsChanged(container,
						oldContainer);
				this.logger.info("---------------restriction changed -:" + restrictionsChanged);
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
			this.logger.debug("container.getActivityStatus() " + container.getActivityStatus());
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
					this.logger.debug("container.getActivityStatus() "
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
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.error(e.getMessage(), e);
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
			persistentobject.setSimilarContainerMap(newObject.getSimilarContainerMap());
			persistentobject.setSite(newObject.getSite());
			if (newObject.getSpecimenPositionCollection() != null)
			{
				final Collection<SpecimenPosition> specPosColl = persistentobject
						.getSpecimenPositionCollection();
				specPosColl.addAll(newObject.getSpecimenPositionCollection());
			}
			persistentobject.setStartNo(newObject.getStartNo());
			persistentobject.setStorageType(newObject.getStorageType());
			persistentobject.setTempratureInCentigrade(newObject.getTempratureInCentigrade());
		}
		catch (final ApplicationException exp)
		{
			this.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
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
			this.logger.error(e.getMessage(), e);
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
			if (list.size() != 0)
			{
				final Object[] obj = (Object[]) list.get(0);
				this.logger.debug("**********SC found for given ID ****obj::::::: --------------- "
						+ obj);
				this.logger.debug((Long) obj[0]);
				this.logger.debug((Integer) obj[1]);
				this.logger.debug((Integer) obj[2]);
				this.logger.debug((String) obj[3]);

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
				this.logger.debug("hasAccess..............." + hasAccess);
				if (!hasAccess)
				{
					throw this.getBizLogicException(null, "access.use.object.denied", "");
				}
				this.checkStatus(dao, pc, "Storage Container");
				new SiteBizLogic().checkClosedSite(dao, pc.getId(), "Container Site");
				final boolean isValidPosition = StorageContainerUtil.validatePosition(pc, positionOne, positionTwo);
				this.logger.debug("isValidPosition : " + isValidPosition);
				boolean canUsePosition = false;
				if (isValidPosition) // if position is valid
				{
					canUsePosition = StorageContainerUtil.isPositionAvailable(dao, pc, positionOne, positionTwo,
							specimen);
					this.logger.debug("canUsePosition : " + canUsePosition);
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
			this.logger.error(daoExp.getMessage(), daoExp);
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
				final Integer conts = new Integer(1);
				container.setNoOfContainers(conts);
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
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * Returns a list of storage containers. Each index corresponds to the entry:<br>
	 * 		[id, name, one_dimension_capacity, two_dimension_capacity ...]
	 * @param holdsType - The type that the containers can 
	 * hold (Specimen/SpecimenArray/Container)
	 * @param cpId - Collection Protocol Id
	 * @param spClass - Specimen Class
	 * @param aliquotCount - Number of aliquotes that the fetched containers 
	 * should minimally support. A value of <b>0</b> specifies that there's
	 * no such restriction
	 * @param containerTypeId  containerTypeId
	 * @param specimenArrayTypeId specimenArrayTypeId
	 * @param exceedingLimit exceedingLimit value
	 * @param storageType Auto, Manual, Virtual
	 * @return a list of storage containers
	 * @throws DAOException
	 */
	private List getStorageContainerList(String holdsType, final Long cpId, final String spClass,
			int aliquotCount, final SessionDataBean sessionData, Long containerTypeId,
			Long specimenArrayTypeId, String exceedingLimit, String storageType) throws BizLogicException, DAOException
	{
		final JDBCDAO dao = this.openJDBCSession();
		final List containers = new ArrayList();
		try
		{
			final String[] queries = this
					.getStorageContainerQueries(holdsType, cpId, spClass, aliquotCount,
							sessionData, containerTypeId, specimenArrayTypeId, exceedingLimit);

			int remainingContainersNeeded = containersMaxLimit;
			// iteratively run each query, and break if the required number of containers are found
			for (int i = 0; i < queries.length; i++)
			{
				this.logger.debug(String.format("Firing query: query%d", i));
				this.logger.debug(queries[i]);
				final List resultList = dao.executeQuery(queries[i]);
				if (resultList == null || resultList.size() == 0)
				{
					continue;
				}
				if(!"Manual".equals(storageType) && resultList.size() >= remainingContainersNeeded)
				{
					List subListOfCont = resultList.subList(0, remainingContainersNeeded);
					if(!containers.containsAll( subListOfCont ))
					{
					  containers.addAll(subListOfCont);
					  break;
					}
				}
				if(!containers.containsAll( resultList ))
				{
				   containers.addAll(resultList);
				}
				remainingContainersNeeded = remainingContainersNeeded - resultList.size();
			}
		}
		finally
		{
			this.closeJDBCSession(dao);
		}
		logger.debug(String.format("%s:%s:%d", this.getClass().getSimpleName(),
				"getStorageContainers() number of containers fetched", containers.size()));
		return containers;
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

		final List list = this.getList(sourceObjectName, displayNameFields, valueField, true);
		return list;
	}
	
	/**
	 * @param type_id - long .
	 * @param exceedingMaxLimit - String object
	 * @param selectedContainerName - String
	 * @param sessionDataBean - SessionDataBean object
  	 * @param storageType Auto, Manual, Virtual
	 * @return TreeMap of allocated containers
	 * @throws BizLogicException throws BizLogicException
	 */
	public TreeMap getAllocatedContainerMapForContainer(long type_id, String exceedingMaxLimit,
			String selectedContainerName, SessionDataBean sessionDataBean, DAO dao, String storageType)
			throws BizLogicException, DAOException
	{
		final List containerList = this.getStorageContainerList(TYPE_CONTAINER, null, null, 0,
				sessionDataBean, type_id, null, null, storageType);
		final TreeMap tm = (TreeMap) this.getAllocDetailsForContainers(containerList, dao);
		return tm;
	}

	/**
	 * @param cpId - Long .
	 * @param specimenClass - String
	 * @param aliquotCount - int
	 * @param exceedingMaxLimit - String
	 * @param sessionData - SessionDataBean object
	 * @param jdbcDAO - JDBCDAO object
	 * @return TreeMap of Allocated Contaienrs
	 * @throws BizLogicException throws BizLogicException
	 * @throws DAOException throws DAOException
	 */
	public TreeMap getAllocatedContainerMapForSpecimen(long cpId, String specimenClass,
			int aliquotCount, String exceedingMaxLimit, SessionDataBean sessionData, DAO dao)
			throws BizLogicException, DAOException
	{
		final List containerList = this.getStorageContainerList(TYPE_SPECIMEN, cpId, specimenClass,
				aliquotCount, sessionData, null, null, null,null);
		final TreeMap tm = (TreeMap) this.getAllocDetailsForContainers(containerList, dao);
		return tm;
	}

	/**
	 * Gets allocated container map for specimen array.
	 * @param specimen_array_type_id
	 *            specimen array type id
	 * @param noOfAliqoutes
	 *            No. of aliquotes
	 * @param sessionData - SessionDataBean object
	 * @param exceedingMaxLimit String
	 * @return container map
	 * @throws BizLogicException --
	 *             throws DAO Exception
	 * @see edu.wustl.common.dao.JDBCDAOImpl
	 */
	public TreeMap getAllocatedContainerMapForSpecimenArray(long specimen_array_type_id,
			int noOfAliqoutes, SessionDataBean sessionData, String exceedingMaxLimit, DAO dao)
			throws BizLogicException
	{
		final JDBCDAO jdbcDAO = null;
		try
		{
			final List containerList = this.getStorageContainerList(TYPE_SPECIMEN_ARRAY, null,
					null, 0, sessionData, null, specimen_array_type_id, null, null);
			final TreeMap tm = (TreeMap) this.getAllocDetailsForContainers(containerList, dao);
			return tm;
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getSpecimenClassList(String id) throws ApplicationException
	{
		final String sql = " SELECT SP.SPECIMEN_CLASS CLASS FROM CATISSUE_STOR_CONT_SPEC_CLASS SP "
				+ "WHERE SP.STORAGE_CONTAINER_ID = " + id;
		final List resultList = AppUtility.executeSQLQuery(sql);
		final Iterator iterator = resultList.iterator();
		final List returnList = new ArrayList();
		while (iterator.hasNext())
		{
			final List list = (List) iterator.next();
			for (int cnt = 0; cnt < list.size(); cnt++)
			{
				final String data = (String) list.get(cnt);
				returnList.add(data);
			}
		}
		if (returnList.isEmpty())
		{
			returnList.add(new String(Constants.NONE));
		}
		return returnList;
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
		if (spcimenArrayTypeCollection.isEmpty())
		{
			return new long[]{-1};
		}
		else
		{
			return AppUtility.getobjectIds(spcimenArrayTypeCollection);
		}
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
		try
		{
			if (containerId != null)
			{
				final List specimenPosColl = dao.retrieve(SpecimenPosition.class.getName(),
						"storageContainer.id", containerId);
				return specimenPosColl;
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return null;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check.
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 * @param dao DAO object
	 * @param domainObject Abstract Domain object
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
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
					this.logger.error(e.getMessage(), e);
					e.printStackTrace();
					return null;
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
				return sb.toString();
			}
		}
		return null;
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
	 * Returns a map of allocated containers vs. their respective
	 * postition maps, for the given containers
	 * 
	 * @return Returns a map of allocated containers vs. their respective free
	 *         locations.
	 * @throws DAOException
	 */
	private Map getAllocDetailsForContainers(List containerList, DAO dao) throws BizLogicException
	{
		this.logger.info("No of containers:" + containerList.size());

		final Map containerMap = new TreeMap(new NameValueBeanRelevanceComparator());
		long relevance = 1;
		for (final Iterator itr = containerList.listIterator(); itr.hasNext(); relevance++)
		{
			final ArrayList container = (ArrayList) itr.next();
			final Map positionMap = StorageContainerUtil.getAvailablePositionMapForContainer(String
					.valueOf(container.get(0)), 0, Integer.parseInt(String
					.valueOf(container.get(2))),
					Integer.parseInt(String.valueOf(container.get(3))), dao);
			if (!positionMap.isEmpty())
			{
				final NameValueBean nvb = new NameValueBean(container.get(1), container.get(0),
						relevance);
				containerMap.put(nvb, positionMap);
			}
		}

		return containerMap;
	}

	/**
	 * Generic method to return queries for Specimen/Specimen Array/Containers
	 * @param holdsType
	 * @param cpId
	 * @param spClass
	 * @param aliquotCount
	 * @param sessionData
	 * @param containerTypeId
	 * @param specimen_array_type_id
	 * @param exceedingLimit
	 * @return
	 */
	private String[] getStorageContainerQueries(String holdsType, Long cpId, String spClass,
			Integer aliquotCount, SessionDataBean sessionData, Long containerTypeId,
			Long specimen_array_type_id, String exceedingLimit)
	{
		if (holdsType.equals(TYPE_CONTAINER))
		{
			return this.getStorageContainerForContainerQuery(containerTypeId, sessionData);
		}
		else if (holdsType.equals(TYPE_SPECIMEN_ARRAY))
		{
			return this.getStorageContainerForSpecimenArrQuery(specimen_array_type_id, sessionData);
		}
		else if (holdsType.equals(TYPE_SPECIMEN))
		{
			return this.getStorageContainerForSpecimenQuery(cpId, spClass, aliquotCount,
					sessionData);
		}

		return new String[]{};
	}

	/**
	 * Gets the query for Specimen Array
	 * @param specimen_array_type_id
	 * @param aliquotCount
	 * @param sessionData
	 * @param exceedingLimit
	 * @return
	 */
	private String[] getStorageContainerForSpecimenArrQuery(long specimen_array_type_id,
			SessionDataBean sessionData)
	{
		String includeAllIdQueryStr = " OR t4.SPECIMEN_ARRAY_TYPE_ID = '"
				+ Constants.ARRAY_TYPE_ALL_ID + "'";

		if (!(new Validator().isValidOption(String.valueOf(specimen_array_type_id))))
		{
			includeAllIdQueryStr = "";
		}
		final StringBuilder sb = new StringBuilder();
		sb
				.append("SELECT VIEW1.IDENTIFIER, VIEW1.NAME, VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY from ");
		sb.append("(");
		sb
				.append(" SELECT t1.IDENTIFIER,t1.name,c.one_dimension_capacity , c.two_dimension_capacity, (c.ONE_DIMENSION_CAPACITY * c.TWO_DIMENSION_CAPACITY)  CAPACITY ");
		sb
				.append(" FROM CATISSUE_CONTAINER t1 JOIN CATISSUE_CAPACITY c on t1.CAPACITY_ID=c.IDENTIFIER ");
		sb.append(" LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER t2 on t2.IDENTIFIER=t1.IDENTIFIER ");
		sb
				.append(" LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON t1.IDENTIFIER = K.CONTAINER_ID ");
		sb
				.append(" LEFT OUTER JOIN CATISSUE_CONTAINER_POSITION L ON t1.IDENTIFIER = L.PARENT_CONTAINER_ID ");
		sb.append(" LEFT OUTER join catissue_site S on S.IDENTIFIER = t2.SITE_ID ");
		sb.append(" WHERE t2.IDENTIFIER IN ");
		sb
				.append(" (SELECT t4.STORAGE_CONTAINER_ID from CATISSUE_CONT_HOLDS_SPARRTYPE t4  where t4.SPECIMEN_ARRAY_TYPE_ID = '"
						+ specimen_array_type_id + "'");
		sb.append(includeAllIdQueryStr);
		sb.append(")");
		if (!sessionData.isAdmin())
		{
			sb.append(" AND t2.SITE_ID in (SELECT SITE_ID from CATISSUE_SITE_USERS where USER_ID="
					+ sessionData.getUserId() + ")");
		}
		sb.append("AND t1.ACTIVITY_STATUS='" + Status.ACTIVITY_STATUS_ACTIVE
				+ "' and S.ACTIVITY_STATUS='Active' and t1.CONT_FULL=0) VIEW1 ");
		sb
				.append(" GROUP BY VIEW1.IDENTIFIER, VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY ,VIEW1.CAPACITY ");
		sb.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) >  0");
		sb.append(" order by IDENTIFIER");
		return new String[]{sb.toString()};
	}

	/**
	 * Gets the query array for Container
	 */
	private String[] getStorageContainerForContainerQuery(long type_id, SessionDataBean sessionData)
	{
		final StringBuilder sb = new StringBuilder();
		sb
				.append("SELECT VIEW1.IDENTIFIER, VIEW1.NAME, VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY FROM  ");
		sb
				.append("(SELECT cont.IDENTIFIER, cont.NAME, cap.ONE_DIMENSION_CAPACITY, cap.TWO_DIMENSION_CAPACITY, (cap.ONE_DIMENSION_CAPACITY * cap.TWO_DIMENSION_CAPACITY)  CAPACITY ");
		sb
				.append("	FROM CATISSUE_CAPACITY cap JOIN CATISSUE_CONTAINER cont   on cap.IDENTIFIER = cont.CAPACITY_ID  ");
		sb
				.append(" LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON cont.IDENTIFIER = K.CONTAINER_ID ");
		sb
				.append("  LEFT OUTER JOIN CATISSUE_CONTAINER_POSITION L ON cont.IDENTIFIER = L.PARENT_CONTAINER_ID ");
		sb.append(" WHERE cont.IDENTIFIER IN ");
		sb.append(" (SELECT t4.STORAGE_CONTAINER_ID   FROM CATISSUE_ST_CONT_ST_TYPE_REL t4 ");
		sb.append(" WHERE (t4.STORAGE_TYPE_ID = '" + type_id);
		sb.append("' OR t4.STORAGE_TYPE_ID='1') and t4.STORAGE_CONTAINER_ID in ");
		sb.append(" (select SC.IDENTIFIER from CATISSUE_STORAGE_CONTAINER SC ");
		sb
				.append(" join CATISSUE_SITE S on sc.site_id=S.IDENTIFIER and S.ACTIVITY_STATUS!='Closed' ");
		if (!sessionData.isAdmin())
		{
			sb.append(" and S.IDENTIFIER in(SELECT SITE_ID from CATISSUE_SITE_USERS where USER_ID="
					+ sessionData.getUserId() + ")");
		}
		sb.append(")) ");
		sb.append(" AND cont.ACTIVITY_STATUS='" + Status.ACTIVITY_STATUS_ACTIVE);
		sb.append("' and cont.CONT_FULL=0 ) VIEW1 ");
		sb
				.append(" GROUP BY VIEW1.IDENTIFIER, VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY ,VIEW1.CAPACITY ");
		sb.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) >  0 ");
		sb.append(" ORDER BY VIEW1.IDENTIFIER");
		return new String[]{sb.toString()};
	}

	/**
	 * Gets the query array for Specimen Storage Containers
	 * @param cpId
	 * @param spClass
	 * @param aliquotCount
	 * @param sessionData
	 * @return
	 */
	private String[] getStorageContainerForSpecimenQuery(Long cpId, String spClass,
			int aliquotCount, SessionDataBean sessionData)
	{
		// Containers allowing Only this CP and Specimen Class
		final String q0 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), IS_CP_UNIQUE, IS_SPCLASS_UNIQUE);
		// Containers allowing Only this CP but other Specimen Classes also
		final String q1 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), IS_CP_UNIQUE, IS_SPCLASS_NONUNIQUE);
		// Containers no CP restriction and just this Specimen Class
		final String q2 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), null, IS_SPCLASS_UNIQUE);
		// Containers allowing Other CPs also but just this Specimen Class
		final String q3 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), IS_CP_NONUNIQUE, IS_SPCLASS_UNIQUE);
		// Containers allowing Others CPs also and other Specimen Classes too
		final String q4 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), IS_CP_NONUNIQUE, IS_SPCLASS_NONUNIQUE);
		//Containers allowing any CP and other Specimen Classes too
		final String q5 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), null, IS_SPCLASS_NONUNIQUE);
		//Containers allowing any CP and any Specimen Class
		final String q6 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), null, null);
		return new String[]{q0, q1, q2, q3, q4, q5, q6};
	}

	/**
	 * Forms a Query to get the Storage Container list
	 * @param cpId - Collection Protocol Id
	 * @param spClass - Specimen Class
	 * @param aliquotCount - Number of aliquotes that the fetched containers 
	 * should minimally support. A value of <b>0</b> specifies that there's
	 * no such restriction
	 * @param siteId - Site Id
	 * @return query string
	 */
	private String createSCQuery(final Long cpId, final String spClass, int aliquotCount,
			final Long userId, final boolean isAdmin, final Boolean isCPUnique,
			final Boolean isSPClassUnique)
	{
		final StringBuilder sb = new StringBuilder();
		sb
				.append("SELECT VIEW1.IDENTIFIER,VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY,VIEW1.TWO_DIMENSION_CAPACITY,VIEW1.CAPACITY-COUNT(*)  AVAILABLE_SLOTS ");
		sb
				.append(" FROM"
						+ " (SELECT D.IDENTIFIER,D.NAME,F.ONE_DIMENSION_CAPACITY, F.TWO_DIMENSION_CAPACITY,(F.ONE_DIMENSION_CAPACITY * F.TWO_DIMENSION_CAPACITY)  CAPACITY");
		sb
				.append(" FROM CATISSUE_CAPACITY F JOIN CATISSUE_CONTAINER D  ON F.IDENTIFIER = D.CAPACITY_ID");
		sb
				.append(" LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON D.IDENTIFIER = K.CONTAINER_ID ");
		sb.append(" LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER C ON D.IDENTIFIER = C.IDENTIFIER ");
		sb.append(" LEFT OUTER JOIN CATISSUE_SITE L ON C.SITE_ID = L.IDENTIFIER ");
		if (isCPUnique != null) //DO not join on CP if there is no restriction on CP. i.e isCPUnique=null 
		{
			sb
					.append(" LEFT OUTER JOIN CATISSUE_ST_CONT_COLL_PROT_REL A ON A.STORAGE_CONTAINER_ID = C.IDENTIFIER ");
		}
		if (isSPClassUnique != null) //DO not join on SP CLS if there is no restriction on SP CLS. i.e isSPClassUnique=null
		{
			sb
					.append(" LEFT OUTER JOIN CATISSUE_STOR_CONT_SPEC_CLASS B ON B.STORAGE_CONTAINER_ID = C.IDENTIFIER ");
		}
		sb.append(" WHERE ");
		if (isCPUnique != null)
		{
			sb.append(" A.COLLECTION_PROTOCOL_ID = ");
			sb.append(cpId);
			sb.append(" AND ");
		}
		if (isSPClassUnique != null)
		{
			sb.append("  B.SPECIMEN_CLASS = '");
			sb.append(spClass);
			sb.append("'");
			sb.append(" AND ");
		}
		if (!isAdmin)
		{
			sb.append(" C.SITE_ID IN (SELECT M.SITE_ID FROM  ");
			sb.append(" CATISSUE_SITE_USERS M WHERE M.USER_ID = ");
			sb.append(userId);
			sb.append(" ) ");
			sb.append(" AND ");
		}
		sb.append("  L.ACTIVITY_STATUS = 'Active' and D.ACTIVITY_STATUS='Active' and D.CONT_FULL=0 "); //Added cont_full condition by Preeti
		sb.append(") VIEW1  ");
		sb.append(" GROUP BY IDENTIFIER, VIEW1.NAME, ");
		sb.append(" VIEW1.ONE_DIMENSION_CAPACITY, ");
		sb.append(" VIEW1.TWO_DIMENSION_CAPACITY, ");
		sb.append(" VIEW1.CAPACITY ");
		if (aliquotCount > 0)
		{
			sb.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) >=  ");
			sb.append(aliquotCount);
		}
		else
		{
			sb.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) > 0  ");
		}

		sb.append(this.getStorageContainerCPQuery(isCPUnique));

		sb.append(this.getStorageContainerSPClassQuery(isSPClassUnique));

		sb.append(" ORDER BY VIEW1.IDENTIFIER ");

		Logger.out.debug(String.format("%s:%s:%s", this.getClass().getSimpleName(),
				"createSCQuery() query ", sb));

		return sb.toString();
	}

	/**
	 * Gets the restriction query for Containers for Collection Protocol
	 * @param isUnique - Specifies the kind of restriction where:
	 * <li><strong>true</strong> implies that the container should allow only one CP</li>
	 * <li><strong>false</strong> implies that the container allows more than one CPs</li>
	 * @return the query string
	 */
	private String getStorageContainerCPQuery(Boolean isUnique)
	{

		final String SC_CP_TABLE_NAME = "CATISSUE_ST_CONT_COLL_PROT_REL";
		if (isUnique == null) //No restrictions on CP. Any CP condition
		{
			final StringBuilder sb = new StringBuilder();
			sb.append(" AND VIEW1.IDENTIFIER NOT IN ");
			sb.append(" ( ");
			sb.append(" SELECT t2.STORAGE_CONTAINER_ID FROM " + SC_CP_TABLE_NAME + " t2 ");
			sb.append(" ) ");
			return sb.toString();
		}
		else
		{
			return this.getSCBaseRestrictionQuery(SC_CP_TABLE_NAME, isUnique);
		}
	}

	/**
	 * Generates the base container restriction string 
	 * This allows for selection of Containers that allow Single/Multiple CPs,
	 * as well as Containers that allow Single/Multiple Specimen Classes
	 * @param tableName - the table name to apply the restriction on
	 * @param isUnique - specifies the multiplicity of the restriction
	 * @return the base query string
	 */
	private String getSCBaseRestrictionQuery(String tableName, boolean isUnique)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(" AND  ");
		sb.append(" (( ");
		sb.append(" SELECT COUNT(*) ");
		sb.append(" FROM ");
		sb.append(tableName);
		sb.append(" AA WHERE AA.STORAGE_CONTAINER_ID = VIEW1.IDENTIFIER )");
		if (isUnique)
		{
			sb.append(" = 1 ");
		}
		else
		{
			sb.append(" >1 ");
		}
		sb.append(" ) ");
		return sb.toString();
	}

	/**
	 * Gets the restriction query for Containers for Specimen Class
	 * @param isUnique - Specifies the kind of restriction where:
	 * <li><strong>true</strong> implies that the container should allow only one type of Specimen</li>
	 * <li><strong>false</strong> implies that the container allows more than type of Specimens</li>
	 * @return the query string
	 */
	private String getStorageContainerSPClassQuery(Boolean isUnique)
	{
		final String SC_SPCLS_TABLE_NAME = "CATISSUE_STOR_CONT_SPEC_CLASS";
		if (isUnique == null) //No restrictions on CP. Any CP condition
		{
			final StringBuilder sb = new StringBuilder();
			sb.append(" AND ");
			sb.append(" ( ");
			sb.append(" SELECT COUNT(*) FROM ");
			sb.append(SC_SPCLS_TABLE_NAME);
			sb.append(" AA WHERE AA.STORAGE_CONTAINER_ID = VIEW1.IDENTIFIER ");
			sb.append(" ) ");
			sb.append(" =4 "); //No restriction on specimen class means it can store any of the 4 specimen classes
			return sb.toString();
		}
		else
		{
			return this.getSCBaseRestrictionQuery("CATISSUE_STOR_CONT_SPEC_CLASS", isUnique);
		}
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
			this.logger.error(daoExp.getMessage(), daoExp);
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
			this.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			ErrorKey errorKey = ErrorKey.getErrorKey(exp.getErrorKeyName());
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