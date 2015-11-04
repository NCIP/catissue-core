package com.krishagni.catissueplus.core.common.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;

@Configurable
public class MessageUtil {

	private static MessageUtil instance = null;

	@Autowired
	private MessageSource messageSource;

	public static MessageUtil getInstance() {
		if (instance == null) {
			instance = new MessageUtil();
		}

		return instance;
	}

	public String getBooleanMsg(Boolean value) {
		String bool = "common_no";
		if (value != null && value) {
			bool = "common_yes";
		}

		return messageSource.getMessage(bool, null, Locale.getDefault());
	}
	
	public String getMessage(String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}

}