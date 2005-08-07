/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	NewSpecimenBizLogicHDAO is used to add new specimen information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * NewSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class NewSpecimenBizLogic extends DefaultBizLogic
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
		Specimen type = (Specimen)obj;
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