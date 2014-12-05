package com.krishagni.openspecimen.custom.demo.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimensCollectedEvent extends ResponseEvent {
	private SpecimenCollectionDetail collectionDetails;

	public SpecimenCollectionDetail getCollectionDetails() {
		return collectionDetails;
	}

	public void setCollectionDetails(SpecimenCollectionDetail collectionDetails) {
		this.collectionDetails = collectionDetails;
	}
	
	public static SpecimensCollectedEvent ok(SpecimenCollectionDetail detail) {
		SpecimensCollectedEvent resp = new SpecimensCollectedEvent();
		resp.setCollectionDetails(detail);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static SpecimensCollectedEvent badRequest(Throwable ... t) {
		SpecimensCollectedEvent resp = new SpecimensCollectedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(t != null && t.length > 0 ? t[0] : null);
		return resp;
	}
	
	public static SpecimensCollectedEvent serverError(Throwable ... t) {
		SpecimensCollectedEvent resp = new SpecimensCollectedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t != null && t.length > 0 ? t[0] : null);
		return resp;		
	}
}
