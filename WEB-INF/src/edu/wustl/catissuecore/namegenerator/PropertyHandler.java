/**
 * 
 */
package edu.wustl.catissuecore.namegenerator;

import java.util.Properties;

/**
 * @author abhijit_naik
 *
 */
public class PropertyHandler {

	private static Properties nameGeneratorProperties = null;


	public static void init(String path) throws Exception
	{
		nameGeneratorProperties = new Properties();
		nameGeneratorProperties.load(
			PropertyHandler.class.getClassLoader().getResourceAsStream(path)										);
		
	}
	/**
	 * <p>
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String. 
	 * </p>
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
