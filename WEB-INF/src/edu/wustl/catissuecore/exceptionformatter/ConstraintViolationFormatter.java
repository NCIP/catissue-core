package edu.wustl.catissuecore.exceptionformatter;
/**
 * 
 * @author sachin_lale
 * Description: Class implementing Exception_Formatter interface method for Constarint_Violation_Exception  
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.text.MessageFormat;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.exceptions.CSTransactionException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

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
		
		if(Constants.DATABASE_IN_USED.equalsIgnoreCase("mysql")); 
		{
			errMessage = MySQLformatMessage(objExcp,args); // method for MYSQL Database
		}
		
		if(errMessage==null)
		{
			errMessage = " Database in used not specified " + Constants.GENERIC_DATABASE_ERROR; 
		}
		return errMessage;
	}
	private String MySQLformatMessage(Exception objExcp, Object[] args)
	{
		Logger.out.debug(objExcp.getClass().getName());
		if(objExcp instanceof gov.nih.nci.security.exceptions.CSTransactionException)
		{
			
			objExcp = (Exception)objExcp.getCause();
			Logger.out.debug(objExcp);
		}
		
		String tableName=null; // stores Table_Name for which column name to be found 
		String columnName=null; //stores Column_Name of table  
		String formattedErrMsg=null; // Formatted Error Message return by this method
		//SessionFactory sessionFactory=null;
		///Session session=null;
		Connection connection=null;
		if(args[0]!=null)
		{
			tableName = (String)args[0];
		}
		else
		{
			Logger.out.debug("Table Name not specified");
			System.out.println("Error Message: Table Name not given" );
			tableName=new String("Unknown Table");
		}
		Logger.out.debug("Table Name:" + tableName);
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
			
			// Create new Connection with database
			/*
			Configuration cfg = new Configuration();
			sessionFactory = cfg.buildSessionFactory();
			session = sessionFactory.openSession();
			*/
			//connection =session.connection();
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
			
        	int indexCount=1;
        	
        	while(rs.next())
        	{
				// In this loop, all the indexes are stored as key of the HashMap
				// and the column names are stored as value.
        		
        		columnName = rs.getString("COLUMN_NAME");
        		Logger.out.debug("Column Name: " + columnName);
        		Logger.out.debug("Key: " + indexCount);
        		if(key==indexCount)
        		{
        			found=true; // column name for given key index found
        			break;
        		}
        		indexCount++; // increment record count
        	}
        	Logger.out.debug("out of loop" );
        	// close the Database Connection
        	rs.close();
        //	connection.close();
        	//session.close();
        	
        	// Create arrays of object containing data to insert in CONSTRAINT_VOILATION_ERROR
        	Object[] arguments = new Object[2];
			arguments[0]=tableName;
			
			if(found)
        	{
        		arguments[1]=columnName; 	
        		Logger.out.debug("Column Name: " + columnName);
        	}
        	else
        	{
        		// Column_Name for given Key Index not found
        		arguments[1]="Unknown Column";  
        	}
			
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
