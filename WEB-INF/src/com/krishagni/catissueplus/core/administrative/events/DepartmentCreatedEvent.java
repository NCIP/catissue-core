
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DepartmentCreatedEvent extends ResponseEvent {

	private DepartmentDetails deparmentDetails;

	public DepartmentDetails getDepartmentDetails() {
		return deparmentDetails;
	}

	public void setDepartmentDetails(DepartmentDetails deparmentDetails) {
		this.deparmentDetails = deparmentDetails;
	}

	public static DepartmentCreatedEvent ok(DepartmentDetails details) {
		DepartmentCreatedEvent event = new DepartmentCreatedEvent();
		event.setDepartmentDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static DepartmentCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		DepartmentCreatedEvent resp = new DepartmentCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static DepartmentCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DepartmentCreatedEvent resp = new DepartmentCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
