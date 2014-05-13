
package com.krishagni.catissueplus.core.privileges.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.repository.RoleDao;

public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Role getRoleByName(String roleName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ROLE_BY_NAME);
		query.setString("roleName", roleName);
		List<Role> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public Role getRole(Long roleId) {
		return (Role) sessionFactory.getCurrentSession().get(Role.class, roleId);
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<Role> getAllRoles() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_ROLES);
		return query.list();
		//return result.isEmpty() ? null : result.get(0);
	}

	private static final String FQN = Role.class.getName();

	private static final String GET_ROLE_BY_NAME = FQN + ".getRoleByName";

	private static final String GET_ALL_ROLES = FQN + ".getAllRoles";

}
