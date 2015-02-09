
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdatePasswordEvent extends RequestEvent {

	private PasswordDetails passwordDetails;

	public UpdatePasswordEvent(PasswordDetails passwordDetails) {
		setPasswordDetails(passwordDetails);
	}

	public PasswordDetails getPasswordDetails() {
		return passwordDetails;
	}

	public void setPasswordDetails(PasswordDetails passwordDetails) {
		this.passwordDetails = passwordDetails;
	}
}
