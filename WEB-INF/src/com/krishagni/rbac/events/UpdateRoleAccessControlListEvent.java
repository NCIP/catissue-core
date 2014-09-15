package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateRoleAccessControlListEvent extends RequestEvent {
	public enum Action {
		ADD,
		DELETE	
	}
	
	private Action action;
	
	private String roleName;
	
	private List<RoleAccessControlDetails> roleAccessDetails = new ArrayList<RoleAccessControlDetails>();

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<RoleAccessControlDetails> getRoleAccessDetails() {
		return roleAccessDetails;
	}

	public void setRoleAccessDetails(List<RoleAccessControlDetails> roleAccessDetails) {
		this.roleAccessDetails = roleAccessDetails;
	}
}
