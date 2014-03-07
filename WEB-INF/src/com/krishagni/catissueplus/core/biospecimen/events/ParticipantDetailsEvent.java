
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantDetailsEvent extends ResponseEvent {

	private ParticipantDetails participantDetails;

	public ParticipantDetails getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantDetails participantDetails) {
		this.participantDetails = participantDetails;
	}

	public static ParticipantDetailsEvent ok(ParticipantDetails participantDetails) {
		ParticipantDetailsEvent event = new ParticipantDetailsEvent();
		event.setParticipantDetails(participantDetails);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
