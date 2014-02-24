
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSpecimenCollGroupSummaryEvent extends RequestEvent {

	private Long collectionProtocolRegistrationId;

	public Long getCollectionProtocolRegistrationId() {
		return collectionProtocolRegistrationId;
	}

	public void setCollectionProtocolRegistrationId(Long collectionProtocolRegistrationId) {
		this.collectionProtocolRegistrationId = collectionProtocolRegistrationId;
	}

}
