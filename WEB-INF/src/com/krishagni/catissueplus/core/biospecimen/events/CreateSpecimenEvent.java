
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateSpecimenEvent extends RequestEvent {

	private Long scgId;

	private SpecimenDetail specimenDetail;

	public Long getScgId() {
		return scgId;
	}

	public void setScgId(Long scgId) {
		this.scgId = scgId;
	}

	public SpecimenDetail getSpecimenDetail() {
		return specimenDetail;
	}

	public void setSpecimenDetail(SpecimenDetail specimenDetail) {
		this.specimenDetail = specimenDetail;
	}

}
