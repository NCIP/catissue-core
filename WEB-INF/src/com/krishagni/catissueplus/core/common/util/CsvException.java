package com.krishagni.catissueplus.core.common.util;

public class CsvException extends RuntimeException {
	private static final long serialVersionUID = 6984302629828751710L;

	public CsvException() {
		super();
	}

	public CsvException(String errorMessage) {
		super(errorMessage);
	}

	public CsvException(Exception e) {
		super(e);
	}

	public CsvException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}
}
