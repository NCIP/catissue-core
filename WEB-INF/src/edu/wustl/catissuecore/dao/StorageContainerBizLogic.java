/**
 * <p>Title: StorageContainerHDAO Class>
 * <p>Description:	StorageContainerHDAO is used to add Storage Container information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 23, 2005
 */

package edu.wustl.catissuecore.dao;

import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * StorageContainerHDAO is used to add site type information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class StorageContainerBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageContainer object in the database.
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	public void insert(Object obj) throws DAOException 
	{
		StorageContainer container = (StorageContainer)obj;
        
		AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		dao.openSession();
		
		int noOfContainers = container.getNoOfContainers().intValue();
		
			List list = dao.retrieve(StorageType.class.getName(), "systemIdentifier", container.getStorageType().getSystemIdentifier());
			if (list.size() != 0)
			{
				StorageType type = (StorageType) list.get(0);
				container.setStorageType(type);
			}

			if(container.getSite() != null)
			{
				list = dao.retrieve(Site.class.getName(), "systemIdentifier", container.getSite().getSystemIdentifier());
				if (list.size() != 0)
				{
					Site site = (Site) list.get(0);
					container.setSite(site);
				}
			}
		
			if(container.getParentContainer() != null)
			{
				list = dao.retrieve(StorageContainer.class.getName(), "systemIdentifier", container.getParentContainer().getSystemIdentifier());
				if (list.size() != 0)
				{
					StorageContainer pc = (StorageContainer) list.get(0);
					container.setParentContainer(pc);
					pc.getChildrenContainerCollection().add(container);
				}
			}
			
			for(int i=0;i<noOfContainers;i++)
			{
				StorageContainer cont = getCopy(container); 
				cont.setName(String.valueOf(i + container.getStartNo().intValue()));
				dao.insert(cont.getStorageContainerCapacity());
				dao.insert(cont);
				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&="+i);
			}
	    dao.closeSession();
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException
    {
    }
    
//    public String getNextStorageContainerNo(Site site, StorageType type )
//    {
//    	AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
//    	
//    	String whereColNames = {}
//    	dao.retrieve(StorageContainer.class.getName(),)
//    	
//    	return null;
//    }
    
    public int getNextContainerNumber(long siteID, long typeID ) throws DAOException
    {
    	String sourceObjectName = "CATISSUE_STORAGE_CONTAINER";
		String[] selectColumnName = {"max(NAME) MAX_NAME"};
        String[] whereColumnName = {"STORAGE_TYPE_ID","SITE_ID"};
		String[] whereColumnCondition = {"=","="};
		Object[] whereColumnValue = {Long.toString(typeID),Long.toString(siteID)};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		
		AbstractDAO dao = DAOFactory.getDAO(Constants.JDBC_DAO);
		
		dao.openSession();
		
		List list = dao.retrieve(sourceObjectName,selectColumnName,whereColumnName,whereColumnCondition,
				whereColumnValue,joinCondition);
		
		if(!list.isEmpty())
		{
			Object obj = list.get(0);
			if(obj != null)
			{
				String str = (String)obj;
				int no = Integer.parseInt(str);
				return no+1; 
			}
		}
		return 1;
    }
    
    private StorageContainer getCopy(StorageContainer oldContainer)
    {
    	StorageContainer newContainer = new StorageContainer();
    	newContainer.setActivityStatus(oldContainer.getActivityStatus());
    	newContainer.setParentContainer(oldContainer.getParentContainer());
    	newContainer.setSite(oldContainer.getSite());
    	newContainer.setStorageContainerCapacity(oldContainer.getStorageContainerCapacity());
    	newContainer.setStorageType(oldContainer.getStorageType());
    	newContainer.setTempratureInCentigrade(oldContainer.getTempratureInCentigrade());
    	return newContainer;
    }
}