package edu.wustl.catissuecore.sso;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.sso.SSOInformationObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.yale.its.tp.cas.client.filter.CASFilter;

public class CASSSOAuthentication extends AbstractSSOAuthentication{
	
	@Override
	public SSOInformationObject getLoginName(SSOInformationObject ssoInformationObject) {
		if (ssoInformationObject.getRequest() != null &&
				ssoInformationObject.getRequest().getSession() != null)
		{
			ssoInformationObject.setLoginName((String)ssoInformationObject.getRequest().
					getSession().getAttribute(CASFilter.CAS_FILTER_USER));
		}
		if (Validator.isEmpty(ssoInformationObject.getLoginName()))
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("user.emailAddress")));
			ssoInformationObject.setActionErrors(actionErrors);			
		}
		return ssoInformationObject;
	}
}
