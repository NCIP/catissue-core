
package com.krishagni.catissueplus.core.auth.services.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;

public class CatissueAuthServiceImpl extends AbstractAuthProvider {
	
	private static final String USER = "user";

	@Override
	public void authenticate(LoginDetails loginDetails) {
		User user = super.getDaoFactory().getUserDao().getActiveUser(loginDetails.getLoginId(), loginDetails.getDomainName());
		
		if (user == null) {
			reportError(UserErrorCode.NOT_FOUND, USER);
		}
		
		if (!user.getPasswordCollection().isEmpty()) {
			Password lastPassword = new ArrayList<Password>(user.getPasswordCollection()).get(user.getPasswordCollection().size() -1);
			BCrypt.checkpw(loginDetails.getPassword(), lastPassword.getPassword());
		}
	}

}
