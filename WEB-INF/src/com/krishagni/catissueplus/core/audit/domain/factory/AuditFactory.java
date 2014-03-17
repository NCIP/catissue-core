/**
 * 
 */
package com.krishagni.catissueplus.core.audit.domain.factory;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.audit.events.AuditDetail;

/**
 * @author vinod
 * 
 */
public interface AuditFactory {

	public Audit getAudit(AuditDetail auditDetails);

}
