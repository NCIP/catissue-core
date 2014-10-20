package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FormDeletedEvent extends ResponseEvent {

	private Long formId;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public static FormDeletedEvent ok(Long formId) {
		FormDeletedEvent resp = new FormDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormId(formId);
		return resp;
	}

	public static FormDeletedEvent notFound(Long formId) {
		FormDeletedEvent resp = new FormDeletedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFormId(formId);
		return resp;
	}

	public static FormDeletedEvent notAuthorized(Long formId) {
		FormDeletedEvent resp = new FormDeletedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setFormId(formId);
		return resp;
	}

	public static FormDeletedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}

	private static FormDeletedEvent errorResp(EventStatus status, String message, Throwable t) {
		FormDeletedEvent resp = new FormDeletedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;
	}
}
