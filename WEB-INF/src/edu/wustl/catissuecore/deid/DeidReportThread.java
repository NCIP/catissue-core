package edu.wustl.catissuecore.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jdom.Element;
import org.jdom.output.Format;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is a thread which converts a single identified report into its equivalent de-identified report.
 * @author vijay_pande
 */
public class DeidReportThread extends Thread
{
	public static final Object obj=new Object();
	private IdentifiedSurgicalPathologyReport identifiedReport;
	
	/**
	 * constructor for the DeidReportThread thread 
	 * @param identifiedReport identified Surgical Pathology Report
	 * @throws Exception Generic excpetion
	 */
	public DeidReportThread(IdentifiedSurgicalPathologyReport ispr) throws Exception
	{
		this.identifiedReport=ispr;	
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
			// instantiate document
			org.jdom.Document currentRequestDocument = new org.jdom.Document(new Element("Dataset"));
			// get participant from CPR
			String hqlQuery="select cpr.participant from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr, " +
					" edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg" +
					" where scg.id="+identifiedReport.getSpecimenCollectionGroup().getId() +
					" and scg.id in elements(cpr.specimenCollectionGroupCollection)";
			List participantList=(List)CaCoreAPIService.executeQuery(hqlQuery, Participant.class.getName());
			Participant participant=null;
			if(participantList!=null && participantList.size()>0)
			{
				participant=(Participant)participantList.get(0);
			}
			
			// get textcontent
			TextContent textContent=(TextContent)CaCoreAPIService.getObject(TextContent.class, Constants.SYSTEM_IDENTIFIER, identifiedReport.getTextContent().getId());
			
			// build report element using report text
			Element reportElement = DeidUtils.buildReportElement(participant, identifiedReport, textContent.getData());
			// add report element to root of the document
			currentRequestDocument.getRootElement().addContent(reportElement);
	        
			// convert document ino string
	        String deidRequest = DeidUtils.convertDocumentToString(currentRequestDocument, Format.getPrettyFormat()); 
	        Logger.out.info("Calling native call for report "+identifiedReport.getId().toString());
	        // function call which contains the actual native call for deidentification
	        String deidReportText=deIdentify(deidRequest);
	        Logger.out.info("Calling native finished successfully for report "+identifiedReport.getId().toString());
	        String deidText="";
	        Logger.out.info("Extracting report text for report "+identifiedReport.getId().toString());
	        // extract the report text
	        deidText=DeidUtils.extractReport(deidReportText, CaTIESProperties.getValue(CaTIESConstants.DEID_DTD_FILENAME));
	        Logger.out.info("Extracting report text finished for report "+identifiedReport.getId().toString());
	        Date deidCollectionDate=null;
	   
	        // ectract collection date and time
	        deidCollectionDate=DeidUtils.extractDate(deidText);	
	        deidText = deidText.substring(0, deidText.lastIndexOf("||-"));
	        Logger.out.info("Creating deidentified report for identified report id="+identifiedReport.getId().toString());
	        // Create object of deidentified report
	        DeidentifiedSurgicalPathologyReport pathologyReport = createDeidPathologyReport(identifiedReport, deidText, deidCollectionDate);
	        Logger.out.info("De-identification process finished for "+identifiedReport.getId().toString());
	        saveReports(pathologyReport);
	        Long endTime = new Date().getTime();
	        CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString()+","+identifiedReport.getId()+","+CaTIESConstants.DEIDENTIFIED+","+"Report De-identified successfully,"+(endTime-startTime));
		}
    	catch(Throwable ex)
    	{
    		Logger.out.error("Deidentification process is failed:",ex);
			try
			{
				Long endTime = new Date().getTime();
				CSVLogger.error(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString()+","+identifiedReport.getId()+","+CaTIESConstants.FAILURE+","+ex.getMessage()+",,"+(endTime-startTime));
				// if any exception occures then update the status of the identified report to failed
				identifiedReport.setReportStatus(CaTIESConstants.DEID_PROCESS_FAILED);
				CaCoreAPIService.updateObject(identifiedReport);
			}
			catch(Exception e)
			{
				Logger.out.error("DeidReportThread: Updating Identified report status failed",e);
			}
    		Logger.out.error("Upexpected error in DeidReportThread thread", ex);
    	}
	}
	
	/**
	 * Method to create and initialize object of DeidentifiedSurgicalPathologyReport
	 * @param identifiedReport identified surgical pathology report
	 * @param deidText de-intified text
	 * @param deidCollectedDate collection date and time of report
	 * @return DeidentifiedSurgicalPathologyReport
	 * @throws Exception a generic exception oocured while creating de-identified report instance.
	 */
	private DeidentifiedSurgicalPathologyReport createDeidPathologyReport(IdentifiedSurgicalPathologyReport ispr, String deidText,
            Date deidCollectedDate) throws Exception
	{
		Logger.out.info("Creating deid report for identifiedReport id="+ispr.getId());
		// instnatiate deidentified report
		DeidentifiedSurgicalPathologyReport deidReport=new DeidentifiedSurgicalPathologyReport();
        		  
		// set default values for deidentified report
		deidReport.setActivityStatus(ispr.getActivityStatus()); 
       	deidReport.setReportStatus(CaTIESConstants.PENDING_FOR_XML);
       	deidReport.setIsQuanrantined(Constants.ACTIVITY_STATUS_ACTIVE);
        deidReport.setSpecimenCollectionGroup(ispr.getSpecimenCollectionGroup());
        TextContent tc=new TextContent();
        // set deidentified text to text content
        tc.setData(deidText);
        // set identified report to deidentified report
        tc.setSurgicalPathologyReport(deidReport);
        
        // set text content which contains deidentified text to deidentified report 
        deidReport.setTextContent(tc);
        // set default value for flag for review
        deidReport.setIsFlagForReview(new Boolean(false));
        
        return deidReport;
    }
	
	/**
	 * This method is responsible for preparing and calling a native method call to convert plain text into deindentified text.
	 * @param text text to be de-identified
	 * @return de-identified text
	 * @throws Exception ocured while calling a native method call for de-identification
	 */
	public String deIdentify(String text) throws Exception
	{
		
		String output = "";	
		try 
		{
			synchronized(obj)
			{
				// file for xml formated input text
				File predeidFile = new File("predeid.xml");
				// temp file for processing input file
				File postdeidFile = new File("postdeid.tmp");
	
				FileWriter fw = new FileWriter(predeidFile);
				// write contents to input xml file
				fw.write(text);
				fw.close();
				Logger.out.info("Calling native call");
				DeIDPipelineManager.deid.createDeidentifier(predeidFile.getAbsolutePath(), postdeidFile.getAbsolutePath()+ "?XML", DeIDPipelineManager.configFileName);
				Logger.out.info("Native call success");
				BufferedReader br = new BufferedReader(new FileReader(postdeidFile));
				
				//read all contents from output file
				String line = "";
				while ((line = br.readLine()) != null) 
				{
					// add content to output string
					output += line + "\n";
				}
				Logger.out.info("Deleting temp files");
				br.close();
				// delete temporary input and output files
				predeidFile.delete();
				postdeidFile.delete();
				// notify on object to process next waiting call
				obj.notifyAll();
			}
		
		}
		catch (IOException ex)
		{
			Logger.out.error("File system error occured while creating or deleting temporary files for deidentification",ex);
			throw ex;
		}
		catch (Exception ex)
		{
			Logger.out.error("Severe error occured in the native method call for deidentification",ex);
			throw ex;
		}
		return output;
	}
	
	/**
	 * Method to save deidentified report and to update status of identified report
	 * @param pathologyReport deidentified report to be saved
	 * @throws Exception generic exception occured
	 */
	private void saveReports(DeidentifiedSurgicalPathologyReport pathologyReport) throws Exception
	{
		{
    		Logger.out.info("Saving deidentified report for identified report id="+identifiedReport.getId().toString());
    		// save deidentified report
    		pathologyReport=(DeidentifiedSurgicalPathologyReport)CaCoreAPIService.createObject(pathologyReport);
    		Logger.out.info("deidentified report saved for identified report id="+identifiedReport.getId().toString());
    		// update status of identified report
        	identifiedReport.setReportStatus(CaTIESConstants.DEIDENTIFIED);
        	// set deidentified report to identified report
        	identifiedReport.setDeIdentifiedSurgicalPathologyReport(pathologyReport);
        	Logger.out.debug("Updating identified report report id="+identifiedReport.getId().toString());
        	// update object of identified report
        	CaCoreAPIService.updateObject(identifiedReport);
        }
	}
}