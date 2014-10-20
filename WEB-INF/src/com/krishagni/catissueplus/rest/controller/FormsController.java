package com.krishagni.catissueplus.rest.controller;

import java.io.IOException;
import java.io.Writer;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.AddFormContextsEvent;
import com.krishagni.catissueplus.core.de.events.AllFormsSummaryEvent;
import com.krishagni.catissueplus.core.de.events.BOTemplateGeneratedEvent;
import com.krishagni.catissueplus.core.de.events.BOTemplateGenerationEvent;
import com.krishagni.catissueplus.core.de.events.BulkFormDataSavedEvent;
import com.krishagni.catissueplus.core.de.events.DeleteFormEvent;
import com.krishagni.catissueplus.core.de.events.FormDeletedEvent;
import com.krishagni.catissueplus.core.de.events.SaveBulkFormDataEvent;
import com.krishagni.catissueplus.core.de.events.DeleteRecordEntriesEvent;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormContextsAddedEvent;
import com.krishagni.catissueplus.core.de.events.FormContextsEvent;
import com.krishagni.catissueplus.core.de.events.FormDataEvent;
import com.krishagni.catissueplus.core.de.events.FormDefinitionEvent;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormFieldsEvent;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.RecordEntriesDeletedEvent;
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
		req.setFormType(formType.equals("query") ? FormType.QUERY_FORMS : formType.equals("specimenEvent") ? FormType.SPECIMEN_EVENT_FORMS :
				FormType.DATA_ENTRY_FORMS);
		
		AllFormsSummaryEvent resp = formSvc.getForms(req);		
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getForms();
		}
		
		// TODO: Return appropriate error codes
		return null;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Long deleteForm(@PathVariable("id") Long formId) {
		DeleteFormEvent req = new DeleteFormEvent();
		req.setSessionDataBean(getSession());
		req.setFormId(formId);
		
		FormDeletedEvent resp = formSvc.deleteForm(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFormId();
		}
		
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
			@RequestParam(value="prefixParentCaption", required=false, defaultValue="false") boolean prefixParentCaption,
			@RequestParam(value="cpId", required=false, defaultValue="-1") Long cpId,
			@RequestParam(value="extendedFields", required=false, defaultValue="false") boolean extendedFields) {
		ReqFormFieldsEvent req = new ReqFormFieldsEvent();
		req.setFormId(formId);
		req.setPrefixParentFormCaption(prefixParentCaption);
		req.setCpId(cpId);
		req.setExtendedFields(extendedFields);
		
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
		return saveOrUpdateFormData(formId, formDataJson);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}/data")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String updateFormData(
			@PathVariable("id") Long formId,
			@RequestBody String formDataJson) {		
		return saveOrUpdateFormData(formId, formDataJson);
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
			BOTemplateGenerationEvent boReq = new BOTemplateGenerationEvent();
			boReq.setFormId(formCtxts.get(0).getFormId());

			for (FormContextDetail ctxt : formCtxts) {
				boReq.setFormId(ctxt.getFormId());
				boReq.addEntityLevel(ctxt.getLevel());
			}

			BOTemplateGeneratedEvent boResp = formSvc.genereateBoTemplate(boReq);
            if (boResp.getStatus() != EventStatus.NOT_FOUND) {
				resp.setMessage("Error in generating BO templates");
			}
			return resp.getFormCtxts();				
		}
		
		return null;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="{id}/data")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Long> deleteRecords(@PathVariable("id") Long formId, @RequestBody List<Long> recIds) {
		
		DeleteRecordEntriesEvent delRecEntry = new DeleteRecordEntriesEvent();
		delRecEntry.setFormId(formId);
		delRecEntry.setRecordIds(recIds);
		RecordEntriesDeletedEvent resp = formSvc.deleteRecords(delRecEntry);

		if (resp.getStatus() == EventStatus.OK) {
			return resp.getDeletedRecIds();
		}
		return null;
	}

	private String saveOrUpdateFormData(Long formId, String formDataJson) {
		try {
			formDataJson = URLDecoder.decode(formDataJson,"UTF-8");
			if (formDataJson.endsWith("=")) {
				formDataJson = formDataJson.substring(0, formDataJson.length() -1);
			}	        			
		} catch (Exception e) {
			throw new RuntimeException("Error parsing input JSON", e);
		}
		
		JsonElement formDataJsonEle = new JsonParser().parse(formDataJson);
		if (formDataJsonEle.isJsonArray()) {
			return bulkSaveFormData(formId, formDataJson);
		} else {
			FormData formData = FormData.fromJson(formDataJson, formId);

			SaveFormDataEvent req = new SaveFormDataEvent();
			req.setFormData(formData);
			req.setFormId(formId);
			req.setSessionDataBean(getSession());
			req.setRecordId(formData.getRecordId());

			FormDataEvent resp = formSvc.saveFormData(req);
			if (resp.getStatus() == EventStatus.OK) {
				return resp.getFormData().toJson();
			}
		}

		return null;		
	}
	
	private String bulkSaveFormData(Long formId, String formDataJsonArray) {
		JsonParser parser = new JsonParser();
  		JsonArray records = parser.parse(formDataJsonArray).getAsJsonArray();
  
  		List<FormData> formDataList = new ArrayList<FormData>();
  		for (int i = 0; i < records.size(); i++) {
  			String formDataJson = records.get(i).toString();
  			FormData formData = FormData.fromJson(formDataJson, formId);
  			formDataList.add(formData);
  		}
				
  		SaveBulkFormDataEvent event = new SaveBulkFormDataEvent();
  		event.setFormId(formId);
  		event.setFormDataList(formDataList);
  		event.setSessionDataBean(getSession());
  
  		BulkFormDataSavedEvent bulkFormData = formSvc.saveBulkFormData(event);
  		if (bulkFormData.getStatus() == EventStatus.OK) {
  			List<String> savedFormData = new ArrayList<String>();
  			for (FormData formData : bulkFormData.getFormDataList()) {
  				savedFormData.add(formData.toJson());
  			}
  			return new Gson().toJson(savedFormData);
  		}
  
  		return null;
	}
		
	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
