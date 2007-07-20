package edu.wustl.catissuecore.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;

import com.deid.JniDeID;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.reportloader.CSVLogger;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.SiteInfoHandler;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author vijay_pande
 * This class is responsible to fetch identified reports and to spawn a sepearate thread to convert identified reports into deidentiied reports.
 * This class manages the thread pool so that excessive threads will not be spawned. 
 */
public class DeIDPipelineManager
{
	protected static List<String> sectionPriority;
	protected static HashMap<String,String> abbrToHeader;
	protected static String configFileName;
	private static String pathToConfigFiles;
	protected static JniDeID deid;
	private RejectedExecutionHandler rejectedExecutionHandler; 
	
	private int corePoolSize;
	private int maxPoolSize;
	private int keepAliveSeconds;

	
	/**
	 * Default constructor of the class
	 * 
	 */
	public DeIDPipelineManager()
	{
		try
		{
			this.init();
			this.processManager();
			
		}
		catch(Exception ex)
		{
			Logger.out.error("Initialization of deidentification process failed or error in main thread",ex);
		}
	}
		
	
	/**
	 * @throws Exception throws exception occured in the initialization process.
	 * This method is responsible for creating prerequisite environment that is required for initialization of the DeID process
	 */
	private void init() throws Exception
	{
		sectionPriority = new ArrayList<String>();
		abbrToHeader = new HashMap <String,String>();

		// Initialization methods
		Variables.applicationHome = System.getProperty("user.dir");
		//Logger.out = org.apache.log4j.Logger.getLogger("");
		//Configuring common logger
		Logger.configure(Parser.LOGGER_GENERAL);
		// Configuring CSV logger
		CSVLogger.configure(Parser.LOGGER_DEID_SERVER);
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
		// Intialization to retrieve values of keys from ApplicationResources.properties file
		ApplicationProperties.initBundle("ApplicationResources");
		
		// Min. no of threads that should be created by threadPoolExecutor
		corePoolSize=4;
		// Max no og threads that should be created by threadPoolExecutor
		maxPoolSize=Integer.parseInt(XMLPropertyHandler.getValue("threadPool.maxPoolSize"));
		// Thread alive time 
		keepAliveSeconds=100;
		
		// To store path of the directory for the config files, here 'catissue-properties' directory
		pathToConfigFiles=new String(Variables.applicationHome + System.getProperty("file.separator")+"catissuecore-properties"+System.getProperty("file.separator"));
		// Function call to store name of config file name required for de-identification native call, deid.cfg
		cacheConfigFileName();
		// Function call to set up section header configuration from SectionHeaderConfig.txt file
		setUpSectionHeaderPriorities();

		// Instantiates wrapper class for deid native call
		deid=new JniDeID(); 
		// set path of the directionary that is required by native call for deidentification 
		deid.setDictionaryLocation(XMLPropertyHandler.getValue("deid.home"));	
	}
		
	/**
	 * 
	 * @throws InterruptedException 
	 * This method is responsible for managing the overall process of de-identification
	 */
	public void processManager() throws InterruptedException
	{
		Logger.out.info("Inside process manager");
		while(true)
		{
			try
			{
				Logger.out.info("Deidentification process started at "+new Date().toString());		
				// Fetch the list of identified report Ids that are pending for de-identification
				List isprIDList=getReportIDList();
				// Process reports that are pending for de-identification
				processReports(isprIDList);
			}
			catch(Exception ex)
			{
				Logger.out.error("Unexpected Exception in deid Pipeline ",ex);
				Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
			}
			// To catch error by native call, Since it throws UnsatisfiedLinkError which is an error (not exception)
			catch (Throwable th)
		    {
				Logger.out.error("Unexpected Error in native call ",th);
				Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
		    }
		}
	}
		
