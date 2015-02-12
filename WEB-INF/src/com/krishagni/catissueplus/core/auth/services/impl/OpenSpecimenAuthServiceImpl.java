
package com.krishagni.catissueplus.core.auth.services.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class OpenSpecimenAuthServiceImpl extends AbstractAuthProvider {
	
	private BCryptPasswordEncoder passwordEncoder;
	
	public BCryptPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void authenticate(LoginDetail loginDetails) {
		User user = super.getDaoFactory().getUserDao().getActiveUser(loginDetails.getLoginId(), loginDetails.getDomainName());
		
		if (user == null || !passwordEncoder.matches(loginDetails.getPassword(), user.getPassword())) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}
}
