package com.krishagni.catissueplus.core.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.common.service.ConfigurationService;

@Configurable
public class ConfigUtil {
	private static ConfigUtil instance = null;
	
	@Autowired
	private ConfigurationService cfgSvc;
		
	public static ConfigUtil getInstance() {
		if (instance == null) {
			instance = new ConfigUtil();
		}
		
		return instance;
	}
	
	public String getAppUrl() {
		return cfgSvc.getStrSetting("common", "app_url", "");
	}
	
	public String getDataDir() {
		return cfgSvc.getStrSetting("common", "data_dir", ".");
	}
	
	public String getAdminEmailId() {
		return cfgSvc.getStrSetting("email", "admin_email_id", "");
	}

}
