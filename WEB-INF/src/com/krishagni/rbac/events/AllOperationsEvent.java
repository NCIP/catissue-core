package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AllOperationsEvent extends ResponseEvent {
	private List<OperationDetails> operations = new ArrayList<OperationDetails>();

	public List<OperationDetails> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationDetails> operations) {
		this.operations = operations;
	}
	
	public static AllOperationsEvent ok(List<OperationDetails> operations) {
		AllOperationsEvent resp = new AllOperationsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOperations(operations);
		return resp;
	}
	
	public static AllOperationsEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static AllOperationsEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		AllOperationsEvent resp = new AllOperationsEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
