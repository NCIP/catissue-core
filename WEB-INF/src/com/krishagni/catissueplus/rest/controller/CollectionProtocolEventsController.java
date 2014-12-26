package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpeListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCpeListEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;

@Controller
@RequestMapping("/collection-protocol-events")
public class CollectionProtocolEventsController {

	@Autowired
	private CollectionProtocolService cpSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<CollectionProtocolEventDetail> getEvents(
			@RequestParam(value = "cpId", required = true) Long cpId) {
		
		ReqCpeListEvent req = new ReqCpeListEvent();
		req.setCpId(cpId);
		
		CpeListEvent resp = cpSvc.getProtocolEvents(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getEvents();		
	}
}
