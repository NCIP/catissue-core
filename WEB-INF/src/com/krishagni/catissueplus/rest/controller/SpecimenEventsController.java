package com.krishagni.catissueplus.rest.controller;

import java.util.Arrays;
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

import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenEventFormData;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventFormData;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventFormDataEvent;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenEventService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;


@Controller
@RequestMapping("/specimen-events")
public class SpecimenEventsController {
	
	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private SpecimenEventService specimenEventsSvc;
	
	@Autowired
	private FormService formSvc;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long specimenId) {
		ReqEntityFormsEvent req = new ReqEntityFormsEvent();
		req.setEntityId(specimenId);
		req.setEntityType(EntityType.SPECIMEN_EVENT);
		req.setSessionDataBean(getSession());

		EntityFormsEvent resp = formSvc.getEntityForms(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getForms();
		}

		return null;
	}
	
  	@RequestMapping(method = RequestMethod.GET, value = "{id}/data/{specimenLabels}")
  	@ResponseStatus(HttpStatus.OK)
  	@ResponseBody
  	public SpecimenEventFormData getSpecimenEventFormData(@PathVariable("id") Long formId,
  			@PathVariable("specimenLabels") String[] specimenLabels) {
  		ReqSpecimenEventFormData req = new ReqSpecimenEventFormData();
  		req.setFormId(formId);
  		req.setSpecimenLabels(Arrays.asList(specimenLabels));
  
  		SpecimenEventFormDataEvent resp = specimenEventsSvc.getSpecimenEventFormData(req);
  		if (resp.getStatus() == EventStatus.OK) {
  			return resp.getSpecimenEventFormData();
  		}
  		return null;
  	}
  	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}

}
