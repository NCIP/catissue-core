package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AllPermissionsEvent extends ResponseEvent {
	 private List<PermissionDetails> permissions = new ArrayList<PermissionDetails>();

	public List<PermissionDetails> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionDetails> permissions) {
		this.permissions = permissions;
	}

	public static AllPermissionsEvent ok(List<PermissionDetails> permissions) {
		AllPermissionsEvent resp = new AllPermissionsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setPermissions(permissions);
		return resp;
	}
	
	public static AllPermissionsEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static AllPermissionsEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		AllPermissionsEvent resp = new AllPermissionsEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
