package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class PpidLabelToken extends AbstractSpecimenLabelToken {
	
	public PpidLabelToken() {
		this.name = "PPI";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return specimen.getVisit().getRegistration().getPpid();
	}	
}
