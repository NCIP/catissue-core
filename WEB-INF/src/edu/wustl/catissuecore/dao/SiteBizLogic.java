/**
 * <p>Title: SiteHDAO Class>
 * <p>Description:	SiteHDAO is used to add site type information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.dao;

import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * SiteHDAO is used to add site type information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class SiteBizLogic extends AbstractBizLogic
{
	/**
     * Saves the storageType object in the database.
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	public void insert(Object obj) throws HibernateException,DAOException 
	{
		Site site = (Site)obj;

        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		dao.openSession();
		
		System.out.println("HERE...");
		List list = dao.retrieve(User.class.getName(), "systemIdentifier", site.getCoordinator().getSystemIdentifier());
		if (list.size() != 0)
		{
		    User user = (User) list.get(0);
		    site.setCoordinator(user);
		}
		
		dao.insert(site.getAddress());
	    dao.insert(site);
	    
	    dao.closeSession();
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException,HibernateException
    {
    }
}