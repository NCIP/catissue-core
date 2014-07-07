package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ListSpecimensUpdatedEvent extends ResponseEvent {
	private Long listId;
	
	private List<SpecimenSummary> specimens;

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public List<SpecimenSummary> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenSummary> specimens) {
		this.specimens = specimens;
	}
	
	public static ListSpecimensUpdatedEvent ok(Long listId, List<SpecimenSummary> specimens) {
		ListSpecimensUpdatedEvent resp = new ListSpecimensUpdatedEvent();
		resp.setListId(listId);
		resp.setSpecimens(specimens);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static ListSpecimensUpdatedEvent notFound(Long listId) {
		ListSpecimensUpdatedEvent resp = new ListSpecimensUpdatedEvent();
		resp.setListId(listId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static ListSpecimensUpdatedEvent notAuthorized(Long listId) {
		ListSpecimensUpdatedEvent resp = new ListSpecimensUpdatedEvent();
		resp.setListId(listId);
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		return resp;
	}
	
	public static ListSpecimensUpdatedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static ListSpecimensUpdatedEvent errorResp(EventStatus status, String message, Throwable t) {
		ListSpecimensUpdatedEvent resp = new ListSpecimensUpdatedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
