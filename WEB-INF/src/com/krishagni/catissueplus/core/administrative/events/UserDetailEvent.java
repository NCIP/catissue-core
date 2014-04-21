package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class UserDetailEvent extends ResponseEvent {
	
	private UserDetails userDetails;

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	
	public static UserDetailEvent ok(UserDetails details)
	{
		UserDetailEvent event = new UserDetailEvent();
		event.setUserDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}
}
