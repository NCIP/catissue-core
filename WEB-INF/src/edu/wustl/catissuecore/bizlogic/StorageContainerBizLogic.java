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
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.BarcodeGenerator;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.StorageContainerTreeNode;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.tree.TreeNode;
import edu.wustl.common.tree.TreeNodeImpl;
import edu.wustl.common.util.NameValueBeanRelevanceComparator;
import edu.wustl.common.util.NameValueBeanValueComparator;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * 
 * @author vaishali_khandelwal
 */
public class StorageContainerBizLogic extends DefaultBizLogic implements
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
	 * @throws DAOException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException {
		StorageContainer container = (StorageContainer) obj;
		container.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		// Setting the Parent Container if applicable
		int posOneCapacity = 1, posTwoCapacity = 1;
		int positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW, positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
		boolean fullStatus[][] = null;

		int noOfContainers = container.getNoOfContainers().intValue();

		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null) {
			Object object = dao.retrieve(StorageContainer.class.getName(),
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
				if ((noOfContainers + children.size()) > totalCapacity) {
					throw new DAOException(ApplicationProperties
							.getValue("errors.storageContainer.overflow"));
				} else {

					// Check if position specified is within the parent
					// container's
					// capacity
					if (false == validatePosition(parentContainer, container)) {
						throw new DAOException(
								ApplicationProperties
										.getValue("errors.storageContainer.dimensionOverflow"));
					}

					try {
						// check for all validations on the storage container.
						checkContainer(dao, container.getLocatedAtPosition()
								.getParentContainer().getId().toString(),
								container.getLocatedAtPosition()
										.getPositionDimensionOne().toString(),
								container.getLocatedAtPosition()
										.getPositionDimensionTwo().toString(),
								sessionDataBean, false);

					} catch (SMException sme) {
						sme.printStackTrace();
						throw handleSMException(sme);
					}

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
						throw new DAOException(
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
				throw new DAOException(ApplicationProperties
						.getValue("errors.storageContainerExist"));
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
					throw new DAOException(e.getMessage());
				}
			}
			if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerBarcodeGeneratorAvl) {
				BarcodeGenerator storagecontBarcodeGenerator;
				try {
					storagecontBarcodeGenerator = BarcodeGeneratorFactory
							.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
					// storagecontBarcodeGenerator.setBarcode(cont);
				} catch (NameGeneratorException e) {
					throw new DAOException(e.getMessage());
				}
			}
			dao.insert(cont.getCapacity(), sessionDataBean, true, true);
			dao.insert(cont, sessionDataBean, true, true);

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

			// Inserting authorization data
			Set protectionObjects = new HashSet();
			protectionObjects.add(cont);
			try {
				// SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
				// protectionObjects, getDynamicGroups(cont));

				PrivilegeManager privilegeManager = PrivilegeManager
						.getInstance();

				privilegeManager.insertAuthorizationData(null,
						protectionObjects, getDynamicGroups(cont), cont
								.getObjectId());
			} catch (SMException e) {
				throw handleSMException(e);
			}
		}

	}

	/**
	 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
	 * Description:Storage container will not be added to closed site :check for
	 * closed site
	 */
	public List getSiteList(String sourceObjectName,
			String[] displayNameFields, String valueField,
			String activityStatusArr[], boolean isToExcludeDisabled)
			throws DAOException {
		String[] whereColumnName = null;
		String[] whereColumnCondition = null;
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		whereColumnName = new String[] { "activityStatus" };
		whereColumnCondition = new String[] { "not in" };
		// whereColumnCondition = new String[]{"in"};
		Object[] whereColumnValue = { activityStatusArr };

		return getList(sourceObjectName, displayNameFields, valueField,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition, separatorBetweenFields, isToExcludeDisabled);

	}

	public List getSiteList(String[] displayNameFields, String valueField,
			String activityStatusArr[], Long userId) throws DAOException {
		List siteResultList = getSiteList(Site.class.getName(),
				displayNameFields, valueField, activityStatusArr, false);
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		List userList = null;
		try {
			dao.openSession(null);
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
		}  
		finally
		{
			dao.closeSession();
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
	 * @throws DAOException
	 */
	protected boolean isParentContainerValidToUSe(StorageContainer container,
			StorageContainer parent) throws DAOException {

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
			dynamicGroups = SecurityManager.getInstance(this.getClass())
					.getProtectionGroupByName(
							storageContainer.getLocatedAtPosition()
									.getParentContainer());
		} else {
			dynamicGroups = SecurityManager.getInstance(this.getClass())
					.getProtectionGroupByName(storageContainer.getSite());
		}
		return dynamicGroups;
	}

	public void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException {
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
	 * @throws DAOException
	 */
	protected void update(DAO dao, Object obj, Object oldObj,
			SessionDataBean sessionDataBean) throws DAOException,
			UserNotAuthorizedException {
		StorageContainer container = (StorageContainer) obj;
		StorageContainer oldContainer = (StorageContainer) oldObj;

		// lazy change
		StorageContainer persistentOldContainerForChange = null;
		Object object = dao.retrieve(StorageContainer.class.getName(),
				oldContainer.getId());
		persistentOldContainerForChange = (StorageContainer) object;

		// retrive parent container
		if (container.getLocatedAtPosition() != null) {
			StorageContainer parentStorageContainer = (StorageContainer) dao
					.retrieve(StorageContainer.class.getName(), container
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
					throw new DAOException(ApplicationProperties
							.getValue("errors.container.under.subcontainer"));
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
					throw new DAOException(
							ApplicationProperties
									.getValue("errors.storageContainer.dimensionOverflow"));
				}

				// Mandar : code added for validation bug id 666. 24-11-2005
				// start
				boolean canUse = isContainerAvailableForPositions(dao,
						container);
				Logger.out.debug("canUse : " + canUse);
				if (!canUse) {
					throw new DAOException(ApplicationProperties
							.getValue("errors.storageContainer.inUse"));
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
				// -----------------
				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = { "id",
						"capacity.oneDimensionCapacity",
						"capacity.twoDimensionCapacity" };
				String[] whereColumnName = { "id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition = { "=" };
				Object[] whereColumnValue = { container.getLocatedAtPosition()
						.getParentContainer().getId() };
				String joinCondition = null;

				List list = dao.retrieve(sourceObjectName, selectColumnName,
						whereColumnName, whereColumnCondition,
						whereColumnValue, joinCondition);

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
						throw new DAOException(
								ApplicationProperties
										.getValue("errors.storageContainer.dimensionOverflow"));
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
						throw new DAOException(ApplicationProperties
								.getValue("errors.storageContainer.inUse"));
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
			try {
				// check for all validations on the storage container.
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition()
								.getParentContainer() != null) {
					checkContainer(dao, container.getLocatedAtPosition()
							.getParentContainer().getId().toString(), container
							.getLocatedAtPosition().getPositionDimensionOne()
							.toString(), container.getLocatedAtPosition()
							.getPositionDimensionTwo().toString(),
							sessionDataBean, false);
				}
			} catch (SMException sme) {
				sme.printStackTrace();
				throw handleSMException(sme);
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
				throw new DAOException(ApplicationProperties
						.getValue("errors.storageContainer.cannotReduce"));
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
				throw new DAOException(
						ApplicationProperties
								.getValue("errros.storageContainer.restrictionCannotChanged"));
			}

		}
		Collection<SpecimenPosition> specimenPosColl = getSpecimenPositionCollForContainer(
				dao, container.getId());
		container.setSpecimenPositionCollection(specimenPosColl);
		setValuesinPersistentObject(persistentOldContainerForChange, container,
				dao);

		dao.update(persistentOldContainerForChange, sessionDataBean, true,
				true, false);
		dao.update(persistentOldContainerForChange.getCapacity(),
				sessionDataBean, true, true, false);
		// Audit of update of storage container.
		dao.audit(obj, oldObj, sessionDataBean, true);
		dao.audit(container.getCapacity(), oldContainer.getCapacity(),
				sessionDataBean, true);

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
				Constants.ACTIVITY_STATUS_DISABLED)) {
			Long containerIDArr[] = { container.getId() };
			if (isContainerAvailableForDisabled(dao, containerIDArr)) {
				List disabledConts = new ArrayList();

				/**
				 * Preapare list of parent/child containers to disable
				 * 
				 */
				List<StorageContainer> disabledContainerList = new ArrayList<StorageContainer>();
				disabledContainerList.add(persistentOldContainerForChange);
				persistentOldContainerForChange.setLocatedAtPosition(null);

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

				persistentOldContainerForChange.setLocatedAtPosition(null);

				dao.update(persistentOldContainerForChange, sessionDataBean,
						true, true, false);

				try {
					CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
							.getInstance();
					catissueCoreCacheManager.addObjectToCache(
							Constants.MAP_OF_DISABLED_CONTAINERS,
							(Serializable) disabledConts);
				} catch (CacheException e) {

				}

			} else {
				throw new DAOException(ApplicationProperties
						.getValue("errors.container.contains.specimen"));
			}
		}

	}

	public void setValuesinPersistentObject(StorageContainer persistentobject,
			StorageContainer newObject, DAO dao) throws DAOException {
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
			SessionDataBean sessionDataBean) throws BizLogicException,
			UserNotAuthorizedException {
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
					Constants.ACTIVITY_STATUS_DISABLED)) {
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
			throw new BizLogicException(e.getMessage(), e);
		}
	}

	/*
	 * public boolean isContainerFull(String containerId, int dimX, int dimY)
	 * throws DAOException {
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

	protected void setPrivilege(DAO dao, String privilegeName,
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
			throws Exception {
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
			userName = SecurityManager.getInstance(
					StorageContainerBizLogic.class).getUserById(
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
				throw new DAOException(
						"Error : First de-assign privilege of the Parent Site with system identifier "
								+ row[1].toString());
			} else if (permission == true && row[0] != null)// If the parent is
															// a storage
															// container.
			{
				throw new DAOException(
						"Error : First de-assign privilege of the Parent Container with system identifier "
								+ row[0].toString());
			}
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
	 * @throws DAOException
	 */
	private void assignPrivilegeToSubStorageContainer(DAO dao,
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

	/**
	 * @param dao
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @throws DAOException
	 * @throws SMException
	 */
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
	}

	// This method sets the Storage Type & Site (if applicable) of this
	// container.
	protected void loadSite(DAO dao, StorageContainer container)
			throws DAOException {
		Site site = container.getSite();
		// Setting the site if applicable
		if (site != null) {
			// Commenting dao.retrive() call as retrived object is not realy
			// required for further processing -Prafull
			Site siteObj = (Site) dao.retrieve(Site.class.getName(), container
					.getSite().getId());

			if (siteObj != null) {

				// check for closed site
				checkStatus(dao, siteObj, "Site");

				container.setSite(siteObj);
				setSiteForSubContainers(container, siteObj, dao);
			}
		}
	}

	protected void loadStorageType(DAO dao, StorageContainer container)
			throws DAOException {
		// Setting the Storage Type
		Object storageTypeObj = dao.retrieve(StorageType.class.getName(),
				container.getStorageType().getId());
		if (storageTypeObj != null) {
			StorageType type = (StorageType) storageTypeObj;
			container.setStorageType(type);
		}
	}

	private void setSiteForSubContainers(StorageContainer storageContainer,
			Site site, DAO dao) throws DAOException {
		// Added storageContainer.getId()!=null check as this method fails in
		// case when it gets called from insert(). -PRafull

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

	private boolean isUnderSubContainer(StorageContainer storageContainer,
			Long parentContainerID, DAO dao) throws DAOException {
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
		return false;
	}

	// TODO TO BE REMOVED
	private void setDisableToSubContainer(StorageContainer storageContainer,
			List disabledConts, DAO dao, List disabledContainerList)
			throws DAOException {
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

				container.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
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

	// This method is called from labelgenerator.
	public long getNextContainerNumber() throws DAOException {
		String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
		String[] selectColumnName = { "max(IDENTIFIER) as MAX_NAME" };
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);

		dao.openSession(null);

		List list = dao.retrieve(sourceObjectName, selectColumnName);

		dao.closeSession();
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

	// what to do abt thi
	public String getContainerName(String siteName, String typeName,
			String operation, long Id) throws DAOException {
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
			boolean isInSite) throws DAOException {
		String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
		String[] selectColumnName = { "max(IDENTIFIER) as MAX_NAME" };
		String[] whereColumnName = { "STORAGE_TYPE_ID", "PARENT_CONTAINER_ID" };
		String[] whereColumnCondition = { "=", "=" };
		Object[] whereColumnValue = { Long.valueOf(typeID),
				Long.valueOf(parentID) };

		if (isInSite) {
			whereColumnName = new String[3];
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
			whereColumnCondition[2] = "is";
		}
		String joinCondition = Constants.AND_JOIN_CONDITION;

		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);

		dao.openSession(null);

		List list = dao.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);

		dao.closeSession();

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

	private boolean isContainerEmpty(DAO dao, StorageContainer container)
			throws DAOException {

		// Retrieving all the occupied positions by child containers
		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = { "locatedAtPosition.positionDimensionOne",
				"locatedAtPosition.positionDimensionTwo" };
		String[] whereColumnName = { "locatedAtPosition.parentContainer.id" };
		String[] whereColumnCondition = { "=" };
		Object[] whereColumnValue = { container.getId() };

		List list = dao.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue, null);

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
			list = dao.retrieve(sourceObjectName, selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue,
					null);

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

				list = dao.retrieve(sourceObjectName, selectColumnName,
						whereColumnName, whereColumnCondition,
						whereColumnValue, null);

				if (!list.isEmpty()) 
				{
					return false;
				}

			}

		}

		return true;

	}

	/**
	 * Returns the data for generation of storage container tree view.
	 * 
	 * @return the vector of tree nodes for the storage containers.
	 */
	public Vector getTreeViewData() throws DAOException {

		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		dao.openSession(null);

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
		List list = null;

		try {
			list = dao.executeQuery(queryStr, null, false, null);
			// printRecords(list);
		} catch (Exception ex) {
			throw new DAOException(ex.getMessage());
		}

		dao.closeSession();

		return getTreeNodeList(list);
	}

	/**
	 * Returns the vector of tree node for the storage container list.
	 * 
	 * @param resultList
	 *            the storage container list.
	 * @return the vector of tree node for the storage container list.
	 * @throws DAOException
	 */
	public Vector getTreeNodeList(List resultList) throws DAOException {
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
								.equals(Constants.ACTIVITY_STATUS_DISABLED)) {
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
	 * @throws DAOException
	 */
	private Vector createHierarchy(Map containerRelationMap,
			Vector treeNodeVector) throws DAOException {

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
			Vector tempChildNodeList = parentTreeNodeImpl.getChildNodes();
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
				Vector tempChildNodeList = siteNode.getChildNodes();
				siteNode.setChildNodes(tempChildNodeList);
			}
		}

		// Get the containers under site.
		Vector containersUnderSite = getContainersUnderSite();
		containersUnderSite.removeAll(parentNodeVector);
		parentNodeVector.addAll(containersUnderSite);
		Utility.sortTreeVector(parentNodeVector);
		return parentNodeVector;
	}

	private Vector getContainersUnderSite() throws DAOException {
		// String sql = " SELECT sc.IDENTIFIER, sc.CONTAINER_NAME, scType.TYPE,
		// site.IDENTIFIER, site.NAME, site.TYPE "
		// + " from catissue_storage_container sc, catissue_site site,
		// catissue_storage_type scType "
		// + " where sc.SITE_ID = site.IDENTIFIER AND sc.STORAGE_TYPE_ID =
		// scType.IDENTIFIER "
		// + " and sc.PARENT_CONTAINER_ID is NULL";

		String sql = "SELECT sc.IDENTIFIER, cn.NAME, scType.NAME, site.IDENTIFIER,"
				+ "site.NAME, site.TYPE from catissue_storage_container sc, "
				+ "catissue_site site, catissue_container_type scType, "
				+ "catissue_container cn where sc.SITE_ID = site.IDENTIFIER "
				+ "AND sc.STORAGE_TYPE_ID = scType.IDENTIFIER "
				+ "and sc.IDENTIFIER = cn.IDENTIFIER "
				+ "and cn.IDENTIFIER not in (select pos.CONTAINER_ID from catissue_container_position pos)";

		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		List resultList = new ArrayList();
		Vector containerNodeVector = new Vector();

		try {
			dao.openSession(null);
			resultList = dao.executeQuery(sql, null, false, null);
			dao.closeSession();
			// System.out.println("\nIn getContainersUnderSite()\n ");
			printRecords(resultList);
		} catch (Exception daoExp) {
			throw new DAOException(daoExp.getMessage(), daoExp);
		}

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

		return containerNodeVector;
	}

	/**
	 * Returns the site tree node of the container with the given identifier.
	 * 
	 * @param identifier
	 *            the identifier of the container.
	 * @return the site tree node of the container with the given identifier.
	 * @throws DAOException
	 */
	private TreeNodeImpl getSiteTreeNode(Long identifier) throws DAOException {
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
	public Vector getSiteWithDummyContainer(Long userId) throws DAOException {
		String sql = "SELECT site.IDENTIFIER, site.NAME,COUNT(site.NAME) FROM CATISSUE_SITE "
				+ " site join CATISSUE_STORAGE_CONTAINER sc ON sc.site_id = site.identifier join "
				+ "CATISSUE_CONTAINER con ON con.identifier = sc.identifier WHERE con.ACTIVITY_STATUS!='Disabled' "
				+ "GROUP BY site.IDENTIFIER, site.NAME"
				+" order by upper(site.NAME)";
				

		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		List resultList = new ArrayList();
		Long nodeIdentifier;
		String nodeName = null;
		String dummyNodeName = null;

		Vector containerNodeVector = new Vector();
		try {
			dao.openSession(null);
			resultList = dao.executeQuery(sql, null, false, null);
			dao.closeSession();
		} catch (Exception daoExp) {
			throw new DAOException(daoExp.getMessage(), daoExp);
		}

		Iterator iterator = resultList.iterator();
		AbstractDAO hibernateDao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		hibernateDao.openSession(null);
		Set<Long> siteIdSet = new UserBizLogic().getRelatedSiteIds(userId);
		hibernateDao.closeSession();

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
	 * @throws DAOException
	 * @Description This method will retrieve all the containers under the
	 *              selected node
	 */
	public Vector<StorageContainerTreeNode> getStorageContainers(
			Long identifier, String nodeName, String parentId)
			throws DAOException {
		String sql = createSql(identifier, parentId);
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		String dummyNodeName = Constants.DUMMY_NODE_NAME;
		String containerName = null;
		Long nodeIdentifier;
		Long parentContainerId;
		Long childCount;

		List resultList = new ArrayList();
		Vector<StorageContainerTreeNode> containerNodeVector = new Vector<StorageContainerTreeNode>();
		try {
			dao.openSession(null);
			resultList = dao.executeQuery(sql, null, false, null);
			dao.closeSession();
		} catch (Exception daoExp) {
			throw new DAOException(daoExp.getMessage(), daoExp);
		}
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
	private String createSql(Long identifier, String parentId) {
		String sql;
		if (!Constants.ZERO_ID.equals(parentId)) {
			sql = "SELECT cn.IDENTIFIER, cn.name, pos.PARENT_CONTAINER_ID,count(sc3.IDENTIFIER) "
					+ "FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc ON sc.IDENTIFIER=cn.IDENTIFIER "
					+ "left outer join catissue_container_position pos on pos.CONTAINER_ID=cn.IDENTIFIER left outer join "
					+ "catissue_container_position con_pos on con_pos.PARENT_CONTAINER_ID=cn.IDENTIFIER left outer join "
					+ "CATISSUE_STORAGE_CONTAINER sc3 on con_pos.CONTAINER_ID=sc3.IDENTIFIER "
					+ "WHERE pos.PARENT_CONTAINER_ID= "
					+ identifier
					+ " AND cn.ACTIVITY_STATUS!='Disabled' GROUP BY cn.IDENTIFIER, cn.NAME,pos.PARENT_CONTAINER_ID";
		} else {
			sql = "SELECT cn.IDENTIFIER, cn.NAME,site.identifier,COUNT(sc3.IDENTIFIER) "
					+ "FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc "
					+ "ON sc.IDENTIFIER=cn.IDENTIFIER join CATISSUE_SITE site "
					+ "ON sc.site_id = site.identifier left outer join CATISSUE_CONTAINER_POSITION pos "
					+ "ON pos.PARENT_CONTAINER_ID=cn.IDENTIFIER left outer join "
					+ "CATISSUE_STORAGE_CONTAINER sc3 ON pos.CONTAINER_ID=sc3.IDENTIFIER "
					+ "WHERE site.identifier="
					+ identifier
					+ " AND cn.ACTIVITY_STATUS!='Disabled' AND cn.IDENTIFIER NOT IN (SELECT p2.CONTAINER_ID FROM CATISSUE_CONTAINER_POSITION p2) "
					+ "GROUP BY cn.IDENTIFIER, cn.NAME,site.identifier ";

		}
		return sql;
	}

	private boolean[][] getStorageContainerFullStatus(DAO dao,
			StorageContainer parentContainer, Collection children)
			throws DAOException {
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
	 * @throws DAOException
	 */
	public Collection getContainerChildren(Long containerId)
			throws DAOException {
		AbstractDAO dao = DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);

		Collection<Container> children = null;

		try {
			dao.openSession(null);
			children = StorageContainerUtil.getChildren(dao, containerId);
		} catch (DAOException daoExp) {
			daoExp.printStackTrace();
			Logger.out.error(daoExp.getMessage(), daoExp);
		} finally {
			dao.closeSession();
		}
		return children;
	}

	private void disableSubStorageContainer(DAO dao,
			SessionDataBean sessionDataBean,
			List<StorageContainer> disabledContainerList) throws DAOException,
			UserNotAuthorizedException {

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
				"specimenPosition.storageContainer", Utility
						.toLongArray(containerIdList));

		if (!listOfSpecimenIDs.isEmpty()) {
			throw new DAOException(ApplicationProperties
					.getValue("errors.container.contains.specimen"));
		}
		// Uodate containers to disabled
		for (int i = 0; i < count; i++) {
			StorageContainer container = disabledContainerList.get(i);
			dao.update(container, sessionDataBean, true, true, false);
		}

		auditDisabledObjects(dao, "CATISSUE_CONTAINER", containerIdList);
	}

	private void disableSubStorageContainer(DAO dao,
			SessionDataBean sessionDataBean, Long storageContainerIDArr[])
			throws DAOException, UserNotAuthorizedException {

		// adding updated participantMap to cache
		// catissueCoreCacheManager.addObjectToCache(Constants.MAP_OF_PARTICIPANTS,
		// participantMap);
		List listOfSpecimenIDs = getRelatedObjects(dao, Specimen.class,
				"specimenPosition.storageContainer", storageContainerIDArr);

		if (!listOfSpecimenIDs.isEmpty()) {
			throw new DAOException(ApplicationProperties
					.getValue("errors.container.contains.specimen"));
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

				Object object = dao.retrieve(sourceObjectName, contId);
				if (object != null) {
					StorageContainer cont = (StorageContainer) object;

					// cont.setParent(null);

					cont.setLocatedAtPosition(null);
					// dao.update(cont, sessionDataBean, true, true, false);
				}

			}
		}

		disableSubStorageContainer(dao, sessionDataBean, Utility
				.toLongArray(listOfSubStorageContainerId));
	}

	// Checks for whether the user is trying to use a container without
	// privilege to use it
	// This is needed since now users can enter the values in the edit box
	public boolean validateContainerAccess(DAO dao, StorageContainer container,
			SessionDataBean sessionDataBean) throws SMException {
		Logger.out.debug("validateContainerAccess..................");
		String userName = sessionDataBean.getUserName();

		if(sessionDataBean.isAdmin())
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
	 * @throws DAOException
	 *             exception occured while DB handling
	 */
	private boolean validatePosition(DAO dao, StorageContainer container)
			throws DAOException {
		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = { "id", "capacity.oneDimensionCapacity",
				"capacity.twoDimensionCapacity" };
		String[] whereColumnName = { "id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
		String[] whereColumnCondition = { "=" };
		Object[] whereColumnValue = { container.getLocatedAtPosition()
				.getParentContainer().getId() };
		String joinCondition = null;

		List list = dao.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);
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

				List list = dao.retrieve(sourceObjectName, selectColumnName,
						whereColumnName1, whereColumnCondition1,
						whereColumnValue1, joinCondition);
				// check if Specimen exists with the given storageContainer
				// information
				if (list.size() != 0) {
					Object obj = list.get(0);
					return false;
				} else {
					sourceObjectName = SpecimenArray.class.getName();
					whereColumnName1[0] = "locatedAtPosition.parentContainer.id";
					list = dao.retrieve(sourceObjectName, selectColumnName,
							whereColumnName1, whereColumnCondition1,
							whereColumnValue1, joinCondition);
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

		return isContainerAvailableForDisabled(dao, Utility
				.toLongArray(containerList));
	}

	// -- to check if storageContainer is available or used
	protected boolean isContainerAvailableForPositions(DAO dao,
			StorageContainer current) {
		try {
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = { "id" };
			String[] whereColumnName = {
					"locatedAtPosition.positionDimensionOne",
					"locatedAtPosition.positionDimensionTwo",
					"locatedAtPosition.parentContainer" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = { "=", "=", "=" };
			Object[] whereColumnValue = {
					current.getLocatedAtPosition().getPositionDimensionOne(),
					current.getLocatedAtPosition().getPositionDimensionTwo(),
					current.getLocatedAtPosition().getParentContainer() };
			String joinCondition = Constants.AND_JOIN_CONDITION;

			List list = dao.retrieve(sourceObjectName, selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue,
					joinCondition);
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
				String[] whereColumnName1 = {
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
								.getId() };

				list = dao.retrieve(sourceObjectName, selectColumnName,
						whereColumnName1, whereColumnCondition1,
						whereColumnValue1, joinCondition);
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
					whereColumnName1[0] = "locatedAtPosition.positionDimensionOne";
					whereColumnName1[1] = "locatedAtPosition.positionDimensionTwo";
					whereColumnName1[2] = "locatedAtPosition.parentContainer.id";
					list = dao.retrieve(sourceObjectName, selectColumnName,
							whereColumnName1, whereColumnCondition1,
							whereColumnValue1, joinCondition);
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
			StorageContainer storageContainer, String posOne, String posTwo) {
		try {
			String sourceObjectName = Specimen.class.getName();
			String[] selectColumnName = { "id" };
			String[] whereColumnName = {
					"specimenPosition.positionDimensionOne",
					"specimenPosition.positionDimensionTwo",
					"specimenPosition.storageContainer.id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = { "=", "=", "=" };
			Object[] whereColumnValue = { Integer.valueOf(posOne), Integer.valueOf(posTwo),
					storageContainer.getId() };
			String joinCondition = Constants.AND_JOIN_CONDITION;

			List list = dao.retrieve(sourceObjectName, selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue,
					joinCondition);
			Logger.out.debug("storageContainer.getId() :"
					+ storageContainer.getId());
			// check if Specimen exists with the given storageContainer
			// information
			if (list.size() != 0) {
				Object obj = list.get(0);
				Logger.out
						.debug("**************IN isPositionAvailable : obj::::::: --------------- "
								+ obj);
				// Logger.out.debug((Long)obj[0] );
				// Logger.out.debug((Integer)obj[1]);
				// Logger.out.debug((Integer )obj[2]);

				return false;
			} else {
				sourceObjectName = StorageContainer.class.getName();
				String[] whereColumnName1 = {
						"locatedAtPosition.positionDimensionOne",
						"locatedAtPosition.positionDimensionTwo",
						"locatedAtPosition.parentContainer.id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition1 = { "=", "=", "=" };
				Object[] whereColumnValue1 = { Integer.valueOf(posOne), Integer.valueOf(posTwo),
						storageContainer.getId() };

				list = dao.retrieve(sourceObjectName, selectColumnName,
						whereColumnName1, whereColumnCondition1,
						whereColumnValue1, joinCondition);
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
					String[] whereColumnName2 = {
							"locatedAtPosition.positionDimensionOne",
							"locatedAtPosition.positionDimensionTwo",
							"locatedAtPosition.parentContainer.id" };
					String[] whereColumnCondition2 = { "=", "=", "=" };
					Object[] whereColumnValue2 = { Integer.valueOf(posOne), Integer.valueOf(posTwo),
							storageContainer.getId() };

					list = dao.retrieve(sourceObjectName, selectColumnName,
							whereColumnName2, whereColumnCondition2,
							whereColumnValue2, joinCondition);
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
			SessionDataBean sessionDataBean, boolean multipleSpecimen)
			throws DAOException, SMException {
		// List list = dao.retrieve(StorageContainer.class.getName(),
		// "id",storageContainerID );

		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = { Constants.SYSTEM_IDENTIFIER,
				"capacity.oneDimensionCapacity",
				"capacity.twoDimensionCapacity", "name" };
		String[] whereColumnName = { Constants.SYSTEM_IDENTIFIER };
		String[] whereColumnCondition = { "=" };
		Object[] whereColumnValue = { Long.valueOf(storageContainerID) };
		String joinCondition = Constants.AND_JOIN_CONDITION;

		List list = dao.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);

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
				throw new DAOException(ApplicationProperties
						.getValue("access.use.object.denied"));
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
						positionTwo);
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
						throw new DAOException(
								ApplicationProperties
										.getValue("errors.storageContainer.Multiple.inUse"));
					} else {
						throw new DAOException(ApplicationProperties
								.getValue("errors.storageContainer.inUse"));
					}
				}
			} else
			// position is invalid
			{
				throw new DAOException(ApplicationProperties
						.getValue("errors.storageContainer.dimensionOverflow"));
			}
		} else
		// storageContainer does not exist
		{
			throw new DAOException(ApplicationProperties
					.getValue("errors.storageContainerExist"));
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
			throws DAOException {
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
			throw new DAOException("domain.object.null.err.msg");
		Validator validator = new Validator();
		if (container.getStorageType() == null) {
			message = ApplicationProperties.getValue("storageContainer.type");
			throw new DAOException(ApplicationProperties.getValue(
					"errors.item.required", message));

		}
		if (container.getNoOfContainers() == null) {
			Integer conts = new Integer(1);
			container.setNoOfContainers(conts);

		}
		if (validator.isEmpty(container.getNoOfContainers().toString())) {
			message = ApplicationProperties
					.getValue("storageContainer.noOfContainers");
			throw new DAOException(ApplicationProperties.getValue(
					"errors.item.required", message));
		}
		if (!validator.isNumeric(container.getNoOfContainers().toString(), 1)) {
			message = ApplicationProperties
					.getValue("storageContainer.noOfContainers");
			throw new DAOException(ApplicationProperties.getValue(
					"errors.item.format", message));
		}

		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() == null) {
			if (container.getSite() == null
					|| container.getSite().getId() == null
					|| container.getSite().getId() <= 0) {
				message = ApplicationProperties
						.getValue("storageContainer.site");
				throw new DAOException(ApplicationProperties.getValue(
						"errors.item.invalid", message));
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
			throw new DAOException(ApplicationProperties.getValue(
					"errors.item.format", message));

		}

		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null) {

			if (container.getLocatedAtPosition().getParentContainer().getId() == null) {
				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = { "id" };
				String[] whereColumnName = { "name" };
				String[] whereColumnCondition = { "=" };
				Object[] whereColumnValue = { container.getLocatedAtPosition()
						.getParentContainer().getName() };
				String joinCondition = null;

				List list = dao.retrieve(sourceObjectName, selectColumnName,
						whereColumnName, whereColumnCondition,
						whereColumnValue, joinCondition);

				if (!list.isEmpty()) {
					container.getLocatedAtPosition().getParentContainer()
							.setId((Long) list.get(0));
				} else {
					String message1 = ApplicationProperties
							.getValue("specimen.storageContainer");
					throw new DAOException(ApplicationProperties.getValue(
							"errors.invalid", message1));
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
					throw new DAOException(
							"The Storage Container you specified is full");
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
				throw new DAOException(ApplicationProperties.getValue(
						"errors.item.required", message));
			} else {
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition()
								.getPositionDimensionOne() != null
						&& !validator.isNumeric(String.valueOf(container
								.getLocatedAtPosition()
								.getPositionDimensionOne()))) {
					message = ApplicationProperties
							.getValue("storageContainer.oneDimension");
					throw new DAOException(ApplicationProperties.getValue(
							"errors.item.format", message));
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
				throw new DAOException(ApplicationProperties.getValue(
						"errors.item.format", message));

			}

		}
		if (operation.equals(Constants.ADD)) {
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(container
					.getActivityStatus())) {
				throw new DAOException(ApplicationProperties
						.getValue("activityStatus.active.errMsg"));
			}

			if (container.isFull().booleanValue()) {
				throw new DAOException(ApplicationProperties
						.getValue("storageContainer.isContainerFull.errMsg"));
			}
		} else {
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,
					container.getActivityStatus())) {
				throw new DAOException(ApplicationProperties
						.getValue("activityStatus.errMsg"));
			}
		}

		return true;
	}

	// TODO Write the proper business logic to return an appropriate list of
	// containers.
	public List getStorageContainerList() throws DAOException {
		String sourceObjectName = StorageContainer.class.getName();
		String[] displayNameFields = { Constants.SYSTEM_IDENTIFIER };
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List list = getList(sourceObjectName, displayNameFields, valueField,
				true);
		return list;
	}

	public List getCollectionProtocolList() throws DAOException {
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
	 * @throws DAOException
	 */
	public boolean[][] getAvailablePositionsForContainer(String containerId,
			int dimX, int dimY) throws DAOException {
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
	 * @throws DAOException
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
	 * @throws DAOException
	 */

	public Map getAvailablePositionMapForContainer(String containerId,
			int aliquotCount, String positionDimensionOne,
			String positionDimensionTwo) throws DAOException {
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
	 * @throws DAOException
	 */
	// public Map getAvailablePositionMap(String containerId, int aliquotCount)
	// throws DAOException
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
	 * @throws DAOException
	 */
	public Map getAllocatedContainerMap() throws DAOException {
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
			throws DAOException {
		if (container != null) {
			Long sysId = container.getId();
			Object object = dao.retrieve(StorageContainer.class.getName(),
					sysId);
			// System.out.println("siteIdList " + siteIdList);
			StorageContainer sc = (StorageContainer) object;
			// System.out.println("siteId " + sc.getSite().getId());
			container.setSite(sc.getSite());
		}
	}

	public TreeMap getAllocatedContaienrMapForContainer(long type_id,
			String exceedingMaxLimit, String selectedContainerName)
			throws DAOException {
		long start = 0;
		long end = 0;
		TreeMap containerMap = new TreeMap();
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);

		dao.openSession(null);
		start = System.currentTimeMillis();
		String queryStr = "SELECT t1.IDENTIFIER, t1.NAME FROM CATISSUE_CONTAINER t1 WHERE "
				+ "t1.IDENTIFIER IN (SELECT t4.STORAGE_CONTAINER_ID FROM CATISSUE_ST_CONT_ST_TYPE_REL t4 "
				+ "WHERE t4.STORAGE_TYPE_ID = '"
				+ type_id
				+ "' OR t4.STORAGE_TYPE_ID='1') AND "
				+ "t1.ACTIVITY_STATUS='"
				+ Constants.ACTIVITY_STATUS_ACTIVE + "' order by IDENTIFIER";

		Logger.out.debug("Storage Container query......................"
				+ queryStr);
		List list = new ArrayList();

		try {
			list = dao.executeQuery(queryStr, null, false, null);
		} catch (Exception ex) {
			throw new DAOException(ex.getMessage());
		}

		end = System.currentTimeMillis();
		System.out.println("Time taken for executing query : " + (end - start));
		dao.closeSession();

		Map containerMapFromCache = null;

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

	}

	/* temp function end */

	public TreeMap getAllocatedContaienrMapForSpecimen(long cpId,
			String specimenClass, int aliquotCount, String exceedingMaxLimit,
			SessionDataBean sessionData, boolean closeSession)
			throws DAOException {

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
					try {
						AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
						dao.openSession(null);
						hasAccess = validateContainerAccess(dao,sc, sessionData);
						dao.closeSession();
					} catch (SMException sme) {
						sme.printStackTrace();
						throw handleSMException(sme);
					}
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

	/**
	 * This function gets the list of container in order of there relvance.
	 * 
	 * @param cpId
	 *            collection protocol Id
	 * @param specimenClass
	 *            class of the specimen
	 * @param closeSession
	 * @return list of containers in order of there relevence.
	 * @throws DAOException
	 * @author Vaishali
	 */
	public List getRelevantContainerList(long cpId, String specimenClass,
			boolean closeSession) throws DAOException {
		List list = new ArrayList();
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		dao.openSession(null);
		String[] queryArray = new String[6];
		// category # 1
		// Gets all container which stores just specified collection protocol
		// and specified specimen class
		String equalToOne = " = 1 ";
		String greaterThanOne = " > 1 ";

		String equalToFour = " = 4 ";
		String notEqualToFour = " !=4 ";
		String endQry = " and t1.IDENTIFIER = t6.STORAGE_CONTAINER_ID  "
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

	/**
	 * This function executes the query
	 * 
	 * @param query
	 * @param dao
	 * @return
	 * @throws DAOException
	 */
	public List executeStorageContQuery(String query, JDBCDAO dao)
			throws DAOException {
		Logger.out.debug("Storage Container query......................"
				+ query);
		List list = new ArrayList();

		try {
			list = dao.executeQuery(query, null, false, null);
		} catch (Exception ex) {
			throw new DAOException(ex.getMessage());
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
	 * @throws DAOException --
	 *             throws DAO Exception
	 * @see edu.wustl.common.dao.JDBCDAOImpl
	 */
	public TreeMap getAllocatedContaienrMapForSpecimenArray(
			long specimen_array_type_id, int noOfAliqoutes,
			SessionDataBean sessionData, String exceedingMaxLimit)
			throws DAOException {
		NameValueBeanValueComparator contComp = new NameValueBeanValueComparator();
		TreeMap containerMap = new TreeMap(contComp);

		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		dao.openSession(null);
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

		try {
			list = dao.executeQuery(queryStr, null, false, null);
		} catch (Exception ex) {
			throw new DAOException(ex.getMessage());
		}

		dao.closeSession();
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

	// --------------Code for Map Mandar: 04-Sep-06 start
	// Mandar : 29Aug06 : for StorageContainerMap
	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws DAOException
	 */
	public List getCollectionProtocolList(String id) throws DAOException {
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
	 * @throws DAOException
	 */
	public List getSpecimenClassList(String id) throws DAOException {

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
	 * @throws DAOException
	 */
	private List executeSQL(String sql) throws DAOException {
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
				Constants.JDBC_DAO);
		List resultList = new ArrayList();
		try {
			dao.openSession(null);
			resultList = dao.executeQuery(sql, null, false, null);
			dao.closeSession();
		} catch (Exception daoExp) {
			throw new DAOException(daoExp.getMessage(), daoExp);
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
	private String getToolTipData(String containerID) throws DAOException {
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
			NameValueBean key1 = new NameValueBean(key.getName(), key
					.getValue());
			List value = (ArrayList) positionMap.get(key);
			List value1 = new ArrayList();
			Iterator itr1 = value.iterator();

			while (itr1.hasNext()) {
				NameValueBean ypos = (NameValueBean) itr1.next();
				NameValueBean ypos1 = new NameValueBean(ypos.getName(), ypos
						.getValue());
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
	 * @throws DAOException
	 *             Exception occured while DB handling
	 */
	private Site getSite(DAO dao, Long containerId) throws DAOException {
		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = new String[] { "site" };
		String[] whereColumnName = new String[] { "id" }; // "storageContainer."+Constants.SYSTEM_IDENTIFIER
		String[] whereColumnCondition = new String[] { "=" };
		Object[] whereColumnValue = new Long[] { containerId };
		String joinCondition = null;

		List list = dao.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);

		if (!list.isEmpty()) {
			return ((Site) list.get(0));
		}
		return null;
	}

	/**
	 * Name : kalpana thakur Reviewer Name : Vaishali Bug ID: 4922
	 * Description:Storage container will not be added to closed site :check for
	 * closed site
	 */
	public void checkClosedSite(DAO dao, Long containerId, String errMessage)
			throws DAOException {

		Site site = getSite(dao, containerId);

		// check for closed Site
		if (site != null) {
			if (Constants.ACTIVITY_STATUS_CLOSED.equals(site
					.getActivityStatus())) {
				throw new DAOException(errMessage + " "
						+ ApplicationProperties.getValue("error.object.closed"));
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
	 * @throws DAOException
	 */
	public long[] getDefaultHoldCollectionProtocolList(
			StorageContainer container) throws DAOException {
		Collection spcimenArrayTypeCollection = (Collection) retrieveAttribute(
				StorageContainer.class.getName(), container.getId(),
				"elements(collectionProtocolCollection)");
		if (spcimenArrayTypeCollection.isEmpty()) {
			return new long[] { -1 };
		} else {
			return Utility.getobjectIds(spcimenArrayTypeCollection);
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
	 * @throws DAOException
	 */
	public boolean canHoldContainerType(int typeId,
			StorageContainer storageContainer) throws DAOException {
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
	 * @throws DAOException
	 */
	public boolean isValidContaierType(int typeID) throws DAOException {
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
	 * @throws DAOException
	 */
	public boolean canHoldCollectionProtocol(long collectionProtocolId,
			StorageContainer storageContainer) throws DAOException {
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
	 * @throws DAOException
	 */
	public boolean canHoldSpecimenClass(String specimenClass,
			StorageContainer storageContainer) throws DAOException {
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
			StorageContainer storageContainer) throws DAOException {

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
			DAO dao, Long containerId) throws DAOException {
		if (containerId != null) {
			List specimenPosColl = dao.retrieve(SpecimenPosition.class
					.getName(), "storageContainer.id", containerId);
			return specimenPosColl;
		}
		return null;
	}
	
	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject) 
	{
		if (domainObject instanceof StorageContainer)
			
		{
			StorageContainer storageContainer = (StorageContainer) domainObject;
			Site site = storageContainer.getSite();
			if (site != null)
			{
				StringBuffer sb = new StringBuffer();
				sb.append(Site.class.getName()).append("_").append(site.getId().toString());
				return sb.toString(); 
			}
		}
		return Constants.ADMIN_PROTECTION_ELEMENT;
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
}