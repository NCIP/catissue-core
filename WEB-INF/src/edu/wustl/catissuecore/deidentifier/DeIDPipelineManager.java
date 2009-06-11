package edu.wustl.catissuecore.deidentifier;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.StopServer;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * This class is responsible to fetch identified reports and to spawn a separate thread to convert identified reports into deidentiied reports.
 * This class manages the thread pool so that excessive threads will not be spawned. 
 * @author vijay_pande
 */
public class DeIDPipelineManager
{
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private transient Logger logger = Logger.getCommonLogger(DeIDPipelineManager.class);
	protected static HashMap<String,String> abbrToHeader;
	
	private RejectedExecutionHandler rejectedExecutionHandler;
	private int corePoolSize;
	private int maxPoolSize;
	private int keepAliveSeconds;

	private AbstractDeidentifier deidentifier;
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
			logger.error("Initialization of deidentification process failed or error in main thread",ex);
			throw ex;
		}
	}
			
	/**
	 * This method is responsible for creating prerequisite environment that is required for initialization of the DeID process
	 * @throws Exception throws exception occured in the initialization process.
	 */
	private void initDeid() throws Exception
	{
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
		try
		{
			deidentifier=DeidentifierFactory.getDeidentiifier();
		}
		catch (ClassCastException ex) 
		{
			logger.error("Class not found:"+CaTIESProperties.getValue(CaTIESConstants.DEIDENTIFIER_CLASSNAME+"\n"+ex));
			throw ex;
		}
		deidentifier.initialize();
	}
		
	/**
	 * This method is responsible for managing the overall process of de-identification
	 * @throws InterruptedException 
	 */
	public void startProcess() throws InterruptedException
	{
		logger.info("Inside process manager");
		while(true)
		{
			try
			{
				logger.info("Deidentification process started at "+new Date().toString());		
				// Fetch the list of identified report Ids that are pending for de-identification
				List isprIDList=getReportIDList();
				// Process reports that are pending for de-identification
				processReports(isprIDList);
				// if report list contains less than one report then thread will go to sleep
				logger.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.DEID_SLEEPTIME)));
			}
			catch(Exception ex)
			{
				logger.error("Unexpected Exception in deid Pipeline ",ex);
				logger.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
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
		
		if(isprIDList!=null || isprIDList.size()>0)
		{
			// if report list contains more than or equal to one reports then process reports
			try
			{
				CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER,CaTIESConstants.CSVLOGGER_DATETIME+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_IDENTIFIED_REPORT+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_STATUS+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_MESSAGE+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_PROCESSING_TIME);
				IdentifiedSurgicalPathologyReport identifiedReport=null;	
				// loop to process each report
				logger.info("Starting to process list of size "+ isprIDList.size());
				int isprListSize=isprIDList.size();
				for(int i=0;i<isprListSize;i++)
				{
					logger.info("Processing report serial no:"+i);
					// retrieve the identified report using its id
					
					identifiedReport=getIdentifiedReport((Long)isprIDList.get(i));
					// instantiate a thread to process the report
					logger.info("Instantiating thread for report id="+identifiedReport.getId());
					Thread th = new DeidentifierReportThread(identifiedReport, deidentifier);
					// add thread to thread pool manager
					deidExecutor.execute(th);
				}					
			}
			catch(Exception ex)
			{
				logger.error("Deidentification pipeline is failed:",ex);
				// shut down the thread pool manager
				deidExecutor.shutdown();
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
		}
	}


	private IdentifiedSurgicalPathologyReport getIdentifiedReport(Long identifiedReportId) throws Exception 
	{
		IdentifiedSurgicalPathologyReport identifiedReport=(IdentifiedSurgicalPathologyReport)CaCoreAPIService.getObject(edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport.class, Constants.SYSTEM_IDENTIFIER, identifiedReportId);
		String hqlQuery="select cpr.participant from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr, " +
						" edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg" +
						" where scg.id="+identifiedReport.getSpecimenCollectionGroup().getId() +
						" and scg.id in elements(cpr.specimenCollectionGroupCollection)";
		List participantList=(List)CaCoreAPIService.executeQuery(hqlQuery, Participant.class.getName());
		Participant participant=null;
		if(participantList!=null && participantList.size()>0)
		{
			participant=(Participant)participantList.get(0);
			
			hqlQuery="select elements(p.participantMedicalIdentifierCollection)" +
							" from edu.wustl.catissuecore.domain.Participant as p" +
							" where p.id="+participant.getId();
			Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection=(List)CaCoreAPIService.executeQuery(hqlQuery, Participant.class.getName());
			participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
		}
		SpecimenCollectionGroup specimenCollectionGroup=new SpecimenCollectionGroup();
		specimenCollectionGroup.setId(identifiedReport.getSpecimenCollectionGroup().getId());
		CollectionProtocolRegistration collectionProtocolRegistration=new CollectionProtocolRegistration();
		collectionProtocolRegistration.setParticipant(participant);
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
		identifiedReport.setSpecimenCollectionGroup(specimenCollectionGroup);
		// get textcontent
		TextContent textContent=(TextContent)CaCoreAPIService.getObject(TextContent.class, Constants.SYSTEM_IDENTIFIER, identifiedReport.getTextContent().getId());
		// get source
		Site reportSource=(Site)CaCoreAPIService.getObject(Site.class, Constants.SYSTEM_IDENTIFIER, identifiedReport.getReportSource().getId());
		identifiedReport.setReportSource(reportSource);
		identifiedReport.setTextContent(textContent);
		return identifiedReport;
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
			ex.printStackTrace();
			//Logger.error("Error while initializing DeidPipelineManager "+ ex);
		}
		
	}	
}