
package com.krishagni.catissueplus.core.audit.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AuditReportCreatedEvent extends ResponseEvent {

	private String auditReportData;

	public static AuditReportCreatedEvent ok(String reportData) {
		AuditReportCreatedEvent auditReportCreatedEvent = new AuditReportCreatedEvent();
		auditReportCreatedEvent.setReportData(reportData);
		auditReportCreatedEvent.setStatus(EventStatus.OK);
		return auditReportCreatedEvent;
	}

	public static AuditReportCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AuditReportCreatedEvent resp = new AuditReportCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public void setReportData(String auditReport) {
		this.auditReportData = auditReport;

	}

	public String getReportData() {
		return this.auditReportData;

	}

}
