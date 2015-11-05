package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum DpRequirementErrorCode implements ErrorCode {
	NOT_FOUND,
	
	ALREADY_EXISTS,
	
	DP_REQUIRED,
	
	SPECIMEN_TYPE_REQUIRED,
	
	ANATOMIC_SITE_REQUIRED,
	
	PATHOLOGY_STATUS_REQUIRED,
	
	SPECIMEN_COUNT_REQUIRED,
	
	INVALID_SPECIMEN_COUNT,
	
	QUANTITY_REQUIRED,
	
	INVALID_QUANTITY;
	
	@Override
	public String code() {
		return "DPR_" + this.name();
	}
}
