
package com.krishagni.catissueplus.core.events.participants;

import com.krishagni.catissueplus.core.events.EventStatus;
import com.krishagni.catissueplus.core.events.ResponseEvent;

public class ParticipantDetailsEvent extends ResponseEvent {

	private ParticipantDetails participantDto;

	public ParticipantDetails getParticipantDto() {
		return participantDto;
	}

	public void setParticipantDto(ParticipantDetails participantDto) {
		this.participantDto = participantDto;
	}
	
	public static ParticipantDetailsEvent ok(ParticipantDetails details)
	{
		ParticipantDetailsEvent event = new ParticipantDetailsEvent();
		event.setParticipantDto(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
