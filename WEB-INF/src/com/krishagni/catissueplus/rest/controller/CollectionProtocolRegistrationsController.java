
package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentFormDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormRecords;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordsList;
import com.krishagni.catissueplus.core.de.events.GetEntityFormRecordsOp;
import com.krishagni.catissueplus.core.de.events.GetFormRecordsListOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;

@Controller
@RequestMapping("/collection-protocol-registrations")
public class CollectionProtocolRegistrationsController {
	@Autowired
	private CollectionProtocolRegistrationService cprSvc;

	@Autowired
	private CollectionProtocolService cpSvc;

	@Autowired
	private FormService formSvc;
	
	@Autowired
	private HttpServletRequest httpReq;
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<CprSummary> getRegistrations(
			@RequestParam(value = "cpId",         required = true)
			Long cpId,
			
			@RequestParam(value = "query",        required = false)
			String searchStr,
			
			@RequestParam(value = "name",         required = false)
			String name,
			
			@RequestParam(value = "ppid",         required = false)
			String ppid,
			
			@RequestParam(value = "mrn",          required = false)
			String mrn,
			
			@RequestParam(value = "empi",         required = false)
			String empi,
			
			@RequestParam(value = "dob",          required = false) 
			@DateTimeFormat(pattern="yyyy-MM-dd")
			Date dob,
			
			@RequestParam(value = "specimen",     required = false)
			String specimen,
			
			@RequestParam(value = "startAt",      required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxRecs",      required = false, defaultValue = "100")
			int maxRecs,
			
			@RequestParam(value = "includeStats", required = false, defaultValue = "false") 
			boolean includeStats) {

		CprListCriteria crit = new CprListCriteria()
			.cpId(cpId)
			.query(searchStr)
			.name(name)
			.ppid(ppid)
			.mrn(mrn)
			.empi(empi)
			.dob(dob)
			.specimen(specimen)
			.startAt(startAt)
			.maxResults(maxRecs)
			.includeStat(includeStats)
			.includePhi(true);
		
		ResponseEvent<List<CprSummary>> resp = cpSvc.getRegisteredParticipants(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{cprId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail getRegistration(@PathVariable("cprId") Long cprId) {
		RegistrationQueryCriteria crit = new RegistrationQueryCriteria();
		crit.setCprId(cprId);
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail register(@RequestBody CollectionProtocolRegistrationDetail cprDetail) {				
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(cprDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail updateRegistration(
			@PathVariable("id")
			Long cprId,
			
			@RequestBody 
			CollectionProtocolRegistrationDetail cprDetail) {
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.updateRegistration(getRequest(cprDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/consent-form")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void downloadConsentForm(@PathVariable("id") Long cprId, HttpServletResponse response) throws IOException {
		RegistrationQueryCriteria crit = new RegistrationQueryCriteria();
		crit.setCprId(cprId);
		
		ResponseEvent<File> resp = cprSvc.getConsentForm(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		
		File file =resp.getPayload();
		String fileType = Files.probeContentType(file.toPath());
		
		response.setContentType(fileType);
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

		InputStream in = null;
		try {
			in = new FileInputStream(file);
			IOUtils.copy(in, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IOUtils.closeQuietly(in);
		}	
	}

	@RequestMapping(method = RequestMethod.POST, value="/{id}/consent-form")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadConsentForm(@PathVariable("id") Long cprId, @PathVariable("file") MultipartFile file) 
	throws IOException {
		ConsentFormDetail detail = new ConsentFormDetail();
		detail.setCprId(cprId);
		detail.setFileName(file.getOriginalFilename());
		detail.setInputStream(file.getInputStream());
		
		ResponseEvent<String> resp = cprSvc.uploadConsentForm(getRequest(detail));
		resp.throwErrorIfUnsuccessful();

		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}/consent-form")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean deleteConsentForm(@PathVariable("id") Long cprId) {
		RegistrationQueryCriteria crit = new RegistrationQueryCriteria();
		crit.setCprId(cprId);
		
		ResponseEvent<Boolean> resp = cprSvc.deleteConsentForm(getRequest(crit));
		resp.throwErrorIfUnsuccessful();

		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long cprId) {
		ListEntityFormsOp opDetail = new ListEntityFormsOp();
		opDetail.setEntityId(cprId);
		opDetail.setEntityType(EntityType.COLLECTION_PROTOCOL_REGISTRATION);

		ResponseEvent<List<FormCtxtSummary>> resp = formSvc.getEntityForms(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms/{formCtxtId}/records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EntityFormRecords getFormRecords(@PathVariable("id") Long cprId,
			@PathVariable("formCtxtId") Long formCtxtId) { // TODO: Remove

		GetEntityFormRecordsOp opDetail = new GetEntityFormRecordsOp();
		opDetail.setEntityId(cprId);
		opDetail.setFormCtxtId(formCtxtId);


		ResponseEvent<EntityFormRecords> resp = formSvc.getEntityFormRecords(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/extension-records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<FormRecordsList> getExtensionRecords(@PathVariable("id") Long cprId) {
		GetFormRecordsListOp opDetail = new GetFormRecordsListOp();
		opDetail.setObjectId(cprId);
		opDetail.setEntityType("Participant");
		
		ResponseEvent<List<FormRecordsList>> resp = formSvc.getFormRecords(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();				
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
