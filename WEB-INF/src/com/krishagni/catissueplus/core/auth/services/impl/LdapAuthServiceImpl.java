
package com.krishagni.catissueplus.core.auth.services.impl;

import javax.naming.NamingException;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthenticationType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class LdapAuthServiceImpl extends AbstractAuthProvider {
	public void authenticate(String username, String password) {
		AuthDomain authDomain = getDaoFactory().getDomainDao()
				.getAuthDomainByName(AuthenticationType.LDAP.name().toLowerCase());

		try {
			LdapAuthenticationManager.authenticate(username, password, authDomain.getLdap());
		} catch (NamingException e) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}

}
