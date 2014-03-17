/**
 * 
 */
package com.krishagni.catissueplus.core.audit.events;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CreateAuditEvent extends ResponseEvent {

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

}
