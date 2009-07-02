/**
 * <p>Title: ConflictSCGAction Class>
 * <p>Description:	Initialized to retrieve the new and existing reports
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 *@author kalpana Thakur
 * Created on sep 18,2007
  */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictSCGForm;
import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
import edu.wustl.catissuecore.caties.util.ViewSPRUtil;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;

/**
 * @author renuka_bajpai
 *
 */
public class ConflictSCGAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ConflictSCGForm conflictSCGForm = (ConflictSCGForm) form;

		String reportQueueId = (String) request.getParameter(Constants.REPORT_ID);

		List reportQueueDataList = new ArrayList();
		ReportLoaderQueue reportLoaderQueue = null;
		reportQueueDataList = getReportQueueDataList(reportQueueId);
		if ((reportQueueDataList != null) && (reportQueueDataList).size() > 0)
		{
			reportLoaderQueue = (ReportLoaderQueue) reportQueueDataList.get(0);
		}

		String newConfictedReport = reportLoaderQueue.getReportText();
		newConfictedReport = ViewSPRUtil.getSynthesizedText(newConfictedReport);

		String existingConflictedReport = retrieveExistingReport(reportQueueId);
		conflictSCGForm.setExistingConflictedReport(existingConflictedReport);
		conflictSCGForm.setNewConflictedReport(newConfictedReport);
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * To retrieve the reportQueue list.
	 * @param reportQueueId : reportQueueId
	 * @return List : List
	 * @throws BizLogicException : BizLogicException
	 */
	private List getReportQueueDataList(String reportQueueId) throws BizLogicException
	{

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
				.getBizLogic(ReportLoaderQueue.class.getName());
		List reportQueueList = (List) reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class
				.getName(), Constants.SYSTEM_IDENTIFIER, Long.valueOf(reportQueueId));
		return reportQueueList;
	}

	/**
	 * To retrieve the existing report.
	 * @param reportQueueId : reportQueueId
	 * @return String : String
	 * @throws BizLogicException : BizLogicException
	 * @throws ClassNotFoundException : ClassNotFoundException
	 */
	private String retrieveExistingReport(String reportQueueId) throws BizLogicException,
			ClassNotFoundException
	{
		String existingConflictedReport = "";
		Long reportId = Long.parseLong(reportQueueId);
		Long scgId = null;

		String scgHql = "select rlq.specimenCollectionGroup.id "
				+ " from edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue as rlq "
				+ " where rlq.id= " + reportId;

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
				.getBizLogic(ReportLoaderQueue.class.getName());
		List scgList = (List) reportLoaderQueueBizLogic.executeQuery(scgHql);
		if ((scgList != null) && (scgList).size() > 0)
		{
			scgId = (Long) scgList.get(0);
		}

		String ispReportHql = "select scg.identifiedSurgicalPathologyReport.textContent.data"
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg "
				+ " where scg.id =" + scgId + "";

		List ispReportList = (List) reportLoaderQueueBizLogic.executeQuery(ispReportHql);
		if ((ispReportList != null) && (ispReportList).size() > 0)
		{
			existingConflictedReport = (String) ispReportList.get(0);
		}

		return existingConflictedReport;
	}

}
