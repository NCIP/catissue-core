
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Roles;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * This class is called when user clicks on BioSpecimen tab. Technicians can not
 * access cp based view. But other users can access , so this class checks if
 * the logged user is a technician .If yes then default page shown is specimen
 * search. For users other than technician default page is CP based view.
 *
 * @author deepti_shelar Bug id :4278 Patch id : 4278_1
 */
public class RoleBasedForwardAction extends BaseAction
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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String roleName = getRoleNameForUser(request);
		if (roleName.equalsIgnoreCase(Roles.TECHNICIAN))
		{
			return mapping.findForward(Constants.ACCESS_DENIED);
		}
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * @param request
	 *            HttpServletRequest
	 * @return String rolename
	 * @throws NumberFormatException
	 *             NumberFormatException
	 * @throws SMException
	 *             SMException
	 */
	private String getRoleNameForUser(HttpServletRequest request) throws NumberFormatException,
			SMException
	{
		SessionDataBean sessionData = getSessionData(request);
		Long userId = new Long(sessionData.getCsmUserId());
		ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
		String roleName = securityManager.getRoleName(userId);
		return roleName;
	}
}
