
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
 * This class is responsible to fetch identified reports and to spawn a separate
 * thread to convert identified reports into deidentiied reports. This class
 * manages the thread pool so that excessive threads will not be spawned.
 * @author vijay_pande
 */
public class DeIDPipelineManager
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DeIDPipelineManager.class);
	/**
	 * abbrToHeader.
	 */
	protected static HashMap<String, String> abbrToHeader;
	/**
	 * rejectedExecutionHandler.
	 */
	private RejectedExecutionHandler rejectedExecutionHandler;
	/**
	 * corePoolSize.
	 */
	private int corePoolSize;
	/**
	 * maxPoolSize.
	 */
	private int maxPoolSize;
	/**
	 * keepAliveSeconds.
	 */
	private int keepAliveSeconds;
	/**
	 * deidentifier.
	 */
	private AbstractDeidentifier deidentifier;

	/**
	 * Default constructor of the class.
	 * @throws Exception : Exception
	 */
	public DeIDPipelineManager() throws Exception
	{
		try
		{
			this.initDeid();
		}
		catch (final Exception ex)
		{
			this.logger.error("Initialization of deidentification process"
					+ " failed or error in main thread"+ex.getMessage(), ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * This method is responsible for creating prerequisite environment that is.
	 * required for initialization of the DeID process
	 * @throws Exception
	 *             throws exception occured in the initialization process.
	 */
	private void initDeid() throws Exception
	{
		Utility.init();
		// Configuring CSV logger
		CSVLogger.configure(CaTIESConstants.LOGGER_DEID_SERVER);
		// Initializing caCoreAPI instance
		CaCoreAPIService.initialize();
		// Min. no of threads that should be created by threadPoolExecutor
		this.corePoolSize = 4;
		// Max no og threads that should be created by threadPoolExecutor
		this.maxPoolSize = Integer.parseInt(CaTIESProperties
				.getValue(CaTIESConstants.MAX_THREADPOOL_SIZE));
		// Thread alive time
		this.keepAliveSeconds = 100;
		try
		{
			this.deidentifier = DeidentifierFactory.getDeidentiifier();
		}
		catch (final ClassCastException ex)
		{
			this.logger.error("Class not found:"
					+ CaTIESProperties.
					getValue(CaTIESConstants.DEIDENTIFIER_CLASSNAME
					+ "\n" + ex.getMessage()),ex);
			ex.printStackTrace();
			throw ex;
		}
		this.deidentifier.initialize();
	}

	/**
	 * This method is responsible for managing the overall process of.
	 * de-identification
	 * @throws InterruptedException : InterruptedException
	 */
	public void startProcess() throws InterruptedException
	{
		this.logger.info("Inside process manager");
		while (true)
		{
			try
			{
				this.logger.info("Deidentification process started at " + new Date().toString());
				// Fetch the list of identified report Ids that are pending for
				// de-identification
				final List isprIDList = this.getReportIDList();
				// Process reports that are pending for de-identification
				this.processReports(isprIDList);
				// if report list contains less than one report then thread will
				// go to sleep
				this.logger.info("Deidentification process finished at " + new Date().toString()
						+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(CaTIESProperties
						.getValue(CaTIESConstants.DEID_SLEEPTIME)));
			}
			catch (final Exception ex)
			{
				this.logger.error("Unexpected Exception in deid Pipeline "+ex.getMessage(), ex);
				this.logger.error("Deidentification process finished at " + new Date().toString()
						+ ". Thread is going to sleep.");
				ex.printStackTrace();
				Thread.sleep(Integer.parseInt(CaTIESProperties
						.getValue(CaTIESConstants.DEID_SLEEPTIME)));
			}
		}
	}

	/**
	 * This method is responsible for managing the pool of thread, fetching
	 * individual reports by ID and intiating the de-identification rpocess.
	 * @param isprIDList
	 *            Identified surgical pathology report ID list
	 * @throws Exception
	 *             generic exception
	 */
	private void processReports(List isprIDList) throws Exception
	{
		// set policy for the handling of rejected threads by thread pool
		// manager
		this.rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
		// Instantiate threadPoolExecutor which manages the pool of thread, this
		// is feature of java 1.5
		final ThreadPoolExecutor deidExecutor = new ThreadPoolExecutor(this.corePoolSize,
				this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(),
				this.rejectedExecutionHandler);

		if (isprIDList != null || !isprIDList.isEmpty())
		{
			// if report list contains more than or equal to one reports then
			// process reports
			try
			{
				CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER,
						CaTIESConstants.CSVLOGGER_DATETIME + CaTIESConstants.CSVLOGGER_SEPARATOR
								+ CaTIESConstants.CSVLOGGER_IDENTIFIED_REPORT
								+ CaTIESConstants.CSVLOGGER_SEPARATOR
								+ CaTIESConstants.CSVLOGGER_STATUS
								+ CaTIESConstants.CSVLOGGER_SEPARATOR
								+ CaTIESConstants.CSVLOGGER_MESSAGE
								+ CaTIESConstants.CSVLOGGER_SEPARATOR
								+ CaTIESConstants.CSVLOGGER_PROCESSING_TIME);
				IdentifiedSurgicalPathologyReport identifiedReport = null;
				// loop to process each report
				this.logger.info("Starting to process list of size " + isprIDList.size());
				final int isprListSize = isprIDList.size();
				for (int i = 0; i < isprListSize; i++)
				{
					this.logger.info("Processing report serial no:" + i);
					// retrieve the identified report using its id

					identifiedReport = this.getIdentifiedReport((Long) isprIDList.get(i));
					// instantiate a thread to process the report
					this.logger.info("Instantiating thread for report id="
							+ identifiedReport.getId());
					final Thread thread = new DeidentifierReportThread(identifiedReport,
							this.deidentifier);
					// add thread to thread pool manager
					deidExecutor.execute(thread);
				}
			}
			catch (final Exception ex)
			{
				this.logger.error("Deidentification pipeline is failed:"+ex.getMessage(), ex);
				ex.printStackTrace();
				// shut down the thread pool manager
				deidExecutor.shutdown();
				throw ex;
			}
			// check to wait until all active theads finish their task before
			// shutting down the thread pool manager
			while (deidExecutor.getActiveCount() > 0)
			{
				// if there are active threads then sleep for 100 seconds
				Thread.sleep(100000);
			}
			// shut down the thread pool manager
			deidExecutor.shutdown();
		}
	}

	/**
	 * @param identifiedReportId : identifiedReportId
	 * @return IdentifiedSurgicalPathologyReport
	 * @throws Exception : Exception
	 */
	private IdentifiedSurgicalPathologyReport getIdentifiedReport(Long identifiedReportId)
			throws Exception
	{
		final IdentifiedSurgicalPathologyReport identifiedReport = (IdentifiedSurgicalPathologyReport) CaCoreAPIService
				.getObject(
						edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport.class,
						Constants.SYSTEM_IDENTIFIER, identifiedReportId);
		String hqlQuery = "select cpr.participant from edu.wustl.catissuecore.domain"
				+ ".CollectionProtocolRegistration cpr, "
				+ " edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg" + " where scg.id="
				+ identifiedReport.getSpecimenCollectionGroup().getId()
				+ " and scg.id in elements(cpr.specimenCollectionGroupCollection)";
		final List participantList = (List) CaCoreAPIService.executeQuery(hqlQuery,
				Participant.class.getName());
		Participant participant = null;
		if (participantList != null && !participantList.isEmpty())
		{
			participant = (Participant) participantList.get(0);

			hqlQuery = "select elements(p.participantMedicalIdentifierCollection)"
					+ " from edu.wustl.catissuecore.domain.Participant as p" + " where p.id="
					+ participant.getId();
			final Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection = CaCoreAPIService
					.executeQuery(hqlQuery, Participant.class.getName());
			participant
					.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
		}
		final SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		specimenCollectionGroup.setId(identifiedReport.getSpecimenCollectionGroup().getId());
		final CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setParticipant(participant);
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
		identifiedReport.setSpecimenCollectionGroup(specimenCollectionGroup);
		// get textcontent
		final TextContent textContent = (TextContent) CaCoreAPIService.getObject(TextContent.class,
				Constants.SYSTEM_IDENTIFIER, identifiedReport.getTextContent().getId());
		// get source
		final Site reportSource = (Site) CaCoreAPIService.getObject(Site.class,
				Constants.SYSTEM_IDENTIFIER, identifiedReport.getReportSource().getId());
		identifiedReport.setReportSource(reportSource);
		identifiedReport.setTextContent(textContent);
		return identifiedReport;
	}

	/**
	 * Method to retrieve Identified report for deidentification process.
	 * @return object of IdentifiedSurgicalPathologyReport
	 * @throws Exception
	 *             generic exception occured while retrieving object of
	 *             deidentified report
	 */
	private List getReportIDList() throws Exception
	{
		final String hqlQuery = "select id from edu.wustl.catissuecore"
				+ ".domain.pathology.IdentifiedSurgicalPathologyReport where "
				+ CaTIESConstants.COLUMN_NAME_REPORT_STATUS + "='"
				+ CaTIESConstants.PENDING_FOR_DEID + "'";
		final List isprIDList = (List) CaCoreAPIService.executeQuery(hqlQuery,
				IdentifiedSurgicalPathologyReport.class.getName());
		return isprIDList;
	}

	/**
	 * Main method for the DeIDPipeline class.
	 * @param args
	 *            commandline arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			final DeIDPipelineManager deidPipeline = new DeIDPipelineManager();
			// Thread for stopping deid poller server
			final Thread stopThread = new StopServer(CaTIESConstants.DEID_PORT);
			stopThread.start();
			deidPipeline.startProcess();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			// Logger.error("Error while initializing DeidPipelineManager "+
			// ex);
		}

	}
}