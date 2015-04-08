package com.krishagni.rbac.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectAccess;
import com.krishagni.rbac.events.CpSiteInfo;
import com.krishagni.rbac.events.UserAccessCriteria;
import com.krishagni.rbac.repository.SubjectDao;

public class SubjectDaoImpl extends AbstractDao<Subject> implements SubjectDao {
        
	@Override
	public Class<?> getType() {
		return Subject.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CpSiteInfo> getCpSiteForOpExecution(UserAccessCriteria uac) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CP_SITE_FOR_OP_EXEC)
				.setString("resource", uac.resource())
				.setString("operation", uac.operation())
				.setLong("userId", uac.subjectId())
				.list();
		
		return getCpSites(rows);
	}
	
	@Override
	public boolean canUserPerformOp(UserAccessCriteria crit) {
		Query query = null;
		
		if (CollectionUtils.isNotEmpty(crit.sites())) {
			query = sessionFactory.getCurrentSession()
					.getNamedQuery(CAN_USER_PERFORM_OP_ON_CP_SITE)
					.setParameterList("siteIds", crit.sites());
		} else {
			query = sessionFactory.getCurrentSession()
					.getNamedQuery(CAN_USER_PERFORM_OP_ON_CP);
		}
		
		query.setString("resource", crit.resource())
				.setString("operation", crit.operation())
				.setLong("subjectId", crit.subjectId())
				.setLong("cpId", crit.cpId() == null ? -1L : crit.cpId());
		
		return CollectionUtils.isNotEmpty(query.list()); 
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
	
	
	/*
	 * Private methods
	 */
	
	private List<CpSiteInfo> getCpSites(List<Object[]> rows) {
		List<CpSiteInfo> list = new ArrayList<CpSiteInfo>();
		for (Object[] row : rows) {
			list.add(new CpSiteInfo((Long)row[0], (Long)row[1]));
		}
		
		return list;
	}

        
	
	private static final String FQN = Subject.class.getName();

	private static final String GET_CP_SITE_FOR_OP_EXEC = FQN + ".getCpSiteForOpExec";

	private static final String CAN_USER_PERFORM_OP_ON_CP = FQN + ".canUserPerformOpOnCp";
	
	private static final String CAN_USER_PERFORM_OP_ON_CP_SITE = FQN + ".canUserPerformOpOnCpSite";

	private static final String GET_ACCESS_LIST = FQN + ".getAccessList";
	
	private static final String GET_ACCESS_LIST_COUNT = FQN + ".getAccessListCount";
}
