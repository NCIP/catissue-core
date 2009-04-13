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
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
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
import edu.wustl.dao.HibernateDAO;
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
 * 
 * @author vaishali_khandelwal
 */
public class StorageContainerBizLogic extends CatissueDefaultBizLogic implements
		TreeDataInterface {

	// Getting containersMaxLimit from the xml file in static variable
	private static final int containersMaxLimit = Integer
			.parseInt(XMLPropertyHandler
					.getValue(Constants.CONTAINERS_MAX_LIMIT));

	/**
	 * Saves the storageContainer object in the database.
	 * 
	 * @param obj
	 *            The storageType object to be saved.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws BizLogicException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			StorageContainer container = (StorageContainer) obj;
			container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());

			// Setting the Parent Container if applicable
			int posOneCapacity = 1, posTwoCapacity = 1;
			int positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW, positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
			boolean fullStatus[][] = null;

			int noOfContainers = container.getNoOfContainers().intValue();

			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null) {
				Object object = dao.retrieveById(StorageContainer.class.getName(),
						container.getLocatedAtPosition().getParentContainer()
						.getId());

				if (object != null) {
					StorageContainer parentContainer = (StorageContainer) object;

					// check for closed ParentContainer
					checkStatus(dao, parentContainer, "Parent Container");

					int totalCapacity = parentContainer.getCapacity()
					.getOneDimensionCapacity().intValue()
					* parentContainer.getCapacity()
					.getTwoDimensionCapacity().intValue();
					Collection children = StorageContainerUtil.getChildren(dao,
							parentContainer.getId());
					if ((noOfContainers + children.size()) > totalCapacity) 
					{

						throw getBizLogicException(null, "errors.storageContainer.overflow", "");
					} else {

						// Check if position specified is within the parent
						// container's
						// capacity
						if (false == validatePosition(parentContainer, container)) {
							throw getBizLogicException(null, "errors.storageContainer.dimensionOverflow", "");
						}


						// check for all validations on the storage container.
						checkContainer(dao, container.getLocatedAtPosition()
								.getParentContainer().getId().toString(),
								container.getLocatedAtPosition()
								.getPositionDimensionOne().toString(),
								container.getLocatedAtPosition()
								.getPositionDimensionTwo().toString(),
								sessionDataBean, false,null);



						// check for availability of position
						/*
						 * boolean canUse = isContainerAvailableForPositions(dao,
						 * container);
						 * 
						 * if (!canUse) { throw new
						 * DAOException(ApplicationProperties.getValue("errors.storageContainer.inUse")); }
						 */

						// Check weather parent container is valid container to use
						boolean parentContainerValidToUSe = isParentContainerValidToUSe(
								container, parentContainer);

						if (!parentContainerValidToUSe) {
							throw getBizLogicException(null, "dao.error", 
							"Parent Container is not valid for this container type");
						}
						ContainerPosition cntPos = container.getLocatedAtPosition();

						cntPos.setParentContainer(parentContainer);

						container.setSite(parentContainer.getSite());

						posOneCapacity = parentContainer.getCapacity()
						.getOneDimensionCapacity().intValue();
						posTwoCapacity = parentContainer.getCapacity()
						.getTwoDimensionCapacity().intValue();

						fullStatus = getStorageContainerFullStatus(dao,
								parentContainer, children);
						positionDimensionOne = cntPos.getPositionDimensionOne()
						.intValue();
						positionDimensionTwo = cntPos.getPositionDimensionTwo()
						.intValue();
						container.setLocatedAtPosition(cntPos);

					}
				} else {
					throw getBizLogicException(null, "errors.storageContainerExist", "");
				}
			} else {
				loadSite(dao, container);
			}

			loadStorageType(dao, container);

			for (int i = 0; i < noOfContainers; i++) {
				StorageContainer cont = new StorageContainer(container);
				if (cont.getLocatedAtPosition() != null
						&& cont.getLocatedAtPosition().getParentContainer() != null) {
					ContainerPosition cntPos = cont.getLocatedAtPosition();

					cntPos
					.setPositionDimensionOne(new Integer(
							positionDimensionOne));
					cntPos
					.setPositionDimensionTwo(new Integer(
							positionDimensionTwo));
					cntPos.setOccupiedContainer(cont);
					cont.setLocatedAtPosition(cntPos);
				}

				Logger.out.debug("Collection protocol size:"
						+ container.getCollectionProtocolCollection().size());
				// by falguni
				// Call Storage container label generator if its specified to use
				// automatic label generator
				if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl) {
					LabelGenerator storagecontLblGenerator;
					try {
						storagecontLblGenerator = LabelGeneratorFactory
						.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
						storagecontLblGenerator.setLabel(cont);
						container.setName(cont.getName());
					} catch (NameGeneratorException e) {
						throw getBizLogicException(null, "dao.error", "");

					}
				}
				if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerBarcodeGeneratorAvl) {
					BarcodeGenerator storagecontBarcodeGenerator;
					try {
						storagecontBarcodeGenerator = BarcodeGeneratorFactory
						.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
						// storagecontBarcodeGenerator.setBarcode(cont);
					} catch (NameGeneratorException e) {

						throw getBizLogicException(null, "dao.error", "");
					}
				}
				dao.insert(cont.getCapacity(), true);
				if(cont.isFull()==null)
				{
					cont.setFull(false);
				}
				dao.insert(cont,true);

				// Used for showing the success message after insert and using it
				// for edit.
				container.setId(cont.getId());
				container.setCapacity(cont.getCapacity());

				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getParentContainer() != null) {
					Logger.out.debug("In if: ");
					do {
						if (positionDimensionTwo == posTwoCapacity) {
							if (positionDimensionOne == posOneCapacity)
								positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW;
							else
								positionDimensionOne = (positionDimensionOne + 1)
								% (posOneCapacity + 1);

							positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
						} else {
							positionDimensionTwo = positionDimensionTwo + 1;
						}

						Logger.out.debug("positionDimensionTwo: "
								+ positionDimensionTwo);
						Logger.out.debug("positionDimensionOne: "
								+ positionDimensionOne);
					} while (fullStatus[positionDimensionOne][positionDimensionTwo] != false);
				}
			}

		}catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		} catch (ApplicationException e) {
			throw getBizLogicException(e, "utility.error", "");
		}
	}

	
	/**
	 *  Name : Pathik Sheth  Reviewer Name :Vishvesh Mulay 	 
	 * Description:Retrive only repository sites which are not closed.
	 */
	public List getRepositorySiteList(String sourceObjectName,
			String[] displayNameFields, String valueField,
			String activityStatusArr[], boolean isToExcludeDisabled)
			throws BizLogicException {
		String[] whereColumnName = null;
		String[] whereColumnCondition = null;
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		whereColumnName = new String[] { "activityStatus","type"};
		whereColumnCondition = new String[] { "not in","=" };
		// whereColumnCondition = new String[]{"in"};
		Object[] whereColumnValue = { activityStatusArr,Constants.REPOSITORY};

		return getList(sourceObjectName, displayNameFields, valueField,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition, separatorBetweenFields, isToExcludeDisabled);

	}


	public List getSiteList(String[] displayNameFields, String valueField,
			String activityStatusArr[], Long userId) throws BizLogicException {
		List siteResultList = getRepositorySiteList(Site.class.getName(),
				displayNameFields, valueField, activityStatusArr, false);
		List userList = null;
		Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(userId);
		userList = new ArrayList();
		Iterator siteListIterator = siteResultList.iterator();
		while (siteListIterator.hasNext()) {
			NameValueBean nameValBean = (NameValueBean) siteListIterator
					.next();
			Long siteId = new Long(nameValBean.getValue());
			if (hasPrivilegeonSite(idSet, siteId)) {
				userList.add(nameValBean);
			}
		}
		  
		
		return userList;
	}

	private boolean hasPrivilegeonSite(Set<Long> siteidSet, Long siteId) {
		boolean hasPrivilege = true;
		if (siteidSet != null) {
			if (!siteidSet.contains(siteId)) {
				hasPrivilege = false;
			}
		}
		return hasPrivilege;
	}
	
	
	/**
	 * this function checks weather parent of the container is valid or not
	 * according to restriction provided for the containers
	 * 
	 * @param container -
	 *            Container
	 * @param parent -
	 *            Parent Container
	 * @return boolean true indicating valid to use , false indicating not valid
	 *         to use.
	 * @throws BizLogicException
	 */
	protected boolean isParentContainerValidToUSe(StorageContainer container,
			StorageContainer parent) throws BizLogicException {

		StorageType storageTypeAny = new StorageType();
		storageTypeAny.setId(new Long("1"));
		storageTypeAny.setName("All");
		if (parent.getHoldsStorageTypeCollection().contains(storageTypeAny)) {
			return true;
		}
		if (!parent.getHoldsStorageTypeCollection().contains(
				container.getStorageType())) {
			return false;
		}
		return true;
	}

	// This method sets the collection Storage Types.
	protected String[] getDynamicGroups(AbstractDomainObject obj)
			throws SMException {
		String[] dynamicGroups = null;
		StorageContainer storageContainer = (StorageContainer) obj;

		if (storageContainer.getLocatedAtPosition() != null
				&& storageContainer.getLocatedAtPosition().getParentContainer() != null) {
			dynamicGroups =  SecurityManagerFactory.getSecurityManager()
					.getProtectionGroupByName(
							storageContainer.getLocatedAtPosition()
								.getParentContainer());
		} else {
			dynamicGroups = SecurityManagerFactory.getSecurityManager()
			.getProtectionGroupByName(storageContainer.getSite());
		}
		return dynamicGroups;
	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException {
		StorageContainer container = (StorageContainer) obj;
		try {

			Map containerMap = StorageContainerUtil.getContainerMapFromCache();
			StorageContainerUtil.addStorageContainerInContainerMap(container,
					containerMap);

		} catch (Exception e) {

		}

	}

	/**
	 * Updates the persistent object in the database.
	 * 
	 * @param obj
	 *            The object to be updated.
	 * @param session
	 *            The session in which the object is saved.
	 * @throws BizLogicException
	 */
	protected void update(DAO dao, Object obj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		
		try
		{
			StorageContainer container = (StorageContainer) obj;
			StorageContainer oldContainer = (StorageContainer) oldObj;

			// lazy change
			StorageContainer persistentOldContainerForChange = null;
			Object object = dao.retrieveById(StorageContainer.class.getName(),
					oldContainer.getId());
			persistentOldContainerForChange = (StorageContainer) object;

			// retrive parent container
			if (container.getLocatedAtPosition() != null) {
				StorageContainer parentStorageContainer = (StorageContainer) dao
				.retrieveById(StorageContainer.class.getName(), container
						.getLocatedAtPosition().getParentContainer()
						.getId());
				container.getLocatedAtPosition().setParentContainer(
						parentStorageContainer);
			}

			Logger.out.debug("container.isParentChanged() : "
					+ container.isParentChanged());

			if (container.isParentChanged()) {
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition().getParentContainer() != null) {
					// Check whether continer is moved to one of its sub container.
					if (isUnderSubContainer(container, container
							.getLocatedAtPosition().getParentContainer().getId(),
							dao)) {

						throw getBizLogicException(null, "errors.container.under.subcontainer", "");
					}
					Logger.out.debug("Loading ParentContainer: "
							+ container.getLocatedAtPosition().getParentContainer()
							.getId());

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
					if (false == validatePosition(dao, container)) {

						throw getBizLogicException(null, "errors.storageContainer.dimensionOverflow", "");
					}

					// Mandar : code added for validation bug id 666. 24-11-2005
					// start
					boolean canUse = isContainerAvailableForPositions(dao,
							container);
					Logger.out.debug("canUse : " + canUse);
					if (!canUse) {
						throw getBizLogicException(null, "errors.storageContainer.inUse", "");
					}
					// Mandar : code added for validation bug id 666. 24-11-2005 end

					// check for closed ParentContainer
					checkStatus(dao, container.getLocatedAtPosition()
							.getParentContainer(), "Parent Container");

					// container.setParent(pc);

					Site site = getSite(dao, container.getLocatedAtPosition()
							.getParentContainer().getId());

					// Site
					// site=((StorageContainer)container.getParent()).getSite();
					// check for closed Site
					checkStatus(dao, site, "Parent Container Site");

					container.setSite(site);
					/** -- patch ends here -- */
				}
			}
			// Mandar : code added for validation 25-11-05-----------
			else
				// if parent container is not changed only the position is changed.
			{
				if (container.isPositionChanged()) {
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = { "id",
							"capacity.oneDimensionCapacity",
					"capacity.twoDimensionCapacity" };
					String[] whereColumnName = { "id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
					String[] whereColumnCondition = { "=" };
					Object[] whereColumnValue = { container.getLocatedAtPosition()
							.getParentContainer().getId() };
					String joinCondition = null;

					QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("id",container.getLocatedAtPosition()
							.getParentContainer().getId()));

					List list = dao.retrieve(sourceObjectName, selectColumnName,queryWhereClause);

					if (!list.isEmpty()) {
						Object[] obj1 = (Object[]) list.get(0);
						Logger.out
						.debug("**************PC obj::::::: --------------- "
								+ obj1);
						Logger.out.debug((Long) obj1[0]);
						Logger.out.debug((Integer) obj1[1]);
						Logger.out.debug((Integer) obj1[2]);

						Integer pcCapacityOne = (Integer) obj1[1];
						Integer pcCapacityTwo = (Integer) obj1[2];

						if (!validatePosition(pcCapacityOne.intValue(),
								pcCapacityTwo.intValue(), container)) {
							throw getBizLogicException(null, "errors.storageContainer.dimensionOverflow", "");
						}
					} else {

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
							&& oldContainer.getLocatedAtPosition()
							.getPositionDimensionOne() != null
							&& oldContainer.getLocatedAtPosition()
							.getPositionDimensionOne().intValue() != container
							.getLocatedAtPosition()
							.getPositionDimensionOne().intValue()
							|| oldContainer.getLocatedAtPosition()
							.getPositionDimensionTwo().intValue() != container
							.getLocatedAtPosition()
							.getPositionDimensionTwo().intValue()) {
						boolean canUse = isContainerAvailableForPositions(dao,
								container);
						Logger.out.debug("canUse : " + canUse);
						if (!canUse) {

							throw getBizLogicException(null, "errors.storageContainer.inUse", "");
						}
					}

				}
			}

			// Mandar : --------- end 25-11-05 -----------------

			boolean flag = true;

			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null
					&& oldContainer.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer()
					.getId().longValue() == oldContainer
					.getLocatedAtPosition().getParentContainer().getId()
					.longValue()
					&& container.getLocatedAtPosition().getPositionDimensionOne()
					.longValue() == oldContainer.getLocatedAtPosition()
					.getPositionDimensionOne().longValue()
					&& container.getLocatedAtPosition().getPositionDimensionTwo()
					.longValue() == oldContainer.getLocatedAtPosition()
					.getPositionDimensionTwo().longValue()) {
				flag = false;
			}

			if (flag) {

				// check for all validations on the storage container.
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition()
						.getParentContainer() != null) {
					checkContainer(dao, container.getLocatedAtPosition()
							.getParentContainer().getId().toString(), container
							.getLocatedAtPosition().getPositionDimensionOne()
							.toString(), container.getLocatedAtPosition()
							.getPositionDimensionTwo().toString(),
							sessionDataBean, false,null);
				}

			}

			// Check whether size has been reduced
			// Sri: fix for bug #355 (Storage capacity: Reducing capacity should be
			// handled)
			Integer oldContainerDimOne = oldContainer.getCapacity()
			.getOneDimensionCapacity();
			Integer oldContainerDimTwo = oldContainer.getCapacity()
			.getTwoDimensionCapacity();
			Integer newContainerDimOne = container.getCapacity()
			.getOneDimensionCapacity();
			Integer newContainerDimTwo = container.getCapacity()
			.getTwoDimensionCapacity();

			// If any size is reduced, object was present at any of the deleted
			// positions throw error
			if (oldContainerDimOne.intValue() > newContainerDimOne.intValue()
					|| oldContainerDimTwo.intValue() > newContainerDimTwo
					.intValue()) {
				boolean canReduceDimension = StorageContainerUtil
				.checkCanReduceDimension(oldContainer, container);
				if (!canReduceDimension) {
					throw getBizLogicException(null, "errors.storageContainer.cannotReduce", "");

				}
			}

			/**
			 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
			 * Description:Storage container will not be added to closed site :check
			 * for closed site
			 */
			if (container.getId() != null) {
				checkClosedSite(dao, container.getId(), "Container site");
			}
			setSiteForSubContainers(container, container.getSite(), dao);

			boolean restrictionsCanChange = isContainerEmpty(dao, container);
			Logger.out.info("--------------container Available :"
					+ restrictionsCanChange);
			if (!restrictionsCanChange) {

				boolean restrictionsChanged = checkForRestrictionsChanged(
						container, oldContainer);
				Logger.out.info("---------------restriction changed -:"
						+ restrictionsChanged);
				if (restrictionsChanged) {

					throw getBizLogicException(null, "errros.storageContainer.restrictionCannotChanged", "");
				}

			}
			Collection<SpecimenPosition> specimenPosColl = getSpecimenPositionCollForContainer(
					dao, container.getId());
			container.setSpecimenPositionCollection(specimenPosColl);
			setValuesinPersistentObject(persistentOldContainerForChange, container,
					dao);

			dao.update(persistentOldContainerForChange);
			dao.update(persistentOldContainerForChange.getCapacity());
			// Audit of update of storage container.
			((HibernateDAO)dao)	.audit(obj, oldObj);
			((HibernateDAO)dao)	.audit(container.getCapacity(), oldContainer.getCapacity());

			Logger.out.debug("container.getActivityStatus() "
					+ container.getActivityStatus());
			// lazy change
			/*
			 * if (container.getParent() != null) {
			 * 
			 * StorageContainer pc = (StorageContainer)
			 * dao.retrieve(StorageContainer.class.getName(),
			 * container.getParent().getId()); container.setParent(pc); }
			 */
			if (container.getActivityStatus().equals(
					Status.ACTIVITY_STATUS_DISABLED)) {
				Long containerIDArr[] = { container.getId() };
				if (isContainerAvailableForDisabled(dao, containerIDArr)) {
					List disabledConts = new ArrayList();

					/**
					 * Preapare list of parent/child containers to disable
					 * 
					 */
					List<StorageContainer> disabledContainerList = new ArrayList<StorageContainer>();
					disabledContainerList.add(persistentOldContainerForChange);
					//persistentOldContainerForChange.setLocatedAtPosition(null);

					addEntriesInDisabledMap(persistentOldContainerForChange,
							disabledConts);
					// disabledConts.add(new StorageContainer(container));
					setDisableToSubContainer(persistentOldContainerForChange,
							disabledConts, dao, disabledContainerList);

					persistentOldContainerForChange.getOccupiedPositions().clear();

					Logger.out.debug("container.getActivityStatus() "
							+ container.getActivityStatus());

					disableSubStorageContainer(dao, sessionDataBean,
							disabledContainerList);
					ContainerPosition prevPosition = persistentOldContainerForChange.getLocatedAtPosition(); 
					persistentOldContainerForChange.setLocatedAtPosition(null);

					dao.update(persistentOldContainerForChange);

					if(prevPosition!=null)
					{
						dao.delete(prevPosition);
					}	

					try {
						CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
						.getInstance();
						catissueCoreCacheManager.addObjectToCache(
								Constants.MAP_OF_DISABLED_CONTAINERS,
								(Serializable) disabledConts);
					} catch (CacheException e) {

					}

				} else {
					throw getBizLogicException(null, "errors.container.contains.specimen", "");
				}
			}

		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}


	}

	public void setValuesinPersistentObject(StorageContainer persistentobject,
			StorageContainer newObject, DAO dao) throws BizLogicException
	{
		try
		{
		persistentobject.setActivityStatus(newObject.getActivityStatus());
		persistentobject.setBarcode(newObject.getBarcode());
		Capacity persistCapacity = persistentobject.getCapacity();
		Capacity newCapacity = newObject.getCapacity();
		persistCapacity.setOneDimensionCapacity(newCapacity
				.getOneDimensionCapacity());
		persistCapacity.setTwoDimensionCapacity(newCapacity
				.getTwoDimensionCapacity());
		Collection children = StorageContainerUtil.getChildren(dao, newObject
				.getId());
		StorageContainerUtil.setChildren(children, dao, persistentobject
				.getId());
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
		if (newObject.getLocatedAtPosition() != null) {
			ContainerPosition cntPos = persistentobject.getLocatedAtPosition();
			if (cntPos == null) {
				cntPos = new ContainerPosition();
				persistentobject.setLocatedAtPosition(cntPos);
			}
			cntPos.setPositionDimensionOne(newObject.getLocatedAtPosition()
					.getPositionDimensionOne());
			cntPos.setPositionDimensionTwo(newObject.getLocatedAtPosition()
					.getPositionDimensionTwo());
			cntPos.setParentContainer(newObject.getLocatedAtPosition()
					.getParentContainer());
			cntPos.setOccupiedContainer(persistentobject);
			// persistentobject.setLocatedAtPosition(cntPos);
		}
		persistentobject.setSimilarContainerMap(newObject
				.getSimilarContainerMap());
		persistentobject.setSite(newObject.getSite());
		if (newObject.getSpecimenPositionCollection() != null) {
			Collection<SpecimenPosition> specPosColl = persistentobject
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
		persistentobject.setTempratureInCentigrade(newObject
				.getTempratureInCentigrade());
		}
		catch(ApplicationException exp)
		{
			throw getBizLogicException(exp, "utility.error", "");
		}
	}

	private void addEntriesInDisabledMap(StorageContainer container,
			List disabledConts) {
		String contNameKey = "StorageContName";
		String contIdKey = "StorageContIdKey";
		String parentContNameKey = "ParentContName";
		String parentContIdKey = "ParentContId";
		String pos1Key = "pos1";
		String pos2Key = "pos2";
		Map containerDetails = new TreeMap();
		containerDetails.put(contNameKey, container.getName());
		containerDetails.put(contIdKey, container.getId());
		if (container != null
				&& container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null) {
			containerDetails.put(parentContNameKey, container
					.getLocatedAtPosition().getParentContainer().getName());
			containerDetails.put(parentContIdKey, container
					.getLocatedAtPosition().getParentContainer().getId());
			containerDetails.put(pos1Key, container.getLocatedAtPosition()
					.getPositionDimensionOne());
			containerDetails.put(pos2Key, container.getLocatedAtPosition()
					.getPositionDimensionTwo());
		}

		disabledConts.add(containerDetails);

	}

	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
			{
		try {
			Map containerMap = StorageContainerUtil.getContainerMapFromCache();
			StorageContainer currentContainer = (StorageContainer) currentObj;
			StorageContainer oldContainer = (StorageContainer) oldObj;

			// if name gets change then update the cache with new key
			if (!currentContainer.getName().equals(oldContainer.getName())) {
				StorageContainerUtil.updateNameInCache(containerMap,
						currentContainer, oldContainer);
			}

			// If capacity of container gets increased then insert all the new
			// positions in map ..........
			int xOld = oldContainer.getCapacity().getOneDimensionCapacity()
					.intValue();
			int xNew = currentContainer.getCapacity().getOneDimensionCapacity()
					.intValue();
			int yOld = oldContainer.getCapacity().getTwoDimensionCapacity()
					.intValue();
			int yNew = currentContainer.getCapacity().getTwoDimensionCapacity()
					.intValue();
			if (xNew != xOld || yNew != yOld) {
				StorageContainerUtil.updateStoragePositions(containerMap,
						currentContainer, oldContainer);

			}
			// finish
			if (oldContainer != null
					&& oldContainer.getLocatedAtPosition() != null
					&& oldContainer.getLocatedAtPosition().getParentContainer() != null) {
				StorageContainer oldParentCont = (StorageContainer) HibernateMetaData
						.getProxyObjectImpl(oldContainer.getLocatedAtPosition()
								.getParentContainer());
				StorageContainerUtil.insertSinglePositionInContainerMap(
						oldParentCont, containerMap, oldContainer
								.getLocatedAtPosition()
								.getPositionDimensionOne().intValue(),
						oldContainer.getLocatedAtPosition()
								.getPositionDimensionTwo().intValue());
			}
			if (currentContainer != null
					&& currentContainer.getLocatedAtPosition() != null
					&& currentContainer.getLocatedAtPosition()
							.getParentContainer() != null) {
				StorageContainer currentParentCont = (StorageContainer) currentContainer
						.getLocatedAtPosition().getParentContainer();
				StorageContainerUtil.deleteSinglePositionInContainerMap(
						currentParentCont, containerMap, currentContainer
								.getLocatedAtPosition()
								.getPositionDimensionOne().intValue(),
						currentContainer.getLocatedAtPosition()
								.getPositionDimensionTwo().intValue());
			}

			if (currentContainer.getActivityStatus().equals(
					Status.ACTIVITY_STATUS_DISABLED)) {
				List disabledConts = StorageContainerUtil
						.getListOfDisabledContainersFromCache();
				List disabledContsAfterReverse = new ArrayList();
				for (int i = disabledConts.size() - 1; i >= 0; i--) {
					disabledContsAfterReverse.add(disabledConts.get(i));
				}

				Iterator itr = disabledContsAfterReverse.iterator();
				while (itr.hasNext()) {

					Map disabledContDetails = (TreeMap) itr.next();
					String contNameKey = "StorageContName";
					String contIdKey = "StorageContIdKey";
					String parentContNameKey = "ParentContName";
					String parentContIdKey = "ParentContId";
					String pos1Key = "pos1";
					String pos2Key = "pos2";

					StorageContainer cont = new StorageContainer();
					cont.setId((Long) disabledContDetails.get(contIdKey));
					cont.setName((String) disabledContDetails.get(contNameKey));

					if (disabledContDetails.get(parentContIdKey) != null) {
						StorageContainer parent = new StorageContainer();
						parent.setName((String) disabledContDetails
								.get(parentContNameKey));
						parent.setId((Long) disabledContDetails
								.get(parentContIdKey));
						// cont.setParent(parent);

						ContainerPosition cntPos = new ContainerPosition();

						cntPos
								.setPositionDimensionOne((Integer) disabledContDetails
										.get(pos1Key));
						cntPos
								.setPositionDimensionTwo((Integer) disabledContDetails
										.get(pos2Key));
						cntPos.setParentContainer(parent);
						cntPos.setOccupiedContainer(cont);
						cont.setLocatedAtPosition(cntPos);
					}

					StorageContainerUtil.removeStorageContainerInContainerMap(
							cont, containerMap);
				}

			}

		} catch (Exception e) {
			Logger.out.error(e.getMessage(), e);
			throw getBizLogicException(e, "dao.error", "");
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

	private boolean checkForRestrictionsChanged(StorageContainer newContainer,
			StorageContainer oldContainer) {
		int flag = 0;
		Collection cpCollNew = newContainer.getCollectionProtocolCollection();
		Collection cpCollOld = oldContainer.getCollectionProtocolCollection();

		Collection storTypeCollNew = newContainer
				.getHoldsStorageTypeCollection();
		Collection storTypeCollOld = oldContainer
				.getHoldsStorageTypeCollection();

		Collection spClassCollNew = newContainer
				.getHoldsSpecimenClassCollection();
		Collection spClassCollOld = oldContainer
				.getHoldsSpecimenClassCollection();

		Collection spArrayTypeCollNew = newContainer
				.getHoldsSpecimenArrayTypeCollection();
		Collection spArrayTypeCollOld = oldContainer
				.getHoldsSpecimenArrayTypeCollection();

		/*
		 * if (cpCollNew.size() != cpCollOld.size()) return true;
		 */

		/**
		 * Bug 3612 - User should be able to change the restrictions if he
		 * specifies the superset of the old restrictions if container is not
		 * empty.
		 */
		Iterator itrOld = cpCollOld.iterator();
		while (itrOld.hasNext()) {
			flag = 0;
			CollectionProtocol cpOld = (CollectionProtocol) itrOld.next();
			Iterator itrNew = cpCollNew.iterator();
			if (cpCollNew.size() == 0) {
				break;
			}
			while (itrNew.hasNext()) {
				CollectionProtocol cpNew = (CollectionProtocol) itrNew.next();
				if (cpOld.getId().longValue() == cpNew.getId().longValue()) {
					flag = 1;
					break;
				}
			}
			if (flag != 1)
				return true;
		}

		/*
		 * if (storTypeCollNew.size() != storTypeCollOld.size()) return true;
		 */

		itrOld = storTypeCollOld.iterator();
		while (itrOld.hasNext()) {
			flag = 0;
			StorageType storOld = (StorageType) itrOld.next();
			Iterator itrNew = storTypeCollNew.iterator();
			while (itrNew.hasNext()) {
				StorageType storNew = (StorageType) itrNew.next();
				if (storNew.getId().longValue() == storOld.getId().longValue()
						|| storNew.getId().longValue() == 1) {
					flag = 1;
					break;
				}
			}
			if (flag != 1)
				return true;

		}

		/*
		 * if (spClassCollNew.size() != spClassCollOld.size()) return true;
		 */

		itrOld = spClassCollOld.iterator();
		while (itrOld.hasNext()) {
			flag = 0;
			String specimenOld = (String) itrOld.next();
			Iterator itrNew = spClassCollNew.iterator();
			while (itrNew.hasNext()) {
				String specimenNew = (String) itrNew.next();
				if (specimenNew.equals(specimenOld)) {
					flag = 1;
					break;
				}
			}
			if (flag != 1)
				return true;
		}

		/*
		 * if (spArrayTypeCollNew.size() != spArrayTypeCollOld.size()) return
		 * true;
		 */

		itrOld = spArrayTypeCollOld.iterator();
		while (itrOld.hasNext()) {
			flag = 0;
			SpecimenArrayType spArrayTypeOld = (SpecimenArrayType) itrOld
					.next();

			Iterator itrNew = spArrayTypeCollNew.iterator();
			while (itrNew.hasNext()) {
				SpecimenArrayType spArrayTypeNew = (SpecimenArrayType) itrNew
						.next();

				if (spArrayTypeNew.getId().longValue() == spArrayTypeOld
						.getId().longValue()
						|| spArrayTypeNew.getId().longValue() == 1) {
					flag = 1;
					break;
				}
			}
			if (flag != 1)
				return true;
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
	 * 
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
	 * @throws Exception
	 */
	private void isDeAssignable(DAO dao, String privilegeName,
			Long[] objectIds, Long userId, String roleId, boolean assignToUser)
			throws BizLogicException 
	{
		try
		{
			// Aarti: Bug#2364 - Error while assigning privileges since attribute
			// parentContainer changed to parent
			String[] selectColumnNames = { "locatedAtPosition.parentContainer.id",
			"site.id" };
			String[] whereColumnNames = { "id" };
			List listOfSubElement = super.getRelatedObjects(dao,
					StorageContainer.class, selectColumnNames, whereColumnNames,
					objectIds);

			Logger.out.debug("Related Objects>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
					+ listOfSubElement.size());

			String userName = new String();
			if (assignToUser == true) {
				userName =SecurityManagerFactory.getSecurityManager().getUserById(
						userId.toString()).getLoginName();
			}

			// To get privilegeCache through
			// Singleton instance of PrivilegeManager, requires User LoginName
			PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			PrivilegeCache privilegeCache = privilegeManager
			.getPrivilegeCache(userName);

			Iterator iterator = listOfSubElement.iterator();
			while (iterator.hasNext()) {
				Object[] row = (Object[]) iterator.next();

				// Parent storage container identifier.
				Object containerObject = (Object) row[0];
				String className = StorageContainer.class.getName();

				// Parent storage container identifier is null, the parent is a
				// site..
				if ((row[0] == null) || (row[0].equals(""))) {
					containerObject = row[1];
					className = Site.class.getName();
				}

				Logger.out.debug("Container Object After ********************** : "
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
				} else
					// If the privilege is assigned/deassigned to a user group.
				{
					permission = privilegeManager.hasGroupPrivilege(roleId,
							className + "_" + containerObject.toString(),
							privilegeName);
					// permission =
					// SecurityManager.getInstance(StorageContainerBizLogic.class).checkPermission(roleId,
					// className,
					// containerObject.toString());
				}

				// If the parent is a Site.
				if (permission == true && row[0] == null) {

					throw getBizLogicException(null, "dao.error", 
							"Error : First de-assign privilege of the Parent Site with system identifier "
							+ row[1].toString());
				} else if (permission == true && row[0] != null)// If the parent is
					// a storage
					// container.
				{
					throw getBizLogicException(null, "dao.error", 
							"Error : First de-assign privilege of the Parent Container with system identifier "
							+ row[0].toString());
				}
			}
		}
		catch(SMException exp)
		{
			throw AppUtility.handleSMException(exp);
		}
	}

	/**
	 * Assigns the privilege to all the sub-containers down the hierarchy.
	 * 
	 * @param dao
	 *            The dao object to get the related objects down the hierarchy.
	 * @param privilegeName
	 *            The privilege name.
	 * @param storageContainerIDArr
	 *            The storage container id array.
	 * @param userId
	 *            The user identifier.
	 * @param roleId
	 *            The roleId in case privilege is assigned/deassigned to a role.
	 * @param assignToUser
	 *            boolean which determines whether privilege is
	 *            assigned/deassigned to a user or role.
	 * @param assignOperation
	 *            boolean which determines assign/deassign.
	 * @throws SMException
	 * @throws BizLogicException
	 */
	/*private void assignPrivilegeToSubStorageContainer(DAO dao,
			String privilegeName, Long[] storageContainerIDArr, Long userId,
			String roleId, boolean assignToUser, boolean assignOperation)
			throws SMException, DAOException {
		// Aarti: Bug#2364 - Error while assigning privileges since attribute
		// parentContainer changed to parent
		// Get list of sub container identifiers.
		List listOfSubStorageContainerId = super.getRelatedObjects(dao,
				StorageContainer.class, "locatedAtPosition.parentContainer",
				storageContainerIDArr);

		if (listOfSubStorageContainerId.isEmpty())
			return;

		super.setPrivilege(dao, privilegeName, StorageContainer.class, Utility
				.toLongArray(listOfSubStorageContainerId), userId, roleId,
				assignToUser, assignOperation);

		assignPrivilegeToSubStorageContainer(dao, privilegeName, Utility
				.toLongArray(listOfSubStorageContainerId), userId, roleId,
				assignToUser, assignOperation);
	}
*/
	/**
	 * @param dao
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @throws BizLogicException
	 * @throws SMException
	 *//*
	public void assignPrivilegeToRelatedObjectsForSite(DAO dao,
			String privilegeName, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation) throws SMException,
			DAOException {
		List listOfSubElement = super.getRelatedObjects(dao,
				StorageContainer.class, "site", objectIds);

		if (!listOfSubElement.isEmpty()) {
			super.setPrivilege(dao, privilegeName, StorageContainer.class,
					Utility.toLongArray(listOfSubElement), userId, roleId,
					assignToUser, assignOperation);
		}
	}*/

	// This method sets the Storage Type & Site (if applicable) of this
	// container.
	protected void loadSite(DAO dao, StorageContainer container)
	throws BizLogicException {

		try
		{
			Site site = container.getSite();
			// Setting the site if applicable
			if (site != null) {
				// Commenting dao.retrive() call as retrived object is not realy
				// required for further processing -Prafull
				Site siteObj = (Site) dao.retrieveById(Site.class.getName(), container
						.getSite().getId());

				if (siteObj != null) {

					// check for closed site
					checkStatus(dao, siteObj, "Site");

					container.setSite(siteObj);
					setSiteForSubContainers(container, siteObj, dao);
				}
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	protected void loadStorageType(DAO dao, StorageContainer container)
			throws BizLogicException
	{
		
		// Setting the Storage Type
		try
		{
			Object storageTypeObj = dao.retrieveById(StorageType.class.getName(),
					container.getStorageType().getId());
			if (storageTypeObj != null) {
				StorageType type = (StorageType) storageTypeObj;
				container.setStorageType(type);
			}

		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private void setSiteForSubContainers(StorageContainer storageContainer,
			Site site, DAO dao) throws BizLogicException {
		// Added storageContainer.getId()!=null check as this method fails in
		// case when it gets called from insert(). -PRafull

		try
		{
			if (storageContainer != null && storageContainer.getId() != null) {
				// Collection children = (Collection)
				// dao.retrieveAttribute(storageContainer.getClass().getName(),
				// storageContainer.getId(), "elements(children)");

				Collection children = StorageContainerUtil.getChildren(dao,
						storageContainer.getId());
				Logger.out
				.debug("storageContainer.getChildrenContainerCollection() "
						+ children.size());

				Iterator iterator = children.iterator();
				while (iterator.hasNext()) {
					StorageContainer container = (StorageContainer) HibernateMetaData
					.getProxyObjectImpl(iterator.next());
					container.setSite(site);
					setSiteForSubContainers(container, site, dao);
				}
			}
		}
		catch(ApplicationException exp)
		{
			throw getBizLogicException(exp, "utility.error", "");
		}

	}

	private boolean isUnderSubContainer(StorageContainer storageContainer,
			Long parentContainerID, DAO dao) throws BizLogicException {

		try
		{
			if (storageContainer != null) {
				// Ashish - 11/6/07 - Retriving children containers for performance
				// improvement.
				// Collection childrenColl =
				// (Collection)dao.retrieveAttribute(StorageContainer.class.getName(),
				// storageContainer.getId(),Constants.COLUMN_NAME_CHILDREN );

				Collection childrenColl = StorageContainerUtil.getChildren(dao,
						storageContainer.getId());
				Iterator iterator = childrenColl.iterator();
				// storageContainer.getChildren()
				while (iterator.hasNext()) {
					StorageContainer container = (StorageContainer) iterator.next();
					// Logger.out.debug("SUB CONTINER container
					// "+parentContainerID.longValue()+"
					// "+container.getId().longValue()+"
					// "+(parentContainerID.longValue()==container.getId().longValue()));
					if (parentContainerID.longValue() == container.getId()
							.longValue())
						return true;
					if (isUnderSubContainer(container, parentContainerID, dao))
						return true;
				}
			}
		}
		catch(ApplicationException exp)
		{
			throw getBizLogicException(exp, "utility.error", "");
		}


		return false;
	}

	// TODO TO BE REMOVED
	private void setDisableToSubContainer(StorageContainer storageContainer,
			List disabledConts, DAO dao, List disabledContainerList)
	throws BizLogicException {

		try
		{
			if (storageContainer != null) {
				// Ashish - 11/6/07 - Retriving children containers for performance
				// improvement.
				// Collection childrenColl =
				// (Collection)dao.retrieveAttribute(StorageContainer.class.getName(),
				// storageContainer.getId(),Constants.COLUMN_NAME_CHILDREN );

				Collection childrenColl = StorageContainerUtil.getChildren(dao,
						storageContainer.getId());

				Iterator iterator = childrenColl.iterator();
				while (iterator.hasNext()) {
					StorageContainer container = (StorageContainer) iterator.next();

					container.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
					addEntriesInDisabledMap(container, disabledConts);
					/* whenever container is disabled free it's used positions */

					container.setLocatedAtPosition(null);
					disabledContainerList.add(container);
					setDisableToSubContainer(container, disabledConts, dao,
							disabledContainerList);
				}
			}
			storageContainer.getOccupiedPositions().clear();

		}
		catch(ApplicationException exp)
		{
			throw getBizLogicException(exp, "utility.error", "");
		}

	}

	// This method is called from labelgenerator.
	public long getNextContainerNumber() throws BizLogicException 
	{
		DAO dao = null;
		try
		{
			String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
			String[] selectColumnName = { "max(IDENTIFIER) as MAX_NAME" };
			dao = openDAOSession(null);

			List list = dao.retrieve(sourceObjectName, selectColumnName);

			if (!list.isEmpty()) {
				List columnList = (List) list.get(0);
				if (!columnList.isEmpty()) {
					String str = (String) columnList.get(0);
					if (!str.equals("")) {
						long no = Long.parseLong(str);
						return no + 1;
					}
				}
			}

			return 1;
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		finally
		{
			closeDAOSession(dao);
		}
		
	}

	// what to do abt thi
	public String getContainerName(String siteName, String typeName,
			String operation, long Id) throws BizLogicException {
		String containerName = "";
		if (typeName != null && siteName != null && !typeName.equals("")
				&& !siteName.equals("")) {
			// Poornima:Max length of site name is 50 and Max length of
			// container type name is 100, in Oracle the name does not truncate
			// and it is giving error. So these fields are truncated in case it
			// is longer than 40.
			// It also solves Bug 2829:System fails to create a default unique
			// storage container name
			String maxSiteName = siteName;
			String maxTypeName = typeName;
			if (siteName.length() > 40) {
				maxSiteName = siteName.substring(0, 39);
			}
			if (typeName.length() > 40) {
				maxTypeName = typeName.substring(0, 39);
			}

			if (operation.equals(Constants.ADD)) {
				containerName = maxSiteName + "_" + maxTypeName + "_"
						+ String.valueOf(getNextContainerNumber());
			} else {
				containerName = maxSiteName + "_" + maxTypeName + "_"
						+ String.valueOf(Id);
			}

		}
		return containerName;
	}

	public int getNextContainerNumber(long parentID, long typeID,
			boolean isInSite) throws BizLogicException 
	{
		
		try
		{
			String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);

			String[] selectColumnName = { "max(IDENTIFIER) as MAX_NAME" };
			/*	String[] whereColumnName = { "STORAGE_TYPE_ID", "PARENT_CONTAINER_ID" };
				String[] whereColumnCondition = { "=", "=" };
				Object[] whereColumnValue = { Long.valueOf(typeID),
				Long.valueOf(parentID) };
			 */

			queryWhereClause.addCondition(new EqualClause("STORAGE_TYPE_ID",Long.valueOf(typeID))).andOpr().
			addCondition(new EqualClause("PARENT_CONTAINER_ID",Long.valueOf(parentID)));


			if (isInSite) {
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

				queryWhereClause.addCondition(new EqualClause("STORAGE_TYPE_ID",Long.valueOf(typeID))).andOpr().
				addCondition(new EqualClause("SITE_ID",Long.valueOf(parentID))).andOpr().
				addCondition(new NullClause("PARENT_CONTAINER_ID"));
			}
			String joinCondition = Constants.AND_JOIN_CONDITION;

			JDBCDAO jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).
			getJDBCDAO();


			jdbcDAO.openSession(null);

			List list = jdbcDAO.retrieve(sourceObjectName, selectColumnName,queryWhereClause);

			jdbcDAO.closeSession();

			if (!list.isEmpty()) {
				List columnList = (List) list.get(0);
				if (!columnList.isEmpty()) {
					String str = (String) columnList.get(0);
					Logger.out.info("str---------------:" + str);
					if (!str.equals("")) {
						int no = Integer.parseInt(str);
						return no + 1;
					}
				}
			}

			return 1;
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private boolean isContainerEmpty(DAO dao, StorageContainer container)
			throws BizLogicException {

		try
		{
			// Retrieving all the occupied positions by child containers
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = { "locatedAtPosition.positionDimensionOne",
			"locatedAtPosition.positionDimensionTwo" };
			String[] whereColumnName = { "locatedAtPosition.parentContainer.id" };
		

			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("locatedAtPosition.parentContainer.id",container.getId()));



			List list = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);

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

				QueryWhereClause queryWhereClausenew = new QueryWhereClause(sourceObjectName);
				queryWhereClausenew.addCondition(new EqualClause("specimenPosition.storageContainer.id",container.getId()));

				list = dao.retrieve(sourceObjectName, selectColumnName,
						queryWhereClausenew);

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

					QueryWhereClause queryWhereClauseinner = new QueryWhereClause(sourceObjectName);
					queryWhereClauseinner.addCondition(new EqualClause("locatedAtPosition.parentContainer.id",container.getId()));
					list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClauseinner);

					if (!list.isEmpty()) 
					{
						return false;
					}

				}

			}

			return true;

		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}

	}

	/**
	 * Returns the data for generation of storage container tree view.
	 * 
	 * @return the vector of tree nodes for the storage containers.
	 */
	public Vector getTreeViewData() {

		JDBCDAO dao = null;
		List list = null;
		try
		{
			 dao = openJDBCSession();
			// String queryStr = " SELECT t8.IDENTIFIER, t8.CONTAINER_NAME, t5.TYPE,
			// t8.SITE_ID, "
			// + " t4.TYPE, t8.PARENT_IDENTIFIER, "
			// + " t8.PARENT_CONTAINER_NAME, t8.PARENT_CONTAINER_TYPE "
			// + " FROM (SELECT t7.IDENTIFIER, t7.CONTAINER_NAME, t7.SITE_ID, "
			// + " t7.STORAGE_TYPE_ID, t7.PARENT_IDENTIFIER, "
			// + " t7.PARENT_CONTAINER_NAME, t6.TYPE AS PARENT_CONTAINER_TYPE FROM "
			// + " (select t1.IDENTIFIER AS IDENTIFIER, t1.CONTAINER_NAME AS
			// CONTAINER_NAME, "
			// + " t1.SITE_ID AS SITE_ID, t1.STORAGE_TYPE_ID AS STORAGE_TYPE_ID, "
			// + " t2.IDENTIFIER AS PARENT_IDENTIFIER, t2.CONTAINER_NAME AS
			// PARENT_CONTAINER_NAME, "
			// + " t2.STORAGE_TYPE_ID AS PARENT_STORAGE_TYPE_ID "
			// + " from CATISSUE_STORAGE_CONTAINER t1 LEFT OUTER JOIN
			// CATISSUE_STORAGE_CONTAINER t2 "
			// + " on t1.PARENT_CONTAINER_ID = t2.IDENTIFIER) AS t7 LEFT OUTER JOIN
			// CATISSUE_STORAGE_TYPE t6 "
			// + " on t7.PARENT_STORAGE_TYPE_ID = t6.IDENTIFIER) AS t8, "
			// + " CATISSUE_SITE t4, CATISSUE_STORAGE_TYPE t5 "
			// + " WHERE t8.SITE_ID = t4.IDENTIFIER " + " AND t8.STORAGE_TYPE_ID =
			// t5.IDENTIFIER ";

			// String queryStr = "SELECT " + " t8.IDENTIFIER, t8.CONTAINER_NAME,
			// t5.NAME, t8.SITE_ID, t4.TYPE, t8.PARENT_IDENTIFIER, "
			// + " t8.PARENT_CONTAINER_NAME, t8.PARENT_CONTAINER_TYPE,
			// t8.ACTIVITY_STATUS, t8.PARENT_ACTIVITY_STATUS " + " FROM ( " + "
			// SELECT "
			// + " t7.IDENTIFIER, t7.CONTAINER_NAME, t7.SITE_ID, t7.STORAGE_TYPE_ID,
			// t7.ACTIVITY_STATUS, t7.PARENT_IDENTIFIER, "
			// + " t7.PARENT_CONTAINER_NAME, t6.NAME AS PARENT_CONTAINER_TYPE,
			// t7.PARENT_ACTIVITY_STATUS " + " FROM " + " ( "
			// + " select "
			// + " t1.IDENTIFIER AS IDENTIFIER, t1.NAME AS CONTAINER_NAME,
			// t11.SITE_ID AS SITE_ID, T1.ACTIVITY_STATUS AS ACTIVITY_STATUS,"
			// + " t11.STORAGE_TYPE_ID AS STORAGE_TYPE_ID, t2.IDENTIFIER AS
			// PARENT_IDENTIFIER, "
			// + " t2.NAME AS PARENT_CONTAINER_NAME, t22.STORAGE_TYPE_ID AS
			// PARENT_STORAGE_TYPE_ID, T2.ACTIVITY_STATUS AS PARENT_ACTIVITY_STATUS"
			// + " from " + " CATISSUE_STORAGE_CONTAINER t11,
			// CATISSUE_STORAGE_CONTAINER t22, "
			// + " CATISSUE_CONTAINER t1 LEFT OUTER JOIN CATISSUE_CONTAINER t2 " + "
			// on t1.PARENT_CONTAINER_ID = t2.IDENTIFIER "
			// + " where " + " t1.identifier = t11.identifier and (t2.identifier is
			// null OR t2.identifier = t22.identifier)" + " ) "
			// + " t7 LEFT OUTER JOIN CATISSUE_CONTAINER_TYPE t6 on " + "
			// t7.PARENT_STORAGE_TYPE_ID = t6.IDENTIFIER " + " ) "
			// + " t8, CATISSUE_SITE t4, CATISSUE_CONTAINER_TYPE t5 WHERE t8.SITE_ID
			// = t4.IDENTIFIER " + " AND t8.STORAGE_TYPE_ID = t5.IDENTIFIER ";

			// Bug-2630: Added by jitendra
			String queryStr = "SELECT "
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
				+ ") t8, CATISSUE_SITE t4, CATISSUE_CONTAINER_TYPE t5 "
				+ "WHERE "
				+ "t8.SITE_ID = t4.IDENTIFIER  AND t8.STORAGE_TYPE_ID = t5.IDENTIFIER ";

			Logger.out.debug("Storage Container query......................"
					+ queryStr);
		


			list = dao.executeQuery(queryStr);
			getTreeNodeList(list);
		
			// printRecords(list);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			try {
				closeJDBCSession(dao);
			} catch (BizLogicException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (Vector)list;
		
	}

	/**
	 * Returns the vector of tree node for the storage container list.
	 * 
	 * @param resultList
	 *            the storage container list.
	 * @return the vector of tree node for the storage container list.
	 * @throws BizLogicException
	 */
	public Vector getTreeNodeList(List resultList) throws BizLogicException {
		Map containerRelationMap = new HashMap();

		// Vector of Tree Nodes for all the storage containers.
		Vector treeNodeVector = new Vector();
		Vector finalNodeVector = new Vector();

		if (resultList.isEmpty() == false) {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				List rowList = (List) iterator.next();

				// Bug-2630: Added by jitendra
				if ((String) rowList.get(8) != null
						&& !((String) rowList.get(8))
								.equals(Status.ACTIVITY_STATUS_DISABLED)) {
					// Mandar : code for tooltip for the container
					String toolTip = getToolTipData((String) rowList.get(0));

					// Create the tree node for the child node.
					TreeNode treeNodeImpl = new StorageContainerTreeNode(Long
							.valueOf((String) rowList.get(0)), (String) rowList
							.get(1), (String) rowList.get(1), toolTip,
							(String) rowList.get(8));

					// Add the tree node in the Vector if it is not present.
					if (treeNodeVector.contains(treeNodeImpl) == false) {
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
					if (containerRelationMap.containsKey(Long
							.valueOf((String) rowList.get(5)))) {
						childIds = (List) containerRelationMap.get(Long
								.valueOf((String) rowList.get(5)));
					}

					// Put the container in the child container list of the
					// parent container
					// and update the Map.
					childIds.add(Long.valueOf((String) rowList.get(0)));
					containerRelationMap.put(Long.valueOf((String) rowList
							.get(5)), childIds);

					// Create the tree node for the parent node and add it in
					// the vector if not present.
					String toolTip = getToolTipData((String) rowList.get(5));
					TreeNode treeNodeImpl = new StorageContainerTreeNode(Long
							.valueOf((String) rowList.get(5)), (String) rowList
							.get(6), (String) rowList.get(6), toolTip,
							(String) rowList.get(9));
					if (treeNodeVector.contains(treeNodeImpl) == false) {
						treeNodeVector.add(treeNodeImpl);
					}
				}

			}
			// printVectorMap(treeNodeVector, containerRelationMap);

			finalNodeVector = createHierarchy(containerRelationMap,
					treeNodeVector);
		}

		return finalNodeVector;
	}

	/**
	 * Creates the hierarchy of the tree nodes of the container according to the
	 * container relationship map.
	 * 
	 * @param containerRelationMap
	 *            the container relationship map.
	 * @param treeNodeVector
	 *            the vector of tree nodes.
	 * @return the hierarchy of the tree nodes of the container according to the
	 *         container relationship map.
	 * @throws BizLogicException
	 */
	private Vector createHierarchy(Map containerRelationMap,
			Vector treeNodeVector) throws BizLogicException {

		// Get the ket set of the parent containers.
		Set keySet = containerRelationMap.keySet();
		Iterator iterator = keySet.iterator();

		while (iterator.hasNext()) {
			// Get the parent container id and create the tree node.
			Long parentId = (Long) iterator.next();
			StorageContainerTreeNode parentTreeNodeImpl = new StorageContainerTreeNode(
					parentId, null, null);
			parentTreeNodeImpl = (StorageContainerTreeNode) treeNodeVector
					.get(treeNodeVector.indexOf(parentTreeNodeImpl));

			// Get the child container ids and create the tree nodes.
			List childNodeList = (List) containerRelationMap.get(parentId);
			Iterator childIterator = childNodeList.iterator();
			while (childIterator.hasNext()) {
				Long childId = (Long) childIterator.next();
				StorageContainerTreeNode childTreeNodeImpl = new StorageContainerTreeNode(
						childId, null, null);
				childTreeNodeImpl = (StorageContainerTreeNode) treeNodeVector
						.get(treeNodeVector.indexOf(childTreeNodeImpl));

				// Set the relationship between the parent and child tree nodes.
				childTreeNodeImpl.setParentNode(parentTreeNodeImpl);
				parentTreeNodeImpl.getChildNodes().add(childTreeNodeImpl);
			}
			// for sorting
			List tempChildNodeList = parentTreeNodeImpl.getChildNodes();
			parentTreeNodeImpl.setChildNodes(tempChildNodeList);
		}

		// Get the container whose tree node has parent null
		// and get its site tree node and set it as its child.
		Vector parentNodeVector = new Vector();
		iterator = treeNodeVector.iterator();
		// System.out.println("\nNodes without Parent\n");
		while (iterator.hasNext()) {
			StorageContainerTreeNode treeNodeImpl = (StorageContainerTreeNode) iterator
					.next();

			if (treeNodeImpl.getParentNode() == null) {
				// System.out.print("\n" + treeNodeImpl);
				TreeNodeImpl siteNode = getSiteTreeNode(treeNodeImpl
						.getIdentifier());
				// System.out.print("\tSiteNodecreated: " + siteNode);
				if (parentNodeVector.contains(siteNode)) {
					siteNode = (TreeNodeImpl) parentNodeVector
							.get(parentNodeVector.indexOf(siteNode));
					// System.out.print("SiteNode Found");
				} else {
					parentNodeVector.add(siteNode);
					// System.out.print("\tSiteNodeSet: " + siteNode);
				}
				treeNodeImpl.setParentNode(siteNode);
				siteNode.getChildNodes().add(treeNodeImpl);

				// for sorting
				List tempChildNodeList = siteNode.getChildNodes();
				siteNode.setChildNodes(tempChildNodeList);
			}
		}

		// Get the containers under site.
		Vector containersUnderSite = getContainersUnderSite();
		containersUnderSite.removeAll(parentNodeVector);
		parentNodeVector.addAll(containersUnderSite);
		edu.wustl.common.util.Utility.sortTreeVector(parentNodeVector);
		return parentNodeVector;
	}

	private Vector getContainersUnderSite() throws BizLogicException {
		// String sql = " SELECT sc.IDENTIFIER, sc.CONTAINER_NAME, scType.TYPE,
		// site.IDENTIFIER, site.NAME, site.TYPE "
		// + " from catissue_storage_container sc, catissue_site site,
		// catissue_storage_type scType "
		// + " where sc.SITE_ID = site.IDENTIFIER AND sc.STORAGE_TYPE_ID =
		// scType.IDENTIFIER "
		// + " and sc.PARENT_CONTAINER_ID is NULL";

		JDBCDAO dao = null;
		Vector containerNodeVector = new Vector();
		try {
			String sql = "SELECT sc.IDENTIFIER, cn.NAME, scType.NAME, site.IDENTIFIER,"
				+ "site.NAME, site.TYPE from catissue_storage_container sc, "
				+ "catissue_site site, catissue_container_type scType, "
				+ "catissue_container cn where sc.SITE_ID = site.IDENTIFIER "
				+ "AND sc.STORAGE_TYPE_ID = scType.IDENTIFIER "
				+ "and sc.IDENTIFIER = cn.IDENTIFIER "
				+ "and cn.IDENTIFIER not in (select pos.CONTAINER_ID from catissue_container_position pos)";

			dao = openJDBCSession();
			List resultList = new ArrayList();
			


			resultList = dao.executeQuery(sql);
			dao.closeSession();
			// System.out.println("\nIn getContainersUnderSite()\n ");
			printRecords(resultList);


			Iterator iterator = resultList.iterator();
			while (iterator.hasNext()) {
				List rowList = (List) iterator.next();
				StorageContainerTreeNode containerNode = new StorageContainerTreeNode(
						Long.valueOf((String) rowList.get(0)), (String) rowList
						.get(1), (String) rowList.get(1));
				StorageContainerTreeNode siteNode = new StorageContainerTreeNode(
						Long.valueOf((String) rowList.get(3)), (String) rowList
						.get(4), (String) rowList.get(4));

				if (containerNodeVector.contains(siteNode)) {
					siteNode = (StorageContainerTreeNode) containerNodeVector
					.get(containerNodeVector.indexOf(siteNode));
				} else
					containerNodeVector.add(siteNode);
				containerNode.setParentNode(siteNode);
				siteNode.getChildNodes().add(containerNode);
			}

		} catch (Exception daoExp) {
			
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		finally
		{
			try {
				dao.closeSession();
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return containerNodeVector;
	}

	/**
	 * Returns the site tree node of the container with the given identifier.
	 * 
	 * @param identifier
	 *            the identifier of the container.
	 * @return the site tree node of the container with the given identifier.
	 * @throws BizLogicException
	 */
	private TreeNodeImpl getSiteTreeNode(Long identifier) throws BizLogicException {
		String sql = "SELECT site.IDENTIFIER, site.NAME, site.TYPE "
				+ " from catissue_storage_container sc, catissue_site site "
				+ " where sc.SITE_ID = site.IDENTIFIER AND sc.IDENTIFIER = "
				+ identifier.longValue();

		Logger.out.debug("Site Query........................." + sql);

		List resultList = executeSQL(sql);

		TreeNodeImpl siteTreeNode = null;
		if (resultList.isEmpty() == false) {
			List siteRecord = (List) resultList.get(0);
			siteTreeNode = new StorageContainerTreeNode(Long
					.valueOf((String) siteRecord.get(0)), (String) siteRecord
					.get(1), (String) siteRecord.get(1));
		}

		return siteTreeNode;
	}

	/**
	 * This method will add all the node into the vector that contains any
	 * container node and add a dummy container node to show [+] sign on the UI,
	 * so that on clicking expand sign ajax call will retrieve child container
	 * node under the site node.
	 */
	public Vector getSiteWithDummyContainer(Long userId) throws BizLogicException {
		String sql = "SELECT site.IDENTIFIER, site.NAME,COUNT(site.NAME) FROM CATISSUE_SITE "
				+ " site join CATISSUE_STORAGE_CONTAINER sc ON sc.site_id = site.identifier join "
				+ "CATISSUE_CONTAINER con ON con.identifier = sc.identifier WHERE con.ACTIVITY_STATUS!='Disabled' "
				+ "GROUP BY site.IDENTIFIER, site.NAME"
				+" order by upper(site.NAME)";
		
		JDBCDAO dao = null;
		Vector containerNodeVector = new Vector();

		try {
			dao = openJDBCSession();
			List resultList = new ArrayList();
			Long nodeIdentifier;
			String nodeName = null;
			String dummyNodeName = null;

			
		
			resultList = dao.executeQuery(sql);
			dao.closeSession();


			Iterator iterator = resultList.iterator();
			Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(userId);


			while (iterator.hasNext()) {
				List rowList = (List) iterator.next();

				nodeIdentifier = Long.valueOf((String) rowList.get(0));

				if (hasPrivilegeonSite(siteIdSet, nodeIdentifier)) {
					nodeName = (String) rowList.get(1);
					dummyNodeName = Constants.DUMMY_NODE_NAME;

					StorageContainerTreeNode siteNode = new StorageContainerTreeNode(
							nodeIdentifier, nodeName, nodeName);
					StorageContainerTreeNode dummyContainerNode = new StorageContainerTreeNode(
							nodeIdentifier, dummyNodeName, dummyNodeName);
					dummyContainerNode.setParentNode(siteNode);
					siteNode.getChildNodes().add(dummyContainerNode);
					containerNodeVector.add(siteNode);
				}
			}

		} catch (Exception daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		return containerNodeVector;
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node
	 * @param nodeName
	 *            Name of the site or container
	 * @param parentId
	 *            parent identifier of the selected node
	 * @return containerNodeVector This vector contains all the containers
	 * @throws BizLogicException
	 * @Description This method will retrieve all the containers under the
	 *              selected node
	 */
	public Vector<StorageContainerTreeNode> getStorageContainers(
			Long identifier, String nodeName, String parentId)
			throws BizLogicException 
	{
		JDBCDAO dao = null;
		Vector<StorageContainerTreeNode> containerNodeVector = new Vector<StorageContainerTreeNode>();
		try
		{
			dao = openJDBCSession();
			List resultList = new ArrayList();
			if(Constants.ZERO_ID.equals(parentId))
			{
				resultList = getContainersForSite(identifier, dao);//Bug 11694
			}
			else
			{
				String sql = createSql(identifier, parentId);
				resultList = dao.executeQuery(sql);
				
				
			}
			String dummyNodeName = Constants.DUMMY_NODE_NAME;
			String containerName = null;
			Long nodeIdentifier;
			Long parentContainerId;
			Long childCount;

			
			Iterator iterator = resultList.iterator();
			while (iterator.hasNext()) {
				List rowList = (List) iterator.next();
				nodeIdentifier = Long.valueOf((String) rowList.get(0));
				containerName = (String) rowList.get(1);
				parentContainerId = Long.valueOf((String) rowList.get(2));
				childCount = Long.valueOf((String) rowList.get(3));

				StorageContainerTreeNode containerNode = new StorageContainerTreeNode(
						nodeIdentifier, containerName, containerName);
				StorageContainerTreeNode parneContainerNode = new StorageContainerTreeNode(
						parentContainerId, nodeName, nodeName);

				if (childCount != null && childCount > 0) {
					StorageContainerTreeNode dummyContainerNode = new StorageContainerTreeNode(
							Long.valueOf((String) rowList.get(0)), dummyNodeName,
							dummyNodeName);
					dummyContainerNode.setParentNode(containerNode);
					containerNode.getChildNodes().add(dummyContainerNode);
				}

				if (containerNodeVector.contains(containerNode)) {
					containerNode = (StorageContainerTreeNode) containerNodeVector
					.get(containerNodeVector.indexOf(containerNode));
				} else {
					containerNodeVector.add(containerNode);
				}
				containerNode.setParentNode(parneContainerNode);
				parneContainerNode.getChildNodes().add(containerNode);
			}
			if (containerNodeVector.isEmpty()) {
				StorageContainerTreeNode containerNode = new StorageContainerTreeNode(
						identifier, nodeName, nodeName);
				containerNodeVector.add(containerNode);
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		finally
		{
			try {
				dao.closeSession();
			} catch (DAOException e) {
				// TODO Auto-generated catch block
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
		sql = "SELECT cn.IDENTIFIER, cn.name, pos.PARENT_CONTAINER_ID,COUNT(sc3.IDENTIFIER) "
			+ "FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc ON sc.IDENTIFIER=cn.IDENTIFIER "
			+ "left outer join catissue_container_position pos on pos.CONTAINER_ID=cn.IDENTIFIER left outer join "
			+ "catissue_container_position con_pos on con_pos.PARENT_CONTAINER_ID=cn.IDENTIFIER left outer join "
			+ "CATISSUE_STORAGE_CONTAINER sc3 on con_pos.CONTAINER_ID=sc3.IDENTIFIER "
			+ "WHERE pos.PARENT_CONTAINER_ID= "
			+ identifier
			+ " AND cn.ACTIVITY_STATUS!='Disabled' GROUP BY cn.IDENTIFIER, cn.NAME,pos.PARENT_CONTAINER_ID"
			+ " ORDER BY cn.IDENTIFIER ";		
		return sql;
	}	
	//Bug 11694
	/**
	 * This method will return list of parent containers.
	 * @param identifier - site id
	 * @param dao
	 * @return
	 * @throws BizLogicException
	 */
	private List getContainersForSite(Long identifier,JDBCDAO dao) throws BizLogicException
	{
		List resultList = new ArrayList();
		Map<Long,List> resultSCMap= new LinkedHashMap<Long,List>();
		List storageContainerList = new ArrayList();
		List childContainerList = new ArrayList();
		Set childContainerIds = new LinkedHashSet();
		/**
		 *  This query will return list of all storage containers within the specified site.
		 */
		String query = "SELECT cn.IDENTIFIER,cn.NAME FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc " +
				"ON sc.IDENTIFIER=cn.IDENTIFIER join CATISSUE_SITE site "+
				"ON sc.site_id = site.identifier WHERE " +
				"cn.ACTIVITY_STATUS!='Disabled' AND site_id="+identifier;
		try 
		{
			dao.openSession(null);
			storageContainerList = dao.executeQuery(query);
			Iterator iterator = storageContainerList.iterator();
			while (iterator.hasNext())
            {
				List rowList = (List) iterator.next();
				Long id = Long.valueOf((String) rowList.get(0));
				String name = ((String)rowList.get(1));
				resultSCMap.put(id, rowList);
            }
			/**
			 * This query will return list of all child storage containers within the specified site.
			 */
			String childQuery = "SELECT pos.CONTAINER_ID FROM CATISSUE_CONTAINER_POSITION pos " +
					"join CATISSUE_STORAGE_CONTAINER sc ON pos.CONTAINER_ID=sc.IDENTIFIER " +
			        "WHERE sc.site_id="+ identifier;
			childContainerList = dao.executeQuery(childQuery);
			Iterator iterator1 = childContainerList.iterator();
			while (iterator1.hasNext())
            {
				List rowListPos = (List) iterator1.next();
				childContainerIds.add(Long.valueOf((String) rowListPos.get(0)));
            }
			Set parentIds = resultSCMap.keySet();
			//removed all child containers 
			//this will return set of all parent(1st level) containers 
			parentIds.removeAll(childContainerIds);
					
			StringBuffer parentContainerIdsBuffer = new StringBuffer();
			Iterator it = parentIds.iterator();
			while(it.hasNext())
			{ 
				parentContainerIdsBuffer.append(it.next());
				parentContainerIdsBuffer.append(",");
			
			}
			parentContainerIdsBuffer.deleteCharAt(parentContainerIdsBuffer.length()-1);
			
			/*
			 * This query will return child count of parent containers.
			 */
			String imediateChildCountQuery = "SELECT PARENT_CONTAINER_ID,COUNT(*) FROM CATISSUE_CONTAINER_POSITION GROUP BY " +
					"PARENT_CONTAINER_ID HAVING PARENT_CONTAINER_ID IN ("+parentContainerIdsBuffer.toString()+")";
			List result = dao.executeQuery(imediateChildCountQuery);
			Map<Long,Long> childCountMap= new LinkedHashMap<Long,Long>();
			Iterator itr = result.iterator();
			while (itr.hasNext())
            {
				List rowList = (List) itr.next();
				Long id = Long.valueOf((String) rowList.get(0));
				Long childCount = Long.valueOf((String) rowList.get(1));
				childCountMap.put(id, childCount);
            }
			dao.closeSession();		
			
			Iterator parentContainerIterator = parentIds.iterator();
			/**
			 * Formed resultList having data as follows:
			 * 1. parent container id
			 * 2. container name
			 * 3. site id
			 * 4. child count
			 *  
			 */
			while(parentContainerIterator.hasNext())
			{
				Long id = (Long) parentContainerIterator.next();//parent container id
				List strorageContainerList = resultSCMap.get(id);	
				//strorageContainerList -> containing container id and name
				strorageContainerList.add(identifier.toString());//site id			
				if(childCountMap.containsKey(id))
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
		catch (Exception daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
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

	private boolean[][] getStorageContainerFullStatus(DAO dao,
			StorageContainer parentContainer, Collection children)
			throws BizLogicException {
		// List list = dao.retrieve(StorageContainer.class.getName(), "id", id);
		boolean[][] fullStatus = null;

		Integer oneDimensionCapacity = parentContainer.getCapacity()
				.getOneDimensionCapacity();
		Integer twoDimensionCapacity = parentContainer.getCapacity()
				.getTwoDimensionCapacity();
		fullStatus = new boolean[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
				.intValue() + 1];

		// Collection children = StorageContainerUtil.getChildren(dao,
		// storageContainer.getId());

		if (children != null) {
			Iterator iterator = children.iterator();
			Logger.out
					.debug("storageContainer.getChildrenContainerCollection().size(): "
							+ children.size());
			while (iterator.hasNext()) {
				StorageContainer childStorageContainer = (StorageContainer) iterator
						.next();
				if (childStorageContainer.getLocatedAtPosition() != null) {
					Integer positionDimensionOne = childStorageContainer
							.getLocatedAtPosition().getPositionDimensionOne();
					Integer positionDimensionTwo = childStorageContainer
							.getLocatedAtPosition().getPositionDimensionTwo();
					Logger.out.debug("positionDimensionOne : "
							+ positionDimensionOne.intValue());
					Logger.out.debug("positionDimensionTwo : "
							+ positionDimensionTwo.intValue());
					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = true;
				}
			}
		}

		return fullStatus;
	}

	/**
	 * @param containerId
	 * @return
	 * @throws BizLogicException
	 */
	public Collection getContainerChildren(Long containerId)
			throws BizLogicException 
	{
		DAO dao = null;
		Collection<Container> children = null;
		try 
		{
			dao = openDAOSession(null);
			children = StorageContainerUtil.getChildren(dao, containerId);
		} 
		catch (ApplicationException e) {
			throw getBizLogicException(e, "utility.error", "");
		} 
		finally {
			closeDAOSession(dao);
		}
		return children;
	}

	private void disableSubStorageContainer(DAO dao,
			SessionDataBean sessionDataBean,
			List<StorageContainer> disabledContainerList) throws BizLogicException
	 {

		try
		{
			// adding updated participantMap to cache
			// catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS,
			// participantMap);
			int count = disabledContainerList.size();
			List containerIdList = new ArrayList();
			for (int i = 0; i < count; i++) {
				StorageContainer container = disabledContainerList.get(i);
				containerIdList.add(container.getId());
			}

			List listOfSpecimenIDs = getRelatedObjects(dao, Specimen.class,
					"specimenPosition.storageContainer", edu.wustl.common.util.Utility
					.toLongArray(containerIdList));

			if (!listOfSpecimenIDs.isEmpty()) 
			{

				throw getBizLogicException(null, "errors.container.contains.specimen", "");
			}
			// Uodate containers to disabled
			for (int i = 0; i < count; i++) {
				StorageContainer container = disabledContainerList.get(i);
				dao.update(container);
			}

			auditDisabledObjects(dao, "CATISSUE_CONTAINER", containerIdList);
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private void disableSubStorageContainer(DAO dao,
			SessionDataBean sessionDataBean, Long storageContainerIDArr[])
			throws BizLogicException 
	{
		try
		{

			// adding updated participantMap to cache
			// catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS,
			// participantMap);
			List listOfSpecimenIDs = getRelatedObjects(dao, Specimen.class,
					"specimenPosition.storageContainer", storageContainerIDArr);

			if (!listOfSpecimenIDs.isEmpty()) {

				throw getBizLogicException(null, "errors.container.contains.specimen", "");
			}

			List listOfSubStorageContainerId = super.disableObjects(dao,
					Container.class, "locatedAtPosition.parentContainer",
					"CATISSUE_CONTAINER", "PARENT_CONTAINER_ID",
					storageContainerIDArr);

			if (listOfSubStorageContainerId.isEmpty()) {
				return;
			} else {
				Iterator itr = listOfSubStorageContainerId.iterator();
				while (itr.hasNext()) {
					Long contId = (Long) itr.next();
					String sourceObjectName = StorageContainer.class.getName();

					Object object = dao.retrieveById(sourceObjectName, contId);
					if (object != null) {
						StorageContainer cont = (StorageContainer) object;

						// cont.setParent(null);

						cont.setLocatedAtPosition(null);
						// dao.update(cont, sessionDataBean, true, true, false);
					}

				}
			}

			disableSubStorageContainer(dao, sessionDataBean, edu.wustl.common.util.Utility
					.toLongArray(listOfSubStorageContainerId));
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	// Checks for whether the user is trying to use a container without
	// privilege to use it
	// This is needed since now users can enter the values in the edit box
	public boolean validateContainerAccess(DAO dao, StorageContainer container,
			SessionDataBean sessionDataBean) throws BizLogicException {
		Logger.out.debug("validateContainerAccess..................");
		String userName = sessionDataBean.getUserName();

		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		
		// To get privilegeCache through
		// Singleton instance of PrivilegeManager, requires User LoginName
		// PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		// PrivilegeCache privilegeCache =
		// privilegeManager.getPrivilegeCache(userName);
		// Implemented as per the requirements of MSR. User should use only
		// those sites for which he has access to.

		Long userId = sessionDataBean.getUserId();
		Site site = null;
		Set loggedInUserSiteIdSet = null;
		try {
			site = getSite(dao, container.getId());
			loggedInUserSiteIdSet = new UserBizLogic().getRelatedSiteIds(userId);
			if(dao instanceof HibernateDAO)
				{
					((HibernateDAO)dao).openSession(null);
				}
		} catch (DAOException e) {
			return false;
		} finally {
			// try {
			// //dao.closeSession();
			// } catch (DAOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		if (loggedInUserSiteIdSet != null && loggedInUserSiteIdSet.contains(new Long(site.getId()))) {
			return true;
		} else {
			return false;
		}

		// if
		// (!SecurityManager.getInstance(this.getClass()).isAuthorized(userName,
		// StorageContainer.class.getName() + "_" + container.getId(),
		// Permissions.USE))

		// Call to SecurityManager.isAuthorized bypassed &
		// instead, call redirected to privilegeCache.hasPrivilege

		// Commented by Ravindra and Vishvesh because this is not how
		// if (!privilegeCache.hasPrivilege(StorageContainer.class.getName() +
		// "_" + container.getId(), Permissions.USE))
		// {
		// return false;
		// }

		// else
		// return true;
	}

	// Checks for whether the user is trying to place the container in a
	// position
	// outside the range of parent container
	// This is needed since now users can enter the values in the edit box
	protected boolean validatePosition(StorageContainer parent,
			StorageContainer current) {
		int posOneCapacity = parent.getCapacity().getOneDimensionCapacity()
				.intValue();
		int posTwoCapacity = parent.getCapacity().getTwoDimensionCapacity()
				.intValue();

		int positionDimensionOne = current.getLocatedAtPosition()
				.getPositionDimensionOne().intValue();
		int positionDimensionTwo = current.getLocatedAtPosition()
				.getPositionDimensionTwo().intValue();

		Logger.out.debug("validatePosition C : " + positionDimensionOne + " : "
				+ positionDimensionTwo);
		Logger.out.debug("validatePosition P : " + posOneCapacity + " : "
				+ posTwoCapacity);

		if ((positionDimensionOne > posOneCapacity)
				|| (positionDimensionTwo > posTwoCapacity)) {
			Logger.out.debug("validatePosition false");
			return false;
		}
		Logger.out.debug("validatePosition true");
		return true;
	}

	private boolean validatePosition(int posOneCapacity, int posTwoCapacity,
			StorageContainer current) {
		int positionDimensionOne = current.getLocatedAtPosition()
				.getPositionDimensionOne().intValue();
		int positionDimensionTwo = current.getLocatedAtPosition()
				.getPositionDimensionTwo().intValue();

		Logger.out.debug("validatePosition C : " + positionDimensionOne + " : "
				+ positionDimensionTwo);
		Logger.out.debug("validatePosition P : " + posOneCapacity + " : "
				+ posTwoCapacity);

		if ((positionDimensionOne > posOneCapacity)
				|| (positionDimensionTwo > posTwoCapacity)) {
			Logger.out.debug("validatePosition false");
			return false;
		}
		Logger.out.debug("validatePosition true");
		return true;
	}

	/**
	 * Bug ID: 4038 Patch ID: 4038_2 See also: 1-3
	 */
	/**
	 * This method is to validae position based on parent container id
	 * 
	 * @param dao
	 *            Object DAO
	 * @param container
	 *            current container
	 * @return boolean value based on validation
	 * @throws BizLogicException
	 *             exception occured while DB handling
	 */
	private boolean validatePosition(DAO dao, StorageContainer container)
			throws BizLogicException 
	{
		try
		{
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = { "id", "capacity.oneDimensionCapacity",
			"capacity.twoDimensionCapacity" };
			String[] whereColumnName = { "id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER


			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("id",container.getLocatedAtPosition()
					.getParentContainer().getId()));

			List list = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);

			Integer pcCapacityOne = 0;
			Integer pcCapacityTwo = 0;
			if (!list.isEmpty()) {
				Object[] obj1 = (Object[]) list.get(0);
				pcCapacityOne = (Integer) obj1[1];
				pcCapacityTwo = (Integer) obj1[2];

			}

			int positionDimensionOne = container.getLocatedAtPosition()
			.getPositionDimensionOne().intValue();
			int positionDimensionTwo = container.getLocatedAtPosition()
			.getPositionDimensionTwo().intValue();

			Logger.out.debug("validatePosition C : " + positionDimensionOne + " : "
					+ positionDimensionTwo);
			Logger.out.debug("validatePosition P : " + pcCapacityOne + " : "
					+ pcCapacityTwo);

			if ((positionDimensionOne > pcCapacityOne)
					|| (positionDimensionTwo > pcCapacityTwo)) {
				Logger.out.debug("validatePosition false");
				return false;
			}
			Logger.out.debug("validatePosition true");
			return true;
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	private boolean isContainerAvailableForDisabled(DAO dao, Long[] containerIds) {
		List containerList = new ArrayList();
		if (containerIds.length != 0) {
			try {
				String sourceObjectName = Specimen.class.getName();
				String[] selectColumnName = { "id" };
				String[] whereColumnName1 = { "specimenPosition.storageContainer.id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition1 = { "in" };
				Object[] whereColumnValue1 = { containerIds };
				String joinCondition = Constants.AND_JOIN_CONDITION;

				QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
				queryWhereClause.addCondition(new INClause("specimenPosition.storageContainer.id",containerIds));
				
				List list = dao.retrieve(sourceObjectName, selectColumnName,
						queryWhereClause);
				// check if Specimen exists with the given storageContainer
				// information
				if (list.size() != 0) {
					Object obj = list.get(0);
					return false;
				} else {
					sourceObjectName = SpecimenArray.class.getName();
					whereColumnName1[0] = "locatedAtPosition.parentContainer.id";
					
					QueryWhereClause queryWhereClauseNew = new QueryWhereClause(sourceObjectName);
					queryWhereClauseNew.addCondition(new INClause("locatedAtPosition.parentContainer.id",containerIds));
					
					list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClauseNew);
					// check if Specimen exists with the given storageContainer
					// information
					if (list.size() != 0) {
						return false;
					}
					/*
					 * else { sourceObjectName =
					 * StorageContainer.class.getName(); String[]
					 * whereColumnName = {"parent.id"}; containerList =
					 * dao.retrieve(sourceObjectName, selectColumnName,
					 * whereColumnName, whereColumnCondition1,
					 * whereColumnValue1, joinCondition);
					 *  }
					 */
				}

			} catch (Exception e) {
				Logger.out.debug("Error in isContainerAvailable : " + e);
				return false;
			}
		} else {
			return true;
		}

		return isContainerAvailableForDisabled(dao,edu.wustl.common.util.Utility
				.toLongArray(containerList));
	}

	// -- to check if storageContainer is available or used
	protected boolean isContainerAvailableForPositions(DAO dao,
			StorageContainer current) {
		try {
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = { "id" };
			/*String[] whereColumnName = {
			"locatedAtPosition.positionDimensionOne",
			"locatedAtPosition.positionDimensionTwo",
			"locatedAtPosition.parentContainer" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = { "=", "=", "=" };
			Object[] whereColumnValue = {
			current.getLocatedAtPosition().getPositionDimensionOne(),
			current.getLocatedAtPosition().getPositionDimensionTwo(),
			current.getLocatedAtPosition().getParentContainer() };*/
			String joinCondition = Constants.AND_JOIN_CONDITION;

			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("locatedAtPosition.positionDimensionOne",current.getLocatedAtPosition().getPositionDimensionOne())).andOpr().
			addCondition(new EqualClause("locatedAtPosition.positionDimensionTwo",current.getLocatedAtPosition().getPositionDimensionTwo())).andOpr().
			addCondition(new EqualClause("locatedAtPosition.parentContainer",current.getLocatedAtPosition().getParentContainer()));

			List list = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);
			Logger.out.debug("current.getParentContainer() :"
					+ current.getLocatedAtPosition().getParentContainer());
			// check if StorageContainer exists with the given storageContainer
			// information
			if (list.size() != 0) {
				Object obj = list.get(0);
				Logger.out
				.debug("**********IN isContainerAvailable : obj::::::: --------- "
						+ obj);
				return false;
			} else {
				sourceObjectName = Specimen.class.getName();
				/*String[] whereColumnName1 = {
				"specimenPosition.positionDimensionOne",
				"specimenPosition.positionDimensionTwo",
				"specimenPosition.storageContainer.id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition1 = { "=", "=", "=" };
				Object[] whereColumnValue1 = {
				current.getLocatedAtPosition()
						.getPositionDimensionOne(),
				current.getLocatedAtPosition()
						.getPositionDimensionTwo(),
				current.getLocatedAtPosition().getParentContainer()
						.getId() };*/

				QueryWhereClause queryWhereClauseNew = new QueryWhereClause(sourceObjectName);
				queryWhereClauseNew.addCondition(new EqualClause("specimenPosition.positionDimensionOne",current.getLocatedAtPosition()
						.getPositionDimensionOne())).andOpr().
						addCondition(new EqualClause("specimenPosition.positionDimensionTwo",current.getLocatedAtPosition()
								.getPositionDimensionTwo())).andOpr().
								addCondition(new EqualClause("specimenPosition.storageContainer.id",current.getLocatedAtPosition().getParentContainer()
										.getId()));


				list = dao.retrieve(sourceObjectName, selectColumnName,
						queryWhereClauseNew);
				// check if Specimen exists with the given storageContainer
				// information
				if (list.size() != 0) {
					Object obj = list.get(0);
					Logger.out
					.debug("**************IN isPositionAvailable : obj::::::: --------------- "
							+ obj);
					return false;
				} else {
					sourceObjectName = SpecimenArray.class.getName();
					/*whereColumnName1[0] = "locatedAtPosition.positionDimensionOne";
					whereColumnName1[1] = "locatedAtPosition.positionDimensionTwo";
					whereColumnName1[2] = "locatedAtPosition.parentContainer.id";*/
					
					
					QueryWhereClause queryWhereClauseInner = new QueryWhereClause(sourceObjectName);
					queryWhereClauseInner.addCondition(new EqualClause("locatedAtPosition.positionDimensionOne",current.getLocatedAtPosition()
							.getPositionDimensionOne())).andOpr().
					addCondition(new EqualClause("locatedAtPosition.positionDimensionTwo",current.getLocatedAtPosition()
							.getPositionDimensionTwo())).andOpr().
					addCondition(new EqualClause("locatedAtPosition.parentContainer.id",current.getLocatedAtPosition().getParentContainer()
							.getId()));

					
					
					list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClauseInner);
					// check if Specimen exists with the given storageContainer
					// information
					if (list.size() != 0) {
						Object obj = list.get(0);
						Logger.out
						.debug("**************IN isPositionAvailable : obj::::::: --------------- "
								+ obj);
						return false;
					}
				}

			}

			return true;
		} catch (Exception e) {
			Logger.out.debug("Error in isContainerAvailable : " + e);
			return false;
		}
	}

	// Will check only for valid range of the StorageContainer
	protected boolean validatePosition(StorageContainer storageContainer,
			String posOne, String posTwo) {
		try {
			Logger.out
					.debug("storageContainer.getCapacity().getOneDimensionCapacity() : "
							+ storageContainer.getCapacity()
									.getOneDimensionCapacity());
			Logger.out
					.debug("storageContainer.getCapacity().getTwoDimensionCapacity() : "
							+ storageContainer.getCapacity()
									.getTwoDimensionCapacity());
			int oneDimensionCapacity = (storageContainer.getCapacity()
					.getOneDimensionCapacity() != null ? storageContainer
					.getCapacity().getOneDimensionCapacity().intValue() : -1);
			int twoDimensionCapacity = (storageContainer.getCapacity()
					.getTwoDimensionCapacity() != null ? storageContainer
					.getCapacity().getTwoDimensionCapacity().intValue() : -1);
			if (((oneDimensionCapacity) < Integer.parseInt(posOne))
					|| ((twoDimensionCapacity) < Integer.parseInt(posTwo))) {
				return false;
			}
			return true;
		} catch (Exception e) {
			Logger.out.debug("Error in validatePosition : " + e);
			return false;
		}
	}

	// Will check only for Position is used or not.
	protected boolean isPositionAvailable(DAO dao,
			StorageContainer storageContainer, String posOne, String posTwo,Specimen specimen) {
		try {
			String sourceObjectName = Specimen.class.getName();
			String[] selectColumnName = { "id" };
			/*String[] whereColumnName = {
			"specimenPosition.positionDimensionOne",
			"specimenPosition.positionDimensionTwo",
			"specimenPosition.storageContainer.id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = { "=", "=", "=" };
			Object[] whereColumnValue = { Integer.valueOf(posOne), Integer.valueOf(posTwo),
			storageContainer.getId() };*/
			String joinCondition = Constants.AND_JOIN_CONDITION;

			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("specimenPosition.positionDimensionOne",Integer.valueOf(posOne))).andOpr().
			addCondition(new EqualClause("specimenPosition.positionDimensionTwo",Integer.valueOf(posTwo))).andOpr().
			addCondition(new EqualClause("specimenPosition.storageContainer.id",storageContainer.getId()));


			List list = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);
			Logger.out.debug("storageContainer.getId() :"
					+ storageContainer.getId());
			// check if Specimen exists with the given storageContainer
			// information

			if (list.size() != 0) 
			{
				Object obj = list.get(0);
				boolean isPosAvail=false;
				if(specimen!=null)
				{
					if((!((specimen.getLineage()).equalsIgnoreCase("New")))&&((Long)obj).longValue()==specimen.getId().longValue())
					{
						isPosAvail= true;
					}
				}
				Logger.out
				.debug("**************IN isPositionAvailable : obj::::::: --------------- "
						+ obj);
				// Logger.out.debug((Long)obj[0] );
				// Logger.out.debug((Integer)obj[1]);
				// Logger.out.debug((Integer )obj[2]);

				return isPosAvail;
			}
			else 
			{
				sourceObjectName = StorageContainer.class.getName();
				/*String[] whereColumnName1 = {
						"locatedAtPosition.positionDimensionOne",
						"locatedAtPosition.positionDimensionTwo",
						"locatedAtPosition.parentContainer.id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition1 = { "=", "=", "=" };
				Object[] whereColumnValue1 = { Integer.valueOf(posOne), Integer.valueOf(posTwo),
						storageContainer.getId() };*/
				
				QueryWhereClause queryWhereClauseNew = new QueryWhereClause(sourceObjectName);
				queryWhereClauseNew.addCondition(new EqualClause("locatedAtPosition.positionDimensionOne",Integer.valueOf(posOne))).andOpr().
				addCondition(new EqualClause("locatedAtPosition.positionDimensionTwo",Integer.valueOf(posTwo))).andOpr().
				addCondition(new EqualClause("locatedAtPosition.parentContainer.id",storageContainer.getId()));

				list = dao.retrieve(sourceObjectName, selectColumnName,
						queryWhereClauseNew);
				
				Logger.out.debug("storageContainer.getId() :"
						+ storageContainer.getId());
				// check if Specimen exists with the given storageContainer
				// information
				if (list.size() != 0) {
					Object obj = list.get(0);
					Logger.out
					.debug("**********IN isPositionAvailable : obj::::: --------- "
							+ obj);
					return false;
				} else {
					sourceObjectName = SpecimenArray.class.getName();
					/*String[] whereColumnName2 = {
							"locatedAtPosition.positionDimensionOne",
							"locatedAtPosition.positionDimensionTwo",
							"locatedAtPosition.parentContainer.id" };
					String[] whereColumnCondition2 = { "=", "=", "=" };
					Object[] whereColumnValue2 = { Integer.valueOf(posOne), Integer.valueOf(posTwo),
							storageContainer.getId() };*/

					QueryWhereClause queryWhereClauseInner = new QueryWhereClause(sourceObjectName);
					queryWhereClauseInner.addCondition(new EqualClause("locatedAtPosition.positionDimensionOne",Integer.valueOf(posOne))).andOpr().
					addCondition(new EqualClause("locatedAtPosition.positionDimensionTwo",Integer.valueOf(posTwo))).andOpr().
					addCondition(new EqualClause("locatedAtPosition.parentContainer.id",storageContainer.getId()));

					list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClauseInner);

					Logger.out.debug("storageContainer.getId() :"
							+ storageContainer.getId());
					// check if Specimen exists with the given storageContainer
					// information
					if (list.size() != 0) {
						Object obj = list.get(0);
						Logger.out
						.debug("**********IN isPositionAvailable : obj::::: --------- "
								+ obj);
						return false;
					}
				}
			}

			return true;

		} catch (Exception e) {
			Logger.out.debug("Error in isPositionAvailable : " + e);
			return false;
		}
	}

	// -- storage container validation for specimen
	public void checkContainer(DAO dao, String storageContainerID,
			String positionOne, String positionTwo,
			SessionDataBean sessionDataBean, boolean multipleSpecimen,Specimen specimen)
			throws BizLogicException {
		// List list = dao.retrieve(StorageContainer.class.getName(),
		// "id",storageContainerID );

		try
		{
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = { Constants.SYSTEM_IDENTIFIER,
					"capacity.oneDimensionCapacity",
					"capacity.twoDimensionCapacity", "name" };
			/*	String[] whereColumnName = { Constants.SYSTEM_IDENTIFIER };
				String[] whereColumnCondition = { "=" };
				Object[] whereColumnValue = { Long.valueOf(storageContainerID) };
				String joinCondition = Constants.AND_JOIN_CONDITION;*/

			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(Constants.SYSTEM_IDENTIFIER,Long.valueOf(storageContainerID)));

			List list = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);

			// check if StorageContainer exists with the given ID
			if (list.size() != 0) {
				Object[] obj = (Object[]) list.get(0);
				Logger.out
				.debug("**********SC found for given ID ****obj::::::: --------------- "
						+ obj);
				Logger.out.debug((Long) obj[0]);
				Logger.out.debug((Integer) obj[1]);
				Logger.out.debug((Integer) obj[2]);
				Logger.out.debug((String) obj[3]);

				StorageContainer pc = new StorageContainer();
				pc.setId((Long) obj[0]);
				pc.setName((String) obj[3]);

				Capacity cntPos = new Capacity();
				if (obj[1] != null && obj[2] != null) {
					cntPos.setOneDimensionCapacity((Integer) obj[1]);
					cntPos.setTwoDimensionCapacity((Integer) obj[2]);
					pc.setCapacity(cntPos);
				}

				// check if user has privilege to use the container
				boolean hasAccess = validateContainerAccess(dao,pc, sessionDataBean);
				Logger.out.debug("hasAccess..............." + hasAccess);
				if (!hasAccess) {

					throw getBizLogicException(null, "access.use.object.denied", "");
				}
				// check for closed Container
				checkStatus(dao, pc, "Storage Container");

				/**
				 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
				 * Description:Storage container will not be added to closed site
				 * :check for closed site
				 */

				checkClosedSite(dao, pc.getId(), "Container Site");

				// check for valid position
				boolean isValidPosition = validatePosition(pc, positionOne,
						positionTwo);
				Logger.out.debug("isValidPosition : " + isValidPosition);
				boolean canUsePosition = false;
				if (isValidPosition) // if position is valid
				{
					/*
					 * try {
					 */
					canUsePosition = isPositionAvailable(dao, pc, positionOne,
							positionTwo,specimen);
					/*
					 * } catch (Exception e) {
					 * 
					 * e.printStackTrace(); }
					 */
					/*
					 * try { canUsePosition =
					 * StorageContainerUtil.isPostionAvaialble(pc.getId().toString(),
					 * pc.getName(), positionOne, positionTwo); } catch
					 * (CacheException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); }
					 */
					Logger.out.debug("canUsePosition : " + canUsePosition);
					if (canUsePosition) // position empty. can be used
					{

					} else
						// position already in use
					{
						if (multipleSpecimen) {

							throw getBizLogicException(null, "errors.storageContainer.Multiple.inUse", "");
						} else {

							throw getBizLogicException(null, "errors.storageContainer.inUse", "");
						}
					}
				} else
					// position is invalid
				{
					throw getBizLogicException(null, "errors.storageContainer.dimensionOverflow", "");
				}
			} else
				// storageContainer does not exist
			{
				throw getBizLogicException(null, "errors.storageContainerExist", "");
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData(edu.wustl.common.beans.SessionDataBean,
	 *      java.util.Map)
	 */
	public Vector getTreeViewData(SessionDataBean sessionData, Map map,
			List list) throws DAOException {
		return null;
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values
	 */
	protected boolean validate(Object obj, DAO dao, String operation)
	throws BizLogicException {

		try
		{
			StorageContainer container = (StorageContainer) obj;

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
				throw getBizLogicException(null, "domain.object.null.err.msg", "");

			Validator validator = new Validator();
			if (container.getStorageType() == null) {
				message = ApplicationProperties.getValue("storageContainer.type");

				throw getBizLogicException(null, "errors.item.required", message);

			}
			if (container.getNoOfContainers() == null) {
				Integer conts = new Integer(1);
				container.setNoOfContainers(conts);

			}
			if (validator.isEmpty(container.getNoOfContainers().toString())) {
				message = ApplicationProperties
				.getValue("storageContainer.noOfContainers");

				throw getBizLogicException(null, "errors.item.required", message);
			}
			if (!validator.isNumeric(container.getNoOfContainers().toString(), 1)) {
				message = ApplicationProperties
				.getValue("storageContainer.noOfContainers");

				throw getBizLogicException(null, "errors.item.format", message);
			}

			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() == null) {
				if (container.getSite() == null
						|| container.getSite().getId() == null
						|| container.getSite().getId() <= 0) {
					message = ApplicationProperties
					.getValue("storageContainer.site");

					throw getBizLogicException(null,"errors.item.invalid", message);
				}
			}
			/*
			 * if
			 * (!validator.isNumeric(String.valueOf(container.getPositionDimensionOne()),
			 * 1) ||
			 * !validator.isNumeric(String.valueOf(container.getPositionDimensionTwo()),
			 * 1) ||
			 * !validator.isNumeric(String.valueOf(container.getParent().getId()),
			 * 1)) { message =
			 * ApplicationProperties.getValue("storageContainer.parentContainer");
			 * throw new
			 * DAOException(ApplicationProperties.getValue("errors.item.format",
			 * message)); }
			 */
			// validations for Container name
			// by falguni
			/*
			 * if (validator.isEmpty(container.getName())) { message =
			 * ApplicationProperties.getValue("storageContainer.name"); throw new
			 * DAOException(ApplicationProperties.getValue("errors.item.required",
			 * message)); }
			 */

			// validations for temperature
			if (container.getTempratureInCentigrade() != null
					&& !validator.isEmpty(container.getTempratureInCentigrade()
							.toString())
							&& (!validator.isDouble(container.getTempratureInCentigrade()
									.toString(), false))) {
				message = ApplicationProperties
				.getValue("storageContainer.temperature");

				throw getBizLogicException(null, "errors.item.format", message);

			}

			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null) {

				if (container.getLocatedAtPosition().getParentContainer().getId() == null) {
					String sourceObjectName = StorageContainer.class.getName();
					String[] selectColumnName = { "id" };
					/*String[] whereColumnName = { "name" };
				String[] whereColumnCondition = { "=" };
				Object[] whereColumnValue = { container.getLocatedAtPosition()
						.getParentContainer().getName() };
				String joinCondition = null;*/

					QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
					queryWhereClause.addCondition(new EqualClause("name" ,container.getLocatedAtPosition()
							.getParentContainer().getName()));

					List list = dao.retrieve(sourceObjectName, selectColumnName,
							queryWhereClause);

					if (!list.isEmpty()) {
						container.getLocatedAtPosition().getParentContainer()
						.setId((Long) list.get(0));
					} else {
						String message1 = ApplicationProperties
						.getValue("specimen.storageContainer");
						throw getBizLogicException(null, "errors.invalid", message1);
					}
				}

				// Long storageContainerId = specimen.getStorageContainer().getId();
				Integer xPos = container.getLocatedAtPosition()
				.getPositionDimensionOne();
				Integer yPos = container.getLocatedAtPosition()
				.getPositionDimensionTwo();
				boolean isContainerFull = false;
				/**
				 * Following code is added to set the x and y dimension in case only
				 * storage container is given and x and y positions are not given
				 */

				if (xPos == null || yPos == null) {
					isContainerFull = true;
					Map containerMapFromCache = null;
					try {
						containerMapFromCache = (TreeMap) StorageContainerUtil
						.getContainerMapFromCache();
					} catch (CacheException e) {
						e.printStackTrace();
					}

					if (containerMapFromCache != null) {
						Iterator itr = containerMapFromCache.keySet().iterator();
						while (itr.hasNext()) {
							NameValueBean nvb = (NameValueBean) itr.next();
							if (container.getLocatedAtPosition() != null
									&& container.getLocatedAtPosition()
									.getParentContainer() != null
									&& nvb.getValue().toString().equals(
											container.getLocatedAtPosition()
											.getParentContainer().getId()
											.toString())) {

								Map tempMap = (Map) containerMapFromCache.get(nvb);
								Iterator tempIterator = tempMap.keySet().iterator();
								;
								NameValueBean nvb1 = (NameValueBean) tempIterator
								.next();

								List list = (List) tempMap.get(nvb1);
								NameValueBean nvb2 = (NameValueBean) list.get(0);

								ContainerPosition cntPos = container
								.getLocatedAtPosition();

								cntPos.setPositionDimensionOne(new Integer(nvb1
										.getValue()));
								cntPos.setPositionDimensionTwo(new Integer(nvb2
										.getValue()));
								cntPos.setOccupiedContainer(container);
								isContainerFull = false;
								break;
							}

						}
					}

					if (isContainerFull) {
						throw getBizLogicException(null, "dao.error", "The Storage Container you specified is full");
					}
				}

				// VALIDATIONS FOR DIMENSION 1.
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition()
						.getPositionDimensionOne() != null
						&& validator.isEmpty(String.valueOf(container
								.getLocatedAtPosition().getPositionDimensionOne()))) {
					message = ApplicationProperties
					.getValue("storageContainer.oneDimension");

					throw getBizLogicException(null, "errors.item.required", message);
				} else {
					if (container.getLocatedAtPosition() != null
							&& container.getLocatedAtPosition()
							.getPositionDimensionOne() != null
							&& !validator.isNumeric(String.valueOf(container
									.getLocatedAtPosition()
									.getPositionDimensionOne()))) {
						message = ApplicationProperties
						.getValue("storageContainer.oneDimension");

						throw getBizLogicException(null, "errors.item.format", message);
					}
				}

				// Validations for dimension 2
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition()
						.getPositionDimensionTwo() != null
						&& !validator.isEmpty(String.valueOf(container
								.getLocatedAtPosition().getPositionDimensionTwo()))
								&& (!validator.isNumeric(String.valueOf(container
										.getLocatedAtPosition().getPositionDimensionTwo())))) {
					message = ApplicationProperties
					.getValue("storageContainer.twoDimension");

					throw getBizLogicException(null, "errors.item.format", message);

				}

			}
			if (operation.equals(Constants.ADD)) {
				if (!Status.ACTIVITY_STATUS_ACTIVE.equals(container
						.getActivityStatus())) {

					throw getBizLogicException(null, "activityStatus.active.errMsg", "");
				}

				if (container.isFull().booleanValue()) {

					throw getBizLogicException(null, "storageContainer.isContainerFull.errMsg", "");
				}
			} else {
				if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,
						container.getActivityStatus())) {

					throw getBizLogicException(null, "activityStatus.errMsg", "");
				}
			}

			return true;
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}

	}

	// TODO Write the proper business logic to return an appropriate list of
	// containers.
	public List getStorageContainerList() throws BizLogicException {
		String sourceObjectName = StorageContainer.class.getName();
		String[] displayNameFields = { Constants.SYSTEM_IDENTIFIER };
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List list = getList(sourceObjectName, displayNameFields, valueField,
				true);
		return list;
	}

	public List getCollectionProtocolList() throws BizLogicException {
		String sourceObjectName = CollectionProtocol.class.getName();
		List returnList = new ArrayList();
		NameValueBean nvb1 = new NameValueBean("--Any--", "-1");
		returnList.add(nvb1);
		List list = retrieve(sourceObjectName);
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			CollectionProtocol collectionProtocol = (CollectionProtocol) itr
					.next();
			NameValueBean nvb = new NameValueBean(
					collectionProtocol.getTitle(), collectionProtocol);
			returnList.add(nvb);
		}
		return returnList;
	}

	/**
	 * This functions returns a double dimensional boolean array which tells the
	 * availablity of storage positions of particular container. True -
	 * Available. False - Not Available.
	 * 
	 * @param container
	 *            The container.
	 * @return Returns a double dimensional boolean array of position
	 *         availablity.
	 * @throws BizLogicException
	 */
	public boolean[][] getAvailablePositionsForContainer(String containerId,
			int dimX, int dimY) throws BizLogicException {
		boolean[][] positions = new boolean[dimX][dimY];

		// Initializing the array
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				positions[i][j] = true;
			}
		}

		// Retrieving all the occupied positions by child containers
		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = { "locatedAtPosition.positionDimensionOne",
				"locatedAtPosition.positionDimensionTwo" };
		String[] whereColumnName = { "locatedAtPosition.parentContainer.id" };
		String[] whereColumnCondition = { "=" };
		Object[] whereColumnValue = { new Long(containerId) };

		List list = retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue, null);
		// Logger.out.debug("all the occupied positions by child
		// containers"+list);
		setPositions(positions, list);

		// Retrieving all the occupied positions by specimens
		sourceObjectName = Specimen.class.getName();
		whereColumnName[0] = "specimenPosition.storageContainer.id";
		selectColumnName[0] = "specimenPosition.positionDimensionOne";
		selectColumnName[1] = "specimenPosition.positionDimensionTwo";

		list = retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, null);
		setPositions(positions, list);

		// Retrieving all the occupied positions by specimens array
		sourceObjectName = SpecimenArray.class.getName();
		whereColumnName[0] = "locatedAtPosition.parentContainer.id";
		selectColumnName[0] = "locatedAtPosition.positionDimensionOne";
		selectColumnName[1] = "locatedAtPosition.positionDimensionTwo";

		list = retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, null);
		setPositions(positions, list);

		return positions;
	}

	/**
	 * @param positions
	 * @param list
	 */
	private void setPositions(boolean[][] positions, List list) {
		if (!list.isEmpty()) {
			int x, y;

			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
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
	 * 
	 * @param containerId
	 *            The container identifier.
	 * @return Returns a double dimensional boolean array of position
	 *         availablity.
	 * @throws BizLogicException
	 */
	// public boolean[][] getAvailablePositions(String containerId) throws
	// DAOException
	// {
	// // List list = retrieve(StorageContainer.class.getName(),
	// Constants.SYSTEM_IDENTIFIER, new Long(containerId));
	// //
	// // if (list != null)
	// // {
	// // StorageContainer container = (StorageContainer) list.get(0);
	// return getAvailablePositionsForContainer(containerId);
	// // }
	// // else
	// // {
	// // return new boolean[0][0];
	// // }
	// }
	/**
	 * This functions returns a map of available rows vs. available columns.
	 * 
	 * @param container
	 *            The container.
	 * @return Returns a map of available rows vs. available columns.
	 * @throws BizLogicException
	 */

	public Map getAvailablePositionMapForContainer(String containerId,
			int aliquotCount, String positionDimensionOne,
			String positionDimensionTwo) throws BizLogicException {
		Map map = new TreeMap();
		int count = 0;
		// Logger.out.debug("dimX:"+positionDimensionOne+":dimY:"+positionDimensionTwo);
		// if (!container.isFull().booleanValue())
		// {
		int dimX = Integer.parseInt(positionDimensionOne) + 1;
		int dimY = Integer.parseInt(positionDimensionTwo) + 1;

		boolean[][] availablePosistions = getAvailablePositionsForContainer(
				containerId, dimX, dimY);

		for (int x = 1; x < availablePosistions.length; x++) {

			List list = new ArrayList();

			for (int y = 1; y < availablePosistions[x].length; y++) {
				if (availablePosistions[x][y]) {
					list.add(new NameValueBean(new Integer(y), new Integer(y)));
					count++;
				}
			}

			if (!list.isEmpty()) {
				Integer xObj = new Integer(x);
				NameValueBean nvb = new NameValueBean(xObj, xObj);
				map.put(nvb, list);

			}
		}
		// }
		// Logger.out.info("Map :"+map);
		if (count < aliquotCount) {
			return new TreeMap();
		}
		return map;
	}

	/**
	 * This functions returns a map of available rows vs. available columns.
	 * 
	 * @param containerId
	 *            The container identifier.
	 * @return Returns a map of available rows vs. available columns.
	 * @throws BizLogicException
	 */
	// public Map getAvailablePositionMap(String containerId, int aliquotCount)
	// throws BizLogicException
	// {
	// // List list = retrieve(StorageContainer.class.getName(),
	// Constants.SYSTEM_IDENTIFIER, new Long(containerId));
	// //
	// // if (list != null)
	// // {
	// // StorageContainer container = (StorageContainer) list.get(0);
	// return getAvailablePositionMapForContainer(containerId, aliquotCount);
	// // }
	// // else
	// // {
	// // return new TreeMap();
	// // }
	// }
	/**
	 * This functions returns a map of allocated containers vs. their respective
	 * free locations.
	 * 
	 * @return Returns a map of allocated containers vs. their respective free
	 *         locations.
	 * @throws BizLogicException
	 */
	public Map getAllocatedContainerMap() throws BizLogicException {
		/*
		 * A code snippet inside the commented block should actually be replaced
		 * by the code to get the allocated containers of specific collection
		 * protocol
		 */
		// List list = retrieve(StorageContainer.class.getName());
		String[] selectColumnName = { Constants.SYSTEM_IDENTIFIER, "name",
				"capacity.oneDimensionCapacity",
				"capacity.twoDimensionCapacity" };
		List list = retrieve(StorageContainer.class.getName(), selectColumnName);
		Map containerMap = new TreeMap();
		Logger.out.info("===================== list size:" + list.size());
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			Object containerList[] = (Object[]) itr.next();
			// Logger.out.info("+++++++++++++++++++++++++++:"+container.getName()+"++++++++++:"+container.getId());
			Map positionMap = getAvailablePositionMapForContainer(String
					.valueOf(containerList[0]), 0, containerList[2].toString(),
					containerList[3].toString());

			if (!positionMap.isEmpty()) {
				// Logger.out.info("---------"+container.getName()+"------"+container.getId());
				NameValueBean nvb = new NameValueBean(containerList[1],
						containerList[0]);
				containerMap.put(nvb, positionMap);

			}
		}

		return containerMap;
	}

	protected void loadSiteFromContainerId(DAO dao, StorageContainer container)
			throws BizLogicException 
	{
		try
		{
			if (container != null)
			{
				Long sysId = container.getId();
				Object object = dao.retrieveById(StorageContainer.class.getName(),
						sysId);
				// System.out.println("siteIdList " + siteIdList);
				StorageContainer sc = (StorageContainer) object;
				// System.out.println("siteId " + sc.getSite().getId());
				container.setSite(sc.getSite());
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}

	}

	public TreeMap getAllocatedContaienrMapForContainer(long type_id,
			String exceedingMaxLimit, String selectedContainerName, SessionDataBean sessionDataBean)
	throws BizLogicException
	{
		long start = 0;
		long end = 0;
		TreeMap containerMap = new TreeMap();
		JDBCDAO jdbcdao= null;
		try 
		{

			jdbcdao = openJDBCSession();

			start = System.currentTimeMillis();
			//		String queryStr = "SELECT t1.IDENTIFIER, t1.NAME FROM CATISSUE_CONTAINER t1 WHERE "
			//				+ "t1.IDENTIFIER IN (SELECT t4.STORAGE_CONTAINER_ID FROM CATISSUE_ST_CONT_ST_TYPE_REL t4 "
			//				+ "WHERE t4.STORAGE_TYPE_ID = '"
			//				+ type_id
			//				+ "' OR t4.STORAGE_TYPE_ID='1') AND "
			//				+ "t1.ACTIVITY_STATUS='"
			//				+ Constants.ACTIVITY_STATUS_ACTIVE + "' order by IDENTIFIER";

			String queryStr = "SELECT t1.IDENTIFIER, t1.NAME FROM CATISSUE_CONTAINER t1 WHERE "
				+ "t1.IDENTIFIER IN (SELECT t4.STORAGE_CONTAINER_ID FROM CATISSUE_ST_CONT_ST_TYPE_REL t4 "
				+ "WHERE t4.STORAGE_TYPE_ID = '"
				+ type_id
				+ "' OR t4.STORAGE_TYPE_ID='1' and t4.STORAGE_CONTAINER_ID not in (select IDENTIFIER from catissue_storage_container where site_id in (select IDENTIFIER from catissue_site s1 where s1.ACTIVITY_STATUS='Closed'))) AND "
				+ "t1.ACTIVITY_STATUS='"
				+ Status.ACTIVITY_STATUS_ACTIVE.toString() + "' order by IDENTIFIER";


			Logger.out.debug("Storage Container query......................"
					+ queryStr);
			List list = new ArrayList();


			list = jdbcdao.executeQuery(queryStr);


			end = System.currentTimeMillis();
			System.out.println("Time taken for executing query : " + (end - start));


			Map containerMapFromCache = null;
			Set<Long> siteIds = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());

			try {
				containerMapFromCache = StorageContainerUtil
				.getContainerMapFromCache();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			boolean flag = true;
			if (containerMapFromCache != null) {
				int i = 1;
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					List list1 = (List) itr.next();
					String Id = (String) list1.get(0);

					Long siteId = getSiteIdForStorageContainerId(Long.valueOf(Id));
					if(!sessionDataBean.isAdmin())
					{
						if(!siteIds.contains(siteId))
						{
							continue;
						}
					}

					String name = (String) list1.get(1);
					NameValueBean nvb = new NameValueBean(name, Id, new Long(Id));
					if (selectedContainerName != null && flag) {
						if (!name.equalsIgnoreCase(selectedContainerName.trim())) {
							continue;
						}
						flag = false;
					}

					try {
						Map positionMap = (TreeMap) containerMapFromCache.get(nvb);

						if (positionMap != null && !positionMap.isEmpty()) {
							Map positionMap1 = deepCopyMap(positionMap);
							// NameValueBean nvb = new NameValueBean(Name, Id);
							if (i > containersMaxLimit) {
								exceedingMaxLimit = "true";
								break;
							} else {
								containerMap.put(nvb, positionMap1);
							}
							i++;
						}
					}

					catch (Exception e) {
						Logger.out.info("Error while getting map from cache");
						e.printStackTrace();
					}

				}
			}

			return containerMap;

		} catch (Exception ex) {
			throw getBizLogicException(ex, "dao.error", "");
		}
		finally
		{
			closeJDBCSession(jdbcdao);
		}

	}
	/* temp function end */

	
	private Long getSiteIdForStorageContainerId(Long scId) throws BizLogicException
	{
		DAO dao = null;
		Long siteId = null;
		try 
		{
			dao = openDAOSession(null);
			StorageContainer sc = (StorageContainer) dao.retrieveById(StorageContainer.class.getName(), scId);
			if(sc != null)
			{
				Site site = sc.getSite();
				siteId = site.getId();
			}
		}
		catch (Exception e1) 
		{
			throw getBizLogicException(e1, "dao.error", "");
		}
		finally
		{
			closeDAOSession(dao);
		}
		return siteId;
	}


	public TreeMap getAllocatedContaienrMapForSpecimen(long cpId,
			String specimenClass, int aliquotCount, String exceedingMaxLimit,
			SessionDataBean sessionData, boolean closeSession)
			throws BizLogicException {

		NameValueBeanRelevanceComparator comparator = new NameValueBeanRelevanceComparator();
		Logger.out
				.debug("method : getAllocatedContaienrMapForSpecimen()---getting containers for specimen--------------");
		TreeMap containerMap = new TreeMap(comparator);
		List list = getRelevantContainerList(cpId, specimenClass, closeSession);
		Logger.out
				.debug("getAllocatedContaienrMapForSpecimen()----- Size of list--------:"
						+ list.size());
		Map containerMapFromCache = null;
		try {
			containerMapFromCache = (TreeMap) StorageContainerUtil
					.getContainerMapFromCache();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (containerMapFromCache != null) {
			int i = 1;
			int relevenceCounter = 1;
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				List list1 = (List) itr.next();
				String Id = (String) list1.get(1);
				String Name = (String) list1.get(2);
				NameValueBean nvb = new NameValueBean(Name, Id, new Long(
						relevenceCounter));
				Map positionMap = (TreeMap) containerMapFromCache.get(nvb);
				if (positionMap != null && !positionMap.isEmpty()) {
					StorageContainer sc = new StorageContainer();
					sc.setId(new Long(Id));
					boolean hasAccess = true;
				
					DAO dao = openDAOSession(null);
					hasAccess = validateContainerAccess(dao,sc, sessionData,cpId);
					closeDAOSession(dao);
					
					if (!hasAccess)
						continue;

					if (i > containersMaxLimit) {
						Logger.out.debug("CONTAINERS_MAX_LIMIT reached");
						exceedingMaxLimit = new String("true");
						break;
					} else {
						if (aliquotCount > 0) {
							long count = countPositionsInMap(positionMap);
							if (count >= aliquotCount) {
								containerMap.put(nvb, positionMap);
							}
						} else {
							containerMap.put(nvb, positionMap);
						}
					}
					i++;
				}
				relevenceCounter++;
			}
			Logger.out
					.debug("getAllocatedContaienrMapForSpecimen()----Size of containerMap:"
							+ containerMap.size());
		}
		Logger.out.debug("exceedingMaxLimit----------" + exceedingMaxLimit);

		return containerMap;

	}

	private boolean validateContainerAccess(DAO dao, StorageContainer sc, SessionDataBean sessionData, long cpId) throws BizLogicException
    {
        boolean isValidContainer = validateContainerAccess(dao,sc,sessionData);
        
        if(sessionData != null && sessionData.isAdmin())
		{
			return true;
		}
        
        Collection<Site> siteCollection = null;
        Site site = null;
        if (isValidContainer)
        {
        	
			site = getSite(dao, sc.getId());
			siteCollection = new CollectionProtocolBizLogic().getRelatedSites(cpId);
            
            	if (siteCollection != null)  
            	{
            		for(Site site1 : siteCollection)
            		{
            			if(site1.getId().equals(site.getId()))
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
	 * 
	 * @param cpId
	 *            collection protocol Id
	 * @param specimenClass
	 *            class of the specimen
	 * @param closeSession
	 * @return list of containers in order of there relevence.
	 * @throws BizLogicException
	 * @author Vaishali
	 */
	public List getRelevantContainerList(long cpId, String specimenClass,
			boolean closeSession) throws BizLogicException
			{
		List list = new ArrayList();
		JDBCDAO dao = null;
		try
		{

			dao =openJDBCSession();

			String[] queryArray = new String[6];
			// category # 1
			// Gets all container which stores just specified collection protocol
			// and specified specimen class
			String equalToOne = " = 1 ";
			String greaterThanOne = " > 1 ";

			String equalToFour = " = 4 ";
			String notEqualToFour = " !=4 ";
			String endQry = " and t1.IDENTIFIER = t6.STORAGE_CONTAINER_ID  and t1.IDENTIFIER = t7.IDENTIFIER"
				+ " group by t6.STORAGE_CONTAINER_ID, t1.NAME "
				+ " order by co asc ";

			String cpRestrictionCountQuery = "(select count(*) from CATISSUE_ST_CONT_COLL_PROT_REL t4 where t4.STORAGE_CONTAINER_ID = t1.IDENTIFIER)";
			String specimenClassRestrictionQuery = "(select count(*) from CATISSUE_STOR_CONT_SPEC_CLASS t5 where t5.STORAGE_CONTAINER_ID = t1.IDENTIFIER)";
			// Vijay main query and default restriction query is updated according
			// to bug id#8076
			String mainQuery = " SELECT count(*) co, t6.STORAGE_CONTAINER_ID, t1.NAME FROM CATISSUE_CONTAINER t1 , CATISSUE_STOR_CONT_SPEC_CLASS t6 , CATISSUE_STORAGE_CONTAINER t7 "
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
			String defaultRestrictionQuery = " SELECT  count(*) co, t6.STORAGE_CONTAINER_ID, t1.NAME FROM CATISSUE_CONTAINER t1 , CATISSUE_STOR_CONT_SPEC_CLASS t6 , CATISSUE_STORAGE_CONTAINER t7 "
				+ " WHERE t1.IDENTIFIER NOT IN (SELECT t2.STORAGE_CONTAINER_ID FROM CATISSUE_ST_CONT_COLL_PROT_REL t2)"
				+ " and t1.IDENTIFIER IN (SELECT t3.STORAGE_CONTAINER_ID FROM CATISSUE_STOR_CONT_SPEC_CLASS t3"
				+ " WHERE t3.SPECIMEN_CLASS = '"
				+ specimenClass
				+ "') "
				+ " AND t1.ACTIVITY_STATUS='Active' AND t7.SITE_ID NOT IN (SELECT IDENTIFIER FROM CATISSUE_SITE WHERE ACTIVITY_STATUS='Closed')";

			String queryStr1 = mainQuery + " and " + cpRestrictionCountQuery
			+ equalToOne + " and " + specimenClassRestrictionQuery
			+ equalToOne + endQry;
			// category # 2
			// Gets all containers which holds just specified container and any
			// specimen class
			String queryStr2 = mainQuery + " and " + cpRestrictionCountQuery
			+ equalToOne + " and " + specimenClassRestrictionQuery
			+ greaterThanOne + endQry;

			// catgory # 3
			// Gets all the containers which holds other than specified collection
			// protocol and only specified specimen class
			String queryStr3 = mainQuery + " and " + cpRestrictionCountQuery
			+ greaterThanOne + " and " + specimenClassRestrictionQuery
			+ equalToOne + endQry;

			// catgory # 4
			// Gets all the containers which holds specified cp and other than
			// specified collection protocol and specified specimen class and other
			// than specified specimen class
			String queryStr4 = mainQuery + " and " + cpRestrictionCountQuery
			+ greaterThanOne + " and " + specimenClassRestrictionQuery
			+ greaterThanOne + endQry;

			// catgory # 5
			// Gets all the containers which holds any collection protocol and
			// specified specimen class and other than specified specimen class

			String queryStr5 = defaultRestrictionQuery + " and "
			+ specimenClassRestrictionQuery + notEqualToFour + endQry;
			// catgory # 6
			// Gets all the containers which holds any collection protocol and any
			// specimen class
			String queryStr6 = defaultRestrictionQuery + " and "
			+ specimenClassRestrictionQuery + equalToFour + endQry;

			queryArray[0] = queryStr1;
			queryArray[1] = queryStr2;
			queryArray[2] = queryStr3;
			queryArray[3] = queryStr4;
			queryArray[4] = queryStr5;
			queryArray[5] = queryStr6;

			for (int i = 0; i < 6; i++) {
				Logger.out.debug("Storage Container query......................"
						+ queryArray[i]);
				System.out.println("Storage Container query......................"
						+ queryArray[i]);
				List queryResultList = executeStorageContQuery(queryArray[i], dao);
				list.addAll(queryResultList);
			}

			if (closeSession) {
				dao.closeSession();
			}
			return list;

		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		finally
		{
			closeJDBCSession(dao);
		}
	}

	/**
	 * This function executes the query
	 * 
	 * @param query
	 * @param dao
	 * @return
	 * @throws BizLogicException
	 */
	public List executeStorageContQuery(String query, JDBCDAO dao)
			throws BizLogicException 
			{
		Logger.out.debug("Storage Container query......................"
				+ query);
		List list = new ArrayList();

		try {
			list = dao.executeQuery(query);
		} catch (DAOException daoExp) {
			throw getBizLogicException(daoExp, "dao.error", "");
			
		}

		return list;
	}

	/**
	 * Gets allocated container map for specimen array.
	 * 
	 * @param specimen_array_type_id
	 *            specimen array type id
	 * @param noOfAliqoutes
	 *            No. of aliquotes
	 * @return container map
	 * @throws BizLogicException --
	 *             throws DAO Exception
	 * @see edu.wustl.common.dao.JDBCDAOImpl
	 */
	public TreeMap getAllocatedContaienrMapForSpecimenArray(
			long specimen_array_type_id, int noOfAliqoutes,
			SessionDataBean sessionData, String exceedingMaxLimit)
			throws BizLogicException
	{
		
		NameValueBeanValueComparator contComp = new NameValueBeanValueComparator();
		TreeMap containerMap = new TreeMap(contComp);

		JDBCDAO dao = null;	
		try
		{
		 dao = openJDBCSession();
		
		String includeAllIdQueryStr = " OR t4.SPECIMEN_ARRAY_TYPE_ID = '"
				+ Constants.ARRAY_TYPE_ALL_ID + "'";

		if (!(new Validator().isValidOption(String
				.valueOf(specimen_array_type_id)))) {
			includeAllIdQueryStr = "";
		}
		String queryStr = "select t1.IDENTIFIER,t1.name from CATISSUE_CONTAINER t1,CATISSUE_STORAGE_CONTAINER t2 "
				+ "where t1.IDENTIFIER IN (select t4.STORAGE_CONTAINER_ID from CATISSUE_CONT_HOLDS_SPARRTYPE t4 "
				+ "where t4.SPECIMEN_ARRAY_TYPE_ID = '"
				+ specimen_array_type_id
				+ "'"
				+ includeAllIdQueryStr
				+ ") and t1.IDENTIFIER = t2.IDENTIFIER";

		Logger.out.debug("SPECIMEN ARRAY QUERY ......................"
				+ queryStr);
		List list = new ArrayList();
		
		Set<Long> siteIds = new UserBizLogic().getRelatedSiteIds(sessionData.getUserId());
		list = dao.executeQuery(queryStr);
		
		
		Logger.out.info("Size of list:" + list.size());
		Map containerMapFromCache = null;
		try {
			containerMapFromCache = (TreeMap) StorageContainerUtil
					.getContainerMapFromCache();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (containerMapFromCache != null) {
			int i = 1;
			Iterator itr = list.iterator();

			while (itr.hasNext()) {
				List list1 = (List) itr.next();
				String Id = (String) list1.get(0);

				Long siteId = getSiteIdForStorageContainerId(Long.valueOf(Id));
				if(!sessionData.isAdmin())
				{
					if(!siteIds.contains(siteId))
					{
						continue;
					}
				}
				
				String Name = (String) list1.get(1);
				NameValueBean nvb = new NameValueBean(Name, Id);
				Map positionMap = (TreeMap) containerMapFromCache.get(nvb);
				if (positionMap != null && !positionMap.isEmpty()) {
					// deep copy is required due to cache updation by reference
					Map positionMap1 = deepCopyMap(positionMap);
					// NameValueBean nvb = new NameValueBean(Name, Id);
					StorageContainer sc = new StorageContainer();
					sc.setId(new Long(Id));
					/*
					 * boolean hasAccess = true; try { hasAccess =
					 * validateContainerAccess(sc, sessionData); } catch
					 * (SMException sme) { sme.printStackTrace(); throw
					 * handleSMException(sme); } if (!hasAccess) continue;
					 */
					if (i > containersMaxLimit) {
						exceedingMaxLimit = "true";
						break;
					} else {
						containerMap.put(nvb, positionMap1);
					}
					i++;
				}

			}
		}

		return containerMap;
	
	}
	catch(DAOException daoExp)
	{
		throw getBizLogicException(daoExp, "dao.error", "");
	}
	finally
	{
		closeJDBCSession(dao);
	}
	}

	// --------------Code for Map Mandar: 04-Sep-06 start
	// Mandar : 29Aug06 : for StorageContainerMap
	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws BizLogicException
	 */
	public List getCollectionProtocolList(String id) throws BizLogicException {
		// Query to return titles of collection protocol related to given
		// storagecontainer. 29-Aug-06 Mandar.
		String sql = " SELECT SP.TITLE TITLE FROM CATISSUE_SPECIMEN_PROTOCOL SP, CATISSUE_ST_CONT_COLL_PROT_REL SC "
				+ " WHERE SP.IDENTIFIER = SC.COLLECTION_PROTOCOL_ID AND SC.STORAGE_CONTAINER_ID = "
				+ id;
		List resultList = executeSQL(sql);
		Iterator iterator = resultList.iterator();
		List returnList = new ArrayList();
		while (iterator.hasNext()) {
			List list = (List) iterator.next();
			String data = (String) list.get(0);
			returnList.add(data);
		}

		if (returnList.isEmpty()) {
			returnList.add(new String(Constants.ALL));
		}
		return returnList;
	}

	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws BizLogicException
	 */
	public List getSpecimenClassList(String id) throws BizLogicException {

		// Query to return specimen classes related to given storagecontainer.
		// 29-Aug-06 Mandar.
		String sql = " SELECT SP.SPECIMEN_CLASS CLASS FROM CATISSUE_STOR_CONT_SPEC_CLASS SP "
				+ "WHERE SP.STORAGE_CONTAINER_ID = " + id;
		List resultList = executeSQL(sql);
		Iterator iterator = resultList.iterator();
		List returnList = new ArrayList();
		while (iterator.hasNext()) {
			List list = (List) iterator.next();
			for (int cnt = 0; cnt < list.size(); cnt++) {
				String data = (String) list.get(cnt);
				returnList.add(data);
			}
		}
		if (returnList.isEmpty()) {
			// bug id 7438
			// returnList.add(new String(Constants.ALL));
			returnList.add(new String(Constants.NONE));
		}
		return returnList;
	}

	/**
	 * @param sql
	 * @return
	 * @throws BizLogicException
	 */
	private List executeSQL(String sql) throws BizLogicException
	{
		List resultList = new ArrayList();
		JDBCDAO dao = null;
		try {
			dao = openJDBCSession();
			resultList = dao.executeQuery(sql);
			
		} catch (Exception daoExp) {
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		finally
		{
			closeJDBCSession(dao);
		}
		return resultList;
	}

	// prints results returned from DAO executeQuery To comment after debug
	private void printRecords(List list) {
		if (list != null) {
			if (!list.isEmpty()) {
				// System.out.println("OuterList Size : " + list.size());
				for (int i = 0; i < list.size(); i++) {
					List innerList = (List) list.get(i);
					// System.out.println("\nInnerList Size : " +
					// innerList.size() + "\n");
					String s = "";
					for (int j = 0; j < innerList.size(); j++) {
						String s1 = (String) innerList.get(j);
						s = s + " | " + s1;
					}
					// System.out.print(s);
				}
			}
		}
	}

	// Method to fetch ToolTipData for a given Container
	private String getToolTipData(String containerID) throws BizLogicException {
		String toolTipData = "";

		List specimenClassList = getSpecimenClassList(containerID);

		String classData = "SpecimenClass";
		for (int counter = 0; counter < specimenClassList.size(); counter++) {
			String data = (String) specimenClassList.get(counter);
			classData = classData + " | " + data;
		}

		List collectionProtocolList = getCollectionProtocolList(containerID);

		String protocolData = "CollectionProtocol";
		for (int cnt = 0; cnt < collectionProtocolList.size(); cnt++) {
			String data = (String) collectionProtocolList.get(cnt);
			protocolData = protocolData + " | " + data;
		}

		toolTipData = protocolData + "\n" + classData;
		// System.out.println(toolTipData);

		return toolTipData;
	}

	// --------------Code for Map Mandar: 04-Sep-06 end

	// this function is for making the deep copy of map

	private Map deepCopyMap(Map positionMap) {

		Map positionMap1 = new TreeMap();
		Set keySet = positionMap.keySet();
		Iterator itr = keySet.iterator();

		while (itr.hasNext()) {
			NameValueBean key = (NameValueBean) itr.next();
			NameValueBean key1 = new NameValueBean(new Integer(key.getName()), new Integer(key
					.getValue()));
			List value = (ArrayList) positionMap.get(key);
			List value1 = new ArrayList();
			Iterator itr1 = value.iterator();

			while (itr1.hasNext()) {
				NameValueBean ypos = (NameValueBean) itr1.next();
				NameValueBean ypos1 = new NameValueBean(new Integer(ypos.getName()), new Integer(ypos
						.getValue()));
				value1.add(ypos1);

			}
			positionMap1.put(key1, value1);

		}
		return positionMap1;
	}

	private long countPositionsInMap(Map positionMap) {
		long count = 0;
		Set keySet = positionMap.keySet();
		Iterator itr = keySet.iterator();
		while (itr.hasNext()) {
			NameValueBean key = (NameValueBean) itr.next();
			List value = (ArrayList) positionMap.get(key);
			count = count + value.size();

		}
		return count;
	}

	/**
	 * Bug ID: 4038 Patch ID: 4038_3 See also: 1-3
	 */
	/**
	 * 
	 * @param dao
	 *            Object of DAO
	 * @param containerId
	 *            id of container whose site is to be retrieved
	 * @return Site object belongs to container with given id
	 * @throws BizLogicException
	 *             Exception occured while DB handling
	 */
	private Site getSite(DAO dao, Long containerId) throws BizLogicException {
		
		try
		{
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = new String[] { "site" };
			String[] whereColumnName = new String[] { "id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = new String[] { "=" };
			Object[] whereColumnValue = new Long[] { containerId };
			String joinCondition = null;

			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause("id",containerId));
			List list = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);

			if (!list.isEmpty()) {
				return ((Site) list.get(0));
			}
			return null;
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
	}

	/**
	 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
	 * Description:Storage container will not be added to closed site :check for
	 * closed site
	 */
	public void checkClosedSite(DAO dao, Long containerId, String errMessage)
			throws BizLogicException {

		Site site = getSite(dao, containerId);

		// check for closed Site
		if (site != null) {
			if (Status.ACTIVITY_STATUS_CLOSED.equals(site
					.getActivityStatus())) {
				
				throw getBizLogicException(null, "error.object.closed", "");
			}
		}

	}

	/**
	 * To get the ids of the CollectionProtocol that the given StorageContainer
	 * can hold.
	 * 
	 * @param type
	 *            The reference to StorageType object.
	 * @return The array of ids of CollectionProtocol that the given
	 *         StorageContainer can hold.
	 * @throws BizLogicException
	 */
	public long[] getDefaultHoldCollectionProtocolList(
			StorageContainer container) throws BizLogicException {
		Collection spcimenArrayTypeCollection = (Collection) retrieveAttribute(
				StorageContainer.class.getName(), container.getId(),
				"elements(collectionProtocolCollection)");
		if (spcimenArrayTypeCollection.isEmpty()) {
			return new long[] { -1 };
		} else {
			return AppUtility.getobjectIds(spcimenArrayTypeCollection);
		}
	}

	/**
	 * To check wether the Continer to display can holds the given type of
	 * container.
	 * 
	 * @param typeId
	 *            ContinerType id of container
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @param StorageContainerBizLogic
	 *            The reference to bizLogic class object.
	 * @return true if the given continer can hold the typet.
	 * @throws BizLogicException
	 */
	public boolean canHoldContainerType(int typeId,
			StorageContainer storageContainer) throws BizLogicException {
		/**
		 * Name: Smita Reviewer: Sachin Bug iD: 4598 Patch ID: 4598_1
		 * Description: Check for valid container type
		 */
		if (!isValidContaierType(typeId)) {
			return false;
		}

		boolean canHold = false;
		Collection containerTypeCollection = (Collection) retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(holdsStorageTypeCollection)");// storageContainer.getHoldsStorageTypeCollection();
		if (!containerTypeCollection.isEmpty()) {
			Iterator itr = containerTypeCollection.iterator();
			while (itr.hasNext()) {
				StorageType type = (StorageType) itr.next();
				long storagetypeId = type.getId().longValue();
				if (storagetypeId == Constants.ALL_STORAGE_TYPE_ID
						|| storagetypeId == typeId) {
					return true;
				}
			}
		}
		return canHold;
	}

	/**
	 * Patch ID: 4598_2 Is container type one from the container types present
	 * in the system
	 * 
	 * @param typeID
	 *            Container type ID
	 * @return true/ false
	 * @throws BizLogicException
	 */
	public boolean isValidContaierType(int typeID) throws BizLogicException {
		Long longId = (Long) retrieveAttribute(StorageType.class.getName(),
				new Long(typeID), "id");
		return !(longId == null);
	}

	/**
	 * To check wether the Continer to display can holds the given
	 * CollectionProtocol.
	 * 
	 * @param collectionProtocolId
	 *            The collectionProtocol Id.
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @return true if the given continer can hold the CollectionProtocol.
	 * @throws BizLogicException
	 */
	public boolean canHoldCollectionProtocol(long collectionProtocolId,
			StorageContainer storageContainer) throws BizLogicException {
		boolean canHold = true;
		Collection collectionProtocols = (Collection) retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(collectionProtocolCollection)");// storageContainer.getCollectionProtocolCollection();
		if (!collectionProtocols.isEmpty()) {
			Iterator itr = collectionProtocols.iterator();
			canHold = false;
			while (itr.hasNext()) {
				CollectionProtocol cp = (CollectionProtocol) itr.next();
				if (cp.getId().longValue() == collectionProtocolId) {
					return true;

				}
			}
		}
		return canHold;
	}

	/**
	 * To check wether the Continer to display can holds the given
	 * specimenClass.
	 * 
	 * @param specimenClass
	 *            The specimenClass Name.
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @param bizLogic
	 *            The reference to bizLogic class object.
	 * @return true if the given continer can hold the specimenClass.
	 * @throws BizLogicException
	 */
	public boolean canHoldSpecimenClass(String specimenClass,
			StorageContainer storageContainer) throws BizLogicException {
		Collection specimenClasses = (Collection) retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(holdsSpecimenClassCollection)");// storageContainer.getHoldsSpecimenClassCollection();
		Iterator itr = specimenClasses.iterator();
		while (itr.hasNext()) {
			String className = (String) itr.next();
			if (className.equals(specimenClass))
				return true;

		}
		return false;
	}

	/**
	 * To check wether the Continer to display can holds the given
	 * specimenArrayTypeId.
	 * 
	 * @param specimenArrayTypeId
	 *            The Specimen Array Type Id.
	 * @param storageContainer
	 *            The StorageContainer reference to be displayed on the page.
	 * @param bizLogic
	 *            The reference to bizLogic class object.
	 * @return true if the given continer can hold the specimenArrayType.
	 */
	public boolean canHoldSpecimenArrayType(int specimenArrayTypeId,
			StorageContainer storageContainer) throws BizLogicException {

		boolean canHold = true;
		Collection specimenArrayTypes = (Collection) retrieveAttribute(
				StorageContainer.class.getName(), storageContainer.getId(),
				"elements(holdsSpecimenArrayTypeCollection)");// storageContainer.getHoldsSpArrayTypeCollection();
		// if (!specimenArrayTypes.isEmpty())
		{
			Iterator itr = specimenArrayTypes.iterator();
			canHold = false;
			while (itr.hasNext()) {
				SpecimenArrayType specimenarrayType = (SpecimenArrayType) itr
						.next();
				long arraytypeId = specimenarrayType.getId().longValue();

				if (arraytypeId == Constants.ALL_SPECIMEN_ARRAY_TYPE_ID
						|| arraytypeId == specimenArrayTypeId) {
					return true;
				}
			}
		}
		return canHold;
	}

	public Collection<SpecimenPosition> getSpecimenPositionCollForContainer(
			DAO dao, Long containerId) throws BizLogicException {
		
		try
		{
			if (containerId != null) 
			{
				List specimenPosColl = dao.retrieve(SpecimenPosition.class
						.getName(), "storageContainer.id", containerId);
				return specimenPosColl;
			}
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
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
			StorageContainer storageContainer = (StorageContainer) domainObject;
            Site site = null;
            if (storageContainer.getLocatedAtPosition() != null
                    && storageContainer.getLocatedAtPosition().getParentContainer() != null)
            {
                try
                {
                    Object object = dao.retrieveById(StorageContainer.class.getName(),
                            storageContainer.getLocatedAtPosition().getParentContainer()
                                    .getId());
                    if (object != null)
                    {
                        StorageContainer parentContainer = (StorageContainer) object;
                        site = parentContainer.getSite();
                    }
                }
                catch (DAOException e)
                {
                   return null;
                }
            }
            else
            {
            site = storageContainer.getSite();
            }
			if (site != null)
			{
				StringBuffer sb = new StringBuffer();
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
	 * @param containerId
	 * @return Site
	 */
	public Site getRelatedSite(Long containerId)throws BizLogicException
	{
		Site site = null;
		DAO dao = null;
		try 
		{
			if(containerId >=1)
			{
				dao = openDAOSession(null);
				StorageContainer storageContainer = null;
				storageContainer = (StorageContainer) dao.retrieveById(StorageContainer.class.getName(), containerId);		

				if(storageContainer!=null)
				{
					site = storageContainer.getSite();
				}
			}
		} 
		catch (DAOException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
		finally
		{
			closeDAOSession(dao);
		}
		return site;
	}
	
	/**
	 * Gives the Site Object related to given container whose name is given.
	 * @param containerName
	 * @return Site
	 */
	public Site getRelatedSiteForManual(String containerName)throws BizLogicException
	{
		Site site = null;
		DAO dao = null;
		try 
		{
			if(containerName!=null&&!("").equals(containerName))
			{
				dao = openDAOSession(null);
				StorageContainer storageContainer = null;
				String[] strArray = {containerName};
				List contList = null;


				if(strArray!=null)
				{
					contList = dao.retrieve(StorageContainer.class.getName(),Constants.NAME,containerName);
				}
				if(contList!=null && !contList.isEmpty())
				{
					storageContainer =(StorageContainer)contList.get(0);
				}


				if(storageContainer != null)
				{
					site = storageContainer.getSite();
				}
			}
		} 
		catch (DAOException e) 
		{
			throw getBizLogicException(e, "dao.error", "");
		}
		finally
		{
			closeDAOSession(dao);
		}
		return site;
	}
	
/**
 * Gives List of NameValueBean of CP for given cpIds array.
 * @param cpIds
 * @return NameValueBean
 */
	public List<NameValueBean> getCPNameValueList(long[] cpIds)throws BizLogicException
	{
		DAO dao = null;
		List<NameValueBean> cpNameValueList = new ArrayList<NameValueBean>();
		
		try 
		{
			dao = openDAOSession(null);
			NameValueBean cpNameValueBean;
			for(long cpId:cpIds)
			{
				if(cpId!=-1)
				{
					CollectionProtocol cp = new CollectionProtocol();

					cp = (CollectionProtocol) dao.retrieveById(CollectionProtocol.class.getName(), cpId);		

					String cpShortTitle = cp.getShortTitle();
					cpNameValueBean = new NameValueBean(cpShortTitle,cpId);
					cpNameValueList.add(cpNameValueBean);
				}
			}
		} 
		catch (DAOException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
		finally
		{
			closeDAOSession(dao);
		}
		
		return cpNameValueList;
	}
	
}