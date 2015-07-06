package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenAliquotsSpec;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class SpecimenAliquotImporter implements ObjectImporter<SpecimenAliquotsSpec, List<SpecimenDetail>> {
	
	private SpecimenService specimenSvc;
	
	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}
	
	@Override
	public ResponseEvent<List<SpecimenDetail>> importObject(RequestEvent<ImportObjectDetail<SpecimenAliquotsSpec>> req) {
		try {
			ImportObjectDetail<SpecimenAliquotsSpec> detail = req.getPayload();
			RequestEvent<SpecimenAliquotsSpec> specReq = new RequestEvent<SpecimenAliquotsSpec>(detail.getObject());			
			return specimenSvc.createAliquots(specReq);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
}