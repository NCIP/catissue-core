package com.krishagni.catissueplus.core.auth.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
	INVALID_CREDENTIALS,
	
	INVALID_TOKEN,
	
	TOKEN_EXPIRED,
	
	IP_ADDRESS_CHANGED;

	@Override
	public String code() {
		return "AUTH_" + this.name();
	}

}
