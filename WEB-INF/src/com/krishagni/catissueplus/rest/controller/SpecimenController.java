
package com.krishagni.catissueplus.rest.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
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

import com.krishagni.catissueplus.core.biospecimen.events.AliquotCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.de.events.EntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;
import com.krishagni.catissueplus.core.printer.printService.events.CreateLabelPrintEvent;
import com.krishagni.catissueplus.core.printer.printService.events.LabelPrintCreatedEvent;
import com.krishagni.catissueplus.core.printer.printService.services.PrintService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/specimens")
public class SpecimenController {

	@Autowired
	private SpecimenService specimenSvc;

	@Autowired
	private FormService formSvc;

	@Autowired
	private PrintService specimenLabelPrintSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSpecimens(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "max", required = false, defaultValue = "100") int max,
			@RequestParam(value = "countReq", required = false, defaultValue = "false") boolean countReq,
			@RequestParam(value = "searchString", required = false, defaultValue = "") String searchString,
			@RequestParam(value="label", required=false) String[] labels){
		
			ReqSpecimensEvent req = new ReqSpecimensEvent();
			req.setStartAt(start);
			req.setMaxRecords(max);
			req.setCountReq(countReq);
			req.setSearchString(searchString);
			req.setSessionDataBean(getSession());
			
			List<String> specimenLabels = (labels != null) ? new ArrayList(Arrays.asList(labels)) : null; 
			req.setSpecimenLabels(specimenLabels);
			SpecimensSummaryEvent resp = specimenSvc.getSpecimens(req);
			if (!resp.isSuccess()) {
				resp.raiseException();
			}
			Map<String, Object> result = new HashMap<String, Object>();
			if (resp.getCount() != null) {
				result.put("count", resp.getCount());
			}
			result.put("specimens", resp.getSpecimensSummary());
			
			return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long specimenId) {
		ReqEntityFormsEvent req = new ReqEntityFormsEvent();
		req.setEntityId(specimenId);
		req.setEntityType(EntityType.SPECIMEN);
		req.setSessionDataBean(getSession());

		EntityFormsEvent resp = formSvc.getEntityForms(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getForms();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms/{formCtxtId}/records")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormRecordSummary> getFormRecords(@PathVariable("id") Long specimenId,
			@PathVariable("formCtxtId") Long formCtxtId) {

		ReqEntityFormRecordsEvent req = new ReqEntityFormRecordsEvent();
		req.setEntityId(specimenId);
		req.setFormCtxtId(formCtxtId);
		req.setSessionDataBean(getSession());

		EntityFormRecordsEvent resp = formSvc.getEntityFormRecords(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getFormRecords();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenDetail patchSpecimen(@PathVariable Long id, @RequestBody Map<String, Object> specimenProps) {
		PatchSpecimenEvent event = new PatchSpecimenEvent();
		SpecimenPatchDetail detail = new SpecimenPatchDetail();
		try {
			BeanUtils.populate(detail, specimenProps);
		}
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event.setId(id);
		event.setSessionDataBean(getSession());
		event.setDetail(detail);
		SpecimenUpdatedEvent response = specimenSvc.patchSpecimen(event);
		if (!response.isSuccess()) {
			response.raiseException();
		}
		return response.getSpecimenDetail();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id}/aliquots")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenDetail> createAliquot(@PathVariable Long id, @RequestBody AliquotDetail aliquotDetail) {
		CreateAliquotEvent createAliquotEvent = new CreateAliquotEvent();
		createAliquotEvent.setAliquotDetail(aliquotDetail);
		createAliquotEvent.setSpecimenId(id);

		AliquotCreatedEvent response = specimenSvc.createAliquot(createAliquotEvent);
		if (!response.isSuccess()) {
			response.raiseException();
		}
		return response.getAliquots();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public SpecimenDetail createSpecimen(@RequestBody SpecimenDetail specimenDetail) {
		CreateSpecimenEvent createSpecimenEvent = new CreateSpecimenEvent();
		createSpecimenEvent.setSpecimenDetail(specimenDetail);
		createSpecimenEvent.setSessionDataBean(getSession());
		createSpecimenEvent.setScgId(specimenDetail.getScgId());

		SpecimenCreatedEvent response = specimenSvc.createSpecimen(createSpecimenEvent);
		if (!response.isSuccess()) {
			response.raiseException();
		}
		return response.getSpecimenDetail();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenDetail updateSpecimen(@PathVariable Long id, @RequestBody SpecimenDetail specimenDetail) {
		UpdateSpecimenEvent event = new UpdateSpecimenEvent();
		event.setId(id);
		event.setSpecimenDetail(specimenDetail);
		event.setSessionDataBean(getSession());
		SpecimenUpdatedEvent response = specimenSvc.updateSpecimen(event);
		if (!response.isSuccess()) {
			response.raiseException();
		}
			return response.getSpecimenDetail();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/labelPrint/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String printLabel(@PathVariable Long id) {
		CreateLabelPrintEvent event = new CreateLabelPrintEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		LabelPrintCreatedEvent resp = specimenLabelPrintSvc.print(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/labelPrint/name={name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String printLabel(@PathVariable String name) {
		CreateLabelPrintEvent event = new CreateLabelPrintEvent();
		event.setName(name);
		event.setSessionDataBean(getSession());
		LabelPrintCreatedEvent resp = specimenLabelPrintSvc.print(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
