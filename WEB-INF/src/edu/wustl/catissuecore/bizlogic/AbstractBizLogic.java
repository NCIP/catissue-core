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

import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.exception.BizLogicException;
import edu.wustl.catissuecore.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.catissuecore.exceptionformatter.ExceptionFormatter;
import edu.wustl.catissuecore.exceptionformatter.ExceptionFormatterFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;


/**
 * AbstractBizLogic is the base class of all the Biz Logic classes.
 * @author gautam_shetty
 */
public abstract class AbstractBizLogic
{
    /**
     * Inserts an object into the database.
     * @param obj The object to be inserted.
     * @param sessionDataBean TODO
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     */
    protected abstract void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException;    

    /**
     * Updates an objects into the database.
     * @param obj The object to be updated into the database. 
     * @param sessionDataBean TODO
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     */
    protected abstract void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException;
    
    /**
     * Validates the domain object for enumerated values.
     * @param obj The domain object to be validated. 
     * @param dao The DAO object
     * @param operation The operation(Add/Edit) that is to be performed.
     * @return True if all the enumerated value attributes contain valid values
     * @throws DAOException
     */
    protected abstract boolean validate(Object obj, DAO dao, String operation) throws DAOException;
    
    public abstract List retrieve(String sourceObjectName, String[] selectColumnName, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition) throws DAOException;
            
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
    
    public abstract List getList(String sourceObjectName, String[] displayNameFields, String valueField, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition, String separatorBetweenFields,  boolean isToExcludeDisabled) throws DAOException;
    
    public abstract List getList(String sourceObjectName, String[] displayNameFields, String valueField, boolean isToExcludeDisabled) 
    			throws DAOException;
    
    public final void insert(Object obj,SessionDataBean sessionDataBean, int daoType) throws BizLogicException, UserNotAuthorizedException
	{
		AbstractDAO dao = DAOFactory.getDAO(daoType);
		try
		{
	        dao.openSession(sessionDataBean);
	        validate(obj, dao, Constants.ADD);
	        insert(obj, dao, sessionDataBean);
	        dao.commit();
		}
		catch(DAOException ex)
		{
			
			String errMsg=formatException(ex.getWrapException(),obj,"Inserting");
			if(errMsg==null)
			{
				errMsg=ex.getMessage();
			}
			try
			{
				dao.rollback();
			}
			catch(DAOException daoEx)
			{
				throw new BizLogicException(daoEx.getMessage(), daoEx);
			}
			Logger.out.debug("Error in insert");
			throw new BizLogicException(errMsg, ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException();
			}
		}
	}
    
    public final void update(Object currentObj,Object oldObj,int daoType, SessionDataBean sessionDataBean) throws BizLogicException, UserNotAuthorizedException
	{
		AbstractDAO dao = DAOFactory.getDAO(daoType);
		try
		{
	        dao.openSession(sessionDataBean);
	        validate(currentObj, dao, Constants.EDIT);
	        update(dao, currentObj, oldObj, sessionDataBean);
	        dao.commit();
		}
		catch(DAOException ex)
		{
			//added to format constrainviolation message
			
			String errMsg=formatException(ex.getWrapException(),currentObj,"Updating");
			if(errMsg==null)
			{
				errMsg=ex.getMessage();
			}
			try
			{
				dao.rollback();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException(daoEx.getMessage(), daoEx);
				//throw new BizLogicException(ex.getMessage(), ex);
			}
			//TODO ERROR Handling
			throw new BizLogicException(errMsg, ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException();
			}
		}
	}
    
    public abstract List getRelatedObjects(DAO dao, Class sourceClass, String classIdentifier,Long objIDArr[])throws DAOException;
    
    protected abstract void setPrivilege(DAO dao,String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException,DAOException;
    
    public final void setPrivilege(int daoType,String privilegeName, Class objectType, Long[] objectIds, Long userId, SessionDataBean sessionDataBean, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, BizLogicException
    {
        AbstractDAO dao = DAOFactory.getDAO(daoType);
		try
		{
		    Logger.out.debug(" privilegeName:"+privilegeName+" objectType:"+objectType+" objectIds:"+edu.wustl.common.util.Utility.getArrayString(objectIds)+" userId:"+userId+" roleId:"+roleId+" assignToUser:"+assignToUser);
	        dao.openSession(sessionDataBean);
	        setPrivilege(dao, privilegeName,objectType,objectIds,userId, roleId, assignToUser, assignOperation);
	        dao.commit();
		}
		catch(DAOException ex)
		{
			try
			{
				dao.rollback();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException(daoEx.getMessage(), daoEx);
				//throw new BizLogicException(ex.getMessage(), ex);
			}
			//TODO ERROR Handling
			throw new BizLogicException(ex.getMessage(), ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException("Unknown Error");
			}
		}
    }

	/**
	 *  Method to check the ActivityStatus of the given identifier
	 * @param dao
	 * @param identifier of the Element
	 * @param className of the Element
	 * @param errorName Dispaly Name of the Element
	 * @throws DAOException
	 */
	protected void checkStatus(DAO dao, AbstractDomainObject ado , String errorName) throws DAOException 
    {
		if(ado !=null)
		{
			Long identifier = ado.getSystemIdentifier() ;
			if(identifier != null)
			{
				String className = ado.getClass().getName();  
				String activityStatus = dao.getActivityStatus(className,identifier);
				if(activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
				{
					throw new DAOException(errorName + " " + ApplicationProperties.getValue("error.object.closed"));
				}
			}
		}
    }
	private String formatException(Exception ex,Object obj,String operation)
	{
		String errMsg="";
		if(ex==null)
		{
			return null;
		}
		String roottableName=HibernateMetaData.getRootTableName(obj.getClass());
		String tableName=HibernateMetaData.getTableName(obj.getClass());
    	try
    	{   				
			// Get ExceptionFormatter
        	ExceptionFormatter ef = ExceptionFormatterFactory.getFormatter(ex);
			// call for Formating Message
			if(ef!=null)
			{
				Object[] arguments = {roottableName,DBUtil.currentSession().connection(),tableName};
				errMsg = ef.formatMessage(ex,arguments);
			}
			else
			{
				// if ExceptionFormatter not found Format message through Default Formatter 
				//String arg[]={operation,tableName};
	            //errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.SMException.01",arg);
				errMsg=ex.getMessage();
			}
    	}
    	catch(Exception e)
    	{
    		Logger.out.error(ex.getMessage(),ex);
    		// if Error occured while formating message then get message
    		// formatted through Default Formatter
    		String arg[]={operation,tableName};
            errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.SMException.01",arg);   
    	}
    	return errMsg;
	}
}