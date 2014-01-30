
package com.krishagni.catissueplus.events.specimencollectiongroups;

import com.krishagni.catissueplus.events.RequestEvent;

public class ReqSpecimenCollGroupSummaryEvent extends RequestEvent {

	private Long collectionProtocolRegistrationId;

	public Long getCollectionProtocolRegistrationId() {
		return collectionProtocolRegistrationId;
	}

	public void setCollectionProtocolRegistrationId(Long collectionProtocolRegistrationId) {
		this.collectionProtocolRegistrationId = collectionProtocolRegistrationId;
	}

}
