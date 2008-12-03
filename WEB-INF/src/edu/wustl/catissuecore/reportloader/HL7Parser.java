package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.lookup.MatchingStatusForSSNPMI;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;



/**
 * @author sandeep_ranade
 * Represents a file parser which parses HL7 Report format.    
 */
public class HL7Parser implements Parser
{
	//private static org.apache.log4j.Logger logger =Logger.getLogger(CatissueCoreServletContextListener.class);
	private static final int pointsForSSNExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_SSN_EXACT));
	private static final int pointsForPMIExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_PMI_EXACT));
	private static final int pointsForDOBExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_DOB_EXACT));
	private static final int pointsForLastNameExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_LAST_NAME_EXACT));
	private static final int pointsForFirstNameExact = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_FIRST_NAME_EXACT));
	private static final int totalPointsFromProperties = pointsForFirstNameExact + pointsForLastNameExact
	+ pointsForDOBExact + pointsForSSNExact +pointsForPMIExact;

	private static final int pointsForThreshold1 = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_THRESHOLD1));
	private static final int pointsForThreshold2 = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_THRESHOLD2));
	private static final int sprCutOff = Integer.parseInt(XMLPropertyHandler.getValue(Constants.SPR_CUT_OFF));
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
	protected static String validateAndSaveReportMap(Map<String, Set> reportMap) 
	{
		Set<Participant> participantList=null;
		List<DefaultLookupResult> defaultLookUPResultList=null;
		String reportText=null;
		SpecimenCollectionGroup scg=null;
		String status=null;
		String siteName=null;
		String participantName=null;
		String surgicalPathologyNumber=HL7ParserUtil.getSurgicalPathologyNumber(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR));
		// parse site information
		Site site=null;
		try
		{
			site = HL7ParserUtil.parseSiteInformation(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.PID));
		}
		catch (Exception ex)
		{
			Logger.out.error("Error while parsing Site information", ex);
		}
		ReportLoaderQueue queue =new ReportLoaderQueue();
		if(site!=null)
		{
			try
			{
				siteName=site.getName();
				if(HL7ParserUtil.validateReportMap(reportMap))
				{
					status=CaTIESConstants.NEW;
					
					// Creating participant object from report text
					Participant participant = HL7ParserUtil.parserParticipantInformation(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.PID), site);
					participantName=participant.getLastName()+","+participant.getFirstName();
					
					Logger.out.info("Checking for Matching SCG");
					//	check for matching scg here
					Logger.out.info("-------------"+site.getName());
					Logger.out.info("-------------"+surgicalPathologyNumber);
					scg=ReportLoaderUtil.getExactMatchingSCG(site, surgicalPathologyNumber);
					
					if((participant.getSocialSecurityNumber() == null || participant.getSocialSecurityNumber().trim().equals(""))&& (participant.getFirstName() == null || participant.getFirstName().trim().equals("")) && (participant.getLastName() == null || participant.getLastName().trim().equals("")) && participant.getBirthDate() == null && participant.getParticipantMedicalIdentifierCollection()== null )
					{
						queue = getQueueForAllFieldsEmpty(scg,participant, status);
					}	
					else
					{
						defaultLookUPResultList=CaCoreAPIService.getParticipantMatchingObects(participant);
						
						if((defaultLookUPResultList!=null)&& defaultLookUPResultList.size()>0)
						{
							queue = getQueueForNotEmptyAmbiguityList(scg,defaultLookUPResultList, participant, status,site);
						}
						else
						{
							Logger.out.info("No matching participant found");
							if(scg==null)
							{
								Logger.out.info("Creating new one no scg with same spr is found--Action1");
								participantList = createPartcipant(participant, status);
								queue.setParticipantCollection(participantList);
								queue.setStatus(CaTIESConstants.NEW);
							}
							else
							{
								Logger.out.info("Conflict resolver scg with same spr is found-Action4");
								Participant scgParticipant=ReportLoaderUtil.getParticipant(scg.getId());
								participantList = new HashSet<Participant>();
								participantList.add(scgParticipant);
								queue.setParticipantCollection(participantList);
								queue.setStatus(CaTIESConstants.STATUS_PARTICIPANT_CONFLICT);
							}
							
						}
					}
				}
				else
				{
					status=CaTIESConstants.INVALID_REPORT_SECTION;
					Logger.out.error("Report section under process is not valid");
					queue.setParticipantCollection(participantList);
					queue.setStatus(status);
				}
			}
			catch(Exception ex)
			{
				Logger.out.error("Error occured while creating participant",ex);
				status=CaTIESConstants.PARTICIPANT_CREATION_ERROR;
				queue.setStatus(status);
			}
		}
		else
		{
			status=CaTIESConstants.SITE_NOT_FOUND;
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		
		
		IdentifiedSurgicalPathologyReport report=IdentifiedReportGenerator.extractOBRSegment(HL7ParserUtil.getReportDataFromReportMap(reportMap, CaTIESConstants.OBR));
		reportText=HL7ParserUtil.getReportText(reportMap);
		
		// Save report to report queue 
		addReportToQueue(queue,reportText,scg,  siteName, participantName, surgicalPathologyNumber, report.getCollectionDateTime());
		return status;
	}

	/**
	 * @param scg
	 * @param defaultLookUPResultList
	 * @param participant
	 * @param status
	 * @param site
	 * @return
	 * @throws Exception
	 */
	private static ReportLoaderQueue getQueueForNotEmptyAmbiguityList(SpecimenCollectionGroup scg,List<DefaultLookupResult> defaultLookUPResultList,Participant participant, String status, Site site) throws Exception 
	{
		ReportLoaderQueue queue = new  ReportLoaderQueue();
		MatchingStatusForSSNPMI isSSNPMI = MatchingStatusForSSNPMI.NOMATCH;
		int oneMatchOtherNullCounter =0;
		boolean isOneMatchOtherNull = false;
		Logger.out.info("defaultLookUPResultListsize ***********"+defaultLookUPResultList.size());
		for(int count=0;count<defaultLookUPResultList.size();count++)
		{
			DefaultLookupResult defaultLookupResult = defaultLookUPResultList.get(count);
			Logger.out.info("*********existing participant weight ="+defaultLookupResult.getWeight());
			MatchingStatusForSSNPMI isSSNPMITemp = defaultLookupResult.getIsSSNPMI();
			if(isSSNPMITemp==MatchingStatusForSSNPMI.EXACT)
			{
				isSSNPMI = MatchingStatusForSSNPMI.EXACT;
				break;
			}
			else if(isSSNPMITemp == MatchingStatusForSSNPMI.NOMATCH)
			{
				isSSNPMI = MatchingStatusForSSNPMI.NOMATCH;
			}
			else if(isSSNPMITemp == MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH) 
			{
				isSSNPMI = MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH;
			}
			else if(isSSNPMITemp == MatchingStatusForSSNPMI.ONEMATCHOTHERNULL) 
			{
				isSSNPMI = MatchingStatusForSSNPMI.ONEMATCHOTHERNULL;
				oneMatchOtherNullCounter=oneMatchOtherNullCounter+1;
				isOneMatchOtherNull = true;
			}
		}
		if((isSSNPMI!=MatchingStatusForSSNPMI.EXACT)&&(isSSNPMI!=MatchingStatusForSSNPMI.NOMATCH))
		{
			if(isOneMatchOtherNull)
			{
				isSSNPMI = MatchingStatusForSSNPMI.ONEMATCHOTHERNULL;
			}
			else
			{
				isSSNPMI = MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH;
			}
		}
		
		queue = getQueueForAllCases(scg, defaultLookUPResultList, participant,
				status, site, isSSNPMI, oneMatchOtherNullCounter);
		return queue;
	}

	/**
	 * @param scg
	 * @param defaultLookUPResultList
	 * @param participant
	 * @param status
	 * @param site
	 * @param isSSNPMI
	 * @param oneMatchOtherNullCounter
	 * @return
	 * @throws Exception
	 */
	private static ReportLoaderQueue getQueueForAllCases
	(
			SpecimenCollectionGroup scg,
			List<DefaultLookupResult> defaultLookUPResultList,
			Participant participant, String status, Site site,
			MatchingStatusForSSNPMI isSSNPMI, int oneMatchOtherNullCounter)
			throws Exception {
		ReportLoaderQueue queue = new  ReportLoaderQueue();
		 if((isSSNPMI == MatchingStatusForSSNPMI.EXACT)||((isSSNPMI == MatchingStatusForSSNPMI.ONEMATCHOTHERNULL)&&(oneMatchOtherNullCounter==1)))
		 {
			queue = getQueueForSSNandPMIExactMatch(scg,defaultLookUPResultList,status,site);
		 }
		
		 else if((isSSNPMI == MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH)||((isSSNPMI == MatchingStatusForSSNPMI.ONEMATCHOTHERNULL)&&(oneMatchOtherNullCounter>1)))
		 {
			 queue = getQueueForSSNPMIPartial(defaultLookUPResultList, status);
		 }
		 else if(isSSNPMI == MatchingStatusForSSNPMI.NOMATCH)
		 {
			double participantWeight = defaultLookUPResultList.get(0).getWeight();
			double totalPoint = totalPointsFromProperties;
			if(defaultLookUPResultList.size()==1 && totalPoint==participantWeight)
			{
				// Method for All fields except scg are exact matched.
				queue = getQueueForAllFieldsExactMatched(scg,defaultLookUPResultList, status,site);
			}
			else
			{
				//All fields doesn't match exactly.
				if(scg== null)
				{
					// scg doesn't match exactly.
					queue = getQueueForNotExactMatched(scg,defaultLookUPResultList,participant,status,site);
				}
				else
				{
					// scg is exact matched.
					queue = getQueueForSCGExactMatch(scg,defaultLookUPResultList,status);
				}
			}
		 }
		 return queue;
	}

	private static ReportLoaderQueue getQueueForSSNandPMIExactMatch(SpecimenCollectionGroup scg,List<DefaultLookupResult> defaultLookUPResultList, String status,Site site) throws Exception {
		Set<Participant> participantList= new HashSet<Participant>();
		ReportLoaderQueue queue = new ReportLoaderQueue();
		Participant existingParticipant = (Participant)defaultLookUPResultList.get(0).getObject();
		boolean flagForPartialSCG =false;
		if(scg==null)
		{
			flagForPartialSCG = ReportLoaderUtil.isPartialMatchingSCG(existingParticipant, site);
			if(flagForPartialSCG)
			{
				Logger.out.info("********SSN and PMI are Exact matched OR one of them is Exact matched and other is null and SCG partial match---Action-4");
				status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
			}
			else
			{
				Logger.out.info("********SSN and PMI are Exact matched OR one of them is Exact matched and other is null and SCG mismatch---Action-2");
				status = CaTIESConstants.NEW;
			}
		}
		else
		{
			Logger.out.info("********SSN and PMI are Exact matched OR one of them is Exact matched and other is null and SCG exact match---Action-3");
			status=CaTIESConstants.NEW;
		}
		Logger.out.info("********Use existing participant --");
		participantList.add(existingParticipant);
		queue.setParticipantCollection(participantList);
		queue.setStatus(status);
		return queue;
		
	}

	/**
	 * 
	 * @param defaultLookUPResultList
	 * @param status
	 * @return
	 * @throws Exception
	 */
	private static ReportLoaderQueue getQueueForSSNPMIPartial(List<DefaultLookupResult> defaultLookUPResultList,String status) throws Exception 
	{
		Logger.out.info("********Between SSN and PMI one matches and other mismatches  OR Between SSN and PMI one matches and other null and more than 1 participants -Action4");
		ReportLoaderQueue queue = new ReportLoaderQueue();
		Set<Participant> participantList = ReportLoaderUtil.getParticipantList(defaultLookUPResultList);
		status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;	
		queue.setParticipantCollection(participantList);
		queue.setStatus(status);
		return queue;
	}

