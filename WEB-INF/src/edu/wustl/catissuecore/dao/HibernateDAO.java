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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.wustl.catissuecore.audit.AuditManager;
import edu.wustl.catissuecore.audit.Auditable;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.exception.AuditException;
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
    protected AuditManager auditManager;
    
    public void openSession() throws DAOException
    {
        try
        {
            session = DBUtil.currentSession();
            transaction = session.beginTransaction();
            
            auditManager = new AuditManager();
        }
        catch (HibernateException dbex)
        {
            Logger.out.error(dbex.getMessage(),dbex);
            new DAOException("Error in opening connection", dbex);
        }
    }

    public void closeSession() throws DAOException
    {
    	try
		{
	        DBUtil.closeSession();
		}
    	catch(HibernateException dx)
		{
    		Logger.out.error(dx.getMessage(),dx);
    		new DAOException("Error in closing connection", dx);
		}
        session = null;
        transaction = null;
        auditManager = null;
    }
    
    public void commit() throws DAOException
    {
        try
        {
        	auditManager.insert(this);
        	
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
    public void insert(Object obj,boolean isToAudit) throws DAOException
    {
        try
        {
        	session.save(obj);
        	
        	if(obj instanceof Auditable && isToAudit )
        		auditManager.compare((Auditable)obj,null,"INSERT");
        }
        catch(HibernateException hibExp)
        {
        	throw handleError(hibExp);
        }
        catch(AuditException hibExp)
        {
        	throw handleError(hibExp);
        }
    }
    
    private DAOException handleError(Exception hibExp)
    {
        Logger.out.error(hibExp.getMessage(),hibExp);
        String msg = generateErrorMessage(hibExp);
        return new DAOException(msg , hibExp);
    }
    
    private String generateErrorMessage(Exception ex)
    {
    	if(ex instanceof HibernateException)
    	{
    		HibernateException hibernateException = (HibernateException)ex;
		  	StringBuffer message = new StringBuffer();
		  	String str[] = hibernateException.getMessages();
		  	if(message!=null)
		  	{
		    	for (int i = 0; i < str.length; i++)
				{
		    		message.append(str[i]+" ");
				}
			}
			else
			{
				return "Unknown Error";
		  	}
		  	return message.toString();
    	}
	  	else
	  	{
	  		return ex.getMessage();
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
            
//            if(isAuditable)
//        		auditManager.compare((AbstractDomainObject)obj,null,"INSERT");
        }
        catch (HibernateException hibExp)
        {
            Logger.out.error(hibExp.getMessage(),hibExp);
            throw new DAOException("Error in update",hibExp);
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
            
//            if(isAuditable)
//        		auditManager.compare((AbstractDomainObject)obj,null,"INSERT");
        }
        catch (HibernateException hibExp)
        {
            Logger.out.error(hibExp.getMessage(),hibExp);
            throw new DAOException("Error in delete",hibExp);
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
    
	public Object retrieve (String sourceObjectName, Long systemIdentifier) throws DAOException
	{
		try
		{
			return session.load(Class.forName(sourceObjectName), systemIdentifier);
		}
		catch (ClassNotFoundException cnFoundExp)
	    {
	        Logger.out.error(cnFoundExp.getMessage(),cnFoundExp);
	        throw new DAOException("Error in retrieve " + cnFoundExp.getMessage(),cnFoundExp);
	    }
	    catch (HibernateException hibExp)
	    {
	        Logger.out.error(hibExp.getMessage(),hibExp);
	        throw new DAOException("Error in retrieve " + hibExp.getMessage(),hibExp);
	    }
	}
	
	public static void main(String[] args) throws Exception
	{
		Variables.catissueHome = System.getProperty("user.dir");
		Logger.configure("Application.properties");
		
    	HibernateDAO dao = new HibernateDAO();
    	
    	try
		{
    		dao.openSession();
    		
//    		Specimen specimen = new TissueSpecimen();
//    		Biohazard biohazard1 = (Biohazard)dao.retrieve(Biohazard.class.getName(),new Long(1));
//    		Biohazard biohazard2 = (Biohazard)dao.retrieve(Biohazard.class.getName(),new Long(2));
//    		
//    		specimen.getBiohazardCollection().add(biohazard1);
//    		specimen.getBiohazardCollection().add(biohazard2);
    		
//	    	dao.insert(specimen,false);
    		Department dept = new Department();
    		dept.setName("AABBCC1");
    		dao.insert(dept,false);
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