package com.krishagni.openspecimen.custom.demo.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CollectSpecimensEvent extends RequestEvent {
	private SpecimenCollectionDetail collectionDetail;

	public SpecimenCollectionDetail getCollectionDetail() {
		return collectionDetail;
	}

	public void setCollectionDetail(SpecimenCollectionDetail collectionDetail) {
		this.collectionDetail = collectionDetail;
	}
}
