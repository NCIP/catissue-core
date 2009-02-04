package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
		//Initialization methods
		Variables.applicationHome = System.getProperty("user.dir");
		//Logger.out = org.apache.log4j.Logger.getLogger("");
		//Configuring common logger
		Logger.configure(Parser.LOGGER_GENERAL);
		// Configuring CSV logger
		CSVLogger.configure(Parser.LOGGER_FILE_POLLER);
		//Configuring logger properties
		PropertyConfigurator.configure(Variables.applicationHome + File.separator+"logger.properties");
		// Setting properties for UseImplManager
		System.setProperty("gov.nih.nci.security.configFile",
				"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
		// initializing cache manager
		CDEManager.init();
		//initializing XMLPropertyHandler to read properties from caTissueCore_Properties.xml file
		XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
		// initializing SiteInfoHandler to read site names from site configuration file
		SiteInfoHandler.init(XMLPropertyHandler.getValue("site.info.filename"));
		ApplicationProperties.initBundle("ApplicationResources");
		// required for valdation in bizLogic
		edu.wustl.catissuecore.util.global.Variables.isLoadFromCaties=true;
	}
	
	/**
	 * @param args
	 * start up method
	 */
	public static void main(String[] args)
	{
		String[] files=null;
		File inputDir=null;
		FilePoller poller =null;
		
		try
		{
			poller = new FilePoller();
			// Initializing file poller
			poller.init();
			CSVLogger.info(Parser.LOGGER_FILE_POLLER," Date/Time, FileName, Report Loder Queue ID, Status, Message");
			CSVLogger.info(Parser.LOGGER_FILE_POLLER,"");
			
			Observer obr=new ReportProcessor();
			// registering poller to the object obr
			poller.register(obr);
			// Create new directories if does not exists
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.PROCESSED_FILE_DIR));
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.INPUT_DIR));
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.BAD_FILE_DIR));
			// Thread for stopping file poller server
			Thread th=new Thread()
			{
				public void	run()
				{
					try
					{
						int port=Integer.parseInt(XMLPropertyHandler.getValue("filepollerport"));
						ServerSocket serv = new ServerSocket(port);
					  	BufferedReader r;
				    	Socket sock = serv.accept();
				    	r =new BufferedReader (new InputStreamReader (sock.getInputStream()));
				    	PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()),true);
				    	String str=r.readLine();
				    	Logger.out.info("Stopping server");
				    	r.close();
				    	sock.close(); 
					    serv.close();
					    System.exit(0);
						}
						catch(Exception e)
						{
							Logger.out.error("Error stopping server ",e);
						}
					}
				};			
				th.start();	     	      	
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
			// Loop to contineusly poll on directory for new incoming files
			while(true)
			{
				inputDir = new File(XMLPropertyHandler.getValue(Parser.INPUT_DIR)); 
				files=	inputDir.list();
				 if(files.length>0)
				 {
					 Logger.out.info("Invoking parser to parse input file");
					 // this invokes ReportProcessor thread
					 poller.obr.notifyEvent(files);
				 }
				 Logger.out.info("Report Loader Server is going to sleep for "+XMLPropertyHandler.getValue(Parser.POLLER_SLEEP)+"ms");
				 Thread.sleep(Long.parseLong(XMLPropertyHandler.getValue(Parser.POLLER_SLEEP)));
			}
		}
		catch(Exception ex)
		{     
	  		Logger.out.error("Error while initializing parser manager ",ex);
		}	
	}
	
	/** 
	 * @see edu.wustl.catissuecore.reportloader.Observable#register(edu.wustl.catissuecore.reportloader.Observer)
	 * @param o object of observer 
	 */
	public void register(Observer o)
	{
		this.obr=o;
	}
	
	/**
	 * @return obr object of Observer
	 */
	public Observer getObr()
	{
		return obr;
	}
	
	/**
	 * @param obr object of observer
	 */
	public void setObr(Observer obr)
	{
		this.obr = obr;
	}	
}
