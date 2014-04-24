
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.krishagni.catissueplus.core.auth.events.LoginDetails;

public class CatissueAuthServiceImpl extends AbstractAuthProvider {
	
	@Override
	public void authenticate(LoginDetails loginDetails) {
		List<String> results =  super.getDaoFactory().getUserDao().getOldPasswordsByLoginName(loginDetails.getLoginId());
		if (!results.isEmpty()) {
			BCrypt.checkpw(loginDetails.getPassword(), results.get(results.size() - 1));
		}
	}

}
