/**
 * 
 */
package com.krishagni.catissueplus.core.audit.services;

import com.krishagni.catissueplus.core.audit.events.AuditCreatedEvent;
import com.krishagni.catissueplus.core.audit.events.CreateAuditEvent;

public interface AuditService {

	public AuditCreatedEvent insertAuditDetails(
			CreateAuditEvent createAuditEvent);

}
