package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FolderQueriesEvent extends ResponseEvent {
	private Long folderId;
	
	private Long count;

	private List<SavedQuerySummary> savedQueries;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<SavedQuerySummary> getSavedQueries() {
		return savedQueries;
	}

	public void setSavedQueries(List<SavedQuerySummary> savedQueries) {
		this.savedQueries = savedQueries;
	}
	
	public static FolderQueriesEvent ok(List<SavedQuerySummary> queries, Long count) {
		FolderQueriesEvent resp = new FolderQueriesEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSavedQueries(queries);
		resp.setCount(count);
		return resp;
	}
	
	public static FolderQueriesEvent notFound(Long folderId) {
		FolderQueriesEvent resp = new FolderQueriesEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFolderId(folderId);
		return resp;
	}
	
	public static FolderQueriesEvent notAuthorized(Long folderId) {
		FolderQueriesEvent resp = new FolderQueriesEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setFolderId(folderId);
		return resp;
	}
	
		
	public static FolderQueriesEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static FolderQueriesEvent errorResp(EventStatus status, String message, Throwable t) {
		FolderQueriesEvent resp = new FolderQueriesEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
