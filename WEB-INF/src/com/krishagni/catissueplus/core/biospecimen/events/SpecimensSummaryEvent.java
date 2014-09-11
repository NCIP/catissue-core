package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimensSummaryEvent extends ResponseEvent {

	private List<SpecimenSummary> specimens;

	public List<SpecimenSummary> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenSummary> specimens) {
		this.specimens = specimens;
	}
	
	public static SpecimensSummaryEvent ok(List<SpecimenSummary> specimens) {
		SpecimensSummaryEvent resp = new SpecimensSummaryEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSpecimens(specimens);
		return resp;
	}
	
	public static SpecimensSummaryEvent badRequest(String message){
		return errorResp(EventStatus.BAD_REQUEST, message, null);
	}
	
	public static SpecimensSummaryEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, t.getMessage(),t);
	}
	
	private static SpecimensSummaryEvent errorResp(EventStatus status, String message, Throwable t) {
		SpecimensSummaryEvent resp = new SpecimensSummaryEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
