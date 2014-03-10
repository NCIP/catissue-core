package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;

public class User {
	
	private final String ACTIVITY_STATUS_DISABLED = "Disabled";
	
	private Long id;

	private String lastName;

	private String firstName;
 
	private Long ldapId;
 
	private String emailId;
 
	private String loginName;
	
	private Date startDate;
	
	private String activityStatus;
	
	private Department department;
	
	private Address address;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Long getLdapId() {
		return ldapId;
	}

	public void setLdapId(Long ldapId) {
		this.ldapId = ldapId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public void update(User user) {

		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setId(user.getId());
		this.setActivityStatus(user.getActivityStatus());
		this.setLdapId(user.getLdapId());
		this.setAddress(user.getAddress());
		this.setLoginName(user.getLoginName());
		this.setStartDate(user.getStartDate());
		this.setDepartment(user.getDepartment());
		this.setEmailId(user.emailId);
	}
	
	public void delete() {
		this.setActivityStatus(ACTIVITY_STATUS_DISABLED);
	}
}
