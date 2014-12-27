package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AddSpecimenRequirementEvent extends RequestEvent {
	private SpecimenRequirementDetail requirement;

	public SpecimenRequirementDetail getRequirement() {
		return requirement;
	}

	public void setRequirement(SpecimenRequirementDetail requirement) {
		this.requirement = requirement;
	}
}
