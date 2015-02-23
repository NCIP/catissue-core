
package com.krishagni.catissueplus.core.auth.events;

import java.util.Map;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;

public class DomainDetail {

	private Long id;

	private String name;

	private String implClass;

	private String authType;
	
	private Map<String, String> authProviderProps;

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

	public static DomainDetail fromDomain(AuthDomain authDomain) {
		DomainDetail detail = new DomainDetail();
		detail.setId(authDomain.getId());
		detail.setName(authDomain.getName());
		detail.setAuthType(authDomain.getAuthProvider().getAuthType());
		detail.setImplClass(detail.getImplClass());
		detail.setAuthProviderProps(authDomain.getAuthProvider().getProps());
		
		return detail;
	}
}
