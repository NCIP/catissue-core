package com.krishagni.catissueplus.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.PrintVisitNameDetail;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/visit-name-printer")
public class VisitNamePrintController {
	@Autowired
	private VisitService visitSvc;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public LabelPrintJobSummary printName(@RequestBody PrintVisitNameDetail detail) {
		ResponseEvent<LabelPrintJobSummary> resp = visitSvc.printVisitNames(new RequestEvent<>(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
