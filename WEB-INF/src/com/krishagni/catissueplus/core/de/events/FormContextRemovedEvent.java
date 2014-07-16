package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FormContextRemovedEvent extends ResponseEvent {
	private Long formId;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public static FormContextRemovedEvent ok(Long formId) {
		FormContextRemovedEvent resp = new FormContextRemovedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormId(formId);
		return resp;
	}

	public static FormContextRemovedEvent notFound(Long formId) {
		FormContextRemovedEvent resp = new FormContextRemovedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFormId(formId);
		return resp;
	}

	public static FormContextRemovedEvent notAuthorized(Long formId) {
		FormContextRemovedEvent resp = new FormContextRemovedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setFormId(formId);
		return resp;
	}
	
	public static FormContextRemovedEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}

	public static FormContextRemovedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}

	private static FormContextRemovedEvent errorResp(EventStatus status, String message, Throwable t) {
		FormContextRemovedEvent resp = new FormContextRemovedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;
	}

}
