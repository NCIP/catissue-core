/**
 * <p>Title: UserBizLogic Class>
 * <p>Description:	UserBizLogic is used to add user information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 13, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.auth.exception.MigrationRuleException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.catissuecore.comparator.PasswordComparator;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.utils.UserUtility;
import edu.wustl.catissuecore.gridgrouper.GridGrouperUtil;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.uiobject.UserUIObject;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolAuthorization;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;

import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.passwordutil.Password;
import edu.wustl.catissuecore.passwordutil.Util;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.locator.CSMGroupLocator;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import edu.wustl.security.privilege.PrivilegeUtility;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.util.StringUtilities;

// TODO: Auto-generated Javadoc
/**
 * UserBizLogic is used to add user information into the database using
 * Hibernate.
 *
 * @author kapil_kaveeshwar
 */
public class UserBizLogic extends CatissueDefaultBizLogic implements IUserBizLogic
{
    static
    {
        LoggerConfig.configureLogger(System.getProperty("user.dir"));
    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = Logger.getCommonLogger(UserBizLogic.class);

    /** The Constant FAIL_SAME_AS_LAST_N. */
    public static final int FAIL_SAME_AS_LAST_N = 8;

    /** The Constant FAIL_FIRST_LOGIN. */
    public static final int FAIL_FIRST_LOGIN = 9;

    /** The Constant FAIL_EXPIRE. */
    public static final int FAIL_EXPIRE = 10;

    /** The Constant FAIL_CHANGED_WITHIN_SOME_DAY. */
    public static final int FAIL_CHANGED_WITHIN_SOME_DAY = 11;

    /** The Constant FAIL_SAME_NAME_SURNAME_EMAIL. */
    public static final int FAIL_SAME_NAME_SURNAME_EMAIL = 12;

    /** The Constant FAIL_PASSWORD_EXPIRED. */
    public static final int FAIL_PASSWORD_EXPIRED = 13;

    /** The Constant USER_EMAIL_ADDRESS. */
    private static final String USER_EMAIL_ADDRESS = "user.emailAddress";

    /** The Constant ERRORS_ITEM_REQUIRED. */
    private static final String ERRORS_ITEM_REQUIRED = "errors.item.required";

    /** The Constant SUCCESS. */
    public static final int SUCCESS = 0;

    @Override
    protected void insert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
    throws BizLogicException
    {
    	UserUIObject userUIObject = new UserUIObject();
    	insert(obj,userUIObject,dao,sessionDataBean);
    }

    /**
     * Saves the user object in the database.
     *
     * @param obj
     *            The user object to be saved.
     * @param dao
     *            the dao
     * @param sessionDataBean
     *            the session data bean
     *
     * @throws BizLogicException
     *             the biz logic exception
     */
    @Override
    protected void insert(final Object obj, Object uiObject, final DAO dao, final SessionDataBean sessionDataBean)
            throws BizLogicException
    {
    	User user = null;
        UserUIObject userUIObject = (UserUIObject)uiObject;

    	
        Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();

        if (obj instanceof UserDTO)
        {
            user = ((UserDTO) obj).getUser();
            userRowIdMap = ((UserDTO) obj).getUserRowIdBeanMap();
        }
        else
        {
            user = (User) obj;
        }
        // Method to populate rowIdMap in case, Add Privilege button is not
        // clicked
        userRowIdMap = getUserRowIdMap(user, userRowIdMap);
        final gov.nih.nci.security.authorization.domainobjects.User csmUser = new gov.nih.nci.security.authorization.domainobjects.User();

        try
        {
            Object object = dao.retrieveById(Department.class.getName(), user.getDepartment().getId());
            Department department = null;
            if (object != null)
            {
                department = (Department) object;
            }

            object = dao.retrieveById(Institution.class.getName(), user.getInstitution().getId());
            Institution institution = null;
            if (object != null)
            {
                institution = (Institution) object;
            }

            object = dao.retrieveById(CancerResearchGroup.class.getName(), user.getCancerResearchGroup().getId());
            CancerResearchGroup cancerResearchGroup = null;
            if (object != null)
            {
                cancerResearchGroup = (CancerResearchGroup) object;
            }

            user.setDepartment(department);
            user.setInstitution(institution);
            user.setCancerResearchGroup(cancerResearchGroup);
            final String generatedPassword = PasswordManager.generatePassword();
            final Password password = new Password();
            boolean grouperUser = false ;
            if (!StringUtilities.isBlank(userUIObject.getGrouperUser())) {
            	grouperUser = true;
            }
            
            // If the page is of signup user don't create the csm user.
            if ((userUIObject.getPageOf() != null && !userUIObject.getPageOf().equals(Constants.PAGE_OF_SIGNUP)) || grouperUser)
            {
                csmUser.setLoginName(user.getLoginName());
                csmUser.setLastName(user.getLastName());
                csmUser.setFirstName(user.getFirstName());
                csmUser.setEmailId(user.getEmailAddress());
                csmUser.setStartDate(user.getStartDate());
                csmUser.setPassword(generatedPassword);

                SecurityManagerFactory.getSecurityManager().createUser(csmUser);

                String role =userUIObject.getRoleId() ;
                if (role != null)
                {
                    if (Constants.SUPER_ADMIN_USER.equalsIgnoreCase(role))
                    {
                        role = Constants.ADMIN_USER;
                    }
                    else
                    {
                        role = Constants.NON_ADMIN_USER;
                    }

                    SecurityManagerFactory.getSecurityManager().assignRoleToUser(csmUser.getUserId().toString(),
                            role);
                }

                user.setCsmUserId(csmUser.getUserId());
                // user.setPassword(csmUser.getPassword());
                // Add password of user in password table.Updated by Supriya
                // Dankh
                // comment nhassan
                /*InstanceFactory<Password> instFact = DomainInstanceFactory.getInstanceFactory(Password.class);
                final Password password = instFact.createObject();*/

                /**
                 * Start: Change for API Search --- Jitendra 06/10/2006 In Case
                 * of Api Search, previoulsy it was failing since there was
                 * default class level initialization on domain object. For
                 * example in User object, it was initialized as protected
                 * String lastName=""; So we removed default class level
                 * initialization on domain object and are initializing in
                 * method setAllValues() of domain object. But in case of Api
                 * Search, default values will not get set since setAllValues()
                 * method of domainObject will not get called. To avoid null
                 * pointer exception, we are setting the default values same as
                 * we were setting in setAllValues() method of domainObject.
                 */
                ApiSearchUtil.setPasswordDefault(password);
                // End:- Change for API Search

                password.setUser(user);
                password.setPassword(PasswordManager.encrypt(generatedPassword));
                password.setUpdateDate(new Date());
                /*if(user.getPasswordCollection()==null)
                {
                	user.setPasswordCollection(new HashSet<Password>());
                }*/
                //user.getPasswordCollection().add(password);
                if (grouperUser) {
                	//auto approve 
                	user.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
                }

                LOGGER.debug("password stored in passwore table");
            }

            /**
             * First time login is always set to true when a new user is created
             */
            user.setFirstTimeLogin(Boolean.TRUE);

            // Create address and the user in catissue tables.
            dao.insert(user.getAddress());

            if (userRowIdMap != null && !userRowIdMap.isEmpty())
            {
                updateUserDetails(user, userRowIdMap);
            }
            dao.insert(user);
            dao.insert(password);
            if (Constants.PAGE_OF_SIGNUP.equals(userUIObject.getPageOf()))
            {
                insertUserIDPInformation(user,userUIObject);
            }

            final Set<User> protectionObjects = new HashSet<User>();
            protectionObjects.add(user);

            final EmailHandler emailHandler = new EmailHandler();
            // Send the user registration email to user and the administrator.

         //   if (Constants.PAGE_OF_SIGNUP.equals(userUIObject.getPageOf()) && !grouperUser )

            if ( (Constants.PAGE_OF_SIGNUP.equals(userUIObject.getPageOf()) || userUIObject.getPageOf() == null) && !grouperUser)

            {
                // SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
                // protectionObjects, null);

                emailHandler.sendUserSignUpEmail(user);
            }
            else
            // Send the user creation email to user and the administrator.
            {
                // SecurityManager.getInstance(this.getClass()).insertAuthorizationData(getAuthorizationData(user),
                // protectionObjects, null);

                final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

                privilegeManager.insertAuthorizationData(getAuthorizationData(user, userRowIdMap),
                        protectionObjects, null, user.getObjectId());
                if (grouperUser) {
                	// assign user to his grid groups in caTissue ...
                	Map<String , String> filteredUserGroups = GridGrouperUtil.getFilteredUserGroups(null, user.getLoginName());
                	Collection<String> csmGroupIds = filteredUserGroups.values();
                	Set<String> gridGroupNames = filteredUserGroups.keySet();
                	UserProvisioningManager userProvisioningManager = new PrivilegeUtility().getUserProvisioningManager();
                	userProvisioningManager.addGroupsToUser(user.getCsmUserId()+"", csmGroupIds.toArray(new String[0]));
                	// and also need to assign the user to all CPs for which his group has access to . 
                	// get cp ids for user groups from catissue_cp_grid_prvg
                	List<String> cpIds = GridGrouperUtil.getCPsForUserGroups(gridGroupNames);
                	CollectionProtocolAuthorization collectionProtocolAuthorization = new CollectionProtocolAuthorization();
                	CollectionProtocol collectionProtocol = null;
                	for (String cpId:cpIds) {
                		collectionProtocol = (CollectionProtocol)dao.retrieveById(CollectionProtocol.class.getName(), Long.valueOf(cpId));
                		Set<User> userCollection = new HashSet<User>();
                		userCollection.add(user);
                		collectionProtocolAuthorization.addUsers(collectionProtocol, userCollection);
                	}
                }

                emailHandler.sendApprovalEmail(user,userUIObject);
            }
        }
        catch (final SMException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
            // added to format constrainviolation message
            deleteCSMUser(csmUser);
            throw getBizLogicException(e, "sm.check.priv", "");
        }
        catch (final PasswordEncryptionException exception)
        {
            UserBizLogic.LOGGER.error(exception.getMessage(), exception);
            deleteCSMUser(csmUser);
            throw getBizLogicException(exception, "pwd.encrytion.error", "");
        }
        catch (final ApplicationException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
            deleteCSMUser(csmUser);
            // ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
            throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());

        } catch (final Exception e) {
        	 UserBizLogic.LOGGER.error(e.getMessage(), e);
        	 deleteCSMUser(csmUser);
        	 throw getBizLogicException(e, "sm.check.priv", "");
		}
    }
    

