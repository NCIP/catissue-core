
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

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
