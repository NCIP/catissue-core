
package com.krishagni.catissueplus.core.auth.events;

public class LoginDetail {
	private String loginName;

	private String password;

	private String domainName;
	
	private String ipAddress;
	
	private boolean doNotGenerateToken;
	
	private String apiUrl;
	
	private String requestMethod;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isDoNotGenerateToken() {
		return doNotGenerateToken;
	}

	public void setDoNotGenerateToken(boolean doNotGenerateToken) {
		this.doNotGenerateToken = doNotGenerateToken;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

}
