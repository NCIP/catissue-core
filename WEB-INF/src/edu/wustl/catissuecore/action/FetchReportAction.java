package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;


/**
 * @author ashish_gupta
 *
 */
public class FetchReportAction extends BaseAction
{
	/**
     * Overrides the execute method of Action class.
     * Sets the various Collection and Received events based on SCG id.
     */
	public ActionForward  executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		Logger.out.info("Inside FetchReportAction...................................!");
    	String scgId = request.getParameter("reportId");
    	
    	StringBuffer xmlData = new StringBuffer();
    	if(scgId != null && !scgId.equals(""))
    	{
    		IdentifiedSurgicalPathologyReportBizLogic identifiedReportBizLogic = (IdentifiedSurgicalPathologyReportBizLogic)BizLogicFactory.getInstance().getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
    		String colName = Constants.SYSTEM_IDENTIFIER;	
    		List reportListFromDB = identifiedReportBizLogic.retrieve(IdentifiedSurgicalPathologyReport.class.getName(), colName, scgId);
    		if(reportListFromDB != null && !reportListFromDB.isEmpty())
    		{
    			IdentifiedSurgicalPathologyReport identifiedReport = (IdentifiedSurgicalPathologyReport)reportListFromDB.get(0);
    			if(identifiedReport.getSpecimenCollectionGroup() != null && identifiedReport.getSpecimenCollectionGroup().getSurgicalPathologyNumber()!=null)
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
	 */
	private StringBuffer makeXMLData(StringBuffer xmlData, IdentifiedSurgicalPathologyReport idenitifiedReport)
	{		
		
		xmlData.append("<ReportInfo>");
		
		xmlData.append("<IdentifiedReportAccessionNumber>");
		if(idenitifiedReport.getSpecimenCollectionGroup()!=null && idenitifiedReport.getSpecimenCollectionGroup().getSurgicalPathologyNumber()!=null)
		{
			xmlData.append(idenitifiedReport.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
		}
		xmlData.append("</IdentifiedReportAccessionNumber>");
		
		xmlData.append("<IdentifiedReportSite>");
		if(idenitifiedReport.getSource()!=null)
		{
			xmlData.append(idenitifiedReport.getSource().getName());
		}
		xmlData.append("</IdentifiedReportSite>");
		
		xmlData.append("<IdentifiedReportTextContent>");
		if(idenitifiedReport.getTextContent()!=null)
		{
			xmlData.append(idenitifiedReport.getTextContent().getData());
		}
		xmlData.append("</IdentifiedReportTextContent>");
		
		xmlData.append("<DeIdentifiedReportTextContent>");
		if(idenitifiedReport.getDeidentifiedSurgicalPathologyReport()!=null && idenitifiedReport.getDeidentifiedSurgicalPathologyReport().getTextContent()!=null)
		{
			xmlData.append(idenitifiedReport.getDeidentifiedSurgicalPathologyReport().getTextContent().getData());
		}
		xmlData.append("</DeIdentifiedReportTextContent>");
		
		xmlData.append("</ReportInfo>");
		return xmlData;
		
	}
}
