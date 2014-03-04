
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RegistrationUpdatedEvent extends ResponseEvent {

	private Long registrationId;

	private CollectionProtocolRegistrationDetails registrationDetails;

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public CollectionProtocolRegistrationDetails getRegistrationDetails() {
		return registrationDetails;
	}

	public void setRegistrationDetails(CollectionProtocolRegistrationDetails registrationDetails) {
		this.registrationDetails = registrationDetails;
	}

	public static RegistrationUpdatedEvent ok(CollectionProtocolRegistrationDetails details) {
		RegistrationUpdatedEvent event = new RegistrationUpdatedEvent();
		event.setRegistrationDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static RegistrationUpdatedEvent invalidRequest(String message, Long... id) {
		RegistrationUpdatedEvent resp = new RegistrationUpdatedEvent();
		//		resp.setDealId(id != null && id.length > 0 ? id[0] : null);
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
		resp.setRegistrationId(registrationId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
