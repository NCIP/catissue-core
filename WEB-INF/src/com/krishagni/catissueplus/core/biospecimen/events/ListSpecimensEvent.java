package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ListSpecimensEvent extends ResponseEvent {
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
	
	public static ListSpecimensEvent ok(List<SpecimenSummary> specimens) {
		ListSpecimensEvent resp = new ListSpecimensEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSpecimens(specimens);
		return resp;
	}
	
	public static ListSpecimensEvent notFound(Long listId) {
		ListSpecimensEvent resp = new ListSpecimensEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setListId(listId);
		return resp;
	}
	
	public static ListSpecimensEvent notAuthorized(Long listId) {
		ListSpecimensEvent resp = new ListSpecimensEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setListId(listId);
		return resp;
	}
	
		
	public static ListSpecimensEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static ListSpecimensEvent errorResp(EventStatus status, String message, Throwable t) {
		ListSpecimensEvent resp = new ListSpecimensEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
