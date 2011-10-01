
package edu.wustl.catissuecore.action;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.catissuecore.actionForm.CasLoginForm;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.factory.utils.UserUtility;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.migrator.MigrationState;
import edu.wustl.processor.LoginProcessor;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Roles;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 *
 * @author sagar_baldwa
 *
 */
public class CasLoginAction extends XSSSupportedAction
{

	/**
	 * Common Logger for Login Action.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(CasLoginAction.class);

	/**
	 *
	 */
	@Override
	public ActionForward executeXSS(final ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response)
	{
		String forwardTo = Constants.FAILURE;
		if (form == null)
		{
			CasLoginAction.LOGGER.debug("Form is Null");
			forwardTo = Constants.FAILURE;
		}
		else
		{
			try
			{
				CasLoginAction.LOGGER.info("Inside Login Action, Just before validation");
				forwardTo = processUserLogin(form, request);
				String pageOf = request.getParameter(Constants.PAGE_OF);
				if (pageOf != null && pageOf.equals(CDMSIntegrationConstants.SCG))
				{
					forwardTo=Constants.SCG;
				}
			}
			catch (final Exception ex)
			{
				CasLoginAction.LOGGER.error("Exception: " + ex.getMessage(), ex);
				cleanSession(request);
				handleError(request, "errors.incorrectLoginIDPassword");
				forwardTo = Constants.FAILURE;
			}
		}
		return mapping.findForward(forwardTo);
	}

	private String processUserLogin(final ActionForm form, final HttpServletRequest request)
			throws CatissueException, NamingException, ApplicationException
	{
		String forwardTo = null;
		final CasLoginForm casLoginForm = (CasLoginForm) form;
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.setLoginName(casLoginForm.getLoginName());
		edu.wustl.domain.LoginResult loginResult = null;
		try
		{
			loginResult = LoginProcessor.processUserLogin(loginCredentials);
			CatissueLoginProcessor.auditLogin(loginResult, casLoginForm.getLoginName(), request);
		}
		catch (AuthenticationException authExp)
		{
			LOGGER.error(authExp.getMessage(), authExp);
			authExp.printStackTrace();
		}

		if (MigrationState.NEW_IDP_USER.equals(loginResult.getMigrationState()))
		{
			forwardTo = setSignUpPageAttributes(request, casLoginForm);
		}
		else
		{
			forwardTo = validateUser(request, loginResult);
		}

		if (MigrationState.TO_BE_MIGRATED.equals(loginResult.getMigrationState()))
		{
			forwardTo = Constants.SUCCESS;
		}
		return forwardTo;
	}

	private String setSignUpPageAttributes(final HttpServletRequest request,
			final CasLoginForm loginForm) throws NamingException
	{
		final String firstName = "Fname";
		final String lastName = "Lname";
		request.setAttribute(edu.wustl.wustlkey.util.global.Constants.WUSTLKEY, loginForm
				.getLoginName());
		request.setAttribute(edu.wustl.wustlkey.util.global.Constants.FIRST_NAME, firstName);
		request.setAttribute(edu.wustl.wustlkey.util.global.Constants.LAST_NAME, lastName);
		return edu.wustl.wustlkey.util.global.Constants.WASHU;
	}

	/**
	 *
	 * @param request
	 */
	private void cleanSession(final HttpServletRequest request)
	{
		final HttpSession prevSession = request.getSession();
		if (prevSession != null)
		{
			prevSession.invalidate();
		}
	}

	/**
	 *
	 * @param request
	 * @param loginResult
	 * @return
	 * @throws ApplicationException
	 * @throws CatissueException
	 */
	private String validateUser(final HttpServletRequest request, final LoginResult loginResult)
			throws ApplicationException, CatissueException
	{
		final User validUser = CatissueLoginProcessor.getUser(loginResult.getAppLoginName());
		return performAdminChecks(request, validUser, loginResult);
	}

	/**
	 *
	 * @param request
	 * @param validUser
	 * @param loginResult
	 * @return
	 * @throws CatissueException
	 */
	private String performAdminChecks(final HttpServletRequest request, final User validUser,
			final LoginResult loginResult) throws CatissueException
	{
		String forwardTo = edu.wustl.wustlkey.util.global.Constants.PAGE_NON_WASHU;

		final HttpSession session = request.getSession(true);

		final String userRole = CatissueLoginProcessor.getUserRole(validUser);

		final SessionDataBean sessionData = initCacheAndSessionData(request, validUser, session,
				userRole);

		final String validRole = CatissueLoginProcessor.getForwardToPageOnLogin(userRole);
		if (isUserHasRole(validRole))
		{
			forwardTo = handleUserWithNoRole(request, session);
		}
		String result = Constants.SUCCESS;
		// do not check for first time login and login expiry for wustl key user
		// and migrated washu users
		if (isToCheckForPasswordExpiry(loginResult, validRole))
		{
			result = CatissueLoginProcessor.isPasswordExpired(validUser);
		}
		LOGGER.info("Result: " + result);
		if (!result.equals(Constants.SUCCESS))
		{
			forwardTo = handleChangePassword(request, session, sessionData, result);
		}
		LOGGER.info("forwardToPage: " + forwardTo);
		return forwardTo;
	}

