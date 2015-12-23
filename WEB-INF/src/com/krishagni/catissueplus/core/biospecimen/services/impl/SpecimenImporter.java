package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class SpecimenImporter implements ObjectImporter<SpecimenDetail, SpecimenDetail> {
	
	private SpecimenService specimenSvc;
	
	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}
	
	@Override
	public ResponseEvent<SpecimenDetail> importObject(RequestEvent<ImportObjectDetail<SpecimenDetail>> req) {
		try {
			ImportObjectDetail<SpecimenDetail> detail = req.getPayload();
			RequestEvent<SpecimenDetail> specReq = new RequestEvent<SpecimenDetail>(detail.getObject());
			
			if (detail.isCreate()) {
				return specimenSvc.createSpecimen(specReq);
			} else {
				return specimenSvc.updateSpecimen(specReq);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
}
