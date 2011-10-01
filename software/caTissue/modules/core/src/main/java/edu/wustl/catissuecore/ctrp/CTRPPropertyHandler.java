package edu.wustl.catissuecore.ctrp;

import java.io.InputStream;
import java.util.Properties;

/****
 * @author Ravi Batchu
 */
public class CTRPPropertyHandler {
	/**
	 * private constructor.
	 */
	private CTRPPropertyHandler() {
	}

	private static Properties crtpProperties;

	/**
	 * Load the Properties file.
	 * 
	 * @param path
	 *            Path
	 * @throws Exception
	 *             Generic exception
	 */
	public static void init() throws Exception {
		InputStream inputStream = CTRPPropertyHandler.class.getClassLoader()
				.getResourceAsStream(CTRPConstants.CTRP_PROPERTIES_FILE);
		if (inputStream == null) {
			throw new Exception("Properties file not found:"
					+ CTRPConstants.CTRP_PROPERTIES_FILE);
		}
		crtpProperties = new Properties();
		crtpProperties.load(inputStream);
	}

	/**
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String.
	 * 
	 * @param propertyName
	 *            Property Name
	 * @return String
	 * @throws Exception
	 *             Generic exception
	 */

	public static String getProperty(String propertyName) throws Exception {
		init();
		if (crtpProperties == null) {
			init();
		}
		return (String) crtpProperties.get(propertyName);
	}
}
