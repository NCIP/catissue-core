package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;


public class UpdatePasswordEvent extends RequestEvent {

	private PasswordDetails passwordDetails;
	
	private String passwordToken;
	
	public UpdatePasswordEvent(PasswordDetails passwordDetails, String token, Long id) {
		this.passwordDetails = passwordDetails;
		if(this.passwordDetails.getId() == null ) {
			this.passwordDetails.setId(id);
		}
		if(this.getPasswordToken() == null) {
			this.passwordToken = token;
		}
	}	

	public PasswordDetails getPasswordDetails() {
		return passwordDetails;
	}

	public void setPasswordDetails(PasswordDetails passwordDetails) {
		this.passwordDetails = passwordDetails;
	}

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}
	
}
