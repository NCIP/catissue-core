package edu.wustl.catissuecore.testcase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * This class retrieves database credentials from caTissueInstall properties file. 
 * Hard code path of your caTissueInstall.properties file
 * @author Himanshu Aseeja
 *
 */

public class DataSourceFinder 
{
	/**
	 * String containing database type
	 */
	static String databaseType = null;
	/**
	 * String containing database name
	 */
	static String databaseName = null;
	/**
	 * String containing database username
	 */
	static String databaseUser = null;
	/**
	 * String containing database password
	 */
	static String databasePassword = null;
	/**
	 * String containing database host name
	 */
	static String databaseHost = null;
	/**
	 * String containing port no. at which the service is running
	 */
	static int port;
	
	public static void setAllValues() 
	{
		Properties properties = new Properties();
	    try 
	    {
	        properties.load(new FileInputStream("D:\\eclipse\\workspace\\caTissuecorev11p\\caTissueInstall.properties"));
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