
package com.krishagni.catissueplus.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.auth.events.LoginDetails;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;

@Controller
@RequestMapping("/sessions")
public class AuthenticationController {

	@Autowired
	private UserAuthenticationService userAuthService;

	@Autowired
	private HttpServletRequest httpReq;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, String> authenticateUser(@RequestBody LoginDetails loginDetails) 
	throws Exception {
		User user = authenticate(httpReq, loginDetails.getLoginId(), loginDetails.getPassword());
		if (user == null) {
			return null;
		}
		
		SessionDataBean sdb = setSessionDataBean(user, httpReq.getRemoteAddr());
		httpReq.getSession().setAttribute(Constants.SESSION_DATA, sdb);
		
		Map<String, String> userProps = new HashMap<String, String>();
		userProps.put("firstName", user.getFirstName());
		userProps.put("lastName", user.getLastName());
		userProps.put("loginName", user.getLoginName());
		userProps.put("token", httpReq.getSession().getId());
		return userProps;
	}
	
	private User authenticate(HttpServletRequest request, String user, String password)
	throws AuthenticationException, ApplicationException, CatissueException
	{
		LoginCredentials loginCredentials = new LoginCredentials();
		loginCredentials.setLoginName(user);
		loginCredentials.setPassword(password);
		
		LoginResult loginResult = CatissueLoginProcessor.processUserLogin(request, loginCredentials);
		if (loginResult.isAuthenticationSuccess()) {
			return CatissueLoginProcessor.getUser(loginResult.getAppLoginName());
		}

		return null;
	}
	
	private SessionDataBean setSessionDataBean(User validUser, String ipAddress)
	throws CatissueException {
		final SessionDataBean sessionData = new SessionDataBean();
		sessionData.setAdmin(isAdminUser(validUser.getRoleId()));
		sessionData.setUserName(validUser.getLoginName());
		sessionData.setIpAddress(ipAddress);
		sessionData.setUserId(validUser.getId());
		sessionData.setFirstName(validUser.getFirstName());
		sessionData.setLastName(validUser.getLastName());
		sessionData.setCsmUserId(validUser.getCsmUserId().toString());
		return sessionData;
	}

	private boolean isAdminUser(final String userRole) {
		return userRole.equalsIgnoreCase(Constants.ADMIN_USER);
	}	
}
