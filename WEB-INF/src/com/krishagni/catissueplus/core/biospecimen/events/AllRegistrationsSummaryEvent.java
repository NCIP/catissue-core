
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllRegistrationsSummaryEvent extends ResponseEvent {

	public List<ParticipantDetails> getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(List<ParticipantDetails> participantDetails) {
		this.participantDetails = participantDetails;
	}

	private List<ParticipantDetails> participantDetails;

}
