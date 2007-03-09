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
	public static final Object OBJ=new Object();
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
			org.jdom.Document currentRequestDocument = new org.jdom.Document(new Element("Dataset"));
			SpecimenCollectionGroup scg=ispr.getSpecimenCollectionGroup();
			Participant participant=scg.getCollectionProtocolRegistration().getParticipant();
			
			TextContent tc=ispr.getTextContent();
			tc.setData(synthesizeSPRText(ispr));
			ispr.setTextContent(tc);
			
			Element reportElement = DeidUtils.buildReportElement(participant, ispr, ispr.getTextContent().getData());
			currentRequestDocument.getRootElement().addContent(reportElement);
	        
	        String deidRequest = DeidUtils.convertDocumentToString(currentRequestDocument, Format.getPrettyFormat()); 
	        
	        String deidReport=deIdentify(deidRequest);
	        
	        String deidText="";
	        
	        deidText=DeidUtils.extractReport(deidReport, XMLPropertyHandler.getValue("deid.dtd.filename"));
	        
	        Date deidCollectionDate=null;
	   
	        deidCollectionDate=DeidUtils.extractDate(deidText);	
	    
	        DeidentifiedSurgicalPathologyReport pathologyReport = createPathologyReport(ispr, deidText, deidCollectionDate);
	        
	    	try
	    	{
	    		ReportLoaderUtil.saveObject(pathologyReport);
	        	ispr.setReportStatus(Parser.DEIDENTIFIED);
	        	ispr.setDeidentifiedSurgicalPathologyReport(pathologyReport);
	        	ReportLoaderUtil.updateObject(ispr);
	    	}
	    	catch(DAOException daoEx)
	    	{
	    		Logger.out.error("Error while saving//updating Deidentified//Identified report ",daoEx);
	    	} 
		}
    	catch(Exception ex)
    	{
    		Logger.out.error("Deidentification process is failed:",ex);
			try
			{
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
		DeidentifiedSurgicalPathologyReport deidReport=new DeidentifiedSurgicalPathologyReport();
		if (ispr.getCollectionDateTime() != null) 
		{
            deidReport.setCollectionDateTime(deidCollectedDate);
        }
		  
       	deidReport.setAccessionNumber(ispr.getAccessionNumber());
       	deidReport.setActivityStatus(ispr.getActivityStatus()); 
       	deidReport.setReportStatus(Parser.PENDING_FOR_XML);
       	deidReport.setIsQuanrantined(Constants.ACTIVITY_STATUS_ACTIVE);
        deidReport.setSpecimenCollectionGroup(ispr.getSpecimenCollectionGroup());
        TextContent tc=new TextContent();
        tc.setData(deidText);
        tc.setSurgicalPathologyReport(deidReport);
        
        deidReport.setSource(ispr.getSource());
        deidReport.setTextContent(tc);
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

		//Get report sections for each report

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
				nameToText.put(abbr, text);
			}
		}
		else
		{
			Logger.out.info("NULL report section collection found in synthesizeSPRText method");
		}

		for (int x = 0; x < DeIDPipelineManager.sectionPriority.size(); x++) 
		{
			String abbr = (String) DeIDPipelineManager.sectionPriority.get(x);
			String sectionHeader = (String) DeIDPipelineManager.abbrToHeader.get(abbr);
			if (nameToText.containsKey(abbr)) 
			{
				String sectionText = (String) nameToText.get(abbr);

				docText += "[" + sectionHeader + "]" + "\n\n" + sectionText + "\n\n\n";
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
			synchronized(OBJ)
			{
				File f = new File("predeid.xml");
				File f2 = new File("postdeid.tmp");
	
				FileWriter fw = new FileWriter(f);
				fw.write(text);
				fw.close();
	
				DeIDPipelineManager.deid.createDeidentifier(f.getAbsolutePath(), f2.getAbsolutePath()+ "?XML", DeIDPipelineManager.configFileName);
	
				BufferedReader br = new BufferedReader(new FileReader(f2));
	
				String line = "";
				while ((line = br.readLine()) != null) 
				{
					output += line + "\n";
				}
				br.close();
				f.delete();
				f2.delete();
				OBJ.notifyAll();
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
