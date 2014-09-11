package com.krishagni.catissueplus.rest.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.krishagni.catissueplus.core.biospecimen.events.SaveSpecimenEventsDataEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventsSavedEvent;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenEventService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.napi.FormData;
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

	@RequestMapping(method = RequestMethod.POST, value = "{id}/data")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String saveSpecimenEvents(@PathVariable("id") Long formId, @RequestBody String specimenEventsFormData) {
		try {
			specimenEventsFormData = URLDecoder.decode(specimenEventsFormData, "UTF-8");
			if (specimenEventsFormData.endsWith("=")) {
				specimenEventsFormData = specimenEventsFormData.substring(0, specimenEventsFormData.length() - 1);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error parsing input JSON", e);
		}
		
		List<FormData> formDataList = new ArrayList<FormData>();
		JsonArray records = new JsonParser().parse(specimenEventsFormData).getAsJsonArray();
		for (int i = 0; i < records.size(); i++) {
			String formDataJson = records.get(i).toString();
			FormData formData = FormData.fromJson(formDataJson, formId);
			formDataList.add(formData);
		}

		SaveSpecimenEventsDataEvent event = new SaveSpecimenEventsDataEvent();
		event.setFormId(formId);
		event.setFormDataList(formDataList);
		event.setSessionDataBean(getSession());

		SpecimenEventsSavedEvent resp = specimenEventsSvc.saveSpecimenEvents(event);
		if (resp.getStatus() == EventStatus.OK) {
			List<String> savedFormData = new ArrayList<String>();
			for (FormData formData : resp.getFormDataList()) {
				savedFormData.add(formData.toJson());
			}
			return savedFormData.toString();
		}
		
		return null;
	}
	

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
	}

}
