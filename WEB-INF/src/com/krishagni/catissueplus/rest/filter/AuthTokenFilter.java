package com.krishagni.catissueplus.rest.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.events.TokenDetail;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

public class AuthTokenFilter extends GenericFilterBean {
	private static final String OS_AUTH_TOKEN_HDR = "X-OS-API-TOKEN";
	
	private static final String OS_CLIENT_HDR = "X-OS-API-CLIENT";
	
	private static final String BASIC_AUTH = "Basic ";
	
	private static final String DEFAULT_AUTH_DOMAIN = "openspecimen";
	
	private UserAuthenticationService authService;
	
	private Map<String, List<String>> excludeUrls;
	
	public UserAuthenticationService getAuthService() {
		return authService;
	}

	public void setAuthService(UserAuthenticationService authService) {
		this.authService = authService;
	}

	public Map<String, List<String>> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(Map<String, List<String>> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
	throws IOException, ServletException {
		if (!(req instanceof HttpServletRequest)) {
			throw new IllegalAccessError("Unknown protocol request");
		}
		
		HttpServletRequest httpReq = (HttpServletRequest)req;
		HttpServletResponse httpResp = (HttpServletResponse)resp;
		
		httpResp.setHeader("Access-Control-Allow-Origin", "http://localhost:9000");
		httpResp.setHeader("Access-Control-Allow-Credentials", "true");
		httpResp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		httpResp.setHeader("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, X-OS-API-TOKEN, X-OS-API-CLIENT");
		
		if (httpReq.getMethod().equalsIgnoreCase("options")) {
			httpResp.setStatus(HttpServletResponse.SC_OK);	
			return;
		}

		List<String> urls = excludeUrls.get(httpReq.getMethod());
		if (urls == null) {
			urls = Collections.emptyList();
		}
		
		for (String url : urls) {
			if (httpReq.getRequestURI().endsWith(url)) {  
				chain.doFilter(req, resp);
				return;
			}
		}

		User userDetails = null;
		String authToken = httpReq.getHeader(OS_AUTH_TOKEN_HDR);
		if (authToken != null) {
			TokenDetail tokenDetail = new TokenDetail();
			tokenDetail.setToken(authToken);
			tokenDetail.setIpAddress(httpReq.getRemoteAddr());
			
			SessionDataBean sdb = (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
			RequestEvent<TokenDetail> atReq = new RequestEvent<TokenDetail>(sdb, tokenDetail);
			
			ResponseEvent<User> atResp = authService.validateToken(atReq);
			if (atResp.isSuccessful()) {
				userDetails = atResp.getPayload();
			}
		} else if(httpReq.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			userDetails = doBasicAuthentication(httpReq, httpResp);
		}
		
		if (userDetails == null) {
			String clientHdr = httpReq.getHeader(OS_CLIENT_HDR);
                        if (clientHdr != null && clientHdr.equals("webui")) {
				setUnauthorizedResp(httpResp);
			} else {
				setRequireAuthResp(httpResp);
			}
			return;
		}
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, authToken, userDetails.getAuthorities());
		token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpReq));
		SecurityContextHolder.getContext().setAuthentication(token);
		chain.doFilter(req, resp);
		SecurityContextHolder.clearContext();
	}
	
	private User doBasicAuthentication(HttpServletRequest httpReq, HttpServletResponse httpResp) throws UnsupportedEncodingException {
		String header = httpReq.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith(BASIC_AUTH)) {
			return null;
		}

		byte[] base64Token = header.substring(BASIC_AUTH.length()).getBytes();
		String[] parts = new String(Base64.decode(base64Token)).split(":");
		if (parts.length != 2) {
			return null;
		}

		LoginDetail detail = new LoginDetail();
		detail.setLoginName(parts[0]);
		detail.setPassword(parts[1]);
		detail.setIpAddress(httpReq.getRemoteAddr());
		detail.setDomainName(DEFAULT_AUTH_DOMAIN);
		detail.setDoNotGenerateToken(true);

		SessionDataBean sdb = (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
		RequestEvent<LoginDetail> req = new RequestEvent<LoginDetail>(sdb, detail);
		ResponseEvent<Map<String, Object>> resp = authService.authenticateUser(req);
		if (resp.isSuccessful()) {
			return (User)resp.getPayload().get("user");
		}

		return null;
	}

	private void setRequireAuthResp(HttpServletResponse httpResp) throws IOException {
		httpResp.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"OpenSpecimen\"");
		setUnauthorizedResp(httpResp);
	}

	private void setUnauthorizedResp(HttpServletResponse httpResp) throws IOException {
		httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"You must supply valid credentials to access the OpenSpecimen REST API");
	}

}
