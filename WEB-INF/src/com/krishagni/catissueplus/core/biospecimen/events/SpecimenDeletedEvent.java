package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class SpecimenDeletedEvent extends ResponseEvent{
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static SpecimenDeletedEvent ok() {
		SpecimenDeletedEvent event = new SpecimenDeletedEvent();
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static SpecimenDeletedEvent invalidRequest(String message, Long... id) {
		SpecimenDeletedEvent resp = new SpecimenDeletedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static SpecimenDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SpecimenDeletedEvent resp = new SpecimenDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static SpecimenDeletedEvent notFound(Long participantId) {
		SpecimenDeletedEvent resp = new SpecimenDeletedEvent();
		resp.setId(participantId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
}
