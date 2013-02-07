
package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;

/**
 *Common tab action class defined for forwarding the action to the
 * corresponding Add action from the simpleQueryInterface.jsp file.
 * @author nitesh_marwaha
 *
 */
public class CommonTabAction extends CatissueBaseAction
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
	@Override
	public ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		System.out.println("CommonTabAction  TOKEN : "+request.getParameter("org.apache.struts.taglib.html.TOKEN"));
		System.out.println("CommonTabAction  SESSION TOKEN : " + request.getSession().getAttribute("org.apache.struts.action.TOKEN"));
		final String page = request.getParameter(Constants.PAGE_OF);
		if (page == null)
		{
			return mapping.findForward(Constants.SUCCESS);
		}
		return mapping.findForward(page);
	}
}
