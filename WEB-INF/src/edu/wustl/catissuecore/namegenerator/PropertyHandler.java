package edu.wustl.catissuecore.namegenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * This class has functions to read LabelGenerator Properties file.
 * @author abhijit_naik
 */
public class PropertyHandler
{
	/**
	 * Name generator properties.
	 */
	private static Properties nameGeneratorProperties = null;

	/**
	 * Load the Properties file.
	 * @param path Path
	 * @throws Exception Generic exception
	 */
	public static void init(String path) throws Exception
	{
		String absolutePath = CommonServiceLocator.getInstance().getPropDirPath() + File.separator + path;
		InputStream inpurStream = new FileInputStream(new File(absolutePath));
		nameGeneratorProperties = new Properties();
		nameGeneratorProperties.load(inpurStream);
	}

	/**
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String.
	 * @param propertyName Property Name
	 * @return String
	 * @throws Exception Generic exception
	 */

	public static String getValue(String propertyName) throws Exception
	{

		if (nameGeneratorProperties == null)
		{
			init("LabelGenerator.Properties");
		}
		return (String) nameGeneratorProperties.get(propertyName);

	}
}
