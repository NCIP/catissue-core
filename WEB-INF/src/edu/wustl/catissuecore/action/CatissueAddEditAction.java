package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.BaseAddEditAction;
import edu.wustl.common.action.CommonAddAction;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.action.CommonEdtAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class CatissueAddEditAction extends CommonAddEditAction
{

	private static final Logger LOGGER = Logger.getCommonLogger(CatissueAddEditAction.class);
	
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		request.getSession().setAttribute("displayMsg", "false");
		LOGGER.info("in execute method");
		BaseAddEditAction addEditAction;
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		ActionForward actionfwd;
		try
		{
				if (abstractForm.isAddOperation())
				{
					addEditAction = new CommonAddAction();
				}
				else
				{
					addEditAction = new CommonEdtAction();
				}
				actionfwd = addEditAction.executeXSS(mapping, abstractForm, request, response);
		}
		catch (ApplicationException applicationException)
		{
			LOGGER.error("Common Add/Edit failed.." + applicationException.getCustomizedMsg());

			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = new ActionError("errors.item",
					applicationException.getCustomizedMsg());
			actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
			saveErrors(request, actionErrors);

			actionfwd = mapping.findForward(Constants.FAILURE);
		}
		return actionfwd;
	}
}
