
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

/**
 * @author renuka_bajpai
 *
 */
public class UpdateSpillterStatusAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(UpdateSpillterStatusAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param actionMap
	 *            object of ActionMapping
	 * @param actionForm
	 *            object of ActionForm
	 * @param req
	 *            object of HttpServletRequest
	 * @param res
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping actionMap, ActionForm actionForm,
			HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		//Reads the menu splitter status from request
		final String menuStatus = req.getParameter(Constants.SPLITTER_STATUS_REQ_PARAM);
		this.logger.debug("Menu Status " + menuStatus);

		//updates the splitter status in session scope of user
		final HttpSession session = req.getSession();
		if (session != null)
		{
			session.setAttribute(Constants.SPLITTER_STATUS_REQ_PARAM, menuStatus);
		}
		return null;
	}
}