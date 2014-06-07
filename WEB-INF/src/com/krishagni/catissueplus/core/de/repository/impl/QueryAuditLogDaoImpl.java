package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.QueryAuditLog;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogSummary;
import com.krishagni.catissueplus.core.de.repository.QueryAuditLogDao;

public class QueryAuditLogDaoImpl extends AbstractDao<QueryAuditLog> implements QueryAuditLogDao {

	private static final String FQN = QueryAuditLog.class.getName();
	
	private static final String GET_AUDIT_LOGS_COUNT = FQN + ".getAuditLogsCount";
	
	private static final String GET_AUDIT_LOGS_BTWN_COUNT = FQN + ".getAuditLogsBtwnCount";
	
	private static final String GET_AUDIT_LOGS = FQN + ".getAuditLogs";
	
	private static final String GET_AUDIT_LOGS_BTWN = FQN + ".getAuditLogsBtwn";
	
	private static final String GET_AUDIT_LOGS_BY_QUERY_N_USER = FQN + ".getAuditLogsByQueryAndUser";
	
	private static final String GET_AUDIT_LOG = FQN + ".getAuditLog";
	
	@Override
	public Long getAuditLogsCount() {
		return ((Number)sessionFactory.getCurrentSession()
			.getNamedQuery(GET_AUDIT_LOGS_COUNT)
			.uniqueResult()).longValue();
	}
	
	@Override
	public Long getAuditLogsCount(Date intervalSt, Date intervalEnd) {
		return ((Number)sessionFactory.getCurrentSession()
			.getNamedQuery(GET_AUDIT_LOGS_BTWN_COUNT)
			.setTimestamp("intervalSt", intervalSt)
			.setTimestamp("intervalEnd", intervalEnd)
			.uniqueResult()).longValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QueryAuditLogSummary> getAuditLogs(int startAt, int maxRecords) {
		Query query = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUDIT_LOGS)
				.setFirstResult(startAt);
		
		if (maxRecords > 0) {
			query.setMaxResults(maxRecords);
		}
		
		return getAllLogs(query.list());		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QueryAuditLogSummary> getAuditLogs(Date start, Date end, int startAt, int maxRecords) {
		Query query = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUDIT_LOGS_BTWN)
				.setTimestamp("intervalSt", start)
				.setTimestamp("intervalEnd", end)
				.setFirstResult(startAt);
		
		if (maxRecords > 0) {
			query.setMaxResults(maxRecords);
		}
		
		return getAllLogs(query.list());
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QueryAuditLogSummary> getAuditLogs(Long savedQueryId, Long runBy, int startAt, int maxRecords) {
		Query query = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUDIT_LOGS_BY_QUERY_N_USER)
				.setLong("queryId", savedQueryId)
				.setLong("userId", runBy)
				.setFirstResult(startAt);
		
		if (maxRecords > 0) {
			query.setMaxResults(maxRecords);			
		}
		
		List<QueryAuditLogSummary> result = new ArrayList<QueryAuditLogSummary>();
		
		List<Object[]> rows = query.list();
		for (Object[] row : rows) {
			QueryAuditLogSummary summary = new QueryAuditLogSummary();
			summary.setId((Long)row[0]);
			summary.setTimeOfExecution((Date)row[1]);
			summary.setTimeToFinish((Long)row[2]);
			summary.setRunType((String)row[3]);
			
			result.add(summary);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public QueryAuditLog getAuditLog(Long auditLogId) {
		List<QueryAuditLog> auditLogs = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_AUDIT_LOG)
				.setLong("auditLogId", auditLogId)
				.list();
				
		return auditLogs == null || auditLogs.isEmpty() ? null : auditLogs.iterator().next();
	}

	private List<QueryAuditLogSummary> getAllLogs(List<Object[]> rows) {
		List<QueryAuditLogSummary> result = new ArrayList<QueryAuditLogSummary>();
		
		for (Object[] row : rows) {
			QueryAuditLogSummary summary = new QueryAuditLogSummary();
			summary.setId((Long)row[0]);
			
			UserSummary user = new UserSummary();
			user.setId((Long)row[1]);
			user.setFirstName((String)row[2]);
			user.setLastName((String)row[3]);			
			summary.setRunBy(user);
			
			summary.setQueryId((Long)row[4]);
			summary.setQueryTitle((String)row[5]);
			summary.setTimeOfExecution((Date)row[6]);
			summary.setTimeToFinish((Long)row[7]);
			summary.setRunType((String)row[8]);			
			result.add(summary);
		}
		
		return result;		
	}

}
