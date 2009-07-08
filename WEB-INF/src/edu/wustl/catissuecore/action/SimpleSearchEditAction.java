/**
 *
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

/**
 * Action class to forward the request to the proper action based on
 *  the Alias Name of the object to be edited in Simple Search.
 * @author prafull_kadam
 * Patch ID: SimpleSearchEdit_4
 * Description: Action class for forwarding request to the proper Action.
 */
public class SimpleSearchEditAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param response
	 *            object of HttpServletResponse
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final Long identifier = Long.valueOf(request.getParameter(Constants.SYSTEM_IDENTIFIER));
		request.setAttribute(Constants.SYSTEM_IDENTIFIER, identifier);

		final String aliasName = request.getParameter(Constants.TABLE_ALIAS_NAME);
		//		request.setAttribute(Constants.TABLE_ALIAS_NAME, aliasName);

		final String pageOf = Variables.aliasAndPageOfMap.get(aliasName);
		request.setAttribute(Constants.PAGE_OF, pageOf);

		Logger.out.debug("identifier:" + identifier + " TABLE_ALIAS_NAME:" + aliasName + ":PAGEOF:"
				+ pageOf);
		return mapping.findForward(pageOf);
	}

}
