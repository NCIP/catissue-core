
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DepartmentUpdatedEvent extends ResponseEvent {

	private DepartmentDetails departmentDetails;

	private Long departmentId;

	public DepartmentDetails getDepartmentDetails() {
		return departmentDetails;
	}

	public void setDepartmentDetails(DepartmentDetails departmentDetails) {
		this.departmentDetails = departmentDetails;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public static DepartmentUpdatedEvent ok(DepartmentDetails details) {
		DepartmentUpdatedEvent event = new DepartmentUpdatedEvent();
		event.setDepartmentDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static DepartmentUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		DepartmentUpdatedEvent resp = new DepartmentUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static DepartmentUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DepartmentUpdatedEvent resp = new DepartmentUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static DepartmentUpdatedEvent notFound(Long departmentId) {
		DepartmentUpdatedEvent resp = new DepartmentUpdatedEvent();
		resp.setDepartmentId(departmentId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
