package com.krishagni.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class TestUtils {

	private static boolean setupCompleted = false;
	
	public static void setupCommonItems(ApplicationContext ctx) {
		if (setupCompleted == false) {
			uploadCommonDatabaseItems(ctx);
			setupCompleted = true;
		}
		
		System.out.println("Common items have been already uploaded, skipping the input!");
	}
	
	private static void uploadCommonDatabaseItems(ApplicationContext ctx) {
		try {
			
			Connection connection = DriverManager.getConnection("jdbc:h2:mem:datajpa", "sa", "");
			IDatabaseConnection con = new DatabaseConnection(connection);
			
			//File file = new File("./com/krishagni/core/tests/storage-container-test/create-sc-initial.xml");
			File file = new File("/home/ibrahim/code/test/os/WEB-INF/classes/com/krishagni/core/tests/storage-container-test/create-sc-initial.xml");
			
			if (file.exists() == false) {
				throw new RuntimeException("file not found!");
			}
			FlatXmlDataSet dataSet = new FlatXmlDataSet(new FileInputStream(file));
			DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);
			con.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
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
