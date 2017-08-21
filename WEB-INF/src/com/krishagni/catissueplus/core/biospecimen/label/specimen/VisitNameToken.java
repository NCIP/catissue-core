package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class VisitNameToken extends AbstractSpecimenLabelToken {

	public VisitNameToken() {
		this.name = "VISIT_NAME";
	}
	
	@Override
	public String getLabel(Specimen specimen) {
		return specimen.getVisit().getName();
	}
}
