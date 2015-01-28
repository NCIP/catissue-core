package com.krishagni.core.common;

import org.junit.Assert;

import com.google.gson.Gson;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class TestUtils {

	public static void recordResponse(ResponseEvent response) {
		System.out.println("Operation-Status: " +  response.getStatus());
		System.out.println("Operation-Message: " + response.getMessage());
		if (response.getErroneousFields() != null) {
			for (ErroneousField error: response.getErroneousFields()) {
				System.out.println("----------------------------ERROR----------------------------");
				System.out.println("Error-Code: " + error.getErrorCode());
				System.out.println("Error-Message: " + error.getErrorMessage());
				System.out.println("Errored-Field: " + error.getFieldName());
			}
		}
	}
	
	public static boolean isErrorCodePresent(ResponseEvent event, CatissueErrorCode errorCode, String field) {
		Assert.assertNotNull("Error fields not expected to be null!", event.getErroneousFields());
		
		for (ErroneousField ef: event.getErroneousFields()) {
			if (ef.getErrorMessage().equals(errorCode.message())
					&& ef.getErrorCode() == errorCode.code()) {
				
				if (field != null) {
					if (ef.getFieldName().equals(field)) {
						return true;
					} 
				} else {
					return true;	
				}
			}
		}
		return false;
	}
}
