
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.auth.events.AllDomainsEvent;
import com.krishagni.catissueplus.core.auth.events.DomainDetails;
import com.krishagni.catissueplus.core.auth.events.DomainRegisteredEvent;
import com.krishagni.catissueplus.core.auth.events.RegisterDomainEvent;
import com.krishagni.catissueplus.core.auth.events.ReqAllAuthDomainEvent;
import com.krishagni.catissueplus.core.auth.services.DomainRegistrationService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

@Controller
@RequestMapping("/auth-domains")
public class AuthDomainController {

	@Autowired
	private DomainRegistrationService domainRegService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DomainDetails> getAllDomainDetails(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") String maxResults) {
		ReqAllAuthDomainEvent req = new ReqAllAuthDomainEvent();
		req.setMaxResults(Integer.parseInt(maxResults));
		AllDomainsEvent resp = domainRegService.getAllDomains(req);
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getDomains();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DomainDetails registerDomain(@RequestBody DomainDetails domainDetails) {
		DomainRegisteredEvent resp = domainRegService.registerDomain(new RegisterDomainEvent(domainDetails));
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getDomainDetails();
		}
		return null;
	}

}