package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;

public interface IUserBizLogic {

	/**
	 * Gets the user row id map.
	 *
	 * @param user
	 *            the user
	 * @param userRowIdMap
	 *            the user row id map
	 *
	 * @return the user row id map
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public Map<String, SiteUserRolePrivilegeBean> getUserRowIdMap(
			final User user, Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
			throws BizLogicException;

	/**
	 * Deletes the csm user from the csm user table.
	 *
	 * @param csmUser
	 *            The csm user to be deleted.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public void deleteCSMUser(
			final gov.nih.nci.security.authorization.domainobjects.User csmUser)
			throws BizLogicException;

	/**
	 * Insert cp site privileges.
	 *
	 * @param user1
	 *            the user1
	 * @param authorizationData
	 *            the authorization data
	 * @param userRowIdMap
	 *            the user row id map
	 */
	public void insertCPSitePrivileges(final User user1,
			final Vector<SecurityDataBean> authorizationData,
			final Map<String, SiteUserRolePrivilegeBean> userRowIdMap);

	/**
	 * Update user details.
	 *
	 * @param user1
	 *            the user1
	 * @param userRowIdMap
	 *            the user row id map
	 */
	public void updateUserDetails(final User user1,
			final Map<String, SiteUserRolePrivilegeBean> userRowIdMap);

	/**
	 * Update user.
	 *
	 * @param dao
	 *            DAO object.
	 * @param obj
	 *            The object to be updated
	 * @param oldObj
	 *            The old object
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 *
	 * @throws BizLogicException
	 *             BizLogic Exception
	 */
	public void updateUser(final DAO dao, final Object obj,
			final Object oldObj, final SessionDataBean sessionDataBean)
			throws BizLogicException;

	/**
	 * Returns the list of NameValueBeans with name as "LastName,Firstname" and
	 * value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @param operation
	 *            the operation
	 *
	 * @return the list of NameValueBeans with name as "LastName,Firstname" and
	 *         value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public Vector<NameValueBean> getUsers(final String operation)
			throws BizLogicException;

	/**
	 * Returns the list of NameValueBeans with name as "LastName,Firstname" and
	 * value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @return the list of NameValueBeans with name as "LastName,Firstname" and
	 *         value as systemtIdentifier, of all users who are not disabled.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 * @throws SMException
	 *             the SM exception
	 */
	public Vector<NameValueBean> getCSMUsers() throws BizLogicException,
			SMException;

	/**
	 * Returns a list of users according to the column name and value.
	 *
	 * @param colName
	 *            column name on the basis of which the user list is to be
	 *            retrieved.
	 * @param colValue
	 *            Value for the column name.
	 * @param className
	 *            the class name
	 *
	 * @return the list
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public List retrieve(final String className, final String colName,
			final Object colValue) throws BizLogicException;

	/**
	 * Retrieves and sends the login details email to the user whose email
	 * address is passed else returns the error key in case of an error.
	 *
	 * @param emailAddress
	 *            the email address of the user whose password is to be sent.
	 * @param sessionData
	 *            the session data
	 *
	 * @return the error key in case of an error.
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public String sendForgotPassword(final String emailAddress,
			final SessionDataBean sessionData) throws BizLogicException;

	/**
	 * This function checks whether user has logged in for first time or whether
	 * user's password is expired. In both these case user needs to change his
	 * password so Error key is returned
	 *
	 * @param user
	 *            - user object
	 *
	 * @return String
	 * @throws ApplicationException 
	 *
	 * @throws BizLogicException
	 *             - throws BizLogicException
	 */
	public String checkFirstLoginAndExpiry(final User user) throws ApplicationException;

	/**
	 * This function will check if the user is First time logging.
	 *
	 * @param user
	 *            user object
	 *
	 * @return firstTimeLogin
	 */
	public boolean getFirstLogin(final User user);

	/**
	 * To Sort CP's in CP based view according to the Privilges of User on CP.
	 * Done for MSR functionality change
	 *
	 * @param userId
	 *            the user id
	 * @param isCheckForCPBasedView
	 *            the is check for cp based view
	 *
	 * @return the related cp ids
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 *
	 * @author ravindra_jain
	 */

	public Set<Long> getRelatedCPIds(final Long userId,
			final boolean isCheckForCPBasedView) throws BizLogicException;

	/**
	 * Gets the related site ids.
	 *
	 * @param userId
	 *            user Id
	 *
	 * @return Set
	 *
	 * @throws BizLogicException
	 *             BizLogicException
	 */
	public Set<Long> getRelatedSiteIds(final Long userId)
			throws BizLogicException;

	/**
	 * Custom method for Add User Case.
	 *
	 * @param dao
	 *            DAO object
	 * @param domainObject
	 *            Domain object
	 *
	 * @return the object id
	 */
	public String getObjectId(final DAO dao, final Object domainObject);

	/**
	 * Check user.
	 *
	 * @param domainObject
	 *            the domain object
	 * @param sessionDataBean
	 *            the session data bean
	 *
	 * @return true, if successful
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 */
	public boolean checkUser(final Object domainObject,
			final SessionDataBean sessionDataBean, Object uiObject)
			throws BizLogicException;

	public boolean isAuthorized(final DAO dao, final Object domainObject,
			final SessionDataBean sessionDataBean) throws BizLogicException;

	/**
	 * Over-ridden for the case of Non - Admin user should be able to edit
	 * his/her details e.g. Password (non-Javadoc)
	 *
	 * @param dao
	 *            the dao
	 * @param domainObject
	 *            the domain object
	 * @param sessionDataBean
	 *            the session data bean
	 *
	 * @return true, if checks if is authorized
	 *
	 * @throws BizLogicException
	 *             the biz logic exception
	 *
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO,
	 *      java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(final DAO dao, final Object domainObject,
			final SessionDataBean sessionDataBean, Object uiObject)
			throws BizLogicException;

	/**
	 *
	 * @param loginName
	 *            : loginName
	 * @return User : User
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public User getUser(final String loginName) throws BizLogicException;

	public List<User> getActiveUsers() throws BizLogicException;

	public User getAdminUser() throws BizLogicException;

	public abstract boolean hasRegistrationPermission(SessionDataBean sessionDataBean)
			throws BizLogicException;

}