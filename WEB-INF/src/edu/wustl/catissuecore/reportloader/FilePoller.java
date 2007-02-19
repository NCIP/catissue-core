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
	private static String[] files=null;
	private static File inputDir=null;
	private static File errorFileDir=null;
	private static File fileDir=null;
	private static FilePoller poller =null;


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
		edu.wustl.catissuecore.util.global.Variables.isLoadFromCaties=true;
	}
	
	/**
	 * @param args
	 * start up method
	 */
	public static void main(String[] args)
	{
		try
		{
			poller = new FilePoller();
			poller.init();
			Observer obr=new ReportProcessor();
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.PROCESSED_FILE_DIR));
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.INPUT_DIR));
			ReportLoaderUtil.createDir(XMLPropertyHandler.getValue(Parser.BAD_FILE_DIR));
			Logger.out.info("debug3");
			Thread th=new Thread(){
				public void	run()
				{
					try
					{
						int PORT=Integer.parseInt(XMLPropertyHandler.getValue("filepollerport"));
						ServerSocket serv = new ServerSocket(PORT);
					  	BufferedReader r;
				    	Socket sock = serv.accept();
				    	r =new BufferedReader (new InputStreamReader (sock.getInputStream()) );
				    	PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()),true);
				    	String str=r.readLine();
				    	System.out.println("Stopping server");
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
			while(true)
			{
				inputDir = new File(XMLPropertyHandler.getValue(Parser.INPUT_DIR)); 
				files=	inputDir.list();
				 if(files.length>0)
				  {
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
