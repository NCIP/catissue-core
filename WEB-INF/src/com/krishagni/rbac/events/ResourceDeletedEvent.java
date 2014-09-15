package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class ResourceDeletedEvent extends ResponseEvent {
	String resourceName;
	
	private ResourceDetails resource;

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public ResourceDetails getResource() {
		return resource;
	}

	public void setResource(ResourceDetails resource) {
		this.resource = resource;
	}

	public static ResourceDeletedEvent ok(ResourceDetails resource) {
		ResourceDeletedEvent resp = new ResourceDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setResource(resource);
		return resp;
	}
	
	public static ResourceDeletedEvent notFound(String resourceName) {
		ResourceDeletedEvent resp = new ResourceDeletedEvent();
		resp.setResourceName(resourceName);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static ResourceDeletedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
		
	public static ResourceDeletedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static ResourceDeletedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		ResourceDeletedEvent resp = new ResourceDeletedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
