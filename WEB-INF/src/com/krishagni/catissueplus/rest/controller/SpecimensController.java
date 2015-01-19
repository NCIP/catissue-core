
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.CollectSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensCollectedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;
import com.krishagni.catissueplus.core.printer.printService.services.PrintService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/specimens")
public class SpecimensController {

	@Autowired
	private SpecimenService specimenSvc;
	
	@Autowired
	private CollectionProtocolRegistrationService cprSvc;
	
	@Autowired
	private FormService formSvc;

	@Autowired
	private PrintService specimenLabelPrintSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@RequestMapping(method = RequestMethod.HEAD)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public Boolean doesSpecimenExists(@RequestParam(value = "label") String label) {
		Boolean exists = specimenSvc.doesSpecimenExists(label);
		if (!exists) {
			ResponseEvent resp = new ResponseEvent();
			resp.setStatus(EventStatus.NOT_FOUND);
			resp.raiseException();
		}
		
		return exists;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<SpecimenDetail> getSpecimens(
			@RequestParam(value = "cprId") Long cprId,
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam(value = "visitId", required = false) Long visitId) {
		
		ReqVisitSpecimensEvent req = new ReqVisitSpecimensEvent();
		req.setCprId(cprId);
		req.setEventId(eventId);
		req.setVisitId(visitId);
		req.setSessionDataBean(getSession());
		
		VisitSpecimensEvent resp = cprSvc.getSpecimens(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getSpecimens();
	}


	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long specimenId) {
		ReqEntityFormsEvent req = new ReqEntityFormsEvent();
		req.setEntityId(specimenId);
		req.setEntityType(EntityType.SPECIMEN);
		req.setSessionDataBean(getSession());

		EntityFormsEvent resp = formSvc.getEntityForms(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getForms();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms/{formCtxtId}/records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormRecordSummary> getFormRecords(@PathVariable("id") Long specimenId,
			@PathVariable("formCtxtId") Long formCtxtId) {

		ReqEntityFormRecordsEvent req = new ReqEntityFormRecordsEvent();
		req.setEntityId(specimenId);
		req.setFormCtxtId(formCtxtId);
		req.setSessionDataBean(getSession());

		EntityFormRecordsEvent resp = formSvc.getEntityFormRecords(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getFormRecords();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<SpecimenDetail> collectSpecimens(@RequestBody List<SpecimenDetail> specimens) {		
		CollectSpecimensEvent req = new CollectSpecimensEvent();
		req.setSessionDataBean(getSession());
		req.setSpecimens(specimens);
		
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getSpecimens();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
