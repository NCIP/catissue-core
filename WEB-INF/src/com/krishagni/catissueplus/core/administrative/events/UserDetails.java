package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;
import com.krishagni.catissueplus.core.administrative.domain.User;

public class UserDetails {

	protected Long id;

	protected String lastName;

	protected String firstName;
 
	protected Long ldapId;
 
	protected String emailId;
 
	protected String loginName;
	
	protected Date startDate;
	
	protected String activityStatus;
	
	protected Long departmentId;
	
	protected Long addressId;

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

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public static UserDetails fromDomain(User user) {
		UserDetails dto = new UserDetails();
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setDepartmentId(user.getDepartment().getId());
		dto.setActivityStatus(user.getActivityStatus());
		dto.setEmailId(user.getEmailId());
		dto.setId(user.getId());
		dto.setLdapId(user.getLdapId());
		dto.setStartDate(user.getStartDate());
		return dto;
	}
}
