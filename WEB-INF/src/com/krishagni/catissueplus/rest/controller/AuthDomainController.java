
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

import com.krishagni.catissueplus.core.auth.events.AuthDomainDetail;
import com.krishagni.catissueplus.core.auth.events.AuthDomainSummary;
import com.krishagni.catissueplus.core.auth.events.ListAuthDomainCriteria;
import com.krishagni.catissueplus.core.auth.services.DomainRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

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
	public List<AuthDomainSummary> getAuthDomains(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") 
			int maxResults) {
		
		ListAuthDomainCriteria crit = new ListAuthDomainCriteria().maxResults(maxResults);
		RequestEvent<ListAuthDomainCriteria> req = new RequestEvent<ListAuthDomainCriteria>(crit);
		ResponseEvent<List<AuthDomainSummary>> resp = domainRegService.getDomains(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AuthDomainDetail registerDomain(@RequestBody AuthDomainDetail domainDetail) {
		RequestEvent<AuthDomainDetail> req = new RequestEvent<AuthDomainDetail>(domainDetail);
		ResponseEvent<AuthDomainDetail> resp = domainRegService.registerDomain(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AuthDomainDetail updaterDomain(@PathVariable Long id, @RequestBody AuthDomainDetail domainDetail) {
		domainDetail.setId(id);
		RequestEvent<AuthDomainDetail> req = new RequestEvent<AuthDomainDetail>(domainDetail);
		ResponseEvent<AuthDomainDetail> resp = domainRegService.updateDomain(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
}
