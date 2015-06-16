package com.krishagni.catissueplus.core.common.errors;

public class ParameterizedError {
	private ErrorCode error;
	
	private Object[] params;

	public ParameterizedError(ErrorCode error, Object param) {
		this(error, new Object[] { param });
	}
	
	public ParameterizedError(ErrorCode error, Object[] params) {
		this.error = error;
		this.params = params;
	}
	
	public ErrorCode error() {
		return error;
	}
	
	public Object[] params() {
		return params;
	}
}
