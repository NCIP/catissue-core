package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.util.querysuite.QueryModuleSearchQueryUtil;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.IQuery;
/**
 * 
 * @author deepti_shelar
 *
 */
public class OpenDecisionMakingPageAction extends BaseAction
{
	/**
	 * This method loads define results jsp.
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		CategorySearchForm actionForm = (CategorySearchForm)form;
		HttpSession session = request.getSession();
		IQuery query = (IQuery)session.getAttribute(AppletConstants.QUERY_OBJECT);
		String noOfResults = (String)session.getAttribute(Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS);
		String option = actionForm.getOptions();
		QueryModuleSearchQueryUtil QMSearchQuery = new QueryModuleSearchQueryUtil(request, query);
		
		if(option != null)
		{
			boolean isRulePresentInDag = QueryModuleUtil.checkIfRulePresentInDag(query) ;
			if (isRulePresentInDag)
			{
				QMSearchQuery.searchQuery(option);
			}
			return mapping.findForward(Constants.VIEW_ALL_RECORDS);
		}
		ActionErrors errors = new ActionErrors();
		ActionError addMsg = new ActionError("query.decision.making.message", noOfResults,Variables.maximumTreeNodeLimit);
		errors.add(ActionErrors.GLOBAL_ERROR, addMsg);
		saveErrors(request, errors);
		return mapping.findForward(Constants.SUCCESS);
	}
}
