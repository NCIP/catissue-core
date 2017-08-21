package com.krishagni.rbac.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Permission;

public interface PermissionDao extends Dao<Permission> {
	public Permission getPermission(Long permissionId);
	
	public Permission getPermission(String resourceName, String operationName);
	
	public List<Permission> getPermissions(PermissionListCriteria listCriteria);
}
