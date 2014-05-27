package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryDefEvent extends ResponseEvent {
	private Long queryId;
	
	private String queryDef;

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getQueryDef() {
		return queryDef;
	}

	public void setQueryDef(String queryDef) {
		this.queryDef = queryDef;
	}
	
	public static QueryDefEvent ok(Long queryId, String queryDef) {
		QueryDefEvent resp = new QueryDefEvent();
		resp.setStatus(EventStatus.OK);
		resp.setQueryId(queryId);
		resp.setQueryDef(queryDef);
		return resp;
	}
	
	public static QueryDefEvent notFound(Long queryId) {
		QueryDefEvent resp = new QueryDefEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setQueryId(queryId);
		return resp;
	}
	
	public static QueryDefEvent forbidden(Long queryId) {
		QueryDefEvent resp = new QueryDefEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setQueryId(queryId);
		return resp;
	}
	
	public static QueryDefEvent serverError(String message, Throwable ... t) {
		QueryDefEvent resp = new QueryDefEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t != null && t.length > 0 ? t[0] : null);
		resp.setMessage(message != null ? message : "Internal Server Error");
		return resp;		
	}
}
