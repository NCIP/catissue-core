package com.krishagni.catissueplus.bulkoperator.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum BulkOperationErrorCode implements CatissueErrorCode {
	INVALID_OPERATION_NAME(1500, "Invalid operation name."),
	INVALID_CSV_TEMPLATE(1501, "Provided csv template is invalid!"),
	INVALID_PAGINATION_FILTER(1502, "Invalid pagination filter"),
	INVALID_JOB_DETAILS(1503, "Invalid Job-id"),
	MISSING_JOB_ID(1504, "Required job-id is null")
	;
	
	private int code;

	private String message;

	private BulkOperationErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int code() {
		return code;
	}

	public String message() {
		return message;
	}
}
