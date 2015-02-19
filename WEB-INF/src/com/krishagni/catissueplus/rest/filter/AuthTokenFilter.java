package com.krishagni.catissueplus.rest.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

public class AuthTokenFilter extends GenericFilterBean {
	private static final String OS_AUTH_TOKEN_HDR = "X-OS-API-TOKEN";
	
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
		httpResp.setHeader("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, X-OS-API-TOKEN");			
		
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
		
		String authToken = httpReq.getHeader(OS_AUTH_TOKEN_HDR);

		if (authToken == null) {
			httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED,	"Missing api token from API request");
			return;
		}
		
		Map<String, Object> tokenDetail = new HashMap<String, Object>();
		tokenDetail.put("token", authToken);
		tokenDetail.put("IpAddress", httpReq.getRemoteAddr());
		
		SessionDataBean sdb = (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
		RequestEvent<Map<String, Object>> atReq = new RequestEvent<Map<String, Object>>(sdb, tokenDetail);
		
		ResponseEvent<User> atResp = authService.validateToken(atReq);
		atResp.throwErrorIfUnsuccessful();
		
		User userDetails = atResp.getPayload();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, authToken, userDetails.getAuthorities());
		token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpReq));
		SecurityContextHolder.getContext().setAuthentication(token);
		chain.doFilter(req, resp);
		SecurityContextHolder.clearContext();
	}
	
}
