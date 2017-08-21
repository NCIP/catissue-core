package com.krishagni.catissueplus.core.common.errors;

public enum ActivityStatusErrorCode implements ErrorCode {
	INVALID;

	@Override
	public String code() {
		return "ACTIVITY_STATUS_" + this.name();
	}
}
