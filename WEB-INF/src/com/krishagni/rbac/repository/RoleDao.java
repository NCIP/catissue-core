package com.krishagni.rbac.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.domain.RoleAccessControl;

public interface RoleDao extends Dao<Role> {
	public List<Role> getRoles(RoleListCriteria listCriteria);
	
	public List<Role> getRolesByNames(List<String> roleNames);

	public Role getRoleByName(String roleName);
	
	public RoleAccessControl getRoleAccessControl(Long racId, Long roleId);
	
	public void deleteRole(Role role);
	
}
