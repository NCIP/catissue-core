
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AuthenticateUserEvent extends RequestEvent {

	private LoginDetails loginDetails;

	public AuthenticateUserEvent(LoginDetails loginDetails) {
		this.loginDetails = loginDetails;
	}

	public LoginDetails getLoginDetails() {
		return loginDetails;
	}

	public void setLoginDetails(LoginDetails loginDetails) {
		this.loginDetails = loginDetails;
	}

}
