/**
 * 
 */
package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;


/**
 * @author supriya_dankh
 *
 */
public class QueryMessageAction extends BaseAction
{

	/**
	 * This method loads the html for addlimits section.This html is the replaced with the div data with the help of Ajax script.
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
		String message = (String)request.getSession().getAttribute(Constants.NO_MAIN_OBJECT_IN_QUERY);
		message = "<li><font color='blue' family='arial,helvetica,verdana,sans-serif'>"+message+"</font></li>";
		request.getSession().removeAttribute(Constants.NO_MAIN_OBJECT_IN_QUERY);
		response.setContentType("text/html");
		response.getWriter().write(message);
		return null;
	}

}
