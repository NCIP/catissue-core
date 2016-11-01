package com.krishagni.catissueplus.core.common.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class UnhandledException extends BaseEntity {
	private User user;
	
	private String className;
	
	private String methodName;
	
	private Date timestamp;
	
	private String exception;
	
	private String stackTrace;
	
	private String inputArgs;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	public String getInputArgs() {
		return inputArgs;
	}

	public void setInputArgs(String inputArgs) {
		this.inputArgs = inputArgs;
	}

}
