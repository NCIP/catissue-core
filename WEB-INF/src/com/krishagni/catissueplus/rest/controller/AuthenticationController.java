
package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/sessions")
public class AuthenticationController {
	
	@Autowired
	private UserAuthenticationService userAuthService;

	@Autowired
	private HttpServletRequest httpReq;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> authenticate(@RequestBody LoginDetail loginDetail) {
		loginDetail.setIpAddress(httpReq.getRemoteAddr());
		loginDetail.setApiUrl(httpReq.getRequestURI());
		loginDetail.setRequestMethod(RequestMethod.POST.name());
		RequestEvent<LoginDetail> req = new RequestEvent<LoginDetail>(loginDetail);
		ResponseEvent<Map<String, Object>> resp = userAuthService.authenticateUser(req);
		resp.throwErrorIfUnsuccessful();
		
		User user = (User) resp.getPayload().get("user");
		Map<String, Object> detail = new HashMap<String, Object>();
		detail.put("id", user.getId());
		detail.put("firstName", user.getFirstName());
		detail.put("lastName", user.getLastName());
		detail.put("loginName", user.getLoginName());
		detail.put("token", (String)resp.getPayload().get("token"));
		detail.put("admin", user.isAdmin());
		detail.put("instituteAdmin", user.isInstituteAdmin());
		
		return detail;
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, String> delete(HttpServletResponse httpResp) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		RequestEvent<String> req = new RequestEvent<String>((String)auth.getCredentials());
		ResponseEvent<String> resp = userAuthService.removeToken(req);
		resp.throwErrorIfUnsuccessful();

		return Collections.singletonMap("Status", resp.getPayload());
	}
}
