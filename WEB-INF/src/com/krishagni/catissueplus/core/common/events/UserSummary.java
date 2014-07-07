
package com.krishagni.catissueplus.core.common.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class UserSummary {

	private Long id;

	private String firstName;

	private String lastName;

	private String loginName;

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

	public static UserSummary fromUser(User user) {
		UserSummary userSummary = new UserSummary();
		userSummary.setId(user.getId());
		userSummary.setFirstName(user.getFirstName());
		userSummary.setLastName(user.getLastName());
		userSummary.setLoginName(user.getLoginName());
		return userSummary;
	}
	
	public static List<UserSummary> fromUsers(Collection<User> users) {
		List<UserSummary> summaries = new ArrayList<UserSummary>();
		for (User user : users) {
			summaries.add(UserSummary.fromUser(user));
		}
		
		return summaries;
	}
}
