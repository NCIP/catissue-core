/*
 * Created on Jul 19, 2004
 *
 */
package edu.wustl.catissuecore.util.global;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * This class is used to retrieve values of keys from the ApplicationResources.properties file.
 * @author kapil_kaveeshwar
 */
public class HibernateProperties
{
	private static Properties p;
    
    public static void initBundle(String baseName)
    {
    	try
		{
			File file = new File(baseName);
			BufferedInputStream stram = new BufferedInputStream(new FileInputStream(file));  
			p = new Properties();
			p.load(stram);
			stram.close();
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
			throw new RuntimeException(
				"Exception building HibernateProperties: " + exe.getMessage(), exe);
		}
    	
    	//ResourceBundle.
    }
	
	public static String getValue(String theKey)
	{
		return p.getProperty(theKey);
	}
}