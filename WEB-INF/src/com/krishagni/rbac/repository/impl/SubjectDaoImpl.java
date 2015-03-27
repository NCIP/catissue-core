package com.krishagni.rbac.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.events.CpSiteInfo;
import com.krishagni.rbac.events.UserAccessCriteria;
import com.krishagni.rbac.repository.SubjectDao;

public class SubjectDaoImpl extends AbstractDao<Subject> implements SubjectDao {
	@Override
	public Subject getSubject(Long subjectId) {
		return (Subject) sessionFactory.getCurrentSession()
				.get(Subject.class, subjectId);
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
}
