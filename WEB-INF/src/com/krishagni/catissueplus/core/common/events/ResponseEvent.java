
package com.krishagni.catissueplus.core.common.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class ResponseEvent {

	private EventStatus status;

	private String message;

	private Throwable exception;

	private ErroneousField[] erroneousFields;

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getException() {
		return exception;
	}

	public ErroneousField[] getErroneousFields() {
		return erroneousFields;
	}

	public void setErroneousFields(ErroneousField[] erroneousFields) {
		this.erroneousFields = erroneousFields;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	public boolean isSuccess(){
		return status == EventStatus.OK; 
	}
	
	public void raiseException(){
		throw new OpenSpecimenException(this);
	}

}
