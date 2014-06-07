package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryAuditLogsEvent extends ResponseEvent {	
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
	
	public static QueryAuditLogsEvent ok(List<QueryAuditLogSummary> auditLogs, Long count) {
		QueryAuditLogsEvent resp = new QueryAuditLogsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setAuditLogs(auditLogs);
		resp.setCount(count);
		return resp;
	}
	
	public static QueryAuditLogsEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static QueryAuditLogsEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	public static QueryAuditLogsEvent forbidden() {
		return errorResp(EventStatus.NOT_AUTHORIZED, null, null);
	}
	
	private static QueryAuditLogsEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryAuditLogsEvent resp = new QueryAuditLogsEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
