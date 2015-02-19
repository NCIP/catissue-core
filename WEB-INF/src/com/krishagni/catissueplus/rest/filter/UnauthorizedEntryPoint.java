package com.krishagni.catissueplus.rest.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	public void commence(HttpServletRequest req, HttpServletResponse resp,	AuthenticationException ae) 
	throws IOException, ServletException {
		String msg = ae.getMessage();
		resp.sendError(
				HttpServletResponse.SC_UNAUTHORIZED,
				msg == null ? "Unauthorized: Authentication token was either missing or invalid." : msg);
	}
}
	