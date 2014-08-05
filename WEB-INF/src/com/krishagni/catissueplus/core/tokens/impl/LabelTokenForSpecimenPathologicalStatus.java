
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForSpecimenPathologicalStatus implements LabelToken<Specimen> {

	private static final String EMPTY_STATUS = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getPathologicalStatus() != null) {
			return specimen.getPathologicalStatus();
		}
		return EMPTY_STATUS;
	}

}
