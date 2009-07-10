
package edu.wustl.catissuecore.caties.util;

import java.util.ResourceBundle;

/**
 * This class is used to retrieve values of keys from the CaTIES.properties file.
 * @author vijay_pande
 *
 */
public class CaTIESProperties
{

	/**
	 * ResourceBunder object.
	 */
	private static ResourceBundle bundle;

	/**
	 * Initialization method to set resourcebundle to the bundle.
	 * @param baseName name of the properties file
	 */
	public static void initBundle(String baseName)
	{
		bundle = ResourceBundle.getBundle(baseName);
	}

	/**
	 * Method to get value of property from property file.
	 * @param theKey key for property
	 * @return String object containing value for given proprty
	 */
	public static String getValue(String theKey)
	{
		return bundle.getString(theKey);
	}
}
