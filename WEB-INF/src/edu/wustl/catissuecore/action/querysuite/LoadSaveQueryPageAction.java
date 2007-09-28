
package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.util.global.ApplicationProperties;

public class LoadSaveQueryPageAction extends BaseAction
{

	@Override
	/**
	 * This action loads all the conditions from the query.   
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		IQuery queryObject = (IQuery) request.getSession().getAttribute(
				AppletConstants.QUERY_OBJECT);
		String target = Constants.FAILURE;
		if (queryObject != null)
		{

			boolean isShowAll = request.getParameter(Constants.SHOW_ALL) == null ? false : true;
			GenerateHtmlForAddLimitsBizLogic htmlGenerator = new GenerateHtmlForAddLimitsBizLogic();
			String htmlContents = htmlGenerator.getHTMLForSavedQuery(queryObject, isShowAll,
					Constants.SAVE_QUERY_PAGE);
			request.setAttribute(Constants.HTML_CONTENTS, htmlContents);
			String showAllLink = isShowAll
					? Constants.SHOW_SELECTED_ATTRIBUTE
					: Constants.SHOW_ALL_ATTRIBUTE;
			request.setAttribute("showAllLink", showAllLink);
			if (!isShowAll)
				request.setAttribute(Constants.SHOW_ALL, "true");
			target = Constants.SUCCESS;
		}
		else
		{
			// Handle null query 
			String errorMsg = ApplicationProperties.getValue("query.noLimit.error");
			setActionError(request, errorMsg);

		}
		return mapping.findForward(target);

	}

	/**
	 * This method sets the error action 
	 * @param request
	 * @param errorMessage
	 */
	private void setActionError(HttpServletRequest request, String errorMessage)
	{
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError("errors.item", errorMessage);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		saveErrors(request, errors);
	}

}
