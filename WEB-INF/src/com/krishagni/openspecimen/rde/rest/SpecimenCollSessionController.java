package com.krishagni.openspecimen.rde.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.rde.events.SpecimenCollSessionDetail;
import com.krishagni.openspecimen.rde.services.SpecimenCollSessionService;

@Controller
@RequestMapping("/specimen-collection-sessions")
public class SpecimenCollSessionController {
	@Autowired
	private SpecimenCollSessionService specimenCollSessionSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SpecimenCollSessionDetail> getSessions() {
		ResponseEvent<List<SpecimenCollSessionDetail>> resp = specimenCollSessionSvc.getSessions();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenCollSessionDetail getSession(@PathVariable Long id) {
		ResponseEvent<List<SpecimenCollSessionDetail>> resp = specimenCollSessionSvc.getSessions();
		resp.throwErrorIfUnsuccessful();

		for (SpecimenCollSessionDetail session : resp.getPayload()) {
			if (session.getId().equals(id)) {
				return session;
			}
		}

		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenCollSessionDetail saveSession(@RequestBody SpecimenCollSessionDetail detail) {
		ResponseEvent<SpecimenCollSessionDetail> resp = specimenCollSessionSvc.createSession(new RequestEvent<SpecimenCollSessionDetail>(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SpecimenCollSessionDetail updateSession(@PathVariable Long id, @RequestBody SpecimenCollSessionDetail detail) {
		detail.setId(id);
		ResponseEvent<SpecimenCollSessionDetail> resp = specimenCollSessionSvc.updateSession(new RequestEvent<SpecimenCollSessionDetail>(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean deleteSession(@PathVariable Long id) {
		ResponseEvent<Boolean> resp = specimenCollSessionSvc.deleteSession(new RequestEvent<Long>(id));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
