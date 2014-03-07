
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllRegistrationsSummaryEvent extends ResponseEvent {

	public List<ParticipantDetail> getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(List<ParticipantDetail> participantDetails) {
		this.participantDetails = participantDetails;
	}

	private List<ParticipantDetail> participantDetails;

}
