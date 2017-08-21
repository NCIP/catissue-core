package com.krishagni.catissueplus.core.auth.events;

public class TokenDetail {
	private String token;
	
	private String ipAddress;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
}
