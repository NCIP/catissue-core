package com.krishagni.catissueplus.core.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.ErrorMessage;
import com.krishagni.catissueplus.core.common.errors.ErrorResponse;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@ControllerAdvice
public class RestErrorController extends ResponseEntityExceptionHandler {

	public RestErrorController() {
    super();
}
	
	@ExceptionHandler(value = {OpenSpecimenException.class,ObjectCreationException.class})
  public ResponseEntity<Object> handleOtherException(RuntimeException ex, WebRequest request) {

		OpenSpecimenException excep = (OpenSpecimenException)ex;  
		System.out.println("handleOtherException - Catching: " + excep.getClass().getSimpleName());
		
		HttpStatus status = getStatus(excep.getResponse().getStatus());
		ErrorResponse bodyOfResponse = populateResponseBody(excep,status);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	
  return handleExceptionInternal(excep, bodyOfResponse, headers, status, request);
  }

	private ErrorResponse populateResponseBody(OpenSpecimenException ex, HttpStatus status) {
		
		String errorURL = "";//"http://localhost:8180/errorcode:";
		ErrorResponse response = new ErrorResponse();
		response.setStatus(status);
		if(status != HttpStatus.INTERNAL_SERVER_ERROR){
			ResponseEvent resp = ex.getResponse();
			ErroneousField[] list = resp.getErroneousFields();
			for (ErroneousField erroneousField : list) {
				ErrorMessage message = new ErrorMessage();
				message.setAttributeName(erroneousField.getFieldName());
				message.setErrorCode(erroneousField.getErrorCode());
				message.setMessage(erroneousField.getErrorMessage());
				message.setErrorUrl(errorURL+erroneousField.getErrorCode());
				response.getErrorMessages().add(message);
			}
			response.setMessage(ex.getMessage());
		}
		return response;
	}

	private HttpStatus getStatus(EventStatus status) {
		switch(status){
			case BAD_REQUEST:
				return HttpStatus.BAD_REQUEST;
			case NOT_FOUND:
				return HttpStatus.NOT_FOUND;
			case INTERNAL_SERVER_ERROR:
				return HttpStatus.INTERNAL_SERVER_ERROR;
			case NOT_AUTHENTICATED:
				return HttpStatus.UNAUTHORIZED;
			case NOT_AUTHORIZED:
				return HttpStatus.FORBIDDEN;
				
		}
		return null;
	}
}
