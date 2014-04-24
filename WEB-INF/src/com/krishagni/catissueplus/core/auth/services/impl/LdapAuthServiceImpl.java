
package com.krishagni.catissueplus.core.auth.services.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import javax.naming.NamingException;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;

public class LdapAuthServiceImpl extends AbstractAuthProvider {

	private static final String LDAP = "ldap";

	public void authenticate(LoginDetails loginDetails) {
		AuthDomain authDomain = super.getDaoFactory().getDomainDao().getAuthDomainByName(loginDetails.getDomainName());

		try {
			LdapAuthenticationManager.authenticate(loginDetails, authDomain.getLdap());
		}
		catch (NamingException e) {
			reportError(UserErrorCode.AUTH_FAILED, LDAP);
		}
	}

}
