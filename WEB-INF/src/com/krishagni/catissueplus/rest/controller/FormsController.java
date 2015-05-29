package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.FormContextDetail;
import com.krishagni.catissueplus.core.de.events.FormDataDetail;
import com.krishagni.catissueplus.core.de.events.FormFieldSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordsList;
import com.krishagni.catissueplus.core.de.events.FormSummary;
import com.krishagni.catissueplus.core.de.events.FormType;
import com.krishagni.catissueplus.core.de.events.GetFormDataOp;
import com.krishagni.catissueplus.core.de.events.GetFormRecordsListOp;
import com.krishagni.catissueplus.core.de.events.ListFormFields;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.nutility.ContainerJsonSerializer;
import edu.common.dynamicextensions.nutility.ContainerSerializer;
import edu.common.dynamicextensions.nutility.FormDefinitionExporter;
import edu.common.dynamicextensions.nutility.IoUtil;


@Controller
@RequestMapping("/forms")
public class FormsController {
	@Autowired
	private HttpServletRequest httpServletRequest;

	private static AtomicInteger formCnt = new AtomicInteger();
	
	@Autowired
	private FormService formSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormSummary> getForms(
			@RequestParam(value="formType", required=false, defaultValue="dataEntry") 
			String formType) {
		
		FormType type = FormType.fromType(formType);
		if (type == null) {
			type = FormType.DATA_ENTRY_FORMS;
		}
		
		ResponseEvent<List<FormSummary>> resp = formSvc.getForms(getRequestEvent(type));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Boolean deleteForm(@PathVariable("id") Long formId) {
		ResponseEvent<Boolean> resp = formSvc.deleteForm(getRequestEvent(formId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="{id}/definition")
	@ResponseStatus(HttpStatus.OK)
	public void getFormDefinition(@PathVariable("id") Long formId, Writer writer) 
	throws IOException {
		ResponseEvent<Container> resp = formSvc.getFormDefinition(getRequestEvent(formId));		
		resp.throwErrorIfUnsuccessful();
		
		ContainerSerializer serializer = new ContainerJsonSerializer(resp.getPayload(), writer);
		serializer.serialize();
		writer.flush();		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}/fields")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormFieldSummary> getFormFields(
			@PathVariable("id") Long formId,
			@RequestParam(value="prefixParentCaption", required=false, defaultValue="false") boolean prefixParentCaption,
			@RequestParam(value="cpId", required=false, defaultValue="-1") Long cpId,
			@RequestParam(value="extendedFields", required=false, defaultValue="false") boolean extendedFields) {
		ListFormFields crit = new ListFormFields();
		crit.setFormId(formId);
		crit.setPrefixParentFormCaption(prefixParentCaption);
		crit.setCpId(cpId);
		crit.setExtendedFields(extendedFields);
		
		ResponseEvent<List<FormFieldSummary>> resp = formSvc.getFormFields(getRequestEvent(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
		
	@RequestMapping(method = RequestMethod.GET, value="{id}/data/{recordId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getFormData(@PathVariable("id") Long formId, @PathVariable("recordId") Long recordId) {
		GetFormDataOp op = new GetFormDataOp();
		op.setFormId(formId);
		op.setRecordId(recordId);
		
		ResponseEvent<FormDataDetail> resp = formSvc.getFormData(getRequestEvent(op));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload().getFormData().toJson();
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
		ResponseEvent<List<FormContextDetail>> resp = formSvc.getFormContexts(getRequestEvent(formId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}/contexts")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormContextDetail> addFormContexts(@PathVariable("id") Long formId, @RequestBody List<FormContextDetail> formCtxts) {
		for (FormContextDetail formCtxt : formCtxts) {
			formCtxt.setFormId(formId);
		}
		
		ResponseEvent<List<FormContextDetail>> resp = formSvc.addFormContexts(getRequestEvent(formCtxts));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<FormRecordsList> getRecords(
			@PathVariable("id") 
			Long formId,
			
			@RequestParam(value = "objectId", required = true)
			Long objectId,
			
			@RequestParam(value = "entityType", required = true)
			String entityType) {
		
		GetFormRecordsListOp opDetail = new GetFormRecordsListOp();
		opDetail.setObjectId(objectId);
		opDetail.setEntityType(entityType);
		opDetail.setFormId(formId);
		
		ResponseEvent<List<FormRecordsList>> resp = formSvc.getFormRecords(getRequestEvent(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
		
	@RequestMapping(method = RequestMethod.DELETE, value="{id}/data/{recordId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Long deleteRecords(@PathVariable("id") Long formId, @PathVariable("recordId") Long recId) {		
		ResponseEvent<Long> resp = formSvc.deleteRecord(getRequestEvent(recId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/definition-zip")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void exportForm(@PathVariable("id") Long containerId, HttpServletResponse httpResp) {
		ResponseEvent<Container> resp = formSvc.getFormDefinition(getRequestEvent(containerId));
		resp.throwErrorIfUnsuccessful();
			
		String tmpDir = getTmpDirName();
		String zipFileName = null;
		FileInputStream fin = null;
		
		try {
			Container container = resp.getPayload();
			FormDefinitionExporter formExporter = new FormDefinitionExporter();
			formExporter.export(container, tmpDir);
			
			String fileName = container.getName() + ".zip";
			zipFileName = zipFiles(tmpDir);
			
			httpResp.setContentType("application/zip");
			httpResp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			OutputStream out = httpResp.getOutputStream();
			fin = new FileInputStream(zipFileName);
			IoUtil.copy(fin, out);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred when exporting form", e);
		} finally {
			IoUtil.delete(tmpDir);
			IoUtil.delete(zipFileName);
			IoUtil.close(fin);
		}
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
			FormDataDetail saveOp = FormDataDetail.ok(formId, formData.getRecordId(), formData);

			ResponseEvent<FormDataDetail> resp = formSvc.saveFormData(getRequestEvent(saveOp));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload().getFormData().toJson();
		}
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
				
  		ResponseEvent<List<FormData>> resp = formSvc.saveBulkFormData(getRequestEvent(formDataList));
  		resp.throwErrorIfUnsuccessful();
  		
  		List<String> savedFormData = new ArrayList<String>();
  		for (FormData formData : resp.getPayload()) {
  			savedFormData.add(formData.toJson());
  		}
  		return new Gson().toJson(savedFormData);
  	}
	
	private String zipFiles(String dir) {
		String zipFile = new StringBuilder(System.getProperty("java.io.tmpdir"))
			.append(File.separator).append("export-form-")
			.append(formCnt.incrementAndGet()).append(".zip")
			.toString();
		
		IoUtil.zipFiles(dir, zipFile);
		return zipFile;
	}
	
	private String getTmpDirName() {
		return new StringBuilder().append(System.getProperty("java.io.tmpdir")).append(File.separator)
				.append(System.currentTimeMillis()).append(formCnt.incrementAndGet()).append("create").toString();
	}
  
	private <T> RequestEvent<T> getRequestEvent(T payload) {
		return new RequestEvent<T>(payload);				
	}	
}
