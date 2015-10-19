package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum DistributionProtocolRequirementErrorCode implements ErrorCode {
	NOT_FOUND,
	
	DP_REQUIRED,
	
	SPECIMEN_TYPE_REQUIRED,
	
	ANATOMIC_SITE_REQUIRED,
	
	PATHOLOGY_STATUS_REQUIRED,
	
	SPECIMEN_REQUESTED_REQUIRED,
	
	PRICE_REQUIRED,
	
	SPECIMEN_ALREADY_EXISTS;
	
	@Override
	public String code() {
		return "DPR_" + this.name();
	}
}
