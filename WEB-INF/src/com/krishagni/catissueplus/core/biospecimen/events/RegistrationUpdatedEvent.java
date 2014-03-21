
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RegistrationUpdatedEvent extends ResponseEvent {

	private Long cprId;

	private CollectionProtocolRegistrationDetail cprDetail;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long registrationId) {
		this.cprId = registrationId;
	}

	public CollectionProtocolRegistrationDetail getCprDetail() {
		return cprDetail;
	}

	public void setCprDetail(CollectionProtocolRegistrationDetail cprDetail) {
		this.cprDetail = cprDetail;
	}

	public static RegistrationUpdatedEvent ok(CollectionProtocolRegistrationDetail details) {
		RegistrationUpdatedEvent event = new RegistrationUpdatedEvent();
		event.setCprDetail(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static RegistrationUpdatedEvent invalidRequest(String message, ErroneousField... fields) {
		RegistrationUpdatedEvent resp = new RegistrationUpdatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static RegistrationUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		RegistrationUpdatedEvent resp = new RegistrationUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static RegistrationUpdatedEvent notFound(Long registrationId) {
		RegistrationUpdatedEvent resp = new RegistrationUpdatedEvent();
		resp.setCprId(registrationId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
