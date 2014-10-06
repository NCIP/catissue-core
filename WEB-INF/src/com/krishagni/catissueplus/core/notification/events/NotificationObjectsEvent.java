package com.krishagni.catissueplus.core.notification.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class NotificationObjectsEvent extends ResponseEvent{
	
	List<NotificationDetails> notificationObjects;

	public List<NotificationDetails> getNotificationObjects() {
		return notificationObjects;
	}

	public void setNotificationObjects(List<NotificationDetails> notificationObjects) {
		this.notificationObjects = notificationObjects;
	}
	
	public static NotificationObjectsEvent ok(List<NotificationDetails> notificationDetails) {
		NotificationObjectsEvent event = new NotificationObjectsEvent();
		event.setNotificationObjects(notificationDetails);
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static NotificationObjectsEvent serverError(String msg, Throwable t) {
		NotificationObjectsEvent event = new NotificationObjectsEvent();
		event.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		event.setException(t);
		event.setMessage(msg);
		return event;
	}
	
}
