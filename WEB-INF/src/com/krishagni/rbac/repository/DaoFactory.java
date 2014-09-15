package com.krishagni.rbac.repository;

public interface DaoFactory {
	public ResourceDao getResourceDao();
	
	public OperationDao getOperationDao();
	
	public PermissionDao getPermissionDao();
	
	public RoleDao getRoleDao();
	
	public GroupDao getGroupDao();
	
	public SubjectDao getSubjectDao();
}
