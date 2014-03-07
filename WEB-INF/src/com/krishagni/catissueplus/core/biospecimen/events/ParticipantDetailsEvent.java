
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantDetailsEvent extends ResponseEvent {

	private ParticipantDetail participantDetails;

	public ParticipantDetail getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantDetail participantDetails) {
		this.participantDetails = participantDetails;
	}

	public static ParticipantDetailsEvent ok(ParticipantDetail participantDetails) {
		ParticipantDetailsEvent event = new ParticipantDetailsEvent();
		event.setParticipantDetails(participantDetails);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
