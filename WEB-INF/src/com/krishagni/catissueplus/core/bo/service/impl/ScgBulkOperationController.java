package com.krishagni.catissueplus.core.bo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.bo.events.BulkOperationRequest;
import com.krishagni.catissueplus.core.bo.events.BulkOperationResponse;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationController;

@Configurable
public class ScgBulkOperationController implements BulkOperationController {
	@Autowired
	private SpecimenCollGroupService scgSvc;
	
	@Override
	public BulkOperationResponse saveObject(BulkOperationRequest req) {
		if (!(req.getObject() instanceof ScgDetail)) {
			throw new RuntimeException("ScgDetail object expected for this operation!");
		}
		
		CreateScgEvent event = new CreateScgEvent();
		event.setScgDetail((ScgDetail)req.getObject());
		event.setSessionDataBean(req.getSessionDataBean());
		
		ScgCreatedEvent response = scgSvc.createScg(event);
		return BulkOperationResponse.buildResponse(response, response.getDetail());
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
