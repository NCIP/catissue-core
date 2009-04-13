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
 * This class is a thread which converts a single identified report into its equivalent de-identified report.
 * @author vijay_pande
 */
public class DeidentifierReportThread extends Thread
{
	public static final Object obj=new Object();
	private IdentifiedSurgicalPathologyReport identifiedReport;
	private AbstractDeidentifier deidentifier;
	/**
	 * constructor for the DeidReportThread thread 
	 * @param identifiedReport identified Surgical Pathology Report
	 * @throws Exception Generic exception
	 */
	public DeidentifierReportThread(IdentifiedSurgicalPathologyReport ispr, AbstractDeidentifier deidentifier) throws Exception
	{
		this.identifiedReport=ispr;
		this.deidentifier=deidentifier;
	}
	
	/**
	 * This is default run method of the thread. Which is like a deid pipeline. This pipeline manages the de-identification process. 
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		Long startTime = new Date().getTime();
		try
		{
			Logger.out.info("De-identification process started for "+identifiedReport.getId().toString());
			DeidentifiedSurgicalPathologyReport deidentifiedReport=deidentifier.deidentify(identifiedReport);
			saveReports(deidentifiedReport);
		}
    	catch(Throwable ex)
    	{
    		Logger.out.error("Deidentification process is failed:",ex);
			try
			{
				// if any exception occures then update the status of the identified report to failed
				identifiedReport.setReportStatus(CaTIESConstants.DEID_PROCESS_FAILED);
				CaCoreAPIService.updateObject(identifiedReport);
				Long endTime = new Date().getTime();
				CSVLogger.error(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString()+","+identifiedReport.getId()+","+CaTIESConstants.FAILURE+","+ex.getMessage()+",,"+(endTime-startTime));
			}
			catch(Exception e)
			{
				Logger.out.error("DeidReportThread: Updating Identified report status failed",e);
			}
    		Logger.out.error("Upexpected error in DeidReportThread thread", ex);
    		return;
    	}
		Long endTime = new Date().getTime();
        CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString()+","+identifiedReport.getId()+","+CaTIESConstants.DEIDENTIFIED+","+"Report De-identified successfully,"+(endTime-startTime));
	}

	/**
	 * Method to save deidentified report and to update status of identified report
	 * @param deidentifiedReport deidentified report to be saved
	 * @throws Exception generic exception occurred
	 */
	private void saveReports(DeidentifiedSurgicalPathologyReport deidentifiedReport) throws Exception
	{
		{
			// set default values for deidentified report
			deidentifiedReport.setActivityStatus(identifiedReport.getActivityStatus()); 
			deidentifiedReport.setReportStatus(CaTIESConstants.PENDING_FOR_XML);
			deidentifiedReport.setIsQuarantined(Status.ACTIVITY_STATUS_ACTIVE.toString());
			deidentifiedReport.setSpecimenCollectionGroup(identifiedReport.getSpecimenCollectionGroup());
	        
    		Logger.out.info("Saving deidentified report for identified report id="+identifiedReport.getId().toString());
    		// save deidentified report
    		deidentifiedReport=(DeidentifiedSurgicalPathologyReport)CaCoreAPIService.createObject(deidentifiedReport);
    		Logger.out.info("deidentified report saved for identified report id="+identifiedReport.getId().toString());
        }
	}
}