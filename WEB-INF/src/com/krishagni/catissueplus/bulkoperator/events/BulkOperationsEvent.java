package com.krishagni.catissueplus.bulkoperator.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

//
// TODO: Changed name
//
public class BulkOperationsEvent extends ResponseEvent {
	private List<BulkOperationDetail> operations = new ArrayList<BulkOperationDetail>();

	public List<BulkOperationDetail> getOperations() {
		return operations;
	}

	public void setOperations(List<BulkOperationDetail> operations) {
		this.operations = operations;
	}
	
	public static BulkOperationsEvent ok(List<BulkOperationDetail> ops) {
		BulkOperationsEvent resp = new BulkOperationsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOperations(ops);
		return resp;
	}
	
	public static BulkOperationsEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		BulkOperationsEvent resp = new BulkOperationsEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
	
	public static BulkOperationsEvent invalidRequest(CatissueErrorCode error, Throwable t) {
		BulkOperationsEvent resp = new BulkOperationsEvent();
		resp.setupResponseEvent(EventStatus.BAD_REQUEST, error, t);
		return resp;
	}
}
