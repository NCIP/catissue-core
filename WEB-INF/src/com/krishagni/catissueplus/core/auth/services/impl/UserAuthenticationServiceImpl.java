
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.auth.AuthConfig;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.domain.AuthToken;
import com.krishagni.catissueplus.core.auth.domain.LoginAuditLog;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.events.TokenDetail;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class UserAuthenticationServiceImpl implements UserAuthenticationService {
	private DaoFactory daoFactory;
	
	private AuditService auditService;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Map<String, Object>> authenticateUser(RequestEvent<LoginDetail> req) {
		LoginDetail loginDetail = req.getPayload();
		User user = null;
		try {
			user = daoFactory.getUserDao().getUser(loginDetail.getLoginName(), loginDetail.getDomainName());
			
			if (user == null) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
			}
			
			if (user.getActivityStatus().equals(Status.ACTIVITY_STATUS_LOCKED.getStatus())) {
				throw OpenSpecimenException.userError(AuthErrorCode.USER_LOCKED);
			}
			
			if (!user.isEnabled()) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
			}
			
			AuthenticationService authService = user.getAuthDomain().getAuthProviderInstance();
			authService.authenticate(loginDetail.getLoginName(), loginDetail.getPassword());
			
			LoginAuditLog loginAuditLog = insertLoginAudit(user, loginDetail.getIpAddress(), true);
			Map<String, Object> authDetail = new HashMap<String, Object>();
			authDetail.put("user", user);
 
			if (!loginDetail.isDoNotGenerateToken()) {
				String token = UUID.randomUUID().toString();
				
				AuthToken authToken = new AuthToken();
				authToken.setIpAddress(loginDetail.getIpAddress());
				authToken.setToken(token);
				authToken.setUser(user);
				authToken.setLoginAuditLog(loginAuditLog);
				daoFactory.getAuthDao().saveAuthToken(authToken);
				
				insertUserAuditLog(loginDetail, user, token);
				authDetail.put("token", AuthUtil.encodeToken(token));
			}
			
			return ResponseEvent.response(authDetail);
		} catch (OpenSpecimenException ose) {
			if (user != null && user.isEnabled()) {
				insertLoginAudit(user, loginDetail.getIpAddress(), false);
				checkFailedLoginAttempt(user, loginDetail.getIpAddress());
			}
			return ResponseEvent.error(ose, true);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@PlusTransactional
	public ResponseEvent<User> validateToken(RequestEvent<TokenDetail> req) {
		try {
			TokenDetail tokenDetail = req.getPayload();
			String token = AuthUtil.decodeToken(tokenDetail.getToken());

			AuthToken authToken = daoFactory.getAuthDao().getAuthTokenByKey(token);
			if (authToken == null) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_TOKEN);
			}
			
			User user = authToken.getUser();
			long timeSinceLastApiCall = auditService.getTimeSinceLastApiCall(user.getId(), token);
			int sessionExpiryTimeInterval = AuthConfig.getInstance().getSessionExpiryTimeInterval();
			if (timeSinceLastApiCall > sessionExpiryTimeInterval) {
				daoFactory.getAuthDao().deleteAuthToken(authToken);
				throw OpenSpecimenException.userError(AuthErrorCode.TOKEN_EXPIRED);
			}

			if (AuthConfig.getInstance().isTokenIpVerified()) {
				if (!tokenDetail.getIpAddress().equals(authToken.getIpAddress())) {
					throw OpenSpecimenException.userError(AuthErrorCode.IP_ADDRESS_CHANGED);
				}
			}

			if (!Hibernate.isInitialized(user)) {
				Hibernate.initialize(user);
			}
			
			return ResponseEvent.response(user);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@PlusTransactional
	public ResponseEvent<UserSummary> getCurrentLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)auth.getPrincipal();
		return ResponseEvent.response(UserSummary.from(user));
	}

	@PlusTransactional
	public ResponseEvent<String> removeToken(RequestEvent<String> req) {
		String userToken = AuthUtil.decodeToken(req.getPayload());
		try {
			AuthToken token = daoFactory.getAuthDao().getAuthTokenByKey(userToken);
			LoginAuditLog loginAuditLog = token.getLoginAuditLog();
			loginAuditLog.setLogoutTime(Calendar.getInstance().getTime());
			
			daoFactory.getAuthDao().deleteAuthToken(token);
			return ResponseEvent.response("Success");
		} catch (Exception e) {	
			return ResponseEvent.serverError(e);
		}
	}
	
	@Scheduled(cron="0 0 12 ? * *")
	@PlusTransactional
	public void deleteExpiredTokens() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -AuthConfig.getInstance().getSessionExpiryTimeInterval());
		daoFactory.getAuthDao().deleteExpiredAuthToken(cal.getTime());
	}
	
	private LoginAuditLog insertLoginAudit(User user, String ipAddress, boolean loginSuccessful) {
		LoginAuditLog loginAuditLog = new LoginAuditLog();
		loginAuditLog.setUser(user);
		loginAuditLog.setIpAddress(ipAddress);
		loginAuditLog.setLoginTime(Calendar.getInstance().getTime());
		loginAuditLog.setLogoutTime(null);
		loginAuditLog.setLoginSuccessful(loginSuccessful); 
		
		daoFactory.getAuthDao().saveLoginAuditLog(loginAuditLog);
		return loginAuditLog;
	}
	
	private void checkFailedLoginAttempt(User user, String ipAddress) {
		int failedLoginAttempts = AuthConfig.getInstance().getAllowedFailedLoginAttempts();
		List<LoginAuditLog> logs = daoFactory.getAuthDao()
			.getLoginAuditLogsByUser(user.getId(), failedLoginAttempts);
		
		if (logs.size() < failedLoginAttempts) {
			return;
		}
		
		for (LoginAuditLog log: logs) {
			if (log.isLoginSuccessful()) {
				return;
			}
		}
		
		user.setActivityStatus(Status.ACTIVITY_STATUS_LOCKED.getStatus());
	}
	
	private void insertUserAuditLog(LoginDetail loginDetail, User user, String token) {
		UserApiCallLog userAuditLog = new UserApiCallLog();
		userAuditLog.setUser(user);
		userAuditLog.setUrl(loginDetail.getApiUrl());
		userAuditLog.setMethod(loginDetail.getRequestMethod());
		userAuditLog.setAuthToken(token);
		userAuditLog.setResponseCode(Integer.toString(HttpStatus.OK.value()));
		userAuditLog.setCallStartTime(Calendar.getInstance().getTime());
		userAuditLog.setCallEndTime(Calendar.getInstance().getTime());
		auditService.insertUserApiCallLog(userAuditLog);
	}
}
