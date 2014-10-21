package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSpecimensByScgEvent extends RequestEvent {
	private Long scgId;

	public Long getScgId() {
		return scgId;
	}

	public void setScgId(Long scgId) {
		this.scgId = scgId;
	}
}	
