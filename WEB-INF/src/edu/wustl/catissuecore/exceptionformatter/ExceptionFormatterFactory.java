package edu.wustl.catissuecore.exceptionformatter;
/**
 * 
 * @author sachin_lale
 * Description: The Factory class to instatiate ExceptionFormatter object of given Exception    
 */

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
public class ExceptionFormatterFactory {
	static Properties prop = new Properties();
	static
	{
		try
		{
			/* Load ExceptionFormatter.properties file
			 * property file format is as follows:
			 * Exception_Class_Name = Exception_Formatter_Class_Name
			*/
			
			//prop.load(new FileInputStream("D:\\Example\\catissuecore\\WEB-INF\\classes\\ExceptionFormatter.properties"));
			prop.load(new FileInputStream(Variables.catissueHome+System.getProperty("file.separator")+"WEB-INF"+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"ExceptionFormatter.properties"));
			
			Logger.out.debug("File Loaded");
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage()+" " + e);
		}
	}
	
	// @param Exception excp : The fully qualified class name of excp 
	//  and the Exception_Formatter class name should be in ExceptionFormatter.properties file   
	public static ExceptionFormatter getFormatter(Exception excp)
	{
		ExceptionFormatter expFormatter=null;
		try
		{
			//Get Excxeption Class name from given Object
			String excpClassName = excp.getClass().getName(); 
			
			//Get Exception Formatter Class name from Properties file
			String formatterClassName = prop.getProperty(excpClassName);
			if(formatterClassName==null)
			{
				Logger.out.error("ExceptionFormatter Class not found for " + excpClassName);
			}
			else
			{
				//	Instantiate a Exception Formatter
				Logger.out.debug("exceptionClass: " +excpClassName);
				Logger.out.debug("formatterClass: " +formatterClassName);
				expFormatter = (ExceptionFormatter)Class.forName(formatterClassName).newInstance();
			}
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage()+" " + e);
		}
		return expFormatter;
	}
	public static String getDisplayName(String tableName,Connection  conn)
	{
		String displayName="";
		String sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where TABLE_NAME='"+tableName+"'";
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next())
			{
				displayName=rs.getString("DISPLAY_NAME");
				break;
			}
			rs.close();
			st.close();
		}
		catch(Exception ex)
		{
			Logger.out.error(ex.getMessage(),ex);
		}
		return displayName;
	}
}
