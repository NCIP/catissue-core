
package com.krishagni.catissueplus.core.common.errors;

import java.util.HashSet;
import java.util.Set;

public class OpenSpecimenException extends RuntimeException {
	private static final long serialVersionUID = -1473557909717365251L;
	
	private ErrorType errorType = ErrorType.NONE;

	private Set<ErrorCode> errors = new HashSet<ErrorCode>();
	
	private Throwable exception;
	
	public OpenSpecimenException(ErrorType type, Set<ErrorCode> errors) {
		this.errorType = type;
		this.errors = errors;
	}
	
	public OpenSpecimenException(ErrorType type, ErrorCode error) {
		this.errorType = type;
		errors.add(error);
	}
	
	public OpenSpecimenException(ErrorType type) {
		this.errorType = type;
	}
	
	public OpenSpecimenException(Throwable exception) {
		this.errorType = ErrorType.SYSTEM_ERROR;
		this.exception = exception;
	}
	
	public ErrorType getErrorType() {
		return errorType;
	}
	
	public Set<ErrorCode> getErrors() {
		return errors;
	}
	
	public Throwable getException() {
		return exception;
	}
	
	public void addError(ErrorCode error) {
		this.errors.add(error);
	}
	
	public boolean hasAnyErrors() {
		return !this.errors.isEmpty() || exception != null;
	}
	
	public void checkAndThrow() {
		if (hasAnyErrors()) {
			throw this;
		}
	}
	
	public static OpenSpecimenException userError(ErrorCode error) {		
		return new OpenSpecimenException(ErrorType.USER_ERROR, error);
	}
	
	public static OpenSpecimenException serverError(ErrorCode error) {
		return new OpenSpecimenException(ErrorType.SYSTEM_ERROR, error);
	}
	
	public static OpenSpecimenException serverError(Throwable e) {
		return new OpenSpecimenException(e);
	}
}
