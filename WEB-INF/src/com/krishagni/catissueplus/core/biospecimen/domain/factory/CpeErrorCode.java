package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum CpeErrorCode implements ErrorCode {
	NOT_FOUND,
	
	DUP_LABEL,
	
	DUP_CODE,
	
	LABEL_REQUIRED,
	
	INVALID_POINT,
	
	INVALID_CLINICAL_DIAGNOSIS,
	
	INVALID_CLINICAL_STATUS,	
	
	REF_ENTITY_FOUND,
	
	CP_REQUIRED;

	@Override
	public String code() {
		return "CPE_" + this.name();
	}

}
