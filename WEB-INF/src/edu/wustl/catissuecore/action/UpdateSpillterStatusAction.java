
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

/*
 * Name : Kapil Kaveeshwar
 * Reviewer: Sachin Lale
 * Bug ID: Menu_Splitter
 * Patch ID: Menu_Splitter_3
 * See also: All Menu_Splitter
 * Description: Maintains the last state of menu splitter in the session of the user. Next time user
 * navigates to the page the state can be preserved. This part of the code stores the menu status in 
 * session. This is invoked by splitter.js via AJAX call.
 */
public class UpdateSpillterStatusAction extends BaseAction
{

	private transient Logger logger = Logger.getCommonLogger(UpdateSpillterStatusAction.class);

	protected ActionForward executeAction(ActionMapping actionMap, ActionForm actionForm,
			HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		//Reads the menu splitter status from request	
		String menuStatus = req.getParameter(Constants.SPLITTER_STATUS_REQ_PARAM);
		logger.debug("Menu Status " + menuStatus);

		//updates the splitter status in session scope of user
		HttpSession session = req.getSession();
		if (session != null)
		{
			session.setAttribute(Constants.SPLITTER_STATUS_REQ_PARAM, menuStatus);
		}
		return null;
	}
}