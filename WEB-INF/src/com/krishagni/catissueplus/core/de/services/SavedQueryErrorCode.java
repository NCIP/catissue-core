package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum SavedQueryErrorCode implements CatissueErrorCode {
	QUERY_NOT_FOUND(1300, "Mentioned query-id not found!"),
	
	QUERY_ID_FOUND(1301, "Query-id should not be passed in create query event"),
	
	INVALID_PAGINATION_FILTER(1302, "Invalid pagination filter"),
	
	QUERY_ID_NOT_FOUND(1303,"Query-id should be passed in update query event");
	
	private int code;

	private String message;

	private SavedQueryErrorCode(int code, String message) {
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
