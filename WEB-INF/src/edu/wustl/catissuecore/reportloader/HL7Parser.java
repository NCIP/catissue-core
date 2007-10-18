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

import org.springframework.remoting.RemoteAccessException;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.common.exception.BizLogicException;
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
			//participant = parserParticipantInformation(line);
			site=HL7ParserUtil.parseSiteInformation(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.PID));
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
			reportMap=HL7ParserUtil.getReportMap(reportText);
			if(participant!=null)
			{
				process(participant,reportMap, scg, abbrToHeader);
			}
			else
			{
				Long startTime=new Date().getTime();
				String status=validateAndSaveReportMap(reportMap);
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
						HL7ParserUtil.addToReportMap(reportMap,token,line);
					}
					else if (token.equals(CaTIESConstants.MSH)|| token.equals(CaTIESConstants.FTS))
					{
						// save parsed report
						if (reportMap != null && reportMap.size()>0)
						{
							status=validateAndSaveReportMap(reportMap);
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
		String status=null;
		String siteName=null;
		String participantName=null;
		String surgicalPathologyNumber=HL7ParserUtil.getSurgicalPathologyNumber(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR));
		// parse site information
		Site site = HL7ParserUtil.parseSiteInformation(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.PID));
		if(site!=null)
		{
			siteName=site.getName();
			if(HL7ParserUtil.validateReportMap(reportMap))
			{
				status=CaTIESConstants.NEW;
				try
				{
					// Creating participant object from report text
					Participant participant = HL7ParserUtil.parserParticipantInformation(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.PID), site);
					participantName=participant.getLastName()+","+participant.getFirstName();
					
					Logger.out.info("Checking for Matching SCG");
					//	check for matching scg here
					scg=ReportLoaderUtil.getExactMatchingSCG(site, surgicalPathologyNumber);
					
					if(scg!=null) 
					{
						Logger.out.info("SCG found with exact match");
						participantList= new HashSet<Participant>();
						participantList.add(ReportLoaderUtil.getParticipant(scg.getId()));
						if(scg.getIdentifiedSurgicalPathologyReport()!=null)
						{
							Logger.out.info("SCG conflict found with exact match");
							status=CaTIESConstants.STATUS_SCG_CONFLICT;
						}
					}
					else
					{
						// check for matching participant
						participantList=ReportLoaderUtil.checkForParticipant(participant);
						if((participantList!=null)&& participantList.size()>0)
						{
							// matching participant found 
							Logger.out.info("Matching Participant found "+participantList.size());
							status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
						}
						else
						{
							// No matching participant found Create new participant
							Logger.out.debug("No conflicts found. Creating new Participant ");
							// this.setSiteToParticipant(participant, site);
							Logger.out.debug("Creating new Participant");
							participant=(Participant)CaCoreAPIService.createObject(participant);
							Logger.out.info("New Participant Created");
							participantList= new HashSet<Participant>();
							participantList.add(participant);
						}
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
		
		IdentifiedSurgicalPathologyReport report=IdentifiedReportGenerator.extractOBRSegment(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR));
		reportText=HL7ParserUtil.getReportText(reportMap);
		// Save report to report queue 
		addReportToQueue(participantList,reportText,scg, status, siteName, participantName, surgicalPathologyNumber, report.getCollectionDateTime());
		return status;
	}
	
	/**
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 * @param set a set of section
	 * @param reportText plain text format report
	 * @param scg object of SpecimenCollectionGroup
	 */
	private static void addReportToQueue(Set<Participant> set,String reportText, SpecimenCollectionGroup scg, String status, String siteName, String participantName, String surgicalPathologyNumber, Date collectionDate)
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
			queue.setReportCollectionDate(collectionDate);
			queue.setReportLoadedDate(new Date());
			queue=(ReportLoaderQueue)CaCoreAPIService.createObject(queue);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while creating queue",ex);
		}
	}
}