package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.QueryAuditLog;
import com.krishagni.catissueplus.core.de.repository.QueryAuditLogDao;

public class QueryAuditLogDaoImpl extends AbstractDao<QueryAuditLog> implements QueryAuditLogDao {

	private static final String FQN = QueryAuditLog.class.getName();
	
	private static final String GET_AUDIT_LOGS = FQN + ".getAuditLogs";
	
	private static final String GET_AUDIT_LOG = FQN + ".getAuditLog";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QueryAuditLog> getAuditLogs(Long savedQueryId, int startAt, int maxRecords) {
		Query query = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUDIT_LOGS)
				.setLong("queryId", savedQueryId)
				.setFirstResult(startAt);
		if (maxRecords > 0) {
			query.setMaxResults(maxRecords);			
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public QueryAuditLog getAuditLog(Long savedQueryId, Long auditLogId) {
		List<QueryAuditLog> auditLogs = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUDIT_LOG)
				.setLong("queryId", savedQueryId)
				.setLong("auditLogId", auditLogId)
				.list();
				
		return auditLogs == null || auditLogs.isEmpty() ? null : auditLogs.iterator().next();
	}
}
