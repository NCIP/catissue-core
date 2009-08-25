
package edu.wustl.catissuecore.reportloader;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;

/**
 * IdentifiedReportGenerator.
 * @author sagar_baldwa
 */

public final class IdentifiedReportGenerator
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(IdentifiedReportGenerator.class);

	/**
	 * private constructor.
	 */
	private IdentifiedReportGenerator()
	{

	}

	/**
	 * @param reportMap Map of String, Set
	 * @param abbrToHeader HashMap of String, String
	 * @return IdentifiedSurgicalPathologyReport
	 */
	public static IdentifiedSurgicalPathologyReport getIdentifiedReport(Map<String, Set> reportMap,
			Map<String, String> abbrToHeader)
	{
		ReportSection reportSection = null;
		final TextContent textContent = new TextContent();
		Set<ReportSection> reportSectionSet = null;
		IdentifiedSurgicalPathologyReport report = null;
		reportSectionSet = new HashSet<ReportSection>();
		Set reportSet = null;
		Iterator reportIterator = null;
		logger.info("creating report");

		try
		{
			final String obrLine = HL7ParserUtil.getReportDataFromReportMap(reportMap,
					CaTIESConstants.OBR);
			report = extractOBRSegment(obrLine);
			reportSet = getReportSectionDataFromReportMap(reportMap, CaTIESConstants.OBX);
			if (reportSet != null && reportSet.size() > 0)
			{
				reportIterator = reportSet.iterator();
				// Iterate on the reportSet to create report section
				while (reportIterator.hasNext())
				{
					reportSection = extractOBXSegment((String) reportIterator.next());
					if (reportSection.getDocumentFragment() != null)
					{
						reportSectionSet.add(reportSection);
						reportSection.setTextContent(textContent);
					}
				}
			}
			// synthesize report text
			textContent.setData(IdentifiedReportGenerator.synthesizeSPRText(reportSectionSet,
					abbrToHeader));
			//for oracle
			for (final ReportSection tempReportSection : reportSectionSet)
			{
				if (tempReportSection.getDocumentFragment().length() > 3900)
				{
					logger.info("*****************for trim");
					tempReportSection.setDocumentFragment(tempReportSection.getDocumentFragment()
							.substring(0, 3900));
					logger.info("*****************trim completed");
				}
			}
			// set textContent to report
			report.setTextContent(textContent);
			textContent.setSurgicalPathologyReport(report);
			if (reportSectionSet != null && reportSectionSet.size() > 1)
			{
				report.getTextContent().setReportSectionCollection(reportSectionSet);
			}
			logger.info("Report created");
		}
		catch (final Exception excp)
		{
			logger.error(excp);
		}
		return report;
	}

	/**
	* Method to retrieve specific report section from reportMap.
	* @param reportMap report map
	* @param key key of the report map
	* @return collection of report section data from the report map
	*/
	private static Set getReportSectionDataFromReportMap(Map<String, Set> reportMap, String key)
	{
		final Set tempSet = reportMap.get(key);
		return tempSet;
	}

	/**
	 * This method parses each observations in the report (OBX section) and returns report section.
	 * @param obxLine observation text of the pathology report
	 * @return report section of the pathology report.
	 */
	private static ReportSection extractOBXSegment(String obxLine)
	{
		final ReportSection section = new ReportSection();
		try
		{
			String newObxLine = obxLine.replace('|', '~');
			newObxLine = newObxLine.replaceAll("~", "|~~");
			final StringTokenizer strTokenizer = new StringTokenizer(newObxLine, "|");
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
				// token for text section
				if ((x == 2) && !(field.equals(CaTIESConstants.TEXT_SECTION)))
				{
					break;
				}
				// token for name of the section
				if (x == CaTIESConstants.NAME_SECTION_INDEX)
				{
					section.setName(field);
				}
				// token for document fragment
				if (x == CaTIESConstants.DOCUMENT_FRAGMENT_INDEX)
				{
					String text = field;
					text = text.replace('\\', '~');
					text = text.replaceAll("~.br~", "\n");
					section.setDocumentFragment(text);
				}
			}
		}
		catch (final Exception excp)
		{
			logger.error(excp);
		}
		return section;
	}

	/**
	* Method to extract information from OBR segment.
	* @param obrLine report information text
	* @return Identified surgical pathology report from report text
	*/
	protected static IdentifiedSurgicalPathologyReport extractOBRSegment(String obrLine)
	{
		final IdentifiedSurgicalPathologyReport report = new IdentifiedSurgicalPathologyReport();
		try
		{
			String newObrLine = obrLine.replace('|', '~');
			newObrLine = newObrLine.replaceAll("~", "|~~");
			//set default values to report
			report.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			report.setIsFlagForReview(Boolean.FALSE);
			final StringTokenizer strTokenizer = new StringTokenizer(newObrLine, "|");
			// iterate over token to create report
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
				if (x == CaTIESConstants.REPORT_ACCESSIONNUMBER_INDEX)
				{
					final StringTokenizer st2 = new StringTokenizer(field, "^");
					st2.nextToken();
				}
				// report collection date and time
				if (x == CaTIESConstants.REPORT_DATE_INDEX)
				{
					final String year = field.substring(0, 4);
					final String month = field.substring(4, 6);
					final String day = field.substring(6, 8);
					final String hours = field.substring(8, 10);
					final String seconds = field.substring(10, 12);

					final GregorianCalendar gregorianCalen = new GregorianCalendar(Integer
							.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day),
							Integer.parseInt(hours), Integer.parseInt(seconds));
					report.setCollectionDateTime(gregorianCalen.getTime());
				}
			}
		}
		catch (final Exception e)
		{
			logger.error("Error while parsing the report map", e);
		}
		return report;
	}

	/**
	 * Method to synthesize report text.
	 * @return synthesizeSPRText Surgical pathology report text
	 * @param reportSectionSet Set of ReportSection.
	 * @param abbrToHeader hashMap of String, String
	 */
	private static String synthesizeSPRText(final Set<ReportSection> reportSectionSet,
			Map<String, String> abbrToHeader)
	{
		String docText = "";
		try
		{
			//Get report sections for report
			final HashMap<String, String> nameToText = new HashMap<String, String>();
			if (reportSectionSet == null)
			{
				logger.info("NULL report section collection found in synthesizeSPRText method");
			}
			else
			{
				logger.info("Synthesizing report");
				for (final ReportSection rs : reportSectionSet)
				{
					final String abbr = rs.getName();
					final String text = rs.getDocumentFragment();
					//add abbreviation and report text to hash map
					nameToText.put(abbr, text);
				}
			}
			for (final String key : abbrToHeader.keySet())
			{
				if (nameToText.containsKey(key))
				{
					// get full section header name from its abbreviation
					final String sectionHeader = abbrToHeader.get(key);
					//if the key is present in the report section collection map then
					//format the section header and section text
					final String sectionText = nameToText.get(key);
					// format for section header and section text
					docText += "\n\n[" + sectionHeader + "]" + "\n\n" + sectionText + "\n\n";
				}
			}
		}
		catch (final Exception excp)
		{
			logger.error(excp);
		}
		return docText;
	}
}