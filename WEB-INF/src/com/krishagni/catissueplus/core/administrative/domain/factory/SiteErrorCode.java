
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum SiteErrorCode implements CatissueErrorCode {
	DUPLICATE_SITE_NAME(1400, "Site with the same site name is already present in the system."), 
	INVALID_ATTR_VALUE(1401,  "Attribute value is invalid."), 
	ERRORS(1402, "Please resolve the errors listed in error list."), 
	BAD_REQUEST(1403, "Bad Request"), 
	MISSING_ATTR_VALUE(1404, "Required attribute is either empty or null"), 
	ACTIVE_CHILDREN_FOUND(1405, "Cannot be deleted, Active childrens found.");

	private int code;

	private String message;

	private SiteErrorCode(int code, String message) {
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
