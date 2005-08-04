/**
 * <p>Title: HibernateDAO Class>
 * <p>Description:	HibernateDAO is default implemention of AbstractDAO through Hibernate ORM tool.
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 16, 2005
 */

package edu.wustl.catissuecore.dao;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * Default implemention of AbstractDAO through Hibernate ORM tool.
 * @author kapil_kaveeshwar
 */
public class HibernateDAO extends AbstractDAO
{
    protected Session session = null;
    protected Transaction transaction = null;
    
    public void openSession() throws DAOException
    {
        try
        {
            session = DBUtil.currentSession();
            //System.out.println("Auto Commit "+session.connection().getAutoCommit());
            transaction = session.beginTransaction();
        }
        catch (HibernateException dbex)
        {
            Logger.out.error(dbex.getMessage(),dbex);
            new DAOException("Error in open connection", dbex);
        }
    }

    public void closeSession()
    {
    	try
		{
	        DBUtil.closeSession();
		}
    	catch(Exception dx)
		{
    		Logger.out.error(dx.getMessage(),dx);
		}
        session = null;
        transaction = null;
    }
    
    public void commit() throws DAOException
    {
        try
        {
        	if (transaction != null)
        		transaction.commit();
        }
        catch (HibernateException dbex)
        {
        	Logger.out.error(dbex.getMessage(),dbex);
        	new DAOException("Error in commit", dbex);
        }
    }
    
    public void rollback() throws DAOException
    {
        try
        {
        	if (transaction != null)
                transaction.rollback();
        }
        catch (HibernateException dbex)
        {
        	Logger.out.error(dbex.getMessage(),dbex);
        	new DAOException("Error in rollback", dbex);
        }
    }

    /**
     * Saves the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException
     */
    public void insert(Object obj) throws DAOException
    {
        try
        {
            session.save(obj);
        }
        catch(HibernateException hibExp)
        {
        	generateExceptionMessage(hibExp,obj);
            Logger.out.error(hibExp.getMessage(),hibExp);
            throw new DAOException("Error in insert",hibExp);
        }
    }

    /**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException
    {
        try
        {
            session.update(obj);
        }
        catch (HibernateException hibExp)
        {
            Logger.out.error(hibExp.getMessage(),hibExp);
            throw new DAOException(hibExp.getMessage(),hibExp);
        }
    }

    /**
     * Deletes the persistent object from the database.
     * @param obj The object to be deleted.
     */
    public void delete(Object obj) throws DAOException
    {
        try
        {
            session.delete(obj);
        }
        catch (HibernateException hibExp)
        {
            Logger.out.error(hibExp.getMessage(),hibExp);
            throw new DAOException(hibExp.getMessage(),hibExp);
        }
    }
    
    /**
     * Retrieves all the records for class name in sourceObjectName.
     * @param sourceObjectName Contains the classname whose records are to be retrieved.
     */
    public List retrieve(String sourceObjectName) throws DAOException
    {
        return retrieve(sourceObjectName, null, null, null, null, null);
    }

