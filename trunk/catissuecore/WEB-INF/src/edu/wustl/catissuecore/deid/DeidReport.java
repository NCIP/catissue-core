/**
 * 
 */
package edu.wustl.catissuecore.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jdom.Element;
import org.jdom.output.Format;


import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.reportloader.CSVLogger;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author vijay_pande
 * This class is a thread which converts a single identified report into its equivalent de-identified report.
 */
public class DeidReport extends Thread
{
	public static final Object obj=new Object();
	private IdentifiedSurgicalPathologyReport ispr;
	/**
	 * @param ispr identified Surgical Pathology Report
	 * @throws Exception
	 * constructor for the DeidReport thread 
	 */
	public DeidReport(IdentifiedSurgicalPathologyReport ispr) throws Exception
	{
		this.ispr=ispr;	
	}
	
	
	/** 
	 * @see java.lang.Thread#run()
	 * This is default run method of the thread. Which is like a deid pipeline. This pipeline manages the de-identification process. 
	 */
	public void run()
	{
		try
		{
			
			Logger.out.info("De-identification process started for "+ispr.getId().toString());
			// instantiate document
			org.jdom.Document currentRequestDocument = new org.jdom.Document(new Element("Dataset"));
			// get SCG
			SpecimenCollectionGroup scg=ispr.getSpecimenCollectionGroup();
			// get participant from SCG
			Participant participant=scg.getCollectionProtocolRegistration().getParticipant();
			
			// get textcontent
			TextContent tc=ispr.getTextContent();
			// synthesize the text content od identified report
			tc.setData(synthesizeSPRText(ispr));
			Logger.out.info("ReportText is synthesized for report "+ispr.getId().toString());
			// set synthesized text content back to identified report
			ispr.setTextContent(tc);
			
			// build report element using report text
			Element reportElement = DeidUtils.buildReportElement(participant, ispr, ispr.getTextContent().getData());
			// add report element to root of the document
			currentRequestDocument.getRootElement().addContent(reportElement);
	        
			// convert document ino string
	        String deidRequest = DeidUtils.convertDocumentToString(currentRequestDocument, Format.getPrettyFormat()); 
	        Logger.out.info("Calling native call for report "+ispr.getId().toString());
	        // function call which contains the actual native call for deidentification
	        String deidReport=deIdentify(deidRequest);
	        Logger.out.info("Calling native finished successfully for report "+ispr.getId().toString());
	        String deidText="";
	        Logger.out.info("Extracting report text for report "+ispr.getId().toString());
	        // extract the report text
	        deidText=DeidUtils.extractReport(deidReport, XMLPropertyHandler.getValue("deid.dtd.filename"));
	        Logger.out.info("Extracting report text finished for report "+ispr.getId().toString());
	        Date deidCollectionDate=null;
	   
	        // ectract collection date and time
	        deidCollectionDate=DeidUtils.extractDate(deidText);	
	        Logger.out.info("Creating deidentified report for identified report id="+ispr.getId().toString());
	        // Create object of deidentified report
	        DeidentifiedSurgicalPathologyReport pathologyReport = createPathologyReport(ispr, deidText, deidCollectionDate);
	        Logger.out.info("De-identification process finished for "+ispr.getId().toString());
	    	try
	    	{
	    		Logger.out.info("Saving deidentified report for identified report id="+ispr.getId().toString());
	    		// save deidentified report
	    		ReportLoaderUtil.saveObject(pathologyReport);
	    		Logger.out.info("deidentified report saved for identified report id="+ispr.getId().toString());
	    		// update status of identified report
	        	ispr.setReportStatus(Parser.DEIDENTIFIED);
	        	// set deidentified report to identified report
	        	ispr.setDeidentifiedSurgicalPathologyReport(pathologyReport);
	        	Logger.out.info("Updating identified report report id="+ispr.getId().toString());
	        	// update object of identified report
	        	ReportLoaderUtil.updateObject(ispr);
	        	CSVLogger.info(Parser.LOGGER_DEID_SERVER, new Date().toString()+","+ispr.getId()+","+Parser.DEIDENTIFIED+","+"Report De-identified successfully");
	    	}
	    	catch(DAOException daoEx)
	    	{
	    		CSVLogger.info(Parser.LOGGER_DEID_SERVER, new Date().toString()+","+ispr.getId()+","+Parser.FAILURE+","+daoEx.getMessage());
	    		Logger.out.error("Error while saving//updating Deidentified//Identified report ",daoEx);
	    	} 
		}
    	catch(Exception ex)
    	{
    		Logger.out.error("Deidentification process is failed:",ex);
			try
			{
				CSVLogger.error(Parser.LOGGER_DEID_SERVER, new Date().toString()+","+ispr.getId()+","+Parser.FAILURE+","+ex.getMessage());
				// if any exception occures then update the status of the identified report to failed
				ispr.setReportStatus(Parser.DEID_PROCESS_FAILED);
				ReportLoaderUtil.updateObject(ispr);
			}
			catch(Exception e)
			{
				Logger.out.error("DeidReport: Updating Identified report status failed",e);
			}
    		Logger.out.error("Upexpected error in DeidReport thread", ex);
    	}
	}
	
