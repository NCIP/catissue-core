
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateParticipantEvent extends RequestEvent {

	private ParticipantDetail participantDetails;

	public ParticipantDetail getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantDetail participantDetails) {
		this.participantDetails = participantDetails;
	}

}
