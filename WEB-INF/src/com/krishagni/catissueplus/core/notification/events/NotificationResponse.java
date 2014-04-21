
package com.krishagni.catissueplus.core.notification.events;

public class NotificationResponse {

	public enum Status {
		SUCCESS, FAIL
	};

	private Status status;

	private String message;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static NotificationResponse success(String message) {
		NotificationResponse notificationResponse = new NotificationResponse();
		notificationResponse.setMessage(message);
		notificationResponse.setStatus(Status.SUCCESS);
		return notificationResponse;
	}

	public static NotificationResponse fail(String message) {
		NotificationResponse notificationResponse = new NotificationResponse();
		notificationResponse.setMessage(message);
		notificationResponse.setStatus(Status.FAIL);
		return notificationResponse;
	}
}
