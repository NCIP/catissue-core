
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantDetailEvent extends ResponseEvent {

	private ParticipantDetail participantDetail;

	public ParticipantDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

	public static ParticipantDetailEvent ok(ParticipantDetail participantDetail) {
		ParticipantDetailEvent event = new ParticipantDetailEvent();
		event.setParticipantDetail(participantDetail);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
