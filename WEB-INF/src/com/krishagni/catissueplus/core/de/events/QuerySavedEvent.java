package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QuerySavedEvent extends ResponseEvent {

	private SavedQueryDetail savedQueryDetail;

	public SavedQueryDetail getSavedQueryDetail() {
		return savedQueryDetail;
	}

	public void setSavedQueryDetail(SavedQueryDetail savedQueryDetail) {
		this.savedQueryDetail = savedQueryDetail;
	}
	
	public static QuerySavedEvent ok(SavedQueryDetail savedQueryDetail) {
		QuerySavedEvent resp = new QuerySavedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSavedQueryDetail(savedQueryDetail);
		return resp;
	}
	
	public static QuerySavedEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static QuerySavedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QuerySavedEvent errorResp(EventStatus status, String message, Throwable t) {
		QuerySavedEvent resp = new QuerySavedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
