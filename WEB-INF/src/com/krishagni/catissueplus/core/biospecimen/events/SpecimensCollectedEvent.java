package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimensCollectedEvent extends ResponseEvent {
	private List<SpecimenDetail> specimens;

	public List<SpecimenDetail> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenDetail> specimens) {
		this.specimens = specimens;
	}
	
	public static SpecimensCollectedEvent ok(List<SpecimenDetail> specimens) {
		SpecimensCollectedEvent resp = new SpecimensCollectedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSpecimens(specimens);
		return resp;
	}
	
	public static SpecimensCollectedEvent badRequest(Exception e) {
		SpecimensCollectedEvent resp = new SpecimensCollectedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		return resp;
	}
	
	public static SpecimensCollectedEvent serverError(Exception e) {
		SpecimensCollectedEvent resp = new SpecimensCollectedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
