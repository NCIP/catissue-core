package com.krishagni.catissueplus.core.common.service;

import javax.mail.MessagingException;

import com.krishagni.catissueplus.core.administrative.domain.User;

public interface EmailHandler {
	public Boolean sendForgotPasswordEmail(final User user, final String token) throws MessagingException;
	
	public Boolean sendChangedPasswordEmail(final User user) throws MessagingException;
}
