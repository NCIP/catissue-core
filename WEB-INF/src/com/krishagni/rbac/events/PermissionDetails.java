package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Permission;

public class PermissionDetails {
	private Long id;
	
	private String resourceName;
	
	private String operationName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public static PermissionDetails fromPermission(Permission permission) {
		PermissionDetails permissionSummary = new PermissionDetails();
		permission = permission == null ? new Permission() : permission;
		
		permissionSummary.setId(permission.getId());		
		
		if (permission.getResource() != null) {
			permissionSummary.setResourceName(permission.getResource().getName());
		}
		
		if (permission.getOperation() != null) {
			permissionSummary.setOperationName(permission.getOperation().getName());
		}
		
		return permissionSummary;
	}
	
	public static List<PermissionDetails> fromPermissions(List<Permission> permissions) {
		List<PermissionDetails> pds = new ArrayList<PermissionDetails>();
		
		for (Permission p: permissions) {
			pds.add(PermissionDetails.fromPermission(p));
		}
		
		return pds;
	}
}
