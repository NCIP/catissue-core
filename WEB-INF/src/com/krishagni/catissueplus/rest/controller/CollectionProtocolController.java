
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.krishagni.catissueplus.core.administrative.events.ChildCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqChildProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/collection-protocols")
public class CollectionProtocolController {

	@Autowired
	private CollectionProtocolService cpSvc;

	@Autowired
	private CollectionProtocolRegistrationService cprSvc;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<CollectionProtocolSummary> getCollectionProtocolList(
			@RequestParam(value = "chkPrivilege", required = false, defaultValue = "true") String chkPrivlege) {
		ReqAllCollectionProtocolsEvent req = new ReqAllCollectionProtocolsEvent();
		req.setSessionDataBean(getSession());
		req.setChkPrivileges("true".equalsIgnoreCase(chkPrivlege));

		AllCollectionProtocolsEvent resp = cpSvc.getAllProtocols(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getCpList();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/childProtocols")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<CollectionProtocolSummary> getChildProtocolsList(@PathVariable("id") Long id) {
		ReqChildProtocolEvent req = new ReqChildProtocolEvent();
		req.setSessionDataBean(getSession()); 
		req.setCpId(id);

		ChildCollectionProtocolsEvent result = cpSvc.getChildProtocols(req);
		return result.getChildProtocols();
	}

	/*	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
		@ResponseStatus(HttpStatus.OK)
		@ResponseBody
		public CollectionProtocolDetail getCollectionProtocolList(@PathVariable("id") Long id) {
			ReqCollProtocolDetailEvent event = new ReqCollProtocolDetailEvent();
			event.setId(id);
			event.setSessionDataBean((SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA));
			CollectionProtocolDetailEvent result = collectionProtocolService.getCollectionProtocol(event);
			return result.getCollectionProtocolDetail();
		}*/

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/participants")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ParticipantInfo> getParticipants(@PathVariable("id") Long cpId,
			@RequestParam(value = "query", required = false, defaultValue = "") String searchStr,
			@RequestParam(value = "pId", required = true, defaultValue = "0") String participantId) {
		ReqParticipantsSummaryEvent event = new ReqParticipantsSummaryEvent();
		event.setCpId(cpId);
		event.setSearchString(searchStr);
		event.setSessionDataBean(getSession());
		ParticipantsSummaryEvent resp = cpSvc.getParticipants(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getParticipantsInfo();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/participant")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantInfo getParticipant(@PathVariable("id") Long cpId,
			@RequestParam(value = "pId", required = true, defaultValue = "") String participantId) {
		ReqParticipantSummaryEvent event = new ReqParticipantSummaryEvent();
		event.setCpId(cpId);
		event.setParticipantId(StringUtils.isBlank(participantId) ? null : Long.valueOf(participantId));
		event.setSessionDataBean(getSession());
		ParticipantSummaryEvent resp = cpSvc.getParticipant(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getParticipantInfo();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id}/registrations")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CollectionProtocolRegistrationDetail register(@PathVariable("id") Long cpId,
			@RequestBody CollectionProtocolRegistrationDetail cprDetails) {
		CreateRegistrationEvent event = new CreateRegistrationEvent();
		event.setCpId(cpId);
		cprDetails.setCpId(cpId);
		event.setCprDetail(cprDetails);
		event.setSessionDataBean(getSession());
		RegistrationCreatedEvent resp = cprSvc.createRegistration(event);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		return resp.getCprDetail();
	}

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}

}
