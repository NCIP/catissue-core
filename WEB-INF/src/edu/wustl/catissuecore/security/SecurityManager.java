/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */ 
package edu.wustl.catissuecore.security;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.idp.IdPManager;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.ProvisionManager;
import edu.wustl.security.global.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSTransactionException;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;



public class SecurityManager extends  edu.wustl.security.manager.SecurityManager
{
	
	public void createUser(User user) throws SMException {
        try {
			ProvisionManager.getInstance().getUserProvisioningManager().createUser(user);
            if(isIdpEnabled())
            {
                IdPManager idp = IdPManager.getInstance();
                idp.addUserToQueue(SecurityManagerPropertiesLocator.getInstance()
        				.getApplicationCtxName(), user);
       
            }
        } catch (CSTransactionException exception)
		{
			String mesg = "Unable to create user " + user.getEmailId();
			Utility.getInstance().throwSMException(exception, mesg, "sm.operation.error");
		}
    }
    
    private boolean isIdpEnabled()
    {
        String idpEnabled = XMLPropertyHandler.getValue(Constants.IDP_ENABLED);
        if (Constants.TRUE.equalsIgnoreCase(idpEnabled))
            return true;
        else
            return false;
    }
    
    public void modifyUser(User user) throws SMException {
        try {
			ProvisionManager.getInstance().getUserProvisioningManager().modifyUser(user);
            if(isIdpEnabled())
            {
                IdPManager idp = IdPManager.getInstance();
                idp.addUserToQueue(CommonServiceLocator.getInstance().getAppName(), user);
            }
        } catch (CSException exception)
		{
			String mesg = "Unable to modify user: Exception:  ";
			Utility.getInstance().throwSMException(exception, mesg, "sm.operation.error");
		}
    }

}
