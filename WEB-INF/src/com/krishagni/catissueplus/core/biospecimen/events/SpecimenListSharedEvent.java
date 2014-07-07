package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenListSharedEvent extends ResponseEvent {
	private Long listId;
	
	private List<UserSummary> users;

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public List<UserSummary> getUsers() {
		return users;
	}

	public void setUsers(List<UserSummary> users) {
		this.users = users;
	}

	public static SpecimenListSharedEvent ok(Long folderId, List<UserSummary> users) {
		SpecimenListSharedEvent resp = new SpecimenListSharedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setListId(folderId);
		resp.setUsers(users);
		return resp;
	}
		
	public static SpecimenListSharedEvent notFound(Long folderId) {
		SpecimenListSharedEvent resp = new SpecimenListSharedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setListId(folderId);
		return resp;
	}
	
	public static SpecimenListSharedEvent notAuthorized(Long folderId) {
		SpecimenListSharedEvent resp = new SpecimenListSharedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setListId(folderId);
		return resp;
	}	
	
	public static SpecimenListSharedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static SpecimenListSharedEvent errorResp(EventStatus status, String message, Throwable t) {
		SpecimenListSharedEvent resp = new SpecimenListSharedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
