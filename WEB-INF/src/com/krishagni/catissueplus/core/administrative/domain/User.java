package com.krishagni.catissueplus.core.administrative.domain;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable {
	
	private final String ACTIVITY_STATUS_CLOSED = "Closed";
	
	private Long id;

	private String lastName;

	private String firstName;
 
	private Long ldapId;
 
	private String emailAddress;
 
	private String loginName;
	
	private Date createDate;
	
	private String activityStatus;
	
	private Department department;
	
	private Address address;

	private String comments;
	
	private Password password;
	
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public void update(User user) {
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setActivityStatus(user.getActivityStatus());
		this.setLdapId(user.getLdapId());
		this.setAddress(user.getAddress());
		this.setLoginName(user.getLoginName());
		this.setCreateDate(user.getCreateDate());
		this.setDepartment(user.getDepartment());
		this.setEmailAddress(user.getEmailAddress());
		this.setComments(user.getComments());
		this.setPassword(user.getPassword());
		updateAddressDetails(this.getAddress(), user.getAddress());		
	}
	
	public void close() {
		this.setActivityStatus(ACTIVITY_STATUS_CLOSED);
	}
	
	private void updateAddressDetails(Address oldAddress, Address address) {
		oldAddress.setStreet(address.getStreet());
		oldAddress.setCountry(address.getCountry());
		oldAddress.setFaxNumber(address.getFaxNumber());
		oldAddress.setPhoneNumber(address.getPhoneNumber());
		oldAddress.setState(address.getState());
		oldAddress.setCity(address.getCity());
		oldAddress.setZipCode(address.getZipCode());
	}
}
