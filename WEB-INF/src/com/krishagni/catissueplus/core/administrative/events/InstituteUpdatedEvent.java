package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class InstituteUpdatedEvent extends ResponseEvent {

	private InstituteDetails instituteDetails;

	private Long instituteId;

	private String name;

	public InstituteDetails getInstituteDetails() {
		return instituteDetails;
	}

	public void setInstituteDetails(InstituteDetails instituteDetails) {
		this.instituteDetails = instituteDetails;
	}

	public Long getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static InstituteUpdatedEvent ok(InstituteDetails details) {
		InstituteUpdatedEvent event = new InstituteUpdatedEvent();
		event.setInstituteDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static InstituteUpdatedEvent invalidRequest(String message,
			ErroneousField... erroneousField) {
		InstituteUpdatedEvent resp = new InstituteUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static InstituteUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		InstituteUpdatedEvent resp = new InstituteUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static InstituteUpdatedEvent notFound(Long instituteId) {
		InstituteUpdatedEvent resp = new InstituteUpdatedEvent();
		resp.setInstituteId(instituteId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static InstituteUpdatedEvent notFound(String name) {
		InstituteUpdatedEvent resp = new InstituteUpdatedEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
