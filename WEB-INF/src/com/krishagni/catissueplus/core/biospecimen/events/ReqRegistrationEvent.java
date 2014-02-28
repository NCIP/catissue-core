
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqRegistrationEvent extends RequestEvent {

	private CollectionProtocolRegistrationDetails registrationDetails;

	public CollectionProtocolRegistrationDetails getRegistrationDetails() {
		return registrationDetails;
	}

	public void setRegistrationDetails(CollectionProtocolRegistrationDetails registrationDetails) {
		this.registrationDetails = registrationDetails;
	}
}
