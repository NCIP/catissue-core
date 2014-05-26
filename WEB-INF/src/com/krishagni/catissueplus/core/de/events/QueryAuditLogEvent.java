package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryAuditLogEvent extends ResponseEvent {
	
	private QueryAuditLogDetail auditLog;
	
	public QueryAuditLogDetail getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(QueryAuditLogDetail auditLog) {
		this.auditLog = auditLog;
	}

	public static QueryAuditLogEvent ok(QueryAuditLogDetail auditLog) {
		QueryAuditLogEvent resp = new QueryAuditLogEvent();
		resp.setStatus(EventStatus.OK);
		resp.setAuditLog(auditLog);
		return resp;
	}
	
	public static QueryAuditLogEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static QueryAuditLogEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryAuditLogEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryAuditLogEvent resp = new QueryAuditLogEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
