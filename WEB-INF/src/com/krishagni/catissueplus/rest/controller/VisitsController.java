
package com.krishagni.catissueplus.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.CpEntityDeleteCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.FileDownloadDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprLockDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.FileType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.events.EntityFormRecords;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordsList;
import com.krishagni.catissueplus.core.de.events.GetEntityFormRecordsOp;
import com.krishagni.catissueplus.core.de.events.GetFormRecordsListOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;

@Controller
@RequestMapping("/visits")
public class VisitsController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private VisitService visitService;

	@Autowired
	private CollectionProtocolRegistrationService cprSvc;
	
	@Autowired
	private FormService formSvc;
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitSummary> getVisits(
		@RequestParam(value = "cprId", required = true)
		Long cprId,

		@RequestParam(value = "includeStats", required = false, defaultValue = "false") 
		boolean includeStats) {
		
		VisitsListCriteria crit = new VisitsListCriteria()
			.cprId(cprId)
			.includeStat(includeStats);

		ResponseEvent<List<VisitSummary>> resp = cprSvc.getVisits(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/bynamespr")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitDetail> getVisits(
		@RequestParam(value = "visitName", required = false)
		String visitName,

		@RequestParam(value = "sprNumber", required = false)
		String sprNumber) {

		VisitsListCriteria criteria = new VisitsListCriteria()
			.name(visitName)
			.sprNumber(sprNumber);

		ResponseEvent<List<VisitDetail>> resp = visitService.getVisits(getRequest(criteria));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail getVisit(@PathVariable("id") Long visitId) {
		ResponseEvent<VisitDetail> resp = visitService.getVisit(getVisitQueryReq(visitId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail addVisit(@RequestBody VisitDetail visit) {
		ResponseEvent<VisitDetail> resp = visitService.addVisit(getRequest(visit));
		resp.throwErrorIfUnsuccessful();				
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail updateVisit(
		@PathVariable("id") 
		Long visitId, 
		
		@RequestBody 
		VisitDetail visit) {

		visit.setId(visitId);
		
		ResponseEvent<VisitDetail> resp = visitService.updateVisit(getRequest(visit));
		resp.throwErrorIfUnsuccessful();				
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{id}/spr-file")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadSprFile(
		@PathVariable("id") 
		Long visitId, 
		
		@PathVariable("file") 
		MultipartFile file)
	throws IOException {
		ResponseEvent<String> resp = null;
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();

			SprDetail sprDetail = new SprDetail();
			sprDetail.setId(visitId);
			sprDetail.setFileIn(inputStream);
			sprDetail.setFilename(file.getOriginalFilename());
			sprDetail.setContentType(file.getContentType());

			resp = visitService.uploadSprFile(getRequest(sprDetail));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload();
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/spr-file")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void downloadSpr(
		@PathVariable("id") 
		Long visitId,
		
		@RequestParam(value = "type", required = false, defaultValue="text") 
		String type,
		
		HttpServletResponse httpResp)
	throws IOException {
		FileType fileType = FileType.fromType(type);
		if (fileType == null) {
			fileType = FileType.TEXT;
		}

		FileDownloadDetail sprReqDetail = new FileDownloadDetail();
		sprReqDetail.setId(visitId);
		sprReqDetail.setType(fileType);

		ResponseEvent<FileDetail> resp = visitService.getSpr(getRequest(sprReqDetail));
		resp.throwErrorIfUnsuccessful();

		FileDetail detail = resp.getPayload();
		Utility.sendToClient(httpResp, detail.getFilename(), detail.getFileOut());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/spr-text") 
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getSprText(@PathVariable("id") Long visitId) {
		FileDownloadDetail sprReqDetail = new FileDownloadDetail();
		sprReqDetail.setId(visitId);

		ResponseEvent<FileDetail> resp = visitService.getSpr(getRequest(sprReqDetail));
		resp.throwErrorIfUnsuccessful();

		FileDetail detail = resp.getPayload();
		String contentType = Utility.getContentType(detail.getFileOut());
		if (!contentType.startsWith("text/")) {
			return null;
		}
		
		return Utility.getFileText(detail.getFileOut());
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}/spr-text")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String updateSprText(@PathVariable("id") Long visitId, @RequestBody SprDetail sprDetail) {
		sprDetail.setId(visitId);
		ResponseEvent<String>resp = visitService.updateSprText(getRequest(sprDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}/spr-file")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean deleteSprFile(@PathVariable("id") Long visitId) {
		EntityQueryCriteria crit = new EntityQueryCriteria(visitId);
		ResponseEvent<Boolean> resp = visitService.deleteSprFile(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/spr-lock")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SprLockDetail updateSprLockStatus(@PathVariable("id") Long visitId, @RequestBody SprLockDetail detail) {
		detail.setVisitId(visitId);
		ResponseEvent<SprLockDetail> resp = visitService.updateSprLockStatus(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/collect")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitSpecimenDetail collectVisitAndSpecimens(@RequestBody VisitSpecimenDetail detail) {
		RequestEvent<VisitSpecimenDetail> req = getRequest(detail);
		ResponseEvent<VisitSpecimenDetail> resp = visitService.collectVisitAndSpecimens(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/dependent-entities")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DependentEntityDetail> getDependentEntities(@PathVariable("id") Long visitId) {
		ResponseEvent<List<DependentEntityDetail>> resp = visitService.getDependentEntities(getVisitQueryReq(visitId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail deleteVisit(
			@PathVariable("id")
			Long visitId,

			@RequestParam(value = "forceDelete", required = false, defaultValue = "false")
			boolean forceDelete) {

		CpEntityDeleteCriteria crit = new CpEntityDeleteCriteria();
		crit.setId(visitId);
		crit.setForceDelete(forceDelete);

		ResponseEvent<VisitDetail> resp = visitService.deleteVisit(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
		
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long visitId) {
		ListEntityFormsOp opDetail = new ListEntityFormsOp();
		opDetail.setEntityId(visitId);
		opDetail.setEntityType(EntityType.SPECIMEN_COLLECTION_GROUP);

		ResponseEvent<List<FormCtxtSummary>> resp = formSvc.getEntityForms(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms/{formCtxtId}/records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EntityFormRecords getFormRecords(
		@PathVariable("id") 
		Long visitId,
			
		@PathVariable("formCtxtId") 
		Long formCtxtId) {

		GetEntityFormRecordsOp opDetail = new GetEntityFormRecordsOp();
		opDetail.setEntityId(visitId);
		opDetail.setFormCtxtId(formCtxtId);

		ResponseEvent<EntityFormRecords> resp = formSvc.getEntityFormRecords(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/extension-records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<FormRecordsList> getExtensionRecords(@PathVariable("id") Long visitId) {
		GetFormRecordsListOp opDetail = new GetFormRecordsListOp();
		opDetail.setObjectId(visitId);
		opDetail.setEntityType("SpecimenCollectionGroup");
		
		ResponseEvent<List<FormRecordsList>> resp = formSvc.getFormRecords(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();				
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/extension-form")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getForm(
		@RequestParam(value = "cpId", required = false, defaultValue = "-1")
		Long cpId) {

		return formSvc.getExtensionInfo(cpId, Visit.EXTN);
	}
	
	private RequestEvent<EntityQueryCriteria> getVisitQueryReq(Long visitId) {
		return getRequest(new EntityQueryCriteria(visitId));		
	}

	private RequestEvent<EntityQueryCriteria> getVisitQueryReq(String visitName) {
		return getRequest(new EntityQueryCriteria(visitName));
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);				
	}
}
