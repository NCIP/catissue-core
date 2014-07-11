
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ScgReportUpdatedEvent extends ResponseEvent {

	private ScgReportDetail detail;

	private Long scgId;

	public ScgReportDetail getDetail() {
		return detail;
	}

	public void setDetail(ScgReportDetail detail) {
		this.detail = detail;
	}

	public Long getScgId() {
		return scgId;
	}

	public void setScgId(Long scgId) {
		this.scgId = scgId;
	}

	public static ScgReportUpdatedEvent ok(ScgReportDetail detail) {
		ScgReportUpdatedEvent event = new ScgReportUpdatedEvent();
		event.setStatus(EventStatus.OK);
		event.setDetail(detail);
		return event;
	}

	public static ScgReportUpdatedEvent notAuthorized(CreateScgEvent createScgEvent) {
		ScgReportUpdatedEvent event = new ScgReportUpdatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static ScgReportUpdatedEvent invalidRequest(String message, ErroneousField... fields) {
		ScgReportUpdatedEvent resp = new ScgReportUpdatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static ScgReportUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ScgReportUpdatedEvent resp = new ScgReportUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static ScgReportUpdatedEvent notFound(Long scgId) {
		ScgReportUpdatedEvent resp = new ScgReportUpdatedEvent();
		resp.setScgId(scgId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
