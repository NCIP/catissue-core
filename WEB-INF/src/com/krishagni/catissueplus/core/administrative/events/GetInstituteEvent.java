package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class GetInstituteEvent extends RequestEvent {

	private Long instId;

	private String name;

	public Long getInstId() {
		return instId;
	}

	public void setInstId(Long instId) {
		this.instId = instId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
