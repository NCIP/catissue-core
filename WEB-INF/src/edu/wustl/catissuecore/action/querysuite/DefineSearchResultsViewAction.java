package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
/**
 * This is a action class to load Define Search Results View screen.
 * @author deepti_shelar
 *
 */
public class DefineSearchResultsViewAction extends BaseAction
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
		request.setAttribute(Constants.CURRENT_PAGE, Constants.DEFINE_RESULTS_VIEW);
		CategorySearchForm searchForm = (CategorySearchForm) form;
		searchForm = QueryModuleUtil.setDefaultSelections(searchForm);
		return mapping.findForward(Constants.SUCCESS);
	}
}

