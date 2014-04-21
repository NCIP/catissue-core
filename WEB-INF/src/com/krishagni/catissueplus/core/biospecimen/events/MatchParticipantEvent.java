
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class MatchParticipantEvent extends RequestEvent {

	private ParticipantDetail participantDetail;

	public ParticipantDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

}
