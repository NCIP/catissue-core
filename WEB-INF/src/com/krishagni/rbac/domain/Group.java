package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

public class Group {
	private Long id;
	
	private Set<GroupRole> groupRoles = new HashSet<GroupRole>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<GroupRole> getGroupRoles() {
		return groupRoles;
	}

	public void setGroupRoles(Set<GroupRole> groupRoles) {
		this.groupRoles = groupRoles;
	}
	
	public void assignRole(GroupRole groupRole) {
		boolean found = false;		
		for (GroupRole gr : groupRoles) {
			if (gr.getDsoId().equals(groupRole.getDsoId())) {
				gr.setRole(groupRole.getRole());
				found = true;
				break;
			}
		}
		
		if (!found) {
			groupRole.setGroup(this);
			groupRoles.add(groupRole);
		}
	}
	
	public void updateRoles(List<GroupRole> groupRoles, Session session) {
		Set<Long> dsoIds = new HashSet<Long>();
		session.flush();
		
		for (GroupRole role : groupRoles) {
			if (!dsoIds.add(role.getDsoId())) {
				throw new IllegalArgumentException("Multiple roles defined for same DSO");
			}
			
			role.setGroup(this);
		}
		
		this.groupRoles.clear();
		this.groupRoles.addAll(groupRoles);
	}
}
