
package com.krishagni.catissueplus.core.events.registration;

import com.krishagni.catissueplus.core.events.RequestEvent;
import com.krishagni.catissueplus.core.events.consents.ConsentDetails;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;

public class UpdateRegistrationEvent extends RequestEvent {

	private ParticipantDetails participantDetails;

	private ConsentDetails consents;

	public ParticipantDetails getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantDetails participantDetails) {
		this.participantDetails = participantDetails;
	}

	public ConsentDetails getConsents() {
		return consents;
	}

	public void setConsents(ConsentDetails consents) {
		this.consents = consents;
	}

}
