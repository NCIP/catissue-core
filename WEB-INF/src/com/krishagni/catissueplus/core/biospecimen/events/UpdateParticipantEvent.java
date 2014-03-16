
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateParticipantEvent extends RequestEvent {

	private Long participantId;

	private ParticipantDetail participantDetail;

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public ParticipantDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

}
