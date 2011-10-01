package edu.wustl.catissuecore.smoketest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import edu.wustl.testframework.util.TestCaseDataUtil;
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
//	    	 properties.load(new FileInputStream("E:/Program Files/Eclipse-Galileo/eclipse/workspace/catissuecore_/software/build/install.properties"));
//	    	File file = new File("./install.properties");
//	    	File file = new File(TestCaseDataUtil.getProperty("install.properties.file.path"));
	    	properties.load(new FileInputStream(TestCaseDataUtil.getProperty("install.properties.file.path")));
//	    	if(file.exists())
//	    	{
//	    		properties.load(new FileInputStream("install.properties"));
//	    	}
//	    	else
//	    	{
//	    		properties.load(new FileInputStream("./software/build/install.properties"));
//	    	}
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	    System.out.println("databaseType : "+databaseType);
        databaseType = properties.getProperty("database.type");
        databaseName = properties.getProperty("database.name");
        databaseUser = properties.getProperty("database.user");
        databasePassword = properties.getProperty("database.password");
        port = Integer.parseInt(properties.getProperty("database.port"));
        databaseHost = properties.getProperty("database.server");
	}
}