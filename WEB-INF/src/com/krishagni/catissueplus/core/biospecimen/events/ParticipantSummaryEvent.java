
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantSummaryEvent extends ResponseEvent {

	private ParticipantInfo participantInfo;

	public ParticipantInfo getParticipantInfo() {
		return participantInfo;
	}

	public void setParticipantInfo(ParticipantInfo participantInfo) {
		this.participantInfo = participantInfo;
	}

	public static ParticipantSummaryEvent ok(ParticipantInfo participant) {
		ParticipantSummaryEvent event = new ParticipantSummaryEvent();
		event.setParticipantInfo(participant);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static ParticipantSummaryEvent serverError(CatissueException e) {
		ParticipantSummaryEvent event = new ParticipantSummaryEvent();
		event.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		event.setException(e);
		return event;
	}

}
