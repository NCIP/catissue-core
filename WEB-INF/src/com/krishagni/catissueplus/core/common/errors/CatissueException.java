
package com.krishagni.catissueplus.core.common.errors;

import org.apache.commons.lang.StringUtils;

public class CatissueException extends RuntimeException {

	/**
	 * Auto generated serial version Id  
	 */
	private static final long serialVersionUID = 4544763173779346609L;

	private CatissueErrorCode errorCode;

	private String[] fields;

	public CatissueException(CatissueErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public CatissueException(CatissueErrorCode errorCode, String... fields) {
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

	public static CatissueException reportError(CatissueErrorCode errorCode, String... fields) {
		throw new CatissueException(errorCode, fields);
	}
}