	/**
	 * @param isprIDList Identified surgical pathology report ID list
	 * @throws Exception generic exception
	 * This method is responsible for managing the pool of thread, fetching individual reports by ID and intiating the de-identification rpocess.
	 */
	private void processReports(List isprIDList) throws  Exception
	{
		// set policy for the handling of rejected threads by thread pool manager
		rejectedExecutionHandler=new ThreadPoolExecutor.AbortPolicy();
		// Instantiate threadPoolExecutor which manages the pool of thread, this is feature of java 1.5
		ThreadPoolExecutor deidExecutor=new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), this.rejectedExecutionHandler);
		// Get instance of bizLogicFactory
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		// Get bizlogic of IdentifiedSurgicalPathologyReport
		IdentifiedSurgicalPathologyReportBizLogic bizLogic =(IdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
		
		if(isprIDList ==null || isprIDList.size()<=1)
		{
			// if report list contains less than one report then thread will go to sleep
			Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
			Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
		}
		else
		{
			// if report list contains more than or equal to one reports then process reports
			try
			{
				Logger.out.info("Loading deid library");
				// load deidLibrary required for native call
				JniDeID.loadDeidLibrary();	
				try
				{
					CSVLogger.info(Parser.LOGGER_DEID_SERVER, "Date/Time, Identified report ID, Status, Message");
					String id;
					NameValueBean nb;
					IdentifiedSurgicalPathologyReport ispr=null;	
					// loop to process each report
					Logger.out.info("Starting to process list of size "+ isprIDList.size());
					for(int i=1;i<isprIDList.size();i++)
					{
						Logger.out.info("Processing report serial no:"+i);
						// list contains name value bean, get name value bean from list
						nb=(NameValueBean)isprIDList.get(i);
						// // get value which is an id of the identified report
						id=nb.getValue();
						Logger.out.info("Got report id="+id);
						// retrive the identified report using its id
						ispr=(IdentifiedSurgicalPathologyReport)bizLogic.getReportById(Long.parseLong(id));
						// instantiate a thread to process the report
						Logger.out.info("Instantiating thread for report id="+ispr.getId());
						Thread th = new DeidReport(ispr);
						// add thread to thread pool manager
						deidExecutor.execute(th);
					}					
				}
				catch(Exception ex)
				{
					Logger.out.error("Deidentification pipeline is failed:",ex);			
				}
				// check to wait until all active theads finish their task before shutting down the thread pool manager
				while(deidExecutor.getActiveCount()>0)
				{
					// if there are active threads then sleep for 100 seconds 
					Thread.sleep(100000);
				}
				// shut down the thread pool manager
				deidExecutor.shutdown();
				Logger.out.info("Unloading deid library");
				// unload deid library
				JniDeID.unloadDeidLibrary();
			}
			catch(UnsatisfiedLinkError ex)
			{
				Logger.out.error("Either Loading or unLoading of DeID library is failed:",ex);
				throw new Error(ex.getMessage());
			}
		}
	}
			
	/**
	 * @return List ISPR ID list
	 * @throws Exception generic exception
	 * This method fetche a list of ID's of identified surgical pathology reports pending for de-indetification
	 */
	private List getReportIDList() throws Exception
	{
		List isprIDList=null;
		try
		{
			// set values of the variables that are required to generate required query
			// fetch from identified report
			String sourceObjectName=IdentifiedSurgicalPathologyReport.class.getName();
			// fetch column id
			String[] displayNameFields=new String[] {"id"};
			String valueField=new String("id");
			// where report status
			String[] whereColumnName = new String[]{"reportStatus"};
			// is equal to
			String[] whereColumnCondition = new String[]{"="};
			// the value of Parser.PENDING_FOR_DEID
			Object[] whereColumnValue = new String[]{Parser.PENDING_FOR_DEID};
			// join condition is null since there is single condition
			String joinCondition = null;
			// seperate values with comma
			String separatorBetweenFields = ", ";	
			
			// get instnace of biz logic factory
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			// get biz logic of identified report from biz logic factory
			IdentifiedSurgicalPathologyReportBizLogic bizLogic =(IdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
			// fire query to trtrieve list of identified reports required by identified report
			Logger.out.info("Firing query to retrive ids of identified report");
			isprIDList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
			Logger.out.info("Report id list retrieved successfully, size is:"+isprIDList.size());
		}
		catch(DAOException ex)
		{
			Logger.out.error("Error in fetching identified reports for deidentification:",ex);
		}	
		return isprIDList;
	}
		 
	/**
	 * @throws Exception
	 * This method temporarily saves the name of deid config  file name. 
	 * This deid config file conatins the keywords which will be the input for deidentification
	 */
	protected void cacheConfigFileName() throws Exception	
	{
		// get file name of config file name required for deidentification
		String cfgFileName=new String(pathToConfigFiles+XMLPropertyHandler.getValue("deid.config.filename"));
		// create handle to file
        File cfgFile = new File(cfgFileName);
        // set configFileName  to the path of config file 
        DeIDPipelineManager.configFileName = cfgFile.getAbsolutePath();
        Logger.out.info("Config file name is "+DeIDPipelineManager.configFileName);
    }
	
	/**
	 * @throws Exception 
	 * This nethod sets the priority order and full name of the abrreviated section name
	 * which is used by the synthesizeSPRText method
	 */
	private void setUpSectionHeaderPriorities() throws Exception
	{
		try 
		{
			// get path to the SectionHeaderConfig.txt file
			String configName = new String(pathToConfigFiles+XMLPropertyHandler.getValue("deid.sectionheaderpriority.filename"));
			// set bufferedReader to read file
			BufferedReader br = new BufferedReader(new FileReader(configName));

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

				// add section header abbreviation to list
				sectionPriority.add(abbr);
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
	}
	
	/**
	 * @param args commandline arguments
	 * main method for the DeIDPipeline class
	 */
	public static void main(String[] args)
	{
		DeIDPipelineManager de=new DeIDPipelineManager();
//		 Thread for stopping deid poller server
		Thread th=new Thread(){
			public void	run()
			{
				try
				{
					// get port number for deid server from catissueCore-properties.xml file
					int port=Integer.parseInt(XMLPropertyHandler.getValue("deid.port"));
					ServerSocket serv = new ServerSocket(port);
				  	BufferedReader r;
			    	Socket sock = serv.accept();
			    	r =new BufferedReader (new InputStreamReader (sock.getInputStream()));
			    	PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()),true);
			    	String str=r.readLine();
			    	
			    	Logger.out.info("Stopping server");
			    	r.close();
			    	sock.close(); 
			    	// close server socket
				    serv.close();
				    // stop server
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
}