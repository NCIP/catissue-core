package com.krishagni.catissueplus.core.audit.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class UserAuditLog extends BaseEntity {
	private User user;
	
	private String url;
	
	private String method;
	
	private String responseCode;
	
	private String authToken;
	
	private Date reqStartTime;
	
	private Date reqEndTime;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Date getReqStartTime() {
		return reqStartTime;
	}

	public void setReqStartTime(Date reqStartTime) {
		this.reqStartTime = reqStartTime;
	}

	public Date getReqEndTime() {
		return reqEndTime;
	}

	public void setReqEndTime(Date reqEndTime) {
		this.reqEndTime = reqEndTime;
	}
}
