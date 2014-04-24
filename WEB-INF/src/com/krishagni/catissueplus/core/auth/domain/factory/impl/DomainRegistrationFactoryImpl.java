
package com.krishagni.catissueplus.core.auth.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthenticationType;
import com.krishagni.catissueplus.core.auth.domain.factory.DomainRegistrationFactory;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapFactory;
import com.krishagni.catissueplus.core.auth.events.DomainDetails;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class DomainRegistrationFactoryImpl implements DomainRegistrationFactory {

	private static final String AUTH_PROVIDER = "auth provider";

	private static final String AUTH_TYPE = "auth type";

	private final String IMPL_CLASS = "impl class";

	private final String DOMAIN_NAME = "domain name";

	@Autowired
	private LdapFactory ldapFactory;

	public void setLdapFactory(LdapFactory ldapFactory) {
		this.ldapFactory = ldapFactory;
	}

	@Autowired
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public AuthDomain getAuthDomain(DomainDetails domainDetails) {
		AuthDomain authDomain = new AuthDomain();
		setDomainName(domainDetails.getName(), authDomain);
		validateAuthType(domainDetails.getAuthType());
		
		AuthProvider authProvider = daoFactory.getDomainDao().getAuthProviderByType(domainDetails.getAuthType());
		if (domainDetails.getAuthType().equals(AuthenticationType.LDAP.value())) {
			if (authProvider == null) {
				reportError(AuthErrorCode.INVALID_ATTR_VALUE, AUTH_PROVIDER);
			}
			authDomain.setLdap(ldapFactory.getLdap(domainDetails, authDomain));
		}
		else if (domainDetails.getAuthType().equals(AuthenticationType.CUSTOM.value())) {
			validateImplClass(domainDetails.getImplClass());
			if (authProvider == null) {
				authProvider = getNewAuthProvider(domainDetails);
			}
		}
		authDomain.setAuthProvider(authProvider);
		return authDomain;
	}

	private void validateAuthType(String authType) {
		if(!AuthenticationType.isValidAuthType(authType)){
			reportError(AuthErrorCode.INVALID_ATTR_VALUE, AUTH_TYPE);
		}
	}

	private AuthProvider getNewAuthProvider(DomainDetails domainDetails) {
		AuthProvider authProvider = new AuthProvider();
		authProvider.setAuthType(domainDetails.getName());
		authProvider.setImplClass(domainDetails.getImplClass());
		return authProvider;
	}

	private void setDomainName(String domainName, AuthDomain authDomain) {
		if (isBlank(domainName)) {
			reportError(AuthErrorCode.INVALID_ATTR_VALUE, DOMAIN_NAME);
		}
		authDomain.setName(domainName);
	}

	private void validateImplClass(String implClass) {
		if (isBlank(implClass)) {
			reportError(AuthErrorCode.INVALID_ATTR_VALUE, IMPL_CLASS);
		}
		isValidImplClass(implClass);
	}

	@SuppressWarnings("rawtypes")
	private AuthenticationService isValidImplClass(String implClass) {
		try {
			Class authImplClass = (Class) Class.forName(implClass);
			return (AuthenticationService) authImplClass.newInstance();
		}
		catch (Exception e) {
			reportError(AuthErrorCode.INVALID_ATTR_VALUE, IMPL_CLASS);
		}
		return null;
	}
}
