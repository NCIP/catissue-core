package com.krishagni.catissueplus.core.bo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.biospecimen.events.BulkRegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateBulkRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.bo.events.BulkOperationRequest;
import com.krishagni.catissueplus.core.bo.events.BulkOperationResponse;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationController;

@Configurable
public class CprBulkOperationController implements BulkOperationController {
	@Autowired
	private CollectionProtocolRegistrationService cprSvc;

	@Override
	public BulkOperationResponse saveObject(BulkOperationRequest req) {
		if (!(req.getObject() instanceof ParticipantRegistrationDetails)) {
			throw new RuntimeException("CollectionProtocolRegistrationDetail detail expected for this operation!");
		}
		
		CreateBulkRegistrationEvent request = new CreateBulkRegistrationEvent();
		request.setParticipantDetails((ParticipantRegistrationDetails)req.getObject());
		request.setSessionDataBean(req.getSessionDataBean());
		
		BulkRegistrationCreatedEvent response = cprSvc.createBulkRegistration(request);		
		return BulkOperationResponse.buildResponse(response, response.getParticipantDetails());
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