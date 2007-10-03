package edu.wustl.catissuecore.deid;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.deid.JniDeID;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.StopServer;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is responsible to fetch identified reports and to spawn a sepearate thread to convert identified reports into deidentiied reports.
 * This class manages the thread pool so that excessive threads will not be spawned. 
 * @author vijay_pande
 */
public class DeIDPipelineManager
{
	protected static HashMap<String,String> abbrToHeader;
	protected static String configFileName;
	protected static JniDeID deid;
	private RejectedExecutionHandler rejectedExecutionHandler;
	private static String pathToConfigFiles;
	private int corePoolSize;
	private int maxPoolSize;
	private int keepAliveSeconds;

	/**
	 * Default constructor of the class
	 */
	public DeIDPipelineManager() throws Exception
	{
		try
		{
			this.initDeid();
		}
		catch(Exception ex)
		{
			Logger.out.error("Initialization of deidentification process failed or error in main thread",ex);
			throw ex;
		}
	}
			
	/**
	 * This method is responsible for creating prerequisite environment that is required for initialization of the DeID process
	 * @throws Exception throws exception occured in the initialization process.
	 */
	private void initDeid() throws Exception
	{
		abbrToHeader = new LinkedHashMap <String,String>();

		Utility.init();
		// Configuring CSV logger
		CSVLogger.configure(CaTIESConstants.LOGGER_DEID_SERVER);
		// Initializing caCoreAPI instance
		CaCoreAPIService.initialize();
		// Min. no of threads that should be created by threadPoolExecutor
		corePoolSize=4;
		// Max no og threads that should be created by threadPoolExecutor
		maxPoolSize=Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.MAX_THREADPOOL_SIZE));
		// Thread alive time 
		keepAliveSeconds=100;
		
		// To store path of the directory for the config files, here 'catissue-properties' directory
		pathToConfigFiles=new String(Variables.applicationHome + System.getProperty("file.separator")+"caTIES_conf"+System.getProperty("file.separator"));
		// Function call to store name of config file name required for de-identification native call, deid.cfg
		setConfigFileName();

		// Instantiates wrapper class for deid native call
		deid=new JniDeID(); 
		// set path of the directionary that is required by native call for deidentification 
		deid.setDictionaryLocation(CaTIESProperties.getValue(CaTIESConstants.DEID_DCTIONARY_FOLDER));	
	}
		
	/**
	 * This method is responsible for managing the overall process of de-identification
	 * @throws InterruptedException 
	 */
	public void startProcess() throws InterruptedException
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
				Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.DEID_SLEEPTIME)));
			}
			catch(Exception ex)
			{
				Logger.out.error("Unexpected Exception in deid Pipeline ",ex);
				Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.DEID_SLEEPTIME)));
			}
			// To catch error by native call, Since it throws UnsatisfiedLinkError which is an error (not exception)
			catch (Throwable th)
		    {
				Logger.out.error("Unexpected Error in native call ",th);
				Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.DEID_SLEEPTIME)));
		    }
		}
	}
		
	/**
	 *  This method is responsible for managing the pool of thread, fetching individual reports by ID and intiating the de-identification rpocess.
	 * @param isprIDList Identified surgical pathology report ID list
	 * @throws Exception generic exception
	 */
	private void processReports(List isprIDList) throws  Exception
	{
		// set policy for the handling of rejected threads by thread pool manager
		rejectedExecutionHandler=new ThreadPoolExecutor.AbortPolicy();
		// Instantiate threadPoolExecutor which manages the pool of thread, this is feature of java 1.5
		ThreadPoolExecutor deidExecutor=new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), this.rejectedExecutionHandler);
		
		if(isprIDList ==null || isprIDList.size()<1)
		{
			// if report list contains less than one report then thread will go to sleep
			Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
			Thread.sleep(Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.DEID_SLEEPTIME)));
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
					CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER,CaTIESConstants.CSVLOGGER_DATETIME+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_IDENTIFIED_REPORT+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_STATUS+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_MESSAGE+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_PROCESSING_TIME);
					IdentifiedSurgicalPathologyReport identifiedReport=null;	
					List identifiedReportList=null;
					// loop to process each report
					Logger.out.info("Starting to process list of size "+ isprIDList.size());
					int isprListSize=isprIDList.size();
					for(int i=0;i<isprListSize;i++)
					{
						Logger.out.info("Processing report serial no:"+i);
						// retrive the identified report using its id
						identifiedReportList=(List)CaCoreAPIService.executeQuery("from edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport where id="+(Long)isprIDList.get(i),IdentifiedSurgicalPathologyReport.class.getName());//(new IdentifiedSurgicalPathologyReport(), Constants.SYSTEM_IDENTIFIER, (Long)isprIDList.get(i));
						identifiedReport=(IdentifiedSurgicalPathologyReport)identifiedReportList.get(0);
						// instantiate a thread to process the report
						Logger.out.info("Instantiating thread for report id="+identifiedReport.getId());
						Thread th = new DeidReportThread(identifiedReport);
						// add thread to thread pool manager
						deidExecutor.execute(th);
					}					
				}
				catch(Exception ex)
				{
					Logger.out.error("Deidentification pipeline is failed:",ex);
					// shut down the thread pool manager
					deidExecutor.shutdown();
					Logger.out.info("Unloading deid library");
					// unload deid library
					JniDeID.unloadDeidLibrary();
					throw ex;
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
	 * Method to retrieve Identified report for deidentification process 
	 * @return object of IdentifiedSurgicalPathologyReport
	 * @throws Exception generic exception occured while retrieving object of deidentified report
	 */
	private List getReportIDList() throws Exception
	{
		String hqlQuery="select id from edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport where "+CaTIESConstants.COLUMN_NAME_REPORT_STATUS+"='"+CaTIESConstants.PENDING_FOR_DEID+"'";
		List isprIDList=(List)CaCoreAPIService.executeQuery(hqlQuery, IdentifiedSurgicalPathologyReport.class.getName());
		return isprIDList;
	}
	
	/**
	 * This method temporarily saves the name of deid config  file name. 
	 * This deid config file conatins the keywords which will be the input for deidentification
	 * @throws Exception Generic exception
	 */
	protected void setConfigFileName() throws Exception	
	{
		// get file name of config file name required for deidentification
		String cfgFileName=new String(pathToConfigFiles+CaTIESProperties.getValue(CaTIESConstants.DEID_CONFIG_FILE_NAME));
		// create handle to file
        File cfgFile = new File(cfgFileName);
        // set configFileName  to the path of config file 
        DeIDPipelineManager.configFileName = cfgFile.getAbsolutePath();
        Logger.out.info("Config file name is "+DeIDPipelineManager.configFileName);
    }
	
	/**
	 * Main method for the DeIDPipeline class
	 * @param args commandline arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			DeIDPipelineManager deidPipeline =new DeIDPipelineManager();
			// Thread for stopping deid poller server
			Thread stopThread=new StopServer(CaTIESConstants.DEID_PORT);
			stopThread.start();
			deidPipeline.startProcess();
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while initializing DeidPipelineManager "+ ex);
		}
		
	}	
}