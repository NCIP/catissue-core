package edu.wustl.catissuecore.util;

import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.factory.utils.UserUtility;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.util.Utility;
import edu.wustl.processor.LoginProcessor;
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
 */
public class SSOcaTissueCommonLoginUtility
{

    /**
     * Common Logger for Login Action.
     */
    private static final Logger LOGGER = Logger.getCommonLogger(SSOcaTissueCommonLoginUtility.class);
    
    
    public LoginCredentials processUserAuthentication(LoginCredentials loginCredentials, HttpServletRequest request,
    		String loginName)
            throws CatissueException, NamingException, ApplicationException, AuthenticationException
    {
    	LoginProcessor.authenticate(loginCredentials);
        return loginCredentials;
    }
    
    public CommonLoginInfoUtility processUserAuthorization(LoginCredentials loginCredentials, String loginName,
    		HttpServletRequest request) throws NamingException, ApplicationException, CatissueException    
    {
    	CommonLoginInfoUtility loginInfoUtility = new CommonLoginInfoUtility();
    	final edu.wustl.domain.LoginResult loginResult = CatissueLoginProcessor.processUserLogin(request,
                loginCredentials);
        if (loginResult.isAuthenticationSuccess())
        {
            if (MigrationState.NEW_IDP_USER.equals(loginResult.getMigrationState()))
            {
                loginInfoUtility.setForwardTo(setSignUpPageAttributes(request, loginName));
            }
            else
            {
                loginInfoUtility = validateUser(request, loginResult);
            }

            if (MigrationState.TO_BE_MIGRATED.equals(loginResult.getMigrationState()) && 
                    !Constants.ACCESS_DENIED.equals(loginInfoUtility.getForwardTo()))
            {
            	loginInfoUtility.setForwardTo(Constants.SUCCESS);
            }
        }
        else
        {
        	// Grid Grouper Integration: the following has been transferred from
			// casLoginView.jsp.
            String userName = loginResult.getAppLoginName();
            try {
			List idpsList = Utility.getConfiguredIDPNVB(false);
			if (idpsList.size() > 0) {
				UserDetails userDetails;
				
					userDetails = LoginProcessor
							.getUserDetails(userName);
				
				if (userDetails == null) {
					if (!LoginProcessor.isUserPresentInApplicationDB(userName)) {
						loginInfoUtility.setForwardTo("GridGrouperUser");
					}
				}
			}
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				LOGGER.info(e);
				throw new CatissueException(e);
			} catch (AuthFileParseException e) {
				// TODO Auto-generated catch block
				LOGGER.info(e);
				throw new CatissueException(e);
			}
			
			if(loginInfoUtility.getForwardTo()!=null && !loginInfoUtility.getForwardTo().equals("GridGrouperUser"))
			{
	            SSOcaTissueCommonLoginUtility.LOGGER.info("User " + loginName
	                    + " Invalid user. Sending back to the login Page");
	            if (MigrationState.MIGRATED.equals(loginResult.getMigrationState())
	                    && loginName.equals(loginResult.getAppLoginName()))
	            {
	                SSOcaTissueCommonLoginUtility.LOGGER.info("User " + loginName
	                        + " Migrated user. Sending back to the login Page");
	                loginInfoUtility.setActionErrors(handleError(request, "app.migrateduser"));
	            }
	            else
	            {
	                handleError(request, "errors.incorrectLoginIDPassword");
	                loginInfoUtility.setActionErrors(handleError(request, "app.migrateduser"));
	            }
	            loginInfoUtility.setForwardTo(Constants.FAILURE);
			}
            
         
        }
        return loginInfoUtility;
    }

