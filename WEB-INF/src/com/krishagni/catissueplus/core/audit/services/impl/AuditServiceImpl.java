/**
 * 
 */

package com.krishagni.catissueplus.core.audit.services.impl;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.audit.domain.factory.AuditFactory;
import com.krishagni.catissueplus.core.audit.events.AuditCreatedEvent;
import com.krishagni.catissueplus.core.audit.events.CreateAuditEvent;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;

public class AuditServiceImpl implements AuditService {

	private DaoFactory daoFactory;

	private AuditFactory auditFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/**
	 * @param auditFactory
	 *            the auditFactory to set
	 */
	public void setAuditFactory(AuditFactory auditFactory) {
		this.auditFactory = auditFactory;
	}

	@Override
	@PlusTransactional
	public AuditCreatedEvent insertAuditDetails(CreateAuditEvent createAuditEvent) {
		try {
			Audit audit = auditFactory.getAudit(createAuditEvent.getAuditDetail());
			daoFactory.getAuditDao().saveOrUpdate(audit);
			return AuditCreatedEvent.ok(createAuditEvent.getAuditDetail());
		}
		catch (Exception e) {
			return AuditCreatedEvent.serverError(e);
		}
	}
}
