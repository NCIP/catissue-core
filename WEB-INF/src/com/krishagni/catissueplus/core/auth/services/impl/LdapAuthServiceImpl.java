
package com.krishagni.catissueplus.core.auth.services.impl;

import javax.naming.NamingException;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class LdapAuthServiceImpl extends AbstractAuthProvider {
	public void authenticate(LoginDetail loginDetail) {
		AuthDomain authDomain = super.getDaoFactory().getDomainDao().getAuthDomainByName(loginDetail.getDomainName());

		try {
			LdapAuthenticationManager.authenticate(loginDetail, authDomain.getLdap());
		} catch (NamingException e) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}

}
