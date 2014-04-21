
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class UserAuthenticatedEvent extends ResponseEvent {

	private UserDetails userDetails;

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public static UserAuthenticatedEvent ok(UserDetails details) {
		UserAuthenticatedEvent event = new UserAuthenticatedEvent();
		event.setUserDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static UserAuthenticatedEvent notAuthenticated(String message) {
		UserAuthenticatedEvent event = new UserAuthenticatedEvent();
		event.setMessage(message);
		event.setStatus(EventStatus.NOT_AUTHENTICATED);
		return event;
	}

	public static UserAuthenticatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		UserAuthenticatedEvent resp = new UserAuthenticatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static UserAuthenticatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		UserAuthenticatedEvent resp = new UserAuthenticatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
