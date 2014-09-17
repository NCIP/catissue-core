package com.krishagni.rbac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.util.StringUtils;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.domain.Group;
import com.krishagni.rbac.domain.GroupRole;
import com.krishagni.rbac.domain.Operation;
import com.krishagni.rbac.domain.Permission;
import com.krishagni.rbac.domain.Resource;
import com.krishagni.rbac.domain.ResourceInstanceOp;
import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.domain.RoleAccessControl;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectRole;
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
import com.krishagni.rbac.events.GroupDetails;
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
import com.krishagni.rbac.events.ResourceInstanceOpDetails;
import com.krishagni.rbac.events.ResourceSavedEvent;
import com.krishagni.rbac.events.RoleAccessControlDetails;
import com.krishagni.rbac.events.RoleDeletedEvent;
import com.krishagni.rbac.events.RoleDetails;
import com.krishagni.rbac.events.RoleSavedEvent;
import com.krishagni.rbac.events.SaveOperationEvent;
import com.krishagni.rbac.events.SaveResourceEvent;
import com.krishagni.rbac.events.SaveRoleEvent;
import com.krishagni.rbac.events.SubjectDetails;
import com.krishagni.rbac.events.SubjectRoleSummary;
import com.krishagni.rbac.events.SubjectRoleUpdatedEvent;
import com.krishagni.rbac.events.UpdateGroupRoleEvent;
import com.krishagni.rbac.events.UpdateSubjectRolesEvent;
import com.krishagni.rbac.repository.DaoFactory;
import com.krishagni.rbac.service.RbacService;

public class RbacServiceImpl implements RbacService {
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public AllResourcesEvent getAllResources(ListAllEvent req) {
		try {
			return AllResourcesEvent.ok(ResourceDetails.fromResources(
					daoFactory.getResourceDao().getAllResources()));
		} catch(Exception e) { 
			return AllResourcesEvent.serverError(e);
		} 
	}
	
