
package com.krishagni.catissueplus.core.auth.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import javax.naming.NamingException;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapFactory;
import com.krishagni.catissueplus.core.auth.events.DomainDetails;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;
import com.krishagni.catissueplus.core.auth.services.impl.LdapAuthenticationManager;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class LdapFactoryImpl implements LdapFactory {

	private final String HOST = "host";

	private final String PORT = "port";

	private final String LOGIN_NAME = "login name";

	private final String PASSWORD = "password";

	private final String SEARCH_BASE_DIR = "search base dir";

	private final String FILTER_STRING = "filter string";

	private final String DIRECTORY_CONTEXT = "directory context";

	private final String ID_FIELD = "id field";

	private final String LDAP = "ldap";

	private final String LDAP_DETAILS = "ldap details";
	
	@Override
	public Ldap getLdap(DomainDetails details, AuthDomain authDomain) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		LdapDetails ldapDetails = details.getLdapDetails();
		if (ldapDetails == null) {
			reportError(AuthErrorCode.INVALID_ATTR_VALUE, LDAP_DETAILS);
		}
		
		Ldap ldap = new Ldap();
		setDirctoryContext(ldapDetails, ldap, exceptionHandler);
		setIdField(ldapDetails, ldap, exceptionHandler);
		setHost(ldapDetails, ldap, exceptionHandler);
		setPort(ldapDetails, ldap, exceptionHandler);
		setBindUser(ldapDetails, ldap, exceptionHandler);
		setBindPassword(ldapDetails, ldap, exceptionHandler);
		setFilterString(ldapDetails, ldap, exceptionHandler);
		setSearchBaseDir(ldapDetails, ldap, exceptionHandler);

		ldap.setGivenNameField(ldapDetails.getGivenNameField());
		ldap.setEmailField(ldapDetails.getEmailField());
		ldap.setSurnameField(ldapDetails.getSurnameField());
		ldap.setAuthDomain(authDomain);
		exceptionHandler.checkErrorAndThrow();
		checkLdapAuthentication(ldap);
		return ldap;
	}

	private void setSearchBaseDir(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getSearchBaseDir())) {
			exceptionHandler.addError(AuthErrorCode.INVALID_ATTR_VALUE, SEARCH_BASE_DIR);
			return;
		}
		ldap.setSearchBaseDir(ldapDetails.getSearchBaseDir());
	}

	private void setFilterString(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getFilterString())) {
			exceptionHandler.addError(AuthErrorCode.INVALID_ATTR_VALUE, FILTER_STRING);
			return;
		}
		ldap.setFilterString(ldapDetails.getFilterString());
	}

	private void setBindPassword(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getBindPassword())) {
			exceptionHandler.addError(AuthErrorCode.INVALID_ATTR_VALUE, PASSWORD);
			return;
		}
		ldap.setBindPassword(ldapDetails.getBindPassword());
	}

	private void setPort(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (ldapDetails.getPort() == null) {
			exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, PORT);
			return;
		}
		ldap.setPort(ldapDetails.getPort());
	}

	private void setIdField(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getIdField())) {
			exceptionHandler.addError(AuthErrorCode.INVALID_ATTR_VALUE, ID_FIELD);
			return;
		}
		ldap.setIdField(ldapDetails.getIdField());
	}

	private void setDirctoryContext(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getDirectoryContext())) {
			exceptionHandler.addError(AuthErrorCode.INVALID_ATTR_VALUE, DIRECTORY_CONTEXT);
			return;
		}
		ldap.setDirectoryContext(ldapDetails.getDirectoryContext());
	}

	private void setBindUser(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getBindUser())) {
			exceptionHandler.addError(AuthErrorCode.INVALID_ATTR_VALUE, LOGIN_NAME);
			return;
		}
		ldap.setBindUser(ldapDetails.getBindUser());
	}

	private void setHost(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getHost())) {
			exceptionHandler.addError(AuthErrorCode.INVALID_ATTR_VALUE, HOST);
			return;
		}
		ldap.setHost(ldapDetails.getHost());
	}

	private void checkLdapAuthentication(Ldap ldap) {
		try {
			if (ldap != null) {
				LdapAuthenticationManager.authenticateLdap(ldap);
			}
		}
		catch (NamingException e) {
			reportError(AuthErrorCode.INVALID_ATTR_VALUE, LDAP);
		}
	}
}
