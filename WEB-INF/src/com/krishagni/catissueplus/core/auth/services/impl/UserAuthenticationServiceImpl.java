
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.domain.AuthToken;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	@Autowired
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Map<String, Object>> authenticateUser(RequestEvent<LoginDetail> req) {
		try {
			LoginDetail loginDetail = req.getPayload();
			User user = daoFactory.getUserDao().getUser(loginDetail.getLoginName(), loginDetail.getDomainName());
			if (user == null) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
			}
			
			AuthenticationService authService = user.getAuthDomain().getAuthProviderInstance();
			authService.authenticate(loginDetail.getLoginName(), loginDetail.getPassword());

			String token = UUID.randomUUID().toString();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 8); // valid for 8 hrs

			AuthToken authToken = new AuthToken();
			authToken.setExpiresOn(cal.getTime());
			authToken.setIpAddress(loginDetail.getIpAddress());
			authToken.setToken(token);
			authToken.setUser(user);

			daoFactory.getDomainDao().saveAuthToken(authToken);

			String userToken = token + ":" + cal.getTime().getTime();

			Map<String, Object> authDetail = new HashMap<String, Object>();
			authDetail.put("firstName", user.getFirstName());
			authDetail.put("lastName", user.getLastName());
			authDetail.put("loginName", user.getLoginName());
			authDetail.put("token", encodeToken(userToken));

			return ResponseEvent.response(authDetail);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@PlusTransactional
	public ResponseEvent<User> validateToken(RequestEvent<Map<String, Object>> req) {
		try {
			Map<String, Object> tokenDetail = req.getPayload();
			String userToken = decodeToken((String)tokenDetail.get("token"));
			String[] parts = userToken.split(":");
			if (parts.length != 2) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_TOKEN);
			}

			long expiresOn = Long.parseLong(parts[1]);
			if (expiresOn < System.currentTimeMillis()) {
				throw OpenSpecimenException.userError(AuthErrorCode.TOKEN_EXPIRED);
			}

			String token = parts[0];
			AuthToken authToken = daoFactory.getDomainDao().getAuthTokenByKey(token);
			if (authToken == null) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_TOKEN);
			}

			if (authToken.getExpiresOn().getTime() < System.currentTimeMillis()) {
				throw OpenSpecimenException.userError(AuthErrorCode.TOKEN_EXPIRED);
			}

			String ipAddress = (String) tokenDetail.get("IpAddress");
			if (!authToken.getIpAddress().equals(ipAddress)) {
				throw OpenSpecimenException.userError(AuthErrorCode.IP_ADDRESS_CHANGED);
			}

			User user = authToken.getUser();

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
		String userToken = decodeToken(req.getPayload());
		String[] parts = userToken.split(":");

		try {
			daoFactory.getDomainDao().deleteAuthToken(parts[0]);
			return ResponseEvent.response("Success");
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private String encodeToken(String token) {
		return new String(Base64.encode(token.getBytes()));
	}
	
	private String decodeToken(String token) {
		return new String(Base64.decode(token.getBytes()));
	}
	
}
