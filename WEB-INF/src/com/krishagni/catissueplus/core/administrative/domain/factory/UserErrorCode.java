package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum UserErrorCode implements CatissueErrorCode{
	
	MISSING_ATTR_VALUE(1200, "Required attribute is either empty or null"), 
	INVALID_ATTR_VALUE(1201,"Attribute value is invalid"), 
	CONSTRAINT_VIOLATION(1202, "Attribute violates one or more constraints"),
	ACTIVE_CHILDREN_FOUND(1204, "Cannot be deleted, Active childrens found."),
	DUPLICATE_LOGIN_NAME(1205,"User with the same login name is already present in the system."),
	NOT_FOUND(1206,"Attribute value not found in the system."),
	CHANGE_IN_LOGIN_NAME(1207,"User login name cannot be changed in update"),
	ERROR_WHILE_USER_UPDATION(1208,"Error occured while user updation"),
	ERROR_WHILE_USER_CREATION(1209,"Error occured while user creation"),
	ERRORS(1211, "Please resolve the highlighted errors."),
	DUPLICATE_EMAIL_ADDRESS(1212,"User with the same email address is already present in the system.");

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
