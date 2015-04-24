	
package com.krishagni.catissueplus.core.auth.events;

import java.util.Map;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;

public class AuthDomainDetail extends AuthDomainSummary {

	private String implClass;

	private String authType;
	
	private Map<String, String> authProviderProps;

	public String getImplClass() {
		return implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public Map<String, String> getAuthProviderProps() {
		return authProviderProps;
	}

	public void setAuthProviderProps(Map<String, String> authProviderProps) {
		this.authProviderProps = authProviderProps;
	}

	public static AuthDomainDetail from(AuthDomain authDomain) {
		AuthDomainDetail detail = new AuthDomainDetail();
		detail.setId(authDomain.getId());
		detail.setName(authDomain.getName());
		detail.setAuthType(authDomain.getAuthProvider().getAuthType());
		detail.setImplClass(authDomain.getAuthProvider().getImplClass());
		detail.setAuthProviderProps(authDomain.getAuthProvider().getProps());
		
		return detail;
	}
	
}
