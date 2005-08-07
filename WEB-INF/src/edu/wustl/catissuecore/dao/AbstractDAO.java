/**
 * <p>Title: AbstractDAO Class>
 * <p>Description:	AbstractDAO class contains abstract methods which are used to manupulating or accessing () 
 * data to/from database.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */

package edu.wustl.catissuecore.dao;

import edu.wustl.common.util.dbManager.DAOException;

/**
 * AbstractDAO class contains abstract methods which are used to manupulating or accessing () 
 * data to/from database.
 * @author kapil_kaveeshwar  
 */
public abstract class AbstractDAO implements DAO
{
    /**
     * 
     * @throws DAOException
     */
    public abstract void openSession() throws DAOException;
    
    /**
     * 
     * @throws DAOException
     */
    public abstract void closeSession() throws DAOException;
    
    /**
     * Commit the database level changes  
     * @throws DAOException
     */
    public abstract void commit() throws DAOException;
    
    /**
     * Rollback all the changes after last commit.  
     * @throws DAOException
     */
    public abstract void rollback() throws DAOException;
}