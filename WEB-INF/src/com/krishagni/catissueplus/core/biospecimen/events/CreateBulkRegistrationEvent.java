package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateBulkRegistrationEvent extends RequestEvent {
	private ParticipantRegistrationDetails participantDetails;

	public ParticipantRegistrationDetails getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantRegistrationDetails participantDetails) {
		this.participantDetails = participantDetails;
	}
	
}
