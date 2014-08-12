
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum SpecimenErrorCode implements CatissueErrorCode {

	MISSING_ATTR_VALUE(1000, "Required attribute is either empty or null"),
	
	INVALID_ATTR_VALUE(1001, "Attribute value is invalid"),
	
	CONSTRAINT_VIOLATION(1002, "Attribute violates one or more constraints"),
	
	DUPLICATE_PPID(1003, "Same protocol participant identifier is already exists with this collection protocol."),
	
	DUPLICATE_BARCODE(1004, "Registration is already present with same barcode."),
	
	ACTIVE_CHILDREN_FOUND(1005, "Cannot be deleted, Active childrens found."),
	
	DUPLICATE_SSN(1006,	"Participant with the same social security number is already exists."),
	
	DUPLICATE_LABEL(1107,"specimen collection group with same already exists."),
	
	INSUFFICIENT_SPECIMEN_QTY(1008,"Cannot distribute available specimen quantity among the aliquots due to insufficient amount."),
	
	INVALID_ALIQUOT_COUNT(1009, "Aliquot count should be greter than 0"),
	
	CONTAINER_FULL(1010, "Cannot store all the aliquots in the same container at this time due to insufficient number of storage locations"),
	
	ERRORS(1011, "Please resolve the errors listed in error list."),
	
	AUTO_GENERATED_LABEL(1012, "Please do not specify label,it is auto generated."),
	
	AUTO_GENERATED_BARCODE(1013,"Please do not specify barcode,it is auto generated."),
	
	INVALID_LABELS(1014, "Invalid specimen labels");

	private int code;

	private String message;

	private SpecimenErrorCode(int code, String message) {
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
