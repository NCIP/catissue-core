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
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
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
import edu.wustl.common.tree.StorageContainerTreeNode;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.tree.TreeNode;
import edu.wustl.common.tree.TreeNodeImpl;
import edu.wustl.common.util.NameValueBeanRelevanceComparator;
import edu.wustl.common.util.NameValueBeanValueComparator;
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
	 * Getting containersMaxLimit from the xml file in static variable
	 */
	private static final int containersMaxLimit = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.CONTAINERS_MAX_LIMIT));

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
		final StorageContainer container = (StorageContainer) obj;
		try
		{

			final Map containerMap = StorageContainerUtil.getContainerMapFromCache();
			StorageContainerUtil.addStorageContainerInContainerMap(container, containerMap);

		}
		catch (final Exception e)
		{
			this.logger.debug(e.getMessage(), e);
		}
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
				final boolean canReduceDimension = StorageContainerUtil.checkCanReduceDimension(
						oldContainer, container);
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

					try
					{
						final CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
								.getInstance();
						catissueCoreCacheManager.addObjectToCache(
								Constants.MAP_OF_DISABLED_CONTAINERS, (Serializable) disabledConts);
					}
					catch (final CacheException e)
					{
						this.logger.debug(e.getMessage(), e);
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
			final Map containerMap = StorageContainerUtil.getContainerMapFromCache();
			final StorageContainer currentContainer = (StorageContainer) currentObj;
			final StorageContainer oldContainer = (StorageContainer) oldObj;

			// if name gets change then update the cache with new key
			if (!currentContainer.getName().equals(oldContainer.getName()))
			{
				StorageContainerUtil
						.updateNameInCache(containerMap, currentContainer, oldContainer);
			}

			// If capacity of container gets increased then insert all the new
			// positions in map ..........
			final int xOld = oldContainer.getCapacity().getOneDimensionCapacity().intValue();
			final int xNew = currentContainer.getCapacity().getOneDimensionCapacity().intValue();
			final int yOld = oldContainer.getCapacity().getTwoDimensionCapacity().intValue();
			final int yNew = currentContainer.getCapacity().getTwoDimensionCapacity().intValue();
			if (xNew != xOld || yNew != yOld)
			{
				StorageContainerUtil.updateStoragePositions(containerMap, currentContainer,
						oldContainer);

			}
			// finish
			if (oldContainer != null && oldContainer.getLocatedAtPosition() != null
					&& oldContainer.getLocatedAtPosition().getParentContainer() != null)
			{
				final StorageContainer oldParentCont = (StorageContainer) HibernateMetaData
						.getProxyObjectImpl(oldContainer.getLocatedAtPosition()
								.getParentContainer());
				StorageContainerUtil.insertSinglePositionInContainerMap(oldParentCont,
						containerMap, oldContainer.getLocatedAtPosition().getPositionDimensionOne()
								.intValue(), oldContainer.getLocatedAtPosition()
								.getPositionDimensionTwo().intValue());
			}
			if (currentContainer != null && currentContainer.getLocatedAtPosition() != null
					&& currentContainer.getLocatedAtPosition().getParentContainer() != null)
			{
				final StorageContainer currentParentCont = (StorageContainer) currentContainer
						.getLocatedAtPosition().getParentContainer();
				StorageContainerUtil.deleteSinglePositionInContainerMap(currentParentCont,
						containerMap, currentContainer.getLocatedAtPosition()
								.getPositionDimensionOne().intValue(), currentContainer
								.getLocatedAtPosition().getPositionDimensionTwo().intValue());
			}

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

					StorageContainerUtil.removeStorageContainerInContainerMap(cont, containerMap);
				}

			}

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
					+ "t7.PARENT_CONTAINER_NAME, t6.NAME AS  PARENT_CONTAINER_TYPE, t7.PARENT_ACTIVITY_STATUS "
					+ "FROM "
					+ "( "
					+ "select "
					+ "t10. IDENTIFIER AS IDENTIFIER, t10.CONTAINER_NAME AS CONTAINER_NAME, t10.SITE_ID AS SITE_ID, "
					+ "T10. ACTIVITY_STATUS AS ACTIVITY_STATUS, t10.STORAGE_TYPE_ID AS STORAGE_TYPE_ID, "
					+ "t10.PARENT_IDENTIFIER AS PARENT_IDENTIFIER, t10.PARENT_CONTAINER_NAME AS PARENT_CONTAINER_NAME, "
					+ "t22. STORAGE_TYPE_ID AS PARENT_STORAGE_TYPE_ID, T10.PARENT_ACTIVITY_STATUS AS  PARENT_ACTIVITY_STATUS "
					+ "from "
					+ "( "
					+ "select "
					+ "t1. IDENTIFIER AS IDENTIFIER, t1.NAME AS CONTAINER_NAME, t11.SITE_ID AS SITE_ID, "
					+ "T1. ACTIVITY_STATUS AS ACTIVITY_STATUS, t11.STORAGE_TYPE_ID AS STORAGE_TYPE_ID, "
					+ "t2.IDENTIFIER AS PARENT_IDENTIFIER, t2.NAME AS PARENT_CONTAINER_NAME, "
					+ "T2.ACTIVITY_STATUS AS  PARENT_ACTIVITY_STATUS "
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
		final Vector<StorageContainerTreeNode> containerNodeVector = new Vector<StorageContainerTreeNode>();
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

				StorageContainerTreeNode containerNode = new StorageContainerTreeNode(
						nodeIdentifier, containerName, containerName, activityStatus);
				final StorageContainerTreeNode parneContainerNode = new StorageContainerTreeNode(
						parentContainerId, nodeName, nodeName, activityStatus);

				if (childCount != null && childCount > 0)
				{
					final StorageContainerTreeNode dummyContainerNode = new StorageContainerTreeNode(
							Long.valueOf((String) rowList.get(0)), dummyNodeName, dummyNodeName,
							activityStatus);
					dummyContainerNode.setParentNode(containerNode);
					containerNode.getChildNodes().add(dummyContainerNode);
				}

				if (containerNodeVector.contains(containerNode))
				{
					containerNode = (StorageContainerTreeNode) containerNodeVector
							.get(containerNodeVector.indexOf(containerNode));
				}
				else
				{
					containerNodeVector.add(containerNode);
				}
				containerNode.setParentNode(parneContainerNode);
				parneContainerNode.getChildNodes().add(containerNode);
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
		String sql;
		sql = "SELECT cn.IDENTIFIER, cn.name, cn.activity_status, pos.PARENT_CONTAINER_ID,COUNT(sc3.IDENTIFIER) "
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
			dao.openSession(null);
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
			dao.closeSession();

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

		return validatePosition( posOneCapacity, posTwoCapacity, current);
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
							new EqualClause("locatedAtPosition.parentContainer", current
									.getLocatedAtPosition().getParentContainer()));

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
				boolean isContainerFull = false;
				/**
				 * Following code is added to set the x and y dimension in case only
				 * storage container is given and x and y positions are not given
				 */

				if (xPos == null || yPos == null)
				{
					isContainerFull = true;
					Map containerMapFromCache = null;
					try
					{
						containerMapFromCache = (TreeMap) StorageContainerUtil
								.getContainerMapFromCache();
					}
					catch (final CacheException e)
					{
						this.logger.debug(e.getMessage(), e);
						e.printStackTrace();
					}

					if (containerMapFromCache != null)
					{
						final Iterator itr = containerMapFromCache.keySet().iterator();
						while (itr.hasNext())
						{
							final NameValueBean nvb = (NameValueBean) itr.next();
							if (container.getLocatedAtPosition() != null
									&& container.getLocatedAtPosition().getParentContainer() != null
									&& nvb.getValue().toString().equals(
											container.getLocatedAtPosition().getParentContainer()
													.getId().toString()))
							{

								final Map tempMap = (Map) containerMapFromCache.get(nvb);
								final Iterator tempIterator = tempMap.keySet().iterator();;
								final NameValueBean nvb1 = (NameValueBean) tempIterator.next();

								final List list = (List) tempMap.get(nvb1);
								final NameValueBean nvb2 = (NameValueBean) list.get(0);

								final ContainerPosition cntPos = container.getLocatedAtPosition();

								cntPos.setPositionDimensionOne(new Integer(nvb1.getValue()));
								cntPos.setPositionDimensionTwo(new Integer(nvb2.getValue()));
								cntPos.setOccupiedContainer(container);
								isContainerFull = false;
								break;
							}

						}
					}

					if (isContainerFull)
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

	/**
	 * This functions returns a double dimensional boolean array which tells the
	 * availablity of storage positions of particular container. True -
	 * Available. False - Not Available.
	 * @param containerId - The container id.
	 * @param dimX - int
	 * @param dimY - int
	 * @return Returns a double dimensional boolean array of position
	 *         availablity.
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean[][] getAvailablePositionsForContainer(String containerId, int dimX, int dimY)
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

	/**
	 * @param positions - boolean array of position.
	 * @param list - list of objects
	 */
	private void setPositions(boolean[][] positions, List list)
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

	/**
	 * This functions returns a double dimensional boolean array which tells the
	 * availablity of storage positions of particular container. True -
	 * Available. False - Not Available.
	 * @param containerId
	 *            The container identifier.
	 * @return Returns a double dimensional boolean array of position
	 *         availablity.
	 * @throws BizLogicException throws BizLogicException
	 */

	public Map getAvailablePositionMapForContainer(String containerId, int aliquotCount,
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

	/**
	 * This functions returns a map of allocated containers vs. their respective
	 * free locations.
	 * @return Returns a map of allocated containers vs. their respective free
	 *         locations.
	 * @throws BizLogicException throws BizLogicException
	 */
	public Map getAllocatedContainerMap() throws BizLogicException
	{
		/*
		 * A code snippet inside the commented block should actually be replaced
		 * by the code to get the allocated containers of specific collection
		 * protocol
		 */
		// List list = retrieve(StorageContainer.class.getName());
		final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER, "name",
				"capacity.oneDimensionCapacity", "capacity.twoDimensionCapacity"};
		final List list = this.retrieve(StorageContainer.class.getName(), selectColumnName);
		final Map containerMap = new TreeMap();
		this.logger.info("===================== list size:" + list.size());
		final Iterator itr = list.iterator();
		while (itr.hasNext())
		{
			final Object containerList[] = (Object[]) itr.next();
			// Logger.out.info("+++++++++++++++++++++++++++:"+container.getName()+"++++++++++:"+container.getId());
			final Map positionMap = this.getAvailablePositionMapForContainer(String
					.valueOf(containerList[0]), 0, containerList[2].toString(), containerList[3]
					.toString());

			if (!positionMap.isEmpty())
			{
				// Logger.out.info("---------"+container.getName()+"------"+container.getId());
				final NameValueBean nvb = new NameValueBean(containerList[1], containerList[0]);
				containerMap.put(nvb, positionMap);

			}
		}

		return containerMap;
	}

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
			String selectedContainerName, SessionDataBean sessionDataBean) throws BizLogicException
	{
		long start = 0;
		long end = 0;
		final TreeMap containerMap = new TreeMap();
		JDBCDAO jdbcdao = null;
		DAO dao = null;
		try
		{

			jdbcdao = this.openJDBCSession();

			start = System.currentTimeMillis();
			final String queryStr = "SELECT t1.IDENTIFIER, t1.NAME FROM CATISSUE_CONTAINER t1 WHERE "
					+ "t1.IDENTIFIER IN (SELECT t4.STORAGE_CONTAINER_ID FROM CATISSUE_ST_CONT_ST_TYPE_REL t4 "
					+ "WHERE t4.STORAGE_TYPE_ID = '"
					+ type_id
					+ "' OR t4.STORAGE_TYPE_ID='1' and t4.STORAGE_CONTAINER_ID not in (select IDENTIFIER from catissue_storage_container where site_id in (select IDENTIFIER from catissue_site s1 where s1.ACTIVITY_STATUS='Closed'))) AND "
					+ "t1.ACTIVITY_STATUS='"
					+ Status.ACTIVITY_STATUS_ACTIVE.toString()
					+ "' order by IDENTIFIER";

			this.logger.debug("Storage Container query......................" + queryStr);
			List list = new ArrayList();

			list = jdbcdao.executeQuery(queryStr);

			end = System.currentTimeMillis();
			System.out.println("Time taken for executing query : " + (end - start));

			Map containerMapFromCache = null;
			final Set<Long> siteIds = new UserBizLogic().getRelatedSiteIds(sessionDataBean
					.getUserId());

			try
			{
				containerMapFromCache = StorageContainerUtil.getContainerMapFromCache();
			}
			catch (final Exception e1)
			{
				this.logger.debug(e1.getMessage(), e1);
				e1.printStackTrace();
			}
			boolean flag = true;
			if (containerMapFromCache != null)
			{
				int i = 1;
				final Iterator itr = list.iterator();
				dao = this.openDAOSession(null);
				while (itr.hasNext())
				{
					final List list1 = (List) itr.next();
					final String Id = (String) list1.get(0);

					final Long siteId = this.getSiteIdForStorageContainerId(Long.valueOf(Id), dao);
					if (!sessionDataBean.isAdmin())
					{
						if (!siteIds.contains(siteId))
						{
							continue;
						}
					}

					final String name = (String) list1.get(1);
					final NameValueBean nvb = new NameValueBean(name, Id, new Long(Id));
					if (selectedContainerName != null && flag)
					{
						if (!name.equalsIgnoreCase(selectedContainerName.trim()))
						{
							continue;
						}
						flag = false;
					}

					try
					{
						final Map positionMap = (TreeMap) containerMapFromCache.get(nvb);

						if (positionMap != null && !positionMap.isEmpty())
						{
							final Map positionMap1 = this.deepCopyMap(positionMap);
							// NameValueBean nvb = new NameValueBean(Name, Id);
							if (i > containersMaxLimit)
							{
								exceedingMaxLimit = "true";
								break;
							}
							else
							{
								containerMap.put(nvb, positionMap1);
							}
							i++;
						}
					}

					catch (final Exception e)
					{
						this.logger.info("Error while getting map from cache");
						e.printStackTrace();
					}

				}
			}

			return containerMap;

		}
		catch (final DAOException ex)
		{
			this.logger.debug(ex.getMessage(), ex);
			throw this.getBizLogicException(ex, ex.getErrorKeyName(), ex.getMsgValues());
		}
		finally
		{
			this.closeJDBCSession(jdbcdao);
			this.closeDAOSession(dao);
		}

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
			int aliquotCount, String exceedingMaxLimit, SessionDataBean sessionData, JDBCDAO jdbcDAO)
			throws BizLogicException, DAOException
	{

		final NameValueBeanRelevanceComparator comparator = new NameValueBeanRelevanceComparator();
		this.logger
				.debug("method : getAllocatedContaienrMapForSpecimen()---getting containers for specimen--------------");
		final TreeMap containerMap = new TreeMap(comparator);
		final List list = this.getRelevantContainerList(cpId, specimenClass, jdbcDAO);
		this.logger.debug("getAllocatedContaienrMapForSpecimen()----- Size of list--------:"
				+ list.size());
		Map containerMapFromCache = null;
		try
		{
			containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
		}
		catch (final Exception e1)
		{
			this.logger.debug(e1.getMessage(), e1);
			e1.printStackTrace();
		}

		if (containerMapFromCache != null)
		{
			final DAO dao = this.openDAOSession(null);
			int i = 1;
			int relevenceCounter = 1;
			final Iterator itr = list.iterator();
			while (itr.hasNext())
			{
				final List list1 = (List) itr.next();
				final String Id = (String) list1.get(1);
				final String Name = (String) list1.get(2);
				this.logger.debug("Id :" + Id + "name:" + Name);
				final NameValueBean nvb = new NameValueBean(Name, Id, new Long(relevenceCounter));
				final Map positionMap = (TreeMap) containerMapFromCache.get(nvb);
				if (positionMap != null && !positionMap.isEmpty())
				{
					final StorageContainer sc = new StorageContainer();
					sc.setId(new Long(Id));
					boolean hasAccess = true;

					hasAccess = this.validateContainerAccess(dao, sc, sessionData, cpId);

					if (!hasAccess)
					{
						continue;
					}

					if (i > containersMaxLimit)
					{
						this.logger.info("CONTAINERS_MAX_LIMIT reached");
						exceedingMaxLimit = new String("true");
						break;
					}
					else
					{
						if (aliquotCount > 0)
						{
							final long count = this.countPositionsInMap(positionMap);
							if (count >= aliquotCount)
							{
								containerMap.put(nvb, positionMap);
							}
						}
						else
						{
							containerMap.put(nvb, positionMap);
						}
					}
					i++;
				}
				relevenceCounter++;
			}
			this.closeDAOSession(dao);
			this.logger.info("getAllocatedContaienrMapForSpecimen()----Size of containerMap:"
					+ containerMap.size());
		}
		this.logger.info("exceedingMaxLimit----------" + exceedingMaxLimit);

		return containerMap;

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
	 * This function gets the list of container in order of there relvance.
	 * @param cpId
	 *            collection protocol Id
	 * @param specimenClass
	 *            class of the specimen
	 * @param jdbcDAO  JDBCDAO object
	 * @return list of containers in order of there relevence.
	 * @throws BizLogicException
	 * @author Vaishali
	 */
	public List getRelevantContainerList(long cpId, String specimenClass, JDBCDAO jdbcDAO)
			throws BizLogicException
	{
		final List list = new ArrayList();
		final String[] queryArray = new String[6];
		// category # 1
		// Gets all container which stores just specified collection protocol
		// and specified specimen class
		final String equalToOne = " = 1 ";
		final String greaterThanOne = " > 1 ";

		final String equalToFour = " = 4 ";
		final String notEqualToFour = " !=4 ";
		final String endQry = " and t1.IDENTIFIER = t6.STORAGE_CONTAINER_ID  and t1.IDENTIFIER = t7.IDENTIFIER"
				+ " group by t6.STORAGE_CONTAINER_ID, t1.NAME " + " order by co asc ";

		final String cpRestrictionCountQuery = "(select count(*) from CATISSUE_ST_CONT_COLL_PROT_REL t4 where t4.STORAGE_CONTAINER_ID = t1.IDENTIFIER)";
		final String specimenClassRestrictionQuery = "(select count(*) from CATISSUE_STOR_CONT_SPEC_CLASS t5 where t5.STORAGE_CONTAINER_ID = t1.IDENTIFIER)";
		// Vijay main query and default restriction query is updated according
		// to bug id#8076
		final String mainQuery = " SELECT count(*) co, t6.STORAGE_CONTAINER_ID, t1.NAME FROM CATISSUE_CONTAINER t1 , CATISSUE_STOR_CONT_SPEC_CLASS t6 , CATISSUE_STORAGE_CONTAINER t7 "
				+ " WHERE t1.IDENTIFIER IN (SELECT t2.STORAGE_CONTAINER_ID"
				+ " FROM CATISSUE_ST_CONT_COLL_PROT_REL t2 WHERE t2.COLLECTION_PROTOCOL_ID = '"
				+ cpId
				+ "')"
				+ " AND t1.ACTIVITY_STATUS='Active'"
				+ " and t1.IDENTIFIER IN (SELECT t3.STORAGE_CONTAINER_ID FROM CATISSUE_STOR_CONT_SPEC_CLASS t3"
				+ " WHERE t3.SPECIMEN_CLASS = '"
				+ specimenClass
				+ "')"
				+ " AND t1.ACTIVITY_STATUS='Active' AND t1.IDENTIFIER=t7.IDENTIFIER	AND t7.SITE_ID NOT IN (SELECT IDENTIFIER FROM CATISSUE_SITE WHERE ACTIVITY_STATUS='Closed')";
		final String defaultRestrictionQuery = " SELECT  count(*) co, t6.STORAGE_CONTAINER_ID, t1.NAME FROM CATISSUE_CONTAINER t1 , CATISSUE_STOR_CONT_SPEC_CLASS t6 , CATISSUE_STORAGE_CONTAINER t7 "
				+ " WHERE t1.IDENTIFIER NOT IN (SELECT t2.STORAGE_CONTAINER_ID FROM CATISSUE_ST_CONT_COLL_PROT_REL t2)"
				+ " and t1.IDENTIFIER IN (SELECT t3.STORAGE_CONTAINER_ID FROM CATISSUE_STOR_CONT_SPEC_CLASS t3"
				+ " WHERE t3.SPECIMEN_CLASS = '"
				+ specimenClass
				+ "') "
				+ " AND t1.ACTIVITY_STATUS='Active' AND t7.SITE_ID NOT IN (SELECT IDENTIFIER FROM CATISSUE_SITE WHERE ACTIVITY_STATUS='Closed')";

		final String queryStr1 = mainQuery + " and " + cpRestrictionCountQuery + equalToOne
				+ " and " + specimenClassRestrictionQuery + equalToOne + endQry;
		// category # 2
		// Gets all containers which holds just specified container and any
		// specimen class
		final String queryStr2 = mainQuery + " and " + cpRestrictionCountQuery + equalToOne
				+ " and " + specimenClassRestrictionQuery + greaterThanOne + endQry;

		// catgory # 3
		// Gets all the containers which holds other than specified collection
		// protocol and only specified specimen class
		final String queryStr3 = mainQuery + " and " + cpRestrictionCountQuery + greaterThanOne
				+ " and " + specimenClassRestrictionQuery + equalToOne + endQry;

		// catgory # 4
		// Gets all the containers which holds specified cp and other than
		// specified collection protocol and specified specimen class and other
		// than specified specimen class
		final String queryStr4 = mainQuery + " and " + cpRestrictionCountQuery + greaterThanOne
				+ " and " + specimenClassRestrictionQuery + greaterThanOne + endQry;

		// catgory # 5
		// Gets all the containers which holds any collection protocol and
		// specified specimen class and other than specified specimen class

		final String queryStr5 = defaultRestrictionQuery + " and " + specimenClassRestrictionQuery
				+ notEqualToFour + endQry;
		// catgory # 6
		// Gets all the containers which holds any collection protocol and any
		// specimen class
		final String queryStr6 = defaultRestrictionQuery + " and " + specimenClassRestrictionQuery
				+ equalToFour + endQry;

		queryArray[0] = queryStr1;
		queryArray[1] = queryStr2;
		queryArray[2] = queryStr3;
		queryArray[3] = queryStr4;
		queryArray[4] = queryStr5;
		queryArray[5] = queryStr6;

		for (int i = 0; i < 6; i++)
		{
			this.logger.debug("Storage Container query......................" + queryArray[i]);
			System.out.println("Storage Container query......................" + queryArray[i]);
			final List queryResultList = this.executeStorageContQuery(queryArray[i], jdbcDAO);
			list.addAll(queryResultList);
		}
		return list;
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
			int noOfAliqoutes, SessionDataBean sessionData, String exceedingMaxLimit)
			throws BizLogicException
	{

		final NameValueBeanValueComparator contComp = new NameValueBeanValueComparator();
		final TreeMap containerMap = new TreeMap(contComp);

		JDBCDAO jdbcDAO = null;
		DAO dao = null;
		try
		{
			jdbcDAO = this.openJDBCSession();

			String includeAllIdQueryStr = " OR t4.SPECIMEN_ARRAY_TYPE_ID = '"
					+ Constants.ARRAY_TYPE_ALL_ID + "'";

			if (!(new Validator().isValidOption(String.valueOf(specimen_array_type_id))))
			{
				includeAllIdQueryStr = "";
			}
			final String queryStr = "select t1.IDENTIFIER,t1.name from CATISSUE_CONTAINER t1,CATISSUE_STORAGE_CONTAINER t2 "
					+ "where t1.IDENTIFIER IN (select t4.STORAGE_CONTAINER_ID from CATISSUE_CONT_HOLDS_SPARRTYPE t4 "
					+ "where t4.SPECIMEN_ARRAY_TYPE_ID = '"
					+ specimen_array_type_id
					+ "'"
					+ includeAllIdQueryStr + ") and t1.IDENTIFIER = t2.IDENTIFIER";

			this.logger.debug("SPECIMEN ARRAY QUERY ......................" + queryStr);
			List list = new ArrayList();

			final Set<Long> siteIds = new UserBizLogic().getRelatedSiteIds(sessionData.getUserId());
			list = jdbcDAO.executeQuery(queryStr);

			this.logger.info("Size of list:" + list.size());
			Map containerMapFromCache = null;
			try
			{
				containerMapFromCache = (TreeMap) StorageContainerUtil.getContainerMapFromCache();
			}
			catch (final Exception e1)
			{
				this.logger.debug(e1.getMessage(), e1);
				e1.printStackTrace();
			}

			if (containerMapFromCache != null)
			{
				int i = 1;
				final Iterator itr = list.iterator();

				dao = this.openDAOSession(null);
				while (itr.hasNext())
				{
					final List list1 = (List) itr.next();
					final String Id = (String) list1.get(0);

					final Long siteId = this.getSiteIdForStorageContainerId(Long.valueOf(Id), dao);
					if (!sessionData.isAdmin())
					{
						if (!siteIds.contains(siteId))
						{
							continue;
						}
					}

					final String Name = (String) list1.get(1);
					final NameValueBean nvb = new NameValueBean(Name, Id);
					final Map positionMap = (TreeMap) containerMapFromCache.get(nvb);
					if (positionMap != null && !positionMap.isEmpty())
					{
						// deep copy is required due to cache updation by reference
						final Map positionMap1 = this.deepCopyMap(positionMap);
						// NameValueBean nvb = new NameValueBean(Name, Id);
						final StorageContainer sc = new StorageContainer();
						sc.setId(new Long(Id));
						/*
						 * boolean hasAccess = true; try { hasAccess =
						 * validateContainerAccess(sc, sessionData); } catch
						 * (SMException sme) { sme.printStackTrace(); throw
						 * handleSMException(sme); } if (!hasAccess) continue;
						 */
						if (i > containersMaxLimit)
						{
							exceedingMaxLimit = "true";
							break;
						}
						else
						{
							containerMap.put(nvb, positionMap1);
						}
						i++;
					}

				}
			}

			return containerMap;

		}
		catch (final DAOException daoExp)
		{
			this.logger.debug(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		finally
		{
			this.closeJDBCSession(jdbcDAO);
			this.closeDAOSession(dao);
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
			final String[] whereColumnName = new String[]{"id"}; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
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

		final Site site = this.getSite(dao, containerId);

		// check for closed Site
		if (site != null)
		{
			if (Status.ACTIVITY_STATUS_CLOSED.toString().equals(site.getActivityStatus()))
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

	/**Gives the Site Object related to given container
	 * @param containerId - Long
	 * @return Site object
	 * @throws BizLogicException throws BizLogicException
	 */
	public Site getRelatedSite(Long containerId) throws BizLogicException
	{
		Site site = null;
		DAO dao = null;
		try
		{
			if (containerId >= 1)
			{
				dao = this.openDAOSession(null);
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
		finally
		{
			this.closeDAOSession(dao);
		}
		return site;
	}

	/**
	 * Gives the Site Object related to given container whose name is given.
	 * @param containerName - String 
	 * @return Site - site object
	 */
	public Site getRelatedSiteForManual(String containerName) throws BizLogicException
	{
		Site site = null;
		DAO dao = null;
		try
		{
			dao = this.openDAOSession(null);
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
		finally
		{
			this.closeDAOSession(dao);
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

}