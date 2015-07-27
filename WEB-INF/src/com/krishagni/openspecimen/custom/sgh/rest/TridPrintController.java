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
import com.krishagni.openspecimen.custom.sgh.events.BulkTridPrintSummary;
import com.krishagni.openspecimen.custom.sgh.services.TridPrintSvc;

@Controller
@RequestMapping("/sgh/trids")
public class TridPrintController {
	@Autowired
	private TridPrintSvc tridPrintSvc;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public Boolean generateAndPrintTrids(@RequestBody BulkTridPrintSummary summary) {
		RequestEvent<BulkTridPrintSummary> req = new RequestEvent<BulkTridPrintSummary>(summary);
		ResponseEvent<Boolean> resp = tridPrintSvc.generateAndPrintTrids(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

}
