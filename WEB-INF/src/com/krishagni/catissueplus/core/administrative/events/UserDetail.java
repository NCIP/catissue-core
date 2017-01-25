
package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@ListenAttributeChanges
public class UserDetail extends AttributeModifiedSupport {

	private Long id;

	private String firstName;

	private String lastName;

	private String emailAddress;

	private String domainName;

	private String loginName;

	private String instituteName;

	private String primarySite;

	private String type;

	private String phoneNumber;

	private boolean manageForms;

	private String address;

	private Date creationDate;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public String getPrimarySite() {
		return primarySite;
	}

	public void setPrimarySite(String primarySite) {
		this.primarySite = primarySite;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getManageForms() {
		return manageForms;
	}

	public void setManageForms(Boolean manageForms) {
		this.manageForms = manageForms;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static UserDetail from(User user) {
		UserDetail detail = new UserDetail();
		detail.setId(user.getId());
		detail.setFirstName(user.getFirstName());
		detail.setLastName(user.getLastName());
		detail.setEmailAddress(user.getEmailAddress());
		detail.setDomainName(user.getAuthDomain().getName());
		detail.setLoginName(user.getLoginName());
		detail.setInstituteName(user.getInstitute().getName());
		detail.setPrimarySite(user.getPrimarySite() != null ? user.getPrimarySite().getName() : null);
		detail.setType(user.getType().name());
		detail.setPhoneNumber(user.getPhoneNumber());
		detail.setManageForms(user.getManageForms());
		detail.setAddress(user.getAddress());
		detail.setCreationDate(user.getCreationDate());
		detail.setActivityStatus(user.getActivityStatus());
		return detail;
	}
	
	public static List<UserDetail> from(Collection<User> users) {
		return users.stream().map(UserDetail::from).collect(Collectors.toList());
	}
}
