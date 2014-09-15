package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class OperationDeletedEvent extends ResponseEvent {
	private String operationName;
	
	private OperationDetails operation;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public OperationDetails getOperation() {
		return operation;
	}

	public void setOperation(OperationDetails operation) {
		this.operation = operation;
	}

	public static OperationDeletedEvent ok(OperationDetails operation) {
		OperationDeletedEvent resp = new OperationDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOperation(operation);
		return resp;
	}
	
	public static OperationDeletedEvent notFound(String operationName) {
		OperationDeletedEvent resp = new OperationDeletedEvent();
		resp.setOperationName(operationName);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static OperationDeletedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static OperationDeletedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static OperationDeletedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		OperationDeletedEvent resp = new OperationDeletedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
