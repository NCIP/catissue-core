package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DerivedSpecimenReqCreatedEvent extends ResponseEvent {
	private SpecimenRequirementDetail derived;

	public SpecimenRequirementDetail getDerived() {
		return derived;
	}

	public void setDerived(SpecimenRequirementDetail derived) {
		this.derived = derived;
	}
	
	public static DerivedSpecimenReqCreatedEvent ok(SpecimenRequirementDetail derived) {
		DerivedSpecimenReqCreatedEvent resp = new DerivedSpecimenReqCreatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setDerived(derived);
		return resp;
	}
	
	public static DerivedSpecimenReqCreatedEvent badRequest(Exception e) {
		DerivedSpecimenReqCreatedEvent resp = new DerivedSpecimenReqCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		if (e instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)e;
			resp.setErroneousFields(oce.getErroneousFields());
			resp.setMessage(oce.getMessage());
		}
		
		return resp;
	}
	
	public static DerivedSpecimenReqCreatedEvent serverError(Exception e) {
		DerivedSpecimenReqCreatedEvent resp = new DerivedSpecimenReqCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
