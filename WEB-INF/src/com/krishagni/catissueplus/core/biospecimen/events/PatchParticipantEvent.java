
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchParticipantEvent extends RequestEvent {

	private ParticipantPatchDetail participantDetail;

	private Long id;

	public ParticipantPatchDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantPatchDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
