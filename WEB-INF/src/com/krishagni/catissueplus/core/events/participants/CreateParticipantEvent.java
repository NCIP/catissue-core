
package com.krishagni.catissueplus.core.events.participants;

import com.krishagni.catissueplus.core.events.RequestEvent;

public class CreateParticipantEvent extends RequestEvent {

	private ParticipantDetails participantDto;

	public ParticipantDetails getParticipantDto() {
		return participantDto;
	}

	public void setParticipantDto(ParticipantDetails participantDto) {
		this.participantDto = participantDto;
	}

}
