package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class PermissionDeletedEvent extends ResponseEvent {
	private PermissionDetails permission;

	public PermissionDetails getPermissionDetails() {
		return permission;
	}

	public void setPermissionDetails(PermissionDetails permission) {
		this.permission = permission;
	}
	
	public static PermissionDeletedEvent ok(PermissionDetails permission) {
		PermissionDeletedEvent resp = new PermissionDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setPermissionDetails(permission);
		return resp;
	}
	
	public static PermissionDeletedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static PermissionDeletedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static PermissionDeletedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		PermissionDeletedEvent resp = new PermissionDeletedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
