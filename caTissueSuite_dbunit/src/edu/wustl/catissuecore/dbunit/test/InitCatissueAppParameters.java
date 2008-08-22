/**
 * @Class InitCatissueAppParameters.java
 * @Author abhijit_naik
 * @Created on Aug 22, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;


/**
 * @author abhijit_naik
 *
 */
public class InitCatissueAppParameters
{
	private static boolean initComplete= false;
	public void initTestData(Properties props) throws Exception
	{
			System.out.println("test cases...");
			InputStream inStream = CaTissueBaseDBUnitTestCase.class.getClassLoader().getResourceAsStream("caTissueDBUnit.properties");
			Variables.applicationHome = ".";
			props.load(inStream);
			//System.setProperties(props);
			inStream.close();
			inStream = null;
			System.setProperty("gov.nih.nci.security.configFile",props.getProperty("gov.nih.nci.security.configFile"));
			System.setProperty("app.propertiesFile",props.getProperty("app.propertiesFile"));
			if (!initComplete)
			{
				initComplete = true;
				System.out.println("Started class loading...");
				Class.forName(DBUtil.class.getName());
				System.out.println("Class loading finished!!!");
				CatissueCoreServletContextListener init = new CatissueCoreServletContextListener();
		    	Logger.configure("");
				String path = System.getProperty("app.propertiesFile");
		    	XMLPropertyHandler.init(path);
		    	File propetiesDirPath = new File(path);
		    	Variables.propertiesDirPath = propetiesDirPath.getParent();
		    	if (Variables.propertiesDirPath.startsWith("file:\\"))
		    	{
		    		int beginIndex = "file:\\".length();
		    		Variables.propertiesDirPath= Variables.propertiesDirPath.substring(beginIndex);
		    	}
	
				init.initCatissueParams();
			}
	}

}
