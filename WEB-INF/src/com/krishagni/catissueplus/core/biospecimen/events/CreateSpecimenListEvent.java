package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateSpecimenListEvent extends RequestEvent {
	private SpecimenListDetails listDetails;

	public SpecimenListDetails getListDetails() {
		return listDetails;
	}

	public void setListDetails(SpecimenListDetails listDetails) {
		this.listDetails = listDetails;
	}
}
