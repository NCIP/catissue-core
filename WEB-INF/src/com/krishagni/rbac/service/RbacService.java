package com.krishagni.rbac.service;

import com.krishagni.rbac.events.AccessCheckedEvent;
import com.krishagni.rbac.events.AddPermissionEvent;
import com.krishagni.rbac.events.AllGroupRolesEvent;
import com.krishagni.rbac.events.AllOperationsEvent;
import com.krishagni.rbac.events.AllPermissionsEvent;
import com.krishagni.rbac.events.AllResourcesEvent;
import com.krishagni.rbac.events.AllRolesEvent;
import com.krishagni.rbac.events.AllSubjectRolesEvent;
import com.krishagni.rbac.events.CheckAccessEvent;
import com.krishagni.rbac.events.DeleteOperationEvent;
import com.krishagni.rbac.events.DeletePermissionEvent;
import com.krishagni.rbac.events.DeleteResourceEvent;
import com.krishagni.rbac.events.DeleteRoleEvent;
import com.krishagni.rbac.events.GroupRoleUpdatedEvent;
import com.krishagni.rbac.events.ListAllEvent;
import com.krishagni.rbac.events.OperationDeletedEvent;
import com.krishagni.rbac.events.OperationSavedEvent;
import com.krishagni.rbac.events.PermissionAddedEvent;
import com.krishagni.rbac.events.PermissionDeletedEvent;
import com.krishagni.rbac.events.ReqAllGroupRolesEvent;
import com.krishagni.rbac.events.ReqAllSubjectRolesEvent;
import com.krishagni.rbac.events.ResourceDeletedEvent;
import com.krishagni.rbac.events.ResourceSavedEvent;
import com.krishagni.rbac.events.RoleDeletedEvent;
import com.krishagni.rbac.events.RoleSavedEvent;
import com.krishagni.rbac.events.SaveOperationEvent;
import com.krishagni.rbac.events.SaveResourceEvent;
import com.krishagni.rbac.events.SaveRoleEvent;
import com.krishagni.rbac.events.SubjectRoleUpdatedEvent;
import com.krishagni.rbac.events.UpdateGroupRoleEvent;
import com.krishagni.rbac.events.UpdateSubjectRolesEvent;

public interface RbacService {
	public AllResourcesEvent getAllResources(ListAllEvent req);
	
	public ResourceSavedEvent saveResource(SaveResourceEvent req);
	
	public ResourceDeletedEvent deleteResource(DeleteResourceEvent req);
		
	//
	// Operation APIs
	//
	
	public AllOperationsEvent getAllOperations(ListAllEvent req);
	
	public OperationSavedEvent saveOperation(SaveOperationEvent req);
	
	public OperationDeletedEvent deleteOperation(DeleteOperationEvent req);
	
		
	//
	// Permission APIs
	//
	
	public AllPermissionsEvent getAllPermissions(ListAllEvent req);
	
	public PermissionAddedEvent addPermission(AddPermissionEvent req);
	
	public PermissionDeletedEvent deletePermission(DeletePermissionEvent req);
	
	//
	// Role APIs
	//
	
	public RoleSavedEvent saveRole(SaveRoleEvent req);
	
	public AllRolesEvent getAllRoles(ListAllEvent req);
	
	public RoleDeletedEvent deleteRole(DeleteRoleEvent req);
	
	//
	// Subject - Group roles API's
	//
	
	public SubjectRoleUpdatedEvent updateSubjectRoles(UpdateSubjectRolesEvent req);
	
	public AccessCheckedEvent checkAccess(CheckAccessEvent req);
	
	public AllSubjectRolesEvent getAllSubjectRoles(ReqAllSubjectRolesEvent req);
	
	public GroupRoleUpdatedEvent updateGroupRoles(UpdateGroupRoleEvent req);
		
	public AllGroupRolesEvent getAllGroupRoles(ReqAllGroupRolesEvent req);
}
