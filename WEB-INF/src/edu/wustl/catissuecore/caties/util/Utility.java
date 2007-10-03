package edu.wustl.catissuecore.caties.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

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
	
	
	public static Map initializeReportSectionHeaderMap(String configFileName) throws Exception
	{
		HashMap<String,String> abbrToHeader = new LinkedHashMap <String,String>();
		Logger.out.info("Initializing section header map");
		// Function call to set up section header configuration from SectionHeaderConfig.txt file
		return(setSectionHeaderPriorities(abbrToHeader, configFileName));
	}
	
	/**
	 * This nethod sets the priority order and full name of the abrreviated section name
	 * which is used by the synthesizeSPRText method
	 * @throws Exception Generic exception
	 */
	private static Map setSectionHeaderPriorities(HashMap<String,String> abbrToHeader, String configFileName) throws Exception
	{
		try 
		{
			// set bufferedReader to read file
			BufferedReader br = new BufferedReader(new FileReader(configFileName));

			String line = "";
			StringTokenizer st;
			String name;
			String abbr;
			String prty;
			// iterate while file EOF
			while ((line = br.readLine()) != null) 
			{
				// sepearete values for section header name, abbreviation of section header and its priority
				st = new StringTokenizer(line, "|");
				name = st.nextToken().trim();
				abbr = st.nextToken().trim();
				prty = st.nextToken().trim();
				
				// add abbreviation to section header maping in hash map
				abbrToHeader.put(abbr, name);
			}
			Logger.out.info("Section Headers set successfully to the map");
		}
		catch (IOException ex) 
		{
			Logger.out.error("Error in setting Section header Priorities",ex);
			throw new Exception(ex.getMessage());
		}
		return abbrToHeader;
	}
}
