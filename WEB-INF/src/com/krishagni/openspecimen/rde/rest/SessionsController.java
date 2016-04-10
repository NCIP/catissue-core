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

import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.rde.domain.RdeError;
import com.krishagni.openspecimen.rde.domain.SessionError;
import com.krishagni.openspecimen.rde.events.SessionDetail;
import com.krishagni.openspecimen.rde.services.SessionService;

@Controller
@RequestMapping("/rde-sessions")
public class SessionsController {

	@Autowired
	private SessionService sessionSvc;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<SessionDetail> getSessions() {
		return response(sessionSvc.getSessions());
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SessionDetail getSession(@PathVariable Long id) {
		ResponseEvent<SessionDetail> resp = sessionSvc.getSession(request(id));
		if (resp.isSuccessful()) {
			return resp.getPayload();
		}

		ErrorCode error = resp.getError().getErrors().iterator().next().error();
		if (error.equals(SessionError.NOT_FOUND)) {
			return null;
		}

		throw resp.getError();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SessionDetail saveSession(@RequestBody SessionDetail detail) {
		return response(sessionSvc.createSession(request(detail)));
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public SessionDetail updateSession(@PathVariable Long id, @RequestBody SessionDetail detail) {
		detail.setId(id);
		return response(sessionSvc.updateSession(request(detail)));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Boolean deleteSession(@PathVariable Long id) {
		return response(sessionSvc.deleteSession(request(id)));
	}

	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<>(payload);
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