    public List retrieve(String sourceObjectName, String whereColumnName, Object whereColumnValue)
            throws DAOException
    {
        String whereColumnNames[] = {whereColumnName};
        String colConditions[] = {"="};
        Object whereColumnValues[] = {whereColumnValue};

        return retrieve(sourceObjectName, null, whereColumnNames, colConditions, whereColumnValues,
                Constants.AND_JOIN_CONDITION);
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#retrieve(java.lang.String, java.lang.String[])
     */
    public List retrieve(String sourceObjectName, String[] selectColumnName)
            throws DAOException
    {
    	String[] whereColumnName = null;
    	String[] whereColumnCondition = null;
    	Object[] whereColumnValue = null;
    	String joinCondition = null;
    	return retrieve(sourceObjectName, selectColumnName,whereColumnName,
    			whereColumnCondition,whereColumnValue,joinCondition);
    }
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed in the passed session.
     * @param whereColumnName An array of field names.
     * @param whereColumnCondition The comparision condition for the field values. 
     * @param whereColumnValue An array of field values.
     * @param joinCondition The join condition.
     * @param The session object.
     */
    public List retrieve(String sourceObjectName, String[] selectColumnName,
            String[] whereColumnName, String[] whereColumnCondition,
            Object[] whereColumnValue, String joinCondition)
            throws DAOException
    {
        List list = null;
        try
        {
            StringBuffer sqlBuff = new StringBuffer();
            
            String className = parseClassName(sourceObjectName);
            
            if(selectColumnName != null && selectColumnName.length>0)
            {
                sqlBuff.append("Select ");
                for(int i = 0; i< selectColumnName.length;i++)
                {
                    sqlBuff.append(className+"."+selectColumnName[i]);
                    if(i != selectColumnName.length-1)
                    {
                        sqlBuff.append(", ");
                    }
                }
                sqlBuff.append(" ");
            }
            Logger.out.debug(" String : "+sqlBuff.toString());
            
            Query query = null;
            sqlBuff.append("from " + sourceObjectName
                    + " " + className);
            Logger.out.debug(" String : "+sqlBuff.toString());

            if ((whereColumnName != null && whereColumnName.length > 0)
                    && (whereColumnCondition != null && whereColumnCondition.length == whereColumnName.length)
                    && (whereColumnValue != null && whereColumnName.length == whereColumnValue.length))
            {
                if (joinCondition == null)
                    joinCondition = Constants.AND_JOIN_CONDITION;

                
                sqlBuff.append(" where ");

                //Adds the column name and search condition in where clause. 
                for (int i = 0; i < whereColumnName.length; i++)
                {
                    sqlBuff.append(className + "." + whereColumnName[i] + " ");
                    sqlBuff.append(whereColumnCondition[i] + " ? ");
                    if (i < (whereColumnName.length - 1))
                        sqlBuff.append(" " + joinCondition + " ");
                }

                System.out.println(sqlBuff.toString());

                query = session.createQuery(sqlBuff.toString());

                //Adds the column values in where clause
                for (int i = 0; i < whereColumnValue.length; i++)
                {
                    Logger.out.debug("whereColumnValue[i]. " + whereColumnValue[i]);
                    query.setParameter(i, whereColumnValue[i]);
                }
            }
            else
            {
                query = session.createQuery(sqlBuff.toString());
            }
            
            list = query.list();
            
            Logger.out.debug(" String : "+sqlBuff.toString());
        }
        
        catch (HibernateException hibExp)
        {
            Logger.out.error(hibExp.getMessage(),hibExp);
            throw new DAOException("Error in retrieve " + hibExp.getMessage(),hibExp);
        }
        catch (Exception exp)
        {
            Logger.out.error(exp.getMessage(), exp);
            throw new DAOException("Logical Erroe in retrieve method "+exp.getMessage(), exp);
        }
        return list;
    }
    
    
    /**
     * Parses the fully qualified classname and returns only the classname.
     * @param fullyQualifiedName The fully qualified classname. 
     * @return The classname.
     */
    private String parseClassName(String fullyQualifiedName)
    {
        try
        {
            return fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf(".") + 1);
        }
        catch (Exception e)
        {
            return fullyQualifiedName;
        }
    }
    
    private void generateExceptionMessage(HibernateException dbex,Object obj)
    {
    	Throwable t = dbex.getCause();
    	System.out.println("Cause "+t.getMessage());
    	String msg[] = dbex.getMessages();
    	for (int i = 0; i < msg.length; i++)
		{
    		System.out.println(i+" : "+msg[i]);
    		
		}
    }
    public static void main(String[] args) //throws Exception
	{
		Variables.catissueHome = System.getProperty("user.dir");
		Logger.configure("Application.properties");
		
    	HibernateDAO dao = new HibernateDAO();
    	
    	try
		{
    		dao.openSession();
	    	Department dept = new Department();
	    	dept.setName("AAAAA");
	    	dao.insert(dept);
	    	dao.commit();
		}
    	catch(DAOException ex)
		{
    		ex.printStackTrace();
    		try
			{
    			dao.rollback();
			}
    		catch(DAOException sex)
			{
    			
			}
		}
    	dao.closeSession();
	}
}