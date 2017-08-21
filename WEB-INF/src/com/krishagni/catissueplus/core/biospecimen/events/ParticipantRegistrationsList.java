package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class ParticipantRegistrationsList {
	private ParticipantDetail participant;
		
	private List<CollectionProtocolRegistrationDetail> registrations = new ArrayList<CollectionProtocolRegistrationDetail>();

	public ParticipantDetail getParticipant() {
		return participant;
	}

	public void setParticipant(ParticipantDetail participant) {
		this.participant = participant;
	}

	public List<CollectionProtocolRegistrationDetail> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<CollectionProtocolRegistrationDetail> registrations) {
		this.registrations = registrations;
	}

	public void setForceDelete(boolean forceDelete) {
		if (participant != null) {
			participant.setForceDelete(forceDelete);
		}
		getRegistrations().forEach(cpr -> cpr.setForceDelete(forceDelete));
	}
}