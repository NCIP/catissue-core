
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

import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.EntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;
import com.krishagni.catissueplus.events.specimens.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.events.specimens.SpecimenInfo;
//import com.krishagni.catissueplus.service.SpecimenService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/ng/specimens")
public class SpecimenController {

//	@Autowired
//	private SpecimenService specimenService; // TODO: this needs to be merged with below one
	 
	@Autowired
	private SpecimenService specimenSvc;
	
	@Autowired
	private FormService formSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/child-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> getchildSpecimenList(@PathVariable("id") Long parentId) {
		ReqSpecimenSummaryEvent event = new ReqSpecimenSummaryEvent();
		event.setParentId(parentId);
		event.setSessionDataBean((SessionDataBean)httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		//return specimenService.getSpecimensList(event).getSpecimensInfo();
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long specimenId) {
		ReqEntityFormsEvent req = new ReqEntityFormsEvent();
		req.setEntityId(specimenId);
		req.setEntityType(EntityType.SPECIMEN);
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
			@PathVariable("id") Long specimenId,
			@PathVariable("formCtxtId") Long formCtxtId) {
		
		ReqEntityFormRecordsEvent req = new ReqEntityFormRecordsEvent();
		req.setEntityId(specimenId);
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
