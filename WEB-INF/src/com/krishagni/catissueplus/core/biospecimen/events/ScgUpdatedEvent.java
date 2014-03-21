
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ScgUpdatedEvent extends ResponseEvent {

	private ScgDetail detail;

	private Long scgId;

	public ScgDetail getDetail() {
		return detail;
	}

	public void setDetail(ScgDetail detail) {
		this.detail = detail;
	}

	public Long getScgId() {
		return scgId;
	}

	public void setScgId(Long scgId) {
		this.scgId = scgId;
	}

	public static ScgUpdatedEvent ok(ScgDetail detail) {
		ScgUpdatedEvent event = new ScgUpdatedEvent();
		event.setStatus(EventStatus.OK);
		event.setDetail(detail);
		return event;
	}

	public static ScgUpdatedEvent notAuthorized(CreateScgEvent createScgEvent) {
		ScgUpdatedEvent event = new ScgUpdatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static ScgUpdatedEvent invalidRequest(String message, ErroneousField... fields) {
		ScgUpdatedEvent resp = new ScgUpdatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static ScgUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ScgUpdatedEvent resp = new ScgUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static ScgUpdatedEvent notFound(Long scgId) {
		ScgUpdatedEvent resp = new ScgUpdatedEvent();
		resp.setScgId(scgId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
