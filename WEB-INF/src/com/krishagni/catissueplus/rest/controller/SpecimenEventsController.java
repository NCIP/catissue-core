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
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenEventsService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp.EntityType;
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
	private SpecimenEventsService specimenEventsSvc;

	@Autowired
	private FormService formSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long specimenId) {
		ListEntityFormsOp opDetail = new ListEntityFormsOp();
		opDetail.setEntityId(specimenId);
		opDetail.setEntityType(EntityType.SPECIMEN_EVENT);
		

		ResponseEvent<List<FormCtxtSummary>> resp = formSvc.getEntityForms(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
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

		ResponseEvent<List<FormData>> resp = specimenEventsSvc.saveSpecimenEvents(getRequest(formDataList));
		resp.throwErrorIfUnsuccessful();
		
		List<String> savedFormData = new ArrayList<String>();
		for (FormData formData : resp.getPayload()) {
			savedFormData.add(formData.toJson());
		}
		
		return savedFormData.toString();
	}
	

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(getSession(), payload);
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
	}

}
