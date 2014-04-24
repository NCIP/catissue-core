
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class RegisterDomainEvent extends RequestEvent {

	private DomainDetails domainDetails;

	public RegisterDomainEvent(DomainDetails domainDetails) {
		this.domainDetails = domainDetails;
	}

	public DomainDetails getDomainDetails() {
		return domainDetails;
	}

	public void setDomainDetails(DomainDetails domainDetails) {
		this.domainDetails = domainDetails;
	}

}
