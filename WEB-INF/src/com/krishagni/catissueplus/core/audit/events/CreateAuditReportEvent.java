
package com.krishagni.catissueplus.core.audit.events;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CreateAuditReportEvent extends ResponseEvent {

	private AuditReportDetail auditReportDetail;

	public AuditReportDetail getAuditReportDetail() {
		return auditReportDetail;
	}

	public void setAuditReportDetail(AuditReportDetail auditReportDetail) {
		this.auditReportDetail = auditReportDetail;
	}
}
