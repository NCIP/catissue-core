package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateGroupRoleEvent extends RequestEvent {
	private Long groupId;
	
	private List<GroupRoleSummary> roles = new ArrayList<GroupRoleSummary>();

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<GroupRoleSummary> getRoles() {
		return roles;
	}

	public void setRoles(List<GroupRoleSummary> roles) {
		this.roles = roles;
	}
}
