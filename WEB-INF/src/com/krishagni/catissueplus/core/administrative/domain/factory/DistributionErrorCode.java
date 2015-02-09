package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum DistributionErrorCode implements CatissueErrorCode {
	INVALID_ATTR_VALUE(1900, "Attribute value is invalid."),
	MISSING_ATTR_VALUE(1902, "Required attribute is either empty or null"),
	DUPLICATE_DISTRI_NAME(1903, "Provided distribution name already exists"),
	SPECIMEN_NOT_COLLECTED(1904, "Specimen not collected yet!")
	;

	private int code;

	private String message;

	private DistributionErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public int code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}

}
