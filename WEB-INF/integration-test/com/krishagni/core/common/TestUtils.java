package com.krishagni.core.common;

import org.junit.Assert;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class TestUtils {

	public static void recordResponse(ResponseEvent response) {
		System.out.println("Operation-successful?: " +  response.isSuccessful());
		
		if (response.getError() != null) {
			System.out.println("Errortype: " + response.getError().getErrorType());
			
			for (ErrorCode error: response.getError().getErrors()) {
				System.out.println("----------------------------ERROR----------------------------");
				System.out.println("Error-Code: " + error.code());
			}
		}
	}
	
	public static boolean isErrorCodePresent(ResponseEvent event, ErrorCode error, ErrorType errorType) {
		if (event.isSuccessful()) {
			return false;
		}
		
		if (!event.getError().getErrorType().equals(errorType)) {
			System.out.println("Error type mismatch: expected: " + errorType + " actual: " + event.getError().getErrorType());
			return false;
		}
		
		for (ErrorCode e : event.getError().getErrors()) {
			if (e.code().equals(error.code())) {
				return true;
			}
		}
		return false;
	}
	
	public static void checkErrorCode(ResponseEvent resp, ErrorCode code, ErrorType type) {
		if(isErrorCodePresent(resp, code, type) == false) {
			String msg = "Is successful: [" + resp.isSuccessful() + "] "; 
			
			if (resp.getError() != null) {
				msg += " Errortype: [" + resp.getError().getErrorType() + "] " ;
				
				if (resp.getError().getErrors() != null) {
					for (ErrorCode e : resp.getError().getErrors()) {
						msg += " Found Error: [" + e.code() + "] "; 
					}
				}
			}
			Assert.fail("The errorcode was not found in response: [" + code.code() + "] type: [" + type + "] Actual Response: " + msg);
		}
	}
}
