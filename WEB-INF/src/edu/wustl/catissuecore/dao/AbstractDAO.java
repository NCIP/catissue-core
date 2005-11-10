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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.catissuecore.util.global.Constants;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;


/**
 * AbstractDAO class contains abstract methods which are used to manupulating or accessing () 
 * data to/from database.
 * @author kapil_kaveeshwar  
 */
public abstract class AbstractDAO implements DAO
{
    /**
     * This method will be used to establish the session with the database.
     * 
     * @throws DAOException
     */
    public abstract void openSession(SessionDataBean sessionDataBean) throws DAOException;
    
    /**
     * This method will be used to close the session with the database.
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
    
    
    // Given the SQLException string and the table name, this method
    // returns the column on which the constraint has failed
    public String getIndexName(String tableName,String expMsg, Connection conn) throws DAOException
	{

    	try
		{
			expMsg = expMsg.trim(); // Trim the error message
	
			// From the MySQL error msg and extract the key ID 
			// The unique key voilation message is "Duplicate entry %s for key %d"
			
			// Search for "for key" in the error message
			int index = expMsg.indexOf(Constants.MYSQL_DUPL_KEY_MSG);  
			index += Constants.MYSQL_DUPL_KEY_MSG.length();
			// Get the %d part of the string
			int key = new Integer(expMsg.substring(index,expMsg.length()-1)).intValue(); 
	
			// For the key extracted frm the string, get the column name on which the 
			// costraint has failed
			HashMap indexDetails = new HashMap(); 
			int indexCount = 0;
			String constraintVoilated = "";
			
			// Get database metadata object for the connection
	    	DatabaseMetaData dbmd = conn.getMetaData();
			
	    	// Get the list of indexes on the table
	    	ResultSet rs = dbmd.getIndexInfo(conn.getCatalog(), null,
					tableName, true, false);
			
			while(rs.next())
			{
				// In this loop, all the indexes are stored as key of the HashMap
				// and the column names are stored as value.
				
				String constraintName = rs.getString("INDEX_NAME");
				StringBuffer columnNames;
				
				// In case of same index on multiple columns....
				if(indexDetails.containsKey(constraintName))
				{
					columnNames = (StringBuffer)indexDetails.get(constraintName);
					columnNames.append(","); // Multiple columns are CSV
				}
				else
				{
					// New index, create the Value object
					columnNames = new StringBuffer();
					indexDetails.put(constraintName,columnNames);
	
					indexCount++;
					if(indexCount == key)
					{
						// If this is the constraint voilated, then store the name
						constraintVoilated = constraintName;
					}
				}
				columnNames.append(rs.getString("COLUMN_NAME"));
			}
	
			// Get the columnnames of the constrait violation
			StringBuffer columnNames = (StringBuffer)indexDetails.get(constraintVoilated);
			return columnNames.toString();
		}//while (rs.next())

    	catch(Exception ex)
		{
			Logger.out.debug(ex.getMessage(), ex);
			throw new DAOException(ex.getMessage(), ex);
		}//catch(Exception ex)

	}//try
}