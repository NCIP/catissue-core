package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.Set;

public class RoleAccessControl {
	private Long id;
	
	private Role role;
	
	private Resource resource;
	
	private Set<ResourceInstanceOp> operations = new HashSet<ResourceInstanceOp>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Set<ResourceInstanceOp> getOperations() {
		return operations;
	}

	public void setOperations(Set<ResourceInstanceOp> operations) {
		this.operations = operations;
	}	
}
