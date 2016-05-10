package com.krishagni.catissueplus.rest.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.events.TokenDetail;
import com.krishagni.catissueplus.core.auth.services.impl.UserAuthenticationServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class SamlFilter extends FilterChainProxy {
	private DaoFactory daoFactory;
	
	private UserAuthenticationServiceImpl authService;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setAuthService(UserAuthenticationServiceImpl authService) {
		this.authService = authService;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
	throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;
		
		try {
			boolean samlEnabled = enableSaml();
			if (samlEnabled && !isAuthenticated(httpReq)) {
				super.doFilter(request, response, chain); 
			} else {
				httpResp.sendRedirect(httpReq.getContextPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			httpResp.sendRedirect(httpReq.getContextPath());
		}
	}
	
	@SuppressWarnings({"deprecation" })
	public void setFilterChain(Map<String, Filter> filters) {
		Map<RequestMatcher, List<Filter>> filterChainMap = new HashMap<>();
		for (Map.Entry<String, Filter> entry : filters.entrySet()) {
			filterChainMap.put(new AntPathRequestMatcher(entry.getKey()), Collections.singletonList(entry.getValue()));
		}
		
		setFilterChainMap(filterChainMap);
	}

	@PlusTransactional
	private boolean enableSaml() {
		boolean samlEnabled = ConfigUtil.getInstance().getBoolSetting("auth", "saml_enable", false);
		if (!samlEnabled) {
			return false;
		}

		//
		// TODO: This is assuming there will be only one SAML domain
		//
		AuthDomain domain = daoFactory.getAuthDao().getAuthDomainByType("saml");
		if (domain != null) {
			//
			// This intialises SAML auth provider
			//
			domain.getAuthProviderInstance();
		}

		return samlEnabled;
	}

	private boolean isAuthenticated(HttpServletRequest httpReq) {
		String authToken = AuthUtil.getTokenFromCookie(httpReq);
		if (authToken == null) {
			return false;
		}

		TokenDetail tokenDetail = new TokenDetail();
		tokenDetail.setToken(authToken);
		tokenDetail.setIpAddress(httpReq.getRemoteAddr());

		RequestEvent<TokenDetail> atReq = new RequestEvent<>(tokenDetail);
		ResponseEvent<User> atResp = authService.validateToken(atReq);
		return atResp.isSuccessful();
	}
}
