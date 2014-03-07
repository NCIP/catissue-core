
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateParticipantEvent extends RequestEvent {

	private ParticipantDetail participantDto;

	public ParticipantDetail getParticipantDto() {
		return participantDto;
	}

	public void setParticipantDto(ParticipantDetail participantDto) {
		this.participantDto = participantDto;
	}

}
