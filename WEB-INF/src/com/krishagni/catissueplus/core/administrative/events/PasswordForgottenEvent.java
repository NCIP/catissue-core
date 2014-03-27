
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PasswordForgottenEvent extends ResponseEvent {
	
	private UserDetails userDetails;

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public static PasswordForgottenEvent ok(UserDetails userDetails) {
		PasswordForgottenEvent event = new PasswordForgottenEvent();
		event.setUserDetails(userDetails);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static PasswordForgottenEvent invalidRequest(String message) {
		PasswordForgottenEvent resp = new PasswordForgottenEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static PasswordForgottenEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PasswordForgottenEvent resp = new PasswordForgottenEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static PasswordForgottenEvent notAuthorized(UpdateParticipantEvent event1) {
		PasswordForgottenEvent event = new PasswordForgottenEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static PasswordForgottenEvent notFound(Long userId) {
		PasswordForgottenEvent resp = new PasswordForgottenEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
