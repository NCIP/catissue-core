package com.krishagni.catissueplus.core.auth.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
	INVALID_CREDENTIALS,
	
	USER_LOCKED,
	
	INVALID_TOKEN,
	
	TOKEN_EXPIRED,
	
	IP_ADDRESS_CHANGED,

	PASSWD_EXPIRED;

	@Override
	public String code() {
		return "AUTH_" + this.name();
	}

}
