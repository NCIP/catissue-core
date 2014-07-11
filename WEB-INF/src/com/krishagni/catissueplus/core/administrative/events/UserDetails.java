
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

public class UserDetails {

	private Long id;

	private String lastName;

	private String firstName;

	private String domainName;

	private String emailAddress;

	private String loginName;

	private List<String> userSiteNames = new ArrayList<String>();

	private List<UserCPRoleDetails> userCPRoles = new ArrayList<UserCPRoleDetails>();

	private Date createDate;

	private String activityStatus;

	private String deptName;

	private String comments;

	private String street;

	private String city;

	private String state;

	private String country;

	private String zipCode;

	private String faxNumber;

	private String phoneNumber;

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

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
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

	public List<String> getUserSiteNames() {
		return userSiteNames;
	}

	public void setUserSiteNames(List<String> userSiteNames) {
		this.userSiteNames = userSiteNames;
	}

	public List<UserCPRoleDetails> getUserCPRoles() {
		return userCPRoles;
	}

	public void setUserCPRoles(List<UserCPRoleDetails> userCPRoles) {
		this.userCPRoles = userCPRoles;
	}

	public static UserDetails fromDomain(User user) {
		UserDetails userDto = new UserDetails();
		userDto.setLoginName(user.getLoginName());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());

		if (user.getDepartment() != null) {
			userDto.setDeptName(user.getDepartment().getName());
		}
		userDto.setActivityStatus(user.getActivityStatus());
		userDto.setEmailAddress(user.getEmailAddress());
		userDto.setId(user.getId());
		userDto.setCreateDate(user.getCreateDate());
		userDto.setComments(user.getComments());

		if (user.getAuthDomain() != null) {
			userDto.setDomainName(user.getAuthDomain().getName());
		}
		setUserCPRoles(userDto, user.getUserCPRoles());
		setUserSiteNames(userDto, user.getUserSites());	
		
		if(user.getAddress() != null){ 
			updateAddressDetails(userDto, user.getAddress());
		}
		return userDto;
	}

	private static void setUserSiteNames(UserDetails userDto, Set<Site> userSites) {
		List<String> siteNames = new ArrayList<String>();
		for (Site site : userSites) {
			siteNames.add(site.getName());
		}
		userDto.setUserSiteNames(siteNames);
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

	private static void setUserCPRoles(UserDetails userDto, Set<UserCPRole> userCPRoles) {
		List<UserCPRoleDetails> userCPRoleDetailsList = new ArrayList<UserCPRoleDetails>();
		for (UserCPRole userCPRole : userCPRoles) {
			UserCPRoleDetails userCPRoleDetails = new UserCPRoleDetails();
			userCPRoleDetails.setCpTitle(userCPRole.getCollectionProtocol().getTitle());
			userCPRoleDetails.setRoleName(userCPRole.getRole().getName());
			userCPRoleDetails.setId(userCPRole.getId());
			userCPRoleDetailsList.add(userCPRoleDetails);
		}
		userDto.setUserCPRoles(userCPRoleDetailsList);
	}
}
