package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.ConceptHighLightingBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.caties.util.ViewSPRUtil;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.dao.exception.DAOException;


/**
 * @author vijay_pande
 *
 */
public class FetchReportAction extends BaseAction
{
	/**
     * Overrides the execute method of Action class.
     * Sets the various Collection and Received events based on SCG id.
     */
	protected ActionForward  executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		String reportId = request.getParameter("reportId");
    	
    	StringBuffer xmlData = new StringBuffer();
    	if(reportId != null && !reportId.equals(""))
    	{
    		IdentifiedSurgicalPathologyReportBizLogic identifiedReportBizLogic = (IdentifiedSurgicalPathologyReportBizLogic)BizLogicFactory.getInstance().getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
    		
    		Object object = identifiedReportBizLogic.retrieve(IdentifiedSurgicalPathologyReport.class.getName(), new Long(reportId));
    		if(object != null)
    		{
    			IdentifiedSurgicalPathologyReport identifiedReport = (IdentifiedSurgicalPathologyReport) object;
    			if(identifiedReport.getSpecimenCollectionGroup() != null) 
    			{
    				xmlData = makeXMLData(xmlData, identifiedReport);
    			}
    		}
    	}
//    	Writing to response
		PrintWriter out = response.getWriter();	
		response.setContentType("text/xml");
		out.write(xmlData.toString());
		
    	return null;
    }
	/**
	 * @return
	 * @throws DAOException 
	 */
	private StringBuffer makeXMLData(StringBuffer xmlData, IdentifiedSurgicalPathologyReport identifiedReport) throws DAOException, SQLException
	{	
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		SpecimenCollectionGroup scg=(SpecimenCollectionGroup)defaultBizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), Constants.COLUMN_NAME_SCG);
		DeidentifiedSurgicalPathologyReport deidReport=(DeidentifiedSurgicalPathologyReport)defaultBizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), Constants.COLUMN_NAME_DEID_REPORT);
		Site source=(Site)defaultBizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), Constants.COLUMN_NAME_REPORT_SOURCE);
		TextContent identifiedReportText=(TextContent)defaultBizLogic.retrieveAttribute(IdentifiedSurgicalPathologyReport.class.getName(), identifiedReport.getId(), Constants.COLUMN_NAME_TEXT_CONTENT);
		TextContent deidReportText=null;
		if(deidReport!=null)
		{
			deidReportText=(TextContent)defaultBizLogic.retrieveAttribute(DeidentifiedSurgicalPathologyReport.class.getName(), deidReport.getId(), Constants.COLUMN_NAME_TEXT_CONTENT);
		}
		List conceptBeanList=ViewSPRUtil.getConceptBeanList(deidReport);
		String conceptBeans=getConceptBeans(conceptBeanList);
		
		xmlData.append("<ReportInfo>");
		
		xmlData.append("<SurgicalPathologyNumber>");
		if(scg!=null && scg.getSurgicalPathologyNumber()!=null)
		{
			xmlData.append(scg.getSurgicalPathologyNumber());
		}
		xmlData.append("</SurgicalPathologyNumber>");
		
		xmlData.append("<IdentifiedReportSite>");
		if(source!=null)
		{
			xmlData.append(source.getName());
		}
		xmlData.append("</IdentifiedReportSite>");
		
		xmlData.append("<IdentifiedReportTextContent>");
		if(identifiedReport.getTextContent()!=null)
		{
			xmlData.append(identifiedReportText.getData());
		}
		xmlData.append("</IdentifiedReportTextContent>");
		
		xmlData.append("<DeIdentifiedReportTextContent>");
		if(deidReportText!=null) 
		{
			xmlData.append(deidReportText.getData());
		}
		else
		{
			xmlData.append("");
		}
		xmlData.append("</DeIdentifiedReportTextContent>");
		xmlData.append("<JavaScriptFunction>");
		if(conceptBeans!=null) 
		{
			xmlData.append(conceptBeans);
		}
		xmlData.append("</JavaScriptFunction>");
		
		xmlData.append("</ReportInfo>");
		return xmlData;
		
	}
	
	private String getConceptBeans(List conceptBeanList)
	{
		String[] onClickMethod=null;
		String[] colours = Constants.CATEGORY_HIGHLIGHTING_COLOURS;
		StringBuffer script=new StringBuffer();
		if(conceptBeanList!=null)
		{
			ConceptHighLightingBean referentClassificationObj;
			String classificationName;
			String conceptName;
			String startOff;
			String endOff;	
			Pattern pattern = Pattern.compile("['\"]");
			Matcher matcher;
			
			onClickMethod=new String[conceptBeanList.size()];
			for(int i=0;i<conceptBeanList.size();i++)
			{
				referentClassificationObj=(ConceptHighLightingBean) conceptBeanList.get(i);
				classificationName=referentClassificationObj.getClassificationName();
				conceptName = referentClassificationObj.getConceptName();
				startOff = referentClassificationObj.getStartOffsets();
				endOff = referentClassificationObj.getEndOffsets();	
				matcher=pattern.matcher(conceptName);
				conceptName=matcher.replaceAll("");

				String chkBoxId = "select"+i; 
				onClickMethod[i] = "selectByOffset(document.getElementById('"+chkBoxId+"'),'"+startOff+"','"+endOff+"','"+colours[i]+"','"+conceptName+"')";
				script.append("<ConceptBean>");
					script.append("<ConceptName>");
						script.append(conceptName);
					script.append("</ConceptName>");
					script.append("<StartOff>");
						script.append(startOff);
					script.append("</StartOff>");
					script.append("<EndOff>");
						script.append(endOff);
					script.append("</EndOff>");
					script.append("<ClassificationName>");
						script.append(classificationName);
					script.append("</ClassificationName>");	
				script.append("</ConceptBean>");
			}
		}
		return script.toString();
	}
}
