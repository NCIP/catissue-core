package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a file parser which parses HL7 Report format.    
 */
public class HL7Parser implements Parser
{

	/**
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 * @param participant Object of participant
	 * @param reportMap a report map containing sections to be processed
	 * @param scg object of SpecimenCollectionGroup
	 * @throws Exception generic exception
	 */
	private void process(Participant participant,Map<String, Set> reportMap, SpecimenCollectionGroup scg) throws Exception
	{
		Logger.out.debug("Procesing report");
		ReportSection reportSection = null;
		TextContent textContent = null;
		Set<ReportSection> reportSectionSet = null;
		ReportLoader reportLoader = null;
		IdentifiedSurgicalPathologyReport report = null;
		reportSectionSet = new HashSet<ReportSection>();
		Set reportSet=null;
		Iterator reportIterator=null;
		String spn=null;
		Site site=null;
		
		try
		{
			String line = "";
			line=HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.PID);
			//participant = parserParticipantInformation(line);
			site=parseSiteInformation(line);
			line=HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR);
			Logger.out.debug("Creating report");
			report = extractOBRSegment(line);
			Logger.out.debug("Report Created Successfully");
			spn=HL7ParserUtil.getSurgicalPathologyNumber(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR));
			Logger.out.debug("Creating report section from report text");
			reportSet = this.getReportSectionDataFromReportMap(reportMap, CaTIESConstants.OBX);
			textContent=new TextContent();
			// Create report section using reportMap. Only OBX section will be stored into report section
			if(reportSet!=null && reportSet.size()>0)
			{
				reportIterator=reportSet.iterator();
				// Iterate on the reportSet to create report section
				while (reportIterator.hasNext())
				{
					reportSection = extractOBXSegment((String)reportIterator.next(), textContent);
					if (reportSection.getDocumentFragment() != null)
					{	
						reportSectionSet.add(reportSection);
						reportSection.setTextContent(textContent);
					}	
				}		
			}	
			Logger.out.debug("Associating report sections to report");
			if (report != null)
			{
				// set textContent to report
				report.setTextContent(textContent);
				textContent.setSurgicalPathologyReport(report);
				if (reportSectionSet != null && reportSectionSet.size() > 1)
				{
					report.getTextContent().setReportSectionCollection(reportSectionSet);
					
				}
				if(site!=null)
				{
					// create instance of ReportLoader to load report into the DB
					Logger.out.debug("Instantiating report loader to load report");
					reportLoader = new ReportLoader(participant, report,site, scg, spn);
				}
				else
				{
					// Update report queue with appropriate status
					//	reportLoader = new ReportLoader(participant, report,createNewSite(), scg);
					throw new Exception("Site not found!");
				}
				// initiate reportLoader to load report
				reportLoader.process();
			}
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while parsing the report map",ex);
			throw ex;
		}
	}	
	
	/**
	 * This is a mehod which parses report string from queue and process it. 
	 * @param participant represents participant
	 * @param reportText report string
	 * @param scg object of SpecimenCollectionGroup
	 * @throws Exception generic exception
	 */
	public void parseString(Participant participant , String reportText, SpecimenCollectionGroup scg)throws Exception
	{
		Logger.out.debug("Inside parseString method");
		Map<String, Set> reportMap=null;
		try
		{
			reportMap=this.getReportMap(reportText);
			if(participant!=null)
			{
				process(participant,reportMap, scg);
			}
			else
			{
				Long startTime=new Date().getTime();
				String status=HL7ParserUtil.validateAndSaveReportMap(reportMap);
				Long endTime=new Date().getTime();
				CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER,new Date().toString()+",,,"+status+","+(endTime-startTime));
			}
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while creating report map ",ex);
			throw ex;
		}
	}
	
	/**
	 * This method parses the report
	 * @param fileName HL7 file name 
	 * @throws Exception generic exception
	 */
	public void parse(String fileName)throws Exception
	{
		//IdentifiedSurgicalPathologyReport report = null;
		StringTokenizer st = null;
		String token = null;
		Map<String, Set> reportMap=null;
		String status=null;
		FileReader fr=null;
		BufferedReader br=null;
		int counter=0;
		Long startTime=null;
		Long endTime=null;
		Long fileParseStartTime=null;
		Long fileParseEndTime=null;
		
		
		try
		{
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line = "";
			counter=0;
			Logger.out.info("Parsing File "+fileName+": Started at "+new Date().toString());
			fileParseStartTime=new Date().getTime();
			// loop while !EOF
			while ((line = br.readLine()) != null)
			{
				st = new StringTokenizer(line, "|");
				if (st.hasMoreTokens())
				{
					token = st.nextToken();
					// MSH or FTS are the starting point of individual report
					if(token.equals(CaTIESConstants.PID) || token.equals(CaTIESConstants.OBR) || token.equals(CaTIESConstants.OBX))
					{	
						this.addToReportMap(reportMap,token,line);
					}
					else if (token.equals(CaTIESConstants.MSH)|| token.equals(CaTIESConstants.FTS))
					{
						// save parsed report
						if (reportMap != null && reportMap.size()>0)
						{
							status=HL7ParserUtil.validateAndSaveReportMap(reportMap);
							endTime=new Date().getTime();
							counter++;
							CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER,new Date().toString()+","+fileName+","+counter+","+status+",,"+(endTime-startTime));
							Logger.out.info("Report is added to queue. Current Count is="+counter);
							// reinitialize variables to null to process next report
							reportMap.clear();
							reportMap=null;
						} 
						// if start of report found then initialize report map and set queue status to NEW
						startTime=new Date().getTime();
						reportMap = new HashMap<String, Set>();
					}
					// add PID, OBR, OBX section from report text to report map
					
				}	
			}
			fileParseEndTime=new Date().getTime();
			Logger.out.info("Parsing File "+fileName+": Finished at "+new Date().toString());
			CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER,"Total "+counter+" reports are added to report queue from file "+fileName+" Time reuired for parsing:"+(fileParseEndTime-fileParseStartTime));
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while reading HL7 file ",ex);
			throw ex;
		}
		finally
		{
			try
			{
				Logger.out.info("Final Count of the reports that are added to queue is="+counter);
				// close handle to file
				br.close();
				fr.close();
			}
			catch(IOException ex)
			{
				Logger.out.error("Error while closing file handle ",ex);
				throw ex;
			}
			catch(Exception ex)
			{
				Logger.out.error("Error while closing file handle ",ex);
				throw ex;
			}
		}
	}
	
   /**
    * Method to retrieve specific report section from reportMap
    * @param reportMap report map
    * @param key key of the report map
    * @return collection of report section data from the report map
    */
   private Set getReportSectionDataFromReportMap(Map<String, Set> reportMap , String key)
   {
	  Set tempSet = reportMap.get(key);
	  return tempSet;
   }
   
    /**
     * Method to create participant object using HL7 format report section for participant (PID)
     * @param pidLine participant information text
     * @return Participant from the participant information text
     * @throws Exception exception while parsing the participant information 
     */
	protected Participant parserParticipantInformation(String pidLine, Site site)throws Exception
	{
		Logger.out.info("Parsing participant information");
		Participant participant = new Participant();
		String pidLineforSite=pidLine;
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		ParticipantMedicalIdentifier medicalIdentification = null;
		Collection<ParticipantMedicalIdentifier> medicalIdentificationCollection = null;
		String field = null;
		String newPidLine = pidLine.replace('|', '~');
		newPidLine = newPidLine.replaceAll("~", "|~~");
		StringTokenizer st = new StringTokenizer(newPidLine, "|");
		//Validator validator = new Validator();

		for (int x = 0; st.hasMoreTokens(); x++)
		{

			// 	USE STRINGBUFFER FOR FIELD
			field = st.nextToken();
			if (field.equals("~~"))
			{
				continue;
			}
			else
			{
				field = field.replaceAll("~~", "");
			}	
			// Token for Participant medical record number
			if (x == CaTIESConstants.PARTICIPANT_MEDICAL_RECORD_INDEX) // Getting MRN
			{
				StringTokenizer st2 = new StringTokenizer(field, "^^^");
				String mrn = st2.nextToken();
				medicalIdentification = new ParticipantMedicalIdentifier();
				medicalIdentification.setMedicalRecordNumber(mrn);
				//set site
				// Site is set at the end of this function
				medicalIdentificationCollection = participant
						.getParticipantMedicalIdentifierCollection();
				if (medicalIdentificationCollection != null
						&& medicalIdentificationCollection.size() > 0)
				{	
					// add MRI to set
					medicalIdentificationCollection.add(medicalIdentification);
				}	
				else
				{
					// initialization of set
					medicalIdentificationCollection = new HashSet<ParticipantMedicalIdentifier>();
					medicalIdentificationCollection.add(medicalIdentification);
					participant
							.setParticipantMedicalIdentifierCollection(medicalIdentificationCollection);
				}
			}
			// token for participant name
			if (x == CaTIESConstants.PARTICIPANT_NAME_INDEX)
			{
				StringTokenizer st2 = new StringTokenizer(field, "^");
				String lname = "";
				String fname = "";
				String mname = "";
				
				// Last name 
				if (st2.hasMoreTokens())
				{	
					lname = st2.nextToken();
				}
				// first name
				if (st2.hasMoreTokens())
				{
					fname = st2.nextToken();
				}	
				// middle name
				if (st2.hasMoreTokens())
				{
					mname = st2.nextToken();
				}	
				participant.setFirstName(fname);
				participant.setLastName(lname);
				if (mname.trim().length() > 0)
				{	
					participant.setMiddleName(mname);
				}	
			}
			// token for participant date of birth
			if (x == CaTIESConstants.PARTICIPANT_DATEOFBIRTH_INDEX)
			{
				String year = field.substring(0, 4);
				String month = field.substring(4, 6);
				String day = field.substring(6, 8);

				GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(year), Integer
						.parseInt(month)-1, Integer.parseInt(day));

				participant.setBirthDate(gc.getTime());

			}
			// token for participant gender
			if (x == CaTIESConstants.PARTICIPANT_GENDER_INDEX)
			{
				if (field.equalsIgnoreCase(CaTIESConstants.MALE))
				{
					participant.setGender("Male Gender");
				}	
				else if (field.equalsIgnoreCase(CaTIESConstants.FEMALE))
				{
					participant.setGender("Female Gender");
				}	
			}
			// token for participant ethnicity
			if (x == CaTIESConstants.PARTICIPANT_ETHNICITY_INDEX)
			{
				String ethnicity = field;
				//make it null 
				ethnicity=null;
				participant.setEthnicity(ethnicity);
			}
			// token for participant Social Security Number
			if (x == CaTIESConstants.PARTICIPANT_SSN_INDEX)
			{
				String ssn = field;
				ssn=ReportLoaderUtil.getValidSSN(ssn);
				participant.setSocialSecurityNumber(ssn);
			}
		}
		//code of setSitetoParticipant function to avoid sepearte function call
		Collection<ParticipantMedicalIdentifier> collection= participant.getParticipantMedicalIdentifierCollection();
		ParticipantMedicalIdentifier medicalId=null; 
		if(collection!=null)
		{
			Iterator<ParticipantMedicalIdentifier> it = collection.iterator();
			while(it.hasNext())
			{
				medicalId=it.next();
				medicalId.setSite(site);
			}
		}
		Logger.out.info("Participant Created having Name="+participant.getLastName()+","+participant.getFirstName());
		return participant;
	}
	
	/**
	 * This method parse the site information into site object
	 * @param pidLine PID line (participant information)
	 * @return Site object
	 * @throws Exception generic exception
	 */
	protected Site parseSiteInformation(String pidLine)throws Exception
	{
		Logger.out.info("Parsing Site Information");
		StringTokenizer st=null;
		String field=null;
		String siteName=null;
		Site siteObj=null;
		String newPidLine = pidLine.replace('|', '~');
		newPidLine = newPidLine.replaceAll("~", "|~~");
		st=new StringTokenizer(pidLine,"|");
		for (int x=0; st.hasMoreTokens(); x++)
		{
			field = st.nextToken();
			if (field.equals("~~"))
			{
				continue;
			}
			else
			{
				field = field.replaceAll("~~", "");
			}	
			// token for participant site informaion
			if (x == CaTIESConstants.PARTICIPANT_SITE_INDEX) // Site info
			{
				if(field!=null && field.length()>0)
				{
					StringTokenizer st2 = new StringTokenizer(field, "^^^");
					if (st2.hasMoreTokens())
					{	
						st2.nextToken();
					}	
					// site in abrrevatted for
					if (st2.hasMoreTokens())
					{
						siteName= st2.nextToken();
						Logger.out.info("Site name found:"+siteName);
					}	
				}
				try
				{
					// find out actual name of site from its abbreviation using site configuration file
					siteName=SiteInfoHandler.getSiteName(siteName);
				}
				catch(Exception ex)
				{
					Logger.out.error("Site name not found in config file: "+siteName);
				}
				
				if(siteName!=null)
				{
					// check for site in DB
					siteObj=(Site)CaCoreAPIService.getObject(new Site(), Constants.NAME, siteName);
					if(siteObj==null)
					{
						Logger.out.error("Site name "+siteName+" not found in the database!");
					}
				}
				break;
			}
		}
		return siteObj;
	}
	
	
	
	/**
	 * This method parses each observations in the report (OBX section) and returns report section.  
	 * @param obxLine observation text of the pathology report
	 * @param textContent text content of the pathology report 
	 * @return report section of the pathology report.
	 * @throws Exception while parsing the report text information 
	 */
	private ReportSection extractOBXSegment(String obxLine, TextContent textContent)throws Exception
	{
		ReportSection section = new ReportSection();
		String newObxLine = obxLine.replace('|', '~');
		newObxLine = newObxLine.replaceAll("~", "|~~");
		StringTokenizer st = new StringTokenizer(newObxLine, "|");
		boolean isFINText = false;

		for (int x = 0; st.hasMoreTokens(); x++)
		{

			String field = st.nextToken();
			if (field.equals("~~"))
			{
				continue;
			}
			else
			{	
				field = field.replaceAll("~~", "");
			}	
			// token for text section
			if ((x == 2) && !(field.equals(CaTIESConstants.TEXT_SECTION)))
			{
				break;
			}
			// token for name of the section
			if (x == CaTIESConstants.NAME_SECTION_INDEX)
			{
				section.setName(field);
				if (field.equals(CaTIESConstants.FINAL_SECTION))
				{
					isFINText = true;
				}
			}
			// token for document fragment
			if (x == CaTIESConstants.DOCUMENT_FRAGMENT_INDEX)
			{
				String text = field;
				text = text.replace('\\', '~');
				text = text.replaceAll("~.br~", "\n");
				section.setDocumentFragment(text);
				if (isFINText)
				{	
					textContent.setData(text);
				}	
			}

		}
		return section;
	}
		
	
   /**
    * Method to extract information from OBR segment
	* @param obrLine report information text
	* @return Identified surgical pathology report from report text
	* @throws Exception while parsing the report text information
	*/
	private IdentifiedSurgicalPathologyReport extractOBRSegment(String obrLine)
	{
		IdentifiedSurgicalPathologyReport report = new IdentifiedSurgicalPathologyReport();
		try
		{
	        String newObrLine = obrLine.replace('|', '~');
	        newObrLine = newObrLine.replaceAll("~", "|~~");
	        //set default values to report
	        report.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
	        report.setIsFlagForReview(new Boolean(false));
	        StringTokenizer st = new StringTokenizer(newObrLine, "|");
	        // iterate over token to create report
	        for (int x = 0; st.hasMoreTokens(); x++)
	        {
	            String field = st.nextToken();
	            if (field.equals("~~"))
	            {
	                continue;
	            }

	            else
	            {
	                field = field.replaceAll("~~", "");
	            } 
	            // token for accession number
	            if (x == CaTIESConstants.REPORT_ACCESSIONNUMBER_INDEX) 
	            {
	                StringTokenizer st2 = new StringTokenizer(field, "^");
	                String accNum = st2.nextToken();
// 		Field removed from SurgicalPathologyReport and Assigned to SCG as SurgicalPathologyNumber
//	                report.setAccessionNumber(accNum);
	            }
	            // report collection date and time
	            if (x == CaTIESConstants.REPORT_DATE_INDEX)
	            {
	                String year = field.substring(0, 4);
	                String month = field.substring(4, 6);
	                String day = field.substring(6, 8);
	                String hours = field.substring(8, 10);
	                String seconds = field.substring(10, 12);

	                GregorianCalendar gc = new GregorianCalendar(Integer
	                        .parseInt(year), Integer.parseInt(month)-1, Integer
	                        .parseInt(day), Integer.parseInt(hours), Integer
	                        .parseInt(seconds));

	                report.setCollectionDateTime(gc.getTime());
	            }
	        }	        
		}
		catch(Exception e)
		{
			Logger.out.error("Error while parsing the report map",e);
		}
		return report;
	}
	
	/**
	 * This method creats a report map using reportText
	 * @param reportText plain text report
	 * @return Map report map
	 */
	private Map<String, Set> getReportMap(String reportText)
	{
		Logger.out.debug("Inside parseString method");
		String[] lines=null;
		StringTokenizer st=null;
		String token = null;
		Map<String, Set> reportMap=null;
		
		lines=reportText.split("\n");
		String line = "";
		reportMap = new HashMap<String, Set>();
		//create reportMap using reportText
		for (int i=0;i<lines.length;i++)
		{
			line=lines[i];
			st = new StringTokenizer(line, "|");
			if (st.hasMoreTokens())
			{
				token = st.nextToken();
				addToReportMap(reportMap,token,line);
			}
		}	
		return reportMap;	
	}
	
	 /**
	    * Method to add report section to report map
	    * @param tempMap temporary map
	    * @param key key of the map
	    * @param value value
	    */
	   private void addToReportMap(Map<String, Set> tempMap,String key,String value)
	   {
		   Set<String> tempSet=null;
		   if(key !=null && value!=null)
		   {
			   if(tempMap.containsKey(key))
			   {
				   tempSet = tempMap.get(key);
				   tempSet.add(value);
			   }
			   else
			   {
				   tempSet = new HashSet<String>();
				   tempSet.add(value);
				   tempMap.put(key, tempSet);
			   }
		   }
	   }
}