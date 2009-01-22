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
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(HL7Parser.class);

	/**
	 * Points for exact SSN.
	 */
	private static final int POINTS_FOR_SSN_EXACT = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_SSN_EXACT));

	/**
	 * Points for exact PMI.
	 */
	private static final int POINTS_FOR_PMI_EXACT = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_PMI_EXACT));

	/**
	 * Points for exact Date Of Birth.
	 */
	private static final int POINTS_FOR_DOB_EXACT = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_DOB_EXACT));

	/**
	 * Points for exact Last Name.
	 */
	private static final int POINTS_FOR_LASTNAME_EXACT = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_LAST_NAME_EXACT));

	/**
	 * Points for exact First Name.
	 */
	private static final int POINTS_FOR_FIRSTNAME_EXACT = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_FIRST_NAME_EXACT));

	/**
	 * Total Points.
	 */
	private static final int TOTAL_POINTS_FROM_PROPERTIES = POINTS_FOR_FIRSTNAME_EXACT +
	POINTS_FOR_LASTNAME_EXACT + POINTS_FOR_DOB_EXACT + POINTS_FOR_SSN_EXACT
			+ POINTS_FOR_PMI_EXACT;

	/**
	 * Points of Threshold 1.
	 */
	private static final int POINTS_FOR_THRESHOLD1 = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_THRESHOLD1));

	/**
	 * Points of Threshold 2.
	 */
	private static final int POINTS_FOR_THRESHOLD2 = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_THRESHOLD2));

	/**
	 * Cut Off points.
	 */
	private static final int SPR_CUT_OFF = Integer.parseInt(
			XMLPropertyHandler.getValue(Constants.SPR_CUT_OFF));

	/**
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections
	 * from it.
	 * @param participant Object of participant
	 * @param reportMap a report map containing sections to be processed
	 * @param scg object of SpecimenCollectionGroup
	 * @param abbrToHeader hashMap of String, String
	 */
	private void process(Participant participant, Map<String, Set> reportMap,
			SpecimenCollectionGroup scg, HashMap<String, String> abbrToHeader)
	{
		logger.debug("Procesing report");
		ReportLoader reportLoader = null;
		IdentifiedSurgicalPathologyReport report = null;
		String spn = null;
		Site site = null;
		try
		{
			//participant = parserParticipantInformation(line);
			site = HL7ParserUtil.parseSiteInformation(HL7ParserUtil.
					getReportDataFromReportMap(reportMap, CaTIESConstants.PID));
			spn = HL7ParserUtil.getSurgicalPathologyNumber(HL7ParserUtil.
					getReportDataFromReportMap(reportMap, CaTIESConstants.OBR));
			report = IdentifiedReportGenerator.getIdentifiedReport(reportMap, abbrToHeader);
			if (report != null)
			{
				if (site == null)
				{
					//Update report queue with appropriate status
					//reportLoader=new ReportLoader(participant, report,createNewSite(), scg);
					throw new Exception("Site not found!");
				}
				else
				{
					//create instance of ReportLoader to load report into the DB
					logger.debug("Instantiating report loader to load report");
					reportLoader = new ReportLoader(participant, report, site, scg, spn);
				}
				// initiate reportLoader to load report
				reportLoader.process();
			}
		}
		catch (Exception ex)
		{
			logger.error("Error while parsing the report map", ex);
		}
	}

	/**
	 * This is a mehod which parses report string from queue and process it.
	 * @param participant represents participant
	 * @param reportText report string
	 * @param scg object of SpecimenCollectionGroup
	 * @param abbrToHeader hashMap of String, String
	 */
	public void parseString(Participant participant, String reportText,
			SpecimenCollectionGroup scg, HashMap<String, String> abbrToHeader)
	{
		logger.debug("Inside parseString method");
		Map<String, Set> reportMap = null;
		try
		{
			reportMap = HL7ParserUtil.getReportMap(reportText);
			if (participant == null)
			{
				Long startTime = new Date().getTime();
				String status = validateAndSaveReportMap(reportMap);
				Long endTime = new Date().getTime();
				CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER, new Date().
						toString() + ",,," + status + "," + (endTime - startTime));
			}
			else
			{
				process(participant, reportMap, scg, abbrToHeader);
			}
		}
		catch (Exception ex)
		{
			logger.error("Error while creating report map ", ex);
		}
	}

	/**
	 * This method parses the report.
	 * @param fileName HL7 file name
	 */
	public void parse(String fileName)
	{
		//IdentifiedSurgicalPathologyReport report = null;
		StringTokenizer strTokenizer = null;
		String token = null;
		Map<String, Set> reportMap = null;
		String status = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		int counter = 0;
		Long startTime = null;
		Long endTime = null;
		Long fileParseStartTime = null;
		Long fileParseEndTime = null;

		try
		{
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			String line = "";
			counter = 0;
			logger.info("Parsing File " + fileName + ": Started at " + new Date().toString());
			fileParseStartTime = new Date().getTime();
			// loop while !EOF
			while ((line = bufferedReader.readLine()) != null)
			{
				strTokenizer = new StringTokenizer(line, "|");
				if (strTokenizer.hasMoreTokens())
				{
					token = strTokenizer.nextToken();
					// MSH or FTS are the starting point of individual report
					if (token.equals(CaTIESConstants.PID) ||
							token.equals(CaTIESConstants.OBR) ||
								token.equals(CaTIESConstants.OBX))
					{
						HL7ParserUtil.addToReportMap(reportMap, token, line);
					}
					else if (token.equals(CaTIESConstants.MSH) ||
							token.equals(CaTIESConstants.FTS))
					{
						// save parsed report
						if (reportMap != null && !reportMap.isEmpty())
						{
							status = validateAndSaveReportMap(reportMap);
							endTime = new Date().getTime();
							counter++;
							CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER,
								new Date().toString() + "," + fileName + "," +
								counter + "," + status + ",," +
								(endTime - startTime));

							logger.info("Report is added to queue. Current Count is="
									+ counter);
							// reinitialize variables to null to process next report
							reportMap.clear();
							reportMap = null;
						}
						//if start of report found then initialize report map and set
						//queue status to NEW
						startTime = new Date().getTime();
						reportMap = new HashMap<String, Set>();
					}
					// add PID, OBR, OBX section from report text to report map
				}
			}
			fileParseEndTime = new Date().getTime();
			logger.info("Parsing File " + fileName + ": Finished at " + new Date().toString());
			CSVLogger.info(CaTIESConstants.LOGGER_FILE_POLLER, "Total " + counter +
				" reports are added to report queue from file " + fileName + " Time " +
				"reuired for parsing:" + (fileParseEndTime - fileParseStartTime));
		}
		catch (Exception ex)
		{
			logger.error("Error while reading HL7 file ", ex);
		}
		finally
		{
			try
			{
				logger.info("Final Count of the reports that are added to queue is=" + counter);
				// close handle to file
				bufferedReader.close();
				fileReader.close();
			}
			catch (IOException ex)
			{
				logger.error("Error while closing file handle ", ex);
			}
			catch (Exception ex)
			{
				logger.error("Error while closing file handle ", ex);
			}
		}
	}

	/**
	 * This method validates and save report queue object.
	 * @param reportMap report map containing report
	 * @return status String representing status of the execution on method
	 */
	protected static String validateAndSaveReportMap(Map<String, Set> reportMap)
	{
		Set<Participant> participantList = null;
		List<DefaultLookupResult> defaultLookUPResultList = null;
		String reportText = null;
		SpecimenCollectionGroup scg = null;
		String status = null;
		String siteName = null;
		String participantName = null;
		String surgicalPathologyNumber = HL7ParserUtil.getSurgicalPathologyNumber(
				HL7ParserUtil.getReportDataFromReportMap(reportMap,
				CaTIESConstants.OBR));
		// parse site information
		Site site = null;
		try
		{
			site = HL7ParserUtil.parseSiteInformation(HL7ParserUtil.
					getReportDataFromReportMap(reportMap, CaTIESConstants.PID));
		}
		catch (Exception ex)
		{
			logger.error("Error while parsing Site information", ex);
		}
		ReportLoaderQueue queue = new ReportLoaderQueue();
		if (site == null)
		{
			status = CaTIESConstants.SITE_NOT_FOUND;
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		else
		{
			try
			{
				siteName = site.getName();
				if (HL7ParserUtil.validateReportMap(reportMap))
				{
					status = CaTIESConstants.NEW;

					// Creating participant object from report text
					Participant participant = HL7ParserUtil.parserParticipantInformation(
							HL7ParserUtil.getReportDataFromReportMap(reportMap,
							CaTIESConstants.PID), site);
					participantName = participant.getLastName() + "," +
						participant.getFirstName();

					logger.info("Checking for Matching SCG");
					//	check for matching scg here
					logger.info("-------------" + site.getName());
					logger.info("-------------" + surgicalPathologyNumber);
					scg = ReportLoaderUtil.getExactMatchingSCG(site, surgicalPathologyNumber);

					if ((participant.getSocialSecurityNumber() == null ||
							participant.getSocialSecurityNumber().trim().equals(""))
						&& (participant.getFirstName() == null ||
								participant.getFirstName().trim().equals(""))
						&& (participant.getLastName() == null ||
								participant.getLastName().trim().equals(""))
						&& participant.getBirthDate() == null
						&& participant.getParticipantMedicalIdentifierCollection() == null)
					{
						queue = getQueueForAllFieldsEmpty(scg, participant, status);
					}
					else
					{
						defaultLookUPResultList =
							CaCoreAPIService.getParticipantMatchingObects(participant);

						if ((defaultLookUPResultList != null) &&
								!defaultLookUPResultList.isEmpty())
						{
							queue = getQueueForNotEmptyAmbiguityList(scg,
									defaultLookUPResultList, participant,
									status, site);
						}
						else
						{
							logger.info("No matching participant found");
							if (scg == null)
							{
								logger.info("Creating new one no scg with same spr is found--Action1");
								participantList = createPartcipant(participant, status);
								queue.setParticipantCollection(participantList);
								queue.setStatus(CaTIESConstants.NEW);
							}
							else
							{
								logger.info("Conflict resolver scg with same spr is found-Action4");
								Participant scgParticipant = ReportLoaderUtil.getParticipant(scg.getId());
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
					status = CaTIESConstants.INVALID_REPORT_SECTION;
					logger.error("Report section under process is not valid");
					queue.setParticipantCollection(participantList);
					queue.setStatus(status);
				}
			}
			catch (Exception ex)
			{
				logger.error("Error occured while creating participant", ex);
				status = CaTIESConstants.PARTICIPANT_CREATION_ERROR;
				queue.setStatus(status);
			}
		}

		IdentifiedSurgicalPathologyReport report = IdentifiedReportGenerator.
			extractOBRSegment(HL7ParserUtil.getReportDataFromReportMap(reportMap,
				CaTIESConstants.OBR));
		reportText = HL7ParserUtil.getReportText(reportMap);

		// Save report to report queue
		addReportToQueue(queue, reportText, scg, siteName, participantName,
				surgicalPathologyNumber, report.getCollectionDateTime());
		return status;
	}

	/**
	 * @param scg SpecimenCollectionGroup.
	 * @param defaultLookUPResultList List of DefaultLookupResult.
	 * @param participant Participant.
	 * @param status String.
	 * @param site Site.
	 * @return ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForNotEmptyAmbiguityList(SpecimenCollectionGroup
			scg, List<DefaultLookupResult> defaultLookUPResultList, Participant
			participant, String status, Site site)
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			MatchingStatusForSSNPMI isSSNPMI = MatchingStatusForSSNPMI.NOMATCH;
			int oneMatchOtherNullCounter = 0;
			boolean isOneMatchOtherNull = false;
			logger.info("defaultLookUPResultListsize ***********" + defaultLookUPResultList.size());
			for (int count = 0; count < defaultLookUPResultList.size(); count++)
			{
				DefaultLookupResult defaultLookupResult = defaultLookUPResultList.get(count);
				logger.info("*********existing participant weight =" +
						defaultLookupResult.getWeight());
				MatchingStatusForSSNPMI isSSNPMITemp = defaultLookupResult.getIsSSNPMI();
				if (isSSNPMITemp == MatchingStatusForSSNPMI.EXACT)
				{
					isSSNPMI = MatchingStatusForSSNPMI.EXACT;
					break;
				}
				else if (isSSNPMITemp == MatchingStatusForSSNPMI.NOMATCH)
				{
					isSSNPMI = MatchingStatusForSSNPMI.NOMATCH;
				}
				else if (isSSNPMITemp == MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH)
				{
					isSSNPMI = MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH;
				}
				else if (isSSNPMITemp == MatchingStatusForSSNPMI.ONEMATCHOTHERNULL)
				{
					isSSNPMI = MatchingStatusForSSNPMI.ONEMATCHOTHERNULL;
					oneMatchOtherNullCounter = oneMatchOtherNullCounter + 1;
					isOneMatchOtherNull = true;
				}
			}
			if ((isSSNPMI != MatchingStatusForSSNPMI.EXACT) &&
					(isSSNPMI != MatchingStatusForSSNPMI.NOMATCH))
			{
				if (isOneMatchOtherNull)
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
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * @param scg SpecimenCollectionGroup
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param participant Participant
	 * @param status String
	 * @param site Site
	 * @param isSSNPMI MatchingStatusForSSNPMI
	 * @param oneMatchOtherNullCounter int
	 * @return ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForAllCases(SpecimenCollectionGroup scg,
			List<DefaultLookupResult> defaultLookUPResultList, Participant
			participant, String status, Site site, MatchingStatusForSSNPMI
			isSSNPMI, int oneMatchOtherNullCounter)
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{	if ((isSSNPMI == MatchingStatusForSSNPMI.EXACT)
				|| ((isSSNPMI == MatchingStatusForSSNPMI.ONEMATCHOTHERNULL) &&
						(oneMatchOtherNullCounter == 1)))
			{
				queue = getQueueForSSNandPMIExactMatch(scg, defaultLookUPResultList, status, site);
			}

			else if ((isSSNPMI == MatchingStatusForSSNPMI.ONEMATCHOTHERMISMATCH)
				|| ((isSSNPMI == MatchingStatusForSSNPMI.ONEMATCHOTHERNULL) &&
						(oneMatchOtherNullCounter > 1)))
			{
				queue = getQueueForSSNPMIPartial(defaultLookUPResultList, status);
			}
			else if (isSSNPMI == MatchingStatusForSSNPMI.NOMATCH)
			{
				double participantWeight = defaultLookUPResultList.get(0).getWeight();
				double totalPoint = TOTAL_POINTS_FROM_PROPERTIES;
				if (defaultLookUPResultList.size() == 1 && totalPoint == participantWeight)
				{
					// Method for All fields except scg are exact matched.
					queue = getQueueForAllFieldsExactMatched(scg,
							defaultLookUPResultList, status, site);
				}
				else
				{
					//All fields doesn't match exactly.
					if (scg == null)
					{
						// scg doesn't match exactly.
						queue = getQueueForNotExactMatched(scg,
								defaultLookUPResultList, participant,
								status, site);
					}
					else
					{
						// scg is exact matched.
						queue = getQueueForSCGExactMatch(scg,
								defaultLookUPResultList, status);
					}
				}
			}
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * Get Queue For SSN and PMI Exact Match.
	 * @param scg SpecimenCollectionGroup
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param status String
	 * @param site Site
	 * @return ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForSSNandPMIExactMatch(SpecimenCollectionGroup
			scg, List<DefaultLookupResult> defaultLookUPResultList,	String status,
			Site site)
	{
		Set<Participant> participantList = new HashSet<Participant>();
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			Participant existingParticipant = (Participant) defaultLookUPResultList.get(0).getObject();
			boolean flagForPartialSCG = false;
			if (scg == null)
			{
				flagForPartialSCG = ReportLoaderUtil.isPartialMatchingSCG(
						existingParticipant, site);
				if (flagForPartialSCG)
				{
					logger.info("********SSN and PMI are Exact matched OR one of them is Exact"
						+ "matched and other is null and SCG partial match---Action-4");
					status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
				}
				else
				{
					logger.info("********SSN and PMI are Exact matched OR one of them is Exact"
						+ "matched and other is null and SCG mismatch---Action-2");
					status = CaTIESConstants.NEW;
				}
			}
			else
			{
				logger.info("********SSN and PMI are Exact matched OR one of them is"
				+ "Exact matched and other is null and SCG exact match---Action-3");
				status = CaTIESConstants.NEW;
			}
			logger.info("********Use existing participant --");
			participantList.add(existingParticipant);
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * Get Queue For SSN PMI Partial.
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param status String
	 * @return ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForSSNPMIPartial(List<DefaultLookupResult>
		defaultLookUPResultList, String status)
	{
		logger.info("********Between SSN and PMI one matches and other mismatches OR " +
		"Between SSN and PMI one matches and other null and more than 1 participants -Action4");

		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			Set<Participant> participantList = ReportLoaderUtil.getParticipantList(
					defaultLookUPResultList);
			status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/*	private static MatchingStatus isSSNPMIMethod(List<DefaultLookupResult> defaultLookUPResultList) {
			MatchingStatus isSSNPMI = MatchingStatus.NOMATCH;
			if(defaultLookUPResultList!=null && defaultLookUPResultList.size()>0)
			{
				for(int count=0;count<defaultLookUPResultList.size();count++)
				{
					DefaultLookupResult defaultLookupResult=defaultLookUPResultList.get(count);
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
	 * Gives ReportLoaderQueue based on condition that scg not exact matches and
	 * other fields not exact matched.
	 * @param scg SpecimenCollectionGroup.
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param participant Participant
	 * @param status String
	 * @param site Site
	 * @return ReportLoaderQueue ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForNotExactMatched(SpecimenCollectionGroup
			scg, List<DefaultLookupResult> defaultLookUPResultList, Participant
			participant, String status, Site site)
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			Set<Participant> participantList = new HashSet<Participant>();
			logger.info("Matching Participant found " + defaultLookUPResultList.size());
			Set<Participant> PListBasedOnWt = new HashSet<Participant>();

			double lowerWtLimit = SPR_CUT_OFF;
			double upperWtLimit = POINTS_FOR_THRESHOLD2;
			boolean lowerWtLimitFlag = false;
			boolean upperWtLimitFlag = true;

			PListBasedOnWt = getPListForGivenWt(defaultLookUPResultList, lowerWtLimit,
					upperWtLimit, lowerWtLimitFlag, upperWtLimitFlag, site);

			if (defaultLookUPResultList.size() == PListBasedOnWt.size())
			{
				/*	Logger.out.info("********-140<weight<0 and  -Action1");
					Logger.out.info("********Creating new participant");
				participantList = createPartcipant(participant, status);
				status=CaTIESConstants.NEW;	*/
				logger.info("********-140<weight<0 and  -Action4");
				logger.info("********Ambiguity resolver");
				participantList = ReportLoaderUtil.getParticipantList(defaultLookUPResultList);
				status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
				queue.setParticipantCollection(participantList);
				queue.setStatus(status);
			}
			else
			{
				queue = getQueueForAllFieldsPositiveWeight(defaultLookUPResultList, status, site);
			}
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param status String
	 * @param site Site
	 * @return ReportLoaderQueue ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForAllFieldsPositiveWeight(
			List<DefaultLookupResult> defaultLookUPResultList, String status,
			Site site)
	{
		Set<Participant> participantList = new HashSet<Participant>();
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			double lowerWtLimit;
			double upperWtLimit;
			boolean lowerWtLimitFlag;
			boolean upperWtLimitFlag;
			lowerWtLimit = POINTS_FOR_THRESHOLD1 - 1;
			upperWtLimit = 0;
			lowerWtLimitFlag = true;
			upperWtLimitFlag = false;
			Set<Participant> PListBasedOnWt2 = getPListForGivenWt(
					defaultLookUPResultList, lowerWtLimit, upperWtLimit,
					lowerWtLimitFlag, upperWtLimitFlag, site);

			if (PListBasedOnWt2 != null && !PListBasedOnWt2.isEmpty())
			{
				if (PListBasedOnWt2.size() == 1)
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
		//			{
					logger.info("********one Participant weight>59 partially match" +
							" and SCG mis match -Action-2");
					participantList = PListBasedOnWt2;
					status = CaTIESConstants.NEW;
		//			}
				}
				else
				{
					logger.info("********Participant weight>59 more than one " +
							"partially match and SCG mis or no match");
					participantList = ReportLoaderUtil.getParticipantList(
							defaultLookUPResultList);
					status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
				}
			}
			else
			{
				logger.info("********Participant 60>weight>0 partially match and SCG" +
						" mis or no match -Action-4");
				participantList = ReportLoaderUtil.getParticipantList(defaultLookUPResultList);
				status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
			}
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * Gives ReportLoaderQueue based on condition that scg exact matches and other fields not exact matched.
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param status String
	 * @param scg SpecimenCollectionGroup
	 * @return ReportLoaderQueue ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForSCGExactMatch(SpecimenCollectionGroup
			scg, List<DefaultLookupResult> defaultLookUPResultList,
			String status)
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			Participant scgParticipant = ReportLoaderUtil.getParticipant(scg.getId());
			Set<Participant> participantList = new HashSet<Participant>();
			Set<Participant> PListBasedOnWt = new HashSet<Participant>();
			double participantWt = 0;

			for (DefaultLookupResult defaultLookupResult : defaultLookUPResultList)
			{
				participantWt = defaultLookupResult.getWeight();
				if (participantWt >= POINTS_FOR_THRESHOLD1)
				{
					PListBasedOnWt.add((Participant) defaultLookupResult.getObject());
				}
			}
			logger.info("********" + scgParticipant.getId() + "  " + scgParticipant.getFirstName());

			if (contains(PListBasedOnWt, scgParticipant))
			{
				logger.info("********Participant weight>59 partially match and SCG " +
						"exact match-Action3");
				status = CaTIESConstants.NEW;
			}
			else
			{
				logger.info("********Participant weight<60 partially match and SCG " +
						"exact match-Action4");
				status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
			}
			participantList.clear();
			participantList.add(scgParticipant);
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * Gives ReportLoaderQueue based on condition that all fields other then scg are exact matched.
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param status String
	 * @param site Site
	 * @param scg SpecimenCollectionGroup
	 * @return ReportLoaderQueue ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForAllFieldsExactMatched(
			SpecimenCollectionGroup scg, List<DefaultLookupResult>
			defaultLookUPResultList, String status, Site site)
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			Participant existingParticipant = (Participant) defaultLookUPResultList.get(0).getObject();
			logger.info("Matching participant found. Using existing participant");
			Set<Participant> participantList = new HashSet<Participant>();
			participantList.add(existingParticipant);
			boolean flagForPartialSCG = false;
			if (scg == null)
			{
				flagForPartialSCG = ReportLoaderUtil.isPartialMatchingSCG(
						existingParticipant, site);
				if (flagForPartialSCG)
				{
					logger.info("********Participant Exact match and SCG partial match");
					status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
				}
				else
				{
					logger.info("********Participant Exact match and SCG Mismatch");
					status = CaTIESConstants.NEW;
				}
			}
			else
			{
				logger.info("********Participant Exact match and SCG exact match");
				status = CaTIESConstants.NEW;
			}
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * Gives ReportLoaderQueue based on condition that all fields other then scg are null.
	 * @param status String
	 * @param participant Participant
	 * @param scg SpecimenCollectionGroup
	 * @return ReportLoaderQueue ReportLoaderQueue
	 */
	private static ReportLoaderQueue getQueueForAllFieldsEmpty(
			SpecimenCollectionGroup scg, Participant participant, String status)
	{
		ReportLoaderQueue queue = new ReportLoaderQueue();
		try
		{
			Set<Participant> participantList = null;
			if (scg == null)
			{
				logger.info("******** UI Participant has null fields and SCG mis or no match");
				participantList = createPartcipant(participant, status);
				status = CaTIESConstants.NEW;
			}
			else
			{
				logger.info("******** UI Participant has null fields and SCG exact match");
				Participant scgParticipant = ReportLoaderUtil.getParticipant(scg.getId());
				participantList = new HashSet<Participant>();
				participantList.add(scgParticipant);
				status = CaTIESConstants.STATUS_PARTICIPANT_CONFLICT;
			}
			queue.setParticipantCollection(participantList);
			queue.setStatus(status);
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return queue;
	}

	/**
	 * Gives Set of Participant having given Participant.
	 * @param participant Participant
	 * @param status String
	 * @return Set of Participant
	 */
	private static Set<Participant> createPartcipant(Participant participant, String status)
	{
		Set<Participant> participantList = new HashSet<Participant>();
		try
		{
			// No matching participant found Create new participant
			logger.debug("No conflicts found. Creating new Participant ");
			// this.setSiteToParticipant(participant, site);
			logger.debug("Creating new Participant");
			participant = (Participant) CaCoreAPIService.createObject(participant);
			logger.info("New Participant Created");
			participantList = new HashSet<Participant>();
			participantList.add(participant);
		}
		catch (Exception e)
		{
			logger.error("Error while creating participant", e);
			status = CaTIESConstants.PARTICIPANT_CREATION_ERROR;
		}
		return participantList;
	}

	/**
	 * Gives Set of Participants having weight according to condition given flag
	 * tells which limit is considered.
	 * @param defaultLookUPResultList List of DefaultLookupResult
	 * @param lowerWtLimit double
	 * @param upperWtLimit double
	 * @param lowerWtLimitFlag boolean
	 * @param upperWtLimitFlag boolean
	 * @param site Site
	 * @return Set of Participant
	 */
	private static Set<Participant> getPListForGivenWt(List<DefaultLookupResult>
		defaultLookUPResultList, double lowerWtLimit, double upperWtLimit,
			boolean lowerWtLimitFlag, boolean upperWtLimitFlag, Site site)
	{
		double participantWt = 0;
		Set<Participant> PListBasedOnWt = new HashSet<Participant>();
		try
		{
			if (defaultLookUPResultList != null && !defaultLookUPResultList.isEmpty())
			{
				if (lowerWtLimitFlag && !upperWtLimitFlag)
				{
					for (DefaultLookupResult defaultLookupResult : defaultLookUPResultList)
					{
						participantWt = defaultLookupResult.getWeight();
						if (participantWt > lowerWtLimit)
						{
							PListBasedOnWt.add((Participant) defaultLookupResult.
									getObject());
						}
					}
				}
				else if (!lowerWtLimitFlag && upperWtLimitFlag)
				{
					for (DefaultLookupResult defaultLookupResult : defaultLookUPResultList)
					{
						participantWt = defaultLookupResult.getWeight();
						if (participantWt < upperWtLimit)
						{
							PListBasedOnWt.add((Participant) defaultLookupResult.
									getObject());
						}
					}
				}
				else if (lowerWtLimitFlag && upperWtLimitFlag)
				{
					for (DefaultLookupResult defaultLookupResult : defaultLookUPResultList)
					{
						participantWt = defaultLookupResult.getWeight();
						if (participantWt > lowerWtLimit && participantWt < upperWtLimit)
						{
							PListBasedOnWt.add((Participant) defaultLookupResult.
									getObject());
						}
					}
				}
				else
				{
					PListBasedOnWt = new HashSet<Participant>();
				}
			}
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return PListBasedOnWt;
	}

	/**
	 * This method processes the map structure of a report in a HL7 file.
	 * It gets different sections in the map and creates different report sections from it.
	 * @param reportText plain text format report
	 * @param scg object of SpecimenCollectionGroup
	 * @param queue ReportLoaderQueue
	 * @param siteName String
	 * @param participantName String
	 * @param surgicalPathologyNumber String
	 * @param collectionDate Date
	 */
	private static void addReportToQueue(ReportLoaderQueue queue, String reportText,
			SpecimenCollectionGroup scg, String siteName, String participantName,
			String surgicalPathologyNumber, Date collectionDate)
	{
		logger.info("Adding report to queue");
		try
		{
			queue.setReportText(reportText);
			// if no any error status is set means it should be set to NEW
			if (queue.getStatus() == null)
			{
				String status = CaTIESConstants.NEW;
				queue.setStatus(status);
			}
			queue.setSpecimenCollectionGroup(scg);
			queue.setSiteName(siteName);
			queue.setParticipantName(participantName);
			queue.setSurgicalPathologyNumber(surgicalPathologyNumber);
			queue.setReportCollectionDate(collectionDate);
			queue.setReportLoadedDate(new Date());
			queue = (ReportLoaderQueue) CaCoreAPIService.createObject(queue);
		}
		catch (Exception ex)
		{
			logger.error("Error while creating queue", ex);
		}
	}

	/**
	 * Checks whether the given Set contains the given element.
	 * @param participantList Set of Participant
	 * @param participant Participant
	 * @return boolean :-whether the given Set contains the given element
	 */
	private static boolean contains(Set<Participant> participantList, Participant participant)
	{
		boolean isContains = false;
		Iterator<Participant> itr = participantList.iterator();
		while (itr.hasNext())
		{
			Participant tempP = itr.next();
			logger.info("********" + tempP.getId() + "  " + tempP.getFirstName());
			if ((participant.getId().longValue()) == (tempP.getId().longValue()))
			{
				isContains = true;
				break;
			}
		}
		logger.info("********" + isContains);
		return isContains;
	}
}