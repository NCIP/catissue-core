
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantCreatedEvent extends ResponseEvent {

	private ParticipantDetail participantDetail;

	public ParticipantDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

	public static ParticipantCreatedEvent ok(ParticipantDetail detail) {
		ParticipantCreatedEvent event = new ParticipantCreatedEvent();
		event.setParticipantDetail(detail);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static ParticipantCreatedEvent notAuthorized(CreateParticipantEvent event1) {
		ParticipantCreatedEvent event = new ParticipantCreatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static ParticipantCreatedEvent invalidRequest(String message, ErroneousField... fields) {
		ParticipantCreatedEvent resp = new ParticipantCreatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static ParticipantCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ParticipantCreatedEvent resp = new ParticipantCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
