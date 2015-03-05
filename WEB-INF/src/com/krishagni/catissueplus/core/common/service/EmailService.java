package com.krishagni.catissueplus.core.common.service;

import java.io.File;
import java.util.Map;

import com.krishagni.catissueplus.core.common.domain.Email;

public interface EmailService {
	public boolean sendEmail(String emailTmplKey, String[] to, Map<String, String> props);
	
	public boolean sendEmail(String emailTmplKey, String[] to, File[] attachments, Map<String, String> props);
	
	public boolean sendEmail(Email mail);
}
