
package com.krishagni.catissueplus.core.auth.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapRegistrationErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapRegistrationFactory;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class LdapRegistrationFactoryImpl implements LdapRegistrationFactory {

	private DaoFactory daoFactory;

	private final String HOST = "host";

	private final String PORT = "port";

	private final String LOGIN_NAME = "login name";

	private final String PASSWORD = "password";

	private final String DIRECTORY_CONTEXT = "directory context";

	private final String ID_FIELD = "id field";

	private final String LDAP_NAME = "ldap name";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Ldap getLdap(LdapDetails ldapDetails) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		Ldap ldap = new Ldap();
		setLdapName(ldapDetails, ldap, exceptionHandler);
		setDirctoryContext(ldapDetails, ldap, exceptionHandler);
		setIdField(ldapDetails, ldap, exceptionHandler);
		setHost(ldapDetails, ldap, exceptionHandler);
		setPort(ldapDetails, ldap, exceptionHandler);
		setLoginName(ldapDetails, ldap, exceptionHandler);
		setPassword(ldapDetails, ldap, exceptionHandler);;

		ldap.setEmailField(ldapDetails.getEmailField());
		ldap.setGivenNameField(ldapDetails.getGivenNameField());
		ldap.setSurnameField(ldapDetails.getSurnameField());
		exceptionHandler.checkErrorAndThrow();

		return ldap;
	}

	private void setPassword(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getPassword())) {
			exceptionHandler.addError(LdapRegistrationErrorCode.MISSING_ATTR_VALUE, PASSWORD);
			return;
		}
		ldap.setPassword(ldapDetails.getPassword());
	}

	private void setPort(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (ldapDetails.getPort() == null) {
			exceptionHandler.addError(UserErrorCode.MISSING_ATTR_VALUE, PORT);
			return;
		}
		ldap.setPort(ldapDetails.getPort());
	}

	private void setIdField(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getIdField())) {
			exceptionHandler.addError(LdapRegistrationErrorCode.MISSING_ATTR_VALUE, ID_FIELD);
			return;
		}
		ldap.setIdField(ldapDetails.getIdField());

	}

	private void setLdapName(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getLdapName())) {
			exceptionHandler.addError(LdapRegistrationErrorCode.MISSING_ATTR_VALUE, LDAP_NAME);
			return;
		}
		ldap.setLdapName(ldapDetails.getLdapName());
	}

	private void setDirctoryContext(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getDirectoryContext())) {
			exceptionHandler.addError(LdapRegistrationErrorCode.MISSING_ATTR_VALUE, DIRECTORY_CONTEXT);
			return;
		}
		ldap.setDirectoryContext(ldapDetails.getDirectoryContext());

	}

	private void setLoginName(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getLoginName())) {
			exceptionHandler.addError(LdapRegistrationErrorCode.MISSING_ATTR_VALUE, LOGIN_NAME);
			return;
		}
		ldap.setLoginName(ldapDetails.getLoginName());

	}

	private void setHost(LdapDetails ldapDetails, Ldap ldap, ObjectCreationException exceptionHandler) {
		if (isBlank(ldapDetails.getHost())) {
			exceptionHandler.addError(LdapRegistrationErrorCode.MISSING_ATTR_VALUE, HOST);
			return;
		}
		ldap.setHost(ldapDetails.getHost());

	}
}
