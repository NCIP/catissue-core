
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Roles;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * <p>
 * Title:
 * </p>
 *<p>
 * Description:
 * </p>
 *<p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 *<p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 *
 * @author Aarti Sharma
 *@version 1.0
 */
public class LoginAction extends Action
{
	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(LoginAction.class);

	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * drop down fields in Institute.jsp Page.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws IOException
	 *             I/O exception
	 * @throws ServletException
	 *             servlet exception
	 * @return value for ActionForward object
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		if (form == null)
		{
			Logger.out.debug("Form is Null");
			return (mapping.findForward(Constants.FAILURE));
		}

		HttpSession prevSession = request.getSession();
		if (prevSession != null) prevSession.invalidate();

		LoginForm loginForm = (LoginForm) form;
		Logger.out.info("Inside Login Action, Just before validation");

		String loginName = loginForm.getLoginName();
		// String password = PasswordManager.encode(loginForm.getPassword());

		try
		{
			User validUser = getUser(loginName);
			String password = loginForm.getPassword();
			if (validUser != null)
			{
				boolean loginOK = SecurityManagerFactory.getSecurityManager().login(loginName,
						password);
				if (loginOK)
				{
					/*
					 * PrivilegeCache privilegeCache =
					 * PrivilegeManager.getInstance()
					 * .getPrivilegeCache(loginName);
					 */

					logger.info(">>>>>>>>>>>>> SUCESSFUL LOGIN A <<<<<<<<< ");
					HttpSession session = request.getSession(true);

					Long userId = validUser.getId();
					String ipAddress = request.getRemoteAddr();

					SessionDataBean sessionData = new SessionDataBean();

					boolean adminUser = false;

					if (validUser.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
					{
						adminUser = true;
					}

					sessionData.setAdmin(adminUser);
					sessionData.setUserName(loginName);
					sessionData.setIpAddress(ipAddress);
					sessionData.setUserId(userId);
					sessionData.setFirstName(validUser.getFirstName());
					sessionData.setLastName(validUser.getLastName());
					logger.debug("CSM USer ID ....................... : "
							+ validUser.getCsmUserId());
					sessionData.setCsmUserId(validUser.getCsmUserId().toString());
					session.setAttribute(Constants.SESSION_DATA, sessionData);
					session.setAttribute(Constants.USER_ROLE, validUser.getRoleId());
					IFactory factory = AbstractFactoryConfig.
					getInstance().getBizLogicFactory();
					UserBizLogic userBizLogic = (UserBizLogic) factory
							.getBizLogic(Constants.USER_FORM_ID);

					String result = userBizLogic.checkFirstLoginAndExpiry(validUser);

					setSecurityParamsInSessionData(validUser, sessionData);

					String validRole = getForwardToPageOnLogin
					(validUser.getCsmUserId().longValue());
					if (validRole != null && validRole.contains(Constants.PAGE_OF_SCIENTIST))
					{
						ActionErrors errors = new ActionErrors();
						errors.add(ActionErrors.GLOBAL_ERROR,
								new ActionError("errors.noRole"));
						saveErrors(request, errors);
						session.setAttribute(Constants.SESSION_DATA, null);
						return mapping.findForward(Constants.FAILURE);
					}

					if (!result.equals(Constants.SUCCESS))
					{
						// ActionError changed to ActionMessage
						ActionMessages messages = new ActionMessages();
						messages.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(result));
						saveMessages(request, messages);

						session.setAttribute(Constants.SESSION_DATA, null);
						session.setAttribute(Constants.TEMP_SESSION_DATA, sessionData);
						request.setAttribute(Constants.PAGE_OF,
								Constants.PAGE_OF_CHANGE_PASSWORD);
						return mapping.findForward(Constants.ACCESS_DENIED);
					}

					// Determining Role Name- Start
					/**
					 * Name : Virender Mehta Reviewer: Sachin Lale Bug ID: 3842
					 * Patch ID: 3842_1 See also: 3842_2 Description: Default
					 * views based on user login If user login as admin Default
					 * view is set Administrator Tab If user login as Technicion
					 * Default view is set as Collection Protocol Base View
					 * Under Biospecimen Tab If user login as Supervisor Default
					 * view is set as Collection Protocol Base View Under
					 * Biospecimen Tab If user login as Scientist Default view
					 * is set as Admin Advance Search unser Search tab
					 * Forwarding to default page depending on user role
					 */
					String forwardToPage = Constants.SUCCESS;
					//getForwardToPageOnLogin(validUser.getCsmUserId().longValue
					// ());
					return (mapping.findForward(forwardToPage));
				}
				else
				{
					logger.info("User " + loginName
							+ " Invalid user. Sending back to the login Page");
					handleError(request, "errors.incorrectLoginNamePassword");
					return (mapping.findForward(Constants.FAILURE));
				}
			} // if valid user
			else
			{
				logger.info("User " + loginName + " Invalid user. Sending back to the login Page");
				handleError(request, "errors.incorrectLoginNamePassword");
				return (mapping.findForward(Constants.FAILURE));
			} // invalid user
		}
		catch (Exception e)
		{
			logger.info("Exception: " + e.getMessage(), e);
			handleError(request, "errors.incorrectLoginNamePassword");
			return (mapping.findForward(Constants.FAILURE));
		}
	}

	/**
	 * To set the Security Parameters in the given SessionDataBean object
	 * depending upon the role of the user.
	 *
	 * @param validUser
	 *            reference to the User.
	 * @param sessionData
	 *            The reference to the SessionDataBean object.
	 * @throws SMException : SMException
	 */
	private void setSecurityParamsInSessionData(User validUser, SessionDataBean sessionData)
			throws SMException
	{
		String userRole = SecurityManagerFactory.getSecurityManager().getRoleName(
				validUser.getCsmUserId());
		if (userRole != null
				&& (userRole.equalsIgnoreCase(Roles.ADMINISTRATOR) || userRole
						.equals(Roles.SUPERVISOR)))
		{
			sessionData.setSecurityRequired(false);
		}
		else
		{
			sessionData.setSecurityRequired(true);
		}
	}

	/**
	 * Patch ID: 3842_2 This function will take LoginID for user and return the
	 * appropriate default page. Get role from securitymanager and modify the
	 * role name where first character is in upper case and rest all are in
	 * lower case add prefix "pageOf" to modified role name and forward to that
	 * page.
	 *
	 * @param loginId : loginId
	 * @return String : String
	 * @throws SMException : SMException
	 */

	private String getForwardToPageOnLogin(Long loginId) throws SMException
	{
		ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
		String roleName = securityManager.getRoleName(loginId);
		String modifiedRolename = "";
		if (roleName == null || roleName.equals(""))
		{
			modifiedRolename = "pageOfScientist";
		}
		else
		{
			modifiedRolename = "pageOfAdministrator";
		}
		// String modifiedRolename =
		// roleName.substring(0,1).toUpperCase()+roleName
		// .substring(1,roleName.length()).toLowerCase();
		return (modifiedRolename);
	}
	/**
	 *
	 * @param request : request
	 * @param errorKey : errorKey
	 */
	private void handleError(HttpServletRequest request, String errorKey)
	{
		ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey));
		// Report any errors we have discovered
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
		}
	}
	/**
	 *
	 * @param loginName : loginName
	 * @return User : User
	 * @throws BizLogicException : BizLogicException
	 */
	private User getUser(String loginName) throws BizLogicException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
		String[] whereColumnName = {"activityStatus", "loginName"};
		String[] whereColumnCondition = {"=", "="};
		String[] whereColumnValue = {Status.ACTIVITY_STATUS_ACTIVE.toString(), loginName};

		List users = userBizLogic.retrieve(User.class.getName(), whereColumnName,
				whereColumnCondition, whereColumnValue, Constants.AND_JOIN_CONDITION);

		if (users != null && !users.isEmpty())
		{
			User validUser = (User) users.get(0);
			return validUser;
		}
		return null;
	}
}