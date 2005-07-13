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
import edu.wustl.catissuecore.util.global.Constants;
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

    protected Transaction tx = null;
    
    public void openSession() throws DAOException
    {
        try
        {
            session = DBUtil.currentSession();
            tx = session.beginTransaction();
        }
        catch (HibernateException dbex)
        {
            throw handleException(dbex, "Object can not be added.", tx);
        }
    }

    public void closeSession() throws DAOException
    {
        try
        {
            tx.commit();
        }
        catch (HibernateException dbex)
        {
            dbex.printStackTrace();
            throw handleException(dbex, "Object can not be added.", tx);
        }
        finally
        {
            DBUtil.closeSession();
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
            Logger.out.error(hibExp.getMessage(),hibExp);
        }
    }

    /**
     * Handles Hibernate exceptions.
     * @param dbex The exception that has occured.
     * @param message The message which is to be printed.
     * @param tx The transaction in which the exception has occured.	 
     */
    private DAOException handleException(Exception dbex, String message,
            Transaction tx)
    {
        try
        {
            dbex.printStackTrace();
            if (tx != null)
                tx.rollback();
            return new DAOException(message, dbex);
        }
        catch (HibernateException hbe)
        {
            return new DAOException("Hibernate Error", hbe);
        }
    }

//    /**
//     * Inserts a persistent object into the database.
//     * @param obj The persistent object.
//     */
//    public void insert(Object obj) throws DAOException
//    {
//        Transaction tx = null;
//        try
//        {
//            session = DBUtil.currentSession();
//            tx = session.beginTransaction();
//
//            add(obj);
//
//            tx.commit();
//        }
//        catch (HibernateException dbex)
//        {
//            throw handleException(dbex, "Object can not be added.", tx);
//        }
//        finally
//        {
//            DBUtil.closeSession();
//        }
//    }

    //    /**
    //     * Retrieves all the records for class name in sourceObjectName.
    //     * @param sourceObjectName Contains the classname whose records are to be retrieved.
    //     */
    //    public List retrieve(String sourceObjectName) throws DAOException
    //    {
    //        return retrieve(sourceObjectName, null, null, null, null);
    //    }

    //    /**
    //     * Retrieves the records for class name in sourceObjectName according to field values passed.
    //     * @param colName Contains the field name.
    //     * @param colValue Contains the field value.
    //     */
    //    public List retrieve(String className, String colName, Object colValue)
    //            throws DAOException
    //    {
    //        String colNames[] = {colName};
    //        String colConditions[] = {"="};
    //        Object colValues[] = {colValue};
    //
    //        return retrieve(className, colNames, colConditions, colValues,
    //                Constants.AND_JOIN_CONDITION);
    //        //        try
    //        //        {
    //        //            session = DBUtil.currentSession();
    //        //            return retrieveInSameSession(className, colName, colValue);
    //        //        }
    //        //        catch (HibernateException dbex)
    //        //        {
    //        //            throw handleException(dbex, className + " can not be searched.",
    //        //                    null);
    //        //        }
    //        //        finally
    //        //        {
    //        //            DBUtil.closeSession();
    //        //        }
    //    }

    //    /**
    //     * Retrieves the records for class name in sourceObjectName according to field values passed.
    //     * @param whereColumnName An array of field names.
    //     * @param whereColumnCondition The comparision condition for the field values. 
    //     * @param whereColumnValue An array of field values.
    //     * @param joinCondition The join condition.
    //     */
    //    public List retrieve(String sourceObjectName, String[] whereColumnName,
    //            String[] whereColumnCondition, Object[] whereColumnValue,
    //            String joinCondition) throws DAOException
    //    {
    //        try
    //        {
    //            session = DBUtil.currentSession();
    //
    //            return retrieveInSameSession(sourceObjectName, whereColumnName,
    //                    whereColumnCondition, whereColumnValue, joinCondition);
    //
    //        }
    //        catch (HibernateException dbex)
    //        {
    //            throw handleException(dbex, "User can not be searched.", null);
    //        }
    //        finally
    //        {
    //            DBUtil.closeSession();
    //        }
    //    }

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

            Query query = null;
            StringBuffer sqlBuff = new StringBuffer("from " + sourceObjectName
                    + " ");

            if ((whereColumnName != null && whereColumnName.length > 0)
                    && (whereColumnCondition != null && whereColumnCondition.length == whereColumnName.length)
                    && (whereColumnValue != null && whereColumnName.length == whereColumnValue.length))
            {
                if (joinCondition == null)
                    joinCondition = Constants.AND_JOIN_CONDITION;

                String className = parseClassName(sourceObjectName);
                sqlBuff.append(className + " where ");

                //Adds the column name and search condition in where clause. 
                for (int i = 0; i < whereColumnName.length; i++)
                {
                    sqlBuff.append(className + "." + whereColumnName[i] + " ");
                    sqlBuff.append(whereColumnCondition[i] + " ? ");
                    if (i < (whereColumnName.length - 1))
                        sqlBuff.append(" " + joinCondition + " ");
                }

                Logger.out.debug(sqlBuff);

                query = session.createQuery(sqlBuff.toString());

                //Adds the column values in where clause
                for (int i = 0; i < whereColumnValue.length; i++)
                {
                    Logger.out.debug("whereColumnValue[i]. "
                            + whereColumnValue[i]);
                    query.setParameter(i, whereColumnValue[i]);
                }
            }
            else
            {
                query = session.createQuery(sqlBuff.toString());
            }
            list = query.list();
        }
        catch (HibernateException hibExp)
        {
//            Logger.out.error(hibExp.getMessage(),hibExp);
            hibExp.printStackTrace();
        }
        return list;
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#retrieve(java.lang.String, java.lang.String[])
     */
    public List retrieve(String sourceObjectName, String[] selectColumnName)
            throws DAOException
    {
        return null;
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
            return fullyQualifiedName.substring(fullyQualifiedName
                    .lastIndexOf(".") + 1);
        }
        catch (Exception e)
        {
            return fullyQualifiedName;
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
        catch(HibernateException hibExp)
        {
            Logger.out.error(hibExp.getMessage(),hibExp);
        }
        
    }

//    /**
//     * Updates the persistent object in the database.
//     * @param obj The object to be updated.
//     */
//    public void update(Object obj) throws DAOException
//    {
//        Transaction tx = null;
//        try
//        {
//            session = DBUtil.currentSession();
//            tx = session.beginTransaction();
//
//            edit(obj);
//
//            tx.commit();
//        }
//        catch (HibernateException dbex)
//        {
//            throw handleException(dbex, obj.getClass().getName()
//                    + " can not be added.", tx);
//        }
//        finally
//        {
//            DBUtil.closeSession();
//        }
//    }

    /**
     * Deletes the persistent object from the database.
     * @param obj The object to be deleted.
     */
    public boolean delete(Object obj) throws DAOException
    {
        Transaction tx = null;
        try
        {
            session = DBUtil.currentSession();
            tx = session.beginTransaction();

            session.delete(obj);

            tx.commit();
            return true;
        }
        catch (HibernateException dbex)
        {
            throw handleException(dbex, obj.getClass().getName()
                    + " can not be added.", tx);
        }
        finally
        {
            DBUtil.closeSession();
        }
    }
}