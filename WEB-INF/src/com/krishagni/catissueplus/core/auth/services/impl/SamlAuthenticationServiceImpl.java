package com.krishagni.catissueplus.core.auth.services.impl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.RequestMethod;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.SamlBootstrap;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

@Configurable
public class SamlAuthenticationServiceImpl extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationService {

	private static final String OS_AUTH_TOKEN = "osAuthToken";

	@Autowired
	private UserAuthenticationServiceImpl userAuthService;

	public void setUserAuthService(UserAuthenticationServiceImpl userAuthService) {
		this.userAuthService = userAuthService;
	}

	public SamlAuthenticationServiceImpl() {
		
	}
	
	public SamlAuthenticationServiceImpl(Map<String, String> props) {
		SamlBootstrap samlBootStrap = new SamlBootstrap(this, props);

		//calling initialize after all beans are injected
		samlBootStrap.initialize();
	}
	
	@Override
	public void authenticate(String username, String password) {
		throw OpenSpecimenException.serverError(new UnsupportedOperationException("Not supported for this implementation"));
	}

	@Override
	@PlusTransactional
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth)
	throws IOException, ServletException {

		User user = (User) auth.getPrincipal();

		LoginDetail loginDetail = new LoginDetail();
		loginDetail.setIpAddress(req.getRemoteAddr());
		loginDetail.setApiUrl(req.getRequestURI());
		loginDetail.setRequestMethod(RequestMethod.POST.name());

		String encodedToken = userAuthService.generateToken(user, loginDetail);
		Cookie cookie = new Cookie(OS_AUTH_TOKEN, encodedToken);
		cookie.setMaxAge(-1);
		cookie.setPath(req.getContextPath());
		resp.addCookie(cookie);
	
		getRedirectStrategy().sendRedirect(req, resp, getDefaultTargetUrl());
	}

}
