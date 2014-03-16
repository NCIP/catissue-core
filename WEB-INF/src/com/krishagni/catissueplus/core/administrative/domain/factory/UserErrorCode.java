package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum UserErrorCode implements CatissueErrorCode{
	
	MISSING_ATTR_VALUE(1200, "Required attribute is either empty or null"), 
	INVALID_ATTR_VALUE(1201,"Attribute value is invalid"), 
	CONSTRAINT_VIOLATION(1202, "Attribute violates one or more constraints"),
	ACTIVE_CHILDREN_FOUND(1204, "Cannot be deleted, Active childrens found."),
	DUPLICATE_LOGIN_NAME(1205,"User with the same login name is already present in the system."),
	CHANGE_IN_LOGIN_NAME(1206,"User login name cannot be changed in update"),
	ERRORS(1209, "Please resolve the highlighted errors.");

	private int code;

	private String message;

	private UserErrorCode(int code, String message) {
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
