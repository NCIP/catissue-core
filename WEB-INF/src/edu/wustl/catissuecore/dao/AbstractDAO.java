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

import java.util.List;

import edu.wustl.common.util.dbManager.DAOException;

/**
 * AbstractDAO class contains abstract methods which are used to manupulating or accessing () 
 * data to/from database.
 * @author kapil_kaveeshwar  
 */
public abstract class AbstractDAO
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
	 * Insert the Object in the database.
	 * @param obj Object to be inserted in database
	 */
	public abstract void insert (Object obj) throws DAOException;
	
	/**
	 * Retrive and returns the list of all source objects that satisfy the  
	 * for given conditions on a various columns.
	 * @param sourceObjectName Source object's name to be retrived from database.
	 * @param selectColumnName Column names in SELECT clause of the query.  
	 * @param whereColumnName Array of column name to be included in where clause.
	 * @param whereColumnCondition condition to be statify between column and its value.
	 * e.g. "=", "<", ">", "=<", ">=" etc 
	 * @param whereColumnValue Value of the column name that included in where clause.
	 * @param joinCondition join condition between two columns. (AND, OR) 
	 * @return the list of all source objects that satisfy the seasch conditions.     
	 */
	public abstract List retrieve (String sourceObjectName, String[] selectColumnName,
            String[] whereColumnName, String[] whereColumnCondition,
            Object[] whereColumnValue, String joinCondition) throws DAOException;
	
	/**
	 * Retrive and returns the list of all source objects for given 
	 * condition on a single column. The condition value 
	 * @param sourceObjectName Source object's name to be retrived from database.
	 * @param whereColumnName Column name to be included in where clause.
	 * @param whereColumnValue Value of the Column name that included in where clause.
	 * @return the list of all source objects for given condition on a single column.     
	 */
	public abstract List retrieve (String sourceObjectName, String whereColumnName, 
			   Object whereColumnValue) throws DAOException;

	/**
	 * Returns the list of all source objects available in database.
	 * @param sourceObjectName Source object's name to be retrived from database.
	 * @return the list of all source objects available in database.     
	 */
	public abstract List retrieve (String sourceObjectName) throws DAOException;
	
	/**
	 * Returns the list of all objects with the select columns specified.
	 * @param sourceObjectName Source object in the Database.
	 * @param selectColumnName column names in the select clause. 
	 * @return the list of all objects with the select columns specified.
	 * @throws DAOException
	 */
	public abstract List retrieve (String sourceObjectName, String[] selectColumnName) throws DAOException;
	
	/**
	 * updates the persisted object in the database.
	 * @param obj Object to be updated in database
	 */
	public abstract void update (Object obj) throws DAOException;
	
	/**
     * Deletes the persistent object from the database.
     * @param obj The object to be deleted.
     */
	public abstract boolean delete (Object obj)throws DAOException;	
}