package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryUpdatedEvent extends ResponseEvent {
	
	private SavedQueryDetail savedQueryDetail;

	public SavedQueryDetail getSavedQueryDetail() {
		return savedQueryDetail;
	}

	public void setSavedQueryDetail(SavedQueryDetail savedQueryDetail) {
		this.savedQueryDetail = savedQueryDetail;
	}
	
	public static QueryUpdatedEvent ok(SavedQueryDetail savedQueryDetail) {
		QueryUpdatedEvent resp = new QueryUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSavedQueryDetail(savedQueryDetail);
		return resp;
	}
	
	public static QueryUpdatedEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static QueryUpdatedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryUpdatedEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryUpdatedEvent resp = new QueryUpdatedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
