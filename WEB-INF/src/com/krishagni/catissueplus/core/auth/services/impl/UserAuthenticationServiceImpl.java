
package com.krishagni.catissueplus.core.auth.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.auth.events.AuthenticateUserEvent;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;
import com.krishagni.catissueplus.core.auth.events.UserAuthenticatedEvent;
import com.krishagni.catissueplus.core.auth.services.CatissueAuthService;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	private static final String LOGIN_ID = "login id";

	private static final String PASSWORD = "password";

	private static final String ACTIVITY_STATUS = "activity_status";

	private static final String USER = "user";

	private DaoFactory daoFactory;

	private CatissueAuthService catissueAuthService;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setCatissueAuthService(CatissueAuthService catissueAuthService) {
		this.catissueAuthService = catissueAuthService;
	}

	@Override
	@PlusTransactional
	public UserAuthenticatedEvent authenticateUser(AuthenticateUserEvent event) {
		try {
			LoginDetails loginDetails = event.getLoginDetails();
			if (loginDetails.getLdapId() == null) {
				validateTerms(loginDetails);
				User user = daoFactory.getUserDao().getUserByLoginName(loginDetails.getLoginId());
				ensureActiveUser(user);
				catissueAuthService.authenticateUser(loginDetails);
				return UserAuthenticatedEvent.ok(UserDetails.fromDomain(user));
			}
			else {
				return new UserAuthenticatedEvent();
			}
		}
		catch (CatissueException ce) {
			return UserAuthenticatedEvent.notAuthenticated(ce.getMessage());
		}
		catch (Exception e) {
			return UserAuthenticatedEvent.serverError(e);
		}
	}

	private void ensureActiveUser(User user) {
		if (user == null) {
			reportError(UserErrorCode.NOT_FOUND, USER);
		}

		if (!user.isActive()) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
		}
	}

	private void validateTerms(LoginDetails loginDetails) {
		if (isBlank(loginDetails.getPassword())) {
			reportError(UserErrorCode.MISSING_ATTR_VALUE, PASSWORD);
		}

		if (isBlank(loginDetails.getLoginId())) {
			reportError(UserErrorCode.MISSING_ATTR_VALUE, LOGIN_ID);
		}
	}
}
