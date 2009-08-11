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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import net.sf.ehcache.CacheException;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.tree.StorageContainerTreeNode;
import edu.wustl.catissuecore.tree.TreeDataInterface;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
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
import edu.wustl.common.tree.TreeNode;
import edu.wustl.common.tree.TreeNodeImpl;
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
import edu.wustl.dao.condition.INClause;
import edu.wustl.dao.condition.NullClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * @author vaishali_khandelwal
 */
/**
 * @author geeta_jaggal
 *
 */
public class StorageContainerBizLogic extends CatissueDefaultBizLogic implements TreeDataInterface
{

	/**
	 * Logger object.
	 */
	private transient final Logger logger = Logger.getCommonLogger(StorageContainerBizLogic.class);

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

			// Setting the Parent Container if applicable
			int posOneCapacity = 1, posTwoCapacity = 1;
			int positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW, positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
			boolean fullStatus[][] = null;

			final int noOfContainers = container.getNoOfContainers().intValue();

			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null)
			{
				final Object object = dao.retrieveById(StorageContainer.class.getName(), container
						.getLocatedAtPosition().getParentContainer().getId());

				if (object != null)
				{
					final StorageContainer parentContainer = (StorageContainer) object;

					// check for closed ParentContainer
					this.checkStatus(dao, parentContainer, "Parent Container");

					final int totalCapacity = parentContainer.getCapacity()
							.getOneDimensionCapacity().intValue()
							* parentContainer.getCapacity().getTwoDimensionCapacity().intValue();
					final Collection children = StorageContainerUtil.getChildren(dao,
							parentContainer.getId());
					if ((noOfContainers + children.size()) > totalCapacity)
					{

						throw this.getBizLogicException(null, "errors.storageContainer.overflow",
								"");
					}
					else
					{

						// Check if position specified is within the parent
						// container's
						// capacity
						if (false == this.validatePosition(parentContainer, container))
						{
							throw this.getBizLogicException(null,
									"errors.storageContainer.dimensionOverflow", "");
						}

						// check for all validations on the storage container.
						this.checkContainer(dao, container.getLocatedAtPosition()
								.getParentContainer().getId().toString(), container
								.getLocatedAtPosition().getPositionDimensionOne().toString(),
								container.getLocatedAtPosition().getPositionDimensionTwo()
										.toString(), sessionDataBean, false, null);

						// Check weather parent container is valid container to use
						final boolean parentContainerValidToUSe = this.isParentContainerValidToUSe(
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

						fullStatus = this.getStorageContainerFullStatus(dao, parentContainer,
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
				this.loadSite(dao, container);
			}

			this.loadStorageType(dao, container);

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
				// by falguni
				// Call Storage container label generator if its specified to use
				// automatic label generator
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
						this.logger.debug(e.getMessage(), e);
						throw this.getBizLogicException(e, "name.generator.exp", "");

					}
				}
				if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerBarcodeGeneratorAvl)
				{
					BarcodeGenerator storagecontBarcodeGenerator;
					try
					{
						storagecontBarcodeGenerator = BarcodeGeneratorFactory
								.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
						// storagecontBarcodeGenerator.setBarcode(cont);
					}
					catch (final NameGeneratorException e)
					{
						this.logger.debug(e.getMessage(), e);
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

				// Used for showing the success message after insert and using it
				// for edit.
				container.setId(cont.getId());
				container.setCapacity(cont.getCapacity());

				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getParentContainer() != null)
				{
					this.logger.debug("In if: ");
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
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 *  Name : Pathik Sheth  Reviewer Name :Vishvesh Mulay.
	 * Description:Retrive only repository sites which are not closed.
	 */
	/**
	 * @param sourceObjectName - sourceObjectName.
	 * @param displayNameFields - displayNameFields
	 * @param valueField - valueField
	 * @param activityStatusArr - activityStatusArr
	 * @param isToExcludeDisabled - isToExcludeDisabled
	 * @return List of objects
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getRepositorySiteList(String sourceObjectName, String[] displayNameFields,
			String valueField, String activityStatusArr[], boolean isToExcludeDisabled)
			throws BizLogicException
	{
		String[] whereColumnName = null;
		String[] whereColumnCondition = null;
		final String joinCondition = Constants.AND_JOIN_CONDITION;
		final String separatorBetweenFields = ", ";

		whereColumnName = new String[]{"activityStatus", "type"};
		whereColumnCondition = new String[]{"not in", "="};
		// whereColumnCondition = new String[]{"in"};
		final Object[] whereColumnValue = {activityStatusArr, Constants.REPOSITORY};

		return this.getList(sourceObjectName, displayNameFields, valueField, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields,
				isToExcludeDisabled);

	}

	/**
	 * @param displayNameFields - displayNameFields.
	 * @param valueField - valueField
	 * @param activityStatusArr - activityStatusArr
	 * @param userId - userId
	 * @return List of site list
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getSiteList(String[] displayNameFields, String valueField,
			String activityStatusArr[], Long userId) throws BizLogicException
	{
		final List siteResultList = this.getRepositorySiteList(Site.class.getName(),
				displayNameFields, valueField, activityStatusArr, false);
		List userList = null;
		final Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(userId);
		userList = new ArrayList();
		final Iterator siteListIterator = siteResultList.iterator();
		while (siteListIterator.hasNext())
		{
			final NameValueBean nameValBean = (NameValueBean) siteListIterator.next();
			final Long siteId = new Long(nameValBean.getValue());
			if (this.hasPrivilegeonSite(idSet, siteId))
			{
				userList.add(nameValBean);
			}
		}

		return userList;
	}

	/**
	 * @param siteidSet - siteidSet.
	 * @param siteId - siteId 
	 * @return boolean value
	 */
	private boolean hasPrivilegeonSite(Set<Long> siteidSet, Long siteId)
	{
		boolean hasPrivilege = true;
		if (siteidSet != null)
		{
			if (!siteidSet.contains(siteId))
			{
				hasPrivilege = false;
			}
		}
		return hasPrivilege;
	}

	/**
	 * this function checks weather parent of the container is valid or not
	 * according to restriction provided for the containers.
	 * 
	 * @param container -
	 *            Container
	 * @param parent -
	 *            Parent Container
	 * @return boolean true indicating valid to use , false indicating not valid
	 *         to use.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected boolean isParentContainerValidToUSe(StorageContainer container,
			StorageContainer parent) throws BizLogicException
	{

		final StorageType storageTypeAny = new StorageType();
		storageTypeAny.setId(new Long("1"));
		storageTypeAny.setName("All");
		if (parent.getHoldsStorageTypeCollection().contains(storageTypeAny))
		{
			return true;
		}
		if (!parent.getHoldsStorageTypeCollection().contains(container.getStorageType()))
		{
			return false;
		}
		return true;
	}

	// This method sets the collection Storage Types.
	/**
	 * @param obj - obj.
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

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic#postInsert(java.lang.Object, edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean)
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

			// lazy change
			StorageContainer persistentOldContainerForChange = null;
			final Object object = dao.retrieveById(StorageContainer.class.getName(), oldContainer
					.getId());
			persistentOldContainerForChange = (StorageContainer) object;

			// retrive parent container
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
					// Check whether continer is moved to one of its sub container.
					if (this.isUnderSubContainer(container, container.getLocatedAtPosition()
							.getParentContainer().getId(), dao))
					{

						throw this.getBizLogicException(null,
								"errors.container.under.subcontainer", "");
					}
					this.logger.debug("Loading ParentContainer: "
							+ container.getLocatedAtPosition().getParentContainer().getId());

					/**
					 * Name : Vijay_Pande Reviewer : Sntosh_Chandak Bug ID: 4038
					 * Patch ID: 4038_1 See also: 1-3 Description: In the edit mode
					 * while updating parent container there was a hibernet session
					 * error Since we were retrieving parent container it was
					 * retriving all child containers as well. Hence only required
					 * filed of parent containcer is retrieved.
					 */
					// StorageContainer pc = (StorageContainer)
					// dao.retrieve(StorageContainer.class.getName(),
					// container.getParent().getId());
					/*
					 * Check if position specified is within the parent container's
					 * capacity
					 */
					if (false == this.validatePosition(dao, container))
					{

						throw this.getBizLogicException(null,
								"errors.storageContainer.dimensionOverflow", "");
					}

					// Mandar : code added for validation bug id 666. 24-11-2005
					// start
					final boolean canUse = this.isContainerAvailableForPositions(dao, container);
					this.logger.debug("canUse : " + canUse);
					if (!canUse)
					{
						throw this.getBizLogicException(null, "errors.storageContainer.inUse", "");
					}
					// Mandar : code added for validation bug id 666. 24-11-2005 end

					// check for closed ParentContainer
					this.checkStatus(dao, container.getLocatedAtPosition().getParentContainer(),
							"Parent Container");

					// container.setParent(pc);

					final Site site = this.getSite(dao, container.getLocatedAtPosition()
							.getParentContainer().getId());

					// Site
					// site=((StorageContainer)container.getParent()).getSite();
					// check for closed Site
					this.checkStatus(dao, site, "Parent Container Site");

					container.setSite(site);
					/** -- patch ends here -- */
				}
			}
			// Mandar : code added for validation 25-11-05-----------
			else
			// if parent container is not changed only the position is changed.
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

						if (!this.validatePosition(pcCapacityOne.intValue(), pcCapacityTwo
								.intValue(), container))
						{
							throw this.getBizLogicException(null,
									"errors.storageContainer.dimensionOverflow", "");
						}
					}

					// -----------------
					// StorageContainer pc = (StorageContainer)
					// dao.retrieve(StorageContainer.class.getName(),
					// container.getParentContainer().getId());

					// if(!validatePosition(container.getParentContainer().getStorageContainerCapacity().getOneDimensionCapacity().intValue(),
					// container.getParentContainer().getStorageContainerCapacity().getTwoDimensionCapacity().intValue(),
					// container))
					// /*Check if position specified is within the parent
					// container's capacity*/
					// // if(!validatePosition(pc,container))
					// {
					// throw new
					// DAOException(ApplicationProperties.getValue("errors.storageContainer.dimensionOverflow"));
					// }
					//
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
						final boolean canUse = this
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

			// Mandar : --------- end 25-11-05 -----------------

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

				// check for all validations on the storage container.
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getParentContainer() != null)
				{
					this.checkContainer(dao, container.getLocatedAtPosition().getParentContainer()
							.getId().toString(), container.getLocatedAtPosition()
							.getPositionDimensionOne().toString(), container.getLocatedAtPosition()
							.getPositionDimensionTwo().toString(), sessionDataBean, false, null);
				}

			}

			// Check whether size has been reduced
			// Sri: fix for bug #355 (Storage capacity: Reducing capacity should be
			// handled)
			final Integer oldContainerDimOne = oldContainer.getCapacity().getOneDimensionCapacity();
			final Integer oldContainerDimTwo = oldContainer.getCapacity().getTwoDimensionCapacity();
			final Integer newContainerDimOne = container.getCapacity().getOneDimensionCapacity();
			final Integer newContainerDimTwo = container.getCapacity().getTwoDimensionCapacity();

			// If any size is reduced, object was present at any of the deleted
			// positions throw error
			if (oldContainerDimOne.intValue() > newContainerDimOne.intValue()
					|| oldContainerDimTwo.intValue() > newContainerDimTwo.intValue())
			{
				final boolean canReduceDimension = this.canReduceDimension(dao, oldContainer
						.getId(), newContainerDimOne, newContainerDimTwo);
				if (!canReduceDimension)
				{
					throw this.getBizLogicException(null, "errors.storageContainer.cannotReduce",
							"");

				}
			}

			/**
			 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
			 * Description:Storage container will not be added to closed site :check
			 * for closed site
			 */
			if (container.getId() != null)
			{
				this.checkClosedSite(dao, container.getId(), "Container site");
			}
			this.setSiteForSubContainers(container, container.getSite(), dao);

			final boolean restrictionsCanChange = this.isContainerEmpty(dao, container);
			this.logger.info("--------------container Available :" + restrictionsCanChange);
			if (!restrictionsCanChange)
			{

				final boolean restrictionsChanged = this.checkForRestrictionsChanged(container,
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

			// Audit of update of storage container.
			final AuditManager auditManager = this.getAuditManager(sessionDataBean);
			auditManager.updateAudit(dao, obj, oldObj);
			auditManager.updateAudit(dao, container.getCapacity(), oldContainer.getCapacity());

			this.logger.debug("container.getActivityStatus() " + container.getActivityStatus());

			if (container.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				final Long containerIDArr[] = {container.getId()};
				if (this.isContainerAvailableForDisabled(dao, containerIDArr))
				{
					final List disabledConts = new ArrayList();

					/**
					 * Preapare list of parent/child containers to disable
					 */
					final List<StorageContainer> disabledContainerList = new ArrayList<StorageContainer>();
					disabledContainerList.add(persistentOldContainerForChange);
					//persistentOldContainerForChange.setLocatedAtPosition(null);

					this.addEntriesInDisabledMap(persistentOldContainerForChange, disabledConts);
					// disabledConts.add(new StorageContainer(container));
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

					/*try
					{
						final CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
								.getInstance();
						catissueCoreCacheManager.addObjectToCache(
								Constants.MAP_OF_DISABLED_CONTAINERS, (Serializable) disabledConts);
					}
					catch (final CacheException e)
					{
						this.logger.debug(e.getMessage(), e);
					}*/

				}
				else
				{
					throw this.getBizLogicException(null, "errors.container.contains.specimen", "");
				}
			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final AuditException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}

	}

	/**
	 * @param persistentobject - StorageContainer persistentobject.
	 * @param newObject - StorageContainer newObject
	 * @param dao - DAO object
	 * @throws BizLogicException throws BizLogicException
	 */
	public void setValuesinPersistentObject(StorageContainer persistentobject,
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
			final Collection children = StorageContainerUtil.getChildren(dao, newObject.getId());
			StorageContainerUtil.setChildren(children, dao, persistentobject.getId());
			// persistentobject.setChildren(newObject.getChildren());
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
				// persistentobject.setLocatedAtPosition(cntPos);
			}
			persistentobject.setSimilarContainerMap(newObject.getSimilarContainerMap());
			persistentobject.setSite(newObject.getSite());
			if (newObject.getSpecimenPositionCollection() != null)
			{
				final Collection<SpecimenPosition> specPosColl = persistentobject
						.getSpecimenPositionCollection();
				// if(specPosColl == null)
				// {
				// specPosColl = new HashSet<SpecimenPosition>();
				// }
				specPosColl.addAll(newObject.getSpecimenPositionCollection());
				// specPos.setSpecimen(newObject.getSpecimenPosition().getSpecimen());
				// specPos.setStorageContainer(newObject);
				// persistentobject.setSpecimenPosition(specPos);
			}
			persistentobject.setStartNo(newObject.getStartNo());
			persistentobject.setStorageType(newObject.getStorageType());
			persistentobject.setTempratureInCentigrade(newObject.getTempratureInCentigrade());
		}
		catch (final ApplicationException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
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
		final Map containerDetails = new TreeMap();
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

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic#postUpdate(edu.wustl.dao.DAO, java.lang.Object, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			/*final StorageContainer currentContainer = (StorageContainer) currentObj;
			final StorageContainer oldContainer = (StorageContainer) oldObj;

			// If capacity of container gets increased then insert all the new
			// positions in map ..........
			final int xOld = oldContainer.getCapacity().getOneDimensionCapacity().intValue();
			final int xNew = currentContainer.getCapacity().getOneDimensionCapacity().intValue();
			final int yOld = oldContainer.getCapacity().getTwoDimensionCapacity().intValue();
			final int yNew = currentContainer.getCapacity().getTwoDimensionCapacity().intValue();

			if (currentContainer.getActivityStatus().equals(
					Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				final List disabledConts = StorageContainerUtil
						.getListOfDisabledContainersFromCache();
				final List disabledContsAfterReverse = new ArrayList();
				for (int i = disabledConts.size() - 1; i >= 0; i--)
				{
					disabledContsAfterReverse.add(disabledConts.get(i));
				}

				final Iterator itr = disabledContsAfterReverse.iterator();
				while (itr.hasNext())
				{

					final Map disabledContDetails = (TreeMap) itr.next();
					final String contNameKey = "StorageContName";
					final String contIdKey = "StorageContIdKey";
					final String parentContNameKey = "ParentContName";
					final String parentContIdKey = "ParentContId";
					final String pos1Key = "pos1";
					final String pos2Key = "pos2";

					final StorageContainer cont = new StorageContainer();
					cont.setId((Long) disabledContDetails.get(contIdKey));
					cont.setName((String) disabledContDetails.get(contNameKey));

					if (disabledContDetails.get(parentContIdKey) != null)
					{
						final StorageContainer parent = new StorageContainer();
						parent.setName((String) disabledContDetails.get(parentContNameKey));
						parent.setId((Long) disabledContDetails.get(parentContIdKey));
						// cont.setParent(parent);

						final ContainerPosition cntPos = new ContainerPosition();

						cntPos.setPositionDimensionOne((Integer) disabledContDetails.get(pos1Key));
						cntPos.setPositionDimensionTwo((Integer) disabledContDetails.get(pos2Key));
						cntPos.setParentContainer(parent);
						cntPos.setOccupiedContainer(cont);
						cont.setLocatedAtPosition(cntPos);
					}
				}

			}
*/
			super.postUpdate(dao, currentObj, oldObj, sessionDataBean);

		}
		catch (final ApplicationException e)
		{
			this.logger.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/*
	 * public boolean isContainerFull(String containerId, int dimX, int dimY)
	 * throws BizLogicException {
	 * 
	 * boolean availablePositions[][] =
	 * getAvailablePositionsForContainer(containerId, dimX, dimY);
	 * 
	 * dimX = availablePositions.length; for (int x = 1; x < dimX; x++) { dimY =
	 * availablePositions[x].length; for (int y = 1; y < dimY; y++) { if
	 * (availablePositions[x][y] == true) return false; } } return true;
	 *  }
	 */

	/**
	 * @param newContainer - StorageContainer object.
	 * @param oldContainer -  StorageContainer object
	 * @return boolean value
	 */
	private boolean checkForRestrictionsChanged(StorageContainer newContainer,
			StorageContainer oldContainer)
	{
		int flag = 0;
		final Collection cpCollNew = newContainer.getCollectionProtocolCollection();
		final Collection cpCollOld = oldContainer.getCollectionProtocolCollection();

		final Collection storTypeCollNew = newContainer.getHoldsStorageTypeCollection();
		final Collection storTypeCollOld = oldContainer.getHoldsStorageTypeCollection();

		final Collection spClassCollNew = newContainer.getHoldsSpecimenClassCollection();
		final Collection spClassCollOld = oldContainer.getHoldsSpecimenClassCollection();

		final Collection spArrayTypeCollNew = newContainer.getHoldsSpecimenArrayTypeCollection();
		final Collection spArrayTypeCollOld = oldContainer.getHoldsSpecimenArrayTypeCollection();

		/*
		 * if (cpCollNew.size() != cpCollOld.size()) return true;
		 */

		/**
		 * Bug 3612 - User should be able to change the restrictions if he
		 * specifies the superset of the old restrictions if container is not
		 * empty.
		 */
		Iterator itrOld = cpCollOld.iterator();
		while (itrOld.hasNext())
		{
			flag = 0;
			final CollectionProtocol cpOld = (CollectionProtocol) itrOld.next();
			final Iterator itrNew = cpCollNew.iterator();
			if (cpCollNew.size() == 0)
			{
				break;
			}
			while (itrNew.hasNext())
			{
				final CollectionProtocol cpNew = (CollectionProtocol) itrNew.next();
				if (cpOld.getId().longValue() == cpNew.getId().longValue())
				{
					flag = 1;
					break;
				}
			}
			if (flag != 1)
			{
				return true;
			}
		}

		/*
		 * if (storTypeCollNew.size() != storTypeCollOld.size()) return true;
		 */

		itrOld = storTypeCollOld.iterator();
		while (itrOld.hasNext())
		{
			flag = 0;
			final StorageType storOld = (StorageType) itrOld.next();
			final Iterator itrNew = storTypeCollNew.iterator();
			while (itrNew.hasNext())
			{
				final StorageType storNew = (StorageType) itrNew.next();
				if (storNew.getId().longValue() == storOld.getId().longValue()
						|| storNew.getId().longValue() == 1)
				{
					flag = 1;
					break;
				}
			}
			if (flag != 1)
			{
				return true;
			}

		}

		/*
		 * if (spClassCollNew.size() != spClassCollOld.size()) return true;
		 */

		itrOld = spClassCollOld.iterator();
		while (itrOld.hasNext())
		{
			flag = 0;
			final String specimenOld = (String) itrOld.next();
			final Iterator itrNew = spClassCollNew.iterator();
			while (itrNew.hasNext())
			{
				final String specimenNew = (String) itrNew.next();
				if (specimenNew.equals(specimenOld))
				{
					flag = 1;
					break;
				}
			}
			if (flag != 1)
			{
				return true;
			}
		}

		/*
		 * if (spArrayTypeCollNew.size() != spArrayTypeCollOld.size()) return
		 * true;
		 */

		itrOld = spArrayTypeCollOld.iterator();
		while (itrOld.hasNext())
		{
			flag = 0;
			final SpecimenArrayType spArrayTypeOld = (SpecimenArrayType) itrOld.next();

			final Iterator itrNew = spArrayTypeCollNew.iterator();
			while (itrNew.hasNext())
			{
				final SpecimenArrayType spArrayTypeNew = (SpecimenArrayType) itrNew.next();

				if (spArrayTypeNew.getId().longValue() == spArrayTypeOld.getId().longValue()
						|| spArrayTypeNew.getId().longValue() == 1)
				{
					flag = 1;
					break;
				}
			}
			if (flag != 1)
			{
				return true;
			}
		}

		return false;
	}

	/*protected void setPrivilege(DAO dao, String privilegeName,
			Class objectType, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException,
			DAOException {
		Logger.out.debug(" privilegeName:" + privilegeName + " objectType:"
				+ objectType + " objectIds:"
				+ edu.wustl.common.util.Utility.getArrayString(objectIds)
				+ " userId:" + userId + " roleId:" + roleId + " assignToUser:"
				+ assignToUser);

		// Aarti: Bug#1199 - We should be able to deassign
		// privilege on child even though user has privilege on the parent.
		// Thus commenting the check for privileges on parent.
		// if (assignOperation == Constants.PRIVILEGE_DEASSIGN)
		// {
		// isDeAssignable(dao, privilegeName, objectIds, userId, roleId,
		// assignToUser);
		// }

		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId,
				roleId, assignToUser, assignOperation);

		assignPrivilegeToSubStorageContainer(dao, privilegeName, objectIds,
				userId, roleId, assignToUser, assignOperation);
	}
	*/
	/**
	 * Checks whether the user/role has privilege on the parent
	 * (Container/Site). If the user has privilege an exception is thrown
	 * stating to deassign the privilege of parent first.
	 * @param dao
	 *            The dao object to get the related objects down the hierarchy.
	 * @param objectIds
	 *            The objects ids of containerwhose parent is to be checked.
	 * @param privilegeName
	 *            The privilege name.
	 * @param userId
	 *            The user identifier.
	 * @param roleId
	 *            The roleId in case privilege is assigned/deassigned to a role.
	 * @param assignToUser
	 *            boolean which determines whether privilege is
	 *            assigned/deassigned to a user or role.
	 * @throws Exception throws exception.
	 */
	private void isDeAssignable(DAO dao, String privilegeName, Long[] objectIds, Long userId,
			String roleId, boolean assignToUser) throws BizLogicException
	{
		try
		{
			// Aarti: Bug#2364 - Error while assigning privileges since attribute
			// parentContainer changed to parent
			final String[] selectColumnNames = {"locatedAtPosition.parentContainer.id", "site.id"};
			final String[] whereColumnNames = {"id"};
			final List listOfSubElement = super.getRelatedObjects(dao, StorageContainer.class,
					selectColumnNames, whereColumnNames, objectIds);

			this.logger.debug("Related Objects>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
					+ listOfSubElement.size());

			String userName = new String();
			if (assignToUser == true)
			{
				userName = SecurityManagerFactory.getSecurityManager().getUserById(
						userId.toString()).getLoginName();
			}

			// To get privilegeCache through
			// Singleton instance of PrivilegeManager, requires User LoginName
			final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);

			final Iterator iterator = listOfSubElement.iterator();
			while (iterator.hasNext())
			{
				final Object[] row = (Object[]) iterator.next();

				// Parent storage container identifier.
				Object containerObject = (Object) row[0];
				String className = StorageContainer.class.getName();

				// Parent storage container identifier is null, the parent is a
				// site..
				if ((row[0] == null) || (row[0].equals("")))
				{
					containerObject = row[1];
					className = Site.class.getName();
				}

				this.logger.debug("Container Object After ********************** : "
						+ containerObject + "row[1] : " + row[1]);

				boolean permission = false;
				// Check the permission on the parent container or site.
				if (assignToUser == true)// If the privilege is
				// assigned/deassigned to a user.
				{
					// Call to SecurityManager.checkPermission bypassed &
					// instead, call redirected to privilegeCache.hasPrivilege

					permission = true;
					// Commented by Vishvesh & Ravindra for MSR for C1
					// privilegeCache.hasPrivilege(className+"_"+containerObject.toString(),
					// privilegeName);

					// permission =
					// SecurityManager.getInstance(StorageContainerBizLogic.class).checkPermission(userName,
					// className,
					// containerObject.toString(), privilegeName);
				}
				else
				// If the privilege is assigned/deassigned to a user group.
				{
					permission = privilegeManager.hasGroupPrivilege(roleId, className + "_"
							+ containerObject.toString(), privilegeName);
					// permission =
					// SecurityManager.getInstance(StorageContainerBizLogic.class).checkPermission(roleId,
					// className,
					// containerObject.toString());
				}

				// If the parent is a Site.
				if (permission == true && row[0] == null)
				{
					throw this.getBizLogicException(null, "de.assgn.priv.site", row[1].toString());
				}
				else if (permission == true && row[0] != null)// If the parent is
				// a storage
				// container.
				{
					throw this.getBizLogicException(null, "de.assgn.priv.container", row[0]
							.toString());
				}
			}
		}
		catch (final SMException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw AppUtility.handleSMException(exp);
		}
	}

	// This method sets the Storage Type & Site (if applicable) of this
	// container.
	/**
	 * @param dao - DAO object.
	 * @param container - StorageContainer object.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void loadSite(DAO dao, StorageContainer container) throws BizLogicException
	{

		try
		{
			final Site site = container.getSite();
			// Setting the site if applicable
			if (site != null)
			{
				// Commenting dao.retrive() call as retrived object is not realy
				// required for further processing -Prafull
				final Site siteObj = (Site) dao.retrieveById(Site.class.getName(), container
						.getSite().getId());

				if (siteObj != null)
				{

					// check for closed site
					this.checkStatus(dao, siteObj, "Site");

					container.setSite(siteObj);
					this.setSiteForSubContainers(container, siteObj, dao);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param dao - DAO object.
	 * @param container - StorageContainer object
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void loadStorageType(DAO dao, StorageContainer container) throws BizLogicException
	{

		// Setting the Storage Type
		try
		{
			final Object storageTypeObj = dao.retrieveById(StorageType.class.getName(), container
					.getStorageType().getId());
			if (storageTypeObj != null)
			{
				final StorageType type = (StorageType) storageTypeObj;
				container.setStorageType(type);
			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param storageContainer - StorageContainer object.
	 * @param site Site object
	 * @param dao DAO object
	 * @throws BizLogicException throws BizLogicException
	 */
	private void setSiteForSubContainers(StorageContainer storageContainer, Site site, DAO dao)
			throws BizLogicException
	{
		// Added storageContainer.getId()!=null check as this method fails in
		// case when it gets called from insert(). -PRafull

		try
		{
			if (storageContainer != null && storageContainer.getId() != null)
			{
				// Collection children = (Collection)
				// dao.retrieveAttribute(storageContainer.getClass().getName(),
				// storageContainer.getId(), "elements(children)");

				final Collection children = StorageContainerUtil.getChildren(dao, storageContainer
						.getId());
				this.logger.debug("storageContainer.getChildrenContainerCollection() "
						+ children.size());

				final Iterator iterator = children.iterator();
				while (iterator.hasNext())
				{
					final StorageContainer container = (StorageContainer) HibernateMetaData
							.getProxyObjectImpl(iterator.next());
					container.setSite(site);
					this.setSiteForSubContainers(container, site, dao);
				}
			}
		}
		catch (final ApplicationException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}

	}

	/**
	 * @param storageContainer - StorageContainer object.
	 * @param parentContainerID - StorageContainer
	 * @param dao - DAO object.
	 * @return boolean value.
	 * @throws BizLogicException throws BizLogicException
	 */
	private boolean isUnderSubContainer(StorageContainer storageContainer, Long parentContainerID,
			DAO dao) throws BizLogicException
	{

		try
		{
			if (storageContainer != null)
			{
				// Ashish - 11/6/07 - Retriving children containers for performance
				// improvement.
				// Collection childrenColl =
				// (Collection)dao.retrieveAttribute(StorageContainer.class.getName(),
				// storageContainer.getId(),Constants.COLUMN_NAME_CHILDREN );

				final Collection childrenColl = StorageContainerUtil.getChildren(dao,
						storageContainer.getId());
				final Iterator iterator = childrenColl.iterator();
				// storageContainer.getChildren()
				while (iterator.hasNext())
				{
					final StorageContainer container = (StorageContainer) iterator.next();
					// Logger.out.debug("SUB CONTINER container
					// "+parentContainerID.longValue()+"
					// "+container.getId().longValue()+"
					// "+(parentContainerID.longValue()==container.getId().longValue()));
					if (parentContainerID.longValue() == container.getId().longValue())
					{
						return true;
					}
					if (this.isUnderSubContainer(container, parentContainerID, dao))
					{
						return true;
					}
				}
			}
		}
		catch (final ApplicationException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}

		return false;
	}

	// TODO TO BE REMOVED
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
				// Ashish - 11/6/07 - Retriving children containers for performance
				// improvement.
				// Collection childrenColl =
				// (Collection)dao.retrieveAttribute(StorageContainer.class.getName(),
				// storageContainer.getId(),Constants.COLUMN_NAME_CHILDREN );

				final Collection childrenColl = StorageContainerUtil.getChildren(dao,
						storageContainer.getId());

				final Iterator iterator = childrenColl.iterator();
				while (iterator.hasNext())
				{
					final StorageContainer container = (StorageContainer) iterator.next();

					container.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
					this.addEntriesInDisabledMap(container, disabledConts);
					/* whenever container is disabled free it's used positions */

					container.setLocatedAtPosition(null);
					disabledContainerList.add(container);
					this.setDisableToSubContainer(container, disabledConts, dao,
							disabledContainerList);
				}
			}
			storageContainer.getOccupiedPositions().clear();

		}
		catch (final ApplicationException exp)
		{
			this.logger.debug(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}

	}

	// This method is called from labelgenerator.
	/**
	 * @return Long value next container number.
	 * @throws BizLogicException throws BizLogicException
	 */
	public long getNextContainerNumber() throws BizLogicException
	{
		DAO dao = null;
		try
		{
			final String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
			final String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};
			dao = this.openDAOSession(null);

			final List list = dao.retrieve(sourceObjectName, selectColumnName);

			if (!list.isEmpty())
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
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}

	}

	// what to do abt thi
	/**
	 * @param siteName - Site name.
	 * @param typeName - site type.
	 * @param operation - operation
	 * @param Id - id.
	 * @return String container name.
	 * @throws BizLogicException throws BizLogicException
	 */
	public String getContainerName(String siteName, String typeName, String operation, long Id)
			throws BizLogicException
	{
		String containerName = "";
		if (typeName != null && siteName != null && !typeName.equals("") && !siteName.equals(""))
		{
			// Poornima:Max length of site name is 50 and Max length of
			// container type name is 100, in Oracle the name does not truncate
			// and it is giving error. So these fields are truncated in case it
			// is longer than 40.
			// It also solves Bug 2829:System fails to create a default unique
			// storage container name
			String maxSiteName = siteName;
			String maxTypeName = typeName;
			if (siteName.length() > 40)
			{
				maxSiteName = siteName.substring(0, 39);
			}
			if (typeName.length() > 40)
			{
				maxTypeName = typeName.substring(0, 39);
			}

			if (operation.equals(Constants.ADD))
			{
				containerName = maxSiteName + "_" + maxTypeName + "_"
						+ String.valueOf(this.getNextContainerNumber());
			}
			else
			{
				containerName = maxSiteName + "_" + maxTypeName + "_" + String.valueOf(Id);
			}

		}
		return containerName;
	}

	/**
	 * @param parentID - parentID.
	 * @param typeID - typeID
	 * @param isInSite - isInSite
	 * @return next container number
	 * @throws BizLogicException throws BizLogicException
	 */
	public int getNextContainerNumber(long parentID, long typeID, boolean isInSite)
			throws BizLogicException
	{

		try
		{
			final String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);

			final String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};
			/*	String[] whereColumnName = { "STORAGE_TYPE_ID", "PARENT_CONTAINER_ID" };
				String[] whereColumnCondition = { "=", "=" };
				Object[] whereColumnValue = { Long.valueOf(typeID),
				Long.valueOf(parentID) };
			 */

			queryWhereClause.addCondition(new EqualClause("STORAGE_TYPE_ID", Long.valueOf(typeID)))
					.andOpr().addCondition(
							new EqualClause("PARENT_CONTAINER_ID", Long.valueOf(parentID)));

			if (isInSite)
			{
				/*whereColumnName = new String[3];
				whereColumnName[0] = "STORAGE_TYPE_ID";
				whereColumnName[1] = "SITE_ID";
				whereColumnName[2] = "PARENT_CONTAINER_ID";

				whereColumnValue = new Object[3];
				whereColumnValue[0] = Long.valueOf(typeID);
				whereColumnValue[1] = Long.valueOf(parentID);
				whereColumnValue[2] = "null";

				whereColumnCondition = new String[3];
				whereColumnCondition[0] = "=";
				whereColumnCondition[1] = "=";
				whereColumnCondition[2] = "is";*/

				queryWhereClause.addCondition(
						new EqualClause("STORAGE_TYPE_ID", Long.valueOf(typeID))).andOpr()
						.addCondition(new EqualClause("SITE_ID", Long.valueOf(parentID))).andOpr()
						.addCondition(new NullClause("PARENT_CONTAINER_ID"));
			}
			final String joinCondition = Constants.AND_JOIN_CONDITION;

			final JDBCDAO jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(
					Constants.APPLICATION_NAME).getJDBCDAO();

			jdbcDAO.openSession(null);

			final List list = jdbcDAO
					.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			jdbcDAO.closeSession();

			if (!list.isEmpty())
			{
				final List columnList = (List) list.get(0);
				if (!columnList.isEmpty())
				{
					final String str = (String) columnList.get(0);
					this.logger.info("str---------------:" + str);
					if (!str.equals(""))
					{
						final int no = Integer.parseInt(str);
						return no + 1;
					}
				}
			}

			return 1;
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param dao - DAO object.
	 * @param container - StorageContainer object.
	 * @return boolean value.
	 * @throws BizLogicException throws BizLogicException
	 */
	private boolean isContainerEmpty(DAO dao, StorageContainer container) throws BizLogicException
	{

		try
		{
			// Retrieving all the occupied positions by child containers
			String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {"locatedAtPosition.positionDimensionOne",
					"locatedAtPosition.positionDimensionTwo"};
			final String[] whereColumnName = {"locatedAtPosition.parentContainer.id"};

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("locatedAtPosition.parentContainer.id",
					container.getId()));

			List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			if (!list.isEmpty())
			{
				return false;
			}
			else
			{
				// Retrieving all the occupied positions by specimens
				sourceObjectName = Specimen.class.getName();
				whereColumnName[0] = "specimenPosition.storageContainer.id";
				selectColumnName[0] = "specimenPosition.positionDimensionOne";
				selectColumnName[1] = "specimenPosition.positionDimensionTwo";

				final QueryWhereClause queryWhereClausenew = new QueryWhereClause(sourceObjectName);
				queryWhereClausenew.addCondition(new EqualClause(
						"specimenPosition.storageContainer.id", container.getId()));

				list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClausenew);

				if (!list.isEmpty())
				{
					return false;
				}
				else
				{
					// Retrieving all the occupied positions by specimens array type
					sourceObjectName = SpecimenArray.class.getName();
					whereColumnName[0] = "locatedAtPosition.parentContainer.id";
					selectColumnName[0] = "locatedAtPosition.positionDimensionOne";
					selectColumnName[1] = "locatedAtPosition.positionDimensionTwo";

					final QueryWhereClause queryWhereClauseinner = new QueryWhereClause(
							sourceObjectName);
					queryWhereClauseinner.addCondition(new EqualClause(
							"locatedAtPosition.parentContainer.id", container.getId()));
					list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClauseinner);

					if (!list.isEmpty())
					{
						return false;
					}

				}

			}

			return true;

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * Returns the data for generation of storage container tree view.
	 * @return the vector of tree nodes for the storage containers.
	 */
	public Vector getTreeViewData()
	{

		JDBCDAO dao = null;
		List list = null;
		try
		{
			dao = this.openJDBCSession();
			// Bug-2630: Added by jitendra
			final String queryStr = "SELECT "
					+ "t8.IDENTIFIER, t8.CONTAINER_NAME, t5.NAME, t8.SITE_ID, t4.TYPE, "
					+ "t8. PARENT_IDENTIFIER,  t8.PARENT_CONTAINER_NAME, t8.PARENT_CONTAINER_TYPE, "
					+ "t8. ACTIVITY_STATUS, t8.PARENT_ACTIVITY_STATUS "
					+ "FROM "
					+ "( "
					+ "SELECT "
					+ "t7. IDENTIFIER, t7.CONTAINER_NAME, t7.SITE_ID, t7.STORAGE_TYPE_ID, "
					+ "t7.ACTIVITY_STATUS, t7. PARENT_IDENTIFIER, "
					+ "t7.PARENT_CONTAINER_NAME, t6.NAME   PARENT_CONTAINER_TYPE, t7.PARENT_ACTIVITY_STATUS "
					+ "FROM "
					+ "( "
					+ "select "
					+ "t10. IDENTIFIER  IDENTIFIER, t10.CONTAINER_NAME  CONTAINER_NAME, t10.SITE_ID  SITE_ID, "
					+ "T10. ACTIVITY_STATUS  ACTIVITY_STATUS, t10.STORAGE_TYPE_ID  STORAGE_TYPE_ID, "
					+ "t10.PARENT_IDENTIFIER  PARENT_IDENTIFIER, t10.PARENT_CONTAINER_NAME  PARENT_CONTAINER_NAME, "
					+ "t22. STORAGE_TYPE_ID  PARENT_STORAGE_TYPE_ID, T10.PARENT_ACTIVITY_STATUS   PARENT_ACTIVITY_STATUS "
					+ "from "
					+ "( "
					+ "select "
					+ "t1. IDENTIFIER  IDENTIFIER, t1.NAME  CONTAINER_NAME, t11.SITE_ID  SITE_ID, "
					+ "T1. ACTIVITY_STATUS  ACTIVITY_STATUS, t11.STORAGE_TYPE_ID  STORAGE_TYPE_ID, "
					+ "t2.IDENTIFIER  PARENT_IDENTIFIER, t2.NAME  PARENT_CONTAINER_NAME, "
					+ "T2.ACTIVITY_STATUS   PARENT_ACTIVITY_STATUS "
					+ "from "
					+ "CATISSUE_STORAGE_CONTAINER t11,CATISSUE_CONTAINER t1 LEFT OUTER JOIN "
					+ "CATISSUE_CONTAINER t2 "
					+ "on t1.PARENT_CONTAINER_ID = t2.IDENTIFIER "
					+ "where t1.identifier = t11.identifier "
					+ ")t10 "
					+ "LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER t22 on t10.PARENT_IDENTIFIER = t22.identifier "
					+ ")t7 "
					+ "LEFT OUTER JOIN CATISSUE_CONTAINER_TYPE t6 on t7.PARENT_STORAGE_TYPE_ID = t6.IDENTIFIER "
					+ ") t8, CATISSUE_SITE t4, CATISSUE_CONTAINER_TYPE t5 " + "WHERE "
					+ "t8.SITE_ID = t4.IDENTIFIER  AND t8.STORAGE_TYPE_ID = t5.IDENTIFIER ";

			this.logger.debug("Storage Container query......................" + queryStr);

			list = dao.executeQuery(queryStr);
			this.getTreeNodeList(list);

			// printRecords(list);
		}
		catch (final Exception ex)
		{
			this.logger.debug(ex.getMessage(), ex);
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				this.closeJDBCSession(dao);
			}
			catch (final BizLogicException e)
			{
				this.logger.debug(e.getMessage(), e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (Vector) list;

	}

	/**
	 * Returns the vector of tree node for the storage container list.
	 * @param resultList
	 *            the storage container list.
	 * @return the vector of tree node for the storage container list.
	 * @throws BizLogicException throws BizLogicException
	 */
	public Vector getTreeNodeList(List resultList) throws BizLogicException
	{
		final Map containerRelationMap = new HashMap();

		// Vector of Tree Nodes for all the storage containers.
		final Vector treeNodeVector = new Vector();
		Vector finalNodeVector = new Vector();

		if (resultList.isEmpty() == false)
		{
			final Iterator iterator = resultList.iterator();

			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();

				// Bug-2630: Added by jitendra
				if ((String) rowList.get(8) != null
						&& !((String) rowList.get(8)).equals(Status.ACTIVITY_STATUS_DISABLED
								.toString()))
				{
					// Mandar : code for tooltip for the container
					final String toolTip = this.getToolTipData((String) rowList.get(0));

					// Create the tree node for the child node.
					final TreeNode treeNodeImpl = new StorageContainerTreeNode(Long
							.valueOf((String) rowList.get(0)), (String) rowList.get(1),
							(String) rowList.get(1), toolTip, (String) rowList.get(8));

					// Add the tree node in the Vector if it is not present.
					if (treeNodeVector.contains(treeNodeImpl) == false)
					{
						treeNodeVector.add(treeNodeImpl);
					}
				}

				if ((String) rowList.get(5) != "") // if parent container is
				// not null
				{
					List childIds = new ArrayList();

					// Create the relationship map for parent container id and
					// the child container ids.
					// Check if the parent container already has an entry in the
					// Map and get it.
					if (containerRelationMap.containsKey(Long.valueOf((String) rowList.get(5))))
					{
						childIds = (List) containerRelationMap.get(Long.valueOf((String) rowList
								.get(5)));
					}

					// Put the container in the child container list of the
					// parent container
					// and update the Map.
					childIds.add(Long.valueOf((String) rowList.get(0)));
					containerRelationMap.put(Long.valueOf((String) rowList.get(5)), childIds);

					// Create the tree node for the parent node and add it in
					// the vector if not present.
					final String toolTip = this.getToolTipData((String) rowList.get(5));
					final TreeNode treeNodeImpl = new StorageContainerTreeNode(Long
							.valueOf((String) rowList.get(5)), (String) rowList.get(6),
							(String) rowList.get(6), toolTip, (String) rowList.get(9));
					if (treeNodeVector.contains(treeNodeImpl) == false)
					{
						treeNodeVector.add(treeNodeImpl);
					}
				}

			}
			// printVectorMap(treeNodeVector, containerRelationMap);

			finalNodeVector = this.createHierarchy(containerRelationMap, treeNodeVector);
		}

		return finalNodeVector;
	}

	/**
	 * Creates the hierarchy of the tree nodes of the container according to the
	 * container relationship map.
	 * @param containerRelationMap
	 *            the container relationship map.
	 * @param treeNodeVector
	 *            the vector of tree nodes.
	 * @return the hierarchy of the tree nodes of the container according to the
	 *         container relationship map.
	 * @throws BizLogicException throws BizLogicException
	 */
	private Vector createHierarchy(Map containerRelationMap, Vector treeNodeVector)
			throws BizLogicException
	{

		// Get the ket set of the parent containers.
		final Set keySet = containerRelationMap.keySet();
		Iterator iterator = keySet.iterator();

		while (iterator.hasNext())
		{
			// Get the parent container id and create the tree node.
			final Long parentId = (Long) iterator.next();
			StorageContainerTreeNode parentTreeNodeImpl = new StorageContainerTreeNode(parentId,
					null, null);
			parentTreeNodeImpl = (StorageContainerTreeNode) treeNodeVector.get(treeNodeVector
					.indexOf(parentTreeNodeImpl));

			// Get the child container ids and create the tree nodes.
			final List childNodeList = (List) containerRelationMap.get(parentId);
			final Iterator childIterator = childNodeList.iterator();
			while (childIterator.hasNext())
			{
				final Long childId = (Long) childIterator.next();
				StorageContainerTreeNode childTreeNodeImpl = new StorageContainerTreeNode(childId,
						null, null);
				childTreeNodeImpl = (StorageContainerTreeNode) treeNodeVector.get(treeNodeVector
						.indexOf(childTreeNodeImpl));

				// Set the relationship between the parent and child tree nodes.
				childTreeNodeImpl.setParentNode(parentTreeNodeImpl);
				parentTreeNodeImpl.getChildNodes().add(childTreeNodeImpl);
			}
			// for sorting
			final Vector tempChildNodeList = parentTreeNodeImpl.getChildNodes();
			parentTreeNodeImpl.setChildNodes(tempChildNodeList);
		}

		// Get the container whose tree node has parent null
		// and get its site tree node and set it as its child.
		final Vector parentNodeVector = new Vector();
		iterator = treeNodeVector.iterator();
		// System.out.println("\nNodes without Parent\n");
		while (iterator.hasNext())
		{
			final StorageContainerTreeNode treeNodeImpl = (StorageContainerTreeNode) iterator
					.next();

			if (treeNodeImpl.getParentNode() == null)
			{
				// System.out.print("\n" + treeNodeImpl);
				TreeNodeImpl siteNode = this.getSiteTreeNode(treeNodeImpl.getIdentifier());
				// System.out.print("\tSiteNodecreated: " + siteNode);
				if (parentNodeVector.contains(siteNode))
				{
					siteNode = (TreeNodeImpl) parentNodeVector.get(parentNodeVector
							.indexOf(siteNode));
					// System.out.print("SiteNode Found");
				}
				else
				{
					parentNodeVector.add(siteNode);
					// System.out.print("\tSiteNodeSet: " + siteNode);
				}
				treeNodeImpl.setParentNode(siteNode);
				siteNode.getChildNodes().add(treeNodeImpl);

				// for sorting
				final Vector tempChildNodeList = siteNode.getChildNodes();
				siteNode.setChildNodes(tempChildNodeList);
			}
		}

		// Get the containers under site.
		final Vector containersUnderSite = this.getContainersUnderSite();
		containersUnderSite.removeAll(parentNodeVector);
		parentNodeVector.addAll(containersUnderSite);
		edu.wustl.common.util.Utility.sortTreeVector(parentNodeVector);
		return parentNodeVector;
	}

	/**
	 * @return vector of ContainersUnderSite.
	 * @throws BizLogicException throws ContainersUnderSite
	 */
	private Vector getContainersUnderSite() throws BizLogicException
	{
		JDBCDAO dao = null;
		final Vector containerNodeVector = new Vector();
		try
		{
			final String sql = "SELECT sc.IDENTIFIER, cn.NAME, scType.NAME, site.IDENTIFIER,"
					+ "site.NAME, site.TYPE from catissue_storage_container sc, "
					+ "catissue_site site, catissue_container_type scType, "
					+ "catissue_container cn where sc.SITE_ID = site.IDENTIFIER "
					+ "AND sc.STORAGE_TYPE_ID = scType.IDENTIFIER "
					+ "and sc.IDENTIFIER = cn.IDENTIFIER "
					+ "and cn.IDENTIFIER not in (select pos.CONTAINER_ID from catissue_container_position pos)";

			dao = this.openJDBCSession();
			List resultList = new ArrayList();

			resultList = dao.executeQuery(sql);
			dao.closeSession();
			// System.out.println("\nIn getContainersUnderSite()\n ");
			this.printRecords(resultList);

			final Iterator iterator = resultList.iterator();
			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				final StorageContainerTreeNode containerNode = new StorageContainerTreeNode(Long
						.valueOf((String) rowList.get(0)), (String) rowList.get(1),
						(String) rowList.get(1));
				StorageContainerTreeNode siteNode = new StorageContainerTreeNode(Long
						.valueOf((String) rowList.get(3)), (String) rowList.get(4),
						(String) rowList.get(4));

				if (containerNodeVector.contains(siteNode))
				{
					siteNode = (StorageContainerTreeNode) containerNodeVector
							.get(containerNodeVector.indexOf(siteNode));
				}
				else
				{
					containerNodeVector.add(siteNode);
				}
				containerNode.setParentNode(siteNode);
				siteNode.getChildNodes().add(containerNode);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (final DAOException e)
			{
				this.logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return containerNodeVector;
	}

	/**
	 * Returns the site tree node of the container with the given identifier.
	 * @param identifier
	 *            the identifier of the container.
	 * @return the site tree node of the container with the given identifier.
	 * @throws BizLogicException throws BizLogicException
	 */
	private TreeNodeImpl getSiteTreeNode(Long identifier) throws BizLogicException
	{
		final String sql = "SELECT site.IDENTIFIER, site.NAME, site.TYPE "
				+ " from catissue_storage_container sc, catissue_site site "
				+ " where sc.SITE_ID = site.IDENTIFIER AND sc.IDENTIFIER = "
				+ identifier.longValue();

		this.logger.debug("Site Query........................." + sql);

		final List resultList = this.executeSQL(sql);

		TreeNodeImpl siteTreeNode = null;
		if (resultList.isEmpty() == false)
		{
			final List siteRecord = (List) resultList.get(0);
			siteTreeNode = new StorageContainerTreeNode(Long.valueOf((String) siteRecord.get(0)),
					(String) siteRecord.get(1), (String) siteRecord.get(1));
		}

		return siteTreeNode;
	}

	/**
	 * 
	 */
	/**
	 * This method will add all the node into the vector that contains any
	 * container node and add a dummy container node to show [+] sign on the UI,
	 * so that on clicking expand sign ajax call will retrieve child container
	 * node under the site node.
	 * @param userId - user id
	 * @return vector of sites with dummy container
	 * @throws BizLogicException throws BizLogicException
	 */
	public Vector getSiteWithDummyContainer(Long userId) throws BizLogicException
	{
		final String sql = "SELECT site.IDENTIFIER, site.NAME,COUNT(site.NAME) FROM CATISSUE_SITE "
				+ " site join CATISSUE_STORAGE_CONTAINER sc ON sc.site_id = site.identifier join "
				+ "CATISSUE_CONTAINER con ON con.identifier = sc.identifier WHERE con.ACTIVITY_STATUS!='Disabled' "
				+ "GROUP BY site.IDENTIFIER, site.NAME" + " order by upper(site.NAME)";

		JDBCDAO dao = null;
		final Vector containerNodeVector = new Vector();

		try
		{
			dao = this.openJDBCSession();
			List resultList = new ArrayList();
			Long nodeIdentifier;
			String nodeName = null;
			String dummyNodeName = null;

			resultList = dao.executeQuery(sql);
			dao.closeSession();

			final Iterator iterator = resultList.iterator();
			final Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(userId);

			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();

				nodeIdentifier = Long.valueOf((String) rowList.get(0));

				if (this.hasPrivilegeonSite(siteIdSet, nodeIdentifier))
				{
					nodeName = (String) rowList.get(1);
					dummyNodeName = Constants.DUMMY_NODE_NAME;

					final StorageContainerTreeNode siteNode = new StorageContainerTreeNode(
							nodeIdentifier, nodeName, nodeName);
					final StorageContainerTreeNode dummyContainerNode = new StorageContainerTreeNode(
							nodeIdentifier, dummyNodeName, dummyNodeName);
					dummyContainerNode.setParentNode(siteNode);
					siteNode.getChildNodes().add(dummyContainerNode);
					containerNodeVector.add(siteNode);
				}
			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return containerNodeVector;
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node.
	 * @param nodeName
	 *            Name of the site or container
	 * @param parentId
	 *            parent identifier of the selected node
	 * @return containerNodeVector This vector contains all the containers
	 * @throws BizLogicException throws BizLogicException.
	 * @Description This method will retrieve all the containers under the
	 *              selected node
	 */
	public Vector<StorageContainerTreeNode> getStorageContainers(Long identifier, String nodeName,
			String parentId) throws BizLogicException
	{
		JDBCDAO dao = null;
		Vector<StorageContainerTreeNode> containerNodeVector = new Vector<StorageContainerTreeNode>();
		try
		{
			dao = this.openJDBCSession();
			List resultList = new ArrayList();
			if (Constants.ZERO_ID.equals(parentId))
			{
				resultList = this.getContainersForSite(identifier, dao);//Bug 11694
			}
			else
			{
				final String sql = this.createSql(identifier, parentId);
				resultList = dao.executeQuery(sql);
			}
			final String dummyNodeName = Constants.DUMMY_NODE_NAME;
			String containerName = null;
			Long nodeIdentifier;
			Long parentContainerId;
			Long childCount;
			String activityStatus = null;

			final Iterator iterator = resultList.iterator();
			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				nodeIdentifier = Long.valueOf((String) rowList.get(0));
				containerName = (String) rowList.get(1);
				activityStatus = (String) rowList.get(2);
				parentContainerId = Long.valueOf((String) rowList.get(3));
				childCount = Long.valueOf((String) rowList.get(4));

				containerNodeVector = AppUtility.getTreeNodeDataVector(containerNodeVector,
						nodeIdentifier, containerName, activityStatus, childCount,
						parentContainerId, nodeName);
			}
			if (containerNodeVector.isEmpty())
			{
				final StorageContainerTreeNode containerNode = new StorageContainerTreeNode(
						identifier, nodeName, nodeName, activityStatus);
				containerNodeVector.add(containerNode);
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (final DAOException e)
			{
				this.logger.debug(e.getMessage(), e);
				e.printStackTrace();
			}
		}

		return containerNodeVector;
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node
	 * @param parentId
	 *            Parent identifier of the selected node
	 * @return String sql This method with return the sql depending on the node
	 *         clicked (i.e parent Node or child node)
	 */
	private String createSql(Long identifier, String parentId)
	{
		final String sql = "SELECT cn.IDENTIFIER, cn.name, cn.activity_status, pos.PARENT_CONTAINER_ID,COUNT(sc3.IDENTIFIER) "
				+ "FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc ON sc.IDENTIFIER=cn.IDENTIFIER "
				+ "left outer join catissue_container_position pos on pos.CONTAINER_ID=cn.IDENTIFIER left outer join "
				+ "catissue_container_position con_pos on con_pos.PARENT_CONTAINER_ID=cn.IDENTIFIER left outer join "
				+ "CATISSUE_STORAGE_CONTAINER sc3 on con_pos.CONTAINER_ID=sc3.IDENTIFIER "
				+ "WHERE pos.PARENT_CONTAINER_ID= "
				+ identifier
				+ " AND cn.ACTIVITY_STATUS!='Disabled' GROUP BY cn.IDENTIFIER, cn.NAME, cn.activity_status, pos.PARENT_CONTAINER_ID"
				+ " ORDER BY cn.IDENTIFIER ";

		return sql;
	}

	//Bug 11694
	/**
	 * This method will return list of parent containers.
	 * @param identifier - site id
	 * @param dao - DAO object
	 * @return list of container for sites
	 * @throws BizLogicException throws BizLogicException
	 */
	private List getContainersForSite(Long identifier, JDBCDAO dao) throws BizLogicException
	{
		final List resultList = new ArrayList();
		final Map<Long, List> resultSCMap = new LinkedHashMap<Long, List>();
		List storageContainerList = new ArrayList();
		List childContainerList = new ArrayList();
		final Set childContainerIds = new LinkedHashSet();
		/**
		 *  This query will return list of all storage containers within the specified site.
		 */
		final String query = "SELECT cn.IDENTIFIER,cn.NAME,cn.ACTIVITY_STATUS FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc "
				+ "ON sc.IDENTIFIER=cn.IDENTIFIER join CATISSUE_SITE site "
				+ "ON sc.site_id = site.identifier WHERE "
				+ "cn.ACTIVITY_STATUS!='Disabled' AND site_id=" + identifier;
		try
		{
			if (dao == null)
			{
				dao.openSession(null);
			}
			storageContainerList = dao.executeQuery(query);
			final Iterator iterator = storageContainerList.iterator();
			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				final Long id = Long.valueOf((String) rowList.get(0));
				final String name = ((String) rowList.get(1));
				resultSCMap.put(id, rowList);
			}
			/**
			 * This query will return list of all child storage containers within the specified site.
			 */
			final String childQuery = "SELECT pos.CONTAINER_ID FROM CATISSUE_CONTAINER_POSITION pos "
					+ "join CATISSUE_STORAGE_CONTAINER sc ON pos.CONTAINER_ID=sc.IDENTIFIER "
					+ "WHERE sc.site_id=" + identifier;
			childContainerList = dao.executeQuery(childQuery);
			final Iterator iterator1 = childContainerList.iterator();
			while (iterator1.hasNext())
			{
				final List rowListPos = (List) iterator1.next();
				childContainerIds.add(Long.valueOf((String) rowListPos.get(0)));
			}
			final Set parentIds = resultSCMap.keySet();
			//removed all child containers 
			//this will return set of all parent(1st level) containers 
			parentIds.removeAll(childContainerIds);

			final StringBuffer parentContainerIdsBuffer = new StringBuffer();
			final Iterator it = parentIds.iterator();
			while (it.hasNext())
			{
				parentContainerIdsBuffer.append(it.next());
				parentContainerIdsBuffer.append(",");

			}
			parentContainerIdsBuffer.deleteCharAt(parentContainerIdsBuffer.length() - 1);

			/*
			 * This query will return child count of parent containers.
			 */
			final String imediateChildCountQuery = "SELECT PARENT_CONTAINER_ID,COUNT(*) FROM CATISSUE_CONTAINER_POSITION GROUP BY "
					+ "PARENT_CONTAINER_ID HAVING PARENT_CONTAINER_ID IN ("
					+ parentContainerIdsBuffer.toString() + ")";
			final List result = dao.executeQuery(imediateChildCountQuery);
			final Map<Long, Long> childCountMap = new LinkedHashMap<Long, Long>();
			final Iterator itr = result.iterator();
			while (itr.hasNext())
			{
				final List rowList = (List) itr.next();
				final Long id = Long.valueOf((String) rowList.get(0));
				final Long childCount = Long.valueOf((String) rowList.get(1));
				childCountMap.put(id, childCount);
			}
			//dao.closeSession();

			final Iterator parentContainerIterator = parentIds.iterator();
			/**
			 * Formed resultList having data as follows:
			 * 1. parent container id
			 * 2. container name
			 * 3. site id
			 * 4. child count
			 *  
			 */
			while (parentContainerIterator.hasNext())
			{
				final Long id = (Long) parentContainerIterator.next();//parent container id
				final List strorageContainerList = resultSCMap.get(id);
				//strorageContainerList -> containing container id and name
				strorageContainerList.add(identifier.toString());//site id			
				if (childCountMap.containsKey(id))
				{
					strorageContainerList.add(childCountMap.get(id).toString()); //child count
				}
				else
				{
					strorageContainerList.add("0");
				}
				resultList.add(strorageContainerList);
			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			resultSCMap.clear();
			storageContainerList.clear();
			childContainerList.clear();
			childContainerIds.clear();
		}
		return resultList;
	}

	/**
	 * @param dao - DAO object.
	 * @param parentContainer - StorageContainer object
	 * @param children - children 
	 * @return boolean array
	 * @throws BizLogicException throws BizLogicException
	 */
	private boolean[][] getStorageContainerFullStatus(DAO dao, StorageContainer parentContainer,
			Collection children) throws BizLogicException
	{
		boolean[][] fullStatus = null;

		final Integer oneDimensionCapacity = parentContainer.getCapacity()
				.getOneDimensionCapacity();
		final Integer twoDimensionCapacity = parentContainer.getCapacity()
				.getTwoDimensionCapacity();
		fullStatus = new boolean[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
				.intValue() + 1];
		if (children != null)
		{
			final Iterator iterator = children.iterator();
			this.logger.debug("storageContainer.getChildrenContainerCollection().size(): "
					+ children.size());
			while (iterator.hasNext())
			{
				final StorageContainer childStorageContainer = (StorageContainer) iterator.next();
				if (childStorageContainer.getLocatedAtPosition() != null)
				{
					final Integer positionDimensionOne = childStorageContainer
							.getLocatedAtPosition().getPositionDimensionOne();
					final Integer positionDimensionTwo = childStorageContainer
							.getLocatedAtPosition().getPositionDimensionTwo();
					this.logger.debug("positionDimensionOne : " + positionDimensionOne.intValue());
					this.logger.debug("positionDimensionTwo : " + positionDimensionTwo.intValue());
					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = true;
				}
			}
		}

		return fullStatus;
	}

	/**
	 * @param containerId - containerId.
	 * @return collection of container childrens
	 * @throws BizLogicException
	 */
	public Collection getContainerChildren(Long containerId) throws BizLogicException
	{
		DAO dao = null;
		Collection<Container> children = null;
		try
		{
			dao = this.openDAOSession(null);
			children = StorageContainerUtil.getChildren(dao, containerId);
		}
		catch (final ApplicationException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return children;
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
			// Uodate containers to disabled
			for (int i = 0; i < count; i++)
			{
				final StorageContainer container = disabledContainerList.get(i);
				dao.update(container);
			}

			this.auditDisabledObjects(dao, "CATISSUE_CONTAINER", containerIdList);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param dao - DAO object.
	 * @param sessionDataBean - SessionDataBean object
	 * @param storageContainerIDArr - array of storageContainerIDArr
	 * @throws BizLogicException throws BizLogicException
	 */
	private void disableSubStorageContainer(DAO dao, SessionDataBean sessionDataBean,
			Long storageContainerIDArr[]) throws BizLogicException
	{
		try
		{
			final List listOfSpecimenIDs = this.getRelatedObjects(dao, Specimen.class,
					"specimenPosition.storageContainer", storageContainerIDArr);

			if (!listOfSpecimenIDs.isEmpty())
			{

				throw this.getBizLogicException(null, "errors.container.contains.specimen", "");
			}

			final List listOfSubStorageContainerId = super.disableObjects(dao, Container.class,
					"locatedAtPosition.parentContainer", "CATISSUE_CONTAINER",
					"PARENT_CONTAINER_ID", storageContainerIDArr);

			if (listOfSubStorageContainerId.isEmpty())
			{
				return;
			}
			else
			{
				final Iterator itr = listOfSubStorageContainerId.iterator();
				while (itr.hasNext())
				{
					final Long contId = (Long) itr.next();
					final String sourceObjectName = StorageContainer.class.getName();

					final Object object = dao.retrieveById(sourceObjectName, contId);
					if (object != null)
					{
						final StorageContainer cont = (StorageContainer) object;
						cont.setLocatedAtPosition(null);
					}

				}
			}

			this.disableSubStorageContainer(dao, sessionDataBean, edu.wustl.common.util.Utility
					.toLongArray(listOfSubStorageContainerId));
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	// Checks for whether the user is trying to use a container without
	// privilege to use it
	// This is needed since now users can enter the values in the edit box
	/**
	 * @param dao - DAO object.
	 * @param container - StorageContainer object
	 * @param sessionDataBean - SessionDataBean object
	 * @return boolean value
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean validateContainerAccess(DAO dao, StorageContainer container,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		this.logger.debug("validateContainerAccess..................");
		final String userName = sessionDataBean.getUserName();

		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		final Long userId = sessionDataBean.getUserId();
		Site site = null;
		Set loggedInUserSiteIdSet = null;
		site = this.getSite(dao, container.getId());
		loggedInUserSiteIdSet = new UserBizLogic().getRelatedSiteIds(userId);

		if (loggedInUserSiteIdSet != null && loggedInUserSiteIdSet.contains(new Long(site.getId())))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// Checks for whether the user is trying to place the container in a
	// position
	// outside the range of parent container
	// This is needed since now users can enter the values in the edit box
	/**
	 * @param parent - StorageContainer object.
	 * @param current - StorageContainer object
	 * @return boolean value
	 */
	protected boolean validatePosition(StorageContainer parent, StorageContainer current)
	{
		final int posOneCapacity = parent.getCapacity().getOneDimensionCapacity().intValue();
		final int posTwoCapacity = parent.getCapacity().getTwoDimensionCapacity().intValue();

		return this.validatePosition(posOneCapacity, posTwoCapacity, current);
	}

	/**
	 * @param posOneCapacity - int.
	 * @param posTwoCapacity - int
	 * @param current - StorageContainer object
	 * @return boolean value
	 */
	private boolean validatePosition(int posOneCapacity, int posTwoCapacity,
			StorageContainer current)
	{
		final int positionDimensionOne = current.getLocatedAtPosition().getPositionDimensionOne()
				.intValue();
		final int positionDimensionTwo = current.getLocatedAtPosition().getPositionDimensionTwo()
				.intValue();

		this.logger.debug("validatePosition C : " + positionDimensionOne + " : "
				+ positionDimensionTwo);
		this.logger.debug("validatePosition P : " + posOneCapacity + " : " + posTwoCapacity);

		if ((positionDimensionOne > posOneCapacity) || (positionDimensionTwo > posTwoCapacity))
		{
			this.logger.debug("validatePosition false");
			return false;
		}
		this.logger.debug("validatePosition true");
		return true;
	}

	/**
	 * Bug ID: 4038 Patch ID: 4038_2 See also: 1-3
	 */
	/**
	 * This method is to validae position based on parent container id.
	 * @param dao
	 *            Object DAO
	 * @param container
	 *            current container
	 * @return boolean value based on validation
	 * @throws BizLogicException
	 *             exception occured while DB handling
	 */
	private boolean validatePosition(DAO dao, StorageContainer container) throws BizLogicException
	{
		try
		{
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {"id", "capacity.oneDimensionCapacity",
					"capacity.twoDimensionCapacity"};
			final String[] whereColumnName = {"id"}; // "storageContainer."+Constants.SYSTEM_IDENTIFIER

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("id", container.getLocatedAtPosition()
					.getParentContainer().getId()));

			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			Integer pcCapacityOne = 0;
			Integer pcCapacityTwo = 0;
			if (!list.isEmpty())
			{
				final Object[] obj1 = (Object[]) list.get(0);
				pcCapacityOne = (Integer) obj1[1];
				pcCapacityTwo = (Integer) obj1[2];

			}

			final int positionDimensionOne = container.getLocatedAtPosition()
					.getPositionDimensionOne().intValue();
			final int positionDimensionTwo = container.getLocatedAtPosition()
					.getPositionDimensionTwo().intValue();

			this.logger.debug("validatePosition C : " + positionDimensionOne + " : "
					+ positionDimensionTwo);
			this.logger.debug("validatePosition P : " + pcCapacityOne + " : " + pcCapacityTwo);

			if ((positionDimensionOne > pcCapacityOne) || (positionDimensionTwo > pcCapacityTwo))
			{
				this.logger.debug("validatePosition false");
				return false;
			}
			this.logger.debug("validatePosition true");
			return true;
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * @param dao - DAO object.
	 * @param containerIds - Long type array of containerIds
	 * @return boolean value based on container available for disabled
	 */
	private boolean isContainerAvailableForDisabled(DAO dao, Long[] containerIds)
	{
		final List containerList = new ArrayList();
		if (containerIds.length != 0)
		{
			try
			{
				String sourceObjectName = Specimen.class.getName();
				final String[] selectColumnName = {"id"};
				final String[] whereColumnName1 = {"specimenPosition.storageContainer.id"}; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
				final String[] whereColumnCondition1 = {"in"};
				final Object[] whereColumnValue1 = {containerIds};
				final String joinCondition = Constants.AND_JOIN_CONDITION;

				final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				queryWhereClause.addCondition(new INClause("specimenPosition.storageContainer.id",
						containerIds));

				List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
				// check if Specimen exists with the given storageContainer
				// information
				if (list.size() != 0)
				{
					final Object obj = list.get(0);
					return false;
				}
				else
				{
					sourceObjectName = SpecimenArray.class.getName();
					whereColumnName1[0] = "locatedAtPosition.parentContainer.id";

					final QueryWhereClause queryWhereClauseNew = new QueryWhereClause(
							sourceObjectName);
					queryWhereClauseNew.addCondition(new INClause(
							"locatedAtPosition.parentContainer.id", containerIds));

					list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClauseNew);
					// check if Specimen exists with the given storageContainer
					// information
					if (list.size() != 0)
					{
						return false;
					}
				}

			}
			catch (final Exception e)
			{
				this.logger.debug("Error in isContainerAvailable : " + e);
				return false;
			}
		}
		else
		{
			return true;
		}

		return this.isContainerAvailableForDisabled(dao, edu.wustl.common.util.Utility
				.toLongArray(containerList));
	}

	// -- to check if storageContainer is available or used
	/**
	 * @param dao -DAO object.
	 * @param current - StorageContainer object
	 * @return boolean value  based on Container available for positions
	 */
	protected boolean isContainerAvailableForPositions(DAO dao, StorageContainer current)
	{
		try
		{
			String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = {"id"};
			final String joinCondition = Constants.AND_JOIN_CONDITION;

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(
					new EqualClause("locatedAtPosition.positionDimensionOne", current
							.getLocatedAtPosition().getPositionDimensionOne())).andOpr()
					.addCondition(
							new EqualClause("locatedAtPosition.positionDimensionTwo", current
									.getLocatedAtPosition().getPositionDimensionTwo())).andOpr()
					.addCondition(
							new EqualClause("locatedAtPosition.parentContainer.id", current
									.getLocatedAtPosition().getParentContainer().getId()));

			List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			this.logger.debug("current.getParentContainer() :"
					+ current.getLocatedAtPosition().getParentContainer());
			// check if StorageContainer exists with the given storageContainer
			// information
			if (list.size() != 0)
			{
				final Object obj = list.get(0);
				this.logger
						.debug("**********IN isContainerAvailable : obj::::::: --------- " + obj);
				return false;
			}
			else
			{
				sourceObjectName = Specimen.class.getName();
				final QueryWhereClause queryWhereClauseNew = new QueryWhereClause(sourceObjectName);
				queryWhereClauseNew.addCondition(
						new EqualClause("specimenPosition.positionDimensionOne", current
								.getLocatedAtPosition().getPositionDimensionOne())).andOpr()
						.addCondition(
								new EqualClause("specimenPosition.positionDimensionTwo", current
										.getLocatedAtPosition().getPositionDimensionTwo()))
						.andOpr().addCondition(
								new EqualClause("specimenPosition.storageContainer.id", current
										.getLocatedAtPosition().getParentContainer().getId()));

				list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClauseNew);
				// check if Specimen exists with the given storageContainer
				// information
				if (list.size() != 0)
				{
					final Object obj = list.get(0);
					this.logger
							.debug("**************IN isPositionAvailable : obj::::::: --------------- "
									+ obj);
					return false;
				}
				else
				{
					sourceObjectName = SpecimenArray.class.getName();
					/*whereColumnName1[0] = "locatedAtPosition.positionDimensionOne";
					whereColumnName1[1] = "locatedAtPosition.positionDimensionTwo";
					whereColumnName1[2] = "locatedAtPosition.parentContainer.id";*/

					final QueryWhereClause queryWhereClauseInner = new QueryWhereClause(
							sourceObjectName);
					queryWhereClauseInner.addCondition(
							new EqualClause("locatedAtPosition.positionDimensionOne", current
									.getLocatedAtPosition().getPositionDimensionOne())).andOpr()
							.addCondition(
									new EqualClause("locatedAtPosition.positionDimensionTwo",
											current.getLocatedAtPosition()
													.getPositionDimensionTwo())).andOpr()
							.addCondition(
									new EqualClause("locatedAtPosition.parentContainer.id", current
											.getLocatedAtPosition().getParentContainer().getId()));

					list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClauseInner);
					// check if Specimen exists with the given storageContainer
					// information
					if (list.size() != 0)
					{
						final Object obj = list.get(0);
						this.logger
								.debug("**************IN isPositionAvailable : obj::::::: --------------- "
										+ obj);
						return false;
					}
				}

			}

			return true;
		}
		catch (final Exception e)
		{
			this.logger.debug("Error in isContainerAvailable : " + e);
			return false;
		}
	}

	// Will check only for valid range of the StorageContainer
	/**
	 * @param storageContainer - StorageContainer object.
	 * @param posOne - String 
	 * @param posTwo - String
	 * @return boolean value based on position validation
	 */
	protected boolean validatePosition(StorageContainer storageContainer, String posOne,
			String posTwo)
	{
		try
		{
			this.logger.debug("storageContainer.getCapacity().getOneDimensionCapacity() : "
					+ storageContainer.getCapacity().getOneDimensionCapacity());
			this.logger.debug("storageContainer.getCapacity().getTwoDimensionCapacity() : "
					+ storageContainer.getCapacity().getTwoDimensionCapacity());
			final int oneDimensionCapacity = (storageContainer.getCapacity()
					.getOneDimensionCapacity() != null ? storageContainer.getCapacity()
					.getOneDimensionCapacity().intValue() : -1);
			final int twoDimensionCapacity = (storageContainer.getCapacity()
					.getTwoDimensionCapacity() != null ? storageContainer.getCapacity()
					.getTwoDimensionCapacity().intValue() : -1);
			if (((oneDimensionCapacity) < Integer.parseInt(posOne))
					|| ((twoDimensionCapacity) < Integer.parseInt(posTwo)))
			{
				return false;
			}
			return true;
		}
		catch (final Exception e)
		{
			this.logger.debug("Error in validatePosition : " + e);
			return false;
		}
	}

	// Will check only for Position is used or not.
	/**
	 * @param dao - DAO object.
	 * @param storageContainer - StorageContainer object
	 * @param posOne - String
	 * @param posTwo - String
	 * @param specimen - Specimen object
	 * @return boolean value based on position available.
	 */
	protected boolean isPositionAvailable(DAO dao, StorageContainer storageContainer,
			String posOne, String posTwo, Specimen specimen)
	{
		try
		{
			String sourceObjectName = Specimen.class.getName();
			final String[] selectColumnName = {"id"};
			final String joinCondition = Constants.AND_JOIN_CONDITION;

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(
					new EqualClause("specimenPosition.positionDimensionOne", Integer
							.valueOf(posOne))).andOpr().addCondition(
					new EqualClause("specimenPosition.positionDimensionTwo", Integer
							.valueOf(posTwo))).andOpr().addCondition(
					new EqualClause("specimenPosition.storageContainer.id", storageContainer
							.getId()));

			List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			this.logger.debug("storageContainer.getId() :" + storageContainer.getId());
			// check if Specimen exists with the given storageContainer
			// information

			if (list.size() != 0)
			{
				final Object obj = list.get(0);
				boolean isPosAvail = false;
				if (specimen != null)
				{
					if ((!((specimen.getLineage()).equalsIgnoreCase("New")))
							&& ((Long) obj).longValue() == specimen.getId().longValue())
					{
						isPosAvail = true;
					}
				}
				this.logger
						.debug("**************IN isPositionAvailable : obj::::::: --------------- "
								+ obj);
				return isPosAvail;
			}
			else
			{
				sourceObjectName = StorageContainer.class.getName();

				final QueryWhereClause queryWhereClauseNew = new QueryWhereClause(sourceObjectName);
				queryWhereClauseNew.addCondition(
						new EqualClause("locatedAtPosition.positionDimensionOne", Integer
								.valueOf(posOne))).andOpr().addCondition(
						new EqualClause("locatedAtPosition.positionDimensionTwo", Integer
								.valueOf(posTwo))).andOpr().addCondition(
						new EqualClause("locatedAtPosition.parentContainer.id", storageContainer
								.getId()));

				list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClauseNew);

				this.logger.debug("storageContainer.getId() :" + storageContainer.getId());
				// check if Specimen exists with the given storageContainer
				// information
				if (list.size() != 0)
				{
					final Object obj = list.get(0);
					this.logger.debug("**********IN isPositionAvailable : obj::::: --------- "
							+ obj);
					return false;
				}
				else
				{
					sourceObjectName = SpecimenArray.class.getName();
					final QueryWhereClause queryWhereClauseInner = new QueryWhereClause(
							sourceObjectName);
					queryWhereClauseInner.addCondition(
							new EqualClause("locatedAtPosition.positionDimensionOne", Integer
									.valueOf(posOne))).andOpr().addCondition(
							new EqualClause("locatedAtPosition.positionDimensionTwo", Integer
									.valueOf(posTwo))).andOpr().addCondition(
							new EqualClause("locatedAtPosition.parentContainer.id",
									storageContainer.getId()));

					list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClauseInner);

					this.logger.debug("storageContainer.getId() :" + storageContainer.getId());
					// check if Specimen exists with the given storageContainer
					// information
					if (list.size() != 0)
					{
						final Object obj = list.get(0);
						this.logger.debug("**********IN isPositionAvailable : obj::::: --------- "
								+ obj);
						return false;
					}
				}
			}

			return true;

		}
		catch (final Exception e)
		{
			this.logger.debug("Error in isPositionAvailable : " + e);
			return false;
		}
	}

	// -- storage container validation for specimen
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

			// check if StorageContainer exists with the given ID
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
				final boolean hasAccess = this.validateContainerAccess(dao, pc, sessionDataBean);
				this.logger.debug("hasAccess..............." + hasAccess);
				if (!hasAccess)
				{

					throw this.getBizLogicException(null, "access.use.object.denied", "");
				}
				// check for closed Container
				this.checkStatus(dao, pc, "Storage Container");

				/**
				 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
				 * Description:Storage container will not be added to closed site
				 * :check for closed site
				 */

				this.checkClosedSite(dao, pc.getId(), "Container Site");

				// check for valid position
				final boolean isValidPosition = this.validatePosition(pc, positionOne, positionTwo);
				this.logger.debug("isValidPosition : " + isValidPosition);
				boolean canUsePosition = false;
				if (isValidPosition) // if position is valid
				{
					/*
					 * try {
					 */
					canUsePosition = this.isPositionAvailable(dao, pc, positionOne, positionTwo,
							specimen);

					this.logger.debug("canUsePosition : " + canUsePosition);
					if (canUsePosition) // position empty. can be used
					{

					}
					else
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
				// position is invalid
				{
					throw this.getBizLogicException(null,
							"errors.storageContainer.dimensionOverflow", "");
				}
			}
			else
			// storageContainer does not exist
			{
				throw this.getBizLogicException(null, "errors.storageContainerExist", "");
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData(edu.wustl.common.beans.SessionDataBean,
	 *      java.util.Map)
	 */
	public Vector getTreeViewData(SessionDataBean sessionData, Map map, List list)
			throws DAOException
	{
		return null;
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{

		try
		{
			final StorageContainer container = (StorageContainer) obj;

			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
			 * Search, default values will not get set for the object since
			 * setAllValues() method of domainObject will not get called. To avoid
			 * null pointer exception, we are setting the default values same we
			 * were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setContainerDefault(container);
			// End:- Change for API Search

			String message = "";
			if (container == null)
			{
				throw this.getBizLogicException(null, "domain.object.null.err.msg", "");
			}

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
			if (validator.isEmpty(container.getNoOfContainers().toString()))
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
			// validations for temperature
			if (container.getTempratureInCentigrade() != null
					&& !validator.isEmpty(container.getTempratureInCentigrade().toString())
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

				// Long storageContainerId = specimen.getStorageContainer().getId();
				final Integer xPos = container.getLocatedAtPosition().getPositionDimensionOne();
				final Integer yPos = container.getLocatedAtPosition().getPositionDimensionTwo();
				final boolean isContainerFull = false;
				/**
				 * Following code is added to set the x and y dimension in case only
				 * storage container is given and x and y positions are not given
				 */

				if (xPos == null || yPos == null)
				{
					final Container cont = container.getLocatedAtPosition().getParentContainer();
					final Position position = this.getFirstAvailablePositionInContainer(cont, dao);
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

				// VALIDATIONS FOR DIMENSION 1.
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getPositionDimensionOne() != null
						&& validator.isEmpty(String.valueOf(container.getLocatedAtPosition()
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

				// Validations for dimension 2
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getPositionDimensionTwo() != null
						&& !validator.isEmpty(String.valueOf(container.getLocatedAtPosition()
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
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * This function returns the first available position in a container which can be allocated. 
	 * If container is full, returns null
	 * @param container : Container for which available position is to be searched
	 * @return Position
	 * @throws BizLogicException
	 */
	public Position getFirstAvailablePositionInContainer(Container container, DAO dao)
			throws BizLogicException
	{

		Position position = null;
		try
		{
			Integer xPos;
			Integer yPos;

			final Capacity scCapacity = this.getContainerCapacity(container, dao);
			final Map positionMap = this.getAvailablePositionMapForContainer(String
					.valueOf(container.getId()), 0, scCapacity.getOneDimensionCapacity(),
					scCapacity.getTwoDimensionCapacity(), dao);
			if (positionMap != null)
			{
				final Iterator containerPosIterator = positionMap.keySet().iterator();
				if(containerPosIterator.hasNext())
				{
					NameValueBean nvb = (NameValueBean) containerPosIterator.next();
					xPos = Integer.valueOf(nvb.getValue());
					final List yposValues = (List) positionMap.get(nvb);
					final Iterator yposIterator = yposValues.iterator();

					if(yposIterator.hasNext())
					{
						nvb = (NameValueBean) yposIterator.next();
						yPos = Integer.valueOf(nvb.getValue());
						position = new Position();
						position.setXPos(xPos);
						position.setYPos(yPos);
					}
				}
			}
		}
		catch (final DAOException d)
		{
			d.printStackTrace();
			System.out.println();
			throw new BizLogicException(d);
		}
		return position;
	}

	public Position getFirstAvailablePositionInContainer(Container container, DAO dao,
			HashSet<String> allocatedPositions) throws BizLogicException
	{
		Position position = null;
		try
		{
			Integer xPos;
			Integer yPos;
			String containerValue = null;
			final Capacity capacity = this.getContainerCapacity(container, dao);
			final Map positionMap = this.getAvailablePositionMapForContainer(String
					.valueOf(container.getId()), 0, capacity.getOneDimensionCapacity(), capacity
					.getTwoDimensionCapacity(), dao);
			if (positionMap != null)
			{
				final Iterator containerPosIterator = positionMap.keySet().iterator();
				boolean positionAllottedFlag = false;
				while (containerPosIterator.hasNext() && !positionAllottedFlag)
				{
					NameValueBean nvb = (NameValueBean) containerPosIterator.next();
					xPos = Integer.valueOf(nvb.getValue());
					final List yposValues = (List) positionMap.get(nvb);
					final Iterator yposIterator = yposValues.iterator();

					while (yposIterator.hasNext())
					{
						nvb = (NameValueBean) yposIterator.next();
						yPos = Integer.valueOf(nvb.getValue());
						final Long containerId = container.getId();

						if (container.getName() != null)
						{
							containerValue = StorageContainerUtil.getStorageValueKey(container
									.getName(), null, xPos, yPos);
						}
						else
						{
							containerValue = StorageContainerUtil.getStorageValueKey(null,
									containerId.toString(), xPos, yPos);
						}
						if (!allocatedPositions.contains(containerValue))
						{
							positionAllottedFlag = true;
							position = new Position();
							position.setXPos(xPos);
							position.setYPos(yPos);
							break;
						}
					}
				}
			}
		}
		catch (final DAOException d)
		{
			d.printStackTrace();
			System.out.println();
			throw new BizLogicException(d);
		}
		return position;
	}

	private Capacity getContainerCapacity(Container container, DAO dao) throws DAOException
	{
		Capacity scCapacity = container.getCapacity();
		final String dim1, dim2;
		if (scCapacity == null)
		{
			scCapacity = new Capacity();
			final String[] selectColumnName = new String[]{"capacity.oneDimensionCapacity",
					"capacity.twoDimensionCapacity"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(StorageContainer.class
					.getName());
			queryWhereClause.addCondition(new EqualClause("id", container.getId()));
			final List list = dao.retrieve(StorageContainer.class.getName(), selectColumnName,
					queryWhereClause);
			final Object[] returnValues = (Object[]) list.get(0);
			scCapacity.setOneDimensionCapacity((Integer) returnValues[0]);
			scCapacity.setTwoDimensionCapacity((Integer) returnValues[1]);
		}
		return scCapacity;

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
	 * @param containerTypeId 
	 * @param specimenArrayTypeId 
	 * @param exceedingLimit 
	 * @return a list of storage containers
	 * @throws DAOException
	 */
	private List getStorageContainerList(String holdsType, final Long cpId, final String spClass,
			int aliquotCount, final SessionDataBean sessionData, Long containerTypeId,
			Long specimenArrayTypeId, String exceedingLimit) throws BizLogicException, DAOException
	{
		final JDBCDAO dao = this.openJDBCSession();

		final List containers = new ArrayList();
		try
		{
			final String queries[] = this
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
					// skip to the next query, if any
					continue;
				}

				if (resultList.size() >= remainingContainersNeeded)
				{
					containers.addAll(resultList.subList(0, remainingContainersNeeded));
					break;
				}
				containers.addAll(resultList);
				remainingContainersNeeded = containersMaxLimit - resultList.size();
			}
		}

		finally
		{
			closeJDBCSession(dao);
		}
		Logger.out.debug(String.format("%s:%s:%d", this.getClass().getSimpleName(),
				"getStorageContainers() number of containers fetched", containers.size()));
		return containers;
	}

	// TODO Write the proper business logic to return an appropriate list of
	// containers.
	/**
	 * @return list of storahe containers
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
	 * @return list of collection protocl list.
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getCollectionProtocolList() throws BizLogicException
	{
		final String sourceObjectName = CollectionProtocol.class.getName();
		final List returnList = new ArrayList();
		final NameValueBean nvb1 = new NameValueBean("--Any--", "-1");
		returnList.add(nvb1);
		final List list = this.retrieve(sourceObjectName);
		final Iterator itr = list.iterator();
		while (itr.hasNext())
		{
			final CollectionProtocol collectionProtocol = (CollectionProtocol) itr.next();
			final NameValueBean nvb = new NameValueBean(collectionProtocol.getTitle(),
					collectionProtocol);
			returnList.add(nvb);
		}
		return returnList;
	}

	/*public boolean[][] getAvailablePositionsForContainer(String containerId, int dimX, int dimY)
			throws BizLogicException
	{
		final boolean[][] positions = new boolean[dimX][dimY];

		// Initializing the array
		for (int i = 0; i < dimX; i++)
		{
			for (int j = 0; j < dimY; j++)
			{
				positions[i][j] = true;
			}
		}

		// Retrieving all the occupied positions by child containers
		String sourceObjectName = StorageContainer.class.getName();
		final String[] selectColumnName = {"locatedAtPosition.positionDimensionOne",
				"locatedAtPosition.positionDimensionTwo"};
		final String[] whereColumnName = {"locatedAtPosition.parentContainer.id"};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {new Long(containerId)};

		List list = this.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, null);
		// Logger.out.debug("all the occupied positions by child
		// containers"+list);
		this.setPositions(positions, list);

		// Retrieving all the occupied positions by specimens
		sourceObjectName = Specimen.class.getName();
		whereColumnName[0] = "specimenPosition.storageContainer.id";
		selectColumnName[0] = "specimenPosition.positionDimensionOne";
		selectColumnName[1] = "specimenPosition.positionDimensionTwo";

		list = this.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, null);
		this.setPositions(positions, list);

		// Retrieving all the occupied positions by specimens array
		sourceObjectName = SpecimenArray.class.getName();
		whereColumnName[0] = "locatedAtPosition.parentContainer.id";
		selectColumnName[0] = "locatedAtPosition.positionDimensionOne";
		selectColumnName[1] = "locatedAtPosition.positionDimensionTwo";

		list = this.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, null);
		this.setPositions(positions, list);

		return positions;
	}
	*/
	/**
	 * This functions returns a double dimensional boolean array which tells the
	 * availablity of storage positions of particular container. True -
	 * Available. False - Not Available.
	 * @param containerId - The container id.
	 * @param dimX - int
	 * @param dimY - int
	 * @param JDBCDAO dao  to be used for fetching data
	 * @return Returns a double dimensional boolean array of position
	 *         availablity.
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean[][] getAvailablePositionsForContainer(String containerId, int dimX, int dimY,
			DAO dao) throws BizLogicException
	{
		final boolean[][] positions = new boolean[dimX][dimY];
		try
		{

			for (int i = 0; i < dimX; i++)
			{
				for (int j = 0; j < dimY; j++)
				{
					positions[i][j] = true;
				}
			}

			/*final StringBuilder storageContainerQuery = new StringBuilder();
			storageContainerQuery
					.append("select ap.position_dimension_one, ap.position_dimension_two ");
			storageContainerQuery
					.append(" from catissue_abstract_position ap join catissue_container_position cp on cp.identifier=ap.identifier ");
			storageContainerQuery.append(" and cp.parent_container_id=" + containerId);
			List list = dao.executeQuery(storageContainerQuery.toString());
			*/
			//System.out.print("f");
			final String[] selectColumnName = new String[]{"positionDimensionOne",
					"positionDimensionTwo"};
			//System.out.print("c");
			final QueryWhereClause queryWhereClause = new QueryWhereClause(SpecimenPosition.class
					.getName());
			queryWhereClause.addCondition(new EqualClause("storageContainer.id", containerId));
			final List list = dao.retrieve(SpecimenPosition.class.getName(), selectColumnName,
					queryWhereClause);
			this.setPositions(positions, list);

			/*final StringBuilder specimenQuery = new StringBuilder();
			specimenQuery.append(" select ap.position_dimension_one, ap.position_dimension_two ");
			specimenQuery
					.append(" from catissue_abstract_position ap join catissue_specimen_position sp on sp.identifier=ap.identifier ");
			specimenQuery.append(" and sp.container_id=");
			specimenQuery.append(containerId);
			list = dao.executeQuery(specimenQuery.toString());*/

			final QueryWhereClause queryWhereClause2 = new QueryWhereClause(ContainerPosition.class
					.getName());
			queryWhereClause2.addCondition(new EqualClause("parentContainer.id", containerId));
			final List list2 = dao.retrieve(ContainerPosition.class.getName(), selectColumnName,
					queryWhereClause2);
			this.setPositions(positions, list2);
		}
		catch (final DAOException e)
		{
			e.printStackTrace();

			throw new BizLogicException(e);
		}
		return positions;
	}

	/**
	 * @param positions - boolean array of position.
	 * @param list - list of objects
	 */
	/**
	 * @param positions
	 * @param list
	 */
	private void setPositions(boolean[][] positions, List resultSet)
	{
		if (resultSet != null)
		{
			//			System.out.print("asd");
			int x, y;
			for (int i = 0; i < resultSet.size(); i++)
			{
				final Object[] columnList = (Object[]) resultSet.get(i);
				if ((columnList != null) && (columnList.length == 2))
				{
					x = (Integer) columnList[0];
					y = (Integer) columnList[1];
					positions[x][y] = false;
				}
			}
		}

	}

	/*private void setPositions(boolean[][] positions, List list)
	{
		if (!list.isEmpty())
		{
			int x, y;

			for (int i = 0; i < list.size(); i++)
			{
				final Object[] object = (Object[]) list.get(i);
				x = Integer.parseInt(object[0].toString());
				y = Integer.parseInt(object[1].toString());

				positions[x][y] = false;
			}
		}
	}
	*/
	/**
	 * @param containerId
	 * @param aliquotCount
	 * @param positionDimensionOne
	 * @param positionDimensionTwo
	 * @param dao
	 * @return Map
	 */
	public Map getAvailablePositionMapForContainer(String containerId, int aliquotCount,
			Integer positionDimensionOne, Integer positionDimensionTwo, DAO dao)
			throws BizLogicException
	{
		final Map map = new TreeMap();
		int count = 0;
		// Logger.out.debug("dimX:"+positionDimensionOne+":dimY:"+positionDimensionTwo);
		// if (!container.isFull().booleanValue())
		// {
		final int dimX = positionDimensionOne + 1;
		final int dimY = positionDimensionTwo + 1;

		final boolean[][] availablePosistions = this.getAvailablePositionsForContainer(containerId,
				dimX, dimY, dao);

		for (int x = 1; x < availablePosistions.length; x++)
		{

			final List list = new ArrayList();

			for (int y = 1; y < availablePosistions[x].length; y++)
			{
				if (availablePosistions[x][y])
				{
					list.add(new NameValueBean(new Integer(y), new Integer(y)));
					count++;
				}
			}

			if (!list.isEmpty())
			{
				final Integer xObj = new Integer(x);
				final NameValueBean nvb = new NameValueBean(xObj, xObj);
				map.put(nvb, list);
			}
		}
		// }
		// Logger.out.info("Map :"+map);
		if (count < aliquotCount)
		{
			return new TreeMap();
		}
		return map;
	}

	/*public Map getAvailablePositionMapForContainer(String containerId, int aliquotCount,
			String positionDimensionOne, String positionDimensionTwo) throws BizLogicException
	{
		final Map map = new TreeMap();
		int count = 0;
		final int dimX = Integer.parseInt(positionDimensionOne) + 1;
		final int dimY = Integer.parseInt(positionDimensionTwo) + 1;

		final boolean[][] availablePosistions = this.getAvailablePositionsForContainer(containerId,
				dimX, dimY);

		for (int x = 1; x < availablePosistions.length; x++)
		{

			final List list = new ArrayList();

			for (int y = 1; y < availablePosistions[x].length; y++)
			{
				if (availablePosistions[x][y])
				{
					list.add(new NameValueBean(new Integer(y), new Integer(y)));
					count++;
				}
			}

			if (!list.isEmpty())
			{
				final Integer xObj = new Integer(x);
				final NameValueBean nvb = new NameValueBean(xObj, xObj);
				map.put(nvb, list);

			}
		}
		// }
		// Logger.out.info("Map :"+map);
		if (count < aliquotCount)
		{
			return new TreeMap();
		}
		return map;
	}
	*/
	/**
	* @param dao - DAO object.
	* @param container - StorageContainer object
	* @throws BizLogicException throws BizLogicException
	*/
	protected void loadSiteFromContainerId(DAO dao, StorageContainer container)
			throws BizLogicException
	{
		try
		{
			if (container != null)
			{
				final Long sysId = container.getId();
				final Object object = dao.retrieveById(StorageContainer.class.getName(), sysId);
				// System.out.println("siteIdList " + siteIdList);
				final StorageContainer sc = (StorageContainer) object;
				// System.out.println("siteId " + sc.getSite().getId());
				container.setSite(sc.getSite());
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * @param type_id - long .
	 * @param exceedingMaxLimit - String object
	 * @param selectedContainerName - String
	 * @param sessionDataBean - SessionDataBean object
	 * @return TreeMap of allocated containers
	 * @throws BizLogicException throws BizLogicException
	 */
	public TreeMap getAllocatedContaienrMapForContainer(long type_id, String exceedingMaxLimit,
			String selectedContainerName, SessionDataBean sessionDataBean, DAO dao)
			throws BizLogicException, DAOException
	{
		final Long startTime = System.currentTimeMillis();
		final List containerList = this.getStorageContainerList(TYPE_CONTAINER, null, null, 0,
				sessionDataBean, type_id, null, null);
		final TreeMap tm = (TreeMap) this.getAllocDetailsForContainers(containerList, dao);
		final Long endTime = System.currentTimeMillis();
		System.out.println("Total a Time Taken [getAllocatedContaienrMapForSpecimen(Syed)] = "
				+ ((endTime - startTime) / 1000));
		return tm;
	}

	/* temp function end */

	/**
	 * @param scId - Long.
	 * @param dao DAO object
	 * @return Long - siet id for storage contaienr id.
	 * @throws DAOException throws DAOException
	 */
	private Long getSiteIdForStorageContainerId(Long scId, DAO dao) throws DAOException
	{

		Long siteId = null;

		final StorageContainer sc = (StorageContainer) dao.retrieveById(StorageContainer.class
				.getName(), scId);
		if (sc != null)
		{
			final Site site = sc.getSite();
			siteId = site.getId();
		}

		return siteId;
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
	public TreeMap getAllocatedContaienrMapForSpecimen(long cpId, String specimenClass,
			int aliquotCount, String exceedingMaxLimit, SessionDataBean sessionData, DAO dao)
			throws BizLogicException, DAOException
	{

		final Long startTime = System.currentTimeMillis();

		final List containerList = this.getStorageContainerList(TYPE_SPECIMEN, cpId, specimenClass,
				aliquotCount, sessionData, null, null, null);
		final TreeMap tm = (TreeMap) this.getAllocDetailsForContainers(containerList, dao);
		final Long endTime = System.currentTimeMillis();
		System.out.println("Total a Time Taken [getAllocatedContaienrMapForSpecimen(Syed)] = "
				+ ((endTime - startTime) / 1000));
		return tm;
	}

	/**
	 * @param dao - DAO object.
	 * @param sc - StorageContainer object
	 * @param sessionData - SessionDataBean object
	 * @param cpId - Long
	 * @return boolean value based on Container access validation
	 * @throws DAOException throws DAOException
	 * @throws BizLogicException throws BizLogicException
	 */
	private boolean validateContainerAccess(DAO dao, StorageContainer sc,
			SessionDataBean sessionData, long cpId) throws DAOException, BizLogicException
	{
		final boolean isValidContainer = this.validateContainerAccess(dao, sc, sessionData);

		if (sessionData != null && sessionData.isAdmin())
		{
			return true;
		}

		Collection<Site> siteCollection = null;
		Site site = null;
		if (isValidContainer)
		{

			site = this.getSite(dao, sc.getId());
			siteCollection = new CollectionProtocolBizLogic().getRelatedSites(dao, cpId);

			if (siteCollection != null)
			{
				for (final Site site1 : siteCollection)
				{
					if (site1.getId().equals(site.getId()))
					{
						return true;
					}
				}
			}

		}
		return false;
	}

	/**
	 * This function executes the query 
	 * @param query - String.
	 * @param dao = DAO object
	 * @return List of objects
	 * @throws BizLogicException
	 */
	public List executeStorageContQuery(String query, JDBCDAO dao) throws BizLogicException
	{
		this.logger.debug("Storage Container query......................" + query);
		List list = new ArrayList();

		try
		{
			list = dao.executeQuery(query);
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());

		}

		return list;
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
	public TreeMap getAllocatedContaienrMapForSpecimenArray(long specimen_array_type_id,
			int noOfAliqoutes, SessionDataBean sessionData, String exceedingMaxLimit, DAO dao)
			throws BizLogicException
	{
		final JDBCDAO jdbcDAO = null;
		try
		{
			final Long startTime = System.currentTimeMillis();
			final List containerList = this.getStorageContainerList(TYPE_SPECIMEN_ARRAY, null,
					null, 0, sessionData, null, specimen_array_type_id, null);
			final TreeMap tm = (TreeMap) this.getAllocDetailsForContainers(containerList, dao);
			final Long endTime = System.currentTimeMillis();

			System.out.println("Total a Time Taken [getAllocatedContaienrMapForSpecimen(Syed)] = "
					+ ((endTime - startTime) / 1000));
			return tm;

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	// --------------Code for Map Mandar: 04-Sep-06 start
	// Mandar : 29Aug06 : for StorageContainerMap
	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getCollectionProtocolList(String id) throws BizLogicException
	{
		// Query to return titles of collection protocol related to given
		// storagecontainer. 29-Aug-06 Mandar.
		final String sql = " SELECT SP.TITLE TITLE FROM CATISSUE_SPECIMEN_PROTOCOL SP, CATISSUE_ST_CONT_COLL_PROT_REL SC "
				+ " WHERE SP.IDENTIFIER = SC.COLLECTION_PROTOCOL_ID AND SC.STORAGE_CONTAINER_ID = "
				+ id;
		final List resultList = this.executeSQL(sql);
		final Iterator iterator = resultList.iterator();
		final List returnList = new ArrayList();
		while (iterator.hasNext())
		{
			final List list = (List) iterator.next();
			final String data = (String) list.get(0);
			returnList.add(data);
		}

		if (returnList.isEmpty())
		{
			returnList.add(new String(Constants.ALL));
		}
		return returnList;
	}

	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getSpecimenClassList(String id) throws BizLogicException
	{

		// Query to return specimen classes related to given storagecontainer.
		// 29-Aug-06 Mandar.
		final String sql = " SELECT SP.SPECIMEN_CLASS CLASS FROM CATISSUE_STOR_CONT_SPEC_CLASS SP "
				+ "WHERE SP.STORAGE_CONTAINER_ID = " + id;
		final List resultList = this.executeSQL(sql);
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
			// bug id 7438
			// returnList.add(new String(Constants.ALL));
			returnList.add(new String(Constants.NONE));
		}
		return returnList;
	}

	/**
	 * @param sql - String.
	 * @return list of objects
	 * @throws BizLogicException
	 */
	private List executeSQL(String sql) throws BizLogicException
	{
		List resultList = new ArrayList();
		JDBCDAO dao = null;
		try
		{
			dao = this.openJDBCSession();
			resultList = dao.executeQuery(sql);

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeJDBCSession(dao);
		}
		return resultList;
	}

	// prints results returned from DAO executeQuery To comment after debug
	/**
	 * @param list -List
	 */
	private void printRecords(List list)
	{
		if (list != null)
		{
			if (!list.isEmpty())
			{
				// System.out.println("OuterList Size : " + list.size());
				for (int i = 0; i < list.size(); i++)
				{
					final List innerList = (List) list.get(i);
					// System.out.println("\nInnerList Size : " +
					// innerList.size() + "\n");
					String s = "";
					for (int j = 0; j < innerList.size(); j++)
					{
						final String s1 = (String) innerList.get(j);
						s = s + " | " + s1;
					}
					// System.out.print(s);
				}
			}
		}
	}

	// Method to fetch ToolTipData for a given Container
	/**
	 * @param containerID - String.
	 * @return tool tip data
	 * @throws BizLogicException throws BizLogicException
	 */
	private String getToolTipData(String containerID) throws BizLogicException
	{
		String toolTipData = "";

		final List specimenClassList = this.getSpecimenClassList(containerID);

		String classData = "SpecimenClass";
		for (int counter = 0; counter < specimenClassList.size(); counter++)
		{
			final String data = (String) specimenClassList.get(counter);
			classData = classData + " | " + data;
		}

		final List collectionProtocolList = this.getCollectionProtocolList(containerID);

		String protocolData = "CollectionProtocol";
		for (int cnt = 0; cnt < collectionProtocolList.size(); cnt++)
		{
			final String data = (String) collectionProtocolList.get(cnt);
			protocolData = protocolData + " | " + data;
		}

		toolTipData = protocolData + "\n" + classData;
		// System.out.println(toolTipData);

		return toolTipData;
	}

	// --------------Code for Map Mandar: 04-Sep-06 end

	// this function is for making the deep copy of map

	/**
	 * @param positionMap - Map
	 * @return Map
	 */
	private Map deepCopyMap(Map positionMap)
	{
		final Map positionMap1 = new TreeMap();
		final Set keySet = positionMap.keySet();
		final Iterator itr = keySet.iterator();

		while (itr.hasNext())
		{
			final NameValueBean key = (NameValueBean) itr.next();
			final NameValueBean key1 = new NameValueBean(new Integer(key.getName()), new Integer(
					key.getValue()));
			final List value = (ArrayList) positionMap.get(key);
			final List value1 = new ArrayList();
			final Iterator itr1 = value.iterator();

			while (itr1.hasNext())
			{
				final NameValueBean ypos = (NameValueBean) itr1.next();
				final NameValueBean ypos1 = new NameValueBean(new Integer(ypos.getName()),
						new Integer(ypos.getValue()));
				value1.add(ypos1);

			}
			positionMap1.put(key1, value1);

		}
		return positionMap1;
	}

	/**
	 * @param positionMap - Map
	 * @return Long - count Position in map.
	 */
	private long countPositionsInMap(Map positionMap)
	{
		long count = 0;
		final Set keySet = positionMap.keySet();
		final Iterator itr = keySet.iterator();
		while (itr.hasNext())
		{
			final NameValueBean key = (NameValueBean) itr.next();
			final List value = (ArrayList) positionMap.get(key);
			count = count + value.size();

		}
		return count;
	}

	/**
	 * Bug ID: 4038 Patch ID: 4038_3 See also: 1-3
	 */
	/**
	 * @param dao
	 *            Object of DAO
	 * @param containerId
	 *            id of container whose site is to be retrieved
	 * @return Site object belongs to container with given id
	 * @throws BizLogicException
	 *             Exception occured while DB handling
	 */
	private Site getSite(DAO dao, Long containerId) throws BizLogicException
	{
		try
		{
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = new String[]{"site"};
			final String[] whereColumnName = new String[]{"id"};
			final String[] whereColumnCondition = new String[]{"="};
			final Object[] whereColumnValue = new Long[]{containerId};
			final String joinCondition = null;

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("id", containerId));
			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

			if (!list.isEmpty())
			{
				return ((Site) list.get(0));
			}
			return null;
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
				.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	/**
	 * Retrieve activity status of the Site.
	 * @param dao
	 * @param containerId
	 * @return String string
	 * @throws DAOException
	 */
	private String getSiteId(DAO dao, Long containerId) throws BizLogicException
	{
		String activityStatus = null;
		try
		{
			final String sourceObjectName = StorageContainer.class.getName();
			final String[] selectColumnName = new String[]{"site.activityStatus"};
			final String[] whereColumnName = new String[]{"id"};
			final String[] whereColumnCondition = new String[]{"="};
			final Object[] whereColumnValue = new Long[]{containerId};
			final String joinCondition = null;
			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("id", containerId));
			final List list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
			if (!list.isEmpty())
			{
				activityStatus = ((String) list.get(0));
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
				.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return activityStatus;
	}

	/**
	 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
	 * Description:Storage container will not be added to closed site :check for
	 * closed site
	 */
	/**
	 * @param dao DAO object.
	 * @param containerId - Long.
	 * @param errMessage - String.
	 * @throws BizLogicException throws BizLogicException
	 */
	public void checkClosedSite(DAO dao, Long containerId, String errMessage)
			throws BizLogicException
	{

		/*final Site site = this.getSite(dao, containerId);

		// check for closed Site
		if (site != null)
		{
			if (Status.ACTIVITY_STATUS_CLOSED.toString().equals(site.getActivityStatus()))
			{

				throw this.getBizLogicException(null, "error.object.closed", errMessage);
			}
		}*/
		final String siteActivityStatus = this.getSiteId(dao, containerId);

		// check for closed Site
		if (siteActivityStatus != null)
		{
			if (Status.ACTIVITY_STATUS_CLOSED.toString().equals(siteActivityStatus))
			{
				throw this.getBizLogicException(null, "error.object.closed", errMessage);
			}
		}
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
	 * To check wether the Continer to display can holds the given type of
	 * container.
	 * @param typeId
	 *            ContinerType id of container
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the typet.
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean canHoldContainerType(int typeId, StorageContainer storageContainer)
			throws BizLogicException
	{
		/**
		 * Name: Smita Reviewer: Sachin Bug iD: 4598 Patch ID: 4598_1
		 * Description: Check for valid container type
		 */
		if (!this.isValidContaierType(typeId))
		{
			return false;
		}

		final boolean canHold = false;
		final Collection containerTypeCollection = (Collection) this.retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(holdsStorageTypeCollection)");// storageContainer.getHoldsStorageTypeCollection();
		if (!containerTypeCollection.isEmpty())
		{
			final Iterator itr = containerTypeCollection.iterator();
			while (itr.hasNext())
			{
				final StorageType type = (StorageType) itr.next();
				final long storagetypeId = type.getId().longValue();
				if (storagetypeId == Constants.ALL_STORAGE_TYPE_ID || storagetypeId == typeId)
				{
					return true;
				}
			}
		}
		return canHold;
	}

	/**
	 * Patch ID: 4598_2 Is container type one from the container types present
	 * in the system.
	 * 
	 * @param typeID
	 *            Container type ID
	 * @return true/ false
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean isValidContaierType(int typeID) throws BizLogicException
	{
		final Long longId = (Long) this.retrieveAttribute(StorageType.class.getName(), new Long(
				typeID), "id");
		return !(longId == null);
	}

	/**
	 * To check wether the Continer to display can holds the given
	 * CollectionProtocol.
	 * @param collectionProtocolId
	 *            The collectionProtocol Id.
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the CollectionProtocol.
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean canHoldCollectionProtocol(long collectionProtocolId,
			StorageContainer storageContainer) throws BizLogicException
	{
		boolean canHold = true;
		final Collection collectionProtocols = (Collection) this.retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(collectionProtocolCollection)");// storageContainer.getCollectionProtocolCollection();
		if (!collectionProtocols.isEmpty())
		{
			final Iterator itr = collectionProtocols.iterator();
			canHold = false;
			while (itr.hasNext())
			{
				final CollectionProtocol cp = (CollectionProtocol) itr.next();
				if (cp.getId().longValue() == collectionProtocolId)
				{
					return true;

				}
			}
		}
		return canHold;
	}

	/**
	 * To check wether the Continer to display can holds the given
	 * specimenClass.
	 * @param specimenClass
	 *            The specimenClass Name.
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the specimenClass.
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean canHoldSpecimenClass(String specimenClass, StorageContainer storageContainer)
			throws BizLogicException
	{
		final Collection specimenClasses = (Collection) this.retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(holdsSpecimenClassCollection)");// storageContainer.getHoldsSpecimenClassCollection();
		final Iterator itr = specimenClasses.iterator();
		while (itr.hasNext())
		{
			final String className = (String) itr.next();
			if (className.equals(specimenClass))
			{
				return true;
			}

		}
		return false;
	}

	/**
	 * To check wether the Continer to display can holds the given
	 * specimenArrayTypeId.
	 * @param specimenArrayTypeId
	 *            The Specimen Array Type Id.
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the specimenArrayType.
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean canHoldSpecimenArrayType(int specimenArrayTypeId,
			StorageContainer storageContainer) throws BizLogicException
	{

		boolean canHold = true;
		final Collection specimenArrayTypes = (Collection) this.retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(holdsSpecimenArrayTypeCollection)");// storageContainer.getHoldsSpArrayTypeCollection();
		// if (!specimenArrayTypes.isEmpty())
		{
			final Iterator itr = specimenArrayTypes.iterator();
			canHold = false;
			while (itr.hasNext())
			{
				final SpecimenArrayType specimenarrayType = (SpecimenArrayType) itr.next();
				final long arraytypeId = specimenarrayType.getId().longValue();

				if (arraytypeId == Constants.ALL_SPECIMEN_ARRAY_TYPE_ID
						|| arraytypeId == specimenArrayTypeId)
				{
					return true;
				}
			}
		}
		return canHold;
	}

	/**
	 * @param dao - DAO object.
	 * @param containerId - Long 
	 * @return  collection of SpecimenPosition
	 * @throws BizLogicException throws BizLogicException
	 */
	public Collection<SpecimenPosition> getSpecimenPositionCollForContainer(DAO dao,
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
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return null;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
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
					this.logger.debug(e.getMessage(), e);
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
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_STORAGE_CONTAINER;
	}

	/**
	 * Gives the Site Object related to given container whose name is given.
	 * @param containerName - String 
	 * @return Site - site object
	 */
	public Site getRelatedSiteForManual(String containerName, DAO dao) throws BizLogicException
	{
		Site site = null;
		try
		{
			if (containerName != null && !("").equals(containerName))
			{

				StorageContainer storageContainer = null;
				final String[] strArray = {containerName};
				List contList = null;

				if (strArray != null)
				{
					contList = dao.retrieve(StorageContainer.class.getName(), Constants.NAME,
							containerName);
				}
				if (contList != null && !contList.isEmpty())
				{
					storageContainer = (StorageContainer) contList.get(0);
				}

				if (storageContainer != null)
				{
					site = storageContainer.getSite();
				}
			}
		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}

		return site;
	}

	/**
	 * Gives List of NameValueBean of CP for given cpIds array.
	 * @param cpIds - Long array of cpIds
	 * @return NameValueBean - List of NameValueBean
	 */
	public List<NameValueBean> getCPNameValueList(long[] cpIds) throws BizLogicException
	{
		DAO dao = null;
		final List<NameValueBean> cpNameValueList = new ArrayList<NameValueBean>();

		try
		{
			dao = this.openDAOSession(null);
			NameValueBean cpNameValueBean;
			for (final long cpId : cpIds)
			{
				if (cpId != -1)
				{
					CollectionProtocol cp = new CollectionProtocol();

					cp = (CollectionProtocol) dao.retrieveById(CollectionProtocol.class.getName(),
							cpId);

					final String cpShortTitle = cp.getShortTitle();
					cpNameValueBean = new NameValueBean(cpShortTitle, cpId);
					cpNameValueList.add(cpNameValueBean);
				}
			}
		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
		}
		finally
		{
			this.closeDAOSession(dao);
		}

		return cpNameValueList;
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
			//Object container[] = (Object[]) itr.next();
			final ArrayList container = (ArrayList) itr.next();
			final Map positionMap = this.getAvailablePositionMapForContainer(String
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
				.append("SELECT t1.IDENTIFIER,t1.name,c.one_dimension_capacity , c.two_dimension_capacity ");
		sb
				.append(" from CATISSUE_CONTAINER t1 join catissue_capacity c on t1.capacity_id=c.identifier ");
		sb.append(" join CATISSUE_STORAGE_CONTAINER t2 on t2.identifier=t1.identifier ");
		sb.append(" join catissue_site S on S.identifier = t2.SITE_ID");
		sb.append(" where t2.IDENTIFIER IN ");
		sb
				.append(" (select t4.STORAGE_CONTAINER_ID from CATISSUE_CONT_HOLDS_SPARRTYPE t4  where t4.SPECIMEN_ARRAY_TYPE_ID = '");
		sb.append(specimen_array_type_id);
		sb.append("'");
		sb.append(includeAllIdQueryStr);
		sb.append(") ");
		if (!sessionData.isAdmin())
		{
			sb.append(" and t2.SITE_ID in (SELECT SITE_ID from CATISSUE_SITE_USERS where USER_ID="
					+ sessionData.getUserId() + ")");
		}
		sb.append("AND t1.ACTIVITY_STATUS='" + Status.ACTIVITY_STATUS_ACTIVE
				+ "' and t1.CONT_FULL=0 ");
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
				.append("SELECT cont.IDENTIFIER, cont.NAME, cap.ONE_DIMENSION_CAPACITY, cap.TWO_DIMENSION_CAPACITY ");
		sb.append("FROM CATISSUE_CAPACITY cap JOIN CATISSUE_CONTAINER cont ");
		sb.append("	on cap.IDENTIFIER = cont.CAPACITY_ID where  ");
		sb.append(" cont.IDENTIFIER IN (SELECT t4.STORAGE_CONTAINER_ID ");
		sb.append("  FROM CATISSUE_ST_CONT_ST_TYPE_REL t4 ");
		sb.append("WHERE t4.STORAGE_TYPE_ID = '" + type_id);
		sb.append("' OR t4.STORAGE_TYPE_ID='1' and t4.STORAGE_CONTAINER_ID in ");
		sb.append(" (select SC.IDENTIFIER from CATISSUE_STORAGE_CONTAINER SC ");
		sb
				.append(" join CATISSUE_SITE S on sc.site_id=S.IDENTIFIER and S.ACTIVITY_STATUS!='Closed' ");
		if (!sessionData.isAdmin())
		{
			sb.append(" and S.IDENTIFIER in(SELECT SITE_ID from CATISSUE_SITE_USERS where USER_ID="
					+ sessionData.getUserId() + ")");
		}
		sb.append("))");
		sb.append("AND cont.ACTIVITY_STATUS='" + Status.ACTIVITY_STATUS_ACTIVE);
		sb.append("' and cont.CONT_FULL=0 order by IDENTIFIER");
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
		// Containers allowing Other CPs also but just this Specimen Class
		final String q2 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), IS_CP_NONUNIQUE, IS_SPCLASS_UNIQUE);
		// Containers allowing Others CPs also and other Specimen Classes too
		final String q3 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), IS_CP_NONUNIQUE, IS_SPCLASS_NONUNIQUE);
		//Containers allowing any CP and other Specimen Classes too
		final String q4 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), null, IS_SPCLASS_NONUNIQUE);
		//Containers allowing any CP and any Specimen Class
		final String q5 = this.createSCQuery(cpId, spClass, aliquotCount, sessionData.getUserId(),
				sessionData.isAdmin(), null, null);
		return new String[]{q0, q1, q2, q3, q4, q5};
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
		sb.append("  L.ACTIVITY_STATUS = 'Active' and D.CONT_FULL=0 "); //Added cont_full condition by Preeti
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
			sb.append(" AA WHERE AA.STORAGE_CONTAINER_ID = VIEW1.IDENTIFIER " );
			sb.append(" ) " );
			sb.append(" =4 " );	//No restriction on specimen class means it can store any of the 4 specimen classes
			return sb.toString();
		}
		else
		{
			return this.getSCBaseRestrictionQuery("CATISSUE_STOR_CONT_SPEC_CLASS", isUnique);
		}
	}

	public boolean isContainerFull(String containerId, String containerName)
			throws BizLogicException
	{

		/*System.out.println();
		query.append("select cont.id from " + Container.class.getName() + " as cont ");
		query.append("where cont.id= " + containerId);
		query
				.append(" and ((cont.capacity.oneDimensionCapacity * cont.capacity.twoDimensionCapacity) = ");
		query.append(" (select count(*) from " + SpecimenPosition.class.getName());
		query.append(" as specPos where specPos.storageContainer.id=" + containerId + ")");
		query
				.append(" or (cont.capacity.oneDimensionCapacity * cont.capacity.twoDimensionCapacity) = ");
		query.append(" (select count(*) from " + ContainerPosition.class.getName());
		query.append(" as contPos where contPos.parentContainer.id=" + containerId + "))");
		List contList = executeQuery(query.toString());
		if ((contList != null) && (contList.size() > 0))
		{*/
		final JDBCDAO jdbcDAO = this.openJDBCSession();
		final Long freePositions = this.getCountofFreeLocationOfContainer(jdbcDAO, containerId,
				containerName);
		this.closeJDBCSession(jdbcDAO);
		if (freePositions == 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public boolean canReduceDimension(DAO dao, Long storageContainerId, Integer dimOne,
			Integer dimTwo) throws DAOException
	{
		if (!this.isSpecimenAssignedWithinDimensions(dao, storageContainerId, dimOne, dimTwo))
		{
			if (!this.isContainerAssignedWithinDimensions(dao, storageContainerId, dimOne, dimTwo))
			{
				return true;
			}
		}
		return false;

	}

	private boolean isContainerAssignedWithinDimensions(DAO dao, Long storageContainerId,
			Integer dimOne, Integer dimTwo) throws DAOException
	{
		final StringBuilder contQuery = new StringBuilder();
		contQuery.append("from ContainerPosition cp ");
		contQuery.append("where cp.parentContainer.id =");
		contQuery.append(storageContainerId);
		contQuery.append(" and (cp.positionDimensionOne>");
		contQuery.append(dimOne);
		contQuery.append(" or cp.positionDimensionTwo>");
		contQuery.append(dimOne);
		contQuery.append(")");
		final List contList = dao.executeQuery(contQuery.toString());
		if ((contList != null) && (contList.size() > 0))
		{
			return true;
		}
		return false;
	}

	private boolean isSpecimenAssignedWithinDimensions(DAO dao, Long storageContainerId,
			Integer dimOne, Integer dimTwo) throws DAOException
	{
		final StringBuilder specQuery = new StringBuilder();
		specQuery.append("from SpecimenPosition sp ");
		specQuery.append("where sp.storageContainer.id =");
		specQuery.append(storageContainerId);
		specQuery.append(" and (sp.positionDimensionOne>");
		specQuery.append(dimOne);
		specQuery.append(" or sp.positionDimensionTwo>");
		specQuery.append(dimOne);
		specQuery.append(")");
		final List list = dao.executeQuery(specQuery.toString());
		if ((list != null) && (list.size() > 0))
		{
			return true;
		}
		return false;
	}

	public Long getCountofFreeLocationOfContainer(JDBCDAO jdbcDAO, String containerId,
			String storageContainerName) throws BizLogicException
	{
		long freeLocations = 0;
		final StringBuilder totalCapacityQuery = new StringBuilder();
		final StringBuilder query = new StringBuilder();
		if ((containerId == null) || (containerId.trim().equals("")))
		{
			containerId = this.getContainerId(jdbcDAO, storageContainerName);
		}

		query
				.append("select (capacity.one_dimension_capacity * capacity.two_dimension_capacity -view1.specLocations-view2.contLocns)");
		query
				.append(" from catissue_container  cont join catissue_capacity  capacity on cont.capacity_id=capacity.identifier,");
		query
				.append(" (select count(*)  specLocations from catissue_specimen_position sp where container_id = "
						+ containerId + ") view1,");
		query
				.append(" (select count(*)  contLocns from catissue_container_position cp where cp.parent_container_id="
						+ containerId + ") view2 ");
		query.append(" where cont.identifier = " + containerId);
		try
		{
			final List results = jdbcDAO.executeQuery(query.toString());
			final Object result = this.getResultSetData(results, 0, 0);
			if ((result != null) && (result instanceof String))
			{
				try
				{
					freeLocations = Long.valueOf((String) result);
				}
				catch (final NumberFormatException e)
				{
					freeLocations = 0;
				}
			}
		}
		catch (final DAOException e1)
		{
			e1.printStackTrace();
			throw new BizLogicException(e1);
		}

		return freeLocations;
	}

	private String getContainerId(JDBCDAO jdbcDAO, String containerName) throws BizLogicException
	{
		final StringBuilder query = new StringBuilder();
		query.append("select identifier from catissue_container cont ");
		query.append("where cont.name='" + containerName + "'");
		if (jdbcDAO != null)
		{
			try
			{
				final List results = jdbcDAO.executeQuery(query.toString());
				final Object result = this.getResultSetData(results, 0, 0);
				if ((result != null) && (result instanceof String))
				{
					return (String) result;
				}
			}
			catch (final DAOException daoExp)
			{
				throw new BizLogicException(daoExp);
			}
		}
		return null;
	}

	private Object getResultSetData(List resultSet, int rowNumber, int columnNumber)
	{
		//Get 0th row and 0th column of the resultset
		if ((resultSet != null) && (resultSet.size() > rowNumber))
		{
			final List columnList = (List) resultSet.get(rowNumber);
			if ((columnList != null) && (columnList.size() > columnNumber))
			{
				return columnList.get(columnNumber);
			}
		}
		return null;
	}

	public boolean isPositionAvailable(JDBCDAO jdbcDao, String containerId, String containerName,
			String pos1, String pos2) throws BizLogicException
	{
		if (!this.isSpecimenAssigned(jdbcDao, containerId, containerName, pos1, pos2))
		{
			if (!this.isContainerAssigned(jdbcDao, containerId, containerName, pos1, pos2))
			{
				return true;
			}
		}
		return false;

	}

	private boolean isContainerAssigned(JDBCDAO jdbcDao, String containerId, String containerName,
			String pos1, String pos2) throws BizLogicException
	{
		final StringBuilder query = new StringBuilder();
		if ((containerId == null) || (containerId.trim().equals("")))
		{
			query.append("select id from " + ContainerPosition.class.getName()
					+ " contPos where contPos.parentContainer.name='" + containerName + "'");
		}
		else
		{
			query.append("select id from " + ContainerPosition.class.getName()
					+ " contPos where contPos.parentContainer.id=" + containerId);
		}

		query.append(" and contPos.positionDimensionOne=" + pos1
				+ " and contPos.positionDimensionTwo=" + pos2);
		final List allocatedList = this.executeQuery(query.toString());

		if ((allocatedList != null) && (allocatedList.size() > 0))
		{
			return true;
		}
		return false;
	}

	private boolean isSpecimenAssigned(JDBCDAO jdbcDao, String containerId, String containerName,
			String pos1, String pos2) throws BizLogicException
	{
		final StringBuilder query = new StringBuilder();
		if ((containerId == null) || (containerId.trim().equals("")))
		{
			query.append("select id from " + SpecimenPosition.class.getName()
					+ " specPos where specPos.storageContainer.name='" + containerName + "'");
		}
		else
		{
			query.append("select id from " + SpecimenPosition.class.getName()
					+ " specPos where specPos.storageContainer.id=" + containerId);
		}
		query.append(" and specPos.positionDimensionOne=" + pos1
				+ "  and specPos.positionDimensionTwo=" + pos2);
		final List allocatedList = this.executeQuery(query.toString());
		if ((allocatedList != null) && (allocatedList.size() > 0))
		{
			return true;
		}
		return false;
	}

	/**Gives the Site Object related to given container
	 * @param containerId - Long
	 * @return Site object
	 * @throws BizLogicException throws BizLogicException
	 */
	public Site getRelatedSite(Long containerId, DAO dao) throws BizLogicException
	{
		Site site = null;
		try
		{
			if (containerId >= 1)
			{
				StorageContainer storageContainer = null;
				storageContainer = (StorageContainer) dao.retrieveById(StorageContainer.class
						.getName(), containerId);

				if (storageContainer != null)
				{
					site = storageContainer.getSite();
				}
			}
		}
		catch (final DAOException e)
		{
			this.logger.debug(e.getMessage(), e);
		}

		return site;
	}
}