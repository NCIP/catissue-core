package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.QueryAuditLog;

public class QueryAuditLogDetail extends QueryAuditLogSummary {
	
	private String sql;
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public static QueryAuditLogDetail from(QueryAuditLog auditLog){
		QueryAuditLogDetail detail = new QueryAuditLogDetail();
		
		detail.setId(auditLog.getId());
		detail.setRunBy(UserSummary.from(auditLog.getRunBy()));
		detail.setTimeOfExecution(auditLog.getTimeOfExecution());
		detail.setTimeToFinish(auditLog.getTimeToFinish());
		detail.setRunType(auditLog.getRunType());
		detail.setRecordCount(auditLog.getRecordCount());
		detail.setSql(auditLog.getSql());		
		return detail;
	}	
}
