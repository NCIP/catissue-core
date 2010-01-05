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
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.ProvisionManager;
import edu.wustl.security.global.Utility;
import edu.wustl.security.locator.SecurityManagerPropertiesLocator;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSTransactionException;

public class SecurityManager extends edu.wustl.security.manager.SecurityManager
{
	/**
	 * Logger instance.
	 */
	private final transient Logger logger = Logger.getCommonLogger(SecurityManager.class);
	@Override
	public void createUser(User user) throws SMException
	{
		try
		{
			ProvisionManager.getInstance().getUserProvisioningManager().createUser(user);
			if (this.isIdpEnabled())
			{
				final IdPManager idp = IdPManager.getInstance();
				idp.addUserToQueue(SecurityManagerPropertiesLocator.getInstance()
						.getApplicationCtxName(), user);

			}
		}
		catch (final CSTransactionException exception)
		{
			this.logger.error(exception.getMessage(),exception) ;
			exception.printStackTrace() ;
			final String mesg = "Unable to create user " + user.getEmailId();
			Utility.getInstance().throwSMException(exception, mesg, "sm.operation.error");
		}
	}

	private boolean isIdpEnabled()
	{
		final String idpEnabled = XMLPropertyHandler.getValue(Constants.IDP_ENABLED);
		if (Constants.TRUE.equalsIgnoreCase(idpEnabled))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void modifyUser(User user) throws SMException
	{
		try
		{
			ProvisionManager.getInstance().getUserProvisioningManager().modifyUser(user);
			if (this.isIdpEnabled())
			{
				final IdPManager idp = IdPManager.getInstance();
				//idp.addUserToQueue(CommonServiceLocator.getInstance().getAppName(), user);
				idp.addUserToQueue(SecurityManagerPropertiesLocator.getInstance()
						.getApplicationCtxName(), user);			}
		}
		catch (final CSException exception)
		{
			this.logger.error(exception.getMessage(),exception) ;
			exception.printStackTrace() ;
			final String mesg = "Unable to modify user: Exception:  ";
			Utility.getInstance().throwSMException(exception, mesg, "sm.operation.error");
		}
	}

}
