package edu.wustl.catissuecore.domain.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Ion C. Olaru
 *         Date: 8/29/11 - 2:42 PM
 */
public class PropertiesLoader {

    private static Logger log = Logger.getLogger(PropertiesLoader.class);
	private static Properties properties = new Properties();
    private static final String propertiesFile = "edu/wustl/catissuecore/domain/catissueauth.properties";

	static {
		try {
            log.debug(">>> Loading properties from : " + propertiesFile);
			URL url = PropertiesLoader.class.getClassLoader().getResource(propertiesFile);
			properties.load(url.openStream());
		} catch (IOException e) {
            log.debug(">>> Loading properties failed.");
			e.printStackTrace();
            throw new RuntimeException("Error on loading property file: " + propertiesFile, e);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static String getCaTissueSuperUserUsername() {
		return properties.getProperty("superuser.username");
	}

	public static String getCaTissueSuperUserPassword() {
		return properties.getProperty("superuser.password");
	}

}
