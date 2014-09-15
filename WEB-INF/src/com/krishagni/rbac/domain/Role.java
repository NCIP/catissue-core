package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

public class Role {
	private Long id;

	private String name;

	private String description;
	
	private Set<RoleAccessControl> acl = new HashSet<RoleAccessControl>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<RoleAccessControl> getAcl() {
		return acl;
	}

	public void setAcl(Set<RoleAccessControl> acl) {
		this.acl = acl;
	}
	
	public void updateRole(Role newRole, Session session) {
		setName(newRole.getName());
		setDescription(newRole.getDescription());
		updateAcl(newRole.getAcl(), session);
	}
	
	private void updateAcl(Set<RoleAccessControl> newAcl, Session session) {
		acl.clear();
		session.flush();
		acl.addAll(newAcl);
		
		for (RoleAccessControl rac : acl) {
			rac.setRole(this);
		}
	}
}
