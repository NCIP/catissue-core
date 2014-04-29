package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FolderQueriesUpdatedEvent extends ResponseEvent {
	private Long folderId;
	
	private List<SavedQuerySummary> queries;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public List<SavedQuerySummary> getQueries() {
		return queries;
	}

	public void setQueries(List<SavedQuerySummary> queries) {
		this.queries = queries;
	}
	
	public static FolderQueriesUpdatedEvent ok(Long folderId, List<SavedQuerySummary> queries) {
		FolderQueriesUpdatedEvent resp = new FolderQueriesUpdatedEvent();
		resp.setFolderId(folderId);
		resp.setQueries(queries);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static FolderQueriesUpdatedEvent notFound(Long folderId) {
		FolderQueriesUpdatedEvent resp = new FolderQueriesUpdatedEvent();
		resp.setFolderId(folderId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static FolderQueriesUpdatedEvent notAuthorized(Long folderId) {
		FolderQueriesUpdatedEvent resp = new FolderQueriesUpdatedEvent();
		resp.setFolderId(folderId);
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		return resp;
	}
	
	public static FolderQueriesUpdatedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static FolderQueriesUpdatedEvent errorResp(EventStatus status, String message, Throwable t) {
		FolderQueriesUpdatedEvent resp = new FolderQueriesUpdatedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}

	

}
