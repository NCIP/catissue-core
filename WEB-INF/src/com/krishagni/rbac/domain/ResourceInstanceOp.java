package com.krishagni.rbac.domain;

public class ResourceInstanceOp {
	private Long id;
	
	private RoleAccessControl roleAccessControl;
	
	private Long resourceInstanceId;
	
	private Operation operation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleAccessControl getRoleAccessControl() {
		return roleAccessControl;
	}

	public void setRoleAccessControl(RoleAccessControl roleAccessControl) {
		this.roleAccessControl = roleAccessControl;
	}

	public Long getResourceInstanceId() {
		return resourceInstanceId;
	}

	public void setResourceInstanceId(Long resourceInstanceId) {
		this.resourceInstanceId = resourceInstanceId;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}	
}
