package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum UserErrorCode implements ErrorCode {
	DUP_LOGIN_NAME,

	LOGIN_NAME_CHANGE_NOT_ALLOWED,
	
	OLD_PASSWD_NOT_SPECIFIED,
	
	INVALID_OLD_PASSWD,
	
	INVALID_PASSWD_TOKEN,
	
	PASSWD_VIOLATES_RULES,
	
	DOMAIN_CHANGE_NOT_ALLOWED,
	
	FIRST_NAME_REQUIRED,
	
	LAST_NAME_REQUIRED,
	
	LOGIN_NAME_REQUIRED,
	
	EMAIL_REQUIRED,
	
	INVALID_EMAIL,
	
	DUP_EMAIL,
	
	DOMAIN_NAME_REQUIRED,
	
	DOMAIN_NOT_FOUND,
	
	NOT_FOUND,
	
	DEPT_REQUIRED,
	DELETE_STATUS_CHANGE_NOT_ALLOWED;
	
	public String code() {
		return "USER_" + this.name();
	}

}
