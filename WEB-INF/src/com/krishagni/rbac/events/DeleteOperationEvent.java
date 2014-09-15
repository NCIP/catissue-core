package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeleteOperationEvent extends RequestEvent {
	private String operationName;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
}
