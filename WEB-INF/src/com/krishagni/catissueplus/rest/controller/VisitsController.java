
package com.krishagni.catissueplus.rest.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
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
			@RequestParam(value = "cprId", required = true) Long cprId,
			@RequestParam(value = "includeStats", required = false, defaultValue = "false") boolean includeStats) {
		
		VisitsListCriteria crit = new VisitsListCriteria()
			.cprId(cprId)
			.includeStat(includeStats);
		
		ResponseEvent<List<VisitSummary>> resp = cprSvc.getVisits(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail getVisit(@PathVariable("id") Long visitId) {
		EntityQueryCriteria crit = new EntityQueryCriteria(visitId);
		
		ResponseEvent<VisitDetail> resp = visitService.getVisit(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail addVisit(@RequestBody VisitDetail visit) {
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(visit));
		resp.throwErrorIfUnsuccessful();				
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public VisitDetail updateVisit(@PathVariable("id") Long visitId, @RequestBody VisitDetail visit) {
		visit.setId(visitId);
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(visit));
		resp.throwErrorIfUnsuccessful();				
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{id}/spr")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadSpr(@PathVariable("id") Long visitId, @PathVariable("file") MultipartFile file)
	throws IOException {
		SprDetail sprDetail = new SprDetail();
		sprDetail.setSpr(file.getInputStream());
		sprDetail.setSprName(file.getOriginalFilename());
		sprDetail.setVisitId(visitId);
		
		ResponseEvent<String> resp = visitService.uploadSpr(getRequest(sprDetail));
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
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);				
	}
}
