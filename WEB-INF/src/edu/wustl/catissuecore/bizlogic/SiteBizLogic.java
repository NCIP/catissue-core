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

import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
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
     * @param session The session in which the object is saved.
     * @param obj The storageType object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException
	{
		Site site = (Site)obj;

		Logger.out.debug("site.getCoordinator().getSystemIdentifier() "+site.getCoordinator().getSystemIdentifier());
		List list = dao.retrieve(User.class.getName(), "systemIdentifier", site.getCoordinator().getSystemIdentifier());
		Logger.out.debug("list "+list.size());
		if (list.size() != 0)
		{
		    User user = (User) list.get(0);
		    site.setCoordinator(user);
		}
		
		dao.insert(site.getAddress());
	    dao.insert(site);
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
    }
}