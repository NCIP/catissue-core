
package com.krishagni.catissueplus.core.auth.domain;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class AuthDomain {
	private static Map<String, AuthenticationService> authProviderMap = new HashMap<String, AuthenticationService>();

	private Long id;

	private String name;

	private AuthProvider authProvider;

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

	public AuthenticationService getAuthProviderInstance() {
		return getAuthProviderInstance(this.getAuthProvider());
	}

	@SuppressWarnings("rawtypes")
	private AuthenticationService getAuthProviderInstance(AuthProvider authProvider) {
		try {
			AuthenticationService authService = authProviderMap.get(authProvider.getAuthType());
			if (authService == null) {
				Class authImplClass = (Class) Class.forName(authProvider.getImplClass());
				authService = ((AuthenticationService) authImplClass.newInstance()).init(authProvider.getProps());
				authProviderMap.put(authProvider.getAuthType(), authService);
			}
			
			return authService;
		}
		catch (final Exception e) {
			throw OpenSpecimenException.userError(AuthProviderErrorCode.INVALID_AUTH_IMPL);
		}
	}
}
