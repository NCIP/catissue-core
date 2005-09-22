/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.Permissions;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ApplicationSearchCriteria;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class SecurityManager implements Permissions
{

    private static AuthenticationManager authenticationManager = null;
    private static AuthorizationManager authorizationManager = null;
    private Class requestingClass = null;
    private static final String CATISSUE_CORE_CONTEXT_NAME = "catissuecore";
    private static final String ADMINISTRATOR_ROLE = "1";
    private static final String SUPERVISOR_ROLE = "2";
    private static final String TECHNICIAN_ROLE = "3";
    private static final String PUBLIC_ROLE = "7";
    private static final String ADMINISTRATOR_GROUP = "ADMINISTRATOR_GROUP";
    private static final String SUPERVISOR_GROUP = "SUPERVISOR_GROUP";
    private static final String TECHNICIAN_GROUP = "TECHNICIAN_GROUP";
    private static final String PUBLIC_GROUP = "PUBLIC_GROUP";
    private static final String ADMINISTRATOR_GROUP_ID = "1";
    private static final String SUPERVISOR_GROUP_ID = "2";
    private static final String TECHNICIAN_GROUP_ID = "3";
    private static final String PUBLIC_GROUP_ID = "4";

    /**
     * @param class1
     */
    public SecurityManager(Class class1)
    {
        requestingClass = class1;
    }

    /**
     * @param class1
     * @return
     */
    public static SecurityManager getInstance(Class class1)
    {
        return new SecurityManager(class1);
    }

    /**
     * Returns the AuthenticationManager for the caTISSUE Core. This method follows the
     * singleton pattern so that only one AuthenticationManager is created for
     * the caTISSUE Core.
     * 
     * @return
     * @throws CSException
     */
    protected AuthenticationManager getAuthenticationManager()
            throws CSException
    {
        if (authenticationManager == null)
        {
            synchronized (requestingClass)
            {
                if (authenticationManager == null)
                {
                    authenticationManager = SecurityServiceProvider
                            .getAuthenticationManager(CATISSUE_CORE_CONTEXT_NAME);
                }
            }
        }

        return authenticationManager;

    }

    /**
     * Returns the Authorization Manager for the caTISSUE Core.
     * This method follows the singleton pattern so that
     * only one AuthorizationManager is created.
     * 
     * @return
     * @throws CSException
     */
    protected AuthorizationManager getAuthorizationManager() throws CSException
    {

        if (authorizationManager == null)
        {
            synchronized (requestingClass)
            {
                if (authorizationManager == null)
                {
                    authorizationManager = SecurityServiceProvider
                            .getAuthorizationManager(CATISSUE_CORE_CONTEXT_NAME);
                }
            }
        }

        return authorizationManager;

    }

    /**
     * Returns the UserProvisioningManager singleton object.
     * 
     * @return
     * @throws CSException
     */
    protected UserProvisioningManager getUserProvisioningManager()
            throws CSException
    {
        UserProvisioningManager userProvisioningManager = (UserProvisioningManager) getAuthorizationManager();

        return userProvisioningManager;
    }

    public Application getApplication(String applicationName)
            throws CSException
    {
        Application application = new Application();
        application.setApplicationName(applicationName);
        ApplicationSearchCriteria applicationSearchCriteria = new ApplicationSearchCriteria(
                application);
        application = (Application) getUserProvisioningManager().getObjects(
                applicationSearchCriteria).get(0);
        return application;
    }

    /**
     * Returns true or false depending on the person gets authenticated or not.
     * @param requestingClass
     * @param loginName login name
     * @param password password
     * @return
     * @throws CSException
     */
    public boolean login(String loginName, String password) throws SMException
    {
        boolean loginSuccess = false;
        try
        {
            Logger.out.debug("login name: " + loginName + " passowrd: "
                    + password);
            AuthenticationManager authMngr = getAuthenticationManager();
            loginSuccess = authMngr.login(loginName, password);
        }
        catch (CSException ex)
        {
            Logger.out
                    .debug("Authentication|"
                            + requestingClass
                            + "|"
                            + loginName
                            + "|login|Success| Authentication is not successful for user "
                            + loginName + "|" + ex.getMessage());
            throw new SMException(ex.getMessage(), ex);
        }
        return loginSuccess;
    }

    /**
     * This method creates a new User in the database based on the data passed
     * @param user user to be created
     * @throws SMTransactionException If there is any exception in creating the User
     */
    public void createUser(User user) throws SMTransactionException
    {
        try
        {
            getUserProvisioningManager().createUser(user);
        }
        catch (CSTransactionException e)
        {
            Logger.out.debug("Unable to create user: Exception: "
                    + e.getMessage());
            throw new SMTransactionException(e.getMessage(), e);
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to create user: Exception: " + e);
        }
    }

    /**
     * This method returns the User object from the database for the passed User's Login Name. 
     * If no User is found then null is returned 
     * @param loginName Login name of the user
     * @return
     * @throws SMException
     */
    public User getUser(String loginName) throws SMException
    {
        try
        {
            return getAuthorizationManager().getUser(loginName);
        }
        catch (CSException e)
        {
            Logger.out
                    .debug("Unable to get user: Exception: " + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    /**
     * This method checks whether a user exists in the database or not
     * @param loginName Login name of the user
     * @return TRUE is returned if a user exists else FALSE is returned
     * @throws SMException
     */
    public boolean userExists(String loginName) throws SMException
    {
        boolean userExists = true;
        try
        {
            if (getUser(loginName) == null)
            {
                userExists = false;
            }
        }
        catch (SMException e)
        {
            Logger.out
                    .debug("Unable to get user: Exception: " + e.getMessage());
            throw e;
        }
        return userExists;
    }

    /**
     * This method returns Vactor of all the role objects defined for the application from the database
     * @return
     * @throws SMException
     */
    public Vector getRoles() throws SMException
    {
        Vector roles = new Vector();
        UserProvisioningManager userProvisioningManager = null;
        try
        {
            userProvisioningManager = getUserProvisioningManager();
            roles.add(userProvisioningManager.getRoleById(ADMINISTRATOR_ROLE));
            roles.add(userProvisioningManager.getRoleById(SUPERVISOR_ROLE));
            roles.add(userProvisioningManager.getRoleById(TECHNICIAN_ROLE));
            roles.add(userProvisioningManager.getRoleById(PUBLIC_ROLE));
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get roles: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
        return roles;
    }

    /**
     * Assigns a Role to a User
     * @param userName - the User Name to to whom the Role will be assigned
     * @param roleID - The id of the Role which is to be assigned to the user 
     * @throws SMException
     */
    public void assignRoleToUser(String userName, String roleID)
            throws SMException
    {
        Logger.out.debug("UserName: " + userName + " Role ID:" + roleID);
        UserProvisioningManager userProvisioningManager = null;
        User user;
        try
        {
            userProvisioningManager = getUserProvisioningManager();
            user = userProvisioningManager.getUser(userName);

            //Remove user from any other role if he is assigned some
            userProvisioningManager.removeUserFromGroup(ADMINISTRATOR_ROLE,
                    String.valueOf(user.getUserId()));
            userProvisioningManager.removeUserFromGroup(SUPERVISOR_ROLE, String
                    .valueOf(user.getUserId()));
            userProvisioningManager.removeUserFromGroup(TECHNICIAN_ROLE, String
                    .valueOf(user.getUserId()));
            userProvisioningManager.removeUserFromGroup(PUBLIC_GROUP_ID, String
                    .valueOf(user.getUserId()));

            if (roleID.equals(ADMINISTRATOR_ROLE))
            {
                assignAdditionalGroupsToUser(String.valueOf(user.getUserId()),
                        new String[]{ADMINISTRATOR_GROUP_ID});
                Logger.out.debug(" User assigned Administrator role");
            }
            else if (roleID.equals(SUPERVISOR_ROLE))
            {
                assignAdditionalGroupsToUser(String.valueOf(user.getUserId()),
                        new String[]{SUPERVISOR_GROUP_ID});
                Logger.out.debug(" User assigned supervisor role");
            }
            else if (roleID.equals(TECHNICIAN_ROLE))
            {
                assignAdditionalGroupsToUser(String.valueOf(user.getUserId()),
                        new String[]{TECHNICIAN_GROUP_ID});
                Logger.out.debug(" User assigned technician role");
            }
            else if (roleID.equals(PUBLIC_ROLE))
            {
                assignAdditionalGroupsToUser(String.valueOf(user.getUserId()),
                        new String[]{PUBLIC_GROUP_ID});
                Logger.out.debug(" User assigned public role");
            }
            else
            {
                Logger.out.debug(" User assigned no role");
            }
        }
        catch (CSException e)
        {
            Logger.out.debug("UNABLE TO ASSIGN ROLE TO USER: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    public Role getUserRole(long userID) throws SMException
    {
        Set groups;
        UserProvisioningManager userProvisioningManager = null;
        Iterator it;
        Group group;
        Role role = null;
        try
        {
            userProvisioningManager = getUserProvisioningManager();
            groups = userProvisioningManager.getGroups(String.valueOf(userID));
            it = groups.iterator();
            while (it.hasNext())
            {
                group = (Group) it.next();
                if (group.getGroupName().equals(ADMINISTRATOR_GROUP))
                {
                    role = userProvisioningManager
                            .getRoleById(ADMINISTRATOR_ROLE);
                    return role;
                }
                else if (group.getGroupName().equals(SUPERVISOR_GROUP))
                {
                    role = userProvisioningManager.getRoleById(SUPERVISOR_ROLE);
                    return role;
                }
                else if (group.getGroupName().equals(TECHNICIAN_GROUP))
                {
                    role = userProvisioningManager.getRoleById(TECHNICIAN_ROLE);
                    return role;
                }
                else if (group.getGroupName().equals(PUBLIC_GROUP))
                {
                    role = userProvisioningManager.getRoleById(PUBLIC_ROLE);
                    return role;
                }
            }
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get roles: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
        return role;

    }

    /**
     * Modifies an entry for an existing User in the database based on the data passed 
     * @param user - the User object that needs to be modified in the database 
     * @throws SMException if there is any exception in modifying the User in the database
     */
    public void modifyUser(User user) throws SMException
    {
        try
        {
            getUserProvisioningManager().modifyUser(user);
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to modify user: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    /**
     * Returns the User object for the passed User id 
     * @param userId - The id of the User object which is to be obtained 
     * @return The User object from the database for the passed User id 
     * @throws SMException if the User object is not found for the given id
     */
    public User getUserById(String userId) throws SMException
    {
        Logger.out.debug("user Id: " + userId);
        try
        {
            User user = getUserProvisioningManager().getUserById(userId);
            Logger.out.debug("User returned: " + user.getLoginName());
            return user;
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get user by Id: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    /**
     * Returns list of the User objects for the passed email address
     * @param emailAddress - Email Address for which users need to be searched
     * @return
     * @throws SMException if there is any exception while querying the database 
     */
    public List getUsersByEmail(String emailAddress) throws SMException
    {
        try
        {
            User user = new User();
            user.setEmailId(emailAddress);
            SearchCriteria searchCriteria = new UserSearchCriteria(user);
            return getUserProvisioningManager().getObjects(searchCriteria);
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get users by emailAddress: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    /**
     * @throws SMException
     * 
     */
    public List getUsers() throws SMException
    {
        try
        {
            User user = new User();
            SearchCriteria searchCriteria = new UserSearchCriteria(user);
            return getUserProvisioningManager().getObjects(searchCriteria);
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get all users: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    /**
     * Checks wether the user has EXECUTE privilege on the Action subclass of
     * SecureAction.
     * 
     * @param string
     * @return
     * @throws CSException
     */
    public boolean isAuthorizedToExecuteAction(String loginName, String objectId)
            throws Exception
    {
        Logger.out.debug("Login Name: " + loginName);
        User user = getUser(loginName);
        //        String objectId = getObjectIdForSecureMethodAccess();

        Logger.out.debug("The User name is: " + user.getName());
        Logger.out.debug("The Object ID is: " + objectId);

        boolean isAuthorized = false;
        try
        {
            isAuthorized = getAuthorizationManager().checkPermission(
                    user.getName(), objectId, EXECUTE);

        }
        catch (CSException ex)
        {
            Logger.out.fatal("The Security Service encountered "
                    + "a fatal exception.", ex);
            throw new Exception(
                    "The Security Service encountered a fatal exception.", ex);
        }

        return isAuthorized;

    }

    //    /**
    //     * Returns the object id of the protection element that represents
    //     * the Action that is being requested for invocation.
    //     * @param clazz
    //     * @return
    //     */
    //    private String getObjectIdForSecureMethodAccess()
    //    {
    //        return requestingClass.getName();
    //    }

    /**
     * Returns list of objects corresponding to the searchCriteria passed
     * @param searchCriteria
     * @return List of resultant objects
     * @throws SMException if searchCriteria passed is null or if search results in no results
     * @throws CSException
     */
    private List getObjects(SearchCriteria searchCriteria) throws SMException,
            CSException
    {
        if (null == searchCriteria)
        {
            Logger.out.debug(" Null Parameters passed");
            throw new SMException("Null Parameters passed");
        }
        UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
        List list = userProvisioningManager.getObjects(searchCriteria);
        if (null == list || list.size() <= 0)
        {
            Logger.out.debug("Search resulted in no results");
            throw new SMException("Search resulted in no results");
        }
        return list;
    }

    public void assignAdditionalGroupsToUser(String userId, String[] groupIds)
            throws SMException
    {
        if (userId == null || groupIds == null || groupIds.length < 1)
        {
            Logger.out.debug(" Null or insufficient Parameters passed");
            throw new SMException("Null or insufficient Parameters passed");
        }

        Logger.out.debug(" userId: " + userId + " groupIds:" + groupIds);

        Set consolidatedGroupIds = new HashSet();
        Set consolidatedGroups;
        String[] finalUserGroupIds;
        UserProvisioningManager userProvisioningManager;
        User user;
        UserSearchCriteria userSearchCriteria;
        Group group = new Group();
        GroupSearchCriteria groupSearchCriteria;
        List list;
        try
        {
            userProvisioningManager = getUserProvisioningManager();
            //            user = new User();
            //            user.setUserId(userId);
            //            userSearchCriteria = new UserSearchCriteria(user);
            //            list = getObjects(userSearchCriteria);
            //            user =  (User)(list.get(0));
            //            if(user == null )
            //    	    {
            //    	        Logger.out.debug("User with user ID "+userId+" not found");
            //    	        throw new SMException("User with user ID "+userId+" not found");
            //    	    }

            consolidatedGroups = userProvisioningManager.getGroups(userId);
            if (null != consolidatedGroups)
            {
                Iterator it = consolidatedGroups.iterator();
                while (it.hasNext())
                {
                    group = (Group) it.next();
                    consolidatedGroupIds
                            .add(String.valueOf(group.getGroupId()));
                }
            }

            /**
             * Consolidating all the Groups
             */

            for (int i = 0; i < groupIds.length; i++)
            {
                consolidatedGroupIds.add(groupIds[i]);
            }

            finalUserGroupIds = new String[consolidatedGroupIds.size()];
            Iterator it = consolidatedGroupIds.iterator();

            for (int i = 0; it.hasNext(); i++)
            {
                finalUserGroupIds[i] = (String) it.next();
                Logger.out.debug("Group user is assigned to: "
                        + finalUserGroupIds[i]);
            }

            /**
             * Setting groups for user and updating it
             */
            userProvisioningManager.assignGroupsToUser(userId,
                    finalUserGroupIds);

        }
        catch (CSException ex)
        {
            Logger.out.fatal("The Security Service encountered "
                    + "a fatal exception.", ex);
            throw new SMException(
                    "The Security Service encountered a fatal exception.", ex);
        }

    }

    /**
     * This method creates protection elements corresponding to protection objects
     * passed and associates them with static as well as dynamic protection groups 
     * that are passed. It also creates user group, role, protection group mapping for 
     * all the elements in authorization data
     * @param authorizationData Vector of SecurityDataBean objects
     * @param protectionObjects Set of AbstractDomainObject instances
     * @param dynamicGroups Array of dynamic group names
     * @throws SMException
     */
    public void insertAuthorizationData(Vector authorizationData,
            Set protectionObjects, String[] dynamicGroups) throws SMException
    {

        Set protectionElements;

        Iterator it;

        try
        {
            Logger.out
                    .debug("************** Inserting authorization Data ***************");

            /**
             * Create protection elements corresponding to all protection objects
             */
            protectionElements = createProtectionElementsFromProtectionObjects(protectionObjects);

            /**
             * Create user group role protection group and their mappings if required
             */
            createUserGroupRoleProtectionGroup(authorizationData,
                    protectionElements);

            /**
             * Assigning protection elements to dynamic groups
             */
            assignProtectionElementsToGroups(protectionElements, dynamicGroups);

            Logger.out
                    .debug("************** Inserted authorization Data ***************");

        }
        catch (CSException e)
        {
            Logger.out.fatal("The Security Service encountered "
                    + "a fatal exception.", e);
            throw new SMException(
                    "The Security Service encountered a fatal exception.", e);
        }

    }

    /**
     * This method assigns Protection Elements passed to the Protection group names passed.
     * @param protectionElements
     * @param groups
     * @throws CSException
     */
    private void assignProtectionElementsToGroups(Set protectionElements,
            String[] groups)
    {
        ProtectionElement protectionElement;
        Iterator it;
        if (groups != null)
        {
            for (int i = 0; i < groups.length; i++)
            {
                for (it = protectionElements.iterator(); it.hasNext();)
                {
                    protectionElement = (ProtectionElement) it.next();
                    assignProtectionElementToGroup(protectionElement, groups[i]);
                }
            }
        }
    }

    /**
     * This method creates user group, role, protection group mappings in database for the 
     * passed authorizationData. It also adds protection elements to the protection groups
     * for which mapping is made. 
     * For each element in authorization Data passed: 
     * User group is created and users are added to user group if 
     * one does not exist by the name passed. Similarly Protection Group is created 
     * and protection elements are added to it if one does not exist.
     * Finally user group and protection group are associated with each other 
     * by the role they need to be associated with.
     * If no role exists by the name an exception is thrown and the corresponding mapping is not created
     * @param authorizationData
     * @param protectionElements
     * @throws CSException
     * @throws SMException
     */
    private void createUserGroupRoleProtectionGroup(Vector authorizationData,
            Set protectionElements) throws CSException, SMException
    {
        ProtectionElement protectionElement;
        ProtectionGroup protectionGroup = null;
        SecurityDataBean userGroupRoleProtectionGroupBean;
        RoleSearchCriteria roleSearchCriteria;
        Role role;
        String[] roleIds = null;
        List list;
        ProtectionGroupSearchCriteria protectionGroupSearchCriteria;
        Group group = new Group();
        GroupSearchCriteria groupSearchCriteria;
        Set userGroup;
        User user;
        Iterator it;
        UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
        groupSearchCriteria = new GroupSearchCriteria(group);

        if (authorizationData != null)
        {
            Logger.out.debug(" UserGroupRoleProtectionGroup Size:"
                    + authorizationData.size());
            for (int i = 0; i < authorizationData.size(); i++)
            {
                Logger.out.debug(" authorizationData:" + i + " "
                        + authorizationData.get(i).toString());
                try
                {
                    userGroupRoleProtectionGroupBean = (SecurityDataBean) authorizationData
                            .get(i);

                    group
                            .setApplication(getApplication(CATISSUE_CORE_CONTEXT_NAME));
                    group.setGroupName(userGroupRoleProtectionGroupBean
                            .getGroupName());
                    groupSearchCriteria = new GroupSearchCriteria(group);
                    /**
                     * If group already exists
                     */
                    try
                    {
                        list = getObjects(groupSearchCriteria);
                        Logger.out.debug("User group " + group.getGroupName()
                                + " already exists");
                    }
                    /**
                     * If group does not exist already
                     */
                    catch (SMException ex)
                    {
                        Logger.out.debug("User group " + group.getGroupName()
                                + " does not exist");
                        //                        group.setUsers(userGroupRoleProtectionGroupBean.getGroup());
                        userProvisioningManager.createGroup(group);
                        Logger.out.debug("User group " + group.getGroupName()
                                + " created");
                        Logger.out.debug("Users added to group : "
                                + group.getUsers());
                        list = getObjects(groupSearchCriteria);
                    }
                    group = (Group) list.get(0);

                    /**
                     * Assigning group to users in userGroup
                     */
                    userGroup = userGroupRoleProtectionGroupBean.getGroup();
                    for (it = userGroup.iterator(); it.hasNext();)
                    {
                        user = (User) it.next();
                        //                    userProvisioningManager.assignGroupsToUser(String.valueOf(user.getUserId()),new String[] {String.valueOf(group.getGroupId())});
                        assignAdditionalGroupsToUser(String.valueOf(user
                                .getUserId()), new String[]{String
                                .valueOf(group.getGroupId())});
                        Logger.out.debug("userId:" + user.getUserId()
                                + " group Id:" + group.getGroupId());
                    }

                    protectionGroup = new ProtectionGroup();
                    protectionGroup
                            .setApplication(getApplication(CATISSUE_CORE_CONTEXT_NAME));
                    protectionGroup
                            .setProtectionGroupName(userGroupRoleProtectionGroupBean
                                    .getProtectionGroupName());
                    protectionGroupSearchCriteria = new ProtectionGroupSearchCriteria(
                            protectionGroup);
                    /**
                     * If Protection group already exists add protection elements to the group
                     */
                    try
                    {
                        list = getObjects(protectionGroupSearchCriteria);
                        protectionGroup = (ProtectionGroup) list.get(0);
                        Logger.out.debug(" From Database: "
                                + protectionGroup.toString());

                    }
                    /**
                     * If the protection group does not already exist create the protection group
                     * and add protection elements to it.
                     */
                    catch (SMException sme)
                    {
                        protectionGroup
                                .setProtectionElements(protectionElements);
                        userProvisioningManager
                                .createProtectionGroup(protectionGroup);
                        Logger.out.debug("Protection group created: "
                                + protectionGroup.toString());
                    }

                    role = new Role();
                    role
                            .setName(userGroupRoleProtectionGroupBean
                                    .getRoleName());
                    roleSearchCriteria = new RoleSearchCriteria(role);
                    list = getObjects(roleSearchCriteria);
                    roleIds = new String[1];
                    roleIds[0] = String.valueOf(((Role) list.get(0)).getId());
                    userProvisioningManager.assignGroupRoleToProtectionGroup(
                            String.valueOf(protectionGroup
                                    .getProtectionGroupId()), String
                                    .valueOf(group.getGroupId()), roleIds);
                    Logger.out.debug("Assigned Group Role To Protection Group "
                            + protectionGroup.getProtectionGroupId() + " "
                            + String.valueOf(group.getGroupId()) + " "
                            + roleIds);
                }
                catch (CSTransactionException ex)
                {
                    Logger.out.error(
                            "Error occured Assigned Group Role To Protection Group "
                                    + protectionGroup.getProtectionGroupId()
                                    + " " + String.valueOf(group.getGroupId())
                                    + " " + roleIds, ex);
                }
            }
        }
    }

    /**
     * This method creates protection elements from the protection objects passed 
     * and associate them with respective static groups they should be added to 
     * depending on their class name 
     * if the corresponding protection element does not already exist.
     * @param protectionObjects 
     * @return
     * @throws CSException
     */
    private Set createProtectionElementsFromProtectionObjects(
            Set protectionObjects) throws CSException
    {
        ProtectionElement protectionElement;
        Set protectionElements = new HashSet();
        ProtectionGroup protectionGroup;
        List list;
        AbstractDomainObject protectionObject;
        String[] staticGroups;
        Set protectionGroups = null;
        ProtectionGroupSearchCriteria protectionGroupSearchCriteria;
        Iterator it;
        UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
        if (protectionObjects != null)
        {
            for (it = protectionObjects.iterator(); it.hasNext();)
            {
                protectionElement = new ProtectionElement();
                protectionObject = (AbstractDomainObject) it.next();

                protectionElement.setObjectId(protectionObject.getClass()
                        .getName()
                        + "_" + protectionObject.getSystemIdentifier());

                try
                {

                    /**
                     * In case protection element already exists
                     */
                    try
                    {
                        protectionElement = userProvisioningManager
                                .getProtectionElement(protectionElement
                                        .getObjectId());
                        Logger.out.debug(" Protection Element: "
                                + protectionElement.getObjectId()
                                + " already exists");
                    }
                    /**
                     * If protection element does not exist already
                     */
                    catch (CSObjectNotFoundException csex)
                    {
                        protectionElement
                                .setApplication(getApplication(CATISSUE_CORE_CONTEXT_NAME));
                        protectionElement
                                .setProtectionElementDescription(protectionObject
                                        .getClass().getName()
                                        + " object");
                        protectionElement
                                .setProtectionElementName(protectionObject
                                        .getClass().getName()
                                        + "_"
                                        + protectionObject
                                                .getSystemIdentifier());
                        /**
                         * Adding protection elements to static groups they should be added to
                         */
                        staticGroups = (String[]) Constants.STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES
                                .get(protectionObject.getClass().getName());

                        if (staticGroups != null)
                        {
                            protectionGroups = new HashSet();
                            for (int i = 0; i < staticGroups.length; i++)
                            {
                                Logger.out.debug(" group name " + i + " "
                                        + staticGroups[i]);
                                protectionGroup = new ProtectionGroup();
                                protectionGroup
                                        .setProtectionGroupName(staticGroups[i]);
                                protectionGroupSearchCriteria = new ProtectionGroupSearchCriteria(
                                        protectionGroup);
                                try
                                {
                                    list = getObjects(protectionGroupSearchCriteria);
                                    protectionGroup = (ProtectionGroup) list
                                            .get(0);
                                    Logger.out.debug(" From Database: "
                                            + protectionGroup.toString());
                                    protectionGroups.add(protectionGroup);
                                }
                                catch (SMException sme)
                                {
                                    Logger.out.error(
                                            "Error occured while retrieving "
                                                    + staticGroups[i]
                                                    + "  From Database: ", sme);
                                }

                            }
                            protectionElement
                                    .setProtectionGroups(protectionGroups);
                        }

                        userProvisioningManager
                                .createProtectionElement(protectionElement);

                        Logger.out.debug("Protection element created: "
                                + protectionElement.toString());
                        Logger.out
                                .debug("Protection element added to groups : "
                                        + protectionGroups);
                    }

                    protectionElements.add(protectionElement);
                }
                catch (CSTransactionException ex)
                {
                    Logger.out.error(
                            "Error occured while creating Potection Element "
                                    + protectionElement
                                            .getProtectionElementName(), ex);
                }
            }
        }
        return protectionElements;
    }

    /**
     * @param protectionElement
     * @param userProvisioningManager
     * @param dynamicGroups
     * @param i
     * @throws CSException
     */
    private void assignProtectionElementToGroup(
            ProtectionElement protectionElement, String GroupsName)

    {
        try
        {
            UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
            userProvisioningManager.assignProtectionElement(GroupsName,
                    protectionElement.getObjectId());
            Logger.out.debug("Associated protection group: " + GroupsName
                    + " to protectionElement"
                    + protectionElement.getProtectionElementName());
        }

        catch (CSException e)
        {
            Logger.out
                    .error(
                            "The Security Service encountered an error while associating protection group: "
                                    + GroupsName
                                    + " to protectionElement"
                                    + protectionElement
                                            .getProtectionElementName(), e);
        }
    }

    public boolean isAuthorized(String userName, String objectId,
            String privilegeName) throws SMException
    {
        try
        {
            boolean isAuthorized = getAuthorizationManager().checkPermission(
                    userName, objectId, privilegeName);
            Logger.out.debug(" User:" + userName + " objectId:" + objectId
                    + " privilegeName:" + privilegeName + " isAuthorized:"
                    + isAuthorized);
            return isAuthorized;
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get all users: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    public boolean isAuthorized(String userName, String objectId,
            String attributeName, String privilegeName) throws SMException
    {
        try
        {
            return getAuthorizationManager().checkPermission(userName,
                    objectId, attributeName, privilegeName);
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get all users: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
    }

    /**
     * This method returns name of the Protection groupwhich consists of obj as Protection Element and 
     * whose name consists of string nameConsistingOf
     * @param obj
     * @param nameConsistingOf
     * @return
     * @throws SMException
     */
    public String getProtectionGroupByName(AbstractDomainObject obj,
            String nameConsistingOf) throws SMException
    {
        Set protectionGroups;
        Iterator it;
        ProtectionGroup protectionGroup;
        ProtectionElement protectionElement;
        String name = null;
        String protectionElementName = obj.getClass().getName() + "_"
                + obj.getSystemIdentifier();
        try
        {
            protectionElement = getAuthorizationManager().getProtectionElement(
                    protectionElementName);
            protectionGroups = getAuthorizationManager().getProtectionGroups(
                    protectionElement.getProtectionElementId().toString());
            it = protectionGroups.iterator();
            while (it.hasNext())
            {
                protectionGroup = (ProtectionGroup) it.next();
                name = protectionGroup.getProtectionGroupName();
                if (name.indexOf(nameConsistingOf) != -1)
                {
                    Logger.out.debug("protection group by name "
                            + nameConsistingOf + " for Protection Element "
                            + protectionElementName + " is " + name);
                    return name;
                }
            }
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get protection group by name "
                    + nameConsistingOf + " for Protection Element "
                    + protectionElementName + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
        return name;

    }

    /**
     * Returns name value beans corresponding to all 
     * privileges that can be assigned for Assign Privileges Page
     * @param userName login name of user logged in 
     * @return
     */
    public Vector getPrivilegesForAssignPrivilege(String userName)
    {
        Vector privileges = new Vector();
        NameValueBean nameValueBean;
        nameValueBean = new NameValueBean(Permissions.READ, Permissions.READ);
        privileges.add(nameValueBean);

        nameValueBean = new NameValueBean(Permissions.USE, Permissions.USE);
        privileges.add(nameValueBean);
        return privileges;
    }

    /**
     * This method returns NameValueBeans for all the objects of type objectType
     * on which user with identifier userID has privilege ASSIGN_<<privilegeName>>.
     * @param userID
     * @param objectType
     * @param privilegeName
     * @return
     * @throws SMException thrown if any error occurs while retreiving ProtectionElementPrivilegeContextForUser
     */
    private Set getObjectsForAssignPrivilege(
            Set protectionElementPrivilegeContextSet, String objectType,
            String privilegeName) throws SMException
    {
        Set objects = new HashSet();
        NameValueBean nameValueBean;

        ProtectionElementPrivilegeContext protectionElementPrivilegeContext;

        Set privileges;
        Iterator iterator;
        String objectId;
        Privilege privilege;

        if (protectionElementPrivilegeContextSet != null)
        {
            iterator = protectionElementPrivilegeContextSet.iterator();
            while (iterator.hasNext())
            {
                protectionElementPrivilegeContext = (ProtectionElementPrivilegeContext) iterator
                        .next();
                objectId = protectionElementPrivilegeContext
                        .getProtectionElement().getObjectId();
                Logger.out.debug(objectId);
                if (objectId.indexOf(objectType + "_") != -1)
                {
                    privileges = protectionElementPrivilegeContext
                            .getPrivileges();
                    Iterator it = privileges.iterator();
                    while (it.hasNext())
                    {
                        privilege = (Privilege) it.next();

                        if (privilege.getName().equals(
                                "ASSIGN_" + privilegeName))
                        {
                            nameValueBean = new NameValueBean(objectId
                                    .substring(objectId.lastIndexOf(".") + 1),
                                    objectId);
                            objects.add(nameValueBean);
                            Logger.out.debug(nameValueBean);
                            break;
                        }
                    }

                }
            }
        }

        return objects;
    }

    public Set getObjectsForAssignPrivilege(String userID,
            String[] objectTypes, String[] privilegeNames) throws SMException
    {
        Set objects = new HashSet();
        UserProvisioningManager userProvisioningManager;
        Set protectionElementPrivilegeContextSet;
        try
        {
            if (objectTypes == null || privilegeNames == null)
            {
                return objects;
            }
            userProvisioningManager = getUserProvisioningManager();
            protectionElementPrivilegeContextSet = userProvisioningManager
                    .getProtectionElementPrivilegeContextForUser(userID);

            for (int i = 0; i < objectTypes.length; i++)
            {
                for (int j = 0; j < privilegeNames.length; j++)
                {
                    objects.addAll(getObjectsForAssignPrivilege(
                            protectionElementPrivilegeContextSet,
                            objectTypes[i], privilegeNames[j]));
                }
            }
        }
        catch (CSException e)
        {
            Logger.out.debug("Unable to get objects: Exception: "
                    + e.getMessage());
            throw new SMException(e.getMessage(), e);
        }
        return objects;

    }

    public void setOwnerForProtectionElement(edu.wustl.catissuecore.domain.User user,
            java.lang.String userName) throws SMException
    {
        if (user != null && userName != null)
        {
            String protectionElementObjectId = user.getClass().getName() + "_"
                    + user.getSystemIdentifier();
            Logger.out.debug(" Protection Element Object Id:"
                    + protectionElementObjectId);
            Logger.out.debug(" userName:" + userName);
            try
            {
                UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
                userProvisioningManager.setOwnerForProtectionElement(
                        protectionElementObjectId, new String[]{userName});
            }
            catch (CSException e)
            {
                Logger.out.debug("Unable to set owner: Exception: "
                        + e.getMessage());
                throw new SMException(e.getMessage(), e);
            }
        }
        else
        {
            Logger.out.debug("user:"+user+" username:"+userName);
        }
    }

}

