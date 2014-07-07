package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenListDetailEvent extends ResponseEvent {
	private Long listId;
	
	private SpecimenListDetails details;

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public SpecimenListDetails getDetails() {
		return details;
	}

	public void setDetails(SpecimenListDetails details) {
		this.details = details;
	}
	
	public static SpecimenListDetailEvent ok(SpecimenListDetails details) {
		SpecimenListDetailEvent resp = new SpecimenListDetailEvent();
		resp.setStatus(EventStatus.OK);
		resp.setListId(details.getId());
		resp.setDetails(details);
		return resp;
	}
	
	public static SpecimenListDetailEvent notFound(Long listId) {
		SpecimenListDetailEvent resp = new SpecimenListDetailEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setListId(listId);
		return resp;		
	}
	
	public static SpecimenListDetailEvent notAuthorized(Long listId) {
		SpecimenListDetailEvent resp = new SpecimenListDetailEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setListId(listId);
		return resp;
	}
	
	public static SpecimenListDetailEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static SpecimenListDetailEvent errorResp(EventStatus status, String message, Throwable t) {
		SpecimenListDetailEvent resp = new SpecimenListDetailEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
