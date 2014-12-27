package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenRequirementAddedEvent extends ResponseEvent {
	private SpecimenRequirementDetail requirement;

	public SpecimenRequirementDetail getRequirement() {
		return requirement;
	}

	public void setRequirement(SpecimenRequirementDetail requirement) {
		this.requirement = requirement;
	}
	
	public static SpecimenRequirementAddedEvent ok(SpecimenRequirementDetail requirement) {
		SpecimenRequirementAddedEvent resp = new SpecimenRequirementAddedEvent();
		resp.setRequirement(requirement);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static SpecimenRequirementAddedEvent badRequest(Exception e) {
		SpecimenRequirementAddedEvent resp = new SpecimenRequirementAddedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		if (e instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)e;
			resp.setErroneousFields(oce.getErroneousFields());
			resp.setMessage(oce.getMessage());
		}
		
		return resp;		
	}
	
	public static SpecimenRequirementAddedEvent serverError(Exception e) {
		SpecimenRequirementAddedEvent resp = new SpecimenRequirementAddedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
