package com.krishagni.catissueplus.core.auth.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
	INVALID_CREDENTIALS;

	@Override
	public String code() {
		return "AUTH_" + this.name();
	}

}
