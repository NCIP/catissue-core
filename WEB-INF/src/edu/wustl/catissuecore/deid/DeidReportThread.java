package edu.wustl.catissuecore.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Hibernate;

import org.jdom.Element;
import org.jdom.output.Format;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
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
		try
		{
			DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
			Logger.out.info("De-identification process started for "+identifiedReport.getId().toString());
			// instantiate document
			org.jdom.Document currentRequestDocument = new org.jdom.Document(new Element("Dataset"));
			// get SCG
			List scgList=defaultBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), Constants.SYSTEM_IDENTIFIER, identifiedReport.getSpecimenCollectionGroup().getId());
			SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgList.get(0);
			// get participant from SCG
			Participant participant=(Participant)defaultBizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(), scg.getCollectionProtocolRegistration().getId(), Constants.COLUMN_NAME_PARTICIPANT); //scg.getCollectionProtocolRegistration().getParticipant();
			
			// get textcontent
			TextContent textContent=(TextContent)defaultBizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), Constants.COLUMN_NAME_TEXT_CONTENT);
			// synthesize the text content od identified report
			textContent.setData(Hibernate.createClob( synthesizeSPRText(identifiedReport)));
			Logger.out.info("ReportText is synthesized for report "+identifiedReport.getId().toString());
			// set synthesized text content back to identified report
			identifiedReport.setTextContent(textContent);
			
			String textContentData=textContent.getData().getSubString(1,(int) textContent.getData().length());
			// build report element using report text
			Element reportElement = DeidUtils.buildReportElement(participant, identifiedReport, textContentData);
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
	        Logger.out.info("Creating deidentified report for identified report id="+identifiedReport.getId().toString());
	        // Create object of deidentified report
	        DeidentifiedSurgicalPathologyReport pathologyReport = createDeidPathologyReport(identifiedReport, deidText, deidCollectionDate);
	        Logger.out.info("De-identification process finished for "+identifiedReport.getId().toString());
	    	try
	    	{
	    		Logger.out.info("Saving deidentified report for identified report id="+identifiedReport.getId().toString());
	    		// save deidentified report
//	    		pathologyReport.setConceptReferentCollection(new HashSet());
	    		ReportLoaderUtil.saveObject(pathologyReport);
	    		Logger.out.info("deidentified report saved for identified report id="+identifiedReport.getId().toString());
	    		// update status of identified report
	        	identifiedReport.setReportStatus(CaTIESConstants.DEIDENTIFIED);
	        	// set deidentified report to identified report
	        	identifiedReport.setDeIdentifiedSurgicalPathologyReport(pathologyReport);
	        	Logger.out.debug("Updating identified report report id="+identifiedReport.getId().toString());
	        	// update object of identified report
	        	ReportLoaderUtil.updateObject(identifiedReport);
	        	CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString()+","+identifiedReport.getId()+","+CaTIESConstants.DEIDENTIFIED+","+"Report De-identified successfully");
	    	}
	    	catch(DAOException daoEx)
	    	{
	    		CSVLogger.info(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString()+","+identifiedReport.getId()+","+CaTIESConstants.FAILURE+","+daoEx.getMessage());
	    		Logger.out.error("Error while saving//updating Deidentified//Identified report ",daoEx);
	    	} 
		}
    	catch(Throwable ex)
    	{
    		Logger.out.error("Deidentification process is failed:",ex);
			try
			{
				CSVLogger.error(CaTIESConstants.LOGGER_DEID_SERVER, new Date().toString()+","+identifiedReport.getId()+","+CaTIESConstants.FAILURE+","+ex.getMessage());
				// if any exception occures then update the status of the identified report to failed
				identifiedReport.setReportStatus(CaTIESConstants.DEID_PROCESS_FAILED);
				ReportLoaderUtil.updateObject(identifiedReport);
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
		
		if (ispr.getCollectionDateTime() != null) 
		{
			//if collection date and time is not null then set it to deidentifide repot
            deidReport.setCollectionDateTime(deidCollectedDate);
        }
		  
		// set default values for deidentified report
		deidReport.setActivityStatus(ispr.getActivityStatus()); 
       	deidReport.setReportStatus(CaTIESConstants.PENDING_FOR_XML);
       	deidReport.setIsQuanrantined(Constants.ACTIVITY_STATUS_ACTIVE);
        deidReport.setSpecimenCollectionGroup(ispr.getSpecimenCollectionGroup());
        TextContent tc=new TextContent();
        // set deidentified text to text content
        tc.setData(Hibernate.createClob(deidText));
        // set identified report to deidentified report
        tc.setSurgicalPathologyReport(deidReport);
        
        // set reportSource for deidentified report
        deidReport.setReportSource(ispr.getReportSource());
        // set text content which contains deidentified text to deidentified report 
        deidReport.setTextContent(tc);
        // set default value for flag for review
        deidReport.setIsFlagForReview(new Boolean(false));
        
        return deidReport;
    }
	
	/**
	 * Method to synthesize report text
	 * @param identifiedReport identified surgical pathoology report
	 * @return sysnthesized Surgical pathology report text
	 * @throws Exception a generic exception occured while synthesizing report text
	 */
	private String synthesizeSPRText(final IdentifiedSurgicalPathologyReport identifiedReport) throws Exception
	{
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		String docText = "";
		//Get report sections for report
		Set<ReportSection> iss=(Set)defaultBizLogic.retrieveAttribute(TextContent.class.getName(), identifiedReport.getTextContent().getId(), Constants.COLUMN_NAME_REPORT_SECTION_COLL);
		HashMap <String,String>nameToText = new HashMap<String, String>();
		if(iss!=null)
		{       	
			for (ReportSection rs : iss) 
			{
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
		for (String key : DeIDPipelineManager.abbrToHeader.keySet()) 
		{
			if (nameToText.containsKey(key)) 
			{
				// get full section header name from its abbreviation 
				String sectionHeader = (String) DeIDPipelineManager.abbrToHeader.get(key);
				//if the key is present in the report section collection map then format the section header and section text
				String sectionText = nameToText.get(key);
				// format for section header and section text
				docText += "\n[" + sectionHeader + "]" + "\n\n" + sectionText + "\n\n";
			}
		}
		return docText.trim();
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
}