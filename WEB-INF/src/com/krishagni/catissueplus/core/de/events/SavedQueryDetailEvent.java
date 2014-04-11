package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SavedQueryDetailEvent extends ResponseEvent {
	
	private SavedQueryDetail savedQueryDetail;

	public SavedQueryDetail getSavedQueryDetail() {
		return savedQueryDetail;
	}

	public void setSavedQueryDetail(SavedQueryDetail savedQueryDetail) {
		this.savedQueryDetail = savedQueryDetail;
	}
	
	public static SavedQueryDetailEvent ok(SavedQueryDetail sqd) {
		SavedQueryDetailEvent resp = new SavedQueryDetailEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSavedQueryDetail(sqd);
		return resp;
	}
	
	public static SavedQueryDetailEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static SavedQueryDetailEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static SavedQueryDetailEvent errorResp(EventStatus status, String message, Throwable t) {
		SavedQueryDetailEvent resp = new SavedQueryDetailEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
