package edu.wustl.catissuecore.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.util.CommonLoginInfoUtility;
import edu.wustl.catissuecore.util.SSOcaTissueCommonLoginUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.sso.SSOAuthentication;
import edu.wustl.common.sso.SSOInformationObject;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.domain.LoginCredentials;

public abstract class AbstractSSOAuthentication implements SSOAuthentication{
	
	private static final Logger LOGGER = Logger.getCommonLogger(AbstractSSOAuthentication.class);
	
	@Override
	public SSOInformationObject authenticate(SSOInformationObject ssoInformationObject)
			throws ApplicationException
	{
        CommonLoginInfoUtility infoUtility = null;
        if (ssoInformationObject == null)
        {
        	AbstractSSOAuthentication.LOGGER.debug("SSO Information Object is Null");
        }
        else
        {
            try
            {
                AbstractSSOAuthentication.LOGGER.info("Inside AbstractSSOAuthentication, Just before authorization");                
                ssoInformationObject = getLoginName(ssoInformationObject);
                SSOcaTissueCommonLoginUtility loginUtility = new SSOcaTissueCommonLoginUtility();
            	LoginCredentials loginCredentials = new LoginCredentials();
                loginCredentials.setLoginName(ssoInformationObject.getLoginName());
            	infoUtility = loginUtility.processUserAuthorization(loginCredentials, ssoInformationObject.getLoginName(),
            			ssoInformationObject.getRequest());
            	if(infoUtility.getActionErrors() != null)
            	{
            		ssoInformationObject.setActionErrors(infoUtility.getActionErrors());
            	}
            	if(infoUtility.getActionMessages() != null)
            	{
            		ssoInformationObject.setActionMessages(infoUtility.getActionMessages());
            	}
            }
            catch (final Exception ex)
            {
            	AbstractSSOAuthentication.LOGGER.error("Exception: " + ex.getMessage(), ex);
                cleanSession(ssoInformationObject.getRequest());
                ssoInformationObject.setActionErrors(handleError(
                		ssoInformationObject.getRequest(), "errors.incorrectLoginIDPassword"));
            }
        }
        ssoInformationObject.setForwardTo(infoUtility.getForwardTo());
        return ssoInformationObject;
    }

	/**
	 *
	 * @param request
	 */
	private void cleanSession(final HttpServletRequest request)
	{
		final HttpSession prevSession = request.getSession();
		if (prevSession != null)
		{
			prevSession.invalidate();
		}
	}

	/**
	 *
	 * @param request
	 * @param errorKey
	 */
	private ActionErrors handleError(final HttpServletRequest request, final String errorKey)
	{
		final ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey));
		return errors;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract SSOInformationObject getLoginName(SSOInformationObject informationObject);
}
