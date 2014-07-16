package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryFormContextDeletedEvent extends ResponseEvent {
	private Long formId;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public static QueryFormContextDeletedEvent ok(Long formId) {
		QueryFormContextDeletedEvent resp = new QueryFormContextDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormId(formId);
		return resp;
	}

	public static QueryFormContextDeletedEvent notFound(Long formId) {
		QueryFormContextDeletedEvent resp = new QueryFormContextDeletedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFormId(formId);
		return resp;
	}

	public static QueryFormContextDeletedEvent notAuthorized(Long formId) {
		QueryFormContextDeletedEvent resp = new QueryFormContextDeletedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setFormId(formId);
		return resp;
	}

	public static QueryFormContextDeletedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}

	private static QueryFormContextDeletedEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryFormContextDeletedEvent resp = new QueryFormContextDeletedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;
	}

}
