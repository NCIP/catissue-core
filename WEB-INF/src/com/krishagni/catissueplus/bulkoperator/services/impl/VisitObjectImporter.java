package com.krishagni.catissueplus.bulkoperator.services.impl;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class VisitObjectImporter implements ObjectImporter<VisitDetail> {
	private VisitService visitService;
	
	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}

	@Override
	public ResponseEvent<VisitDetail> importObject(RequestEvent<VisitDetail> req) {
		return visitService.addOrUpdateVisit(req);
	}
}