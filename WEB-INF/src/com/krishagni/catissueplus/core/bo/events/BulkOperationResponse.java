package com.krishagni.catissueplus.core.bo.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BulkOperationResponse extends ResponseEvent {
	public Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public static BulkOperationResponse ok(Object object) {
		BulkOperationResponse res = new BulkOperationResponse();
		res.setStatus(EventStatus.OK);
		res.setObject(object);
		return res;
	}
	
	public static BulkOperationResponse badRequest(String message, ErroneousField[] erroneousFields, Throwable e) {
		BulkOperationResponse res = new BulkOperationResponse();
		res.setErroneousFields(erroneousFields);
		res.setException(e);
		res.setMessage(message);
		res.setStatus(EventStatus.BAD_REQUEST);
		return res;
	}
	
	public static BulkOperationResponse serverError(String message, Throwable e) {
		BulkOperationResponse res = new BulkOperationResponse();
		res.setException(e);
		res.setMessage(message);
		res.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		return res;
	}
	
	public static BulkOperationResponse buildResponse(ResponseEvent res, Object object) {
		BulkOperationResponse br = new BulkOperationResponse();
		br.setErroneousFields(res.getErroneousFields());
		br.setStatus(res.getStatus());
		br.setException(res.getException());
		br.setMessage(res.getMessage());
		br.setObject(object);
		return br;
	}
}
