
package com.krishagni.catissueplus.core.auth.domain;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.auth.services.AuthenticationService;

public class AuthDomain {

	private Long id;

	private String name;

	private AuthProvider authProvider;

	private Ldap ldap;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	public AuthProvider getAuthProvider() {
		return authProvider;
	}

	public Ldap getLdap() {
		return ldap;
	}

	public void setLdap(Ldap ldap) {
		this.ldap = ldap;
	}

	public AuthenticationService getAuthProviderInstance() {
		return getAuthProviderInstance(this.getAuthProvider().getImplClass());
	}

	public static Map<String, Object> authImplMap = new HashMap<String, Object>();

	@SuppressWarnings("rawtypes")
	private AuthenticationService getAuthProviderInstance(String implClassName) {
		try {
			Class authImplClass = (Class) authImplMap.get(implClassName);
			if (authImplClass == null) {
				authImplClass = (Class) Class.forName(implClassName);
				authImplMap.put(implClassName, authImplClass);
			}
			return (AuthenticationService) authImplClass.newInstance();
		}
		catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
