/**
 * <p>
 * Title: UserEditProfileAction Class>
 * <p>
 * Description: This action class has been added for bug: 7978, because id has
 * been removed for request parameter from User Profile> Edit.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ravi Kumar
 * @version 1.00 Created on Aug 02, 2008
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.BaseAction;
import edu.wustl.common.action.CommonSearchAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.Constants;

/**
 * This class set id in request from session and call CommonSearchAction.
 */
public class UserEditProfileAction extends BaseAction
{

	/**
	 * Overrides the execute method in Action class.
	 *
	 * @param mapping
	 *            ActionMapping object
	 * @param form
	 *            ActionForm object
	 * @param request
	 *            HttpServletRequest object
	 * @param response
	 *            HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception
	 *             object
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		ActionForward actionForward = mapping.findForward("homePage");
		final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		if (sessionDataBean == null)
		{
			ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
			if (errors == null)
			{
				errors = new ActionErrors();
			}
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("app.session.timeout"));
			this.saveErrors(request, errors);
		}
		else
		{
			final CommonSearchAction commonSearchAction = new CommonSearchAction();
			request.setAttribute(Constants.SYSTEM_IDENTIFIER, sessionDataBean.getUserId());
			actionForward = commonSearchAction.execute(mapping, form, request, response);
		}
		return actionForward;
	}
}
