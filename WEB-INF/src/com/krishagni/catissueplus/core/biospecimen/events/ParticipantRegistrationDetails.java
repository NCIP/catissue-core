package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class ParticipantRegistrationDetails extends ParticipantDetail {
	private List<CprRegistrationDetails> registrationDetails = new ArrayList<CprRegistrationDetails>();

	public List<CprRegistrationDetails> getRegistrationDetails() {
		return registrationDetails;
	}

	public void setRegistrationDetails(List<CprRegistrationDetails> registrationDetails) {
		this.registrationDetails = registrationDetails;
	}
}