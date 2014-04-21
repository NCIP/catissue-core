package com.krishagni.catissueplus.core.audit.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AuditCreatedEvent extends ResponseEvent {

	private AuditDetail auditDetail;

	/**
	 * @return the auditDetail
	 */
	public AuditDetail getAuditDetail() {
		return auditDetail;
	}

	/**
	 * @param auditDetail
	 *            the auditDetail to set
	 */
	public void setAuditDetail(AuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

	public static AuditCreatedEvent ok(AuditDetail auditDetail) {
		AuditCreatedEvent auditEvent = new AuditCreatedEvent();
		auditEvent.setAuditDetail(auditDetail);
		auditEvent.setStatus(EventStatus.OK);
		return auditEvent;
	}

	public static AuditCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AuditCreatedEvent resp = new AuditCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
