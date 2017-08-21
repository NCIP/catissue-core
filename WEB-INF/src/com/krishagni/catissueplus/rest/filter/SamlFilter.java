package com.krishagni.catissueplus.rest.filter;

import java.io.IOException;
import java.util.Arrays;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.krishagni.catissueplus.core.auth.domain.AuthDomain;
import com.krishagni.catissueplus.core.auth.domain.AuthToken;
import com.krishagni.catissueplus.core.auth.events.TokenDetail;
import com.krishagni.catissueplus.core.auth.services.impl.UserAuthenticationServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class SamlFilter extends FilterChainProxy {
	private static final Log logger = LogFactory.getLog(SamlFilter.class);

	private static final String SHOW_ERROR = "/#/alert";

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
		String appUrl = ConfigUtil.getInstance().getAppUrl();
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

		try {
			boolean samlEnabled = enableSaml();
			if (samlEnabled && !isAuthenticated(httpReq)) {
				super.doFilter(request, response, chain);
			} else {
				httpResp.sendRedirect(appUrl + "/");
			}
		} catch (UsernameNotFoundException use) {
			httpResp.sendRedirect(appUrl + SHOW_ERROR + "?redirectTo=login&type=danger&msg=" + use.getMessage());
		} catch (Exception e) {
			logger.error("Error doing SAML based authentication", e);
			httpResp.sendRedirect(appUrl + SHOW_ERROR + "?redirectTo=login&type=danger&msg=" + e.getMessage());
		}
	}
	
	@SuppressWarnings({"deprecation" })
	public void setFilterChain(Filter generatorFilter, Map<String, Filter> filters) {
		Map<RequestMatcher, List<Filter>> filterChainMap = new HashMap<>();
		for (Map.Entry<String, Filter> entry : filters.entrySet()) {
			filterChainMap.put(new AntPathRequestMatcher(entry.getKey()), Arrays.asList(generatorFilter, entry.getValue()));
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
		ResponseEvent<AuthToken> atResp = authService.validateToken(atReq);
		return atResp.isSuccessful();
	}
}
