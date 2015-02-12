package com.krishagni.catissueplus.core.de.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum FormErrorCode implements ErrorCode {
	NOT_FOUND,
	
	INVALID_REQ,
	
	REC_NOT_FOUND,
	
	INVALID_DATA,
	
	FILE_NOT_FOUND,
	
	OP_NOT_ALLOWED;

	@Override
	public String code() {
		return "FORMS_" + this.name();
	}

}
