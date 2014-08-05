package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;


public enum ScgErrorCode implements CatissueErrorCode {
	MISSING_ATTR_VALUE(1000, "Required attribute is either empty or null"), 
	INVALID_ATTR_VALUE(1001,"Attribute value is invalid"), 
	CONSTRAINT_VIOLATION(1002, "Attribute violates one or more constraints"),
	DUPLICATE_PPID(1003, "Same protocol participant identifier is already exists with this collection protocol."),
	DUPLICATE_BARCODE(1004, "Specimen Collection Group with same barcode already exists."),
	ACTIVE_CHILDREN_FOUND(1005, "Cannot be deleted, Active childrens found."),
	DUPLICATE_SSN(1006,"Participant with the same social security number is already exists."),
	DUPLICATE_NAME(1107,"specimen collection group with same name already exists."),
	ERRORS(1008, "Please resolve the highlighted errors."),
	INVALID_CPR_CPE(1109,"registraion and event point refering to different protocols."),
	AUTO_GENERATED_LABEL(1110,"Please do not specify label,it is auto generated.")
	;

	private int code;

	private String message;

	private ScgErrorCode(int code, String message) {
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
