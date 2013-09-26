/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.processor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.domain.LoginDetails;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
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
        LoginResult loginResult = null;
        try
        {
            loginResult = processUserLogin(loginCredentials);
        }
        catch (final Exception exception)
        {
            LOGGER.debug(exception);
        }
        finally
        {
            auditLogin(loginResult.isAuthenticationSuccess(), loginCredentials.getLoginName(), request.getRemoteAddr());
        }

        return loginResult;
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
    public static String auditLogin(boolean isLoginSuccessful, final String loginName,
            final String remoteAddress) throws CatissueException
    {
        HibernateDAO dao = null;
        String message = "";
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
                    + "' and user.loginName =" + "'" + loginName + "'";

            final List UserIds = dao.executeQuery(userIdhql);
            if (UserIds != null && !UserIds.isEmpty())
            {
                final Object[] obj = (Object[]) UserIds.get(0);
                userId = (Long) obj[0];
                csmUserId = (Long) obj[1];
            }

            final LoginDetails loginDetails = new LoginDetails(userId, csmUserId, remoteAddress);

//            boolean isLoginSuccessful;
//            if (loginResult == null)
//            {
//                isLoginSuccessful = false;
//            }
//            else
//            {
//                isLoginSuccessful = loginResult.isAuthenticationSuccess();
//            }

            (dao).auditLoginEvents(isLoginSuccessful, loginDetails);
            
            dao.commit();
            if(lockAccount(isLoginSuccessful,loginName))
            {
            	message = ApplicationProperties.getValue("error.account.locked");
            }
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
        return message;
    }

    private static Boolean lockAccount(boolean isLoginSuccessful,String loginName) throws ApplicationException 
    {
    	boolean accountLocked = Boolean.FALSE;
    	if(!isLoginSuccessful && Variables.invalidLoginAttemptsAllowed > 0)
    	{
//    		int doubleAttempts = Variables.invalidLoginAttemptsAllowed;
    		int invalidAttempts = getInvalidLoginAttempts(loginName);
	    		if(invalidAttempts == Variables.invalidLoginAttemptsAllowed)
	    		{
	    			String sqll = "update catissue_user set activity_status='Locked' where login_name=?";
	    			List<ColumnValueBean> beanList = new ArrayList<ColumnValueBean>();
	    			ColumnValueBean userIdBean = new ColumnValueBean(loginName);
	    	    	beanList.add(userIdBean);
	    	    	AppUtility.executeUpdateQuery(sqll, beanList);
	    	    	accountLocked = Boolean.TRUE;
	    		}
    	}
    	return accountLocked;
	}


	public static int getInvalidLoginAttempts(String loginName)
			throws ApplicationException {
		String sql;
		final String databaseType = DAOConfigFactory.getInstance()
		.getDAOFactory(CommonServiceLocator.getInstance()
				.getAppName()).getDataBaseType();
		if (databaseType.equals(Constants.ORACLE_DATABASE)) {
//			sql = "select is_login_successful from (select is_login_successful,row_number() over (order by login_timestamp desc) r from catissue_login_audit_event_log log, catissue_user user1 where log.user_login_id=user1.identifier " +
//			" and user1.login_name like ? and user1.activity_status='Active'  ) where r between 1 and ?";
			sql="SELECT IS_LOGIN_SUCCESSFUL,LOGIN_TIMESTAMP from(SELECT LOGIN_TIMESTAMP,IS_LOGIN_SUCCESSFUL from catissue_login_audit_event_log log1, " +
					" catissue_user user1 WHERE LOG1.USER_LOGIN_ID=USER1.IDENTIFIER AND USER1.LOGIN_NAME=? AND " +
					" USER1.ACTIVITY_STATUS='Active' ORDER BY LOG1.LOGIN_TIMESTAMP DESC) where ROWNUM <=? ORDER BY rownum";
		}
		else 
		{
			sql = "select is_login_successful from catissue_login_audit_event_log log, catissue_user user "+
			" where log.user_login_id=user.identifier and user.login_name like ? and user.activity_status='Active' order by login_timestamp desc limit ?";
		}
		int doubleAttempts = Variables.invalidLoginAttemptsAllowed;
		ColumnValueBean userIdBean = new ColumnValueBean(loginName);
		ColumnValueBean limitBean = new ColumnValueBean(doubleAttempts);
		List<ColumnValueBean> beansList = new ArrayList<ColumnValueBean>();
		beansList.add(userIdBean);
		beansList.add(limitBean); 
		List list = AppUtility.executeSQLQuery(sql, beansList);
		Boolean loginAttempt = Boolean.FALSE;
		int loginAttemptCtr = 0;
		if(list != null && list.size() > 0)
		{
			for (Object object : list) 
			{
				List list1 = (List)object;
				String atmpt = list1.get(0).toString();
				loginAttempt = Boolean.valueOf(atmpt);
				if(!loginAttempt && !"1".equals(atmpt))
				{
					loginAttemptCtr++;
				}
				else 
					break;
			}
		}
		return loginAttemptCtr;
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
        catch (ApplicationException exception) {
			// TODO Auto-generated catch block
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

}
