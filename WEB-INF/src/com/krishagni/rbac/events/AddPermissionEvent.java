package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AddPermissionEvent extends RequestEvent {
	private PermissionDetails permission;

	public PermissionDetails getPermission() {
		return permission;
	}

	public void setPermission(PermissionDetails permission) {
		this.permission = permission;
	}
}
