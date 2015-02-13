package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class ParticipantRegistrationsList extends ParticipantDetail {
	private List<CollectionProtocolRegistrationDetail> registrations = new ArrayList<CollectionProtocolRegistrationDetail>();

	public List<CollectionProtocolRegistrationDetail> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<CollectionProtocolRegistrationDetail> registrations) {
		this.registrations = registrations;
	}
}