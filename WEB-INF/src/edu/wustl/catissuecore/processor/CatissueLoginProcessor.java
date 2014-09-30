package edu.wustl.catissuecore.processor;




import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import krishagni.catissueplus.util.DAOUtil;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.dao.UserDAO;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.LoginDetails;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.migrator.MigrationState;
import edu.wustl.processor.LoginProcessor;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * This class contains the business login for login functionality.
 *
 * @author niharika_sharma
 */
public final class CatissueLoginProcessor extends LoginProcessor
{

    /**
     * Empty private constructor to avoid instantiation.
     */
    private CatissueLoginProcessor()
    {

    }

    /** Common Logger for catissue Login Processor. */
    private static final Logger LOGGER = Logger.getCommonLogger(CatissueLoginProcessor.class);

    /**
     * This method process the action of user login by authenticaton the user as
     * well as auditing the login attempt.
     *
     * @param request
     *            the request
     * @param loginCredentials
     *            the login credentials
     *
     * @return the login result
     *
     * @throws CatissueException
     *             the catissue exception
     */
    public static LoginResult processUserLogin(final HttpServletRequest request,
            final LoginCredentials loginCredentials) throws CatissueException
    {
        LoginResult loginResult = new LoginResult();
        loginResult.setAppLoginName(loginCredentials.getLoginName()); 
        loginResult.setAuthenticationSuccess(false);
        loginResult.setMigrationState(MigrationState.DO_NOT_MIGRATE);
        try
        {
        	LoginProcessor.authenticate(loginCredentials);
            loginResult = processUserLogin(loginCredentials);
            loginResult.setIsAccountLocked(Boolean.FALSE);
        }
        catch (final Exception exception)
        {
            LOGGER.debug(exception);
        }
        finally
        {
            manageLoginHistory(loginResult, request);
        }
        return loginResult;
    }
    
