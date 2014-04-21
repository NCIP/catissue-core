package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class ScgDeletedEvent extends ResponseEvent{

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static ScgDeletedEvent ok() {
		ScgDeletedEvent event = new ScgDeletedEvent();
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static ScgDeletedEvent invalidRequest(String message, ErroneousField... field) {
		ScgDeletedEvent resp = new ScgDeletedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static ScgDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ScgDeletedEvent resp = new ScgDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static ScgDeletedEvent notFound(Long participantId) {
		ScgDeletedEvent resp = new ScgDeletedEvent();
		resp.setId(participantId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
