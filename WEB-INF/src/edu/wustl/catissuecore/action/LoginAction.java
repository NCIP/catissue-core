
package edu.wustl.catissuecore.action;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import krishagni.catissueplus.util.DAOUtil;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.dao.UserDAO;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.migrator.MigrationState;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Roles;
import edu.wustl.security.privilege.PrivilegeManager;

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
public class LoginAction extends XSSSupportedAction {

	/**
	 * Common Logger for Login Action.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(LoginAction.class);

	String loginAttempts = XMLPropertyHandler.getValue(Constants.LOGIN_FAILURE_ATTEMPTS_LIMIT);

	/**
	 * Overrides the execute method of Action class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @return value for ActionForward object
	 */
	@Override
	public ActionForward executeXSS(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forwardTo = Constants.FAILURE;
		if (form == null) {
			LoginAction.LOGGER.debug("Form is Null");
			forwardTo = Constants.FAILURE;
		}
		else {
			try {
				cleanSession(request);
				LoginAction.LOGGER.info("Inside Login Action, Just before validation");
				forwardTo = processUserLogin(form, request);
				String pageOf = request.getParameter(Constants.PAGE_OF);
				if (pageOf != null && (pageOf.equals(CDMSIntegrationConstants.SCG) || pageOf.equals("pageOfDownload"))) {
					request.setAttribute(Constants.PAGE_OF, pageOf);
					forwardTo = pageOf;
				}

				String redirectTo = request.getParameter("redirectTo");
				if (redirectTo != null) {
					ActionForward forward = new ActionForward(redirectTo);
					forward.setRedirect(true);
					return forward;
				}

			}
			catch (final Exception ex) {
				LoginAction.LOGGER.error("Exception: " + ex.getMessage(), ex);
				cleanSession(request);
				int loginAttemptCount = Integer.valueOf(loginAttempts) - 1;
				handleError(request, "errors.incorrectLoginIDPassword", loginAttemptCount);
				forwardTo = Constants.FAILURE;
			}
		}
		return mapping.findForward(forwardTo);
	}

