
package com.krishagni.catissueplus.errors;

import org.apache.commons.lang.StringUtils;

public class CaTissueException extends RuntimeException {

	private static final long serialVersionUID = 7175402055782776269L;

	private ErrorCodeEnum errorCode;

	private String[] fields;

	public CaTissueException(ErrorCodeEnum errorCode) {
		this.errorCode = errorCode;
	}

	public CaTissueException(ErrorCodeEnum errorCode, String... fields) {
		this.errorCode = errorCode;
		this.fields = fields;
	}

	public int getErrorCode() {
		return errorCode.code();
	}

	public String getMessage() {
		return errorCode.message();
	}

	public String getErroneousFields() {
		String result = "";
		if (fields != null) {
			result = StringUtils.join(fields, ",");
		}

		return result;
	}

	public static CaTissueException raiseError(ErrorCodeEnum errorCode, String... fields) {
		throw new CaTissueException(errorCode, fields);
	}

}
