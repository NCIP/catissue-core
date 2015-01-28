package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateDerivedSpecimenReqEvent extends RequestEvent {
	private DerivedSpecimenRequirement requirement;

	public DerivedSpecimenRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(DerivedSpecimenRequirement req) {
		this.requirement = req;
	}
}
