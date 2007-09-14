package edu.wustl.catissuecore.caties.util;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

public class Utility 
{
	/**
	 * Generic Initialization process for caTIES servers
	 * @throws Exception
	 */
	public static void init()throws Exception
	{
		// Initialization methods
		Variables.applicationHome = System.getProperty("user.dir");
		//Logger.out = org.apache.log4j.Logger.getLogger("");
		// Configuring common logger
		Logger.configure(CaTIESConstants.LOGGER_GENERAL);
		// Configuring logger properties
		PropertyConfigurator.configure(Variables.applicationHome + File.separator+"logger.properties");
		// initializing caties property configurator
		CaTIESProperties.initBundle("caTIES");
		// initializing SiteInfoHandler to read site names from site configuration file
		SiteInfoHandler.init(CaTIESProperties.getValue(CaTIESConstants.SITE_INFO_FILENAME));
	}
}
