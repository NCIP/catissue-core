/*
 * Created on Sep 1, 2005
 * This class is used to redirect the user to the Home / SignIn Page after session is timedOut.
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.global.Validator;

/**
 * @author mandar_deshmukh
 *
 * This class is used to redirect the user to the Home / SignIn Page after session is timedOut.
 */
public class ForwardAction extends BaseAction
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
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		String isDashboardVisible = request.getParameter("isDashboardVisible");
		if(!Validator.isEmpty(isDashboardVisible) && !Boolean.valueOf(isDashboardVisible))
		{
			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = new ActionError("access.execute.action.denied","");
			actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
			saveErrors(request, actionErrors);
		}
		if(request.getAttribute("org.apache.struts.action.ERROR")!=null){
			ActionErrors actionErrors = (ActionErrors)request.getAttribute("org.apache.struts.action.ERROR");
			saveErrors(request, actionErrors);
		}
		return (mapping.findForward(Constants.SUCCESS));
	}

}