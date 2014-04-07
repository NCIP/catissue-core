package com.krishagni.catissueplus.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.auth.events.AddLdapEvent;
import com.krishagni.catissueplus.core.auth.events.LdapAddedEvent;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;
import com.krishagni.catissueplus.core.auth.services.LdapRegistrationService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

@Controller
@RequestMapping("/ldaps")
public class LdapController {

	@Autowired
	private LdapRegistrationService ldapRegService;
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public LdapDetails addLDAP(@RequestBody LdapDetails ldapDetails) {
		LdapAddedEvent resp = ldapRegService.addLdap(new AddLdapEvent(ldapDetails));
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getLdapDetails();
		}		
		return null;
	}
	
}