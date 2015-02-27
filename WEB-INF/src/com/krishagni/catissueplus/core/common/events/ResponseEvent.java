
package com.krishagni.catissueplus.core.common.events;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class ResponseEvent<T> {

	private T payload;
	
	private boolean commitTx;
	
	private OpenSpecimenException error; 
	
	public ResponseEvent(T payload) {
		this.payload = payload;
	}
	
	public ResponseEvent(OpenSpecimenException error) {
		this.error = error;
	}
	
	public ResponseEvent(OpenSpecimenException error, boolean commitTx) {
		this.error = error;
		this.commitTx = commitTx;
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
	
	public boolean isCommitTx() {
		return commitTx;
	}

	public void setCommitTx(boolean commitTx) {
		this.commitTx = commitTx;
	}

	public void throwErrorIfUnsuccessful() {
		if (error != null) {
			throw error;
		}
	}
	
	public boolean isSuccessful() {
		return error == null;
	}
	
	public static <P> ResponseEvent<P> response(P payload) {
		return new ResponseEvent<P>(payload);
	}
	
	public static <P> ResponseEvent<P> error(OpenSpecimenException error) {
		return new ResponseEvent<P>(error);
	}
	
	public static <P> ResponseEvent<P> error(OpenSpecimenException error, boolean commitTx) {
		return new ResponseEvent<P>(error, commitTx);
	}
	
	public static <P> ResponseEvent<P> userError(ErrorCode error) {
		return new ResponseEvent<P>(OpenSpecimenException.userError(error));
	}
	
	public static <P> ResponseEvent<P> serverError(Exception e) {
		return new ResponseEvent<P>(OpenSpecimenException.serverError(e));
	}
}
