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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.StorageContainerTreeNode;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.tree.TreeNode;
import edu.wustl.common.tree.TreeNodeImpl;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * @author aniruddha_phadnis
 */
public class StorageContainerBizLogic extends DefaultBizLogic implements TreeDataInterface
{
	/**
	 * Saves the storageContainer object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		StorageContainer container = (StorageContainer) obj;
		container.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		//Setting the Parent Container if applicable
		int posOneCapacity = 1, posTwoCapacity = 1;
		int positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW, positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
		boolean fullStatus[][] = null;
		
	if(container.getSimilarContainerMap() == null)
	{		
		int noOfContainers = container.getNoOfContainers().intValue();

		if (container.getParentContainer() != null)
		{
			List list = dao.retrieve(StorageContainer.class.getName(), "systemIdentifier",
					container.getParentContainer().getSystemIdentifier());

			if (list.size() != 0)
			{
				StorageContainer pc = (StorageContainer) list.get(0);

				// check for closed ParentContainer
				checkStatus(dao, pc, "Parent Container");

				int totalCapacity = pc.getStorageContainerCapacity().getOneDimensionCapacity()
						.intValue()
						* pc.getStorageContainerCapacity().getTwoDimensionCapacity().intValue();
				if ((noOfContainers + pc.getChildrenContainerCollection().size()) > totalCapacity)
				{
					throw new DAOException(ApplicationProperties
							.getValue("errors.storageContainer.overflow"));
				}
				else
				{

					//Check if position specified is within the parent
					// container's
					//capacity  
					if (false == validatePosition(pc, container))
					{
						throw new DAOException(ApplicationProperties
								.getValue("errors.storageContainer.dimensionOverflow"));
					}

					// check for availability of position
					boolean canUse = isContainerAvailable(dao, container);

					if (!canUse)
					{
						throw new DAOException(ApplicationProperties
								.getValue("errors.storageContainer.inUse"));
					}

					container.setParentContainer(pc);

					// check for closed ParentSite
					checkStatus(dao, pc.getSite(), "Parent Site");

					container.setSite(pc.getSite());

					posOneCapacity = pc.getStorageContainerCapacity().getOneDimensionCapacity()
							.intValue();
					posTwoCapacity = pc.getStorageContainerCapacity().getTwoDimensionCapacity()
							.intValue();

					fullStatus = getStorageContainerFullStatus(dao, container.getParentContainer()
							.getSystemIdentifier());
					positionDimensionOne = container.getPositionDimensionOne().intValue();
					positionDimensionTwo = container.getPositionDimensionTwo().intValue();
				}
			}
			else
			{
				throw new DAOException(ApplicationProperties
						.getValue("errors.storageContainerExist"));
			}
		}
		else
		{
			loadSite(dao, container);
		}

		loadStorageType(dao, container);
		for (int i = 0; i < noOfContainers; i++)
		{
			StorageContainer cont = new StorageContainer(container);
			if (cont.getParentContainer() != null)
			{
				cont.setPositionDimensionOne(new Integer(positionDimensionOne));
				cont.setPositionDimensionTwo(new Integer(positionDimensionTwo));
			}

			
			Logger.out.debug("Collection protocol size:" + container.getCollectionProtocolCollection().size());

			dao.insert(cont.getStorageContainerCapacity(), sessionDataBean, true, true);
			dao.insert(cont, sessionDataBean, true, true);

			//Used for showing the success message after insert and using it
			// for edit.
			container.setSystemIdentifier(cont.getSystemIdentifier());

			if (container.getParentContainer() != null)
			{
				Logger.out.debug("In if: ");
				do
				{
					if (positionDimensionTwo == posTwoCapacity)
					{
						if (positionDimensionOne == posOneCapacity)
							positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW;
						else
							positionDimensionOne = (positionDimensionOne + 1)
									% (posOneCapacity + 1);

						positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
					}
					else
					{
						positionDimensionTwo = positionDimensionTwo + 1;
					}

					Logger.out.debug("positionDimensionTwo: " + positionDimensionTwo);
					Logger.out.debug("positionDimensionOne: " + positionDimensionOne);
				}
				while (fullStatus[positionDimensionOne][positionDimensionTwo] != false);
			}

			//Inserting authorization data
			Set protectionObjects = new HashSet();
			protectionObjects.add(cont);
			try
			{
				SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
						protectionObjects, getDynamicGroups(cont));
			}
			catch (SMException e)
			{
				throw handleSMException(e);
			}
		}
	 }else // if similarContainerMap is not null
     {
     	int noOfContainers = container.getNoOfContainers().intValue();
     	Map simMap = container.getSimilarContainerMap();
     	// --- common values for all similar containers ---
     	loadStorageType(dao, container);
     	Logger.out.debug("cont.getCollectionProtocolCollection().size()  "+container.getCollectionProtocolCollection().size());
     	Logger.out.debug("container.getParentContainer() site id -->>()<<-- "+container.getParentContainer()); //.getSite().getSystemIdentifier()
     	Logger.out.debug("Container siteId "+container.getSite());
     	int checkButton = Integer.parseInt((String)simMap.get("checkedButton"));
     	for(int i = 1; i <= noOfContainers; i++)
     	{
     		String simContPrefix = "simCont:"+i+"_";
     		String contName = (String) simMap.get(simContPrefix+"name");
     		String barcode = (String)  simMap.get(simContPrefix+"barcode");
     		StorageContainer cont = new StorageContainer(container);
     		if(checkButton == 1)  // site
     		{
     			String  siteId = (String)simMap.get(simContPrefix+"siteId");
     			Site site = new Site();
     			site.setSystemIdentifier(new Long(siteId));
     			cont.setSite(site);
     			loadSite(dao, cont);    // <<----
     			
     		}else  // parentContainer
     		{
     			String parentId = (String) simMap.get(simContPrefix+"parentContainerId");
     			String posOne = (String) simMap.get(simContPrefix+"positionDimensionOne");
     			String posTwo = (String) simMap.get(simContPrefix+"positionDimensionTwo");
     			
     			
     			StorageContainer parentContainer = new StorageContainer();
 				parentContainer.setSystemIdentifier(new Long(parentId));
 				parentContainer.setPositionDimensionOne(new Integer(posOne));
 				parentContainer.setPositionDimensionTwo(new Integer(posTwo));
     			cont.setParentContainer(parentContainer);  // <<----
     			// Have to set Site object for parentContainer
     			loadSite(dao,parentContainer);                         // 17-07-2006
     			loadSiteFromContainerId(dao,parentContainer);
     			cont.setPositionDimensionOne(new Integer(posOne));
     			cont.setPositionDimensionTwo(new Integer(posTwo));
     			cont.setSite(parentContainer.getSite());             // 16-07-2006 chetan
     			Logger.out.debug("^^>> "+parentContainer.getSite());
     		}
     		//StorageContainer cont = new StorageContainer();
     		cont.setName(contName);     // <<----
     		cont.setBarcode(barcode);   // <<----     		
     		
     		Logger.out.debug(cont.getParentContainer()+" <<<<---- parentContainer");
     		Logger.out.debug("cont.getCollectionProtocol().size() "+cont.getCollectionProtocolCollection().size());
     		dao.insert(cont.getStorageContainerCapacity(), sessionDataBean, true, true);
     		dao.insert(cont, sessionDataBean, true, true);
     		
     		container.setSystemIdentifier(cont.getSystemIdentifier());
     		
     		//        		Inserting authorization data
        	 	Set protectionObjects = new HashSet();
        	 	protectionObjects.add(cont);
        	 	try
        	 	{
        	 		SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
						protectionObjects, getDynamicGroups(cont));
        	 	}
				catch (SMException e)
				{
					throw handleSMException(e);
				}
     	}     	
     }
	}

	//	public Set getProtectionObjects(AbstractDomainObject obj)
	//    {
	//        Set protectionObjects = new HashSet();
	//        
	//        StorageContainer storageContainer = (StorageContainer) obj;
	//        protectionObjects.add(storageContainer);
	//        
	//        Iterator storageContainerIterator = storageContainer.getChildrenContainerCollection().iterator();
	//        while (storageContainerIterator.hasNext())
	//        {
	//            StorageContainer childContainer = (StorageContainer) storageContainerIterator.next();
	//            protectionObjects.add(childContainer);
	//        }
	//        
	//        Logger.out.debug(protectionObjects.toString());
	//        return protectionObjects;
	//    }

	private String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
	{
		String[] dynamicGroups = null;
		StorageContainer storageContainer = (StorageContainer) obj;

		if (storageContainer.getParentContainer() != null)
		{
			dynamicGroups = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(
					storageContainer.getParentContainer());
		}
		else
		{
			dynamicGroups = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(
					storageContainer.getSite());
		}
		return dynamicGroups;
	}

	/**
	 * Updates the persistent object in the database.
	 * 
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		StorageContainer container = (StorageContainer) obj;
		StorageContainer oldContainer = (StorageContainer) oldObj;
		Logger.out.debug("container.isParentChanged() : " + container.isParentChanged());

		if (container.isParentChanged())
		{
			if (container.getParentContainer() != null)
			{
				//Check whether continer is moved to one of its sub container.
				if (isUnderSubContainer(container, container.getParentContainer()
						.getSystemIdentifier()))
				{
					throw new DAOException(ApplicationProperties
							.getValue("errors.container.under.subcontainer"));
				}
				Logger.out.debug("Loading ParentContainer: "
						+ container.getParentContainer().getSystemIdentifier());

				StorageContainer pc = (StorageContainer) dao.retrieve(StorageContainer.class
						.getName(), container.getParentContainer().getSystemIdentifier());

				/* Check if position specified is within the parent container's capacity*/
				if (false == validatePosition(pc, container))
				{
					throw new DAOException(ApplicationProperties
							.getValue("errors.storageContainer.dimensionOverflow"));
				}

