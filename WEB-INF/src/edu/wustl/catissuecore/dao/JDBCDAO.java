/**
 * <p>Title: JDBCDAO Class>
 * <p>Description:	JDBCDAO is default implementation of AbstractDAO through JDBC.
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * Default implementation of AbstractDAO through JDBC.
 * @author gautam_shetty
 */
public class JDBCDAO extends AbstractDAO
{
    
    private PreparedStatement stmt = null;
    
    private Connection connection = null;
    
    public void openSession() throws DAOException
    {
        
    }
    
    public void closeSession() throws DAOException
    {
        
    }
    
    /**
     * Returns a connection to the database.
     * @return a connection to the database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection createConnection() throws ClassNotFoundException,
            SQLException
    {
        //Initializes the oracle driver.
        Class.forName(ApplicationProperties.getValue("database.driver"));

        String database = ApplicationProperties.getValue("database.URL.1");
        String loginName = ApplicationProperties
                .getValue("database.loginName.1");
        String password = ApplicationProperties.getValue("database.password.1");

        //Creates a connection.
        connection = DriverManager.getConnection(database, loginName, password);
        return connection;
    }
    
    /**
     * Returns the ResultSet containing all the rows in the table represented in sourceObjectName. 
     * @param sourceObjectName The table name.
     * @return The ResultSet containing all the rows in the table represented in sourceObjectName.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List retrieve(String sourceObjectName) throws DAOException
    {
        return retrieve(sourceObjectName, null, null, null, null, null);
    }
    
    /**
     * Returns the ResultSet containing all the rows according to the columns specified 
     * from the table represented in sourceObjectName.
     * @param sourceObjectName The table name.
     * @param selectColumnName The column names in select clause.
     * @return The ResultSet containing all the rows according to the columns specified 
     * from the table represented in sourceObjectName.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List retrieve(String sourceObjectName, String[] selectColumnName)
            throws DAOException
    {
        return retrieve(sourceObjectName, selectColumnName, null, null, null,
                null);
    }
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed in the passed session.
     * @param selectColumnName An array of field names in select clause.
     * @param whereColumnName An array of field names in where clause.
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
            StringBuffer query = new StringBuffer("SELECT ");

            if (joinCondition == null)
            {
                joinCondition = Constants.AND_JOIN_CONDITION;
            }

            //Prepares the select clause of the query.
            if ((selectColumnName != null) && (selectColumnName.length > 0))
            {
                int i;
                for (i = 0; i < (selectColumnName.length - 1); i++)
                {
                    query.append(selectColumnName[i] + " ");
                    query.append(",");
                }
                query.append(selectColumnName[i] + " ");
            }
            else
            {
                query.append("* ");
            }

            //Prepares the from clause of the query.
            query.append("FROM " + sourceObjectName);

            //Prepares the where clause of the query.
            if ((whereColumnName != null && whereColumnName.length > 0)
                    && (whereColumnCondition != null && whereColumnCondition.length == whereColumnName.length)
                    && (whereColumnValue != null && whereColumnName.length == whereColumnValue.length))
            {
                query.append(" WHERE ");
                int i;
                for (i = 0; i < (whereColumnName.length - 1); i++)
                {
                    query.append(sourceObjectName + "." + whereColumnName[i]
                            + " " + whereColumnCondition[i] + " "
                            + whereColumnValue[i]);
                    query.append(" " + joinCondition + " ");
                }
                query.append(sourceObjectName + "." + whereColumnName[i] + " "
                        + whereColumnCondition[i] + " " + whereColumnValue[i]);
            }

            //Creates connection.
            createConnection();

            System.out.println("query "+query);
            PreparedStatement stmt = connection.prepareStatement(query
                    .toString());
            ResultSet resultSet = stmt.executeQuery();
           
            list = new ArrayList();
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            List aList= new ArrayList();
            
            while (resultSet.next())
            {
                int i = 1;
                                   
                while (i <= columnCount)
                {
                	if(resultSet.getObject(i) != null)
                	{
                		aList.add(new String(resultSet.getObject(i).toString()));
                	}
                	i++;
                }
                
                for(i=0;i<aList.size();i++)
                {
                	list.add(aList.get(0));
                }
            }
        }
        catch (SQLException sqlExp)
        {
            Logger.out.error(sqlExp.getMessage(), sqlExp);
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.error(classExp.getMessage(), classExp);
        }

        return list;
    }
    
    public List retrieve(String sourceObjectName, String whereColumnName,
            Object whereColumnValue) throws DAOException
    {
        String whereColumnNames[] = {whereColumnName};
        String whereColumnConditions[] = {"="};
        Object whereColumnValues[] = {whereColumnValue};

        return retrieve(sourceObjectName, null, whereColumnNames,
                whereColumnConditions, whereColumnValues, Constants.AND_JOIN_CONDITION);
    }
    
    /**
     * Closes the Connection and PreparedStatement objects.  
     */
    protected void finalize() throws Throwable
    {
        try
        {
            stmt.close();
            connection.close();
        }
        finally
        {
            super.finalize();
        }
    }
    
    //    public static void main(String[] args)
    //    {
    //        JDBCDAO dao = new JDBCDAO();
    //        String sourceObjectName = "CATISSUE_QUERY_RESULTS";
    //        String[] selectColumnName = {"PARTICIPANT_ID","ACCESSION_ID","SPECIMEN_ID","SEGMENT_ID","SAMPLE_ID"};
    //        String[] whereColumnName = null;
    //        String[] whereColumnCondition = null;
    //        Object[] whereColumnValue = null;
    //        String joinCondition = null;
    //        try{
    ////            System.out.println(dao.retrieve(sourceObjectName));
    ////   		  System.out.println(dao.retrieve(sourceObjectName,selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,joinCondition));
    //            ResultSet rs = dao.retrieve(sourceObjectName, selectColumnName);
    //            System.out.println("Got o/p");
    //            rs.next();
    //            System.out.println("PID............"+rs.getString(Constants.QUERY_RESULTS_PARTICIPANT_ID));
    //            rs.close();
    //        }
    //        catch(Exception e){
    //        e.printStackTrace();    
    //        }
    //
    //    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#insert(java.lang.Object)
     */
    public void insert(Object obj) throws DAOException
    {
        // TODO Auto-generated method stub
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#update(java.lang.Object)
     */
    public void update(Object obj) throws DAOException
    {
        // TODO Auto-generated method stub
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#delete(java.lang.Object)
     */
    public boolean delete(Object obj) throws DAOException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
//    private 
}