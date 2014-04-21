
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AddLdapEvent extends RequestEvent {

	private LdapDetails ldapDetails;

	public AddLdapEvent(LdapDetails ldapDetails) {
		this.ldapDetails = ldapDetails;
	}

	public LdapDetails getLdapDetails() {
		return ldapDetails;
	}

	public void setLdapDetails(LdapDetails ldapDetails) {
		this.ldapDetails = ldapDetails;
	}

}