	/**
	 * @param ispr identified surgical pathology report
	 * @param deidText de-intified text
	 * @param deidCollectedDate collection date and time of report
	 * @return DeidentifiedSurgicalPathologyReport
	 * @throws Exception a generic exception oocured while creating de-identified report instance.
	 */
	private DeidentifiedSurgicalPathologyReport createPathologyReport(IdentifiedSurgicalPathologyReport ispr, String deidText,
            Date deidCollectedDate) throws Exception
	{
		Logger.out.info("Creating deid report for ispr id="+ispr.getId());
		// instnatiate deidentified report
		DeidentifiedSurgicalPathologyReport deidReport=new DeidentifiedSurgicalPathologyReport();
		
		if (ispr.getCollectionDateTime() != null) 
		{
			//if collection date and time is not null then set it to deidentifide repot
            deidReport.setCollectionDateTime(deidCollectedDate);
        }
		  
		// set default values for deidentified report
		// deidReport.setAccessionNumber(ispr.getAccessionNumber());
       	deidReport.setActivityStatus(ispr.getActivityStatus()); 
       	deidReport.setReportStatus(Parser.PENDING_FOR_XML);
       	deidReport.setIsQuanrantined(Constants.ACTIVITY_STATUS_ACTIVE);
        deidReport.setSpecimenCollectionGroup(ispr.getSpecimenCollectionGroup());
        TextContent tc=new TextContent();
        // set deidentified text to text content
        tc.setData(deidText);
        // set identified report to deidentified report
        tc.setSurgicalPathologyReport(deidReport);
        
        // set source for deidentified report
        deidReport.setSource(ispr.getSource());
        // set text content which contains deidentified text to deidentified report 
        deidReport.setTextContent(tc);
        // set default value for flag for review
        deidReport.setIsFlagForReview(new Boolean(false));
        
        return deidReport;
    }
	
	/**
	 * @param ispr identified surgical pathoology report
	 * @return sysnthesized Surgical pathology report text
	 * @throws Exception a generic exception occured while synthesizing report text
	 */
	private String synthesizeSPRText(final IdentifiedSurgicalPathologyReport ispr) throws Exception
	{
		String docText = "";
		//Get report sections for report
		Set iss=ispr.getTextContent().getReportSectionCollection();
		HashMap <String,String>nameToText = new HashMap<String, String>();
		if(iss!=null)
		{       	
			for (Iterator i = iss.iterator(); i.hasNext();) 
			{
				//Synthesize sections
				ReportSection rs = (ReportSection) i.next();
				String abbr = rs.getName();
				String text = rs.getDocumentFragment();
				//add abbreviation and report text to hash map
				nameToText.put(abbr, text);
			}
		}
		else
		{
			Logger.out.info("NULL report section collection found in synthesizeSPRText method");
		}
		for (int x = 0; x < DeIDPipelineManager.sectionPriority.size(); x++) 
		{
			// get abbreviation
			String abbr = (String) DeIDPipelineManager.sectionPriority.get(x);
			// get full section header name from its abbreviation 
			String sectionHeader = (String) DeIDPipelineManager.abbrToHeader.get(abbr);
			
			if (nameToText.containsKey(abbr)) 
			{
				//if the key is present in the report section collection map then format the section header and section text
				String sectionText = (String) nameToText.get(abbr);
				// format for section header and section text
				docText += "\n[" + sectionHeader + "]" + "\n\n" + sectionText + "\n\n";
			}
		}
		return docText.trim();
	}
	
	/**
	 * @param text text to be de-identified
	 * @return de-identified text
	 * @throws Exception ocured while calling a native method call for de-identification
	 * This method is responsible for preparing and calling a native method call to convert plain text into deindentified text.
	 */
	public String deIdentify(String text) throws Exception
	{
		
		String output = "";	
		try 
		{
			synchronized(obj)
			{
				// file for xml formated input text
				File f = new File("predeid.xml");
				// temp file for processing input file
				File f2 = new File("postdeid.tmp");
	
				FileWriter fw = new FileWriter(f);
				// write contents to input xml file
				fw.write(text);
				fw.close();
				Logger.out.info("Calling native call");
				DeIDPipelineManager.deid.createDeidentifier(f.getAbsolutePath(), f2.getAbsolutePath()+ "?XML", DeIDPipelineManager.configFileName);
				Logger.out.info("Native call success");
				BufferedReader br = new BufferedReader(new FileReader(f2));
				
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
				f.delete();
				f2.delete();
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
}