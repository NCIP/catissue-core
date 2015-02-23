
package com.krishagni.catissueplus.core.auth.domain.factory.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.domain.factory.DomainRegistrationFactory;
import com.krishagni.catissueplus.core.auth.events.DomainDetail;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class DomainRegistrationFactoryImpl implements DomainRegistrationFactory {

	@Autowired
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public AuthDomain getAuthDomain(DomainDetail detail) {
		AuthDomain authDomain = new AuthDomain();
		setDomainName(detail, authDomain);
		setAuthProvider(detail, authDomain);
		return authDomain;
	}
	
	private void setDomainName(DomainDetail detail, AuthDomain authDomain) {
		String domainName = detail.getName();
		if (StringUtils.isBlank(domainName)) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.DOMAIN_NOT_SPECIFIED);
		}
		
		authDomain.setName(domainName);
	}
	
	private void setAuthProvider(DomainDetail detail, AuthDomain authDomain) {
		AuthProvider authProvider = daoFactory.getAuthDao().getAuthProviderByType(detail.getAuthType());
		if (authProvider == null) {
			authProvider = getNewAuthProvider(detail);
		}
		
		authDomain.setAuthProvider(authProvider);
	}

	private AuthProvider getNewAuthProvider(DomainDetail detail) {
		String implClass = detail.getImplClass();
		if (StringUtils.isBlank(implClass)) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.IMPL_NOT_SPECIFIED);
		}
		isValidImplClass(implClass);
		
		AuthProvider authProvider = new AuthProvider();
		authProvider.setAuthType(detail.getAuthType());
		authProvider.setImplClass(implClass);
		authProvider.setProps(detail.getAuthProviderProps());
		
		return authProvider;
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
