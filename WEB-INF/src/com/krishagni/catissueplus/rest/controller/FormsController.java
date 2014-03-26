package com.krishagni.catissueplus.rest.controller;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
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

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.AddFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.AllFormsSummaryEvent;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormContextsAddedEvent;
import com.krishagni.catissueplus.core.de.events.FormContextsEvent;
import com.krishagni.catissueplus.core.de.events.FormDataEvent;
import com.krishagni.catissueplus.core.de.events.FormDefinitionEvent;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormFieldsEvent;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.ReqAllFormsSummaryEvent;
import com.krishagni.catissueplus.core.de.events.ReqAllFormsSummaryEvent.FormType;
import com.krishagni.catissueplus.core.de.events.ReqFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormDataEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormDefinitionEvent;
import com.krishagni.catissueplus.core.de.events.ReqFormFieldsEvent;
import com.krishagni.catissueplus.core.de.events.SaveFormDataEvent;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.nutility.ContainerJsonSerializer;
import edu.common.dynamicextensions.nutility.ContainerSerializer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;


@Controller
@RequestMapping("/forms")
public class FormsController {
	@Autowired
	private HttpServletRequest httpServletRequest;

	
	@Autowired
	private FormService formSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormSummary> getAllFormsSummary(
			@RequestParam(value="formType", required=false, defaultValue="dataEntry") String formType) {
		ReqAllFormsSummaryEvent req = new ReqAllFormsSummaryEvent();
		req.setFormType(formType.equals("query") ? FormType.QUERY_FORMS : FormType.DATA_ENTRY_FORMS);
		
		AllFormsSummaryEvent resp = formSvc.getForms(req);		
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getForms();
		}
		
		// TODO: Return appropriate error codes
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}/definition")
	@ResponseStatus(HttpStatus.OK)
	public void getFormDefinition(@PathVariable("id") Long formId, Writer writer) 
	throws IOException {
		ReqFormDefinitionEvent req = new ReqFormDefinitionEvent();
		req.setFormId(formId);
		
		FormDefinitionEvent resp = formSvc.getFormDefinition(req);		
		if (resp.getStatus() == EventStatus.OK) {
			ContainerSerializer serializer = new ContainerJsonSerializer(resp.getFormDef(), writer);
			serializer.serialize();
			writer.flush();
		}		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}/fields")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormFieldSummary> getFormFields(
			@PathVariable("id") Long formId,
			@RequestParam(value="prefixParentCaption", required=false, defaultValue="false") boolean prefixParentCaption) {
		ReqFormFieldsEvent req = new ReqFormFieldsEvent();
		req.setFormId(formId);
		req.setPrefixParentFormCaption(prefixParentCaption);
		
		FormFieldsEvent resp = formSvc.getFormFields(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFields();
		}
		
		return null;
	}
		
	@RequestMapping(method = RequestMethod.GET, value="{id}/data/{recordId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getFormData(@PathVariable("id") Long formId, @PathVariable("recordId") Long recordId) {
		ReqFormDataEvent req = new ReqFormDataEvent();
		req.setFormId(formId);
		req.setRecordId(recordId);
		
		FormDataEvent resp = formSvc.getFormData(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFormData().toJson();
		}
		
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="{id}/data")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String saveFormData(@PathVariable("id") Long formId, @RequestBody String formDataJson) {
		return saveFormData(formId, null, formDataJson);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}/data/{recordId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String updateFormData(
			@PathVariable("id") Long formId, 
			@PathVariable("recordId") Long recordId, 
			@RequestBody String formDataJson) {		
		return saveFormData(formId, recordId, formDataJson);
	}
		
	@RequestMapping(method = RequestMethod.GET, value="{id}/contexts")	
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormContextDetail> getFormContexts(@PathVariable("id") Long formId) {
		ReqFormContextsEvent req = new ReqFormContextsEvent();
		req.setFormId(formId);
		
		FormContextsEvent resp = formSvc.getFormContexts(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFormCtxts();
		}
		
		return null;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}/contexts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormContextDetail> addFormContexts(@PathVariable("id") Long formId, @RequestBody List<FormContextDetail> formCtxts) {
		for (FormContextDetail formCtxt : formCtxts) {
			formCtxt.setFormId(formId);
		}
		
		AddFormContextsEvent req = new AddFormContextsEvent();
		req.setFormContexts(formCtxts);
		
		FormContextsAddedEvent resp = formSvc.addFormContexts(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFormCtxts();
		}
		
		return null;
	}

	private String saveFormData(Long formId, Long recordId, String formDataJson) {
		try {
			formDataJson = URLDecoder.decode(formDataJson,"UTF-8");
			if (formDataJson.endsWith("=")) {
				formDataJson = formDataJson.substring(0, formDataJson.length() -1);
			}	        			
		} catch (Exception e) {
			throw new RuntimeException("Error parsing input JSON", e);
		}
		
		FormData formData = FormData.fromJson(formDataJson, formId);
	
		SaveFormDataEvent req = new SaveFormDataEvent();
		req.setFormData(formData);
		req.setFormId(formId);
		req.setSessionDataBean(getSession());
		req.setRecordId(recordId);
		
		FormDataEvent resp = formSvc.saveFormData(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFormData().toJson();
		}
		
		return null;		
	}
		
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
