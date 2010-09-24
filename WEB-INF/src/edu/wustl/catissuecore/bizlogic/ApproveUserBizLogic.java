/**
 * <p>
 * Title: ApproveUserBizLogic Class>
 * <p>
 * Description: ApproveUserBizLogic is the bizLogic class for approve users.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * ApproveUserBizLogic is the bizLogic class for approve users.
 *
 * @author gautam_shetty
 */
public class ApproveUserBizLogic extends CatissueDefaultBizLogic
{
    /**
     * Common Logger for Login Action.
     */
    private transient final Logger logger = Logger.getCommonLogger(ApproveUserBizLogic.class);

    /**
     * Updates the persistent object in the database.
     *
     * @param obj
     *            The object to be updated.
     * @param oldObj
     *            Persistent object
     * @param dao
     *            DAO object
     * @param sessionDataBean
     *            The session in which the object is saved
     * @throws BizLogicException
     *             Database related Exception
     */
    @Override
    protected void update(final DAO dao, final Object obj, final Object oldObj,
            final SessionDataBean sessionDataBean) throws BizLogicException
    {
        User user = null;
        UserDTO userDTO = null;

        User oldUser = null;
        if (obj instanceof UserDTO)
        {
            userDTO = (UserDTO) obj;
            user = userDTO.getUser();
            oldUser = (User) oldObj;
        }
        else
        {
            user = (User) obj;
            oldUser = (User) oldObj;
        }
        final UserBizLogic objUserBizlogic = new UserBizLogic();
        objUserBizlogic.validate(user, dao, Constants.EDIT);
        ApiSearchUtil.setUserDefault(user);
        // End:- Change for API Search
        final gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();
        try
        {
            dao.update(user.getAddress());
            // If the activity status is Active, create a csm user.
            if (Status.ACTIVITY_STATUS_ACTIVE.toString().equals(user.getActivityStatus()))
            {
                approveUser(obj, oldUser, csmUser, dao, sessionDataBean);
            }
            else
            {
                dao.update(user, oldUser);
            }
            emailHandler(user);
            final EmailHandler emailHandler = new EmailHandler();

            // If user is approved send approval and login details emails to the
            // user and administrator.
            if (Status.ACTIVITY_STATUS_ACTIVE.toString().equals(user.getActivityStatus()))
            {
                // Send approval email to the user and administrator.
                emailHandler.sendApprovalEmail(user);
            }
            else if (Status.ACTIVITY_STATUS_REJECT.toString().equals(user.getActivityStatus()))
            {
                // If user is rejected send rejection email to the user and
                // administrator.
                // Send rejection email to the user and administrator.
                emailHandler.sendRejectionEmail(user);
            }
        }
        catch (final Exception exp)
        {
            logger.error(exp.getMessage(), exp);
            new UserBizLogic().deleteCSMUser(csmUser);
            final ErrorKey errorKey = ErrorKey.getErrorKey("pwd.encrytion.error");
            throw new BizLogicException(errorKey, exp, "");
        }
        // Code related to approve User, WUSTLKEY column is added for this
        // purpose.
      //  migrateUser(user);
    }

    /**
     * @param user
     *            User Object
     * @throws ApplicationException
     *             ApplicationException
     */
    private void emailHandler(final User user) throws ApplicationException
    {
        final EmailHandler emailHandler = new EmailHandler();
        // If user is approved send approval and login details emails to the
        // user and administrator.
        if (Status.ACTIVITY_STATUS_ACTIVE.toString().equals(user.getActivityStatus()))
        {
            // Send approval email to the user and administrator.
            emailHandler.sendApprovalEmail(user);
        }
        else if (Status.ACTIVITY_STATUS_REJECT.toString().equals(user.getActivityStatus()))
        {
            // If user is rejected send rejection email to the user and
            // administrator.
            // Send rejection email to the user and administrator.
            emailHandler.sendRejectionEmail(user);
        }
    }

