
package com.krishagni.catissueplus.core.common.events;

public enum EventStatus {
	OK(0), NOT_FOUND(1), NOT_AUTHORIZED(2), NOT_AUTHENTICATED(3), INTERNAL_SERVER_ERROR(4), BAD_REQUEST(5);

	private int code;

	private EventStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
