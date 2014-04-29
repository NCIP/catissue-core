package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class QueryFolderSharedEvent extends ResponseEvent {
	private Long folderId;
	
	private List<UserSummary> users;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public List<UserSummary> getUsers() {
		return users;
	}

	public void setUsers(List<UserSummary> users) {
		this.users = users;
	}

	public static QueryFolderSharedEvent ok(Long folderId, List<UserSummary> users) {
		QueryFolderSharedEvent resp = new QueryFolderSharedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFolderId(folderId);
		resp.setUsers(users);
		return resp;
	}
		
	public static QueryFolderSharedEvent notFound(Long folderId) {
		QueryFolderSharedEvent resp = new QueryFolderSharedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFolderId(folderId);
		return resp;
	}
	
	public static QueryFolderSharedEvent notAuthorized(Long folderId) {
		QueryFolderSharedEvent resp = new QueryFolderSharedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setFolderId(folderId);
		return resp;
	}	
	
	public static QueryFolderSharedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryFolderSharedEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryFolderSharedEvent resp = new QueryFolderSharedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