/*	private static MatchingStatus isSSNPMIMethod(List<DefaultLookupResult> defaultLookUPResultList) {
		MatchingStatus isSSNPMI = MatchingStatus.NOMATCH;
		if(defaultLookUPResultList!=null && defaultLookUPResultList.size()>0)
		{
			for(int count=0;count<defaultLookUPResultList.size();count++)
			{
				DefaultLookupResult defaultLookupResult = defaultLookUPResultList.get(count);
				MatchingStatus isSSNPMITemp = defaultLookupResult.getIsSSNPMI();
				if(isSSNPMITemp==MatchingStatus.EXACT)
				{
					isSSNPMI = MatchingStatus.EXACT;
					break;
				}
				else if(isSSNPMITemp == MatchingStatus.PARTIAL)
				{
					isSSNPMI = MatchingStatus.PARTIAL;
				}
				else 
				{
					isSSNPMI = MatchingStatus.NOMATCH;
				}
			}
		}
		return isSSNPMI;
	}
*/
	/**
	 * Gives ReportLoaderQueue based on condition that scg not exact matches and other fields not exact matched.
	 * @param scg
	 * @param defaultLookUPResultList
	 * @param status
	 * @param site
	 * @return ReportLoaderQueue
	 * @throws Exception
	 */
	private static ReportLoaderQueue getQueueForNotExactMatched(
			SpecimenCollectionGroup scg,
			List<DefaultLookupResult> defaultLookUPResultList,Participant participant , String status,Site site) throws Exception 
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		Set<Participant> participantList= new HashSet<Participant>();
		Logger.out.info("Matching Participant found "+defaultLookUPResultList.size());
		Set<Participant> PListBasedOnWt = new HashSet<Participant>();
		
		double lowerWtLimit= sprCutOff;
		double upperWtLimit= pointsForThreshold2;
		boolean lowerWtLimitFlag = false;
		boolean upperWtLimitFlag = true;
		
		 PListBasedOnWt = getPListForGivenWt(defaultLookUPResultList,lowerWtLimit,upperWtLimit,lowerWtLimitFlag,upperWtLimitFlag,site);
		 
		 if(defaultLookUPResultList.size()==PListBasedOnWt.size())
		 {
			 /*	Logger.out.info("********-140<weight<0 and  -Action1");
			 	Logger.out.info("********Creating new participant");
				participantList = createPartcipant(participant, status);
				status=CaTIESConstants.NEW;	*/
			 	Logger.out.info("********-140<weight<0 and  -Action4");
			 	Logger.out.info("********Ambiguity resolver");
			 	participantList = ReportLoaderUtil.getParticipantList(defaultLookUPResultList);
				status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
				queue.setParticipantCollection(participantList);
				queue.setStatus(status);
		}
		 else
		 {
			 queue = getQueueForAllFieldsPositiveWeight(defaultLookUPResultList,status,site);
		 }
		 
		 return queue;
	}

