
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForSpecimenLabel implements LabelToken<Specimen> {

	private static final String EMPTY_LABEL = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getLabel() != null) {
			return specimen.getLabel();
		}
		return EMPTY_LABEL;
	}

}