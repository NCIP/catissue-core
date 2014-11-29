
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.VisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
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

@Controller
@RequestMapping("/collection-protocol-registrations")
public class CollectionProtocolRegistrationController {

	@Autowired
	private CollectionProtocolRegistrationService cprSvc;

	@Autowired
	private CollectionProtocolService cpSvc;

	@Autowired
	private FormService formSvc;

	@Autowired
	private HttpServletRequest httpReq;
	

	@RequestMapping(method = RequestMethod.GET, value = "/{cprId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail getRegistration(@PathVariable("cprId") Long cprId) {
		ReqRegistrationEvent req = new ReqRegistrationEvent();
		req.setCprId(cprId);
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getCpr();		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail register(@RequestBody CollectionProtocolRegistrationDetail cprDetails) {				
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(cprDetails.getCpId());		
		req.setCprDetail(cprDetails);
		req.setSessionDataBean(getSession());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getCprDetail();
	}
	

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/visits")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<VisitSummary> getVisits(
			@PathVariable("id") Long cprId,
			@RequestParam(value = "includeStats", required = false, defaultValue = "false") boolean includeStats) {
		
		ReqVisitsEvent req = new ReqVisitsEvent();
		req.setCprId(cprId);
		req.setIncludeStats(includeStats);
		req.setSessionDataBean(getSession());
				
		VisitsEvent resp = cprSvc.getVisits(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getVisits();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/forms")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormCtxtSummary> getForms(@PathVariable("id") Long cprId) {
		ReqEntityFormsEvent req = new ReqEntityFormsEvent();
		req.setEntityId(cprId);
		req.setEntityType(EntityType.COLLECTION_PROTOCOL_REGISTRATION);
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
	public List<FormRecordSummary> getFormRecords(@PathVariable("id") Long cprId,
			@PathVariable("formCtxtId") Long formCtxtId) {

		ReqEntityFormRecordsEvent req = new ReqEntityFormRecordsEvent();
		req.setEntityId(cprId);
		req.setFormCtxtId(formCtxtId);
		req.setSessionDataBean(getSession());

		EntityFormRecordsEvent resp = formSvc.getEntityFormRecords(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getFormRecords();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Long delete(@PathVariable Long id,
			@RequestParam(value = "includeChildren", required = false, defaultValue = "false") String includeChildren) {
		DeleteRegistrationEvent event = new DeleteRegistrationEvent();
		event.setSessionDataBean(getSession());
		event.setId(id);
		event.setIncludeChildren(Boolean.valueOf(includeChildren));
		RegistrationDeletedEvent resp = cprSvc.delete(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getId();
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail patchRegistration(@PathVariable Long id,
			@RequestBody Map<String, Object> regProps) {
		PatchRegistrationEvent event = new PatchRegistrationEvent();
		CollectionProtocolRegistrationPatchDetail detail = new CollectionProtocolRegistrationPatchDetail();
		try {
			BeanUtils.populate(detail, regProps);
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
		event.setCollectionProtocolRegistrationDetail(detail);
		RegistrationUpdatedEvent resp = cprSvc.patchRegistration(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getCprDetail();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
