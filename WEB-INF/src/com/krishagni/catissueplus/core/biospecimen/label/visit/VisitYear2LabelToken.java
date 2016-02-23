package com.krishagni.catissueplus.core.biospecimen.label.visit;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class VisitYear2LabelToken extends VisitYearLabelToken {

	public VisitYear2LabelToken() {
		this.name = "YR_OF_VISIT2";
	}

	@Override
	public String getLabel(Visit visit) {
		return String.valueOf(getYearOfVisit(visit) % 100);
	}
}
