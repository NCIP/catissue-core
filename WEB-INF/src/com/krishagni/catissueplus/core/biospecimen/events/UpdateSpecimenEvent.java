
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateSpecimenEvent extends RequestEvent {

	private Long id;

	private SpecimenDetail specimenDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SpecimenDetail getSpecimenDetail() {
		return specimenDetail;
	}

	public void setSpecimenDetail(SpecimenDetail specimenDetail) {
		this.specimenDetail = specimenDetail;
	}
}
