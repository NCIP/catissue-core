package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class LdapAuthenticationServiceImpl implements AuthenticationService {
	
	private LdapAuthenticationProvider provider;
	
	public LdapAuthenticationServiceImpl() {
		
	}
	
	public LdapAuthenticationServiceImpl(Map<String, String> props) {
		provider = getProvider(props);
	}
	
	@Override
	public void authenticate(String username, String password) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		
			provider.authenticate(authenticationToken);
		} catch (AuthenticationException e) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}	
	
	private LdapAuthenticationProvider getProvider(Map<String, String> props) {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(props.get("url"));
		contextSource.setUserDn(props.get("userDn"));
		contextSource.setPassword(props.get("password"));
		
		contextSource.afterPropertiesSet();
		
		BindAuthenticator authenticator = new BindAuthenticator(contextSource);
		
		if (props.get("userDnPatterns") != null) {
			String[] patterns = props.get("userDnPatterns").split(";");
			authenticator.setUserDnPatterns(patterns);
		}
		
		String userSearchFilter = props.get("userSearchFilter");
		String userSearchBase = props.get("userSearchBase");
		if (userSearchFilter != null && userSearchBase != null) {
			FilterBasedLdapUserSearch   userSearch = new FilterBasedLdapUserSearch(userSearchBase, userSearchFilter, contextSource);
			authenticator.setUserSearch(userSearch);
		}
		
		return new LdapAuthenticationProvider(authenticator);
	}

}
