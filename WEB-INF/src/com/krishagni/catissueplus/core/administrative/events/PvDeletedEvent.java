
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PvDeletedEvent extends ResponseEvent {

	private static final String SUCCESS = "success";

	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static PvDeletedEvent ok() {
		PvDeletedEvent event = new PvDeletedEvent();
		event.setStatus(EventStatus.OK);
		event.setMessage(SUCCESS);
		return event;
	}

	public static PvDeletedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		PvDeletedEvent resp = new PvDeletedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static PvDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PvDeletedEvent resp = new PvDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static PvDeletedEvent notFound(Long id) {
		PvDeletedEvent resp = new PvDeletedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static PvDeletedEvent notFound(String name) {
		PvDeletedEvent resp = new PvDeletedEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
