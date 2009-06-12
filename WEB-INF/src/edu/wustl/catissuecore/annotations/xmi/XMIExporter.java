/*
 * Created on Sep 27, 2007
 * @author
 *
 */
package edu.wustl.catissuecore.annotations.xmi;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * @author preeti_lodha
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XMIExporter
{
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(XMIExporter.class);
	public static final String XMI_VERSION_1_1 = "1.1";
	public static final String XMI_VERSION_1_2 = "1.2";
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		

		try
		{
		if(args.length<3)
		{
			logger.info("Please specify all parameters. '-Dgroupname <groupname> -Dfilename <export filename> -Dversion <version>'");
			throw new Exception("Please specify all parameters. '-Dgroupname <groupname> -Dfilename <export filename> -Dversion <version>'");
		}
		String groupName = args[0];
		if(groupName==null)
		{
			logger.info("Please specify groupname to be exported");
			throw new Exception("Please specify groupname to be exported");
		}
		else
		{
			String filename = args[1]; 
			if(filename==null)
			{
				logger.info("Kindly specify the filename where XMI should be exported.");
				throw new Exception("Kindly specify the filename where XMI should be exported.");
			}
			else
			{
				String xmiVersion = args[2]; 
				if(xmiVersion==null)
				{
					logger.info("Export version not specified. Exporting as XMI 1.2");
					System.out.println("Export version not specified. Exporting as XMI 1.2");
					xmiVersion = XMI_VERSION_1_2;
				}
				edu.common.dynamicextensions.xmi.exporter.XMIExporter xmiExporter = new edu.common.dynamicextensions.xmi.exporter.XMIExporter();
				EntityGroupInterface entityGroup = XMIUtility.getEntityGroup(groupName);
				if (entityGroup!=null && XMI_VERSION_1_1.equals(xmiVersion))
				{
					XMIUtility.addHookEntitiesToGroup(entityGroup);
				}
				if(entityGroup==null)
				{
					logger.info("Specified group does not exist. Could not export to XMI");
					throw new Exception("Specified group does not exist. Could not export to XMI");
				}
				else
				{
					xmiExporter.exportXMI(filename, entityGroup, xmiVersion);
				}
			}

		}
		}catch(Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

	}

}
