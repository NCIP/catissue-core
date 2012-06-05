
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.exception.ApplicationException;

public class AssignNewPasswordAction extends XSSSupportedAction
{

	/**
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionFwd = null;
		try
		{
			UserForm userForm = (UserForm) form;
			final String pageOf = request.getParameter(Constants.PAGE_OF);
			String resetPasswordToken = (String) request
					.getParameter(Constants.RESET_PASSWORD_TOKEN);
			String paramValue = request.getParameter(Constants.RESET_PASSWORD_TOKEN);
			if (paramValue != null)
			{
				request.setAttribute(Constants.RESET_PASSWORD_TOKEN, paramValue);
			}
			request.setAttribute(Constants.PAGE_OF, pageOf);
			final UserBizLogic objUserBizlogic = new UserBizLogic();
			objUserBizlogic.updatePasswordUsingToken(resetPasswordToken,userForm.getNewPassword(),pageOf);
			String message = "password.update.success";
			request.setAttribute(Constants.STATUS_MESSAGE_KEY, message);

			actionFwd = mapping.findForward(Constants.SUCCESS);
		}
		catch (ApplicationException applicationException)
		{
			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = new ActionError("errors.item",
					applicationException.getCustomizedMsg());
			actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
			saveErrors(request, actionErrors);

			actionFwd = mapping.findForward(Constants.FAILURE);
		}
		return actionFwd;

	}

}
