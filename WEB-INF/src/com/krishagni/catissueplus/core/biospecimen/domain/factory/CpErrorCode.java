package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum CpErrorCode implements CatissueErrorCode {
	MISSING_TITLE(1000, "Title is required attribute"),
	
	MISSING_SHORT_TITLE(1001, "Short title is required attribute"),
	
	MISSING_PI(1002, "Principal investigator is required attribute"),
	
	INVALID_PI(1003, "Invalid Principal investigator attribute value"),
	
	INVALID_COORDINATORS(1004, "Invalid coordinators attribute value"),
	
	MISSING_CONSENTS_WAIVED(1005, "Consents waived is required attribute"),
	
	TITLE_NOT_UNIQUE(1006, "Title is not unique");
	
	private int code;

	private String message;

	private CpErrorCode(int code, String message) {
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
