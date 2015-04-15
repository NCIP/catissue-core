package com.krishagni.catissueplus.core.auth.domain;

import java.util.Date;

import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class AuthToken {
	private String token;
	
	private User user;
	
	private Date expiresOn;
	
	private String ipAddress;
	
	private LoginAuditLog loginAuditLog;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LoginAuditLog getLoginAuditLog() {
		return loginAuditLog;
	}

	public void setLoginAuditLog(LoginAuditLog loginAuditLog) {
		this.loginAuditLog = loginAuditLog;
	}
    
}
