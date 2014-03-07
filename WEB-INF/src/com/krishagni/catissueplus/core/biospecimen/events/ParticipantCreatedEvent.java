
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantCreatedEvent extends ResponseEvent {

	private ParticipantDetail participantDto;

	public ParticipantDetail getParticipantDto() {
		return participantDto;
	}

	public void setParticipantDto(ParticipantDetail participantDto) {
		this.participantDto = participantDto;
	}

	public static ParticipantCreatedEvent ok(ParticipantDetail details) {
		ParticipantCreatedEvent event = new ParticipantCreatedEvent();
		event.setParticipantDto(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static ParticipantCreatedEvent notAuthorized(CreateParticipantEvent event1) {
		ParticipantCreatedEvent event = new ParticipantCreatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}

	public static ParticipantCreatedEvent invalidRequest(String message, Long... id) {
		ParticipantCreatedEvent resp = new ParticipantCreatedEvent();
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
