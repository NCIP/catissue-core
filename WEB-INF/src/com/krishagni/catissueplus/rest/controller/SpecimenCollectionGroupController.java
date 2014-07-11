
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

import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
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
@RequestMapping("/specimen-collection-groups")
public class SpecimenCollectionGroupController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private SpecimenCollGroupService specimenCollGroupService;

	@Autowired
	private FormService formSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> getSpecimensList(@PathVariable("id") Long id,
			@RequestParam(value = "objectType", required = false, defaultValue = "") String objectType) {
		ReqSpecimenSummaryEvent req = new ReqSpecimenSummaryEvent();
		req.setSessionDataBean(getSession());
		req.setId(id);
		req.setObjectType(objectType);
		return specimenCollGroupService.getSpecimensList(req).getSpecimensInfo();
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
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getForms();
		}

		return null;
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
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFormRecords();
		}

		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScgDetail createSCG(@RequestBody ScgDetail scgDetail) {
		CreateScgEvent createScgEvent = new CreateScgEvent();
		createScgEvent.setScgDetail(scgDetail);
		createScgEvent.setSessionDataBean(getSession());
		ScgCreatedEvent scgCreated = specimenCollGroupService.createScg(createScgEvent);
		if (scgCreated.getStatus().equals(EventStatus.OK)) {
			return scgCreated.getDetail();
		}
		return null;

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScgDetail updateSCG(@PathVariable Long id, @RequestBody ScgDetail scgDetail) {
		UpdateScgEvent event = new UpdateScgEvent();
		event.setId(id);
		event.setScgDetail(scgDetail);
		event.setSessionDataBean(getSession());
		ScgUpdatedEvent response = specimenCollGroupService.updateScg(event);
		if (response.getStatus().equals(EventStatus.OK)) {
			return response.getDetail();
		}
		return null;

	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/reports")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScgReportDetail updateSCGReports(@PathVariable Long id, @RequestBody ScgReportDetail reportDetail) {
		UpdateScgReportEvent event = new UpdateScgReportEvent();
		event.setId(id);
		event.setDetail(reportDetail);
		event.setSessionDataBean(getSession());
		ScgReportUpdatedEvent response = specimenCollGroupService.updateScgReport(event);
		if (response.getStatus().equals(EventStatus.OK)) {
			return response.getDetail();
		}
		return null;

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/reports")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScgReportDetail getSCGWithReports(@PathVariable Long id) {
		GetScgReportEvent event = new GetScgReportEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		ScgReportUpdatedEvent response = specimenCollGroupService.getScgReport(event);
		if (response.getStatus().equals(EventStatus.OK)) {
			return response.getDetail();
		}
		return null;

	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
