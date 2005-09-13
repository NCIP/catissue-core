/**
 * <p>Title: DistributionHDAO Class>
 * <p>Description:	DistributionHDAO is used to add distribution information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Aug 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * DistributionHDAO is used to add distribution information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class DistributionBizLogic extends DefaultBizLogic
{
	/**
     * Saves the Distribution object in the database.
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException 
	{
		Distribution dist = (Distribution)obj;
		
		//Load & set the User
		List list = dao.retrieve(User.class.getName(), "systemIdentifier", dist.getUser().getSystemIdentifier());
		
		if(list!=null && list.size()!=0)
		{
			User user = (User)list.get(0);
			dist.setUser(user);
		}
		
		//Load & set From Site
		list = dao.retrieve(Site.class.getName(),"systemIdentifier", dist.getFromSite().getSystemIdentifier());
		
		if(list!=null && list.size()!=0)
		{
			Site site = (Site)list.get(0);
			dist.setFromSite(site);
		}

		//Load & set the To Site
		list = dao.retrieve(Site.class.getName(),"systemIdentifier", dist.getToSite().getSystemIdentifier());
		
		if(list!=null && list.size()!=0)
		{
			Site site = (Site)list.get(0);
			dist.setToSite(site);
		}

		dao.insert(dist,true);
		
		Collection distributedItemCollection = dist.getDistributedItemCollection();		
		Iterator it = distributedItemCollection.iterator();
		
		while(it.hasNext())
		{
			DistributedItem item = (DistributedItem)it.next();
			item.setDistribution(dist);
			dao.insert(item,true);
		}
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	protected void update(DAO dao, Object obj) throws DAOException 
    {
		Distribution distribution = (Distribution)obj;
		dao.update(obj);
		
		Collection distributedItemCollection = distribution.getDistributedItemCollection();		
		Iterator it = distributedItemCollection.iterator();
		while(it.hasNext())
		{
			DistributedItem item = (DistributedItem)it.next();
			item.setDistribution(distribution);
			
			dao.update(item);
		}
    }
}