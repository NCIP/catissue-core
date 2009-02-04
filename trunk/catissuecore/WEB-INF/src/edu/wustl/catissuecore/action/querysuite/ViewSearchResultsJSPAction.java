
package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * This is a action called whern user clicks on search button from addlimits .jsp. The result data lists are already
 * stored in session through an applet action ViewSearchResultsAction. This class just forwards the control to ViewSearchResults.jsp.
 * @author deepti_shelar
 *
 */
public class ViewSearchResultsJSPAction extends BaseAction
{

	/**
	 * This method loads the data required for ViewSearchResults.jsp
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
		String nextOperation = actionForm.getNextOperation();
		if(nextOperation != null && nextOperation.equalsIgnoreCase(AppletConstants.SHOW_ERROR_PAGE))
		{
			return mapping.findForward(Constants.FAILURE);
		}
		return mapping.findForward(Constants.SUCCESS);
	}
}
