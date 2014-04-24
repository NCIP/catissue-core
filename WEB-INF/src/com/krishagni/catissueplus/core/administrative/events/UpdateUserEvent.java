package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateUserEvent extends RequestEvent {

	private UserDetails userDetails;

	public UpdateUserEvent(UserDetails userDetails, Long userId) {
		this.userDetails = userDetails;
		this.userDetails.setId(userId);
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

}
