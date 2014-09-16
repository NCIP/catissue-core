
package com.krishagni.catissueplus.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionGroupsDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensInfoEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;
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
@RequestMapping("/specimen-collection-groups")
public class SpecimenCollectionGroupController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private SpecimenCollGroupService specimenCollGroupService;

	@Autowired
	private FormService formSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAllCollGroups(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "100") int max,
			@RequestParam(value = "countReq", required = false, defaultValue = "false") boolean countReq,
			@RequestParam(value = "searchString", required = false, defaultValue = "") String searchString){
		ReqAllScgEvent req = new ReqAllScgEvent();
		req.setStartAt(start);
		req.setMaxRecords(max);
		req.setCountReq(countReq);
		req.setSearchString(searchString);
		req.setSessionDataBean(getSession());
		AllCollectionGroupsDetailEvent resp = specimenCollGroupService.getAllCollectionGroups(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (resp.getCount() != null) {
			result.put("count", resp.getCount());
		}
		result.put("collectionGroups", resp.getCollectionGroupsDetail());
		return result;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> getSpecimensList(@PathVariable("id") Long id,
			@RequestParam(value = "objectType", required = false, defaultValue = "") String objectType) {
		ReqSpecimenSummaryEvent req = new ReqSpecimenSummaryEvent();
		req.setSessionDataBean(getSession());
		req.setId(id);
		req.setObjectType(objectType);
		SpecimensInfoEvent resp = specimenCollGroupService.getSpecimensList(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getInfo(); 
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
	public ScgDetail createSCG(@RequestBody ScgDetail scgDetail) {
		CreateScgEvent createScgEvent = new CreateScgEvent();
		createScgEvent.setScgDetail(scgDetail);
		createScgEvent.setSessionDataBean(getSession());
		ScgCreatedEvent resp = specimenCollGroupService.createScg(createScgEvent);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetail();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScgDetail updateSCG(@PathVariable Long id, @RequestBody ScgDetail scgDetail) {
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
