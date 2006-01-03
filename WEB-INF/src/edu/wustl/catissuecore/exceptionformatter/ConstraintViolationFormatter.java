package edu.wustl.catissuecore.exceptionformatter;
/**
 * 
 * @author sachin_lale
 * Description: Class implementing Exception_Formatter interface method for Constarint_Violation_Exception  
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.HashMap;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.exception.ConstraintViolationException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.logger.Logger;

public class ConstraintViolationFormatter implements ExceptionFormatter 
{
	/*  
	 * @param objExcp - Exception object 
	 * @param args[] where args[0] must be String 'Table_Name' 
	 * and args[1] must be java.sql.Connection object get from session.getCOnnection().   
	 */
			
	public String formatMessage(Exception objExcp, Object[] args) 
	{
		String errMessage =null;
		
		// Check which database is in used and called appropriate Format_Method
		
		if(Variables.databaseName.equals(Constants.MYSQL_DATABASE)) 
		{
			errMessage = MySQLformatMessage(objExcp,args); // method for MYSQL Database
		}
		else
		{
			errMessage = OracleformatMessage(objExcp,args); // method for ORACLE Database
		}
		
		if(errMessage==null)
		{
			errMessage = " Database in used not specified " + Constants.GENERIC_DATABASE_ERROR; 
		}
		return errMessage;
	}
	private String OracleformatMessage(Exception objExcp, Object[] args) 
    {
        String tableName="";
        String columnName="";
        String formattedErrMsg=null; // Formatted Error Message return by this method
		Logger.out.debug(objExcp.getClass().getName());
        if(objExcp instanceof gov.nih.nci.security.exceptions.CSTransactionException)
        {
            
            objExcp = (Exception)objExcp.getCause();
            Logger.out.debug(objExcp);
        }
        try
        {
//        	 Get database metadata object for the connection
        	Connection connection=null;
        	if(args[1]!=null)
            {
                connection =(Connection)args[1];
            }
            else
            {
                Logger.out.debug("Error Message: Connection object not given");
            }
        	// Get Contraint Name from messages         		
        	String sqlMessage = generateErrorMessage(objExcp);
        	int tempstartIndexofMsg = sqlMessage.indexOf("(");
            
            String temp = sqlMessage.substring(tempstartIndexofMsg);
            int startIndexofMsg = temp.indexOf(".");
            int endIndexofMsg = temp.indexOf(")");
            String strKey =temp.substring((startIndexofMsg+1),endIndexofMsg);
            Logger.out.debug("Contraint Name: "+strKey);
           
            String Query = "select COLUMN_NAME,TABLE_NAME from user_cons_columns where constraint_name = '"+strKey+"'";
            Logger.out.debug("ExceptionFormatter Query: "+Query);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(Query);
            while(rs.next())
            {
                	columnName+=rs.getString("COLUMN_NAME")+",";
                	Logger.out.debug("columnName: "+columnName);
                	tableName=rs.getString("TABLE_NAME");
                	Logger.out.debug("tableName: "+tableName);
            }
            if(columnName.length()>0&&tableName.length()>0)
            {
            	columnName=columnName.substring(0,columnName.length()-1);
            	Logger.out.debug("columnName befor formatting: "+columnName);
            	String displayName = ExceptionFormatterFactory.getDisplayName(tableName,connection);
            	
            	Object[] arguments = new Object[]{displayName,columnName};
            	formattedErrMsg = MessageFormat.format(Constants.CONSTRAINT_VOILATION_ERROR,arguments);
            }	
            rs.close();
            statement.close();
         }
        catch(Exception e)
        {
            Logger.out.error(e.getMessage(),e);
            formattedErrMsg = Constants.GENERIC_DATABASE_ERROR;  
        }
        return formattedErrMsg;
    }
	private String MySQLformatMessage(Exception objExcp, Object[] args)
	{
		Logger.out.debug(objExcp.getClass().getName());
		if(objExcp instanceof gov.nih.nci.security.exceptions.CSTransactionException)
		{
			
			objExcp = (Exception)objExcp.getCause();
			Logger.out.debug(objExcp);
		}
		String dispTableName=null;
		String tableName=null; // stores Table_Name for which column name to be found 
		String columnName=null; //stores Column_Name of table  
		String formattedErrMsg=null; // Formatted Error Message return by this method

		Connection connection=null;
		if(args[0]!=null)
		{
			tableName = (String)args[0];
		}
		else
		{
			Logger.out.debug("Table Name not specified");
			tableName=new String("Unknown Table");
		}
		Logger.out.debug("Table Name:" + tableName);
		dispTableName=tableName;
		if(args.length>2)
		{
			if(args[2]!=null)
			{
				dispTableName = (String)args[2];
			}
			else
			{
				Logger.out.debug("Table Name not specified");
				dispTableName=tableName;
			}
		}	
		try
	     {
	            //get Class name from message "could not insert [classname]"
	            ConstraintViolationException  cEX = (ConstraintViolationException)objExcp;
	            String message = cEX.getMessage();
	            Logger.out.debug("message :"+message);
	            int startIndex = message.indexOf("[");
	            int endIndex = message.indexOf("]"); 
	            String className = message.substring((startIndex+1),endIndex);
	            Logger.out.debug("ClassName: "+className);
	            Class classObj = Class.forName(className);
	            // get table name from class 
	            tableName = HibernateMetaData.getRootTableName(classObj);
	     }
		 catch(Exception e)
		 {
			 Logger.out.error(e.getMessage(),e);
		 }
		try
		{
			// Generate Error Message by appending all messages of previous cause Exceptions
			String sqlMessage = generateErrorMessage(objExcp);
			
			// From the MySQL error msg and extract the key ID 
			// The unique key voilation message is "Duplicate entry %s for key %d"
			
			int key=-1;
			int indexofMsg=0;
			indexofMsg=sqlMessage.indexOf(Constants.MYSQL_DUPL_KEY_MSG);
			indexofMsg+=Constants.MYSQL_DUPL_KEY_MSG.length();
			
			// Get the %d part of the string
			String strKey =sqlMessage.substring(indexofMsg,sqlMessage.length()-2);
			key = Integer.parseInt(strKey);
		 	Logger.out.debug(String.valueOf(key));		
			
		 	// For the key extracted frm the string, get the column name on which the 
			// costraint has failed
			boolean found=false;
			// get connection from arguments
			if(args[1]!=null)
			{
				connection =(Connection)args[1];
			}
			else
			{
				Logger.out.debug("Error Message: Connection object not given");
			}
			
			
			// Get database metadata object for the connection
			DatabaseMetaData dbmd = connection.getMetaData();
			
			//  Get a description of the given table's indices and statistics
			ResultSet rs = dbmd.getIndexInfo(connection.getCatalog(), null,
					tableName, true, false);
			
        	HashMap indexDetails = new HashMap(); 
        	StringBuffer columnNames=new StringBuffer("");
			int indexCount = 1;
			String constraintVoilated = "";
        	while(rs.next())
        	{
				// In this loop, all the indexes are stored as key of the HashMap
				// and the column names are stored as value.
        		Logger.out.debug("Key: " + indexCount);
        		if(key==indexCount)
        		{
        			constraintVoilated=rs.getString("INDEX_NAME");
        			Logger.out.debug("Constraint: "+constraintVoilated);
        			found=true; // column name for given key index found
        			//break;
        		}
        		StringBuffer temp = (StringBuffer)indexDetails.get(rs.getString("INDEX_NAME"));
        		if(temp!=null)
        		{
        			temp.append(rs.getString("COLUMN_NAME"));
        			temp.append(",");
        			indexDetails.remove(rs.getString("INDEX_NAME"));
        			indexDetails.put(rs.getString("INDEX_NAME"),temp);
        			Logger.out.debug("Column :"+temp.toString());
        		}
        		else
        		{
        			temp = new StringBuffer(rs.getString("COLUMN_NAME"));
        			//temp.append(rs.getString("COLUMN_NAME"));
        			temp.append(",");
        			indexDetails.put(rs.getString("INDEX_NAME"),temp);
        		}
        		
        		indexCount++; // increment record count*/
        	}
        	Logger.out.debug("out of loop" );
        	if(found)
    		{
        		columnNames = (StringBuffer) indexDetails.get(constraintVoilated);
        		columnName=columnNames.toString();
        		Logger.out.debug("Column Name: " + columnNames.toString());
				Logger.out.debug("Constraint: "+constraintVoilated);
    		}
        	rs.close();
 	
        	// Create arrays of object containing data to insert in CONSTRAINT_VOILATION_ERROR
        	Object[] arguments = new Object[2];
        	dispTableName = ExceptionFormatterFactory.getDisplayName(tableName,connection);
			arguments[0]=dispTableName;
			columnName=columnNames.toString(); 
			columnName=columnName.substring(0,columnName.length()-1);
			arguments[1]=columnName; 	
			Logger.out.debug("Column Name: " + columnNames.toString());
			
			// Insert Table_Name and Column_Name in  CONSTRAINT_VOILATION_ERROR message   
			formattedErrMsg = MessageFormat.format(Constants.CONSTRAINT_VOILATION_ERROR,arguments);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			formattedErrMsg = Constants.GENERIC_DATABASE_ERROR;  
		}
		return formattedErrMsg;
		
	}
	private String generateErrorMessage(Exception ex)
	{
		String messageToAdd = "";
		if (ex instanceof HibernateException)
        {
            HibernateException hibernateException = (HibernateException) ex;
            StringBuffer message = new StringBuffer(messageToAdd);
            String str[] = hibernateException.getMessages();
            if (message != null)
            {
                for (int i = 0; i < str.length; i++)
                {
                	Logger.out.debug("str:" + str[i]);
                	message.append(str[i] + " ");
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
}
