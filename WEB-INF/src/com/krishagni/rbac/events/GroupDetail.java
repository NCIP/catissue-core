package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Group;
import com.krishagni.rbac.domain.GroupRole;

public class GroupDetail {
	private Long id;
	
	private List<GroupRoleDetail> roles = new ArrayList<GroupRoleDetail>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<GroupRoleDetail> getRoles() {
		return roles;
	}

	public void setRoles(List<GroupRoleDetail> roles) {
		this.roles = roles;
	}
	
	public static GroupDetail from(Group group) {
		if (group == null) {
			group = new Group();
		}
		
		GroupDetail gd = new GroupDetail();
		
		gd.setId(group.getId());
		
		for (GroupRole gr : group.getGroupRoles()) {
			gd.getRoles().add(GroupRoleDetail.from(gr));
		}
		
		return gd;
	}
}
