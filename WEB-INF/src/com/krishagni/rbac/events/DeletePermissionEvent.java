package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeletePermissionEvent extends RequestEvent {
	private PermissionDetails permission;

	public PermissionDetails getPermission() {
		return permission;
	}

	public void setPermissionDetails(PermissionDetails permission) {
		this.permission = permission;
	}
}
