package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public abstract class AbstractSpecimenLabelToken extends AbstractLabelTmplToken implements LabelTmplToken {
	protected String name = "";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReplacement(Object object) {
		if (!(object instanceof Specimen)) {
			throw new RuntimeException("Invalid input object type");
		}
		
		return getLabel((Specimen)object);
	}
	
	public abstract String getLabel(Specimen specimen);
}
