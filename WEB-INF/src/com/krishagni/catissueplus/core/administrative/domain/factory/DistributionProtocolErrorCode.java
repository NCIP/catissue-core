
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum DistributionProtocolErrorCode implements CatissueErrorCode {

	MISSING_ATTR_VALUE(2200, "Required attribute is either empty or null"), 
	INVALID_ATTR_VALUE(2201,"Attribute value is invalid."), 
	ERRORS(2202, "Please resolve the errors listed in error list."), 
	DUPLICATE_PROTOCOL_TITLE(2203, "Distribution Protocol with the same title already exists."), 
	DUPLICATE_PROTOCOL_SHORT_TITLE(2204,"Distribution Protocol with the same short title already exists."), 
	INVALID_PRINCIPAL_INVESTIGATOR(2205,"Principal Investigator value is invalid"), 
	BAD_REQUEST(2206, "Bad Request");

	private int code;

	private String message;

	private DistributionProtocolErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public int code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}
}
