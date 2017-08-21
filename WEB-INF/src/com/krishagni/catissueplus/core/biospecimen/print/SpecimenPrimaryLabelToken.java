package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenPrimaryLabelToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_primary_label";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;

		String label = specimen.getLabel();
		while (specimen.getParentSpecimen() != null) {
			specimen = specimen.getParentSpecimen();
			label = specimen.getLabel();
		}

		return label;
	}
}
