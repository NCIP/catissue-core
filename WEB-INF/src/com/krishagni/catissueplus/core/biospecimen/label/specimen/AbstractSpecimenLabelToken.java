package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public abstract class AbstractSpecimenLabelToken implements LabelTmplToken {
	protected String name = "";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReplacement(Object object) {
		if (!(object instanceof Specimen)) {
			throw OpenSpecimenException.userError(null); // TODO:
		}
		
		Specimen specimen = (Specimen)object;
		if (!specimen.isCollected()) {
			return "";
		}
				
		return getLabel(specimen);
	}
	
	public abstract String getLabel(Specimen specimen);
}
