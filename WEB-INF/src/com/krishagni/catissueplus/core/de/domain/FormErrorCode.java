package com.krishagni.catissueplus.core.de.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum FormErrorCode implements ErrorCode {
	NOT_FOUND,
	
	INVALID_REQ,
	
	NAME_REQUIRED,
	
	ENTITY_TYPE_REQUIRED,
	
	REC_NOT_FOUND,
	
	INVALID_DATA,
	
	FILE_NOT_FOUND,
	
	NO_ASSOCIATION,
			
	OP_NOT_ALLOWED,
	
	MULTIPLE_RECS_NOT_ALLOWED,
	
	INVALID_REC_ID,
	
	REC_ID_REQUIRED,
	
	REC_ID_SPECIFIED_FOR_CREATE,
	
	SYS_FORM_DEL_NOT_ALLOWED,
	
	SYS_REC_DEL_NOT_ALLOWED,

	MULTIPLE_CTXS_NOT_ALLOWED;

	@Override
	public String code() {
		return "FORMS_" + this.name();
	}

}
