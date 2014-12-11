
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

import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.events.VisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.de.events.EntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/visits")
public class VisitsController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private SpecimenCollGroupService specimenCollGroupService;

	@Autowired
	private CollectionProtocolRegistrationService cprSvc;
	
	@Autowired
	private FormService formSvc;
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitSummary> getVisits(
			@RequestParam(value = "cprId", required = true) Long cprId,
			@RequestParam(value = "includeStats", required = false, defaultValue = "false") boolean includeStats) {
		
		ReqVisitsEvent req = new ReqVisitsEvent();
		req.setCprId(cprId);
		req.setIncludeStats(includeStats);
		req.setSessionDataBean(getSession());
				
		VisitsEvent resp = cprSvc.getVisits(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getVisits();
	}
		
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long scgId) {
		ReqEntityFormsEvent req = new ReqEntityFormsEvent();
		req.setEntityId(scgId);
		req.setEntityType(EntityType.SPECIMEN_COLLECTION_GROUP);
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
	public List<FormRecordSummary> getFormRecords(@PathVariable("id") Long scgId,
			@PathVariable("formCtxtId") Long formCtxtId) {

		ReqEntityFormRecordsEvent req = new ReqEntityFormRecordsEvent();
		req.setEntityId(scgId);
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
	public VisitDetail createSCG(@RequestBody VisitDetail scgDetail) {
		AddVisitEvent createScgEvent = new AddVisitEvent();
		createScgEvent.setVisit(scgDetail);
		createScgEvent.setSessionDataBean(getSession());
		VisitAddedEvent resp = specimenCollGroupService.createScg(createScgEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getVisit();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail updateSCG(@PathVariable Long id, @RequestBody VisitDetail scgDetail) {
		UpdateScgEvent event = new UpdateScgEvent();
		event.setId(id);
		event.setScgDetail(scgDetail);
		event.setSessionDataBean(getSession());
		ScgUpdatedEvent resp = specimenCollGroupService.updateScg(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetail();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/reports")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScgReportDetail updateSCGReports(@PathVariable Long id, @RequestBody ScgReportDetail reportDetail) {
		UpdateScgReportEvent event = new UpdateScgReportEvent();
		event.setId(id);
		event.setDetail(reportDetail);
		event.setSessionDataBean(getSession());
		ScgReportUpdatedEvent resp = specimenCollGroupService.updateScgReport(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetail();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/reports")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScgReportDetail getSCGWithReports(@PathVariable Long id) {
		GetScgReportEvent event = new GetScgReportEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		ScgReportUpdatedEvent resp = specimenCollGroupService.getScgReport(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetail();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
