package com.krishagni.rbac.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.repository.RoleDao;
import com.krishagni.rbac.repository.RoleListCriteria;

public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {
	private static final String FQN = Role.class.getName();
	
	private static final String GET_ALL_ROLES = FQN + ".getAllRoles";
	
	private static final String GET_ROLES_BY_NAMES = FQN + ".getRolesByNames";
	
	@Override
	public Role getRole(Long roleId) {
		return (Role) sessionFactory.getCurrentSession()
				.get(Role.class, roleId);
	}

	@Override
	public void deleteRole(Role role) {
		sessionFactory.getCurrentSession()
		.delete(role);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Role getRoleByName(String roleName) {
		List<Role> roles = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ROLES_BY_NAMES)
				.setParameterList("roleNames", Collections.singletonList(roleName))
				.list();
		return roles.isEmpty() ? null : roles.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Role> getRoles(RoleListCriteria listCriteria) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Role.class)
				.setFirstResult(listCriteria.startAt())
				.setMaxResults(listCriteria.maxResults());
		
		if (StringUtils.isNotBlank(listCriteria.query())) {
			query.add(Restrictions.ilike("name", listCriteria.query(), MatchMode.ANYWHERE));
		}
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRolesByName(List<String> roles) {
		return (List<Role>) sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ROLES_BY_NAMES)
				.setParameterList("roleNames", roles)
				.list();
	}
	
}
