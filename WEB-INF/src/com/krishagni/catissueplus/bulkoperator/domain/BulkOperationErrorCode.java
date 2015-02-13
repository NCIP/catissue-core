package com.krishagni.catissueplus.bulkoperator.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum BulkOperationErrorCode implements ErrorCode {
	INVALID_OP_NAME,
	
	INVALID_CSV_TEMPLATE,
		
	INVALID_JOB_ID,
	
	REQUIRED_JOB_ID;
	
	public String code() {
		return "BO_" + this.name();
	}
}
