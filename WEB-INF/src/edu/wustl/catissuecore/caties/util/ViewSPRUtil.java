package edu.wustl.catissuecore.caties.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.bean.ConceptHighLightingBean;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeIdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

public class ViewSPRUtil 
{
	/**
	 * @param request
	 * @param deIdentifiedSurgicalPathologyReport
	 */
	public static List getConceptBeanList(HttpServletRequest request,DeIdentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport) throws DAOException
	{		
		List conceptBeanList = new ArrayList();
		if(deIdentifiedSurgicalPathologyReport != null)
		{
			DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
			Collection conceptReferentColl =(Set)defaultBizLogic.retrieveAttribute(DeIdentifiedSurgicalPathologyReport.class.getName(), deIdentifiedSurgicalPathologyReport.getId(), Constants.COLUMN_NAME_CONCEPT_REF_COLL);
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
}
