/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.beans.SecurityDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.query.AbstractClient;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.ApplicationSearchCriteria;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 * <p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 * 
 * @author Aarti Sharma
 * @version 1.0
 */

public class SecurityManager implements Permissions {

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
	public SecurityManager(Class class1) {
		requestingClass = class1;
	}

	/**
	 * @param class1
	 * @return
	 */
	public static SecurityManager getInstance(Class class1) {
		return new SecurityManager(class1);
	}

	/**
	 * Returns the AuthenticationManager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthenticationManager is
	 * created for the caTISSUE Core.
	 * 
	 * @return @throws
	 *         CSException
	 */
	protected AuthenticationManager getAuthenticationManager()
			throws CSException {
		if (authenticationManager == null) {
			synchronized (requestingClass) {
				if (authenticationManager == null) {
					authenticationManager = SecurityServiceProvider
							.getAuthenticationManager(CATISSUE_CORE_CONTEXT_NAME);
				}
			}
		}

		return authenticationManager;

	}

	/**
	 * Returns the Authorization Manager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthorizationManager is
	 * created.
	 * 
	 * @return @throws
	 *         CSException
	 */
	protected AuthorizationManager getAuthorizationManager() throws CSException {

		if (authorizationManager == null) {
			synchronized (requestingClass) {
				if (authorizationManager == null) {
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
	 * @return @throws
	 *         CSException
	 */
	protected UserProvisioningManager getUserProvisioningManager()
			throws CSException {
		UserProvisioningManager userProvisioningManager = (UserProvisioningManager) getAuthorizationManager();

		return userProvisioningManager;
	}

	public Application getApplication(String applicationName)
			throws CSException {
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
	 * 
	 * @param requestingClass
	 * @param loginName
	 *            login name
	 * @param password
	 *            password
	 * @return @throws
	 *         CSException
	 */
	public boolean login(String loginName, String password) throws SMException {
		boolean loginSuccess = false;
		try {
			Logger.out.debug("login name: " + loginName + " passowrd: "
					+ password);
			AuthenticationManager authMngr = getAuthenticationManager();
			loginSuccess = authMngr.login(loginName, password);
		} catch (CSException ex) {
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
	 * 
	 * @param user
	 *            user to be created
	 * @throws SMTransactionException
	 *             If there is any exception in creating the User
	 */
	public void createUser(User user) throws SMTransactionException {
		try {
			getUserProvisioningManager().createUser(user);
		} catch (CSTransactionException e) {
			Logger.out.debug("Unable to create user: Exception: "
					+ e.getMessage());
			throw new SMTransactionException(e.getMessage(), e);
		} catch (CSException e) {
			Logger.out.debug("Unable to create user: Exception: " + e);
		}
	}

	/**
	 * This method returns the User object from the database for the passed
	 * User's Login Name. If no User is found then null is returned
	 * 
	 * @param loginName
	 *            Login name of the user
	 * @return @throws
	 *         SMException
	 */
	public User getUser(String loginName) throws SMException {
		try {
			return getAuthorizationManager().getUser(loginName);
		} catch (CSException e) {
			Logger.out
					.debug("Unable to get user: Exception: " + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}
	
	/**
	 * This method returns array of CSM user id of all users who are administrators
	 * @return
	 * @throws SMException
	 */
	public Long[] getAllAdministrators() throws SMException 
	{
		try {
			Group group = new Group();
			group.setGroupName(ADMINISTRATOR_GROUP);
			GroupSearchCriteria groupSearchCriteria= new GroupSearchCriteria(group);
			List list = getObjects(groupSearchCriteria);
			group = (Group) list.get(0);
			Set users = group.getUsers();
			Long[] userId= new Long[users.size()];
			Iterator it= users.iterator();
			for(int i=0; i<users.size(); i++)
			{
				userId[i] =  ((User)it.next()).getUserId();
			}
			return userId;
		} catch (CSException e) {
			Logger.out
					.debug("Unable to get users: Exception: " + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * This method checks whether a user exists in the database or not
	 * 
	 * @param loginName
	 *            Login name of the user
	 * @return TRUE is returned if a user exists else FALSE is returned
	 * @throws SMException
	 */
	public boolean userExists(String loginName) throws SMException {
		boolean userExists = true;
		try {
			if (getUser(loginName) == null) {
				userExists = false;
			}
		} catch (SMException e) {
			Logger.out
					.debug("Unable to get user: Exception: " + e.getMessage());
			throw e;
		}
		return userExists;
	}
	
	public void removeUser(String userId) throws SMException
	{
		try {
			getUserProvisioningManager().removeUser(userId);
		} catch (CSTransactionException ex) {
			Logger.out
					.debug("Unable to get user: Exception: " + ex.getMessage());
			throw new SMTransactionException("Failed to find this user with userId:"+userId,ex);
		} catch (CSException e) {
			Logger.out
			.debug("Unable to obtain Authorization Manager: Exception: " + e.getMessage());
			throw new SMException("Failed to find this user with userId:"+userId,e);
		}
	}

	/**
	 * This method returns Vactor of all the role objects defined for the
	 * application from the database
	 * 
	 * @return @throws
	 *         SMException
	 */
	public Vector getRoles() throws SMException {
		Vector roles = new Vector();
		UserProvisioningManager userProvisioningManager = null;
		try {
			userProvisioningManager = getUserProvisioningManager();
			roles.add(userProvisioningManager.getRoleById(ADMINISTRATOR_ROLE));
			roles.add(userProvisioningManager.getRoleById(SUPERVISOR_ROLE));
			roles.add(userProvisioningManager.getRoleById(TECHNICIAN_ROLE));
			roles.add(userProvisioningManager.getRoleById(PUBLIC_ROLE));
		} catch (CSException e) {
			Logger.out.debug("Unable to get roles: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return roles;
	}

	/**
	 * Assigns a Role to a User
	 * 
	 * @param userName -
	 *            the User Name to to whom the Role will be assigned
	 * @param roleID -
	 *            The id of the Role which is to be assigned to the user
	 * @throws SMException
	 */
	public void assignRoleToUser(String userID, String roleID)
			throws SMException {
		Logger.out.debug("UserName: " + userID + " Role ID:" + roleID);
		UserProvisioningManager userProvisioningManager = null;
		User user;
		String groupId;
		try {
			userProvisioningManager = getUserProvisioningManager();
			//user = userProvisioningManager.getUser(userName);
			user = userProvisioningManager.getUserById(userID);

			//Remove user from any other role if he is assigned some
			userProvisioningManager.removeUserFromGroup(ADMINISTRATOR_ROLE,
					String.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(SUPERVISOR_ROLE, String
					.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(TECHNICIAN_ROLE, String
					.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(PUBLIC_GROUP_ID, String
					.valueOf(user.getUserId()));

			//Add user to corresponding group
			groupId = getGroupIdForRole(roleID);
			if (groupId == null) {
				Logger.out.debug(" User assigned no role");
			} else {
				assignAdditionalGroupsToUser(String.valueOf(user.getUserId()),
						new String[] { groupId });
				Logger.out.debug(" User assigned role:" + groupId);
			}

		} catch (CSException e) {
			Logger.out.debug("UNABLE TO ASSIGN ROLE TO USER: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	private String getGroupIdForRole(String roleID) {
		if (roleID.equals(ADMINISTRATOR_ROLE)) {
			Logger.out.debug(" role corresponds to Administrator group");
			return ADMINISTRATOR_GROUP_ID;
		} else if (roleID.equals(SUPERVISOR_ROLE)) {
			Logger.out.debug(" role corresponds to Supervisor group");
			return SUPERVISOR_GROUP_ID;
		} else if (roleID.equals(TECHNICIAN_ROLE)) {
			Logger.out.debug(" role corresponds to Technician group");
			return TECHNICIAN_GROUP_ID;
		} else if (roleID.equals(PUBLIC_ROLE)) {
			Logger.out.debug(" role corresponds to public group");
			return PUBLIC_GROUP_ID;
		} else {
			Logger.out.debug("role corresponds to no group");
			return null;
		}
	}

	public Role getUserRole(long userID) throws SMException {
		Set groups;
		UserProvisioningManager userProvisioningManager = null;
		Iterator it;
		Group group;
		Role role = null;
		try {
			userProvisioningManager = getUserProvisioningManager();
			groups = userProvisioningManager.getGroups(String.valueOf(userID));
			it = groups.iterator();
			while (it.hasNext()) {
				group = (Group) it.next();
				if (group.getGroupName().equals(ADMINISTRATOR_GROUP)) {
					role = userProvisioningManager
							.getRoleById(ADMINISTRATOR_ROLE);
					return role;
				} else if (group.getGroupName().equals(SUPERVISOR_GROUP)) {
					role = userProvisioningManager.getRoleById(SUPERVISOR_ROLE);
					return role;
				} else if (group.getGroupName().equals(TECHNICIAN_GROUP)) {
					role = userProvisioningManager.getRoleById(TECHNICIAN_ROLE);
					return role;
				} else if (group.getGroupName().equals(PUBLIC_GROUP)) {
					role = userProvisioningManager.getRoleById(PUBLIC_ROLE);
					return role;
				}
			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get roles: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return role;

	}

	/**
	 * Modifies an entry for an existing User in the database based on the data
	 * passed
	 * 
	 * @param user -
	 *            the User object that needs to be modified in the database
	 * @throws SMException
	 *             if there is any exception in modifying the User in the
	 *             database
	 */
	public void modifyUser(User user) throws SMException {
		try {
			getUserProvisioningManager().modifyUser(user);
		} catch (CSException e) {
			Logger.out.debug("Unable to modify user: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * Returns the User object for the passed User id
	 * 
	 * @param userId -
	 *            The id of the User object which is to be obtained
	 * @return The User object from the database for the passed User id
	 * @throws SMException
	 *             if the User object is not found for the given id
	 */
	public User getUserById(String userId) throws SMException {
		Logger.out.debug("user Id: " + userId);
		try {
			User user = getUserProvisioningManager().getUserById(userId);
			Logger.out.debug("User returned: " + user.getLoginName());
			return user;
		} catch (CSException e) {
			Logger.out.debug("Unable to get user by Id: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * Returns list of the User objects for the passed email address
	 * 
	 * @param emailAddress -
	 *            Email Address for which users need to be searched
	 * @return @throws
	 *         SMException if there is any exception while querying the database
	 */
	public List getUsersByEmail(String emailAddress) throws SMException {
		try {
			User user = new User();
			user.setEmailId(emailAddress);
			SearchCriteria searchCriteria = new UserSearchCriteria(user);
			return getUserProvisioningManager().getObjects(searchCriteria);
		} catch (CSException e) {
			Logger.out.debug("Unable to get users by emailAddress: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * @throws SMException
	 *  
	 */
	public List getUsers() throws SMException {
		try {
			User user = new User();
			SearchCriteria searchCriteria = new UserSearchCriteria(user);
			return getUserProvisioningManager().getObjects(searchCriteria);
		} catch (CSException e) {
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
	 * @return @throws
	 *         CSException
	 */
	public boolean isAuthorizedToExecuteAction(String loginName, String objectId)
			throws Exception {
		Logger.out.debug("Login Name: " + loginName);
		User user = getUser(loginName);
		//        String objectId = getObjectIdForSecureMethodAccess();

		Logger.out.debug("The User name is: " + user.getName());
		Logger.out.debug("The Object ID is: " + objectId);

		boolean isAuthorized = false;
		try {
			isAuthorized = getAuthorizationManager().checkPermission(
					user.getName(), objectId, EXECUTE);

		} catch (CSException ex) {
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
	 * 
	 * @param searchCriteria
	 * @return List of resultant objects
	 * @throws SMException
	 *             if searchCriteria passed is null or if search results in no
	 *             results
	 * @throws CSException
	 */
	public List getObjects(SearchCriteria searchCriteria) throws SMException,
			CSException {
		if (null == searchCriteria) {
			Logger.out.debug(" Null Parameters passed");
			throw new SMException("Null Parameters passed");
		}
		UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
		List list = userProvisioningManager.getObjects(searchCriteria);
		if (null == list || list.size() <= 0) {
			Logger.out.debug("Search resulted in no results");
			throw new SMException("Search resulted in no results");
		}
		return list;
	}

	public void assignUserToGroup(String userGroupname, String userId)throws SMException
	{
	    Logger.out.debug(" userId: " + userId + " userGroupname:" + userGroupname);
	    
	    if (userId == null || userGroupname == null)
	    {
			Logger.out.debug(" Null or insufficient Parameters passed");
			throw new SMException("Null or insufficient Parameters passed");
		}
	    
		try
		{
		    UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
		    
		    Group group = getUserGroup(userGroupname);
		    if (group != null)
		    {
		        String[] groupIds = {group.getGroupId().toString()};
			    
			    assignAdditionalGroupsToUser(userId, groupIds);
		    }
		    else
		    {
		        Logger.out.debug("No user group with name "+userGroupname+" is present");
		    }
		}
		catch(CSException ex)
		{
		    Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", ex);
			throw new SMException(
					"The Security Service encountered a fatal exception.", ex);
		}
	}
	
	public void removeUserFromGroup(String userGroupname, String userId)throws SMException
	{
	    Logger.out.debug(" userId: " + userId + " userGroupname:" + userGroupname);
	    
	    if (userId == null || userGroupname == null)
	    {
			Logger.out.debug(" Null or insufficient Parameters passed");
			throw new SMException("Null or insufficient Parameters passed");
		}
	    
		try
		{
		    UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
		    
		    Group group = getUserGroup(userGroupname);
		    
		    if (group != null)
		    {
		        userProvisioningManager.removeUserFromGroup(group.getGroupId().toString(), userId);
		    }
		    else
		    {
		        Logger.out.debug("No user group with name "+userGroupname+" is present");
		    }
		}
		catch(CSException ex)
		{
		    Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", ex);
			throw new SMException(
					"The Security Service encountered a fatal exception.", ex);
		}
	}
	
	/**
     * @param userGroupname
     * @return
     * @throws SMException
     * @throws CSException
     */
    private Group getUserGroup(String userGroupname) throws SMException, CSException
    {
        Group group = new Group();
        group.setGroupName(userGroupname);
        SearchCriteria searchCriteria = new GroupSearchCriteria(group);
        List list = getObjects(searchCriteria);
        if (list.isEmpty() == false)
        {
            Logger.out.debug("list size********************"+list.size());
            group = (Group) list.get(0);
            
            return group;
        }
        
        return null;
    }

    public void assignAdditionalGroupsToUser(String userId, String[] groupIds)
			throws SMException {
		if (userId == null || groupIds == null || groupIds.length < 1) {
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
		try {
			userProvisioningManager = getUserProvisioningManager();
			//            user = new User();
			//            user.setUserId(userId);
			//            userSearchCriteria = new UserSearchCriteria(user);
			//            list = getObjects(userSearchCriteria);
			//            user = (User)(list.get(0));
			//            if(user == null )
			//    	    {
			//    	        Logger.out.debug("User with user ID "+userId+" not found");
			//    	        throw new SMException("User with user ID "+userId+" not found");
			//    	    }

			consolidatedGroups = userProvisioningManager.getGroups(userId);
			if (null != consolidatedGroups) {
				Iterator it = consolidatedGroups.iterator();
				while (it.hasNext()) {
					group = (Group) it.next();
					consolidatedGroupIds
							.add(String.valueOf(group.getGroupId()));
				}
			}

			/**
			 * Consolidating all the Groups
			 */

			for (int i = 0; i < groupIds.length; i++) {
				consolidatedGroupIds.add(groupIds[i]);
			}

			finalUserGroupIds = new String[consolidatedGroupIds.size()];
			Iterator it = consolidatedGroupIds.iterator();

			for (int i = 0; it.hasNext(); i++) {
				finalUserGroupIds[i] = (String) it.next();
				Logger.out.debug("Group user is assigned to: "
						+ finalUserGroupIds[i]);
			}

			/**
			 * Setting groups for user and updating it
			 */
			userProvisioningManager.assignGroupsToUser(userId,
					finalUserGroupIds);

		} catch (CSException ex) {
			Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", ex);
			throw new SMException(
					"The Security Service encountered a fatal exception.", ex);
		}

	}

	/**
	 * This method creates protection elements corresponding to protection
	 * objects passed and associates them with static as well as dynamic
	 * protection groups that are passed. It also creates user group, role,
	 * protection group mapping for all the elements in authorization data
	 * 
	 * @param authorizationData
	 *            Vector of SecurityDataBean objects
	 * @param protectionObjects
	 *            Set of AbstractDomainObject instances
	 * @param dynamicGroups
	 *            Array of dynamic group names
	 * @throws SMException
	 */
	public void insertAuthorizationData(Vector authorizationData,
			Set protectionObjects, String[] dynamicGroups) throws SMException {

		Set protectionElements;

		Iterator it;

		try {
			Logger.out
					.debug("************** Inserting authorization Data ***************");

			/**
			 * Create protection elements corresponding to all protection
			 * objects
			 */
			protectionElements = createProtectionElementsFromProtectionObjects(protectionObjects);

			/**
			 * Create user group role protection group and their mappings if
			 * required
			 */
			createUserGroupRoleProtectionGroup(authorizationData,
					protectionElements);
			
			/**
			 * Assigning protection elements to dynamic groups
			 */
			assignProtectionElementsToGroups(protectionElements, dynamicGroups);

			Logger.out
					.debug("************** Inserted authorization Data ***************");

		} catch (CSException e) {
			Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", e);
			throw new SMException(
					"The Security Service encountered a fatal exception.", e);
		}

	}

	/**
	 * This method assigns Protection Elements passed to the Protection group
	 * names passed.
	 * 
	 * @param protectionElements
	 * @param groups
	 * @throws CSException
	 */
	private void assignProtectionElementsToGroups(Set protectionElements,
			String[] groups) {
		ProtectionElement protectionElement;
		Iterator it;
		if (groups != null) {
			for (int i = 0; i < groups.length; i++) {
				for (it = protectionElements.iterator(); it.hasNext();) {
					protectionElement = (ProtectionElement) it.next();
					assignProtectionElementToGroup(protectionElement, groups[i]);
				}
			}
		}
	}

	/**
	 * This method creates user group, role, protection group mappings in
	 * database for the passed authorizationData. It also adds protection
	 * elements to the protection groups for which mapping is made. For each
	 * element in authorization Data passed: User group is created and users are
	 * added to user group if one does not exist by the name passed. Similarly
	 * Protection Group is created and protection elements are added to it if
	 * one does not exist. Finally user group and protection group are
	 * associated with each other by the role they need to be associated with.
	 * If no role exists by the name an exception is thrown and the
	 * corresponding mapping is not created
	 * 
	 * @param authorizationData
	 * @param protectionElements
	 * @throws CSException
	 * @throws SMException
	 */
	private void createUserGroupRoleProtectionGroup(Vector authorizationData,
			Set protectionElements) throws CSException, SMException {
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

		if (authorizationData != null) {
			Logger.out.debug(" UserGroupRoleProtectionGroup Size:"
					+ authorizationData.size());
			for (int i = 0; i < authorizationData.size(); i++) {
				Logger.out.debug(" authorizationData:" + i + " "
						+ authorizationData.get(i).toString());
				try {
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
					try {
						list = getObjects(groupSearchCriteria);
						Logger.out.debug("User group " + group.getGroupName()
								+ " already exists");
					}
					/**
					 * If group does not exist already
					 */
					catch (SMException ex) {
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
					for (it = userGroup.iterator(); it.hasNext();) {
						user = (User) it.next();
						//                    userProvisioningManager.assignGroupsToUser(String.valueOf(user.getUserId()),new
						// String[] {String.valueOf(group.getGroupId())});
						assignAdditionalGroupsToUser(String.valueOf(user
								.getUserId()), new String[] { String
								.valueOf(group.getGroupId()) });
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
					 * If Protection group already exists add protection
					 * elements to the group
					 */
					try {
						list = getObjects(protectionGroupSearchCriteria);
						protectionGroup = (ProtectionGroup) list.get(0);
						Logger.out.debug(" From Database: "
								+ protectionGroup.toString());

					}
					/**
					 * If the protection group does not already exist create the
					 * protection group and add protection elements to it.
					 */
					catch (SMException sme) {
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
				} catch (CSTransactionException ex) {
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
	 * This method creates protection elements from the protection objects
	 * passed and associate them with respective static groups they should be
	 * added to depending on their class name if the corresponding protection
	 * element does not already exist.
	 * 
	 * @param protectionObjects
	 * @return @throws
	 *         CSException
	 */
	private Set createProtectionElementsFromProtectionObjects(
			Set protectionObjects) throws CSException {
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
		if (protectionObjects != null) {
			for (it = protectionObjects.iterator(); it.hasNext();) {
				protectionElement = new ProtectionElement();
				protectionObject = (AbstractDomainObject) it.next();
				protectionElement.setObjectId(protectionObject.getObjectId());

				try {

					/**
					 * In case protection element already exists
					 */
					try {
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
					catch (CSObjectNotFoundException csex) {
						protectionElement
								.setApplication(getApplication(CATISSUE_CORE_CONTEXT_NAME));
						protectionElement
								.setProtectionElementDescription(protectionObject
										.getClass().getName()
										+ " object");
						protectionElement
								.setProtectionElementName(protectionObject.getObjectId());
						/**
						 * Adding protection elements to static groups they
						 * should be added to
						 */
						staticGroups = (String[]) Constants.STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES
								.get(protectionObject.getClass().getName());

						if (staticGroups != null) {
							protectionGroups = new HashSet();
							for (int i = 0; i < staticGroups.length; i++) {
								Logger.out.debug(" group name " + i + " "
										+ staticGroups[i]);
								protectionGroup = new ProtectionGroup();
								protectionGroup
										.setProtectionGroupName(staticGroups[i]);
								protectionGroupSearchCriteria = new ProtectionGroupSearchCriteria(
										protectionGroup);
								try {
									list = getObjects(protectionGroupSearchCriteria);
									protectionGroup = (ProtectionGroup) list
											.get(0);
									Logger.out.debug(" From Database: "
											+ protectionGroup.toString());
									protectionGroups.add(protectionGroup);
								} catch (SMException sme) {
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
				} catch (CSTransactionException ex) {
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
	 * @param protectionObject
	 * @return
	 */
//	private String getObjectId(AbstractDomainObject protectionObject) {
//		if(protectionObject instanceof Specimen)
//		{
//			Logger.out.debug(protectionObject.getClass().getName()+" is an instance of Specimen class");
//			return Specimen.class.getName()
//			+ "_" + protectionObject.getSystemIdentifier();
//		}
//		return protectionObject.getClass()
//				.getName()
//				+ "_" + protectionObject.getSystemIdentifier();
//	}

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
		try {
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			userProvisioningManager.assignProtectionElement(GroupsName,
					protectionElement.getObjectId());
			Logger.out.debug("Associated protection group: " + GroupsName
					+ " to protectionElement"
					+ protectionElement.getProtectionElementName());
		}

		catch (CSException e) {
			Logger.out
					.error(
							"The Security Service encountered an error while associating protection group: "
									+ GroupsName
									+ " to protectionElement"
									+ protectionElement
											.getProtectionElementName());
		}
	}

	public boolean isAuthorized(String userName, String objectId,
			String privilegeName) throws SMException {
		try {
			boolean isAuthorized = getAuthorizationManager().checkPermission(
					userName, objectId, privilegeName);
			Logger.out.debug(" User:" + userName + " objectId:" + objectId
					+ " privilegeName:" + privilegeName + " isAuthorized:"
					+ isAuthorized);
			return isAuthorized;
		} catch (CSException e) {
			Logger.out.debug("Unable to get all users: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	public boolean isAuthorized(String userName, String objectId,
			String attributeName, String privilegeName) throws SMException {
		try {
			return getAuthorizationManager().checkPermission(userName,
					objectId, attributeName, privilegeName);
		} catch (CSException e) {
			Logger.out.debug("Unable to get all users: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	public boolean checkPermission(String userName, String objectType,
			String objectIdentifier, String privilegeName) throws SMException {
		try {
			boolean isAuthorized = getAuthorizationManager().checkPermission(
					userName, objectType + "_" + objectIdentifier,
					privilegeName);
			Logger.out.debug(" User:" + userName + "objectType:" + objectType
					+ " objectId:" + objectIdentifier + " privilegeName:"
					+ privilegeName + " isAuthorized:" + isAuthorized);
			return isAuthorized;
		} catch (CSException e) {
			Logger.out.debug("Unable to get all users: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * This method returns name of the Protection groupwhich consists of obj as
	 * Protection Element and whose name consists of string nameConsistingOf
	 * 
	 * @param obj
	 * @param nameConsistingOf
	 * @return @throws
	 *         SMException
	 */
	public String getProtectionGroupByName(AbstractDomainObject obj,
			String nameConsistingOf) throws SMException {
		Set protectionGroups;
		Iterator it;
		ProtectionGroup protectionGroup;
		ProtectionElement protectionElement;
		String name = null;
		String protectionElementName = obj.getObjectId();
		try {
			protectionElement = getAuthorizationManager().getProtectionElement(
					protectionElementName);
			protectionGroups = getAuthorizationManager().getProtectionGroups(
					protectionElement.getProtectionElementId().toString());
			it = protectionGroups.iterator();
			while (it.hasNext()) {
				protectionGroup = (ProtectionGroup) it.next();
				name = protectionGroup.getProtectionGroupName();
				if (name.indexOf(nameConsistingOf) != -1) {
					Logger.out.debug("protection group by name "
							+ nameConsistingOf + " for Protection Element "
							+ protectionElementName + " is " + name);
					return name;
				}
			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get protection group by name "
					+ nameConsistingOf + " for Protection Element "
					+ protectionElementName + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return name;

	}
	
	/**
	 * This method returns name of the Protection groupwhich consists of obj as
	 * Protection Element and whose name consists of string nameConsistingOf
	 * 
	 * @param obj
	 * @param nameConsistingOf
	 * @return @throws
	 *         SMException
	 */
	public String[] getProtectionGroupByName(AbstractDomainObject obj
			) throws SMException {
		Set protectionGroups;
		Iterator it;
		ProtectionGroup protectionGroup;
		ProtectionElement protectionElement;
		String name = null;
		String[] names = null;
		String protectionElementName = obj.getObjectId();
		try {
			protectionElement = getAuthorizationManager().getProtectionElement(
					protectionElementName);
			protectionGroups = getAuthorizationManager().getProtectionGroups(
					protectionElement.getProtectionElementId().toString());
			it = protectionGroups.iterator();
			names = new String[protectionGroups.size()];
			int i=0;
			while (it.hasNext()) {
				protectionGroup = (ProtectionGroup) it.next();
				names[i++] = protectionGroup.getProtectionGroupName();
				
			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get protection group by name "
					+  " for Protection Element "
					+ protectionElementName + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return names;

	}
	
	
	

	/**
	 * Returns name value beans corresponding to all privileges that can be
	 * assigned for Assign Privileges Page
	 * 
	 * @param userName
	 *            login name of user logged in
	 * @return
	 */
	public Vector getPrivilegesForAssignPrivilege(String userName) {
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
	 * on which user with identifier userID has privilege ASSIGN_ <
	 * <privilegeName>>.
	 * 
	 * @param userID
	 * @param objectType
	 * @param privilegeName
	 * @return @throws
	 *         SMException thrown if any error occurs while retreiving
	 *         ProtectionElementPrivilegeContextForUser
	 */
	private Set getObjectsForAssignPrivilege(Collection privilegeMap,
			String objectType, String privilegeName) throws SMException {
		Logger.out.debug(" objectType:" + objectType + " privilegeName:"
				+ privilegeName);
		Set objects = new HashSet();
		NameValueBean nameValueBean;

		ObjectPrivilegeMap objectPrivilegeMap;

		Collection privileges;
		Iterator iterator;
		String objectId;
		Privilege privilege;

		if (privilegeMap != null) {
			iterator = privilegeMap.iterator();
			while (iterator.hasNext()) {
				objectPrivilegeMap = (ObjectPrivilegeMap) iterator.next();
				objectId = objectPrivilegeMap.getProtectionElement()
						.getObjectId();
				Logger.out.debug(objectId);
				if (objectId.indexOf(objectType + "_") != -1) {
					privileges = objectPrivilegeMap.getPrivileges();
					Logger.out.debug("Privileges:" + privileges.size());
					Iterator it = privileges.iterator();
					while (it.hasNext()) {
						privilege = (Privilege) it.next();
						Logger.out.debug(" Privilege:" + privilege.getName());

						if (privilege.getName().equals(
								"ASSIGN_" + privilegeName)) {
							nameValueBean = new NameValueBean(objectId
									.substring(objectId.lastIndexOf("_") + 1),
									objectId.substring(objectId
											.lastIndexOf("_") + 1));
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

	/**
	 * This method returns name value beans of the object ids for types
	 * identified by objectTypes on which user can assign privileges identified
	 * by privilegeNames User needs to have ASSIGN_ < <privilegeName>>privilege
	 * on these objects to assign corresponding privilege on them identified by
	 * userID has
	 * 
	 * @param userID
	 * @param objectTypes
	 * @param privilegeNames
	 * @return @throws
	 *         SMException
	 */
	public Set getObjectsForAssignPrivilege(String userID,
			String[] objectTypes, String[] privilegeNames) throws SMException {
		Set objects = new HashSet();
		AuthorizationManager authorizationManager;
		Collection privilegeMap;
		List list;
		try {
			if (objectTypes == null || privilegeNames == null) {
				return objects;
			}
			authorizationManager = getAuthorizationManager();

			ProtectionElement protectionElement;
			ProtectionElementSearchCriteria protectionElementSearchCriteria;
			User user = new User();
			user = getUserById(userID);
			if (user == null) {
				Logger.out.debug(" User not found");
				return objects;
			}
			Logger.out.debug("user login name:" + user.getLoginName());

			for (int i = 0; i < objectTypes.length; i++) {
				for (int j = 0; j < privilegeNames.length; j++) {

					try {
						Logger.out.debug("objectType:" + objectTypes[i]);
						protectionElement = new ProtectionElement();
						protectionElement.setObjectId(objectTypes[i] + "_*");
						protectionElementSearchCriteria = new ProtectionElementSearchCriteria(
								protectionElement);
						list = getObjects(protectionElementSearchCriteria);
						privilegeMap = authorizationManager.getPrivilegeMap(
								user.getLoginName(), list);
						for (int k = 0; k < list.size(); k++) {
							protectionElement = (ProtectionElement) list.get(k);
							Logger.out.debug(protectionElement.getObjectId()
									+ " " + protectionElement.getAttribute());
						}

						objects
								.addAll(getObjectsForAssignPrivilege(
										privilegeMap, objectTypes[i],
										privilegeNames[j]));
					} catch (SMException smex) {
						Logger.out.debug(" Exception:", smex);
					}
				}
			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get objects: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return objects;

	}

	//    public void setOwnerForProtectionElement(
	//            edu.wustl.catissuecore.domain.User user, java.lang.String userName)
	//            throws SMException
	//    {
	//        if (user != null && userName != null)
	//        {
	//            String protectionElementObjectId = user.getClass().getName() + "_"
	//                    + user.getSystemIdentifier();
	//            Logger.out.debug(" Protection Element Object Id:"
	//                    + protectionElementObjectId);
	//            Logger.out.debug(" userName:" + userName);
	//            try
	//            {
	//                UserProvisioningManager userProvisioningManager =
	// getUserProvisioningManager();
	//                userProvisioningManager.setOwnerForProtectionElement(
	//                        protectionElementObjectId, new String[]{userName});
	//            }
	//            catch (CSException e)
	//            {
	//                Logger.out.debug("Unable to set owner: Exception: "
	//                        + e.getMessage());
	//                throw new SMException(e.getMessage(), e);
	//            }
	//        }
	//        else
	//        {
	//            Logger.out.debug("user:" + user + " username:" + userName);
	//        }
	//    }
	
	/**
	 * This method assigns privilege by privilegeName to the user identified by
	 * userId on the objects identified by objectIds
	 * 
	 * @param privilegeName
	 * @param objectIds
	 * @param userId
	 * @throws SMException
	 */
	public void assignPrivilegeToUser(String privilegeName, Class objectType,
			Long[] objectIds, Long userId, boolean assignOperation) throws SMException 
	{
	    Logger.out.debug("In assignPrivilegeToUser...");
		Logger.out.debug("privilegeName:" + privilegeName + " objectType:"
				+ objectType + " objectIds:"
				+ Utility.getArrayString(objectIds) + " userId:" + userId);
		
		if (privilegeName == null || objectType == null || objectIds == null
				|| userId == null) 
		{
			Logger.out
				.debug("Cannot assign privilege to user. One of the parameters is null.");
		} 
		else 
		{
			String protectionGroupName = null;
			String roleName;
			Role role;
			ProtectionGroup protectionGroup;
			
			try 
			{
				
				//Getting Appropriate Role
				//role name is generated as <<privilegeName>>_ONLY
			    if (privilegeName.equals(Permissions.READ))
			        roleName = Permissions.READ_DENIED;
			    else
			        roleName = privilegeName + "_ONLY";
				role = getRole(roleName);
				Logger.out.debug("Operation>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(assignOperation == true?"Remove READ_DENIED":"Add READ_DENIED"));
				
				Set roles = new HashSet();
				roles.add(role);
				
				if (privilegeName.equals(Permissions.USE))
			    {
				    protectionGroupName = "PG_" + userId + "_ROLE_" + role.getId();
				    
					if (assignOperation == edu.wustl.catissuecore.util.global.Constants.PRIVILEGE_ASSIGN)
					{
					    Logger.out.debug("Assign Protection elements");
					    
					    protectionGroup = getProtectionGroup(protectionGroupName);
					    
					    // Assign Protection elements to Protection Group
					    assignProtectionElements(protectionGroup
								.getProtectionGroupName(), objectType, objectIds);
					    
					    assignUserRoleToProtectionGroup(userId, roles, protectionGroup, assignOperation);
					}
					else
					{
					    Logger.out.debug("De Assign Protection elements");
					    Logger.out.debug("protectionGroupName : "+protectionGroupName+" objectType : "+objectType+" objectIds : "+Utility.getArrayString(objectIds)); 
					    deAssignProtectionElements(protectionGroupName,objectType,objectIds);
					}
			    }
				else
				{
				    // In case of assign remove the READ_DENIED privilege of the user
				    // and in case of de-assign add the READ_DENIED privilege to the user.
				    assignOperation = ! assignOperation;
				    
				    for (int i = 0; i < objectIds.length;i++)
					{
					    // Getting Appropriate Group
						// Protection Group Name is generated as
						// PG_<<userID>>_ROLE_<<roleID>>
					    
					    Logger.out.debug("objectType............................"+objectType);
					    //changed by ajay
					    
					    if (objectType.getName().equals(CollectionProtocol.class.getName()))
					        protectionGroupName = Constants.getCollectionProtocolPGName(objectIds[i]);
					    else if (objectType.getName().equals(DistributionProtocol.class.getName()))
					        protectionGroupName = Constants.getDistributionProtocolPGName(objectIds[i]);
					    
					    protectionGroup = getProtectionGroup(protectionGroupName);
					    
						Logger.out.debug("Assign User Role To Protection Group");
						
						//Assign User Role To Protection Group
						assignUserRoleToProtectionGroup(userId, roles, protectionGroup, assignOperation);
					}
				}
			} 
			catch (CSException csex)
			{
				throw new SMException(csex);
			}
		}
	}

	/**
	 * This method returns protection group corresponding to the naem passed. In
	 * case it does not exist it creates one and returns that.
	 * 
	 * @param protectionGroupName
	 * @return @throws
	 *         CSException
	 * @throws CSTransactionException
	 * @throws SMException
	 */
	private ProtectionGroup getProtectionGroup(String protectionGroupName)
			throws CSException, CSTransactionException, SMException {
		Logger.out.debug(" protectionGroupName:" + protectionGroupName);
		if (protectionGroupName == null) {
			Logger.out.debug("protectionGroupName passed is null");
			throw new SMException("No protectionGroup of name null");
		}
		
		//Search for Protection Group of the name passed
		ProtectionGroupSearchCriteria protectionGroupSearchCriteria;
		ProtectionGroup protectionGroup;
		protectionGroup = new ProtectionGroup();
		protectionGroup.setProtectionGroupName(protectionGroupName);
		protectionGroupSearchCriteria = new ProtectionGroupSearchCriteria(
				protectionGroup);
		UserProvisioningManager userProvisioningManager = null;
		List list;
		try {
			userProvisioningManager = getUserProvisioningManager();
			list = getObjects(protectionGroupSearchCriteria);
		} catch (SMException e) {
			Logger.out.debug("Protection Group not found by name "
					+ protectionGroupName);
			userProvisioningManager.createProtectionGroup(protectionGroup);
			list = getObjects(protectionGroupSearchCriteria);
		}
		protectionGroup = (ProtectionGroup) list.get(0);

		Logger.out.debug(" ID of ProtectionGroup "
				+ protectionGroup.getProtectionGroupName() + " is "
				+ protectionGroup.getProtectionGroupId());
		return protectionGroup;
	}

	/**
	 * This method returns role corresponding to the rolename passed
	 * 
	 * @param privilegeName
	 * @param list
	 * @return @throws
	 *         CSException
	 * @throws SMException
	 */
	private Role getRole(String roleName) throws CSException, SMException {
		Logger.out.debug(" roleName:" + roleName);
		if (roleName == null) {
			Logger.out.debug("Rolename passed is null");
			throw new SMException("No role of name null");
		}

		//Search for role by the name roleName
		RoleSearchCriteria roleSearchCriteria;
		Role role;
		role = new Role();
		role.setName(roleName);
		roleSearchCriteria = new RoleSearchCriteria(role);
		List list;
		try {
			list = getObjects(roleSearchCriteria);
		} catch (SMException e) {
			Logger.out.debug("Role not found by name " + roleName);
			throw new SMException("Role not found by name " + roleName, e);
		}
		role = (Role) list.get(0);
		Logger.out.debug(" RoleId of role " + role.getName() + " is "
				+ role.getId());
		return role;
	}

	/**
	 * This method assigns privilege by privilegeName to the user group
	 * identified by role corresponding to roleId on the objects identified by
	 * objectIds
	 * 
	 * @param privilegeName
	 * @param objectIds
	 * @param roleId
	 * @throws SMException
	 */
	public void assignPrivilegeToGroup(String privilegeName, Class objectType,
			Long[] objectIds, String roleId, boolean assignOperation) throws SMException {

		Logger.out.debug("privilegeName:" + privilegeName + " objectType:"
				+ objectType + " objectIds:"
				+ Utility.getArrayString(objectIds) + " roleId:" + roleId);
		if (privilegeName == null || objectType == null || objectIds == null
				|| roleId == null) {
			Logger.out
					.debug("Cannot assign privilege to user. One of the parameters is null.");
		} else {
			String groupId;
			UserProvisioningManager userProvisioningManager;
			String protectionGroupName = null;
			String roleName;
//			RoleSearchCriteria roleSearchCriteria;
			Role role;
			List list;
//			ProtectionGroupSearchCriteria protectionGroupSearchCriteria;
			ProtectionGroup protectionGroup;
			try {
				//Get user group for the corresponding role
				groupId = getGroupIdForRole(roleId);
				userProvisioningManager = getUserProvisioningManager();
				
				//Getting Appropriate Role
				//role name is generated as <<privilegeName>>_ONLY
				if (privilegeName.equals(Permissions.READ))
			        roleName = Permissions.READ_DENIED;
			    else
			        roleName = privilegeName + "_ONLY";
				role = getRole(roleName);
				
				Set roles = new HashSet();
				roles.add(role);
				
				if (privilegeName.equals("USE"))
			    {
				    protectionGroupName = "PG_GROUP_" + groupId + "_ROLE_" + role.getId();
				    
				    if (assignOperation == edu.wustl.catissuecore.util.global.Constants.PRIVILEGE_ASSIGN)
					{
				        protectionGroup = getProtectionGroup(protectionGroupName);
					    
				        Logger.out.debug("Assign Protection elements");
						//Assign Protection elements to Protection Group
						assignProtectionElements(protectionGroup
								.getProtectionGroupName(), objectType, objectIds);
					    
					    assignGroupRoleToProtectionGroup(Long.valueOf(groupId), roles,
								protectionGroup, assignOperation);
					}
					else
					{
					    Logger.out.debug("De Assign Protection elements");
					    
					    deAssignProtectionElements(protectionGroupName, objectType, objectIds);
					}
			    }
				else
				{
					Logger.out.debug("Value Before#####################"+assignOperation);

				    // In case of assign remove the READ_DENIED privilege of the group
				    // and in case of de-assign add the READ_DENIED privilege to the group.
				    assignOperation = ! assignOperation;
				    
				    Logger.out.debug("Value After#####################"+assignOperation);
				
					for (int i = 0; i < objectIds.length;i++)
					{
	
						//Getting Appropriate Group
						// Protection Group Name is generated as
						// PG_<<userID>>_ROLE_<<roleID>>
		//				protectionGroupName = "PG_GROUP_" + groupId + "_ROLE_"
		//						+ role.getId();
						Logger.out.debug("objectType............................"+objectType);
					    if (objectType.getName().equals(CollectionProtocol.class.getName()))
					        protectionGroupName = Constants.getCollectionProtocolPGName(objectIds[i]);
					    else if (objectType.getName().equals(DistributionProtocol.class.getName()))
					        protectionGroupName = Constants.getDistributionProtocolPGName(objectIds[i]);
					    
						protectionGroup = getProtectionGroup(protectionGroupName);
						
		//				Logger.out.debug("Assign Protection elements");
		//				//Assign Protection elements to Protection Group
		//				assignProtectionElements(protectionGroup
		//						.getProtectionGroupName(), objectType, objectIds);
		
						Logger.out.debug("Assign Group Role To Protection Group");
						
						//Assign User Role To Protection Group
						assignGroupRoleToProtectionGroup(Long.valueOf(groupId), roles,
								protectionGroup, assignOperation);
					}
				}

			} catch (CSException csex) {
				throw new SMException(csex);
			}
		}

	}

	/**
	 * This method assigns additional protection Elements identified by
	 * protectionElementIds to the protection Group identified by
	 * protectionGroupName
	 * 
	 * @param protectionGroupName
	 * @param objectIds
	 * @throws SMException
	 */
	public void assignProtectionElements(String protectionGroupName,
			Class objectType, Long[] objectIds) throws SMException {
		try {
			Logger.out.debug("Protection Group Name:" + protectionGroupName
					+ " objectType:" + objectType + " protectionElementIds:"
					+ Utility.getArrayString(objectIds));

			if (protectionGroupName == null || objectType == null
					|| objectIds == null) {
				Logger.out.debug(" One of the parameters is null");
				throw new SMException(
						"Could not assign Protection elements to protection group. One or more parameters are null");
			}

			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			for (int i = 0; i < objectIds.length; i++) {
				try {
					userProvisioningManager.assignProtectionElement(
							protectionGroupName, objectType.getName() + "_"
									+ objectIds[i]);
				} catch (CSTransactionException txex) //thrown when association
				// already exists
				{
					Logger.out.debug("Exception:" + txex.getMessage());
				}
			}
		} catch (CSException csex) {
			Logger.out.debug(
					"Could not assign Protection elements to protection group",
					csex);
			throw new SMException(
					"Could not assign Protection elements to protection group",
					csex);
		}
	}

	/**
	 * This method assigns user identified by userId, roles identified by roles
	 * on protectionGroup
	 * 
	 * @param userId
	 * @param roles
	 * @param protectionGroup
	 * @throws SMException
	 */
	public void assignUserRoleToProtectionGroup(Long userId, Set roles,
			ProtectionGroup protectionGroup, boolean assignOperation) throws SMException {
		Logger.out.debug("userId:" + userId + " roles:" + roles
				+ " protectionGroup:" + protectionGroup);
		if (userId == null || roles == null || protectionGroup == null) {
			Logger.out
					.debug("Could not assign user role to protection group. One or more parameters are null");
			throw new SMException(
					"Could not assign user role to protection group");
		}
		Set protectionGroupRoleContextSet;
		ProtectionGroupRoleContext protectionGroupRoleContext;
		Iterator it;
		Set aggregatedRoles = new HashSet();
		String[] roleIds = null;
		try {
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			protectionGroupRoleContextSet = userProvisioningManager
					.getProtectionGroupRoleContextForUser(String
							.valueOf(userId));
			
			//get all the roles that user has on this protection group
			it = protectionGroupRoleContextSet.iterator();
			while (it.hasNext()) {
				protectionGroupRoleContext = (ProtectionGroupRoleContext) it
						.next();
				if (protectionGroupRoleContext.getProtectionGroup()
						.getProtectionGroupId().equals(
								protectionGroup.getProtectionGroupId())) {
					aggregatedRoles.addAll(protectionGroupRoleContext
							.getRoles());
					
					break;
				}
			}
			
			// if the operation is assign, add the roles to be assigned.
			if (assignOperation == edu.wustl.catissuecore.util.global.Constants.PRIVILEGE_ASSIGN)
			{
			    aggregatedRoles.addAll(roles);
			}
			else // if the operation is de-assign, remove the roles to be de-assigned.
			{
			    Set newaggregateRoles = removeRoles(aggregatedRoles, roles);
				aggregatedRoles = newaggregateRoles;
			}
			
			roleIds = new String[aggregatedRoles.size()];
			Iterator roleIt = aggregatedRoles.iterator();
			
			for (int i = 0; roleIt.hasNext(); i++) {
				roleIds[i] = String.valueOf(((Role) roleIt.next()).getId());
			}

			userProvisioningManager.assignUserRoleToProtectionGroup(String
					.valueOf(userId), roleIds, String.valueOf(protectionGroup
					.getProtectionGroupId()));

		} catch (CSException csex) {
			Logger.out.debug("Could not assign user role to protection group",
					csex);
			throw new SMException(
					"Could not assign user role to protection group", csex);
		}
	}

	/**
	 * This method assigns user group identified by groupId, roles identified by
	 * roles on protectionGroup
	 * 
	 * @param groupId
	 * @param roles
	 * @param protectionGroup
	 * @throws SMException
	 */
	public void assignGroupRoleToProtectionGroup(Long groupId, Set roles,
			ProtectionGroup protectionGroup, boolean assignOperation) throws SMException {
		Logger.out.debug("userId:" + groupId + " roles:" + roles
				+ " protectionGroup:"
				+ protectionGroup.getProtectionGroupName());

		if (groupId == null || roles == null || protectionGroup == null) {
			Logger.out
					.debug("Could not assign group role to protection group. One or more parameters are null");
			throw new SMException(
					"Could not assign group role to protection group. One or more parameters are null");
		}
		Set protectionGroupRoleContextSet;
		ProtectionGroupRoleContext protectionGroupRoleContext;
		Iterator it;
		Set aggregatedRoles = new HashSet();
		String[] roleIds = null;
		Role role;
		try {
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			protectionGroupRoleContextSet = userProvisioningManager
					.getProtectionGroupRoleContextForGroup(String
							.valueOf(groupId));

			it = protectionGroupRoleContextSet.iterator();
			while (it.hasNext()) {
				protectionGroupRoleContext = (ProtectionGroupRoleContext) it
						.next();
				if (protectionGroupRoleContext.getProtectionGroup()
						.getProtectionGroupId().equals(
								protectionGroup.getProtectionGroupId())) {
					aggregatedRoles.addAll(protectionGroupRoleContext
							.getRoles());

					break;
				}
			}

			// if the operation is assign, add the roles to be assigned.
			if (assignOperation == edu.wustl.catissuecore.util.global.Constants.PRIVILEGE_ASSIGN)
			{
			    aggregatedRoles.addAll(roles);
			}
			else // if the operation is de-assign, remove the roles to be de-assigned.
			{
			    Set newaggregateRoles = removeRoles(aggregatedRoles, roles);
				aggregatedRoles = newaggregateRoles;
			}
			
			roleIds = new String[aggregatedRoles.size()];
			Iterator roleIt = aggregatedRoles.iterator();

			for (int i = 0; roleIt.hasNext(); i++) {
				role = (Role) roleIt.next();
				Logger.out.debug(" Role " + i + 1 + " " + role.getName());
				roleIds[i] = String.valueOf(role.getId());
			}
			
			userProvisioningManager.assignGroupRoleToProtectionGroup(String
					.valueOf(protectionGroup.getProtectionGroupId()), String
					.valueOf(groupId), roleIds);

		} catch (CSException csex) {
			Logger.out.debug("Could not assign user role to protection group",
					csex);
			throw new SMException(
					"Could not assign user role to protection group", csex);
		}
	}

	private Set removeRoles(Set fromSet, Set toSet)
	{
	    Set differnceRoles = new HashSet();
		Iterator fromSetiterator = fromSet.iterator();
		while (fromSetiterator.hasNext())
		{
		    Role role1  = (Role) fromSetiterator.next();
		    
		    Iterator toSetIterator = toSet.iterator();
		    while (toSetIterator.hasNext())
		    {
		        Role role2 = (Role) toSetIterator.next();
		        
		        if (role1.getId().equals(role2.getId()) == false)
		        {
		            differnceRoles.add(role1);
		        }
		    }
		}
		
		return differnceRoles;
	}

	/**
	 * @param protectionGroupName
	 * @param objectType
	 * @param objectIds
	 * @throws SMException
	 */
	private void deAssignProtectionElements(String protectionGroupName,
			Class objectType, Long[] objectIds) throws SMException {
		try {
			Logger.out.debug("Protection Group Name:" + protectionGroupName
					+ " protectionElementIds:"
					+ Utility.getArrayString(objectIds));
			if (protectionGroupName == null || objectType == null
					|| objectIds == null) {
				Logger.out
						.debug("Cannot disassign protection elements. One of the parameters is null.");
				throw new SMException(
						"Could not deassign Protection elements to protection group. One of the parameters is null.");
			}
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			for (int i = 0; i < objectIds.length; i++) {
				try {
					Logger.out.debug(" protectionGroupName:"
							+ protectionGroupName + " objectId:"
							+ objectType.getName() + "_" + objectIds[i]);
					userProvisioningManager.deAssignProtectionElements(
							protectionGroupName, objectType.getName() + "_"
									+ objectIds[i]);
				} catch (CSTransactionException txex) //thrown when no
				// association exists
				{
					Logger.out.debug("Exception:" + txex.getMessage(), txex);
				}
			}
		} catch (CSException csex) {
			Logger.out
					.debug(
							"Could not deassign Protection elements to protection group",
							csex);
			throw new SMException(
					"Could not deassign Protection elements to protection group",
					csex);
		}
	}

	/**
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param aList
	 */
	public void filterRow(SessionDataBean sessionDataBean,
			Map queryResultObjectDataMap, List aList) 
	{
	    // boolean that indicated whether user has privilege on main object
		boolean isAuthorizedForMain = false;
		
		// boolean that indicated whether user has privilege on related object
		boolean isAuthorizedForRelated = false;
		
		// boolean that indicates whether user has privilege on identified data
		boolean hasPrivilegeOnIdentifiedData = false;
		
		Vector objectColumnIds;
		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData2;
		QueryResultObjectData queryResultObjectData3;
		Vector queryObjects;
		Map columnIdsMap = new HashMap();

		//Aarti: For all objects in objectIdentifiers check permission on the
		// objects
		//In case user is not authorized to access an object make
		//value of all the columns that are dependent on this object ##
		//		for(int j=0; j< objectIdentifiers.length; j++)
		//		{
		//			isAuthorized =
		// checkPermission(sessionDataBean.getUserName(),objectIdentifiers[j][0],aList.get(Integer.parseInt(objectIdentifiers[j][1])));
		//			if(!isAuthorized)
		//			{
		//				objectColumnIds = (Vector)columnIdsMap.get(objectIdentifiers[j][0]);
		//				if(objectColumnIds!=null)
		//				{
		//					for(int k=0; k<objectColumnIds.size();k++)
		//					{
		//						aList.set(((Integer)objectColumnIds.get(k)).intValue()-1,"##");
		//					}
		//				}
		//			}
		//		}
		
		for (; keyIterator.hasNext();) 
		{
			queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap
					.get(keyIterator.next());
			isAuthorizedForMain = checkPermission(sessionDataBean.getUserName(), queryResultObjectData2
							.getAliasName(), aList.get(queryResultObjectData2
							.getIdentifierColumnId()), Permissions.READ_DENIED);
			
			isAuthorizedForMain = !isAuthorizedForMain;
			Logger.out.debug("Main object:"
					+ queryResultObjectData2.getAliasName()
					+ " isAuthorizedForMain:" + isAuthorizedForMain);
			
			//Remove the data from the fields directly related to main object
			if (!isAuthorizedForMain) 
			{
			    Logger.out.debug("Removed Main Object Fields...................");
				removeUnauthorizedFieldsData(aList, queryResultObjectData2, false);
			} 
			else 
			{
				hasPrivilegeOnIdentifiedData = checkPermission(sessionDataBean
						.getUserName(), queryResultObjectData2.getAliasName(),
						aList.get(queryResultObjectData2.getIdentifierColumnId()),
						Permissions.IDENTIFIED_DATA_ACCESS);
				
				Logger.out.debug("hasPrivilegeOnIdentifiedData:"
						+ hasPrivilegeOnIdentifiedData);
				
				if (!hasPrivilegeOnIdentifiedData) 
				{
					removeUnauthorizedFieldsData(aList, queryResultObjectData2, true);
				}
			}

			Logger.out.debug("isAuthorizedForMain***********************"+isAuthorizedForMain);
			// Check the privilege on related objects when the privilege on main object is de-assigned.
//			if (isAuthorizedForMain == false)
			{
			    Logger.out.debug("Check Permission of Related Objects..................");
			    queryObjects = queryResultObjectData2.getRelatedQueryResultObjects();
				for (int j = 0; j < queryObjects.size(); j++) 
				{
					queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
					
					//If authorized to see the main object then check for
					// authorization on dependent object
					if (isAuthorizedForMain) 
					{
						isAuthorizedForRelated = checkPermission(sessionDataBean
								.getUserName(), queryResultObjectData3
								.getAliasName(), aList.get(queryResultObjectData3
								.getIdentifierColumnId()), Permissions.READ_DENIED);
						isAuthorizedForRelated = !isAuthorizedForRelated;
					}
					//else set it false
					else 
					{
						isAuthorizedForRelated = false;
					}
					
					Logger.out.debug("Related object:"
							+ queryResultObjectData3.getAliasName()
							+ " isAuthorizedForRelated:" + isAuthorizedForRelated);

					//If not authorized to see related objects
					//remove the data from the fields directly related to related
					// object
					if (!isAuthorizedForRelated) 
					{
						removeUnauthorizedFieldsData(aList, queryResultObjectData3,
								false);
					} 
					else 
					{
						hasPrivilegeOnIdentifiedData = checkPermission(
								sessionDataBean.getUserName(),
								queryResultObjectData3.getAliasName(), aList
										.get(queryResultObjectData3.getIdentifierColumnId()),
								Permissions.IDENTIFIED_DATA_ACCESS);
						
						if (!hasPrivilegeOnIdentifiedData) 
						{
							removeUnauthorizedFieldsData(aList,queryResultObjectData3, true);
						}
					}
				}
			}
		}
	}

	/**
	 * This method removes data from list aList.
	 * It could be all data related to QueryResultObjectData
	 * or only the identified fields depending on 
	 * the value of boolean removeOnlyIdentifiedData
	 * user
	 * @param aList
	 * @param queryResultObjectData3
	 * @param removeOnlyIdentifiedData
	 */
	private void removeUnauthorizedFieldsData(List aList,
			QueryResultObjectData queryResultObjectData3,
			boolean removeOnlyIdentifiedData) {

		Logger.out.debug(" Table:" + queryResultObjectData3.getAliasName()
				+ " removeOnlyIdentifiedData:" + removeOnlyIdentifiedData);
		Vector objectColumnIds;

		//If removeOnlyIdentifiedData is true then get Identified data column
		// ids
		//else get all column Ids to remove them
		if (removeOnlyIdentifiedData) {
			objectColumnIds = queryResultObjectData3
					.getIdentifiedDataColumnIds();
		} else {
			objectColumnIds = queryResultObjectData3.getDependentColumnIds();
		}
		Logger.out.debug("objectColumnIds:" + objectColumnIds);
		if (objectColumnIds != null) {
			for (int k = 0; k < objectColumnIds.size(); k++) {
				aList.set(((Integer) objectColumnIds.get(k)).intValue() - 1, "##");
			}
		}
	}

	/**
	 * This method checks whether user identified by userName has given
	 * permission on object identified by identifier of table identified by
	 * tableAlias
	 * 
	 * @param userName
	 * @param tableAlias
	 * @param identifier
	 * @param permission
	 * @return
	 */
	public boolean checkPermission(String userName, String tableAlias,
			Object identifier, String permission) {
		boolean isAuthorized = false;
		String tableName = (String) AbstractClient.objectTableNames.get(tableAlias);
		Logger.out.debug(" AliasName:" + tableAlias + " tableName:" + tableName
				+ " Identifier:" + identifier + " Permission:" + permission);
		
		String className;
		if(tableName.equals(Constants.CATISSUE_SPECIMEN))
		{
			//modified by ajay
			className = "edu.wustl.catissuecore.domain.Specimen";
		}
		//Get classname mapping to tableAlias
		else
		{
			className = HibernateMetaData.getClassName(tableName);
		}
		if (className == null) {
			return isAuthorized;
		}
		

		//checking privilege type on class.
		//whether it is class level / object level / no privilege
		int privilegeType = Integer.parseInt((String) AbstractClient.privilegeTypeMap
				.get(tableAlias));
		Logger.out.debug(" privilege type:" + privilegeType);

		try {
			//If type of privilege is class level check user's privilege on
			// class
			if (privilegeType == Constants.CLASS_LEVEL_SECURE_RETRIEVE) {
				isAuthorized = SecurityManager.getInstance(this.getClass())
						.isAuthorized(userName, className, permission);
			}
			//else if it is object level check user's privilege on object
			// identifier
			else if (privilegeType == Constants.OBJECT_LEVEL_SECURE_RETRIEVE) {
				isAuthorized = SecurityManager.getInstance(this.getClass())
						.checkPermission(userName, className,
								String.valueOf(identifier), permission);
			}
			//else no privilege needs to be checked
			else if (privilegeType == Constants.INSECURE_RETRIEVE) 
			{
				isAuthorized = true;
			}

		} catch (SMException e) {
			Logger.out.debug(" Exception while checking permission:"
					+ e.getMessage(), e);
			return isAuthorized;
		}
		return isAuthorized;
	}
	
	

}



