
package com.krishagni.catissueplus.core.auth.domain.factory.impl;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapFactory;
import com.krishagni.catissueplus.core.auth.events.DomainDetail;
import com.krishagni.catissueplus.core.auth.events.LdapDetail;
import com.krishagni.catissueplus.core.auth.services.impl.LdapAuthenticationManager;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class LdapFactoryImpl implements LdapFactory {
	
	@Override
	public Ldap getLdap(DomainDetail details, AuthDomain authDomain) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		LdapDetail ldapDetails = details.getLdapDetails();
		
		if (ldapDetails == null) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.LDAP_CONFIG_NOT_SPECIFIED);
		}
		
		Ldap ldap = new Ldap();
		setDirctoryContext(ldapDetails, ldap, ose);
		setIdField(ldapDetails, ldap, ose);
		setHost(ldapDetails, ldap, ose);
		setPort(ldapDetails, ldap, ose);
		setBindUser(ldapDetails, ldap, ose);
		setBindPassword(ldapDetails, ldap, ose);
		setFilterString(ldapDetails, ldap, ose);
		setSearchBaseDir(ldapDetails, ldap, ose);

		ldap.setGivenNameField(ldapDetails.getGivenNameField());
		ldap.setEmailField(ldapDetails.getEmailField());
		ldap.setSurnameField(ldapDetails.getSurnameField());
		ldap.setAuthDomain(authDomain);
		
		ose.checkAndThrow();
		checkLdapAuthentication(ldap);
		return ldap;
	}

	private void setSearchBaseDir(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (StringUtils.isBlank(ldapDetails.getSearchBaseDir())) {
			ose.addError(AuthProviderErrorCode.LDAP_BASE_DIR_NOT_SPECIFIED);
			return;
		}
		
		ldap.setSearchBaseDir(ldapDetails.getSearchBaseDir());
	}

	private void setFilterString(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (StringUtils.isBlank(ldapDetails.getFilterString())) {
			ose.addError(AuthProviderErrorCode.LDAP_FILTER_NOT_SPECIFIED);
			return;
		}
		
		ldap.setFilterString(ldapDetails.getFilterString());
	}

	private void setBindPassword(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (StringUtils.isBlank(ldapDetails.getBindPassword())) {
			ose.addError(AuthProviderErrorCode.LDAP_BIND_PASSWORD_NOT_SPECIFIED);
			return;
		}
		ldap.setBindPassword(ldapDetails.getBindPassword());
	}

	private void setPort(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (ldapDetails.getPort() == null) {
			ose.addError(AuthProviderErrorCode.LDAP_PORT_NOT_SPECIFIED);
			return;
		}
		ldap.setPort(ldapDetails.getPort());
	}

	private void setIdField(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (StringUtils.isBlank(ldapDetails.getIdField())) {
			ose.addError(AuthProviderErrorCode.LDAP_ID_NOT_SPECIFIED);
			return;
		}
		
		ldap.setIdField(ldapDetails.getIdField());
	}

	private void setDirctoryContext(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (StringUtils.isBlank(ldapDetails.getDirectoryContext())) {
			ose.addError(AuthProviderErrorCode.LDAP_DIR_CTXT_NOT_SPECIFIED);
			return;
		}
		
		ldap.setDirectoryContext(ldapDetails.getDirectoryContext());
	}

	private void setBindUser(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (StringUtils.isBlank(ldapDetails.getBindUser())) {
			ose.addError(AuthProviderErrorCode.LDAP_BIND_USER_NOT_SPECIFIED);
			return;
		}
		
		ldap.setBindUser(ldapDetails.getBindUser());
	}

	private void setHost(LdapDetail ldapDetails, Ldap ldap, OpenSpecimenException ose) {
		if (StringUtils.isBlank(ldapDetails.getHost())) {
			ose.addError(AuthProviderErrorCode.LDAP_HOST_NOT_SPECIFIED);
			return;
		}
		
		ldap.setHost(ldapDetails.getHost());
	}

	private void checkLdapAuthentication(Ldap ldap) {
		try {
			if (ldap != null) {
				LdapAuthenticationManager.authenticateLdap(ldap);
			}
		} catch (NamingException e) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.LDAP_INVALID_CONFIG);
		}
	}
}
