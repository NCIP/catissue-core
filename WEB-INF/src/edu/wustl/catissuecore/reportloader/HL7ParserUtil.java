package edu.wustl.catissuecore.reportloader;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.SiteInfoHandler;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;

/**
 * AppUtility class for HL7Parser class.
 * @author vijay_pande
 *
 */

public class HL7ParserUtil
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(HL7ParserUtil.class);

	/**
	 * Method to validate report map.
	 * @param reportMap report map representing map of different pathology reports
	 * @return boolean represents report is valid or not
	 */
	public static boolean validateReportMap(Map<String, Set> reportMap)
	{
		boolean isValid = false;
		try
		{
			if (reportMap.containsKey(CaTIESConstants.PID) &&
					reportMap.get(CaTIESConstants.PID) != null)
			{
				// if site is not null then check for section
				if (reportMap.containsKey(CaTIESConstants.OBR) &&
						reportMap.get(CaTIESConstants.OBR) != null)
				{
					if (reportMap.containsKey(CaTIESConstants.OBX) &&
							reportMap.get(CaTIESConstants.OBX) != null)
					{
						String surgicalPathologyNumber = HL7ParserUtil.
							getSurgicalPathologyNumber(HL7ParserUtil.
								getReportDataFromReportMap(reportMap,
										CaTIESConstants.OBR));

						if (!(surgicalPathologyNumber == null ||
								surgicalPathologyNumber.equals("")))
						{
							isValid = true;
						}
					}
				}
			}
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return isValid;
	}

	/**
	* Method to create report text from reportMap.
	* @param reportMap report map representing map of different pathology reports
	* @return String represents report text
	*/
	public static String getReportText(Map<String, Set> reportMap)
	{
		StringBuffer reportTxt = new StringBuffer();
		Collection<Set> collection = null;
		collection = reportMap.values();
		Iterator itr = null;
		Iterator<Set> iterator = collection.iterator();
		while (iterator.hasNext())
		{
			itr = iterator.next().iterator();
			while (itr.hasNext())
			{
				reportTxt.append((String) itr.next());
				reportTxt.append("\n");
			}
		}
		return reportTxt.toString();
	}

	/**
	* @param obrLine report information text
	* @return String for Surgical Pathology Number
	*/
	public static String getSurgicalPathologyNumber(String obrLine)
	{
		try
		{
			String newObrLine = obrLine.replace('|', '~');
			newObrLine = newObrLine.replaceAll("~", "|~~");

			StringTokenizer strTokenizer = new StringTokenizer(newObrLine, "|");

			for (int x = 0; strTokenizer.hasMoreTokens(); x++)
			{
				String field = strTokenizer.nextToken();
				if ("~~".equals(field))
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
		catch (Exception e)
		{
			logger.error("Error while parsing the report map", e);
		}
		return null;
	}

	/**
	 * Method to create participant object using HL7 format report section for participant (PID).
	 * @param pidLine participant information text
	 * @param site Site
	 * @return Participant from the participant information text
	 */
	public static Participant parserParticipantInformation(String pidLine, Site site)
	{
		logger.info("Parsing participant information");
		Participant participant = new Participant();
		try
		{
			participant.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			ParticipantMedicalIdentifier medicalIdentification = null;
			Collection<ParticipantMedicalIdentifier> medicalIdentificationCollection = null;
			String field = null;
			String newPidLine = pidLine.replace('|', '~');
			newPidLine = newPidLine.replaceAll("~", "|~~");
			StringTokenizer strTokenizer = new StringTokenizer(newPidLine, "|");
			for (int x = 0; strTokenizer.hasMoreTokens(); x++)
			{
				// 	CAN NOT USE STRINGBUFFER FOR FIELD
				field = strTokenizer.nextToken();
				if ("~~".equals(field))
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
					medicalIdentificationCollection =
						participant.getParticipantMedicalIdentifierCollection();
					if (medicalIdentificationCollection != null &&
							medicalIdentificationCollection.size() > 0)
					{
						// add MRI to set
						medicalIdentificationCollection.add(medicalIdentification);
					}
					else
					{
						// initialization of set
						medicalIdentificationCollection =
							new HashSet<ParticipantMedicalIdentifier>();
						medicalIdentificationCollection.add(medicalIdentification);
						participant.setParticipantMedicalIdentifierCollection(
								medicalIdentificationCollection);
					}
				}
				// token for participant name
				if (x == CaTIESConstants.PARTICIPANT_NAME_INDEX)
				{
					StringTokenizer st2 = new StringTokenizer(field, "^");
					String mname = null;
					// Last name
					if (st2.hasMoreTokens())
					{
						participant.setLastName(st2.nextToken());
					}
					// first name
					if (st2.hasMoreTokens())
					{
						participant.setFirstName(st2.nextToken());
					}
					// middle name
					if (st2.hasMoreTokens())
					{
						mname = st2.nextToken();
					}
					if (mname != null && mname.trim().length() > 0)
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

					GregorianCalendar gregorianCal = new GregorianCalendar(
							Integer.parseInt(year), Integer.parseInt(month) - 1,
							Integer.parseInt(day));

					participant.setBirthDate(gregorianCal.getTime());
				}
				// token for participant gender
				if (x == CaTIESConstants.PARTICIPANT_GENDER_INDEX)
				{
					if (field.equalsIgnoreCase(CaTIESConstants.MALE))
					{
						participant.setGender(CaTIESConstants.MALE_GENDER);
					}
					else if (field.equalsIgnoreCase(CaTIESConstants.FEMALE))
					{
						participant.setGender(CaTIESConstants.FEMALE_GENDER);
					}
				}
				// token for participant ethnicity
				if (x == CaTIESConstants.PARTICIPANT_ETHNICITY_INDEX)
				{
					// no matching ethinicity found according to CDE value,
					//hence set it to null
					participant.setEthnicity(null);
				}
				// token for participant Social Security Number
				if (x == CaTIESConstants.PARTICIPANT_SSN_INDEX)
				{
					participant.setSocialSecurityNumber(ReportLoaderUtil.getValidSSN(field));
				}
			}
			//code of setSitetoParticipant function to avoid sepearte function call
			Collection<ParticipantMedicalIdentifier> collection =
				participant.getParticipantMedicalIdentifierCollection();
			ParticipantMedicalIdentifier medicalId = null;
			if (collection != null)
			{
				Iterator<ParticipantMedicalIdentifier> iterator = collection.iterator();
				while (iterator.hasNext())
				{
					medicalId = iterator.next();
					medicalId.setSite(site);
				}
			}
			logger.info("Participant Object Created ");
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return participant;
	}

	/**
	 * This method creats a report map using reportText.
	 * @param reportText plain text report
	 * @return Map report map
	 */
	public static Map<String, Set> getReportMap(String reportText)
	{
		logger.debug("Inside parseString method");
		String[] lines = null;
		StringTokenizer strTokenizer = null;
		String token = null;
		Map<String, Set> reportMap = null;

		lines = reportText.split("\n");
		String line = "";
		reportMap = new HashMap<String, Set>();
		//create reportMap using reportText
		for (int i = 0; i < lines.length; i++)
		{
			line = lines[i];
			strTokenizer = new StringTokenizer(line, "|");
			if (strTokenizer.hasMoreTokens())
			{
				token = strTokenizer.nextToken();
				addToReportMap(reportMap, token, line);
			}
		}
		return reportMap;
	}

	/**
	* Method to add report section to report map.
	* @param tempMap temporary map
	* @param key key of the map
	* @param value value
	*/
	public static void addToReportMap(Map<String, Set> tempMap, String key, String value)
	{
		Set<String> tempSet = null;
		if (key != null && value != null)
		{
			if (tempMap.containsKey(key))
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

	/**
	 * This method parse the site information into site object.
	 * @param pidLine PID line (participant information)
	 * @return Site object
	 */
	protected static Site parseSiteInformation(String pidLine)
	{
		logger.info("Parsing Site Information");
		StringTokenizer strTokenizer = null;
		String field = null;
		String siteName = null;
		Site siteObj = null;
		try
		{
			String newPidLine = pidLine.replace('|', '~');
			newPidLine = newPidLine.replaceAll("~", "|~~");
			strTokenizer = new StringTokenizer(pidLine, "|");
			for (int x = 0; strTokenizer.hasMoreTokens(); x++)
			{
				field = strTokenizer.nextToken();
				if ("~~".equals(field))
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
					if (field != null && field.length() > 0)
					{
						StringTokenizer st2 = new StringTokenizer(field, "^^^");
						if (st2.hasMoreTokens())
						{
							st2.nextToken();
						}
						// site in abrrevatted for
						if (st2.hasMoreTokens())
						{
							siteName = st2.nextToken();
							logger.info("Site name found:" + siteName);
						}
					}
					try
					{
						// find out actual name of site from its abbreviation using
						//site configuration file
						siteName = SiteInfoHandler.getSiteName(siteName);
					}
					catch (Exception ex)
					{
						logger.error("Site name not found in config file: " + siteName);
					}
					if (siteName != null)
					{
						// check for site in DB
						siteObj = (Site) CaCoreAPIService.getObject(Site.class,
								Constants.NAME, siteName);
						if (siteObj == null)
						{
							logger.error("Site name " + siteName + " not found in" +
									" the database!");
						}
					}
					break;
				}
			}
		}
		catch (Exception excp)
		{
			logger.error(excp);
		}
		return siteObj;
	}

	/**
	* Method to retrieve report data from report map.
	* @param reportMap report map
	* @param key key of the report map
	* @return raport data information from the report map
	*/
	protected static String getReportDataFromReportMap(Map<String, Set> reportMap, String key)
	{
		String returnValue = null;
		Set tempSet = reportMap.get(key);
		if (tempSet != null && tempSet.size() > 0)
		{
			Iterator iterator = tempSet.iterator();
			returnValue = (String) iterator.next();
		}
		return returnValue;
	}
}