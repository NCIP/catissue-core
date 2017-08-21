package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class InstituteImporter implements ObjectImporter<InstituteDetail, InstituteDetail> {
	private InstituteService instituteSvc;
	
	public void setInstituteSvc(InstituteService instituteSvc) {
		this.instituteSvc = instituteSvc;
	}

	@Override
	public ResponseEvent<InstituteDetail> importObject(RequestEvent<ImportObjectDetail<InstituteDetail>> req) {
		try {
			ImportObjectDetail<InstituteDetail> detail = req.getPayload();
			RequestEvent<InstituteDetail> instituteReq = new RequestEvent<InstituteDetail>(detail.getObject());
			if (detail.isCreate()) {
				return instituteSvc.createInstitute(instituteReq);
			} else {
				return instituteSvc.updateInstitute(instituteReq);
			}			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
