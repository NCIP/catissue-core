package edu.wustl.catissuecore.processor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.LoginDetails;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.domain.LoginResult;
import edu.wustl.processor.LoginProcessor;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

public final class CatissueLoginProcessor extends LoginProcessor
{
    /**
     * Empty private constructor to avoid instantiation.
     */
    private CatissueLoginProcessor()
    {

    }

    /**
     * Common Logger for catissue Login Processor.
     */
    private static final Logger LOGGER = Logger.getCommonLogger(CatissueLoginProcessor.class);

    public static LoginResult processUserLogin(final HttpServletRequest request, final String loginName,
            final String password) throws CatissueException
    {
        LoginResult loginResult = null;
        try
        {
            loginResult = processUserLogin(loginName, password);
        }
        catch (final Exception exception)
        {
            LOGGER.debug(exception);
        }
        finally
        {
            auditLogin(loginResult, loginName, request);
        }

        return loginResult;
    }

    private static void auditLogin(final LoginResult loginResult, final String loginName,
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
                    + "' and user.loginName =" + "'" + loginName + "'";

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

            (dao).auditLoginEvents(isLoginSuccessful, loginDetails);
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
     * @param loginId
     *            : loginId
     * @return String : String
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
     * @param loginForm
     *            LoginForm
     * @param validUser
     *            Object of valid user
     * @return result
     * @throws CatissueException
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
