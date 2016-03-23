
package com.krishagni.catissueplus.core.common.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.krishagni.catissueplus.core.administrative.domain.User;

@JsonFilter("withoutId")
public class UserSummary implements Serializable {
	
	private static final long serialVersionUID = -8113791999197573026L;

	private Long id;

	private String firstName;

	private String lastName;

	private String loginName;
	
	private String domain;
	
	private String emailAddress;

	private String instituteName;

	private Boolean admin;

	private int cpCount;

	private Date creationDate;

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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public int getCpCount() {
		return cpCount;
	}

	public void setCpCount(int cpCount) {
		this.cpCount = cpCount;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public static UserSummary from(User user) {
		UserSummary userSummary = new UserSummary();
		userSummary.setId(user.getId());
		userSummary.setFirstName(user.getFirstName());
		userSummary.setLastName(user.getLastName());
		userSummary.setLoginName(user.getLoginName());
		userSummary.setDomain(user.getAuthDomain().getName());
		userSummary.setEmailAddress(user.getEmailAddress());
		userSummary.setAdmin(user.isAdmin());
		userSummary.setCreationDate(user.getCreationDate());
		return userSummary;
	}
	
	public static List<UserSummary> from(Collection<User> users) {
		List<UserSummary> result = new ArrayList<UserSummary>();
		if (users == null) {
			return result;
		}	

		for (User user : users) {
			result.add(UserSummary.from(user));
		}
		
		return result;
	}
}
