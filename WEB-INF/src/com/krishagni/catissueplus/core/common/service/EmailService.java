package com.krishagni.catissueplus.core.common.service;

import javax.mail.MessagingException;

import com.krishagni.catissueplus.core.common.domain.Email;

public interface EmailService {
	public boolean sendMail(Email mail) throws MessagingException;
}
