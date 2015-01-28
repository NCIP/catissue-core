package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateAliquotsRequirementEvent extends RequestEvent {
	private AliquotSpecimensRequirement requirement;

	public AliquotSpecimensRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(AliquotSpecimensRequirement requirement) {
		this.requirement = requirement;
	}
}
