
package com.krishagni.catissueplus.core.events.registration;

import com.krishagni.catissueplus.core.events.RequestEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;

public class ReqRegistrationEvent extends RequestEvent {

	private Long cpId;

	private ParticipantDetails participantDetails;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public ParticipantDetails getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantDetails participantDetails) {
		this.participantDetails = participantDetails;
	}
}
