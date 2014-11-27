package com.krishagni.catissueplus.rest.controller;

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

import com.krishagni.catissueplus.core.administrative.events.CreateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.GetInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;
import com.krishagni.catissueplus.core.administrative.events.InstituteDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteGotEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/institutes")
public class InstituteController {

	@Autowired
	private InstituteService instituteSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public InstituteDetails createInstitute(
			@RequestBody InstituteDetails instituteDetails) {
		CreateInstituteEvent event = new CreateInstituteEvent();
		event.setSessionDataBean(getSession());
		event.setInstituteDetails(instituteDetails);
		InstituteCreatedEvent resp = instituteSvc.createInstitute(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getInstituteDetails();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{instituteId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstituteDetails updateInstitute(@PathVariable Long instituteId,
			@RequestBody InstituteDetails instituteDetails) {
		UpdateInstituteEvent event = new UpdateInstituteEvent();
		event.setInstituteDetails(instituteDetails);
		event.setId(instituteId);
		InstituteUpdatedEvent resp = instituteSvc.updateInstitute(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getInstituteDetails();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String disableInst(@PathVariable Long id) {
		DisableInstituteEvent event = new DisableInstituteEvent();
		event.setId(id);
		event.setSessionDataBean(getSession());
		InstituteDisabledEvent resp = instituteSvc.deleteInstitute(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE, value = "/name={name}")
	@ResponseStatus(HttpStatus.OK)
	public String disableInstitute(@PathVariable String name) {
		DisableInstituteEvent event = new DisableInstituteEvent();
		event.setName(name);
		event.setSessionDataBean(getSession());
		InstituteDisabledEvent resp = instituteSvc.deleteInstitute(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getMessage();
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<InstituteDetails> getInstitutes(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		ReqAllInstitutesEvent event  = new ReqAllInstitutesEvent();
		event.setMaxResults(Integer.parseInt(maxResults));
		GetAllInstitutesEvent resp = instituteSvc.getInstitutes(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstituteDetails getInstitute(@PathVariable Long id) {
		GetInstituteEvent event = new GetInstituteEvent();
		event.setInstId(id);
		InstituteGotEvent resp = instituteSvc.getInstitute(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/name={name}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstituteDetails getInstitute(@PathVariable String name) {
		GetInstituteEvent event = new GetInstituteEvent();
		event.setName(name);
		InstituteGotEvent resp = instituteSvc.getInstitute(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
			return resp.getDetails();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(
				Constants.SESSION_DATA);
	}
}
