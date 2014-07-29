package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum PVErrorCode implements CatissueErrorCode{
	
	MISSING_ATTR_VALUE(2500, "Required attribute is either empty or null."), 
	INVALID_ATTR_VALUE(2501,"Attribute value is invalid."), 
	CONSTRAINT_VIOLATION(2502, "Attribute violates one or more constraints."),
	NOT_FOUND(2503,"Attribute value not found in the system."),
	AUTH_FAILED(2504,"Error occured while user authentication."),
	ERRORS(2505, "Please resolve the highlighted errors."), 
	DUPLICATE_PV_VALUE(2506, "PV with same value already present in application."),
	DUPLICATE_CONCEPT_CODE(2507, "Concept code with same value already present in application.");
	
	private int code;

	private String message;

	private PVErrorCode(int code, String message) {
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
