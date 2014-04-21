package com.krishagni.catissueplus.core.common.errors;


public enum ErrorCodeEnum implements CatissueErrorCode{
	QUERY_EXECUTION_ERROR(1000, "Error while executing query"),
	AUDIT_ERROR(1001, "Error while performing the audit");
		
	private int code;
	
	private String message;
	
	private ErrorCodeEnum(int code, String message) {
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
