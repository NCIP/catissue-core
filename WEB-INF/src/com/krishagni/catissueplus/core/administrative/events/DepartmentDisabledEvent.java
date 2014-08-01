package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DepartmentDisabledEvent extends ResponseEvent{
	
	private static final String SUCCESS = "success";
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static DepartmentDisabledEvent ok() {
		DepartmentDisabledEvent event = new DepartmentDisabledEvent();
		event.setStatus(EventStatus.OK);
		event.setMessage(SUCCESS);
		return event;
	}

	public static DepartmentDisabledEvent invalidRequest(String message, ErroneousField... erroneousField) {
		DepartmentDisabledEvent resp = new DepartmentDisabledEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp; 
	}

	public static DepartmentDisabledEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DepartmentDisabledEvent resp = new DepartmentDisabledEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static DepartmentDisabledEvent notFound(Long deptId) {
		DepartmentDisabledEvent resp = new DepartmentDisabledEvent();
		resp.setId(deptId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}

