
package edu.wustl.catissuecore.reportloader;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * AppUtility class containing all general purpose methods.
 */
public final class ReportLoaderUtil
{

	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getCommonLogger(ReportLoaderUtil.class);

	/**
	 * private constructor.
	 */
	private ReportLoaderUtil()
	{

	}

	/**
	 * This method gets Participant List.
	 * @param defaultLookUPResultList default Look UP ResultList.
	 * @return number of matching participants
	 * @throws Exception throws exception
	 */
	public static Set<Participant> getParticipantList(List defaultLookUPResultList)
			throws Exception
	{
		Set<Participant> result = null;
		// check for matching participant list
		if (defaultLookUPResultList != null && defaultLookUPResultList.size() > 0)
		{
			result = new HashSet<Participant>();
			// prepare list of participant object out of DefaultLookupResult List
			for (int i = 0; i < defaultLookUPResultList.size(); i++)
			{
				final DefaultLookupResult participantResult = (DefaultLookupResult) defaultLookUPResultList
						.get(i);
				result.add((Participant) participantResult.getObject());
			}
		}
		return result;
	}

	/**
	 * This method creates new directory.
	 * @param dirStr directory name
	 * @throws IOException IOException occured while creating directories
	 */
	public static void createDir(String dirStr) throws IOException
	{
		final File dir = new File(dirStr);
		if (dir == null || !dir.exists())
		{
			dir.mkdir();
		}
	}

	/**
	 * This method gets valid SSN.
	 * @param ssn Social Security Number to check
	 * @return boolean depending on the value of ssn.
	 */
	public static String getValidSSN(String ssn)
	{
		boolean result = true;
		Pattern re = null;
		Matcher mat = null;
		StringBuffer buff = null;
		try
		{
			re = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}", Pattern.CASE_INSENSITIVE);
			mat = re.matcher(ssn);
			result = mat.matches();
			if (!result)
			{
				re = Pattern.compile("[0-9]{9}", Pattern.CASE_INSENSITIVE);
				mat = re.matcher(ssn);
				result = mat.matches();
				if (result)
				{
					buff = new StringBuffer();
					buff.append(ssn.substring(0, 3)).append("-").append(ssn.substring(3, 5))
							.append("-").append(ssn.substring(5, 9));
				}
			}
			else
			{
				buff = new StringBuffer(ssn);
			}
		}
		catch (final Exception exp)
		{
			ReportLoaderUtil.logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			return buff.toString();
		}
		return buff.toString();
	}

	/**
	 * This method gets Line From Report.
	 * @param reportText report Text.
	 * @param lineToExtract line To Extract
	 * @return line
	 */
	public static String getLineFromReport(String reportText, String lineToExtract)
	{
		final String[] lines = reportText.split("\n");
		for (final String line : lines)
		{
			if (line.startsWith(lineToExtract))
			{
				return line;
			}
		}
		return "";
	}

	/**
	 * This method returns the matching SCG (based on SPN and site) present in the database.
	 * @param site site
	 * @param surgicalPathologyNumber surgical Pathology Number
	 * @return specimenCollectionGroupObj
	 * @throws Exception Exception
	 */
	public static SpecimenCollectionGroup getExactMatchingSCG(Site site,
			String surgicalPathologyNumber) throws Exception
	{
		final String scgHql = "select scg"
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg "
				+ " where scg.specimenCollectionSite.name='" + site.getName() + "' "
				+ " and scg.surgicalPathologyNumber='" + surgicalPathologyNumber + "'";

		final List resultList = (List) CaCoreAPIService.executeQuery(scgHql,
				SpecimenCollectionGroup.class.getName());

		logger.info("-------------" + scgHql + "   " + resultList.size());
		if (resultList != null && resultList.size() == 1)
		{
			return (SpecimenCollectionGroup) resultList.get(0);
		}
		return null;
	}

	public static Site getSite(String siteName) throws Exception {
		  
		Site site = null;
		final String scgHql = "select site"
			+ " from edu.wustl.catissuecore.domain.Site as site "
			+ " where site.name='" + siteName +"'";
		logger.info("-------------" + scgHql );
		final List resultList = (List) CaCoreAPIService.executeQuery(scgHql,
			Site.class.getName());

		logger.info("-------------" + scgHql + "   " + resultList.size());
		if (resultList != null && resultList.size() == 1)
		{
		 site = (Site) resultList.get(0);
		}
		  return site;
	  }
	
	/**
	 * Checks is Partial Matching SCG.
	 * @param participant participant
	 * @param site site
	 * @return boolean
	 * @throws Exception Exception
	 */
	public static boolean isPartialMatchingSCG(Participant participant, Site site) throws Exception
	{
		final String scgHql = "select scg"
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg, "
				+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr,"
				+ " edu.wustl.catissuecore.domain.Participant as p " + " where p.id = "
				+ participant.getId() + " and p.id = cpr.participant.id "
				+ " and scg.id in elements(cpr.specimenCollectionGroupCollection)"
				+ " and scg.specimenCollectionSite.name='" + site.getName() + "' "
				+ " and (scg.surgicalPathologyNumber=" + null
				+ " or scg.surgicalPathologyNumber='')";

		final List resultList = (List) CaCoreAPIService.executeQuery(scgHql,
				SpecimenCollectionGroup.class.getName());
		if (resultList != null && resultList.size() > 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * This method returns the respective participant to which SCG is associated with.
	 * @param scgId scg Id
	 * @return participntObj participant Object
	 * @throws Exception Exception
	 */
	public static Participant getParticipant(Long scgId) throws Exception
	{
		final String hqlString = "select p"
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg, "
				+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr,"
				+ " edu.wustl.catissuecore.domain.Participant as p " + " where scg.id = " + scgId
				+ " and p.id = cpr.participant.id "
				+ " and scg.id in elements(cpr.specimenCollectionGroupCollection)";

		final List resultList = (List) CaCoreAPIService.executeQuery(hqlString,
				SpecimenCollectionGroup.class.getName());
		if (resultList != null && resultList.size() > 0)
		{
			return (Participant) resultList.get(0);
		}
		return null;
	}
}
