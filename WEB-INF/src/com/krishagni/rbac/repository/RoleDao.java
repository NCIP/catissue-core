package com.krishagni.rbac.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Role;

public interface RoleDao extends Dao<Role> {
	public Role getRoleByName(String roleName);
	
	public List<Role> getRolesByName(List<String> roles);
	
	public Role getRole(Long roleId); 
	
	public void deleteRole(Role role);
	
	public List<Role> getAllRoles();
}
