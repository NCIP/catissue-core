package com.krishagni.catissueplus.bulkoperator.services.impl;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationsList;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CprObjectImporter implements ObjectImporter<ParticipantRegistrationsList> {
	CollectionProtocolRegistrationService cprSvc;
	
	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	@Override
	public ResponseEvent<ParticipantRegistrationsList> importObject(RequestEvent<ParticipantRegistrationsList> req) {
		return cprSvc.createBulkRegistration(req);
	}
}