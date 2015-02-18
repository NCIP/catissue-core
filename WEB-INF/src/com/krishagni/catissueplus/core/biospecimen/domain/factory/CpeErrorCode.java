package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum CpeErrorCode implements ErrorCode {
	NOT_FOUND,
	
	DUP_LABEL,
	
	LABEL_NOT_FOUND,
	
	LABEL_REQUIRED,
	
	INVALID_POINT;

	@Override
	public String code() {
		return "CPE_" + this.name();
	}

}
