package com.krishagni.catissueplus.core.common.events;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class UserSummary {
	private Long id;
	
	private String firstName;
	
	private String lastName;

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
	
	public static UserSummary fromUser(User user){
		UserSummary userSummary = new UserSummary();
		userSummary.setId(user.getId());
		userSummary.setFirstName(user.getFirstName());
		userSummary.setLastName(user.getLastName());		
		return userSummary;
	}	
}
