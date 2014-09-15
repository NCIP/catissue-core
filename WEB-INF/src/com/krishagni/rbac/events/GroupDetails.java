package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Group;
import com.krishagni.rbac.domain.GroupRole;

public class GroupDetails {
	private Long id;
	
	private List<GroupRoleDetails> roles = new ArrayList<GroupRoleDetails>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<GroupRoleDetails> getRoles() {
		return roles;
	}

	public void setRoles(List<GroupRoleDetails> roles) {
		this.roles = roles;
	}
	
	public static GroupDetails fromGroup(Group group) {
		if (group == null) {
			group = new Group();
		}
		
		GroupDetails gd = new GroupDetails();
		
		gd.setId(group.getId());
		
		for (GroupRole gr : group.getGroupRoles()) {
			gd.getRoles().add(GroupRoleDetails.fromGroupRole(gr));
		}
		
		return gd;
	}
}
