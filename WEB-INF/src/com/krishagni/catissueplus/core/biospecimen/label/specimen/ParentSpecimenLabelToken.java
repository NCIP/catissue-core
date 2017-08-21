package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class ParentSpecimenLabelToken extends AbstractSpecimenLabelToken { 

	public ParentSpecimenLabelToken() {
		this.name = "PSPEC_LABEL"; 
	}
	
	@Override
	public String getLabel(Specimen specimen) {
		if (specimen.getParentSpecimen() == null) {
			return "";
		}
		
		return specimen.getParentSpecimen().getLabel();
	}
}
