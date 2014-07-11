package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum UserErrorCode implements CatissueErrorCode{
	
	MISSING_ATTR_VALUE(1200, "Required attribute is either empty or null."), 
	INVALID_ATTR_VALUE(1201,"Attribute value is invalid."), 
	CONSTRAINT_VIOLATION(1202, "Attribute violates one or more constraints."),
	DUPLICATE_LOGIN_NAME(1205,"User with the same login name is already present in the system."),
	NOT_FOUND(1206,"Attribute value not found in the system."),
	CHANGE_IN_LOGIN_NAME(1207,"User login name cannot be changed in update."),
	CHANGE_IN_DOMAIN(1203,"User domain cannot be changed in update."),
	AUTH_FAILED(1210,"Error occured while user authentication."),
	ERRORS(1211, "Please resolve the highlighted errors."),
	INVALID_OPERATION(1212,"Operation is invalid for current user."), 
	DUPLICATE_EMAIL(1214,"User with the same email address is already present in the system."), 
	BAD_REQUEST(1215, "Bad Request"), 
	DUPLICATE_DEPARTMENT_NAME(1216, "Department with the same name is already present in the system."),
	REFERENCED_ATTRIBUTE(1220, "Attribute with reference cannot be disabled."),
	DUPLICATE_INSTITUTE_NAME(1217, "Institute with the same name is already present in the system.");

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
