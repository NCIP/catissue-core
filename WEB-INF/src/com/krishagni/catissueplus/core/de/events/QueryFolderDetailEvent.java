package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryFolderDetailEvent extends ResponseEvent {
	private Long folderId;
	
	private QueryFolderDetails details;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public QueryFolderDetails getDetails() {
		return details;
	}

	public void setDetails(QueryFolderDetails details) {
		this.details = details;
	}
	
	public static QueryFolderDetailEvent ok(QueryFolderDetails details) {
		QueryFolderDetailEvent resp = new QueryFolderDetailEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFolderId(details.getId());
		resp.setDetails(details);
		return resp;
	}
	
	public static QueryFolderDetailEvent notFound(Long folderId) {
		QueryFolderDetailEvent resp = new QueryFolderDetailEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFolderId(folderId);
		return resp;		
	}
	
	public static QueryFolderDetailEvent notAuthorized(Long folderId) {
		QueryFolderDetailEvent resp = new QueryFolderDetailEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setFolderId(folderId);
		return resp;
	}
	
	public static QueryFolderDetailEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryFolderDetailEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryFolderDetailEvent resp = new QueryFolderDetailEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}

}
