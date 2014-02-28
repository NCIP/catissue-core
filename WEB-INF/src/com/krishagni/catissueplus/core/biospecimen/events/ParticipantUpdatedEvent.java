
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantUpdatedEvent extends ResponseEvent {

	private ParticipantDetails participantDto;

	public ParticipantDetails getParticipantDto() {
		return participantDto;
	}

	public void setParticipantDto(ParticipantDetails participantDto) {
		this.participantDto = participantDto;
	}
	
	public static ParticipantUpdatedEvent ok(ParticipantDetails details)
	{
		ParticipantUpdatedEvent event = new ParticipantUpdatedEvent();
		event.setParticipantDto(details);
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static ParticipantUpdatedEvent invalidRequest(String message, Long ... id) {
		ParticipantUpdatedEvent resp = new ParticipantUpdatedEvent();
//		resp.setId(id != null && id.length > 0 ? id[0] : null);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}
	
	public static ParticipantUpdatedEvent serverError(Throwable ... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ParticipantUpdatedEvent resp = new ParticipantUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;		
	}
	
	public static ParticipantUpdatedEvent notAuthorized(UpdateParticipantEvent event1) {
		ParticipantUpdatedEvent event = new ParticipantUpdatedEvent();
		event.setStatus(EventStatus.NOT_AUTHORIZED);
		return event;
	}
}
