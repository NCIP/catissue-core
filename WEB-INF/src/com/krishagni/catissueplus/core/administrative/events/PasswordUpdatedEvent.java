package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class PasswordUpdatedEvent extends ResponseEvent {
	
	private PasswordDetails  passwordDetails;
	
	public PasswordDetails getPasswordDetails() {
		return passwordDetails;
	}

	public void setPasswordDetails(PasswordDetails passwordDetails) {
		this.passwordDetails = passwordDetails;
	}

	public static PasswordUpdatedEvent ok(PasswordDetails details) {
		PasswordUpdatedEvent event = new PasswordUpdatedEvent();
		event.setPasswordDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static PasswordUpdatedEvent invalidRequest(String message) {
		PasswordUpdatedEvent resp = new PasswordUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static PasswordUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PasswordUpdatedEvent resp = new PasswordUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static PasswordUpdatedEvent notAuthorized(UpdateParticipantEvent event1) {
		PasswordUpdatedEvent event = new PasswordUpdatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static PasswordUpdatedEvent notFound(Long userId) {
		PasswordUpdatedEvent resp = new PasswordUpdatedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
