package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.WordUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken {

	@Override
	public String getLabel(Specimen specimen) {
		return WordUtils.initials(specimen.getSpecimenType());
	}

}
