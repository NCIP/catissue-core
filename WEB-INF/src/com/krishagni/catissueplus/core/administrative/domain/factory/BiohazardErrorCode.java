
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum BiohazardErrorCode implements CatissueErrorCode {

	DUPLICATE_BIOHAZARD_NAME(1500, "biohazard with the same biohazard name is already present in the system."), 
	INVALID_ATTR_VALUE(1501, "Attribute value is invalid."), 
	ERRORS(1502, "Please resolve the errors listed in error list."), 
	BAD_REQUEST(1503, "Bad Request"), 
	MISSING_ATTR_VALUE(1504, "Required attribute is either empty or null"), 
	ACTIVE_CHILDREN_FOUND(1505, "Cannot be deleted, Active childrens found.");

	private int code;

	private String message;

	private BiohazardErrorCode(int code, String message) {
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
