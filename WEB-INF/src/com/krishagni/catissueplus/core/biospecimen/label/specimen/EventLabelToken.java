package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class EventLabelToken extends AbstractSpecimenLabelToken {

	public EventLabelToken() {
		this.name = "EVENT_LABEL";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return specimen.getVisit().getCpEvent().getEventLabel();
	}
}
