package com.krishagni.catissueplus.bulkoperator.services.impl;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.bulkoperator.events.ImportObjectEvent;
import com.krishagni.catissueplus.bulkoperator.events.ObjectImportedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;

public class ScgObjectImporter implements ObjectImporter {
	private SpecimenCollGroupService scgSvc;
	
	public void setScgSvc(SpecimenCollGroupService scgSvc) {
		this.scgSvc = scgSvc;
	}

	@Override
	public ObjectImportedEvent importObject(ImportObjectEvent req) {
		if (!(req.getObject() instanceof VisitDetail)) {
			throw new RuntimeException("ScgDetail object expected for this operation!");
		}

		AddVisitEvent request = new AddVisitEvent();
		request.setVisit((VisitDetail)req.getObject());
		request.setSessionDataBean(req.getSessionDataBean());
		
		VisitAddedEvent response = scgSvc.createScg(request);
		return ObjectImportedEvent.buildResponse(response, response.getVisit());
	}
}