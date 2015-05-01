package com.krishagni.rbac.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.repository.RoleDao;
import com.krishagni.rbac.repository.RoleListCriteria;

public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Role> getRoles(RoleListCriteria listCriteria) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(Role.class)
				.setFirstResult(listCriteria.startAt())
				.setMaxResults(listCriteria.maxResults())
				.addOrder(Order.asc("name"));
		
		if (StringUtils.isNotBlank(listCriteria.query())) {
			query.add(Restrictions.ilike("name", listCriteria.query(), MatchMode.ANYWHERE));
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRolesByNames(List<String> roleNames) {
		return (List<Role>) sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ROLES_BY_NAMES)
				.setParameterList("roleNames", roleNames)
				.list();
	}
	
	@Override
	public Role getRoleByName(String roleName) {
		List<Role> roles = getRolesByNames(Collections.singletonList(roleName));
		return roles.isEmpty() ? null : roles.get(0);
	}
	
	@Override
	public Class<?> getType() {
		return Role.class;
	}
	
	private static final String FQN = Role.class.getName();
	
	private static final String GET_ROLES_BY_NAMES = FQN + ".getRolesByNames";

}
