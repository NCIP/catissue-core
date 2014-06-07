package com.krishagni.catissueplus.core.de.repository;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.domain.QueryAuditLog;
import com.krishagni.catissueplus.core.de.events.QueryAuditLogSummary;

public interface QueryAuditLogDao extends Dao<QueryAuditLog>{
	public Long getAuditLogsCount();
	
	public Long getAuditLogsCount(Date start, Date end);
	
	public List<QueryAuditLogSummary> getAuditLogs(int startAt, int maxRecords);
	
	public List<QueryAuditLogSummary> getAuditLogs(Date start, Date end, int startAt, int maxRecords);
	
	public List<QueryAuditLogSummary> getAuditLogs(Long queryId, Long runBy, int startAt, int maxRecords);
	
	public QueryAuditLog getAuditLog(Long auditLogId);
}
