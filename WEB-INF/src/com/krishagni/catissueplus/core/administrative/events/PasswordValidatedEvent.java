
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PasswordValidatedEvent extends ResponseEvent {

	Boolean isValid;

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
}
