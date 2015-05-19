package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class UserListCriteria extends AbstractListCriteria<UserListCriteria> {
	private String name;
	
	private String loginName;
	
	private String activityStatus;
	
	private String instituteName;
	
	@Override
	public UserListCriteria self() {
		return this;
	}

	public String name() {
		return name;
	}

	public UserListCriteria name(String name) {
		this.name = name;
		return self();
	}

	public String loginName() {
		return loginName;
	}

	public UserListCriteria loginName(String loginName) {
		this.loginName = loginName;
		return self();
	}

	public String activityStatus() {
		return activityStatus;
	}

	public UserListCriteria activityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
		return self();
	}
	
	public UserListCriteria instituteName(String instituteName) {
		this.instituteName = instituteName;
		return self();
	}
	
	public String instituteName() {
		return instituteName;
	}	
}
