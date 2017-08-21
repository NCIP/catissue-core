package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenYoc2LabelToken extends SpecimenYocLabelToken {

	public SpecimenYoc2LabelToken() {
		this.name = "YR_OF_COLL2";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return String.valueOf(getYearOfCollection(specimen) % 100);
	}
}