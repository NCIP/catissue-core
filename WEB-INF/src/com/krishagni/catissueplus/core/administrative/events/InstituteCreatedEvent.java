
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class InstituteCreatedEvent extends ResponseEvent {

	private InstituteDetails instituteDetails;

	public InstituteDetails getInstituteDetails() {
		return instituteDetails;
	}

	public void setInstituteDetails(InstituteDetails instituteDetails) {
		this.instituteDetails = instituteDetails;
	}

	public static InstituteCreatedEvent ok(InstituteDetails details) {
		InstituteCreatedEvent event = new InstituteCreatedEvent();
		event.setInstituteDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static InstituteCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		InstituteCreatedEvent resp = new InstituteCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static InstituteCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		InstituteCreatedEvent resp = new InstituteCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
