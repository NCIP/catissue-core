package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
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
	private void process(Participant participant,Map<String, Set> reportMap, SpecimenCollectionGroup scg, HashMap<String,String> abbrToHeader) throws Exception
	{
		Logger.out.debug("Procesing report");
		ReportLoader reportLoader = null;
		IdentifiedSurgicalPathologyReport report = null;
		String spn=null;
		Site site=null;
		
		try
		{
			String line = "";
			line=HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.PID);
			//participant = parserParticipantInformation(line);
			site=parseSiteInformation(line);
			line=HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR);
			spn=HL7ParserUtil.getSurgicalPathologyNumber(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR));
			report=IdentifiedReportGenerator.getIdentifiedReport(reportMap, abbrToHeader);
			if (report != null)
			{
				
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
	public void parseString(Participant participant , String reportText, SpecimenCollectionGroup scg, HashMap<String,String> abbrToHeader)throws Exception
	{
		Logger.out.debug("Inside parseString method");
		Map<String, Set> reportMap=null;
		try
		{
			reportMap=this.getReportMap(reportText);
			if(participant!=null)
			{
				process(participant,reportMap, scg, abbrToHeader);
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
					siteObj=(Site)CaCoreAPIService.getObject(Site.class, Constants.NAME, siteName);
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