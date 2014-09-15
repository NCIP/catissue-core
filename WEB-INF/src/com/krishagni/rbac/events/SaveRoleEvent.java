package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class SaveRoleEvent extends RequestEvent {
	private RoleDetails role;

	public RoleDetails getRole() {
		return role;
	}

	public void setRole(RoleDetails role) {
		this.role = role;
	}
}
