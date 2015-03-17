package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class EventDateLabelToken extends AbstractSpecimenLabelToken {	

	public EventDateLabelToken() {
		this.name = "EVENT_DATE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		Date visitDate = specimen.getVisit().getVisitDate();
		return new SimpleDateFormat("yyyyMMdd").format(visitDate);
	}
}