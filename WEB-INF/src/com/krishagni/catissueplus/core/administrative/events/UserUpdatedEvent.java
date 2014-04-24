package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class UserUpdatedEvent extends ResponseEvent {

	private UserDetails userDetails;

	private Long userId;

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public static UserUpdatedEvent ok(UserDetails details) {
		UserUpdatedEvent event = new UserUpdatedEvent();
		event.setUserDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static UserUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		UserUpdatedEvent resp = new UserUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static UserUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		UserUpdatedEvent resp = new UserUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static UserUpdatedEvent notFound(Long userId) {
		UserUpdatedEvent resp = new UserUpdatedEvent();
		resp.setUserId(userId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}

