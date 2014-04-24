package com.krishagni.catissueplus.core.common.email;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class EmailSender {

	public Boolean sendUserCreatedEmail(final User user) {
		return EmailHandler.sendUserCreatedEmail(user);		
	}
	
	public Boolean sendForgotPasswordEmail(final User user) {
		return EmailHandler.sendForgotPasswordEmail(user);		
	}
}
