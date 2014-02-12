
package com.krishagni.catissueplus.core.events.registration;

import java.util.List;

import com.krishagni.catissueplus.core.events.ResponseEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantSummary;

public class AllParticipantsSummaryEvent extends ResponseEvent {
	
	private List<ParticipantSummary> participantsSummary;

	public List<ParticipantSummary> getParticipantsSummary() {
		return participantsSummary;
	}

	public void setParticipantsSummary(List<ParticipantSummary> participantsSummary) {
		this.participantsSummary = participantsSummary;
	}

}
