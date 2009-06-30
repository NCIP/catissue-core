/**
 * <p>Title: ConflictDetailsAction Class>
 * <p>Description:	Initialization action for conflict details view
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
  * @version 1.00
  * @author kalpana_thakur
  * @date 9/18/2007
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictDetailsForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
/**
 * 
 * @author renuka_bajpai
 *
 */
/**
 * @author renuka_bajpai
 *
 */
public class ConflictDetailsAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in ConflictView.jsp Page.
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 * */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String reportQueueId = request.getParameter(Constants.REPORT_ID);

		ConflictDetailsForm conflictDetailsForm = (ConflictDetailsForm) form;
		conflictDetailsForm.setReportQueueId(reportQueueId);

		request.setAttribute(Constants.REPORT_ID, reportQueueId);
		return mapping.findForward(Constants.SUCCESS);

	}

}
