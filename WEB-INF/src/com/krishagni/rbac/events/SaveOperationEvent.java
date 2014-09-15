package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class SaveOperationEvent extends RequestEvent {
	private OperationDetails operation;

	public OperationDetails getOperation() {
		return operation;
	}

	public void setOperation(OperationDetails operation) {
		this.operation = operation;
	}
}
