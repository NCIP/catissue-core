
package com.krishagni.catissueplus.core.auth.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.krishagni.catissueplus.core.auth.domain.factory.AuthProviderErrorCode;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class AuthDomain {
	private static final Log logger = LogFactory.getLog(AuthDomain.class);

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
	
	public void update(AuthDomain domain) {
		Map<String, String> newProps = domain.getAuthProvider().getProps();
		Map<String, String> oldProps = this.authProvider.getProps();
		List<String> oldNames = new ArrayList<String>(oldProps.keySet());
		
		for (Map.Entry<String, String> entry: newProps.entrySet()) {
			oldNames.remove(entry.getKey());
			oldProps.put(entry.getKey(), entry.getValue());
		}
		
		for (String name: oldNames) {
			oldProps.remove(name);
		}

		//
		// Removing updated domain's auth provider implementation instance from cached
		// instances so that new instance with new properties can be created
		//
		authProviderMap.remove(domain.getAuthProvider().getAuthType());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AuthenticationService getAuthProviderInstance(AuthProvider authProvider) {
		try {
			AuthenticationService authService = authProviderMap.get(authProvider.getAuthType());
			if (authService == null) {
				Class authImplClass = (Class) Class.forName(authProvider.getImplClass());
				authService = (AuthenticationService) authImplClass
							.getConstructor(Map.class)
							.newInstance(authProvider.getProps());
				authProviderMap.put(authProvider.getAuthType(), authService);
			}
			
			return authService;
		} catch (Exception e) {
			logger.error("Error obtaining an instance of auth provider", e);
			throw OpenSpecimenException.userError(AuthProviderErrorCode.INVALID_AUTH_IMPL);
		}
	}
}
