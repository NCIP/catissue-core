
package com.krishagni.catissueplus.core.events.participants;

import com.krishagni.catissueplus.core.events.ResponseEvent;

public class ParticipantCreatedEvent extends ResponseEvent {

	private ParticipantDetails participantDto;

	public ParticipantDetails getParticipantDto() {
		return participantDto;
	}

	public void setParticipantDto(ParticipantDetails participantDto) {
		this.participantDto = participantDto;
	}

}
