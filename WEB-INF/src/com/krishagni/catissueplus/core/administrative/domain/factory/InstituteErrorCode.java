package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum InstituteErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	DUP_NAME,
	
	DEPT_NOT_FOUND,
	
	REF_ENTITY_FOUND,
	
	DEPT_REF_ENTITY_FOUND;
	
	@Override
	public String code() {
		return "INSTITUTE_" + this.name();
	}
}
