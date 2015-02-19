package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.rbac.domain.GroupRole;

public class GroupRoleDetail {
	private Long dsoId;
	
	private RoleDetail roleDetails;

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}

	public RoleDetail getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetail roleDetails) {
		this.roleDetails = roleDetails;
	}
	
	public static GroupRoleDetail from(GroupRole groupRole) {
		GroupRoleDetail gd = new GroupRoleDetail();
		gd.setDsoId(groupRole.getDsoId());
		
		if (groupRole.getRole() != null) {
			gd.setRoleDetails(RoleDetail.from(groupRole.getRole()));
		}
		
		return gd;
	}
	
	public static List<GroupRoleDetail> from(Collection<GroupRole> groupRoles) {
		List<GroupRoleDetail> roles = new ArrayList<GroupRoleDetail>();
		for (GroupRole gr : groupRoles) {
			roles.add(from(gr));
		}
		
		return roles;
	}
}
