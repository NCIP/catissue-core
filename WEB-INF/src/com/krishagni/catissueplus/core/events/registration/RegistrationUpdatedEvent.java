
package com.krishagni.catissueplus.core.events.registration;

import com.krishagni.catissueplus.core.events.ResponseEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;

public class RegistrationUpdatedEvent extends ResponseEvent {

	private ParticipantDetails participantDetails;

	public ParticipantDetails getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantDetails participantDetails) {
		this.participantDetails = participantDetails;
	}

}
