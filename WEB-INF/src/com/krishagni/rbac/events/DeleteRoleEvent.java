package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeleteRoleEvent extends RequestEvent {
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
