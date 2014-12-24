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

import com.krishagni.catissueplus.core.biospecimen.events.ClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;

@Controller
@RequestMapping("/clinical-diagnoses")
public class ClinicalDiagnosesController {
	
	@Autowired
	private CollectionProtocolService cpSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<String> getClinicalDiagnoses(
			@RequestParam(value = "cpId", required = false, defaultValue = "-1") Long cpId,
			@RequestParam(value = "searchTerm", required = false, defaultValue = "") String searchTerm) {
		ReqClinicalDiagnosesEvent req = new ReqClinicalDiagnosesEvent();
		req.setCpId(cpId);
		req.setSearchTerm(searchTerm);
		
		ClinicalDiagnosesEvent resp = cpSvc.getDiagnoses(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getDiagnoses();
	}
}
