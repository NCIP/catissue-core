
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;

public class DomainDetail {

	private Long id;

	private String name;

	private String implClass;

	private String authType;

	private LdapDetail ldapDetails;

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

	public LdapDetail getLdapDetails() {
		return ldapDetails;
	}

	public void setLdapDetails(LdapDetail ldapDetails) {
		this.ldapDetails = ldapDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static DomainDetail fromDomain(AuthDomain authDomain) {
		DomainDetail details = new DomainDetail();
		details.setId(authDomain.getId());
		details.setName(authDomain.getName());
		details.setAuthType(authDomain.getAuthProvider().getAuthType());
		details.setImplClass(details.getImplClass());
		if (authDomain.getLdap() != null) {
			details.setLdapDetails(LdapDetail.fromDomain(authDomain.getLdap()));
		}
		return details;
	}
}
