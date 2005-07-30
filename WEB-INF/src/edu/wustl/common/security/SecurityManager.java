/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.common.security;


import java.util.List;
import java.util.Vector;

import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSTransactionException;


/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class SecurityManager
{
    private static AuthenticationManager authenticationManager = null;
    private static AuthorizationManager authorizationManager = null;
    private Class requestingClass=null;
    private static final String CATISSUE_CORE_CONTEXT_NAME = "catissuecore";
    private static final String ADMINISTRATOR_ROLE="1";
    private static final String SUPERVISOR_ROLE="2";
    private static final String TECHNICIAN_ROLE="3";
    private static final String ADMINISTRATOR_GROUP="ADMINISTRATOR_GROUP";
    private static final String SUPERVISOR_GROUP="SUPERVISOR_GROUP";
    private static final String TECHNICIAN_GROUP="TECHNICIAN_GROUP";
    
    
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
	 * Returns the Authorization Manager for the caTISSUE Core.
	 * This method follows the singleton pattern so that
	 * only one AuthorizationManager is created.
	 * 
	 * @return
	 * @throws CSException
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
	 * @return
	 * @throws CSException
	 */
	protected UserProvisioningManager getUserProvisioningManager()
			throws CSException {
		return (UserProvisioningManager) getAuthorizationManager();
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
		try {
		    Logger.out.debug("login name: "+loginName+" passowrd: "+password);
		    AuthenticationManager authMngr=getAuthenticationManager();
			loginSuccess =authMngr.login(
					loginName, password);
		} catch (CSException ex) {
            Logger.out.debug("Authentication|"+requestingClass+"|"+loginName+"|login|Success| Authentication is not successful for user "+loginName+"|" + ex.getMessage());                                  
		    throw new SMException (ex.getMessage(), ex);    
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
	        Logger.out.debug("Unable to create user: Exception: "+e.getMessage());
	        throw new SMTransactionException (e.getMessage(), e);
	    }
	    catch (CSException e)
	    {
	        Logger.out.debug("Unable to create user: Exception: "+e);
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
	        Logger.out.debug("Unable to get user: Exception: "+e.getMessage());
	        throw new SMException (e.getMessage(), e);
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
	   boolean userExists=true;
	   try
	    {
	        if(getUser(loginName)==null)
	        {
	            userExists = false;
	        }
	    }
	    catch (SMException e)
	    {
	        Logger.out.debug("Unable to get user: Exception: "+e.getMessage());
	        throw e;
	    }
    return userExists;
	}
	
	/**
	 * This method returns Vactor of all the role objects defined for the application from the database
	 * @return
	 * @throws SMException
	 */
	public Vector getRoles()throws SMException
	{
	    Vector roles=new Vector();
	    UserProvisioningManager userProvisioningManager=null;
	    try
	    {
	        userProvisioningManager=getUserProvisioningManager();
	        roles.add(userProvisioningManager.getRoleById(ADMINISTRATOR_ROLE));
	        roles.add(userProvisioningManager.getRoleById(SUPERVISOR_ROLE));
	        roles.add(userProvisioningManager.getRoleById(TECHNICIAN_ROLE));
	    }
	    catch (CSException e)
	    {
	        Logger.out.debug("Unable to get roles: Exception: "+e.getMessage());
	        throw new SMException (e.getMessage(), e);
	    }
	    return roles;
	}
	
	/**
	 * Assigns a Role to a User
	 * @param userName - the User Name to to whom the Role will be assigned
	 * @param roleID - The id of the Role which is to be assigned to the user 
	 * @throws SMException
	 */
	public void assignRoleToUser(String userName, String roleID) throws SMException
	{
	    UserProvisioningManager userProvisioningManager=null;
	    try
	    {
	        userProvisioningManager=getUserProvisioningManager();
	        if(roleID.equals(ADMINISTRATOR_ROLE))
	        {
	            userProvisioningManager.assignUserToGroup(userName,ADMINISTRATOR_GROUP);
	        }
	        else  if(roleID.equals(SUPERVISOR_ROLE))
	        {
	            userProvisioningManager.assignUserToGroup(userName,SUPERVISOR_GROUP);
	        }
	        else if(roleID.equals(TECHNICIAN_ROLE))
	        {
	            userProvisioningManager.assignUserToGroup(userName,TECHNICIAN_GROUP);
	        }
	    }
	    catch (CSException e)
	    {
	        Logger.out.debug("UNABLE TO ASSIGN ROLE TO USER: Exception: "+e.getMessage());
	        throw new SMException (e.getMessage(), e);
	    }
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
	        Logger.out.debug("Unable to modify user: Exception: "+e.getMessage());
	        throw new SMException (e.getMessage(), e);
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
	    try
	    {
	        return getUserProvisioningManager().getUserById(userId);
	    }
	    catch (CSException e)
	    {
	        Logger.out.debug("Unable to get user by Id: Exception: "+e.getMessage());
	        throw new SMException (e.getMessage(), e);
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
	        Logger.out.debug("Unable to get users by emailAddress: Exception: "+e.getMessage());
	        throw new SMException (e.getMessage(), e);
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
	        Logger.out.debug("Unable to get all users: Exception: "+e.getMessage());
	        throw new SMException (e.getMessage(), e);
	    }
    }
}
