
package com.krishagni.catissueplus.core.auth.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.events.AuthenticateUserEvent;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;
import com.krishagni.catissueplus.core.auth.events.UserAuthenticatedEvent;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	private static final String LOGIN_ID_AND_PASS = "login id and Password";

	private static final String USER = "user";

	@Autowired
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public UserAuthenticatedEvent authenticateUser(AuthenticateUserEvent event) {
		try {
			LoginDetails loginDetails = event.getLoginDetails();
			checkEmptyLoginNamePassword(loginDetails);
			User user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(loginDetails.getLoginId(),
					loginDetails.getDomainName());
			if (user == null) {
				reportError(UserErrorCode.NOT_FOUND, USER);
			}
			AuthenticationService authService = user.getAuthDomain().getAuthProviderInstance();
			authService.authenticate(loginDetails);
			return UserAuthenticatedEvent.ok();
		}
		catch (CatissueException ce) {
			return UserAuthenticatedEvent.notAuthenticated(ce.getMessage());
		}
		catch (Exception e) {
			return UserAuthenticatedEvent.serverError(e);
		}
	}

	private void checkEmptyLoginNamePassword(LoginDetails loginDetails) {
		if (isBlank(loginDetails.getPassword()) || isBlank(loginDetails.getLoginId())) {
			reportError(UserErrorCode.MISSING_ATTR_VALUE, LOGIN_ID_AND_PASS);
		}
	}
}
