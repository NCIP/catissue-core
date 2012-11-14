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
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


@SuppressWarnings("deprecation")
public class SignUpUserAddAction extends XSSSupportedAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(SignUpUserAddAction.class);

	@Override
	protected ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
			LOGGER.info("in execute method");
			BaseAddEditAction addEditAction;
			AbstractActionForm abstractForm = (AbstractActionForm) form;
			ActionForward actionfwd=null;
			try
			{
				if (abstractForm.isAddOperation())
				{
					addEditAction = new CommonAddAction();
					actionfwd = addEditAction.executeXSS(mapping, abstractForm, request, response);
				}
				else
				{
					actionfwd = mapping.findForward(Constants.FAILURE);
				}
				
			}
			catch (ApplicationException applicationException)
			{
				LOGGER.error("SignUpUserAddAction failed.." + applicationException.getCustomizedMsg());

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
