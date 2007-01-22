
package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
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
	
	/**
	 * @param p
	 * @param reportMap
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 */
	public void process(Participant p,Map reportMap)
	{
		ReportSection reportSection = null;
		TextContent textContent = null;
		Participant participant = null;
		Set reportSectionSet = null;
		ReportLoader reportLoader = null;
		IdentifiedSurgicalPathologyReport report = null;
		reportSectionSet = new HashSet();
		Set reportSet=null;
		Iterator reportIterator=null;
		
	try
	{
			String line = "";
			line=this.getParticipantDataFromReportMap(reportMap, Parser.PID);
			//participant = parserParticipantInformation(line);
			this.site=parseSiteInformation(line);
			participant =p;
			line=this.getReportDataFromReportMap(reportMap, Parser.OBR);
			report = extractOBRSegment(line);
			reportSet = this.getReportSectionDataFromReportMap(reportMap, Parser.OBX);
			textContent=new TextContent();
			if(reportSet!=null && reportSet.size()>0)
			{
				reportIterator=reportSet.iterator();
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
			if (participant != null && report != null)
			{
				report.setTextContent(textContent);
				textContent.setSurgicalPathologyReport(report);
				if (reportSectionSet != null && reportSectionSet.size() > 1)
				{
					report.getTextContent().setReportSectionCollection(reportSectionSet);
					
				}
				if(this.site!=null)
				{
					reportLoader = new ReportLoader(participant, report,this.site);
				}
				else
				{
					reportLoader = new ReportLoader(participant, report,createNewSite());
				}
				reportLoader.process();
			}
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while parsing the report map",ex);
		}
	}	
	
	/**
	 * @return new site 
	 * @throws Exception throws exception
	 */
	private Site createNewSite()throws Exception
	{
		if(this.site!=null)
		{
			return this.site;
		}
		NameValueBean nv=null;
		List list=null;
		Site s = new Site();
		s.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		Address address = new Address();
		list =CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_STATE_LIST, null);
		nv =(NameValueBean) list.get(1);
		address.setState(nv.getValue());
		list =CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COUNTRY_LIST, null);
		nv =(NameValueBean) list.get(1);
		address.setCountry(nv.getValue());
		address.setStreet("street");
		address.setCity("city");
		address.setFaxNumber("faxNumber");
		address.setPhoneNumber("phoneNumber");
		address.setZipCode("99999");
		
		s.setAddress(address);
		s.setEmailAddress("admin@admin.com");
		s.setName("DummySite");
		
		list = CDEManager.getCDEManager().getPermissibleValueList(
				edu.wustl.catissuecore.util.global.Constants.CDE_NAME_SITE_TYPE, null);
		nv =(NameValueBean) list.get(1);
		s.setType(nv.getValue());
		return s;
	}
	
	/**
	 * This is a mehod which parses report string from queue and process it. 
	 * @param participant represents participant
	 * @param str report string
	 * @throws Exception
	 */
	public void parseString(Participant participant , String str)throws Exception
	{
		IdentifiedSurgicalPathologyReport report = null;
		String[] lines=null;
		StringTokenizer st=null;
		String token = null;
		Map reportMap=null;
		try
		{
			lines=str.split("\n");
			String line = "";
			reportMap = new HashMap();
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
			process(participant,reportMap);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while reading HL7 file ",ex);
			throw ex;
		}
	}
	
	/**
	 * @param fileName HL7 file name 
	 * @throws Exception
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
		
		try
		{
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null)
			{
					st = new StringTokenizer(line, "|");
					if (st.hasMoreTokens())
					{
						token = st.nextToken();
						if (token.equals(Parser.MSH)|| token.equals(Parser.FTS))
						{
							if (reportMap != null && reportMap.size()>0)
							{
								if(validateReportMap(reportMap))
								{
									try
									{
										Participant participant = parserParticipantInformation(this.getParticipantDataFromReportMap(reportMap, Parser.PID));
										
										participantList=ReportLoaderUtil.checkForParticipant(participant);
										reportText=this.getReportText(reportMap);
										if(participantList!=null)
										{
											addReportToQueue(participantList,reportText);
										}
										else
										{
											this.setSiteToParticipant(participant, site);
											ReportLoaderUtil.saveObject(participant);
											participantList = new HashSet();
											participantList.add(participant);
											addReportToQueue(participantList,reportText);
										}
										
										//process(reportMap);
										reportMap.clear();
										reportMap=null;
									}
									catch(BizLogicException ex)
									{
										Logger.out.error("Error while reading HL7 file: "+ex.getMessage());
									}
									catch(Exception ex)
									{
										Logger.out.error("Report section under process is not valid");
										reportMap=null;
									}
								}
								else
								{
									Logger.out.error("Report section under process is not valid");
									reportMap=null;
								}
							} 
							reportMap = new HashMap();
						}
						if(token.equals(Parser.PID) || token.equals(Parser.OBR) || token.equals(Parser.OBX))
						{	
							addToReportMap(reportMap,token,line);
						}
					}	
			}
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
	 * @param participant
	 * @param site
	 * @throws Exception
	 * Associate site object with respective participant object
	 */
	private void setSiteToParticipant(Participant participant,Site site)throws Exception
	{
		Collection collection= participant.getParticipantMedicalIdentifierCollection();
		ParticipantMedicalIdentifier medicalId=null; 
		if(collection!=null)
		{
			Iterator it = collection.iterator();
			while(it.hasNext())
			{
				medicalId=(ParticipantMedicalIdentifier)it.next();
				medicalId.setSite(site);
			}
		}
	}
	
	/**
	 * @param set
	 * @param reportText
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 */
	public void addReportToQueue(Set set,String reportText)
	{
		try
		{
				ReportLoaderQueue queue= new ReportLoaderQueue(reportText);
				if(set.size()>1)
				{
					queue.setStatus(Parser.PENDING);
				}
				else
				{
					queue.setStatus(Parser.NEW);
				}
			
				queue.setParticipantCollection(set);
				ReportLoaderUtil.saveObject(queue);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while creating queue",ex);
		}
	}	
	
	
   
   /**
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
     * @param pidLine participant information text
     * @return Participant from the participant information text
     * @throws Exception exception while parsing the participant information 
     */
	public Participant parserParticipantInformation(String pidLine)throws Exception
	{
		Participant participant = new Participant();
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

			if (x == Parser.PARTICIPANT_MEDICAL_RECORD_INDEX) // Getting MRN
			{
				StringTokenizer st2 = new StringTokenizer(field, "^^^");
				String mrn = st2.nextToken();
				medicalIdentification = new ParticipantMedicalIdentifier();
				medicalIdentification.setMedicalRecordNumber(mrn);
				//set site
				medicalIdentificationCollection = participant
						.getParticipantMedicalIdentifierCollection();
				if (medicalIdentificationCollection != null
						&& medicalIdentificationCollection.size() > 0)
				{	
					medicalIdentificationCollection.add(medicalIdentification);
				}	
				else
				{
					medicalIdentificationCollection = new HashSet();
					medicalIdentificationCollection.add(medicalIdentification);
					participant
							.setParticipantMedicalIdentifierCollection(medicalIdentificationCollection);
				}
			}
			if (x == Parser.PARTICIPANT_NAME_INDEX)
			{
				StringTokenizer st2 = new StringTokenizer(field, "^");
				String lname = "";
				String fname = "";
				String mname = "";

				if (st2.hasMoreTokens())
				{	
					lname = st2.nextToken();
				}	
				if (st2.hasMoreTokens())
				{
					fname = st2.nextToken();
				}	
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
			if (x == Parser.PARTICIPANT_DATEOFBIRTH_INDEX)
			{
				String year = field.substring(0, 4);
				String month = field.substring(4, 6);
				String day = field.substring(6, 8);

				GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(year), Integer
						.parseInt(month)-1, Integer.parseInt(day));

				participant.setBirthDate(gc.getTime());

			}
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
			if (x == Parser.PARTICIPANT_ETHNICITY_INDEX)
			{
				String ethnicity = field;
				//make it null 
				ethnicity=null;
				participant.setEthnicity(ethnicity);
			}
			if (x == Parser.PARTICIPANT_SSN_INDEX)
			{
				String ssn = field;
				ssn=ReportLoaderUtil.getValidSSN(ssn);
				participant.setSocialSecurityNumber(ssn);
			}
		}
		return participant;
	}
	
	public Site parseSiteInformation(String pidLine)throws Exception
	{
		StringTokenizer st=null;
		String field=null;
		String siteName=null;
		List siteList=null;
		Site site=null;
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
			if (x == Parser.PARTICIPANT_SITE_INDEX) // Site info
			{
				if(field!=null && field.length()>0)
				{
					StringTokenizer st2 = new StringTokenizer(field, "^^^");
					if (st2.hasMoreTokens())
					{	
						st2.nextToken();
					}	
					
					if (st2.hasMoreTokens())
					{
						siteName= st2.nextToken();
					}	
				}
				
				siteName=SiteInfoHandler.getSiteName(siteName);
				if(siteName!=null)
				{
					siteList=ReportLoaderUtil.getObject(Site.class.getName(), "name", siteName);
					if(siteList!=null && siteList.size()>0)
					{
						site=(Site)siteList.get(0);
					}
					else
					{
						Logger.out.error("Site name "+siteName+" not found in the database!");
					}
				}
				break;
			}
		}
		return site;
	}
	
	
	/**
	 * @param obrLine report information text
	 * @return Identified surgical pathology report from report text
	 * @throws Exception while parsing the report text information
	 */
	public IdentifiedSurgicalPathologyReport extractOBRSegment(String obrLine)throws Exception
	{
	        String newObrLine = obrLine.replace('|', '~');
	        newObrLine = newObrLine.replaceAll("~", "|~~");
	        IdentifiedSurgicalPathologyReport report = new IdentifiedSurgicalPathologyReport();
	        report.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
	        report.setIsFlagForReview(new Boolean(false));
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

	            if (x == Parser.REPORT_ACCESSIONNUMBER_INDEX) // Getting MRN
	            {
	                StringTokenizer st2 = new StringTokenizer(field, "^");
	                String accNum = st2.nextToken();

	                report.setAccessionNumber(accNum);
	            }
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
	        return report;
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
			if ((x == 2) && !(field.equals(Parser.TEXT_SECTION)))
			{
				break;
			}

			if (x == Parser.NAME_SECTION_INDEX)
			{
				section.setName(field);
				if (field.equals(Parser.FINAL_SECTION))
				{
					isFINText = true;
				}
			}
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
	 * @param reportMap report map representing map of different pathology reports 
	 * @return boolean represents report is valid or not
	 * @throws Exception
	 */
   public boolean validateReportMap(Map reportMap)throws Exception
   {
	   boolean isValid=false;
	   Set set=null;
	   Iterator it=null;
	   if(reportMap.containsKey(Parser.PID) && reportMap.get(Parser.PID)!=null)
	   {
		   set=(HashSet)reportMap.get(Parser.PID);
		   if(set!=null && set.size()>0)
		   {
			   this.site = parseSiteInformation((String)set.iterator().next());
			   
			   if(this.site!=null)
			   {   
				   if(reportMap.containsKey(Parser.OBR) && reportMap.get(Parser.OBR)!=null)
				   {
					   if(reportMap.containsKey(Parser.OBX) && reportMap.get(Parser.OBX)!=null)
					   {
						   isValid=true;
					   }
				   }
			   }
		   } 
	   }
	   return isValid;
   }
  
	
}
