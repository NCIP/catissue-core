package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class OperationSavedEvent extends ResponseEvent {
	private OperationDetails operation;
	
	public OperationDetails getOperation() {
		return operation;
	}

	public void setOperation(OperationDetails operationSummary) {
		this.operation = operationSummary;
	}

	public static OperationSavedEvent ok(OperationDetails operation) {
		OperationSavedEvent resp = new OperationSavedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOperation(operation);
		return resp;
	}
	
	public static OperationSavedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static OperationSavedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static OperationSavedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		OperationSavedEvent resp = new OperationSavedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
