
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class EquipmentUpdatedEvent extends ResponseEvent {

	private EquipmentDetails details;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EquipmentDetails getDetails() {
		return details;
	}

	public void setDetails(EquipmentDetails details) {
		this.details = details;
	}

	public static EquipmentUpdatedEvent ok(EquipmentDetails details) {
		EquipmentUpdatedEvent event = new EquipmentUpdatedEvent();
		event.setStatus(EventStatus.OK);
		event.setDetails(details);
		return event;
	}

	public static EquipmentUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		EquipmentUpdatedEvent resp = new EquipmentUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static EquipmentUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		EquipmentUpdatedEvent resp = new EquipmentUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static EquipmentUpdatedEvent notFound(Long id) {
		EquipmentUpdatedEvent resp = new EquipmentUpdatedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;

	}
}
