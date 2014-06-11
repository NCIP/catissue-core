
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

import com.java.common.CacheControl;
import com.java.common.CachePolicy;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantInfo;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

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
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public List<CollectionProtocolSummary> getCollectionProtocolList() {
		ReqAllCollectionProtocolsEvent req = new ReqAllCollectionProtocolsEvent();
		req.setSessionDataBean(getSession());

		AllCollectionProtocolsEvent result = cpSvc.getAllProtocols(req);
		return result.getCpList();
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
	@CacheControl(policy = {CachePolicy.NO_STORE, CachePolicy.NO_CACHE})
	@ResponseBody
	public List<ParticipantInfo> getParticipants(@PathVariable("id") Long cpId,
			@RequestParam(value = "query", required = false, defaultValue = "") String searchStr) {
		ReqParticipantsSummaryEvent event = new ReqParticipantsSummaryEvent();
		event.setCpId(cpId);
		event.setSearchString(searchStr);
		event.setSessionDataBean(getSession());
		ParticipantsSummaryEvent result = cpSvc.getParticipants(event);
		return result.getParticipantsInfo();
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
		RegistrationCreatedEvent result = cprSvc.createRegistration(event);
		if (result.getStatus() == EventStatus.OK) {
			return result.getCprDetail();
		}
		return null;
	}
	

	private SessionDataBean getSession() {
		return (SessionDataBean) httpServletRequest.getSession().getAttribute(Constants.SESSION_DATA);
	}

}
