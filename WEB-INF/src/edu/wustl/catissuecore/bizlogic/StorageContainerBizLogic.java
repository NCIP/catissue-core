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
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.dao.JDBCDAO;
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
 * StorageContainerHDAO is used to add Storage Container information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class StorageContainerBizLogic extends DefaultBizLogic
        implements
            TreeDataInterface
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
        int positionDimensionOne = 0, positionDimensionTwo = 0;
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
    			checkStatus(dao, pc, "Parent Container" );
    			
                int totalCapacity = pc.getStorageContainerCapacity().getOneDimensionCapacity().intValue()
                        * pc.getStorageContainerCapacity().getTwoDimensionCapacity().intValue();
                if ((noOfContainers + pc.getChildrenContainerCollection()
                        .size()) > totalCapacity)
                {
                    throw new DAOException(ApplicationProperties.getValue("errors.storageContainer.overflow"));
                }
                else
                {
                    container.setParentContainer(pc);
                    
//                  check for closed ParentSite
        			checkStatus(dao, pc.getSite() , "Parent Site" );
        			
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

            if (container.getStartNo() != null)
                cont.setNumber(new Integer(i
                        + container.getStartNo().intValue()));

            dao.insert(cont.getStorageContainerCapacity(), sessionDataBean,
                    true, true);
            dao.insert(cont, sessionDataBean, true, true);
            
            //Used for showing the success message after insert and using it for edit. 
            container.setSystemIdentifier(cont.getSystemIdentifier());
            
            Collection storageContainerDetailsCollection = cont
                    .getStorageContainerDetailsCollection();
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
                do
                {
                    if (positionDimensionTwo == (posTwoCapacity - 1))
                    {
                        positionDimensionOne = (positionDimensionOne + 1)
                                % posOneCapacity;
                    }
                    positionDimensionTwo = (positionDimensionTwo + 1)
                            % posTwoCapacity;
                }
                while (fullStatus[positionDimensionOne][positionDimensionTwo] != false);
            }
            
            //Inserting authorization data
            Set protectionObjects=new HashSet();
            protectionObjects.add(cont);
    	    try
            {
                SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,null);
            }
            catch (SMException e)
            {
                Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
            }
        }
        
    }

    /**
     * Updates the persistent object in the database.
     * @param obj The object to be updated.
     * @param session The session in which the object is saved.
     * @throws DAOException 
     */
    protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
            throws DAOException, UserNotAuthorizedException
    {
        StorageContainer container = (StorageContainer) obj;
        StorageContainer oldContainer = (StorageContainer) oldObj;
        
        if(container.isParentChanged())
        {
        	if(container.getParentContainer() !=null)
        	{
            	//Check whether continer is moved to one of its sub container.
            	if(isUnderSubContainer(container,container.getParentContainer().getSystemIdentifier()))
            	{
            		throw new DAOException(ApplicationProperties.getValue("errors.container.under.subcontainer"));  
            	}
            	Logger.out.debug("Loading ParentContainer: "+container.getParentContainer().getSystemIdentifier());
    			
            	StorageContainer pc = (StorageContainer) dao.retrieve(StorageContainer.class.getName(), container.getParentContainer().getSystemIdentifier());
//            	 check for closed ParentContainer
    			checkStatus(dao, pc, "Parent Container" );
    			
                container.setParentContainer(pc);
//              check for closed Site
               	checkStatus(dao, pc.getSite(), "Parent Site" );

               	container.setSite(pc.getSite());
        	}
        }
        
//      check for closed Site
        if(!container.getSite().equals(oldContainer.getSite() )  )
		{
        	checkStatus(dao, container.getSite(), "Site" );
		}
        setSiteForSubContainers(container, container.getSite());
        
        dao.update(container, sessionDataBean, true, true, false);

        Collection storageContainerDetailsCollection = container.getStorageContainerDetailsCollection();
        Iterator it = storageContainerDetailsCollection.iterator();
        while (it.hasNext())
        {
            StorageContainerDetails storageContainerDetails = (StorageContainerDetails) it.next();
            storageContainerDetails.setStorageContainer(container);
            dao.update(storageContainerDetails, sessionDataBean, true, true, false);
        }
        
        Logger.out.debug("container.getActivityStatus() "+ container.getActivityStatus());
        if (container.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
        {
        	setDisableToSubContainer(container);
            Logger.out.debug("container.getActivityStatus() "+ container.getActivityStatus());
            Long containerIDArr[] = {container.getSystemIdentifier()};

            disableSubStorageContainer(dao, containerIDArr);
        }
    }
    
    public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
	    Logger.out.debug(" privilegeName:"+privilegeName+" objectType:"+objectType+" objectIds:"+edu.wustl.common.util.Utility.getArrayString(objectIds)+" userId:"+userId+" roleId:"+roleId+" assignToUser:"+assignToUser);
	    super.setPrivilege(dao,privilegeName,objectType,objectIds,userId, roleId, assignToUser);
		assignPrivilegeToSubStorageContainer(dao,privilegeName,objectIds,userId,roleId,assignToUser );
    }
    
    private void assignPrivilegeToSubStorageContainer(DAO dao, String privilegeName,Long[] storageContainerIDArr, Long userId, String roleId, boolean assignToUser) throws SMException, DAOException
    {
    	
        List listOfSubStorageContainerId = super.getRelatedObjects(dao,
                StorageContainer.class, "parentContainer", storageContainerIDArr);

        if (listOfSubStorageContainerId.isEmpty())
            return;

        super.setPrivilege(dao,privilegeName,StorageContainer.class,Utility.toLongArray(listOfSubStorageContainerId),userId, roleId, assignToUser);
        assignPrivilegeToSubStorageContainer(dao,privilegeName, Utility.toLongArray(listOfSubStorageContainerId),userId,roleId,assignToUser);
   
    }

    // This method sets the Storage Type & Site (if applicable) of this container.
    private void loadSite(DAO dao, StorageContainer container)
            throws DAOException
    {
        //Setting the site if applicable
        if (container.getSite() != null)
        {
            List list = dao.retrieve(Site.class.getName(), "systemIdentifier",
                    container.getSite().getSystemIdentifier());
            if (list.size() != 0)
            {
                Site site = (Site) list.get(0);
                
                // check for closed site
    			checkStatus(dao, site, "Site" );
                
    			container.setSite(site);
                setSiteForSubContainers(container, site);
            }
        }
    }
    
    private void loadStorageType(DAO dao, StorageContainer container)
            throws DAOException
    {
        //Setting the Storage Type
        List list = dao.retrieve(StorageType.class.getName(),
                "systemIdentifier", container.getStorageType()
                        .getSystemIdentifier());
        if (list.size() != 0)
        {
            StorageType type = (StorageType) list.get(0);
            container.setStorageType(type);
        }
    }

    private void setSiteForSubContainers(StorageContainer storageContainer, Site site)
    {
        if (storageContainer != null)
        {
        	Logger.out.debug("site() "+site.getSystemIdentifier());
        	Logger.out.debug("storageContainer.getChildrenContainerCollection() "+storageContainer.getChildrenContainerCollection().size());
            
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
                //Logger.out.debug("SUB CONTINER container "+parentContainerID.longValue()+" "+container.getSystemIdentifier().longValue()+"  "+(parentContainerID.longValue()==container.getSystemIdentifier().longValue()));
                if(parentContainerID.longValue()==container.getSystemIdentifier().longValue())
                	return true;
                if(isUnderSubContainer(container,parentContainerID))
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

    public int getNextContainerNumber(long parentID, long typeID,
            boolean isInSite) throws DAOException
    {
        String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
        String[] selectColumnName = {"max(CONTAINER_NUMBER) as MAX_NAME"};
        String[] whereColumnName = {"STORAGE_TYPE_ID", "PARENT_CONTAINER_ID"};
        String[] whereColumnCondition = {"=", "="};
        Object[] whereColumnValue = {Long.toString(typeID),
                Long.toString(parentID)};

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

        List list = dao.retrieve(sourceObjectName, selectColumnName,
                whereColumnName, whereColumnCondition, whereColumnValue,
                joinCondition);

        dao.closeSession();

        if (!list.isEmpty())
        {
            List columnList = (List) list.get(0);
            if (!columnList.isEmpty())
            {
                String str = (String) columnList.get(0);
                if (!str.equals(""))
                {
                    int no = Integer.parseInt(str);
                    return no + 1;
                }
            }
        }

        return 1;
    }

    public Vector getTreeViewData() throws DAOException
    {
    	//SRI: Made changes for performance enhancement of the 
    	//tree applet
        JDBCDAO dao = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
        dao.openSession(null);


        // Sri: Get all container, thier site details and storage type details
        String queryStr = "select t1.PARENT_CONTAINER_ID,t1.IDENTIFIER, " + 
        		"t1.SITE_ID, t2.NAME,t2.TYPE, t3.TYPE,CONTAINER_NUMBER " +
				"from CATISSUE_STORAGE_CONTAINER t1, " +
				"CATISSUE_SITE t2, " +
				"catissue_storage_type t3 " +
				"where t1.SITE_ID = t2.IDENTIFIER and t1.STORAGE_TYPE_ID = t3.IDENTIFIER";


        List list = null;
        try{
        list = dao.executeQuery(queryStr);
        }
        catch(Exception ex)
		{
		   throw new DAOException (ex.getMessage());
		}

        dao.closeSession();

        Vector vector = new Vector();
        if (list != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
            	List rowList = (List)list.get(i);
                StorageContainerTreeNode treeNode = new StorageContainerTreeNode();
                treeNode.setStorageContainerIdentifier(Long.valueOf((String)rowList.get(1)));
                treeNode.setStorageContainerName((String)rowList.get(6)); 
                treeNode.setStorageContainerType((String)rowList.get(5));
                if ((String)rowList.get(0) != "") // if parent is null in db
                {
                    treeNode.setParentStorageContainerIdentifier(Long.valueOf((String)rowList.get(0)));
                }
                treeNode.setSiteSystemIdentifier(Long.valueOf((String)rowList.get(2)));
                treeNode.setSiteName((String)rowList.get(3));
                treeNode.setSiteType((String)rowList.get(4));

                vector.add(treeNode);
            }
        }
        return vector;
    }

    
    public boolean[][] getStorageContainerFullStatus(DAO dao,
            Long systemIdentifier) throws DAOException
    {
        List list = dao.retrieve(StorageContainer.class.getName(),
                "systemIdentifier", systemIdentifier);
        boolean[][] fullStatus = null;

        if (!list.isEmpty())
        {
            StorageContainer storageContainer = (StorageContainer) list.get(0);
            Integer oneDimensionCapacity = storageContainer
                    .getStorageContainerCapacity().getOneDimensionCapacity();
            Integer twoDimensionCapacity = storageContainer
                    .getStorageContainerCapacity().getTwoDimensionCapacity();
            fullStatus = new boolean[oneDimensionCapacity.intValue()][twoDimensionCapacity
                    .intValue()];

            if (storageContainer.getChildrenContainerCollection() != null)
            {
                Iterator iterator = storageContainer
                        .getChildrenContainerCollection().iterator();
                while (iterator.hasNext())
                {
                    StorageContainer childStorageContainer = (StorageContainer) iterator
                            .next();
                    Integer positionDimensionOne = childStorageContainer
                            .getPositionDimensionOne();
                    Integer positionDimensionTwo = childStorageContainer
                            .getPositionDimensionTwo();
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo
                            .intValue()] = true;
                }
            }
        }
        return fullStatus;
    }

    private void disableSubStorageContainer(DAO dao,
            Long storageContainerIDArr[]) throws DAOException
    {
    	List listOfSpecimenIDs = getRelatedObjects(dao, Specimen.class, "storageContainer" ,storageContainerIDArr);
    	if(!listOfSpecimenIDs.isEmpty())
    	{
    		throw new DAOException(ApplicationProperties.getValue("errors.container.contains.specimen"));
    	}
    	
        List listOfSubStorageContainerId = super.disableObjects(dao,
                StorageContainer.class, "parentContainer", "CATISSUE_STORAGE_CONTAINER", "PARENT_CONTAINER_ID", storageContainerIDArr);

        if (listOfSubStorageContainerId.isEmpty())
            return;

        disableSubStorageContainer(dao, Utility.toLongArray(listOfSubStorageContainerId));
    }

}