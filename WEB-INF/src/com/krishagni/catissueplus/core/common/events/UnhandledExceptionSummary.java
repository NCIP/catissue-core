package com.krishagni.catissueplus.core.common.events;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.common.domain.UnhandledException;

public class UnhandledExceptionSummary {
	private Long id;
	
	private UserSummary user;
	
	private String className;
	
	private String methodName;
	
	private Date timestamp;
	
	private String exception;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserSummary getUser() {
		return user;
	}

	public void setUser(UserSummary user) {
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

	public static UnhandledExceptionSummary from(UnhandledException exception) {
		UnhandledExceptionSummary detail = new UnhandledExceptionSummary();
		detail.setId(exception.getId());
		detail.setUser(UserSummary.from(exception.getUser()));
		detail.setClassName(exception.getClassName());
		detail.setMethodName(exception.getMethodName());
		detail.setException(exception.getException());
		detail.setTimestamp(exception.getTimestamp());
		return detail;
	}
	
	public static List<UnhandledExceptionSummary> from(Collection<UnhandledException> exceptions) {
		return exceptions.stream().map(UnhandledExceptionSummary::from).collect(Collectors.toList());
	}

}