/**
 * @param defaultLookUPResultList
 * @param site
 * @param queue
 * @return
 * @throws Exception
 */
private static ReportLoaderQueue getQueueForAllFieldsPositiveWeight(List<DefaultLookupResult> defaultLookUPResultList,String status, Site site) throws Exception 
{
	Set<Participant> participantList = new HashSet<Participant>();
	ReportLoaderQueue queue = new ReportLoaderQueue();
	boolean flagForPartialSCG = false;;
	double lowerWtLimit;
	double upperWtLimit;
	boolean lowerWtLimitFlag;
	boolean upperWtLimitFlag;
	lowerWtLimit= pointsForThreshold1-1;
	upperWtLimit= 0;
	lowerWtLimitFlag = true;
	upperWtLimitFlag = false;
	Set<Participant> PListBasedOnWt2 = getPListForGivenWt(defaultLookUPResultList,lowerWtLimit,upperWtLimit,lowerWtLimitFlag,upperWtLimitFlag,site);
	
	 if(PListBasedOnWt2!=null && !PListBasedOnWt2.isEmpty())
	 {
		 if(PListBasedOnWt2.size()==1)
		 {
			 //Changes as per New requirement from John Reber
//			 flagForPartialSCG = ReportLoaderUtil.isPartialMatchingSCG(PListBasedOnWt2.iterator().next(), site); 
//			 if(flagForPartialSCG)
//			 {
//				 Logger.out.info("********one Participant weight>59 partially match and SCG no match -Action-4");
//				 //only one participant should be in the list
//				 participantList = ReportLoaderUtil.getParticipantList(defaultLookUPResultList);
//				 status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;						 
//			 }
//			 else
			 {
				 Logger.out.info("********one Participant weight>59 partially match and SCG mis match -Action-2");
				 participantList = PListBasedOnWt2;
				 status=CaTIESConstants.NEW;
			 }
		 }
		 else
		 {
			 Logger.out.info("********Participant weight>59 more than one  partially match and SCG mis or no match");
			 participantList = ReportLoaderUtil.getParticipantList(defaultLookUPResultList);
			 status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;  
		 }
	 }
	 else
	 {
		 Logger.out.info("********Participant 60>weight>0 partially match and SCG mis or no match -Action-4");
		 participantList = ReportLoaderUtil.getParticipantList(defaultLookUPResultList);
		 status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT; 
	 }
	 queue.setParticipantCollection(participantList);
	 queue.setStatus(status);
	 return queue;
}

	/**
	 * Gives ReportLoaderQueue based on condition that scg exact matches and other fields not exact matched.
	 * @param scg
	 * @param defaultLookUPResultList
	 * @param status
	 * @return ReportLoaderQueue
	 * @throws Exception
	 */
	private static ReportLoaderQueue getQueueForSCGExactMatch(
			SpecimenCollectionGroup scg,
			List<DefaultLookupResult> defaultLookUPResultList, String status) throws Exception
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		Participant scgParticipant=ReportLoaderUtil.getParticipant(scg.getId());
		Set<Participant> participantList= new HashSet<Participant>();
		Set<Participant> PListBasedOnWt = new HashSet<Participant>();
		double participantWt =0;
		
		for(DefaultLookupResult defaultLookupResult:defaultLookUPResultList)
		{
			participantWt = defaultLookupResult.getWeight();
			if(participantWt>=pointsForThreshold1)
			{
				PListBasedOnWt.add((Participant)defaultLookupResult.getObject());
			}
		}
		Logger.out.info("********"+scgParticipant.getId()+"  "+scgParticipant.getFirstName());
		
		 if(contains(PListBasedOnWt,scgParticipant)) 
		 {
			 Logger.out.info("********Participant weight>59 partially match and SCG exact match-Action3");
			status=CaTIESConstants.NEW;
		 }
		 else
		 {
			 Logger.out.info("********Participant weight<60 partially match and SCG exact match-Action4");
			status=CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
		 }
		 participantList.clear();
		 participantList.add(scgParticipant);
		 queue.setParticipantCollection(participantList);
		 queue.setStatus(status);
		 return queue;
	}

	/**
	 * Gives ReportLoaderQueue based on condition that all fields other then scg are exact matched.
	 * @param scg
	 * @param defaultLookUPResultList
	 * @param status
	 * @param site
	 * @return ReportLoaderQueue
	 * @throws Exception
	 */
	private static ReportLoaderQueue getQueueForAllFieldsExactMatched(
			SpecimenCollectionGroup scg, List<DefaultLookupResult> defaultLookUPResultList, String status, Site site) throws Exception
	{
		ReportLoaderQueue queue= new ReportLoaderQueue();
		Participant existingParticipant = (Participant)defaultLookUPResultList.get(0).getObject();
		Logger.out.info("Matching participant found. Using existing participant");
		Set<Participant> participantList= new HashSet<Participant>();
		participantList.add(existingParticipant);
		boolean flagForPartialSCG =false;
		if(scg==null)
		{
			flagForPartialSCG = ReportLoaderUtil.isPartialMatchingSCG(existingParticipant, site);
			if(flagForPartialSCG)
			{
				Logger.out.info("********Participant Exact match and SCG partial match");
				status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
			}
			else
			{
				Logger.out.info("********Participant Exact match and SCG Mismatch");
				status = CaTIESConstants.NEW;
			}
		}
		else
		{
			Logger.out.info("********Participant Exact match and SCG exact match");
			status=CaTIESConstants.NEW;
		}
		queue.setParticipantCollection(participantList);
		queue.setStatus(status);
		return queue;
	}

	/**
	 * Gives ReportLoaderQueue based on condition that all fields other then scg are null
	 * @param scg
	 * @param participant
	 * @param status
	 * @return ReportLoaderQueue
	 * @throws Exception
	 */
	private static ReportLoaderQueue getQueueForAllFieldsEmpty(SpecimenCollectionGroup scg,Participant participant,String status) throws Exception
	{
		ReportLoaderQueue queue= new ReportLoaderQueue();
		Set<Participant> participantList=null;

		if(scg==null)
		{
			Logger.out.info("******** UI Participant has null fields and SCG mis or no match");
			participantList = createPartcipant(participant, status);
			status=CaTIESConstants.NEW;
		}
		else
		{
			Logger.out.info("******** UI Participant has null fields and SCG exact match");
			Participant scgParticipant=ReportLoaderUtil.getParticipant(scg.getId());
			participantList = new HashSet<Participant>();
			participantList.add(scgParticipant);
			status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
		}
		queue.setParticipantCollection(participantList);
		queue.setStatus(status);
		return queue;
	}
	
	/**
	 * Gives Set<Participant> having given Participant
	 * @param participant
	 * @param status
	 * @return Set<Participant>
	 */
	private static Set<Participant> createPartcipant(Participant participant, String status)
	{
		Set<Participant> participantList = new HashSet<Participant>();
		try 
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
		catch (Exception e) 
		{
			Logger.out.error("Error while creating participant", e);
			status = CaTIESConstants.PARTICIPANT_CREATION_ERROR;
		}
		return participantList;
	}
	
	/**
	 * Gives Set<Participant> having weight according to condition given .flag tells which limit is considered
	 * @param defaultLookUPResultList
	 * @param lowerWtLimit
	 * @param upperWtLimit
	 * @param lowerWtLimitFlag
	 * @param upperWtLimitFlag
	 * @param site
	 * @return Set<Participant> 
	 * @throws Exception
	 */
	private static Set<Participant> getPListForGivenWt(List<DefaultLookupResult> defaultLookUPResultList,double lowerWtLimit,double upperWtLimit,boolean lowerWtLimitFlag,boolean upperWtLimitFlag, Site site) throws Exception
	{
		double participantWt = 0;
		Set<Participant> PListBasedOnWt = new HashSet<Participant>();
		
		if(defaultLookUPResultList!=null && !defaultLookUPResultList.isEmpty())
		{
			if(lowerWtLimitFlag && !upperWtLimitFlag)
			{
				for(DefaultLookupResult defaultLookupResult:defaultLookUPResultList)
				{
					participantWt = defaultLookupResult.getWeight();
					if(participantWt>lowerWtLimit)
					{
						PListBasedOnWt.add((Participant)defaultLookupResult.getObject());
					}
				}
			}
			else if(!lowerWtLimitFlag && upperWtLimitFlag)
			{
				for(DefaultLookupResult defaultLookupResult:defaultLookUPResultList)
				{
					participantWt = defaultLookupResult.getWeight();
					if(participantWt<upperWtLimit)
					{
						PListBasedOnWt.add((Participant)defaultLookupResult.getObject());
					}
				}
			}
			else if(lowerWtLimitFlag && upperWtLimitFlag)
			{
				for(DefaultLookupResult defaultLookupResult:defaultLookUPResultList)
				{
					participantWt = defaultLookupResult.getWeight();
					if(participantWt>lowerWtLimit && participantWt<upperWtLimit)
					{
						PListBasedOnWt.add((Participant)defaultLookupResult.getObject());
					}
				}
			}
			else
			{
				PListBasedOnWt = new HashSet<Participant>();
			}
		}
		return PListBasedOnWt;
	}

	/**
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it. 
	 * @param set a set of section
	 * @param reportText plain text format report
	 * @param scg object of SpecimenCollectionGroup
	 */
	private static void addReportToQueue(ReportLoaderQueue queue,String reportText, SpecimenCollectionGroup scg, String siteName, String participantName, String surgicalPathologyNumber, Date collectionDate)
	{
		Logger.out.info("Adding report to queue");
		try
		{
			queue.setReportText(reportText);
			// if no any error status is set means it should be set to NEW
			if(queue.getStatus()==null)
			{
				String status=CaTIESConstants.NEW;
				queue.setStatus(status);
			}
			
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
	/**
	 * Checks whether the given Set contains the given element
	 * @param participantList
	 * @param participant
	 * @return boolean:-whether the given Set contains the given element
	 */
	private static boolean contains(Set<Participant> participantList, Participant participant)
	{
		boolean isContains = false;
		Iterator<Participant> itr = participantList.iterator();
		while(itr.hasNext())
		{
			Participant tempP= itr.next();
			Logger.out.info("********"+tempP.getId()+"  "+tempP.getFirstName());
			if((participant.getId().longValue())==(tempP.getId().longValue()))
			{
				isContains = true;
				break;
			}
		}
		Logger.out.info("********"+isContains);
		return isContains;
	}
}