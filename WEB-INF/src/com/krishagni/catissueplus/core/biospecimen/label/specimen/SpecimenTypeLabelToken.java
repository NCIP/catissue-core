package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.WordUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken {
	
	public SpecimenTypeLabelToken() {
		this.name = "SP_TYPE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return WordUtils.initials(specimen.getSpecimenType());
	}
}
