
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateSpecimenEvent extends RequestEvent {

	private Long visitId;

	private SpecimenDetail specimen;

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public SpecimenDetail getSpecimen() {
		return specimen;
	}

	public void setSpecimen(SpecimenDetail specimen) {
		this.specimen = specimen;
	}
}
