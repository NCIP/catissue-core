package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AllResourcesEvent extends ResponseEvent {
	private List<ResourceDetails> resources = new ArrayList<ResourceDetails>();

	public List<ResourceDetails> getResources() {
		return resources;
	}

	public void setResources(List<ResourceDetails> resources) {
		this.resources = resources;
	}
	
	public static AllResourcesEvent ok(List<ResourceDetails> resources) {
		AllResourcesEvent resp = new AllResourcesEvent();
		resp.setStatus(EventStatus.OK);
		resp.setResources(resources);
		return resp;
	}
	
	public static AllResourcesEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static AllResourcesEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		AllResourcesEvent resp = new AllResourcesEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
