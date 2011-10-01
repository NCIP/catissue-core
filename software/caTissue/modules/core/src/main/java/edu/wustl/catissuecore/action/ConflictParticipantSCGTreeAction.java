/**
 * <p>Title: ConflictParticipantSCGTreeAction Class>
 * <p>Description:	ConflictParticipantSCGTreeAction class: it sets the tree node
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 *@author kalpana Thakur
 * Created on sep 18,2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ReportLoaderQueueBizLogic;
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
public class ConflictParticipantSCGTreeAction extends BaseAction
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
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final String reportQueueId = request.getParameter(Constants.REPORT_ID);
		ReportLoaderQueue reportLoaderQueue = null;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
				.getBizLogic(ReportLoaderQueue.class.getName());

		Vector treeData = new Vector();
		List reportQueueData = new ArrayList();
		Iterator iter = null;
		reportQueueData = this.getReportQueueDataList(reportQueueId);
		iter = reportQueueData.iterator();
		if (iter.hasNext())
		{
			reportLoaderQueue = (ReportLoaderQueue) iter.next();
		}
		final String siteName = reportLoaderQueue.getSiteName();
		final Long reportId = reportLoaderQueue.getId();

		//To retrieve the tree data
		treeData = reportLoaderQueueBizLogic.getTreeViewData(reportId, siteName, treeData);
		request.setAttribute("treeData", treeData);

		return mapping.findForward(Constants.SUCCESS);

	}

	/**
	 *
	 * @param reportQueueId : reportQueueId
	 * @return List : List
	 * @throws BizLogicException : BizLogicException
	 */
	private List getReportQueueDataList(String reportQueueId) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
				.getBizLogic(ReportLoaderQueue.class.getName());
		final List reportQueueList = reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class
				.getName(), Constants.SYSTEM_IDENTIFIER, new Long(reportQueueId));
		return reportQueueList;
	}
}
