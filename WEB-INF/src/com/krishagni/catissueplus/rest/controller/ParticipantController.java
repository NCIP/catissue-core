
package com.krishagni.catissueplus.rest.controller;

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

import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipants;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/participants")
public class ParticipantController {
	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private ParticipantService participantSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail getParticipantById(@PathVariable("id") Long participantId) {
		ResponseEvent<ParticipantDetail> resp = participantSvc.getParticipant(getRequest(participantId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail createParticipant(@RequestBody ParticipantDetail participantDetail) {
		ResponseEvent<ParticipantDetail> resp = participantSvc.createParticipant(getRequest(participantDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail updateParticipant(@PathVariable Long id, @RequestBody ParticipantDetail participantDetail) {
		ResponseEvent<ParticipantDetail> resp = participantSvc.updateParticipant(getRequest(participantDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ParticipantDetail delete(@PathVariable Long id,
		@RequestParam(value = "includeChildren", required = false, defaultValue = "false") 
		String includeChildren) {
		
		ResponseEvent<ParticipantDetail> resp = participantSvc.delete(getRequest(id));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/match")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public MatchedParticipants getMatchedParticipants(@RequestBody ParticipantDetail criteria) {
		ResponseEvent<MatchedParticipants> resp = participantSvc.getMatchingParticipants(getRequest(criteria));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}	
}
