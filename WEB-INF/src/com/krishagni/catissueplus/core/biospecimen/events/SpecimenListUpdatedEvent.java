package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenListUpdatedEvent extends ResponseEvent {

	private SpecimenListDetails listDetails;

	public SpecimenListDetails getListDetails() {
		return listDetails;
	}

	public void setListDetails(SpecimenListDetails listDetails) {
		this.listDetails = listDetails;
	}
	
	public static SpecimenListUpdatedEvent ok(SpecimenListDetails listDetails) {
		SpecimenListUpdatedEvent resp = new SpecimenListUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setListDetails(listDetails);
		return resp;
	}
	
	public static SpecimenListUpdatedEvent badRequest(CatissueErrorCode errorCode) {
		SpecimenListUpdatedEvent resp = new SpecimenListUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(errorCode.message());
		return resp;		
	}
	
	public static SpecimenListUpdatedEvent badRequest(ObjectCreationException oce) {
		SpecimenListUpdatedEvent resp = new SpecimenListUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setErroneousFields(oce.getErroneousFields());
		resp.setException(oce);
		resp.setMessage(oce.getMessage());
		return resp;		
	}	
	
	public static SpecimenListUpdatedEvent serverError(String message, Throwable t) {
		SpecimenListUpdatedEvent resp = new SpecimenListUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
