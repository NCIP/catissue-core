package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.rbac.events.AddPermissionEvent;
import com.krishagni.rbac.events.AllGroupRolesEvent;
import com.krishagni.rbac.events.AllOperationsEvent;
import com.krishagni.rbac.events.AllPermissionsEvent;
import com.krishagni.rbac.events.AllResourcesEvent;
import com.krishagni.rbac.events.AllRolesEvent;
import com.krishagni.rbac.events.AllSubjectRolesEvent;
import com.krishagni.rbac.events.DeleteOperationEvent;
import com.krishagni.rbac.events.DeletePermissionEvent;
import com.krishagni.rbac.events.DeleteResourceEvent;
import com.krishagni.rbac.events.DeleteRoleEvent;
import com.krishagni.rbac.events.GroupRoleDetails;
import com.krishagni.rbac.events.GroupRoleSummary;
import com.krishagni.rbac.events.GroupRoleUpdatedEvent;
import com.krishagni.rbac.events.ListAllEvent;
import com.krishagni.rbac.events.OperationDeletedEvent;
import com.krishagni.rbac.events.OperationDetails;
import com.krishagni.rbac.events.OperationSavedEvent;
import com.krishagni.rbac.events.PermissionAddedEvent;
import com.krishagni.rbac.events.PermissionDeletedEvent;
import com.krishagni.rbac.events.PermissionDetails;
import com.krishagni.rbac.events.ReqAllGroupRolesEvent;
import com.krishagni.rbac.events.ReqAllSubjectRolesEvent;
import com.krishagni.rbac.events.ResourceDeletedEvent;
import com.krishagni.rbac.events.ResourceDetails;
import com.krishagni.rbac.events.ResourceSavedEvent;
import com.krishagni.rbac.events.RoleDeletedEvent;
import com.krishagni.rbac.events.RoleDetails;
import com.krishagni.rbac.events.RoleSavedEvent;
import com.krishagni.rbac.events.SaveOperationEvent;
import com.krishagni.rbac.events.SaveResourceEvent;
import com.krishagni.rbac.events.SaveRoleEvent;
import com.krishagni.rbac.events.SubjectRoleDetails;
import com.krishagni.rbac.events.SubjectRoleSummary;
import com.krishagni.rbac.events.SubjectRoleUpdatedEvent;
import com.krishagni.rbac.events.UpdateGroupRoleEvent;
import com.krishagni.rbac.events.UpdateSubjectRolesEvent;
import com.krishagni.rbac.service.RbacService;

//@Controller
@RequestMapping("/rbac")
public class RbacController {

	@Autowired
	private RbacService rbacSvc;
	
	//
	// - Resource APIs
	//
	
