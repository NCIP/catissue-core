package com.krishagni.rbac.domain;

public class GroupRole {
	private Long id;
	
	private Group group;

	private Role role;
	
	private Long dsoId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}	
}
