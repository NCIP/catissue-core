/**
 * 
 */
package edu.wustl.catissuecore.namegenerator;

import java.util.Properties;

/**
 * This class has functions to read LabelGenerator Properties file.
 * @author abhijit_naik
 *
 */
public class PropertyHandler {

	private static Properties nameGeneratorProperties = null;


	/**
	 * Load the Properties file.
	 * @param path
	 * @throws Exception
	 */
	public static void init(String path) throws Exception
	{
		nameGeneratorProperties = new Properties();
		nameGeneratorProperties.load(
			PropertyHandler.class.getClassLoader().getResourceAsStream(path));
		
	}
	

	/**
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String. 
	 * 
	 * @param propertyName
	 * @return String
	 * @throws Exception
	 */
	 
	public static String getValue(String propertyName) throws Exception
	{

		if (nameGeneratorProperties == null)
		{
			init("LabelGenerator.Properties");
		}
		return (String)nameGeneratorProperties.get(propertyName);

	}
}
