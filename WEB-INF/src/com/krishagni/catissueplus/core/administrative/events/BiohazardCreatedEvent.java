
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BiohazardCreatedEvent extends ResponseEvent {

	private BiohazardDetails biohazardDetails;

	public BiohazardDetails getBiohazardDetails() {
		return biohazardDetails;
	}

	public void setBiohazardDetails(BiohazardDetails biohazardDetails) {
		this.biohazardDetails = biohazardDetails;
	}

	public static BiohazardCreatedEvent ok(BiohazardDetails details) {
		BiohazardCreatedEvent event = new BiohazardCreatedEvent();
		event.setBiohazardDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static BiohazardCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		BiohazardCreatedEvent resp = new BiohazardCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static BiohazardCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		BiohazardCreatedEvent resp = new BiohazardCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
