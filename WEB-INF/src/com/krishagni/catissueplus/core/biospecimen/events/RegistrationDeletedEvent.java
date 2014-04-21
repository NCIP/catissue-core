package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class RegistrationDeletedEvent extends ResponseEvent{
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static RegistrationDeletedEvent ok() {
		RegistrationDeletedEvent event = new RegistrationDeletedEvent();
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static RegistrationDeletedEvent invalidRequest(String message, Long... id) {
		RegistrationDeletedEvent resp = new RegistrationDeletedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static RegistrationDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		RegistrationDeletedEvent resp = new RegistrationDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static RegistrationDeletedEvent notFound(Long participantId) {
		RegistrationDeletedEvent resp = new RegistrationDeletedEvent();
		resp.setId(participantId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
