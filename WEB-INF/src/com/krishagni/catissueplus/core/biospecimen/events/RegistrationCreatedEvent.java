
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RegistrationCreatedEvent extends ResponseEvent {

	private CollectionProtocolRegistrationDetail cprDetail;

	public CollectionProtocolRegistrationDetail getCprDetail() {
		return cprDetail;
	}

	public void setCprDetail(CollectionProtocolRegistrationDetail cprDetail) {
		this.cprDetail = cprDetail;
	}

	public static RegistrationCreatedEvent ok(CollectionProtocolRegistrationDetail cprdetail) {
		RegistrationCreatedEvent event = new RegistrationCreatedEvent();
		event.setCprDetail(cprdetail);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static RegistrationCreatedEvent invalidRequest(String message, ErroneousField... fields) {
		RegistrationCreatedEvent resp = new RegistrationCreatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static RegistrationCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		RegistrationCreatedEvent resp = new RegistrationCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static RegistrationCreatedEvent accessDenied(String registration, Long cpId) {
		RegistrationCreatedEvent resp = new RegistrationCreatedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setMessage("User doens't have "+registration+" privileges.");
		return resp;
	}
	
}
