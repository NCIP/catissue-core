package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class ScgCreatedEvent extends ResponseEvent{

	private ScgDetail detail;
	
	
	public ScgDetail getDetail() {
		return detail;
	}

	
	public void setDetail(ScgDetail detail) {
		this.detail = detail;
	}

	public static ScgCreatedEvent ok(ScgDetail detail) {
		ScgCreatedEvent event = new ScgCreatedEvent();
		event.setStatus(EventStatus.OK);
		event.setDetail(detail);
		return event;
	}

	public static ScgCreatedEvent notAuthorized(CreateScgEvent createScgEvent) {
		ScgCreatedEvent event = new ScgCreatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static ScgCreatedEvent invalidRequest(String message, ErroneousField... fields) {
		ScgCreatedEvent resp = new ScgCreatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static ScgCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ScgCreatedEvent resp = new ScgCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
