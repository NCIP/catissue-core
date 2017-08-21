package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class ForgotPasswordToken extends BaseEntity {
	
	private static final long DEFAULT_EXPIRY_TIME = 1000 * 60 * 60 * 24; //24 hours
	
	private User user;

	private String token;
	
	private Date createdDate;
	
	public ForgotPasswordToken() {
		
	}
	
	public ForgotPasswordToken(User user) {
		this.user = user;
		this.token = UUID.randomUUID().toString();
		this.createdDate = new Date();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean hasExpired() {
		Calendar now = Calendar.getInstance();
		return (this.createdDate.getTime() + DEFAULT_EXPIRY_TIME) < now.getTimeInMillis();
	}
}
