
package com.krishagni.catissueplus.rest.controller;

import java.lang.reflect.InvocationTargetException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.AliquotCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.EntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.EntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.FormRecordSummary;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormRecordsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent;
import com.krishagni.catissueplus.core.de.events.ReqEntityFormsEvent.EntityType;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

//import com.krishagni.catissueplus.service.SpecimenService;

@Controller
@RequestMapping("/specimens")
public class SpecimenController {

	@Autowired
	private SpecimenService specimenSvc;

	@Autowired
	private FormService formSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/child-specimens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenInfo> getChildSpecimenList(@PathVariable("id") Long parentId) {
		ReqSpecimenSummaryEvent event = new ReqSpecimenSummaryEvent();
		event.setId(parentId);
		event.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
		return specimenSvc.getSpecimensList(event).getSpecimensInfo();
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
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getForms();
		}

		return null;
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
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getFormRecords();
		}

		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenDetail patchSpecimen(@PathVariable Long id, @RequestBody Map<String, Object> specimenProps) {
		PatchSpecimenEvent event = new PatchSpecimenEvent();
		SpecimenDetail detail = new SpecimenDetail();
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
		if (response.getStatus() == EventStatus.OK) {
			return response.getSpecimenDetail();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id}/aliquots")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenDetail> createAliquot(@PathVariable Long id, @RequestBody AliquotDetail aliquotDetail) {
		CreateAliquotEvent createAliquotEvent = new CreateAliquotEvent();
		createAliquotEvent.setAliquotDetail(aliquotDetail);
		createAliquotEvent.setSpecimenId(id);

		AliquotCreatedEvent response = specimenSvc.createAliquot(createAliquotEvent);
		if (response.getStatus() == EventStatus.OK) {
			return response.getAliquots();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenDetail createSpecimen(@RequestBody SpecimenDetail specimenDetail) {
		CreateSpecimenEvent createSpecimenEvent = new CreateSpecimenEvent();
		createSpecimenEvent.setSpecimenDetail(specimenDetail);
		createSpecimenEvent.setSessionDataBean(getSession());
		createSpecimenEvent.setScgId(specimenDetail.getScgId());

		SpecimenCreatedEvent response = specimenSvc.createSpecimen(createSpecimenEvent);
		if (response.getStatus() == EventStatus.OK) {
			return response.getSpecimenDetail();
		}

		return null;
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
