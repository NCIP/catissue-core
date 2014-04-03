
package com.krishagni.catissueplus.core.auth.services.impl;

import javax.naming.NamingException;

import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapRegistrationErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapRegistrationFactory;
import com.krishagni.catissueplus.core.auth.events.AddLdapEvent;
import com.krishagni.catissueplus.core.auth.events.LdapAddedEvent;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;
import com.krishagni.catissueplus.core.auth.services.LdapRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class LdapRegistrationServiceImpl implements LdapRegistrationService {

	private DaoFactory daoFactory;

	private LdapRegistrationFactory ldapRegFactory;

	private final String LDAP_NAME = "ldap name";

	private final String LDAP = "ldap";

	public void setLdapRegFactory(LdapRegistrationFactory ldapRegFactory) {
		this.ldapRegFactory = ldapRegFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public LdapAddedEvent addLdap(AddLdapEvent event) {
		try {
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			Ldap ldap = ldapRegFactory.getLdap(event.getLdapDetails());
			ensureUniqueLdapName(ldap, exceptionHandler);
			checkLdapAuthentication(ldap, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getLdapDao().saveOrUpdate(ldap);
			return LdapAddedEvent.ok(LdapDetails.fromDomain(ldap));
		}
		catch (ObjectCreationException ce) {
			return LdapAddedEvent.invalidRequest(LdapRegistrationErrorCode.ERROR_WHILE_LDAP_CONFIGURATION.message(),
					ce.getErroneousFields());
		}
		catch (Exception e) {
			return LdapAddedEvent.serverError(e);
		}
	}

	private void checkLdapAuthentication(Ldap ldap, ObjectCreationException exceptionHandler) {
		try {
			LdapAuthenticationManager.authenticateLdap(ldap);
		}
		catch (NamingException e) {
			exceptionHandler.addError(LdapRegistrationErrorCode.AUTHENTICATION_FAILURE, LDAP);
		}
	}

	private void ensureUniqueLdapName(Ldap ldap, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getLdapDao().isUniqueLdapName(ldap.getLdapName())) {
			exceptionHandler.addError(LdapRegistrationErrorCode.DUPLICATE_LDAP_NAME, LDAP_NAME);
		}
	}
}
