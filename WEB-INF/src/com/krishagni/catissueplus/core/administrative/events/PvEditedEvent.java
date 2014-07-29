
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PvEditedEvent extends ResponseEvent {

	private PermissibleValueDetails details;

	private Long id;

	public PermissibleValueDetails getDetails() {
		return details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDetails(PermissibleValueDetails details) {
		this.details = details;
	}

	public static PvEditedEvent ok(PermissibleValueDetails details) {
		PvEditedEvent event = new PvEditedEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static PvEditedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		PvEditedEvent resp = new PvEditedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static PvEditedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PvEditedEvent resp = new PvEditedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static PvEditedEvent notFound(Long pvId) {
		PvEditedEvent resp = new PvEditedEvent();
		resp.setId(pvId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
