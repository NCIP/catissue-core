package edu.wustl.catissuecore.reportloader;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.PropertyConfigurator;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a poller which picks up the report files
 * and then pass them to the appropriate parser which parsers those files and import the data into datastore.  
 */
public class FilePoller implements Observable
{
	private Observer obr;
	


	/**
	 * @throws Exception
	 * Initializes the report processor. It initilizes the logging and reference data.  
	 */
	public void init()throws Exception
	{
		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		Logger.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
		PropertyConfigurator.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
		System.setProperty("gov.nih.nci.security.configFile",
				"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
		CDEManager.init();
		XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
		SiteInfoHandler.init(XMLPropertyHandler.getValue("site.info.filename"));
		ApplicationProperties.initBundle("ApplicationResources");
		
	}
	
	/**
	 * @param args
	 * start up method
	 */
	public static void main(String args[])
	{
		String files[]=null;
		File inputDir=null;
		File errorFileDir=null;
		File fileDir=null;
		FilePoller poller =null;
		try
		{
			poller = new FilePoller();
			poller.init();
			Observer obr=new ReportProcessor();
			poller.register(obr);
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.PROCESSED_FILE_DIR));
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.INPUT_DIR));
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.BAD_FILE_DIR));
		}
		catch(IOException ex)
		{
			Logger.out.error("Error while creating directories ",ex);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while creating directories ",ex);
		}
		try
		{	
			while(true)
			{
				inputDir = new File(XMLPropertyHandler.getValue(Parser.INPUT_DIR)); 
				files=	inputDir.list();
				 if(files.length>0)
				  {
				    	poller.obr.notifyEvent(files);
				  }
				Thread.sleep(Long.parseLong(XMLPropertyHandler.getValue(Parser.POLLER_SLEEP)));
			}
		}
		catch(Exception ex)
		{     
	  		Logger.out.error("Error while initializing parser manager ",ex);
		}	
	}
	
	public void register(Observer o)
	{
		this.obr=o;
	}

	
	public Observer getObr()
	{
		return obr;
	}

	
	public void setObr(Observer obr)
	{
		this.obr = obr;
	}
   
	
}
