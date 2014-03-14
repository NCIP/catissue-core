
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateRegistrationEvent extends RequestEvent {

	private CollectionProtocolRegistrationDetail cprDetail;

	public CollectionProtocolRegistrationDetail getCprDetail() {
		return cprDetail;
	}

	public void setCprDetail(CollectionProtocolRegistrationDetail cprDetail) {
		this.cprDetail = cprDetail;
	}

}
