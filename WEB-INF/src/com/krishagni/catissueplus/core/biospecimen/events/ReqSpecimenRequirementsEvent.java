package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSpecimenRequirementsEvent extends RequestEvent {
	private Long cpeId;

	public Long getCpeId() {
		return cpeId;
	}

	public void setCpeId(Long cpeId) {
		this.cpeId = cpeId;
	}
}
