package com.krishagni.rbac.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Permission;
import com.krishagni.rbac.repository.PermissionDao;
import com.krishagni.rbac.repository.PermissionListCriteria;

public class PermissionDaoImpl extends AbstractDao<Permission> implements PermissionDao  {
	private static final String FQN = Permission.class.getName();
	
	private static final String GET_PERM_BY_RESOURCE_OPERATION = FQN + ".getPermissionByResourceAndOperation";
	
	private static final String GET_ALL_PERMISSIONS = FQN + ".getAllPermissions";
	
	@Override
	public Permission getPermission(Long permissionId) {
		return (Permission)sessionFactory.getCurrentSession()
				.get(Permission.class, permissionId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Permission getPermission(String resourceName, String operationName) {
		List<Permission> perms = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_PERM_BY_RESOURCE_OPERATION)
				.setString("resourceName", resourceName)
				.setString("operationName", operationName)
				.list();
		
		return perms.isEmpty() ? null : perms.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getPermissions(PermissionListCriteria listCriteria) {
		return sessionFactory.getCurrentSession().createCriteria(Permission.class)
				.setFirstResult(listCriteria.startAt())
				.setMaxResults(listCriteria.maxResults())
				.list();
	}
}
