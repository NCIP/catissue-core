

/**
 * <p>Title: ConflictReportAction Class>
 * <p>Description:	Initialization action for conflict Report view
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 *@version 1.00
 *@author kalpana Thakur
 * Created on sep 18,2007
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.AbstractAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictCommonForm;
import edu.wustl.catissuecore.actionForm.ConflictDetailsForm;
import edu.wustl.catissuecore.actionForm.ConflictSCGForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.Utility;
import edu.wustl.catissuecore.caties.util.ViewSPRUtil;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.reportloader.HL7Parser;
import edu.wustl.catissuecore.reportloader.HL7ParserUtil;
import edu.wustl.catissuecore.reportloader.IdentifiedReportGenerator;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

public class ConflictReportAction extends BaseAction{
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ConflictSCGForm conflictSCGForm = (ConflictSCGForm)form;
		
		String reportQueueId = (String) request.getParameter(Constants.REPORT_ID);
	    	
		List reportQueueDataList =  new ArrayList();
		ReportLoaderQueue reportLoaderQueue =null;
		reportQueueDataList = getReportQueueDataList(reportQueueId);
		if((reportQueueDataList!=null) && (reportQueueDataList).size()>0)
		{
			reportLoaderQueue = (ReportLoaderQueue)reportQueueDataList.get(0);
		}
		
		String newConfictedReport = reportLoaderQueue.getReportText();
		
		//retrieved the identified report
		newConfictedReport=ViewSPRUtil.getSynthesizedText(newConfictedReport);
		conflictSCGForm.setNewConflictedReport(newConfictedReport);
		return mapping.findForward(Constants.SUCCESS);
	}
	
	/**To retrieve the list of report loader Queue
	 * @param reportQueueId
	 * @return
	 * @throws DAOException
	 */
	private List getReportQueueDataList(String reportQueueId) throws DAOException
	{
		
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic)BizLogicFactory.getInstance().getBizLogic(ReportLoaderQueue.class.getName());
	    List reportQueueList = (List)reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class.getName(),Constants.SYSTEM_IDENTIFIER, new Long(reportQueueId));
		return reportQueueList;		
	}

}
