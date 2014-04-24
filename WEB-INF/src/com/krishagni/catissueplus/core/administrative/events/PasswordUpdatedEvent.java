package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class PasswordUpdatedEvent extends ResponseEvent {

	private static String SUCCESS = "success";
	
	public static PasswordUpdatedEvent ok() {
		PasswordUpdatedEvent event = new PasswordUpdatedEvent();
		event.setStatus(EventStatus.OK);
		event.setMessage(SUCCESS);
		return event;
	}

	public static PasswordUpdatedEvent invalidRequest(String message) {
		PasswordUpdatedEvent resp = new PasswordUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static PasswordUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PasswordUpdatedEvent resp = new PasswordUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static PasswordUpdatedEvent notFound() {
		PasswordUpdatedEvent resp = new PasswordUpdatedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
