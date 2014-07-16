
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum ParticipantErrorCode implements CatissueErrorCode {

	MISSING_ATTR_VALUE(1000, "Required attribute is either empty or null"), 
	INVALID_ATTR_VALUE(1001,"Attribute value is invalid"), 
	CONSTRAINT_VIOLATION(1002, "Attribute violates one or more constraints"),
	DUPLICATE_PPID(1003, "Same protocol participant identifier is already exists with this collection protocol."),
	DUPLICATE_BARCODE(1004, "Registration is already present with same barcode."),
	ACTIVE_CHILDREN_FOUND(1005, "Cannot be deleted, Active childrens found."),
	DUPLICATE_SSN(1005,"Participant with the same social security number is already exists."),
	DUPLICATE_PMI(1005,"Participant with the same mrn and site already exists."),
	ERRORS(1009, "Please resolve the errors listed in error list."),
	BAD_REQUEST(1009, "Bad Request");

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
