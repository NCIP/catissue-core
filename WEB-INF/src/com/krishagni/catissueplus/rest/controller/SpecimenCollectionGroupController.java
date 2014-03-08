
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
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
@RequestMapping("/ng/specimen-collection-groups")
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
	public List<SpecimenInfo> getSpecimensList(@PathVariable("id") Long scgId) {
		ReqSpecimenSummaryEvent req = new ReqSpecimenSummaryEvent();
		req.setSessionDataBean(getSession());
		req.setScgId(scgId);
		return specimenCollGroupService.getSpecimensList(req).getSpecimensInfo();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/forms")
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

	@RequestMapping(method = RequestMethod.GET, value="/{id}/forms/{formCtxtId}/records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormRecordSummary> getFormRecords(
			@PathVariable("id") Long scgId,
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

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);		
	}	
}
