
package com.krishagni.catissueplus.core.auth.services.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;
import com.krishagni.catissueplus.core.auth.services.CatissueAuthService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class CatissueAuthServiceImpl implements CatissueAuthService {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private static final String USER = "user";

	@Override
	public void authenticateUser(LoginDetails loginDetails) {
		Boolean isValid = false;
		List<String> results = daoFactory.getUserDao().getOldPasswordsByLoginName(loginDetails.getLoginId());
		if (results.size() > 0) {
			isValid = results.get(results.size() - 1).equals(loginDetails.getPassword()) ? true : false;
		}

		if (!isValid) { 
			reportError(UserErrorCode.INVALID_ATTR_VALUE, USER);
		}
	}

}
