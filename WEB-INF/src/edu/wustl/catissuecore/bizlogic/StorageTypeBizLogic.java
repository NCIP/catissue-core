/**
 * <p>Title: StorageTypeHDAO Class>
 * <p>Description:	StorageTypeHDAO is used to add site type information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * StorageTypeHDAO is used to add site type information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class StorageTypeBizLogic extends DefaultBizLogic
{
	/**
     * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException  
	{
		StorageType type = (StorageType)obj;
		dao.insert(type.getDefaultStorageCapacity(),sessionDataBean, true, true);
	    dao.insert(type,sessionDataBean, true, true);
	}
	
	/**
     * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
    {
		StorageType type = (StorageType)obj;

		dao.update(type.getDefaultStorageCapacity(), sessionDataBean, true, true, false);
	    dao.update(type, sessionDataBean, true, true, false);
    }
}