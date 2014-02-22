
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum ParticipantErrorCode implements CatissueErrorCode {

	MISSING_ATTR_VALUE(1000, "Required attribute is either empty or null"), 
	INVALID_ATTR_VALUE(1001,"Attribute value is invalid"), 
	CONSTRAINT_VIOLATION(1002, "Attribute violates one or more constraints");

	private int code;

	private String message;

	private ParticipantErrorCode(int code, String message) {
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
