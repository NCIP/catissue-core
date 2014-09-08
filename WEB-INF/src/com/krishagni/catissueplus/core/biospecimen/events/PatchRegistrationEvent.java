
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationPatchDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchRegistrationEvent extends RequestEvent {

	private Long id;

	private CollectionProtocolRegistrationPatchDetail collectionProtocolRegistrationDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CollectionProtocolRegistrationPatchDetail getCollectionProtocolRegistrationDetail() {
		return collectionProtocolRegistrationDetail;
	}

	public void setCollectionProtocolRegistrationDetail(
			CollectionProtocolRegistrationPatchDetail collectionProtocolRegistrationDetail) {
		this.collectionProtocolRegistrationDetail = collectionProtocolRegistrationDetail;
	}

}
