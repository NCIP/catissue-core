
package com.krishagni.catissueplus.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CpEntityDeleteCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenStatusDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
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
@RequestMapping("/specimens")
public class SpecimensController {

	@Autowired
	private SpecimenService specimenSvc;
	
	@Autowired
	private CollectionProtocolRegistrationService cprSvc;
	
	@Autowired
	private FormService formSvc;
	
	@RequestMapping(method = RequestMethod.HEAD)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public Boolean doesSpecimenExists(
		@RequestParam(value = "cpShortTitle", required = false)
		String cpShortTitle,

		@RequestParam(value = "label")
		String label) {

		SpecimenQueryCriteria crit = new SpecimenQueryCriteria(cpShortTitle, label);
		ResponseEvent<Boolean> resp = specimenSvc.doesSpecimenExists(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		if (resp.getPayload() == true) {
			return true;
		}

		throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND, label);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<?> getSpecimens(
			@RequestParam(value = "cpId", required = false)
			Long cpId,

			@RequestParam(value = "cprId", required = false) 
			Long cprId,
			
			@RequestParam(value = "eventId", required = false) 
			Long eventId,
			
			@RequestParam(value = "visitId", required = false) 
			Long visitId,
			
			@RequestParam(value = "id", required = false)
			List<Long> ids,

			@RequestParam(value = "label", required = false)
			List<String> labels) {
				
		if (cprId != null) { // TODO: Move this to CPR controller
			VisitSpecimensQueryCriteria crit = new VisitSpecimensQueryCriteria();
			crit.setCprId(cprId);
			crit.setEventId(eventId);
			crit.setVisitId(visitId);

			ResponseEvent<List<SpecimenDetail>> resp = cprSvc.getSpecimens(getRequest(crit));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload();
		} else if (CollectionUtils.isNotEmpty(ids)) {
			ResponseEvent<List<SpecimenInfo>> resp = specimenSvc.getSpecimensById(getRequest(ids));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload();
		} else if (CollectionUtils.isNotEmpty(labels)) {
			ResponseEvent<List<SpecimenInfo>> resp = specimenSvc.getSpecimens(getRequest(labels));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload();
		} else if (cpId != null) {
			ResponseEvent<List<SpecimenInfo>> resp = specimenSvc.getPrimarySpecimensByCp(getRequest(cpId));
			resp.throwErrorIfUnsuccessful();
			return resp.getPayload();
		} else {
			return Collections.emptyList();
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenDetail getSpecimen(@PathVariable("id") Long id) {
		SpecimenQueryCriteria crit = new SpecimenQueryCriteria(id);
		
		ResponseEvent<SpecimenDetail> resp = specimenSvc.getSpecimen(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public SpecimenDetail createSpecimen(@RequestBody SpecimenDetail detail) {
		ResponseEvent<SpecimenDetail> resp = specimenSvc.createSpecimen(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public SpecimenDetail updateSpecimen(
			@PathVariable("id") 
			Long specimenId, 
			
			@RequestBody 
			SpecimenDetail detail) {
		
		detail.setId(specimenId);
		
		ResponseEvent<SpecimenDetail> resp = specimenSvc.updateSpecimen(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}/status")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenDetail updateSpecimenStatus(
			@PathVariable("id")
			Long specimenId,

			@RequestBody
			SpecimenStatusDetail detail) {

		detail.setId(specimenId);

		ResponseEvent<List<SpecimenDetail>>  resp = specimenSvc.updateSpecimensStatus(getRequest(Collections.singletonList(detail)));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload().get(0);
	}

	@RequestMapping(method = RequestMethod.PUT, value="/status")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenDetail> updateSpecimensStatus(
			@RequestBody
			List<SpecimenStatusDetail> details) {
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.updateSpecimensStatus(getRequest(details));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/dependent-entities")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DependentEntityDetail> getDependentEntities(@PathVariable("id") Long specimenId) {
		SpecimenQueryCriteria crit = new SpecimenQueryCriteria(specimenId);
		ResponseEvent<List<DependentEntityDetail>> resp = specimenSvc.getDependentEntities(getRequest(crit));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenInfo deleteSpecimen(@PathVariable("id") Long specimenId) {
		CpEntityDeleteCriteria crit = new CpEntityDeleteCriteria();
		crit.setId(specimenId);
		crit.setForceDelete(false);

		ResponseEvent<List<SpecimenInfo>> resp = specimenSvc.deleteSpecimens(getRequest(Collections.singletonList(crit)));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload().get(0);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> deleteSpecimens(@RequestParam(value = "id") Long[] specimenIds) {
		List<CpEntityDeleteCriteria> criteria = new ArrayList<>();
		for (Long specimenId : specimenIds) {
			CpEntityDeleteCriteria criterion = new CpEntityDeleteCriteria();
			criterion.setId(specimenId);
			criterion.setForceDelete(true);
			criteria.add(criterion);
		}

		ResponseEvent<List<SpecimenInfo>> resp = specimenSvc.deleteSpecimens(getRequest(criteria));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(
			@PathVariable("id") 
			Long specimenId,
			
			@RequestParam(value = "entityType", required = false, defaultValue="Specimen")
			String entityType
			) {
		ListEntityFormsOp opDetail = new ListEntityFormsOp();
		opDetail.setEntityId(specimenId);
		
		if (entityType.equals("Specimen")) {
			opDetail.setEntityType(EntityType.SPECIMEN);
		} else {
			opDetail.setEntityType(EntityType.SPECIMEN_EVENT);
		}
		
		ResponseEvent<List<FormCtxtSummary>> resp = formSvc.getEntityForms(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms/{formCtxtId}/records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EntityFormRecords getFormRecords(@PathVariable("id") Long specimenId,
			@PathVariable("formCtxtId") Long formCtxtId) {

		GetEntityFormRecordsOp opDetail = new GetEntityFormRecordsOp();
		opDetail.setEntityId(specimenId);
		opDetail.setFormCtxtId(formCtxtId);
		
		ResponseEvent<EntityFormRecords> resp = formSvc.getEntityFormRecords(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/events")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<FormRecordsList> getEvents(@PathVariable("id") Long specimenId) {
		return getRecords(specimenId, "SpecimenEvent");
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/extension-records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormRecordsList> getExtensionRecords(@PathVariable("id") Long specimenId) {
		return getRecords(specimenId, "Specimen");
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/collect")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public List<SpecimenDetail> collectSpecimens(@RequestBody List<SpecimenDetail> specimens) {		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(specimens));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	/** API present for UI purpose **/
	@RequestMapping(method = RequestMethod.GET, value="/{id}/cpr-visit-ids")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public Map<String, Object> getCprAndVisitIds(@PathVariable("id") Long specimenId) {
		ResponseEvent<Map<String, Object>> resp = specimenSvc.getCprAndVisitIds(new RequestEvent<Long>(specimenId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/extension-form")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getForm(
			@RequestParam(value = "cpId", required = false, defaultValue = "-1")
			Long cpId) {

		return formSvc.getExtensionInfo(cpId, Specimen.EXTN);
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}

	private List<FormRecordsList> getRecords(Long specimenId, String entityType) {
		GetFormRecordsListOp opDetail = new GetFormRecordsListOp();
		opDetail.setObjectId(specimenId);
		opDetail.setEntityType(entityType);
		
		ResponseEvent<List<FormRecordsList>> resp = formSvc.getFormRecords(getRequest(opDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();		
	}
}