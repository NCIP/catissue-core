
package com.krishagni.catissueplus.core.auth.services.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.auth.services.UserAuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	@Autowired
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> authenticateUser(RequestEvent<LoginDetail> req) {
		try {
			LoginDetail loginDetail = req.getPayload();
			
			checkEmptyLoginNamePassword(loginDetail);
			
			User user = daoFactory.getUserDao().getUserByLoginNameAndDomainName(
					loginDetail.getLoginId(),
					loginDetail.getDomainName());
			
			if (user == null) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
			}
			
			AuthenticationService authService = user.getAuthDomain().getAuthProviderInstance();
			authService.authenticate(loginDetail);
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void checkEmptyLoginNamePassword(LoginDetail loginDetail) {
		if (StringUtils.isBlank(loginDetail.getPassword()) || StringUtils.isBlank(loginDetail.getLoginId())) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}
}
