
package com.krishagni.catissueplus.core.common.errors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	private HttpStatus status;

	private String message;

	private List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<ErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
