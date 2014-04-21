
package com.krishagni.catissueplus.rest.controller;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchRegistrationEvent extends RequestEvent {

	private Long id;

	private CollectionProtocolRegistrationDetail collectionProtocolRegistrationDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CollectionProtocolRegistrationDetail getCollectionProtocolRegistrationDetail() {
		return collectionProtocolRegistrationDetail;
	}

	public void setCollectionProtocolRegistrationDetail(
			CollectionProtocolRegistrationDetail collectionProtocolRegistrationDetail) {
		this.collectionProtocolRegistrationDetail = collectionProtocolRegistrationDetail;
	}

}
