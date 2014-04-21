
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenUpdatedEvent extends ResponseEvent {

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
	
	public static SpecimenUpdatedEvent ok(SpecimenDetail detail) {
		SpecimenUpdatedEvent event = new SpecimenUpdatedEvent();
		event.setStatus(EventStatus.OK);
		event.setSpecimenDetail(detail);
		return event;
	}

	public static SpecimenUpdatedEvent notAuthorized(CreateSpecimenEvent createSpecimenEvent) {
		SpecimenUpdatedEvent event = new SpecimenUpdatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static SpecimenUpdatedEvent invalidRequest(String message, ErroneousField... fields) {
		SpecimenUpdatedEvent resp = new SpecimenUpdatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static SpecimenUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SpecimenUpdatedEvent resp = new SpecimenUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
	
	public static SpecimenUpdatedEvent notFound(Long id) {
		SpecimenUpdatedEvent resp = new SpecimenUpdatedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
