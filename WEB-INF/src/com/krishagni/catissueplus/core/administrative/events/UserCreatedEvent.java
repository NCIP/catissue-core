package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class UserCreatedEvent extends ResponseEvent {
	
	private UserDetails userDetails;

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public static UserCreatedEvent ok(UserDetails details) {
		UserCreatedEvent event = new UserCreatedEvent();
		event.setUserDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static UserCreatedEvent notAuthorized(CreateUserEvent event1) {
		UserCreatedEvent event = new UserCreatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static UserCreatedEvent invalidRequest(String message, Long... id) {
		UserCreatedEvent resp = new UserCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static UserCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		UserCreatedEvent resp = new UserCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
