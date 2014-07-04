package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryDeletedEvent extends ResponseEvent {

	private Long queryId;

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public static QueryDeletedEvent ok(Long queryId) {
		QueryDeletedEvent resp = new QueryDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setQueryId(queryId);
		return resp;
	}

	public static QueryDeletedEvent notFound(Long queryId) {
		QueryDeletedEvent resp = new QueryDeletedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setQueryId(queryId);
		return resp;
	}

	public static QueryDeletedEvent notAuthorized(Long queryId) {
		QueryDeletedEvent resp = new QueryDeletedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setQueryId(queryId);
		return resp;
	}

	public static QueryDeletedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}

	private static QueryDeletedEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryDeletedEvent resp = new QueryDeletedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;
	}
}
