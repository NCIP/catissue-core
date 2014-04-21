/**
 * 
 */

package com.krishagni.catissueplus.core.audit.domain.factory.impl;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.audit.domain.factory.AuditFactory;
import com.krishagni.catissueplus.core.audit.events.AuditDetail;

public class AuditFactoryImpl implements AuditFactory {

	@Override
	public Audit getAudit(AuditDetail auditDetails) {
		Audit audit = new Audit();
		audit.setObjectId(auditDetails.getObjectId());
		audit.setObjectType(auditDetails.getObjectType());
		audit.setOperation(auditDetails.getOperation());
		audit.setUpdatedDate(auditDetails.getUpdatedDate());
		audit.setReasonForChange(auditDetails.getReasonForChange());
		audit.setIpAddress(auditDetails.getIpAddress());
		audit.setUserId(auditDetails.getUserId());
		return audit;
	}
}
