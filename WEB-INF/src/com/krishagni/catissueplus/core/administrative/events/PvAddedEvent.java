package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class PvAddedEvent extends ResponseEvent {
	
	private PermissibleValueDetails details;

	public PermissibleValueDetails getDetails() {
		return details;
	}

	public void setDetails(PermissibleValueDetails details) {
		this.details = details;
	}

	public static PvAddedEvent ok(PermissibleValueDetails details) {
		PvAddedEvent event = new PvAddedEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static PvAddedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		PvAddedEvent resp = new PvAddedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static PvAddedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PvAddedEvent resp = new PvAddedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
