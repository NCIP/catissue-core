/*
 * Created on Sep 21, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * @author santosh_chandak
 *
 * This class is used to set request/session data after successful password change.
 */
public class SetSessionDataForUpdatePasswordAction extends BaseAction
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
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// PASSWORD_CHANGE_IN_SESSION is set to indicate that password has been changed in this session.
		request.getSession().setAttribute(Constants.PASSWORD_CHANGE_IN_SESSION, new Boolean(true));
		SessionDataBean sessionDataBean=(SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		String loginName="";
		if(sessionDataBean!=null)
		{
			loginName=sessionDataBean.getUserName();
		}

		/**
		 *  TEMP_SESSION_DATA is set when user is forced to change the password, at that time
		 *  SESSION_DATA is set to Null. Here we set the SESSION_DATA with TEMP_SESSION_DATA, if
		 *  TEMP_SESSION_DATA is not null.
		 */
		if (request.getSession().getAttribute(Constants.TEMP_SESSION_DATA) != null)
		{
			request.getSession().setAttribute(Constants.SESSION_DATA,
					request.getSession().getAttribute(Constants.TEMP_SESSION_DATA));
			request.getSession().setAttribute(Constants.TEMP_SESSION_DATA, null);
		}
		final UserBizLogic ubizLogic=new UserBizLogic();
		String migrationState=ubizLogic.getMigrationStatus(loginName);
		if(!migrationState.equals(""))
		{
			return mapping.findForward(Constants.HOME);
		}
		return mapping.findForward(Constants.SUCCESS);
	}

}