    /**
     * Populates CsmUser data & creates Protection elements for User.
     *
     * @param user1
     *            User Object
     * @param csmUser
     *            CSM user object
     * @param dao
     *            DAO object
     * @param sessionDataBean
     *            Object of SessionDataBean
     * @throws DAOException
     *             DAOException
     * @throws SMException
     *             SMException
     * @throws PasswordEncryptionException
     *             PasswordEncryptionException
     * @throws BizLogicException
     *             BizLogicException
     */
    private void approveUser(final Object user1, final User oldUser,
            final gov.nih.nci.security.authorization.domainobjects.User csmUser, final DAO dao,
            final SessionDataBean sessionDataBean) throws BizLogicException, DAOException, SMException,
            PasswordEncryptionException
    {
        User user = null;
        UserDTO userDTO = null;
        Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
        if (user1 instanceof UserDTO)
        {
            userDTO = (UserDTO) user1;
            user = userDTO.getUser();
            userRowIdMap = userDTO.getUserRowIdBeanMap();
        }
        else
        {
            user = (User) user1;
        }
        // Method to populate rowIdMap in case, Add Privilege button is not
        // clicked
        userRowIdMap = new UserBizLogic().getUserRowIdMap(user, userRowIdMap);
        csmUser.setLoginName(user.getLoginName());
        csmUser.setLastName(user.getLastName());
        csmUser.setFirstName(user.getFirstName());
        csmUser.setEmailId(user.getEmailAddress());
        csmUser.setStartDate(Calendar.getInstance().getTime());
        final String generatedPassword = PasswordManager.generatePassword();
        if (user.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
        {
            csmUser.setPassword(generatedPassword);
        }
        SecurityManagerFactory.getSecurityManager().createUser(csmUser);
        decideRole(csmUser, user);
        user.setCsmUserId(csmUser.getUserId());
        final Password password = new Password(PasswordManager.encrypt(generatedPassword), user);
        user.getPasswordCollection().add(password);
        logger.debug("password stored in passwore table");
        final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
        final Set protectionObjects = new HashSet();
        protectionObjects.add(user);
        if (userRowIdMap != null && !userRowIdMap.isEmpty())
        {
            new UserBizLogic().updateUserDetails(user, userRowIdMap);
        }
        privilegeManager.insertAuthorizationData(getAuthorizationData(user, userRowIdMap), protectionObjects,
                null, user.getObjectId());
        dao.update(user, oldUser);
    }

    /**
     * @param csmUser
     *            CSM User
     * @param user
     *            User Object
     * @throws SMException
     *             SMException
     */
    private void decideRole(final gov.nih.nci.security.authorization.domainobjects.User csmUser, final User user)
            throws SMException
    {
        String role = "";
        if (user.getRoleId() != null)
        {
            if (user.getRoleId().equalsIgnoreCase(Constants.SUPER_ADMIN_USER))
            {
                role = Constants.ADMIN_USER;
            }
            else
            {
                role = Constants.NON_ADMIN_USER;
            }
            SecurityManagerFactory.getSecurityManager().assignRoleToUser(csmUser.getUserId().toString(), role);
        }
    }

    /**
     * This method returns collection of UserGroupRoleProtectionGroup objects
     * that speciefies the user group protection group linkage through a role.
     * It also specifies the groups the protection elements returned by this
     * class should be added to.
     *
     * @param obj
     *            Domain object
     * @param userRowIdMap
     *            Map of SiteUserRolePrivilegeBean
     * @return authorizationData
     * @throws SMException
     *             SMException
     */
    private Vector getAuthorizationData(final AbstractDomainObject obj,
            final Map<String, SiteUserRolePrivilegeBean> userRowIdMap) throws SMException
    {
        final Vector authorizationData = new Vector();
        final Set group = new HashSet();
        SecurityDataBean userGroupRoleProtectionGroupBean;
        String protectionGroupName;

        final User aUser = (User) obj;
        final String userId = String.valueOf(aUser.getCsmUserId());
        final gov.nih.nci.security.authorization.domainobjects.User user = SecurityManagerFactory
                .getSecurityManager().getUserById(userId);
        Logger.out.debug(" User: " + user.getLoginName());
        group.add(user);

        // Protection group of PI
        protectionGroupName = Constants.getUserPGName(aUser.getId());
        userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
        userGroupRoleProtectionGroupBean.setGroupName(Constants.getUserGroupName(aUser.getId()));
        userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);
        logger.debug(authorizationData.toString());
        if (userRowIdMap != null)
        {
            new UserBizLogic().insertCPSitePrivileges(aUser, authorizationData, userRowIdMap);
        }
        return authorizationData;
    }

