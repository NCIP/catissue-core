package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateCollectionProtocolEvent extends RequestEvent {
	private CollectionProtocolDetail cp;

	public CollectionProtocolDetail getCp() {
		return cp;
	}

	public void setCp(CollectionProtocolDetail cp) {
		this.cp = cp;
	}
}
