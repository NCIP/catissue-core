package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CheckAccessEvent extends RequestEvent {
	private Long subjectId;
	
	private Long groupId;
	
	private Long dsoId;
	
	private String resourceName;
	
	private String operationName;
	
	private Long resourceInstanceId;

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Long getResourceInstanceId() {
		return resourceInstanceId;
	}

	public void setResourceInstanceId(Long resourceInstanceId) {
		this.resourceInstanceId = resourceInstanceId;
	}
}