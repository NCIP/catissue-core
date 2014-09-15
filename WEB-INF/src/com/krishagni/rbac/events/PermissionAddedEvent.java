package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class PermissionAddedEvent extends ResponseEvent {
	private PermissionDetails permission;
	
	public PermissionDetails getPermission() {
		return permission;
	}

	public void setPermission(PermissionDetails permission) {
		this.permission = permission;
	}

	public static PermissionAddedEvent ok(PermissionDetails permission) {
		PermissionAddedEvent resp = new PermissionAddedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setPermission(permission);
		return resp;
	}
	
	public static PermissionAddedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static PermissionAddedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static PermissionAddedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		PermissionAddedEvent resp = new PermissionAddedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
