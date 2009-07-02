
package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;

/**
 *Common tab action class defined for forwarding the action to the
 * corresponding Add action from the simpleQueryInterface.jsp file.
 * @author nitesh_marwaha
 *
 */
public class CommonTabAction extends Action
{
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
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 * @return ActionForward : ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		String page = (String) request.getParameter(Constants.PAGE_OF);
		if (page == null)
		{
			return mapping.findForward(Constants.SUCCESS);
		}
		return mapping.findForward(page);
	}
}
