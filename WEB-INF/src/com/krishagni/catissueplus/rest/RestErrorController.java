package com.krishagni.catissueplus.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.errors.ParameterizedError;

@ControllerAdvice
public class RestErrorController extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	private static final String INTERNAL_ERROR = "internal_error";

	public RestErrorController() {
		super();
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleOtherException(Exception exception, WebRequest request) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<ErrorMessage> errorMsgs = new ArrayList<ErrorMessage>();

		if (exception instanceof OpenSpecimenException) {
			OpenSpecimenException ose = (OpenSpecimenException) exception;
			status = getHttpStatus(ose.getErrorType());

			if (ose.getException() != null) {
				logger.error("Error handling request", ose.getException());

				if (CollectionUtils.isEmpty(ose.getErrors())) {
					errorMsgs.add(getMessage(INTERNAL_ERROR, null));
				}
			}

			for (ParameterizedError error : ose.getErrors()) {
				errorMsgs.add(getMessage(error.error(), error.params()));
			}
		} else {
			logger.error("Error handling request", exception);
			errorMsgs.add(getMessage(INTERNAL_ERROR, null));
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(exception, errorMsgs, headers, status, request);
	}

	private HttpStatus getHttpStatus(ErrorType type) {
		switch (type) {
			case SYSTEM_ERROR:
				return HttpStatus.INTERNAL_SERVER_ERROR;
				
			case USER_ERROR:
				return HttpStatus.BAD_REQUEST;
				
			case UNKNOWN_ERROR:
				return HttpStatus.INTERNAL_SERVER_ERROR;
				
			case NONE:
				return HttpStatus.OK;
				
			default:
				throw new RuntimeException("Unknown error type: " + type);
		}
	}

	private ErrorMessage getMessage(ErrorCode error, Object[] params) {
		return getMessage(error.code(), params);
	}

	private ErrorMessage getMessage(String code, Object[] params) {
		String message = messageSource.getMessage(
				code.toLowerCase(), 
				params,
				Locale.getDefault());
		return new ErrorMessage(code, message);
	}
}
