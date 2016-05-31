package com.krishagni.rbac.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectAccess;
import com.krishagni.rbac.repository.SubjectDao;

public class SubjectDaoImpl extends AbstractDao<Subject> implements SubjectDao {
        
	@Override
	public Class<?> getType() {
		return Subject.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canUserPerformOps(Long subjectId, String resource, String[] ops) {
		List<Long> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ACCESS_LIST_COUNT)
				.setLong("subjectId", subjectId)
				.setString("resource", resource)
				.setParameterList("operations", ops)
				.list();
		
		return rows.isEmpty() ? false : (rows.iterator().next() > 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SubjectAccess> getAccessList(Long subjectId, String resource, String[] ops) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ACCESS_LIST)
				.setLong("subjectId", subjectId)
				.setString("resource", resource)
				.setParameterList("operations", ops)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubjectAccess> getAccessList(Long subjectId, String resource, String[] ops, String[] siteNames) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ACCESS_LIST_BY_SITES)
				.setLong("subjectId", subjectId)
				.setString("resource", resource)
				.setParameterList("operations", ops)
				.setParameterList("sites", siteNames)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override	
	public List<SubjectAccess> getAccessList(Long subjectId, Long cpId, String resource, String[] ops) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ACCESS_LIST_BY_CP)
				.setLong("subjectId", subjectId)
				.setLong("cpId", cpId)
				.setString("resource", resource)
				.setParameterList("operations", ops)
				.list();		
	}

	@Override
	public Integer removeRolesByCp(Long cpId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(REMOVE_ROLES_BY_CP)
				.setLong("cpId", cpId)
				.executeUpdate();
	}

	private static final String FQN = Subject.class.getName();

	private static final String GET_ACCESS_LIST = FQN + ".getAccessList";
	
	private static final String GET_ACCESS_LIST_BY_SITES = FQN + ".getAccessListBySites";
	
	private static final String GET_ACCESS_LIST_BY_CP = FQN + ".getAccessListByCp";

	private static final String GET_ACCESS_LIST_COUNT = FQN + ".getAccessListCount";
	
	private static final String REMOVE_ROLES_BY_CP = FQN + ".removeRolesByCp";
}