	private String handleChangePassword(final HttpServletRequest request,
			final HttpSession session, final SessionDataBean sessionData, final String result)
	{
		String forwardTo;
		handleCustomMessage(request, result);
		session.setAttribute(Constants.SESSION_DATA, null);
		session.setAttribute(Constants.TEMP_SESSION_DATA, sessionData);
		request.setAttribute(Constants.PAGE_OF, Constants.PAGE_OF_CHANGE_PASSWORD);
		forwardTo = Constants.ACCESS_DENIED;
		return forwardTo;
	}

	private boolean isToCheckForPasswordExpiry(final LoginResult loginResult, final String validRole)
	{
		return !MigrationState.MIGRATED.equals(loginResult.getMigrationState())
				&& !MigrationState.NEW_IDP_USER.equals(loginResult.getMigrationState())
				&& !MigrationState.TO_BE_MIGRATED.equals(loginResult.getMigrationState())
				&& !(isUserHasRole(validRole));
	}

	private boolean isUserHasRole(final String validRole)
	{
		return validRole != null && validRole.contains(Constants.PAGE_OF_SCIENTIST);
	}

	private String handleUserWithNoRole(final HttpServletRequest request, final HttpSession session)
	{
		String forwardTo;
		handleError(request, "errors.noRole");
		session.setAttribute(Constants.SESSION_DATA, null);
		forwardTo = Constants.FAILURE;
		return forwardTo;
	}

	private SessionDataBean initCacheAndSessionData(final HttpServletRequest request,
			final User validUser, final HttpSession session, final String userRole)
			throws CatissueException
	{
		initPrivilegeCache(validUser);
		CasLoginAction.LOGGER.info(">>>>>>>>>>>>> SUCESSFUL LOGIN A <<<<<<<<< ");
		final String ipAddress = request.getRemoteAddr();
		final SessionDataBean sessionData = setSessionDataBean(validUser, ipAddress, userRole);
		LOGGER.info("creating session data bean " + sessionData);
		CasLoginAction.LOGGER.debug("CSM USer ID ....................... : "
				+ validUser.getCsmUserId());
		session.setAttribute(Constants.SESSION_DATA, sessionData);
		session.setAttribute(Constants.USER_ROLE, UserUtility.getRoleId(validUser));
		return sessionData;
	}

	private void initPrivilegeCache(final User validUser) throws CatissueException
	{
		try
		{
			PrivilegeManager.getInstance().getPrivilegeCache(validUser.getLoginName());
		}
		catch (final SMException exception)
		{
			LOGGER.debug(exception);
			throw new CatissueException(exception);
		}
	}

	private boolean isAdminUser(final String userRole)
	{
		boolean adminUser;
		//if (userRole.equalsIgnoreCase(Constants.ADMIN_USER))
		if (Constants.ADMIN_USER.equalsIgnoreCase(userRole))
		{
			adminUser = true;
		}
		else
		{
			adminUser = false;
		}
		return adminUser;
	}

	/**
	 *
	 * @param validUser
	 * @param ipAddress
	 * @param userRole
	 * @return
	 * @throws CatissueException
	 */
	private SessionDataBean setSessionDataBean(final User validUser, final String ipAddress,
			final String userRole) throws CatissueException
	{
		final SessionDataBean sessionData = new SessionDataBean();
		sessionData.setAdmin(isAdminUser(UserUtility.getRoleId(validUser)));//validUser.getRoleId()));
		sessionData.setUserName(validUser.getLoginName());
		sessionData.setIpAddress(ipAddress);
		sessionData.setUserId(validUser.getId());
		sessionData.setFirstName(validUser.getFirstName());
		sessionData.setLastName(validUser.getLastName());
		sessionData.setCsmUserId(validUser.getCsmUserId().toString());

		setSecurityParamsInSessionData(sessionData, userRole);
		return sessionData;
	}

	/**
	 *
	 * @param sessionData
	 * @param userRole
	 * @throws CatissueException
	 */
	private void setSecurityParamsInSessionData(final SessionDataBean sessionData,
			final String userRole) throws CatissueException
	{
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
	 *
	 * @param request
	 * @param errorKey
	 */
	private void handleError(final HttpServletRequest request, final String errorKey)
	{
		final ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey));
		// Report any errors we have discovered
		if (!errors.isEmpty())
		{
			saveErrors(request, errors);
		}
	}

	/**
	 *
	 * @param request
	 * @param errorMsg
	 */
	private void handleCustomMessage(final HttpServletRequest request, final String errorMsg)
	{
		final ActionMessages msg = new ActionMessages();
		final ActionMessage msgs = new ActionMessage(errorMsg);
		msg.add(ActionErrors.GLOBAL_ERROR, msgs);
		if (!msg.isEmpty())
		{
			saveMessages(request, msg);
		}
	}
}