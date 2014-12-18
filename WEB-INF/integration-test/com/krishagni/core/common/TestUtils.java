package com.krishagni.core.common;

import org.junit.Assert;

import com.google.gson.Gson;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class TestUtils {

	public static void recordResponse(ResponseEvent response) {
		try {
			Gson gson = new Gson();
			System.out.println(gson.toJson(response));
		} catch (Exception e) {
			System.err.println("Failed to decompile the response:");
		}
		
	}
	
	public static boolean isErrorCodePresent(ResponseEvent event, CatissueErrorCode errorCode, String field) {
		Assert.assertNotNull("Error fields not expected to be null!", event.getErroneousFields());
		
		for (ErroneousField ef: event.getErroneousFields()) {
			if (ef.getErrorMessage().equals(errorCode.message())
					&& ef.getErrorCode() == errorCode.code())
				
				if (field != null) {
					if (ef.getFieldName().equals(field)) {
						return true;
					}
					
					continue;
				}
				return true;
		}
		return false;
	}
}
