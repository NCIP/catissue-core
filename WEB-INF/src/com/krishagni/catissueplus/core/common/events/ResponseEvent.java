
package com.krishagni.catissueplus.core.common.events;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class ResponseEvent<T> {

	private T payload;
	
	private boolean forceTxCommitEnabled;

	private boolean rollback;
	
	private OpenSpecimenException error; 
	
	public ResponseEvent(T payload) {
		this.payload = payload;
	}
	
	public ResponseEvent(OpenSpecimenException error) {
		this.error = error;
	}
	
	public ResponseEvent(OpenSpecimenException error, boolean forceTxCommitEnabled) {
		this.error = error;
		this.forceTxCommitEnabled = forceTxCommitEnabled;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	public OpenSpecimenException getError() {
		return error;
	}

	public void setError(OpenSpecimenException error) {
		this.error = error;
	}
	
	public boolean isForceTxCommitEnabled() {
		return forceTxCommitEnabled;
	}

	public void setForceTxCommitEnabled(boolean forceTxCommitEnabled) {
		this.forceTxCommitEnabled = forceTxCommitEnabled;
	}

	public boolean isRollback() {
		return rollback;
	}

	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}

	public void throwErrorIfUnsuccessful() {
		if (error != null) {
			throw error;
		}
	}
	
	public boolean isSuccessful() {
		return error == null;
	}

	public boolean isSystemError() {
		return error != null && error.getErrorType() == ErrorType.SYSTEM_ERROR;
	}

	public boolean isUnknownError() {
		return error != null && error.getErrorType() == ErrorType.UNKNOWN_ERROR;
	}

	public static <P> ResponseEvent<P> response(P payload) {
		return new ResponseEvent<P>(payload);
	}
	
	public static <P> ResponseEvent<P> error(OpenSpecimenException error) {
		return new ResponseEvent<P>(error);
	}
	
	public static <P> ResponseEvent<P> error(OpenSpecimenException error, boolean forceTxCommitEnabled) {
		return new ResponseEvent<P>(error, forceTxCommitEnabled);
	}
	
	public static <P> ResponseEvent<P> userError(ErrorCode error) {
		return new ResponseEvent<P>(OpenSpecimenException.userError(error));
	}

	public static <P> ResponseEvent<P> serverError(ErrorCode error) {
		return new ResponseEvent<P>(OpenSpecimenException.serverError(error));
	}
	
	public static <P> ResponseEvent<P> userError(ErrorCode error, boolean forceTxCommitEnabled) {
		return new ResponseEvent<P>(OpenSpecimenException.userError(error), forceTxCommitEnabled);
	}

	public static <P> ResponseEvent<P> userError(ErrorCode error, Object ... params) {
		return new ResponseEvent<P>(OpenSpecimenException.userError(error, params));
	}
	
	public static <P> ResponseEvent<P> serverError(Exception e) {
		return new ResponseEvent<P>(OpenSpecimenException.serverError(e));
	}
}
