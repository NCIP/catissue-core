package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateRolePermissionsEvent extends RequestEvent {
	public enum Action {
		ADD,
		DELETE
	}
	
	private String roleName;
	
	private List<PermissionDetails> permissions = new ArrayList<PermissionDetails>();
	
	private Action action;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<PermissionDetails> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionDetails> permissions) {
		this.permissions = permissions;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
