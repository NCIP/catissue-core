package edu.wustl.catissuecore.reportloader;

import java.io.File;
import java.io.IOException;
import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
import edu.wustl.catissuecore.caties.util.StopServer;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * Represents a poller which picks up the report files
 * and then pass them to the appropriate parser which parsers those files and
 * import the data into datastore.
 * @author sandeep_ranade
 */
public class FilePoller implements Observable
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(FilePoller.class);

	/**
	 * Observer.
	 */
	private Observer obr;

	/**
	 * Main method for FilePoller.
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		String[] files = null;
		File inputDir = null;
		FilePoller poller = null;

		try
		{
			poller = new FilePoller();
			// Initializing file poller
			Utility.init();
			// initializing SiteInfoHandler to read site names from site configuration file
			SiteInfoHandler.init(CaTIESProperties.getValue(CaTIESConstants.SITE_INFO_FILENAME));
			XMLPropertyHandler.init(CaTIESProperties.getValue(CaTIESConstants.XML_PROPERTY_FILENAME));
			// Configuring CSV logger
			CSVLogger.configure(CaTIESConstants.LOGGER_FILE_POLLER);
			//Initializing caCoreAPI instance
			CaCoreAPIService.initialize();
			CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER, CaTIESConstants.CSVLOGGER_DATETIME +
				CaTIESConstants.CSVLOGGER_SEPARATOR + CaTIESConstants.CSVLOGGER_FILENAME +
				CaTIESConstants.CSVLOGGER_SEPARATOR + CaTIESConstants.CSVLOGGER_REPORTQUEUE +
				CaTIESConstants.CSVLOGGER_SEPARATOR + CaTIESConstants.CSVLOGGER_STATUS +
				CaTIESConstants.CSVLOGGER_SEPARATOR + CaTIESConstants.CSVLOGGER_MESSAGE +
				CaTIESConstants.CSVLOGGER_SEPARATOR + CaTIESConstants.CSVLOGGER_PROCESSING_TIME);
			// for empty row after heading
			CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER, "");

			Observer obr = new ReportProcessor();
			// registering poller to the object obr
			poller.register(obr);
			//start thread ReportLoaderQueueProcessor
			ReportLoaderQueueProcessor queueProcessor = new ReportLoaderQueueProcessor();
			// Starts ReportLoaderQueueProcessor thread
			queueProcessor.start();
		}
		catch (Exception ex)
		{
			logger.error("Error occured while inializing File Poller ", ex);
			return;
		}
		try
		{
			// Create new directories if does not exists
			ReportLoaderUtil.createDir(CaTIESProperties.getValue(CaTIESConstants.PROCESSED_FILE_DIR));
			ReportLoaderUtil.createDir(CaTIESProperties.getValue(CaTIESConstants.INPUT_DIR));
			ReportLoaderUtil.createDir(CaTIESProperties.getValue(CaTIESConstants.BAD_FILE_DIR));
			// Thread for stopping file poller server
			Thread stopThread = new StopServer(CaTIESConstants.FILE_POLLER_PORT);
			stopThread.start();
		}
		catch (IOException ex)
		{
			logger.error("Error while creating directories ", ex);
		}
		catch (Exception ex)
		{
			logger.error("Error while creating directories ", ex);
		}
		try
		{
			inputDir = new File(CaTIESProperties.getValue(CaTIESConstants.INPUT_DIR));
			// Loop to contineusly poll on directory for new incoming files
			while (true)
			{
				files = inputDir.list();
				if (files.length > 0)
				{
					logger.info("Invoking parser to parse input file");
					// this invokes ReportProcessor thread
					poller.obr.notifyEvent(files);
				}
				logger.info("Report Loader Server is going to sleep for " +
					CaTIESProperties.getValue(CaTIESConstants.POLLER_SLEEPTIME) + "ms");
				Thread.sleep(Long.parseLong(CaTIESProperties.getValue(
						CaTIESConstants.POLLER_SLEEPTIME)));
			}
		}
		catch (Exception ex)
		{
			logger.error("Error while initializing parser manager ", ex);
		}
	}

	/**
	 * Register.
	 * @see edu.wustl.catissuecore.reportloader.Observable#register(edu.wustl.
	 * catissuecore.reportloader.Observer)
	 * @param observer object of observer
	 */
	public void register(Observer observer)
	{
		this.obr = observer;
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