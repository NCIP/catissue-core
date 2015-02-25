
package com.krishagni.catissueplus.core.auth.domain;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class AuthDomain {
	
	public static Map<String, Object> authImplMap = new HashMap<String, Object>();

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
		return getAuthProviderInstance(this.getAuthProvider().getImplClass());
	}

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
			throw OpenSpecimenException.userError(AuthProviderErrorCode.INVALID_AUTH_IMPL);
		}
	}
}
