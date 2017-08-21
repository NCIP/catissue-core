package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum PvErrorCode implements ErrorCode {
	ATTR_NAME_REQUIRED,
	
	VALUE_REQUIRED,
	
	PARENT_ATTR_NOT_FOUND,
	
	NOT_FOUND;

	@Override
	public String code() {		
		return "PV_" + this.name();
	}	
}
