
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class OpenSpecimenAuthServiceImpl extends AbstractAuthProvider {
	@Override
	public void authenticate(LoginDetail loginDetails) {
		User user = super.getDaoFactory().getUserDao().getActiveUser(loginDetails.getLoginId(), loginDetails.getDomainName());
		
		if (user == null) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
		
		if (!user.getPasswordCollection().isEmpty()) {
			Password lastPassword = new ArrayList<Password>(user.getPasswordCollection()).get(user.getPasswordCollection().size() -1);
			if (!BCrypt.checkpw(loginDetails.getPassword(), lastPassword.getPassword())) {
				throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
			}
		}
	}

}
