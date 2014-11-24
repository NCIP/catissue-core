package com.krishagni.catissueplus.bulkoperator.services.impl;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.bulkoperator.events.ImportObjectEvent;
import com.krishagni.catissueplus.bulkoperator.events.ObjectImportedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.BulkRegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateBulkRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;

public class CprObjectImporter implements ObjectImporter {
	CollectionProtocolRegistrationService cprSvc;
	
	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	@Override
	public ObjectImportedEvent importObject(ImportObjectEvent req) {
		if (!(req.getObject() instanceof ParticipantRegistrationDetails)) {
			throw new RuntimeException("CollectionProtocolRegistrationDetail detail expected for this operation!");
		}
		
		CreateBulkRegistrationEvent request = new CreateBulkRegistrationEvent();
		request.setParticipantDetails((ParticipantRegistrationDetails)req.getObject());
		request.setSessionDataBean(req.getSessionDataBean());
		
		BulkRegistrationCreatedEvent response = cprSvc.createBulkRegistration(request);		
		return ObjectImportedEvent.buildResponse(response, response.getParticipantDetails());
	}
}