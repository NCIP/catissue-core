package com.krishagni.catissueplus.core.biospecimen.label.visit;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class EventLabelToken extends AbstractVisitLabelToken {

	public EventLabelToken() {
		this.name = "EVENT_LABEL";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		return visit.getCpEvent().getEventLabel();
	}
}
