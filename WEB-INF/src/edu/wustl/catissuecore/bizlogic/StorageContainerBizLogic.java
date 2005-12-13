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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.JDBCDAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageContainerDetails;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.storage.StorageContainerTreeNode;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * @author aniruddha_phadnis
 */
public class StorageContainerBizLogic extends DefaultBizLogic implements
		TreeDataInterface {

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

		//Setting the Parent Container if applicable
		int posOneCapacity = 1, posTwoCapacity = 1;
		int positionDimensionOne = Constants.STORAGE_CONTAINER_FIRST_ROW, positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
		boolean fullStatus[][] = null;
		int noOfContainers = container.getNoOfContainers().intValue();
		
		if (container.getParentContainer() != null) 
		{
			List list = dao.retrieve(StorageContainer.class.getName(),
					"systemIdentifier", container.getParentContainer()
							.getSystemIdentifier());
			
			if (list.size() != 0) 
			{
				StorageContainer pc = (StorageContainer) list.get(0);

				// check for closed ParentContainer
				checkStatus(dao, pc, "Parent Container");

				int totalCapacity = pc.getStorageContainerCapacity()
						.getOneDimensionCapacity().intValue()
						* pc.getStorageContainerCapacity()
								.getTwoDimensionCapacity().intValue();
				if ((noOfContainers + pc.getChildrenContainerCollection()
						.size()) > totalCapacity) {
					throw new DAOException(ApplicationProperties
							.getValue("errors.storageContainer.overflow"));
				} 
				else 
				{
					//Check if position specified is within the parent
					// container's
					//capacity
					if (false == validatePosition(pc, container)) {
						throw new DAOException(
								ApplicationProperties
										.getValue("errors.storageContainer.dimensionOverflow"));
					}

					// check for availability of position
					boolean canUse = isContainerAvailable(dao, container);

					if (!canUse) {
						throw new DAOException(ApplicationProperties
								.getValue("errors.storageContainer.inUse"));
					}

					container.setParentContainer(pc);
					
					//                  check for closed ParentSite
					checkStatus(dao, pc.getSite(), "Parent Site");

					container.setSite(pc.getSite());

					posOneCapacity = pc.getStorageContainerCapacity()
							.getOneDimensionCapacity().intValue();
					posTwoCapacity = pc.getStorageContainerCapacity()
							.getTwoDimensionCapacity().intValue();
					
					fullStatus = getStorageContainerFullStatus(dao, container
							.getParentContainer().getSystemIdentifier());
					positionDimensionOne = container.getPositionDimensionOne()
							.intValue();
					positionDimensionTwo = container.getPositionDimensionTwo()
							.intValue();
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
			if (cont.getParentContainer() != null) {
				cont.setPositionDimensionOne(new Integer(positionDimensionOne));
				cont.setPositionDimensionTwo(new Integer(positionDimensionTwo));
			}

			if (container.getStartNo() != null)
				cont.setNumber(new Integer(i
						+ container.getStartNo().intValue()));

			dao.insert(cont.getStorageContainerCapacity(), sessionDataBean,
					true, true);
			dao.insert(cont, sessionDataBean, true, true);

			//Used for showing the success message after insert and using it
			// for edit.
			container.setSystemIdentifier(cont.getSystemIdentifier());

			Collection storageContainerDetailsCollection = cont.getStorageContainerDetailsCollection();
			
			
			if(storageContainerDetailsCollection.isEmpty())
			{
				//add a dummy container details for Query.
				StorageContainerDetails storageContainerDetails = new StorageContainerDetails();
				storageContainerDetails.setParameterName("");
				storageContainerDetails.setParameterValue("");
				storageContainerDetailsCollection.add(storageContainerDetails);
			}
			
			if (storageContainerDetailsCollection.size() > 0) 
			{
				Iterator it = storageContainerDetailsCollection.iterator();
				while (it.hasNext()) 
				{
					StorageContainerDetails storageContainerDetails = (StorageContainerDetails) it
							.next();
					storageContainerDetails.setStorageContainer(cont);
					dao.insert(storageContainerDetails, sessionDataBean, true,
							true);
				}
			}

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
					        							% (posOneCapacity+1);
					    
						positionDimensionTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
					}
					else
					{
					    positionDimensionTwo = positionDimensionTwo + 1;
					}
					
					Logger.out.debug("positionDimensionTwo: "
							+ positionDimensionTwo);
					Logger.out.debug("positionDimensionOne: "
							+ positionDimensionOne);
				} while (fullStatus[positionDimensionOne][positionDimensionTwo] != false);
			}
			
			//Inserting authorization data
			Set protectionObjects = new HashSet();
			protectionObjects.add(cont);
			try
			{
				SecurityManager.getInstance(this.getClass())
						.insertAuthorizationData(null, protectionObjects, null);
			} catch (SMException e) 
			{
				Logger.out.error("Exception in Authorization: "
						+ e.getMessage(), e);
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
	
	public String[] getDynamicGroups(AbstractDomainObject obj)
    {
        String[] dynamicGroups=null;
        StorageContainer storageContainer = (StorageContainer) obj;
        dynamicGroups = null;
        
        try
        {
            if (storageContainer.getParentContainer() != null)
            {
                dynamicGroups = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(storageContainer.getParentContainer(),Constants.getStorageContainerPGName());
            }
            else
            {
                dynamicGroups = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(storageContainer.getSite(), Constants.getStorageContainerPGName());
            }
            
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
        
        return dynamicGroups;
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
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) 
	 throws DAOException,UserNotAuthorizedException 
	{
		StorageContainer container = (StorageContainer) obj;
		StorageContainer oldContainer = (StorageContainer) oldObj;
		Logger.out.debug( "container.isParentChanged() : "+ container.isParentChanged());
		if (container.isParentChanged()) 
		{
			if (container.getParentContainer() != null)
			{
				//Check whether continer is moved to one of its sub container.
				if (isUnderSubContainer(container, container.getParentContainer().getSystemIdentifier()))
				{
					throw new DAOException(ApplicationProperties
							.getValue("errors.container.under.subcontainer"));
				}
				Logger.out.debug("Loading ParentContainer: "
						+ container.getParentContainer().getSystemIdentifier());

				StorageContainer pc = (StorageContainer) dao.retrieve(
						StorageContainer.class.getName(), container
								.getParentContainer().getSystemIdentifier());

				//Check if position specified is within the parent container's
				//capacity
				if (false == validatePosition(pc, container)) 
				{
					throw new DAOException(	ApplicationProperties
									.getValue("errors.storageContainer.dimensionOverflow"));
				}
				
				// MD : code added for validation bug id 666. 24-11-2005  start
				boolean canUse = isContainerAvailable( dao, container );
            	Logger.out.debug("canUse : "+canUse );
            	if(!canUse)
            	{
            		throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.inUse"));
            	}
				// MD : code added for validation bug id 666. 24-11-2005 end

				//check for closed ParentContainer
				checkStatus(dao, pc, "Parent Container");

				container.setParentContainer(pc);
				//check for closed Site
				checkStatus(dao, pc.getSite(), "Parent Site");

				container.setSite(pc.getSite());
			}
		}
// MD : code added for validation 25-11-05-----------
		else		// if parent container is not changed only the position is changed.
        {
        	if(container.isPositionChanged())
        	{
        		// -----------------
    			String sourceObjectName = StorageContainer.class.getName();
    			String[] selectColumnName = { "systemIdentifier","storageContainerCapacity.oneDimensionCapacity","storageContainerCapacity.twoDimensionCapacity"};
    			String[] whereColumnName = { "systemIdentifier" }; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
    			String[] whereColumnCondition = { "=" };
    			Object[] whereColumnValue = { container.getParentContainer().getSystemIdentifier()};
    			String joinCondition = null;

    			List list = dao.retrieve(sourceObjectName, selectColumnName,
    					whereColumnName, whereColumnCondition, whereColumnValue,
    					joinCondition);

    			if(!list.isEmpty() )
    			{
    		      	Object[] obj1 =(Object[]) list.get(0);
    	        	Logger.out.debug("**************PC obj::::::: --------------- "+ obj1);
    	        	Logger.out.debug((Long)obj1[0] );
    	        	Logger.out.debug((Integer)obj1[1]);
    	        	Logger.out.debug((Integer )obj1[2]);
		  
    	        	Integer pcCapacityOne = (Integer)obj1[1];
    	        	Integer pcCapacityTwo = (Integer)obj1[2];
    	        	
    	        	if(!validatePosition(pcCapacityOne.intValue(),pcCapacityTwo.intValue() ,	container))
    	        	{
    	           		throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.dimensionOverflow"));
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
            	Logger.out.debug("oldContainer.getParentContainer().getSystemIdentifier().longValue() : "+oldContainer.getParentContainer().getSystemIdentifier().longValue() );
            	Logger.out.debug("container.getParentContainer().getSystemIdentifier().longValue() : " + container.getParentContainer().getSystemIdentifier().longValue() );
            	Logger.out.debug("oldContainer.getPositionDimensionOne().intValue() : " + oldContainer.getPositionDimensionOne().intValue() );
            	Logger.out.debug("container.getPositionDimensionOne().intValue() : " + container.getPositionDimensionOne().intValue() );
            	Logger.out.debug("oldContainer.getPositionDimensionTwo().intValue() : "+ oldContainer.getPositionDimensionTwo().intValue() );
            	Logger.out.debug("container.getPositionDimensionTwo().intValue() : " + container.getPositionDimensionTwo().intValue() );

            	if( oldContainer.getPositionDimensionOne().intValue() != container.getPositionDimensionOne().intValue() ||
					oldContainer.getPositionDimensionTwo().intValue() != container.getPositionDimensionTwo().intValue())
            	{
            		boolean canUse = isContainerAvailable( dao, container );
                	Logger.out.debug("canUse : "+canUse );
                	if(!canUse)
                	{
                		throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.inUse"));
                	}
            	}
   	
        	}
        }

// MD : --------- end  25-11-05 -----------------		

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
				|| (oldContainerDimTwo.compareTo(newContainerDimTwo) > 0)) {
			throw new DAOException(ApplicationProperties
					.getValue("errors.storageContainer.cannotReduce"));
		}
		
		//Check for closed Site
		if ((container.getSite() != null) && (oldContainer.getSite() != null)) {
			if ((container.getSite().getSystemIdentifier() != null)
					&& (oldContainer.getSite().getSystemIdentifier() != null)) {
				if ((!container.getSite().getSystemIdentifier().equals(
						oldContainer.getSite().getSystemIdentifier()))) {
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

		Collection storageContainerDetailsCollection = container
				.getStorageContainerDetailsCollection();

		Collection oldStorageContainerDetailsCollection = oldContainer
				.getStorageContainerDetailsCollection();
		Iterator it = storageContainerDetailsCollection.iterator();
		while (it.hasNext()) {
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
		}
		
		Logger.out.debug("container.getActivityStatus() "
				+ container.getActivityStatus());
		if (container.getActivityStatus().equals(
				Constants.ACTIVITY_STATUS_DISABLED)) {
			setDisableToSubContainer(container);
			Logger.out.debug("container.getActivityStatus() "
					+ container.getActivityStatus());
			Long containerIDArr[] = { container.getSystemIdentifier() };
			
			disableSubStorageContainer(dao, containerIDArr);
		}
	}
	
	public void setPrivilege(DAO dao, String privilegeName, Class objectType,
			Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation)
			throws SMException, DAOException 
	{
		Logger.out.debug(" privilegeName:" + privilegeName + " objectType:"
				+ objectType + " objectIds:"
				+ edu.wustl.common.util.Utility.getArrayString(objectIds)
				+ " userId:" + userId + " roleId:" + roleId + " assignToUser:"
				+ assignToUser);
		Logger.out.debug("assignOeration..................."+assignOperation);
		if (assignOperation == Constants.PRIVILEGE_DEASSIGN)
		{
		    String [] selectColumnNames = {"parentContainer.systemIdentifier","site.systemIdentifier"};
	        String [] whereColumnNames = {"systemIdentifier"};
	        List listOfSubElement = super.getRelatedObjects(dao, StorageContainer.class, selectColumnNames, whereColumnNames, objectIds);

	        Iterator iterator = listOfSubElement.iterator();
	        String userName = SecurityManager.getInstance(StorageContainerBizLogic.class).getUserById(userId.toString()).getLoginName();
	        while (iterator.hasNext())
	        {
	            Object[] row = (Object[]) iterator.next();
	            if ((row[0] == null) || (row[0].equals("")))
	            {
	                boolean permission = SecurityManager.getInstance(StorageContainerBizLogic.class).checkPermission(userName,Site.class.getName(),row[1].toString(),privilegeName);
	                if (permission == true)
	                {
	                    throw new DAOException("Error : First de-assign privilege of the Parent Site with system identifier "+row[1].toString());
	                }
	            }
	            else
	            {
	                boolean permission = SecurityManager.getInstance(StorageContainerBizLogic.class).checkPermission(userName,objectType.getName(),row[0].toString(),privilegeName);
	                if (permission == true)
	                {
	                    throw new DAOException("Error : First de-assign privilege of the Parent Container with system identifier "+row[0].toString());
	                }
	            }
	        }
		}
		
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId,
				roleId, assignToUser, assignOperation);
		
		assignPrivilegeToSubStorageContainer(dao, privilegeName, objectIds,
				userId, roleId, assignToUser, assignOperation);
	}
	
	private void assignPrivilegeToSubStorageContainer(DAO dao,
			String privilegeName, Long[] storageContainerIDArr, Long userId,
			String roleId, boolean assignToUser, boolean assignOperation) throws SMException,
			DAOException {

		List listOfSubStorageContainerId = super.getRelatedObjects(dao,
				StorageContainer.class, "parentContainer",
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
    public void assignPrivilegeToRelatedObjectsForSite(DAO dao, String privilegeName, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, DAOException
    {
        List listOfSubElement = super.getRelatedObjects(dao, StorageContainer.class, "site", objectIds);
        
    	if(!listOfSubElement.isEmpty())
    	{
    	    super.setPrivilege(dao,privilegeName,StorageContainer.class,Utility.toLongArray(listOfSubElement),userId, roleId, assignToUser, assignOperation);
    	}
    }
    
	// This method sets the Storage Type & Site (if applicable) of this
	// container.
	private void loadSite(DAO dao, StorageContainer container)
			throws DAOException {
		//Setting the site if applicable
		if (container.getSite() != null) {
			Object siteObj = dao.retrieve(Site.class.getName(), container
					.getSite().getSystemIdentifier());
			if (siteObj != null) {
				Site site = (Site) siteObj;

				// check for closed site
				checkStatus(dao, site, "Site");

				container.setSite(site);
				setSiteForSubContainers(container, site);
			}
		}
	}

	private void loadStorageType(DAO dao, StorageContainer container)
			throws DAOException {
		//Setting the Storage Type
		Object storageTypeObj = dao.retrieve(StorageType.class.getName(),
				container.getStorageType().getSystemIdentifier());
		if (storageTypeObj != null) {
			StorageType type = (StorageType) storageTypeObj;
			container.setStorageType(type);
		}
	}

	private void setSiteForSubContainers(StorageContainer storageContainer,
			Site site) {
		if (storageContainer != null) {
			Logger.out.debug("site() " + site.getSystemIdentifier());
			Logger.out
					.debug("storageContainer.getChildrenContainerCollection() "
							+ storageContainer.getChildrenContainerCollection()
									.size());

			Iterator iterator = storageContainer
					.getChildrenContainerCollection().iterator();
			while (iterator.hasNext()) {
				StorageContainer container = (StorageContainer) iterator.next();
				container.setSite(site);
				setSiteForSubContainers(container, site);
			}
		}
	}

	private boolean isUnderSubContainer(StorageContainer storageContainer,
			Long parentContainerID) {
		if (storageContainer != null) {
			Iterator iterator = storageContainer
					.getChildrenContainerCollection().iterator();
			while (iterator.hasNext()) {
				StorageContainer container = (StorageContainer) iterator.next();
				//Logger.out.debug("SUB CONTINER container
				// "+parentContainerID.longValue()+"
				// "+container.getSystemIdentifier().longValue()+"
				// "+(parentContainerID.longValue()==container.getSystemIdentifier().longValue()));
				if (parentContainerID.longValue() == container
						.getSystemIdentifier().longValue())
					return true;
				if (isUnderSubContainer(container, parentContainerID))
					return true;
			}
		}
		return false;
	}

	//  TODO TO BE REMOVED
	private void setDisableToSubContainer(StorageContainer storageContainer) {
		if (storageContainer != null) {
			Iterator iterator = storageContainer
					.getChildrenContainerCollection().iterator();
			while (iterator.hasNext()) {
				StorageContainer container = (StorageContainer) iterator.next();
				container.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				setDisableToSubContainer(container);
			}
		}
	}

	public int getNextContainerNumber(long parentID, long typeID,
			boolean isInSite) throws DAOException {
		String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
		String[] selectColumnName = { "max(CONTAINER_NUMBER) as MAX_NAME" };
		String[] whereColumnName = { "STORAGE_TYPE_ID", "PARENT_CONTAINER_ID" };
		String[] whereColumnCondition = { "=", "=" };
		Object[] whereColumnValue = { Long.toString(typeID),
				Long.toString(parentID) };

		if (isInSite) {
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

		List list = dao.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);

		dao.closeSession();

		if (!list.isEmpty()) {
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty()) {
				String str = (String) columnList.get(0);
				if (!str.equals("")) {
					int no = Integer.parseInt(str);
					return no + 1;
				}
			}
		}

		return 1;
	}

	public Vector getTreeViewData() throws DAOException {
		//SRI: Made changes for performance enhancement of the
		//tree applet
		JDBCDAO dao = (JDBCDAO) DAOFactory.getDAO(Constants.JDBC_DAO);
		dao.openSession(null);

		// Sri: Get all container, thier site details and storage type details
		String queryStr = "select t1.PARENT_CONTAINER_ID,t1.IDENTIFIER, "
				+ "t1.SITE_ID, t2.NAME,t2.TYPE, t3.TYPE,CONTAINER_NUMBER "
				+ "from CATISSUE_STORAGE_CONTAINER t1, "
				+ "CATISSUE_SITE t2, "
				+ "catissue_storage_type t3 "
				+ "where t1.SITE_ID = t2.IDENTIFIER and t1.STORAGE_TYPE_ID = t3.IDENTIFIER";

		List list = null;
		try {
			list = dao.executeQuery(queryStr, null, false, null);
		} catch (Exception ex) {
			throw new DAOException(ex.getMessage());
		}

		dao.closeSession();

		Vector vector = new Vector();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				List rowList = (List) list.get(i);
				StorageContainerTreeNode treeNode = new StorageContainerTreeNode();
				treeNode.setStorageContainerIdentifier(Long
						.valueOf((String) rowList.get(1)));
				treeNode.setStorageContainerName((String) rowList.get(6));
				treeNode.setStorageContainerType((String) rowList.get(5));
				if ((String) rowList.get(0) != "") // if parent is null in db
				{
					treeNode.setParentStorageContainerIdentifier(Long
							.valueOf((String) rowList.get(0)));
				}
				treeNode.setSiteSystemIdentifier(Long.valueOf((String) rowList.get(2)));
				treeNode.setSiteName((String) rowList.get(3));
				treeNode.setSiteType((String) rowList.get(4));
				vector.add(treeNode);
			}
		}
		
		return vector;
	}

	public boolean[][] getStorageContainerFullStatus(DAO dao,
			Long systemIdentifier) throws DAOException {
		List list = dao.retrieve(StorageContainer.class.getName(),
				"systemIdentifier", systemIdentifier);
		boolean[][] fullStatus = null;

		if (!list.isEmpty()) {
			StorageContainer storageContainer = (StorageContainer) list.get(0);
			Integer oneDimensionCapacity = storageContainer
					.getStorageContainerCapacity().getOneDimensionCapacity();
			Integer twoDimensionCapacity = storageContainer
					.getStorageContainerCapacity().getTwoDimensionCapacity();
			fullStatus = new boolean[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];

			if (storageContainer.getChildrenContainerCollection() != null) {
				Iterator iterator = storageContainer
						.getChildrenContainerCollection().iterator();
				Logger.out.debug("storageContainer.getChildrenContainerCollection().size(): "
								+ storageContainer.getChildrenContainerCollection().size());
				while (iterator.hasNext()) {
					StorageContainer childStorageContainer = (StorageContainer) iterator.next();
					Integer positionDimensionOne = childStorageContainer
							.getPositionDimensionOne();
					Integer positionDimensionTwo = childStorageContainer
							.getPositionDimensionTwo();
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

	private void disableSubStorageContainer(DAO dao,
			Long storageContainerIDArr[]) throws DAOException {
		List listOfSpecimenIDs = getRelatedObjects(dao, Specimen.class,
				"storageContainer", storageContainerIDArr);
		if (!listOfSpecimenIDs.isEmpty()) {
			throw new DAOException(ApplicationProperties
					.getValue("errors.container.contains.specimen"));
		}

		List listOfSubStorageContainerId = super.disableObjects(dao,
				StorageContainer.class, "parentContainer",
				"CATISSUE_STORAGE_CONTAINER", "PARENT_CONTAINER_ID",
				storageContainerIDArr);

		if (listOfSubStorageContainerId.isEmpty())
			return;

		disableSubStorageContainer(dao, Utility
				.toLongArray(listOfSubStorageContainerId));
	}

	// Checks for whether the user is trying to place the container in a
	// position
	// outside the range of parent container
	// This is needed since now users can enter the values in the edit box
	private boolean validatePosition(StorageContainer parent, StorageContainer current) 
	{
		int posOneCapacity = parent.getStorageContainerCapacity()
				.getOneDimensionCapacity().intValue();
		int posTwoCapacity = parent.getStorageContainerCapacity()
				.getTwoDimensionCapacity().intValue();
		
		int positionDimensionOne = current.getPositionDimensionOne().intValue();
		int positionDimensionTwo = current.getPositionDimensionTwo().intValue();
		
		Logger.out.debug("validatePosition C : " + positionDimensionOne + " : " + positionDimensionTwo);
		Logger.out.debug("validatePosition P : " + posOneCapacity + " : " + posTwoCapacity);
		
		if ((positionDimensionOne > posOneCapacity)
				|| (positionDimensionTwo > posTwoCapacity)) 
		{
			Logger.out.debug("validatePosition false");
			return false;
		}
		
		Logger.out.debug("validatePosition true");
		return true;
	}

	private boolean validatePosition(int posOneCapacity, int posTwoCapacity, StorageContainer current) 
	{
		int positionDimensionOne = current.getPositionDimensionOne().intValue();
		int positionDimensionTwo = current.getPositionDimensionTwo().intValue();
		
		Logger.out.debug("validatePosition C : " + positionDimensionOne + " : " + positionDimensionTwo);
		Logger.out.debug("validatePosition P : " + posOneCapacity + " : " + posTwoCapacity);
		
		if ((positionDimensionOne > posOneCapacity)
				|| (positionDimensionTwo > posTwoCapacity)) 
		{
			Logger.out.debug("validatePosition false");
			return false;
		}
		Logger.out.debug("validatePosition true");
		return true;
	}

	// -- to check if storageContainer is available or used
	private boolean isContainerAvailable(DAO dao, StorageContainer current) {
		try {
			String sourceObjectName = StorageContainer.class.getName();
			String[] selectColumnName = { "systemIdentifier" };
			String[] whereColumnName = { "positionDimensionOne",
					"positionDimensionTwo", "parentContainer" }; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition = { "=", "=", "=" };
			Object[] whereColumnValue = { current.getPositionDimensionOne(),
					current.getPositionDimensionTwo(),
					current.getParentContainer() };
			String joinCondition = Constants.AND_JOIN_CONDITION;

			List list = dao.retrieve(sourceObjectName, selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue,
					joinCondition);
			Logger.out.debug("current.getParentContainer() :"
					+ current.getParentContainer());
			// check if StorageContainer exists with the given storageContainer information
			if (list.size() != 0)
			{
				Object obj = list.get(0);
				Logger.out.debug("**********IN isContainerAvailable : obj::::::: --------- "+ obj);
				return false;
			}
			else
			{
		   		sourceObjectName = Specimen.class.getName();
				String[] whereColumnName1 = {"positionDimensionOne","positionDimensionTwo","storageContainer.systemIdentifier"};	//"storageContainer."+Constants.SYSTEM_IDENTIFIER
				String[] whereColumnCondition1 ={"=","=","="};
	    	    Object[] whereColumnValue1 = {current.getPositionDimensionOne() ,current.getPositionDimensionTwo(), current.getParentContainer().getSystemIdentifier()  };
					 	
	            list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName1, 
	            		         		  whereColumnCondition1, whereColumnValue1, joinCondition);
	            // check if Specimen exists with the given storageContainer information
	            if (list.size() != 0)
	            {
	               	Object obj = list.get(0);
	            	Logger.out.debug("**************IN isPositionAvailable : obj::::::: --------------- "+ obj);
	            	return false;
	            }
	
			}

			return true;
		} catch (Exception e) {
			Logger.out.debug("Error in isContainerAvailable : " + e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData(edu.wustl.common.beans.SessionDataBean, java.util.Map)
	 */
	public Vector getTreeViewData(SessionDataBean sessionData, Map map) throws DAOException {

		return null;
	}

	//	 Will check only for valid range of the StorageContainer
    protected boolean validatePosition(StorageContainer storageContainer, String posOne, String posTwo)
    {
    	try
		{
    		Logger.out.debug("storageContainer.getPositionDimensionOne() : " + storageContainer.getPositionDimensionOne());
    		Logger.out.debug("storageContainer.getPositionDimensionTwo() : " + storageContainer.getPositionDimensionTwo());
	        int positionDimensionOne = (storageContainer.getPositionDimensionOne() != null ? storageContainer.getPositionDimensionOne().intValue():-1);
	        int positionDimensionTwo =(storageContainer.getPositionDimensionTwo() != null ? storageContainer.getPositionDimensionTwo().intValue():-1);
	        if(((positionDimensionOne) < Integer.parseInt(posOne)) ||
	           ((positionDimensionTwo) < Integer.parseInt(posTwo)))
	        {
	        	return false;
	        }
	        return true;
		}
    	catch(Exception e)
		{
    		Logger.out.debug("Error in validatePosition : " + e );
    		return false;
		}
	}
    
//  Will check only for Position is used or not.
    protected boolean isPositionAvailable(DAO dao, StorageContainer storageContainer, String posOne, String posTwo)
    {
    	try
		{
    		String sourceObjectName = Specimen.class.getName();
			String[] selectColumnName = {"systemIdentifier"}; 
    	    String[] whereColumnName = {"positionDimensionOne","positionDimensionTwo","storageContainer.systemIdentifier"};	//"storageContainer."+Constants.SYSTEM_IDENTIFIER
			String[] whereColumnCondition ={"=","=","="};
    	    Object[] whereColumnValue = {posOne ,posTwo, storageContainer.getSystemIdentifier()  };
			String joinCondition = Constants.AND_JOIN_CONDITION;
				 	
            List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, 
            		         		  whereColumnCondition, whereColumnValue, joinCondition);
            Logger.out.debug("storageContainer.getSystemIdentifier() :" +storageContainer.getSystemIdentifier());
            // check if Specimen exists with the given storageContainer information
            if (list.size() != 0)
            {
               	Object obj = list.get(0);
            	Logger.out.debug("**************IN isPositionAvailable : obj::::::: --------------- "+ obj);
//            	Logger.out.debug((Long)obj[0] );
//            	Logger.out.debug((Integer)obj[1]);
//            	Logger.out.debug((Integer )obj[2]);
     
            	return false;
            }
            else
            {
        		sourceObjectName = StorageContainer.class.getName();
    			String[] whereColumnName1 = {"positionDimensionOne","positionDimensionTwo","parentContainer"};	//"storageContainer."+Constants.SYSTEM_IDENTIFIER
    			String[] whereColumnCondition1 ={"=","=","="};
        	    Object[] whereColumnValue1 = {posOne ,posTwo, storageContainer.getSystemIdentifier()  };
    				 	
                list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName1, 
                		         		  whereColumnCondition1, whereColumnValue1, joinCondition);
                Logger.out.debug("storageContainer.getSystemIdentifier() :" +storageContainer.getSystemIdentifier());
                // check if Specimen exists with the given storageContainer information
                if (list.size() != 0)
                {
                   	Object obj = list.get(0);
                	Logger.out.debug("**********IN isPositionAvailable : obj::::: --------- "+ obj);
                	return false;
                }
            }
    		return true;
		}
		catch(Exception e)
		{
			Logger.out.debug("Error in isPositionAvailable : " + e );
			return false;
		}
    }
    
//	 -- storage container validation for specimen
    
    public void checkContainer(DAO dao, String storageContainerID, String positionOne, String positionTwo) throws DAOException
    {
//        List list = dao.retrieve(StorageContainer.class.getName(),
//                "systemIdentifier",storageContainerID  );

		String sourceObjectName = StorageContainer.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER,"storageContainerCapacity.oneDimensionCapacity",
				"storageContainerCapacity.twoDimensionCapacity"}; 
	    String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER  };
		String[] whereColumnCondition ={"="};
	    Object[] whereColumnValue = {storageContainerID };
		String joinCondition = Constants.AND_JOIN_CONDITION;
			 	
        List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, 
        		         		  whereColumnCondition, whereColumnValue, joinCondition);
    	
        // check if StorageContainer exists with the given ID
        if (list.size() != 0)
        {
        	Object[] obj =(Object[]) list.get(0);
        	Logger.out.debug("**********SC found for given ID ****obj::::::: --------------- "+ obj);
        	Logger.out.debug((Long)obj[0] );
        	Logger.out.debug((Integer)obj[1]);
        	Logger.out.debug((Integer )obj[2]);
        	
        	StorageContainer pc = new StorageContainer();
        	pc.setSystemIdentifier((Long)obj[0] );
        	
            	if(obj[1] !=null)
                	pc.setPositionDimensionOne((Integer)obj[1] );
               	if(obj[2] !=null)
                	pc.setPositionDimensionTwo((Integer )obj[2] );
        	
            
            // check for closed Container
			checkStatus(dao, pc, "Storage Container" );
			
			// check for valid position
			boolean isValidPosition = validatePosition(pc, positionOne ,positionTwo  );
			Logger.out.debug("isValidPosition : " + isValidPosition);
			if(isValidPosition)		//	if position is valid 
			{
				boolean canUsePosition = isPositionAvailable(dao, pc, positionOne, positionTwo);
				Logger.out.debug("canUsePosition : " + canUsePosition  );
				if(canUsePosition)		// position empty. can be used 
				{
					
				}
				else		// position already in use
				{
		        	throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.inUse"));
				}
			}
			else			// position is invalid
			{
	        	throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.dimensionOverflow"));
			}
        }
        else		//	storageContainer does not exist
        {
        	throw new DAOException(ApplicationProperties.getValue("errors.storageContainerExist"));
        }
   }
}