package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryFoldersEvent extends ResponseEvent {

	private List<QueryFolderSummary> folders;

	public List<QueryFolderSummary> getFolders() {
		return folders;
	}

	public void setFolders(List<QueryFolderSummary> folders) {
		this.folders = folders;
	}

	public static QueryFoldersEvent ok(List<QueryFolderSummary> folders) {
		QueryFoldersEvent resp = new QueryFoldersEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFolders(folders);
		return resp;
	}
	
	public static QueryFoldersEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryFoldersEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryFoldersEvent resp = new QueryFoldersEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