	@RequestMapping(method = RequestMethod.GET, value="/resources")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ResourceDetails> getAllResources() {
		ListAllEvent req = new ListAllEvent();
		
		AllResourcesEvent res = rbacSvc.getAllResources(req);
		if (res.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return res.getResources();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/resources")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResourceDetails createResource(@RequestBody ResourceDetails resourceDetails) {
		SaveResourceEvent req = new SaveResourceEvent();
		req.setResource(resourceDetails);

		ResourceSavedEvent resp = rbacSvc.saveResource(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getResource();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/resources/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResourceDetails deleteResource(@PathVariable("name") String resourceName) {
		DeleteResourceEvent req = new DeleteResourceEvent();
		req.setResourceName(resourceName);
		
		ResourceDeletedEvent resp = rbacSvc.deleteResource(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getResource();
	}
		
	//
	// - Operation APIs
	//

	@RequestMapping(method = RequestMethod.GET, value="/operations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<OperationDetails> getAllOperations() { 
		ListAllEvent req = new ListAllEvent();
		AllOperationsEvent resp = rbacSvc.getAllOperations(req);
		
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getOperations();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/operations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public OperationDetails addOperation(@RequestBody OperationDetails operation) {
		SaveOperationEvent req = new SaveOperationEvent();
		req.setOperation(operation);
		
		OperationSavedEvent resp = rbacSvc.saveOperation(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getOperation();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/operations/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public OperationDetails deleteOperation(@PathVariable("name") String operationName) {
		DeleteOperationEvent req = new DeleteOperationEvent();
		req.setOperationName(operationName);
		
		OperationDeletedEvent resp = rbacSvc.deleteOperation(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getOperation();
	}
	
	
	//
	// - Permission APIs
	//

	@RequestMapping(method = RequestMethod.GET, value="/permissions")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<PermissionDetails> getAllPermissions() {
		ListAllEvent req = new ListAllEvent();
		
		AllPermissionsEvent resp = rbacSvc.getAllPermissions(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getPermissions();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/permissions")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PermissionDetails addPermission(@RequestBody PermissionDetails permissionDetails) {
		AddPermissionEvent req = new AddPermissionEvent();
		req.setPermission(permissionDetails);
		
		PermissionAddedEvent resp = rbacSvc.addPermission(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getPermission();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/permissions")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PermissionDetails deletePermission(
			@RequestParam(value = "resource", required = true) String resource,
			@RequestParam(value = "operation", required = true) String operation) {
		
		PermissionDetails detail = new PermissionDetails();
		detail.setResourceName(resource);
		detail.setOperationName(operation);
		
		DeletePermissionEvent req = new DeletePermissionEvent();
		req.setPermissionDetails(detail);
		
		PermissionDeletedEvent resp = rbacSvc.deletePermission(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getPermissionDetails();
	}
	
	
	//
	// - Role APIs
	//

	@RequestMapping(method = RequestMethod.GET, value="/roles")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<RoleDetails> getAllRoles() {
		ListAllEvent req = new ListAllEvent();

		AllRolesEvent resp = rbacSvc.getAllRoles(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getRoles();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/roles")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public RoleDetails createRole(@RequestBody RoleDetails roleDetails) {
		SaveRoleEvent req = new SaveRoleEvent();
		req.setRole(roleDetails);
		RoleSavedEvent resp = rbacSvc.saveRole(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getRole();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/roles/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public RoleDetails createRole(@PathVariable("name") String roleName, @RequestBody RoleDetails roleDetails) {
		roleDetails.setName(roleName);
		
		SaveRoleEvent req = new SaveRoleEvent();
		req.setRole(roleDetails);
		RoleSavedEvent resp = rbacSvc.saveRole(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getRole();
	}
		
	@RequestMapping(method = RequestMethod.DELETE, value="/roles/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody 
	public RoleDetails deleteRole(@PathVariable("name") String roleName) {
		DeleteRoleEvent req = new DeleteRoleEvent();
		req.setRoleName(roleName);

		RoleDeletedEvent resp = rbacSvc.deleteRole(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getRole();
	}
	

	//
	// - Subject APIs
	//	
	@RequestMapping(method = RequestMethod.PUT, value="/subjects/{subjectId}/roles")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SubjectRoleDetails> updateSubjectRole(
			@PathVariable Long subjectId, 
			@RequestBody List<SubjectRoleSummary> roles) {
		
		UpdateSubjectRolesEvent req = new UpdateSubjectRolesEvent();
		req.setSubjectId(subjectId);
		req.setRoles(roles);
		
		SubjectRoleUpdatedEvent resp = rbacSvc.updateSubjectRoles(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getSubject().getRoles();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/subjects/{subjectId}/roles")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SubjectRoleDetails> getAllSubjectRoles(@PathVariable Long subjectId) {
		ReqAllSubjectRolesEvent req = new ReqAllSubjectRolesEvent();
		req.setSubjectId(subjectId);
		
		AllSubjectRolesEvent resp = rbacSvc.getAllSubjectRoles(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getSubject().getRoles();
	}
		
	//
	// - Group APIs
	//
	@RequestMapping(method = RequestMethod.PUT, value="/groups/{groupId}/roles")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<GroupRoleDetails> updateGroupRole(
			@PathVariable Long groupId,
			@RequestBody List<GroupRoleSummary> roles) {
		
		UpdateGroupRoleEvent req = new UpdateGroupRoleEvent();
		req.setGroupId(groupId);
		req.setRoles(roles);
		
		GroupRoleUpdatedEvent resp = rbacSvc.updateGroupRoles(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getGroupDetails().getRoles();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/groups/{groupId}/roles")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<GroupRoleDetails> getAllGroupRoles(@PathVariable Long groupId) {
		ReqAllGroupRolesEvent req = new ReqAllGroupRolesEvent();
		req.setGroupId(groupId);
		
		AllGroupRolesEvent resp = rbacSvc.getAllGroupRoles(req);
		if (resp.getStatus() != EventStatus.OK) {
			return null;
		}
		
		return resp.getGroup().getRoles();
	}
}
