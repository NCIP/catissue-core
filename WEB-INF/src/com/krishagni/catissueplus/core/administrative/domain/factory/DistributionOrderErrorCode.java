package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum DistributionOrderErrorCode implements ErrorCode {
	NOT_FOUND,
	
	DUP_NAME,
	
	NAME_REQUIRED, 
	
	INVALID_STATUS, 
	
	INVALID_CREATION_DATE,
	
	INVALID_EXECUTION_DATE, 
	
	INVALID_QUANTITY,
	
	STATUS_CHANGE_NOT_ALLOWED, 
	
	ALREADY_DISTRIBUTED,
	
	DUPLICATE_SPECIMEN;

	@Override
	public String code() {
		return "DIST_ORDER_" + this.name();
	}

}
