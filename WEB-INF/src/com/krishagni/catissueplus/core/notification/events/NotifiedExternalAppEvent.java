package com.krishagni.catissueplus.core.notification.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class NotifiedExternalAppEvent extends ResponseEvent {
	
	public static NotifiedExternalAppEvent ok() {
		NotifiedExternalAppEvent event = new NotifiedExternalAppEvent();
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static NotifiedExternalAppEvent serverError(String msg, Throwable t) {
		NotifiedExternalAppEvent event = new NotifiedExternalAppEvent();
		event.setMessage(msg);
		event.setException(t);
		event.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		return event;
	}
}