	@Override
	@PlusTransactional
	public ResourceSavedEvent saveResource(SaveResourceEvent req) {
		try {
			ResourceDetails detail = req.getResource();
			if (detail == null || StringUtils.isEmpty(detail.getName())) { 
				return ResourceSavedEvent.badRequest(RbacErrorCode.INVALID_RESOURCE_NAME, null);
			}
			
			String resourceName = detail.getName();
			Resource resource = daoFactory.getResourceDao().getResourceByName(resourceName);			
			if (resource == null) {
				resource = new Resource();
				resource.setName(resourceName);
				
				daoFactory.getResourceDao().saveOrUpdate(resource);				
			}
			
			return ResourceSavedEvent.ok(ResourceDetails.fromResource(resource));
		} catch (Exception e) {
			return ResourceSavedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResourceDeletedEvent deleteResource(DeleteResourceEvent req) {
		try {
			String resourceName = req.getResourceName();
			if (StringUtils.isEmpty(resourceName)) {
				return ResourceDeletedEvent.badRequest(RbacErrorCode.INVALID_RESOURCE_NAME, null);
			}
			
			Resource resource = daoFactory.getResourceDao().getResourceByName(resourceName);
			if (resource == null) {
				return ResourceDeletedEvent.notFound(resourceName);
			}
			
			daoFactory.getResourceDao().delete(resource);
			return ResourceDeletedEvent.ok(ResourceDetails.fromResource(resource));
		} catch (Exception e) {
			return ResourceDeletedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public AllOperationsEvent getAllOperations(ListAllEvent req) {
		try {
			return AllOperationsEvent.ok(OperationDetails.fromOperations(
					daoFactory.getOperationDao().getAllOperations()));
		} catch (Exception e) {
			return AllOperationsEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public OperationSavedEvent saveOperation(SaveOperationEvent req) {
		try {
			OperationDetails detail = req.getOperation();
			if (detail == null || StringUtils.isEmpty(detail.getName())) {
				return OperationSavedEvent.badRequest(RbacErrorCode.INVALID_OPERATION_NAME, null);
			}
			
			String operationName = detail.getName();
			Operation operation = daoFactory.getOperationDao().getOperationByName(operationName);			
			if (operation == null) {
				operation = new Operation();
				operation.setName(operationName);
				daoFactory.getOperationDao().saveOrUpdate(operation);				
			}
			
			return OperationSavedEvent.ok(OperationDetails.fromOperation(operation));
		} catch (Exception e) {
			return OperationSavedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public OperationDeletedEvent deleteOperation(DeleteOperationEvent req) {
		try {
			String operationName = req.getOperationName();
			if (StringUtils.isEmpty(operationName)) {
				return OperationDeletedEvent.badRequest(RbacErrorCode.INVALID_OPERATION_NAME, null);
			}
			
			Operation operation = daoFactory.getOperationDao().getOperationByName(operationName);
			if (operation == null) {
				return OperationDeletedEvent.notFound(operationName);
			}

			daoFactory.getOperationDao().delete(operation);
			return OperationDeletedEvent.ok(OperationDetails.fromOperation(operation));
		} catch(Exception e) { 
			return OperationDeletedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AllPermissionsEvent getAllPermissions(ListAllEvent req) {
		try {
			return AllPermissionsEvent.ok(PermissionDetails.fromPermissions(
					daoFactory.getPermissionDao().getAllPermissions()));
		} catch (Exception e) {
			return AllPermissionsEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public PermissionAddedEvent addPermission(AddPermissionEvent req) {
		try {
			PermissionDetails details = req.getPermission();
			if (details == null) {
				details = new PermissionDetails();
			}
			
			String resourceName = details.getResourceName();
			if (StringUtils.isEmpty(resourceName)) { 
				return PermissionAddedEvent.badRequest(RbacErrorCode.INVALID_RESOURCE_NAME, null);
			}
			
			String operationName = details.getOperationName();						
			if (StringUtils.isEmpty(operationName)) {
				return PermissionAddedEvent.badRequest(RbacErrorCode.INVALID_OPERATION_NAME, null);
			}
			
			Resource resource = daoFactory.getResourceDao().getResourceByName(resourceName);
			if (resource == null) {
				return PermissionAddedEvent.badRequest(RbacErrorCode.RESOURCE_NOT_FOUND, null);
			}
			
			Operation operation = daoFactory.getOperationDao().getOperationByName(operationName);
			if (operation == null) {
				return PermissionAddedEvent.badRequest(RbacErrorCode.OPERATION_NOT_FOUND, null);
			}
			
			Permission permission = daoFactory.getPermissionDao().getPermission(resourceName, operationName);
			if (permission != null) {
				return PermissionAddedEvent.badRequest(RbacErrorCode.DUPLICATE_PERMISSION, null);
			}
			
			permission = new Permission();
			permission.setResource(resource);
			permission.setOperation(operation);
			daoFactory.getPermissionDao().saveOrUpdate(permission);
			return PermissionAddedEvent.ok(PermissionDetails.fromPermission(permission));
		} catch (Exception e) {
			return PermissionAddedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public PermissionDeletedEvent deletePermission(DeletePermissionEvent req) {
		try {
			PermissionDetails detail = req.getPermission();
			if (detail == null) {
				detail = new PermissionDetails();
			}
			
			String resourceName = detail.getResourceName();
			if (StringUtils.isEmpty(resourceName)) { 
				return PermissionDeletedEvent.badRequest(RbacErrorCode.INVALID_RESOURCE_NAME, null);
			}
			
			String operationName = detail.getOperationName();						
			if (StringUtils.isEmpty(operationName)) {
				return PermissionDeletedEvent.badRequest(RbacErrorCode.INVALID_OPERATION_NAME, null);
			}
			
			Permission permission =  daoFactory.getPermissionDao().getPermission(resourceName, operationName);
			if (permission == null) {
				return PermissionDeletedEvent.badRequest(RbacErrorCode.PERMISSIONS_NOT_FOUND, null);
			}
			
			daoFactory.getPermissionDao().delete(permission);
			return PermissionDeletedEvent.ok(PermissionDetails.fromPermission(permission));
		} catch (Exception e) {
			return PermissionDeletedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AllRolesEvent getAllRoles(ListAllEvent req) {
		try {
			return AllRolesEvent.ok(RoleDetails.fromRoles(daoFactory.getRoleDao().getAllRoles()));
		} catch (Exception e) {
			return AllRolesEvent.serverError(e);
		}
	}
		
	@Override
	@PlusTransactional
	public RoleSavedEvent saveRole(SaveRoleEvent req) {
		try {
			RoleDetails details = req.getRole();
			if (details == null) {
				return RoleSavedEvent.badRequest(RbacErrorCode.INVALID_ROLE_NAME, null);
			}
			
			String roleName = details.getName();
			if (StringUtils.isEmpty(roleName)) {
				return RoleSavedEvent.badRequest(RbacErrorCode.INVALID_ROLE_NAME, null); 
			}
			
			Role newRole = createRole(req.getRole());
			
			Role existing = daoFactory.getRoleDao().getRoleByName(roleName);						
			if (existing == null) {
				existing = newRole;
			} else {
				//TODO - find a solution about this hack
				AbstractDao<?> dao = (AbstractDao<?>)daoFactory.getGroupDao();
				Session session = dao.getSessionFactory().getCurrentSession();
				
				existing.updateRole(newRole, session);
			}
			
			daoFactory.getRoleDao().saveOrUpdate(existing);
			return RoleSavedEvent.ok(RoleDetails.fromRole(existing));
		} catch (Exception e) {
			return RoleSavedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public RoleDeletedEvent deleteRole(DeleteRoleEvent req) {
		try {
			String roleName = req.getRoleName();
			if (StringUtils.isEmpty(roleName)) {
				return RoleDeletedEvent.badRequest(RbacErrorCode.INVALID_ROLE_NAME, null);
			}
			
			Role role = daoFactory.getRoleDao().getRoleByName(roleName);
			if (role == null) {
				return RoleDeletedEvent.badRequest(RbacErrorCode.ROLE_NOT_FOUND, null);
			}
			
			daoFactory.getRoleDao().delete(role);
			return RoleDeletedEvent.ok(RoleDetails.fromRole(role));
		} catch (Exception e) {
			return RoleDeletedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AllSubjectRolesEvent getAllSubjectRoles(ReqAllSubjectRolesEvent req) {
		try {
			Long subjectId = req.getSubjectId();
			if (subjectId == null) {
				return AllSubjectRolesEvent.badRequest(RbacErrorCode.INVALID_SUBJECT_DETAILS);
			}
			
			Subject subject = daoFactory.getSubjectDao().getSubject(subjectId);
			return AllSubjectRolesEvent.ok(SubjectDetails.fromSubject(subject));
		} catch (Exception e) {
			return AllSubjectRolesEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public SubjectRoleUpdatedEvent updateSubjectRoles(UpdateSubjectRolesEvent req) {
		try {
			Long subjectId = req.getSubjectId();
			if (subjectId == null) {
				return SubjectRoleUpdatedEvent.badRequest(RbacErrorCode.INVALID_SUBJECT_DETAILS, null);
			}
			
			List<SubjectRoleSummary> roles = req.getRoles();
			if (roles == null) {
				roles = new ArrayList<SubjectRoleSummary>();
			}

			Subject subject = daoFactory.getSubjectDao().getSubject(subjectId);
			if (subject == null) {
				return SubjectRoleUpdatedEvent.badRequest(RbacErrorCode.SUBJECT_NOT_FOUND, null);
			}
			
			List<SubjectRole> subjectRoles = new ArrayList<SubjectRole>();
			for (SubjectRoleSummary sd : roles) {
				SubjectRole sr = createSubjectRole(sd);
				sr.setSubject(subject);
				
				subjectRoles.add(sr);
			}
			
			//TODO - find a solution about this hack
			AbstractDao<?> dao = (AbstractDao<?>)daoFactory.getGroupDao();
			Session session = dao.getSessionFactory().getCurrentSession();

			subject.updateRoles(subjectRoles,session);			
			daoFactory.getSubjectDao().saveOrUpdate(subject);
			return SubjectRoleUpdatedEvent.ok(SubjectDetails.fromSubject(subject));
		} catch (IllegalArgumentException iae) {
			return SubjectRoleUpdatedEvent.badRequest(RbacErrorCode.INVALID_ROLE_DETAILS, iae);
		} catch (Exception e) {
			return SubjectRoleUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AccessCheckedEvent checkAccess(CheckAccessEvent req) {
		try {			
			Long subjectId = req.getSubjectId();
			Long groupId = req.getGroupId();			
			Long dsoId = req.getDsoId();
			String resourceName = req.getResourceName();
			String operationName = req.getOperationName();
			Long resourceInstanceId = req.getResourceInstanceId() == null ? -1 : req.getResourceInstanceId();
			
			if ((subjectId == null && groupId == null) || 
				StringUtils.isEmpty(resourceName) ||
				StringUtils.isEmpty(operationName) ||
				dsoId == null) {
				return AccessCheckedEvent.badRequest(RbacErrorCode.INVALID_USER_ACCESS_DETAILS);
			}
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("subjectId", subjectId);
			params.put("groupId", groupId);
			params.put("dsoId", dsoId);
			params.put("resource", resourceName);
			params.put("operation", operationName);
			params.put("resourceInstanceId", resourceInstanceId);
			
			if (daoFactory.getSubjectDao().canUserAccess(params)) {
				return AccessCheckedEvent.ok();
			}
			
			if (groupId != null && daoFactory.getGroupDao().canUserAccess(params)) {
				return AccessCheckedEvent.ok();
			}
			
			return AccessCheckedEvent.denied();
		} catch (Exception e) {
			return AccessCheckedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public AllGroupRolesEvent getAllGroupRoles(ReqAllGroupRolesEvent req) {
		try {
			Long groupId = req.getGroupId();
			if (groupId == null) {
				return AllGroupRolesEvent.badRequest(RbacErrorCode.INVALID_GROUP_DETAILS);
			}
			
			Group group = daoFactory.getGroupDao().getGroup(groupId);
			return AllGroupRolesEvent.ok(GroupDetails.fromGroup(group));
		} catch (Exception e) {
			return AllGroupRolesEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public  GroupRoleUpdatedEvent updateGroupRoles(UpdateGroupRoleEvent req) {
		try {
			Long groupId = req.getGroupId();
			if (groupId == null) {
				return GroupRoleUpdatedEvent.badRequest(RbacErrorCode.INVALID_GROUP_DETAILS, null);
			}
			
			List<GroupRoleSummary> roles = req.getRoles();
			if (roles == null) {
				roles = new ArrayList<GroupRoleSummary>();
			}
			
			Group group = daoFactory.getGroupDao().getGroup(groupId);
			if (group == null) {
				GroupRoleUpdatedEvent.badRequest(RbacErrorCode.GROUP_NOT_FOUND, null);
			}
			
			List<GroupRole> groupRoles = new ArrayList<GroupRole>();
			for (GroupRoleSummary gd : roles) {
				GroupRole gr = createGroupRole(gd);
				gr.setGroup(group);
				
				groupRoles.add(gr);
			}
			
			//TODO - find a solution about this hack
			AbstractDao<?> dao = (AbstractDao<?>)daoFactory.getGroupDao();
			Session session = dao.getSessionFactory().getCurrentSession();
			
			group.updateRoles(groupRoles, session);
			daoFactory.getGroupDao().saveOrUpdate(group);
			return GroupRoleUpdatedEvent.ok(GroupDetails.fromGroup(group));
		} catch (IllegalArgumentException iae) {
			return GroupRoleUpdatedEvent.badRequest(RbacErrorCode.INVALID_ROLE_DETAILS, iae);
		} catch (Exception e) {
			return GroupRoleUpdatedEvent.serverError(e);
		}
	}

	//
	// HELPER METHODS
	// 
	
	private Role createRole(RoleDetails details) {
		Role role = new Role();
		role.setName(details.getName());
		role.setDescription(details.getDescription());
		role.setAcl(getAcl(role, details.getAcl()));
		
		return role;
	}

	private Set<RoleAccessControl> getAcl(Role role, List<RoleAccessControlDetails> acl) {
		Set<RoleAccessControl> result = new HashSet<RoleAccessControl>();
		
		for (RoleAccessControlDetails rd : acl) {
			RoleAccessControl rac = new RoleAccessControl();
			rac.setRole(role);
			
			Resource resource = daoFactory.getResourceDao().getResourceByName(rd.getResourceName());
			if (resource == null) {
				throw new IllegalArgumentException("Invalid resource name");
			}
			
			rac.setResource(resource);				
			for (ResourceInstanceOpDetails riod  : rd.getOperations()) {
				Permission permission = daoFactory.getPermissionDao()
						.getPermission(resource.getName(), riod.getOperationName());
				if (permission == null) {
					throw new IllegalArgumentException("Invalid operation for resource");
				}
				
				ResourceInstanceOp op = new ResourceInstanceOp();
				op.setRoleAccessControl(rac);
				op.setResourceInstanceId(riod.getResourceInstanceId() == null ? -1L : riod.getResourceInstanceId());
				op.setOperation(permission.getOperation());
								
				rac.getOperations().add(op);
			}
			
			result.add(rac);
		}
		
		return result;
	}
	
	private SubjectRole createSubjectRole(SubjectRoleSummary sd) {
		if (StringUtils.isEmpty(sd.getRole())) {
			throw new IllegalArgumentException("Invalid role name");
		}
		
		Role role = daoFactory.getRoleDao().getRoleByName(sd.getRole());
		if (role == null) {
			throw new IllegalArgumentException("Invalid role name");
		}
		
		SubjectRole sr = new SubjectRole();
		sr.setDsoId(sd.getDsoId() == null ? -1L : sd.getDsoId());
		sr.setRole(role);
		return sr;
	}
	
	private GroupRole createGroupRole(GroupRoleSummary gd) {
		if (StringUtils.isEmpty(gd.getRole())) {
			throw new IllegalArgumentException("Invalid role name");
		}
		
		Role role = daoFactory.getRoleDao().getRoleByName(gd.getRole());
		if (role == null) {
			throw new IllegalArgumentException("Invalid role name");
		}
		
		GroupRole sr = new GroupRole();
		sr.setDsoId(gd.getDsoId() == null ? -1L : gd.getDsoId());
		sr.setRole(role);
		return sr;
	}
}