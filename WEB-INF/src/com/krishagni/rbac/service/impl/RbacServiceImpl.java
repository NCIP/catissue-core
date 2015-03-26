package com.krishagni.rbac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
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
import com.krishagni.rbac.events.GroupDetail;
import com.krishagni.rbac.events.GroupRoleDetail;
import com.krishagni.rbac.events.OperationDetail;
import com.krishagni.rbac.events.PermissionDetail;
import com.krishagni.rbac.events.ResourceDetail;
import com.krishagni.rbac.events.ResourceInstanceOpDetails;
import com.krishagni.rbac.events.RoleAccessControlDetails;
import com.krishagni.rbac.events.RoleDetail;
import com.krishagni.rbac.events.SubjectDetail;
import com.krishagni.rbac.events.SubjectRoleDetail;
import com.krishagni.rbac.events.UserAccessInformation;
import com.krishagni.rbac.repository.DaoFactory;
import com.krishagni.rbac.repository.OperationListCriteria;
import com.krishagni.rbac.repository.PermissionListCriteria;
import com.krishagni.rbac.repository.ResourceListCriteria;
import com.krishagni.rbac.repository.RoleListCriteria;
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
	public ResponseEvent<List<ResourceDetail>> getResources(RequestEvent<ResourceListCriteria> req) {
		try {
			return ResponseEvent.response(ResourceDetail.from(
					daoFactory.getResourceDao().getResources(req.getPayload())));
		} catch(Exception e) { 
			return ResponseEvent.serverError(e);
		} 
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ResourceDetail> saveResource(RequestEvent<ResourceDetail> req) {
		try {
			ResourceDetail detail = req.getPayload();
			if (detail == null || StringUtils.isEmpty(detail.getName())) { 
				return ResponseEvent.userError(RbacErrorCode.RESOURCE_NAME_REQUIRED);
			}
			
			String resourceName = detail.getName();
			Resource resource = daoFactory.getResourceDao().getResourceByName(resourceName);			
			if (resource == null) {
				resource = new Resource();
				resource.setName(resourceName);
				
				daoFactory.getResourceDao().saveOrUpdate(resource);				
			}
			
			return ResponseEvent.response(ResourceDetail.from(resource));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ResourceDetail> deleteResource(RequestEvent<String> req) {
		try {
			String resourceName = req.getPayload();
			if (StringUtils.isEmpty(resourceName)) {
				return ResponseEvent.userError(RbacErrorCode.RESOURCE_NAME_REQUIRED);
			}
			
			Resource resource = daoFactory.getResourceDao().getResourceByName(resourceName);
			if (resource == null) {
				return ResponseEvent.userError(RbacErrorCode.RESOURCE_NOT_FOUND);
			}
			
			daoFactory.getResourceDao().delete(resource);
			return ResponseEvent.response(ResourceDetail.from(resource));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<OperationDetail>> getOperations(RequestEvent<OperationListCriteria> req) {
		try {
			return ResponseEvent.response(OperationDetail.from(
					daoFactory.getOperationDao().getOperations(req.getPayload())));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<OperationDetail> saveOperation(RequestEvent<OperationDetail> req) {
		try {
			OperationDetail detail = req.getPayload();
			String operationName = detail.getName();
			if (detail == null || StringUtils.isEmpty(operationName)) {
				return ResponseEvent.userError(RbacErrorCode.OPERATION_NAME_REQUIRED);
			}
		
			Operation operation = daoFactory.getOperationDao().getOperationByName(operationName);			
			if (operation == null) {
				operation = new Operation();
				operation.setName(operationName);
				daoFactory.getOperationDao().saveOrUpdate(operation);				
			}
			
			return ResponseEvent.response(OperationDetail.from(operation));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<OperationDetail> deleteOperation(RequestEvent<String> req) {
		try {
			String operationName = req.getPayload();
			if (StringUtils.isEmpty(operationName)) {
				return ResponseEvent.userError(RbacErrorCode.OPERATION_NAME_REQUIRED);
			}
			
			Operation operation = daoFactory.getOperationDao().getOperationByName(operationName);
			if (operation == null) {
				return ResponseEvent.userError(RbacErrorCode.OPERATION_NOT_FOUND);
			}

			daoFactory.getOperationDao().delete(operation);
			return ResponseEvent.response(OperationDetail.from(operation));
		} catch(Exception e) { 
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<PermissionDetail>> getPermissions(RequestEvent<PermissionListCriteria> req) {
		try {
			return ResponseEvent.response(PermissionDetail.from(
					daoFactory.getPermissionDao().getPermissions(req.getPayload())));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<PermissionDetail> addPermission(RequestEvent<PermissionDetail> req) {
		try {
			PermissionDetail details = req.getPayload();
			if (details == null) {
				details = new PermissionDetail();
			}
			
			String resourceName = details.getResourceName();
			if (StringUtils.isEmpty(resourceName)) { 
				return ResponseEvent.userError(RbacErrorCode.RESOURCE_NAME_REQUIRED);
			}
			
			String operationName = details.getOperationName();						
			if (StringUtils.isEmpty(operationName)) {
				return ResponseEvent.userError(RbacErrorCode.OPERATION_NAME_REQUIRED);
			}
			
			Resource resource = daoFactory.getResourceDao().getResourceByName(resourceName);
			if (resource == null) {
				return ResponseEvent.userError(RbacErrorCode.RESOURCE_NOT_FOUND);
			}
			
			Operation operation = daoFactory.getOperationDao().getOperationByName(operationName);
			if (operation == null) {
				return ResponseEvent.userError(RbacErrorCode.OPERATION_NOT_FOUND);
			}
			
			Permission permission = daoFactory.getPermissionDao().getPermission(resourceName, operationName);
			if (permission != null) {
				return ResponseEvent.userError(RbacErrorCode.DUPLICATE_PERMISSION);
			}
			
			permission = new Permission();
			permission.setResource(resource);
			permission.setOperation(operation);
			daoFactory.getPermissionDao().saveOrUpdate(permission);
			return ResponseEvent.response(PermissionDetail.from(permission));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<PermissionDetail> deletePermission(RequestEvent<PermissionDetail> req) {
		try {
			PermissionDetail detail = req.getPayload();
			if (detail == null) {
				detail = new PermissionDetail();
			}
			
			String resourceName = detail.getResourceName();
			if (StringUtils.isEmpty(resourceName)) { 
				return ResponseEvent.userError(RbacErrorCode.RESOURCE_NAME_REQUIRED);
			}
			
			String operationName = detail.getOperationName();						
			if (StringUtils.isEmpty(operationName)) {
				return ResponseEvent.userError(RbacErrorCode.OPERATION_NAME_REQUIRED);
			}
			
			Permission permission =  daoFactory.getPermissionDao().getPermission(resourceName, operationName);
			if (permission == null) {
				return ResponseEvent.userError(RbacErrorCode.PERMISSION_NOT_FOUND);
			}
			
			daoFactory.getPermissionDao().delete(permission);
			return ResponseEvent.response(PermissionDetail.from(permission));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<RoleDetail>> getRoles(RequestEvent<RoleListCriteria> req) {
		try {
			return ResponseEvent.response(RoleDetail.from(daoFactory.getRoleDao().getRoles(req.getPayload())));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<RoleDetail> getRole(RequestEvent<Long> req) {
		try {
			Role role = daoFactory.getRoleDao().getById(req.getPayload(), null);
			if (role == null) {
				return ResponseEvent.userError(RbacErrorCode.ROLE_NOT_FOUND);
			}
			
			return ResponseEvent.response(RoleDetail.from(role));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
		
	@Override
	@PlusTransactional
	public ResponseEvent<RoleDetail> saveRole(RequestEvent<RoleDetail> req) {
		try {
			RoleDetail detail = req.getPayload();
			Role role = createRole(detail);
			ensureUniqueName(role, null);
			checkCycles(role);
			for (Role childRole : role.getChildRoles()) {
				childRole.setParentRole(role);
			}
			
			daoFactory.getRoleDao().saveOrUpdate(role);
			return ResponseEvent.response(RoleDetail.from(role));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<RoleDetail> updateRole(RequestEvent<RoleDetail> request) {
		try {
			RoleDetail detail = request.getPayload();
		
			Role existing = daoFactory.getRoleDao().getById(detail.getId(), null);
			if (existing == null) {
				return ResponseEvent.userError(RbacErrorCode.ROLE_NOT_FOUND);
			}
		
			Role newRole = createRole(detail);
			ensureUniqueName(newRole, existing);
			checkCycles(newRole);
				
			existing.updateRole(newRole);
			daoFactory.getRoleDao().saveOrUpdate(existing);
			return ResponseEvent.response(RoleDetail.from(existing));
		} catch(OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch(Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<RoleDetail> deleteRole(RequestEvent<String> req) {
		try {
			String roleName = req.getPayload();
			if (StringUtils.isEmpty(roleName)) {
				return ResponseEvent.userError(RbacErrorCode.ROLE_NAME_REQUIRED);
			}
			
			Role role = daoFactory.getRoleDao().getRoleByName(roleName);
			if (role == null) {
				return ResponseEvent.userError(RbacErrorCode.ROLE_NOT_FOUND);
			}
			
			adjustParentChildRelationForRoleDeletion(role);
			daoFactory.getRoleDao().delete(role);
			return ResponseEvent.response(RoleDetail.from(role));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SubjectRoleDetail>> getSubjectRoles(RequestEvent<Long> req) {
		try {
			Long subjectId = req.getPayload();
			if (subjectId == null) {
				return ResponseEvent.userError(RbacErrorCode.SUBJECT_ID_REQUIRED);
			}
			
			Subject subject = daoFactory.getSubjectDao().getSubject(subjectId);
			if (subject == null) {
				return ResponseEvent.userError(RbacErrorCode.SUBJECT_NOT_FOUND);
			}
			
			return ResponseEvent.response(SubjectRoleDetail.from(subject.getRoles()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SubjectDetail> updateSubjectRoles(RequestEvent<SubjectDetail> req) {
		try {
			SubjectDetail detail = req.getPayload();
			Long subjectId = detail == null ? null : detail.getId();
			if (subjectId == null) {
				return ResponseEvent.userError(RbacErrorCode.SUBJECT_ID_REQUIRED);
			}
			
			List<SubjectRoleDetail> roles = detail.getRoles();
			if (roles == null) {
				roles = new ArrayList<SubjectRoleDetail>();
			}

			Subject subject = daoFactory.getSubjectDao().getSubject(subjectId);
			if (subject == null) {
				return ResponseEvent.userError(RbacErrorCode.SUBJECT_NOT_FOUND);
			}
			
			List<SubjectRole> subjectRoles = new ArrayList<SubjectRole>();
			for (SubjectRoleDetail sd : roles) {
				SubjectRole sr = createSubjectRole(sd);
				sr.setSubject(subject);
				
				subjectRoles.add(sr);
			}
			
			//TODO - find a solution about this hack
			AbstractDao<?> dao = (AbstractDao<?>)daoFactory.getGroupDao();
			Session session = dao.getSessionFactory().getCurrentSession();

			subject.updateRoles(subjectRoles,session);			
			daoFactory.getSubjectDao().saveOrUpdate(subject);
			return ResponseEvent.response(SubjectDetail.from(subject));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<GroupRoleDetail>> getGroupRoles(RequestEvent<Long> req) {
		try {
			Long groupId = req.getPayload();
			if (groupId == null) {
				return ResponseEvent.userError(RbacErrorCode.GROUP_ID_REQUIRED);
			}
			
			Group group = daoFactory.getGroupDao().getGroup(groupId);
			return ResponseEvent.response(GroupRoleDetail.from(group.getGroupRoles()));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public  ResponseEvent<GroupDetail> updateGroupRoles(RequestEvent<GroupDetail> req) {
		try {
			GroupDetail detail = req.getPayload();
			if (detail == null || detail.getId() == null) {
				return ResponseEvent.userError(RbacErrorCode.GROUP_ID_REQUIRED);
			}
			
			List<GroupRoleDetail> roles = detail.getRoles();
			if (roles == null) {
				roles = new ArrayList<GroupRoleDetail>();
			}
			
			Group group = daoFactory.getGroupDao().getGroup(detail.getId());
			if (group == null) {
				ResponseEvent.response(RbacErrorCode.GROUP_NOT_FOUND);
			}
			
			List<GroupRole> groupRoles = new ArrayList<GroupRole>();
			for (GroupRoleDetail gd : roles) {
				GroupRole gr = createGroupRole(gd);
				gr.setGroup(group);
				
				groupRoles.add(gr);
			}
			
			//TODO - find a solution about this hack
			AbstractDao<?> dao = (AbstractDao<?>)daoFactory.getGroupDao();
			Session session = dao.getSessionFactory().getCurrentSession();
			
			group.updateRoles(groupRoles, session);
			daoFactory.getGroupDao().saveOrUpdate(group);
			return ResponseEvent.response(GroupDetail.from(group));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	//
	// Internal Api's can change without notice
	//
	
	@Override
	@PlusTransactional
	public boolean checkAccess(Long subjectId, String resource, String operation, Long cpId, Long siteId, Long resourceInstanceId) {
		try {			
			UserAccessInformation accessInfo = new UserAccessInformation()
					.subjectId(subjectId)
					.resourceName(resource)
					.operationName(operation)
					.cpId(cpId)
					.siteId(siteId)
					.resourceInstanceId(resourceInstanceId);
			
			if ((accessInfo.subjectId() == null && accessInfo.groupId() == null) || 
				StringUtils.isEmpty(accessInfo.resourceName()) ||
				StringUtils.isEmpty(accessInfo.operationName()) ) {
				throw OpenSpecimenException.userError(RbacErrorCode.INSUFFICIENT_USER_DETAILS);
			}
			
			if (daoFactory.getSubjectDao().canUserAccess(accessInfo)) {
				return true;
			}
			
			if (accessInfo.groupId() != null && daoFactory.getGroupDao().canUserAccess(accessInfo)) {
				return true;
			}
			
			return false;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//
	// HELPER METHODS
	// 
	
	private Role createRole(RoleDetail detail) {
		Role role = new Role();
		setName(detail, role);
		role.setDescription(detail.getDescription());
		role.setAcl(getAcl(detail, role));
		role.setParentRole(getRole(detail.getParentRoleName()));
		
		for (String childRole : detail.getChildRoles()) {
			role.getChildRoles().add(getRole(childRole));
		}
		return role;
	}
	
	private void setName(RoleDetail detail, Role role) {
		if (StringUtils.isBlank(detail.getName())) {
			throw OpenSpecimenException.userError(RbacErrorCode.ROLE_NAME_REQUIRED);
		}
		role.setName(detail.getName());
	}
	
	private void checkCycles(Role role) {
		if (role.equals(role.getParentRole()) || role.getChildRoles().contains(role)) {
			throw OpenSpecimenException.userError(RbacErrorCode.CYCLE_DETECTED_IN_HIERARCHY);
		}
		
		Role parentRole = role.getParentRole();
		if (parentRole == null) {
			return;
		}
		
		for (Role childRole : role.getChildRoles()) {
			if (parentRole.isDescendentOf(childRole)) {
				throw OpenSpecimenException.userError(RbacErrorCode.CYCLE_DETECTED_IN_HIERARCHY);
			}
		}
	}
	
	private Role getRole(String roleName) {
		if (roleName == null) {
			return null;
		}
		
		Role role = daoFactory.getRoleDao().getRoleByName(roleName);
		
		if (role == null) {
			throw OpenSpecimenException.userError(RbacErrorCode.ROLE_NOT_FOUND);
		}
		
		return role;
	}
	
	private Set<RoleAccessControl> getAcl(RoleDetail detail, Role role) {
		Set<RoleAccessControl> result = new HashSet<RoleAccessControl>();
		Map<Long, RoleAccessControl> racMap = new HashMap<Long, RoleAccessControl>();
		
		if (detail.getId() != null) {
			Role existingRole  = daoFactory.getRoleDao().getById(detail.getId(), null);
			for (RoleAccessControl rac : existingRole.getAcl()) {
				racMap.put(rac.getId(), rac);
			}
		}
		
		for (RoleAccessControlDetails rd : detail.getAcl()) {
			RoleAccessControl rac = racMap.get(rd.getId());
			
			if(rac == null) {
				rac = new RoleAccessControl();
				rac.setRole(role);
			}
			
			Resource resource = daoFactory.getResourceDao().getResourceByName(rd.getResourceName());
			if (resource == null) {
				throw OpenSpecimenException.userError(RbacErrorCode.RESOURCE_NOT_FOUND);
			}
			rac.setResource(resource);
			rac.getOperations().clear(); 
			for (ResourceInstanceOpDetails riod  : rd.getOperations()) {
				Operation operation = daoFactory.getOperationDao().getOperationByName(riod.getOperationName());
				if (operation == null) {
					throw OpenSpecimenException.userError(RbacErrorCode.OPERATION_NOT_FOUND);
				}
				
				ResourceInstanceOp op = new ResourceInstanceOp();
				op.setRoleAccessControl(rac);
				op.setResourceInstanceId(riod.getResourceInstanceId());
				op.setOperation(operation);
								
				rac.getOperations().add(op);
			}
			
			result.add(rac);
		}
		
		return result;
	}
	
	private SubjectRole createSubjectRole(SubjectRoleDetail sd) {
		RoleDetail detail = sd.getRoleDetails();
		if (detail == null || StringUtils.isEmpty(detail.getName())) {
			throw OpenSpecimenException.userError(RbacErrorCode.ROLE_NAME_REQUIRED);
		}
		
		Role role = daoFactory.getRoleDao().getRoleByName(detail.getName());
		if (role == null) {
			throw OpenSpecimenException.userError(RbacErrorCode.ROLE_NOT_FOUND);
		}
		
		SubjectRole sr = new SubjectRole();
		sr.setCollectionProtocol(getCollectionProtocol(sd));
		sr.setSite(getSite(sd));
		sr.setRole(role);
		return sr;
	}

	private CollectionProtocol getCollectionProtocol(SubjectRoleDetail sd) {
		if (sd.getCollectionProtocol() == null) {
			/*
			 *  null cp means all cp's
			 */
			return null;
		}
		
		CollectionProtocol cp = null;
		Long cpId = sd.getCollectionProtocol().getId();
		String title = sd.getCollectionProtocol().getTitle();
		
		if (cpId != null) {
			cp = daoFactory.getCollectionProtocolDao().getById(cpId);
		} else if (StringUtils.isNotBlank(title)) {
			cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		}
		
		if (cp == null) {
			throw OpenSpecimenException.userError(CpErrorCode.NOT_FOUND);
		}
		
		return cp;
	}
	
	private Site getSite(SubjectRoleDetail sd) {
		if (sd.getSite() == null) {
			/*
			 * null site means all sites'
			 */
			return null;
		}
		
		Site site = null;
		Long siteId = sd.getSite().getId();
		String name = sd.getSite().getName();
		
		if (siteId != null) {
			site = daoFactory.getSiteDao().getById(siteId);
		} else if (StringUtils.isNotBlank(name)) {
			site = daoFactory.getSiteDao().getSiteByName(name);
		}
		
		if (site == null) {
			throw OpenSpecimenException.userError(SiteErrorCode.NOT_FOUND);
		}
		
		return site;
	}

	private void adjustParentChildRelationForRoleDeletion(Role role) {
		Role parent = role.getParentRole();
		Set<Role> childRoles = role.getChildRoles();
		
		for (Role childRole : childRoles) {
			childRole.setParentRole(parent);
		}
		
		if (parent != null) {
			parent.getChildRoles().remove(role);
			parent.getChildRoles().addAll(childRoles);
			daoFactory.getRoleDao().saveOrUpdate(parent);
		} else {
			for (Role childRole : childRoles) {
				daoFactory.getRoleDao().saveOrUpdate(childRole);
			}
		}
	}
	
	private GroupRole createGroupRole(GroupRoleDetail gd) {
		RoleDetail detail = gd.getRoleDetails();
		if (detail == null || StringUtils.isEmpty(detail.getName())) {
			throw OpenSpecimenException.userError(RbacErrorCode.ROLE_NAME_REQUIRED);
		}
		
		Role role = daoFactory.getRoleDao().getRoleByName(detail.getName());
		if (role == null) {
			throw OpenSpecimenException.userError(RbacErrorCode.ROLE_NOT_FOUND);
		}
		
		GroupRole sr = new GroupRole();
		sr.setDsoId(gd.getDsoId() == null ? -1L : gd.getDsoId());
		sr.setRole(role);
		return sr;
	}
	
	private void ensureUniqueName(Role newRole, Role existingRole) {
		if (existingRole != null && existingRole.getName().equals(newRole.getName())) {
			return;
		}
		
		Role role = daoFactory.getRoleDao().getRoleByName(newRole.getName());
		if (role != null) {
			throw OpenSpecimenException.userError(RbacErrorCode.DUP_ROLE_NAME);
		}
	}

}
