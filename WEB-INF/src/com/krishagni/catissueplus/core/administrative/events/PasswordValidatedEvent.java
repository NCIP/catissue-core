
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PasswordValidatedEvent extends ResponseEvent {

	private String password;

	Boolean isValid;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public static PasswordValidatedEvent ok(Boolean value) {
		PasswordValidatedEvent event = new PasswordValidatedEvent();
		event.setStatus(EventStatus.OK);
		event.setIsValid(value);
		return event;
	}

	public static PasswordValidatedEvent invalidRequest(String message) {
		PasswordValidatedEvent resp = new PasswordValidatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static PasswordValidatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PasswordValidatedEvent resp = new PasswordValidatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