    /**
	 * Lock user account.
	 *
	 * @param loginName the login name
     * @param dao 
     * @param loginResult 
	 */
	public static void lockUserAccount(String loginName, HibernateDAO dao, LoginResult loginResult)
	{
		String loginAttempts = XMLPropertyHandler.getValue(Constants.LOGIN_FAILURE_ATTEMPTS_LIMIT);
		loginResult.setRemainingAttemptsIndex(Integer.valueOf(loginAttempts));
		try {			
			loginResult.setIsAccountLocked(Boolean.FALSE);
			User user = new UserDAO().getUserFromLoginName(dao,loginName);
			if (user != null) {
				if(lockAccount(user,dao,loginResult))
				{
					loginResult.setIsAccountLocked(Boolean.TRUE);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception obtain while locking the user account", e);
			throw new RuntimeException("Exception obtain while locking the user account", e);
		}
	}

    /**
     * This method performs the auditing of the login attempt.
     *
     * @param loginResult
     *            the login result
     * @param loginName
     *            the login name
     * @param request
     *            the request
     *
     * @throws CatissueException
     *             the catissue exception
     */
    public static void manageLoginHistory(LoginResult loginResult,
            final HttpServletRequest request) throws CatissueException
    {
        HibernateDAO hibernateDAO = null;
        try
        {
            hibernateDAO =(HibernateDAO) AppUtility.openDAOSession(null);

            UserDAO userDAO = new UserDAO();
            LoginDetails loginDetails = userDAO.getLoginDetails(hibernateDAO, loginResult.getAppLoginName(), request.getRemoteAddr());
                
            boolean isLoginSuccessful;
            isLoginSuccessful = loginResult.isAuthenticationSuccess();
            if(loginDetails!=null && loginDetails.getUserLoginId() != null)
            {
                userDAO.populateLoginResult(hibernateDAO,loginDetails,loginResult);
            }
            (hibernateDAO).auditLoginEvents(isLoginSuccessful, loginDetails);
            if(!isLoginSuccessful)
            {
            	lockUserAccount(loginResult.getAppLoginName(),hibernateDAO,loginResult);
            }
            
            hibernateDAO.commit();
        }
        catch (final ApplicationException exception)
        {
            LOGGER.debug(exception);
            throw new CatissueException(exception);
        }
        finally
        {
            closeHibernateSession(hibernateDAO);
        }
    }

    /**
     * This private method closes the hibernate session.
     *
     * @param dao
     *            the dao
     *
     * @throws CatissueException
     *             the catissue exception
     */
    private static void closeHibernateSession(final HibernateDAO dao) throws CatissueException
    {
        try
        {
            dao.closeSession();
        }
        catch (final DAOException exception)
        {
            LOGGER.debug(exception);
            throw new CatissueException(exception);
        }
    }

    /**
     * Patch ID: 3842_2 This function will take LoginID for user and return the
     * appropriate default page. Get role from securitymanager and modify the
     * role name where first character is in upper case and rest all are in
     * lower case add prefix "pageOf" to modified role name and forward to that
     * page.
     *
     * @param role
     *            the role
     *
     * @return String : String
     *
     * @throws SMException
     *             : SMException
     */

    public static String getForwardToPageOnLogin(final String role)
    {
        String modifiedRolename = "";
        if (role == null || role.equals(""))
        {
            modifiedRolename = "pageOfScientist";
        }
        else
        {
            modifiedRolename = "pageOfAdministrator";
        }
        return modifiedRolename;
    }

    /**
     * This method will check the expiry of the password.
     *
     * @param validUser
     *            Object of valid user
     *
     * @return result
     *
     * @throws CatissueException
     *             the catissue exception
     */
    public static String isPasswordExpired(final User validUser) throws CatissueException
    {
        String isPasswordExpired = Constants.SUCCESS;
        try
        {
            final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
            final UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
            // check for first time login and login expiry for both washu and
            // non-washu users
            isPasswordExpired = userBizLogic.checkFirstLoginAndExpiry(validUser);
        }
        catch (final BizLogicException exception)
        {
            LOGGER.debug(exception);
            throw new CatissueException(exception);
        }
        return isPasswordExpired;
    }

    /**
     * Gets the user role.
     *
     * @param validUser
     *            the valid user
     *
     * @return the user role
     *
     * @throws CatissueException
     *             the catissue exception
     */
    public static String getUserRole(final User validUser) throws CatissueException
    {
        String role = "";
        try
        {
            role = SecurityManagerFactory.getSecurityManager().getRoleName(validUser.getCsmUserId());
        }
        catch (final SMException exception)
        {
            LOGGER.debug(exception);
            throw new CatissueException(exception);
        }
        return role;
    }

    /**
     * This method fetches the catissue user from the database by making
     * bizlogic calls.
     *
     * @param loginName
     *            the login name
     *
     * @return the user
     *
     * @throws CatissueException
     *             the catissue exception
     */
    public static User getUser(final String loginName) throws CatissueException
    {
        User user = null;
        try
        {
            final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
            final UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
            // check for first time login and login expiry for both washu and
            // non-washu users
            user = userBizLogic.getUser(loginName);
        }
        catch (final BizLogicException exception)
        {
            LOGGER.debug(exception);
            throw new CatissueException(exception);
        }
        return user;
    }

    /**
	 * Lock account.
	 *
	 * @param user the user
     * @param dao 
     * @param loginResult 
	 *
	 * @throws DAOException the DAO exception
	 */
	private static Boolean lockAccount(User user, HibernateDAO dao, LoginResult loginResult) throws ApplicationException
	{
		int maxAttempts = 5;
		String loginAttempts = XMLPropertyHandler.getValue(Constants.LOGIN_FAILURE_ATTEMPTS_LIMIT);
		if (!Validator.isEmpty(loginAttempts) && AppUtility.isNumeric(loginAttempts))
		{
			maxAttempts = Integer.valueOf(loginAttempts);
		}
		LoginAuditManager loginAuditManager = new LoginAuditManager();
		List<LoginDetails> loginDetailsColl = loginAuditManager.getAllLoginDetailsForUser(user
				.getId(), maxAttempts);

		int remainingAttempt = getRemainingAttempt(loginDetailsColl,maxAttempts);
		loginResult.setRemainingAttemptsIndex(remainingAttempt);
		if (remainingAttempt==0)
		{
		   new UserDAO().updateUserActivityStatus(dao,user.getId(),Constants.ACTIVITY_STATUS_LOCKED);
		   return Boolean.TRUE;
		}
		return false;
	}

	private static int getRemainingAttempt(List<LoginDetails> loginDetailsColl,int maxAttempt){
	    for (LoginDetails loginDetails : loginDetailsColl) {
            if (loginDetails.isLoginSuccessful()) {
                break;
            }
	        maxAttempt--;
        }
	    if(maxAttempt>0){
	        maxAttempt--;
	    }
	    return maxAttempt;
	}
	/**
	 * Gets the last sucess index.
	 *
	 * @param loginDetailsColl the login details coll
	 *
	 * @return the last sucess index
	 */
//	public static int getLastSucessIndex(List<LoginDetails> loginDetailsColl)
//	{	
//	   int index = 0;		
//		for (LoginDetails loginDetails : loginDetailsColl) {
//			if (loginDetails.isLoginSuccessful()) {
//				break;
//			}
//			index++;
//		}
//		return index;
//	}
}
