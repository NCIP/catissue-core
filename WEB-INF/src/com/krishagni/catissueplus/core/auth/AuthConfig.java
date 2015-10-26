package com.krishagni.catissueplus.core.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.common.service.ConfigurationService;

@Configurable
public class AuthConfig {
	public static final String MODULE = "auth";
	
	public static final String FAILED_ATTEMPTS   = "allowed_failed_logins";
	
	public static final String VERIFY_TOKEN_IP   = "verify_token_ip_address";
	
	public static final String TOKEN_EXPIRY_INTERVAL = "token_expiry_interval";
	
	private static AuthConfig instance = null;
	
	@Autowired
	private ConfigurationService cfgSvc;
		
	public static AuthConfig getInstance() {
		if (instance == null) {
			instance = new AuthConfig();
		}
		
		return instance;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	public int getAllowedFailedLoginAttempts() {
		return cfgSvc.getIntSetting(MODULE, FAILED_ATTEMPTS, 5);
	}
	
	public boolean isTokenIpVerified() {
		return cfgSvc.getBoolSetting(MODULE, VERIFY_TOKEN_IP, false);
	}
	
	public int getSessionExpiryTimeInterval() {
		return cfgSvc.getIntSetting(MODULE, TOKEN_EXPIRY_INTERVAL, 60);
	}
}
