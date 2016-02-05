package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum DistributionOrderErrorCode implements ErrorCode {
	NOT_FOUND,
	
	DP_REQUIRED,
	
	DUP_NAME,
	
	NAME_REQUIRED, 
	
	INVALID_STATUS, 
	
	INVALID_CREATION_DATE,
	
	INVALID_EXECUTION_DATE, 
	
	INVALID_QUANTITY,
	
	RPT_TMPL_NOT_CONFIGURED,
	
	STATUS_CHANGE_NOT_ALLOWED, 
	
	ALREADY_EXECUTED,
	
	DUPLICATE_SPECIMENS,
	
	INVALID_SPECIMEN_STATUS,
	
	NO_SPECIMENS_TO_DIST,
	
	INVALID_SPECIMENS_FOR_DP,
	
	SPECIMEN_DOES_NOT_EXIST,
	
	INVALID_REQUESTER_RECV_SITE_INST,

	NOT_DISTRIBUTED,

	INVALID_RETURN_QUANTITY,

	SPEC_ALREADY_RETURNED,

	INVALID_RETURN_DATE;
	
	@Override
	public String code() {
		return "DIST_ORDER_" + this.name();
	}

}
