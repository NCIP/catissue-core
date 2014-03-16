package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.User;

public class UserDetails {

	protected Long id;

	protected String lastName;

	protected String firstName;
 
	protected Long ldapId;
 
	protected String emailAddress;
 
	protected String loginName;
	
	protected Date startDate;
	
	protected String activityStatus;
	
	protected String deptName;
	
	protected String comments;

	protected String street;
	
	protected String city;
	
	protected String state;
	
	protected String country;
	
	protected String zipCode;
	
	protected String faxNumber;
	
	protected String phoneNumber;
	
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static UserDetails fromDomain(User user) {
		UserDetails dto = new UserDetails();
		
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setDeptName(user.getDepartment().getName());
		dto.setActivityStatus(user.getActivityStatus());
		dto.setEmailAddress(user.getEmailAddress());
		dto.setId(user.getId());
		dto.setLdapId(user.getLdapId());
		dto.setStartDate(user.getStartDate());
		dto.setComments(user.getComments());
		updateAddressDetails(dto, user.getAddress());
		return dto;	
	}
	
	private static void updateAddressDetails(UserDetails dto, Address address) {
		dto.setStreet(address.getStreet());
		dto.setCountry(address.getCountry());
		dto.setFaxNumber(address.getFaxNumber());
		dto.setPhoneNumber(address.getPhoneNumber());
		dto.setState(address.getState());
		dto.setCity(address.getCity());
		dto.setZipCode(address.getZipCode());
	}
	
}
