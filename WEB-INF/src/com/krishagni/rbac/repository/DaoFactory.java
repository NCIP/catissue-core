package com.krishagni.rbac.repository;

import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;

public interface DaoFactory {
	public ResourceDao getResourceDao();
	
	public OperationDao getOperationDao();
	
	public PermissionDao getPermissionDao();
	
	public RoleDao getRoleDao();
	
	public GroupDao getGroupDao();
	
	public SubjectDao getSubjectDao();
	
	public CollectionProtocolDao getCollectionProtocolDao();
	
	public SiteDao getSiteDao();
	
}
