package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum DistributionOrderErrorCode implements ErrorCode {
	NOT_FOUND,
	
	DUP_NAME,
	
	NAME_REQUIRED, 
	
	INVALID_STATUS;

	@Override
	public String code() {
		return "DIST_ORDER_" + this.name();
	}

}
