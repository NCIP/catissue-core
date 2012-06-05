/*
 * Created on Sep 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.util.global.CommonServiceLocator;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResetPasswordAction extends XSSSupportedAction//extends BaseAction
{

	/**
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return ActionForward : ActionForward
	 */
	//@Override
	public ActionForward executeXSS/*executeAction*/(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionFwd = null;
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final String resetPasswordToken = request.getParameter(Constants.RESET_PASSWORD_TOKEN);
		if(request.getRequestURL()!=null)
		{
			CommonServiceLocator.getInstance().setAppURL(request.getRequestURL().toString());
		}
		if(Constants.PAGE_OF_RESET_PASSWORD.equalsIgnoreCase(pageOf))
		{
			String paramValue = request.getParameter(Constants.RESET_PASSWORD_TOKEN);
			if (paramValue != null)
			{
				request.setAttribute(Constants.RESET_PASSWORD_TOKEN, paramValue);
			}
		}
		final UserBizLogic objUserBizlogic = new UserBizLogic();
		if (objUserBizlogic.isPasswordTokenValid(resetPasswordToken))
		{
			request.setAttribute(Constants.PAGE_OF, pageOf);
			request.setAttribute(Constants.RESET_PASSWORD_TOKEN, resetPasswordToken);
			actionFwd=mapping.findForward(Constants.PAGE_OF_RESET_PASSWORD);
		}
		else
		{
			request.setAttribute(Constants.STATUS_MESSAGE_KEY, Constants.PASSWORD_TOKEN_NOT_EXISTS);
			actionFwd = mapping.findForward(Constants.FAILURE);
		}
		return actionFwd;
	}
	
	public void preExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) 
	{
		/*String pageOf=request.getParameter(Constants.PAGEOF);
		if(request.getRequestURL()!=null)
		{
			CommonServiceLocator.getInstance().setAppURL(request.getRequestURL().toString());
		}
		if(Constants.PAGE_OF_RESET_PASSWORD.equalsIgnoreCase(pageOf))
		{
			setAttributeFromParameter(request, Constants.RESET_PASSWORD_TOKEN);
		}*/
	}
}
