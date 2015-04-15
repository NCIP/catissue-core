package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class InstituteImporter implements ObjectImporter<InstituteDetail> {	
	private InstituteService instituteSvc;
	
	public void setInstituteSvc(InstituteService instituteSvc) {
		this.instituteSvc = instituteSvc;
	}

	@Override
	public ResponseEvent<InstituteDetail> importObject(RequestEvent<InstituteDetail> req) {
		try {
			return instituteSvc.createInstitute(req);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