    private void insertUserIDPInformation(final User user,UserUIObject userUIObject) throws BizLogicException, DAOException
    {
        try
        {
            if (!Validator.isEmpty(userUIObject.getTargetIdp()))
            {
                final UserDetails userDetails = new UserDetails();
                userDetails.setLoginName(user.getLoginName());
                userDetails.setMigratedLoginName(userUIObject.getTargetIdpLoginName());
                userDetails.setTargetIDP(userUIObject.getTargetIdp());
                userDetails.setPassword(userUIObject.getTargetPassword());
                Utility.migrateUser(userDetails);
            }
        }
        catch (final MigratorException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
            throw getBizLogicException(e, "migration.info.updation.error", "");
        }
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getUserRowIdMap(edu.wustl.catissuecore.domain.User, java.util.Map)
	 */
    @Override
	public Map<String, SiteUserRolePrivilegeBean> getUserRowIdMap(final User user,
            Map<String, SiteUserRolePrivilegeBean> userRowIdMap) throws BizLogicException
    {
    	String roleId=UserUtility.getRoleId(user);
        if (roleId != null && !roleId.equalsIgnoreCase("-1")
                && !roleId.equalsIgnoreCase("0"))
        {
            if (userRowIdMap == null || userRowIdMap.isEmpty() && user.getSiteCollection() != null
                    && !user.getSiteCollection().isEmpty())
            {
                final List<NameValueBean> list = new AssignPrivilegePageBizLogic()
                        .getActionsList(roleId);
                final NameValueBean roleBean = new NameValueBean();
                try
                {
                    final List<Role> roleList = SecurityManagerFactory.getSecurityManager().getRoles();
                    roleBean.setValue(roleId);
                    for (final Role role : roleList)
                    {
                        if (role.getId().toString().equalsIgnoreCase(roleId))
                        {
                            roleBean.setName(role.getName());
                            break;
                        }
                    }
                }
                catch (final SMException e)
                {
                    UserBizLogic.LOGGER.error(e.getMessage(), e);
                    throw getBizLogicException(e, "user.roleNotFound", "");
                }
                int identifier = 0;
                userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
                for (final Site site : user.getSiteCollection())
                {
                    final List<Site> siteList = new ArrayList<Site>();
                    siteList.add(site);
                    final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = new SiteUserRolePrivilegeBean();
                    siteUserRolePrivilegeBean.setAllCPChecked(true);
                    siteUserRolePrivilegeBean.setPrivileges(list);
                    siteUserRolePrivilegeBean.setRole(roleBean);
                    siteUserRolePrivilegeBean.setSiteList(siteList);
                    userRowIdMap.put(String.valueOf(identifier), siteUserRolePrivilegeBean);
                    identifier++;
                }
            }
        }
        return userRowIdMap;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#deleteCSMUser(gov.nih.nci.security.authorization.domainobjects.User)
	 */
    @Override
	public void deleteCSMUser(final gov.nih.nci.security.authorization.domainobjects.User csmUser)
            throws BizLogicException
    {
        try
        {
            if (csmUser.getUserId() != null)
            {
                SecurityManagerFactory.getSecurityManager().removeUser(csmUser.getUserId().toString());
            }
        }
        catch (final SMException smExp)
        {
            UserBizLogic.LOGGER.error(smExp.getMessage(), smExp);
            throw getBizLogicException(smExp, "sm.operation.error", "Error in checking has privilege");
        }
    }

    /**
     * This method returns collection of UserGroupRoleProtectionGroup objects
     * that speciefies the user group protection group linkage through a role.
     * It also specifies the groups the protection elements returned by this
     * class should be added to.
     *
     * @param obj
     *            the obj
     * @param userRowIdMap
     *            the user row id map
     *
     * @return the authorization data
     *
     * @throws SMException
     *             the SM exception
     */
    private Vector<SecurityDataBean> getAuthorizationData(final AbstractDomainObject obj,
            final Map<String, SiteUserRolePrivilegeBean> userRowIdMap) throws SMException
    {
        final Vector<SecurityDataBean> authorizationData = new Vector<SecurityDataBean>();
        final Set<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
        final User aUser = (User) obj;

        final String userId = String.valueOf(aUser.getCsmUserId());
        final gov.nih.nci.security.authorization.domainobjects.User user = SecurityManagerFactory
                .getSecurityManager().getUserById(userId);
        LOGGER.debug(" User: " + user.getLoginName());
        group.add(user);

        // Protection group of User
        final String protectionGroupName = Constants.getUserPGName(aUser.getId());
        final SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
        userGroupRoleProtectionGroupBean.setUser(userId);
        userGroupRoleProtectionGroupBean.setRoleName(Roles.UPDATE_ONLY);
        userGroupRoleProtectionGroupBean.setGroupName(Constants.getUserGroupName(aUser.getId()));
        userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
        userGroupRoleProtectionGroupBean.setGroup(group);
        authorizationData.add(userGroupRoleProtectionGroupBean);

        LOGGER.debug(authorizationData.toString());

        if (userRowIdMap != null)
        {
            insertCPSitePrivileges(aUser, authorizationData, userRowIdMap);
        }

        return authorizationData;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#insertCPSitePrivileges(edu.wustl.catissuecore.domain.User, java.util.Vector, java.util.Map)
	 */
    @Override
	public void insertCPSitePrivileges(final User user1, final Vector<SecurityDataBean> authorizationData,
            final Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
    {
        if (userRowIdMap == null || userRowIdMap.isEmpty())
        {
            return;
        }

        final Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap = new HashMap<String, SiteUserRolePrivilegeBean>();
        final Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap = new HashMap<String, SiteUserRolePrivilegeBean>();

        final Object[] mapKeys = userRowIdMap.keySet().toArray();
        for (final Object mapKey : mapKeys)
        {
            final String key = mapKey.toString();
            final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);
            siteUserRolePrivilegeBean.setUser(user1);
            if (siteUserRolePrivilegeBean.isAllCPChecked())
            {
                final Map<String, SiteUserRolePrivilegeBean> map = AppUtility
                        .splitBeanData(siteUserRolePrivilegeBean);
                final SiteUserRolePrivilegeBean bean1 = map.get("SITE");
                final SiteUserRolePrivilegeBean bean2 = map.get("CP");
                userRowIdMap.remove(key);
                if (bean1.getPrivileges().isEmpty())
                {
                    bean1.setPrivileges(bean2.getPrivileges());
                }
                userRowIdMap.put(key, bean1);
                if (bean2.getPrivileges().isEmpty())
                {
                    bean2.setPrivileges(bean1.getPrivileges());
                }
                userRowIdMap.put(key + "All_CurrentnFuture_CPs", bean2);
            }
        }

        distributeMapData(userRowIdMap, cpPrivilegeMap, sitePrivilegeMap);

        /* For SITE Privileges Purpose */
        insertSitePrivileges(user1, authorizationData, sitePrivilegeMap);

        /* For CP Privileges Purpose */
        insertCPPrivileges(user1, authorizationData, cpPrivilegeMap);

        /* Common for both */
        // updateUserDetails(user1, userRowIdMap);
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#updateUserDetails(edu.wustl.catissuecore.domain.User, java.util.Map)
	 */
    @Override
	public void updateUserDetails(final User user1, final Map<String, SiteUserRolePrivilegeBean> userRowIdMap)
    {
        final Set<Site> siteCollection = new HashSet<Site>();
        final Set<CollectionProtocol> cpCollection = new HashSet<CollectionProtocol>();
        final Set<CollectionProtocol> removedCpCollection = new HashSet<CollectionProtocol>();
        for (final String key : userRowIdMap.keySet())
        {
            final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);

            final CollectionProtocol protocol = siteUserRolePrivilegeBean.getCollectionProtocol();
            if (protocol != null && !siteUserRolePrivilegeBean.isRowDeleted())
            {
                cpCollection.add(protocol);
            }
            else if (protocol != null && siteUserRolePrivilegeBean.isRowDeleted())
            {
                removedCpCollection.add(protocol);
            }

            List<Site> siteList = null;

            if (!siteUserRolePrivilegeBean.isRowDeleted())
            {
                siteList = siteUserRolePrivilegeBean.getSiteList();

                if (siteList != null && !siteList.isEmpty())
                {
                    for (final Site site : siteList)
                    {
                        boolean isPresent = false;
                        for (final Site site1 : siteCollection)
                        {
                            if (site1.getId().equals(site.getId()))
                            {
                                isPresent = true;
                            }
                        }
                        if (!isPresent)
                        {
                            siteCollection.add(site);
                        }
                    }
                }
            }
        }
        if(user1.getSiteCollection()==null)
        {
        	user1.setSiteCollection(new HashSet<Site>());
        }
        user1.getSiteCollection().clear();
        user1.getSiteCollection().addAll(siteCollection);
        updateCollectionProtocolCollection(user1, cpCollection, removedCpCollection);
    }

    /**
     * Update collection protocol collection.
     *
     * @param user1
     *            the user1
     * @param cpCollection
     *            the cp collection
     * @param removedCpCollection
     *            the removed cp collection
     */
    private void updateCollectionProtocolCollection(final User user1, final Set<CollectionProtocol> cpCollection,
            final Set<CollectionProtocol> removedCpCollection)
    {
        final Collection<CollectionProtocol> tempCollection = new HashSet<CollectionProtocol>();
        tempCollection.addAll(user1.getAssignedProtocolCollection());
        for (final CollectionProtocol newCp : cpCollection)
        {
            boolean isPresent = false;
            for (final CollectionProtocol cp : user1.getAssignedProtocolCollection())
            {
                if (newCp.getId().equals(cp.getId()))
                {
                    isPresent = true;
                }
            }
            if (!isPresent)
            {
                user1.getAssignedProtocolCollection().add(newCp);
            }
        }
        for (final CollectionProtocol removedCp : removedCpCollection)
        {
            boolean isPresent = false;
            for (final CollectionProtocol cp : cpCollection)
            {
                if (removedCp.getId().equals(cp.getId()))
                {
                    isPresent = true;
                }
            }
            if (!isPresent)
            {
                for (final CollectionProtocol existingCp : tempCollection)
                {
                    if (existingCp.getId().equals(removedCp.getId()))
                    {
                        user1.getAssignedProtocolCollection().remove(existingCp);
                    }
                }
            }
        }
    }

    /**
     * Insert site privileges.
     *
     * @param user1
     *            the user1
     * @param authorizationData
     *            the authorization data
     * @param sitePrivilegeMap
     *            the site privilege map
     */
    private void insertSitePrivileges(final User user1, final Vector<SecurityDataBean> authorizationData,
            final Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap)
    {
        String roleName = "";

        for (final String key : sitePrivilegeMap.keySet())
        {
            try
            {
                final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = sitePrivilegeMap.get(key);

                if (siteUserRolePrivilegeBean.isRowDeleted())
                {
                    AppUtility.processDeletedPrivileges(siteUserRolePrivilegeBean);
                }
                else if (siteUserRolePrivilegeBean.isRowEdited())
                {
                    final Site site = siteUserRolePrivilegeBean.getSiteList().get(0);
                    final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
                    if (defaultRole != null
                            && (defaultRole.equalsIgnoreCase("0") || defaultRole.equalsIgnoreCase("-1")))
                    {
                        roleName = Constants.getSiteRoleName(site.getId(), user1.getCsmUserId(), defaultRole);
                    }
                    else
                    {
                        roleName = siteUserRolePrivilegeBean.getRole().getName();
                    }
                    final Set<String> privileges = new HashSet<String>();
                    final List<NameValueBean> privilegeList = siteUserRolePrivilegeBean.getPrivileges();

                    for (final NameValueBean privilege : privilegeList)
                    {
                        privileges.add(privilege.getValue());
                    }

                    AppUtility.processRole(roleName);

                    PrivilegeManager.getInstance().createRole(roleName, privileges);

                    final String userId = String.valueOf(user1.getCsmUserId());

                    gov.nih.nci.security.authorization.domainobjects.User csmUser = null;
                    csmUser = getUserByID(userId);
                    final HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
                    group.add(csmUser);

                    final String protectionGroupName = CSMGroupLocator.getInstance().getPGName(site.getId(),
                            Site.class);

                    createProtectionGroup(protectionGroupName, site, false);

                    final SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
                    userGroupRoleProtectionGroupBean.setUser("");
                    userGroupRoleProtectionGroupBean.setRoleName(roleName);

                    userGroupRoleProtectionGroupBean.setGroupName(Constants.getSiteUserGroupName(site.getId(),
                            user1.getCsmUserId()));
                    userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
                    userGroupRoleProtectionGroupBean.setGroup(group);
                    authorizationData.add(userGroupRoleProtectionGroupBean);
                }
            }

            catch (final SMException e)
            {
                UserBizLogic.LOGGER.error(e.getMessage(), e);
            }
            catch (final ApplicationException e)
            {
                UserBizLogic.LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Insert cp privileges.
     *
     * @param user1
     *            the user1
     * @param authorizationData
     *            the authorization data
     * @param cpPrivilegeMap
     *            the cp privilege map
     */
    private void insertCPPrivileges(final User user1, final Vector<SecurityDataBean> authorizationData,
            final Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap)
    {
        String roleName = "";
        String protectionGroupName = "";

        for (final String key : cpPrivilegeMap.keySet())
        {
            try
            {
                final SecurityDataBean userGroupRoleProtectionGroupBean = new SecurityDataBean();
                final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = cpPrivilegeMap.get(key);

                if (siteUserRolePrivilegeBean.isRowDeleted())
                {
                    AppUtility.processDeletedPrivileges(siteUserRolePrivilegeBean);
                }
                else if (siteUserRolePrivilegeBean.isRowEdited())
                {
                    // Case for 'All Current & Future CP's selected
                    if (siteUserRolePrivilegeBean.isAllCPChecked())
                    {
                        final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();
                        final Site site = siteUserRolePrivilegeBean.getSiteList().get(0);

                        if (defaultRole != null
                                && (defaultRole.equalsIgnoreCase("-1") || defaultRole.equalsIgnoreCase("0") || defaultRole
                                        .equalsIgnoreCase("7")))
                        {
                            roleName = Constants.getCurrentAndFutureRoleName(site.getId(), user1.getCsmUserId(),
                                    defaultRole);
                        }
                        else
                        {
                            roleName = siteUserRolePrivilegeBean.getRole().getName();
                        }

                        protectionGroupName = Constants.getCurrentAndFuturePGAndPEName(site.getId());
                        createProtectionGroup(protectionGroupName, site, true);

                        userGroupRoleProtectionGroupBean.setGroupName(Constants.getSiteUserGroupName(site.getId(),
                                user1.getCsmUserId()));
                    }
                    else
                    {
                        final CollectionProtocol protocol = siteUserRolePrivilegeBean.getCollectionProtocol();
                        final String defaultRole = siteUserRolePrivilegeBean.getRole().getValue();

                        // if (defaultRole != null &&
                        // (defaultRole.equalsIgnoreCase("0") ||
                        // defaultRole.equalsIgnoreCase("-1") ||
                        // defaultRole.equalsIgnoreCase("7")))
                        // {
                        roleName = Constants.getCPRoleName(protocol.getId(), user1.getCsmUserId(), defaultRole);
                        // }
                        /*
                         * else { roleName =
                         * siteUserRolePrivilegeBean.getRole().getName(); }
                         */

                        protectionGroupName = CSMGroupLocator.getInstance().getPGName(protocol.getId(),
                                CollectionProtocol.class);
                        createProtectionGroup(protectionGroupName, protocol, false);

                        userGroupRoleProtectionGroupBean.setGroupName(Constants.getCPUserGroupName(protocol
                                .getId(), user1.getCsmUserId()));
                    }

                    final Set<String> privileges = new HashSet<String>();
                    final List<NameValueBean> privilegeList = siteUserRolePrivilegeBean.getPrivileges();

                    for (final NameValueBean privilege : privilegeList)
                    {
                        privileges.add(privilege.getValue());
                    }

                    AppUtility.processRole(roleName);

                    PrivilegeManager.getInstance().createRole(roleName, privileges);

                    final String userId = String.valueOf(user1.getCsmUserId());

                    final gov.nih.nci.security.authorization.domainobjects.User csmUser = getUserByID(userId);
                    final HashSet<gov.nih.nci.security.authorization.domainobjects.User> group = new HashSet<gov.nih.nci.security.authorization.domainobjects.User>();
                    group.add(csmUser);

                    userGroupRoleProtectionGroupBean.setUser("");
                    userGroupRoleProtectionGroupBean.setRoleName(roleName);
                    userGroupRoleProtectionGroupBean.setProtGrpName(protectionGroupName);
                    userGroupRoleProtectionGroupBean.setGroup(group);
                    authorizationData.add(userGroupRoleProtectionGroupBean);
                }
            }
            catch (final SMException e)
            {
                UserBizLogic.LOGGER.error(e.getMessage(), e);
            }
            catch (final ApplicationException e)
            {
                UserBizLogic.LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Creates the protection group.
     *
     * @param protectionGroupName
     *            the protection group name
     * @param obj
     *            the obj
     * @param isAllCPChecked
     *            the is all cp checked
     */
    private void createProtectionGroup(final String protectionGroupName, final AbstractDomainObject obj,
            final boolean isAllCPChecked)
    {
        ProtectionElement protElement = new ProtectionElement();
        final Set<ProtectionElement> peSet = new HashSet<ProtectionElement>();
        List<ProtectionElement> peList = new ArrayList<ProtectionElement>();
        final PrivilegeUtility privilegeUtility = new PrivilegeUtility();

        try
        {
            if (isAllCPChecked)
            {
                protElement.setObjectId(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
                protElement.setProtectionElementName(Constants.getCurrentAndFuturePGAndPEName(obj.getId()));
                protElement.setProtectionElementDescription("For All Current & Future CP's for Site with Id "
                        + obj.getId().toString());
                protElement.setApplication(privilegeUtility.getApplication(SecurityManagerPropertiesLocator
                        .getInstance().getApplicationCtxName()));
                final ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(
                        protElement);
                peList = privilegeUtility.getUserProvisioningManager().getObjects(searchCriteria);
                if (peList != null && !peList.isEmpty())
                {
                    protElement = peList.get(0);
                }
                else
                {
                    privilegeUtility.getUserProvisioningManager().createProtectionElement(protElement);
                }
                peList.add(protElement);
            }
            else
            {
                protElement.setObjectId(obj.getObjectId());
                final ProtectionElementSearchCriteria searchCriteria = new ProtectionElementSearchCriteria(
                        protElement);

                peList = privilegeUtility.getUserProvisioningManager().getObjects(searchCriteria);
            }
            final ProtectionGroup protGroup = new ProtectionGroup();
            protGroup.setProtectionGroupName(protectionGroupName);
            peSet.addAll(peList);
            protGroup.setProtectionElements(peSet);
            new PrivilegeUtility().getUserProvisioningManager().createProtectionGroup(protGroup);
        }
        catch (final Exception e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * Distribute map data.
     *
     * @param userRowIdMap
     *            the user row id map
     * @param cpPrivilegeMap
     *            the cp privilege map
     * @param sitePrivilegeMap
     *            the site privilege map
     */
    private void distributeMapData(final Map<String, SiteUserRolePrivilegeBean> userRowIdMap,
            final Map<String, SiteUserRolePrivilegeBean> cpPrivilegeMap,
            final Map<String, SiteUserRolePrivilegeBean> sitePrivilegeMap)
    {
        for (final String key : userRowIdMap.keySet())
        {
            final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);

            if (siteUserRolePrivilegeBean.getCollectionProtocol() == null
                    && siteUserRolePrivilegeBean.isAllCPChecked() == false)
            {
                sitePrivilegeMap.put(key, siteUserRolePrivilegeBean);
            }
            else
            {
                cpPrivilegeMap.put(key, siteUserRolePrivilegeBean);
            }
        }
    }

    /**
     * This method gets user by id.
     *
     * @param userId
     *            user Id
     *
     * @return user
     *
     * @throws SMException
     *             Generic SM Exception
     */
    private gov.nih.nci.security.authorization.domainobjects.User getUserByID(final String userId)
            throws SMException
    {
        final gov.nih.nci.security.authorization.domainobjects.User user = SecurityManagerFactory
                .getSecurityManager().getUserById(userId);

        return user;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#updateUser(edu.wustl.dao.DAO, java.lang.Object, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
    @Override
	public void updateUser(final DAO dao, final Object obj, final Object oldObj,
            final SessionDataBean sessionDataBean) throws BizLogicException
    {
        this.update(dao, obj, oldObj, sessionDataBean);
    }

    @Override
    protected void update(final DAO dao, final Object obj, final Object oldObj,
    		final SessionDataBean sessionDataBean) throws BizLogicException
    {
    	UserUIObject userUIObject = new UserUIObject();
    	update(dao, obj, oldObj,userUIObject,sessionDataBean);

    }



    /**
     * Updates the persistent object in the database.
     *
     * @param obj
     *            The object to be updated.
     * @param sessionDataBean
     *            The session in which the object is saved.
     * @param dao
     *            the dao
     * @param oldObj
     *            the old obj
     *
     * @throws BizLogicException
     *             BizLogic Exception
     */
    @Override
    protected void update(final DAO dao, final Object obj, final Object oldObj,Object uiObject,
            final SessionDataBean sessionDataBean) throws BizLogicException
    {
    	InstanceFactory<Password> instFact = DomainInstanceFactory.getInstanceFactory(Password.class);
        User user = null;
        UserUIObject userUIObject = (UserUIObject)uiObject;
        Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();

        if (obj instanceof UserDTO)
        {
            user = ((UserDTO) obj).getUser();
            userRowIdMap = ((UserDTO) obj).getUserRowIdBeanMap();
        }
        else
        {
            user = (User) obj;
        }
        // Method to populate rowIdMap in case, Add Privilege button is not
        // clicked
        userRowIdMap = getUserRowIdMap(user, userRowIdMap);

        final User oldUser = (User) oldObj;

        boolean isLoginUserUpdate = false;
        if (oldUser.getLoginName().equals(sessionDataBean.getUserName()))
        {
            isLoginUserUpdate = true;
        }

        // If the user is rejected, its record cannot be updated.
        if (Status.ACTIVITY_STATUS_REJECT.toString().equals(oldUser.getActivityStatus()))
        {

            throw getBizLogicException(null, "errors.editRejectedUser", "");
        }
        else if (Status.ACTIVITY_STATUS_NEW.toString().equals(oldUser.getActivityStatus())
                || Status.ACTIVITY_STATUS_PENDING.toString().equals(oldUser.getActivityStatus()))
        {
            // If the user is not approved yet, its record cannot be updated.
            throw getBizLogicException(null, "errors.editNewPendingUser", "");
        }

        try
        {
            // Get the csm userId if present.
            String csmUserId = null;

            /**
             * Santosh: Changes done for Api User should not edit the first time
             * login field.
             */
            if (user.getFirstTimeLogin() == null)
            {
                throw getBizLogicException(null, "domain.object.null.err.msg", "First Time Login");
            }

            if (oldUser.getFirstTimeLogin() != null
                    && user.getFirstTimeLogin().booleanValue() != oldUser.getFirstTimeLogin().booleanValue())
            {
                throw getBizLogicException(null, "errors.cannotedit.firsttimelogin", "");
            }

            if (user.getCsmUserId() != null)
            {
                csmUserId = user.getCsmUserId().toString();
            }

            final gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManagerFactory
                    .getSecurityManager().getUserById(csmUserId);

            // Bug:7979
            if (Constants.DUMMY_PASSWORD.equals(userUIObject.getNewPassword()))
            {
            	userUIObject.setNewPassword(csmUser.getPassword());
            }

            final String oldPassword = userUIObject.getOldPassword();
            // If the page is of change password,
            // update the password of the user in csm and catissue tables.
            if (Constants.PAGE_OF_CHANGE_PASSWORD.equals(userUIObject.getPageOf()))
            {
                if (!oldPassword.equals(csmUser.getPassword()))
                {
                    throw getBizLogicException(null, "errors.oldPassword.wrong", "");
                }

                // Added for Password validation by Supriya Dankh.
                final Validator validator = new Validator();
                if (!validator.isEmpty(userUIObject.getNewPassword()) && !validator.isEmpty(oldPassword))
                {
                    passwordValidation(user, oldUser, oldPassword, userUIObject);
                }
                csmUser.setPassword(userUIObject.getNewPassword());

                Password pass=instFact.createObject();
                pass.setPassword(PasswordManager.encrypt(userUIObject.getNewPassword()));
                pass.setUser(user);


                // Set values in password domain object and adds changed
                // password in Password Collection
                final Password password = instFact.createClone(pass);//new Password(PasswordManager.encrypt(user.getNewPassword()), user);
                /*if(user.getPasswordCollection()==null)
                {
                	user.setPasswordCollection(new HashSet<Password>());
                }*/
                dao.insert(password);
                //user.getPasswordCollection().add(password);

            }

            // Bug-1516: Jitendra Administartor should be able to edit the
            // password
            else if (Constants.PAGE_OF_USER_ADMIN.equals(userUIObject.getPageOf())
                    && !userUIObject.getNewPassword().equals(csmUser.getPassword()))
            {
                final Validator validator = new Validator();
                if (!validator.isEmpty(userUIObject.getNewPassword()))
                {
                    passwordValidation(user, oldUser, oldPassword,userUIObject);
                }
                csmUser.setPassword(userUIObject.getNewPassword());

                Password pass=instFact.createObject();
                pass.setPassword(PasswordManager.encrypt(userUIObject.getNewPassword()));
                pass.setUser(user);

                // Set values in password domain object and
                // adds changed password in Password Collection
                final Password password =instFact.createClone(pass);// new Password(PasswordManager.encrypt(user.getNewPassword()), user);
                dao.insert(password);
                //user.getPasswordCollection().add(password);
                user.setFirstTimeLogin(Boolean.TRUE);
            }
            else
            {
                csmUser.setLoginName(user.getLoginName());
                csmUser.setLastName(user.getLastName());
                csmUser.setFirstName(user.getFirstName());
                csmUser.setEmailId(user.getEmailAddress());

                // Assign Role only if the page is of Administrative user edit.
                if ((Constants.PAGE_OF_USER_PROFILE.equals(userUIObject.getPageOf()) == false)
                        && (Constants.PAGE_OF_CHANGE_PASSWORD.equals(userUIObject.getPageOf()) == false))
                {
                    String role =userUIObject.getRoleId();
                    if (role != null)
                    {
                        if (Constants.SUPER_ADMIN_USER.equalsIgnoreCase(role))
                        {
                            role = Constants.ADMIN_USER;
                        }
                        else
                        {
                            role = Constants.NON_ADMIN_USER;
                        }

                        SecurityManagerFactory.getSecurityManager().assignRoleToUser(
                                csmUser.getUserId().toString(), role);
                    }
                }

                if (userRowIdMap != null && !userRowIdMap.isEmpty())
                {
                    updateUserDetails(user, userRowIdMap);
                }

                final Vector<SecurityDataBean> authorizationData = new Vector<SecurityDataBean>();
                final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

                insertCPSitePrivileges(user, authorizationData, userRowIdMap);

                boolean flag = true;

                final Set<User> protectionObjects = new HashSet<User>();
                protectionObjects.add(user);
                final PrivilegeUtility privilegeUtility = new PrivilegeUtility();
                try
                {
                    final ProtectionElement protectionElement = privilegeUtility.getUserProvisioningManager()
                            .getProtectionElement(User.class.getName() + "_" + user.getId().toString());
                }
                catch (final CSObjectNotFoundException e)
                {
                    UserBizLogic.LOGGER.error(e.getMessage(), e);
                    flag = false;
                    privilegeManager.insertAuthorizationData(authorizationData, protectionObjects, null, user
                            .getObjectId());
                }
                if (flag)
                {
                    privilegeManager.insertAuthorizationData(authorizationData, null, null, user.getObjectId());
                }

                dao.update(user.getAddress(), oldUser.getAddress());

            }

            if (Constants.PAGE_OF_CHANGE_PASSWORD.equals(userUIObject.getPageOf()))
            {
                user.setFirstTimeLogin(Boolean.FALSE);
            }
            dao.update(user, oldUser);

            // Modify the csm user.
            SecurityManagerFactory.getSecurityManager().modifyUser(csmUser);

            if (isLoginUserUpdate)
            {
                sessionDataBean.setUserName(csmUser.getLoginName());
            }

           
   
        }
        catch (final SMException smExp)
        {
            UserBizLogic.LOGGER.error(smExp.getMessage(), smExp);
            throw getBizLogicException(smExp, "sm.operation.error", "Error in checking has privilege");
        }
        catch (final PasswordEncryptionException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
            throw getBizLogicException(e, "pwd.encrytion.error", "");
        }
        catch (final ApplicationException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
            // ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
            throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
        }
    }

    /**
     * This method validate password.
     *
     * @param user
     *            User object.
     * @param oldUser
     *            old User object
     * @param oldPassword
     *            old Password
     *
     * @throws PasswordEncryptionException
     *             Generic Password Encryption Exception
     * @throws BizLogicException
     *             BizLogic Exception
     */
    private void passwordValidation(final User user, final User oldUser, final String oldPassword,UserUIObject userUIObject)
            throws PasswordEncryptionException, ApplicationException
    {
        final int result = validatePassword(oldUser, userUIObject.getNewPassword(), oldPassword);

        LOGGER.debug("return from Password validate " + result);

        // if validatePassword method returns value greater than zero then
        // validation fails
        if (result != SUCCESS)
        {
            // get error message of validation failure
            final List<String> parameters = new ArrayList<String>();
            final String errorKey = getPasswordErrorMsg(result, parameters);
            final StringBuffer strBuf = new StringBuffer("");
            for (final String msgValue : parameters)
            {
                strBuf.append(msgValue).append(":");
            }

            LOGGER.debug("errorKey" + errorKey);
            throw getBizLogicException(null, errorKey, strBuf.toString());
        }
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getUsers(java.lang.String)
	 */
    @Override
	public Vector<NameValueBean> getUsers(final String operation) throws BizLogicException
    {
        final String sourceObjectName = User.class.getName();
        // Get only the fields required
        final String[] selectColumnName = { Constants.SYSTEM_IDENTIFIER, Constants.LASTNAME, Constants.FIRSTNAME };
        String[] whereColumnName;
        String[] whereColumnCondition;
        Object[] whereColumnValue;
        String joinCondition;
        if (operation != null && operation.equalsIgnoreCase(Constants.ADD))
        {
            final String tmpArray1[] = { Status.ACTIVITY_STATUS.toString() };
            final String tmpArray2[] = { Constants.EQUALS };
            final String tmpArray3[] = { Status.ACTIVITY_STATUS_ACTIVE.toString() };
            whereColumnName = tmpArray1;
            whereColumnCondition = tmpArray2;
            whereColumnValue = tmpArray3;
            joinCondition = null;
        }
        else
        {
            final String tmpArray1[] = { Status.ACTIVITY_STATUS.toString(), Status.ACTIVITY_STATUS.toString() };
            final String tmpArray2[] = { Constants.EQUALS, Constants.EQUALS };
            final String tmpArray3[] = { Status.ACTIVITY_STATUS_ACTIVE.toString(),
                    Status.ACTIVITY_STATUS_CLOSED.toString() };
            whereColumnName = tmpArray1;
            whereColumnCondition = tmpArray2;
            whereColumnValue = tmpArray3;
            joinCondition = Constants.OR_JOIN_CONDITION;
        }
        // Retrieve the users whose activity status is not disabled.
        final List users = this.retrieve(sourceObjectName, selectColumnName, whereColumnName,
                whereColumnCondition, whereColumnValue, joinCondition);

        final Vector<NameValueBean> nameValuePairs = new Vector<NameValueBean>();
        nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String
                .valueOf(Constants.SELECT_OPTION_VALUE)));

        // If the list of users retrieved is not empty.
        if (!users.isEmpty())
        {
            // Creating name value beans.
            for (int i = 0; i < users.size(); i++)
            {
                // Changes made to optimize the query to get only required
                // fields data
                final Object[] userData = (Object[]) users.get(i);
                final NameValueBean nameValueBean = new NameValueBean();
                nameValueBean.setName(userData[1] + ", " + userData[2]);
                nameValueBean.setValue(userData[0]);
                nameValuePairs.add(nameValueBean);
            }
        }
        Collections.sort(nameValuePairs);
        return nameValuePairs;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getCSMUsers()
	 */
    @Override
	public Vector<NameValueBean> getCSMUsers() throws BizLogicException, SMException
    {
        // Retrieve the users whose activity status is not disabled.
        final List users = SecurityManagerFactory.getSecurityManager().getUsers();

        final Vector<NameValueBean> nameValuePairs = new Vector<NameValueBean>();
        nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, String
                .valueOf(Constants.SELECT_OPTION_VALUE)));

        // If the list of users retrieved is not empty.
        if (!users.isEmpty())
        {
            // Creating name value beans.
            for (int i = 0; i < users.size(); i++)
            {
                final gov.nih.nci.security.authorization.domainobjects.User user = (gov.nih.nci.security.authorization.domainobjects.User) users
                        .get(i);
                final NameValueBean nameValueBean = new NameValueBean();
                nameValueBean.setName(user.getLastName() + ", " + user.getFirstName());
                nameValueBean.setValue(String.valueOf(user.getUserId()));
                LOGGER.debug(nameValueBean.toString());
                nameValuePairs.add(nameValueBean);
            }
        }

        Collections.sort(nameValuePairs);
        return nameValuePairs;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#retrieve(java.lang.String, java.lang.String, java.lang.Object)
	 */

	@Override
    public List retrieve(final String className, final String colName, final Object colValue)
            throws BizLogicException
    {
        List userList = null;
        LOGGER.debug("In user biz logic retrieve........................");
        try
        {
            // Get the caTISSUE user.
            userList = super.retrieve(className, colName, colValue);

            User appUser = null;
            if (userList != null && !userList.isEmpty())
            {
                appUser = (User) userList.get(0);

                if (appUser.getCsmUserId() != null)
                {
                    // Get the role of the user.
                    final Role role = SecurityManagerFactory.getSecurityManager().getUserRole(
                            appUser.getCsmUserId().longValue());
                    // logger.debug("In USer biz logic.............role........id......."
                    // + role.getId().toString());

                    if (role != null)
                    {
//                        appUser.setRoleId(role.getId().toString());
                    }
                }
            }
        }
        catch (final SMException smExp)
        {
            UserBizLogic.LOGGER.error(smExp.getMessage(), smExp);
            throw getBizLogicException(smExp, "sm.check.priv", "");
        }

        return userList;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#sendForgotPassword(java.lang.String, edu.wustl.common.beans.SessionDataBean)
	 */
    @Override
	public String sendForgotPassword(final String emailAddress, final SessionDataBean sessionData)
            throws BizLogicException
    {
        DAO dao = null;
        try
        {
            String statusMessageKey = null;
            final List list = this.retrieve(User.class.getName(), "emailAddress", emailAddress);
            if (list != null && !list.isEmpty())
            {
                final User user = (User) list.get(0);
                if (user.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
                {
                    final EmailHandler emailHandler = new EmailHandler();

                    // Send the login details email to the user.
                    boolean emailStatus = false;

                    emailStatus = emailHandler.sendLoginDetailsEmail(user, null);

                    if (emailStatus)
                    {
                        // if success commit
                        /**
                         * Update the field FirstTimeLogin which will ensure
                         * user changes his password on login Note --> We can
                         * not use CommonAddEditAction to update as the user has
                         * not still logged in and user authorisation will fail.
                         * So writing saperate code for update.
                         */

                        user.setFirstTimeLogin(Boolean.TRUE);
                        dao = openDAOSession(sessionData);
                        dao.update(user);
                        dao.commit();

                        statusMessageKey = "password.send.success";
                    }
                    else
                    {
                        statusMessageKey = "password.send.failure";
                    }
                }
                else
                {
                    // Error key if the user is not active.
                    statusMessageKey = "errors.forgotpassword.user.notApproved";
                }
            }
            else
            {
                // Error key if the user is not present.
                statusMessageKey = "errors.forgotpassword.user.unknown";
            }
            return statusMessageKey;

        }
        catch (final DAOException daoExp)
        {
            UserBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
        catch (final ApplicationException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
            throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
        }
        finally
        {
            closeDAOSession(dao);
        }
    }


    //@Override
    protected boolean validate(final Object obj, final DAO dao, final String operation) throws BizLogicException
    {
    	UserUIObject userUIObject = new UserUIObject();
    	return validate(obj,dao,operation,userUIObject);
    }

    /**
     * Overriding the parent class's method to validate the enumerated attribute
     * values.
     *
     * @param obj
     *            the obj
     * @param dao
     *            the dao
     * @param operation
     *            the operation
     *
     * @return true, if validate
     *
     * @throws BizLogicException
     *             the biz logic exception
     */
    protected boolean validate(final Object obj, final DAO dao, final String operation,Object uiObject) throws BizLogicException
    {
    	UserUIObject userUIObject = (UserUIObject)uiObject;
        User user = null;
        if (obj instanceof UserDTO)
        {
            user = ((UserDTO) obj).getUser();
        }
        else
        {
            user = (User) obj;
        }

        if (Constants.PAGE_OF_SIGNUP.equals(userUIObject.getPageOf()))
        {
            if (!Validator.isEmpty(userUIObject.getTargetIdp()))
            {
                validateIDPInformation(user,userUIObject);
            }
        }
        /**
         * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
         * Search, previoulsy it was failing since there was default class level
         * initialization on domain object. For example in User object, it was
         * initialized as protected String lastName=""; So we removed default
         * class level initialization on domain object and are initializing in
         * method setAllValues() of domain object. But in case of Api Search,
         * default values will not get set since setAllValues() method of
         * domainObject will not get called. To avoid null pointer exception, we
         * are setting the default values same as we were setting in
         * setAllValues() method of domainObject.
         */
        ApiSearchUtil.setUserDefault(user);
        if (userUIObject.getPageOf()!=null && Constants.PAGE_OF_CHANGE_PASSWORD.equals(userUIObject.getPageOf()) == false)
        {
            // if condition added by Geeta for ECMC
            if ((user.getAddress().getState() != "null" && user.getAddress().getState() != "")
                    && edu.wustl.catissuecore.util.global.Variables.isStateRequired)
            {
                if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
                        Constants.CDE_NAME_STATE_LIST, null), user.getAddress().getState()))
                {

                    throw getBizLogicException(null, "state.errMsg", "");
                }
            }
            if (!Validator.isEnumeratedValue(CDEManager.getCDEManager().getPermissibleValueList(
                    Constants.CDE_NAME_COUNTRY_LIST, null), user.getAddress().getCountry()))
            {
                throw getBizLogicException(null, "country.errMsg", "");
            }

            if (Constants.PAGE_OF_USER_ADMIN.equals(userUIObject.getPageOf()))
            {
                if (operation.equals(Constants.ADD))
                {
                    if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(user.getActivityStatus()))
                    {
                        throw getBizLogicException(null, "activityStatus.active.errMsg", "");
                    }
                }
                else
                {
                    if (!Validator.isEnumeratedValue(Constants.USER_ACTIVITY_STATUS_VALUES, user
                            .getActivityStatus()))
                    {
                        throw getBizLogicException(null, "activityStatus.errMsg", "");
                    }
                }
            }

            // Added by Ashish
            /**
             * Two more parameter 'dao' and 'operation' is added by Vijay Pande
             * to use it in isUniqueEmailAddress method
             */
            apiValidate(user, dao, operation);
            // END
        }
        return true;
    }

    private void validateIDPInformation(final User user,UserUIObject userUIObject) throws BizLogicException
    {
        validateForEmptyMigrationFields(user,userUIObject);
        validateMigrationRule(user,userUIObject);
        authenticateMigrationCredentials(user,userUIObject);
    }

    private void validateMigrationRule(final User user,UserUIObject userUIObject) throws BizLogicException
    {
        final UserDetails userDetails = new UserDetails();
        userDetails.setLoginName(user.getLoginName());
        userDetails.setMigratedLoginName(userUIObject.getTargetIdpLoginName());
        userDetails.setTargetIDP(userUIObject.getTargetIdp());
        try
        {
            final boolean statisfiesRule=Utility.isStatisfyMigrationRule(userDetails);
            if(!statisfiesRule)
            {
                throw getBizLogicException(null, "app.migration.rule.check.error", "");
            }
        }
        catch (final MigrationRuleException e)
        {
            LOGGER.debug(e);
            throw getBizLogicException(null, "app.migration.rule.check.error", "");
        }
    }

    public SAMLAssertion getSAMLAssertion(final User user,UserUIObject userUIObject) throws BizLogicException
    {

    	IDPAuthManager authManager = null;
			try {
				authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
						userUIObject.getTargetIdp());
			} catch (AuthenticationException e) {
				LOGGER.debug(e);
				throw getBizLogicException(null, "app.target.idp.auth.error", "");
			}

            final LoginCredentials loginCredentials = new LoginCredentials();
            loginCredentials.setLoginName(userUIObject.getTargetIdpLoginName());
            loginCredentials.setPassword(userUIObject.getTargetPassword());
           // String identity = null;
            SAMLAssertion saml = null;
			try {
				saml = authManager.getSAMLAssertion(loginCredentials);
			} catch (AuthenticationException e) {
                LOGGER.debug("Authentication of user against " + userUIObject.getTargetIdp() + " failed.");
                throw getBizLogicException(null, "errors.incorrectLoginIDPassword", userUIObject.getTargetIdp());
			}
            
            if (saml == null)
            {
                LOGGER.debug("Authentication of user against " + userUIObject.getTargetIdp() + " failed.");
                throw getBizLogicException(null, "errors.incorrectLoginIDPassword", userUIObject.getTargetIdp());
            }
            boolean grouperUser = false ;
            if (!StringUtilities.isBlank(userUIObject.getGrouperUser())) {
            	grouperUser = true;
            }
            if (grouperUser) {
            	boolean isPresent = false;
				try {
					isPresent = GridGrouperUtil.isIdentityPresentInGridGrouper(userUIObject.getTargetIdpLoginName());
				} catch (Exception e) {
					throw getBizLogicException(null, "app.target.loginname.notingrouper", userUIObject.getTargetIdp());
				}
            	if (!isPresent) {
            		throw getBizLogicException(null, "app.target.loginname.notingrouper", userUIObject.getTargetIdp());
            	}
            }
            return saml;

    }
    
    private void authenticateMigrationCredentials(final User user,UserUIObject userUIObject) throws BizLogicException
    {
        try
        {
            final IDPAuthManager authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
            		userUIObject.getTargetIdp());

            final LoginCredentials loginCredentials = new LoginCredentials();
            loginCredentials.setLoginName(userUIObject.getTargetIdpLoginName());
            loginCredentials.setPassword(userUIObject.getTargetPassword());
            final boolean authenticationSuccesful = authManager.authenticate(loginCredentials);
            if (!authenticationSuccesful)
            {
                LOGGER.debug("Authentication of user against " + userUIObject.getTargetIdp() + " failed.");
                throw getBizLogicException(null, "errors.incorrectLoginIDPassword", "");
            }
        }
        catch (final AuthenticationException e)
        {
            LOGGER.debug(e);
            throw getBizLogicException(null, "app.target.idp.auth.error", "");
        }
    }
    

    private void validateForEmptyMigrationFields(final User user,UserUIObject userUIObject) throws BizLogicException
    {
        if (Validator.isEmpty(userUIObject.getTargetIdpLoginName()))
        {
            if (Validator.isEmpty(userUIObject.getTargetPassword()))
            {
                throw getBizLogicException(null, "app.target.loginname.password.required", "");
            }
            else
            {
                throw getBizLogicException(null, "errors.item.required", "user.loginName");
            }
        }
        else if (Validator.isEmpty(userUIObject.getTargetPassword()))
        {
            throw getBizLogicException(null, "errors.item.required", "user.password");
        }
    }

    // Added by Ashish
    /**
     * Api validate.
     *
     * @param user
     *            user
     * @param dao
     *            the dao
     * @param operation
     *            the operation
     *
     * @return true, if api validate
     *
     * @throws BizLogicException
     *             the biz logic exception
     */

    private boolean apiValidate(final User user, final DAO dao, final String operation) throws BizLogicException
    {
        final Validator validator = new Validator();
        String message = "";
        final boolean validate = true;

        if (validator.isEmpty(user.getEmailAddress()))
        {
            message = ApplicationProperties.getValue(USER_EMAIL_ADDRESS);
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }
        else
        {
            if (!validator.isValidEmailAddress(user.getEmailAddress()))
            {
                message = ApplicationProperties.getValue(USER_EMAIL_ADDRESS);
                throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
            }
            /**
             * Name : Vijay_Pande Reviewer : Sntosh_Chandak Bug ID: 4185_2 Patch
             * ID: 1-2 See also: 1 Description: Wrong error meassage was
             * dispayed while adding user with existing email address in use.
             * Following method is provided to verify whether the email address
             * is already present in the system or not.
             */
            if (!(isUniqueEmailAddress(user.getEmailAddress(), user.getId(), dao, operation)))
            {
                String arguments[] = null;
                arguments = new String[] { "User", ApplicationProperties.getValue(USER_EMAIL_ADDRESS) };
                final String errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.ConstraintViolation",
                        arguments);
                LOGGER.debug("Unique Constraint Violated: " + errMsg);
                throw getBizLogicException(null, "Err.ConstraintViolation", "User :"
                        + ApplicationProperties.getValue(USER_EMAIL_ADDRESS));
            }
//            if (null != user.getTargetIdpLoginName() && !"".equals(user.getTargetIdpLoginName()))
//            {
//                if (!user.getEmailAddress().endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU)
//                        && !user.getEmailAddress().endsWith(
//                                edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU_CAPS))
//                {
//                    message = ApplicationProperties.getValue("email.washu.user");
//                    throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
//                }
//            }
        }
        if (validator.isEmpty(user.getLastName()))
        {
            message = ApplicationProperties.getValue("user.lastName");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }
        else if (validator.isXssVulnerable(user.getLastName()))
        {
            message = ApplicationProperties.getValue("user.lastName");
            throw getBizLogicException(null, "errors.xss.invalid", message);
        }

        if (validator.isEmpty(user.getFirstName()))
        {
            message = ApplicationProperties.getValue("user.firstName");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }
        else if (validator.isXssVulnerable(user.getFirstName()))
        {
            message = ApplicationProperties.getValue("user.firstName");
            throw getBizLogicException(null, "errors.xss.invalid", message);
        }

        if (validator.isEmpty(user.getAddress().getCity()))
        {
            message = ApplicationProperties.getValue("user.city");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }
        if (edu.wustl.catissuecore.util.global.Variables.isStateRequired
                && (!validator.isValidOption(user.getAddress().getState()) || validator.isEmpty(user.getAddress()
                        .getState())))
        {
            message = ApplicationProperties.getValue("user.state");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }
        if (!validator.isValidOption(user.getAddress().getCountry())
                || validator.isEmpty(user.getAddress().getCountry()))
        {
            message = ApplicationProperties.getValue("user.country");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }
        if (user.getInstitution().getId() == null || user.getInstitution().getId().longValue() <= 0)
        {
            message = ApplicationProperties.getValue("user.institution");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }

        if (user.getDepartment().getId() == null || user.getDepartment().getId().longValue() <= 0)
        {
            message = ApplicationProperties.getValue("user.department");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }

        if (user.getCancerResearchGroup().getId() == null
                || user.getCancerResearchGroup().getId().longValue() <= 0)
        {
            message = ApplicationProperties.getValue("user.cancerResearchGroup");
            throw getBizLogicException(null, ERRORS_ITEM_REQUIRED, message);
        }
        return validate;
    }

    /**
     * Validate password.
     *
     * @param oldUser
     *            User object
     * @param newPassword
     *            New Password value
     * @param oldPassword
     *            Old Password value
     *
     * @return SUCCESS (constant int 0) if all condition passed else return
     *         respective error code (constant int) value
     *
     * @throws PasswordEncryptionException
     *             the password encryption exception
     *             @throws PasswordEncryptionException
     *             the password encryption exception
     */
    private int validatePassword(final User oldUser, final String newPassword, final String oldPassword)
            throws PasswordEncryptionException, ApplicationException
    {
    	final ArrayList<Password> oldPwdList = getPasswordCollection(oldUser);
        //Collections.sort(oldPwdList,new PasswordComparator());
      // Collections.sort(oldPwdList);
        if (oldPwdList != null && !oldPwdList.isEmpty())
        {
            // Check new password is equal to last n password if value
            final String encryptedPassword = PasswordManager.encrypt(newPassword);
            if (checkPwdNotSameAsLastN(encryptedPassword, oldPwdList))
            {
                LOGGER.debug("Password is not valid returning FAIL_SAME_AS_LAST_N");
                return FAIL_SAME_AS_LAST_N;
            }

            // Get the last updated date of the password
            final Date lastestUpdateDate = oldPwdList.get(0).getUpdateDate();
            final boolean firstTimeLogin = getFirstLogin(oldUser);
            if (!firstTimeLogin && checkPwdUpdatedOnSameDay(lastestUpdateDate))
            {

                LOGGER.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
                return FAIL_CHANGED_WITHIN_SOME_DAY;
            }

            /**
             * to check password does not contain user name,surname,email
             * address. if same return FAIL_SAME_NAME_SURNAME_EMAIL eg.
             * username=XabcY@abc.com newpassword=abc is not valid
             */

            String emailAddress = oldUser.getEmailAddress();
            final int usernameBeforeMailaddress = emailAddress.indexOf('@');
            // get substring of emailAddress before '@' character
            emailAddress = emailAddress.substring(0, usernameBeforeMailaddress);
            final String userFirstName = oldUser.getFirstName();
            final String userLastName = oldUser.getLastName();

            final StringBuffer sb = new StringBuffer(newPassword);
            if (emailAddress != null && newPassword.toLowerCase().indexOf(emailAddress.toLowerCase()) != -1)
            {
                LOGGER.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
                return FAIL_SAME_NAME_SURNAME_EMAIL;
            }

            if (userFirstName != null && newPassword.toLowerCase().indexOf(userFirstName.toLowerCase()) != -1)
            {
                LOGGER.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
                return FAIL_SAME_NAME_SURNAME_EMAIL;
            }

            if (userLastName != null && newPassword.toLowerCase().indexOf(userLastName.toLowerCase()) != -1)
            {
                LOGGER.debug("Password is not valid returning FAIL_SAME_NAME_SURNAME_EMAIL");
                return FAIL_SAME_NAME_SURNAME_EMAIL;
            }

        }
        return SUCCESS;
    }

    public String getMigrationStatus(User user) throws ApplicationException
    {
    	
		String sql="select MIGRATION_STATUS from csm_migrate_user where LOGIN_NAME='"+user.getLoginName()+"'";
		
		List<List<Object>> migrationList=null;
		
		migrationList=AppUtility.executeSQLQuery(sql);
		if(migrationList!=null && !migrationList.isEmpty())
		{
			List<Object> objectList=(List<Object>) migrationList.get(0);
			if(objectList!=null && !objectList.isEmpty())
			{
				return (String) objectList.get(0);
			}
	    }
		return "";
    }
    
    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#checkFirstLoginAndExpiry(edu.wustl.catissuecore.domain.User)
	 */
    @Override
	public String checkFirstLoginAndExpiry(final User user) throws ApplicationException
    {
    	try
    	{
    		// if user migrated, then no need to check for expiry
    		if(!getMigrationStatus(user).equals(MigrationState.MIGRATED.toString()))
    		{
	    		final ArrayList<Password> passwordList = new ArrayList(getPasswordCollection(user));
	    		final boolean firstTimeLogin = getFirstLogin(user);
		        // If user has logged in for the first time, return key of Change
		        // password on first login
		        if (firstTimeLogin)
		        {
		            return "errors.changePassword.changeFirstLogin";
		        }
		        //Collections.sort(passwordList,new PasswordComparator());
		        
		        	Collections.sort(passwordList);
			        final Password lastPassword = passwordList.get(0);
			        final Date lastUpdateDate = lastPassword.getUpdateDate();
			
			        final Validator validator = new Validator();
			        // Get difference in days between last password update date and current
			        // date.
			        final long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
			        final int expireDaysCount = Integer.parseInt(XMLPropertyHandler.getValue("password.expire_after_n_days"));
			        LOGGER.info("expireDaysCount" + expireDaysCount);
			        LOGGER.info("dayDiff" + dayDiff);
			        if (dayDiff > expireDaysCount)
			        {
			            LOGGER.info("returning error change password expire");
			            return "errors.changePassword.expire";
			        }
	    		}
		        
		 }
        catch (final ApplicationException exp)
		{
			throw exp;
		}
        return Constants.SUCCESS;

    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getFirstLogin(edu.wustl.catissuecore.domain.User)
	 */
    @Override
	public boolean getFirstLogin(final User user)
    {
        boolean firstTimeLogin = false;
        if (user.getFirstTimeLogin() != null)
        {
            firstTimeLogin = user.getFirstTimeLogin().booleanValue();
        }
        return firstTimeLogin;
    }

    /**
     * Check pwd not same as last n.
     *
     * @param newPassword
     *            newPassword
     * @param oldPwdList
     *            oldPwdList
     *
     * @return boolean
     */
    private boolean checkPwdNotSameAsLastN(final String newPassword, final List<Password> oldPwdList)
    {
        int noOfPwdNotSameAsLastN = 0;
        final String pwdNotSameAsLastN = XMLPropertyHandler.getValue("password.not_same_as_last_n");
        if (pwdNotSameAsLastN != null && !pwdNotSameAsLastN.equals(""))
        {
            noOfPwdNotSameAsLastN = Integer.parseInt(pwdNotSameAsLastN);
            noOfPwdNotSameAsLastN = Math.max(0, noOfPwdNotSameAsLastN);
        }

        boolean isSameFound = false;
        final int loopCount = Math.min(oldPwdList.size(), noOfPwdNotSameAsLastN);
        for (int i = loopCount; i < 0; i--)
        {
            final Password pasword = oldPwdList.get(i);
            if (newPassword.equals(pasword.getPassword()))
            {
                isSameFound = true;
                break;
            }
        }
        return isSameFound;
    }

    /**
     * Check pwd updated on same day.
     *
     * @param lastUpdateDate
     *            Date last updated
     *
     * @return boolean
     */
    private boolean checkPwdUpdatedOnSameDay(final Date lastUpdateDate)
    {
        final Validator validator = new Validator();
        // Get difference in days between last password update date and current
        // date.
        final long dayDiff = validator.getDateDiff(lastUpdateDate, new Date());
        final int dayDiffConstant = Integer.parseInt(XMLPropertyHandler.getValue("daysCount"));
        if (dayDiff < dayDiffConstant)
        {
            LOGGER.debug("Password is not valid returning FAIL_CHANGED_WITHIN_SOME_DAY");
            return true;
        }
        return false;
    }

    /**
     * Gets the password error msg.
     *
     * @param errorCode
     *            int value return by validatePassword() method
     * @param parameters
     *            List of strings
     *
     * @return String error message with respect to error code
     */
    private String getPasswordErrorMsg(final int errorCode, final List<String> parameters)
    {
        String errMsg = "";
        switch (errorCode)
        {
        case FAIL_SAME_AS_LAST_N:
            final String dayCount = String.valueOf(Integer.parseInt(XMLPropertyHandler
                    .getValue("password.not_same_as_last_n")));
            parameters.add(dayCount);
            errMsg = "errors.newPassword.sameAsLastn";
            break;
        case FAIL_FIRST_LOGIN:
            errMsg = "errors.changePassword.changeFirstLogin";
            break;
        case FAIL_EXPIRE:
            errMsg = "errors.changePassword.expire";
            break;
        case FAIL_CHANGED_WITHIN_SOME_DAY:
            final Integer daysCount = Integer.parseInt(XMLPropertyHandler.getValue("daysCount"));
            parameters.add(daysCount.toString());
            if (daysCount.intValue() == 1)
            {
                // errMsg =
                // ApplicationProperties.getValue("errors.changePassword.sameDay");
                errMsg = "errors.changePassword.sameDay";
            }
            else
            {
                errMsg = "errors.changePassword.afterSomeDays";
            }
            break;
        case FAIL_SAME_NAME_SURNAME_EMAIL:
            errMsg = "errors.changePassword.sameAsNameSurnameEmail";
            break;
        case FAIL_PASSWORD_EXPIRED:
            errMsg = "errors.changePassword.expire";
        default:
            errMsg = "errors.newPassword.genericmessage";
            break;
        }
        return errMsg;
    }

    /**
     * Name : Vijay_Pande Reviewer : Sntosh_Chandak Bug ID: 4185_2 Patch ID: 1-2
     * See also: 1 Description: Wrong error meassage was dispayed while adding
     * user with existing email address in use. Following method is provided to
     * verify whether the email address is already present in the system or not.
     *
     * @param emailAddress
     *            the email address
     * @param userId
     *            the user id
     * @param dao
     *            the dao
     * @param operation
     *            the operation
     *
     * @return true, if checks if is unique email address
     *
     * @throws BizLogicException
     *             the biz logic exception
     */
    /**
     * Method to check whether email address already exist or not
     *
     * @param emailAddress
     *            email address to be check
     * @param dao
     *            an object of DAO
     * @param userId
     *            User identifier
     * @return isUnique boolean value to indicate presence of similar email
     *         address
     * @throws BizLogicException
     *             database exception
     */
    private boolean isUniqueEmailAddress(final String emailAddress, final Long userId, final DAO dao,
            final String operation) throws BizLogicException
    {
        boolean isUnique = true;
        try
        {
            final String sourceObjectName = User.class.getName();
            final String[] selectColumnName = new String[] { "id", "emailAddress" };

            final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
            queryWhereClause.addCondition(new EqualClause("emailAddress", emailAddress));

            final List userList = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);

            if (!userList.isEmpty())
            {
                final Object[] objects = (Object[]) userList.get(0);

                if (operation.equalsIgnoreCase(Constants.ADD))
                {
                    isUnique = false;
                }
                else
                {
                    if (emailAddress.equals(objects[1]) && !userId.equals(objects[0]))
                    {
                        isUnique = false;
                    }
                }

            }

        }
        catch (final DAOException daoExp)
        {
            UserBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
        return isUnique;
    }

    /**
     * Set Role to user object before populating actionForm out of it.
     *
     * @param domainObj
     *            object of AbstractDomainObject
     * @param uiForm
     *            object of the class which implements IValueObject
     *
     * @throws BizLogicException
     *             the biz logic exception
     */
    @Override
    protected void prePopulateUIBean(final AbstractDomainObject domainObj, final IValueObject uiForm)
            throws BizLogicException
    {
        UserBizLogic.LOGGER.info("Inside prePopulateUIBean method of UserBizLogic...");

        final User user = (User) domainObj;
        Role role = null;
        if (user.getCsmUserId() != null)
        {
            try
            {
                // Get the role of the user.
                role = SecurityManagerFactory.getSecurityManager().getUserRole(user.getCsmUserId().longValue());
                if (role != null)
                {
//                    user.setRoleId(role.getId().toString());
                }
            }
            catch (final SMException e)
            {
                UserBizLogic.LOGGER.error("SMException in " + "prePopulateUIBean method of UserBizLogic..."
                        + e.getMessage(), e);
            }
        }
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getRelatedCPIds(java.lang.Long, boolean)
	 */

    @Override
	public Set<Long> getRelatedCPIds(final Long userId, final boolean isCheckForCPBasedView)
            throws BizLogicException
    {
        DAO dao = null;
        Collection<CollectionProtocol> userCpCollection = new HashSet<CollectionProtocol>();
        Collection<CollectionProtocol> userColl;
        Set<Long> cpIds = new HashSet<Long>();

        try
        {
            dao = getHibernateDao(getAppName(), null);
            final User user = (User) dao.retrieveById(User.class.getName(), userId);
            userColl = user.getCollectionProtocolCollection();
            userCpCollection = user.getAssignedProtocolCollection();

            if (Constants.ADMIN_USER.equalsIgnoreCase(UserUtility.getRoleId(user)))
            {
                cpIds = null;
            }
            else
            {
                if (isCheckForCPBasedView)
                {
                    final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
                    final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());

                    for (final CollectionProtocol collectionProtocol : userCpCollection)
                    {
                        final String privilegeName = edu.wustl.common.util.global.Variables.privilegeDetailsMap
                                .get(Constants.EDIT_PROFILE_PRIVILEGE);
                        if (privilegeCache.hasPrivilege(collectionProtocol.getObjectId(), privilegeName)
                                || collectionProtocol.getPrincipalInvestigator().getLoginName().equals(
                                        user.getLoginName()))
                        {
                            cpIds.add(collectionProtocol.getId());
                        }
                    }
                }
                else
                {
                    for (final CollectionProtocol collectionProtocol : userCpCollection)
                    {
                        cpIds.add(collectionProtocol.getId());
                    }
                }

                for (final CollectionProtocol cp : userColl)
                {
                    cpIds.add(cp.getId());
                }
            }
        }
        catch (final DAOException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
        }
        catch (final SMException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
        }
        finally
        {
            closeDAOSession(dao);
        }

        return cpIds;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getRelatedSiteIds(java.lang.Long)
	 */
    @Override
	public Set<Long> getRelatedSiteIds(final Long userId) throws BizLogicException
    {
        DAO dao = null;

        HashSet<Long> idSet = null;

        try
        {
            dao = openDAOSession(null);

            String query = "select user.csmUserId " + " from edu.wustl.catissuecore.domain.User user "
                    + " where user.id = " + userId;

            final List<Long> userList = executeQuery(query);
            boolean isAdminUser = false;
            if (!userList.isEmpty())
            {
                final Long csmUserId = userList.get(0);
                final Role role = SecurityManagerFactory.getSecurityManager().getUserRole(csmUserId);
                if (role != null && role.getId() != null
                        && Constants.ADMIN_USER.equalsIgnoreCase(role.getId().toString()))
                {
                    isAdminUser = true;
                }
            }
            if (!isAdminUser)
            {
                query = "select user.siteCollection.id " + " from edu.wustl.catissuecore.domain.User user "
                        + " where user.id = " + userId;
                final List<Long> list = executeQuery(query);

                idSet = new HashSet<Long>();

                for (final Long siteId : list)
                {
                    idSet.add(siteId);
                }

            }
        }
        catch (final ApplicationException e1)
        {
            UserBizLogic.LOGGER.error(e1.getMessage(), e1);
            throw getBizLogicException(e1, e1.getErrorKeyName(), e1.getMsgValues());
        }
        finally
        {
            closeDAOSession(dao);
        }

        return idSet;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getObjectId(edu.wustl.dao.DAO, java.lang.Object)
	 */

	@Override
    public String getObjectId(final DAO dao, final Object domainObject)
    {
        User user = null;
        UserDTO userDTO = null;
        Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
        final Collection<Site> siteCollection = new ArrayList<Site>();

        if (domainObject instanceof UserDTO)
        {
            userDTO = (UserDTO) domainObject;
            user = userDTO.getUser();
            userRowIdMap = userDTO.getUserRowIdBeanMap();
        }
        else
        {
            user = (User) domainObject;
        }

        if (Constants.SUPER_ADMIN_USER.equals(UserUtility.getRoleId(user)))
        {
            return Constants.cannotCreateSuperAdmin;
        }
        if (userRowIdMap == null)
        {
            if (UserUtility.getRoleId(user) == null || UserUtility.getRoleId(user).equals("") || user.getSiteCollection() == null
                    || user.getSiteCollection().isEmpty())
            {
                return Constants.siteIsRequired;
            }
            try
            {
                userRowIdMap = getUserRowIdMap(user, userRowIdMap);
            }
            catch (final BizLogicException e)
            {
                UserBizLogic.LOGGER.error(e.getMessage(), e);
            }
        }

        if (userRowIdMap != null)
        {
            final Object[] mapKeys = userRowIdMap.keySet().toArray();
            for (final Object mapKey : mapKeys)
            {
                final String key = mapKey.toString();
                final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = userRowIdMap.get(key);

                siteCollection.add(siteUserRolePrivilegeBean.getSiteList().get(0));
            }
        }
        final StringBuffer sb = new StringBuffer();
        boolean hasUserProvisioningPrivilege = false;

        if (siteCollection != null && !siteCollection.isEmpty())
        {
            sb.append(Constants.SITE_CLASS_NAME);
            for (final Site site : siteCollection)
            {
                if (site.getId() != null)
                {
                    sb.append(Constants.UNDERSCORE).append(site.getId());
                    hasUserProvisioningPrivilege = true;
                }
            }
        }
        if (hasUserProvisioningPrivilege)
        {
            return sb.toString();
        }

        return null;
    }

    /**
     * To get PrivilegeName for authorization check from
     * 'PermissionMapDetails.xml' (non-Javadoc)
     *
     * @param domainObject
     *            the domain object
     *
     * @return the privilege key
     *
     * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
     */
    @Override
    protected String getPrivilegeKey(final Object domainObject)
    {
        return Constants.ADD_EDIT_USER;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#checkUser(java.lang.Object, edu.wustl.common.beans.SessionDataBean, java.lang.Object)
	 */
    @Override
	public boolean checkUser(final Object domainObject, final SessionDataBean sessionDataBean,Object uiObject)
            throws BizLogicException
    {
    	UserUIObject userUIObject = (UserUIObject)uiObject;
        User user = null;
        UserDTO userDTO = null;
        Map<String, SiteUserRolePrivilegeBean> userRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();

        if (sessionDataBean != null && sessionDataBean.isAdmin())
        {
            return true;
        }
        if (domainObject instanceof User)
        {
            user = (User) domainObject;
        }
        if (domainObject instanceof UserDTO)
        {
            userDTO = (UserDTO) domainObject;
            user = userDTO.getUser();
            //if (user.getRoleId().equals(Constants.SUPER_ADMIN_USER))
            if (Constants.SUPER_ADMIN_USER.equals(userUIObject.getRoleId()))
            {
                throw getBizLogicException(null, "user.cannotCreateSuperAdmin", "");
            }
            userRowIdMap = userDTO.getUserRowIdBeanMap();
            if (userRowIdMap == null)
            {

                if (Constants.NON_ADMIN_USER.equals(userUIObject.getRoleId()))
                {

                    throw getBizLogicException(null, "user.cannotCreateScientist", "");
                }
                if (userUIObject.getRoleId() == null || userUIObject.getRoleId().equals("") || user.getSiteCollection() == null
                        || user.getSiteCollection().isEmpty())
                {

                    throw getBizLogicException(null, "user.siteIsRequired", "");
                }
            }
            else
            {
                final Object[] mapKeys = userRowIdMap.keySet().toArray();
                for (final Object mapKey : mapKeys)
                {
                    final String key = mapKey.toString();
                    final SiteUserRolePrivilegeBean bean = userRowIdMap.get(key);

                    if (bean.getSiteList() == null || bean.getSiteList().isEmpty())
                    {
                        throw getBizLogicException(null, "user.cannotCreateScientist", "");
                    }
                }

            }
        }
        if (userUIObject.getPageOf()!=null && userUIObject.getPageOf().equalsIgnoreCase("pageOfChangePassword"))
        {
            return true;
        }
        /*
         * if(user.getId().equals(sessionDataBean.getUserId())) { throw new
         * DAOException
         * (ApplicationProperties.getValue("user.cannotEditOwnPrivileges")); }
         */
        if (userUIObject.getPageOf()!=null && userUIObject.getPageOf().equalsIgnoreCase("pageOfSignUp"))
        {
            return true;
        }
        if (sessionDataBean != null && user.getLoginName().equals(sessionDataBean.getUserName()))
        {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#isAuthorized(edu.wustl.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
    @Override
	public boolean isAuthorized(final DAO dao, final Object domainObject, final SessionDataBean sessionDataBean)
    throws BizLogicException
    {
    	UserUIObject userUIObject = new UserUIObject();
    	return isAuthorized(dao,domainObject,sessionDataBean,userUIObject);

    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#isAuthorized(edu.wustl.dao.DAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean, java.lang.Object)
	 */
    @Override
	public boolean isAuthorized(final DAO dao, final Object domainObject, final SessionDataBean sessionDataBean,Object uiObject)
            throws BizLogicException
    {
        try
        {
            boolean isAuthorized = false;
            isAuthorized = checkUser(domainObject, sessionDataBean,uiObject);
            if (isAuthorized)
            {
                return true;
            }

            final String privilegeName = getPrivilegeName(domainObject);
            final String protectionElementName = getObjectId(dao, domainObject);

            if (Constants.cannotCreateSuperAdmin.equals(protectionElementName))
            {
                throw getBizLogicException(null, "user.cannotCreateSuperAdmin", "");
            }
            if (Constants.siteIsRequired.equals(protectionElementName))
            {

                throw getBizLogicException(null, "user.siteIsRequired", "");
            }

            final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
                    sessionDataBean.getUserName());

            if (protectionElementName == null)
            {
                // return false;
                // bug 11611 and 11659
                throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName, domainObject
                        .getClass().getSimpleName());

            }
            else
            {
                final String[] prArray = protectionElementName.split(Constants.UNDERSCORE);
                final String baseObjectId = prArray[0];
                String objId = null;

                for (int i = 1; i < prArray.length; i++)
                {
                    objId = baseObjectId + Constants.UNDERSCORE + prArray[i];
                    isAuthorized = privilegeCache.hasPrivilege(objId, privilegeName);
                    if (!isAuthorized)
                    {
                        break;
                    }
                }

                if (!isAuthorized)
                {
                    // bug 11611 and 11659
                    throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName,
                            domainObject.getClass().getSimpleName());
                }
                return isAuthorized;
            }
        }
        /*
         * catch(ApplicationException exp) { logger.debug(exp.getMessage(),
         * exp); throw getBizLogicException(exp, "dao.error", ""); }
         */
        catch (final SMException e)
        {
            UserBizLogic.LOGGER.error(e.getMessage(), e);
            throw AppUtility.handleSMException(e);
        }
    }

    

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getUser(java.lang.String)
	 */
    @Override
	public User getUser(final String loginName) throws BizLogicException
    {
        User validUser = null;
        final String getActiveUser = "from " + User.class.getName() + " user where " + "user.activityStatus= "
                + "'" + Status.ACTIVITY_STATUS_ACTIVE.toString() + "' and user.loginName =" + "'" + loginName
                + "'";
        final DefaultBizLogic bizlogic = new DefaultBizLogic();
        final List<User> users = bizlogic.executeQuery(getActiveUser);
        if (users != null && !users.isEmpty())
        {
            validUser = users.get(0);
        }
        return validUser;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getActiveUsers()
	 */
    @Override
	public List<User> getActiveUsers() throws BizLogicException
    {
        final String getActiveUser = "from " + User.class.getName() + " user where " + "user.activityStatus= "
                + "'" + Status.ACTIVITY_STATUS_ACTIVE.toString() + "'";
        final DefaultBizLogic bizlogic = new DefaultBizLogic();
        final List<User> users = bizlogic.executeQuery(getActiveUser);
        return users;
    }

    /* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.IUserBizLogic#getAdminUser()
	 */
    @Override
	public User getAdminUser() throws BizLogicException {
    	for (User user: getActiveUsers()) {
    		if (isAdminUser(UserUtility.getRoleId(user))) {
    			return user;
    		}
    	}
    	return null;
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
	 * @param sessionDataBean
	 * @return
	 * @throws BizLogicException
	 */
	@Override
	public boolean hasRegistrationPermission(SessionDataBean sessionDataBean)
			throws BizLogicException {
		DAO dao = null;
		boolean isAuthorized = false;
		String privilegeName = "REGISTRATION";
		try {
			final PrivilegeCache privilegeCache = PrivilegeManager
					.getInstance().getPrivilegeCache(
							sessionDataBean.getUserName());
			dao = this.openDAOSession(null);
			User user = (User) dao.retrieveById(User.class.getName(),
					sessionDataBean.getUserId());
			final Collection<CollectionProtocol> cpCollection = user
					.getAssignedProtocolCollection();
			if (cpCollection != null && !cpCollection.isEmpty()) {
				for (final CollectionProtocol cp : cpCollection) {
					if (privilegeCache.hasPrivilege(
							CollectionProtocol.class.getName() + "_"
									+ cp.getId(), privilegeName)) {
						isAuthorized = true;
						break;
					}
				}
				if (!isAuthorized) {
					isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(
							privilegeName, sessionDataBean, null);
				}
			} else {
				isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(
						privilegeName, sessionDataBean, null);
			}
		} catch (final DAOException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
		} catch (SMException e) {
			LOGGER.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
		} finally {
			this.closeDAOSession(dao);
		}
		return isAuthorized;
	}

	/**
	 * * @return
	 * 
	 * @throws BizLogicException
	 *             Returns list of remote user that need to be synced
	 *             i.e. remoteManagedFlag is true and dirtyEditFlag is false
	 */
	public List<User> getRemoteSyncUsers()
			throws BizLogicException {
		try {
			final String sourceObjectName = User.class.getName();

			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(
					Constants.REMOTE_MANAGED_FLAG, true));
			queryWhereClause.andOpr();
			queryWhereClause.addCondition(new EqualClause(
					Constants.DIRTY_EDIT_FLAG, false));
			String[] selectedColumns = null;
			final List userList = this.retrieve(
					User.class.getName(), selectedColumns,
					queryWhereClause);
			return userList;
		} catch (final DAOException daoexp) {
			this.LOGGER.error(daoexp.getMessage(), daoexp);
			daoexp.printStackTrace();
			throw this.getBizLogicException(daoexp, daoexp.getErrorKeyName(),
					daoexp.getMsgValues());
		}
	}
	
	public static ArrayList<Password> getPasswordCollection(User user) throws ApplicationException
	{
	  
		ArrayList<Password> passwords=new ArrayList<Password>();
	    
	    JDBCDAO jdbcDAO =null;
	    try
		{
	    	
	    jdbcDAO = AppUtility.openJDBCSession();
	    final StringBuilder query = new StringBuilder();
	    Date date ; 
	    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		query.append("select * from catissue_password password ");
		query.append("where password.USER_ID=?");
		query.append(" order by password.UPDATE_DATE");



		ColumnValueBean columnValueBean = new ColumnValueBean(user.getId());
		List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
		columnValueBeanList.add(columnValueBean);
		if (jdbcDAO != null)
		{
				final List results = jdbcDAO.executeQuery(query.toString(),null,columnValueBeanList);
				for (int i = 0; i < results.size(); i++)
				{  
					Password password=new Password();
					final ArrayList<String> columnList = (ArrayList<String>) results.get(i);
					if ((columnList != null) )
					{
						password.setId(Long.parseLong(columnList.get(0)));
						password.setPassword(columnList.get(1));
						password.setUpdateDate((Date)formatter.parse(columnList.get(2)));
						password.setUser(user);
					}
					passwords.add(password);
				}
			
		}
		}
		catch (final DAOException daoExp)
		{
			throw new BizLogicException(daoExp);
		} catch (ParseException parException) {
			// TODO Auto-generated catch block
			//throw new BizLogicException(parException);
		}
		finally
		{
			if(jdbcDAO!=null)
			AppUtility.closeJDBCSession(jdbcDAO);
		} 
	   return passwords;
	}
}