
package edu.wustl.catissuecore.action.querysuite;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.querysuite.SaveQueryForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleConstants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
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
		/**
		 * Name: Abhishek Mehta
		 * Reviewer Name : Deepti 
		 * Bug ID: 5661
		 * Patch ID: 5661_2
		 * See also: 1-4 
		 * Description : Loading save and update query page
		 */
		
		boolean isDagEmpty = true;
		if (queryObject != null)
		{
			boolean isShowAll = request.getParameter(Constants.SHOW_ALL) == null ? false : true;
			GenerateHtmlForAddLimitsBizLogic htmlGenerator = new GenerateHtmlForAddLimitsBizLogic();
			Map<Integer,ICustomFormula> customFormulaIndexMap = new HashMap<Integer, ICustomFormula>();
			String htmlContents = htmlGenerator.getHTMLForSavedQuery(queryObject, isShowAll,
					Constants.SAVE_QUERY_PAGE,customFormulaIndexMap);
			request.getSession().setAttribute(QueryModuleConstants.CUSTOM_FORMULA_INDEX_MAP, customFormulaIndexMap);
			request.setAttribute(Constants.HTML_CONTENTS, htmlContents);
			String showAllLink = isShowAll
					? Constants.SHOW_SELECTED_ATTRIBUTE
					: Constants.SHOW_ALL_ATTRIBUTE;
			request.setAttribute(Constants.SHOW_ALL_LINK, showAllLink);
			if (!isShowAll)
				request.setAttribute(Constants.SHOW_ALL, Constants.TRUE);
			target = Constants.SUCCESS;
			if (queryObject.getId() != null && queryObject instanceof ParameterizedQuery)
			{
				SaveQueryForm savedQueryForm = (SaveQueryForm)form;
				savedQueryForm.setDescription(((ParameterizedQuery)queryObject).getDescription());
				savedQueryForm.setTitle(((ParameterizedQuery)queryObject).getName());
			}
			
			IConstraints c = queryObject.getConstraints();
			for(IExpression exp: c)
			{
				isDagEmpty = false;
			}
		}
		if(isDagEmpty)
		{
			// Handle null query 
			target = Constants.SUCCESS;
			String errorMsg = ApplicationProperties.getValue("query.noLimit.error");
			setActionError(request, errorMsg);
			request.setAttribute(Constants.IS_QUERY_SAVED,Constants.IS_QUERY_SAVED);
		}
		String errorMessage = (String) request.getSession().getAttribute("errorMessageForEditQuery");
		if (errorMessage != null)
		{
			setActionError(request, errorMessage);
			request.getSession().removeAttribute("errorMessageForEditQuery");
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
