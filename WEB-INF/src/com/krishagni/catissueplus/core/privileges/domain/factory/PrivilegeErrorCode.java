package com.krishagni.catissueplus.core.privileges.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;


public enum PrivilegeErrorCode implements CatissueErrorCode {
	MISSING_ATTR_VALUE(1500, "Required attribute is either empty or null."), 
	INVALID_ATTR_VALUE(1501,"Attribute value is invalid."), 
	DUPLICATE_ROLE_NAME(1502,"Role with the same name is already present in the system."),
	NOT_FOUND(1503,"Attribute value not found in the system."),
	ERRORS(1504, "Please resolve the highlighted errors."),
	BAD_REQUEST(1205, "Bad Request");

	private int code;

	private String message;

	private PrivilegeErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int code() {
		return code;
	}

	public String message() {
		return message;
	}

}
