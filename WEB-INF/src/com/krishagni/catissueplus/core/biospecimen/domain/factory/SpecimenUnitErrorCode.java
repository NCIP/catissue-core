package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SpecimenUnitErrorCode implements ErrorCode {
	NOT_FOUND,
	
	CLASS_REQUIRED,
	
	UNIT_REQUIRED,
	
	NOT_ALLOWED;

	@Override
	public String code() {
		return "SPEC_UNIT_" + this.name();
	}

}
