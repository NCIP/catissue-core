package edu.wustl.catissuecore.testcase.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * This class retrieves database credentials from caTissueInstall properties file. 
 * @author Himanshu Aseeja
 *
 */

public class DataSourceFinder 
{
	/**
	 * String containing database type
	 */
	public static String databaseType = null;
	/**
	 * String containing database name
	 */
	public static String databaseName = null;
	/**
	 * String containing database username
	 */
	public static String databaseUser = null;
	/**
	 * String containing database password
	 */
	public static String databasePassword = null;
	/**
	 * String containing database host name
	 */
	public static String databaseHost = null;
	/**
	 * String containing port no. at which the service is running
	 */
	public static int port;
	
	public static void setAllValues() 
	{
		Properties properties = new Properties();
	    try 
	    {
	        properties.load(new FileInputStream("caTissueInstall.properties"));
	    }
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }
	    
        databaseType = properties.getProperty("database.type");
        databaseName = properties.getProperty("database.name");
        databaseUser = properties.getProperty("database.username");
        databasePassword = properties.getProperty("database.password");
        port = Integer.parseInt(properties.getProperty("database.port"));
        databaseHost = properties.getProperty("database.host");
	}
}	