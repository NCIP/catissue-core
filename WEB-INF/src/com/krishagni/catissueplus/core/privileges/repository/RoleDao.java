
package com.krishagni.catissueplus.core.privileges.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.privileges.domain.Role;

public interface RoleDao extends Dao<Role> {

	Role getRoleByName(String name);

	Role getRole(Long roleId);

	List<Role> getAllRoles();

}
