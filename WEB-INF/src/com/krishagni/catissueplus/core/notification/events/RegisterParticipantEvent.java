
package com.krishagni.catissueplus.core.notification.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class RegisterParticipantEvent extends RequestEvent {

	private NotifiedRegistrationDetail registrationDetails;

	public NotifiedRegistrationDetail getRegistrationDetails() {
		return registrationDetails;
	}

	public void setRegistrationDetails(NotifiedRegistrationDetail registrationDetails) {
		this.registrationDetails = registrationDetails;
	}

}