    /**
     * Returns the list of users according to the column name and value passed.
     *
     * @param className
     *            Name of object class
     * @param colName
     *            Column name
     * @param colValue
     *            Column Value
     * @return the list of users according to the column name and value passed.
     * @throws BizLogicException
     *             BizLogicException
     */
    @Override
    public List retrieve(final String className, final String colName, final Object colValue)
            throws BizLogicException
    {
        List userList = null;
        userList = super.retrieve(className, colName, colValue);
        edu.wustl.catissuecore.domain.User appUser = null;
        if (!userList.isEmpty())
        {
            appUser = (edu.wustl.catissuecore.domain.User) userList.get(0);
        }
        return userList;
    }

    /**
     * Called from DefaultBizLogic to get ObjectId for authorization check.
     * (non-Javadoc)
     *
     * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO,
     *      java.lang.Object)
     * @param dao
     *            DAO object
     * @param domainObject
     *            Domain Object
     * @return Id
     */
    @Override
    public String getObjectId(final DAO dao, final Object domainObject)
    {
        return new UserBizLogic().getObjectId(dao, domainObject);
    }

    /**
     * To get PrivilegeName for authorization check from
     * 'PermissionMapDetails.xml'.
     *
     * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
     * @param domainObject
     *            Domai object
     * @return String
     */
    @Override
    protected String getPrivilegeKey(final Object domainObject)
    {
        return Constants.ADD_EDIT_USER;
    }

    /**
     * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.dao.DAO,
     *      java.lang.Object, edu.wustl.common.beans.SessionDataBean)
     * @param dao
     *            Object of DAO
     * @param domainObject
     *            Domain Object
     * @param sessionDataBean
     *            Object of SessionData bean
     * @return boolean
     * @throws BizLogicException
     *             BizLogicException
     */
    @Override
    public boolean isAuthorized(final DAO dao, final Object domainObject, final SessionDataBean sessionDataBean)
            throws BizLogicException
    {
        boolean isAuthorized = false;
        isAuthorized = checkUser(domainObject, sessionDataBean);
        if (isAuthorized)
        {
            return true;
        }

        final String privilegeName = getPrivilegeName(domainObject);
        final String protectionElementName = getObjectId(dao, domainObject);

        return AppUtility.returnIsAuthorized(sessionDataBean, privilegeName, protectionElementName);

    }

    /**
     * @param domainObject
     *            User domain object
     * @param sessionDataBean
     *            SessionDataBean object
     * @return Boolean true/false
     * @throws BizLogicException
     *             object of BizLogicException
     */
    private boolean checkUser(final Object domainObject, final SessionDataBean sessionDataBean)
            throws BizLogicException
    {
        return new UserBizLogic().checkUser(domainObject, sessionDataBean);
    }

//    /**
//     * This method will migrate user to WUSTLKey.
//     *
//     * @param user
//     *            Object of USER
//     * @param csmUser
//     *            Object of CSM User
//     * @throws BizLogicException
//     *             Object of BizLogicException
//     */
//    private void migrateUser(final User user) throws BizLogicException
//    {
//
//        try
//        {
//            final UserDetails userDetails = new UserDetails();
//            userDetails.setLoginName(user.getLoginName());
//            userDetails.setMigratedLoginName(user.getTargetIdpLoginName());
//            userDetails.setTargetIDP(user.getTargetIdp());
//            if (Status.ACTIVITY_STATUS_ACTIVE.toString().equals(user.getActivityStatus()))
//            {
//                Utility.migrateUser(userDetails);
//            }
//            else if(Status.ACTIVITY_STATUS_REJECT.toString().equals(user.getActivityStatus()))
//            {
//                Utility.rejectUser(userDetails);
//            }
//
//        }
//        catch (final MigratorException e)
//        {
//            logger.error(e.getMessage(), e);
//            throw getBizLogicException(e, "migration.info.updation.error", "");
//        }
//
//    }
}