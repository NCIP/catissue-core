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

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.catissuecore.util.global.Constants;

import edu.wustl.common.util.dbManager.DAOException;

/**
 * DistributionHDAO is used to add distribution information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class DistributionBizLogic extends DefaultBizLogic
{
	/**
     * Saves the Distribution object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
		Distribution dist = (Distribution)obj;
		
		//Load & set the User
		Object userObj = dao.retrieve(User.class.getName(), dist.getUser().getSystemIdentifier());
		if(userObj!=null)
		{
			User user = (User)userObj;
			dist.setUser(user);
		}
		
		//Load & set From Site
		List list = dao.retrieve(Site.class.getName(),Constants.SYSTEM_IDENTIFIER, dist.getFromSite().getSystemIdentifier());
		if(list!=null && !list.isEmpty())
		{
			Site site = (Site)list.get(0);
			dist.setFromSite(site);
		}

		//Load & set the To Site
		list = dao.retrieve(Site.class.getName(),Constants.SYSTEM_IDENTIFIER, dist.getToSite().getSystemIdentifier());
		if(list!=null && !list.isEmpty())
		{
			Site site = (Site)list.get(0);
			dist.setToSite(site);
		}

		dao.insert(dist,sessionDataBean, true, true);
		
		Collection distributedItemCollection = dist.getDistributedItemCollection();		
		Iterator it = distributedItemCollection.iterator();
		while(it.hasNext())
		{
			DistributedItem item = (DistributedItem)it.next();
			item.setDistribution(dist);
			dao.insert(item,sessionDataBean, true, true);
		}
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
     */
	protected void update(DAO dao, Object obj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
    {
		Distribution distribution = (Distribution)obj;
		dao.update(obj, sessionDataBean, true, true, false);
		
		Collection distributedItemCollection = distribution.getDistributedItemCollection();		
		Iterator it = distributedItemCollection.iterator();
		while(it.hasNext())
		{
			DistributedItem item = (DistributedItem)it.next();
			item.setDistribution(distribution);
			
			dao.update(item, sessionDataBean, true, true, false);
		}
    }
}