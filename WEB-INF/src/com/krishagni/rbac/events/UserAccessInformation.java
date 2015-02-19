package com.krishagni.rbac.events;

public class UserAccessInformation {
	private Long subjectId;
	
	private Long groupId;
	
	private Long dsoId;
	
	private String resourceName;
	
	private String operationName;
	
	private Long resourceInstanceId;
	
	private Boolean canUserAccess;

	public Long subjectId() {
		return subjectId;
	}

	public UserAccessInformation subjectId(Long subjectId) {
		this.subjectId = subjectId;
		return this;
	}

	public Long groupId() {
		return groupId;
	}

	public UserAccessInformation groupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	public Long dsoId() {
		return dsoId;
	}

	public UserAccessInformation dsoId(Long dsoId) {
		this.dsoId = dsoId;
		return this;
	}

	public String resourceName() {
		return resourceName;
	}

	public UserAccessInformation resourceName(String resourceName) {
		this.resourceName = resourceName;
		return this;
	}

	public String operationName() {
		return operationName;
	}

	public UserAccessInformation operationName(String operationName) {
		this.operationName = operationName;
		return this;
	}

	public Long resourceInstanceId() {
		return resourceInstanceId;
	}

	public UserAccessInformation resourceInstanceId(Long resourceInstanceId) {
		this.resourceInstanceId = resourceInstanceId;
		return this;
	}
	
	public Boolean canUserAccess() {
		return canUserAccess;
	}
	
	public UserAccessInformation canUserAccess(Boolean canUserAccess) {
		this.canUserAccess = canUserAccess;
		return this;
	}
}