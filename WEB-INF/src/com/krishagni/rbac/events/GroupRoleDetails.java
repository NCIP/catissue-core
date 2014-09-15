package com.krishagni.rbac.events;

import com.krishagni.rbac.domain.GroupRole;

public class GroupRoleDetails {
	private Long dsoId;
	
	private RoleDetails roleDetails;

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}

	public RoleDetails getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetails roleDetails) {
		this.roleDetails = roleDetails;
	}
	
	public static GroupRoleDetails fromGroupRole(GroupRole groupRole) {
		GroupRoleDetails gd = new GroupRoleDetails();
		gd.setDsoId(groupRole.getDsoId());
		
		if (groupRole.getRole() != null) {
			gd.setRoleDetails(RoleDetails.fromRole(groupRole.getRole()));
		}
		
		return gd;
	}
}
