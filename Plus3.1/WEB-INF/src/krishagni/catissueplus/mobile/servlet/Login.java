package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.processor.LoginProcessor;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Roles;
import edu.wustl.security.privilege.PrivilegeManager;


public class Login  extends HttpServlet{
	
	/**
	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		doPost(req, res);
	}

	
	/**
	 * This method is used to download files saved in database.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{

		org.json.JSONObject returnedJObject= new org.json.JSONObject();
		
		try{
			
			//edu.wustl.catissuecore.util.SSOcaTissueCommonLoginUtility loginUtility = new edu.wustl.catissuecore.util.SSOcaTissueCommonLoginUtility();
			LoginCredentials loginCredentials = new LoginCredentials();
			returnedJObject.put("login", "failure");
			
	        loginCredentials.setLoginName( request.getParameter("loginName"));
	        loginCredentials.setPassword( request.getParameter("password"));
	        cleanSession(request);
	        LoginResult loginResult;
	    	 loginResult = CatissueLoginProcessor.processUserLogin(request,
	                 loginCredentials);
	    	 LoginProcessor.authenticate(loginCredentials);

	    	 
	    	  if (loginResult.isAuthenticationSuccess())
	          {
	    		  validateUser(request,loginResult);
	  	    	HttpSession session = request.getSession(true);
	  	    	session.getAttribute(Constants.SESSION_DATA);
	  	    	edu.wustl.common.beans.SessionDataBean sessionData =	(edu.wustl.common.beans.SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
	  	    	returnedJObject.put("firstname",sessionData.getFirstName());
	  	    	returnedJObject.put("lastname", sessionData.getLastName());
	  	    	returnedJObject.put("login", "success");
	  	    
	          }
	    		
    	
    	//response.getWriter().flush();
    	
		}catch(Exception e){
			
		}
		
		response.setContentType("application/json");
    	response.getWriter().write(returnedJObject.toString());

		
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
            throws ApplicationException, CatissueException
    {
        String forwardTo = "";
        final User validUser = CatissueLoginProcessor.getUser(loginResult.getAppLoginName());
        if (validUser != null)
        {
            performAdminChecks(request, validUser, loginResult);
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
            final LoginResult loginResult) throws CatissueException
    {
        String forwardTo = edu.wustl.wustlkey.util.global.Constants.PAGE_NON_WASHU;

        final HttpSession session = request.getSession(true);

        final String userRole = CatissueLoginProcessor.getUserRole(validUser);

        final SessionDataBean sessionData = initCacheAndSessionData(request, validUser, session, userRole);

        final String validRole = CatissueLoginProcessor.getForwardToPageOnLogin(userRole);
     /*   if (isUserHasRole(validRole))
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

        LOGGER.info("forwardToPage: " + forwardTo);*/
        return forwardTo;
    }
    
    private SessionDataBean initCacheAndSessionData(final HttpServletRequest request, final User validUser,
            final HttpSession session, final String userRole) throws CatissueException
    {
        initPrivilegeCache(validUser);

        final String ipAddress = request.getRemoteAddr();

        final SessionDataBean sessionData = setSessionDataBean(validUser, ipAddress, userRole);
        session.setAttribute(Constants.SESSION_DATA, sessionData);
        session.setAttribute(Constants.USER_ROLE, validUser.getRoleId());
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
            throw new CatissueException(exception);
        }
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
    
    private boolean isAdminUser(final String userRole)
    {
        boolean adminUser;
        if (userRole.equalsIgnoreCase(Constants.ADMIN_USER))
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


}