				// Mandar : code added for validation bug id 666. 24-11-2005  start
				boolean canUse = isContainerAvailable(dao, container);
				Logger.out.debug("canUse : " + canUse);
				if (!canUse)
				{
					throw new DAOException(ApplicationProperties
							.getValue("errors.storageContainer.inUse"));
				}
				// Mandar : code added for validation bug id 666. 24-11-2005 end

				//check for closed ParentContainer
				checkStatus(dao, pc, "Parent Container");

				container.setParentContainer(pc);
				//check for closed Site
				checkStatus(dao, pc.getSite(), "Parent Site");

				container.setSite(pc.getSite());
			}
		}
		// Mandar : code added for validation 25-11-05-----------
		else
		// if parent container is not changed only the position is changed.
		{
			if (container.isPositionChanged())
			{
				// -----------------
				String sourceObjectName = StorageContainer.class.getName();
				String[] selectColumnName = {"systemIdentifier",
						"storageContainerCapacity.oneDimensionCapacity",
						"storageContainerCapacity.twoDimensionCapacity"};
				String[] whereColumnName = {"systemIdentifier"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition = {"="};
				Object[] whereColumnValue = {container.getParentContainer().getSystemIdentifier()};
				String joinCondition = null;

				List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName,
						whereColumnCondition, whereColumnValue, joinCondition);

				if (!list.isEmpty())
				{
					Object[] obj1 = (Object[]) list.get(0);
					Logger.out.debug("**************PC obj::::::: --------------- " + obj1);
					Logger.out.debug((Long) obj1[0]);
					Logger.out.debug((Integer) obj1[1]);
					Logger.out.debug((Integer) obj1[2]);

					Integer pcCapacityOne = (Integer) obj1[1];
					Integer pcCapacityTwo = (Integer) obj1[2];

					if (!validatePosition(pcCapacityOne.intValue(), pcCapacityTwo.intValue(),
							container))
					{
						throw new DAOException(ApplicationProperties
								.getValue("errors.storageContainer.dimensionOverflow"));
					}
				}
				else
				{

				}
				// -----------------
				//            	StorageContainer pc = (StorageContainer) dao.retrieve(StorageContainer.class.getName(), container.getParentContainer().getSystemIdentifier());

				//        		if(!validatePosition(container.getParentContainer().getStorageContainerCapacity().getOneDimensionCapacity().intValue(),
				//          			container.getParentContainer().getStorageContainerCapacity().getTwoDimensionCapacity().intValue(),
				//					container))
				//	           	 /*Check if position specified is within the parent container's capacity*/
				////	           	if(!validatePosition(pc,container))
				//	           	{
				//	           		throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.dimensionOverflow"));
				//	           	}
				//
				/**
				 * Only if parentContainerID, positionOne or positionTwo is changed
				 *  check for availability of position
				 */
				Logger.out
						.debug("oldContainer.getParentContainer().getSystemIdentifier().longValue() : "
								+ oldContainer.getParentContainer().getSystemIdentifier()
										.longValue());
				Logger.out
						.debug("container.getParentContainer().getSystemIdentifier().longValue() : "
								+ container.getParentContainer().getSystemIdentifier().longValue());
				Logger.out.debug("oldContainer.getPositionDimensionOne().intValue() : "
						+ oldContainer.getPositionDimensionOne().intValue());
				Logger.out.debug("container.getPositionDimensionOne().intValue() : "
						+ container.getPositionDimensionOne().intValue());
				Logger.out.debug("oldContainer.getPositionDimensionTwo().intValue() : "
						+ oldContainer.getPositionDimensionTwo().intValue());
				Logger.out.debug("container.getPositionDimensionTwo().intValue() : "
						+ container.getPositionDimensionTwo().intValue());

				if (oldContainer.getPositionDimensionOne().intValue() != container
						.getPositionDimensionOne().intValue()
						|| oldContainer.getPositionDimensionTwo().intValue() != container
								.getPositionDimensionTwo().intValue())
				{
					boolean canUse = isContainerAvailable(dao, container);
					Logger.out.debug("canUse : " + canUse);
					if (!canUse)
					{
						throw new DAOException(ApplicationProperties
								.getValue("errors.storageContainer.inUse"));
					}
				}

			}
		}

		// Mandar : --------- end  25-11-05 -----------------		

		//Check whether size has been reduced
		//Sri: fix for bug #355 (Storage capacity: Reducing capacity should be
		// handled)

		Integer oldContainerDimOne = oldContainer.getStorageContainerCapacity()
				.getOneDimensionCapacity();
		Integer oldContainerDimTwo = oldContainer.getStorageContainerCapacity()
				.getTwoDimensionCapacity();
		Integer newContainerDimOne = container.getStorageContainerCapacity()
				.getOneDimensionCapacity();
		Integer newContainerDimTwo = container.getStorageContainerCapacity()
				.getTwoDimensionCapacity();

		// If any size of reduced, throw error
		if ((oldContainerDimOne.compareTo(newContainerDimOne) > 0)
				|| (oldContainerDimTwo.compareTo(newContainerDimTwo) > 0))
		{
			throw new DAOException(ApplicationProperties
					.getValue("errors.storageContainer.cannotReduce"));
		}

		//Check for closed Site
		if ((container.getSite() != null) && (oldContainer.getSite() != null))
		{
			if ((container.getSite().getSystemIdentifier() != null)
					&& (oldContainer.getSite().getSystemIdentifier() != null))
			{
				if ((!container.getSite().getSystemIdentifier().equals(
						oldContainer.getSite().getSystemIdentifier())))
				{
					checkStatus(dao, container.getSite(), "Site");
				}
			}
		}
		setSiteForSubContainers(container, container.getSite());

		dao.update(container, sessionDataBean, true, true, false);

		//Audit of update of storage container.
		dao.audit(obj, oldObj, sessionDataBean, true);
		dao.audit(container.getStorageContainerCapacity(), oldContainer
				.getStorageContainerCapacity(), sessionDataBean, true);

		/*Collection storageContainerDetailsCollection = container
		 .getStorageContainerDetailsCollection();*/

		/*        Collection oldStorageContainerDetailsCollection = oldContainer
		 .getStorageContainerDetailsCollection();
		 Iterator it = storageContainerDetailsCollection.iterator();
		 while (it.hasNext())
		 {
		 StorageContainerDetails storageContainerDetails = (StorageContainerDetails) it
		 .next();
		 storageContainerDetails.setStorageContainer(container);
		 dao.update(storageContainerDetails, sessionDataBean, true, true,
		 false);
		 StorageContainerDetails oldStorageContainerDetails = (StorageContainerDetails) getCorrespondingOldObject(
		 oldStorageContainerDetailsCollection,
		 storageContainerDetails.getSystemIdentifier());

		 dao.audit(storageContainerDetails, oldStorageContainerDetails,
		 sessionDataBean, true);
		 }*/

		Logger.out.debug("container.getActivityStatus() " + container.getActivityStatus());
		if (container.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			setDisableToSubContainer(container);
			Logger.out.debug("container.getActivityStatus() " + container.getActivityStatus());
			Long containerIDArr[] = {container.getSystemIdentifier()};

			disableSubStorageContainer(dao, containerIDArr);
		}
	}

	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds,
			Long userId, String roleId, boolean assignToUser, boolean assignOperation)
			throws SMException, DAOException
	{
		Logger.out.debug(" privilegeName:" + privilegeName + " objectType:" + objectType
				+ " objectIds:" + edu.wustl.common.util.Utility.getArrayString(objectIds)
				+ " userId:" + userId + " roleId:" + roleId + " assignToUser:" + assignToUser);

		if (assignOperation == Constants.PRIVILEGE_DEASSIGN)
		{
			isDeAssignable(dao, privilegeName, objectIds, userId, roleId, assignToUser);
		}

		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser,
				assignOperation);

		assignPrivilegeToSubStorageContainer(dao, privilegeName, objectIds, userId, roleId,
				assignToUser, assignOperation);
	}

	/**
	 * Checks whether the user/role has privilege on the parent (Container/Site). If the user has privilege
	 * an exception is thrown stating to deassign the privilege of parent first. 
	 * @param dao The dao object to get the related objects down the hierarchy. 
	 * @param objectIds The objects ids of containerwhose parent is to be checked.
	 * @param privilegeName The privilege name.
	 * @param userId The user identifier.
	 * @param roleId The roleId in case privilege is assigned/deassigned to a role.
	 * @param assignToUser boolean which determines whether privilege is assigned/deassigned to a user or role. 
	 * @throws DAOException
	 * @throws SMException
	 */
	private void isDeAssignable(DAO dao, String privilegeName, Long[] objectIds, Long userId,
			String roleId, boolean assignToUser) throws DAOException, SMException
	{
		String[] selectColumnNames = {"parentContainer.systemIdentifier", "site.systemIdentifier"};
		String[] whereColumnNames = {"systemIdentifier"};
		List listOfSubElement = super.getRelatedObjects(dao, StorageContainer.class,
				selectColumnNames, whereColumnNames, objectIds);

		Logger.out.debug("Related Objects>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
				+ listOfSubElement.size());

		String userName = new String();
		if (assignToUser == true)
		{
			userName = SecurityManager.getInstance(StorageContainerBizLogic.class).getUserById(
					userId.toString()).getLoginName();
		}

		Iterator iterator = listOfSubElement.iterator();
		while (iterator.hasNext())
		{
			Object[] row = (Object[]) iterator.next();

			//Parent storage container identifier.
			Object containerObject = (Object) row[0];
			String className = StorageContainer.class.getName();

			// Parent storage container identifier is null, the parent is a site..
			if ((row[0] == null) || (row[0].equals("")))
			{
				containerObject = row[1];
				className = Site.class.getName();
			}

			Logger.out.debug("Container Object After ********************** : " + containerObject
					+ "row[1] : " + row[1]);

			boolean permission = false;
			//Check the permission on the parent container or site.
			if (assignToUser == true)//If the privilege is assigned/deassigned to a user.
			{
				permission = SecurityManager.getInstance(StorageContainerBizLogic.class)
						.checkPermission(userName, className, containerObject.toString(),
								privilegeName);
			}
			else
			//If the privilege is assigned/deassigned to a user group.
			{
				permission = SecurityManager.getInstance(StorageContainerBizLogic.class)
						.checkPermission(roleId, className, containerObject.toString());
			}

			//If the parent is a Site.
			if (permission == true && row[0] == null)
			{
				throw new DAOException(
						"Error : First de-assign privilege of the Parent Site with system identifier "
								+ row[1].toString());
			}
			else if (permission == true && row[0] != null)//If the parent is a storage container. 
			{
				throw new DAOException(
						"Error : First de-assign privilege of the Parent Container with system identifier "
								+ row[0].toString());
			}
		}
	}

	/**
	 * Assigns the privilege to all the sub-containers down the hierarchy.
	 * @param dao The dao object to get the related objects down the hierarchy. 
	 * @param privilegeName The privilege name.
	 * @param storageContainerIDArr The storage container id array.
	 * @param userId The user identifier.
	 * @param roleId The roleId in case privilege is assigned/deassigned to a role.
	 * @param assignToUser boolean which determines whether privilege is assigned/deassigned to a user or role. 
	 * @param assignOperation boolean which determines assign/deassign.
	 * @throws SMException
	 * @throws DAOException
	 */
	private void assignPrivilegeToSubStorageContainer(DAO dao, String privilegeName,
			Long[] storageContainerIDArr, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		//Get list of sub container identifiers.
		List listOfSubStorageContainerId = super.getRelatedObjects(dao, StorageContainer.class,
				"parentContainer", storageContainerIDArr);

		if (listOfSubStorageContainerId.isEmpty())
			return;

		super.setPrivilege(dao, privilegeName, StorageContainer.class, Utility
				.toLongArray(listOfSubStorageContainerId), userId, roleId, assignToUser,
				assignOperation);

		assignPrivilegeToSubStorageContainer(dao, privilegeName, Utility
				.toLongArray(listOfSubStorageContainerId), userId, roleId, assignToUser,
				assignOperation);
	}

	/**
	 * @param dao
	 * @param objectIds
	 * @param assignToUser
	 * @param roleId
	 * @throws DAOException
	 * @throws SMException
	 */
	public void assignPrivilegeToRelatedObjectsForSite(DAO dao, String privilegeName,
			Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, StorageContainer.class, "site",
				objectIds);

		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, StorageContainer.class, Utility
					.toLongArray(listOfSubElement), userId, roleId, assignToUser, assignOperation);
		}
	}

	// This method sets the Storage Type & Site (if applicable) of this
	// container.
	private void loadSite(DAO dao, StorageContainer container) throws DAOException
	{
		//Setting the site if applicable
		if (container.getSite() != null)
		{
			Object siteObj = dao.retrieve(Site.class.getName(), container.getSite()
					.getSystemIdentifier());
			if (siteObj != null)
			{
				Site site = (Site) siteObj;

				// check for closed site
				checkStatus(dao, site, "Site");

				container.setSite(site);
				setSiteForSubContainers(container, site);
			}
		}
	}

	private void loadStorageType(DAO dao, StorageContainer container) throws DAOException
	{
		//Setting the Storage Type
		Object storageTypeObj = dao.retrieve(StorageType.class.getName(), container
				.getStorageType().getSystemIdentifier());
		if (storageTypeObj != null)
		{
			StorageType type = (StorageType) storageTypeObj;
			container.setStorageType(type);
		}
	}

	private void setSiteForSubContainers(StorageContainer storageContainer, Site site)
	{
		if (storageContainer != null)
		{
			Logger.out.debug("site() " + site.getSystemIdentifier());
			Logger.out.debug("storageContainer.getChildrenContainerCollection() "
					+ storageContainer.getChildrenContainerCollection().size());

			Iterator iterator = storageContainer.getChildrenContainerCollection().iterator();
			while (iterator.hasNext())
			{
				StorageContainer container = (StorageContainer) iterator.next();
				container.setSite(site);
				setSiteForSubContainers(container, site);
			}
		}
	}

	private boolean isUnderSubContainer(StorageContainer storageContainer, Long parentContainerID)
	{
		if (storageContainer != null)
		{
			Iterator iterator = storageContainer.getChildrenContainerCollection().iterator();
			while (iterator.hasNext())
			{
				StorageContainer container = (StorageContainer) iterator.next();
				//Logger.out.debug("SUB CONTINER container
				// "+parentContainerID.longValue()+"
				// "+container.getSystemIdentifier().longValue()+"
				// "+(parentContainerID.longValue()==container.getSystemIdentifier().longValue()));
				if (parentContainerID.longValue() == container.getSystemIdentifier().longValue())
					return true;
				if (isUnderSubContainer(container, parentContainerID))
					return true;
			}
		}
		return false;
	}

	//  TODO TO BE REMOVED
	private void setDisableToSubContainer(StorageContainer storageContainer)
	{
		if (storageContainer != null)
		{
			Iterator iterator = storageContainer.getChildrenContainerCollection().iterator();
			while (iterator.hasNext())
			{
				StorageContainer container = (StorageContainer) iterator.next();
				container.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				setDisableToSubContainer(container);
			}
		}
	}

	public long getNextContainerNumber() throws DAOException
	{
		String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};
		AbstractDAO dao = DAOFactory.getDAO(Constants.JDBC_DAO);

		dao.openSession(null);

		List list = dao.retrieve(sourceObjectName, selectColumnName);

		dao.closeSession();

		if (!list.isEmpty())
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

	
	public String getContainerName(String siteName,String typeName) throws DAOException
	{
		String containerName="";
		if(!typeName.equals("")&& !siteName.equals(""))
		{
			containerName=siteName+"_"+typeName+"_"+String.valueOf(getNextContainerNumber());
			
		}
		return containerName;
	}
	
	public int getNextContainerNumber(long parentID, long typeID, boolean isInSite)
			throws DAOException
	{
		String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
		String[] selectColumnName = {"max(IDENTIFIER) as MAX_NAME"};
		String[] whereColumnName = {"STORAGE_TYPE_ID", "PARENT_CONTAINER_ID"};
		String[] whereColumnCondition = {"=", "="};
		Object[] whereColumnValue = {Long.toString(typeID), Long.toString(parentID)};

		if (isInSite)
		{
			whereColumnName = new String[3];
			whereColumnName[0] = "STORAGE_TYPE_ID";
			whereColumnName[1] = "SITE_ID";
			whereColumnName[2] = "PARENT_CONTAINER_ID";

			whereColumnValue = new Object[3];
			whereColumnValue[0] = Long.toString(typeID);
			whereColumnValue[1] = Long.toString(parentID);
			whereColumnValue[2] = "null";

			whereColumnCondition = new String[3];
			whereColumnCondition[0] = "=";
			whereColumnCondition[1] = "=";
			whereColumnCondition[2] = "is";
		}
		String joinCondition = Constants.AND_JOIN_CONDITION;

		AbstractDAO dao = DAOFactory.getDAO(Constants.JDBC_DAO);

		dao.openSession(null);

		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);

		dao.closeSession();

		if (!list.isEmpty())
		{
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				Logger.out.info("str---------------:" + str);
				if (!str.equals(""))
				{
					int no = Integer.parseInt(str);
					return no + 1;
				}
			}
		}

		return 1;
	}

	//    private Map createMap(List resultSet, Map containerMap)
	//    {
	//        Map containerRelationMap = new HashMap();
	//        Map siteContainerRelationMap = new HashMap();
	//        
	//        if (resultSet.isEmpty() == false)
	//        {
	//            for (int i = 0; i < resultSet.size(); i++)
	//            {
	//                List rowList = (List) resultSet.get(i);
	//                
	//                StorageContainerTreeNode treeNode = new StorageContainerTreeNode();
	//                treeNode.setStorageContainerIdentifier(Long.valueOf((String) rowList.get(1)));
	//                treeNode.setStorageContainerName((String) rowList.get(6));
	//                treeNode.setStorageContainerType((String) rowList.get(5));
	//                
	//                treeNode.setSiteSystemIdentifier(Long.valueOf((String) rowList.get(2)));
	//                treeNode.setSiteName((String) rowList.get(3));
	//                treeNode.setSiteType((String) rowList.get(4));
	//                
	//                if ((String) rowList.get(0) != "") // if parent is null in db
	//                {
	//                    treeNode.setParentStorageContainerIdentifier(Long.valueOf((String) rowList.get(0)));
	//                    siteContainerRelationMap.put(Long.valueOf((String) rowList.get(2)), 
	//                            					 Long.valueOf((String) rowList.get(1)));
	//                }
	//                
	//                containerRelationMap.put(treeNode.getStorageContainerIdentifier(),
	//                        				 treeNode.getParentStorageContainerIdentifier());
	//                containerMap.put(treeNode.getStorageContainerIdentifier(), treeNode);
	//                
	//                Logger.out.debug("\n");
	//            }
	//        }
	//        
	//        return containerRelationMap;
	//    }
	/**
	 * Returns the data for generation of storage container tree view.
	 * @return the vector of tree nodes for the storage containers. 
	 */
	public Vector getTreeViewData() throws DAOException
	{

		JDBCDAO dao = (JDBCDAO) DAOFactory.getDAO(Constants.JDBC_DAO);
		dao.openSession(null);

		String queryStr = " SELECT t8.IDENTIFIER, t8.CONTAINER_NAME, t5.TYPE, t8.SITE_ID, "
				+ " t4.TYPE, t8.PARENT_IDENTIFIER, "
				+ " t8.PARENT_CONTAINER_NAME, t8.PARENT_CONTAINER_TYPE "
				+ " FROM (SELECT t7.IDENTIFIER, t7.CONTAINER_NAME, t7.SITE_ID, "
				+ " t7.STORAGE_TYPE_ID, t7.PARENT_IDENTIFIER, "
				+ " t7.PARENT_CONTAINER_NAME, t6.TYPE AS PARENT_CONTAINER_TYPE FROM "
				+ " (select t1.IDENTIFIER AS IDENTIFIER, t1.CONTAINER_NAME AS CONTAINER_NAME, "
				+ " t1.SITE_ID AS SITE_ID, t1.STORAGE_TYPE_ID AS STORAGE_TYPE_ID, "
				+ " t2.IDENTIFIER AS PARENT_IDENTIFIER, t2.CONTAINER_NAME AS PARENT_CONTAINER_NAME, "
				+ " t2.STORAGE_TYPE_ID AS PARENT_STORAGE_TYPE_ID "
				+ " from CATISSUE_STORAGE_CONTAINER t1 LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER t2 "
				+ " on t1.PARENT_CONTAINER_ID = t2.IDENTIFIER) AS t7 LEFT OUTER JOIN CATISSUE_STORAGE_TYPE t6 "
				+ " on t7.PARENT_STORAGE_TYPE_ID = t6.IDENTIFIER) AS t8, "
				+ " CATISSUE_SITE t4, CATISSUE_STORAGE_TYPE t5 "
				+ " WHERE t8.SITE_ID = t4.IDENTIFIER " + " AND t8.STORAGE_TYPE_ID = t5.IDENTIFIER ";

		Logger.out.debug("Storage Container query......................" + queryStr);
		List list = null;

		try
		{
			list = dao.executeQuery(queryStr, null, false, null);
		}
		catch (Exception ex)
		{
			throw new DAOException(ex.getMessage());
		}

		dao.closeSession();

		return getTreeNodeList(list);
	}

	/**
	 * Returns the vector of tree node for the storage container list.
	 * @param resultList the storage container list.
	 * @return the vector of tree node for the storage container list.
	 * @throws DAOException
	 */
	public Vector getTreeNodeList(List resultList) throws DAOException
	{
		Map containerRelationMap = new HashMap();

		// Vector of Tree Nodes for all the storage containers.
		Vector treeNodeVector = new Vector();
		Vector finalNodeVector = new Vector();

		if (resultList.isEmpty() == false)
		{
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext())
			{
				List rowList = (List) iterator.next();
				if ((String) rowList.get(5) != "") //if parent container is not null
				{
					List childIds = new ArrayList();

					// Create the relationship map for parent container id and the child container ids.
					// Check if the parent container already has an entry in the Map and get it.
					if (containerRelationMap.containsKey(Long.valueOf((String) rowList.get(5))))
					{
						childIds = (List) containerRelationMap.get(Long.valueOf((String) rowList
								.get(5)));
					}

					// Put the container in the child container list of the parent container
					// and update the Map. 
					childIds.add(Long.valueOf((String) rowList.get(0)));
					containerRelationMap.put(Long.valueOf((String) rowList.get(5)), childIds);

					// Create the tree node for the child node.
					TreeNode treeNodeImpl = new StorageContainerTreeNode(Long
							.valueOf((String) rowList.get(0)), (String) rowList.get(1),
							(String) rowList.get(2));

					// Add the tree node in the Vector if it is not present.
					if (treeNodeVector.contains(treeNodeImpl) == false)
					{
						treeNodeVector.add(treeNodeImpl);
					}

					// Create the tree node for the parent node and add it in the vector if not present.
					treeNodeImpl = new StorageContainerTreeNode(Long.valueOf((String) rowList
							.get(5)), (String) rowList.get(6), (String) rowList.get(7));
					if (treeNodeVector.contains(treeNodeImpl) == false)
					{
						treeNodeVector.add(treeNodeImpl);
					}
				}
			}

			finalNodeVector = createHierarchy(containerRelationMap, treeNodeVector);
		}

		return finalNodeVector;
	}

	/**
	 * Creates the hierarchy of the tree nodes of the container according to the container relationship map.
	 * @param containerRelationMap the container relationship map.
	 * @param treeNodeVector the vector of tree nodes.
	 * @return the hierarchy of the tree nodes of the container according to the container relationship map.
	 * @throws DAOException
	 */
	private Vector createHierarchy(Map containerRelationMap, Vector treeNodeVector)
			throws DAOException
	{

		//Get the ket set of the parent containers.
		Set keySet = containerRelationMap.keySet();
		Iterator iterator = keySet.iterator();

		while (iterator.hasNext())
		{
			//Get the parent container id and create the tree node. 
			Long parentId = (Long) iterator.next();
			StorageContainerTreeNode parentTreeNodeImpl = new StorageContainerTreeNode(parentId,
					null, null);
			parentTreeNodeImpl = (StorageContainerTreeNode) treeNodeVector.get(treeNodeVector
					.indexOf(parentTreeNodeImpl));

			//Get the child container ids and create the tree nodes.
			List childNodeList = (List) containerRelationMap.get(parentId);
			Iterator childIterator = childNodeList.iterator();
			while (childIterator.hasNext())
			{
				Long childId = (Long) childIterator.next();
				StorageContainerTreeNode childTreeNodeImpl = new StorageContainerTreeNode(childId,
						null, null);
				childTreeNodeImpl = (StorageContainerTreeNode) treeNodeVector.get(treeNodeVector
						.indexOf(childTreeNodeImpl));

				// Set the relationship between the parent and child tree nodes.
				childTreeNodeImpl.setParentNode(parentTreeNodeImpl);
				parentTreeNodeImpl.getChildNodes().add(childTreeNodeImpl);
			}
		}

		//Get the container whose tree node has parent null 
		//and get its site tree node and set it as its child.
		Vector parentNodeVector = new Vector();
		iterator = treeNodeVector.iterator();
		while (iterator.hasNext())
		{
			StorageContainerTreeNode treeNodeImpl = (StorageContainerTreeNode) iterator.next();
			if (treeNodeImpl.getParentNode() == null)
			{
				TreeNodeImpl siteNode = getSiteTreeNode(treeNodeImpl.getIdentifier());
				if (parentNodeVector.contains(siteNode))
				{
					siteNode = (TreeNodeImpl) parentNodeVector.get(parentNodeVector
							.indexOf(siteNode));
				}

				treeNodeImpl.setParentNode(siteNode);
				siteNode.getChildNodes().add(treeNodeImpl);
				parentNodeVector.add(siteNode);
			}
		}

		//Get the containers under site.
		Vector containersUnderSite = getContainersUnderSite();
		containersUnderSite.removeAll(parentNodeVector);
		parentNodeVector.addAll(containersUnderSite);

		return parentNodeVector;
	}

	private Vector getContainersUnderSite() throws DAOException
	{
		String sql = " SELECT sc.IDENTIFIER, sc.CONTAINER_NAME, scType.TYPE, site.IDENTIFIER, site.NAME, site.TYPE "
				+ " from catissue_storage_container sc, catissue_site site, catissue_storage_type scType "
				+ " where sc.SITE_ID = site.IDENTIFIER AND sc.STORAGE_TYPE_ID = scType.IDENTIFIER "
				+ " and sc.PARENT_CONTAINER_ID is NULL";

		JDBCDAO dao = (JDBCDAO) DAOFactory.getDAO(Constants.JDBC_DAO);
		List resultList = new ArrayList();
		Vector containerNodeVector = new Vector();

		try
		{
			dao.openSession(null);
			resultList = dao.executeQuery(sql, null, false, null);
			dao.closeSession();
		}
		catch (Exception daoExp)
		{
			throw new DAOException(daoExp.getMessage(), daoExp);
		}

		Iterator iterator = resultList.iterator();
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			StorageContainerTreeNode containerNode = new StorageContainerTreeNode(Long
					.valueOf((String) rowList.get(0)), (String) rowList.get(1), (String) rowList
					.get(2));
			StorageContainerTreeNode siteNode = new StorageContainerTreeNode(Long
					.valueOf((String) rowList.get(3)), (String) rowList.get(4), (String) rowList
					.get(5));

			containerNode.setParentNode(siteNode);
			siteNode.getChildNodes().add(containerNode);

			containerNodeVector.add(siteNode);
		}

		return containerNodeVector;
	}

	/**
	 * Returns the site tree node of the container with the given identifier.
	 * @param identifier the identifier of the container.
	 * @return the site tree node of the container with the given identifier.
	 * @throws DAOException
	 */
	private TreeNodeImpl getSiteTreeNode(Long identifier) throws DAOException
	{
		String sql = "SELECT site.IDENTIFIER, site.NAME, site.TYPE "
				+ " from catissue_storage_container sc, catissue_site site "
				+ " where sc.SITE_ID = site.IDENTIFIER AND sc.IDENTIFIER = "
				+ identifier.longValue();

		Logger.out.debug("Site Query........................." + sql);

		JDBCDAO dao = (JDBCDAO) DAOFactory.getDAO(Constants.JDBC_DAO);
		List resultList = new ArrayList();

		try
		{
			dao.openSession(null);
			resultList = dao.executeQuery(sql, null, false, null);
			dao.closeSession();
		}
		catch (Exception daoExp)
		{
			throw new DAOException(daoExp.getMessage(), daoExp);
		}

		TreeNodeImpl siteTreeNode = null;
		if (resultList.isEmpty() == false)
		{
			List siteRecord = (List) resultList.get(0);
			siteTreeNode = new StorageContainerTreeNode(Long.valueOf((String) siteRecord.get(0)),
					(String) siteRecord.get(1), (String) siteRecord.get(2));
		}

		return siteTreeNode;
	}

	public boolean[][] getStorageContainerFullStatus(DAO dao, Long systemIdentifier)
			throws DAOException
	{
		List list = dao.retrieve(StorageContainer.class.getName(), "systemIdentifier",
				systemIdentifier);
		boolean[][] fullStatus = null;

		if (!list.isEmpty())
		{
			StorageContainer storageContainer = (StorageContainer) list.get(0);
			Integer oneDimensionCapacity = storageContainer.getStorageContainerCapacity()
					.getOneDimensionCapacity();
			Integer twoDimensionCapacity = storageContainer.getStorageContainerCapacity()
					.getTwoDimensionCapacity();
			fullStatus = new boolean[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];

			if (storageContainer.getChildrenContainerCollection() != null)
			{
				Iterator iterator = storageContainer.getChildrenContainerCollection().iterator();
				Logger.out.debug("storageContainer.getChildrenContainerCollection().size(): "
						+ storageContainer.getChildrenContainerCollection().size());
				while (iterator.hasNext())
				{
					StorageContainer childStorageContainer = (StorageContainer) iterator.next();
					Integer positionDimensionOne = childStorageContainer.getPositionDimensionOne();
					Integer positionDimensionTwo = childStorageContainer.getPositionDimensionTwo();
					Logger.out.debug("positionDimensionOne : " + positionDimensionOne.intValue());
					Logger.out.debug("positionDimensionTwo : " + positionDimensionTwo.intValue());
					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = true;
				}
			}
		}
		return fullStatus;
	}

	private void disableSubStorageContainer(DAO dao, Long storageContainerIDArr[])
			throws DAOException
	{
		List listOfSpecimenIDs = getRelatedObjects(dao, Specimen.class, "storageContainer",
				storageContainerIDArr);
		if (!listOfSpecimenIDs.isEmpty())
		{
			throw new DAOException(ApplicationProperties
					.getValue("errors.container.contains.specimen"));
		}

		List listOfSubStorageContainerId = super.disableObjects(dao, StorageContainer.class,
				"parentContainer", "CATISSUE_STORAGE_CONTAINER", "PARENT_CONTAINER_ID",
				storageContainerIDArr);

		if (listOfSubStorageContainerId.isEmpty())
			return;

		disableSubStorageContainer(dao, Utility.toLongArray(listOfSubStorageContainerId));
	}

	// Checks for whether the user is trying to use a container without privilege to use it
	// This is needed since now users can enter the values in the edit box
	private boolean validateContainerAccess(StorageContainer container,
			SessionDataBean sessionDataBean) throws SMException
	{
		Logger.out.debug("validateContainerAccess..................");
		String userName = sessionDataBean.getUserName();
		if (!SecurityManager.getInstance(this.getClass()).isAuthorized(userName,
				StorageContainer.class.getName() + "_" + container.getId(), Permissions.USE))
		{
			return false;
		}

		else
			return true;
	}

	// Checks for whether the user is trying to place the container in a
	// position
	// outside the range of parent container
	// This is needed since now users can enter the values in the edit box
	private boolean validatePosition(StorageContainer parent, StorageContainer current)
	{
		int posOneCapacity = parent.getStorageContainerCapacity().getOneDimensionCapacity()
				.intValue();
		int posTwoCapacity = parent.getStorageContainerCapacity().getTwoDimensionCapacity()
				.intValue();

		int positionDimensionOne = current.getPositionDimensionOne().intValue();
		int positionDimensionTwo = current.getPositionDimensionTwo().intValue();

		Logger.out.debug("validatePosition C : " + positionDimensionOne + " : "
				+ positionDimensionTwo);
		Logger.out.debug("validatePosition P : " + posOneCapacity + " : " + posTwoCapacity);

		if ((positionDimensionOne > posOneCapacity) || (positionDimensionTwo > posTwoCapacity))
		{
			Logger.out.debug("validatePosition false");
			return false;
		}
		Logger.out.debug("validatePosition true");
		return true;
	}

	private boolean validatePosition(int posOneCapacity, int posTwoCapacity,
			StorageContainer current)
	{
		int positionDimensionOne = current.getPositionDimensionOne().intValue();
		int positionDimensionTwo = current.getPositionDimensionTwo().intValue();

		Logger.out.debug("validatePosition C : " + positionDimensionOne + " : "
				+ positionDimensionTwo);
		Logger.out.debug("validatePosition P : " + posOneCapacity + " : " + posTwoCapacity);

		if ((positionDimensionOne > posOneCapacity) || (positionDimensionTwo > posTwoCapacity))
		{
			Logger.out.debug("validatePosition false");
			return false;
		}
		Logger.out.debug("validatePosition true");
		return true;
	}

	// -- to check if storageContainer is available or used
	private boolean isContainerAvailable(DAO dao, StorageContainer current)
	{
		try
		{
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = {"systemIdentifier"};
			String[] whereColumnName = {"positionDimensionOne", "positionDimensionTwo",
					"parentContainer"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = {"=", "=", "="};
			Object[] whereColumnValue = {current.getPositionDimensionOne(),
					current.getPositionDimensionTwo(), current.getParentContainer()};
			String joinCondition = Constants.AND_JOIN_CONDITION;

			List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);
			Logger.out.debug("current.getParentContainer() :" + current.getParentContainer());
			// check if StorageContainer exists with the given storageContainer information
			if (list.size() != 0)
			{
				Object obj = list.get(0);
				Logger.out.debug("**********IN isContainerAvailable : obj::::::: --------- " + obj);
				return false;
			}
			else
			{
				sourceObjectName = Specimen.class.getName();
				String[] whereColumnName1 = {"positionDimensionOne", "positionDimensionTwo",
						"storageContainer.systemIdentifier"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition1 = {"=", "=", "="};
				Object[] whereColumnValue1 = {current.getPositionDimensionOne(),
						current.getPositionDimensionTwo(),
						current.getParentContainer().getSystemIdentifier()};

				list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName1,
						whereColumnCondition1, whereColumnValue1, joinCondition);
				// check if Specimen exists with the given storageContainer information
				if (list.size() != 0)
				{
					Object obj = list.get(0);
					Logger.out
							.debug("**************IN isPositionAvailable : obj::::::: --------------- "
									+ obj);
					return false;
				}

			}

			return true;
		}
		catch (Exception e)
		{
			Logger.out.debug("Error in isContainerAvailable : " + e);
			return false;
		}
	}

	//	 Will check only for valid range of the StorageContainer
	protected boolean validatePosition(StorageContainer storageContainer, String posOne,
			String posTwo)
	{
		try
		{
			Logger.out.debug("storageContainer.getPositionDimensionOne() : "
					+ storageContainer.getPositionDimensionOne());
			Logger.out.debug("storageContainer.getPositionDimensionTwo() : "
					+ storageContainer.getPositionDimensionTwo());
			int positionDimensionOne = (storageContainer.getPositionDimensionOne() != null
					? storageContainer.getPositionDimensionOne().intValue()
					: -1);
			int positionDimensionTwo = (storageContainer.getPositionDimensionTwo() != null
					? storageContainer.getPositionDimensionTwo().intValue()
					: -1);
			if (((positionDimensionOne) < Integer.parseInt(posOne))
					|| ((positionDimensionTwo) < Integer.parseInt(posTwo)))
			{
				return false;
			}
			return true;
		}
		catch (Exception e)
		{
			Logger.out.debug("Error in validatePosition : " + e);
			return false;
		}
	}

	//  Will check only for Position is used or not.
	protected boolean isPositionAvailable(DAO dao, StorageContainer storageContainer,
			String posOne, String posTwo)
	{
		try
		{
			String sourceObjectName = Specimen.class.getName();
			String[] selectColumnName = {"systemIdentifier"};
			String[] whereColumnName = {"positionDimensionOne", "positionDimensionTwo",
					"storageContainer.systemIdentifier"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = {"=", "=", "="};
			Object[] whereColumnValue = {posOne, posTwo, storageContainer.getSystemIdentifier()};
			String joinCondition = Constants.AND_JOIN_CONDITION;

			List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);
			Logger.out.debug("storageContainer.getSystemIdentifier() :"
					+ storageContainer.getSystemIdentifier());
			// check if Specimen exists with the given storageContainer information
			if (list.size() != 0)
			{
				Object obj = list.get(0);
				Logger.out
						.debug("**************IN isPositionAvailable : obj::::::: --------------- "
								+ obj);
				//            	Logger.out.debug((Long)obj[0] );
				//            	Logger.out.debug((Integer)obj[1]);
				//            	Logger.out.debug((Integer )obj[2]);

				return false;
			}
			else
			{
				sourceObjectName = StorageContainer.class.getName();
				String[] whereColumnName1 = {"positionDimensionOne", "positionDimensionTwo",
						"parentContainer"}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition1 = {"=", "=", "="};
				Object[] whereColumnValue1 = {posOne, posTwo,
						storageContainer.getSystemIdentifier()};

				list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName1,
						whereColumnCondition1, whereColumnValue1, joinCondition);
				Logger.out.debug("storageContainer.getSystemIdentifier() :"
						+ storageContainer.getSystemIdentifier());
				// check if Specimen exists with the given storageContainer information
				if (list.size() != 0)
				{
					Object obj = list.get(0);
					Logger.out
							.debug("**********IN isPositionAvailable : obj::::: --------- " + obj);
					return false;
				}
			}
			return true;
		}
		catch (Exception e)
		{
			Logger.out.debug("Error in isPositionAvailable : " + e);
			return false;
		}
	}

	//	 -- storage container validation for specimen
	public void checkContainer(DAO dao, String storageContainerID, String positionOne,
			String positionTwo, SessionDataBean sessionDataBean) throws DAOException, SMException
	{
		//        List list = dao.retrieve(StorageContainer.class.getName(),
		//                "systemIdentifier",storageContainerID  );

		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER,
				"storageContainerCapacity.oneDimensionCapacity",
				"storageContainerCapacity.twoDimensionCapacity"};
		String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {storageContainerID};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);

		// check if StorageContainer exists with the given ID
		if (list.size() != 0)
		{
			Object[] obj = (Object[]) list.get(0);
			Logger.out.debug("**********SC found for given ID ****obj::::::: --------------- "
					+ obj);
			Logger.out.debug((Long) obj[0]);
			Logger.out.debug((Integer) obj[1]);
			Logger.out.debug((Integer) obj[2]);

			StorageContainer pc = new StorageContainer();
			pc.setSystemIdentifier((Long) obj[0]);

			if (obj[1] != null)
				pc.setPositionDimensionOne((Integer) obj[1]);
			if (obj[2] != null)
				pc.setPositionDimensionTwo((Integer) obj[2]);

			//check if user has privilege to use the container
			boolean hasAccess = validateContainerAccess(pc, sessionDataBean);
			Logger.out.debug("hasAccess..............." + hasAccess);
			if (!hasAccess)
			{
				throw new DAOException(ApplicationProperties.getValue("access.use.object.denied"));
			}
			// check for closed Container
			checkStatus(dao, pc, "Storage Container");

			// check for valid position
			boolean isValidPosition = validatePosition(pc, positionOne, positionTwo);
			Logger.out.debug("isValidPosition : " + isValidPosition);
			if (isValidPosition) //	if position is valid 
			{
				boolean canUsePosition = isPositionAvailable(dao, pc, positionOne, positionTwo);
				Logger.out.debug("canUsePosition : " + canUsePosition);
				if (canUsePosition) // position empty. can be used 
				{

				}
				else
				// position already in use
				{
					throw new DAOException(ApplicationProperties
							.getValue("errors.storageContainer.inUse"));
				}
			}
			else
			// position is invalid
			{
				throw new DAOException(ApplicationProperties
						.getValue("errors.storageContainer.dimensionOverflow"));
			}
		}
		else
		//	storageContainer does not exist
		{
			throw new DAOException(ApplicationProperties.getValue("errors.storageContainerExist"));
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData(edu.wustl.common.beans.SessionDataBean, java.util.Map)
	 */
	public Vector getTreeViewData(SessionDataBean sessionData, Map map, List list)
			throws DAOException
	{
		return null;
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		StorageContainer container = (StorageContainer) obj;
		if (operation.equals(Constants.ADD))
		{
			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(container.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties
						.getValue("activityStatus.active.errMsg"));
			}

			if (container.getIsFull().booleanValue())
			{
				throw new DAOException(ApplicationProperties
						.getValue("storageContainer.isContainerFull.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, container
					.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		return true;
	}

	// TODO Write the proper business logic to return an appropriate list of containers.
	public List getStorageContainerList() throws DAOException
	{
		String sourceObjectName = StorageContainer.class.getName();
		String[] displayNameFields = {Constants.SYSTEM_IDENTIFIER};
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List list = getList(sourceObjectName, displayNameFields, valueField, true);
		return list;
	}

	public List getCollectionProtocolList() throws DAOException
	{
		String sourceObjectName = CollectionProtocol.class.getName();
		List returnList = new ArrayList();
		NameValueBean nvb1 = new NameValueBean("--Any--", "-1");
		returnList.add(nvb1);
		List list = retrieve(sourceObjectName);
		Iterator itr = list.iterator();
		while (itr.hasNext())
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) itr.next();
			NameValueBean nvb = new NameValueBean(collectionProtocol.getTitle(), collectionProtocol);
			returnList.add(nvb);
		}
		return returnList;
	}

	/**
	 * This functions returns a double dimensional boolean array which tells the availablity of 
	 * storage positions of particular container. True - Available. False - Not Available.
	 * @param container The container.
	 * @return Returns a double dimensional boolean array of position availablity.
	 * @throws DAOException
	 */
	public boolean[][] getAvailablePositions(StorageContainer container) throws DAOException
	{
		final int dimX = container.getStorageContainerCapacity().getOneDimensionCapacity()
				.intValue() + 1;
		final int dimY = container.getStorageContainerCapacity().getTwoDimensionCapacity()
				.intValue() + 1;
		boolean[][] positions = new boolean[dimX][dimY];

		//Initializing the array
		for (int i = 0; i < dimX; i++)
		{
			for (int j = 0; j < dimY; j++)
			{
				positions[i][j] = true;
			}
		}

		//Retrieving all the occupied positions by child containers
		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = {"positionDimensionOne", "positionDimensionTwo"};
		String[] whereColumnName = {"parentContainer"};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {container.getSystemIdentifier()};

		List list = retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, null);

		if (!list.isEmpty())
		{
			int x, y;

			for (int i = 0; i < list.size(); i++)
			{
				Object[] object = (Object[]) list.get(i);
				x = Integer.parseInt(object[0].toString());
				y = Integer.parseInt(object[1].toString());

				positions[x][y] = false;
			}
		}

		//Retrieving all the occupied positions by specimens
		sourceObjectName = Specimen.class.getName();
		whereColumnName[0] = "storageContainer";

		list = retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition,
				whereColumnValue, null);

		if (!list.isEmpty())
		{
			int x, y;

			for (int i = 0; i < list.size(); i++)
			{
				Object[] object = (Object[]) list.get(i);
				x = Integer.parseInt(object[0].toString());
				y = Integer.parseInt(object[1].toString());

				positions[x][y] = false;
			}
		}

		return positions;
	}

	/**
	 * This functions returns a double dimensional boolean array which tells the availablity of 
	 * storage positions of particular container. True - Available. False - Not Available. 
	 * @param containerId The container identifier.
	 * @return Returns a double dimensional boolean array of position availablity.
	 * @throws DAOException
	 */
	public boolean[][] getAvailablePositions(String containerId) throws DAOException
	{
		List list = retrieve(StorageContainer.class.getName(), Constants.SYSTEM_IDENTIFIER,
				new Long(containerId));

		if (list != null)
		{
			StorageContainer container = (StorageContainer) list.get(0);
			return getAvailablePositions(container);
		}
		else
		{
			return new boolean[0][0];
		}
	}

	/**
	 * This functions returns a map of available rows vs. available columns.
	 * @param container The container.
	 * @return Returns a map of available rows vs. available columns.
	 * @throws DAOException
	 */
	public Map getAvailablePositionMap(StorageContainer container) throws DAOException
	{
		Map map = new TreeMap();
		boolean[][] availablePosistions = getAvailablePositions(container);

		for (int x = 1; x < availablePosistions.length; x++)
		{
			List list = new ArrayList();

			for (int y = 1; y < availablePosistions[x].length; y++)
			{
				if (availablePosistions[x][y])
				{
					list.add(new Integer(y));
				}
			}

			if (!list.isEmpty())
			{
				Integer xObj = new Integer(x);
				map.put(xObj, list);
			}
		}

		return map;
	}

	/**
	 * This functions returns a map of available rows vs. available columns.
	 * @param containerId The container identifier.
	 * @return Returns a map of available rows vs. available columns.
	 * @throws DAOException
	 */
	public Map getAvailablePositionMap(String containerId) throws DAOException
	{
		List list = retrieve(StorageContainer.class.getName(), Constants.SYSTEM_IDENTIFIER,
				new Long(containerId));

		if (list != null)
		{
			StorageContainer container = (StorageContainer) list.get(0);
			return getAvailablePositionMap(container);
		}
		else
		{
			return new TreeMap();
		}
	}

	/**
	 * This functions returns a map of allocated containers vs. their respective free locations.
	 * @return Returns a map of allocated containers vs. their respective free locations.
	 * @throws DAOException
	 */
	public Map getAllocatedContainerMap() throws DAOException
	{
		/*
		 A code snippet inside the commented block should actually be replaced by the
		 code to get the allocated containers of specific collection protocol
		 */
		String[] selectedColumns = {Constants.SYSTEM_IDENTIFIER};
		List list = retrieve(StorageContainer.class.getName(), selectedColumns);
		String[] containerIdArray = new String[list.size()];

		for (int i = 0; i < containerIdArray.length; i++)
		{
			Object object = (Object) list.get(i);
			containerIdArray[i] = object.toString();
		}
		/********************************************************************/

		Map containerMap = new TreeMap();

		for (int i = 0; i < containerIdArray.length; i++)
		{
			Map positionMap = getAvailablePositionMap(containerIdArray[i]);

			if (!positionMap.isEmpty())
			{
				Integer id = new Integer(containerIdArray[i]);
				containerMap.put(id, positionMap);
			}
		}

		return containerMap;
	}

	private void loadSiteFromContainerId(DAO dao, StorageContainer container) throws DAOException
	{
		if(container != null)
		{
			Long sysId = container.getSystemIdentifier();
			List siteIdList = dao.retrieve(StorageContainer.class.getName(),Constants.SYSTEM_IDENTIFIER,sysId);
			System.out.println("siteIdList "+siteIdList);
			StorageContainer sc = (StorageContainer)siteIdList.get(0);
			System.out.println("siteId "+sc.getSite().getSystemIdentifier());
			container.setSite(sc.getSite());
		}
	}
	

}