package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class SaveResourceEvent extends RequestEvent {
	private ResourceDetails resource;

	public ResourceDetails getResource() {
		return resource;
	}

	public void setResource(ResourceDetails resource) {
		this.resource = resource;
	}
}
