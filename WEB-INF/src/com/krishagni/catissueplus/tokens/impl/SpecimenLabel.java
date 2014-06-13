
package com.krishagni.catissueplus.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.tokens.LabelToken;

public class SpecimenLabel implements LabelToken<Specimen> {

	@Override
	public String getTokenValue(Specimen specimen) {
		return specimen.getLabel();
	}

}
