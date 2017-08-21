package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Permission;

public class PermissionDetail {
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

	public static PermissionDetail from(Permission permission) {
		PermissionDetail permissionSummary = new PermissionDetail();
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
	
	public static List<PermissionDetail> from(List<Permission> permissions) {
		List<PermissionDetail> pds = new ArrayList<PermissionDetail>();
		
		for (Permission p: permissions) {
			pds.add(PermissionDetail.from(p));
		}
		
		return pds;
	}
}
