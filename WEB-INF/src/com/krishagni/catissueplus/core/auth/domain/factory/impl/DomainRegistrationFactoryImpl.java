
package com.krishagni.catissueplus.core.auth.domain.factory.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthenticationType;
import com.krishagni.catissueplus.core.auth.domain.factory.DomainRegistrationFactory;
import com.krishagni.catissueplus.core.auth.domain.factory.LdapFactory;
import com.krishagni.catissueplus.core.auth.events.DomainDetail;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class DomainRegistrationFactoryImpl implements DomainRegistrationFactory {

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
	public AuthDomain getAuthDomain(DomainDetail domainDetails) {
		AuthDomain authDomain = new AuthDomain();
		setDomainName(domainDetails.getName(), authDomain);
		validateAuthType(domainDetails.getAuthType());
		
		AuthProvider authProvider = daoFactory.getDomainDao().getAuthProviderByType(domainDetails.getAuthType());
		if (domainDetails.getAuthType().equals(AuthenticationType.LDAP.value())) {
			if (authProvider == null) {
				throw OpenSpecimenException.userError(AuthProviderErrorCode.LDAP_NOT_FOUND);
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
			throw OpenSpecimenException.userError(AuthProviderErrorCode.INVALID_TYPE);
		}
	}

	private AuthProvider getNewAuthProvider(DomainDetail domainDetails) {
		AuthProvider authProvider = new AuthProvider();
		authProvider.setAuthType(domainDetails.getName());
		authProvider.setImplClass(domainDetails.getImplClass());
		return authProvider;
	}

	private void setDomainName(String domainName, AuthDomain authDomain) {
		if (StringUtils.isBlank(domainName)) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.DOMAIN_NOT_SPECIFIED);
		}
		
		authDomain.setName(domainName);
	}

	private void validateImplClass(String implClass) {
		if (StringUtils.isBlank(implClass)) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.IMPL_NOT_SPECIFIED);
		}
		
		isValidImplClass(implClass);
	}

	@SuppressWarnings("rawtypes")
	private AuthenticationService isValidImplClass(String implClass) {
		try {
			Class authImplClass = (Class) Class.forName(implClass);
			return (AuthenticationService) authImplClass.newInstance();
		} catch (Exception e) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.IMPL_LOADING_FAILED);
		}
	}
}
