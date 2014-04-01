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
	
	protected Date createDate;
	
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
	
	protected String passwordToken;
	
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

	public String getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

	public static UserDetails fromDomain(User user) {
		UserDetails userDto = new UserDetails();
		
		userDto.setLoginName(user.getLoginName());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setDeptName(user.getDepartment().getName());
		userDto.setActivityStatus(user.getActivityStatus());
		userDto.setEmailAddress(user.getEmailAddress());
		userDto.setId(user.getId());
		userDto.setLdapId(user.getLdapId());
		userDto.setCreateDate(user.getCreateDate());
		userDto.setComments(user.getComments());
		userDto.setPasswordToken(user.getPasswordToken());
		updateAddressDetails(userDto, user.getAddress());
		return userDto;	
	}
	
	private static void updateAddressDetails(UserDetails userDto, Address address) {
		userDto.setStreet(address.getStreet());
		userDto.setCountry(address.getCountry());
		userDto.setFaxNumber(address.getFaxNumber());
		userDto.setPhoneNumber(address.getPhoneNumber());
		userDto.setState(address.getState());
		userDto.setCity(address.getCity());
		userDto.setZipCode(address.getZipCode());
	}
	
}
