package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

@Configurable
public class LdapAuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private DaoFactory daoFactory;

	private LdapAuthenticationProvider provider;
	
	@Override
	public void authenticate(String username, String password, String domainName) {
		try {
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		
		getProvider(domainName).authenticate(authenticationToken);
		} catch (AuthenticationException e) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}
	
	private LdapAuthenticationProvider getProvider(String domainName) {
		AuthDomain domain = daoFactory.getAuthDao().getAuthDomainByName(domainName);
		Map<String, String> props = domain.getAuthProvider().getProps();
		
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(props.get("url"));
		contextSource.setUserDn(props.get("userDn"));
		contextSource.setPassword(props.get("password"));
		
		contextSource.afterPropertiesSet();
		
		BindAuthenticator authenticator = new BindAuthenticator(contextSource);
		String[] patterns = props.get("userDnPatterns").split(";");
		authenticator.setUserDnPatterns(patterns);
		
		provider = new LdapAuthenticationProvider(authenticator);
		
		return provider;
	}

}
