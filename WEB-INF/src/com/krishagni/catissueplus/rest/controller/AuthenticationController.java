
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

import com.krishagni.catissueplus.core.auth.events.AuthenticateUserEvent;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;
import com.krishagni.catissueplus.core.auth.events.UserAuthenticatedEvent;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

	@Autowired
	private UserAuthenticationService userAuthService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String authenticateUser(@RequestBody LoginDetails loginDetails) {
		UserAuthenticatedEvent resp = userAuthService.authenticateUser(new AuthenticateUserEvent(loginDetails));
		if (resp.getStatus() == EventStatus.OK) {
			return resp.getMessage();
		}
		return null;
	}
}
