package com.krishagni.openspecimen.custom.sgh.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegDetail;
import com.krishagni.openspecimen.custom.sgh.events.BulkParticipantRegSummary;
import com.krishagni.openspecimen.custom.sgh.services.CprService;

@Controller
@RequestMapping("/sgh/registrations")
public class BulkParticipantRegistrationController {
	@Autowired
	private CprService cprService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public BulkParticipantRegDetail registerParticipants(@RequestBody BulkParticipantRegSummary summary) {
		RequestEvent<BulkParticipantRegSummary> req = new RequestEvent<BulkParticipantRegSummary>(summary);
		ResponseEvent<BulkParticipantRegDetail> resp = cprService.registerParticipants(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
}
