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
import edu.wustl.common.action.CommonEdtAction;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to Add/Edit data in the database.
 * @author atul_kaushal
 */
public class ReportedProblemAddEditAction extends XSSSupportedAction
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ReportedProblemAddEditAction.class);

	/**
	 * Overrides the execute method of Action class.
	 * Adds / Updates the data in the database.
	 * @param mapping	ActionMapping
	 * @param form	ActionForm
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @return ActionForward
	 * @throws Exception Exception
	 * */
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		LOGGER.info("in execute method");
		BaseAddEditAction addEditAction;
		AbstractActionForm abstractForm = (AbstractActionForm) form;
		ActionForward actionfwd;
		try
		{
			if ( !isTokenValid(request) ) 
			{
				actionfwd = mapping.findForward(Constants.FAILURE);
				ActionErrors actionErrors = new ActionErrors();
				ActionError actionError = new ActionError("errors.item","Invalid request for add/edit operation");
				actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
				saveErrors(request, actionErrors);
		    }
			else
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