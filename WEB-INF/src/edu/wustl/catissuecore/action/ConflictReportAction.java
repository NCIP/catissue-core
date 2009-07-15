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
import edu.wustl.dao.exception.DAOException;

/**
 * @author renuka_bajpai
 *
 */
public class ConflictReportAction extends BaseAction
{

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ConflictSCGForm conflictSCGForm = (ConflictSCGForm) form;
		String reportQueueId = (String) request.getParameter(Constants.REPORT_ID);
		String newConfictedReport = ViewSPRUtil.getSynthesizedTextForReportQueue(reportQueueId);
		conflictSCGForm.setNewConflictedReport(newConfictedReport);
		return mapping.findForward(Constants.SUCCESS);
	}

	/**To retrieve the list of report loader Queue
	 * @param reportQueueId
	 * @return
	 * @throws DAOException
	 */
	private List getReportQueueDataList(String reportQueueId) throws BizLogicException
	{

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		ReportLoaderQueueBizLogic reportLoaderQueueBizLogic = (ReportLoaderQueueBizLogic) factory
				.getBizLogic(ReportLoaderQueue.class.getName());
		List reportQueueList = (List) reportLoaderQueueBizLogic.retrieve(ReportLoaderQueue.class
				.getName(), Constants.SYSTEM_IDENTIFIER, new Long(reportQueueId));
		return reportQueueList;
	}

}
