/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.common.security;


import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.User;
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
	
	

   
	
}
