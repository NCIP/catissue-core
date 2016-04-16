package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class ConsentImporter implements ObjectImporter<ConsentDetail, ConsentDetail> {
	
	private CollectionProtocolRegistrationService cprSvc;
	
	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	@Override
	public ResponseEvent<ConsentDetail> importObject(RequestEvent<ImportObjectDetail<ConsentDetail>> req) {
		try {
			ConsentDetail detail = req.getPayload().getObject();
			return cprSvc.saveConsents(new RequestEvent<>(detail));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
