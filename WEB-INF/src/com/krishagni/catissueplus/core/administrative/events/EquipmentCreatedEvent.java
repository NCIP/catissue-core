
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class EquipmentCreatedEvent extends ResponseEvent {

	private EquipmentDetails details;

	public EquipmentDetails getDetails() {
		return details;
	}

	public void setDetails(EquipmentDetails details) {
		this.details = details;
	}

	public static EquipmentCreatedEvent ok(EquipmentDetails details) {
		EquipmentCreatedEvent resp = new EquipmentCreatedEvent();
		resp.setDetails(details);
		resp.setStatus(EventStatus.OK);
		return resp;
	}

	public static EquipmentCreatedEvent invalidRequest(String message, ErroneousField[] erroneousFields) {
		EquipmentCreatedEvent resp = new EquipmentCreatedEvent();
		resp.setMessage(message);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setErroneousFields(erroneousFields);
		return resp;
	}

	public static EquipmentCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		EquipmentCreatedEvent resp = new EquipmentCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
