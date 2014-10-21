package com.krishagni.catissueplus.core.bo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.bo.events.BulkOperationRequest;
import com.krishagni.catissueplus.core.bo.events.BulkOperationResponse;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationController;

@Configurable
public class SpecimenBulkOperationController implements BulkOperationController {
	@Autowired
	private SpecimenService spmSvc;
	
	@Override
	public BulkOperationResponse saveObject(BulkOperationRequest req) {
		if (!(req.getObject() instanceof SpecimenDetail)) {
			throw new RuntimeException("SpecimenDetail Object expected for this operation!");
		}
		
		CreateSpecimenEvent event = new CreateSpecimenEvent();
		event.setSpecimenDetail((SpecimenDetail)req.getObject());
		event.setSessionDataBean(req.getSessionDataBean());
		
		SpecimenCreatedEvent response = spmSvc.createSpecimen(event);
		return BulkOperationResponse.buildResponse(response, response.getSpecimenDetail());
	}

	@Override
	public BulkOperationResponse updateObject(BulkOperationRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BulkOperationResponse deleteObject(BulkOperationRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
