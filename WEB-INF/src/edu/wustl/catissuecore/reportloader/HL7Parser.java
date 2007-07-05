
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Represents a file parser which parses HL7 Report format.    
 */
public class HL7Parser extends Parser
{

	/**
	 * source information for pathology report 
	 */
	private Site site=null;
	private String status=null;
	
	/**
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 * @param p Object of participant
	 * @param reportMap a report map containing sections to be processed
	 * @param scg object of SpecimenCollectionGroup
	 * @throws Exception generic exception
	 */
	public void process(Participant participant,Map reportMap, SpecimenCollectionGroup scg) throws Exception
	{
		Logger.out.info("Procesing report");
		ReportSection reportSection = null;
		TextContent textContent = null;
		Set reportSectionSet = null;
		ReportLoader reportLoader = null;
		IdentifiedSurgicalPathologyReport report = null;
		reportSectionSet = new HashSet();
		Set reportSet=null;
		Iterator reportIterator=null;
		String spn=null;
		
		try
		{
			String line = "";
			line=this.getParticipantDataFromReportMap(reportMap, Parser.PID);
			//participant = parserParticipantInformation(line);
			this.site=parseSiteInformation(line);
			line=this.getReportDataFromReportMap(reportMap, Parser.OBR);
			Logger.out.debug("Creating report");
			report = extractOBRSegment(line);
			Logger.out.debug("Report Created Successfully");
			spn=ReportLoaderUtil.getSurgicalPathologyNumber(this.getReportDataFromReportMap(reportMap, Parser.OBR));
			Logger.out.debug("Creating report section from report text");
			reportSet = this.getReportSectionDataFromReportMap(reportMap, Parser.OBX);
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
			if (participant != null && report != null)
			{
				// set textContent to report
				report.setTextContent(textContent);
				textContent.setSurgicalPathologyReport(report);
				if (reportSectionSet != null && reportSectionSet.size() > 1)
				{
					report.getTextContent().setReportSectionCollection(reportSectionSet);
					
				}
				if(this.site!=null)
				{
					// create instance of ReportLoader to load report into the DB
					Logger.out.debug("Instantiating report loader to load report");
					reportLoader = new ReportLoader(participant, report,this.site, scg, spn);
				}
				else
				{
//					Update report queue with appropriate status
//					reportLoader = new ReportLoader(participant, report,createNewSite(), scg);
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
		Logger.out.info("Inside parseString method");
		String[] lines=null;
		StringTokenizer st=null;
		String token = null;
		Map reportMap=null;
		try
		{
			lines=reportText.split("\n");
			String line = "";
			reportMap = new HashMap();
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
			// function call to process the reportMap 
			process(participant,reportMap, scg);
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
		IdentifiedSurgicalPathologyReport report = null;
		StringTokenizer st = null;
		String token = null;
		Map reportMap=null;
		Set participantList=null;
		String reportText=null;
		FileReader fr=null;
		BufferedReader br=null;
		int counter=0;
		SpecimenCollectionGroup scg=null;
		
		try
		{
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line = "";
			counter=0;
			Logger.out.info("Parsing File "+fileName+": Started at "+new Date().toString());
			// loop while !EOF
			while ((line = br.readLine()) != null)
			{
				st = new StringTokenizer(line, "|");
				if (st.hasMoreTokens())
				{
					token = st.nextToken();
					// MSH or FTS are the starting point of individual report
					if (token.equals(Parser.MSH)|| token.equals(Parser.FTS))
					{
						// save parsed report
						if (reportMap != null && reportMap.size()>0)
						{
							// validation before saving
							if(validateReportMap(reportMap))
							{
								try
								{
									// Creating participant object from report text
									Participant participant = parserParticipantInformation(this.getParticipantDataFromReportMap(reportMap, Parser.PID));
									// check for matching participant
									participantList=ReportLoaderUtil.checkForParticipant(participant);
									if((participantList!=null) && (participantList.size() > 0))
									{
										// matching participant found 
										Logger.out.info("Matching Participant found "+participantList.size());
										if(participantList.size()==1)
										{
											// Exactly one matching found, use this participant
											Iterator iter=participantList.iterator();
											Participant aParticipant=(Participant)iter.next();
											List scgList=ReportLoaderUtil.getSCGList(aParticipant);
											if(scgList!=null && scgList.size()>0)
											{
												Logger.out.info("Checking for Matching SCG");
//												check for matching scg here
												String pidLine=this.getParticipantDataFromReportMap(reportMap, Parser.PID);
												String obrLine=this.getReportDataFromReportMap(reportMap, Parser.OBR);
												scg=ReportLoaderUtil.checkForSpecimenCollectionGroup(aParticipant, parseSiteInformation(pidLine), ReportLoaderUtil.getSurgicalPathologyNumber(obrLine));
												if(scg!=null && scg.getSurgicalPathologyNumber().equalsIgnoreCase(null))
												{
													this.status=Parser.CONFLICT;
												}
											}
										}
										else
										{
											// Multiple matching participant found, this is CONFLICT state
											Logger.out.info("Conflict found for Participant "+ counter);
											this.status=Parser.CONFLICT;
										}
									}
									else
									{
										// No matching participant found Create new participant
										Logger.out.info("No conflicts found. Creating new Participant "+counter);
										// this.setSiteToParticipant(participant, site);
										Logger.out.info("Creating new Participant");
										ReportLoaderUtil.saveObject(participant);
										Logger.out.info("New Participant Created");
										participantList = new HashSet();
										participantList.add(participant);
									}
								}
								catch(BizLogicException ex)
								{
									this.status=Parser.DB_ERROR;
									Logger.out.error("Error in database transaction: "+ex.getMessage());
								}
								catch(Exception ex)
								{
									this.status=Parser.INVALID_REPORT_SECTION;
									Logger.out.error("Report section under process is not valid");
								}
							}
							else
							{
								Logger.out.error("Report section under process is not valid");
							}
							// Save report to report queue 
							reportText=this.getReportText(reportMap);
							addReportToQueue(participantList,reportText,scg);
							counter++;
							CSVLogger.info(Parser.LOGGER_FILE_POLLER,new Date().toString()+","+fileName+","+counter+","+this.status);
							Logger.out.info("Report is added to queue. Current Count is="+counter);
							// reinitialize variables to null to process next report
							scg=null;
							reportText=null;
							reportMap.clear();
							reportMap=null;
						} 
						// if start of report found then initialize report map and set queue status to NEW
						reportMap = new HashMap();
						this.status=Parser.NEW;
					}
					// add PID, OBR, OBX section from report text to report map
					if(token.equals(Parser.PID) || token.equals(Parser.OBR) || token.equals(Parser.OBX))
					{	
						addToReportMap(reportMap,token,line);
					}
				}	
			}
			Logger.out.info("Parsing File "+fileName+": Finished at "+new Date().toString());
			CSVLogger.info(Parser.LOGGER_FILE_POLLER,"Total "+counter+" reports are added to report queue from file "+fileName);
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
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 * @param set a set of section
	 * @param reportText plain text format report
	 * @param scg object of SpecimenCollectionGroup
	 */
	public void addReportToQueue(Set set,String reportText, SpecimenCollectionGroup scg)
	{
		Logger.out.info("Adding report to queue");
		try
		{
			ReportLoaderQueue queue= new ReportLoaderQueue(reportText);
			// if no any error status is set means it should be set to NEW
			if(this.status==null)
			{
				this.status=Parser.NEW;
			}
			queue.setStatus(this.status);
			queue.setParticipantCollection(set);
			queue.setSpecimenCollectionGroup(scg);
			ReportLoaderUtil.saveObject(queue);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while creating queue",ex);
		}
	}	
	 
   /**
    * Method to create report text from reportMap
	* @param reportMap report map representing map of different pathology reports 
	* @return String represents report text
	*/
  private String getReportText(Map reportMap)
  {
	  StringBuffer reportTxt=new StringBuffer();
	  Collection collection=null;
	  collection = reportMap.values();
	  Iterator itr=null;
	  Iterator it = collection.iterator();
	  while(it.hasNext())
	  {
		  itr = ((Set)it.next()).iterator();
		  while(itr.hasNext())
		  {
			  reportTxt.append((String)itr.next());
			  reportTxt.append("\n");
		  } 
	  }
	  return reportTxt.toString();
  }
  
   /**
    * Method to add report section to report map
    * @param tempMap temporary map
    * @param key key of the map
    * @param value value
    */
   private void addToReportMap(Map tempMap,String key,String value)
   {
	   Set tempSet=null;
	   if(key !=null && value!=null)
	   {
		   if(tempMap.containsKey(key))
		   {
			   tempSet = (Set)tempMap.get(key);
			   tempSet.add(value);
		   }
		   else
		   {
			   tempSet = new HashSet();
			   tempSet.add(value);
			   tempMap.put(key, tempSet);
		   }
	   }
   }

   /**
    * Method to retrieve participant information from report map
    * @param reportMap report map
    * @param key key of the report map
    * @return participant information from the report map 
    */
   private String getParticipantDataFromReportMap(Map reportMap , String key)
   {
	  Set tempSet = (Set)reportMap.get(key);
	  if(tempSet!=null && tempSet.size()>0)
	  {
		  Iterator it=tempSet.iterator();
		  return (String)it.next();
	  }  
	  return null;
   }
	
   /**
    * Method to retrieve report data from report map
    * @param reportMap report map
    * @param key key of the report map
    * @return raport data information from the report map
    */
   private String getReportDataFromReportMap(Map reportMap , String key)
   {
	  Set tempSet = (Set)reportMap.get(key);
	  if(tempSet!=null && tempSet.size()>0)
	  {
		  Iterator it=tempSet.iterator();
		  return (String)it.next();
	  }  
	  return null;
   }
	
   /**
    * Method to retrieve specific report section from reportMap
    * @param reportMap report map
    * @param key key of the report map
    * @return collection of report section data from the report map
    */
   private Set getReportSectionDataFromReportMap(Map reportMap , String key)
   {
	  Set tempSet = (Set)reportMap.get(key);
	  return tempSet;
   }
   
    /**
     * Method to create participant object using HL7 format report section for participant (PID)
     * @param pidLine participant information text
     * @return Participant from the participant information text
     * @throws Exception exception while parsing the participant information 
     */
	public static Participant parserParticipantInformation(String pidLine)throws Exception
	{
		Logger.out.info("Parsing participant information");
		Participant participant = new Participant();
		String pidLineforSite=pidLine;
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		ParticipantMedicalIdentifier medicalIdentification = null;
		Collection medicalIdentificationCollection = null;
		String field = null;
		String newPidLine = pidLine.replace('|', '~');
		newPidLine = newPidLine.replaceAll("~", "|~~");
		StringTokenizer st = new StringTokenizer(newPidLine, "|");
		Validator validator = new Validator();

		for (int x = 0; st.hasMoreTokens(); x++)
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
			// Token for Participant medical record number
			if (x == Parser.PARTICIPANT_MEDICAL_RECORD_INDEX) // Getting MRN
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
					medicalIdentificationCollection = new HashSet();
					medicalIdentificationCollection.add(medicalIdentification);
					participant
							.setParticipantMedicalIdentifierCollection(medicalIdentificationCollection);
				}
			}
			// token for participant name
			if (x == Parser.PARTICIPANT_NAME_INDEX)
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
			if (x == Parser.PARTICIPANT_DATEOFBIRTH_INDEX)
			{
				String year = field.substring(0, 4);
				String month = field.substring(4, 6);
				String day = field.substring(6, 8);

				GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(year), Integer
						.parseInt(month)-1, Integer.parseInt(day));

				participant.setBirthDate(gc.getTime());

			}
			// token for participant gender
			if (x == Parser.PARTICIPANT_GENDER_INDEX)
			{
				if (field.equalsIgnoreCase(Parser.MALE))
				{
					participant.setGender("Male");
				}	
				else if (field.equalsIgnoreCase(Parser.FEMALE))
				{
					participant.setGender("Female");
				}	
			}
			// token for participant ethnicity
			if (x == Parser.PARTICIPANT_ETHNICITY_INDEX)
			{
				String ethnicity = field;
				//make it null 
				ethnicity=null;
				participant.setEthnicity(ethnicity);
			}
			// token for participant Social Security Number
			if (x == Parser.PARTICIPANT_SSN_INDEX)
			{
				String ssn = field;
				ssn=ReportLoaderUtil.getValidSSN(ssn);
				participant.setSocialSecurityNumber(ssn);
			}
		}
		//code of setSitetoParticipant function to avoid sepearte function call
		Collection collection= participant.getParticipantMedicalIdentifierCollection();
		ParticipantMedicalIdentifier medicalId=null; 
		Site site=parseSiteInformation(pidLineforSite);
		if(collection!=null)
		{
			Iterator it = collection.iterator();
			while(it.hasNext())
			{
				medicalId=(ParticipantMedicalIdentifier)it.next();
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
	public static Site parseSiteInformation(String pidLine)throws Exception
	{
		Logger.out.info("Parsing Site Information");
		StringTokenizer st=null;
		String field=null;
		String siteName=null;
		List siteList=null;
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
			if (x == Parser.PARTICIPANT_SITE_INDEX) // Site info
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
					siteList=ReportLoaderUtil.getObject(Site.class.getName(), "name", siteName);
					if(siteList!=null && siteList.size()>0)
					{
						siteObj=(Site)siteList.get(0);
					}
					else
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
	public ReportSection extractOBXSegment(String obxLine, TextContent textContent)throws Exception
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
			if ((x == 2) && !(field.equals(Parser.TEXT_SECTION)))
			{
				break;
			}
			// token for name of the section
			if (x == Parser.NAME_SECTION_INDEX)
			{
				section.setName(field);
				if (field.equals(Parser.FINAL_SECTION))
				{
					isFINText = true;
				}
			}
			// token for document fragment
			if (x == Parser.DOCUMENT_FRAGMENT_INDEX)
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
	 * Method to validate report map
	 * @param reportMap report map representing map of different pathology reports 
	 * @return boolean represents report is valid or not
	 * @throws Exception generic exception
	 */
   public boolean validateReportMap(Map reportMap)throws Exception
   {
	   boolean isValid=false;
	   Set set=null;
	   Iterator it=null;
	   if(reportMap.containsKey(Parser.PID) && reportMap.get(Parser.PID)!=null)
	   {
		   String line = "";
		   line=this.getParticipantDataFromReportMap(reportMap, Parser.PID);
		   // parse site information
		   this.site = parseSiteInformation(line);
		   if(this.site!=null)
		   {   
			   // if site is not null then check for section
			   if(reportMap.containsKey(Parser.OBR) && reportMap.get(Parser.OBR)!=null)
			   {
				   if(reportMap.containsKey(Parser.OBX) && reportMap.get(Parser.OBX)!=null)
				   {
					   this.status=Parser.NEW;
					   isValid=true;
				   }
			   }
			   else
			   {
				   // if any one section is not found then invalid report
				   this.status=Parser.INVALID_REPORT_SECTION;
			   }
		   }
		   else
		   {
			   // if site not found 
			   this.status=Parser.SITE_NOT_FOUND;
		   } 
	   }
	   Logger.out.info("Validate report map returning "+isValid+" for report");
	   return isValid;
   }
   /**
    * Method to extract information from OBR segment
	* @param obrLine report information text
	* @return Identified surgical pathology report from report text
	* @throws Exception while parsing the report text information
	*/
	public static IdentifiedSurgicalPathologyReport extractOBRSegment(String obrLine)
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
	            if (x == Parser.REPORT_ACCESSIONNUMBER_INDEX) 
	            {
	                StringTokenizer st2 = new StringTokenizer(field, "^");
	                String accNum = st2.nextToken();
// 		Field removed from SurgicalPathologyReport and Assigned to SCG as SurgicalPathologyNumber
//	                report.setAccessionNumber(accNum);
	            }
	            // report collection date and time
	            if (x == Parser.REPORT_DATE_INDEX)
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
}