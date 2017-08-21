package com.krishagni.catissueplus.core.common.util;

public class CsvException extends RuntimeException {
	private static final long serialVersionUID = 6984302629828751710L;

	String[] erroneousLine;

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

	public CsvException(String errorMessage, String[] erroneousLine) {
		this(errorMessage, erroneousLine, null);
	}

	public CsvException(String errorMessage, String[] erroneousLine, Exception e) {
		super(errorMessage, e);
		this.erroneousLine = erroneousLine;
	}

	public String[] getErroneousLine() {
		return erroneousLine;
	}

}
