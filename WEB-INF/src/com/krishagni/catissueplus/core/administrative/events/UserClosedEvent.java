package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class UserClosedEvent extends ResponseEvent{
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static UserClosedEvent ok() {
		UserClosedEvent event = new UserClosedEvent();
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static UserClosedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		UserClosedEvent resp = new UserClosedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static UserClosedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		UserClosedEvent resp = new UserClosedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static UserClosedEvent notFound(Long userId) {
		UserClosedEvent resp = new UserClosedEvent();
		resp.setId(userId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
