package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class QueryAuditLogsList {	
	private Long count;
	
	private List<QueryAuditLogSummary> auditLogs;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<QueryAuditLogSummary> getAuditLogs() {
		return auditLogs;
	}

	public void setAuditLogs(List<QueryAuditLogSummary> auditLogs) {
		this.auditLogs = auditLogs;
	}
	
	public static QueryAuditLogsList create(List<QueryAuditLogSummary> auditLogs, Long count) {
		QueryAuditLogsList resp = new QueryAuditLogsList();
		resp.setAuditLogs(auditLogs);
		resp.setCount(count);
		return resp;
	}
}
