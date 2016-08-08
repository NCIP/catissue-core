package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationsList;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class ExistingParticipantCprImporter implements ObjectImporter<ParticipantRegistrationsList, ParticipantRegistrationsList> {

	private CollectionProtocolRegistrationService cprSvc;

	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	@Override
	public ResponseEvent<ParticipantRegistrationsList> importObject(RequestEvent<ImportObjectDetail<ParticipantRegistrationsList>> req) {
		try {
			ImportObjectDetail<ParticipantRegistrationsList> detail = req.getPayload();
			if (detail.isCreate()) {
				return cprSvc.registerToOtherProtocols(new RequestEvent<>(detail.getObject()));
			}

			return null;
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
