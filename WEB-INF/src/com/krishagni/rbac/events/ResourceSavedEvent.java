package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class ResourceSavedEvent extends ResponseEvent {
	private ResourceDetails resource;
	
	public ResourceDetails getResource() {
		return resource;
	}

	public void setResource(ResourceDetails resource) {
		this.resource = resource;
	}

	public static ResourceSavedEvent ok(ResourceDetails resource) {
		ResourceSavedEvent resp = new ResourceSavedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setResource(resource);
		return resp;
	}
	
	public static ResourceSavedEvent notFound(ResourceDetails resource) {
		ResourceSavedEvent resp = new ResourceSavedEvent();
		resp.setResource(resource);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static ResourceSavedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static ResourceSavedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static ResourceSavedEvent errorResp(EventStatus status, RbacErrorCode errorCode, Throwable t) {
		ResourceSavedEvent resp = new ResourceSavedEvent();
		resp.setupResponseEvent(status, errorCode, t);
		return resp;		
	}
}
