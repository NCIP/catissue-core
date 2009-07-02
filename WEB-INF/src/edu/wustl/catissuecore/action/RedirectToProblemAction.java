/**
 * This class is used to handle any exception condition and redirect to report problem page.
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author mandar_deshmukh
 *
 */
public class RedirectToProblemAction extends Action
{

	/**
	 * RedirectToProblemAction().
	 */
	public RedirectToProblemAction()
	{
	}

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		request.setAttribute(Constants.EXCEPTION_OCCURED, Boolean.TRUE);
		request.setAttribute("operation", "add");
		return mapping.findForward(Constants.SUCCESS);
	}

}
