package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.AddSpecimenRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenRequirementsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementsEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;

@Controller
@RequestMapping("specimen-requirements")
public class SpecimenRequirementsController {

	@Autowired
	private CollectionProtocolService cpSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenRequirementDetail> getRequirements(
			@RequestParam(value = "eventId", required = true) Long eventId) {
		
		ReqSpecimenRequirementsEvent req = new ReqSpecimenRequirementsEvent();
		req.setCpeId(eventId);
		
		SpecimenRequirementsEvent resp = cpSvc.getSpecimenRequirments(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getSpecimenRequirements();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenRequirementDetail addRequirement(@RequestBody SpecimenRequirementDetail requirement) {
		AddSpecimenRequirementEvent req = new AddSpecimenRequirementEvent();
		req.setRequirement(requirement);
		
		SpecimenRequirementAddedEvent resp = cpSvc.addSpecimenRequirement(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getRequirement();
	}
}
