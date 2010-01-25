/**
 * <p>Title: ParticipantAction Class>
 * <p>Description:  This class initializes the fields in the ReportProblem webpage.</p>
 * Copyright:  Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * This class initializes the fields in the ReportProblem webpage.
 * @author gautam_shetty
 */
public class ReportProblemAction extends XSSSupportedAction
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
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		final Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if (obj != null)
		{
			final SessionDataBean sessionData = (SessionDataBean) obj;
			final ReportedProblemForm reportedProblemForm = (ReportedProblemForm) form;
			if (reportedProblemForm != null)
			{
				reportedProblemForm.setNameOfReporter(sessionData.getLastName() + ", "
						+ sessionData.getFirstName());
				reportedProblemForm.setFrom(sessionData.getUserName());
			}
		}

		//Mandar 17-Apr-06 : 1667:- Application URL
		CommonServiceLocator.getInstance().setAppURL(request.getRequestURL().toString());

		//Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);

		//Sets the operation attribute to be used in the Add/Edit Problem Page.
		request.setAttribute(Constants.OPERATION, operation);

		if (operation.equals(Constants.EDIT))
		{
			final Long prevIdentifier = (Long) request.getAttribute(Constants.PREVIOUS_PAGE);
			request.setAttribute(Constants.PREVIOUS_PAGE, prevIdentifier);
			final Long nextIdentifier = (Long) request.getAttribute(Constants.NEXT_PAGE);
			request.setAttribute(Constants.NEXT_PAGE, nextIdentifier);
		}
		request.setAttribute(Constants.ACTIVITYSTATUSLIST,
				Constants.REPORTED_PROBLEM_ACTIVITY_STATUS_VALUES);
		return mapping.findForward(Constants.SUCCESS);
	}

}