	private String processUserLogin(final ActionForm form, final HttpServletRequest request) throws CatissueException,
			NamingException, ApplicationException, AuthenticationException {
		String forwardTo;

		final LoginForm loginForm = (LoginForm) form;
		final LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.setLoginName(loginForm.getLoginName());
		loginCredentials.setPassword(loginForm.getPassword());
		//        LoginProcessor.authenticate(loginCredentials);
		final edu.wustl.domain.LoginResult loginResult;
		if (isUserLocked(loginForm.getLoginName())) {
			ActionErrors errors = new ActionErrors();
			errors.add(
					ActionErrors.GLOBAL_ERROR,
					new ActionError("login.account.locked", XMLPropertyHandler.getValue("contact.number"), XMLPropertyHandler
							.getValue("email.administrative.emailAddress")));
			saveErrors(request, errors);
			return Constants.FAILURE;
		}
		if (isRequestFromClinportal(request)) {
			loginResult = new LoginResult();
			loginResult.setAppLoginName(loginCredentials.getLoginName());
			loginResult.setAuthenticationSuccess(true);
			loginResult.setMigrationState(MigrationState.MIGRATED);
		}
		else {
			loginResult = CatissueLoginProcessor.processUserLogin(request, loginCredentials);
		}

		if (loginResult.isAuthenticationSuccess()) {
			if (MigrationState.NEW_IDP_USER.equals(loginResult.getMigrationState())) {
				IDPAuthManager authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
						edu.wustl.wustlkey.util.global.Constants.WUSTL_IDP);
				if (authManager.authenticate(loginCredentials)) {
					forwardTo = setSignUpPageAttributes(request, loginForm);
				}
				else {
					LoginAction.LOGGER.info("User " + loginForm.getLoginName() + " Invalid user. Sending back to the login Page");
					handleError(request, "errors.incorrectLoginIDPassword", loginResult.getRemainingAttemptsIndex());
					forwardTo = Constants.FAILURE;
				}
			}
			else {
				forwardTo = validateUser(request, loginResult);
			}

			if (!Constants.FAILURE.equals(forwardTo) && MigrationState.TO_BE_MIGRATED.equals(loginResult.getMigrationState())
					&& !"access_denied".equals(forwardTo)) {
				forwardTo = Constants.SUCCESS;
			}
		}
		else {
			LoginAction.LOGGER.info("User " + loginForm.getLoginName() + " Invalid user. Sending back to the login Page");
			if (loginResult.isAccountLocked()) {
				ActionErrors errors = new ActionErrors();
				errors.add(
						ActionErrors.GLOBAL_ERROR,
						new ActionError("login.account.locked", XMLPropertyHandler.getValue("contact.number"), XMLPropertyHandler
								.getValue("email.administrative.emailAddress")));
				saveErrors(request, errors);
				//        		handleError(request, "login.account.locked",null);
			}
			else {

				if (MigrationState.MIGRATED.equals(loginResult.getMigrationState())
						&& loginForm.getLoginName().equals(loginResult.getAppLoginName())) {
					LoginAction.LOGGER
							.info("User " + loginForm.getLoginName() + " Migrated user. Sending back to the login Page");
					handleError(request, "app.migrateduser", null);
				}
				else {
					handleError(request, "errors.incorrectLoginIDPassword", loginResult.getRemainingAttemptsIndex());
				}
			}
			forwardTo = Constants.FAILURE;
		}
		return forwardTo;
	}

	private Boolean isUserLocked(String loginName) throws BizLogicException {
		Boolean isUserLocked = Boolean.FALSE;
		UserDAO userDAO = new UserDAO();
		HibernateDAO dao = null;
		try {
			dao = DAOUtil.openDAOSession(null);
			if (userDAO.getUserIDFromLoginName(dao, loginName, Constants.ACTIVITY_STATUS_LOCKED) != null) {
				isUserLocked = Boolean.TRUE;
			}
		}
		catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (BizLogicException e) {
			LOGGER.error(e);
		}
		finally {
			DAOUtil.closeDAOSession(dao);
		}
		return isUserLocked;
	}

	private boolean isRequestFromClinportal(final HttpServletRequest request) {
		return request.getParameter(CDMSIntegrationConstants.IS_COMING_FROM_CLINPORTAL) != null
				&& !(CDMSIntegrationConstants.DOUBLE_QUOTE.equals(request
						.getParameter(CDMSIntegrationConstants.IS_COMING_FROM_CLINPORTAL)))
				&& Boolean.valueOf(request.getParameter(CDMSIntegrationConstants.IS_COMING_FROM_CLINPORTAL));
	}

	/**
	 * This method will set attributes like firstname, lastname in user sign up
	 * page.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param loginForm
	 *            loginform object
	 * @param authManager
	 *            AuthenticationManager object
	 * @return String
	 * @throws NamingException
	 *             NamingException
	 */
	private String setSignUpPageAttributes(final HttpServletRequest request, final LoginForm loginForm)
			throws NamingException {
		// final Map<String, String> userAttrs =
		// authManager.getUserAttributes(loginForm.getLoginName());
		final String firstName = "Fname";// userAttrs.get(Constants.FIRSTNAME);
		final String lastName = "Lname";// userAttrs.get(Constants.LASTNAME);
		request.setAttribute(edu.wustl.wustlkey.util.global.Constants.WUSTLKEY, loginForm.getLoginName());
		request.setAttribute(edu.wustl.wustlkey.util.global.Constants.FIRST_NAME, firstName);
		request.setAttribute(edu.wustl.wustlkey.util.global.Constants.LAST_NAME, lastName);
		return edu.wustl.wustlkey.util.global.Constants.WASHU;
	}

	/**
	 * This method will clean session.
	 *
	 * @param request
	 *            object of HttpServletRequest
	 */
	private void cleanSession(final HttpServletRequest request) {
		final HttpSession prevSession = request.getSession();
		if (prevSession != null) {
			prevSession.invalidate();
		}
	}

	/**
	 * This method checks the validity of logged in user and perform necessary
	 * action after validating.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param request
	 *            object of HttpServletRequest
	 * @param loginName
	 *            login Name
	 * @param loginForm
	 *            Login Form
	 * @param loginResult
	 * @return value for ActionForward object
	 * @throws ApplicationException
	 *             object of ApplicationException
	 * @throws CatissueException
	 */
	private String validateUser(final HttpServletRequest request, final LoginResult loginResult)
			throws ApplicationException, CatissueException {
		String forwardTo = Constants.FAILURE;
		final User validUser = CatissueLoginProcessor.getUser(loginResult.getAppLoginName());
		if (validUser == null) {
			LoginAction.LOGGER.debug("User " + loginResult.getAppLoginName()
					+ " Invalid user. Sending back to the login Page");
			int loginAttemptCount = Integer.valueOf(loginAttempts) - 1;
			handleError(request, "errors.incorrectLoginIDPassword", loginAttemptCount);
			handleError(request, "errors.incorrectLoginIDPassword", loginAttemptCount);
			forwardTo = Constants.FAILURE;
		}
		else {
			forwardTo = performAdminChecks(request, validUser, loginResult);
		}
		return forwardTo;
	}

	/**
	 * This method will create object of privilege cache for logged in user and
	 * create SessionDataBean object.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param request
	 *            object of HttpServletRequest
	 * @param validUser
	 *            User object
	 * @param loginForm
	 *            Login Form
	 * @param loginResult
	 * @return value for ActionForward object
	 * @throws ApplicationException
	 *             Object of ApplicationException
	 * @throws CatissueException
	 */
	private String performAdminChecks(final HttpServletRequest request, final User validUser,
			final LoginResult loginResult) throws CatissueException {
		String forwardTo = edu.wustl.wustlkey.util.global.Constants.PAGE_NON_WASHU;

		HttpSession session = request.getSession(true);

		final String userRole = CatissueLoginProcessor.getUserRole(validUser);

		final SessionDataBean sessionData = initCacheAndSessionData(request, validUser, loginResult, userRole, session);

		final String validRole = CatissueLoginProcessor.getForwardToPageOnLogin(userRole);
		if (isUserHasRole(validRole)) {
			forwardTo = handleUserWithNoRole(request, session);
		}

		String result = Constants.SUCCESS;
		// do not check for first time login and login expiry for wustl key user
		// and migrated washu users
		if (isToCheckForPasswordExpiry(loginResult, validRole)) {
			result = CatissueLoginProcessor.isPasswordExpired(validUser);
		}

		LOGGER.info("Result: " + result);
		if (!result.equals(Constants.SUCCESS)) {
			forwardTo = handleChangePassword(request, session, sessionData, result);
		}

		LOGGER.info("forwardToPage: " + forwardTo);
		return forwardTo;
	}

	private String handleChangePassword(final HttpServletRequest request, final HttpSession session,
			final SessionDataBean sessionData, final String result) {
		String forwardTo;
		handleCustomMessage(request, result);
		session.setAttribute(Constants.SESSION_DATA, null);
		session.setAttribute(Constants.TEMP_SESSION_DATA, sessionData);
		request.setAttribute(Constants.PAGE_OF, Constants.PAGE_OF_CHANGE_PASSWORD);
		forwardTo = Constants.ACCESS_DENIED;
		return forwardTo;
	}

	private boolean isToCheckForPasswordExpiry(final LoginResult loginResult, final String validRole) {
		return !MigrationState.MIGRATED.equals(loginResult.getMigrationState())
				&& !MigrationState.NEW_IDP_USER.equals(loginResult.getMigrationState()) && !(isUserHasRole(validRole));
	}

	private boolean isUserHasRole(final String validRole) {
		return validRole != null && validRole.contains(Constants.PAGE_OF_SCIENTIST);
	}

	private String handleUserWithNoRole(final HttpServletRequest request, final HttpSession session) {
		String forwardTo;
		handleError(request, "errors.noRole", null);
		session.setAttribute(Constants.SESSION_DATA, null);
		forwardTo = Constants.FAILURE;
		return forwardTo;
	}

	private SessionDataBean initCacheAndSessionData(final HttpServletRequest request, final User validUser,
			final LoginResult loginResult, final String userRole, HttpSession session) throws CatissueException {
		initPrivilegeCache(validUser);

		LoginAction.LOGGER.info(">>>>>>>>>>>>> SUCESSFUL LOGIN A <<<<<<<<< ");
		final String ipAddress = request.getRemoteAddr();
		final SessionDataBean sessionData = setSessionDataBean(validUser, ipAddress, userRole);
		LOGGER.info("creating session data bean " + sessionData);

		LoginAction.LOGGER.debug("CSM USer ID ....................... : " + validUser.getCsmUserId());
		session.setAttribute(Constants.SESSION_DATA, sessionData);
		session.setAttribute("datePattern", ApplicationProperties.getValue("date.pattern"));
		session.setAttribute("uiDatePattern", ApplicationProperties.getValue("ui.date.pattern"));

		String auditDatePattern = ApplicationProperties.getValue("date.pattern").toLowerCase();

		session.setAttribute("auditDtPattern", auditDatePattern.replace("yyyy", "yy"));
		session.setAttribute(Constants.USER_ROLE, validUser.getRoleId());
		String loginAttempt = loginResult.getLastLoginActivityStatus() == null || loginResult.getLastLoginActivityStatus()
				? "successful"
				: "unsuccessful";
		session.setAttribute("lastLoginAttempt", loginAttempt);
		session.setAttribute("lastLoginTimeStamp", loginResult.getLastLoginTime());
		return sessionData;
	}

	private void initPrivilegeCache(final User validUser) throws CatissueException {
		try {
			PrivilegeManager.getInstance().getPrivilegeCache(validUser.getLoginName());
		}
		catch (final SMException exception) {
			LOGGER.debug(exception);
			throw new CatissueException(exception);
		}
	}

	private boolean isAdminUser(final String userRole) {
		boolean adminUser;
		if (userRole.equalsIgnoreCase(Constants.ADMIN_USER)) {
			adminUser = true;
		}
		else {
			adminUser = false;
		}
		return adminUser;
	}

	/**
	 * This method will createSessionDataBeanObject.
	 *
	 * @param validUser
	 *            existing user
	 * @param ipAddress
	 *            IP address of the system
	 * @param adminUser
	 *            true/false
	 * @return sessionData
	 * @throws CatissueException
	 */
	private SessionDataBean setSessionDataBean(final User validUser, final String ipAddress, final String userRole)
			throws CatissueException {
		final SessionDataBean sessionData = new SessionDataBean();
		sessionData.setAdmin(isAdminUser(validUser.getRoleId()));
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
	 * To set the Security Parameters in the given SessionDataBean object
	 * depending upon the role of the user.
	 *
	 * @param validUser
	 *            reference to the User.
	 * @param sessionData
	 *            The reference to the SessionDataBean object.
	 * @param userRole2
	 * @throws SMException
	 *             : SMException
	 */
	private void setSecurityParamsInSessionData(final SessionDataBean sessionData, final String userRole)
			throws CatissueException {
		if (userRole != null && (userRole.equalsIgnoreCase(Roles.ADMINISTRATOR) || userRole.equals(Roles.SUPERVISOR))) {
			sessionData.setSecurityRequired(false);
		}
		else {
			sessionData.setSecurityRequired(true);
		}
	}

	/**
	 *
	 * @param request
	 *            : request
	 * @param errorKey
	 *            : errorKey
	 */
	private void handleError(final HttpServletRequest request, final String errorKey, Object object) {
		final ActionErrors errors = new ActionErrors();
		if (object != null) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey, object));
		}
		else {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey));
		}
		// Report any errors we have discovered
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
	}

	/**
	 * This method is for showing Custom message.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param errorMsg
	 *            Error message
	 */
	private void handleCustomMessage(final HttpServletRequest request, final String errorMsg) {
		final ActionMessages msg = new ActionMessages();
		final ActionMessage msgs = new ActionMessage(errorMsg);
		msg.add(ActionErrors.GLOBAL_ERROR, msgs);
		if (!msg.isEmpty()) {
			saveMessages(request, msg);
		}
	}

}