    private boolean isRequestFromClinportal(final HttpServletRequest request)
    {
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
    private String setSignUpPageAttributes(final HttpServletRequest request, final String loginName)
            throws NamingException
    {
        // final Map<String, String> userAttrs =
        // authManager.getUserAttributes(loginForm.getLoginName());
        final String firstName = "Fname";// userAttrs.get(Constants.FIRSTNAME);
        final String lastName = "Lname";// userAttrs.get(Constants.LASTNAME);
        request.setAttribute(edu.wustl.wustlkey.util.global.Constants.WUSTLKEY, loginName);
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
    private void cleanSession(final HttpServletRequest request)
    {
        final HttpSession prevSession = request.getSession();
        if (prevSession != null)
        {
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
    private CommonLoginInfoUtility validateUser(final HttpServletRequest request, final LoginResult loginResult)
            throws ApplicationException, CatissueException
    {
    	CommonLoginInfoUtility infoUtility = new CommonLoginInfoUtility();
        String forwardTo = Constants.FAILURE;
        final User validUser = CatissueLoginProcessor.getUser(loginResult.getAppLoginName());
        if (validUser == null)
        {
            SSOcaTissueCommonLoginUtility.LOGGER.debug("User " + loginResult.getAppLoginName()
                    + " Invalid user. Sending back to the login Page");
            infoUtility.setActionErrors(handleError(request, "errors.incorrectLoginIDPassword"));
            infoUtility.setForwardTo(forwardTo);
        }
        else
        {
            infoUtility = performAdminChecks(request, validUser, loginResult);
        }        
        return infoUtility;
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
    private CommonLoginInfoUtility performAdminChecks(final HttpServletRequest request, final User validUser,
            final LoginResult loginResult) throws CatissueException, ApplicationException
    {
        String forwardTo = edu.wustl.wustlkey.util.global.Constants.PAGE_NON_WASHU;
        CommonLoginInfoUtility infoUtility = new CommonLoginInfoUtility();
        infoUtility.setForwardTo(forwardTo);

        final HttpSession session = request.getSession(true);

        final String userRole = CatissueLoginProcessor.getUserRole(validUser);

        final SessionDataBean sessionData = initCacheAndSessionData(request, validUser, session, userRole);

        final String validRole = CatissueLoginProcessor.getForwardToPageOnLogin(userRole);
        if (isUserHasRole(validRole))
        {
            infoUtility = handleUserWithNoRole(request, session);
        }
        else
        {
	        String result = CatissueLoginProcessor.isPasswordExpired(validUser);
		    if (result.equals(Constants.SUCCESS))
	        {
		    	final UserBizLogic ubizlogic=new UserBizLogic();
		    	if(MigrationState.DO_NOT_MIGRATE.toString().equals(ubizlogic.getMigrationStatus(validUser.getLoginName())))
		    	{
		    		forwardTo=edu.wustl.wustlkey.util.global.Constants.PAGE_NON_WASHU;
		    	}
		    	else
		    	{
		    		forwardTo = Constants.SUCCESS;
		    	}
	        }
		    else if(!result.equals(Constants.SUCCESS))
		    {
		    	handleChangePassword(request, session, sessionData, result, infoUtility);
		    }
		    	
        }                
        LOGGER.info("forwardToPage: " + forwardTo);
        return infoUtility;
    }

    private void handleChangePassword(final HttpServletRequest request, final HttpSession session,
            final SessionDataBean sessionData, final String result, CommonLoginInfoUtility infoUtility)
    {    	
        String forwardTo;
        infoUtility.setActionMessages(handleCustomMessage(request, result));
        session.setAttribute(Constants.SESSION_DATA, null);
        session.setAttribute(Constants.TEMP_SESSION_DATA, sessionData);
        request.setAttribute(Constants.PAGE_OF, Constants.PAGE_OF_CHANGE_PASSWORD);
        forwardTo = Constants.ACCESS_DENIED;
        infoUtility.setForwardTo(forwardTo);        
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

    private CommonLoginInfoUtility handleUserWithNoRole(final HttpServletRequest request, final HttpSession session)
    {
    	CommonLoginInfoUtility infoUtility = new CommonLoginInfoUtility();
        String forwardTo;        
        session.setAttribute(Constants.SESSION_DATA, null);
        forwardTo = Constants.FAILURE;
        infoUtility.setForwardTo(forwardTo);
        infoUtility.setActionErrors(handleError(request, "errors.noRole"));
        return infoUtility;
    }

    private SessionDataBean initCacheAndSessionData(final HttpServletRequest request, final User validUser,
            final HttpSession session, final String userRole) throws CatissueException
    {
        initPrivilegeCache(validUser);

        SSOcaTissueCommonLoginUtility.LOGGER.info(">>>>>>>>>>>>> SUCESSFUL LOGIN A <<<<<<<<< ");
        final String ipAddress = request.getRemoteAddr();

        final SessionDataBean sessionData = setSessionDataBean(validUser, ipAddress, userRole);
        LOGGER.info("creating session data bean " + sessionData);

        SSOcaTissueCommonLoginUtility.LOGGER.debug("CSM USer ID ....................... : " + validUser.getCsmUserId());
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
            throws CatissueException
    {
        final SessionDataBean sessionData = new SessionDataBean();
        sessionData.setAdmin(isAdminUser(UserUtility.getRoleId(validUser)));
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
            throws CatissueException
    {
        if (userRole != null
                && (userRole.equalsIgnoreCase(Roles.ADMINISTRATOR) || userRole.equals(Roles.SUPERVISOR)))
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
     *            : request
     * @param errorKey
     *            : errorKey
     */
    private ActionErrors handleError(final HttpServletRequest request, final String errorKey)
    {
        final ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid","Username, Password"));
        // Report any errors we have discovered
        return errors;        
    }

    /**
     * This method is for showing Custom message.
     *
     * @param request
     *            HttpServletRequest
     * @param errorMsg
     *            Error message
     */
    private ActionMessages handleCustomMessage(final HttpServletRequest request, final String errorMsg)
    {
        final ActionMessages msg = new ActionMessages();
        final ActionMessage msgs = new ActionMessage(errorMsg);
        msg.add(ActionErrors.GLOBAL_ERROR, msgs);
        return msg;        
    }
}