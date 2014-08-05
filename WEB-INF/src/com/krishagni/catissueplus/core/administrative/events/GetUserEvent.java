
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GetUserEvent extends ResponseEvent {

	private UserDetails userDetails;

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public static GetUserEvent ok(UserDetails userDetails) {
		GetUserEvent getUserEvent = new GetUserEvent();
		getUserEvent.setUserDetails(userDetails);
		getUserEvent.setStatus(EventStatus.OK);
		return getUserEvent;
	}
}
