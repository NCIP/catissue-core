
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class LdapAddedEvent extends ResponseEvent {

	private LdapDetails ldapDetails;

	public LdapDetails getLdapDetails() {
		return ldapDetails;
	}

	public void setLdapDetails(LdapDetails ldapDetails) {
		this.ldapDetails = ldapDetails;
	}

	public static LdapAddedEvent ok(LdapDetails details) {
		LdapAddedEvent event = new LdapAddedEvent();
		event.setLdapDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static LdapAddedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		LdapAddedEvent resp = new LdapAddedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static LdapAddedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		LdapAddedEvent resp = new LdapAddedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
