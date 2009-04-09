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
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.reportloader.HL7ParserUtil;
import edu.wustl.catissuecore.reportloader.IdentifiedReportGenerator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Variables;

public class ViewSPRUtil 
{
	/**
	 * @param request
	 * @param deidentifiedSurgicalPathologyReport
	 */
	public static List getConceptBeanList(DeidentifiedSurgicalPathologyReport deidentifiedSurgicalPathologyReport) throws BizLogicException
	{		
		List conceptBeanList = new ArrayList();
		if(deidentifiedSurgicalPathologyReport != null)
		{
			DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
			Collection conceptReferentColl =(Set)defaultBizLogic.retrieveAttribute(DeidentifiedSurgicalPathologyReport.class.getName(), deidentifiedSurgicalPathologyReport.getId(), Constants.COLUMN_NAME_CONCEPT_REF_COLL);
			//temporary map to make a list of concept referent classification objects
			Map tempMap = new HashMap();
			if(conceptReferentColl != null)
			{
				Iterator iter = conceptReferentColl.iterator();
				while(iter.hasNext())
				{
					ConceptReferent conceptReferent = (ConceptReferent)iter.next();
					ConceptReferentClassification conceptReferentClassification = conceptReferent.getConceptReferentClassification();
					ConceptHighLightingBean conceptHighLightingBean = null;
					if(tempMap.get(conceptReferentClassification.getName().toUpperCase()) == null)
					{	// if concept classification obj is not present in the list						
						conceptHighLightingBean = new ConceptHighLightingBean();
						conceptHighLightingBean.setClassificationName(conceptReferentClassification.getName());
						conceptHighLightingBean.setConceptName(conceptReferent.getConcept().getName());
						conceptHighLightingBean.setStartOffsets(conceptReferent.getStartOffset().toString());
						conceptHighLightingBean.setEndOffsets(conceptReferent.getEndOffset().toString());
						
						tempMap.put(conceptReferentClassification.getName().toUpperCase(), conceptHighLightingBean);
					}
					else
					{
						conceptHighLightingBean =(ConceptHighLightingBean) tempMap.get(conceptReferentClassification.getName().toUpperCase());
						
						conceptHighLightingBean.setConceptName(conceptHighLightingBean.getConceptName() + "," + conceptReferent.getConcept().getName());
						conceptHighLightingBean.setStartOffsets(conceptHighLightingBean.getStartOffsets() + "," + conceptReferent.getStartOffset());
						conceptHighLightingBean.setEndOffsets(conceptHighLightingBean.getEndOffsets() + "," + conceptReferent.getEndOffset());
						
						tempMap.put(conceptReferentClassification.getName().toUpperCase(), conceptHighLightingBean);
					}
				}
				
				Set keySet = tempMap.keySet();
				Iterator keySetIter = keySet.iterator();
				while(keySetIter.hasNext())
				{
					conceptBeanList.add(tempMap.get(keySetIter.next()));
				}
			}
		}
		return conceptBeanList;
	}
	
	/**
	 * Retrieve the synthesized report Text
	 * @param reportText
	 * @return
	 * @throws Exception
	 */
	
	public static String getSynthesizedText(String reportText) throws Exception
	{
		String sysnthesizedReportText="";
		

		String configFileName = CommonServiceLocator.getInstance().getPropDirPath() +File.separator+"SectionHeaderConfig.txt";
		
		//initialize section header map
		Map abbrToHeader = new HashMap();;
		abbrToHeader=Utility.initializeReportSectionHeaderMap(configFileName);
		
		//retieved the report Map
		Map reportMap = new HashMap();;
		reportMap =HL7ParserUtil.getReportMap(reportText);
		
		//retrieved the identified report
		IdentifiedSurgicalPathologyReport report = null;
		report=(IdentifiedSurgicalPathologyReport)IdentifiedReportGenerator.getIdentifiedReport(reportMap, (HashMap<String, String>) abbrToHeader);
		sysnthesizedReportText=report.getTextContent().getData();
		
		return sysnthesizedReportText;
		
	}
	
	public static Map getMedicalIdentifierNumbers(Collection medicalIdentifierNumbers)
	{
		HashMap values= new HashMap();
		int i = 1;
    	
    	Iterator it = medicalIdentifierNumbers.iterator();
    	while(it.hasNext())
    	{
    		ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier)it.next();
    		
    		String key1 = "ParticipantMedicalIdentifier:" + i +"_Site_id";
			String key2 = "ParticipantMedicalIdentifier:" + i +"_medicalRecordNumber";
			String key3 = "ParticipantMedicalIdentifier:" + i  +"_id";

			Site site =participantMedicalIdentifier.getSite();
			if(site!=null)
			{
				values.put(key1,edu.wustl.common.util.Utility.toString(site.getName()));
			}
			else
			{
				values.put(key1,edu.wustl.common.util.Utility.toString(Constants.SELECT_OPTION));
			}					
			
			values.put(key2,edu.wustl.common.util.Utility.toString(participantMedicalIdentifier.getMedicalRecordNumber()));
			values.put(key3,edu.wustl.common.util.Utility.toString(participantMedicalIdentifier.getId()));
			
			i++;
    	}
    	return values;
	}

}
