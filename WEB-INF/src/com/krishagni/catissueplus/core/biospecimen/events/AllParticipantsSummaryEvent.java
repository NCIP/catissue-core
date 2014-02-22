
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllParticipantsSummaryEvent extends ResponseEvent {

	private List<ParticipantSummary> participantsSummary;

	public List<ParticipantSummary> getParticipantsSummary() {
		return participantsSummary;
	}

	public void setParticipantsSummary(List<ParticipantSummary> participantsSummary) {
		this.participantsSummary = participantsSummary;
	}

	public static AllParticipantsSummaryEvent ok(List<ParticipantSummary> participantsSummary) {
		AllParticipantsSummaryEvent event = new AllParticipantsSummaryEvent();
		event.setParticipantsSummary(participantsSummary);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
