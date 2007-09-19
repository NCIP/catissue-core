package edu.wustl.catissuecore.reportloader;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.remoting.RemoteAccessException;


import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * Utility class for HL7Parser class
 * @author vijay_pande
 *
 */
public class HL7ParserUtil 
{
	/**
	 * This method validates and save report queue object
	 * @param reportMap report map containing report
	 * @return status String representing status of the execution on method
	 * @throws Exception Generic exception
	 */
	protected static String validateAndSaveReportMap(Map<String, Set> reportMap) throws Exception
	{
		Set<Participant> participantList=null;
		String reportText=null;
		SpecimenCollectionGroup scg=null;
		HL7Parser parser=new HL7Parser();
		String status=null;
		String siteName="";
		String participantName="";
		String surgicalPathologyNumber="";
		// validation before saving
		String line = "";
		line=getReportDataFromReportMap(reportMap, CaTIESConstants.PID);
		// parse site information
		Site site = parser.parseSiteInformation(line);
		if(site!=null)
		{
			siteName=site.getName();
			if(validateReportMap(reportMap))
			{
				status=CaTIESConstants.NEW;
				try
				{
					// Creating participant object from report text
					Participant participant = parserParticipantInformation(getReportDataFromReportMap(reportMap, CaTIESConstants.PID), site);
					participantName=participant.getLastName()+","+participant.getFirstName();
					// check for matching participant
					participantList=ReportLoaderUtil.checkForParticipant(participant);
					if((participantList!=null)&& participantList.size()>0)
					{
						// matching participant found 
						Logger.out.info("Matching Participant found "+participantList.size());
						if(participantList.size()==1)
						{
							// Exactly one matching found, use this participant
							Iterator<Participant> iter=participantList.iterator();
							Participant aParticipant=iter.next();
							
							Logger.out.info("Checking for Matching SCG");
							//	check for matching scg here
							String obrLine=getReportDataFromReportMap(reportMap, CaTIESConstants.OBR);
							scg=ReportLoaderUtil.checkForSpecimenCollectionGroup(aParticipant, site, getSurgicalPathologyNumber(obrLine));
							if(scg!=null)
							{
								if(scg.getSurgicalPathologyNumber()!=null && scg.getSurgicalPathologyNumber().trim().length()!=0)
								{
									if(scg.getIdentifiedSurgicalPathologyReport()!=null)
									{
										Logger.out.info("SCG conflict found with exact match");
										status=CaTIESConstants.STATUS_SCG_CONFLICT;
									}
								}
								else
								{
									Logger.out.info("SCG conflict found with partial match");
									scg=null;
									status=CaTIESConstants.STATUS_SCG_PARTIAL_CONFLICT;
								}
							}
						}
						else if (participantList.size()>1)
						{
							// Multiple matching participant found, this is STATUS_CONFLICT state
							Logger.out.info("Conflict found for Participant ");
							status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
						}
					}
					else
					{
						// No matching participant found Create new participant
						Logger.out.debug("No conflicts found. Creating new Participant ");
						// this.setSiteToParticipant(participant, site);
						Logger.out.debug("Creating new Participant");
						participant=(Participant)CaCoreAPIService.getAppServiceInstance().createObject(participant);
						Logger.out.info("New Participant Created");
						participantList= new HashSet<Participant>();
						participantList.add(participant);
					}
				}
				catch (RemoteAccessException re) 
				{
					status=CaTIESConstants.API_ERROR;
					Logger.out.error("Either JBoss is down or authentication information is invalid",re);
				}
				catch(BizLogicException ex)
				{
					status=CaTIESConstants.DB_ERROR;
					Logger.out.error("Error in database transaction: ",ex);
				}
				catch(Exception ex)
				{
					status=CaTIESConstants.PARTICIPANT_CREATION_ERROR;
					Logger.out.error("Error while either creating participant or matching conflict ",ex);
				}
			}
			else
			{
				status=CaTIESConstants.INVALID_REPORT_SECTION;
				Logger.out.error("Report section under process is not valid");
			}
		}
		else
		{
			status=CaTIESConstants.SITE_NOT_FOUND;
		}
		String obrLine=getReportDataFromReportMap(reportMap, CaTIESConstants.OBR);
		surgicalPathologyNumber=getSurgicalPathologyNumber(obrLine);
		// Save report to report queue 
		reportText=getReportText(reportMap);
		addReportToQueue(participantList,reportText,scg, status, siteName,participantName,surgicalPathologyNumber);
		return status;
	}
	
	
	
