
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AliquotCreatedEvent extends ResponseEvent {

	private Long specimenId;

	private List<SpecimenDetail> aliquots = new ArrayList<SpecimenDetail>();

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}

	public List<SpecimenDetail> getAliquots() {
		return aliquots;
	}

	public void setAliquots(List<SpecimenDetail> aliquots) {
		this.aliquots = aliquots;
	}

	public static AliquotCreatedEvent ok(List<SpecimenDetail> aliquots) {
		AliquotCreatedEvent event = new AliquotCreatedEvent();
		event.setAliquots(aliquots);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static AliquotCreatedEvent notAuthorized(CreateAliquotEvent event1) {
		AliquotCreatedEvent event = new AliquotCreatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static AliquotCreatedEvent invalidRequest(String message, ErroneousField... fields) {
		AliquotCreatedEvent resp = new AliquotCreatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static AliquotCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AliquotCreatedEvent resp = new AliquotCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static AliquotCreatedEvent notFound(Long specimenId) {
		AliquotCreatedEvent resp = new AliquotCreatedEvent();
		resp.setSpecimenId(specimenId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
