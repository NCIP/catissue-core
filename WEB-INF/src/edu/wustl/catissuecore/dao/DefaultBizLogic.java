/*
 * Created on Jul 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.dao;

import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultBizLogic extends AbstractBizLogic 
{
	/**
     * Inserts an object into the database.
     * @param obj The object to be inserted.
     * @throws DAOException
     */
    public void insert(Object obj) throws DAOException
    {
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        dao.openSession();
        dao.insert(obj);
        dao.closeSession();
    }
    
    /**
     * Updates an objects into the database.
     * @param obj The object to be updated into the database. 
     * @throws DAOException
     */
    public void update(Object obj) throws DAOException
    {
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        dao.openSession();
        dao.update(obj);
        dao.closeSession();
    }
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed.
     * @param whereColumnName An array of field names.
     * @param whereColumnCondition The comparision condition for the field values. 
     * @param whereColumnValue An array of field values.
     * @param joinCondition The join condition.
     */
    public List retrieve(String sourceObjectName, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition) throws DAOException
    {
        
        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
        
        List list = null;
        
        try
        {
            dao.openSession();
            
            list = dao.retrieve(sourceObjectName, null,
                    whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
        }
        catch(DAOException daoExp)
        {
            daoExp.printStackTrace();
            Logger.out.error(daoExp.getMessage(),daoExp);
        }
        finally
        {
            dao.closeSession();
        }
        
        return list;
    }
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed.
     * @param colName Contains the field name.
     * @param colValue Contains the field value.
     */
    public List retrieve(String className, String colName, Object colValue)
            throws DAOException
    {
        String colNames[] = {colName};
        String colConditions[] = {"="};
        Object colValues[] = {colValue};
        
        return retrieve(className, colNames, colConditions, colValues,
                Constants.AND_JOIN_CONDITION);
    }
    
    /**
     * Retrieves all the records for class name in sourceObjectName.
     * @param sourceObjectName Contains the classname whose records are to be retrieved.
     */
    public List retrieve(String sourceObjectName) throws DAOException
    {
        return retrieve(sourceObjectName, null, null, null, null);
    }
}
