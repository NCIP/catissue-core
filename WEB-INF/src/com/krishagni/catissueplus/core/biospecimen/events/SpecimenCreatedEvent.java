
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenCreatedEvent extends ResponseEvent {

	private Long id;

	private SpecimenDetail specimenDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SpecimenDetail getSpecimenDetail() {
		return specimenDetail;
	}

	public void setSpecimenDetail(SpecimenDetail specimenDetail) {
		this.specimenDetail = specimenDetail;
	}

	public static SpecimenCreatedEvent ok(SpecimenDetail detail) {
		SpecimenCreatedEvent event = new SpecimenCreatedEvent();
		event.setStatus(EventStatus.OK);
		event.setSpecimenDetail(detail);
		return event;
	}

	public static SpecimenCreatedEvent notAuthorized(CreateSpecimenEvent createSpecimenEvent) {
		SpecimenCreatedEvent event = new SpecimenCreatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static SpecimenCreatedEvent invalidRequest(String message, ErroneousField... fields) {
		SpecimenCreatedEvent resp = new SpecimenCreatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static SpecimenCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SpecimenCreatedEvent resp = new SpecimenCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
