package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class CpeImporter implements ObjectImporter<CollectionProtocolEventDetail, CollectionProtocolEventDetail> {
	private CollectionProtocolService cpSvc;

	public void setCpSvc(CollectionProtocolService cpSvc) {
		this.cpSvc = cpSvc;
	}

	@Override
	public ResponseEvent<CollectionProtocolEventDetail> importObject(RequestEvent<ImportObjectDetail<CollectionProtocolEventDetail>> req) {
		try {
			ImportObjectDetail<CollectionProtocolEventDetail> detail = req.getPayload();
			RequestEvent<CollectionProtocolEventDetail> cpeReq = new RequestEvent<CollectionProtocolEventDetail>(detail.getObject());
			
			if (detail.isCreate()) {
				return cpSvc.addEvent(cpeReq);
			} else {
				return cpSvc.updateEvent(cpeReq);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
}