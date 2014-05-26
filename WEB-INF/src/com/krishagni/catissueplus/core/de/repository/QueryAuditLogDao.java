package com.krishagni.catissueplus.core.de.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.domain.QueryAuditLog;

public interface QueryAuditLogDao extends Dao<QueryAuditLog>{
	
	public List<QueryAuditLog> getAuditLogs(Long queryId, int startAt, int maxRecords);
	
	public QueryAuditLog getAuditLog(Long queryId, Long auditLogId);
}
