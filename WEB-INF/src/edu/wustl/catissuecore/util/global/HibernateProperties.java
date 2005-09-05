/*
 * Created on Jul 19, 2004
 *
 */
package edu.wustl.catissuecore.util.global;

import java.util.ResourceBundle;

/**
 * This class is used to retrieve values of keys from the ApplicationResources.properties file.
 * @author kapil_kaveeshwar
 */
public class HibernateProperties
{
    private static ResourceBundle bundle; 
    
    public static void initBundle(String baseName)
    {
    	bundle = ResourceBundle.getBundle(baseName);
    }
	
	public static String getValue(String theKey)
	{
		return bundle.getString(theKey);
	}
}