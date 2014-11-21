package com.krishagni.catissueplus.bulkoperator.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ObjectImportedEvent extends ResponseEvent {
	public Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public static ObjectImportedEvent ok(Object object) {
		ObjectImportedEvent res = new ObjectImportedEvent();
		res.setStatus(EventStatus.OK);
		res.setObject(object);
		return res;
	}
	
	public static ObjectImportedEvent badRequest(String message, ErroneousField[] erroneousFields, Throwable e) {
		ObjectImportedEvent res = new ObjectImportedEvent();
		res.setErroneousFields(erroneousFields);
		res.setException(e);
		res.setMessage(message);
		res.setStatus(EventStatus.BAD_REQUEST);
		return res;
	}
	
	public static ObjectImportedEvent serverError(String message, Throwable e) {
		ObjectImportedEvent res = new ObjectImportedEvent();
		res.setException(e);
		res.setMessage(message);
		res.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		return res;
	}
	
	public static ObjectImportedEvent buildResponse(ResponseEvent res, Object object) {
		ObjectImportedEvent br = new ObjectImportedEvent();
		br.setErroneousFields(res.getErroneousFields());
		br.setStatus(res.getStatus());
		br.setException(res.getException());
		br.setMessage(res.getMessage());
		br.setObject(object);
		return br;
	}
}
