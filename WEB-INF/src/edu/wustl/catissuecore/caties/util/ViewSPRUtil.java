
package edu.wustl.catissuecore.caties.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.bean.ConceptHighLightingBean;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.reportloader.HL7ParserUtil;
import edu.wustl.catissuecore.reportloader.IdentifiedReportGenerator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

/**
 * @author
 *
 */
public class ViewSPRUtil
{

	/**
	 * @param deidentifiedSurgicalPathologyReport : deidentifiedSurgicalPathologyReport
	 * @throws BizLogicException : BizLogicException
	 * @return List
	 */
	public static List getConceptBeanList(
			DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport)
			throws BizLogicException
	{
		final List conceptBeanList = new ArrayList();
		if (deidentifiedSurgicalPathologyReport != null)
		{
			final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			final Collection conceptReferentColl = (Set) defaultBizLogic.retrieveAttribute(
					DeidentifiedSurgicalPathologyReport.class.getName(),
					deidentifiedSurgicalPathologyReport.getId(),
					Constants.COLUMN_NAME_CONCEPT_REF_COLL);
			//temporary map to make a list of concept referent classification objects
			final Map tempMap = new HashMap();
			if (conceptReferentColl != null)
			{
				final Iterator iter = conceptReferentColl.iterator();
				while (iter.hasNext())
				{
					final ConceptReferent conceptReferent = (ConceptReferent) iter.next();
					final ConceptReferentClassification conceptReferentClassification = conceptReferent
							.getConceptReferentClassification();
					ConceptHighLightingBean conceptHighLightingBean = null;
					if (tempMap.get(conceptReferentClassification.getName().toUpperCase()) == null)
					{ // if concept classification obj is not present in the list
						conceptHighLightingBean = new ConceptHighLightingBean();
						conceptHighLightingBean.setClassificationName(conceptReferentClassification
								.getName());
						conceptHighLightingBean.setConceptName(conceptReferent.getConcept()
								.getName());
						conceptHighLightingBean.setStartOffsets(conceptReferent.getStartOffset()
								.toString());
						conceptHighLightingBean.setEndOffsets(conceptReferent.getEndOffset()
								.toString());

						tempMap.put(conceptReferentClassification.getName().toUpperCase(),
								conceptHighLightingBean);
					}
					else
					{
						conceptHighLightingBean = (ConceptHighLightingBean) tempMap
								.get(conceptReferentClassification.getName().toUpperCase());

						conceptHighLightingBean.setConceptName(conceptHighLightingBean
								.getConceptName()
								+ "," + conceptReferent.getConcept().getName());
						conceptHighLightingBean.setStartOffsets(conceptHighLightingBean
								.getStartOffsets()
								+ "," + conceptReferent.getStartOffset());
						conceptHighLightingBean.setEndOffsets(conceptHighLightingBean
								.getEndOffsets()
								+ "," + conceptReferent.getEndOffset());

						tempMap.put(conceptReferentClassification.getName().toUpperCase(),
								conceptHighLightingBean);
					}
				}

				final Set keySet = tempMap.keySet();
				final Iterator keySetIter = keySet.iterator();
				while (keySetIter.hasNext())
				{
					conceptBeanList.add(tempMap.get(keySetIter.next()));
				}
			}
		}
		return conceptBeanList;
	}

	/**
	 * Retrieve the synthesized report Text.
	 * @param reportText : reportText
	 * @return String
	 * @throws Exception : Exception
	 */

	public static String getSynthesizedText(String reportText) throws Exception
	{
		String sysnthesizedReportText = "";

		final String configFileName = CommonServiceLocator.getInstance().getPropDirPath()
				+ File.separator + "SectionHeaderConfig.txt";

		//initialize section header map
		Map abbrToHeader = new HashMap();
		abbrToHeader = Utility.initializeReportSectionHeaderMap(configFileName);

		//retieved the report Map
		Map reportMap = new HashMap();
		reportMap = HL7ParserUtil.getReportMap(reportText);

		//retrieved the identified report
		IdentifiedSurgicalPathologyReport report = null;
		report = IdentifiedReportGenerator.getIdentifiedReport(reportMap, abbrToHeader);
		sysnthesizedReportText = report.getTextContent().getData();

		return sysnthesizedReportText;

	}

	/**
	 * @param medicalIdentifierNumbers : medicalIdentifierNumbers
	 * @return Map
	 */
	public static Map getMedicalIdentifierNumbers(Collection medicalIdentifierNumbers)
	{
		final HashMap values = new HashMap();
		int i = 1;

		final Iterator it = medicalIdentifierNumbers.iterator();
		while (it.hasNext())
		{
			final ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) it
					.next();

			final String key1 = "ParticipantMedicalIdentifier:" + i + "_Site_id";
			final String key2 = "ParticipantMedicalIdentifier:" + i + "_medicalRecordNumber";
			final String key3 = "ParticipantMedicalIdentifier:" + i + "_id";

			final Site site = participantMedicalIdentifier.getSite();
			if (site != null)
			{
				values.put(key1, CommonUtilities.toString(site.getName()));
			}
			else
			{
				values.put(key1, CommonUtilities.toString(Constants.SELECT_OPTION));
			}

			values.put(key2, CommonUtilities.toString(participantMedicalIdentifier
					.getMedicalRecordNumber()));
			values.put(key3, CommonUtilities.toString(participantMedicalIdentifier.getId()));

			i++;
		}
		return values;
	}

	/**
	 * @param reportQueueId : reportQueueId
	 * @return String
	 * @throws Exception : Exception
	 */
	public static String getSynthesizedTextForReportQueue(String reportQueueId) throws Exception

	{
		final ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = new ReportLoaderQueueBizLogic();

		List reportQueueDataList = null;

		ReportLoaderQueue reportLoaderQueue = null;

		reportQueueDataList = reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class

		.getName(), Constants.SYSTEM_IDENTIFIER, Long.valueOf(reportQueueId));

		if ((reportQueueDataList != null) && (reportQueueDataList).size() > 0)
		{
			reportLoaderQueue = (ReportLoaderQueue) reportQueueDataList.get(0);
		}
		return ViewSPRUtil.getSynthesizedText(reportLoaderQueue.getReportText());

	}

}
