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
            auditLogin(loginResult, loginCredentials.getLoginName(), request);
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
			User user = getUser(loginName);
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
    public static void auditLogin(final LoginResult loginResult, final String loginName,
            final HttpServletRequest request) throws CatissueException
    {
        HibernateDAO dao = null;
        try
        {
            dao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(
                    CommonServiceLocator.getInstance().getAppName()).getDAO();

            Long userId = null;
            Long csmUserId = null;
            dao.openSession(null);

            final String userIdhql = "select user.id, user.csmUserId from edu.wustl.catissuecore.domain.User user where "
                    + "user.activityStatus= "
                    + "'"
                    + Status.ACTIVITY_STATUS_ACTIVE.toString()
                    + "' and UPPER(user.loginName) =" + "'" + loginName.toUpperCase() + "'";

            final List UserIds = dao.executeQuery(userIdhql);
            if (UserIds != null && !UserIds.isEmpty())
            {
                final Object[] obj = (Object[]) UserIds.get(0);
                userId = (Long) obj[0];
                csmUserId = (Long) obj[1];
            }

            final LoginDetails loginDetails = new LoginDetails(userId, csmUserId, request.getRemoteAddr());

            boolean isLoginSuccessful;
            if (loginResult == null)
            {
                isLoginSuccessful = false;
            }
            else
            {
                isLoginSuccessful = loginResult.isAuthenticationSuccess();
            }
            if(userId != null)
            {
	            String hql = "select loginAudit.userLoginId, loginAudit.sourceId, loginAudit.ipAddress, loginAudit.isLoginSuccessful,loginAudit.timestamp " +
	        				"from edu.wustl.common.domain.LoginEvent loginAudit where loginAudit.userLoginId = ? order by loginAudit.timestamp desc";
	            List columnValueBeans = new ArrayList();
	//            ColumnValueBean bean = new ColumnValueBean(loginDetails.getUserLoginId());
	            columnValueBeans.add(loginDetails.getUserLoginId());
	        		try { 
	        			List result = dao.executeQuery(hql,0,1, columnValueBeans);
	        			if(result != null && result.size() > 0)
	        			{
		        			Object[] obj = (Object[])result.get(0);
		        			loginResult.setLastLoginActivityStatus(Boolean.valueOf(obj[3].toString()));
		        			Timestamp timestamp = (Timestamp)obj[4];
		        			SimpleDateFormat sdf = new SimpleDateFormat(ApplicationProperties.getValue("date.pattern.timestamp"));
		        			String dateVal = sdf.format(timestamp);
		        			loginResult.setLastLoginTime(dateVal);
	        			}
	        		}
	        		catch (ApplicationException e1) {
	        			// TODO Auto-generated catch block
	        			e1.printStackTrace();
	        		}
            }
            (dao).auditLoginEvents(isLoginSuccessful, loginDetails);
            if(!isLoginSuccessful)
            {
            	lockUserAccount(loginName,dao,loginResult);
            }
            
            dao.commit();
        }
        catch (final ApplicationException exception)
        {
            LOGGER.debug(exception);
            throw new CatissueException(exception);
        }
        finally
        {
            closeHibernateSession(dao);
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
	private static Boolean lockAccount(User user, HibernateDAO dao, LoginResult loginResult) throws BizLogicException
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
		
//		LoginDetails details = loginDetailsColl.get(0);
//		loginResult.setLastLoginActivityStatus(details.isLoginSuccessful());
//		loginResult.setLastLoginTime(details.getLoginTimeStamp().toString());
		int lastSuccessIndex = getLastSucessIndex(loginDetailsColl);
		loginResult.setRemainingAttemptsIndex(maxAttempts-(lastSuccessIndex+1));
		if (loginDetailsColl.size() >= maxAttempts)
		{
			
			if (lastSuccessIndex == -1 || lastSuccessIndex+1 >= maxAttempts)
			{
				Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
				params.put("0", new NamedQueryParam(DBTypes.STRING,Constants.ACTIVITY_STATUS_LOCKED));
				params.put("1", new NamedQueryParam(DBTypes.LONG,user.getId()));
				try {
					dao.executeUpdateWithNamedQuery("updateUserActivityStatus", params);
					return Boolean.TRUE;
				} catch (DAOException e) {
					LOGGER.error(e);
					LOGGER.error("Error while updating user activity status");
					throw new BizLogicException(e);
				}
			}
		}
		return false;
	}

	/**
	 * Gets the last sucess index.
	 *
	 * @param loginDetailsColl the login details coll
	 *
	 * @return the last sucess index
	 */
	public static int getLastSucessIndex(List<LoginDetails> loginDetailsColl)
	{	
	   int index = 0;		
		for (LoginDetails loginDetails : loginDetailsColl) {
			if (loginDetails.isLoginSuccessful()) {
				break;
			}
			index++;
		}
		return index;
	}
}
