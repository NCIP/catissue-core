/**
 * <p>Title: SiteHDAO Class>
 * <p>Description:	SiteHDAO is used to add site type information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * SiteHDAO is used to add site type information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class SiteBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		Site site = (Site)obj;
		
		checkStatus(dao, site.getCoordinator(), "Coordinator");
		
		Set protectionObjects = new HashSet();
		
		setCordinator(dao,site);
		
		dao.insert(site.getAddress(),sessionDataBean, true, true);
	    dao.insert(site,sessionDataBean, true, true);
	    protectionObjects.add(site);
	    try
        {
            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,null);
        }
        catch (SMException e)
        {
            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
        }
		
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
		Site site = (Site)obj;
		Site siteOld = (Site)oldObj;
		
		if(!site.getCoordinator().getSystemIdentifier().equals(siteOld.getCoordinator().getSystemIdentifier()))
			checkStatus(dao, site.getCoordinator(), "Coordinator");
		
		setCordinator(dao,site);
		
		dao.update(site.getAddress(), sessionDataBean, true, true, false);
	    dao.update(site, sessionDataBean, true, true, false);
    }
	
	// This method sets the cordinator for a particular site.
	private void setCordinator(DAO dao,Site site) throws DAOException
	{
		List list = dao.retrieve(User.class.getName(), "systemIdentifier", site.getCoordinator().getSystemIdentifier());
		
		if (list.size() != 0)
		{
		    User user = (User) list.get(0);
		    site.setCoordinator(user);
		}
	}
}