package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class VisitImporter implements ObjectImporter<VisitDetail, VisitDetail> {

	private VisitService visitSvc;
	
	public void setVisitSvc(VisitService visitSvc) {
		this.visitSvc = visitSvc;
	}
	
	@Override
	public ResponseEvent<VisitDetail> importObject(RequestEvent<ImportObjectDetail<VisitDetail>> req) {
		try {
			ImportObjectDetail<VisitDetail> detail = req.getPayload();
			RequestEvent<VisitDetail> visitReq = new RequestEvent<VisitDetail>(detail.getObject());
			
			if (detail.isCreate()) {
				return visitSvc.addVisit(visitReq);
			} else {
				return visitSvc.patchVisit(visitReq);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
