package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryFolderDeletedEvent extends ResponseEvent {
	private Long folderId;
	
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public static QueryFolderDeletedEvent ok(Long folderId) {
		QueryFolderDeletedEvent resp = new QueryFolderDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFolderId(folderId);
		return resp;
	}
	
	public static QueryFolderDeletedEvent notFound(Long folderId) {
		QueryFolderDeletedEvent resp = new QueryFolderDeletedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFolderId(folderId);
		return resp;				
	}
	
	public static QueryFolderDeletedEvent notAuthorized(Long folderId) {
		QueryFolderDeletedEvent resp = new QueryFolderDeletedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setFolderId(folderId);
		return resp;						
	}

	public static QueryFolderDeletedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryFolderDeletedEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryFolderDeletedEvent resp = new QueryFolderDeletedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
