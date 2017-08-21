package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.Calendar;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenYocLabelToken extends AbstractSpecimenLabelToken {
	
	public SpecimenYocLabelToken() {
		this.name = "YR_OF_COLL";
	}

	protected int getYearOfCollection(Specimen specimen) {
		while (specimen.isAliquot() || specimen.isDerivative()) {
			specimen = specimen.getParentSpecimen();
		}

		Calendar cal = Calendar.getInstance();
		SpecimenCollectionEvent collEvent = specimen.getCollectionEvent();
		if (collEvent != null) {
			cal.setTime(collEvent.getTime());
		} else if (specimen.getCreatedOn() != null) {
			cal.setTime(specimen.getCreatedOn());
		}

		return cal.get(Calendar.YEAR);
	}

	@Override
	public String getLabel(Specimen specimen) {
		return String.valueOf(getYearOfCollection(specimen));
	}
}
