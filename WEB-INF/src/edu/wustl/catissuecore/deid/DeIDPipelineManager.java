package edu.wustl.catissuecore.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.deid.JniDeID;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.StopServer;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbManager.DAOException;
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
	public DeIDPipelineManager()
	{
		try
		{
			this.initDeid();
		}
		catch(Exception ex)
		{
			Logger.out.error("Initialization of deidentification process failed or error in main thread",ex);
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
		// Function call to set up section header configuration from SectionHeaderConfig.txt file
		setSectionHeaderPriorities();

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
		// Get instance of bizLogicFactory
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		// Get bizlogic of IdentifiedSurgicalPathologyReport
		IdentifiedSurgicalPathologyReportBizLogic bizLogic =(IdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
		
		if(isprIDList ==null || isprIDList.size()<=1)
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
					String id;
					NameValueBean nb;
					IdentifiedSurgicalPathologyReport identifiedReport=null;	
					// loop to process each report
					Logger.out.info("Starting to process list of size "+ isprIDList.size());
					int isprListSize=isprIDList.size();
					for(int i=1;i<isprListSize;i++)
					{
						Logger.out.info("Processing report serial no:"+i);
						// list contains name value bean, get name value bean from list
						nb=(NameValueBean)isprIDList.get(i);
						// get value which is an id of the identified report
						id=nb.getValue();
						Logger.out.info("Got report id="+id);
						// retrive the identified report using its id
						identifiedReport=(IdentifiedSurgicalPathologyReport)bizLogic.getReportById(Long.parseLong(id));
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
	 * This method fetche a list of ID's of identified surgical pathology reports pending for de-indetification
	 * @return List ISPR ID list
	 * @throws Exception generic exception 
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
			String[] displayNameFields=new String[] {Constants.SYSTEM_IDENTIFIER};
			String valueField=new String(Constants.SYSTEM_IDENTIFIER);
			// where report status
			String[] whereColumnName = new String[]{CaTIESConstants.COLUMN_NAME_REPORT_STATUS};
			// is equal to
			String[] whereColumnCondition = new String[]{"="};
			// the value of CaTIESConstants.PENDING_FOR_DEID
			Object[] whereColumnValue = new String[]{CaTIESConstants.PENDING_FOR_DEID};
			// join condition is null since there is single condition
			String joinCondition = null;
			// seperate values with comma
			String separatorBetweenFields = ", ";	
			
			// get instnace of biz logic factory
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			// get biz logic of identified report from biz logic factory
			IdentifiedSurgicalPathologyReportBizLogic bizLogic =(IdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
			// fire query to trtrieve list of identified reports required by identified report
			Logger.out.info("Firing query to retriev ids of identified report");
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
	 * This nethod sets the priority order and full name of the abrreviated section name
	 * which is used by the synthesizeSPRText method
	 * @throws Exception Generic exception
	 */
	private void setSectionHeaderPriorities() throws Exception
	{
		try 
		{
			// get path to the SectionHeaderConfig.txt file
			String configName = new String(pathToConfigFiles+CaTIESProperties.getValue(CaTIESConstants.DEID_SECTION_HEADER_FILENAME));
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
	 * Main method for the DeIDPipeline class
	 * @param args commandline arguments
	 */
	public static void main(String[] args)
	{
		DeIDPipelineManager deidPipeline =new DeIDPipelineManager();
		// Thread for stopping deid poller server
		Thread stopThread=new StopServer(CaTIESConstants.DEID_PORT);
		stopThread.start();
		try
		{
			deidPipeline.startProcess();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		
	}	
}