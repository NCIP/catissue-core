
package com.krishagni.catissueplus.core.events.registration;

import java.util.List;

import com.krishagni.catissueplus.core.events.ResponseEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;

public class AllRegistrationsSummaryEvent extends ResponseEvent {

	public List<ParticipantDetails> getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(List<ParticipantDetails> participantDetails) {
		this.participantDetails = participantDetails;
	}

	private List<ParticipantDetails> participantDetails;

}
