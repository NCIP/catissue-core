
package com.krishagni.catissueplus.core.audit.services;

import com.krishagni.catissueplus.core.audit.events.AuditReportCreatedEvent;
import com.krishagni.catissueplus.core.audit.events.CreateAuditReportEvent;
import com.krishagni.catissueplus.core.audit.events.GetUsersInfoEvent;
import com.krishagni.catissueplus.core.audit.events.GetOperationEvent;
import com.krishagni.catissueplus.core.audit.events.GetObjectNameEvent;

public interface AuditReportService {

	public AuditReportCreatedEvent getAuditReport(CreateAuditReportEvent auditReportEvent);

	public GetUsersInfoEvent getUserDetails();

	public GetObjectNameEvent getObjectTypes() ;

	public GetOperationEvent getEventsTypes();
}
