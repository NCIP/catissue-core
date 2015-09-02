package com.krishagni.catissueplus.core.common.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.common.WildcardReloadableResourceBundle;

@Configurable
public class MessageUtil {
	private static MessageUtil instance = null;
	
	@Autowired
	private WildcardReloadableResourceBundle messageSource; 
	
	public static MessageUtil getInstance() {
		if (instance == null) {
			instance = new MessageUtil();
		}
		
		return instance;
	}
	
	public String getMessage(String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}
	
}
