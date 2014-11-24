package com.krishagni.catissueplus.bulkoperator.services.impl;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.bulkoperator.events.ImportObjectEvent;
import com.krishagni.catissueplus.bulkoperator.events.ObjectImportedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;

public class ScgObjectImporter implements ObjectImporter {
	private SpecimenCollGroupService scgSvc;
	
	public void setScgSvc(SpecimenCollGroupService scgSvc) {
		this.scgSvc = scgSvc;
	}

	@Override
	public ObjectImportedEvent importObject(ImportObjectEvent req) {
		if (!(req.getObject() instanceof ScgDetail)) {
			throw new RuntimeException("ScgDetail object expected for this operation!");
		}

		CreateScgEvent request = new CreateScgEvent();
		request.setScgDetail((ScgDetail)req.getObject());
		request.setSessionDataBean(req.getSessionDataBean());
		
		ScgCreatedEvent response = scgSvc.createScg(request);
		return ObjectImportedEvent.buildResponse(response, response.getDetail());
	}
}