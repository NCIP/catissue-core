/**
 * <p>Title: AbstractBizLogic Class>
 * <p>Description:	AbstractBizLogic is the base class of all the Biz Logic classes.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Vector;

import edu.wustl.common.util.dbManager.DAOException;


/**
 * AbstractBizLogic is the base class of all the Biz Logic classes.
 * @author gautam_shetty
 */
public abstract class AbstractBizLogic
{
    /**
     * Inserts an object into the database.
     * @param obj The object to be inserted.
     * @throws DAOException
     */
    public abstract void insert(Object obj) throws DAOException;    

    /**
     * Updates an objects into the database.
     * @param obj The object to be updated into the database. 
     * @throws DAOException
     */
    public abstract void update(Object obj) throws DAOException;
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed.
     * @param whereColumnName An array of field names.
     * @param whereColumnCondition The comparision condition for the field values. 
     * @param whereColumnValue An array of field values.
     * @param joinCondition The join condition.
     */
    public abstract List retrieve(String sourceObjectName, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition) throws DAOException;    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed.
     * @param colName Contains the field name.
     * @param colValue Contains the field value.
     */
    public abstract List retrieve(String className, String colName, Object colValue)
            throws DAOException;    
    /**
     * Retrieves all the records for class name in sourceObjectName.
     * @param sourceObjectName Contains the classname whose records are to be retrieved.
     */
    public abstract List retrieve(String sourceObjectName) throws DAOException;
    
    public abstract Vector getList(String sourceObjectName, String[] displayNameFields, String valueField, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition, String separatorBetweenFields) throws DAOException;
    
    public abstract Vector getList(String sourceObjectName, String[] displayNameFields, String valueField) 
    			throws DAOException;
}