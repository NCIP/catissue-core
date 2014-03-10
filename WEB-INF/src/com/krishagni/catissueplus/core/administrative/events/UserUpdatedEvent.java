package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
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

	public static UserUpdatedEvent invalidRequest(String message, Long... id) {
		UserUpdatedEvent resp = new UserUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
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

	public static UserUpdatedEvent notAuthorized(UpdateParticipantEvent event1) {
		UserUpdatedEvent event = new UserUpdatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static UserUpdatedEvent notFound(Long participantId) {
		UserUpdatedEvent resp = new UserUpdatedEvent();
		resp.setUserId(participantId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}

