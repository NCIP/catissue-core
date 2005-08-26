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

import edu.wustl.catissuecore.audit.AuditManager;
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
    protected AuditManager auditManager;
    
    public void openSession() throws DAOException
    {
    	auditManager = new AuditManager();
    	
    	try
    	{
    	    //Initializes the oracle driver.
            Class.forName(ApplicationProperties.getValue("database.driver"));

            String database = ApplicationProperties
            						.getValue("database.URL.1");
            String loginName = ApplicationProperties
                    				.getValue("database.loginName.1");
            String password = ApplicationProperties
            						.getValue("database.password.1");

            //Creates a connection.
            connection = DriverManager.getConnection(database, loginName, password);
    	}
    	catch(SQLException sqlExp)
    	{
    	    throw new DAOException(sqlExp.getMessage(),sqlExp);
    	}
    	catch(ClassNotFoundException classExp)
    	{
    	    throw new DAOException(classExp.getMessage(),classExp);
    	}
    }
    
    public void closeSession() throws DAOException
    {
        try
        {
            auditManager = null;
        	stmt.close();
        	if (connection != null)
        	    connection.close();
        }
        catch(SQLException sqlExp)
        {
            new DAOException(sqlExp.getMessage(),sqlExp);
        }
    }
    
    public void commit() throws DAOException
    {
        try
        {
        	auditManager.insert(this);
        	
        	if (connection != null)
        		connection.commit();
        }
        catch (SQLException dbex)
        {
        	Logger.out.error(dbex.getMessage(),dbex);
        	throw new DAOException("Error in commit", dbex);
        }
    }
    
    public void rollback() throws DAOException
    {
        try
        {
        	if (connection != null)
        		connection.rollback();
        }
        catch (SQLException dbex)
        {
        	Logger.out.error(dbex.getMessage(),dbex);
        	new DAOException("Error in rollback", dbex);
        }
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
            
            list = executeQuery(query.toString());
        }
        catch (ClassNotFoundException classExp)
        {
            Logger.out.error(classExp.getMessage(), classExp);
        }
        
        return list;
    }
    
    /**
     * Executes the query.
     * @param query
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List executeQuery(String query) throws ClassNotFoundException, DAOException
    {
        List list = null;
        try
        {
            stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
             
            list = new ArrayList();
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (resultSet.next())
            {
                int i = 1;
                List aList= new ArrayList();
                while (i <= columnCount)
                {
                	if(resultSet.getObject(i) != null)
                	{
                		aList.add(new String(resultSet.getObject(i).toString()));
                	}
                	i++;
                }
                
                list.add(aList);
            }
        }
        catch(SQLException sqlExp)
        {
            throw new DAOException(sqlExp.getMessage(), sqlExp);
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

    /**
     * (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#insert(java.lang.Object)
     */
    public void insert(String tableName, List columnValues) throws DAOException
    {

        StringBuffer query = new StringBuffer("INSERT INTO "+tableName+" values(");
        int i;
	    for (i=0;i<(Constants.DEFAULT_SPREADSHEET_COLUMNS.length-1);i++)
	    {
	        query.append("'"+columnValues.get(i)+"',");
	    }	
	        
	    query.append("'"+columnValues.get(i)+"');");
	        
	    executeUpdate(query.toString());
    }
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#update(java.lang.Object)
     */
    public void update(Object obj) throws DAOException
    {
        // TODO Auto-generated method stub
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.AbstractDAO#delete(java.lang.Object)
     */
    public void delete(Object obj) throws DAOException
    {
        // TODO Auto-generated method stub
        //return false;
    }
    
    public void create(String tableName, String[] columnNames) throws DAOException
    {
        StringBuffer query = new StringBuffer("CREATE TABLE "+tableName+" (");
        int i = 0;
        
        for (;i<(columnNames.length-1);i++)
        {
            query = query.append(columnNames[i]+" VARCHAR(50),");
        }
            
        query.append(columnNames[i]+" VARCHAR(50));");
        
        Logger.out.debug("Create Table*************************"+query.toString());
            
        executeUpdate(query.toString());
    }
    
    public void delete(String tableName) throws DAOException
    {
        StringBuffer query = new StringBuffer("DROP TABLE IF EXISTS "+tableName);
            
        executeUpdate(query.toString());
    }
    
    public Object retrieve (String sourceObjectName, Long systemIdentifier) throws DAOException
	{
		try
		{
			return null;
		}
	    catch (Exception hibExp)
	    {
	        Logger.out.error(hibExp.getMessage(),hibExp);
	        throw new DAOException("Error in retrieve " + hibExp.getMessage(),hibExp);
	    }
	}
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.dao.DAO#insert(java.lang.Object, boolean)
     */
    public void insert(Object obj, boolean isAuditable) throws DAOException
    {
        // TODO Auto-generated method stub
//		if(isAuditable)
//		auditManager.compare((AbstractDomainObject)obj,null,"INSERT");
    }
    
    private void executeUpdate(String query) throws DAOException
    {
        try
        {
            stmt = connection.prepareStatement(query
                    	.toString());
            
            stmt.executeUpdate();
        }
        catch(SQLException sqlExp)
        {
            throw new DAOException(sqlExp.getMessage(), sqlExp);
        }
    }
}