	/**
	 * Method to validate report map
	 * @param reportMap report map representing map of different pathology reports 
	 * @return boolean represents report is valid or not
	 * @throws Exception generic exception
	 */
   public static boolean validateReportMap(Map<String, Set> reportMap)throws Exception
   {
	   boolean isValid=false;
	
	   if(reportMap.containsKey(CaTIESConstants.PID) && reportMap.get(CaTIESConstants.PID)!=null)
	   { 
		   // if site is not null then check for section
		   if(reportMap.containsKey(CaTIESConstants.OBR) && reportMap.get(CaTIESConstants.OBR)!=null)
		   {
			   if(reportMap.containsKey(CaTIESConstants.OBX) && reportMap.get(CaTIESConstants.OBX)!=null)
			   {
				   isValid=true;
			   }
		   }
	   }
	   return isValid;
   }
   
   /**
    * Method to retrieve report data from report map
    * @param reportMap report map
    * @param key key of the report map
    * @return raport data information from the report map
    */
   protected static String getReportDataFromReportMap(Map<String, Set> reportMap , String key)
   {
	  Set tempSet = reportMap.get(key);
	  if(tempSet!=null && tempSet.size()>0)
	  {
		  Iterator it=tempSet.iterator();
		  return (String)it.next();
	  }  
	  return null;
   }
   
   /**
    * Method to create report text from reportMap
	* @param reportMap report map representing map of different pathology reports 
	* @return String represents report text
	*/
  private static String getReportText(Map<String, Set> reportMap)
  {
	  StringBuffer reportTxt=new StringBuffer();
	  Collection<Set> collection=null;
	  collection = reportMap.values();
	  Iterator itr=null;
	  Iterator<Set> it = collection.iterator();
	  while(it.hasNext())
	  {
		  itr = it.next().iterator();
		  while(itr.hasNext())
		  {
			  reportTxt.append((String)itr.next());
			  reportTxt.append("\n");
		  } 
	  }
	  return reportTxt.toString();
  }
  
   /**
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 * @param set a set of section
	 * @param reportText plain text format report
	 * @param scg object of SpecimenCollectionGroup
	 */
	private static void addReportToQueue(Set<Participant> set,String reportText, SpecimenCollectionGroup scg, String status, String siteName, String participantName, String surgicalPathologyNumber)
	{
		Logger.out.info("Adding report to queue");
		try
		{
			ReportLoaderQueue queue= new ReportLoaderQueue(reportText);
			// if no any error status is set means it should be set to NEW
			if(status==null)
			{
				status=CaTIESConstants.NEW;
			}
			queue.setStatus(status);
			queue.setParticipantCollection(set);
			queue.setSpecimenCollectionGroup(scg);
			queue.setSiteName(siteName);
			queue.setParticipantName(participantName);
			queue.setSurgicalPathologyNumber(surgicalPathologyNumber);
			queue.setReportLoadedDate(new Date());
			queue=(ReportLoaderQueue)CaCoreAPIService.getAppServiceInstance().createObject(queue);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while creating queue",ex);
		}
	}
	
	  /**
	 * @param obrLine report information text
	 * @return String for Surgical Pathology Number
	 * @throws Exception while parsing the report text information
	 */
	public static String getSurgicalPathologyNumber(String obrLine)
	{
		try
		{
	        String newObrLine = obrLine.replace('|', '~');
	        newObrLine = newObrLine.replaceAll("~", "|~~");
	        
	        StringTokenizer st = new StringTokenizer(newObrLine, "|");

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
	            //	Accession number is now called as Surgical Pathology Number
	            if (x == CaTIESConstants.REPORT_ACCESSIONNUMBER_INDEX) 
	            {
	                StringTokenizer st2 = new StringTokenizer(field, "^");
	                String accNum = st2.nextToken();

	                return accNum;
	            }           
	        }
		}
		catch(Exception e)
		{
			Logger.out.error("Error while parsing the report map",e);
		}
		return null;
	}
	
	
	   
    /**
     * Method to create participant object using HL7 format report section for participant (PID)
     * @param pidLine participant information text
     * @return Participant from the participant information text
     * @throws Exception exception while parsing the participant information 
     */
	public static Participant parserParticipantInformation(String pidLine,Site site)throws Exception
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
	
	
	
}
