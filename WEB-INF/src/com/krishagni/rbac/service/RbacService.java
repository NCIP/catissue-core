package com.krishagni.rbac.service;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.domain.SubjectAccess;
import com.krishagni.rbac.events.GroupDetail;
import com.krishagni.rbac.events.GroupRoleDetail;
import com.krishagni.rbac.events.OperationDetail;
import com.krishagni.rbac.events.PermissionDetail;
import com.krishagni.rbac.events.ResourceDetail;
import com.krishagni.rbac.events.RoleDetail;
import com.krishagni.rbac.events.SubjectRoleDetail;
import com.krishagni.rbac.events.SubjectRoleOp;
import com.krishagni.rbac.events.SubjectRolesList;
import com.krishagni.rbac.repository.OperationListCriteria;
import com.krishagni.rbac.repository.PermissionListCriteria;
import com.krishagni.rbac.repository.ResourceListCriteria;
import com.krishagni.rbac.repository.RoleListCriteria;

public interface RbacService {
	public ResponseEvent<List<ResourceDetail>> getResources(RequestEvent<ResourceListCriteria> req);
	
	public ResponseEvent<ResourceDetail> saveResource(RequestEvent<ResourceDetail> req);
	
	public ResponseEvent<ResourceDetail> deleteResource(RequestEvent<String> req);
		
	//
	// Operation APIs
	//
	
	public ResponseEvent<List<OperationDetail>> getOperations(RequestEvent<OperationListCriteria> req);
	
	public ResponseEvent<OperationDetail> saveOperation(RequestEvent<OperationDetail> req);
	
	public ResponseEvent<OperationDetail> deleteOperation(RequestEvent<String> req);
	
		
	//
	// Permission APIs
	//
	
	public ResponseEvent<List<PermissionDetail>> getPermissions(RequestEvent<PermissionListCriteria> req);
	
	public ResponseEvent<PermissionDetail> addPermission(RequestEvent<PermissionDetail> req);
	
	public ResponseEvent<PermissionDetail> deletePermission(RequestEvent<PermissionDetail> req);
	
	//
	// Role APIs
	//
	
	public ResponseEvent<List<RoleDetail>> getRoles(RequestEvent<RoleListCriteria> req);
	
	public ResponseEvent<RoleDetail> getRole(RequestEvent<Long> req);

	public ResponseEvent<RoleDetail> saveRole(RequestEvent<RoleDetail> req);
	
	public ResponseEvent<RoleDetail> updateRole(RequestEvent<RoleDetail> req);
	
	public ResponseEvent<RoleDetail> deleteRole(RequestEvent<Long> req);
	
	//
	// Subject - Group roles API's
	//
	
	public ResponseEvent<SubjectRoleDetail> updateSubjectRole(RequestEvent<SubjectRoleOp> req);
	
	public ResponseEvent<List<SubjectRoleDetail>> getSubjectRoles(RequestEvent<Long> req);
	
	public ResponseEvent<GroupDetail> updateGroupRoles(RequestEvent<GroupDetail> req);
		
	public ResponseEvent<List<GroupRoleDetail>> getGroupRoles(RequestEvent<Long> req);
	
	//
	// Bulk update subject roles
	//
	public ResponseEvent<SubjectRolesList> assignRoles(RequestEvent<SubjectRolesList> req);
	
	//
	// Intern API's can change without notice.
	//
	public boolean canUserPerformOp(Long userId, String resource, String[] operations);
	
	public List<SubjectAccess> getAccessList(Long userId, String resource, String[] operations);
	
	public void addSubjectRole(Site site, CollectionProtocol cp, User user, String[] roleNames);
	
	public void removeSubjectRole(Site site, CollectionProtocol cp, User user, String[] roleNames);

	public void removeCpRoles(Long cpId);
}
