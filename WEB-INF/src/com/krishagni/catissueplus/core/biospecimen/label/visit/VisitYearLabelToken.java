package com.krishagni.catissueplus.core.biospecimen.label.visit;

import java.util.Calendar;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class VisitYearLabelToken extends AbstractVisitLabelToken {

	public VisitYearLabelToken() {
		this.name = "YR_OF_VISIT";
	}

	protected int getYearOfVisit(Visit visit) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(visit.getVisitDate());
		return cal.get(Calendar.YEAR);
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		return String.valueOf(getYearOfVisit(visit));
	}
}
