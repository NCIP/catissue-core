
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForSpecimenContainerPositionOne implements LabelToken<Specimen> {

	private static final String EMPTY_POSITION = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getSpecimenPosition().getPositionDimensionOneString() != null) {
			return specimen.getSpecimenPosition().getPositionDimensionOneString();
		}
		return EMPTY_POSITION;
	}

}
