package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenListCreatedEvent extends ResponseEvent {

	private SpecimenListDetails listDetails;
		
	public SpecimenListDetails getListDetails() {
		return listDetails;
	}

	public void setListDetails(SpecimenListDetails listDetails) {
		this.listDetails = listDetails;
	}

	public static SpecimenListCreatedEvent ok(SpecimenListDetails listDetails) {
		SpecimenListCreatedEvent resp = new SpecimenListCreatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setListDetails(listDetails);
		return resp;
	}
	
	public static SpecimenListCreatedEvent badRequest(ObjectCreationException e) {
		return errorResp(EventStatus.BAD_REQUEST, e.getMessage(), e);
	}
	
	public static SpecimenListCreatedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static SpecimenListCreatedEvent errorResp(EventStatus status, String message, Throwable t) {
		SpecimenListCreatedEvent resp = new SpecimenListCreatedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		if (t instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)t;
			resp.setErroneousFields(oce.getErroneousFields());
		}
		
		return resp;		
	}
}
