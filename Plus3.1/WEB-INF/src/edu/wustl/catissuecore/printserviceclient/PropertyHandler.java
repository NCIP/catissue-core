
package edu.wustl.catissuecore.printserviceclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * This class has functions to read PrintServiceImplementor Properties file.
 * @author falguni sachde
 *
 */
public final class PropertyHandler
{

	/**
	 * Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(PropertyHandler.class);
	/**
	 * printimplClass Properties.
	 */
	private static Properties printimplClassProperties = null;

	/**
	 * private constructor.
	 */
	private PropertyHandler()
	{

	}

	/**
	 * @param path path.
	 * @throws Exception Exception
	 */
	public static void init(String path) throws Exception
	{
		try
		{
			final String absolutePath = CommonServiceLocator.getInstance().getPropDirPath()
					+ File.separator + path;
			final InputStream inpurStream = new FileInputStream(new File(absolutePath));
			printimplClassProperties = new Properties();
			printimplClassProperties.load(inpurStream);

			/*printimplClassProperties = new Properties();
			printimplClassProperties.
			load(PropertyHandler.class.getClassLoader().getResourceAsStream(path));*/

		}
		catch (final Exception e)
		{
			PropertyHandler.logger.error(e.getMessage(), e);
		}

	}

	/**
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String.
	 * @param propertyName  name of property Key
	 * @return String	property value
	 * @throws Exception Exception
	 */
	public static String getValue(String propertyName) throws Exception
	{

		if (printimplClassProperties == null)
		{
			init("PrintServiceImplementor.properties");
		}
		return (String) printimplClassProperties.get(propertyName);

	}
}
