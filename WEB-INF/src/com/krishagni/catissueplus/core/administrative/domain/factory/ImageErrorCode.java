package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;


public enum ImageErrorCode implements CatissueErrorCode {
	
	INVALID_ATTR_VALUE(2401,  "Attribute value is invalid."), 
	ERRORS(2402, "Please resolve the errors listed in error list."), 
	BAD_REQUEST(2403, "Bad Request"), 
	MISSING_ATTR_VALUE(2404, "Required attribute is either empty or null"), 
	ACTIVE_CHILDREN_FOUND(2405, "Cannot be deleted, Active childrens found."),
	DUPLICATE_EQUIPMENT_IMAGE_ID(2406,"Image with the same equipment image id is already present in the system.");

	private int code;

	private String message;

	private ImageErrorCode(int code, String message) {
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
