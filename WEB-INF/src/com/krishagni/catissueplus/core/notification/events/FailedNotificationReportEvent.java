package com.krishagni.catissueplus.core.notification.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FailedNotificationReportEvent extends ResponseEvent {
	
	public static FailedNotificationReportEvent ok() {
		FailedNotificationReportEvent event = new FailedNotificationReportEvent();
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static FailedNotificationReportEvent serverError(String msg, Throwable t) {
		FailedNotificationReportEvent event = new FailedNotificationReportEvent();
		event.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		event.setMessage(msg);
		event.setException(t);
		return event;
	}
}
