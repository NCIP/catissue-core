package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum CpErrorCode implements CatissueErrorCode {
	MISSING_TITLE(1000, "Title is required attribute"),
	
	MISSING_SHORT_TITLE(1001, "Short title is required attribute"),
	
	MISSING_PI(1002, "Principal investigator is required attribute"),
	
	INVALID_PI(1003, "Invalid Principal investigator attribute value"),
	
	INVALID_COORDINATORS(1004, "Invalid coordinators attribute value"),
	
	MISSING_CONSENTS_WAIVED(1005, "Consents waived is required attribute"),
	
	TITLE_NOT_UNIQUE(1006, "Title is not unique"),
	
	MISSING_EVENT_LABEL(1007, "Event label is required attribute"),
	
	INVALID_EVENT_POINT(1008, "Invalid event point attribute value"),
	
	INVALID_CP_TITLE(1009, "Invalid collection protocol title"),
	
	INVALID_SITE(1010, "Invalid site");
	
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
