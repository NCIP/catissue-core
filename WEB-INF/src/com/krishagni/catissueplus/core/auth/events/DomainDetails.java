
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;

public class DomainDetails {

	private Long id;

	private String name;

	private String implClass;

	private String authType;

	private LdapDetails ldapDetails;

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

	public LdapDetails getLdapDetails() {
		return ldapDetails;
	}

	public void setLdapDetails(LdapDetails ldapDetails) {
		this.ldapDetails = ldapDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static DomainDetails fromDomain(AuthDomain authDomain) {
		DomainDetails details = new DomainDetails();
		details.setId(authDomain.getId());
		details.setName(authDomain.getName());
		details.setAuthType(authDomain.getAuthProvider().getAuthType());
		details.setImplClass(details.getImplClass());
		if (authDomain.getLdap() != null) {
			details.setLdapDetails(LdapDetails.fromDomain(authDomain.getLdap()));
		}
		return details;
	}
}
