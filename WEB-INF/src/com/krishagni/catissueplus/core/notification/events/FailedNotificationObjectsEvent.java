package com.krishagni.catissueplus.core.notification.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;

public class FailedNotificationObjectsEvent extends ResponseEvent {
	
	private List<ExtAppNotificationStatus> failedNotificationObjects;

	public List<ExtAppNotificationStatus> getFailedNotificationObjects() {
		return failedNotificationObjects;
	}

	public void setFailedNotificationObjects(
			List<ExtAppNotificationStatus> failedNotificationObjects) {
		this.failedNotificationObjects = failedNotificationObjects;
	}
	
	public static FailedNotificationObjectsEvent ok(List<ExtAppNotificationStatus> failedNotificationObjs) {
		FailedNotificationObjectsEvent event = new FailedNotificationObjectsEvent();
		event.setFailedNotificationObjects(failedNotificationObjs);
		event.setStatus(EventStatus.OK);
		return event;
	}	
}
