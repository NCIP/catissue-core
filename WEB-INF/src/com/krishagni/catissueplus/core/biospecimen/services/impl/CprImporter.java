package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class CprImporter implements ObjectImporter<CollectionProtocolRegistrationDetail, CollectionProtocolRegistrationDetail> {
	
	private CollectionProtocolRegistrationService cprSvc;
	
	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	@Override
	public ResponseEvent<CollectionProtocolRegistrationDetail> importObject(RequestEvent<ImportObjectDetail<CollectionProtocolRegistrationDetail>> req) {
		try {
			ImportObjectDetail<CollectionProtocolRegistrationDetail> detail = req.getPayload();
			detail.getObject().setForceDelete(true);
			RequestEvent<CollectionProtocolRegistrationDetail> cprReq = new RequestEvent<>(detail.getObject());
			
			if (detail.isCreate()) {
				return cprSvc.createRegistration(cprReq);
			} else {
				return cprSvc.updateRegistration(cprReq);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
