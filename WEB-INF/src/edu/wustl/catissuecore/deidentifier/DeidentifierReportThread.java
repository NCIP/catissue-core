
package edu.wustl.catissuecore.deidentifier;

import java.util.Date;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is a thread which converts a single identified report into its
 * equivalent de-identified report.
 * @author vijay_pande
 */
public class DeidentifierReportThread extends Thread
{

	/**
	 * looger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DeidentifierReportThread.class);
	/**
	 * obj.
	 */
	public static final Object obj = new Object();
	/**
	 * identifiedReport.
	 */
	private final IdentifiedSurgicalPathologyReport identifiedReport;
	/**
	 * deidentifier.
	 */
	private final AbstractDeidentifier deidentifier;

	/**
	 * constructor for the DeidReportThread thread.
	 * @param deidentifier : deidentifier
	 * @param ispr
	 *            identified Surgical Pathology Report
	 * @throws Exception
	 *             Generic exception
	 */
	public DeidentifierReportThread(IdentifiedSurgicalPathologyReport ispr,
			AbstractDeidentifier deidentifier) throws Exception
	{
		this.identifiedReport = ispr;
		this.deidentifier = deidentifier;
	}

	/**
	 * This is default run method of the thread. Which is like a deid pipeline.
	 * This pipeline manages the de-identification process.
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		final Long startTime = new Date().getTime();
		try
		{
			this.logger.info("De-identification process started for "
					+ this.identifiedReport.getId().toString());
			final DeidentifiedSurgicalPathologyReport deidentifiedReport = this.deidentifier
					.deidentify(this.identifiedReport);
			this.saveReports(deidentifiedReport);
		}
		catch (final Throwable ex)
		{
			this.logger.error("Deidentification process is failed:"+ex.getMessage(), ex);
			ex.printStackTrace();
			try
			{
				// if any exception occures then update the status of the
				// identified report to failed
				this.identifiedReport.setReportStatus(CaTIESConstants.DEID_PROCESS_FAILED);
				CaCoreAPIService.updateObject(this.identifiedReport);
				final Long endTime = new Date().getTime();
				CSVLogger.error(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString() + ","
						+ this.identifiedReport.getId() + "," + CaTIESConstants.FAILURE + ","
						+ ex.getMessage() + ",," + (endTime - startTime));
			}
			catch (final Exception e)
			{
				this.logger.error("DeidReportThread:Updating Identified" +
						" report status failed"+e.getMessage(), e);
				e.printStackTrace();
			}
			this.logger.error("Upexpected error in DeidReportThread thread", ex);
			return;
		}
		final Long endTime = new Date().getTime();
		CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString() + ","
				+ this.identifiedReport.getId() + "," + CaTIESConstants.DEIDENTIFIED + ","
				+ "Report De-identified successfully," + (endTime - startTime));
	}

	/**
	 * Method to save deidentified report and to update status of identified.
	 * report
	 * @param deidentifiedReport
	 *            deidentified report to be saved
	 * @throws Exception
	 *             generic exception occurred
	 */
	private void saveReports(DeidentifiedSurgicalPathologyReport deidentifiedReport)
			throws Exception
	{
		{
			// set default values for deidentified report
			deidentifiedReport.setActivityStatus(this.identifiedReport.getActivityStatus());
			deidentifiedReport.setReportStatus(CaTIESConstants.PENDING_FOR_XML);
			deidentifiedReport.setIsQuarantined(Status.ACTIVITY_STATUS_ACTIVE.toString());
			deidentifiedReport.setSpecimenCollectionGroup(this.identifiedReport
					.getSpecimenCollectionGroup());

			this.logger.info("Saving deidentified report for identified report id="
					+ this.identifiedReport.getId().toString());
			// save deidentified report
			deidentifiedReport = (DeidentifiedSurgicalPathologyReport) CaCoreAPIService
					.createObject(deidentifiedReport);
			this.logger.info("deidentified report saved for identified report id="
					+ this.identifiedReport.getId().toString());
		}
	}
}